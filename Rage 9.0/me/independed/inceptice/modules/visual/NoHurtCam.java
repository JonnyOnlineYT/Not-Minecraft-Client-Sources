//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import me.independed.inceptice.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class NoHurtCam extends Module {
      public NoHurtCam() {
            super("NoHurtCam", "disables hurt effect", 601660773 << 1 & 370066280 ^ 63522797 ^ 96552357, Module.Category.RENDER);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  mc.player.hurtResistantTime = 1812037640 >>> 2 ^ 210141447 ^ 394715397;
                  mc.player.maxHurtResistantTime = ((968725932 | 648417376) ^ 748993322 | 54453854) ^ 322957022;
            }
      }
}
