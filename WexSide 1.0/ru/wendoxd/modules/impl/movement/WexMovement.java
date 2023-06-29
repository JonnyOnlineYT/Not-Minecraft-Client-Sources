package ru.wendoxd.modules.impl.movement;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventMove;
import ru.wendoxd.modules.Module;

public class WexMovement extends Module {

	public void onEvent(Event event) {
		if (event instanceof EventMove) {
		}
	}

	public static void move(EventMove move) {
		
	}
}
