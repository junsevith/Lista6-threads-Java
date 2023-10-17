package com.example.lista6;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Obiekt {@link Pane} który dodatkowo przechowuje {@link #color} i {@link #service} oraz obsługuje {@link #indicate()}
 */
public class PaneTile extends Pane {
   /**
    * Obiekt {@link LifeService} obsługujący ten Pane
    */
   public LifeService service;
   /**
    * Pole przechowujące kolor obiektu
    */
   private Color color;
   /**
    * Obiekt {@link Circle} pokazujący, że {@link #service} jest nieaktywny
    * @see "domyślnie niewidoczny"
    */
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

   /**
    * Przełącza widoczność obiektu {@link #indicator}
    */
   public synchronized void indicate(){
      indicator.setVisible(!indicator.isVisible());
   }

   /**
    * Ustawia kolor node'a na ten w polu {@link #color}
    */
   public synchronized void updateColor(){
      this.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
   }

   /**
    * Zwraca kolor obiektu
    * @return {@link #color}
    */
   public synchronized Color getColor(){
      return color;
   }

   /**
    * Ustawia pole {@link #color} na podany kolor
    * @param color podany kolor
    */
   public synchronized void setColor(Color color){
      this.color = color;
   }
}
