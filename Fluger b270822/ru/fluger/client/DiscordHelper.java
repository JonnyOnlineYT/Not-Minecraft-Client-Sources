/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client;

//import club.minnced.discord.rpc.DiscordEventHandlers;
//import club.minnced.discord.rpc.DiscordRPC;
//import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import ru.fluger.client.Fluger;
import ru.squad47.UserData;

public class DiscordHelper {
    private static final String discordID = "986678277991194685";
   private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
   private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void startRPC() {
        try {
     DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
         discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);
   DiscordHelper.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
   DiscordHelper.discordRichPresence.details = "UID: " + "SLIV SRC FLUGER CLIENT";
    DiscordHelper.discordRichPresence.largeImageKey = "large";
   DiscordHelper.discordRichPresence.largeImageText = "vk.com/mincedclient";
  DiscordHelper.discordRichPresence.state = "build: " + Fluger.version;
    discordRPC.Discord_UpdatePresence(discordRichPresence);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopRPC() {
   //     discordRPC.Discord_Shutdown();
    }
}

