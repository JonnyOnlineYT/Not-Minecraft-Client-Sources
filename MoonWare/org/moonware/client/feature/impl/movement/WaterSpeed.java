package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class WaterSpeed extends Feature {

    public static NumberSetting speed;
    private final BooleanSetting speedCheck;

    public WaterSpeed() {
        super("WaterSpeed", "Делает вас быстрее в воде", Type.Movement);
        speed = new NumberSetting("Speed Amount", 1, 0.1F, 4, 0.01F, () -> true);
        speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
        addSettings(speedCheck, speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!Minecraft.player.isPotionActive(MobEffects.SPEED) && speedCheck.getBoolValue()) {
            return;
        }
        if (Minecraft.player.isInLiquid()) {
            MovementHelper.setSpeed(speed.getNumberValue());
        }
    }
}
