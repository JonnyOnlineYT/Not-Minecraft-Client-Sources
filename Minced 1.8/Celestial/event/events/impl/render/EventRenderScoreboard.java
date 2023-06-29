package Celestial.event.events.impl.render;

import Celestial.event.events.Event;
import Celestial.event.types.EventType;

public class EventRenderScoreboard implements Event {

    private EventType state;

    public EventRenderScoreboard(EventType state) {
        this.state = state;
    }

    public EventType getState() {
        return this.state;
    }

    public void setState(EventType state) {
        this.state = state;
    }

    public boolean isPre() {
        return this.state == EventType.PRE;
    }
}
