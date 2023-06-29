/*
 * Decompiled with CFR 0.150.
 */
package baritone.events.events.baritoneOnly;

import net.minecraft.network.play.server.SPacketChunkData;
import Celestial.event.events.Event;
import Celestial.event.events.callables.EventCancellable;
public class EventBarChunkPost
extends EventCancellable
implements Event {
    public SPacketChunkData packet;

    public EventBarChunkPost(SPacketChunkData packetIn) {
        this.packet = packetIn;
    }
}

