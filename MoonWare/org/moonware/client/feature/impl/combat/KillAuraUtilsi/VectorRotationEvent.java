package org.moonware.client.feature.impl.combat.KillAuraUtilsi;

import org.moonware.client.event.events.callables.EventCancellable;

public class VectorRotationEvent extends EventCancellable {

    private float yaw, pitch;

    public VectorRotationEvent(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;

        if (!isCancelled())
            cancel();
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;

        if (!isCancelled())
            cancel();
    }

}
