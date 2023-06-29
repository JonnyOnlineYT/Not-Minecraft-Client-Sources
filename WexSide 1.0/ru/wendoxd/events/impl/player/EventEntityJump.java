package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;

public class EventEntityJump extends Event {

    private float motionY;

    public EventEntityJump(float motionY) {
        this.motionY = motionY;
    }

    public float getMotionY() {
        return motionY;
    }

    public void setMotionY(float motionY) {
        this.motionY = motionY;
    }
}
