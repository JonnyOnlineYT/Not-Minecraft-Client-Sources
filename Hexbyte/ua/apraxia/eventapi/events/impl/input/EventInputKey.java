package ua.apraxia.eventapi.events.impl.input;


import ua.apraxia.eventapi.events.Event;

public class EventInputKey implements Event {

    private int key;

    public EventInputKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
