//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import java.lang.reflect.Field;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;

public class InventoryMove extends Module {
      TimerUtil timerUtil;

      void moveBack(double var1) {
            float var3 = InventoryMove.getDirection();
            EntityPlayerSP var10000 = mc.player;
            var10000.motionX += (double)MathHelper.sin(var3) * var1;
            var10000 = mc.player;
            var10000.motionZ -= (double)MathHelper.cos(var3) * var1;
      }

      void handleForward(double var1) {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
                  this.moveForward(var1);
            }
      }

      public InventoryMove() {
            super("InventoryMove", "allows you to walk in GUI", (213149979 >> 2 | 22677385) ^ 58662863, Module.Category.MOVEMENT);
            TimerUtil var10001 = new TimerUtil();
            if (((8388616 << 4 | 112709614) << 3 ^ 101375193) != 0) {
                  ;
            }

            this.timerUtil = var10001;
      }

      void moveRight(double var1) {
            float var3 = InventoryMove.getDirection();
            EntityPlayerSP var10000 = mc.player;
            double var10001 = var10000.motionZ;
            if (!"ape covered in human flesh".equals("yo mama name maurice")) {
                  ;
            }

            var10000.motionZ = var10001 - (double)MathHelper.sin(var3) * var1;
            var10000 = mc.player;
            var10001 = var10000.motionX;
            if (!"your mom your dad the one you never had".equals("your mom your dad the one you never had")) {
                  ;
            }

            var10000.motionX = var10001 - (double)MathHelper.cos(var3) * var1;
      }

      void handleLeft(double var1) {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
                  if (!"your mom your dad the one you never had".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  if (!"stringer is a good obfuscator".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  this.moveLeft(var1);
            }
      }

      public static void setPressed(KeyBinding var0, boolean var1) {
            try {
                  if (((((1890425426 ^ 206896867) >>> 3 ^ 9500657) & 202592063) >> 2 ^ 50385609) == 0) {
                        ;
                  }

                  Field var2 = var0.getClass().getDeclaredField("pressed");
                  var2.setAccessible((boolean)(0 & 1674475850 & 430300733 ^ 1));
                  if (!"idiot".equals("stop skidding")) {
                        ;
                  }

                  var2.setBoolean(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  RuntimeException var10000 = new RuntimeException;
                  if (((33849521 >>> 3 | 1333627) ^ 5560703) != 0) {
                  }

                  var10000.<init>(var3);
                  throw var10000;
            }
      }

      @SubscribeEvent
      public void onClientTick(ClientTickEvent var1) {
            if (mc.player != null) {
                  if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat) && mc.player != null) {
                        if (!"minecraft".equals("idiot")) {
                              ;
                        }

                        if (mc.world != null) {
                              this.timerUtil.reset();
                              double var2 = 0.044D;
                              boolean var10000 = mc.player.onGround;
                              if ((416407289 >>> 2 >> 3 >>> 4 ^ 1007165676) != 0) {
                                    ;
                              }

                              if (!var10000) {
                                    var2 /= 4.0D;
                              }

                              this.handleJump();
                              this.handleForward(var2);
                              if (!"i hope you catch fire ngl".equals("please go outside")) {
                                    ;
                              }

                              var10000 = mc.player.onGround;
                              if (((177249526 | 26014248) >> 4 ^ -1355920863) != 0) {
                                    ;
                              }

                              if (!var10000) {
                                    var2 /= 2.0D;
                              }

                              if (((1149407778 << 4 ^ 705064103 | 1281095029) ^ 1685251529 ^ -1263727579) != 0) {
                                    ;
                              }

                              this.handleBack(var2);
                              this.handleLeft(var2);
                              if (!"you're dogshit".equals("you probably spell youre as your")) {
                                    ;
                              }

                              if (!"please dont crack my plugin".equals("please go outside")) {
                                    ;
                              }

                              this.handleRight(var2);
                              if ((13109257 >>> 2 << 2 >>> 1 ^ 205336054) != 0) {
                                    ;
                              }

                              return;
                        }

                        if (!"you're dogshit".equals("nefariousMoment")) {
                              ;
                        }
                  }

            }
      }

      void handleBack(double var1) {
            if ((1628761039 << 2 & 492474921 ^ 72516136) == 0) {
                  ;
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                  this.moveBack(var1);
            }
      }

      void moveForward(double var1) {
            float var3 = InventoryMove.getDirection();
            mc.player.setSprinting((boolean)((0 & 1209486032 ^ 1039508360) >>> 1 >>> 3 ^ 64969273));
            if (!"ape covered in human flesh".equals("your mom your dad the one you never had")) {
                  ;
            }

            EntityPlayerSP var10000 = mc.player;
            double var10001 = var10000.motionX;
            if (((30023386 << 1 << 1 >>> 1 | 34656310) ^ 7194967 ^ 1123483219) != 0) {
                  ;
            }

            float var10002 = MathHelper.sin(var3);
            if (!"please dont crack my plugin".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var10000.motionX = var10001 - (double)var10002 * var1;
            var10000 = mc.player;
            var10001 = var10000.motionZ;
            double var4 = (double)MathHelper.cos(var3);
            if (!"please get a girlfriend and stop cracking plugins".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var4 *= var1;
            if ((673005592 >>> 3 & 24862287 & 14439597 ^ 1) == 0) {
                  ;
            }

            var10000.motionZ = var10001 + var4;
      }

      void handleJump() {
            if (mc.player.onGround && Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
                  mc.player.jump();
            }

      }

      void moveLeft(double var1) {
            float var3 = InventoryMove.getDirection();
            EntityPlayerSP var10000 = mc.player;
            double var10001 = var10000.motionZ;
            double var10002 = (double)MathHelper.sin(var3);
            if ((833797994 << 1 >> 2 << 2 ^ 1667595988) == 0) {
                  ;
            }

            var10000.motionZ = var10001 + var10002 * var1;
            var10000 = mc.player;
            var10000.motionX += (double)MathHelper.cos(var3) * var1;
      }

      public static float getDirection() {
            float var0 = mc.player.rotationYaw;
            EntityPlayerSP var10000 = mc.player;
            if (!"yo mama name maurice".equals("your mom your dad the one you never had")) {
                  ;
            }

            if (var10000.moveForward < 0.0F) {
                  var0 += 180.0F;
            }

            float var1 = 1.0F;
            if (((1982094266 >>> 3 | 55771418) >> 1 ^ 85576107 ^ -1615992547) != 0) {
                  ;
            }

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
                  if (!"nefariousMoment".equals("your mom your dad the one you never had")) {
                        ;
                  }
            }

            var0 *= 0.017453292F;
            return var0;
      }

      void handleRight(double var1) {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
                  this.moveRight(var1);
            }
      }
}
