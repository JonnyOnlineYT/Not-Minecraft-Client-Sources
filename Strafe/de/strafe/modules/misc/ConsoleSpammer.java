package de.strafe.modules.misc;

import com.eventapi.EventTarget;
import de.strafe.events.EventUpdate;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.settings.impl.NumberSetting;
import de.strafe.utils.IMinecraft;
import de.strafe.utils.TimeUtil;

public class ConsoleSpammer extends Module {
    public TimeUtil time = new TimeUtil();
    public NumberSetting spammer = new NumberSetting("Spammer delay", 200, 100, 1000, 100);

    public ConsoleSpammer() {
        super("Console Spammer", 0, Category.MISC);
        addSettings(spammer);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (time.hasReached((long) (spammer.value * 15))) {
            IMinecraft.mc.thePlayer.sendChatMessage("/pl");
            IMinecraft.mc.thePlayer.sendChatMessage("/gm");
            IMinecraft.mc.thePlayer.sendChatMessage("/p");
            IMinecraft.mc.thePlayer.sendChatMessage("/tp");
            IMinecraft.mc.thePlayer.sendChatMessage("/tpa");
            IMinecraft.mc.thePlayer.sendChatMessage("/bukkit:pl");
            IMinecraft.mc.thePlayer.sendChatMessage("/tps");
            IMinecraft.mc.thePlayer.sendChatMessage("/gamemode");
            IMinecraft.mc.thePlayer.sendChatMessage("/strafe");
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
