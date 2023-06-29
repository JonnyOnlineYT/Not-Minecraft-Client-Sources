/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.event.events.impl.player;

import ru.fluger.client.event.events.callables.EventCancellable;

public class EventJump
extends EventCancellable {
    private float yaw;

    public EventJump(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}

