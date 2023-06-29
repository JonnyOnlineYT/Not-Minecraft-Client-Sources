package ru.rockstar.api.utils.world;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.api.utils.Helper;

import java.util.Objects;

public class InventoryHelper{
    private static final Minecraft mc = Minecraft.getMinecraft();
    static TimerHelper timer = new TimerHelper();

    public static int getItemCount(Container container, Item item) {
        int itemCount = 0;
        for (int i = 0; i < 45; ++i) {
            ItemStack is;
            if (!container.getSlot(i).getHasStack() || (is = container.getSlot(i).getStack()).getItem() != item) continue;
            itemCount += is.getMaxStackSize();
        }
        return itemCount;
    }
    public static boolean isArmorPlayer(Entity entity) {
        for (ItemStack stack : entity.getArmorInventoryList()) {
            if (stack.func_190926_b())
                return false;
        }
        return true;
    }
    public static int getTotemAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
                return i;
            }
        }
        return -1;
    }
    public static int getElytraAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.ELYTRA) {
                return i;
            }
        }
        return -1;
    }
    public static int getItemAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.GOLDEN_APPLE) {
                return i;
            }
        }
        return -1;
    }
    public static int getShieldAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.SHIELD) {
                return i;
            }
        }
        return -1;
    }
    public static boolean doesHotbarHaveAxe() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe) {
                return true;
            }
        }
        return false;
    }
    public static boolean doesHotbarHaveCrystal() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemEndCrystal) {
                return true;
            }
        }
        return false;
    }
    public static void attackEntity(Entity entity, boolean packet, boolean swingArm) {
        if (packet) {
            mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
        } else {
            mc.playerController.attackEntity((EntityPlayer)mc.player, entity);
        }
        if (swingArm)
            mc.player.swingArm(EnumHand.MAIN_HAND);
    }
    public static boolean hasAnyBlock() {
        for (int i = 0; i < 9; ++i) {
            Minecraft.getMinecraft();
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock) {
                return true;
            }
        }
        return false;
    }

    public static boolean isShiftable(ItemStack preferedItem) {
        if (preferedItem == null) {
            return true;
        }
        for (int o = 36; o < 45; ++o) {
            if (mc.player.inventoryContainer.getSlot(o).getHasStack()) {
                ItemStack item = mc.player.inventoryContainer.getSlot(o).getStack();
                if (item == null) {
                    return true;
                }
                if (Item.getIdFromItem(item.getItem()) != Item.getIdFromItem(preferedItem.getItem()) || item.getMaxStackSize() + preferedItem.getMaxStackSize() > preferedItem.getMaxStackSize()) continue;
                return true;
            }
            return true;
        }
        return false;
    }


    public static boolean isPotion(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemSplashPotion) {
            for (PotionEffect effect : PotionUtils.getEffectsFromStack(itemStack)) {
                Potion potion = effect.getPotion();
                effect.getPotion();
                if (potion != Potion.getPotionById(5)) continue;
                return true;
            }
        }
        return false;
    }

    public static void usePotion() {
        EnumHand hand = EnumHand.MAIN_HAND;
        ItemStack item = mc.player.getHeldItem(EnumHand.MAIN_HAND);
        if (item != null) {
            if (InventoryHelper.mc.playerController.processRightClick(mc.player, InventoryHelper.mc.world, hand) == EnumActionResult.SUCCESS) {
                // empty if block
            }
        }
    }

    public static boolean isIntercepted(BlockPos pos) {
        for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            if (!new AxisAlignedBB(pos).intersectsWith(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
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

    public static int getAxe() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemAxe) {
                return i;
            }
        }
        return 1;
    }
    
    public static int getEnderPearl() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemEnderPearl) {
                return i;
            }
        }
        return 1;
    }

    public static int getAnyBlockInHotbar() {
        for (int i = 0; i < 9; ++i) {
            Minecraft.getMinecraft();
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (!(item instanceof ItemBlock)) continue;
            return i;
        }
        return -1;
    }


    public static int getArmorItemsEquipSlot(ItemStack stack, boolean equipmentSlot) {
        if (stack.getUnlocalizedName().contains("helmet")) {
            return equipmentSlot ? 4 : 5;
        }
        if (stack.getUnlocalizedName().contains("chestplate")) {
            return equipmentSlot ? 3 : 6;
        }
        if (stack.getUnlocalizedName().contains("leggings")) {
            return equipmentSlot ? 2 : 7;
        }
        if (stack.getUnlocalizedName().contains("boots")) {
            return equipmentSlot ? 1 : 8;
        }
        return -1;
    }






    public static boolean doesHotbarHaveBlock() {
        for (int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                return true;
            }
        }
        return false;
    }


    public static int getSwordH() {
        for (int i = 1; i < 9; ++i) {
            final ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemSword) {
                return i;
            }
        }
        return 0;
    }


    public static int getSwordSlot() {
        int bestSword = -1;
        float bestDamage = 1.0f;
        for (int i = 9; i < 45; ++i) {
            if (!mc.player.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack item = mc.player.inventoryContainer.getSlot(i).getStack();
            if (item == null || !(item.getItem() instanceof ItemSword)) continue;
            ItemSword is = (ItemSword)item.getItem();
            float damage = is.getDamageVsEntity();
            if (!((damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(16), item) * 1.26f + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(20), item) * 0.01f) > bestDamage)) continue;
            bestDamage = damage;
            bestSword = i;
        }
        return bestSword;
    }



    public static int getItemSlot(Container container, Item item) {
        int slot = 0;
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!container.getSlot(i).getHasStack() || (is = container.getSlot(i).getStack()).getItem() != item)
                continue;
            slot = i;
        }
        return slot;
    }
    public static void swap(int slot, int hotbarNum) {
        InventoryHelper.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        InventoryHelper.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, hotbarNum, 0, ClickType.PICKUP, mc.player);
        InventoryHelper.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        InventoryHelper.mc.playerController.updateController();
    }

    public static boolean isBestArmor(ItemStack stack, int type) {
        float prot = InventoryHelper.getProtection(stack);
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
            if (!mc.player.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
            if (!(InventoryHelper.getProtection(is) > prot) || !is.getUnlocalizedName().contains(strType)) continue;
            return false;
        }
        return true;
    }

    public static float getProtection(ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot = (float)((double)prot + ((double)armor.damageReduceAmount + (double)((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), stack)) * 0.0075));
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(3), stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(1), stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(7), stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(34), stack) / 50.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(4), stack) / 100.0);
        }
        return prot;
    }

    public static double getProtectionValue(ItemStack stack) {
        return !(stack.getItem() instanceof ItemArmor) ? 0.0 : (double)((ItemArmor)stack.getItem()).damageReduceAmount + (double)((100 - ((ItemArmor)stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), stack) * 4) * 0.0075;
    }
}
