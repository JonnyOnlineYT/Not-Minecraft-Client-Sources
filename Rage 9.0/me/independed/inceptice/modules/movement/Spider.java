//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Spider extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  Minecraft var10000 = mc;
                  if ((2031971621 << 2 << 3 ^ 511463178 ^ 1037167530) == 0) {
                        ;
                  }

                  if (var10000.player.collidedHorizontally) {
                        mc.player.motionY = 0.2D;
                  }

                  if (((221282127 >> 3 << 3 | 26120330) ^ 2376137 ^ 1529084417) != 0) {
                        ;
                  }

            }
      }

      public Spider() {
            super("Spider", "Climb like a spider", ((854867521 | 814046349) & 399314752) << 3 ^ -1775734272, Module.Category.MOVEMENT);
      }
}
