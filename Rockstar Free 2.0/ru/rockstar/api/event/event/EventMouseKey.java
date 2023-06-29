package ru.rockstar.api.event.event;

import ru.rockstar.api.event.Event;

public class EventMouseKey extends Event {

    private int key;

    public EventMouseKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}