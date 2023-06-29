package org.moonware.client.feature.impl.combat.particle;

import net.minecraft.client.Minecraft;
import org.moonware.client.event.events.callables.EventCancellable;
import org.moonware.client.event.types.EventType;

public class EventMotion extends EventCancellable {

    private EventType type;
    private double posX, posY, posZ;
    private float yaw, pitch;
    private boolean onGround;

    public EventMotion(EventType type, double posX, double posY, double posZ, float yaw, float pitch,
                       boolean onGround) {
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public void setRotation(float yaw, float pitch, boolean clientRotation) {
        if (Float.isNaN(yaw) || Float.isNaN(pitch) || pitch > 90 || pitch < -90)
            return;

        this.yaw = yaw;
        this.pitch = pitch;

        if (clientRotation) {
            Minecraft.player.rotationYaw = (yaw);
            Minecraft.player.rotationPitch = (pitch);
        }
    }

    public EventType getType() {
        return type;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        float f = Minecraft.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float gcd = (f * f * f) * 1.2f;

        yaw -= yaw % gcd;

        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        float f = Minecraft.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float gcd = (f * f * f) * 1.2f;

        pitch -= pitch % gcd;

        this.pitch = pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
