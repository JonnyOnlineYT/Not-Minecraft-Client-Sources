package ru.rockstar.api.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.client.features.Feature;

import org.lwjgl.input.Keyboard;

public class BindCommand extends CommandAbstract {

    public BindCommand() {
        super("bind", "bind", "§6.bind" + ChatFormatting.RED + " add " + "§7<name> §7<key> " + TextFormatting.RED + "/" +" §6.bind " + ChatFormatting.RED + "remove " + "§7<name> §7<key>", "§6.bind" + ChatFormatting.RED + " add " + "§7<name> §7<key> | §6.bind" + ChatFormatting.RED + "remove " + "§7<name> <key> | §6.bind" + ChatFormatting.RED + "clear", "bind");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length == 4) {
                String moduleName = arguments[2];
                String bind = arguments[3].toUpperCase();
                Feature feature = Main.instance.featureDirector.getFeatureByLabel(moduleName);
                if (arguments[0].equals("bind")) {
                    switch (arguments[1]) {
                        case "add":
                            feature.setKey(Keyboard.getKeyIndex(bind));
                            Main.msg(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"",true);
                            NotificationPublisher.queue("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"",  NotificationType.SUCCESS);
                            break;
                        case "remove":
                            feature.setKey(0);
                            Main.msg(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"",true);
                            NotificationPublisher.queue("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"",  NotificationType.SUCCESS);
                            break;
                        case "clear":
                            if (!feature.getLabel().equals("ClickGui")) {
                                feature.setKey(0);
                            }
                            break;
                    }
                }
            } else {
            	if (arguments[1].equals("clear")) {
                    for (final Feature feature2 : Main.instance.featureDirector.getFeatureList()) {
                        if (feature2 == null) {
                            continue;
                        }
                        if (feature2.getKey() == 0) {
                            continue;
                        }
                        feature2.setKey(0);
                    }
                    Main.msg(ChatFormatting.GREEN + "Features successfully" + ChatFormatting.WHITE + " unbinded", true);
                    NotificationPublisher.queue("Bind Manager", ChatFormatting.GREEN + "Features successfully" + ChatFormatting.WHITE + " unbinded", NotificationType.SUCCESS);
                }
                if (arguments[1].equals("list")) {
                	Main.msg(ChatFormatting.WHITE + "Binds:", true);
                    for (final Feature feature3 : Main.instance.featureDirector.getFeatureList()) {
                        if (feature3.getKey() != 0) {
                            Main.msg(ChatFormatting.RED + feature3.getLabel() + ChatFormatting.WHITE + " : " + ChatFormatting.GRAY + Keyboard.getKeyName(feature3.getKey()), true);
                        }
                    }
                }
            	
            }
        } catch (Exception ignored) {

        }
    }
}
