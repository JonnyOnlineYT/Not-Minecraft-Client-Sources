package ru.rockstar.client.features;

import java.awt.*;

public enum Category {
    COMBAT(new Color(220, 20, 60).getRGB(), new Color(137, 3, 42).getRGB(), "Combat"),
    MOVEMENT(new Color(123, 104, 238).getRGB(), new Color(73, 63, 151).getRGB(), "Movement"),
    VISUALS(new Color(0, 206, 209).getRGB(), new Color(2, 121, 123).getRGB(), "Visuals"),
    PLAYER(new Color(244, 164, 96).getRGB(), new Color(132, 68, 9).getRGB(), "Player"),
    MISC(new Color(90, 10, 190).getRGB(), new Color(90, 10, 120).getRGB(), "Misc"),
    DISPLAY(new Color(186, 85, 211).getRGB(), new Color(91, 41, 102).getRGB(), "Display");

    private final int color;
    private final int color2;

    public String name;

    Category(int color, int color2, String name) {
        this.color = color;
        this.color2 = color2;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getColor() {
        return this.color;
    }

    public int getColor2() {
        return this.color2;
    }
}