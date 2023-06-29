package me.wintware.client.event.impl;

import me.wintware.client.event.Event;

public class Event2D implements Event {
   private float width;
   private float height;

   public Event2D(float width, float height) {
      this.width = width;
      this.height = height;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }
}
