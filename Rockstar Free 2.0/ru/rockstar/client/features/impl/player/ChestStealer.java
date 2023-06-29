package ru.rockstar.client.features.impl.player;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class ChestStealer extends Feature {

    private final NumberSetting delay;
    public static BooleanSetting autoDisable;
    public TimerHelper timer = new TimerHelper();

    public ChestStealer() {
        super("ChestStealer", "Забирает содержимое сундука ",0, Category.PLAYER);
        delay = new NumberSetting("Stealer Speed", 10, 0, 100, 1, () -> true);
        autoDisable = new BooleanSetting("AutoDisable", "Автоматически выключает функцию", true, () -> true);
        addSettings(delay, autoDisable);
    }
    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        this.setSuffix("" + (int) delay.getNumberValue(), true);

        float delay = this.delay.getNumberValue() * 10;

        if (mc.player.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest) mc.player.openContainer;
            for (int index = 0; index < container.inventorySlots.size(); ++index) {
                if (container.getLowerChestInventory().getStackInSlot(index).getItem() != Item.getItemById(0) && timer.hasReached((delay))) {
                    ChestStealer.mc.playerController.windowClick(container.windowId, index, 0, ClickType.QUICK_MOVE, mc.player);
                    timer.reset();
                    continue;
                }
                if (!isEmpty(container))
                    continue;
                mc.player.closeScreen();
                if (autoDisable.getBoolValue()) {
                	super.onDisable();
                }
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
