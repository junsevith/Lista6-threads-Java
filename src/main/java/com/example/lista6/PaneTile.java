package com.example.lista6;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PaneTile extends Pane {
   public LifeService service;
   private Color color;
   private final Circle indicator;

   public PaneTile(){

      indicator = new Circle(5,Color.RED);
      indicator.setStroke(Color.BLACK);
      indicator.centerXProperty().bind(this.widthProperty().divide(2));
      indicator.centerYProperty().bind(this.heightProperty().divide(2));
      indicator.radiusProperty().bind(this.heightProperty().divide(5));
      this.getChildren().add(indicator);
      indicator.setVisible(false);
   }
   public synchronized void indicate(){
      indicator.setVisible(!indicator.isVisible());
   }

   public synchronized void updateColor(){
      this.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
   }
   public synchronized Color getColor(){
      return color;
   }

   public synchronized void setColor(Color color){
      this.color = color;
   }
}
