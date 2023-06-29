package summer.cheat.cheats.player;

import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventUpdate;

public class FastUse extends Cheats {

    public FastUse() {
        super("FastUse", "", Selection.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    }
}