package ru.rockstar.client.features.impl.display;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.ScaledResolution;
import ru.rockstar.Main;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import java.awt.*;

public class ClickGUI extends Feature {
    public static BooleanSetting background;
    public static ListSetting backGroundMode;
    public static ListSetting imagemode;
    public static ListSetting style;
    public static BooleanSetting animation = new BooleanSetting("Animation", true, () -> true);
    public static BooleanSetting sound = new BooleanSetting("Sound", true, () -> true);
    public static NumberSetting animateSpeed = new NumberSetting("AnimationSpeed", "Позволяет менять скорость анимации", 1, 0.1f, 5, 0.1f, () -> animation.getBoolValue());
    public static BooleanSetting glow = new BooleanSetting("Glow", false, () -> true);
    public static BooleanSetting blur = new BooleanSetting("Blur", true, () -> true);
    public static ListSetting clickGuiColor = new ListSetting("ClickGui Color", "Astolfo", () -> true, "Astolfo", "Rainbow", "Static", "Color Two", "Client", "Fade");
    public static ColorSetting color;
    public static ColorSetting colorTwo;
    public static NumberSetting fontY;
    public static NumberSetting imagex;
    public static NumberSetting imagey;
    public ListSetting mode = new ListSetting("ClickGui Mode", "Default", () -> true,  "Default", "New", "Rockstar");
    
    public static NumberSetting speed = new NumberSetting("Speed", 35, 10, 100, 1, () -> true);
    public ClickGUI() {
        super("ClickGUI", "Cheat menu.", Keyboard.KEY_RSHIFT, Category.DISPLAY);
        setKey(Keyboard.KEY_RSHIFT);
        color = new ColorSetting("Color One", new Color(255, 255, 255, 120).getRGB(), () -> clickGuiColor.currentMode.equals("Fade") || clickGuiColor.currentMode.equals("Color Two") || clickGuiColor.currentMode.equals("Static"));
        colorTwo = new ColorSetting("Color Two", new Color(154, 154, 154, 120).getRGB(), () -> clickGuiColor.currentMode.equals("Color Two"));
        background = new BooleanSetting("Background", false, () -> true);
       // imagemode = new ListSetting("Image Mode", "DeadInside", () -> image.getBoolValue(), "DeadInside", "DeadInside2", "DeadInside3", "DeadInside4", "DeadInside5", "DeadInside6", "DeadInside7", "DeadInside8", "Allax", "Cat", "Floppa", "Floppa2", "Selli324", "Minecraft", "Putin", "Slava Bebrow", "Simple", "Tyan", "Tyan2", "Tyan3", "Tyan4", "Tyan5", "Tyan6", "Tyan7", "Tyan8", "Tyan9", "Tyan10", "Tyan11", "Tyan12", "Tyan13", "Tyan14", "Tyan15", "Tyan16", "Tyan17", "Tyan18", "Tyan19", "Tyan20", "Tyan21", "Tyan22", "Tyan23", "Tyan24", "Brawl", "Brawl2", "Brawl3", "Brawl4", "Brawl5");
        backGroundMode = new ListSetting("Background Mode", "Bottom", () -> background.getBoolValue(), "Bottom", "Top", "Everywhere");
        fontY = new NumberSetting("FontY", "Позволяет менять вам положение шрифта", 0, 0, 5, 1, () -> true);
        ScaledResolution sr = new ScaledResolution(mc);
        style = new ListSetting("Style", "Rockstar New", () -> true,  "Rockstar", "Rockstar New"/*,  "Default Dark", "Default Light", "NeverLose", "Clear", "Dark"*/);
        addSettings(clickGuiColor, color,colorTwo, speed, background,backGroundMode, glow,animation,animateSpeed, sound);
    }

    @Override
    public void onEnable() {
    	  mc.displayGuiScreen(Main.instance.clickGui);
    	if (mode.currentMode.equals("Default")) {
            mc.displayGuiScreen(Main.instance.clickGui);
        } else if (mode.currentMode.equals("Small")) {
         //   mc.displayGuiScreen(Main.instance.csgui);
        }
        else if (mode.currentMode.equals("New")) {
        	 //mc.displayGuiScreen(Main.instance.newClickGui);
        }
        else if (mode.currentMode.equals("Rockstar")) {
        	// mc.displayGuiScreen(Main.instance.windowGui);
        }
        Main.instance.featureDirector.getFeatureByClass(ClickGUI.class).setEnabled(false);
        super.onEnable();
    }
}