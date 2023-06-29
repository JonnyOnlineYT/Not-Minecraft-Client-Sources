package ru.wendoxd.profile;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import ru.wendoxd.WexSide;

public class DiscordRPC {

	public static DiscordRichPresence discordRichPresence = new DiscordRichPresence();
	public static club.minnced.discord.rpc.DiscordRPC discordRPC = club.minnced.discord.rpc.DiscordRPC.INSTANCE;

	public static void startRPC() {
		Minecraft mc = Minecraft.getMinecraft();
		DiscordEventHandlers eventHandlers = new DiscordEventHandlers();

		discordRPC.Discord_Initialize("910599496327647294", eventHandlers, true, null);

		discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
		discordRichPresence.largeImageKey = "ava";
		discordRichPresence.largeImageText = "@wexsidehack";
		discordRichPresence.smallImageKey = "ava2";
		discordRichPresence.smallImageText = "Version: " + WexSide.VERSION_TYPE;
		discordRPC.Discord_UpdatePresence(discordRichPresence);

		new Thread(() -> {
			while (true) {
				try {
					String state = "";
					if (mc.currentScreen instanceof GuiMainMenu) {
						state = "In Main Menu";
					} else if (mc.currentScreen instanceof GuiMultiplayer) {
						state = "In Multiplayer Menu";
					} else if (mc.isSingleplayer()) {
						state = "In SinglePlayer";
					} else if (mc.getCurrentServerData() != null) {
						state = "Playing " + mc.getCurrentServerData().serverIP;
					} else if (mc.currentScreen instanceof GuiOptions) {
						state = "In Options Gui";
					}
					discordRichPresence.state = state + " [ UID: " + WexSide.getProfile().getUID() + " ]";
					discordRPC.Discord_UpdatePresence(discordRichPresence);
					Thread.sleep(2000);
				} catch (InterruptedException ignored) {

				}
			}
		}).start();
	}

	public static void stopRPC() {
		discordRPC.Discord_Shutdown();
		discordRPC.Discord_ClearPresence();
	}
}
