package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;

public class EventEntityDistanceAttack extends Event {

    private float distance;

    public EventEntityDistanceAttack(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
