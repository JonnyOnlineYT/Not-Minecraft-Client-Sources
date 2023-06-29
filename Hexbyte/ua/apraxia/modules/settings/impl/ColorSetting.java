package ua.apraxia.modules.settings.impl;

import ua.apraxia.modules.settings.Setting;

import java.awt.*;

public class ColorSetting extends Setting {

    public int color;

    public ColorSetting(String name, int color) {
        super(name);
        this.color = color;
    }
    public Color getColorc() {
        return new Color(color);
    }
    public int getColorValue() {
        return color;
    }
    public void setColorValue(int color) {
        this.color = color;
    }
}
