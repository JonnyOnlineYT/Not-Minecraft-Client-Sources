package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;

public class EventBlockDistance extends Event {

    private float distance;

    public EventBlockDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
