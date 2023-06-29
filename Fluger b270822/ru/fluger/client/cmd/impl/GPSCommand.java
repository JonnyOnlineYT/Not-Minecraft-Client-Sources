/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.cmd.impl;

import ru.fluger.client.cmd.CommandAbstract;
import ru.fluger.client.helpers.misc.ChatHelper;

public class GPSCommand
extends CommandAbstract {
    public static int x;
    public static int z;
    public static String mode;

    public GPSCommand() {
        super("gps", "gps coommand", "\u00a7bUsage: \u00a76.gps <x> <z> <off/on>", "gps");
    }

    @Override
    public void execute(String ... args) {
        if (args.length < 4) {
            ChatHelper.addChatMessage(this.getUsage());
        } else {
            mode = args[3].toLowerCase();
            if (mode.equalsIgnoreCase("on")) {
                x = Integer.parseInt(args[1]);
                z = Integer.parseInt(args[2]);
            } else if (mode.equalsIgnoreCase("off")) {
                x = 0;
                z = 0;
            }
        }
    }
}

