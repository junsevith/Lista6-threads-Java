package com.example.lista6;

import javafx.scene.layout.Pane;

public class ServiceGroup extends Pane {
   public LifeService[][] group;
   private final int hCount;
   private final int vCount;

   ServiceGroup(LifeService[][] group) {
      this.group = group;
      vCount = group.length;
      hCount = group[0].length;
   }

   public LifeService get(int v, int h) {
      return group[(vCount + v) % vCount][(hCount + h) % hCount];
   }
}
