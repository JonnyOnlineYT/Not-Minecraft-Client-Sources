//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ua.apraxia.notification;

import java.awt.Color;

public enum NotificationMode {
    SUCCESS("Success", new Color(0, 255, 0), "o"),
    WARNING("WARNING", new Color(255, 128, 0), "l"),
    INFO("Information", new Color(255, 255, 255), "o");

    private final String iconString;
    private final String titleString;
    public Color color;

    private NotificationMode(String titleString, Color color, String iconString) {
        this.titleString = titleString;
        this.color = color;
        this.iconString = iconString;
    }

    public final String getIconString() {
        return this.iconString;
    }

    public final Color getColor() {
        return this.color;
    }

    public final String getTitleString() {
        return this.titleString;
    }
}
