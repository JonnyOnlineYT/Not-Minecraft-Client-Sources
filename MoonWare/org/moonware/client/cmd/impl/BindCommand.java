package org.moonware.client.cmd.impl;

import net.minecraft.util.text.Formatting;
import org.lwjgl.input.Keyboard;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.cmd.CommandAbstract;
import org.moonware.client.feature.Feature;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

public class BindCommand extends CommandAbstract {
    private static boolean isSendedNotify;
    public BindCommand() {
        super("bind", "bind", "§6.bind" + Formatting.LIGHT_PURPLE + " add " + "§3<name> §3<key> | §6.bind " + Formatting.LIGHT_PURPLE + "remove " + "§3<name> §3<key>", "§6.bind" + Formatting.LIGHT_PURPLE + " add " + "§3<name> §3<key> | §6.bind" + Formatting.LIGHT_PURPLE + "remove " + "§3<name> <key> | §6.bind" + Formatting.LIGHT_PURPLE + "clear", "bind");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length > 1) {
                String moduleName = arguments[2];
                String bind = arguments[3].toUpperCase();
                Feature feature = MoonWare.featureManager.getFeatureByLabel(moduleName);
                if (arguments[0].equals("bind")) {
                    switch (arguments[1]) {
                        case "add":
                            feature.setBind(Keyboard.getKeyIndex(bind));
                            MWUtils.sendChat(Formatting.GREEN + feature.getLabel() + Formatting.WHITE + " was set on key " + Formatting.RED + "\"" + bind + "\"");
                            NotificationManager.publicity("Bind Manager", Formatting.GREEN + feature.getLabel() + Formatting.WHITE + " was set on key " + Formatting.RED + "\"" + bind + "\"", 4, NotificationType.SUCCESS);
                            break;
                        case "remove":
                            feature.setBind(0);
                            MWUtils.sendChat(Formatting.GREEN + feature.getLabel() + Formatting.WHITE + " bind was deleted from key " + Formatting.RED + "\"" + bind + "\"");
                            NotificationManager.publicity("Bind Manager", Formatting.GREEN + feature.getLabel() + Formatting.WHITE + " bind was deleted from key " + Formatting.RED + "\"" + bind + "\"", 4, NotificationType.SUCCESS);
                            break;
                        case "clear":
                            NotificationManager.publicity("BindManager Debug", "FeatureKeyBinds Deleted",4,NotificationType.SUCCESS);
                            for (Feature feature1 : MoonWare.featureManager.getFeatureList()) {
                                NotificationManager.publicity("BindManager Debug", "FeatureKeyBinds Deleted",4,NotificationType.SUCCESS);
                                if (!feature1.getLabel().equalsIgnoreCase("ClickGui")) {
                                    feature1.setBind(0);
                                }
                                if (feature1.getLabel().equalsIgnoreCase("KillAura")) {
                                    NotificationManager.publicity("BindManager Debug", "FeatureKeyBinds Deleted",4,NotificationType.SUCCESS);
                                }
                            }
                            break;
                        case "list":
                            if (feature.getBind() == 0) {
                                MWUtils.sendChat(Formatting.RED + "Your macros list is empty!");
                                NotificationManager.publicity("Macro Manager", "Your macros list is empty!", 4, NotificationType.ERROR);
                                return;
                            }
                            MoonWare.featureManager.getFeatureList().forEach(feature1 -> MWUtils.sendChat(Formatting.GREEN + "Binds list: " + Formatting.WHITE + "Binds Name: " + Formatting.RED + feature1.getBind() + Formatting.WHITE + ", Macro Bind: " + Formatting.RED + Keyboard.getKeyName(feature1.getBind())));
                    }
                }
            } else {
                MWUtils.sendChat(getUsage());
            }
        } catch (Exception ignored) {
        }
        if (arguments[1].equalsIgnoreCase("clear")) {
            for (Feature feature1 : MoonWare.featureManager.getFeatureList()) {
                if (!feature1.getLabel().equalsIgnoreCase("ClickGui")) {
                    feature1.setBind(0);
                }
                if (feature1.getBind() == 0 && !isSendedNotify) {
                    NotificationManager.publicity("BindManager Debug", "FeatureKeyBinds Deleted",4,NotificationType.SUCCESS);
                    isSendedNotify = true;
                }
            }

        }
    }
}
