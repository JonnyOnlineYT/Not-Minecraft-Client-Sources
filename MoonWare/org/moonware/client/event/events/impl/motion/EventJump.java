package org.moonware.client.event.events.impl.motion;

import org.moonware.client.event.events.callables.EventCancellable;
import org.moonware.client.event.events.Event;

public class EventJump extends EventCancellable implements Event {

    private float yaw;

    public EventJump(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}