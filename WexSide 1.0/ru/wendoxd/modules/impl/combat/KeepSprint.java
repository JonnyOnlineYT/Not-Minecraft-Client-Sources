package ru.wendoxd.modules.impl.combat;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventPlayerMotion;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class KeepSprint extends Module {

	private Frame keepSprint_frame = new Frame("KeepSprint");
	private final CheckBox keepsprint = (CheckBox) new CheckBox("KeepSprint").markArrayList("KeepSprint")
			.markDescription("Не выключает спринт при ударе.");
	private final Slider speed = new Slider("Speed", 1, 0, 2, 0.5, () -> keepsprint.isEnabled(true));
	private final CheckBox setSprinting = new CheckBox("Set Sprinting", () -> keepsprint.isEnabled(true));

	@Override
	protected void initSettings() {
		keepSprint_frame.register(keepsprint.markSetting("KeepSprint"), speed, setSprinting);
		MenuAPI.combat.register(keepSprint_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPlayerMotion && keepsprint.isEnabled(false)) {
			mc.player.getClass();
			((EventPlayerMotion) event).setMotionXZ(speed.getFloatValue());
			((EventPlayerMotion) event).setSprint(setSprinting.isEnabled(false));
		}
	}
}
