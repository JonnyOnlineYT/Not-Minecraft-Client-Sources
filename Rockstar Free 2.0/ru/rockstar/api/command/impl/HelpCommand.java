package ru.rockstar.api.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;

public class HelpCommand extends CommandAbstract {

    public HelpCommand() {
        super("help", "help", ".help", "help");
    }

    @Override
    public void execute(String... args) {
        if (args.length == 1) {
            if (args[0].equals("help")) {
                Main.msg(ChatFormatting.RED + "All Commands:",true);
                Main.msg(ChatFormatting.GRAY + ".bind",true);
                Main.msg(ChatFormatting.GRAY + ".macro",true);
                Main.msg(ChatFormatting.GRAY + ".friend",true);
                Main.msg(ChatFormatting.GRAY + ".config",true);
                Main.msg(ChatFormatting.GRAY + ".clip",true);
            }
        } else {
            Main.msg(this.getUsage(),true);
        }
    }
}
