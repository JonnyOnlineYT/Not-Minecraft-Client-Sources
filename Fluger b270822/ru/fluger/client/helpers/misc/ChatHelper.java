/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.helpers.misc;

import net.minecraft.util.text.TextComponentString;
import ru.fluger.client.helpers.Helper;

public class ChatHelper
implements Helper {
    public static String chatPrefix = "\u00a7d[FlugerClient] \u00a7f";

    public static void addChatMessage(Object message) {
        ChatHelper.mc.player.addChatMessage(new TextComponentString(chatPrefix + message));
    }
}

