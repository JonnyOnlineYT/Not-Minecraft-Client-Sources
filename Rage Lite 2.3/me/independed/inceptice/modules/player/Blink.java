//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Blink extends Module {
      private EntityOtherPlayerMP oldPlayer;

      public Blink() {
            super("Blink", "Simulates lag. For other players it looks like you are teleporting.", (1004189788 ^ 926558649) >> 2 >> 2 << 1 ^ 27005500, Module.Category.PLAYER);
      }

      public void onEnable() {
            if (mc.player != null) {
                  ;
            }
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player == null) {
                  if (((1794883077 >>> 3 >>> 1 << 3 | 217049804) ^ 1040187340) == 0) {
                        ;
                  }

            } else {
                  if (((1180527218 << 3 >>> 1 | 64714115) >>> 4 ^ -377183916) != 0) {
                        ;
                  }

            }
      }

      public void onDisable() {
            if (mc.player != null) {
                  if ((148671587 >>> 1 >>> 1 & 29246970 ^ -1466717798) != 0) {
                        ;
                  }

            }
      }
}
