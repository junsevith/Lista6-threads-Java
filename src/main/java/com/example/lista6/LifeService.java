package com.example.lista6;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.concurrent.ThreadLocalRandom;

public class LifeService extends ScheduledService<Color> {
   public final PaneTile pane;
   private final int vIndex;
   private final int hIndex;
   private final ServiceGroup serviceGroup;
   private final LifeService This = this;
   private boolean active = true;

   private final int interval;
   private final double probability;

   private final ThreadLocalRandom random = ThreadLocalRandom.current();

   public LifeService(PaneTile pane, int vIndex, int hIndex, ServiceGroup serviceGroup, int interval, double probability) {
      this.pane = pane;
      pane.service = this;

      this.vIndex = vIndex;
      this.hIndex = hIndex;
      this.serviceGroup = serviceGroup;

      this.interval = interval;
      this.probability = probability;

      setColor(randomColor());
      pane.updateColor();
   }

   public synchronized void setColor(Color color) {
//      System.out.println("Begin set: " + this);
      pane.setColor(color);
//      System.out.println("End set  : " + this);
   }

   public synchronized Color getColor() {
//      System.out.println("Read: " + this);
      if (active) {
         return pane.getColor();
      } else {
         return null;
      }
   }

   public Color randomColor() {
      return new Color(random.nextDouble(0, 1), random.nextDouble(0, 1), random.nextDouble(0, 1), 1);
   }

   public void setRandomPeriod() {
      int halfInterval = interval / 2;
      setPeriod(Duration.millis(random.nextInt(halfInterval, halfInterval + interval)));
   }

   public Color assimilate() {
//      System.out.println("Adjust: " + this);
      Color[] colors = new Color[]{
            serviceGroup.get(vIndex, hIndex + 1).getColor(),
            serviceGroup.get(vIndex, hIndex - 1).getColor(),
            serviceGroup.get(vIndex - 1, hIndex).getColor(),
            serviceGroup.get(vIndex + 1, hIndex).getColor(),
      };

      int count = 0;
      double red = 0;
      double green = 0;
      double blue = 0;

      for (Color color : colors) {
         if (color != null) {
            red += color.getRed();
            green += color.getGreen();
            blue += color.getBlue();
            count++;
         }
      }
      count = count == 0 ? 1 : count;

      return new Color(red / count, green / count, blue / count, 1);

   }

   @Override
   protected Task<Color> createTask() {
      return new Task<>() {
         @Override
         protected Color call() throws Exception {
//            synchronized (This) {
            System.out.println("Start: " + This);

            setRandomPeriod();

            if (random.nextDouble(0, 1) < probability) {
               setColor(randomColor());
            } else {
               setColor(assimilate());
            }

//            Platform.runLater(() -> setColor(color));

            System.out.println("End  : " + This);

            return null;
//            }
         }
      };
   }

   public synchronized void holdService() {
      if (this.isRunning()) {
         active = false;
         this.cancel();
//         Platform.runLater(() -> pane.setStyle("-fx-border-color: blue;-fx-border-width: 2px;-fx-border-insets: -2"));
      } else {
         this.restart();
         active = true;
//         Platform.runLater(() -> pane.setStyle("-fx-border-color: black;-fx-border-width: 2; -fx-border-insets: -2"));
      }
      Platform.runLater(this.pane::indicate);
   }
}
