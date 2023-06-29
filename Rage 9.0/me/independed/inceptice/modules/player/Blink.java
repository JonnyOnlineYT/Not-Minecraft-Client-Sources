//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Blink extends Module {
      private EntityOtherPlayerMP oldPlayer;

      public void onDisable() {
            if (mc.player != null) {
                  ;
            }
      }

      public Blink() {
            super("Blink", "Simulates lag. For other players it looks like you are teleporting.", (89529476 ^ 67867398 | 3703763) ^ 25006035, Module.Category.PLAYER);
            if ((34052867 >> 2 << 3 ^ 7812320) != 0) {
                  ;
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  ;
            }
      }

      public void onEnable() {
            if (mc.player != null) {
                  ;
            }
      }
}
