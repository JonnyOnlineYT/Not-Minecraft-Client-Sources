package ua.apraxia.eventapi.events.impl.packet;


import ua.apraxia.eventapi.events.Event;
import ua.apraxia.eventapi.events.callables.EventCancellable;

public class EventMessage extends EventCancellable implements Event {

    public String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
