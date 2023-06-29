package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class ElytraSwap extends Feature {
    public static long delay;
    public ElytraSwap() {
        super("ElytraSwap", "Одевает/снимает элитру если она есть в инвернторе при нажатии на колесико мыши", Type.Other);
    }

    @EventTarget
    public void OnUpdate(EventUpdate event){
        if (MoonWare.featureManager.getFeatureByClass(ElytraSwap.class).getState()) {
            ItemStack stack = Minecraft.player.inventory.getItemStack();
            if (stack != null && stack.getItem() instanceof ItemArmor && System.currentTimeMillis() > delay) {
                ItemArmor ia = (ItemArmor) stack.getItem();
                if (ia.armorType == EntityEquipmentSlot.CHEST
                        && Minecraft.player.inventory.armorItemInSlot(2).getItem() == Items.ELYTRA) {
                    Minecraft.playerController.windowClick(0, 6, 1, ClickType.PICKUP, Minecraft.player);
                    int nullSlot = findNullSlot();
                    boolean needDrop = nullSlot == 999;
                    if (needDrop) {
                        nullSlot = 9;
                    }
                    Minecraft.playerController.windowClick(0, nullSlot, 1, ClickType.PICKUP, Minecraft.player);
                    if (needDrop) {
                        Minecraft.playerController.windowClick(0, -999, 1, ClickType.PICKUP, Minecraft.player);
                    }
                    delay = System.currentTimeMillis() + 300;
                    toggle();
                }
            }
        }
    }


    public static int findNullSlot() {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = Minecraft.player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemAir) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return 999;
    }
}
