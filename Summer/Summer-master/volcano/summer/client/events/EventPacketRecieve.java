package volcano.summer.client.events;

import net.minecraft.network.Packet;
import volcano.summer.base.manager.event.Event;

public class EventPacketRecieve extends Event {

	public Packet packet;

	public EventPacketRecieve(Packet packet) {
		this.packet = packet;
	}

	public Packet getPacket() {
		return this.packet;
	}

	public void setPacket(final Packet packet) {
		this.packet = packet;
	}

	protected boolean cancelled;

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
