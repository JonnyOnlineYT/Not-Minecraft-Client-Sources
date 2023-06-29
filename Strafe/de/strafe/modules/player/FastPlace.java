package de.strafe.modules.player;

import de.strafe.modules.Category;
import com.eventapi.EventTarget;
import de.strafe.events.EventUpdate;
import de.strafe.modules.Module;
import de.strafe.settings.impl.BooleanSetting;
import de.strafe.settings.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class FastPlace extends Module {

    public FastPlace() {
        super("FastPlace", 0, Category.PLAYER);
        addSettings(speed, reset);
    }

    public NumberSetting speed = new NumberSetting("Speed", 2, 1, 3, 1);
    public BooleanSetting reset = new BooleanSetting("Legit", false);

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!isToggled()) return;
        if(reset.enabled) {
            Minecraft.getMinecraft().rightClickDelayTimer *= 1.0;
        }
        else {
            Minecraft.getMinecraft().rightClickDelayTimer /= speed.getValue() + 0;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.getMinecraft().rightClickDelayTimer = 4;
    }

}
