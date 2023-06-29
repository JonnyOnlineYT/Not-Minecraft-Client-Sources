package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventSafeWalk extends Event implements IEventCancelable {

    private boolean canceled;

    @Override
    public void setCanceled() {
        canceled = true;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

}
