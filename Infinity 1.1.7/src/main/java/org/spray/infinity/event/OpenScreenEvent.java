package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.client.gui.screen.Screen;

public class OpenScreenEvent extends EventCancellable {

	private Screen screen;

	public OpenScreenEvent(Screen screen) {
		this.screen = screen;
	}

	public Screen getScreen() {
		return screen;
	}
}
