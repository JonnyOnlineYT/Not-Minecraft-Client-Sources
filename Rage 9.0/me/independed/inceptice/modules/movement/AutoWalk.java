//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoWalk extends Module {
      public AutoWalk() {
            if (((285493565 >>> 2 | 19276171) >>> 3 ^ 2107136508) != 0) {
                  ;
            }

            super("AutoWalk", "auto walk ok", 183077613 >> 3 >> 3 ^ 1400152 ^ 4127603, Module.Category.MOVEMENT);
            if ((37769412 >> 2 >> 3 ^ 2025921343) != 0) {
                  ;
            }

      }

      public void onDisable() {
            EntityPlayerSP var10000 = mc.player;
            if (((306672889 | 198253221) << 1 ^ 583455295) != 0) {
                  ;
            }

            if (var10000 != null || mc.world == null) {
                  int var1 = mc.gameSettings.keyBindForward.getKeyCode();
                  if (((305580458 >>> 2 >>> 1 | 9314326) ^ 47118647) == 0) {
                        ;
                  }

                  KeyBinding.setKeyBindState(var1, (boolean)(1520078630 >> 1 >>> 3 >> 4 ^ 5937807));
                  super.onDisable();
            }
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            Minecraft var10000 = mc;
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("idiot")) {
                  ;
            }

            if (var10000.player == null) {
                  WorldClient var2 = mc.world;
                  if (((1683164293 >>> 3 | 159768041 | 10328563) ^ 29945353 ^ 207033330) == 0) {
                        ;
                  }

                  if (var2 != null) {
                        return;
                  }
            }

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), (boolean)((0 & 430481672) >>> 1 >>> 1 ^ 1));
            if (((454557712 ^ 294621409) << 1 ^ 355410402) == 0) {
                  ;
            }

      }
}
