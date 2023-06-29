package ru.rockstar.client.features.impl.movement;

import net.minecraft.init.MobEffects;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class WaterSpeed extends Feature {

    public static NumberSetting speed;
    private final BooleanSetting speedCheck;

    public WaterSpeed() {
        super("WaterSpeed", "Делает вас быстрее в воде",0, Category.PLAYER);
        speed = new NumberSetting("Speed Amount", 1, 0.1F, 4, 0.01F, () -> true);
        speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
        addSettings(speedCheck, speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!mc.player.isPotionActive(MobEffects.SPEED) && speedCheck.getBoolValue()) {
            return;
        }
        if (mc.player.isInLiquid()) {
            MovementHelper.setSpeed(speed.getNumberValue());
        }
    }
}
