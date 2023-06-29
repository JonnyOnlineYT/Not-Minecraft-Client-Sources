package Celestial.module.impl.Combat;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.ui.settings.impl.NumberSetting;

public class HitBox extends Module {

    public static NumberSetting hitboxSize = new NumberSetting("HitBox Size", "Размер хитбокса игрока", 0.25f, 0.1f, 2.f, 0.1f, () -> true);

    public HitBox() {
        super("HitBox", "Увеличивает хитбокс игрока.", ModuleCategory.Combat);
        addSettings(hitboxSize);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
    }
}
