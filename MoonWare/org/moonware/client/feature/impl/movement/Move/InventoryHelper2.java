package org.moonware.client.feature.impl.movement.Move;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketEntityAction;
import org.moonware.client.helpers.Helper;

public class InventoryHelper2
        implements Helper {
    public static boolean doesHotbarHaveAxe() {
        for (int i = 0; i < 9; ++i) {
            InventoryHelper2.mc.player.inventory.getStackInSlot(i);
            if (!(InventoryHelper2.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe)) continue;
            return true;
        }
        return false;
    }

    public static void disabler(int elytra) {
        InventoryHelper2.mc.playerController.windowClick(0, elytra, 0, ClickType.PICKUP, InventoryHelper2.mc.player);
        InventoryHelper2.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, InventoryHelper2.mc.player);
        InventoryHelper2.mc.player.connection.sendPacket(new CPacketEntityAction(InventoryHelper2.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        InventoryHelper2.mc.player.connection.sendPacket(new CPacketEntityAction(InventoryHelper2.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        InventoryHelper2.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, InventoryHelper2.mc.player);
        InventoryHelper2.mc.playerController.windowClick(0, elytra, 0, ClickType.PICKUP, InventoryHelper2.mc.player);
    }

    public static int getSlotWithElytra() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = InventoryHelper2.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() != Items.ELYTRA) continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    public static int getSlowWithArmor() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = InventoryHelper2.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() != Items.DIAMOND_CHESTPLATE && itemStack.getItem() != Items.GOLDEN_CHESTPLATE && itemStack.getItem() != Items.LEATHER_CHESTPLATE && itemStack.getItem() != Items.CHAINMAIL_CHESTPLATE && itemStack.getItem() != Items.IRON_LEGGINGS) continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    public static void swapElytraToChestplate() {
        for (ItemStack stack : InventoryHelper2.mc.player.inventory.armorInventory) {
            int slot;
            if (stack.getItem() != Items.ELYTRA) continue;
            int n = slot = InventoryHelper2.getSlowWithArmor() < 9 ? InventoryHelper2.getSlowWithArmor() + 36 : InventoryHelper2.getSlowWithArmor();
            if (InventoryHelper2.getSlowWithArmor() == -1) continue;
            InventoryHelper2.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, InventoryHelper2.mc.player);
            InventoryHelper2.mc.playerController.windowClick(6, slot, 0, ClickType.PICKUP, InventoryHelper2.mc.player);
        }
    }

    public static boolean isActiveItemStackBlocking(EntityLivingBase base, int ticks) {
        if (base.isHandActive() && !base.getActiveItemStack().isOnItemFrame()) {
            Item item = base.getActiveItemStack().getItem();
            if (item.getItemUseAction(base.getActiveItemStack()) != EnumAction.BLOCK) {
                return false;
            }
            return item.getMaxItemUseDuration(base.getActiveItemStack()) - base.activeItemStackUseCount >= ticks;
        }
        return false;
    }

    public static int getAxe() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = InventoryHelper2.mc.player.inventory.getStackInSlot(i);
            if (!(itemStack.getItem() instanceof ItemAxe)) continue;
            return i;
        }
        return 1;
    }

    public static boolean doesHotbarHaveBlock() {
        for (int i = 0; i < 9; ++i) {
            if (!(InventoryHelper2.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock)) continue;
            return true;
        }
        return false;
    }

    public static int getTotemAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = InventoryHelper2.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() != Items.TOTEM_OF_UNDYING) continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    public static int getSwordAtHotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = InventoryHelper2.mc.player.inventory.getStackInSlot(i);
            if (!(itemStack.getItem() instanceof ItemSword)) continue;
            return i;
        }
        return 1;
    }
}
