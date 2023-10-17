package com.example.lista6;

/**
 * Obsługuje 2 wymiarowy array jakby był torusem
 */
public class Torus {
   public LifeService[][] group;
   private final int hCount;
   private final int vCount;

   /**
    * Tworzy torus z arraya 2-wymiarowego
    */
   Torus(LifeService[][] group) {
      this.group = group;
      vCount = group.length;
      hCount = group[0].length;
   }

   /**
    * Zwraca element na podstawie jego pozycji w torusie
    * @param v pionowy numer elementu
    * @param h poziomy numer elementu
    * @return element o pozycji (v,h)
    * @see "-1 zwraca ostatni element itd."
    */
   public LifeService get(int v, int h) {
      return group[(vCount + v) % vCount][(hCount + h) % hCount];
   }
}
