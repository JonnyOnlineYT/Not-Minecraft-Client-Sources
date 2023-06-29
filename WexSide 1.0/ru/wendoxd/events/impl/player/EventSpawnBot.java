package ru.wendoxd.events.impl.player;

import net.minecraft.client.network.NetworkPlayerInfo;
import ru.wendoxd.events.Event;

public class EventSpawnBot extends Event {
	private NetworkPlayerInfo npi;
	private boolean canceled;

	public EventSpawnBot(NetworkPlayerInfo npi) {
		this.npi = npi;
	}

	public NetworkPlayerInfo getNPI() {
		return this.npi;
	}

	public void setCanceled() {
		this.canceled = true;
	}

	public boolean isCanceled() {
		return this.canceled;
	}
}
