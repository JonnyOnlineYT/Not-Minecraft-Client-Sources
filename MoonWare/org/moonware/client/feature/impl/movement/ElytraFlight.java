package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.NumberSetting;

public class ElytraFlight extends Feature {

    public static NumberSetting motion;

    public ElytraFlight() {
        super("ElytraFlight", "Позволяет летать на элитрах без фейерверков", Type.Movement);
        motion = new NumberSetting("Elytra Speed", 1.5F, 0.5F, 5F, 0.5F, () -> true);
        addSettings(motion);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.player.isElytraFlying()) {
            Minecraft.player.onGround = false;
            Minecraft.player.setVelocity(0.0, 0.0, 0.0);
            if (Minecraft.gameSettings.keyBindSneak.isKeyDown())
                Minecraft.player.motionY = -motion.getNumberValue();
            if (Minecraft.gameSettings.keyBindJump.isKeyDown())
                Minecraft.player.motionY = motion.getNumberValue();
            if (MovementHelper.isMoving()) {
                MovementHelper.setSpeed(motion.getNumberValue());
            }
        }
    }

    @Override
    public void onDisable() {
        Minecraft.player.capabilities.isFlying = false;
        Minecraft.player.capabilities.setFlySpeed(0.05f);
        if (!Minecraft.player.capabilities.isCreativeMode) {
            Minecraft.player.capabilities.allowFlying = false;
        }
        super.onDisable();
    }
}