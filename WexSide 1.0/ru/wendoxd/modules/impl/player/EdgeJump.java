package ru.wendoxd.modules.impl.player;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventMove;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class EdgeJump extends Module {
	private static Frame frame = new Frame("EdgeJump");
	private static CheckBox edgeJump = (CheckBox) new CheckBox("EdgeJump").markArrayList("EdgeJump")
			.markDescription("Автоматический прыжок на краю блока");

	@Override
	public void initSettings() {
		MenuAPI.movement.register(frame);
		frame.register(edgeJump.markSetting("Edge-Jump"));
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventMove && edgeJump.isEnabled(false)) {
			EventMove move = (EventMove) event;
			if (mc.player.onGround && !move.toGround()) {
				mc.player.jump();
				move.motion().yCoord = mc.player.motionY;
				move.motion().xCoord = mc.player.motionX;
				move.motion().zCoord = mc.player.motionZ;
			}
		}
	}
}
