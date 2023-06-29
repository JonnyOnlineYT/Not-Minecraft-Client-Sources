package de.strafe.modules.movement;

import de.strafe.events.EventUpdate;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.utils.IMinecraft;
import com.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", 0, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!IMinecraft.mc.thePlayer.isCollidedHorizontally && IMinecraft.mc.thePlayer.moveForward > 0)
            IMinecraft.mc.thePlayer.setSprinting(true);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        IMinecraft.mc.thePlayer.setSprinting(false);
    }
}
