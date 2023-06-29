package me.wintware.client.event.impl;

import me.wintware.client.event.Event;

public class EventStep implements Event {
   float stepHeight;

   public EventStep() {
   }

   public EventStep(float stepHeight) {
      this.stepHeight = stepHeight;
   }

   public float getStepHeight() {
      return this.stepHeight;
   }

   public void setStepHeight(float stepHeight) {
      this.stepHeight = stepHeight;
   }
}
