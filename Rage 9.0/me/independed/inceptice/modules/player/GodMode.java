//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class GodMode extends Module {
      public GodMode() {
            super("GodMode", "Makes you invisible and invincible when you die on a vanilla server.", 2164229 >>> 2 >> 1 ^ 270528, Module.Category.PLAYER);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (mc.player.getHealth() <= 0.0F) {
                        EntityPlayerSP var10000 = mc.player;
                        float var10001 = mc.player.getMaxHealth();
                        if ((608206972 << 2 << 3 ^ 321878179 ^ -1690339549) == 0) {
                              ;
                        }

                        var10000.setHealth(var10001);
                        if (mc.currentScreen instanceof GuiGameOver) {
                              mc.currentScreen = null;
                        }
                  }

            }
      }
}
