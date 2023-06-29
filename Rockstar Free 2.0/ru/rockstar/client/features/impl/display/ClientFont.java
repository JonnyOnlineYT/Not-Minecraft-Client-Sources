package ru.rockstar.client.features.impl.display;

import java.awt.Color;

import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class ClientFont extends Feature {
    public static ListSetting fontMode;
    public static BooleanSetting minecraftfont;
    public static ListSetting arrayColor;
    public static NumberSetting time;
    public static ColorSetting onecolor;
    public static ColorSetting twocolor;

    public ClientFont() {
        super("Client", "Позволяет менять клиентный фонт и цвет", 0, Category.DISPLAY);
        arrayColor = new ListSetting("ClientColor", "Custom", () -> true, new String[] { "Custom", "Rainbow", "Pulse", "Astolfo", "None" });
        time = new NumberSetting("ClientColor Time", 10.0f, 1.0f, 100.0f, 1.0f, () -> true);
        onecolor = new ColorSetting("One Color", new Color(1671168).getRGB(), () -> arrayColor.currentMode.equals("Custom"));
        twocolor = new ColorSetting("Two Color", new Color(1671168).getRGB(), () -> arrayColor.currentMode.equals("Custom"));
        
        fontMode = new ListSetting("ClientFont", "WexSide", () -> !minecraftfont.getBoolValue(), "URWGeometric", "Myseo", "SFUI", "Lato", "Roboto Regular", "WexSide", "NeverLose", "Comic Sans", "TenacityBold", "Tenacity", "TahomaBold", "Tahoma", "RubikBold", "Rubik");
        minecraftfont = new BooleanSetting("Minecraft Font", false, () -> true);
        
        addSettings(arrayColor, time, onecolor, twocolor, fontMode,minecraftfont);
    }

    @Override
    public void onEnable() {
        toggle();
        super.onEnable();
    }
}
