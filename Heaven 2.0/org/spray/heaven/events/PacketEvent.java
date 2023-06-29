package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.network.Packet;

/**
 * 
 * Inject into the NetworkManager class
 * @method = sendPacket()V, channelRead0()V
 * @type = SEND, RECEIVE
 *
 */
public class PacketEvent extends EventCancellable {

	private EventType type;
	private Packet packet;
	
	public PacketEvent(EventType type, Packet packet) {
		this.type = type;
		this.packet = packet;
	}

	public EventType getType() {
		return type;
	}

	public Packet getPacket() {
		return packet;
	}
	
}
