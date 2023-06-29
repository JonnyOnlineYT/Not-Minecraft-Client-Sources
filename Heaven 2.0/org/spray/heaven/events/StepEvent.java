package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;

public class StepEvent extends EventCancellable {

	private EventType type;
	private double stepHeight;

	public StepEvent(EventType type, double stepHeight) {
		this.type = type;
		this.stepHeight = stepHeight;
	}

	public EventType getType() {
		return type;
	}

	public double getStepHeight() {
		return stepHeight;
	}

	public void setStepHeight(double stepHeight) {
		this.stepHeight = stepHeight;
	}
}