package de.strafe.modules.misc;

import com.eventapi.EventTarget;
import de.strafe.events.EventUpdate;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.settings.impl.NumberSetting;
import de.strafe.utils.IMinecraft;
import de.strafe.utils.TimeUtil;

public class Spammer extends Module {
    public TimeUtil time = new TimeUtil();
    public NumberSetting spammer = new NumberSetting("Spammer delay", 1, 1, 15, 1);
    public Spammer() {
        super("Spammer", 0, Category.MISC);
        addSettings(spammer);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (time.hasReached((long) (spammer.value * 10))) {
            IMinecraft.mc.thePlayer.sendChatMessage("Strafe on Top");
            time.reset();
        }
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
