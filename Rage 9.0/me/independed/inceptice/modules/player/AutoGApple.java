//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoGApple extends Module {
      private boolean eating = (970281971 | 542140645 | 782446617) << 2 << 4 >> 1 ^ -17858592;
      public NumberSetting needHealth = new NumberSetting("Health", this, 12.0D, 0.0D, 20.0D, 1.0D);

      void stop() {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), (boolean)((1871405198 >>> 2 | 367459562) >>> 1 ^ 267615861));
      }

      boolean isFood(ItemStack var1) {
            return (boolean)(!AutoGApple.isNullOrEmptyStack(var1) && var1.getItem() instanceof ItemAppleGold ? ((0 & 2089021903) >>> 3 | 560537883) ^ 560537882 : (104965997 | 46043454) << 3 << 4 ^ 2145107840);
      }

      public AutoGApple() {
            super("AutoApple", "eat golden apples automatically", (1776642152 << 2 | 911680683 | 403523557) ^ 298832773 ^ -1374669718, Module.Category.PLAYER);
            this.settings.add(this.needHealth);
      }

      public static boolean isNullOrEmptyStack(ItemStack var0) {
            return (boolean)(var0 != null && !var0.isEmpty() ? 1760538793 >>> 4 & 79297129 & 69555265 ^ 67129344 : 0 >> 3 << 2 ^ 1);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  if ((1137345527 >>> 4 << 3 >> 2 ^ 142168190) == 0) {
                        ;
                  }

                  if ((double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()) >= this.needHealth.getValue() && this.eating) {
                        this.eating = (boolean)(1970078473 << 1 >>> 3 >> 2 & 45893260 ^ 34881536);
                        this.stop();
                  } else {
                        if (this.isFood(mc.player.getHeldItemOffhand())) {
                              double var10000 = (double)mc.player.getHealth();
                              if (!"please dont crack my plugin".equals("stop skidding")) {
                                    ;
                              }

                              if (var10000 <= this.needHealth.getValue()) {
                                    GameSettings var2 = mc.gameSettings;
                                    if (((26370560 ^ 16925172) << 2 ^ 37785552) == 0) {
                                          ;
                                    }

                                    KeyBinding.setKeyBindState(var2.keyBindUseItem.getKeyCode(), (boolean)(((0 ^ 1209445407) & 250941862 | 96138178 | 30848207) ^ 234813390));
                                    this.eating = (boolean)((0 & 1876072830) >> 3 ^ 1);
                              }
                        }

                  }
            }
      }
}
