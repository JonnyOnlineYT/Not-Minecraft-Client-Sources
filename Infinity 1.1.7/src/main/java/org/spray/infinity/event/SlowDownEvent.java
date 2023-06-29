package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.client.input.Input;

public class SlowDownEvent extends EventCancellable {
	
	private Input input;

	public SlowDownEvent(Input input) {
		this.setInput(input);
	}

	public Input getInput() {
		return input;
	}

	public void setInput(Input input) {
		this.input = input;
	}

}
