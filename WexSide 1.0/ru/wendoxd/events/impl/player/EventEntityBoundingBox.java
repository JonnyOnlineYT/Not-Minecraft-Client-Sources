package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;

public class EventEntityBoundingBox extends Event {

    private float xz, y;

    public EventEntityBoundingBox(float xz, float y) {
        this.xz = xz;
        this.y = y;
    }

    public float getXZ() {
        return xz;
    }

    public void setXZ(float xz) {
        this.xz = xz;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
