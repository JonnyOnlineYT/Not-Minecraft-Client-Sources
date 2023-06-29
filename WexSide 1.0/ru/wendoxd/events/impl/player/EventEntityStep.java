package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;

public class EventEntityStep extends Event {

    private double stepHeight;
    private Type type;

    public EventEntityStep(Type type, double stepHeight) {
        this.type = type;
        this.stepHeight = stepHeight;
    }

    public Type getType() {
        return type;
    }

    public double getStepHeight() {
        return this.stepHeight;
    }

    public void setStepHeight(double stepHeight) {
        this.stepHeight = stepHeight;
    }

    public enum Type {
        PRE, POST
    }

}
