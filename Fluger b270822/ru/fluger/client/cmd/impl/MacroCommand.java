/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.input.Keyboard
 */
package ru.fluger.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import ru.fluger.client.Fluger;
import ru.fluger.client.cmd.CommandAbstract;
import ru.fluger.client.cmd.macro.Macro;
import ru.fluger.client.files.impl.MacroConfig;
import ru.fluger.client.helpers.misc.ChatHelper;

public class MacroCommand
extends CommandAbstract {
    public MacroCommand() {
        super("macros", "macro", (Object)((Object)TextFormatting.GRAY) + ".macro" + (Object)ChatFormatting.WHITE + " add \ufffd3<key> /home_home | \ufffd7.macro" + (Object)ChatFormatting.WHITE + " remove \ufffd3<key> |" + (Object)((Object)TextFormatting.GRAY) + " .macro" + (Object)ChatFormatting.WHITE + " clear \ufffd3| \ufffd7.macro" + (Object)ChatFormatting.WHITE + " list", "\ufffd7.macro" + (Object)ChatFormatting.WHITE + " add \ufffd3<key> </home_home> | \ufffd7.macro" + (Object)ChatFormatting.WHITE + " remove \ufffd3<key> | \ufffd7.macro" + (Object)ChatFormatting.WHITE + " clear | \ufffd7.macro" + (Object)ChatFormatting.WHITE + " list", "macro");
    }

    @Override
    public void execute(String ... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("macro")) {
                    if (arguments[1].equals("add")) {
                        StringBuilder command = new StringBuilder();
                        for (int i = 3; i < arguments.length; ++i) {
                            command.append(arguments[i]).append(" ");
                        }
                        Fluger.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex((String)arguments[2].toUpperCase()), command.toString()));
                        Fluger.instance.fileManager.getFile(MacroConfig.class).saveFile();
                        ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + "Added macros for key" + (Object)ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + (Object)ChatFormatting.RED + "\" " + (Object)ChatFormatting.WHITE + "with value " + (Object)ChatFormatting.RED + command);
                    }
                    if (arguments[1].equals("clear")) {
                        if (Fluger.instance.macroManager.getMacros().isEmpty()) {
                            ChatHelper.addChatMessage((Object)ChatFormatting.RED + "Your macros list is empty!");
                            return;
                        }
                        Fluger.instance.macroManager.getMacros().clear();
                        ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + "Your macros list " + (Object)ChatFormatting.WHITE + " successfully cleared!");
                    }
                    if (arguments[1].equals("remove")) {
                        Fluger.instance.macroManager.deleteMacroByKey(Keyboard.getKeyIndex((String)arguments[2].toUpperCase()));
                        ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + "Macro " + (Object)ChatFormatting.WHITE + "was deleted from key " + (Object)ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"");
                    }
                    if (arguments[1].equals("list")) {
                        if (Fluger.instance.macroManager.getMacros().isEmpty()) {
                            ChatHelper.addChatMessage((Object)ChatFormatting.RED + "Your macros list is empty!");
                            return;
                        }
                        Fluger.instance.macroManager.getMacros().forEach(macro -> ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + "Macros list: " + (Object)ChatFormatting.WHITE + "Macros Name: " + (Object)ChatFormatting.RED + macro.getValue() + (Object)ChatFormatting.WHITE + ", Macro Bind: " + (Object)ChatFormatting.RED + Keyboard.getKeyName((int)macro.getKey())));
                    }
                }
            } else {
                ChatHelper.addChatMessage(this.getUsage());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

