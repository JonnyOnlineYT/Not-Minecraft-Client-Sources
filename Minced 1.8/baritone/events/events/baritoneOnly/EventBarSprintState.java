/*
 * Decompiled with CFR 0.150.
 */
package baritone.events.events.baritoneOnly;

import Celestial.event.events.Event;
import Celestial.event.events.callables.EventCancellable;
public class EventBarSprintState
extends EventCancellable
implements Event {
    public boolean state;

    public EventBarSprintState(boolean stateIn) {
        this.state = stateIn;
    }
}

