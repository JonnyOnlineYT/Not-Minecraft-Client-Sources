package me.wintware.client.event.callables;

import me.wintware.client.event.Event;
import me.wintware.client.event.Typed;

public abstract class EventTyped implements Event, Typed {
   private final byte type;

   protected EventTyped(byte eventType) {
      this.type = eventType;
   }

   public byte getType() {
      return this.type;
   }
}
