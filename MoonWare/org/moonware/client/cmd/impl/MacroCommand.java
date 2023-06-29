package org.moonware.client.cmd.impl;

import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.macro.Macro;
import org.lwjgl.input.Keyboard;
import org.moonware.client.cmd.CommandAbstract;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

public class MacroCommand extends CommandAbstract {

    public MacroCommand() {
        super("macros", "macro", "§6.macro" + Formatting.LIGHT_PURPLE + " add " + "§3<key> /home_home | §6.macro" + Formatting.LIGHT_PURPLE + " remove " + "§3<key> | §6.macro" + Formatting.LIGHT_PURPLE + " clear " + "§3| §6.macro" + Formatting.LIGHT_PURPLE + " list", "§6.macro" + Formatting.LIGHT_PURPLE + " add " + "§3<key> </home_home> | §6.macro" + Formatting.LIGHT_PURPLE + " remove " + "§3<key> | §6.macro" + Formatting.LIGHT_PURPLE + " clear " + "| §6.macro" + Formatting.LIGHT_PURPLE + " list", "macro");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("macro")) {
                    if (arguments[1].equals("add")) {
                        StringBuilder command = new StringBuilder();
                        for (int i = 3; i < arguments.length; ++i) {
                            command.append(arguments[i]).append(" ");
                        }
                        MoonWare.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(arguments[2].toUpperCase()), command.toString()));
                        MWUtils.sendChat(Formatting.GREEN + "Added" + " macros for key" + Formatting.RED + " \"" + arguments[2].toUpperCase() + Formatting.RED + "\" " + Formatting.WHITE + "with value " + Formatting.RED + command);
                        NotificationManager.publicity("Macro Manager", Formatting.GREEN + "Added" + " macro for key" + Formatting.RED + " \"" + arguments[2].toUpperCase() + Formatting.RED + "\" " + Formatting.WHITE + "with value " + Formatting.RED + command, 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("clear")) {
                        if (MoonWare.macroManager.getMacros().isEmpty()) {
                            MWUtils.sendChat(Formatting.RED + "Your macros list is empty!");
                            NotificationManager.publicity("Macro Manager", "Your macro list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        MoonWare.macroManager.getMacros().clear();
                        MWUtils.sendChat(Formatting.GREEN + "Your macros list " + Formatting.WHITE + " successfully cleared!");
                        NotificationManager.publicity("Macro Manager", Formatting.GREEN + "Your macros list " + Formatting.WHITE + " successfully cleared!", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("remove")) {
                        MoonWare.macroManager.deleteMacroByKey(Keyboard.getKeyIndex(arguments[2].toUpperCase()));
                        MWUtils.sendChat(Formatting.GREEN + "Macro " + Formatting.WHITE + "was deleted from key " + Formatting.RED + "\"" + arguments[2].toUpperCase() + "\"");
                        NotificationManager.publicity("Macro Manager", Formatting.GREEN + "Macro " + Formatting.WHITE + "was deleted from key " + Formatting.RED + "\"" + arguments[2].toUpperCase() + "\"", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("list")) {
                        if (MoonWare.macroManager.getMacros().isEmpty()) {
                            MWUtils.sendChat(Formatting.RED + "Your macros list is empty!");
                            NotificationManager.publicity("Macro Manager", "Your macros list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        MoonWare.macroManager.getMacros().forEach(macro -> MWUtils.sendChat(Formatting.GREEN + "Macros list: " + Formatting.WHITE + "Macros Name: " + Formatting.RED + macro.getValue() + Formatting.WHITE + ", Macro Bind: " + Formatting.RED + Keyboard.getKeyName(macro.getKey())));
                    }
                }
            } else {
                MWUtils.sendChat(getUsage());
            }
        } catch (Exception ignored) {

        }
    }
}
