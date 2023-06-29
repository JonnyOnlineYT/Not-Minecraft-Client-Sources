/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.Sys
 */
package ru.fluger.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.Sys;
import ru.fluger.client.Fluger;
import ru.fluger.client.cmd.CommandAbstract;
import ru.fluger.client.helpers.misc.ChatHelper;
import ru.fluger.client.settings.config.Config;
import ru.fluger.client.settings.config.ConfigManager;

public class ConfigCommand
extends CommandAbstract {
    public ConfigCommand() {
        super("cfg", "configurations", (Object)ChatFormatting.RED + ".cfg" + (Object)ChatFormatting.WHITE + " save <name> | load <name> | delete <name> | list | create <name> | dir" + (Object)ChatFormatting.RED, "<name>", "cfg");
    }

    @Override
    public void execute(String ... args) {
        try {
            if (args.length >= 2) {
                String upperCase = args[1].toUpperCase();
                if (args.length == 3) {
                    switch (upperCase) {
                        case "LOAD": {
                            if (Fluger.instance.configManager.loadConfig(args[2])) {
                                ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + "Successfully " + (Object)ChatFormatting.WHITE + "loaded config: " + (Object)ChatFormatting.RED + "\"" + args[2] + "\"");
                                break;
                            }
                            ChatHelper.addChatMessage((Object)ChatFormatting.RED + "Failed " + (Object)ChatFormatting.WHITE + "load config: " + (Object)ChatFormatting.RED + "\"" + args[2] + "\"");
                            break;
                        }
                        case "SAVE": {
                            if (Fluger.instance.configManager.saveConfig(args[2])) {
                                Fluger.instance.fileManager.saveFiles();
                                ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + "Successfully " + (Object)ChatFormatting.WHITE + "saved config: " + (Object)ChatFormatting.RED + "\"" + args[2] + "\"");
                                ConfigManager.getLoadedConfigs().clear();
                                Fluger.instance.configManager.load();
                                break;
                            }
                            ChatHelper.addChatMessage((Object)ChatFormatting.RED + "Failed " + (Object)ChatFormatting.WHITE + "to save config: " + (Object)ChatFormatting.RED + "\"" + args[2] + "\"");
                            break;
                        }
                        case "DELETE": {
                            if (Fluger.instance.configManager.deleteConfig(args[2])) {
                                ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + "Successfully " + (Object)ChatFormatting.WHITE + "deleted config: " + (Object)ChatFormatting.RED + "\"" + args[2] + "\"");
                                break;
                            }
                            ChatHelper.addChatMessage((Object)ChatFormatting.RED + "Failed " + (Object)ChatFormatting.WHITE + "to delete config: " + (Object)ChatFormatting.RED + "\"" + args[2] + "\"");
                            break;
                        }
                        case "CREATE": {
                            Fluger.instance.configManager.saveConfig(args[2]);
                            ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + "Successfully " + (Object)ChatFormatting.WHITE + "created config: " + (Object)ChatFormatting.RED + "\"" + args[2] + "\"");
                        }
                    }
                } else if (args.length == 2 && upperCase.equalsIgnoreCase("LIST")) {
                    ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + "Configs:");
                    for (Config config : Fluger.instance.configManager.getContents()) {
                        ChatHelper.addChatMessage((Object)ChatFormatting.RED + config.getName());
                    }
                } else if (args.length == 2 && upperCase.equalsIgnoreCase("DIR")) {
                    Sys.openURL((String)ConfigManager.cfg_dir.getAbsolutePath());
                }
            } else {
                ChatHelper.addChatMessage(this.getUsage());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

