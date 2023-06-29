//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoWalk extends Module {
      public void onDisable() {
            if (mc.player != null || mc.world == null) {
                  KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), (boolean)((1193956354 << 1 | 916022169) >>> 3 << 1 ^ 800553702));
                  if ((1754286424 << 1 << 4 << 4 >>> 1 ^ 273242112) == 0) {
                        ;
                  }

                  super.onDisable();
            }
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null || mc.world == null) {
                  KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), (boolean)((0 | 1514810088) >> 2 & 288801018 ^ 269648059));
            }
      }

      public AutoWalk() {
            super("AutoWalk", "auto walk ok", ((89173666 | 58396446) & 108736038) >> 4 >> 1 ^ 3398001, Module.Category.MOVEMENT);
      }
}
