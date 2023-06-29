package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;

public class NoDelay extends Feature {

    public BooleanSetting rightClickDelay = new BooleanSetting("NoRightClickDelay", true, () -> true);
    public BooleanSetting leftClickDelay = new BooleanSetting("NoLeftClickDelay", false, () -> true);
    public BooleanSetting jumpDelay = new BooleanSetting("NoJumpDelay", true, () -> true);
    public BooleanSetting blockHitDelay = new BooleanSetting("NoBlockHitDelay", false, () -> true);

    public NoDelay() {
        super("NoDelay", "Убирает задержку", Type.Other);
        addSettings(rightClickDelay, leftClickDelay, jumpDelay, blockHitDelay);
    }


    @Override
    public void onDisable() {
        Minecraft.rightClickDelayTimer = 4;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        if (!getState())
            return;

        if (rightClickDelay.getBoolValue()) {
            Minecraft.rightClickDelayTimer = 0;
        }

        if (leftClickDelay.getBoolValue()) {
            Minecraft.leftClickCounter = 0;
        }

        if (jumpDelay.getBoolValue()) {
            Minecraft.player.jumpTicks = 0;
        }

        if (blockHitDelay.getBoolValue()) {
            Minecraft.playerController.blockHitDelay = 0;
        }

    }

}
