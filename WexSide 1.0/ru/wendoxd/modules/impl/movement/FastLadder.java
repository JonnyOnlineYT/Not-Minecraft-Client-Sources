package ru.wendoxd.modules.impl.movement;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventEntitySync;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class FastLadder extends Module {

	private Frame fastladder_frame = new Frame("FastLadder");
	private final CheckBox fastladder = new CheckBox("FastLadder").markArrayList("FastLadder");
	private final Slider speed = new Slider("Speed", 1, 0.1F, 1, 0.2F, () -> fastladder.isEnabled(true));

	@Override
	protected void initSettings() {
		fastladder.markSetting("FastLadder");
		speed.modifyPath("Speed_2");
		fastladder_frame.register(fastladder, speed);
		MenuAPI.movement.register(fastladder_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventEntitySync && fastladder.isEnabled(false)) {
			if (mc.player.isOnLadder() && mc.player.isCollidedHorizontally) {
				mc.player.motionY += speed.getFloatValue();
			}
		}
	}
}