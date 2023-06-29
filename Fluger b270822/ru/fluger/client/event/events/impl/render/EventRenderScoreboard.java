/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.event.events.impl.render;

import ru.fluger.client.event.events.Event;
import ru.fluger.client.event.types.EventType;

public class EventRenderScoreboard
implements Event {
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

