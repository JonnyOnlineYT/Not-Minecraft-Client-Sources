package org.spray.heaven.notifications;

import org.spray.heaven.features.module.modules.render.Notifications;
import org.spray.heaven.font.FontRenderer;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.notifications.Notification.Type;
import org.spray.heaven.protect.events.ClientInitializeEvent;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {

    private final List<Notification> notifications;

    public NotificationManager(ClientInitializeEvent event) {
        notifications = new ArrayList<>();
        event.check();
    }

    public void render(double height) {
        if (notifications.size() > 4)
            notifications.remove(0);

        double startY = height - 36;
        double lastY = startY;

        for (int i = 0; i < notifications.size(); i++) {
            Notification notification = notifications.get(i);
            notifications.removeIf(Notification::shouldDelete);

            notification.render(startY, lastY);
            startY -= notification.getHeight() + 3;
        }
    }

    public void call(String message, Type type, FontRenderer font) {
        if (Wrapper.getModule().get("Notifications").isEnabled())
            notifications.add(new Notification(message, type, font));
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

}
