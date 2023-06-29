package ru.wendoxd.modules.impl.visuals;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventSwapState;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class DiscordRPC extends Module {

	private Frame discordrpc_frame = new Frame("DiscordRPC");
	private final CheckBox discordrpc = new CheckBox("DiscordRPC");

	@Override
	protected void initSettings() {
		discordrpc_frame.register(discordrpc.markSetting("DiscordRPC"));
		MenuAPI.visuals.register(discordrpc_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventSwapState) {
			if (((EventSwapState) event).getCheckBox() == discordrpc) {
				if (((EventSwapState) event).getState()) {
					ru.wendoxd.profile.DiscordRPC.startRPC();
				} else {
					ru.wendoxd.profile.DiscordRPC.stopRPC();
				}
			}
		}
	}
}
