package org.spray.heaven.protect;

import com.darkmagician6.eventapi.EventManager;

public class ProtectModule {

	private boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public ProtectModule enable() {
		EventManager.register(this);
		enabled = true;
		return this;
	}

	public ProtectModule disable() {
		EventManager.unregister(this);
		enabled = false;
		return this;
	}
}
