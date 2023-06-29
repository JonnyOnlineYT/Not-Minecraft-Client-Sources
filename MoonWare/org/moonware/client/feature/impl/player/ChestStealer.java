package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.settings.impl.NumberSetting;

public class ChestStealer extends Feature {

    private final NumberSetting delay;
    public TimerHelper timer = new TimerHelper();

    public ChestStealer() {
        super("ChestStealer", "Автоматически забирает вещи из сундуков", Type.Other);
        delay = new NumberSetting("Stealer Speed", 10, 0, 100, 1, () -> true);
        addSettings(delay);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        setSuffix("" + (int) delay.getNumberValue());

        float delay = this.delay.getNumberValue() * 10;

        if (Minecraft.player.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest) Minecraft.player.openContainer;
            for (int index = 0; index < container.inventorySlots.size(); ++index) {
                if (container.getLowerChestInventory().getStackInSlot(index).getItem() != Item.getItemById(0) && timer.hasReached((delay))) {
                    Minecraft.playerController.windowClick(container.windowId, index, 0, ClickType.QUICK_MOVE, Minecraft.player);
                    timer.reset();
                    continue;
                }
                if (!isEmpty(container))
                    continue;
                Minecraft.player.closeScreen();
            }
        }
    }

    public boolean isWhiteItem(ItemStack itemStack) {
        return (itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemEnderPearl || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemBlock || itemStack.getItem() instanceof ItemArrow || itemStack.getItem() instanceof ItemCompass);
    }

    private boolean isEmpty(Container container) {
        for (int index = 0; index < container.inventorySlots.size(); index++) {
            if (isWhiteItem(container.getSlot(index).getStack()))
                return false;
        }
        return true;
    }
}