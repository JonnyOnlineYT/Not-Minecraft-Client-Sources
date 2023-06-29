package ru.rockstar.api.event.event;

import ru.rockstar.api.event.Event;

public class EventMessage extends Event {

    public String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}