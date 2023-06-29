package ru.rockstar.api.event.event;


public class EventMouse extends EventCancellable
{
    public int key;
    
    public EventMouse(final int key) {
        this.key = key;
    }
}
