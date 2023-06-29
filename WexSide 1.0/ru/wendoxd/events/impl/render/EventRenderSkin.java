package ru.wendoxd.events.impl.render;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventRenderSkin extends Event implements IEventCancelable {

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
