package ru.rockstar.api.event.event;

import net.minecraft.client.gui.ScaledResolution;
import ru.rockstar.api.event.Event;

public class Event2D extends Event {
    private float width, height;
    private ScaledResolution resolution;

    public Event2D(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    public ScaledResolution getResolution() {
        return resolution;
    }

}


