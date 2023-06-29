package org.moonware.client.feature.impl.hud;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.utils.MWUtils;

import java.awt.*;

public class ClickGui extends Feature {
    public static ListSetting Guimode = new ListSetting("ClickGui Mode", "Default", () -> true, "Default","Scroll","WILD");
    public static BooleanSetting background;
    public static int opacite = 254;
    public static BooleanSetting blur = new BooleanSetting("Blur", true, () -> true);
    public static ColorSetting color;
    public static ColorSetting colorAll;
    public static ColorSetting colorTwo;
    public static NumberSetting glowRadius;
    public static NumberSetting speed = new NumberSetting("Speed", 35, 10, 100, 1, () -> true);
    public static NumberSetting scrollSpeed = new NumberSetting("ScrollSpeed", 20, 1, 100, 1, () -> true);
    public static BooleanSetting glow = new BooleanSetting("Glow Effect", false, () -> !Guimode.currentMode.equalsIgnoreCase("Default") &&  !Guimode.currentMode.equalsIgnoreCase("WILD"));
    public static NumberSetting glowStrengh = new NumberSetting("glow strength horizontally", 0,-5,5,0.01F, () -> false);
    public static NumberSetting glowStrenghver = new NumberSetting("glow strength vertically", 0,-5,5,1F, () -> false);
    public static ColorSetting glowColor = new ColorSetting("Glow Color", ClientHelper.getClientColor().getRGB(), () -> glow.getBoolValue());
    public static final NumberSetting clickHeight = new NumberSetting("Tab Height", 250, 500, 100, 1, () -> false);


    public static ColorSetting listGlowColor = new ColorSetting("List Glow Color", ClientHelper.getClientColor().getRGB(), () -> glow.getBoolValue());
    public static ColorSetting glowColornumbercircle = new ColorSetting("NumberCicle Glow Color", PaletteHelper.fadeColor(new Color(197, 35, 35,80).getRGB(), new Color(0, 66, 241,90).getRGB(), 1), () -> glow.getBoolValue());
    public static ListSetting mode = new ListSetting("ClickGui Mode", "Black", () -> true, "Black");

    public static BooleanSetting shadow = new BooleanSetting("Shadow Effect", false, () -> Guimode.currentMode.equalsIgnoreCase("Celestial"));
    public static BooleanSetting sliderGlow = new BooleanSetting("Slider Glow", false, () -> glow.getBoolValue() && Guimode.currentMode.equalsIgnoreCase("Celestial"));
    public static BooleanSetting checkBoxGlow = new BooleanSetting("BooleanSettingComponent Effect", false, () -> glow.getBoolValue() && Guimode.currentMode.equalsIgnoreCase("Celestial"));
    public static BooleanSetting searchGlow = new BooleanSetting("Search Effect", false, () -> glow.getBoolValue() && Guimode.currentMode.equalsIgnoreCase("Celestial"));
    public static ColorSetting backgroundColor;
    public static ColorSetting backgroundColor2;
    public static NumberSetting scale;
    public static ListSetting scrollMode;
    public static BooleanSetting scrollInversion;
    public static BooleanSetting PanelGlow;
    public static ColorSetting booleanbackcolor;
    public static ColorSetting booleanbackcolor2;
    public static ListSetting booleanbackcolormode;
    public static ListSetting panelMode;
    public static ListSetting girlMode;
    public static BooleanSetting girl;
    public static BooleanSetting arrows;
    public static BooleanSetting backGroundBlur;
    public static NumberSetting backGroundBlurStrength;
    public static ListSetting glowMode;
    public static BooleanSetting downBorder;

