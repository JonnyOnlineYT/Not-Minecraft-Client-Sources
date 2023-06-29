//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import me.independed.inceptice.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class FuckYou extends Module {
      public FuckYou() {
            super("Hurting", "disable hurt effect", (895878476 << 3 | 778575908) >>> 2 ^ 735971993, Module.Category.RENDER);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  mc.player.performHurtAnimation();
            }
      }
}
