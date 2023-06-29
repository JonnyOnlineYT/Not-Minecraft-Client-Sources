package ua.apraxia.modules.impl.other;

import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.notification.NotificationMode;
import ua.apraxia.notification.NotificationRenderer;
import ua.apraxia.utility.Utility;
import ua.apraxia.utility.other.ChatUtility;
import net.minecraft.client.gui.GuiGameOver;

public class DeathCoords extends Module {
    public DeathCoords() {
        super("DeathCoords", Categories.Other);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Utility.mc.player.getHealth() < 1.0f && Utility.mc.currentScreen instanceof GuiGameOver) {
            int x = Utility.mc.player.getPosition().getX();
            int y = Utility.mc.player.getPosition().getY();
            int z = Utility.mc.player.getPosition().getZ();
            if (Utility.mc.player.deathTime < 1) {
                NotificationRenderer.queue("Корды", "X: " + x + " Y: " + y + " Z: " + z, 15, NotificationMode.WARNING);
                ChatUtility.addChatMessage("Корды смерти: X: " + x + " Y: " + y + " Z: " + z);
            }
        }
    }
}
