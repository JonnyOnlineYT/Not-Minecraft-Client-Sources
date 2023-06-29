package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.Packet;

/**
 * base - https://fabricmc.net/wiki/tutorial:networking
 * 
 * @author spray
 *
 */
public class PacketEvent extends EventCancellable {

	private Packet<?> packet;
	/**
	 * Type estb dva typa v packet - SEND, RECEIVE
	 * SEND - Otpravlennie packeti (client side)
	 * RECEIVE - Prihodyashie packeti (server side)
	 * Use - if (event.getType() == EventType.SEND || RECEIVE)
	 */
	private EventType type;

	public PacketEvent(EventType type, Packet<?> packet) {
		this.type = type;
		this.packet = packet;
	}

	public EventType getType() {
		return type;
	}

	public Packet<?> getPacket() {
		return packet;
	}

	/**
	 * * setPacket
	 * if (packet instanceof PacketC2Move) {
	 * event.cancel();
	 *  event.setPacket(new PacketC2Move())
	 *   
	 * @param packet
	 */
	public void setPacket(Packet<?> packet) {
		this.packet = packet;
	}

}
