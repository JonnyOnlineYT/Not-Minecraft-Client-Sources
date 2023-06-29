package org.moonware.client.feature.impl.movement.Move;

import org.moonware.client.event.events.Event;
import org.moonware.client.event.events.callables.EventCancellable;

public class EventEntityStrafe extends EventCancellable implements Event {

    private boolean canceled;
    private float yaw, strafe, forward, friction;

    public EventEntityStrafe(float yaw, float strafe, float forward, float friction) {
        this.yaw = yaw;
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
    }

    public float getStrafe() {
        return strafe;
    }

    public float getForward() {
        return forward;
    }

    public float getFriction() {
        return friction;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setCanceled() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

}
