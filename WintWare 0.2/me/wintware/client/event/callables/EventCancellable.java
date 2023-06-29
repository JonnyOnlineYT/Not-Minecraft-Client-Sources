package me.wintware.client.event.callables;

import me.wintware.client.event.Cancellable;
import me.wintware.client.event.Event;

public abstract class EventCancellable implements Event, Cancellable {
   private boolean cancelled;

   protected EventCancellable() {
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean state) {
      this.cancelled = state;
   }
}
