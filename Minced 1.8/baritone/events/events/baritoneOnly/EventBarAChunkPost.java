/*
 * Decompiled with CFR 0.150.
 */
package baritone.events.events.baritoneOnly;

import Celestial.event.events.Event;
import Celestial.event.events.callables.EventCancellable;
public class EventBarAChunkPost
extends EventCancellable
implements Event {
    public int chunkX;
    public int chunkZ;
    public boolean loadChunk;

    public EventBarAChunkPost(int chunkX, int chunkZ, boolean load) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.loadChunk = load;
    }
}

