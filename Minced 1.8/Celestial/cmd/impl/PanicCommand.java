package Celestial.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import Celestial.Smertnix;
import Celestial.cmd.CommandAbstract;
import Celestial.module.Module;
import Celestial.utils.other.ChatUtils;

public class PanicCommand
        extends CommandAbstract {
    public PanicCommand() {
        super("panic", "Disabled all modules", ".panic", "panic");
    }

    @Override
    public void execute(String ... args) {
        if (args[0].equalsIgnoreCase("panic")) {
            for (Module feature : Smertnix.instance.featureManager.getAllFeatures()) {
                if (!feature.isEnabled()) continue;
                feature.toggle();
            }
            ChatUtils.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "\u0423\u0441\u043f\u0435\u0448\u043d\u043e " + (Object)((Object)ChatFormatting.RED) + "\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d\u043d\u044b " + (Object)((Object)ChatFormatting.WHITE) + "\u0432\u0441\u0435 \u043c\u043e\u0434\u0443\u043b\u0438!");
        }
    }
}

