package me.wintware.client.utils.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class RotationUtil {
   public static RotationUtil instance = new RotationUtil();
   int[] randoms = new int[]{0, 1};
   public static float sYaw;
   public static float sPitch;
   public static float aacB;
   private static Minecraft mc = Minecraft.getMinecraft();

   public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
      double diffX = entityLiving.posX - Minecraft.getMinecraft().player.posX;
      double diffZ = entityLiving.posZ - Minecraft.getMinecraft().player.posZ;
      float newYaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D);
      double difference = angleDifference((double)newYaw, (double)Minecraft.getMinecraft().player.rotationYaw);
      return difference <= (double)scope;
   }

   public static double angleDifference(double a, double b) {
      float yaw360 = (float)(Math.abs(a - b) % 360.0D);
      if (yaw360 > 180.0F) {
         yaw360 = 360.0F - yaw360;
      }

      return (double)yaw360;
   }

   public static float[] getRotations(Entity entity) {
      double d = entity.posX + (entity.posX - entity.lastTickPosX) - mc.player.posX;
      double d2 = entity.posY + (double)entity.getEyeHeight() - mc.player.posY + (double)mc.player.getEyeHeight() - 3.5D;
      double d3 = entity.posZ + (entity.posZ - entity.lastTickPosZ) - mc.player.posZ;
      double d4 = Math.sqrt(Math.pow(d, 2.0D) + Math.pow(d3, 2.0D));
      float f = (float)Math.toDegrees(-Math.atan(d / d3));
      float f2 = (float)(-Math.toDegrees(Math.atan(d2 / d4)));
      if (d < 0.0D && d3 < 0.0D) {
         f = (float)(90.0D + Math.toDegrees(Math.atan(d3 / d)));
      } else if (d > 0.0D && d3 < 0.0D) {
         double v = Math.toDegrees(Math.atan(d3 / d));
         f = (float)(-90.0D + v);
         float f5 = mc.gameSettings.mouseSensitivity * 0.8F;
         float gcd = f * f * f * 1.2F;
         double var10000 = d - d % (double)gcd;
         var10000 = d2 - d2 % (double)gcd;
      }

      return new float[]{f, f2};
   }

   public static float[] faceEntity(Entity entityIn, float currentYaw, float currentPitch, float yawSpeed, float pitchSpeed, boolean canHit) {
      double x = entityIn.posX - mc.player.posX;
      double y = entityIn.posY + (double)entityIn.getEyeHeight() - (mc.player.posY + (double)mc.player.getEyeHeight());
      double z = entityIn.posZ - mc.player.posZ;
      double randomYaw = 0.05D;
      double randomPitch = 0.05D;
      float range = mc.player.getDistanceToEntity(entityIn);
      float calculate = MathHelper.sqrt(x * x + z * z);
      float calcYaw = (float)(MathHelper.atan2(z, x) * 180.0D / 3.141592653589793D - 90.0D + (double)((int)(4.0F / range)));
      float calcPitch = (float)(-(MathHelper.atan2(y, (double)calculate) * 180.0D / 3.141592653589793D) + (double)(5.0F / range));
      currentPitch = (float)(entityIn.posY + (double)entityIn.getEyeHeight() - mc.player.posY + (double)mc.player.getEyeHeight()) / 3.0F;
      currentYaw = (float)((entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D - mc.player.posY + (double)mc.player.getEyeHeight());
      float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
      float gcd = f * f * f * 1.2F;
      float yaw = updateRotation(currentYaw, calcYaw, yawSpeed);
      float pitch = updateRotation(currentPitch, calcPitch, pitchSpeed);
      yaw -= yaw % gcd;
      pitch -= pitch % gcd;
      return new float[]{yaw, pitch};
   }

   public static float updateRotation(float current, float intended, float speed) {
      float f = MathHelper.wrapDegrees(intended - current);
      if (f > speed) {
         f = speed;
      }

      if (f < -speed) {
         f = -speed;
      }

      return current + f;
   }
}
