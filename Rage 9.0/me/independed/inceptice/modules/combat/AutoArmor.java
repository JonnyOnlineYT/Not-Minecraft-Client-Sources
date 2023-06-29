//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoArmor extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (((1163004490 | 636117418) << 4 ^ 537919045 ^ 2141798629) == 0) {
                  ;
            }

            if (mc.player != null) {
                  if (mc.player.ticksExisted % ((0 & 1295929412 | 351480405) & 186040110 ^ 1190406) != 0) {
                        boolean var10000 = mc.currentScreen instanceof GuiContainer;
                        if (!"stringer is a good obfuscator".equals("nefariousMoment")) {
                              ;
                        }

                        if (!var10000 || mc.currentScreen instanceof InventoryEffectRenderer) {
                              int[] var2 = new int[(1 ^ 0) & 0 ^ 4];
                              int[] var3 = new int[((0 & 1850355840) << 3 >> 1 | 1722194769) ^ 1722194773];

                              int var4;
                              ItemStack var5;
                              int var10002;
                              for(var4 = ((1806848991 | 1793820941) >>> 3 | 110110853) << 2 ^ 1073606652; var4 < ((0 | 222932120) << 2 ^ 891728484); ++var4) {
                                    var5 = mc.player.inventory.armorItemInSlot(var4);
                                    if (var5 != null) {
                                          if (((819004815 >> 4 & 50116004) << 2 ^ 137379840) == 0) {
                                                ;
                                          }

                                          if (var5.getItem() instanceof ItemArmor) {
                                                if ((597111014 << 4 >>> 3 ^ -2140280865) != 0) {
                                                      ;
                                                }

                                                var10002 = ((ItemArmor)var5.getItem()).damageReduceAmount;
                                                if ((1018712816 ^ 745085261 ^ 268320532 ^ 523203753) == 0) {
                                                      ;
                                                }

                                                var3[var4] = var10002;
                                          }
                                    }

                                    var2[var4] = 7408322 >> 1 << 2 ^ 6497019 ^ -8468352;
                              }

                              if (!"please dont crack my plugin".equals("you probably spell youre as your")) {
                                    ;
                              }

                              InventoryPlayer var12;
                              for(var4 = (1053453472 >>> 4 | 25542284) >> 2 ^ 16478131; var4 < ((6 >> 4 ^ 1893128181) << 1 ^ 1202201241 ^ -1509585577); ++var4) {
                                    if (((41491306 << 4 ^ 20820257) >>> 3 >>> 2 ^ 576897329) == 0) {
                                    }

                                    var5 = mc.player.inventory.getStackInSlot(var4);
                                    if (!"ape covered in human flesh".equals("buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }

                                    if (var5.getCount() > ((0 & 704113186) >>> 4 >> 2 ^ 1)) {
                                          if (!"you're dogshit".equals("nefariousMoment")) {
                                                ;
                                          }
                                    } else {
                                          if (((1571068867 ^ 140227979 | 867532040) ^ 2013232968) == 0) {
                                                ;
                                          }

                                          if (var5 != null && var5.getItem() instanceof ItemArmor) {
                                                ItemArmor var6 = (ItemArmor)var5.getItem();
                                                EntityEquipmentSlot var11 = var6.armorType;
                                                if ((299104 << 4 ^ 4785664) == 0) {
                                                      ;
                                                }

                                                int var7 = var11.ordinal() - (1 >>> 3 << 1 ^ 924199241 ^ 924199243);
                                                if (var7 == ((1 << 3 & 2 & 1093789884 | 597069183) ^ 597069181)) {
                                                      var12 = mc.player.inventory;
                                                      if ((((1548234246 | 134429051) ^ 793560809) << 3 << 2 ^ -2018612623) != 0) {
                                                            ;
                                                      }

                                                      if (var12.armorItemInSlot(var7).getItem().equals(Items.ELYTRA)) {
                                                            continue;
                                                      }
                                                }

                                                int var8 = var6.damageReduceAmount;
                                                if ((16843905 ^ 16843905) == 0) {
                                                      ;
                                                }

                                                if (var8 > var3[var7]) {
                                                      var2[var7] = var4;
                                                      var3[var7] = var8;
                                                }
                                          }
                                    }
                              }

                              for(var4 = (82383005 ^ 16532967 ^ 33007406) & 83948927 ^ 83947604; var4 < ((0 >> 2 & 494821792) >>> 2 >> 4 ^ 4); ++var4) {
                                    int var9 = var2[var4];
                                    if (((1765752708 << 2 & 155973759) >>> 4 ^ -997066961) != 0) {
                                          ;
                                    }

                                    int var10001 = 1741085349 << 4 >>> 2 ^ -521890453;
                                    if (((278528112 << 3 << 1 << 4 | 2058723230) ^ 1174223887) != 0) {
                                          ;
                                    }

                                    if (var9 != var10001) {
                                          if (!"idiot".equals("please get a girlfriend and stop cracking plugins")) {
                                                ;
                                          }

                                          var12 = mc.player.inventory;
                                          if (((1290084835 | 847085262) ^ 121532155 ^ 1591185717 ^ 655654945) == 0) {
                                                ;
                                          }

                                          ItemStack var10 = var12.armorItemInSlot(var4);
                                          if (var10 == null || var10 != ItemStack.EMPTY || mc.player.inventory.getFirstEmptyStack() != (((1976177593 | 1750269001) << 1 | 1346229361) ^ 71434252)) {
                                                if (var9 < (3 >>> 2 & 222201762 ^ 9)) {
                                                      var9 += 36;
                                                }

                                                PlayerControllerMP var13 = mc.playerController;
                                                var10001 = 1819435656 >> 2 << 1 & 558804260 ^ 537403652;
                                                var10002 = 2 >> 3 & 894902576 ^ 1220604719 ^ 1220604711;
                                                if (!"nefariousMoment".equals("yo mama name maurice")) {
                                                      ;
                                                }

                                                var13.windowClick(var10001, var10002 - var4, ((476709504 | 13174374 | 456068342) << 3 | 1897696913) ^ -8585295, ClickType.QUICK_MOVE, mc.player);
                                                mc.playerController.windowClick(260751565 << 3 << 3 >>> 4 ^ 237699892, var9, 267168362 ^ 10189191 ^ 144657046 ^ 51695459 ^ 83645976, ClickType.QUICK_MOVE, mc.player);
                                                break;
                                          }
                                    }
                              }

                        }
                  }
            }
      }

      public AutoArmor() {
            if ((((1514509913 ^ 1060353796 | 1044143929 | 904521320) ^ 361328203) >> 3 ^ -341372179) != 0) {
                  ;
            }

            super("AutoArmor", "automatically equip armor", 647177932 >> 4 >>> 4 ^ 2528038, Module.Category.COMBAT);
      }
}
