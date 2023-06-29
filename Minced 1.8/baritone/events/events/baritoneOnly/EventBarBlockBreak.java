/*
 * Decompiled with CFR 0.150.
 */
package baritone.events.events.baritoneOnly;

import Celestial.event.events.Event;
import Celestial.event.events.callables.EventCancellable;
import net.minecraft.util.math.BlockPos;

public class EventBarBlockBreak
extends EventCancellable
implements Event {
    public BlockPos position;

    public EventBarBlockBreak(BlockPos pos) {
        this.position = pos;
    }
}