    public static ColorSetting rectColor = new ColorSetting("Rect Color", new Color(40, 40, 40, 155).getRGB(), () -> Guimode.currentMode.equalsIgnoreCase("Celestial"));
    public static ListSetting clickGuiColor = new ListSetting("ClickGui Color", "Fade", () -> !(Guimode.currentMode.equals("Default")|| Guimode.currentMode.equals("Scroll")), "Astolfo", "Rainbow", "Static", "Color Two", "Client", "Fade");
    public static BooleanSetting глов = new BooleanSetting("GlowNumber Effect", false, () -> false);
    public static ColorSetting glowColornumber = new ColorSetting("Number Glow Color", ClientHelper.getClientColor().getRGB(), () -> false);
    public static final ListSetting colorMode = new ListSetting("Color Mode", () -> Guimode.getCurrentMode().equalsIgnoreCase("Tenacity"), "Sync", "Sync", "Double Color", "Static", "Dynamic", "Dynamic Sync");
    public static final ListSetting settingAccent = new ListSetting("Setting Accent",() -> Guimode.getCurrentMode().equalsIgnoreCase("Tenacity")&& !(Guimode.currentMode.equals("Default")|| Guimode.currentMode.equals("Scroll")), "White", "White", "Color");

    public static BooleanSetting colored = new BooleanSetting("Colored", false,() -> (Guimode.currentMode.equals("Default")|| Guimode.currentMode.equals("Scroll")));
    public static BooleanSetting selfColors = new BooleanSetting("CustomColors", false, () -> false);

    public static ListSetting colorListof = new ListSetting("Color","Default", ()->  (Guimode.currentMode.equals("Default")|| Guimode.currentMode.equals("Scroll")) && colored.get(), "Default","Rainbow","Custom","Astolfo");
    public static ColorSetting colorOnee = new ColorSetting("ColorOne", new Color(241,0,0).getRGB(), () -> (Guimode.currentMode.equals("Default")|| Guimode.currentMode.equals("Scroll")) && colored.get() &&colorListof.currentMode.equalsIgnoreCase("Custom"));
    public static ColorSetting colorTwoo = new ColorSetting("ColorTwo", new Color(139, 63, 180).getRGB(), () -> (Guimode.currentMode.equals("Default")|| Guimode.currentMode.equals("Scroll")) && colored.get() &&  colorListof.currentMode.equalsIgnoreCase("Custom"));

