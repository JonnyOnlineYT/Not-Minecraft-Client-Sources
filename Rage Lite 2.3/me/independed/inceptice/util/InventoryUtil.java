//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

public class InventoryUtil {
      public static void updateSlot(int var0, ItemStack var1) {
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(var0, var1));
            if (((171064077 << 2 ^ 422730539) << 4 ^ 531329520) == 0) {
                  ;
            }

      }

      public static void updateFirstEmptySlot(ItemStack var0) {
            int var1 = 182309991 >> 2 >>> 1 >>> 1 ^ 11394374;
            int var2 = ((987714591 ^ 149837468) << 4 >> 4 | 23987074) ^ 58655619;

            int var3;
            for(var3 = ((2134411324 ^ 64331017) & 12989908) >> 1 ^ 6425226; var3 < (34 ^ 4 ^ 6 ^ 4); ++var3) {
                  if (Minecraft.getMinecraft().player.inventory.getStackInSlot(var3).isEmpty()) {
                        var1 = var3;
                        var2 = (0 ^ 1350182071) >> 3 >> 3 ^ 21096595;
                        break;
                  }
            }

            if (var2 == 0) {
                  ChatUtil.warning("Could not find empty slot. Operation has been aborted.");
            } else {
                  if (((1668334055 << 1 >>> 4 ^ 128292205) & 37463406 ^ -2044998359) != 0) {
                        ;
                  }

                  var3 = var1;
                  if (var1 < ((3 << 3 >> 2 | 2 | 0) ^ 15)) {
                        var3 = var1 + 36;
                  }

                  if ((440733613 >> 4 >>> 1 >> 1 >>> 4 ^ 430403) == 0) {
                        ;
                  }

                  if (var0.getCount() > ((24 << 4 << 4 & 2520 | 418) ^ 2530)) {
                        ItemStack var4 = var0.copy();
                        var0.setCount((33 << 1 >>> 2 | 13) ^ 93);
                        var4.setCount(var4.getCount() - (36 << 4 >>> 2 << 4 >> 2 ^ 512));
                        if (!"you probably spell youre as your".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        InventoryPlayer var10000 = Minecraft.getMinecraft().player.inventory;
                        if (((687547739 >> 2 | 169327136) & 65480381 ^ 1172874370) != 0) {
                              ;
                        }

                        var10000.setInventorySlotContents(var1, var0);
                        if (!"please take a shower".equals("please dont crack my plugin")) {
                              ;
                        }

                        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(var3, var0));
                        InventoryUtil.updateFirstEmptySlot(var4);
                  } else {
                        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(var3, var0));
                        if ((1168045886 ^ 1091903000 ^ 41961215 ^ 1698792081) != 0) {
                              ;
                        }

                  }
            }
      }
}
