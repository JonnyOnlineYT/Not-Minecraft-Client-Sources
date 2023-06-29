package Celestial.module.impl.Player;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;

public class NoJumpDelay extends Module {
    public NoJumpDelay() {
        super("NoJumpDelay", "Нету задержки на прыжок", ModuleCategory.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        Helper.mc.player.setJumpTicks(0);
    }
}
