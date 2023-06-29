package org.moonware.client.feature.impl.hud.ArrayGlowComp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.ScreenHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ArrayList2 extends Feature {
    public static ListSetting colorList;
    public static ListSetting sortMode;
    public static ListSetting fontRenderType;
    public static BooleanSetting showSuffix;
    public static ListSetting suffixType;
    public static ListSetting animationMode;
    public static NumberSetting risingY;
    public static BooleanSetting shadow;
    public static ColorSetting shadowCustomColor;
    public static NumberSetting shadowRadius;
    public static BooleanSetting backGround;
    public static ColorSetting backGroundColor;
    public static BooleanSetting border;
    public static BooleanSetting rightBorder;
    public static ColorSetting onecolor;
    public static ColorSetting twocolor;
    public static NumberSetting time;
    public static NumberSetting x;
    public static NumberSetting y;
    public static NumberSetting offset;
    public static NumberSetting borderWidth;
    public static NumberSetting rainbowSaturation;
    public static NumberSetting rainbowBright;
    public static NumberSetting fontX;
    public static NumberSetting fontY;
    public static NumberSetting backGroundLeft;
    public static BooleanSetting noVisualModules;

    public ArrayList2() {
        super("ArrayList2", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0441\u043f\u0438\u0441\u043e\u043a \u0432\u0441\u0435\u0445 \u0432\u043a\u043b\u044e\u0447\u0435\u043d\u043d\u044b\u0445 \u043c\u043e\u0434\u0443\u043b\u0435\u0439", Type.Hud);
        colorList = new ListSetting("FeatureList Color", "Astolfo", () -> true, "Astolfo", "Static", "Client", "Fade", "Rainbow", "Pulse", "Custom", "None", "Category");
        showSuffix = new BooleanSetting("Show Suffix", true, () -> true);
        suffixType = new ListSetting("Suffix Type", "None", () -> showSuffix.getCurrentValue(), "None", "<>", "()", "[]", "|", "-", "~");
        animationMode = new ListSetting("Animation Mode", "Horizontal", () -> true, "Horizontal", "Rising");
        risingY = new NumberSetting("Rising Y", 50.0f, 5.0f, 50.0f, 1.0f, () -> animationMode.currentMode.equals("Rising"));
        shadow = new BooleanSetting("Shadow", false, () -> true);
        shadowCustomColor = new ColorSetting("Shadow Custom Color", Color.BLACK.getRGB(), () -> shadow.getCurrentValue());
        shadowRadius = new NumberSetting("Shadow Radius", 40.0f, 1.0f, 100.0f, 1.0f, shadow::getCurrentValue);
        sortMode = new ListSetting("FeatureList Sort", "Length", () -> true, "Length", "Alphabetical");
        fontRenderType = new ListSetting("FontRender Type", "Shadow", () -> true, "Default", "Shadow", "Outline");
        backGround = new BooleanSetting("Background", true, () -> true);
        backGroundColor = new ColorSetting("Background Color", Color.BLACK.getRGB(), () -> backGround.getCurrentValue());
        border = new BooleanSetting("Border", false, () -> true);
        rightBorder = new BooleanSetting("Right Border", true, () -> true);
        backGroundLeft = new NumberSetting("BackgroundLeft", 2.0f, 0.0f, 10.0f, 0.5f, () -> backGround.getCurrentValue());
        onecolor = new ColorSetting("One Color", new Color(0xFFFFFF).getRGB(), () -> colorList.currentMode.equals("Fade") || colorList.currentMode.equals("Custom") || colorList.currentMode.equals("Static"));
        twocolor = new ColorSetting("Two Color", new Color(0xFF0000).getRGB(), () -> colorList.currentMode.equals("Custom"));
        rainbowSaturation = new NumberSetting("Rainbow Saturation", 0.8f, 0.1f, 1.0f, 0.1f, () -> colorList.currentMode.equals("Rainbow"));
        rainbowBright = new NumberSetting("Rainbow Brightness", 1.0f, 0.1f, 1.0f, 0.1f, () -> colorList.currentMode.equals("Rainbow"));
        time = new NumberSetting("Color Time", 30.0f, 1.0f, 100.0f, 1.0f, () -> true);
        fontX = new NumberSetting("FontX", 0.0f, 0.0f, 20.0f, 0.5f, () -> true);
        fontY = new NumberSetting("FontY", 0.0f, 0.0f, 20.0f, 0.5f, () -> true);
        x = new NumberSetting("FeatureList X", 0.0f, 0.0f, 500.0f, 1.0f, () -> true);
        y = new NumberSetting("FeatureList Y", 0.0f, 0.0f, 500.0f, 1.0f, () -> true);
        offset = new NumberSetting("Font Offset", 11.0f, 7.0f, 20.0f, 0.5f, () -> true);
        borderWidth = new NumberSetting("Border Width", 1.0f, 0.0f, 10.0f, 0.1f, () -> rightBorder.getCurrentValue());
        noVisualModules = new BooleanSetting("No Visual Modules", false, () -> true);
        addSettings(sortMode, colorList, fontRenderType, showSuffix, suffixType, animationMode, risingY, border, rightBorder, borderWidth, shadow, shadowCustomColor, shadowRadius, backGround, backGroundColor, backGroundLeft, onecolor, twocolor, rainbowSaturation, rainbowBright, time, x, y, fontX, fontY, offset, noVisualModules);
    }

    private static Feature getNextEnabledModule(List<Feature> features, int startingIndex) {
        for (int i = startingIndex; i < features.size(); ++i) {
            Feature feature = features.get(i);
            if (feature == null || !feature.getState() || !feature.visible || noVisualModules.getCurrentValue() && feature.getType() == Type.Visuals || !feature.getState() || !feature.visible || feature.getSuffix().equals("ClickGui") || !feature.visible)
                continue;
            return feature;
        }
        return null;
    }

    public static void onRenderModuleList(ScaledResolution scaledResolution) {
        float width = (float) scaledResolution.getScaledWidth() - (rightBorder.getCurrentValue() ? borderWidth.getCurrentValue() : 0.0f);
        float yDist = 1.0f;
        String arraySort = sortMode.getCurrentMode();
        int color = -1;
        int onecolor = ArrayList2.onecolor.getColor();
        int twoColor = twocolor.getColor();
        double time = ArrayList2.time.getCurrentValue();
        String mode = colorList.getOptions();
        double xSpeed = 0.55f;
        double ySpeed = 0.65f;
        float listOffset = offset.getCurrentValue();
        float yValue = -fontY.getCurrentValue();
        Feature nextFeature = null;
        if (MoonWare.featureManager.getFeatureByClass(ArrayList2.class).getState() && !Minecraft.gameSettings.showDebugInfo) {
            MoonWare.featureManager.getFeatureList().sort(arraySort.equalsIgnoreCase("Alphabetical") ? Comparator.comparing(Feature::getLabel) : Comparator.comparingInt(module -> !HUD.font.getCurrentMode().equalsIgnoreCase("Minecraft") ? -ClientHelper.getFontRender().getWidth(module.getSuffix()) : -Minecraft.font.getStringWidth(module.getSuffix())));
            for (Feature feature : MoonWare.featureManager.getFeatureList()) {
                boolean enable;
                if (feature.getSuffix().equals("ClickGui") || noVisualModules.getCurrentValue() && feature.getType() == Type.Visuals)
                    continue;
                switch (mode.toLowerCase()) {
                    case "rainbow": {
                        color = PaletteHelper.rainbow((int) ((double) yDist * time), rainbowSaturation.getCurrentValue(), rainbowBright.getCurrentValue()).getRGB();
                        break;
                    }
                    case "astolfo": {
                        color = PaletteHelper.astolfo(false, (int) yDist * 4).getRGB();
                        break;
                    }
                    case "static": {
                        color = new Color(onecolor).getRGB();
                        break;
                    }
                    case "client": {
                        color = ClientHelper.getClientColor().getRGB();
                        break;
                    }
                    case "pulse": {
                        color = PaletteHelper.TwoColorEffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs((double) System.currentTimeMillis() / time) / time + 6.0 * ((double) yDist * 2.55) / 60.0).getRGB();
                        break;
                    }
                    case "custom": {
                        color = PaletteHelper.TwoColorEffect(new Color(onecolor), new Color(twoColor), Math.abs((double) System.currentTimeMillis() / time) / time + 6.0 * ((double) yDist * 2.55) / 60.0).getRGB();
                        break;
                    }
                    case "fade": {
                        color = PaletteHelper.TwoColorEffect(new Color(onecolor), new Color(onecolor).darker().darker(), Math.abs((double) System.currentTimeMillis() / time) / time + 6.0 * ((double) yDist * 2.55) / 60.0).getRGB();
                        break;
                    }
                    case "none": {
                        color = -1;
                        break;
                    }
                    case "category": {
                        color = feature.getType().getColor();
                    }
                }
                ScreenHelper screenHelper = feature.getTranslate();
                String moduleLabel = feature.getSuffix();
                float width2 = !HUD.font.getCurrentMode().equalsIgnoreCase("Minecraft") ? ClientHelper.getFontRender().getWidth(moduleLabel) : Minecraft.font.getStringWidth(moduleLabel);
                float translateY = screenHelper.getY();
                float translateX = screenHelper.getX() - (rightBorder.getCurrentValue() ? 2.5f : 1.5f) - fontX.getCurrentValue();
                float length = !HUD.font.getCurrentMode().equalsIgnoreCase("Minecraft") ? (float) ClientHelper.getFontRender().getWidth(moduleLabel) : (float) Minecraft.font.getStringWidth(moduleLabel);
                float featureX = width - length;
                boolean bl = enable = feature.getState() && feature.visible;
                if (enable) {
                    screenHelper.calculateCompensation(featureX, yDist, xSpeed, ySpeed);
                } else {
                    screenHelper.calculateCompensation(width, yDist - (animationMode.currentMode.equals("Rising") ? risingY.getCurrentValue() : 0.0f), xSpeed + (double) 0.1f, ySpeed);
                }
                if (!(screenHelper.getX() < width)) continue;
                GlStateManager.pushMatrix();
                GlStateManager.translate(-x.getCurrentValue(), y.getCurrentValue(), 1.0f);
                if (backGround.getCurrentValue()) {
                    RectHelper.drawRect(translateX - backGroundLeft.getCurrentValue(), translateY - 1.0f, width, translateY + listOffset - 1.0f, backGroundColor.getColor());
                }
                int nextIndex = MoonWare.featureManager.getFeatureList().indexOf(feature) + 1;
                if (MoonWare.featureManager.getFeatureList().size() > nextIndex) {
                    nextFeature = getNextEnabledModule(MoonWare.featureManager.getFeatureList(), nextIndex);
                }
                if (border.getCurrentValue()) {
                    RectHelper.drawRect((double) translateX - 2.8, (double) translateY - 1.0, (double) translateX - 2.0, (double) (translateY + listOffset) - 1.0, color);
                    if (nextFeature != null) {
                        float font = !HUD.font.getCurrentMode().equalsIgnoreCase("Minecraft") ? (float) ClientHelper.getFontRender().getWidth(nextFeature.getSuffix()) : (float) Minecraft.font.getStringWidth(nextFeature.getSuffix());
                        float dif = length - font;
                        RectHelper.drawRect((double) translateX - 2.8, (double) (translateY + listOffset) - 1.0, (double) translateX - 2.8 + (double) dif, translateY + listOffset, color);
                    } else {
                        RectHelper.drawRect((double) translateX - 2.8, (double) (translateY + listOffset) - 1.0, width, translateY + listOffset, color);
                    }
                }
                if (shadow.getCurrentValue()) {
                    int color1 = shadowCustomColor.getColor();
                    RenderHelper2.renderBlurredShadow(new Color(color1), translateX - 1.0f, translateY - 1.0f - yValue, width2 + (float) (rightBorder.getCurrentValue() ? 3 : 6), listOffset, (int) shadowRadius.getCurrentValue());
                }
                if (!HUD.font.getCurrentMode().equalsIgnoreCase("Minecraft")) {
                    float y = 0;
                    String modeArrayFont = HUD.font.getOptions();
                    float f = modeArrayFont.equalsIgnoreCase("Verdana") ? 0.5f : (modeArrayFont.equalsIgnoreCase("Bebas Book") ? 2.5f : (modeArrayFont.equalsIgnoreCase("Kollektif") ? 0.9f : (modeArrayFont.equalsIgnoreCase("Product Sans") ? 0.5f : (modeArrayFont.equalsIgnoreCase("RaleWay") ? 0.3f : (modeArrayFont.equalsIgnoreCase("LucidaConsole") ? 3.0f : (modeArrayFont.equalsIgnoreCase("Lato") ? 1.2f : (modeArrayFont.equalsIgnoreCase("Open Sans") ? 0.5f : (y = modeArrayFont.equalsIgnoreCase("SF UI") ? 1.3f : 2.0f))))))));
                    if (fontRenderType.currentMode.equals("Shadow")) {
                        ClientHelper.getFontRender().drawShadow(moduleLabel, translateX, translateY + y - yValue, color);
                    } else if (fontRenderType.currentMode.equals("Default")) {
                        ClientHelper.getFontRender().draw(moduleLabel, translateX, translateY + y - yValue, color);
                    } else if (fontRenderType.currentMode.equals("Outline")) {
                        ClientHelper.getFontRender().drawOutline(moduleLabel, translateX, translateY + y - yValue, color);
                    }
                } else if (fontRenderType.currentMode.equals("Shadow")) {
                    Minecraft.font.drawStringWithShadow(moduleLabel, translateX, translateY + 1.0f - yValue, color);
                } else if (fontRenderType.currentMode.equals("Default")) {
                    Minecraft.font.drawString(moduleLabel, translateX, translateY + 1.0f - yValue, color);
                } else if (fontRenderType.currentMode.equals("Outline")) {
                    Minecraft.font.drawStringWithOutline(moduleLabel, translateX, translateY + 1.0f - yValue, color);
                }
                if (rightBorder.getCurrentValue()) {
                    RectHelper.drawRect(width, translateY - 1.0f, width + borderWidth.getCurrentValue(), translateY + listOffset - 0.6f, color);
                }
                yDist += listOffset;
                GlStateManager.popMatrix();
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = colorList.getCurrentMode();
        setSuffix(mode);
    }
}
