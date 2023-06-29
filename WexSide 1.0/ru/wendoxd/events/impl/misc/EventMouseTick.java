package ru.wendoxd.events.impl.misc;

import ru.wendoxd.events.Event;

public class EventMouseTick extends Event {

    private int button;

    public EventMouseTick(int button) {
        this.button = button;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }
}
