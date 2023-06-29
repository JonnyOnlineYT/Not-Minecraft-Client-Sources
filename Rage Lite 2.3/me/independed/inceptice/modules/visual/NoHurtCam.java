//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class NoHurtCam extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            EntityPlayerSP var10000 = mc.player;
            if (((983751901 ^ 150685974 ^ 351512621 | 461925632) >>> 4 ^ -1837038514) != 0) {
                  ;
            }

            if (var10000 != null && mc.world != null) {
                  mc.player.hurtResistantTime = 853837656 >> 2 & 141931914 ^ 137437570;
                  if ((958485575 >>> 1 ^ 103338472 ^ 448297931) == 0) {
                        ;
                  }

                  mc.player.maxHurtResistantTime = ((1919387125 ^ 1753153833) << 3 << 1 | 1073197080) ^ -1073742376;
            }
      }

      public NoHurtCam() {
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("shitted on you harder than archybot")) {
                  ;
            }

            super("NoHurtCam", "disables hurt effect", 1879359906 >> 1 & 29685924 & 16023 ^ 367 ^ 8687, Module.Category.RENDER);
      }
}
