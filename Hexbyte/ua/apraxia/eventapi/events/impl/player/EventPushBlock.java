package ua.apraxia.eventapi.events.impl.player;

import ua.apraxia.eventapi.events.Event;
import ua.apraxia.eventapi.events.callables.EventCancellable;

public class EventPushBlock extends EventCancellable implements Event {

    private boolean canceled;


    public void setCanceled() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
