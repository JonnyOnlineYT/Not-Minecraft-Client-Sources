/*
 * Decompiled with CFR 0.150.
 */
package baritone.events.events.network;

import net.minecraft.network.Packet;
import Celestial.event.events.Event;
import Celestial.event.events.callables.EventCancellable;

public class EventPostSendPacket
extends EventCancellable
implements Event {
    private Packet packet;

    public EventPostSendPacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }
}

