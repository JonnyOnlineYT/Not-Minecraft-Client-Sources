//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Mouse;

public class MCP extends Module {
      private boolean clicked;

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if ((1110446176 >>> 3 >>> 2 & 34060641 ^ 1604707837) != 0) {
                        ;
                  }

                  if (mc.world != null) {
                        Minecraft var2 = Minecraft.getMinecraft();
                        EntityPlayerSP var10000 = var2.player;
                        if (((1567981129 ^ 1284662488 | 174087043) ^ -1903872919) != 0) {
                              ;
                        }

                        if (var10000 != null) {
                              if (!"buy a domain and everything else you need at namecheap.com".equals("i hope you catch fire ngl")) {
                                    ;
                              }

                              if (var2.world != null) {
                                    if (((1168193590 ^ 1132702666 | 4545110) ^ 73965801 ^ 34568983) == 0) {
                                          ;
                                    }

                                    if (var2.currentScreen == null) {
                                          if (Mouse.isButtonDown(((1 << 1 & 1) << 1 | 1552955993) ^ 1552955995)) {
                                                if (!this.clicked) {
                                                      RayTraceResult var3 = var2.objectMouseOver;
                                                      if (var3 != null) {
                                                            Type var6 = var3.typeOfHit;
                                                            Type var10001 = Type.MISS;
                                                            if ((109362715 << 1 << 4 ^ -1165906768) != 0) {
                                                                  ;
                                                            }

                                                            if (var6 == var10001) {
                                                                  int var7 = this.findPearlInHotBar(var2);
                                                                  if (!"please go outside".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                                        ;
                                                                  }

                                                                  int var4 = var7;
                                                                  if (var4 != (1215027443 >>> 3 >>> 2 ^ -37969608)) {
                                                                        if (!"ape covered in human flesh".equals("yo mama name maurice")) {
                                                                              ;
                                                                        }

                                                                        int var5 = var2.player.inventory.currentItem;
                                                                        var2.player.inventory.currentItem = var4;
                                                                        if (((800752068 >>> 3 & 29657109) << 1 ^ 1458222502) != 0) {
                                                                              ;
                                                                        }

                                                                        var2.playerController.processRightClick(var2.player, var2.world, EnumHand.MAIN_HAND);
                                                                        if ((1764265507 >>> 3 >>> 4 ^ 13783324) == 0) {
                                                                              ;
                                                                        }

                                                                        var10000 = var2.player;
                                                                        if ((1411765426 >> 4 >>> 1 >>> 2 ^ 1997080167) != 0) {
                                                                              ;
                                                                        }

                                                                        InventoryPlayer var8 = var10000.inventory;
                                                                        if ((1282227586 >> 4 >> 4 >> 4 ^ 313043) == 0) {
                                                                              ;
                                                                        }

                                                                        var8.currentItem = var5;
                                                                  }
                                                            }
                                                      }
                                                }

                                                this.clicked = (boolean)(((0 & 1604962454) >> 2 | 729504227) ^ 729504226);
                                          } else {
                                                this.clicked = (boolean)((202952849 >>> 1 | 2154396) ^ 103612380);
                                                if ((((152307357 >>> 4 & 8799594 ^ 8121763) >>> 4 | 559701) ^ 274934929) != 0) {
                                                      ;
                                                }
                                          }
                                    }

                                    if (!"your mom your dad the one you never had".equals("buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }

                                    return;
                              }
                        }

                        return;
                  }

                  if (((997399710 | 936474438 | 57909989) >>> 3 ^ 134119423) == 0) {
                        ;
                  }
            }

      }

      public MCP() {
            int var10003 = 268435504 >> 3 ^ 33554438;
            if ((577430474 << 4 >> 2 >> 1 ^ 741604420) != 0) {
                  ;
            }

            super("MCP", "Middle Click Pearl, Throws a pearl if you middle-click pointing in mid-air", var10003, Module.Category.PLAYER);
      }

      private int findPearlInHotBar(Minecraft var1) {
            for(int var2 = (1431024144 >> 1 << 3 | 1346212840) ^ 1430248424; InventoryPlayer.isHotbar(var2); ++var2) {
                  if (this.isItemStackPearl(var1.player.inventory.getStackInSlot(var2))) {
                        return var2;
                  }
            }

            return (1599568260 << 3 | 2022919974) >> 4 ^ 5522189;
      }

      private boolean isItemStackPearl(ItemStack var1) {
            boolean var10000 = var1.getItem() instanceof ItemEnderPearl;
            if ((((1308133705 ^ 1101181799) & 170174581 ^ 96992983) & 178826633 ^ 142640257) == 0) {
                  ;
            }

            return var10000;
      }
}
