package org.moonware.client.event.events.impl.packet;

import org.moonware.client.event.events.callables.EventCancellable;
import org.moonware.client.event.events.Event;

public class EventMessage extends EventCancellable implements Event {

    public String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
