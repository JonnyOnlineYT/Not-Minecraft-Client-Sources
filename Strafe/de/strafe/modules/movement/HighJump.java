package de.strafe.modules.movement;

import com.eventapi.EventTarget;
import de.strafe.events.EventUpdate;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.utils.IMinecraft;
import com.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;

public class HighJump extends Module {
    public HighJump() {
        super("High Jump", 0, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {

    }


    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        IMinecraft.mc.thePlayer.motionY = 1.0F;
        IMinecraft.mc.timer.timerSpeed = 1.0F;
    }
}

