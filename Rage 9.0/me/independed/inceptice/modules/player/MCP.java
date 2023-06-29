//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
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

      private boolean isItemStackPearl(ItemStack var1) {
            return var1.getItem() instanceof ItemEnderPearl;
      }

      public MCP() {
            super("MCP", "Middle Click Pearl, Throws a pearl if you middle-click pointing in mid-air", (1505013191 >>> 4 & 50205395 | 5382406) ^ 14296022, Module.Category.PLAYER);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            Minecraft var10000 = mc;
            if ((((213652954 ^ 75039236) & 83177998 | 12877110) ^ 12943166) == 0) {
                  ;
            }

            if (var10000.player != null && mc.world != null) {
                  var10000 = Minecraft.getMinecraft();
                  if (((1625675869 >>> 2 << 4 & 1544628804) << 3 ^ -572808255) != 0) {
                        ;
                  }

                  Minecraft var2 = var10000;
                  if (var2.player != null && var2.world != null) {
                        if ((10490887 >> 1 ^ -277359657) != 0) {
                              ;
                        }

                        GuiScreen var6 = var2.currentScreen;
                        if (((2079618967 >> 3 << 3 | 188462512) ^ 1778429711 ^ -472164913) != 0) {
                              ;
                        }

                        if (var6 == null) {
                              if (Mouse.isButtonDown((1 ^ 0) << 4 >> 2 ^ 6)) {
                                    if (!this.clicked) {
                                          if (((662232025 ^ 206971249 ^ 318556435) >>> 3 ^ -512552019) != 0) {
                                                ;
                                          }

                                          RayTraceResult var3 = var2.objectMouseOver;
                                          if (var3 != null) {
                                                if (((169934848 | 27179382 | 101691756) ^ 596106705) != 0) {
                                                      ;
                                                }

                                                if (var3.typeOfHit == Type.MISS) {
                                                      int var4 = this.findPearlInHotBar(var2);
                                                      if (var4 != (66096 >>> 4 & 3890 ^ 32 ^ -3)) {
                                                            if (((1554717737 >>> 2 | 273305821) ^ -410656369) != 0) {
                                                                  ;
                                                            }

                                                            int var5 = var2.player.inventory.currentItem;
                                                            var2.player.inventory.currentItem = var4;
                                                            PlayerControllerMP var7 = var2.playerController;
                                                            if ((433865119 >>> 1 << 3 & 851876397 ^ 574620200) == 0) {
                                                                  ;
                                                            }

                                                            var7.processRightClick(var2.player, var2.world, EnumHand.MAIN_HAND);
                                                            EntityPlayerSP var8 = var2.player;
                                                            if (((805571719 >> 4 ^ 32053099 | 34589480) ^ 49274795) == 0) {
                                                                  ;
                                                            }

                                                            var8.inventory.currentItem = var5;
                                                      }
                                                }
                                          }
                                    }

                                    this.clicked = (boolean)((0 >> 2 << 4 | 790388900) ^ 790388901);
                              } else {
                                    this.clicked = (boolean)(((779464375 | 627803163) ^ 397080874) & 113751957 ^ 12916629);
                              }
                        }

                  }
            }
      }

      private int findPearlInHotBar(Minecraft var1) {
            int var2 = 1146666554 >> 4 >>> 3 >> 2 ^ 2239583;
            if (((141772230 >>> 4 ^ 1580365) >>> 3 ^ 587178 ^ 613299496) != 0) {
                  ;
            }

            while(true) {
                  if ((710843384 << 4 << 3 ^ 1454042276) != 0) {
                        ;
                  }

                  boolean var10000 = InventoryPlayer.isHotbar(var2);
                  if (!"ape covered in human flesh".equals("you're dogshit")) {
                        ;
                  }

                  if (!var10000) {
                        return (1977941174 << 2 << 4 | 1125293974) ^ -2067775383;
                  }

                  if (((965178366 << 1 & 1404529496 | 815647546) >>> 2 ^ -574837769) != 0) {
                        ;
                  }

                  if (this.isItemStackPearl(var1.player.inventory.getStackInSlot(var2))) {
                        return var2;
                  }

                  ++var2;
            }
      }
}
