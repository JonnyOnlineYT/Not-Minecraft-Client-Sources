package ua.apraxia.modules.impl.player;

import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.utility.Utility;

public class NoJumpDelay extends Module {
    public NoJumpDelay() {
        super("NoJumpDelay", Categories.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
       Utility.mc.player.setJumpTicks(0);
    }
}
