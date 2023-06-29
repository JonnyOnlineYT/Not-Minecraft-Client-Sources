package ru.rockstar.api.event.event;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import ru.rockstar.api.event.Event;
import ru.rockstar.api.utils.combat.LPositionHelper;


public class EventPreMotionUpdate extends Event {

    private boolean cancel;

    public float yaw, pitch;
    public double z;

    public double y;
    public double x;
    public boolean ground;

    private Rotation rotation;

    private LPositionHelper location;

    public EventPreMotionUpdate(float yaw, float pitch, double y, LPositionHelper location) {

        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.z = z;
        this.x = x;
        this.location = location;
    }

    public boolean isCancel() {

        return cancel;
    }

    public void setCancel(boolean cancel) {

        this.cancel = cancel;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public float getYaw() {

        return yaw;
    }

    public void setYaw(float yaw) {
        Minecraft.getMinecraft().player.renderYawOffset = yaw;
        Minecraft.getMinecraft().player.rotationYawHead = yaw;
        this.yaw = yaw;
    }

    public float getPitch() {

        return pitch;
    }

    public void setPitch(float pitch) {
        Minecraft.getMinecraft().player.rotationPitchHead = pitch;
        this.pitch = pitch;
    }
    public double getX() {

        return x;
    }
    public double getY() {

        return y;
    }
    public double getZ() {

        return z;
    }
    public void setY(double y) {

        this.y = y;
    }

    public EventPlayerMotionUpdate getLocation() {

        return null;
    }


    public boolean onGround() {
        return ground;
    }
    public void setGround(boolean ground) {
        this.ground = ground;
    }
}
