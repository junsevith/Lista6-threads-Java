package com.example.lista6;

/**
 * Tworzy torus z arraya 2-wymiarowego
 */
public class Torus{
   public LifeService[][] group;
   private final int hCount;
   private final int vCount;

   Torus(LifeService[][] group) {
      this.group = group;
      vCount = group.length;
      hCount = group[0].length;
   }

   public LifeService get(int v, int h) {
      return group[(vCount + v) % vCount][(hCount + h) % hCount];
   }
}
