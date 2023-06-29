package org.spray.heaven.features.module.modules.misc;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.TickEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.main.Heaven;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.protect.Provider;

import com.darkmagician6.eventapi.EventTarget;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import java.util.stream.Collectors;

@ModuleInfo(name = "DiscordRPC", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class DiscordRPCMod extends Module {

	private static DiscordRichPresence RPC = new DiscordRichPresence();
	private static DiscordRPC INSTANCE = DiscordRPC.INSTANCE;
	private int ticks = 0;

	@Override
	public void onEnable() {
		DiscordEventHandlers handlers = new DiscordEventHandlers();

		INSTANCE.Discord_Initialize("981938055248220250", handlers, true, null);
		RPC.startTimestamp = System.currentTimeMillis() / 1000L;
		RPC.largeImageKey = "logo";
		ticks = 0;
		RPC.largeImageText = Heaven.NAME + " " + Heaven.VERSION;
		
		RPC.smallImageKey = "user";
		RPC.smallImageText = "Role: " + "иди нахуй" + " | Session: " + Provider.getKeyAuth().getApi().getSessionId();

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

			if (mc.isSingleplayer())
				gameStatus = "in SinglePlayer";
			else if (mc.getCurrentServerData() != null)
				gameStatus = "Server: " + mc.getCurrentServerData().serverIP;
			else
				gameStatus = "Idle";

			RPC.details = gameStatus;
			RPC.state = "Features: " + Wrapper.getModule().getModules().values().stream().filter(m -> m.isEnabled()).collect(
					Collectors.toList()).size() + " of " + Wrapper.getModule().getModules().size();
			INSTANCE.Discord_RunCallbacks();
		}

		if (ticks % 200 == 0) {
			INSTANCE.Discord_UpdatePresence(RPC);
		}

		ticks++;
	}

}
