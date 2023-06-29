package de.strafe.modules.movement;

import de.strafe.events.EventUpdate;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.utils.IMinecraft;
import com.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;

public class Step extends Module {

    public Step() {
        super("Step", 0, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (IMinecraft.mc.thePlayer.isCollidedHorizontally) {
            IMinecraft.mc.thePlayer.stepHeight = 1.2F;
        }
        if (IMinecraft.mc.thePlayer.stepHeight >= 1.2 && IMinecraft.mc.thePlayer.onGround) {
            IMinecraft.mc.thePlayer.jump();
            IMinecraft.mc.thePlayer.stepHeight = 0.6F;
        }

    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        IMinecraft.mc.thePlayer.stepHeight = 0.6F;
    }

}
