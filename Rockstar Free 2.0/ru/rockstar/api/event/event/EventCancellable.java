package ru.rockstar.api.event.event;

import ru.rockstar.api.event.*;

public abstract class EventCancellable implements Cancellable
{
    private boolean cancelled;
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean state) {
        this.cancelled = state;
    }
}
