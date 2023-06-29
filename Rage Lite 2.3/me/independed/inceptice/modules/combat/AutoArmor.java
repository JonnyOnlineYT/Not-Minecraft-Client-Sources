//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoArmor extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  int var10000 = mc.player.ticksExisted;
                  int var10001 = 0 << 2 >>> 2 << 3 ^ 2;
                  if (((1078001682 | 582768607) >>> 1 ^ -1669099855) != 0) {
                        ;
                  }

                  var10000 %= var10001;
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("yo mama name maurice")) {
                        ;
                  }

                  if (var10000 != 0) {
                        if (!(mc.currentScreen instanceof GuiContainer) || mc.currentScreen instanceof InventoryEffectRenderer) {
                              int[] var2 = new int[(0 | 1033814453) << 3 ^ -319418964];
                              int[] var3 = new int[(2 >>> 4 | 1188308916 | 779683134) ^ 1862084538];

                              int var4;
                              ItemStack var5;
                              for(var4 = (642425284 >>> 1 & 31490644 | 1252170) ^ 20126538; var4 < ((2 >> 4 >>> 1 >>> 2 | 138339207) ^ 138339203); ++var4) {
                                    var5 = mc.player.inventory.armorItemInSlot(var4);
                                    if (var5 != null && var5.getItem() instanceof ItemArmor) {
                                          if (((1705865741 | 1600653570 | 554223299) ^ 2146303951) == 0) {
                                                ;
                                          }

                                          var3[var4] = ((ItemArmor)var5.getItem()).damageReduceAmount;
                                    }

                                    var2[var4] = 1465116849 >> 4 << 3 << 2 << 2 ^ 1163967103;
                              }

                              if (!"you're dogshit".equals("please go outside")) {
                                    ;
                              }

                              for(var4 = '聄' >>> 4 ^ 2052; var4 < (15 << 4 << 2 ^ 996); ++var4) {
                                    var5 = mc.player.inventory.getStackInSlot(var4);
                                    if (((666572827 ^ 312659668) << 3 >>> 4 ^ 177007463) == 0) {
                                          ;
                                    }

                                    if (var5.getCount() <= ((0 | 119392904 | 61230384 | 99190413) & 53609047 ^ 53608980) && var5 != null && var5.getItem() instanceof ItemArmor) {
                                          ItemArmor var6 = (ItemArmor)var5.getItem();
                                          var10000 = var6.armorType.ordinal();
                                          if (((434554224 << 2 & 780263023 | 124364480) ^ 337817656 ^ 868943608) == 0) {
                                                ;
                                          }

                                          int var7 = var10000 - ((1 ^ 0) >>> 4 >>> 2 ^ 2);
                                          if (var7 != ((0 ^ 2055441548) & 1270589957 & 877542335 ^ 133126) || !mc.player.inventory.armorItemInSlot(var7).getItem().equals(Items.ELYTRA)) {
                                                if (((118603809 >> 2 & 16825289 | 16727753) << 2 ^ 134019876) == 0) {
                                                      ;
                                                }

                                                if (((277573552 >>> 2 & 16998097 | 'ꍚ') ^ 96882 ^ -1211074404) != 0) {
                                                      ;
                                                }

                                                int var8 = var6.damageReduceAmount;
                                                if ((1922023924 << 1 << 1 & 216356520 ^ 136644224) == 0) {
                                                      ;
                                                }

                                                if (var8 > var3[var7]) {
                                                      var2[var7] = var4;
                                                      if ((('�?' & 5506 | 3961) ^ 8057) == 0) {
                                                            ;
                                                      }

                                                      var3[var7] = var8;
                                                }
                                          }
                                    }
                              }

                              var4 = (383388168 >> 2 | 67074114 | 77104529) ^ 134217683;

                              while(var4 < ((2 >>> 1 | 0) >> 2 & 2094510850 ^ 4)) {
                                    if (!"nefariousMoment".equals("please get a girlfriend and stop cracking plugins")) {
                                          ;
                                    }

                                    if (((930346470 << 1 | 724513674) << 3 ^ 2139086448) == 0) {
                                          ;
                                    }

                                    int var9 = var2[var4];
                                    if (((1146272225 | 509341848) << 3 >> 4 << 4 ^ -1272552238) != 0) {
                                          ;
                                    }

                                    label137: {
                                          if (var9 != (((1899797137 >> 4 | 91704948) << 2 | 172371410 | 305260108) ^ -536870911)) {
                                                InventoryPlayer var11 = mc.player.inventory;
                                                if ((((758354076 >>> 1 & 147819386) >> 3 & 251167) >> 3 ^ 8737) == 0) {
                                                      ;
                                                }

                                                ItemStack var10 = var11.armorItemInSlot(var4);
                                                if (!"please go outside".equals("please go outside")) {
                                                      ;
                                                }

                                                if (var10 == null) {
                                                      break label137;
                                                }

                                                if (((954188561 | 864020827 | 455973323) ^ -533829948) != 0) {
                                                      ;
                                                }

                                                if (var10 != ItemStack.EMPTY || mc.player.inventory.getFirstEmptyStack() != (((1286450442 >>> 2 & 95306299) >>> 1 ^ 3311136 | 342140) ^ -10993534)) {
                                                      break label137;
                                                }
                                          }

                                          ++var4;
                                          continue;
                                    }

                                    if (var9 < ((4 ^ 3) << 3 << 2 ^ 233)) {
                                          if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please dont crack my plugin")) {
                                                ;
                                          }

                                          var9 += 36;
                                    }

                                    PlayerControllerMP var12 = mc.playerController;
                                    var10001 = (1898487825 >> 4 & 38511749) << 4 ^ 539492368;
                                    int var10002 = (1 << 1 ^ 0) >> 4 ^ 8;
                                    if (((1903740943 ^ 971997385 | 380248464) & 1395793966 & 447421342 ^ 304218118) == 0) {
                                          ;
                                    }

                                    var10002 -= var4;
                                    if ((507968674 >> 3 >> 2 >> 3 ^ 1984252) == 0) {
                                          ;
                                    }

                                    var12.windowClick(var10001, var10002, 1124143105 << 2 ^ 96388234 ^ 163218574, ClickType.QUICK_MOVE, mc.player);
                                    mc.playerController.windowClick((1837970845 << 4 | 586785565) ^ -84149283, var9, ((1473237945 << 4 | 1965873471) & 833894252 | 14618375) ^ 165095712 ^ 942151183, ClickType.QUICK_MOVE, mc.player);
                                    break;
                              }

                        }
                  }
            }
      }

      public AutoArmor() {
            super("AutoArmor", "automatically equip armor", (632579028 | 246614180) >> 2 << 2 ^ 800549876, Module.Category.COMBAT);
      }
}
