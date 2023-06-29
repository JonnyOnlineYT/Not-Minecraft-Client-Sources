//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoTotem extends Module {
      NumberSetting healthSet = new NumberSetting("Health", this, 10.0D, 1.0D, 20.0D, 0.5D);

      public AutoTotem() {
            super("AutoTotem", "automatically use totems", ((798738842 | 600055611) & 513332191 ^ 6337866 | 210164964) ^ 251592437, Module.Category.COMBAT);
            Setting[] var10001 = new Setting[(0 >>> 2 & 489405493) << 3 ^ 1];
            var10001[1625469456 >> 2 ^ 250918480 ^ 59546146 ^ 356548854] = this.healthSet;
            this.addSettings(var10001);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            EntityPlayerSP var10000 = mc.player;
            if (((2076059478 << 2 ^ 1225482658) & 288900343 ^ 2171514 ^ -1535710848) != 0) {
                  ;
            }

            if (var10000 != null) {
                  if (mc.world != null) {
                        ItemStack var2 = mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
                        NonNullList var3 = Minecraft.getMinecraft().player.inventory.mainInventory;
                        int var4 = 9453956 << 1 ^ 794257 ^ 7416964 ^ 22917405;

                        while(true) {
                              int var10001 = var3.size();
                              if ((60046410 >>> 3 >> 4 ^ 469112) == 0) {
                                    ;
                              }

                              if (var4 >= var10001) {
                                    break;
                              }

                              if (var3.get(var4) != ItemStack.EMPTY && (var2 == null || var2.getItem() != Items.TOTEM_OF_UNDYING) && ((ItemStack)var3.get(var4)).getItem() == Items.TOTEM_OF_UNDYING) {
                                    new ItemStack(Items.TOTEM_OF_UNDYING);
                                    this.b(var4);
                                    if (((342494786 << 1 & 89458781) << 3 ^ 623696550) != 0) {
                                          ;
                                    }
                                    break;
                              }

                              ++var4;
                        }

                        return;
                  }

                  if (((26680 & 2328 & 992) << 2 ^ 0) == 0) {
                        ;
                  }
            }

      }

      public void b(int var1) {
            if (((1264992986 ^ 922931371) >> 3 >> 2 & 22199212 ^ -16230787) != 0) {
                  ;
            }

            if (mc.player.openContainer instanceof ContainerPlayer && mc.player.ticksExisted % (4 >> 2 << 2 ^ 1) == 0 && (double)mc.player.getHealth() <= this.healthSet.getValue()) {
                  if ((536870913 ^ 536870913) == 0) {
                        ;
                  }

                  PlayerControllerMP var10000 = mc.playerController;
                  int var10001 = 562079760 >> 1 ^ 281039880;
                  if ((413860680 << 1 & 646183469 ^ -1184047269) != 0) {
                        ;
                  }

                  var10000.windowClick(var10001, (5 >> 2 >>> 2 | 408151296) ^ 408151341, ((181303014 | 2795097) & 35911639 | 1780017 | 37053459) & 7226697 ^ 3032385, ClickType.PICKUP, mc.player);
                  int var3 = var1 < (((2 | 1) ^ 2 ^ 0) << 3 ^ 1) ? var1 + (((32 ^ 3) >>> 3 | 0) ^ 32) : var1;
                  if (((329527399 >>> 4 | 5003467 | 19449896) ^ 25091055) == 0) {
                        ;
                  }

                  int var2 = var3;
                  var10000 = mc.playerController;
                  var10001 = (875016865 ^ 160866970 | 458819697 | 964577796) ^ 1073699455;
                  if ((((679402944 | 233875199) & 243713004) >>> 4 ^ 1041218046) != 0) {
                        ;
                  }

                  var10000.windowClick(var10001, var2, (705108267 ^ 52336002 | 265240221) >>> 2 << 4 ^ -1082197264, ClickType.PICKUP, mc.player);
            }

      }
}
