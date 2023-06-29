package ru.rockstar.api.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.client.ui.settings.config.Config;
import ru.rockstar.client.ui.settings.config.ConfigManager;

public class ConfigCommand extends CommandAbstract {

    public ConfigCommand() {
        super("config", "configurations", "§6.config" + ChatFormatting.RED + " save | load | delete " + "§7<name>", "config", "cfg");
    }

    @Override
    public void execute(String... args) {
        try {
            if (args.length >= 2) {
                String upperCase = args[1].toUpperCase();
                if (args.length == 3) {
                    switch (upperCase) {
                        case "LOAD":
                            if (Main.instance.configManager.loadConfig(args[2])) {
                                Main.msg(ChatFormatting.GREEN + "Конфиг \"" + args[2] + "\" успешно загружен",true);
                                NotificationPublisher.queue(ChatFormatting.GREEN + "Конфиг", ChatFormatting.GREEN + "Успешно загружен: \"" + args[2] + "\"",  NotificationType.SUCCESS);
                            } else {
                                Main.msg(ChatFormatting.RED + "Ошибка загрузки конфига \"" + args[2] + "\"",true);
                                NotificationPublisher.queue(ChatFormatting.RED + "Конфиг", ChatFormatting.RED + "Не загружен: \"" + args[2] + "\"",  NotificationType.ERROR);
                            }
                            break;
                        case "SAVE":
                            if (Main.instance.configManager.saveConfig(args[2])) {
                                Main.msg(ChatFormatting.GREEN + "Конфиг \"" + args[2] + "\" успешно сохранен",true);
                                NotificationPublisher.queue(ChatFormatting.GREEN + "Конфиг", ChatFormatting.GREEN + "Успешно сохранен: \"" + args[2] + "\"", NotificationType.SUCCESS);
                                ConfigManager.getLoadedConfigs().clear();
                                Main.instance.configManager.load();
                            } else {
                                Main.msg(ChatFormatting.RED + "Ошибка сохранения конфига: \"" + args[2] + "\"",true);
                                NotificationPublisher.queue(ChatFormatting.RED + "Конфиг", ChatFormatting.RED + "Не сохранен: \"" + args[2] + "\"",  NotificationType.ERROR);
                            }
                            break;
                        case "DELETE":
                            if (Main.instance.configManager.deleteConfig(args[2])) {
                                Main.msg(ChatFormatting.GREEN + "Конфиг \"" + args[2] + "\" успешно удален",true);
                                NotificationPublisher.queue(ChatFormatting.GREEN + "Конфиг", ChatFormatting.GREEN + "Успешно удален: \"" + args[2] + "\"",  NotificationType.SUCCESS);
                            } else {
                                Main.msg(ChatFormatting.RED + "Не удалось загрузить конфиг: \"" + args[2] + "\"",true);
                                NotificationPublisher.queue(ChatFormatting.RED + "Конфиг", ChatFormatting.RED + "Не удален :( \"" + args[2] + "\"",  NotificationType.ERROR);
                            }
                            break;
                    }
                } else if (args.length == 2 && upperCase.equalsIgnoreCase("LIST")) {
                    Main.msg(ChatFormatting.GREEN + "Конфиг:",true);
                    for (Config config : Main.instance.configManager.getContents()) {
                        Main.msg(ChatFormatting.RED + config.getName(),true);
                    }
                }
            } else {
                Main.msg(this.getUsage(),true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}