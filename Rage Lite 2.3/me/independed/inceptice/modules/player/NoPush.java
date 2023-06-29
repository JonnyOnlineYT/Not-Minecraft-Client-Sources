//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class NoPush extends Module {
      public float saveReduction;

      @SubscribeEvent
      public void onPushEvent(PlayerSPPushOutOfBlocksEvent var1) {
            var1.setCanceled((boolean)((0 & 1471555011 | 1475691454) >> 4 ^ 92230714));
      }

      @SubscribeEvent
      public void onEvent(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  float var10000 = this.saveReduction;
                  if (((58857488 | 20580465) ^ 1638634010) != 0) {
                        ;
                  }

                  if (var10000 == 1.0E8F) {
                        this.saveReduction = mc.player.entityCollisionReduction;
                  }

                  mc.player.entityCollisionReduction = 1.0F;
            }
      }

      public void onDisable() {
            super.onDisable();
            if ((1903838561 << 1 >>> 4 >> 2 ^ 59494955) == 0) {
                  ;
            }

            if (mc.world != null && mc.player != null) {
                  mc.player.entityCollisionReduction = this.saveReduction;
            }

      }

      public NoPush() {
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("yo mama name maurice")) {
                  ;
            }

            super("NoPush", "other players can't push you", (447730332 | 174153571) ^ 12402128 ^ 441639471, Module.Category.PLAYER);
            if (!"i hope you catch fire ngl".equals("intentMoment")) {
                  ;
            }

            this.saveReduction = 1.0E8F;
      }
}
