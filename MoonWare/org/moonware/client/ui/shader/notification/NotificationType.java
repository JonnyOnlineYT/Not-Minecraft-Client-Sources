package org.moonware.client.ui.shader.notification;

import java.awt.*;

public enum NotificationType {
    SUCCESS(new Color(100, 255, 100).getRGB()),
    INFO(new Color(54, 111, 159).getRGB()),
    ERROR(new Color(255, 100, 100).getRGB()),
    WARNING(new Color(255, 211, 53).getRGB());

    private final int color;

    NotificationType(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
