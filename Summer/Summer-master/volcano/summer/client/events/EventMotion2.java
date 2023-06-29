package volcano.summer.client.events;

import java.util.Random;

import volcano.summer.base.manager.event.Event;
import volcano.summer.client.util.Location;

public class EventMotion2 extends Event {

	private float yaw, pitch;
    private boolean ground;
    private double y;

    public EventMotion2(float yaw, float pitch, boolean ground, double y) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.y = y;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isGround() {
        return ground;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
