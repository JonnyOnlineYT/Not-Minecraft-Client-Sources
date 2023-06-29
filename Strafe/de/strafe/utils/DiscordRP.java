package de.strafe.utils;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;


public class DiscordRP {

    private boolean running = true;
    private long created = 0;

    public DiscordRP() {
        start();
    }

    public void start() {
        this.created = System.currentTimeMillis();

        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(user -> {
            update("Idle", "MainMenu");
        }).build();

        DiscordRPC.discordInitialize("954701760197460048", handlers, true);
        new Thread("Discord RPC Callback") {
            public void run() {
                while (running) {
                    DiscordRPC.discordRunCallbacks();
                }
            }
        }.start();
    }

    public void shutdown() {
        running = false;
        DiscordRPC.discordShutdown();

    }

    public void update(String details, String state) {
        DiscordRichPresence presence = new DiscordRichPresence.Builder(state).setBigImage("logo", "").setDetails(details).setStartTimestamps(created).build();
        DiscordRPC.discordUpdatePresence(presence);

    }
}