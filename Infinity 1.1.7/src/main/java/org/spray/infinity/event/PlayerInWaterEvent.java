package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class PlayerInWaterEvent extends EventCancellable {

	private boolean inWater;

	public PlayerInWaterEvent(boolean inWater) {
		this.inWater = inWater;
	}

	public boolean isInWater() {
		return inWater;
	}

	public void setInWater(boolean inWater) {
		this.inWater = inWater;
	}

}
