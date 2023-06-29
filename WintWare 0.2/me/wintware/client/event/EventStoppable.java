package me.wintware.client.event;

public abstract class EventStoppable implements Event {
   private boolean stopped;

   protected EventStoppable() {
   }

   public void stop() {
      this.stopped = true;
   }

   public boolean isStopped() {
      return this.stopped;
   }
}
