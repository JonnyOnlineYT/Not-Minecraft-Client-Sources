package org.spray.heaven.protect.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.client.gui.GuiScreen;

public class OpenScreenEvent extends EventCancellable {
	
	private GuiScreen screen;
	
	public OpenScreenEvent(GuiScreen screen) {
		this.screen = screen;
	}
	
	public GuiScreen getScreen() {
		return screen;
	}

}
