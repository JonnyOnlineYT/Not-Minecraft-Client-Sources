//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoTotem extends Module {
      NumberSetting healthSet;

      public AutoTotem() {
            super("AutoTotem", "automatically use totems", 1664353497 >> 3 << 4 ^ -966260304, Module.Category.COMBAT);
            NumberSetting var10001 = new NumberSetting;
            if (!"stringer is a good obfuscator".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var10001.<init>("Health", this, 10.0D, 1.0D, 20.0D, 0.5D);
            this.healthSet = var10001;
            if ((1111794678 >>> 3 >>> 2 ^ 1790971168) != 0) {
                  ;
            }

            Setting[] var1 = new Setting[(0 >>> 2 | 1707578487) >>> 1 ^ 853789242];
            var1[(1598984390 >>> 4 | 55336213) ^ 134020381] = this.healthSet;
            this.addSettings(var1);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (!"minecraft".equals("minecraft")) {
                  ;
            }

            if (mc.player != null && mc.world != null) {
                  ItemStack var2 = mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
                  NonNullList var3 = Minecraft.getMinecraft().player.inventory.mainInventory;

                  for(int var4 = 873058655 << 3 >>> 2 ^ 672375486; var4 < var3.size(); ++var4) {
                        if (var3.get(var4) != ItemStack.EMPTY) {
                              if (!"i hope you catch fire ngl".equals("i hope you catch fire ngl")) {
                                    ;
                              }

                              if (var2 == null || var2.getItem() != Items.TOTEM_OF_UNDYING) {
                                    if (((1465897138 << 4 >>> 4 | 115809056) ^ 134208434) == 0) {
                                          ;
                                    }

                                    if (((ItemStack)var3.get(var4)).getItem() == Items.TOTEM_OF_UNDYING) {
                                          new ItemStack(Items.TOTEM_OF_UNDYING);
                                          if ((194846074 >> 2 & 26275073 ^ 282503183) != 0) {
                                                ;
                                          }

                                          this.b(var4);
                                          break;
                                    }
                              }
                        }

                        if (!"your mom your dad the one you never had".equals("you probably spell youre as your")) {
                              ;
                        }
                  }

            }
      }

      public void b(int var1) {
            Container var10000 = mc.player.openContainer;
            if (((705608281 >> 1 >> 4 | 3911021) >>> 4 ^ 1814490883) != 0) {
                  ;
            }

            if (var10000 instanceof ContainerPlayer && mc.player.ticksExisted % ((1 | 0) >>> 3 ^ 5) == 0 && (double)mc.player.getHealth() <= this.healthSet.getValue()) {
                  mc.playerController.windowClick((673780444 | 521033465) >> 1 & 425800512 & 290005761 ^ 285287168, (40 & 12) >>> 2 ^ 47, (1736671742 >> 3 | 154058084) << 1 ^ 469629694, ClickType.PICKUP, mc.player);
                  int var2 = var1 < ((0 >>> 1 & 619254344) >>> 2 ^ 9) ? var1 + ((19 ^ 15) >> 4 & 0 ^ 36) : var1;
                  mc.playerController.windowClick((825042193 ^ 11884987) >>> 3 >> 4 & 5165498 ^ 4329656, var2, 325444941 >> 3 >> 1 ^ 20340308, ClickType.PICKUP, mc.player);
            }

      }
}
