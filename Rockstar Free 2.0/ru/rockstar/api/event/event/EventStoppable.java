package ru.rockstar.api.event.event;

import ru.rockstar.api.event.Event2;

public abstract class EventStoppable implements Event2
{
    private boolean stopped;
    
    public void stop() {
        this.stopped = true;
    }
    
    public boolean isStopped() {
        return this.stopped;
    }
}
