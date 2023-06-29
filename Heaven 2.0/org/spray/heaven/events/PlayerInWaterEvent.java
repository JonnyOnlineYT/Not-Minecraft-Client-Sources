package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class PlayerInWaterEvent extends EventCancellable {
	
	private boolean isInWater;
	
	public PlayerInWaterEvent(boolean isInWater) {
		this.isInWater = isInWater;
	}

	public boolean isInWater() {
		return isInWater;
	}

	public void setInWater(boolean isInWater) {
		this.isInWater = isInWater;
	}
	

}
