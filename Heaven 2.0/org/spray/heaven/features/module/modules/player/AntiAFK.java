package org.spray.heaven.features.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.MovementUtil;
import org.spray.heaven.util.RotationUtil;
import org.spray.heaven.util.Timer;

@ModuleInfo(name = "AntiAFK", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class AntiAFK extends Module {

    private final Setting chat = register(new Setting("Send Chat Message", true));
    private final Setting delay = register(new Setting("Send Delay (ms)", 3000, 100, 60000).setVisible(chat::isToggle));
    private final Setting rotation = register(new Setting("Rotations", true));

    private final Timer timer = new Timer();

    private float spin = 0;

    @Override
    public void onDisable() {
        timer.reset();
    }

    @EventTarget
    public void onMotionTick(MotionEvent event) {
        if (!MovementUtil.isMoving()) {
            if (chat.isToggle()) {
                if (timer.hasReached(delay.getValue())) {
                    mc.player.sendChatMessage("/repair all");
                    mc.player.sendChatMessage("/feed");
                    timer.reset();
                }
            }
            if (rotation.isToggle()) {
                float rotation = RotationUtil.getFixedRotation(spin += 1);
                event.setYaw(rotation);
                mc.player.renderYawOffset = rotation;
                mc.player.rotationYawHead = rotation;
            }
        }
    }
}