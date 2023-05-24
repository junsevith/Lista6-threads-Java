package com.example.lista6;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LifeController {
   private final int horCount = 10;
   private final int verCount = 10;
   private final int interval = 100;
   private final double probability = 0.5;
   private final int threadCount = verCount * horCount;
   private final ExecutorService exec = Executors.newFixedThreadPool(threadCount, r -> {
      Thread t = new Thread(r);
      t.setDaemon(true);
      return t;
   });

   @FXML
   private VBox vBox;
   //   private final PaneTile[][] panes = new PaneTile[verCount][horCount];
   private final Torus torus = new Torus(new LifeService[verCount][horCount]);


   public void initialize() {
      for (int i = 0; i < verCount; i++) {
         HBox hBox = new HBox();
         vBox.getChildren().add(hBox);
         VBox.setVgrow(hBox, Priority.ALWAYS);

         for (int j = 0; j < horCount; j++) {
            PaneTile pane = new PaneTile();
            hBox.getChildren().add(pane);
            pane.setCenterShape(true);
            HBox.setHgrow(pane, Priority.ALWAYS);
//            pane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
//            panes[i][j] = pane;
//            pane.setStyle("-fx-border-color: black;-fx-border-width: 2; -fx-border-insets: -2");
            LifeService service = new LifeService(pane, i, j, torus, interval, probability);
            torus.group[i][j] = service;
            service.setExecutor(exec);
            service.setOnSucceeded(workerStateEvent -> service.paneTile.updateColor());
            service.start();
         }
      }
      vBox.setOnMouseClicked(this::holdService);
   }

   private void holdService(MouseEvent event){
      Node target = (Node) event.getTarget();
      LifeService service = null;
      if (target instanceof PaneTile) {
         service = ((PaneTile) target).service;
      } else if (target.getParent() instanceof PaneTile) {
         service = ((PaneTile) target.getParent()).service;
      }
      if (service != null) {
         service.holdService();
         service.paneTile.updateColor();
      }
   }

   public void stageThings(Stage stage) {
      stage.setOnCloseRequest(event -> {
         for (LifeService[] row : torus.group) {
            for (LifeService service : row) {
               service.cancel();
            }
         }
//         for (int i = 0; i < verCount; i++) {
//            for (int j = 0; j < horCount; j++) {
//               serviceGroup.group[i][j].cancel();
//            }
//         }
      });
   }
}