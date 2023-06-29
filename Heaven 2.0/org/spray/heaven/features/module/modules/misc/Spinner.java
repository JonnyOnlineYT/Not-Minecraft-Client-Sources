package org.spray.heaven.features.module.modules.misc;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.RotationUtil;

@ModuleInfo(name = "Spinner", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class Spinner extends Module {

    private Setting speed = register(new Setting("Speed", 9, 0, 10));

    private float yaw;

    @EventTarget
    public void onMotionTick(MotionEvent event) {
        event.setYaw(yaw);
        mc.player.rotationYawHead = yaw;
        mc.player.renderYawOffset = yaw;

        yaw += speed.getValue();
        yaw = RotationUtil.getFixedRotation(yaw);
    }

}
