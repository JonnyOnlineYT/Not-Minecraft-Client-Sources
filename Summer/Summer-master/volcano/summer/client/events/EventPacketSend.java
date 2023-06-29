package volcano.summer.client.events;

import net.minecraft.network.Packet;
import volcano.summer.base.manager.event.Event;

public class EventPacketSend extends Event {
	private boolean cancel;
	public static Packet packet;




	public EventPacketSend(Packet packetIn) {
		packet = packetIn;
	}

	public static Packet getPacket() {
		return packet;
	}

	public boolean isCancelled() {
		return this.cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	public void cancel() {
		this.cancel = true;
	}
}
