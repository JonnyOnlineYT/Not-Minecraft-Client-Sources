package de.strafe.modules.misc;

import com.eventapi.EventTarget;
import de.strafe.Strafe;
import de.strafe.events.EventUpdate;
import de.strafe.modules.Category;
import de.strafe.modules.Module;

public class Crasher extends Module {

    public Crasher() {
        super("Crasher", 0, Category.MISC);
    }
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {

    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
