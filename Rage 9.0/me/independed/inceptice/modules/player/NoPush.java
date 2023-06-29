//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class NoPush extends Module {
      public float saveReduction;

      public NoPush() {
            if (!"please go outside".equals("shitted on you harder than archybot")) {
                  ;
            }

            int var10003 = (1878684642 >>> 4 | 70971928) >>> 4 >> 1 ^ 3669945;
            Module.Category var10004 = Module.Category.PLAYER;
            if ((887213714 >>> 4 << 3 & 280403448 ^ 271615304) == 0) {
                  ;
            }

            super("NoPush", "other players can't push you", var10003, var10004);
            this.saveReduction = 1.0E8F;
      }

      public void onDisable() {
            super.onDisable();
            Minecraft var10000 = mc;
            if (!"shitted on you harder than archybot".equals("you're dogshit")) {
                  ;
            }

            if (var10000.world != null && mc.player != null) {
                  EntityPlayerSP var1 = mc.player;
                  if ((339627212 >> 2 >>> 3 & 7378681 & 228703 ^ -642465064) != 0) {
                        ;
                  }

                  var1.entityCollisionReduction = this.saveReduction;
            }

      }

      @SubscribeEvent
      public void onEvent(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  if (this.saveReduction == 1.0E8F) {
                        this.saveReduction = mc.player.entityCollisionReduction;
                  }

                  mc.player.entityCollisionReduction = 1.0F;
            }
      }

      @SubscribeEvent
      public void onPushEvent(PlayerSPPushOutOfBlocksEvent var1) {
            var1.setCanceled((boolean)((0 | 1847501796 | 1121050778) ^ 1860166655));
            if (((1142629600 >> 4 | 12165690) ^ 83473022) == 0) {
                  ;
            }

      }
}
