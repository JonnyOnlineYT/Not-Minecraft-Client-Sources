package org.moonware.client.event.events.impl.render;

import org.moonware.client.event.events.Event;
import org.moonware.client.event.types.EventType;

public class EventRenderScoreboard implements Event {

    private EventType state;

    public EventRenderScoreboard(EventType state) {
        this.state = state;
    }

    public EventType getState() {
        return state;
    }

    public void setState(EventType state) {
        this.state = state;
    }

    public boolean isPre() {
        return state == EventType.PRE;
    }
}
