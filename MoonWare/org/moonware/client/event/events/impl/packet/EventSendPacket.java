package org.moonware.client.event.events.impl.packet;

import net.minecraft.network.Packet;
import org.moonware.client.event.events.callables.EventCancellable;

public class EventSendPacket extends EventCancellable {

    public Packet<?> packet;

    public EventSendPacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }
}
