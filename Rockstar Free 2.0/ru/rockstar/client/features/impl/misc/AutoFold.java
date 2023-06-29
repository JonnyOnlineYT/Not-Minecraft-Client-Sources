package ru.rockstar.client.features.impl.misc;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.player.ChestStealer;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class AutoFold extends Feature {

    public AutoFold() {
        super("AutoFold", "Автоматически складывает ресурсы в сундук", 0, Category.MISC);
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        if (mc.player.openContainer instanceof ContainerChest) {
            for (int index = 0; index < 90; ++index) {
                    ChestStealer.mc.playerController.windowClick(0,index, 0, ClickType.QUICK_MOVE, mc.player);
            }
        }
    }
    
}
