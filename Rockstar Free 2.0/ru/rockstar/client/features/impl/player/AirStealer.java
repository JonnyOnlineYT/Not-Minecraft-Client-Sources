package ru.rockstar.client.features.impl.player;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class AirStealer extends Feature {

    private final NumberSetting delay;
    public static BooleanSetting autoDisable;
    public TimerHelper timer = new TimerHelper();

    public AirStealer() {
        super("AirStealer", "Позволяет быстро лутать аирдроп",0, Category.PLAYER);
        delay = new NumberSetting("Stealer Speed", 10, 0, 100, 1, () -> true);
        autoDisable = new BooleanSetting("AutoDisable", "Автоматически выключает функцию", true, () -> true);
        addSettings(autoDisable);
    }
    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        this.setSuffix("" + (int) delay.getNumberValue(), true);

        float delay = 0;

        if (mc.player.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest) mc.player.openContainer;
            for (int index = 0; index < container.inventorySlots.size(); ++index) {
                if (container.getLowerChestInventory().getStackInSlot(index).getItem() != Item.getItemById(0) && timer.hasReached((delay))) {
                    AirStealer.mc.playerController.windowClick(container.windowId, index, 0, ClickType.QUICK_MOVE, mc.player);
                    timer.reset();
                    continue;
                }
                if (autoDisable.getBoolValue()) {
                	mc.player.closeScreen();
                	this.onDisable();
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
