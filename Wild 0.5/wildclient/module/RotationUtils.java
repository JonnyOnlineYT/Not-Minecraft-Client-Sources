//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import java.util.List;

public class RotationUtils
{
    public static float[] getAverageRotations(final List list) {
        double d = 0.0;
        double d2 = 0.0;
        double d3 = 0.0;
        for (final Object entityw : list) {
            final Entity entity = (Entity)entityw;
            d += entity.posX;
            d2 += entity.getEntityBoundingBox().maxY - 2.0;
            d3 += entity.posZ;
        }
        final float[] array = new float[2];
        final int n = 0;
        d /= list.size();
        d3 /= list.size();
        array[n] = getRotationFromPosition(d, d3, d2 /= list.size())[0];
        array[1] = getRotationFromPosition(d, d3, d2)[1];
        return array;
    }
    
    public static float getDistanceBetweenAngles(final float f, final float f2) {
        float f3 = Math.abs(f - f2) % 360.0f;
        if (f3 > 180.0f) {
            f3 = 360.0f - f3;
        }
        return f3;
    }
    
    public static float getTrajAngleSolutionLow(final float f, final float f2, final float f3) {
        final float f4 = f3 * f3 * f3 * f3 - 0.006f * (0.006f * (f * f) + 2.0f * f2 * (f3 * f3));
        return (float)Math.toDegrees(Math.atan((f3 * f3 - Math.sqrt(f4)) / (0.006f * f)));
    }
    
    public static float[] getRotationFromPosition(final double d, final double d2, final double d3) {
        final double d4 = d - Minecraft.getMinecraft().player.posX;
        final double d5 = d2 - Minecraft.getMinecraft().player.posZ;
        final double d6 = d3 - Minecraft.getMinecraft().player.posY - 0.6;
        final double d7 = MathHelper.sqrt(d4 * d4 + d5 * d5);
        final float f = (float)(Math.atan2(d5, d4) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(-(Math.atan2(d6, d7) * 180.0 / 3.141592653589793));
        return new float[] { f, f2 };
    }
    
    public static float[] getNeededRotations(final Entity entityLivingBase) {
        final double d = entityLivingBase.posX - Minecraft.getMinecraft().player.posX;
        final double d2 = entityLivingBase.posZ - Minecraft.getMinecraft().player.posZ;
        final double d3 = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (Minecraft.getMinecraft().player.getEntityBoundingBox().minY + (Minecraft.getMinecraft().player.getEntityBoundingBox().maxY - Minecraft.getMinecraft().player.getEntityBoundingBox().minY));
        final double d4 = MathHelper.sqrt(d * d + d2 * d2);
        final float f = (float)(MathHelper.atan2(d2, d) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(-(MathHelper.atan2(d3, d4) * 180.0 / 3.141592653589793));
        return new float[] { f, f2 };
    }
    
    public static float[] getRotations(final EntityLivingBase entityLivingBase, final String string) {
        if (string == "Head") {
            final double d = entityLivingBase.posX;
            final double d2 = entityLivingBase.posZ;
            final double d3 = entityLivingBase.posY + entityLivingBase.getEyeHeight() / 2.0f;
            return getRotationFromPosition(d, d2, d3);
        }
        if (string == "Chest") {
            final double d = entityLivingBase.posX;
            final double d4 = entityLivingBase.posZ;
            final double d5 = entityLivingBase.posY + entityLivingBase.getEyeHeight() / 2.0f - 0.75;
            return getRotationFromPosition(d, d4, d5);
        }
        if (string == "Dick") {
            final double d = entityLivingBase.posX;
            final double d6 = entityLivingBase.posZ;
            final double d7 = entityLivingBase.posY + entityLivingBase.getEyeHeight() / 2.0f - 1.2;
            return getRotationFromPosition(d, d6, d7);
        }
        if (string == "Legs") {
            final double d = entityLivingBase.posX;
            final double d8 = entityLivingBase.posZ;
            final double d9 = entityLivingBase.posY + entityLivingBase.getEyeHeight() / 2.0f - 1.5;
            return getRotationFromPosition(d, d8, d9);
        }
        final double d = entityLivingBase.posX;
        final double d10 = entityLivingBase.posZ;
        final double d11 = entityLivingBase.posY + entityLivingBase.getEyeHeight() / 2.0f - 0.5;
        return getRotationFromPosition(d, d10, d11);
    }
    
    public static float getNewAngle(float f) {
        if ((f %= 360.0f) >= 180.0f) {
            f -= 360.0f;
        }
        if (f < -180.0f) {
            f += 360.0f;
        }
        return f;
    }
}
