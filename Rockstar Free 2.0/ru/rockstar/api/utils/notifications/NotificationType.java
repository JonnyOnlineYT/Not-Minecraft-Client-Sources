package ru.rockstar.api.utils.notifications;

import java.awt.*;

public enum  NotificationType {
    SUCCESS(new Color(79,117,72), "R"),
    INFO(new Color(100, 100, 255), "p"),
    ERROR(new Color(255, 79, 79), "Q"),
    WARNING(new Color(255, 211, 53), "r");

    private final Color color;
    private final String colorstr;

    NotificationType(Color color, String str) {
        this.color = color;
        this.colorstr = str;
    }

    public final Color getColor() {
        return this.color;
    }

    public final String getColorstr() {
        return colorstr;
    }
}
