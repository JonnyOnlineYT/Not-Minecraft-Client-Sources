package ua.apraxia.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.apache.commons.codec.binary.Hex;
import ua.apraxia.Hexbyte;
import ua.apraxia.cmd.CommandAbstract;
import ua.apraxia.modules.settings.config.Config;
import ua.apraxia.modules.settings.config.ConfigManager;
import ua.apraxia.notification.NotificationMode;
import ua.apraxia.notification.NotificationRenderer;
import ua.apraxia.utility.other.ChatUtility;

public class ConfigCommand extends CommandAbstract {

    public ConfigCommand() {
        super("config", "configurations", "config | save | load | delete " + "<name>", "config");
    }

    @Override
    public void execute(String... args) {
        try {
            if (args.length >= 2) {
                String upperCase = args[1].toUpperCase();
                if (args.length == 3) {
                    switch (upperCase) {
                        case "LOAD":
                            if (Hexbyte.getInstance().configManager.loadConfig(args[2])) {
                                ChatUtility.addChatMessage(ChatFormatting.GRAY + "Успешно загружен конфиг " + ChatFormatting.AQUA + "\"" + args[2] + "\"");
                               // NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
                            } else {
                                ChatUtility.addChatMessage(ChatFormatting.GRAY + "Не удалось загрузить конфиг " + ChatFormatting.AQUA + "\"" + args[2] + "\"");
                                //NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
                            }
                            break;
                        case "SAVE":
                            if (Hexbyte.getInstance().configManager.saveConfig(args[2])) {
                                ChatUtility.addChatMessage(ChatFormatting.GRAY + "Успешно сохранён конфиг " + ChatFormatting.AQUA + "\"" + args[2] + "\"");
                            //    NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
                                ConfigManager.getLoadedConfigs().clear();
                                Hexbyte.getInstance().configManager.load();
                            } else {
                                ChatUtility.addChatMessage(ChatFormatting.GRAY + "Не удалось сохранить конфиг " + ChatFormatting.AQUA  + "\"" + args[2] + "\"");
                             //   NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.ERROR);
                            }
                            break;
                        case "DELETE":
                            if (Hexbyte.getInstance().configManager.deleteConfig(args[2])) {
                                ChatUtility.addChatMessage(ChatFormatting.GRAY +  "Успешно удалён конфиг " + ChatFormatting.AQUA + "\"" + args[2] + "\"");
                               // NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + args[2] + "\"", 4, NotificationType.SUCCESS);
                            } else {
                                ChatUtility.addChatMessage(ChatFormatting.GRAY + "Не удалось удалить конфиг " +  ChatFormatting.AQUA + "\"" + args[2] + "\"");
                               // NotificationRenderer.publish("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + args[2] + "\"",  4, NotificationMode.WARNING);
                            }
                            break;
                    }
                } else if (args.length == 2 && upperCase.equalsIgnoreCase("LIST")) {
                    ChatUtility.addChatMessage(ChatFormatting.AQUA + "Ваши конфиги:");
                    for (Config config : Hexbyte.getInstance().configManager.getContents()) {
                        ChatUtility.addChatMessage(ChatFormatting.GRAY + config.getName());
                    }
                }
            } else {
                ChatUtility.addChatMessage(this.getUsage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}