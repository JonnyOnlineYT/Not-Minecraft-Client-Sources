package de.strafe.modules.combat;

import de.strafe.modules.Category;
import com.eventapi.EventTarget;
import de.strafe.events.EventPacket;
import de.strafe.modules.Module;
import org.lwjgl.input.Keyboard;

public class Reach extends Module {
    public Reach() {
        super("Reach", 0, Category.COMBAT);
    }

    @EventTarget
    public void onUpdate(EventPacket event) {
	//und jetzt noch in getBlockReachDistance in PlayerControllerMP  
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
