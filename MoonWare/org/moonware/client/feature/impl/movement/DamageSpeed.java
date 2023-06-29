package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.movement.Move.MoveUtils;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.BooleanSetting;

public class DamageSpeed extends Feature {
    public BooleanSetting strafing = new BooleanSetting("Strafing", true);
    public BooleanSetting wait = new BooleanSetting("Wait Hurt","Ждать получение урона", false);
    public DamageSpeed() {
        super("DamageSpeed", "Вы получаете огромную скорость от урона", Type.Movement);
        this.addSettings(strafing,wait);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (wait.get() && mc.player.hurtTime == 0) {
            return;
        }
        if (MovementHelper.isMoving()) {
            if (strafing.get())
                MoveUtils.setStrafe(MoveUtils.getSpeed());
            if (Minecraft.player.onGround) {
                Minecraft.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * 9.8 / 24.5, 0, Math.cos(MovementHelper.getDirection()) * 9.8 / 24.5);
                //MovementHelper.strafePlayer();
            } else if (Minecraft.player.isInWater()) {
                Minecraft.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * 8.5 / 24.5, 0, Math.cos(MovementHelper.getDirection()) * 9.5 / 24.5);
                //MovementHelper.strafePlayer();
            } else if (!Minecraft.player.onGround) {
                //mc.player.addVelocity(-Math.sin(MovementHelper.getDirection2()) * 0.11 / 24.5, 0, Math.cos(MovementHelper.getDirection2()) * 0.11 / 24.5);
                //MovementHelper.strafePlayer();
            } else {
                Minecraft.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * 0.005 * MovementHelper.getSpeed(), 0, Math.cos(MovementHelper.getDirection()) * 0.005 * MovementHelper.getSpeed());
                //MovementHelper.strafePlayer();

            }
        }
    }
}
