package me.wintware.client.event.impl;

import me.wintware.client.event.Event;

public class EventKey implements Event {
   private int key;

   public EventKey(int key) {
      this.key = key;
   }

   public int getKey() {
      return this.key;
   }
}
