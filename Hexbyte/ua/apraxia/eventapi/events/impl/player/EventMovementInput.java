package ua.apraxia.eventapi.events.impl.player;

import ua.apraxia.eventapi.events.Event;

public class EventMovementInput implements Event {

    public double moveForward, moveStrafe;;

    public EventMovementInput(double moveForward, double moveStrafe) {
        this.moveForward = moveForward;
        this.moveStrafe = moveStrafe;
    }

    public double getMoveForward() {
        return moveForward;
    }

    public void setMoveForward(double moveForward) {
        this.moveForward = moveForward;
    }

    public double getMoveStrafe() {
        return moveStrafe;
    }

    public void setMoveStrafe(double moveStrafe) {
        this.moveStrafe = moveStrafe;
    }
}
