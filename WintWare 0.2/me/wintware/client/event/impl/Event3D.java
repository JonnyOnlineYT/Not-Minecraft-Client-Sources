package me.wintware.client.event.impl;

import me.wintware.client.event.Event;

public class Event3D implements Event {
   private float partialTicks;

   public Event3D(float partialTicks) {
      this.partialTicks = partialTicks;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }
}
