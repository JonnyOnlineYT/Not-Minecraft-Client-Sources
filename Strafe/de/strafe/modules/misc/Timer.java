package de.strafe.modules.misc;

import com.eventapi.EventTarget;
import de.strafe.events.EventUpdate;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.settings.impl.NumberSetting;
import de.strafe.utils.IMinecraft;

public class Timer extends Module {

    public NumberSetting timer = new NumberSetting("Speed", 1, 0.2, 10, 0.2);

    public Timer() {
        super("Timer", 0, Category.MISC);
        addSettings(timer);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        IMinecraft.mc.timer.timerSpeed = (float) timer.getValue();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        IMinecraft.mc.timer.timerSpeed = 1F;
    }
}
