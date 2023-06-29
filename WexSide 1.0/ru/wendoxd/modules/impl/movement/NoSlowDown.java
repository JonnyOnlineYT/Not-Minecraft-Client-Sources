package ru.wendoxd.modules.impl.movement;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventMovementInput;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class NoSlowDown extends Module {

	private Frame noslowdown_frame = new Frame("NoSlowDown");
	private final CheckBox noslowdown = new CheckBox("NoSlowDown").markArrayList("NoSlowDown");
	private final SelectBox mode = new SelectBox("Mode", new String[] { "Vanilla", "Matrix", "Sunrise" },
			() -> noslowdown.isEnabled(true));
	private final Slider percentage = new Slider("Speed", 2, 1, 100, 100, () -> noslowdown.isEnabled(true));

	@Override
	protected void initSettings() {
		noslowdown.markSetting("NoSlowDown");
		percentage.modifyPath("Speed_3");
		noslowdown_frame.register(noslowdown, mode, percentage);
		MenuAPI.movement.register(noslowdown_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventMovementInput) {
			((EventMovementInput) event)
					.setMoveForward(noslowdown.isEnabled(false) ? percentage.getFloatValue() / 100d : 0.2);
			((EventMovementInput) event)
					.setMoveStrafe(noslowdown.isEnabled(false) ? percentage.getFloatValue() / 100d : 0.2);
		}
		if (event instanceof EventUpdate && noslowdown.isEnabled(false)) {
			if (mc.player.isHandActive()) {
				if (mc.player.onGround) {
					if (mc.player.ticksExisted % 2 == 0) {
						if (mode.get() == 1) {
							mc.player.motionX *= mc.player.moveStrafing == 0 ? 0.55 : 0.5;
							mc.player.motionZ *= mc.player.moveStrafing == 0 ? 0.55 : 0.5;
						} else if (mode.get() == 2) {
							mc.player.motionX *= 0.47;
							mc.player.motionZ *= 0.47;
						}
					}
				} else if (mc.player.fallDistance > (mode.get() == 1 ? 0.7 : 0.2)) {
					if (mode.get() == 1) {
						mc.player.motionX *= 0.93;
						mc.player.motionZ *= 0.93;
					} else if (mode.get() == 2) {
						mc.player.motionX *= 0.91;
						mc.player.motionZ *= 0.91;
					}
				}
			}
		}
	}
}
