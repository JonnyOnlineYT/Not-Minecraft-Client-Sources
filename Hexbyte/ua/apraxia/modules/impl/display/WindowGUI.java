package ua.apraxia.modules.impl.display;

import ua.apraxia.Hexbyte;
import ua.apraxia.modules.Categories;
import org.lwjgl.input.Keyboard;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;

public class WindowGUI extends Module {
    public static BooleanSetting blur = new BooleanSetting("Blur", true);
    public static BooleanSetting filter = new BooleanSetting("GrayFilter", false);

    public WindowGUI() {
        super("WindowGUI", Categories.Display);
        addSetting(blur, filter);
        moduleKey = Keyboard.KEY_RSHIFT;
    }
    @Override
    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(Hexbyte.getInstance().clickUI);
        toggle();
    }

}
