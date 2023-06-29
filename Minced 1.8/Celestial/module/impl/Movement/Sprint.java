package Celestial.module.impl.Movement;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.movement.MovementUtils;

public class Sprint extends Module {
    public Sprint() {
        super("AutoSprint", ModuleCategory.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (Helper.mc.player.getFoodStats().getFoodLevel() / 2 > 3) {
            Helper.mc.player.setSprinting(MovementUtils.isMoving());
        }
    }

    @Override
    public void onDisable() {
        Helper.mc.player.setSprinting(false);
        super.onDisable();
    }
}
