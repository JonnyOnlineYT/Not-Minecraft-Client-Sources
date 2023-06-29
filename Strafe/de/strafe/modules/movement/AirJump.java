package de.strafe.modules.movement;

import com.eventapi.EventTarget;
import de.strafe.events.EventUpdate;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.utils.IMinecraft;
import de.strafe.utils.TimeUtil;
import org.lwjgl.input.Keyboard;

public class AirJump extends Module {
    public AirJump() {
        super("Air Jump", 0, Category.MOVEMENT);
    }

    public TimeUtil time = new TimeUtil();

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (IMinecraft.mc.gameSettings.keyBindJump.isKeyDown()) {
            if (time.hasReached(500) && !IMinecraft.mc.thePlayer.onGround) {
                IMinecraft.mc.thePlayer.motionY = 1F;
                time.reset();
                IMinecraft.mc.thePlayer.motionY = 0.6F;
            }
            if (!IMinecraft.mc.thePlayer.onGround && IMinecraft.mc.thePlayer.fallDistance >= 8) {
                IMinecraft.mc.thePlayer.motionY = 0.0F;
                IMinecraft.mc.timer.timerSpeed = 1.0F;
                if (time.hasReached(12000)) {
                    IMinecraft.mc.thePlayer.motionY = 1.0F;
                }
            }

        }

        if (IMinecraft.mc.thePlayer.onGround) {
            IMinecraft.mc.thePlayer.motionY = 0.5F;
        }
    }


    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        IMinecraft.mc.thePlayer.motionY = 0.0F;
        IMinecraft.mc.timer.timerSpeed = 1.0F;
        if (time.hasReached(28000)) {
            IMinecraft.mc.thePlayer.motionY = 1.0F;
        }
    }
}

