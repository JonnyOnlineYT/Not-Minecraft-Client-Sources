package Celestial.event.events.callables;

import Celestial.event.events.Cancellable;
import Celestial.event.events.Event;

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
