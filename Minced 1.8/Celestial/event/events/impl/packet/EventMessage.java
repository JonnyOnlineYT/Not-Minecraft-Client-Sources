package Celestial.event.events.impl.packet;

import Celestial.event.events.Event;
import Celestial.event.events.callables.EventCancellable;

public class EventMessage extends EventCancellable implements Event {

    public String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
