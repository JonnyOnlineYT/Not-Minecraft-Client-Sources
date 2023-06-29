//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Jesus extends Module {
      public Jesus() {
            super("Jesus", "walk on water", (1583095794 << 4 >> 3 | 203042193) >>> 3 ^ 530008062, Module.Category.MOVEMENT);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  WorldClient var10000 = mc.world;
                  if (((648536998 << 2 & 540735884 | 600599) >> 2 ^ 1185952885) != 0) {
                        ;
                  }

                  if (var10000 != null) {
                        if (mc.player.isInWater()) {
                              mc.player.motionY = 0.27462526D;
                        }

                        return;
                  }

                  if (!"please go outside".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }
            }

            if (((576331935 ^ 415147239) >> 4 ^ 61753415) == 0) {
                  ;
            }

      }
}
