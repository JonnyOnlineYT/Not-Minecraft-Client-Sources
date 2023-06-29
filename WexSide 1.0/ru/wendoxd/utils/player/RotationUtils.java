package ru.wendoxd.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class RotationUtils {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static float[] getRotationVector(Vec3d vec) {
        Random random = new Random();
        float sensitivity = 1.025567f;
        double diffX = vec.xCoord - mc.player.posX;
        double diffY = vec.yCoord - (mc.player.posY + mc.player.getEyeHeight());
        double diffZ = vec.zCoord - mc.player.posZ;
        double diffXZ = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffZ, 2));
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90);
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        yaw = (yaw / sensitivity - 1 + random.nextInt(2)) * sensitivity;
        pitch = (pitch / sensitivity - 1 + random.nextInt(2)) * sensitivity;
        return new float[]{yaw, MathHelper.clamp(pitch, -90, 90)};
    }
}
