package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;

public class EventPlayerMotion extends Event {

    private double motionX, motionZ;
    private boolean sprint;

    public EventPlayerMotion(double motionX, double motionZ, boolean sprint) {
        this.motionX = motionX;
        this.motionZ = motionZ;
        this.sprint = sprint;
    }

    public boolean isSprint() {
        return sprint;
    }

    public void setSprint(boolean sprint) {
        this.sprint = sprint;
    }

    public double getMotionX() {
        return motionX;
    }

    public void setMotionXZ(double speed) {
        this.motionX = speed;
        this.motionZ = speed;
    }

    public double getMotionZ() {
        return motionZ;
    }
}
