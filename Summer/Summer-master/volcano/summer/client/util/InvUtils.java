package volcano.summer.client.util;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public final class InvUtils {

    private InvUtils() {}

    public static int getDepthStriderLevel() {
        return EnchantmentHelper.func_180318_b(Wrapper.getPlayer());
    }

    /**
     * @param slotId The inventory slot you are clicking.
     *               Armor slots:
     *               Helmet is 5 and chest plate is 8
     *               First slot of inventory is 9 (top left)
     *               Last slot of inventory is 44 (bottom right)
     * @param mouseButtonClicked Hot bar slot
     * @param mode The type of click
     */
    public static void windowClick(int slotId, int mouseButtonClicked, ClickType mode) {
        Wrapper.getPlayerController().windowClick(
                Wrapper.getPlayer().inventoryContainer.windowId, slotId, mouseButtonClicked, mode.ordinal(), Wrapper.getPlayer());
    }

    public static double getDamageReduction(ItemStack stack) {
        double reduction = 0.0;

        if (stack != null && stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();

            reduction += armor.damageReduceAmount;
            reduction += EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack);
        }

        return reduction;
    }

    public static double getItemDamage(ItemStack stack) {
        double damage = 0.0;

        if (stack != null) {
            final Multimap<String, AttributeModifier> attributeModifierMap = stack.getAttributeModifiers();

            for (String attributeName : attributeModifierMap.keySet()) {
                if (attributeName.equals("generic.attackDamage")) {
                    Iterator<AttributeModifier> attributeModifiers = attributeModifierMap.get(attributeName).iterator();
                    if (attributeModifiers.hasNext())
                        damage += attributeModifiers.next().getAmount();
                }
            }

            damage += EnchantmentHelper.getFireAspectModifier(Wrapper.getPlayer());
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, Wrapper.getPlayer().getHeldItem()) * 1.25;
        }

        return damage;
    }

    public enum ClickType {
        // if mouseButtonClicked is 0 `DROP_ITEM` will drop 1
        // item from the stack else if it is 1 it will drop the entire stack
        CLICK, SHIFT_CLICK, SWAP_WITH_HOT_BAR_SLOT, DROP_ITEM
    }
}
