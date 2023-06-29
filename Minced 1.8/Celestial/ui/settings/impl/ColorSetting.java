package Celestial.ui.settings.impl;


import Celestial.ui.settings.Setting;

import java.awt.*;
import java.util.function.Supplier;

public class ColorSetting extends Setting {


    public int color;

    public ColorSetting(String name, int color, Supplier<Boolean> visible) {
        this.name = name;
        this.color = color;
        setVisible(visible);
    }
    public ColorSetting(String name, int color) {
        this.name = name;
        this.color = color;
        setVisible(() -> true);
    }
    public int getColorValue() {
        return color;
    }
    public Color getColorValueColor() {
        return new Color(color);
    }
    public void setColorValue(int color) {
        this.color = color;
    }
}
