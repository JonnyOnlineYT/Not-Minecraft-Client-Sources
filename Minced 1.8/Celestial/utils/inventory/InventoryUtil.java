package Celestial.utils.inventory;

import Celestial.utils.Helper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketEntityAction;

public class InventoryUtil implements Helper {
    public static boolean doesHotbarHaveAxe() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe) {
                return true;
            }
        }
        return false;
    }
    public static boolean inventoryHasAir() {
        for (int index = 0; index < mc.player.inventory.getSizeInventory() - 1; index++) {
            if (mc.player.inventory.getStackInSlot(index).getItem() == Items.field_190931_a)
                return true;
        }
        return false;
    }
    public static float getProtection(ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot = (float)((double)prot + ((double)armor.damageReduceAmount + (double)((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), stack)) * 0.0075));
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(3), stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(1), stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(7), stack) / 100.0);
            prot = (float)((double)prot + (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(34), stack) / 50.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(4), stack) / 100.0);
        }
        return prot;
    }
    public static boolean isBestArmor(ItemStack stack, int type) {
        float prot = getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        } else if (type == 2) {
            strType = "chestplate";
        } else if (type == 3) {
            strType = "leggings";
        } else if (type == 4) {
            strType = "boots";
        }
        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            ItemStack is;
            if (!mc.player.inventoryContainer.getSlot(i).getHasStack() || !(getProtection(is = mc.player.inventoryContainer.getSlot(i).getStack()) > prot) || !is.getUnlocalizedName().contains(strType)) continue;
            return false;
        }
        return true;
    }
    public static void disabler(int elytra) {
        mc.playerController.windowClick(0, elytra, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, elytra, 0, ClickType.PICKUP, mc.player);
    }

    public static int getSlotWithElytra() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.ELYTRA) {
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static int getSlowWithArmor() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.DIAMOND_CHESTPLATE || itemStack.getItem() == Items.GOLDEN_CHESTPLATE || itemStack.getItem() == Items.LEATHER_CHESTPLATE || itemStack.getItem() == Items.CHAINMAIL_CHESTPLATE || itemStack.getItem() == Items.IRON_LEGGINGS) {
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }
    public static int getItemIndex(Item item){
        for(int i = 0; i < 45;i++){
            if(mc.player.inventory.getStackInSlot(i).getItem() == item){
                return i;
            }
        }
        return -1;
    }
    public static void swapElytraToChestplate() {
        for (ItemStack stack : mc.player.inventory.armorInventory) {
            if (stack.getItem() == Items.ELYTRA) {

                int slot = getSlowWithArmor() < 9 ? getSlowWithArmor() + 36 : getSlowWithArmor();
                if (getSlowWithArmor() != -1) {
                    mc.playerController.windowClick(0, slot, 1, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
                }
            }
        }
    }
    public static void swapChestPlateToElytra() {
        for (ItemStack stack : mc.player.inventory.armorInventory) {
            if (stack.getItem() == Items.DIAMOND_CHESTPLATE || stack.getItem() == Items.GOLDEN_CHESTPLATE || stack.getItem() == Items.LEATHER_CHESTPLATE || stack.getItem() == Items.CHAINMAIL_CHESTPLATE || stack.getItem() == Items.IRON_LEGGINGS) {
                int slot = getSlotWithElytra() < 9 ? getSlotWithElytra() + 36 : getSlotWithElytra();
                if (getSlotWithElytra() != -1) {
                    mc.playerController.windowClick(0, slot, 1, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
                }
            }
        }
    }

    public static boolean isActiveItemStackBlocking(EntityLivingBase base, int ticks) {
        if (base.isHandActive() && !base.getActiveItemStack().isEmpty()) {
            Item item = base.getActiveItemStack().getItem();

            if (item.getItemUseAction(base.getActiveItemStack()) != EnumAction.BLOCK) {
                return false;
            } else {
                return item.getMaxItemUseDuration(base.getActiveItemStack()) - base.activeItemStackUseCount >= ticks;
            }
        } else {
            return false;
        }
    }

    public static int getAxe() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemAxe) {
                return i;
            }
        }
        return 1;
    }

    public static boolean doesHotbarHaveBlock() {
        for (int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                return true;
            }
        }
        return false;
    }

    public static int getTotemAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.Totem) {
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static int getSwordAtHotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemSword) {
                return i;
            }
        }
        return 1;
    }
}