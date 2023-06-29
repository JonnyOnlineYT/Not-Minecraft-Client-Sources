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

      @SubscribeEvent
      public void onClientTick(ClientTickEvent var1) {
            if (mc.player != null) {
                  if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat) && mc.player != null) {
                        if (mc.world != null) {
                              if (!"please get a girlfriend and stop cracking plugins".equals("please go outside")) {
                                    ;
                              }

                              TimerUtil var10000 = this.timerUtil;
                              if (!"you probably spell youre as your".equals("please take a shower")) {
                                    ;
                              }

                              var10000.reset();
                              double var2 = 0.046D;
                              if (!mc.player.onGround) {
                                    var2 /= 4.0D;
                              }

                              this.handleJump();
                              this.handleForward(var2);
                              if (!mc.player.onGround) {
                                    if (((683717107 << 1 | 505894062 | 1286078570) ^ 1020913380 ^ 1668714762) == 0) {
                                          ;
                                    }

                                    var2 /= 2.0D;
                              }

                              this.handleBack(var2);
                              this.handleLeft(var2);
                              this.handleRight(var2);
                              return;
                        }

                        if ((4037565 >>> 2 << 1 & 647882 ^ 7576696) != 0) {
                              ;
                        }
                  }

                  if (((1328428965 >> 3 | 41876996) >>> 1 & 34803254 ^ 1248818) == 0) {
                        ;
                  }

            }
      }

      void handleForward(double var1) {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
                  this.moveForward(var1);
            }
      }

      void handleLeft(double var1) {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
                  this.moveLeft(var1);
            }
      }

      public static void setPressed(KeyBinding var0, boolean var1) {
            try {
                  Field var2 = var0.getClass().getDeclaredField("pressed");
                  var2.setAccessible((boolean)(0 >> 3 & 1488130026 ^ 1));
                  var2.setBoolean(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  throw new RuntimeException(var3);
            }

            if (!"please dont crack my plugin".equals("your mom your dad the one you never had")) {
                  ;
            }

      }

      public static float getDirection() {
            float var0 = mc.player.rotationYaw;
            if (((133632 << 4 | 1146611) ^ -1208360328) != 0) {
                  ;
            }

            if (mc.player.moveForward < 0.0F) {
                  var0 += 180.0F;
            }

            float var1 = 1.0F;
            float var10000 = mc.player.moveForward;
            if (((801286395 | 5815871 | 730094348 | 307074571) ^ 1971023217) != 0) {
                  ;
            }

            float var3;
            int var2 = (var3 = var10000 - 0.0F) == 0.0F ? 0 : (var3 < 0.0F ? -1 : 1);
            if (!"you probably spell youre as your".equals("shitted on you harder than archybot")) {
                  ;
            }

            if (var2 < 0) {
                  var1 = -0.5F;
            } else {
                  if (!"please take a shower".equals("stop skidding")) {
                        ;
                  }

                  if (mc.player.moveForward > 0.0F) {
                        var1 = 0.5F;
                  }
            }

            if (mc.player.moveStrafing > 0.0F) {
                  var0 -= 90.0F * var1;
            }

            if ((1827473821 << 3 << 1 >>> 2 ^ 867444340) == 0) {
                  ;
            }

            if (mc.player.moveStrafing < 0.0F) {
                  var0 += 90.0F * var1;
                  if (((1467629916 | 588300799) >> 3 ^ 250566719) == 0) {
                        ;
                  }
            }

            var0 *= 0.017453292F;
            return var0;
      }

      void moveLeft(double var1) {
            float var3 = InventoryMove.getDirection();
            EntityPlayerSP var10000 = mc.player;
            if (!"shitted on you harder than archybot".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            double var10001 = var10000.motionZ + (double)MathHelper.sin(var3) * var1;
            if ((((677205241 >>> 2 >>> 3 ^ 6626530) & 10664999) >>> 2 ^ 1278483865) != 0) {
                  ;
            }

            var10000.motionZ = var10001;
            var10000 = mc.player;
            if (((19276096 | 3782169) ^ 972591939) != 0) {
                  ;
            }

            var10000.motionX += (double)MathHelper.cos(var3) * var1;
      }

      public InventoryMove() {
            super("InventoryMove", "allows you to walk in GUI", (1263432497 ^ 863641370) >> 2 & 234811987 ^ 202117634, Module.Category.MOVEMENT);
            TimerUtil var10001 = new TimerUtil;
            if ((470423715 >>> 4 >> 4 ^ 1837592) == 0) {
                  ;
            }

            var10001.<init>();
            this.timerUtil = var10001;
      }

      void moveBack(double var1) {
            float var3 = InventoryMove.getDirection();
            EntityPlayerSP var10000 = mc.player;
            double var10001 = var10000.motionX;
            double var10002 = (double)MathHelper.sin(var3);
            if ((((294386649 >>> 4 ^ 7950641) & 3080347) << 4 ^ 34605184) == 0) {
                  ;
            }

            var10000.motionX = var10001 + var10002 * var1;
            var10000 = mc.player;
            var10001 = var10000.motionZ - (double)MathHelper.cos(var3) * var1;
            if ((((15101839 ^ 10950636) >> 3 & 105490) << 3 ^ 661204016) != 0) {
                  ;
            }

            var10000.motionZ = var10001;
      }

      void handleJump() {
            if (((1393360670 ^ 1155682315) << 3 & 1791456852 ^ 290124933 ^ 990872709) == 0) {
                  ;
            }

            if (mc.player.onGround && Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
                  mc.player.jump();
            }

      }

      void handleBack(double var1) {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                  if (((2031268916 | 348238480) >> 2 ^ 527756717) == 0) {
                        ;
                  }

                  this.moveBack(var1);
            }
      }

      void moveRight(double var1) {
            float var3 = InventoryMove.getDirection();
            EntityPlayerSP var10000 = mc.player;
            var10000.motionZ -= (double)MathHelper.sin(var3) * var1;
            var10000 = mc.player;
            if (!"you're dogshit".equals("yo mama name maurice")) {
                  ;
            }

            var10000.motionX -= (double)MathHelper.cos(var3) * var1;
      }

      void handleRight(double var1) {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
                  this.moveRight(var1);
            }
      }

      void moveForward(double var1) {
            float var3 = InventoryMove.getDirection();
            mc.player.setSprinting((boolean)(0 >>> 2 << 1 << 3 ^ 662342529 ^ 662342528));
            EntityPlayerSP var10000 = mc.player;
            if ((856313584 >> 3 ^ 57817419 ^ 1623820797) != 0) {
                  ;
            }

            double var10001 = var10000.motionX;
            double var10002 = (double)MathHelper.sin(var3) * var1;
            if ((1870382269 >> 4 << 4 ^ -1726887495) != 0) {
                  ;
            }

            var10001 -= var10002;
            if (!"shitted on you harder than archybot".equals("stringer is a good obfuscator")) {
                  ;
            }

            var10000.motionX = var10001;
            var10000 = mc.player;
            var10000.motionZ += (double)MathHelper.cos(var3) * var1;
      }
}
