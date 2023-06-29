//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Strafe extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (!"i hope you catch fire ngl".equals("stop skidding")) {
                        ;
                  }

                  Strafe.strafe();
                  if (((739267328 ^ 225974968 | 402445377) & 767406757 ^ 633114785) == 0) {
                        ;
                  }

            }
      }

      public static double getDirection() {
            float var0 = mc.player.rotationYaw;
            if (!"you probably spell youre as your".equals("i hope you catch fire ngl")) {
                  ;
            }

            if (mc.player.moveForward < 0.0F) {
                  var0 += 180.0F;
            }

            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
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

            if (mc.player.moveStrafing < 0.0F) {
                  var0 += 90.0F * var1;
            }

            return Math.toRadians((double)var0);
      }

      public static void strafe() {
            Strafe.strafe(Strafe.getSpeed());
            if ((703276005 >> 4 ^ 22007231 ^ 64061313) == 0) {
                  ;
            }

      }

      public static void strafe(float var0) {
            if (Strafe.isMoving()) {
                  if (!"i hope you catch fire ngl".equals("please dont crack my plugin")) {
                        ;
                  }

                  double var1 = Strafe.getDirection();
                  Minecraft var10000 = mc;
                  if ((1722772382 >>> 3 << 1 ^ 149659580 ^ 2016753748) != 0) {
                        ;
                  }

                  EntityPlayerSP var3 = var10000.player;
                  double var10001 = -Math.sin(var1);
                  if (((238393436 | 227119153) & 82424158 ^ -1537101269) != 0) {
                        ;
                  }

                  var3.motionX = var10001 * (double)var0;
                  mc.player.motionZ = Math.cos(var1) * (double)var0;
            }
      }

      public static float getSpeed() {
            EntityPlayerSP var10000 = mc.player;
            if (((1907639740 >>> 1 ^ 62410724) << 4 ^ -1238969440) == 0) {
                  ;
            }

            double var0 = var10000.motionX * mc.player.motionX;
            Minecraft var10001 = mc;
            if ((540673 >>> 2 ^ 135168) == 0) {
                  ;
            }

            EntityPlayerSP var1 = var10001.player;
            if (((1050176 >> 2 | 134090) ^ 396250) == 0) {
                  ;
            }

            double var2 = var1.motionZ;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            return (float)Math.sqrt(var0 + var2 * mc.player.motionZ);
      }

      public static boolean isMoving() {
            if (((1295554429 << 3 >> 2 >> 3 & 5418654) << 3 ^ 1406586241) != 0) {
                  ;
            }

            int var10000;
            label37: {
                  if (mc.player != null) {
                        float var0;
                        var10000 = (var0 = mc.player.movementInput.moveForward - 0.0F) == 0.0F ? 0 : (var0 < 0.0F ? -1 : 1);
                        if (!"nefariousMoment".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        if (var10000 != 0) {
                              break label37;
                        }

                        if (!"idiot".equals("nefariousMoment")) {
                              ;
                        }

                        if (mc.player.movementInput.moveStrafe != 0.0F) {
                              break label37;
                        }
                  }

                  var10000 = (1327016345 << 1 & 343852141 | 256449447) & 131556277 ^ 122766245;
                  return (boolean)var10000;
            }

            var10000 = (0 & 508666951 | 34723548) >>> 2 >>> 2 ^ 2170220;
            return (boolean)var10000;
      }

      public Strafe() {
            super("Strafe", "strafing", 740264322 >>> 3 >>> 2 ^ 15465848 ^ 25887028, Module.Category.MOVEMENT);
      }
}
