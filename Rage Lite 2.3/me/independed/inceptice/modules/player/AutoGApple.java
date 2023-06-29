//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoGApple extends Module {
      public NumberSetting needHealth = new NumberSetting("Health", this, 12.0D, 0.0D, 20.0D, 1.0D);
      private boolean eating = ((1906041850 | 1035812056) ^ 294492365 ^ 974963674 | 29849908) ^ 1475345917;

      void stop() {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), (boolean)(886339539 >>> 2 & 208779920 ^ 204544144));
      }

      public static boolean isNullOrEmptyStack(ItemStack var0) {
            int var10000;
            if (var0 != null && !var0.isEmpty()) {
                  if (((668828171 << 4 >>> 4 ^ 27316944) & 67353211 ^ 67178587) == 0) {
                        ;
                  }

                  var10000 = 1602473213 << 4 << 2 << 1 ^ -1041858944;
            } else {
                  if (!"i hope you catch fire ngl".equals("ape covered in human flesh")) {
                        ;
                  }

                  var10000 = ((0 | 307294010) ^ 188646914) << 3 ^ -881636927;
            }

            return (boolean)var10000;
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  EntityPlayerSP var10000 = mc.player;
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  float var2 = var10000.getHealth();
                  if (((119441772 | 80704706) >> 4 >>> 3 >>> 2 ^ 1592860779) != 0) {
                        ;
                  }

                  if ((double)(var2 + mc.player.getAbsorptionAmount()) >= this.needHealth.getValue() && this.eating) {
                        this.eating = (boolean)(((825995050 | 156668864) ^ 314793411 | 345800368) >> 3 ^ 133668823);
                        this.stop();
                        if (((1311956946 ^ 198878034) >>> 3 ^ -1417456670) != 0) {
                              ;
                        }

                  } else {
                        ItemStack var10001 = mc.player.getHeldItemOffhand();
                        if (((492611534 << 3 >>> 3 | 388363979) ^ -1444030592) != 0) {
                              ;
                        }

                        if (this.isFood(var10001)) {
                              double var4;
                              int var3 = (var4 = (double)mc.player.getHealth() - this.needHealth.getValue()) == 0.0D ? 0 : (var4 < 0.0D ? -1 : 1);
                              if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you probably spell youre as your")) {
                                    ;
                              }

                              if (var3 <= 0) {
                                    if (!"please go outside".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), (boolean)(0 & 1272484034 ^ 556390238 ^ 190774385 ^ 712452910));
                                    this.eating = (boolean)((0 ^ 2067138648) & 1571863084 ^ 1496320009);
                              }
                        }

                  }
            }
      }

      boolean isFood(ItemStack var1) {
            return (boolean)(!AutoGApple.isNullOrEmptyStack(var1) && var1.getItem() instanceof ItemAppleGold ? 0 >>> 3 << 2 ^ 239815490 ^ 239815491 : (681394374 << 4 | 708395629) & 419581268 ^ 151014468);
      }

      public AutoGApple() {
            super("AutoApple", "eat golden apples automatically", (1805695718 ^ 1423842152) >>> 1 & 494649165 ^ 490410053, Module.Category.PLAYER);
            this.settings.add(this.needHealth);
      }
}
