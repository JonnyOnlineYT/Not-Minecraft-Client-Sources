/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.event.events.impl.packet;

import ru.fluger.client.event.events.Event;
import ru.fluger.client.event.events.callables.EventCancellable;

public class EventMessage
extends EventCancellable
implements Event {
    public String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

