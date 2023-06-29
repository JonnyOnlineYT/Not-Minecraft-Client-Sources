package ru.wendoxd.modules.impl.movement;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class AirJump extends Module {

	private Frame airjump_frame = new Frame("AirJump");
	private final CheckBox airjump = (CheckBox) new CheckBox("AirJump").markArrayList("AirJump")
			.markDescription("Позволяет прыгать в воздухе");

	@Override
	protected void initSettings() {
		airjump_frame.register(airjump.markSetting("AirJump"));
		MenuAPI.movement.register(airjump_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && airjump.isEnabled(false)) {
			if (mc.gameSettings.keyBindJump.pressed) {
				mc.player.onGround = true;
			}
		}
	}
}