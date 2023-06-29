package ru.rockstar.client.ui.settings.impl;

import java.util.function.Supplier;

import ru.rockstar.client.ui.settings.Setting;

public class ColorSetting extends Setting {

    private int color;

    public ColorSetting(String name, int color, Supplier<Boolean> visible) {
        this.name = name;
        this.color = color;
        setVisible(visible);
    }


	public int getColorValue() {
        return color;
    }

    public void setColorValue(int color) {
        this.color = color;
    }
}
