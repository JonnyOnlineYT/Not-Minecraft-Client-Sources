//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ua.apraxia.utility.other;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import ua.apraxia.utility.Utility;

public class InventoryUtil implements Utility {
    public InventoryUtil() {
    }

    public static boolean doesHotbarHaveAxe() {
        for(int i = 0; i < 9; ++i) {
            Minecraft var10000 = mc;
            Minecraft.player.inventory.getStackInSlot(i);
            var10000 = mc;
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe) {
                return true;
            }
        }

        return false;
    }

    public static void disabler(int elytra) {
        Minecraft var10005 = mc;
        mc.playerController.windowClick(0, elytra, 0, ClickType.PICKUP, Minecraft.player);
        var10005 = mc;
        mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, Minecraft.player);
        Minecraft var10000 = mc;
        Minecraft var10003 = mc;
        Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, Action.START_FALL_FLYING));
        var10000 = mc;
        var10003 = mc;
        Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, Action.START_FALL_FLYING));
        var10005 = mc;
        mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, Minecraft.player);
        var10005 = mc;
        mc.playerController.windowClick(0, elytra, 0, ClickType.PICKUP, Minecraft.player);
    }

    public static int getSlotWithElytra() {
        for(int i = 0; i < 45; ++i) {
            Minecraft var10000 = mc;
            ItemStack itemStack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.ELYTRA) {
                return i < 9 ? i + 36 : i;
            }
        }

        return -1;
    }

    public static int getSlowWithArmor() {
        for(int i = 0; i < 45; ++i) {
            Minecraft var10000 = mc;
            ItemStack itemStack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.DIAMOND_CHESTPLATE || itemStack.getItem() == Items.GOLDEN_CHESTPLATE || itemStack.getItem() == Items.LEATHER_CHESTPLATE || itemStack.getItem() == Items.CHAINMAIL_CHESTPLATE || itemStack.getItem() == Items.IRON_LEGGINGS) {
                return i < 9 ? i + 36 : i;
            }
        }

        return -1;
    }



    public static void swapElytraToChestplate() {
        Minecraft var10000 = mc;
        Iterator var0 = Minecraft.player.inventory.armorInventory.iterator();

        while(var0.hasNext()) {
            ItemStack stack = (ItemStack)var0.next();
            if (stack.getItem() == Items.ELYTRA) {
                int slot = getSlowWithArmor() < 9 ? getSlowWithArmor() + 36 : getSlowWithArmor();
                if (getSlowWithArmor() != -1) {
                    Minecraft var10005 = mc;
                    mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, Minecraft.player);
                    var10005 = mc;
                    mc.playerController.windowClick(6, slot, 0, ClickType.PICKUP, Minecraft.player);
                }
            }
        }

    }

    public static int getAxe() {
        for(int i = 0; i < 9; ++i) {
            Minecraft var10000 = mc;
            ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemAxe) {
                return i;
            }
        }

        return 1;
    }

    public static boolean doesHotbarHaveBlock() {
        for(int i = 0; i < 9; ++i) {
            Minecraft var10000 = mc;
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                return true;
            }
        }

        return false;
    }

    public static int getTotemAtHotbar() {
        for(int i = 0; i < 45; ++i) {
            Minecraft var10000 = mc;
            ItemStack itemStack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
                return i < 9 ? i + 36 : i;
            }
        }

        return -1;
    }

    public static int getSwordAtHotbar() {
        for(int i = 0; i < 9; ++i) {
            Minecraft var10000 = mc;
            ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemSword) {
                return i;
            }
        }

        return 1;
    }
}
