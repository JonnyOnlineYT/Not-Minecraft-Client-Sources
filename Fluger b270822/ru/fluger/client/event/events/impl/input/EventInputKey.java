/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.event.events.impl.input;

import ru.fluger.client.event.events.Event;

public class EventInputKey
implements Event {
    private int key;

    public EventInputKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}

