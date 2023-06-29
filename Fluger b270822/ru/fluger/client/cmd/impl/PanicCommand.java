/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package ru.fluger.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.Fluger;
import ru.fluger.client.cmd.CommandAbstract;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.helpers.misc.ChatHelper;

public class PanicCommand
extends CommandAbstract {
    public PanicCommand() {
        super("panic", "Disabled all modules", ".panic", "panic");
    }

    @Override
    public void execute(String ... args) {
        if (args[0].equalsIgnoreCase("panic")) {
            for (Feature feature : Fluger.instance.featureManager.getFeatureList()) {
                if (!feature.getState()) continue;
                feature.toggle();
            }
            ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd " + (Object)ChatFormatting.RED + "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd " + (Object)ChatFormatting.WHITE + "\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd!");
        }
    }
}

