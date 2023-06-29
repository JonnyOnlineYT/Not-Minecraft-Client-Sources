package Celestial.cmd.impl;

import Celestial.cmd.CommandAbstract;
import com.mojang.realmsclient.gui.ChatFormatting;
import Celestial.Smertnix;
import Celestial.ui.config.Config;
import Celestial.ui.config.ConfigManager;
import Celestial.utils.other.ChatUtils;

public class ConfigCommand extends CommandAbstract {

    public ConfigCommand() {
        super("config", "configurations", ".config" + ChatFormatting.RED + " save | load | delete | list" + "<name>", "config");
    }

    @Override
    public void execute(String... args) {
        try {
            if (args.length >= 2) {
                String upperCase = args[1].toUpperCase();
                if (args.length == 3) {
                    switch (upperCase) {
                        case "LOAD":
                            if (Smertnix.instance.configManager.loadConfig(args[2])) {
                                ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            } else {
                                ChatUtils.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            }
                            break;
                        case "SAVE":
                            if (Smertnix.instance.configManager.saveConfig(args[2])) {
                                ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                ConfigManager.getLoadedConfigs().clear();
                                Smertnix.instance.configManager.load();
                            } else {
                                ChatUtils.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            }
                            break;
                        case "DELETE":
                            if (Smertnix.instance.configManager.deleteConfig(args[2])) {
                                ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            } else {
                                ChatUtils.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            }
                            break;
                    }
                } else if (args.length == 2 && upperCase.equalsIgnoreCase("LIST")) {
                    ChatUtils.addChatMessage(ChatFormatting.GREEN + "Configs:");
                    for (Config config : Smertnix.instance.configManager.getContents()) {
                        ChatUtils.addChatMessage(ChatFormatting.RED + config.getName());
                    }
                }
            } else {
                ChatUtils.addChatMessage(this.getUsage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}