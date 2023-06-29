package ua.apraxia.eventapi.events.callables;

import ua.apraxia.eventapi.events.Cancellable;
import ua.apraxia.eventapi.events.Event;

public abstract class EventCancellable implements Event, Cancellable {

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean state) {
        cancelled = state;
    }

}
