package Celestial.module.impl.Movement;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.movement.MovementUtils;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.init.MobEffects;

public class WaterSpeed extends Module {

    private final NumberSetting speed;
    private final BooleanSetting speedCheck;

    public WaterSpeed() {
        super("WaterSpeed", "Позволяет быстро бегать в воде" , ModuleCategory.Movement);
        speed = new NumberSetting("Speed Amount", 0.4f, 0.1F, 4, 0.01F, () -> true);
        speedCheck = new BooleanSetting("Potion Check", false, () -> true);
        addSettings(speedCheck, speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!Helper.mc.player.isPotionActive(MobEffects.SPEED) && speedCheck.getCurrentValue()) {
            return;
        }
        if (Helper.mc.player.isInWater() || Helper.mc.player.isInLava()) {
            MovementUtils.setSpeed(speed.getCurrentValue());
        }
    }
}
