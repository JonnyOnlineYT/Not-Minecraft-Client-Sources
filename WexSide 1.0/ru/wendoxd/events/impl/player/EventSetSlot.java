package ru.wendoxd.events.impl.player;

import net.minecraft.network.play.server.SPacketSetSlot;
import ru.wendoxd.events.Event;

public class EventSetSlot extends Event {
	SPacketSetSlot packet;

	public EventSetSlot(SPacketSetSlot packet) {
		this.packet = packet;
	}

	public SPacketSetSlot getPacket() {
		return this.packet;
	}
}
