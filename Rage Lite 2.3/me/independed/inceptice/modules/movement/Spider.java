//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Spider extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  EntityPlayerSP var10000 = mc.player;
                  if (!"please get a girlfriend and stop cracking plugins".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  if (var10000.collidedHorizontally) {
                        mc.player.motionY = 0.2D;
                  }

            }
      }

      public Spider() {
            int var10003 = (2492610 ^ 459096) << 4 ^ 34642336;
            Module.Category var10004 = Module.Category.MOVEMENT;
            if ((310936321 << 3 ^ 798805346) != 0) {
                  ;
            }

            super("Spider", "Climb like a spider", var10003, var10004);
      }
}
