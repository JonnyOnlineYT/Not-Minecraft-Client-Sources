package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;

public class ParkourHelper extends Feature {

    public ParkourHelper() {
        super("ParkourHelper", "Автоматически прыгает на конце блока", Type.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.world.getCollisionBoxes(Minecraft.player, Minecraft.player.getEntityBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001)).isEmpty() && Minecraft.player.onGround && !Minecraft.gameSettings.keyBindJump.isKeyDown()) {
            Minecraft.player.jump();
        }
    }
}
