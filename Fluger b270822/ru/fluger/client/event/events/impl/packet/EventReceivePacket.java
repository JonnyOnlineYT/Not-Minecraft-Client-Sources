/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.event.events.impl.packet;

import net.minecraft.network.Packet;
import ru.fluger.client.event.events.callables.EventCancellable;

public class EventReceivePacket
extends EventCancellable {
    private Packet<?> packet;

    public EventReceivePacket(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}

