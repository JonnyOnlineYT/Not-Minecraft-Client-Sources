package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.movement.Move.MoveUtils;
import org.moonware.client.settings.impl.NumberSetting;

public class Strafe extends Feature {

    public NumberSetting speed = new NumberSetting("Strafe Speed", 0.1F, 0.1F, 1, 0.01F, () -> true);

    public Strafe() {
        super("Strafe", "Ты можешь стрейфиться", Type.Movement);
        addSettings(speed);
    }

    @EventTarget
    public void onPlayerTick(EventUpdate e) {
        if (!Minecraft.gameSettings.keyBindForward.pressed)
            return;

        if (MoveUtils.isMoving() && MoveUtils.getSpeed() < 0.2177f) {
            MoveUtils.strafe();
            if (Math.abs(Strafe.mc.player.movementInput.moveStrafe) < 0.1 && Strafe.mc.gameSettings.keyBindForward.pressed) {
                MoveUtils.strafe();
            }
            if (Strafe.mc.player.onGround) {
                MoveUtils.strafe();
            }
        }
    }
}