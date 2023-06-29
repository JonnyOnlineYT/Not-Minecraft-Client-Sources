package org.moonware.client.event.events.impl.packet;

import org.moonware.client.event.events.callables.EventCancellable;

public class EventReceiveMessage extends EventCancellable {

    public String message;
    public boolean cancelled;

    public EventReceiveMessage(String chat) {
        message = chat;
    }

    public String getMessage() {
        return message;
    }

    public void setCancelled(boolean b) {
        cancelled = b;
    }
}