package org.moonware.client.settings.impl;

import org.moonware.client.settings.Setting;

import java.awt.*;
import java.util.function.Supplier;

public class ColorSetting extends Setting {

    private int color;
    private Color colorc;

    public ColorSetting(String name, int color, Supplier<Boolean> visible) {
        this.name = name;
        this.color = color;
        colorc = new Color(color);
        setVisible(visible);
    }
    public ColorSetting(String name, int color) {
        this.name = name;
        this.color = color;
        colorc = new Color(color);
        setVisible(() -> true);
    }
    public ColorSetting(String name, Color colorr) {
        this.name = name;
        color = colorr.getRGB();
        colorc = new Color(color);
        setVisible(() -> true);
    }

    public int getColorValue() {
        return color;
    }
    public int getRed() {
        return colorc.getRed();
    }
    public int getBlue() {
        return colorc.getBlue();
    }
    public int getGreen() {
        return colorc.getGreen();
    }
    public int getAlpha() {
        return colorc.getAlpha();
    }
    public int getColor() {
        return color;
    }
    public Color getColorc() {
        return new Color(color);
    }

    public void setColorValue(int color) {
        this.color = color;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public void setAlpha(int alpha) {
        color = new Color(getColorc().getRed(), getColorc().getGreen(),getColorc().getBlue(), alpha).getRGB();
    }
}
