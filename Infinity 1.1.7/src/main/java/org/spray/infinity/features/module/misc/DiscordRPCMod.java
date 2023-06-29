package org.spray.infinity.features.module.misc;

import org.spray.infinity.event.TickEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;

import com.darkmagician6.eventapi.EventTarget;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

@ModuleInfo(category = Category.HIDDEN, desc = "", key = -2, name = "DiscordRPC", visible = false)
public class DiscordRPCMod extends Module {

	private static DiscordRichPresence RPC = new DiscordRichPresence();
	private static DiscordRPC INSTANCE = DiscordRPC.INSTANCE;
	private int ticks = 0;

	@Override
	public void onEnable() {
		DiscordEventHandlers handlers = new DiscordEventHandlers();

		INSTANCE.Discord_Initialize("827576969478537226", handlers, true, null);
		RPC.startTimestamp = System.currentTimeMillis() / 1000L;
		RPC.largeImageKey = "rpclogo";
		ticks = 0;
		RPC.largeImageText = Infinity.NAME + " " + Infinity.VERSION;

		RPC.smallImageKey = "infinity";
		RPC.smallImageText = ""; //

		INSTANCE.Discord_UpdatePresence(RPC);
		INSTANCE.Discord_RunCallbacks();
	}

	@Override
	public void onDisable() {
		INSTANCE.Discord_ClearPresence();
		INSTANCE.Discord_Shutdown();
	}

	@EventTarget
	public void onTick(TickEvent event) {
		if (ticks % 50 == 0) {

			String gameStatus = "Idle";

			if (Helper.MC.isInSingleplayer())
				gameStatus = "in SinglePlayer";
			else if (Helper.MC.getCurrentServerEntry().address != null)
				gameStatus = "Playing on: " + Helper.MC.getCurrentServerEntry().address;
			else
				gameStatus = "Idle";

			String detail = "Features " + Infinity.getModuleManager().getEnableModules().size() + "/"
					+ Infinity.getModuleManager().getModules().size();

			RPC.details = gameStatus;
			RPC.state = detail;
			INSTANCE.Discord_RunCallbacks();
		}

		if (ticks % 200 == 0) {
			INSTANCE.Discord_UpdatePresence(RPC);
		}

		ticks++;
	}

}
