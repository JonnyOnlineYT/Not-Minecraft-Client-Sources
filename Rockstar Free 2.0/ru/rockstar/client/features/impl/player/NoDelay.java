package ru.rockstar.client.features.impl.player;

import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;

public class NoDelay extends Feature {
    public BooleanSetting rightClickDelay = new BooleanSetting("NoRightClickDelay", true, () -> true);
    public BooleanSetting jumpDelay = new BooleanSetting("NoJumpDelay", true, () -> true);

    public NoDelay() {
        super("NoDelay", "Убирает задержку", 0, Category.PLAYER);
        addSettings(rightClickDelay, jumpDelay);
    }


    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 4;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        if (!isToggled())
            return;

        if (rightClickDelay.getBoolValue()) {
            mc.rightClickDelayTimer = 0;
        }

        if (jumpDelay.getBoolValue()) {
            mc.player.jumpTicks = 0;
        }

    }

}
