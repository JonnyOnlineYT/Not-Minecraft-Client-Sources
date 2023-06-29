package org.moonware.client.cmd.impl;

import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.cmd.CommandAbstract;

public class HelpCommand extends CommandAbstract {

    public HelpCommand() {
        super("help", "help", ".help", "help");
    }

    @Override
    public void execute(String... args) {
        if (args.length == 1) {
            if (args[0].equals("help")) {
                MWUtils.sendChat(Formatting.RED + "All Commands:");
                MWUtils.sendChat(Formatting.DARK_AQUA + ".bind");
                MWUtils.sendChat(Formatting.DARK_AQUA + ".macro");
                MWUtils.sendChat(Formatting.DARK_AQUA + ".clip");
                MWUtils.sendChat(Formatting.DARK_AQUA + ".fakehack");
                MWUtils.sendChat(Formatting.DARK_AQUA + ".friend");
                MWUtils.sendChat(Formatting.DARK_AQUA + ".config");
                MWUtils.sendChat(Formatting.DARK_AQUA + ".clip");
                MWUtils.sendChat(Formatting.DARK_AQUA + ".xray");
            }
        } else {
            MWUtils.sendChat(getUsage());
        }
    }
}
