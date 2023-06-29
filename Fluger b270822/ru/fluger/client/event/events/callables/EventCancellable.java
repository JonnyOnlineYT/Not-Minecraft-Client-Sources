/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.event.events.callables;

import ru.fluger.client.event.events.Cancellable;
import ru.fluger.client.event.events.Event;

public abstract class EventCancellable
implements Event,
Cancellable {
    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancelled = state;
    }
}

