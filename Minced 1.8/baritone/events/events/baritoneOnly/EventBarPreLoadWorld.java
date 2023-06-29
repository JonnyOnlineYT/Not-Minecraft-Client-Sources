/*
 * Decompiled with CFR 0.150.
 */
package baritone.events.events.baritoneOnly;

import net.minecraft.client.multiplayer.WorldClient;
import Celestial.event.events.Event;
import Celestial.event.events.callables.EventCancellable;
public class EventBarPreLoadWorld
extends EventCancellable
implements Event {
    public WorldClient world;

    public EventBarPreLoadWorld(WorldClient worldIn) {
        this.world = worldIn;
    }
}

