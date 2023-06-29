package ru.wendoxd.modules.impl.player;

import net.minecraft.client.gui.GuiGameOver;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class AutoRespawn extends Module {

	private Frame autorespawn_frame = new Frame("AutoRespawn");
	private final CheckBox autorespawn = new CheckBox("AutoRespawn").markArrayList("AutoRespawn");

	@Override
	protected void initSettings() {
		autorespawn.markSetting("AutoRespawn");
		autorespawn_frame.register(autorespawn);
		MenuAPI.player.register(autorespawn_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && autorespawn.isEnabled(false)) {
			if (mc.currentScreen instanceof GuiGameOver) {
				mc.player.respawnPlayer();
				mc.displayGuiScreen(null);
			}
		}
	}
}
