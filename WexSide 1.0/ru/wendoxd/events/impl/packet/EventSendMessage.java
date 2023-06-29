package ru.wendoxd.events.impl.packet;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventSendMessage extends Event implements IEventCancelable {

    private boolean canceled;
    private String message;

    public EventSendMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setCanceled() {
        canceled = true;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }
}
