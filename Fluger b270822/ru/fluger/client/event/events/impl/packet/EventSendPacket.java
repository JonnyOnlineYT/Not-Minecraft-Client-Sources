/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.event.events.impl.packet;

import net.minecraft.network.Packet;
import ru.fluger.client.event.events.callables.EventCancellable;

public class EventSendPacket
extends EventCancellable {
    private final Packet<?> packet;

    public EventSendPacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }
}

