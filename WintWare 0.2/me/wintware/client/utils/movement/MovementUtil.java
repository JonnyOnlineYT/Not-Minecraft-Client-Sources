package me.wintware.client.utils.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;

public class MovementUtil {
   public static Minecraft mc = Minecraft.getMinecraft();

   public static float getDirection() {
      Minecraft mc = Minecraft.getMinecraft();
      float var1 = mc.player.rotationYaw;
      if (mc.player.moveForward < 0.0F) {
         var1 += 180.0F;
      }

      float forward = 1.0F;
      if (mc.player.moveForward < 0.0F) {
         forward = -0.5F;
      } else if (mc.player.moveForward > 0.0F) {
         forward = 0.5F;
      }

      if (mc.player.moveStrafing > 0.0F) {
         var1 -= 90.0F * forward;
      }

      if (mc.player.moveStrafing < 0.0F) {
         var1 += 90.0F * forward;
      }

      var1 *= 0.017453292F;
      return var1;
   }

   public static void setMotion(double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
      double forward = pseudoForward;
      double strafe = pseudoStrafe;
      float yaw = pseudoYaw;
      if (pseudoForward != 0.0D) {
         if (pseudoStrafe > 0.0D) {
            yaw = pseudoYaw + (float)(pseudoForward > 0.0D ? -45 : 45);
         } else if (pseudoStrafe < 0.0D) {
            yaw = pseudoYaw + (float)(pseudoForward > 0.0D ? 45 : -45);
         }

         strafe = 0.0D;
         if (pseudoForward > 0.0D) {
            forward = 1.0D;
         } else if (pseudoForward < 0.0D) {
            forward = -1.0D;
         }
      }

      if (strafe > 0.0D) {
         strafe = 1.0D;
      } else if (strafe < 0.0D) {
         strafe = -1.0D;
      }

      double mx = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double mz = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      mc.player.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
      mc.player.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
   }

   public static void setSpeed(double speed) {
      double forward = (double)MovementInput.moveForward;
      double strafe = (double)MovementInput.moveStrafe;
      float yaw = mc.player.rotationYaw;
      if (forward == 0.0D && strafe == 0.0D) {
         mc.player.motionX = 0.0D;
         mc.player.motionZ = 0.0D;
      } else {
         if (forward != 0.0D) {
            if (strafe > 0.0D) {
               yaw += (float)(forward > 0.0D ? -45 : 45);
            } else if (strafe < 0.0D) {
               yaw += (float)(forward > 0.0D ? 45 : -45);
            }

            strafe = 0.0D;
            if (forward > 0.0D) {
               forward = 1.0D;
            } else if (forward < 0.0D) {
               forward = -1.0D;
            }
         }

         mc.player.motionX = forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F)));
         mc.player.motionZ = forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      }

   }
}
