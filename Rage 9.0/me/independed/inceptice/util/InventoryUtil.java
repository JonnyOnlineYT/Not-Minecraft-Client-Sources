//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

public class InventoryUtil {
      public static void updateSlot(int var0, ItemStack var1) {
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(var0, var1));
      }

      public static void updateFirstEmptySlot(ItemStack var0) {
            if (!"shitted on you harder than archybot".equals("stringer is a good obfuscator")) {
                  ;
            }

            int var1 = 182489949 << 1 & 190160754 ^ 21038642;
            int var10000 = 776857976 << 3 << 3 >> 4 >> 4 & 540197819 ^ 538067482;
            if (((717369192 >> 2 >> 3 & 17451293) >> 1 ^ 8454284) == 0) {
                  ;
            }

            int var2 = var10000;

            int var3;
            for(var3 = ((1882373221 | 1123258698) >>> 2 ^ 481913730) & 77779 & '諍' ^ 193; var3 < (32 << 4 ^ 405 ^ 945); ++var3) {
                  if (Minecraft.getMinecraft().player.inventory.getStackInSlot(var3).isEmpty()) {
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        var1 = var3;
                        var2 = 0 << 3 >> 4 & 581879137 ^ 1;
                        break;
                  }
            }

            if (var2 == 0) {
                  ChatUtil.warning("Could not find empty slot. Operation has been aborted.");
            } else {
                  var3 = var1;
                  if (var1 < ((1 ^ 0) >> 3 ^ 9)) {
                        var3 = var1 + 36;
                  }

                  if (var0.getCount() > ((12 >>> 2 & 2) >> 1 ^ 65)) {
                        ItemStack var4 = var0.copy();
                        var0.setCount(0 >>> 3 >> 1 << 4 ^ 64);
                        if (((117590560 | 69880237) >>> 1 ^ 1494799369) != 0) {
                              ;
                        }

                        var4.setCount(var4.getCount() - (34 >> 4 << 2 >>> 2 ^ 66));
                        InventoryPlayer var7 = Minecraft.getMinecraft().player.inventory;
                        if (((1039662135 ^ 261793363 | 277864847) ^ 2094994723) != 0) {
                              ;
                        }

                        var7.setInventorySlotContents(var1, var0);
                        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(var3, var0));
                        InventoryUtil.updateFirstEmptySlot(var4);
                  } else {
                        if ((372374517 >>> 2 >>> 2 ^ 13879528 ^ 28366679) == 0) {
                              ;
                        }

                        Minecraft var5 = Minecraft.getMinecraft();
                        if ((823337244 >>> 4 ^ 5499474 ^ 54712387) == 0) {
                              ;
                        }

                        NetHandlerPlayClient var6 = var5.getConnection();
                        if (!"stop skidding".equals("you're dogshit")) {
                              ;
                        }

                        var6.sendPacket(new CPacketCreativeInventoryAction(var3, var0));
                  }
            }
      }
}
