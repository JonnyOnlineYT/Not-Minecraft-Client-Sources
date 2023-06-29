package ua.apraxia.cmd.impl;


import com.mojang.realmsclient.gui.ChatFormatting;
import ua.apraxia.cmd.CommandAbstract;
import ua.apraxia.utility.other.ChatUtility;

public class HelpCommand
        extends CommandAbstract {
    public HelpCommand() {
        super("help", "help", ".help", "help");
    }

    @Override
    public void execute(String ... args) {
        if (args.length == 1) {
            if (args[0].equals("help")) {
                ChatUtility.addChatMessage((Object)((Object)ChatFormatting.DARK_GRAY) + "---------------------");
                ChatUtility.addChatMessage((Object)((Object)ChatFormatting.WHITE) + ".gps");
                ChatUtility.addChatMessage((Object)((Object)ChatFormatting.WHITE) + ".vclip");
                ChatUtility.addChatMessage((Object)((Object)ChatFormatting.WHITE) + ".hclip");
                ChatUtility.addChatMessage((Object)((Object)ChatFormatting.WHITE) + ".eclip");
                ChatUtility.addChatMessage((Object)((Object)ChatFormatting.WHITE) + ".config");
                ChatUtility.addChatMessage((Object)((Object)ChatFormatting.DARK_GRAY) + "---------------------");
            }
        } else {
            ChatUtility.addChatMessage(this.getUsage());
        }
    }
}