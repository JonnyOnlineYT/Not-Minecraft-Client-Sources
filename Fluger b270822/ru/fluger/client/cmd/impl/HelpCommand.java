/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package ru.fluger.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.cmd.CommandAbstract;
import ru.fluger.client.helpers.misc.ChatHelper;

public class HelpCommand
extends CommandAbstract {
    public HelpCommand() {
        super("help", "help", ".help", "help");
    }

    @Override
    public void execute(String ... args) {
        if (args.length == 1) {
            if (args[0].equals("help")) {
                ChatHelper.addChatMessage((Object)ChatFormatting.RED + "All Commands:");
                ChatHelper.addChatMessage((Object)ChatFormatting.WHITE + ".bind");
                ChatHelper.addChatMessage((Object)ChatFormatting.WHITE + ".macro");
                ChatHelper.addChatMessage((Object)ChatFormatting.WHITE + ".vclip | .hclip");
                ChatHelper.addChatMessage((Object)ChatFormatting.WHITE + ".friend");
                ChatHelper.addChatMessage((Object)ChatFormatting.WHITE + ".panic");
                ChatHelper.addChatMessage((Object)ChatFormatting.WHITE + ".cfg");
            }
        } else {
            ChatHelper.addChatMessage(this.getUsage());
        }
    }
}

