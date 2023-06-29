package org.moonware.client.feature.impl.hud;

import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;

public class WaterMark extends Feature {

    public static ListSetting logoMode;
    public static ListSetting logoColor;
    public static ListSetting colorRectPosition = new ListSetting("ColorRect Position", "Bottom", () -> !logoMode.currentMode.equals("Skeet") && !logoMode.currentMode.equals("NeverLose") && !logoMode.currentMode.equals("Default") && !logoMode.currentMode.equalsIgnoreCase("Up-Gradient") && !logoMode.currentMode.equalsIgnoreCase("Up-Gradient") , "Bottom");
    public static ColorSetting customRect;
    public static BooleanSetting backgroundGradient = new BooleanSetting("BackGround Gradient",true,() -> false);
    public static ListSetting backgroundGradientColorList = new ListSetting("Gradient ColorList",() -> false, "Rainbow --> Astolfo", "Astolfo --> Rainbow","Rainbow --> Astolfo", "Full Custom");
    public static ColorSetting left_up_color = new ColorSetting("ColorThree",Color.BLUE.getRGB(),() -> false);
    public static ColorSetting left_down_color = new ColorSetting("ColorFour",Color.BLUE.getRGB(),() -> false);
    public static ColorSetting right_up_color = new ColorSetting("ColorOne",Color.BLUE.getRGB(),() -> false);
    public static ColorSetting right_down_color = new ColorSetting("ColorTwo",Color.BLUE.getRGB(),() -> false);
    public static NumberSetting rainbowSaturation;
    public static NumberSetting rainbowBright;
    public static BooleanSetting outline = new BooleanSetting("Outline",true,() -> false);
    public static ListSetting outlineColorMode = new ListSetting("Outline ColorList",()-> false, "Custom","Astolfo","Custom","Rainbow");
    public static ColorSetting customColor = new ColorSetting("Custom Outline Color",Color.BLUE.getRGB(),() -> false);
    public static NumberSetting colorTime = new NumberSetting("ColorTime", 30,1,100,1, ()-> false);

    public static ColorSetting customRectTwo;
    public static BooleanSetting username;
    public static BooleanSetting clientname;
    public static BooleanSetting servers;
    public static BooleanSetting fps;
    public static BooleanSetting time;
    public static BooleanSetting ping;
    public static ListSetting logMode;
    public static BooleanSetting glowEffect = new BooleanSetting("Glow Effect", false, () -> logoMode.currentMode.equals("OneTap v3") || logoMode.currentMode.equals("OneTap v2"));
    public static BooleanSetting shadowEffect = new BooleanSetting("Shadow Effect", false, () -> logoMode.currentMode.equals("NeverLose") || logoMode.currentMode.equals("OneTap v3") || logoMode.currentMode.equals("OneTap v2"));

    public WaterMark() {
        super("WaterMark", "Ватермарк чита", Type.Hud);
        rainbowSaturation = new NumberSetting("Rainbow Saturation", 0.8F, 0.1F, 1F, 0.1F, () -> false);
        rainbowBright = new NumberSetting("Rainbow Brightness", 1F, 0.1F, 1F, 0.1F, () -> false);

        logoMode = new ListSetting("Logo Mode", "Up-Gradient", () -> true, "Up-Gradient");
        clientname = new BooleanSetting("ClientName", false, () -> logoMode.currentMode.equals("Up-Gradient"));
        username = new BooleanSetting("Username", false, () -> logoMode.currentMode.equals("Up-Gradient"));
        servers = new BooleanSetting("Server", false, () -> logoMode.currentMode.equals("Up-Gradient"));
        fps = new BooleanSetting("Fps", false, () -> logoMode.currentMode.equals("Up-Gradient"));
        time = new BooleanSetting("Time", false, () -> logoMode.currentMode.equals("Up-Gradient"));
        ping = new BooleanSetting("Ping", false, () -> logoMode.currentMode.equals("Up-Gradient"));
        logoColor = new ListSetting("Logo Color", "Default", () -> !logoMode.currentMode.equals("NeverLose"), "Client", "Rainbow", "Gradient", "Static", "Default");
        customRect = new ColorSetting("Custom Rect Color", Color.PINK.getRGB(), () -> !logoMode.currentMode.equals("NeverLose") && !logoMode.currentMode.equalsIgnoreCase("Up-Gradient") && !logoMode.currentMode.equalsIgnoreCase("Up-Gradient"));
        customRectTwo = new ColorSetting("Custom Rect Color Two", Color.BLUE.getRGB(), () -> !logoMode.currentMode.equals("NeverLose") && !logoMode.currentMode.equals("Default") && !logoMode.currentMode.equalsIgnoreCase("Up-Gradient") && !logoMode.currentMode.equalsIgnoreCase("Up-Gradient"));
        addSettings(logoMode, backgroundGradient, backgroundGradientColorList, outline, colorTime, rainbowBright, rainbowSaturation, right_up_color, right_down_color, left_up_color, left_down_color, outlineColorMode, customColor, colorRectPosition, clientname, username, servers, fps, time, ping, logoColor, customRect, customRectTwo);
    }
}
