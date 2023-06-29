package ru.rockstar.client.features.impl.display;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventRender2D;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.Translate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.draggable.impl.KeyBindsComponent;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class KeyBinds extends Feature {
    public static BooleanSetting backGround;
    public static ColorSetting backGroundColor;
    public static NumberSetting backGroundAlpha;
    public static ListSetting arrayColor;
    public static NumberSetting time;
    public static NumberSetting x;
    public static NumberSetting y;
    public static ListSetting mode = new ListSetting("Mode", "Rockstar Styled", () -> true, "Rockstar", "Rockstar New", "Rockstar Styled");
    float listOffset = 8.0F;
    public static NumberSetting fonty;
    public static ColorSetting onecolor = new ColorSetting("One Color", new Color(0xFFFFFF).getRGB(), () -> arrayColor.currentMode.equals("Custom"));
    public static ColorSetting twocolor = new ColorSetting("Two Color", new Color(0xFF0000).getRGB(), () -> arrayColor.currentMode.equals("Custom"));

    public KeyBinds() {
        super("KeyBinds", "Показывает список всех забинженных и включенных модулей", 0, Category.DISPLAY);
        
        arrayColor = new ListSetting("KeyBinds Color", "Custom", () -> true, "Custom", "Rainbow", "Pulse", "Astolfo", "None");
        backGround = new BooleanSetting("Background", true, () -> true);
        backGroundColor = new ColorSetting("Background Color",new Color(0x000000).getRGB(),() -> backGround.getBoolValue());
        backGroundAlpha = new NumberSetting("Background Alpha", 255,1,255,1,() -> true);
        time = new NumberSetting("Color Time", 10, 1, 100, 1, () -> true);
        x = new NumberSetting("KeyBinds X", 0, 0, 600, 1, () -> true);
        y = new NumberSetting("KeyBinds Y", 0, 0, 500, 1, () -> true);
        fonty = new NumberSetting("Font Y", 1, 0, 10, 1, () -> true);
        addSettings(mode, arrayColor,onecolor,twocolor,backGround,backGroundColor,backGroundAlpha,time,x,y, fonty);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = arrayColor.getOptions();
        this.setModuleName("KeyBinds §7" + mode);
    }
    
    public void renderKeyBinds(ScaledResolution sr) {
        
    }

    private static Feature getNextEnabledModule(ArrayList<Feature> features, int startingIndex) {
        for (int i = startingIndex; i < features.size(); i++) {
            Feature feature = features.get(i);
            if (feature.isToggled()) {
                if (!feature.getModuleName().equals("ClickGui")) {
                    return feature;
                }
            }
        }
        return null;
    }
    @EventTarget
    public void onRender2D(EventRender2D e) {
        HUD hud = new HUD();
        hud.renderKeyBinds(e.getResolution());
    }
}