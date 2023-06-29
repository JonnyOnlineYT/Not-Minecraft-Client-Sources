package de.strafe.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * @author XButtonn
 * @created 21.02.2022 - 12:37
 */

@UtilityClass
public class RotationUtil implements IMinecraft {

    public Rotation rotation;
    public boolean spoofLook = false;

    public Rotation killauraRotations(Entity entity) {
        double deltaX = entity.posX - mc.thePlayer.posX;
        double deltaY = entity.posY - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        double deltaZ = entity.posZ - mc.thePlayer.posZ;
        double sqrt = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        float yaw = (float) (MathHelper.func_181159_b(deltaZ, deltaX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-(MathHelper.func_181159_b(deltaY, sqrt) * 180.0D / Math.PI));
        return new Rotation(yaw, pitch);
    }

    public float updateRotation(float current, float target, float speed) {
        float f = MathHelper.wrapAngleTo180_float(target - current);

        if (f > speed) f = speed;
        if (f < -speed) f = -speed;

        return current + f;
    }

    public Rotation getCurrentRotation() {
        float currYaw = rotation == null ? mc.thePlayer.rotationYaw : rotation.getYaw();
        float currPitch = rotation == null ? mc.thePlayer.rotationPitch : rotation.getPitch();
        return new Rotation(currYaw, currPitch);
    }

}
