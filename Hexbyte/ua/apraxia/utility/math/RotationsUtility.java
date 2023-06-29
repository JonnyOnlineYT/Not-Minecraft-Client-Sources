//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ua.apraxia.utility.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ua.apraxia.utility.Utility;

public final class RotationsUtility implements Utility {
    public RotationsUtility() {
    }

    public static float[] getRotations(Entity entity) {
        return getRotations(entity.posX, entity.posY, entity.posZ);
    }



    public static float[] getRotations(BlockPos blockPos) {
        return getRotations((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ());
    }

    public static float nextRotation(float currentRotation, float nextRotation, float rotationSpeed) {
        float f = MathHelper.wrapDegrees(nextRotation - currentRotation);
        if (f > rotationSpeed) {
            f = rotationSpeed;
        }

        if (f < -rotationSpeed) {
            f = -rotationSpeed;
        }

        return currentRotation + f;
    }

    public static float[] getRotations(double x, double y, double z) {
        double deltaX = x - mc.player.posX;
        double deltaY = y - (mc.player.posY - (double)mc.player.getEyeHeight()) - 1.77;
        double deltaZ = z - mc.player.posZ;
        double delta = (double) MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        float yaw = (float)(Math.atan2(deltaZ, deltaX) * 180.0 / Math.PI) - 90.0F;
        float pitch = (float)(-(Math.atan2(deltaY, delta) * 180.0 / Math.PI));
        yaw = mc.player.prevRotationYaw + getFixedRotation(MathUtility.wrapDegrees(yaw - mc.player.rotationYaw));
        pitch = mc.player.prevRotationPitch + getFixedRotation(MathUtility.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = (float)MathUtility.clamp((int)pitch, -180, 180);
        return new float[]{yaw, pitch};
    }

    public static float getFixedRotation(float rot) {
        return getDeltaMouse(rot) * getGCDValue();
    }

    private static float getGCDValue() {
        return getGCD() * 0.15F;
    }

    private static float getGCD() {
        float f1 = (float)((double)mc.gameSettings.mouseSensitivity * 0.6 + 0.2);
        return f1 * f1 * f1 * 8.0F;
    }

    private static float getDeltaMouse(float delta) {
        return (float)Math.round(delta / getGCDValue());
    }
}
