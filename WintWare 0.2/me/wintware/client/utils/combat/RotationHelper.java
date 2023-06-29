package me.wintware.client.utils.combat;

import net.minecraft.client.Minecraft;

public class RotationHelper {
   public float yaw;
   public float pitch;
   private Minecraft mc = Minecraft.getMinecraft();
   private static RotationHelper rotationManager;

   public boolean hasDiffrence() {
      double diffYaw = RotationUtil.angleDifference((double)this.mc.player.rotationYaw, (double)this.yaw);
      double diffPitch = RotationUtil.angleDifference((double)this.mc.player.rotationPitch, (double)this.pitch);
      if (diffPitch > 10.0D) {
         return true;
      } else {
         return diffYaw > 30.0D;
      }
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

   public static RotationHelper getRotationManager() {
      return rotationManager;
   }
}
