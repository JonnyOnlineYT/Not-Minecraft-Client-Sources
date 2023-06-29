/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.event.events.impl.input;

import ru.fluger.client.event.events.callables.EventCancellable;

public class EventMouse
extends EventCancellable {
    private int key;

    public EventMouse(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}

