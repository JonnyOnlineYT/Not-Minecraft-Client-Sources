package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DeathScreen;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

public class DeathCoordinates extends Feature {

    public DeathCoordinates() {
        super("DeathCoordinates", "Показывает координаты смерти игрока", Type.Other);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.player.getHealth() < 1 && Minecraft.screen instanceof DeathScreen) {
            int x = Minecraft.player.getPosition().getX();
            int y = Minecraft.player.getPosition().getY();
            int z = Minecraft.player.getPosition().getZ();
            if (Minecraft.player.ticksExisted % 20 == 0) {
                NotificationManager.publicity("Death Coordinates", "X: " + x + " Y: " + y + " Z: " + z, 10, NotificationType.INFO);
                MWUtils.sendChat("Death Coordinates: " + "X: " + x + " Y: " + y + " Z: " + z);
            }
        }
    }
}