package Celestial.module.impl.Combat;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.NumberSetting;

public class KeepSprint extends Module {

    public static NumberSetting speed;
    public static BooleanSetting setSprinting;

    public KeepSprint() {
        super("KeepSprint", "Регулировка скорости пред ударом", ModuleCategory.Combat);
        speed = new NumberSetting("Keep Speed", 1, 0.5F, 2, 0.01F, () -> true);
        setSprinting = new BooleanSetting("Set Sprinting", true, () -> true);
        addSettings(setSprinting, speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    }
}