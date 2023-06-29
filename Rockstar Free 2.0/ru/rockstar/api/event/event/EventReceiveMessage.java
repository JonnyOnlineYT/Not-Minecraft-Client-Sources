package ru.rockstar.api.event.event;

import ru.rockstar.api.event.Event;

public class EventReceiveMessage extends Event {

    public String message;
    public boolean cancelled;

    public EventReceiveMessage(String chat) {
        message = chat;
    }

    public String getMessage() {
        return message;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}