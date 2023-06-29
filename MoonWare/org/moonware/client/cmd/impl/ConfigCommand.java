package org.moonware.client.cmd.impl;

import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.cmd.CommandAbstract;
import org.moonware.client.settings.config.Config;
import org.moonware.client.settings.config.ConfigManager;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

public class ConfigCommand extends CommandAbstract {

    public ConfigCommand() {
        super("config", "configurations", "§6.config" + Formatting.LIGHT_PURPLE + " save | load | delete " + "§3<name>", "config");
    }

    @Override
    public void execute(String... args) {
        try {
            if (args.length >= 2) {
                String upperCase = args[1].toUpperCase();
                if (args.length == 3) {
                    switch (upperCase) {
                        case "LOAD":
                            if (MoonWare.configManager.loadConfig(args[2])) {
                                MWUtils.sendChat(Formatting.GREEN + "Successfully " + Formatting.WHITE + "loaded config: " + Formatting.RED + "\"" + args[2] + "\"");
                                NotificationManager.publicity("Config", Formatting.GREEN + "Successfully " + Formatting.WHITE + "loaded config: " + Formatting.RED + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
                            } else {
                                MWUtils.sendChat(Formatting.RED + "Failed " + Formatting.WHITE + "load config: " + Formatting.RED + "\"" + args[2] + "\"");
                                NotificationManager.publicity("Config", Formatting.RED + "Failed " + Formatting.WHITE + "load config: " + Formatting.RED + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
                            }
                            break;
                        case "SAVE":
                            if (MoonWare.configManager.saveConfig(args[2])) {
                                MWUtils.sendChat(Formatting.GREEN + "Successfully " + Formatting.WHITE + "saved config: " + Formatting.RED + "\"" + args[2] + "\"");
                                NotificationManager.publicity("Config", Formatting.GREEN + "Successfully " + Formatting.WHITE + "saved config: " + Formatting.RED + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
                                ConfigManager.getLoadedConfigs().clear();
                                MoonWare.configManager.load();
                            } else {
                                MWUtils.sendChat(Formatting.RED + "Failed " + Formatting.WHITE + "to save config: " + Formatting.RED + "\"" + args[2] + "\"");
                                NotificationManager.publicity("Config", Formatting.RED + "Failed " + Formatting.WHITE + "to save config: " + Formatting.RED + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
                            }
                            break;
                        case "DELETE":
                            if (MoonWare.configManager.deleteConfig(args[2])) {
                                MWUtils.sendChat(Formatting.GREEN + "Successfully " + Formatting.WHITE + "deleted config: " + Formatting.RED + "\"" + args[2] + "\"");
                                NotificationManager.publicity("Config", Formatting.GREEN + "Successfully " + Formatting.WHITE + "deleted config: " + Formatting.RED + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
                            } else {
                                MWUtils.sendChat(Formatting.RED + "Failed " + Formatting.WHITE + "to delete config: " + Formatting.RED + "\"" + args[2] + "\"");
                                NotificationManager.publicity("Config", Formatting.RED + "Failed " + Formatting.WHITE + "to delete config: " + Formatting.RED + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
                            }
                            break;
                    }
                } else if (args.length == 2 && upperCase.equalsIgnoreCase("LIST")) {
                    MWUtils.sendChat(Formatting.GREEN + "Configs:");
                    for (Config config : MoonWare.configManager.getContents()) {
                        MWUtils.sendChat(Formatting.RED + config.getName());
                    }
                }
            } else {
                MWUtils.sendChat(getUsage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}