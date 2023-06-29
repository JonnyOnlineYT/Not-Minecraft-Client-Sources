package ru.rockstar.client.features.impl.player;

import net.minecraft.client.gui.GuiGameOver;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.notifications.Notification;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class DeathCoordinates extends Feature {

    public DeathCoordinates() {
        super("DeathCoordinates", "ѕосле смерти пишит ваши координаты в чат", 0, Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getHealth() < 1 && mc.currentScreen instanceof GuiGameOver) {
            int x = mc.player.getPosition().getX();
            int y = mc.player.getPosition().getY();
            int z = mc.player.getPosition().getZ();
            if (mc.player.ticksExisted % 20 == 0) {
                NotificationPublisher.queue("Death Coordinates", "X: " + x + " Y: " + y + " Z: " + z, NotificationType.INFO);
                Main.msg("Death Coordinates: " + "X: " + x + " Y: " + y + " Z: " + z, true);
            }
        }
    }
}