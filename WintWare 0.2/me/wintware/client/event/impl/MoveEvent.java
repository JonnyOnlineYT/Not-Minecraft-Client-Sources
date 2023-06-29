package me.wintware.client.event.impl;

import me.wintware.client.event.Event;

public class MoveEvent implements Event {
   public double x;
   public double y;
   public double z;
   public boolean ground;

   public MoveEvent(double x, double y, double z, boolean ground) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.ground = ground;
   }

   public boolean isGround() {
      return this.ground;
   }

   public double getX() {
      return this.x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double y) {
      this.y = y;
   }

   public double getZ() {
      return this.z;
   }

   public void setZ(double z) {
      this.z = z;
   }

   public void setGround(boolean ground) {
      this.ground = ground;
   }
}
