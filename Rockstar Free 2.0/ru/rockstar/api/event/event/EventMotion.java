package ru.rockstar.api.event.event;

import ru.rockstar.api.event.Event;

public class EventMotion extends Event {

    public static float YAW, PITCH, PREVYAW, PREVPITCH;
    public static boolean SNEAKING;
    private final boolean isPre;
    private float yaw;
    private float pitch;
    private double x, y, z;
    private boolean onground;
    private boolean alwaysSend;
    private boolean sneaking;

    public EventMotion(double x, double y, double z, float yaw, float pitch, boolean sneaking, boolean ground) {
        this.y = y;
        this.x = x;
        this.z = z;
        this.isPre = true;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onground = ground;
        this.sneaking = sneaking;
    }

    public EventMotion() {
        PREVYAW = YAW;
        PREVPITCH = PITCH;
        YAW = this.yaw;
        PITCH = this.pitch;
        SNEAKING = this.sneaking;
        this.isPre = false;
    }

    public boolean isPre() {
        return isPre;
    }

    public boolean isPost() {
        return !isPre;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public boolean isOnground() {
        return this.onground;
    }

    public boolean shouldAlwaysSend() {
        return this.alwaysSend;
    }

    public void setGround(boolean ground) {
        this.onground = ground;
    }

    public void setAlwaysSend(boolean alwaysSend) {
        this.alwaysSend = alwaysSend;
    }

    public void setOnGround(boolean onground) {
        this.onground = onground;
    }

    public boolean isRotating() {
        double yaw = (this.yaw - this.yaw);
        double pitch = (this.pitch - this.pitch);
        return (Math.abs(yaw) != 0.0D || Math.abs(pitch) != 0.0D);
    }

}
