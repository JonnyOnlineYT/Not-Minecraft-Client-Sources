//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class GodMode extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (mc.player.getHealth() <= 0.0F) {
                        mc.player.setHealth(mc.player.getMaxHealth());
                        if (mc.currentScreen instanceof GuiGameOver) {
                              mc.currentScreen = null;
                        }
                  }

            }
      }

      public GodMode() {
            if (((2129429527 >>> 2 >> 1 & 177469243) << 2 ^ 709246984) == 0) {
                  ;
            }

            super("GodMode", "Makes you invisible and invincible when you die on a vanilla server.", (726024121 | 572477134) << 1 & 561834332 ^ 3991900, Module.Category.PLAYER);
      }
}
