package ru.wendoxd.events.impl.block;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventWaterPush extends Event implements IEventCancelable {

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
