package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.client.world.ClientWorld;

public class JoinWorldEvent extends EventCancellable {
	
	private ClientWorld world;
	
	public JoinWorldEvent(ClientWorld world) {
		this.world = world;
	}
	
	public ClientWorld getWorld() {
		return world;
	}

}