    public ClickGui() {
        super("ClickGui", "Открывает клик гуй чита", Type.Hud);
        setBind(Keyboard.KEY_RSHIFT);
        downBorder = new BooleanSetting("Down Border", "Бордер под панелями", true, () -> false);
        booleanbackcolormode = new ListSetting("Bsetting ColorMode", "Fade", () -> false, "Astolfo", "Rainbow", "Static", "Color Two", "Client", "Fade");
        booleanbackcolor = new ColorSetting("BsettingBackground", new Color(164, 60, 46,255).getRGB(), () -> false);
        booleanbackcolor2 = new ColorSetting("Bsetting Color Two",new Color(129, 50, 37,255).getRGB(), () -> false);
        PanelGlow = new BooleanSetting("PanelBackGlow", false, () -> true);
        scale = new NumberSetting("Scale", 1.0f, 0.5f, 1.0f, 0.1f, () -> !(Guimode.currentMode.equals("Default")|| Guimode.currentMode.equals("Scroll")));
        scrollMode = new ListSetting("Scroll Mode", "All Panels",() -> true, "All Panels", "One Panel","In-Panels");
        scrollInversion = new BooleanSetting("Scroll Inversion", true, () -> true);
        glowRadius = new NumberSetting("Glow Radius", 18.0f, 1.0f, 100.0f, 1.0f, () -> glow.getBoolValue() && glowMode.getCurrentMode().equalsIgnoreCase("OldSDFSGEWG"));
        colorAll = new ColorSetting("ClickGui color", new Color(132, 0, 255, 120).getRGB(), () -> true);
        color = new ColorSetting("Color", new Color(0, 21, 255, 120).getRGB(), () -> clickGuiColor.currentMode.equals("Fade") || clickGuiColor.currentMode.equals("Color Two") || clickGuiColor.currentMode.equals("Static"));
        colorTwo = new ColorSetting("Color Two", new Color(132, 0, 255, 120).getRGB(), () -> clickGuiColor.currentMode.equals("Color Two"));
        background = new BooleanSetting("BG", true, () -> true);
        backgroundColor = new ColorSetting("BGColorOne", new Color(57, 75, 45, 94).getRGB(), () -> background.getBoolValue() && !(Guimode.currentMode.equals("Default")|| Guimode.currentMode.equals("Scroll")));
        backgroundColor2 = new ColorSetting("BGColorTwo", new Color(57, 75, 45, 94).getRGB(), () -> background.getBoolValue() && !Guimode.getCurrentMode().equalsIgnoreCase("Celestial") && !Guimode.getCurrentMode().equalsIgnoreCase("CSGO") && !(Guimode.currentMode.equals("Default")|| Guimode.currentMode.equals("Scroll")));
        girl = new BooleanSetting("Girl", false, () -> Guimode.currentMode.equalsIgnoreCase("Default") || Guimode.currentMode.equalsIgnoreCase("Celestial"));
        arrows = new BooleanSetting("Arrows", false, () -> Guimode.currentMode.equalsIgnoreCase("ssds"));
        backGroundBlur = new BooleanSetting("BG Blur", false, () -> Guimode.currentMode.equalsIgnoreCase("Celestial") || Guimode.currentMode.equalsIgnoreCase("Window"));
        backGroundBlurStrength = new NumberSetting("Blur Strength", 15.0f, 1.0f, 30.0f, 1.0f, () -> backGroundBlur.getCurrentValue() && Guimode.currentMode.equalsIgnoreCase("Celestial"));
        panelMode = new ListSetting("Panel Mode", "Rect",() -> Guimode.currentMode.equalsIgnoreCase("Default") || Guimode.currentMode.equalsIgnoreCase("Celestial"), "Rect", "Blur");
        glowMode = new ListSetting("Glow Mode", "Old", () -> false, "Old","Rect");
        girlMode = new ListSetting("Girl Mode", "Girl1", () -> girl.getCurrentValue() && Guimode.currentMode.equalsIgnoreCase("Default") || girl.getBoolValue() && Guimode.getCurrentMode().equalsIgnoreCase("Celestial"), "Girl1", "Girl2", "Girl3", "Girl4", "Girl5", "Girl6","Girl7", "Girl9", "Girl10", "Girl11", "Girl12","Girl13", "Girl14", "Girl15", "Girl16", "Girl17", "Girl18","Girl19", "Girl20", "Random");
        addSettings(colored, selfColors,colorListof, colorOnee, colorTwoo, rectColor, clickHeight, colorMode, scrollMode, Guimode, panelMode, girlMode, girl, backGroundBlur, backGroundBlurStrength, arrows, booleanbackcolormode, downBorder, scrollInversion, scrollSpeed, scale, clickGuiColor, color, colorTwo, booleanbackcolor, booleanbackcolor2, glow, глов, glowMode, glowRadius, glowColornumber, glowStrengh, glowStrenghver, blur, background, backgroundColor, backgroundColor2);
    }

    @Override
    public void onEnable() {
        //Guimode.setCurrentMode("Default");

        if (mode.currentMode.equals("Black") || mode.currentMode.equals("White")) {

        }
        if (Guimode.currentMode.equalsIgnoreCase("Default")) {
            Minecraft.openScreen(MoonWare.clickGui);
        }else if (Guimode.currentMode.equalsIgnoreCase("CSGO")){
            MWUtils.sendChat("Гуи в данный момент не доработана, открываю обычную гуишку");
           Minecraft.openScreen(MoonWare.clickGui);
            //mc.displayGuiScreen(new Csgui());
        }else if (Guimode.currentMode.equalsIgnoreCase("Celestial")) {
            //mc.displayGuiScreen(MoonWare.clickGui);
            Minecraft.openScreen(MoonWare.celestialGui);
        }else if (Guimode.currentMode.equalsIgnoreCase("WILD")) {
            Minecraft.openScreen(MoonWare.windowGui);
            //mc.displayGuiScreen(MoonWare.windowGui);
        }
        if (Guimode.currentMode.equalsIgnoreCase("Scroll")) {
            Minecraft.openScreen(MoonWare.guiNew);
        }
        MoonWare.featureManager.getFeatureByClass(ClickGui.class).setState(false);
        super.onEnable();
    }
}
