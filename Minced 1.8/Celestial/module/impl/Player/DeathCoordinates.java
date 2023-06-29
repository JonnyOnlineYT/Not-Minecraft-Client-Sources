package Celestial.module.impl.Player;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.other.ChatUtils;
import Celestial.ui.notif.NotifModern;
import Celestial.ui.notif.NotifRender;
import net.minecraft.client.gui.GuiGameOver;

public class DeathCoordinates
        extends Module {
    public DeathCoordinates() {
        super("DeathCoords", "Показывает координаты вашей смерти", ModuleCategory.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Helper.mc.player.getHealth() < 1.0f && Helper.mc.currentScreen instanceof GuiGameOver) {
            int x = Helper.mc.player.getPosition().getX();
            int y = Helper.mc.player.getPosition().getY();
            int z = Helper.mc.player.getPosition().getZ();
            if (Helper.mc.player.deathTime < 1) {
                NotifRender.queue("DeathCoords", "X: " + x + " Y: " + y + " Z: " + z, 10, NotifModern.INFO);
                ChatUtils.addChatMessage("DeathCoords: X: " + x + " Y: " + y + " Z: " + z);
            }
        }
    }
}
