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
import ru.fluger.client.feature.Feature;
import ru.fluger.client.helpers.misc.ChatHelper;

public class BindCommand
extends CommandAbstract {
    public BindCommand() {
        super("bind", "bind", "\ufffd6.bind" + (Object)ChatFormatting.RED + " add \ufffd7<name> \ufffd7<key> " + (Object)((Object)TextFormatting.RED) + "\n[" + (Object)((Object)TextFormatting.WHITE) + "Fluger Premium" + (Object)((Object)TextFormatting.GRAY) + "] \ufffd6.bind " + (Object)ChatFormatting.RED + "remove \ufffd7<name> \ufffd7<key> \n[" + (Object)((Object)TextFormatting.WHITE) + "Fluger Premium" + (Object)((Object)TextFormatting.GRAY) + "] \ufffd6.bind " + (Object)ChatFormatting.RED + "list ", "bind");
    }

    @Override
    public void execute(String ... arguments) {
        try {
            if (arguments.length == 4) {
                String moduleName = arguments[2];
                String bind = arguments[3].toUpperCase();
                Feature feature = Fluger.instance.featureManager.getFeatureByLabel(moduleName);
                if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("add")) {
                    feature.setBind(Keyboard.getKeyIndex((String)bind));
                    ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + feature.getLabel() + (Object)ChatFormatting.WHITE + " was set on key " + (Object)ChatFormatting.RED + "\"" + bind + "\"");
                } else if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("remove")) {
                    feature.setBind(0);
                    ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + feature.getLabel() + (Object)ChatFormatting.WHITE + " bind was deleted from key " + (Object)ChatFormatting.RED + "\"" + bind + "\"");
                }
            } else if (arguments.length == 2) {
                if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("list")) {
                    for (Feature f : Fluger.instance.featureManager.getFeatureList()) {
                        if (f.getBind() == 0) continue;
                        ChatHelper.addChatMessage(f.getLabel() + " : " + Keyboard.getKeyName((int)f.getBind()));
                    }
                } else {
                    ChatHelper.addChatMessage(this.getUsage());
                }
            } else if (arguments[0].equalsIgnoreCase("bind")) {
                ChatHelper.addChatMessage(this.getUsage());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

