package ru.wendoxd.modules.impl.movement;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.player.MoveUtils;

public class AutoSprint extends Module {

	private Frame autosprint_frame = new Frame("AutoSprint");
	private final CheckBox autosprint = (CheckBox) new CheckBox("AutoSprint").markArrayList("AutoSprint")
			.markDescription("Автоматически включает спринт при ходьбе");

	@Override
	protected void initSettings() {
		autosprint_frame.register(autosprint.markSetting("AutoSprint"));
		MenuAPI.movement.register(autosprint_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && autosprint.isEnabled(false)) {
			mc.player.setSprinting(MoveUtils.isMoving());
		}
	}
}
