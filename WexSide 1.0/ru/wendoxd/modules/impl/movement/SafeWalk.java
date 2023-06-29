package ru.wendoxd.modules.impl.movement;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventSafeWalk;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class SafeWalk extends Module {

	private Frame safewalk_frame = new Frame("SafeWalk");
	public static CheckBox safewalk = new CheckBox("SafeWalk").markArrayList("SafeWalk");

	@Override
	protected void initSettings() {
		safewalk.markSetting("SafeWalk");
		safewalk_frame.register(safewalk);
		MenuAPI.movement.register(safewalk_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventSafeWalk && safewalk.isEnabled(false)) {
			if (mc.player.onGround) {
				((EventSafeWalk) event).setCanceled();
			}
		}
	}
}
