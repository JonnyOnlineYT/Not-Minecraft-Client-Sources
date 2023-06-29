package splash.client.managers.notifications;

import splash.api.notification.Notification;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager {
    private static LinkedBlockingQueue<Notification> pendingNotifications = new LinkedBlockingQueue<>();
    private static Notification currentNotification = null;

    public void show(Notification notification) {
        pendingNotifications.add(notification);
    }

    public static void update() {
        if (currentNotification != null && !currentNotification.isShown()) {
            currentNotification = null;
        }

        if (currentNotification == null && !pendingNotifications.isEmpty()) {
            currentNotification = pendingNotifications.poll();
            currentNotification.show();
        }

    }

    public void render() {
        update();

        if (currentNotification != null)
            currentNotification.render();
    }
}