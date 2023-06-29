package de.strafe.modules.player;

import de.strafe.modules.Category;
import com.eventapi.EventTarget;
import de.strafe.events.EventUpdate;
import de.strafe.modules.Module;
import org.lwjgl.input.Keyboard;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall", 0, Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if ((int) mc.thePlayer.fallDistance % 2 == 0 && !mc.thePlayer.onGround) {
            mc.thePlayer.motionY -= 18F;
        }

    }

}
