package ua.apraxia.utility.other;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DiscordUtility {

    private boolean running = true;
    private long created = 0;

    public void init() {
        running = true;
        this.created = System.currentTimeMillis();
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(discordUser -> {
        }).build();
        net.arikia.dev.drpc.DiscordRPC.discordInitialize("1043219215185887333", handlers, true);

        new Thread("") {

            @Override
            public void run() {
                net.arikia.dev.drpc.DiscordRPC.discordRunCallbacks();
            }

        }.start();
    }

    public void shutdown() {
        running = false;
        net.arikia.dev.drpc.DiscordRPC.discordShutdown();
    }

    public void update(String firstLine, String secondLine) {
        DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(secondLine);
        b.setBigImage("https://media1.tenor.com/images/d0dea097208f16eb2636812552db0185/tenor.gif?itemid=27290339", "discord.gg/hexbyte");
        b.setDetails(firstLine);
        b.setStartTimestamps(created);
        net.arikia.dev.drpc.DiscordRPC.discordUpdatePresence(b.build());
    }

}
