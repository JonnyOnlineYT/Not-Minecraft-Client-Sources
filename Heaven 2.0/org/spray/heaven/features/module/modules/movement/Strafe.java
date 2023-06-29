package org.spray.heaven.features.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.UpdateEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.util.MovementUtil;

@ModuleInfo(name = "Strafe", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class Strafe extends Module {


    @EventTarget
    public void onUpdate(UpdateEvent eventUpdate) {
        if (!isEnabled()) {
            return;
        }
        if (MovementUtil.isMoving()) {
            if (MovementUtil.getSpeed() < 0.2177f) {
                MovementUtil.strafe();
                if (Math.abs(mc.player.movementInput.moveStrafe) < 0.1 && mc.gameSettings.keyBindForward.pressed) {
                    MovementUtil.strafe();
                }
                if (mc.player.onGround) {
                    MovementUtil.strafe();
                }
            }
        }
    }
}
