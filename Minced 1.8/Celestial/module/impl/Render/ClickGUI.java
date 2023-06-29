package Celestial.module.impl.Render;

import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.Smertnix;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.ColorSetting;
import Celestial.ui.settings.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ClickGUI extends Module {
    public static BooleanSetting shadow = new BooleanSetting("Shadow", true, () -> true);

    public static BooleanSetting blur = new BooleanSetting("Blur", false, () -> true);
    public static ColorSetting color;
    public static ColorSetting color2;
    public static NumberSetting speed;

    public ClickGUI() {
        super("Click GUI", "���� ����", ModuleCategory.Render);
        setBind(Keyboard.KEY_RSHIFT);
        color = new ColorSetting("Gui Color", new Color(158, 13, 239, 255).getRGB(), () -> true);
        color2 = new ColorSetting("Gui Color 2", new Color(0, 0, 0).getRGB(), () -> true);
        addSettings(color,color2, blur , shadow, speed);

    }

    @Override
    public void onEnable() {
        Helper.mc.displayGuiScreen(Smertnix.instance.clickGui);
        Smertnix.instance.featureManager.getFeature(ClickGUI.class).setEnabled(false);
        super.onEnable();
    }
}