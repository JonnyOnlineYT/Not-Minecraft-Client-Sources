package org.spray.heaven.protect.components;

import java.util.function.Supplier;

public class StateCheck {

	private Supplier<String> result;
	private boolean started;

	public StateCheck() {
		this.result = null;
		started = false;
	}

	public Supplier<String> getResult() {
		return result;
	}

	public void setResult(Supplier<String> result) {
		if (!started) {
			this.result = result;
			started = true;
		}
	}

}
