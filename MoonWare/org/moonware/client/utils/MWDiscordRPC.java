package org.moonware.client.utils;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import org.moonware.client.MoonWare;

public class MWDiscordRPC {
    private static final String ID = "1002695869616902154";
    public static void start() {
        try {
            DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
            DiscordRPC.discordInitialize(ID, eventHandlers, true, null);
            update();
        } catch (Exception ignored) {}
    }

    public static void update() {
        try {
            DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("Version: " + MoonWare.VERSION)
                    .setDetails(MoonWare.LICENSE)
                    .setBigImage("image", "vk.com/moonwarerage")
                    .build());
        } catch (Exception ignored) {}
    }


    public static void stop() {
        try {
            DiscordRPC.discordShutdown();
        } catch (Exception ignored) {}
    }
}