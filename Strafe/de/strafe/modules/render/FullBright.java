package de.strafe.modules.render;

import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.utils.IMinecraft;
import org.lwjgl.input.Keyboard;

public class FullBright extends Module {
    private float oldBrightness;

    public FullBright() {
        super("Fullbright", 0, Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        oldBrightness = IMinecraft.mc.gameSettings.gammaSetting;
        IMinecraft.mc.gameSettings.gammaSetting = Float.MAX_VALUE;
    }

    @Override
    public void onDisable() {
        IMinecraft.mc.gameSettings.gammaSetting = oldBrightness;
        super.onDisable();
    }
}

