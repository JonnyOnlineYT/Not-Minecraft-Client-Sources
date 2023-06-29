package ru.rockstar.client.features.impl.combat;


import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventRender2D;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class AutoShield extends Feature {
    public static NumberSetting health;

    public AutoShield() {
        super("AutoShield", "Автоматически берет в руку щит при опредленном здоровье",0, Category.COMBAT);
        health = new NumberSetting("Health", 10, 1, 20, 0.5F, () -> true);
        addSettings(health);
    }

    private int fountTotemCount() {
        int count = 0;
        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                count++;
            }
        }
        return count;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getHealth() > health.getNumberValue()) {
            return;
        }
        if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && InventoryHelper.getTotemAtHotbar() != -1) {
        	if (mc.player.getHeldItemOffhand().getItem() != Items.SHIELD && InventoryHelper.getShieldAtHotbar() != -1) {
                mc.playerController.windowClick(0, InventoryHelper.getShieldAtHotbar(), 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, mc.player);
            }
        }
    }
}
