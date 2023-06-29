package org.spray.heaven.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.command.Command;
import org.spray.heaven.features.command.CommandInfo;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.main.Wrapper;

@CommandInfo(name = "bind", desc = "Bind a module key")
public class BindCmd extends Command {

    @Override
    public void command(String[] args, String msg) {
        Module module = Wrapper.getModule().get(args[0]);
        if (module != null) {
            module.setKey(Keyboard.getKeyIndex(args[1].toUpperCase()));
            send(ChatFormatting.LIGHT_PURPLE + module.getName() + ChatFormatting.WHITE + " keybind has changed to: "
                    + ChatFormatting.GREEN + Keyboard.getKeyName(module.getKey()));
            return;
        }

        if ("del".equals(args[0])) {
            module = Wrapper.getModule().get(args[1]);
            module.setKey(Keyboard.KEY_NONE);
            send("Keybind removed from " + ChatFormatting.LIGHT_PURPLE + module.getName());
        }
    }

    @Override
    public void error() {
        send("Error, use:");
        send(Command.PREFIX + "bind " + ChatFormatting.GREEN + "module " + ChatFormatting.GRAY + "key");
        send(Command.PREFIX + "bind del " + ChatFormatting.GREEN + "module");
    }

}
