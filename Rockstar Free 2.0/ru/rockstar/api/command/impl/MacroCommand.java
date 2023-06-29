package ru.rockstar.api.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;
import ru.rockstar.api.command.macro.Macro;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;

import org.lwjgl.input.Keyboard;

public class MacroCommand extends CommandAbstract {

    public MacroCommand() {
        super("macros", "macro", "§6.macro" + ChatFormatting.RED + " add " + "§7<key> /home_home / §6.macro" + ChatFormatting.RED + " remove " + "§7<key> / §6.macro" + ChatFormatting.RED + " clear " + "§7/ §6.macro" + ChatFormatting.RED + " list", "§6.macro" + ChatFormatting.RED + " add " + "§7<key> </home_home> / §6.macro" + ChatFormatting.RED + " remove " + "§7<key> / §6.macro" + ChatFormatting.RED + " clear " + "/ §6.macro" + ChatFormatting.RED + " list", "macro");
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
                        Main.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(arguments[2].toUpperCase()), command.toString()));
                        Main.msg(ChatFormatting.GREEN + "Added" + " macros for key" + ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "with value " + ChatFormatting.RED + command,true);
                        NotificationPublisher.queue("Macro Manager", ChatFormatting.GREEN + "Added" + " macro for key" + ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "with value " + ChatFormatting.RED + command,  NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("clear")) {
                        if (Main.instance.macroManager.getMacros().isEmpty()) {
                            Main.msg(ChatFormatting.RED + "Your macros list is empty!",true);
                            NotificationPublisher.queue("Macro Manager", "Your macro list is empty!", NotificationType.ERROR);
                            return;
                        }
                        Main.instance.macroManager.getMacros().clear();
                        Main.msg(ChatFormatting.GREEN + "Your macros list " + ChatFormatting.WHITE + " successfully cleared!",true);
                        NotificationPublisher.queue("Macro Manager", ChatFormatting.GREEN + "Your macros list " + ChatFormatting.WHITE + " successfully cleared!",NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("remove")) {
                        Main.instance.macroManager.deleteMacroByKey(Keyboard.getKeyIndex(arguments[2].toUpperCase()));
                        Main.msg(ChatFormatting.GREEN + "Macro " + ChatFormatting.WHITE + "was deleted from key " + ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"",true);
                        NotificationPublisher.queue("Macro Manager", ChatFormatting.GREEN + "Macro " + ChatFormatting.WHITE + "was deleted from key " + ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"" , NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("list")) {
                        if (Main.instance.macroManager.getMacros().isEmpty()) {
                            Main.msg(ChatFormatting.RED + "Your macros list is empty!",true);
                            NotificationPublisher.queue("Macro Manager", "Your macros list is empty!", NotificationType.ERROR);
                            return;
                        }
                        Main.instance.macroManager.getMacros().forEach(macro -> Main.msg(ChatFormatting.GREEN + "Macros list: " + ChatFormatting.WHITE + "Macros Name: " + ChatFormatting.RED + macro.getValue() + ChatFormatting.WHITE + ", Macro Bind: " + ChatFormatting.RED + Keyboard.getKeyName(macro.getKey()),true));
                    }
                }
            } else {
                Main.msg(getUsage(),true);
            }
        } catch (Exception ignored) {

        }
    }
}
