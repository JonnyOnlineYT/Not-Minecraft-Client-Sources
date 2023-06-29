package com.eventapi.events.callables;

import com.eventapi.events.Cancellable;
import com.eventapi.events.Event;

public abstract class EventCancellable implements Event, Cancellable {

    private boolean cancelled;

    protected EventCancellable() {
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    @Override
    public void setCancelled(boolean state) {
        cancelled = state;
    }

}
