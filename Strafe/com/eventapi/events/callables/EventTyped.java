package com.eventapi.events.callables;

import com.eventapi.events.Event;
import com.eventapi.events.Typed;

public abstract class EventTyped implements Event, Typed {

    private final byte type;
    protected EventTyped(byte eventType) {
        type = eventType;
    }
    @Override
    public byte getType() {
        return type;
    }

}
