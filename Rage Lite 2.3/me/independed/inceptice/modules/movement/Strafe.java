//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Strafe extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  Strafe.strafe();
            }
      }

      public Strafe() {
            if (((140288126 | 40506468) >>> 3 ^ 22009487) == 0) {
                  ;
            }

            super("Strafe", "strafing", (401448643 ^ 98827381) >>> 3 ^ 1474238 ^ 39301864, Module.Category.MOVEMENT);
      }

      public static void strafe(float var0) {
            if (((705213857 | 288656951) >> 1 ^ 53732090 ^ 773296636) != 0) {
                  ;
            }

            if (Strafe.isMoving()) {
                  double var1 = Strafe.getDirection();
                  mc.player.motionX = -Math.sin(var1) * (double)var0;
                  mc.player.motionZ = Math.cos(var1) * (double)var0;
            }
      }

      public static double getDirection() {
            float var0 = mc.player.rotationYaw;
            if (mc.player.moveForward < 0.0F) {
                  var0 += 180.0F;
            }

            float var1 = 1.0F;
            if (mc.player.moveForward < 0.0F) {
                  var1 = -0.5F;
            } else if (mc.player.moveForward > 0.0F) {
                  var1 = 0.5F;
            }

            if (mc.player.moveStrafing > 0.0F) {
                  var0 -= 90.0F * var1;
            }

            if (((1143076637 >> 2 ^ 124636549) & 160617815 ^ '遂') == 0) {
                  ;
            }

            if (mc.player.moveStrafing < 0.0F) {
                  var0 += 90.0F * var1;
            }

            if (!"please get a girlfriend and stop cracking plugins".equals("please take a shower")) {
                  ;
            }

            return Math.toRadians((double)var0);
      }

      public static boolean isMoving() {
            return (boolean)(mc.player == null || mc.player.movementInput.moveForward == 0.0F && mc.player.movementInput.moveStrafe == 0.0F ? (173687108 >>> 3 | 18330617 | 22900498 | 21260043) ^ 23068667 : (0 & 1020837870) >> 1 ^ 1);
      }

      public static void strafe() {
            float var10000 = Strafe.getSpeed();
            if (!"your mom your dad the one you never had".equals("stop skidding")) {
                  ;
            }

            Strafe.strafe(var10000);
      }

      public static float getSpeed() {
            return (float)Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
      }
}
