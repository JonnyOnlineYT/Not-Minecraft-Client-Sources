//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class HighJump extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            Minecraft var10000 = mc;
            if ((815923911 >>> 3 << 1 >>> 4 ^ 12748811) == 0) {
                  ;
            }

            EntityPlayerSP var2 = var10000.player;
            if (((1826699964 >> 4 & 108649904 | 71010934) << 3 ^ 19633082 ^ -554800818) != 0) {
                  ;
            }

            if (var2 != null) {
                  if (((1129547598 | 288722178 | 607148224 | 1413287173) ^ 2004869071) == 0) {
                        ;
                  }

                  if (!"minecraft".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  if (mc.player.hurtTime > 0 && mc.gameSettings.keyBindJump.isPressed()) {
                        var2 = mc.player;
                        var2.motionY += 0.9736375212669373D;
                  }

                  if (mc.player.hurtTime > 0 && mc.gameSettings.keyBindForward.isKeyDown()) {
                        if ((((104963115 << 2 ^ 322416231) << 3 | 1200186880) ^ -941931417) != 0) {
                              ;
                        }

                        LongJump.setMoveSpeed(0.431237D);
                  }

            }
      }

      public HighJump() {
            if (((1590287215 << 3 << 4 | 1088544414) >>> 3 ^ 211664883) == 0) {
                  ;
            }

            super("HighJump", "higher jump", (((24600692 ^ 14587244) >> 3 | 1004594) & 1123258 | 577089) ^ 1699571, Module.Category.MOVEMENT);
      }
}
