package com.example.lista6;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasa obsługująca zmiany kolorów {@link PaneTile}
 */
public class LifeService extends ScheduledService<Color> {
   /**
    * Obiekt {@link PaneTile} przypisany do {@link LifeService}
    */
   public final PaneTile paneTile;
   /**
    * Pionowy numer pola
    */
   private final int vIndex;
   /**
    * Poziomy numer pola
    */
   private final int hIndex;
   /**
    * Obiekt {@link Torus}, do którego należy wątek
    */
   private final Torus torus;
   /**
    * To samo co this, używane w task
    */
   private final LifeService This = this;
   /**
    * Czy service jest aktywny
    * @see "nieaktywny service nie uruchamia task, a zamiast swojego koloru zwraca null"
    */
   private boolean active = true;
   /**
    * Bazowy czas pomiędzy wywołaniami {@link #createTask()}
    */
   private final int interval;
   /**
    * Prawdopodobieństwo zmiany koloru na losowy
    */
   private final double probability;
   /**
    * Obiekt {@link ThreadLocalRandom#current()} (skrót)
    */
   private final ThreadLocalRandom random = ThreadLocalRandom.current();

   /**
    * Konstruktor obiektu LifeService
    * @param paneTile Obiekt {@link PaneTile} przypisany do service
    * @param vIndex Pionowy numer pola
    * @param hIndex Poziomy numer pola
    * @param torus Obiekt {@link Torus}, do którego należy wątek
    * @param interval Bazowy czas pomiędzy wywołaniami {@link #createTask()}
    * @param probability Prawdopodobieństwo zmiany koloru na losowy
    */
   public LifeService(PaneTile paneTile, int vIndex, int hIndex, Torus torus, int interval, double probability) {
      this.paneTile = paneTile;
      paneTile.service = this;

      this.vIndex = vIndex;
      this.hIndex = hIndex;
      this.torus = torus;

      this.interval = interval;
      this.probability = probability;

      setColor(randomColor());
      paneTile.updateColor();
   }

   /** Zmienia kolor obiektu {@link #paneTile} na podany w parametrze
    * @param color podany kolor
    */
   public synchronized void setColor(Color color) {
//      System.out.println("Begin set: " + this);
      paneTile.setColor(color);
//      System.out.println("End set  : " + this);
   }

   /**
    * Zwraca Kolor pola {@link #paneTile} lub {@code null} gdy service jest nieaktywny
    * @return {@link PaneTile#getColor()} pola {@link #paneTile} lub {@code null} gdy {@link #active} == false
    */
   public synchronized Color getColor() {
//      System.out.println("Read: " + this);
      if (active) {
         return paneTile.getColor();
      } else {
         return null;
      }
   }

   /**
    * Zwraca losowy kolor
    * @return Losowy kolor
    */
   public Color randomColor() {
      return new Color(random.nextDouble(0, 1), random.nextDouble(0, 1), random.nextDouble(0, 1), 1);
   }

   /**
    * Ustawia odstęp czasu pomiędzy wywołaniami service na losowy
    * @see #setPeriod(Duration)
    */
   public void setRandomPeriod() {
      int halfInterval = interval / 2;
      setPeriod(Duration.millis(random.nextInt(halfInterval, halfInterval + interval)));
   }

   /**
    * Pobiera kolory aktywnych sąsiadów z obiektu {@link #torus} i zwraca średnią z tych kolorów
    * @return średni kolor sąsiadów
    * @see Torus#get(int, int)
    * @see #getColor()
    */
   public Color assimilate() {
//      System.out.println("Adjust: " + this);
      Color[] colors = new Color[]{
            torus.get(vIndex, hIndex + 1).getColor(),
            torus.get(vIndex, hIndex - 1).getColor(),
            torus.get(vIndex - 1, hIndex).getColor(),
            torus.get(vIndex + 1, hIndex).getColor(),
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

   /**
    * Obiekt {@link Task} uruchamiany przez service
    * @return null (pozostałość po starszej wersji)
    */
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

   /**
    * Zatrzymuje i restartuje wątek
    * @see ScheduledService#cancel()
    * @see ScheduledService#restart()
    * @see PaneTile#indicate()
    */
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
      Platform.runLater(this.paneTile::indicate);
   }
}
