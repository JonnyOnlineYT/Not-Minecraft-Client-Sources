package de.strafe.modules.gui;

import de.strafe.modules.Category;
import de.strafe.Strafe;
import de.strafe.modules.Module;
import org.lwjgl.input.Keyboard;

public class ClickGui extends Module {
    public ClickGui() {
        super("ClickGui", Keyboard.KEY_RSHIFT, Category.GUI);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new de.strafe.gui.dropdown.ClickGui());
        toggle();
        super.onEnable();
    }
}
