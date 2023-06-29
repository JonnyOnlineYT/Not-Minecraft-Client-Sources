package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DeathScreen;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class AutoRespawn extends Feature {

    public AutoRespawn() {
        super("AutoRespawn", "Автоматический респавн при смерти", Type.Other);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.player.getHealth() < 0 || !Minecraft.player.isEntityAlive() || Minecraft.screen instanceof DeathScreen) {
            Minecraft.player.respawnPlayer();
            Minecraft.openScreen(null);
        }
    }
}
