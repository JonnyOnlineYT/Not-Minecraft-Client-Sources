package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class timer extends Feature {

    public static NumberSetting timer;
    public static BooleanSetting turnofftime;

    public timer() {
        super("Timer", "Увеличивает скорость игры", Type.Movement);
        timer = new NumberSetting("Timer", 2.0F, 0.1F, 10.0F, 0.1F, () -> true);
        turnofftime = new BooleanSetting("turn if cd is over","выключает таймер если кулдаун таймера до флага почти закончился", true, () -> true);
        addSettings(timer, turnofftime);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!getState())
            return;
        setSuffix("" + timer.getNumberValue());
        if (!(turnofftime.getBoolValue())) {
            Minecraft.timer.timerSpeed = timer.getNumberValue();
        }else {
            if (HUD.diap >= 7) {
                Minecraft.timer.timerSpeed = timer.getNumberValue();
            }else {
                Minecraft.timer.timerSpeed = 1.0F;
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.timer.timerSpeed = 1.0F;
    }
}
