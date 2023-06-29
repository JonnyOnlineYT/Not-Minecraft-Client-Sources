package org.moonware.client.feature.impl.hud.ArrayGlowComp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.ScreenHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.ui.sqgui.Panel;

import java.awt.*;
import java.util.Comparator;

import static org.moonware.client.feature.impl.hud.ArrayGlowComp.ArrayList.*;
import static org.moonware.client.helpers.render2.RenderHelper2.alternateColorGradient;
import static org.moonware.client.helpers.render2.RenderHelper2.oneColorGradient;

public class ArrayListGlowPallete {

    public static void drawGlow(boolean onlyShadow) {
        ScaledResolution event = new ScaledResolution(Minecraft.getMinecraft());
        float width = (position.currentMode.equalsIgnoreCase("Right") ? event.getScaledWidth() : 0) - (rightBorder.getBoolValue() ? borderWidth.getNumberValue() : 0);
        for (Feature feature : MoonWare.featureManager.getFeatureList()) {
            width = (position.currentMode.equalsIgnoreCase("Right") ? event.getScaledWidth() : 0);
        }
        float y = 1;
        String arraySort = sortMode.getCurrentMode();
        if (MoonWare.featureManager.getFeatureByClass(ArrayList.class).getState() && !Minecraft.gameSettings.showDebugInfo) {
            sortMethod(arraySort);
            for (Feature feature : MoonWare.featureManager.getFeatureList()) {
                ScreenHelper animationHelper = feature.getScreenHelper();
                String featureSuffix = suffix.getBoolValue() ? feature.getSuffix() : feature.getLabel();
                float listOffset = offset.getNumberValue();
                float length = !HUD.font.currentMode.equals("Minecraft") ? ClientHelper.getFontRender().getWidth(featureSuffix) : Minecraft.font.getStringWidth(featureSuffix);
                float featureX = (position.currentMode.equalsIgnoreCase("Right") ? (width - length) : 0 - length);
                boolean state = feature.getState() && feature.visible;


                if (state) {
                    animationHelper.interpolate(featureX, y, 4F * Minecraft.frameTime / 6);
                } else {
                    animationHelper.interpolate(width, y, 4F * Minecraft.frameTime / 6);
                }

                float translateY = animationHelper.getY() + 2;
                float translateX = position.currentMode.equalsIgnoreCase("Right") ? animationHelper.getX() - (rightBorder.getBoolValue() ? 2.5F : 1.6665F) - fontX.getNumberValue() : 3 + fontX.getCurrentValue();
                int color = 0;
                int colorCustom = onecolor.getColorValue();
                int colorCustom2 = twocolor.getColorValue();
                double time = ArrayList.time.getNumberValue();


                boolean visible = position.currentMode.equalsIgnoreCase("Right") ? animationHelper.getX() < width : feature.visible && feature.getState();

                if (visible) {
                    String mode = colorList.getOptions();
                    switch (mode.toLowerCase()) {
                        case "rainbow":
                            color = PaletteHelper.astolfoarray2(false, (int) y * 4).getRGB();
                            break;
                        case "astolfo":
                            color = PaletteHelper.astolfoarray2(false, (int) y * 4).getRGB();
                            break;
                        case "astolfo2":
                            color = ColorUtil.skyRainbow((int) time, (int) y * 4).getRGB();
                            break;
                        case "static":
                            color = new Color(colorCustom).getRGB();
                            break;
                        case "custom":
                            color = ColorUtil.interpolateColorsBackAndForth((int) time, (int) y * range.getCurrentIntValue(), new Color(colorCustom), new Color(colorCustom2), HUD.hueInterpolation.get()).getRGB();
                            break;
                        case "test":
                            color = ArrayListGlowPallete.getColor(y, (int) time);
                            break;
                        case "fade":
                            color = DrawHelper.fadeColorRich(new Color(colorCustom).getRGB(), new Color(colorCustom).darker().darker().getRGB(), (float) ((y / MoonWare.featureManager.getFeatureList().size()) * time));
                            break;
                        case "none":
                            color = new Color(255, 255, 255).getRGB();
                            break;
                        case "category":
                            color = feature.getType().getColor();
                            break;
                    }
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(position.currentMode.equalsIgnoreCase("Right") ? -x.getNumberValue() : x.get(), ArrayList.y.getNumberValue(), 1);
                    Color gradientColor1 = Color.WHITE, gradientColor2 = Color.WHITE, gradientColor3 = Color.WHITE, gradientColor4 = Color.WHITE;
                    if (!HUD.useCustomColors.get()) {
                        gradientColor1 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 0, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                        gradientColor2 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 90, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                        gradientColor3 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 180, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                        gradientColor4 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 270, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                    } else {
                        gradientColor1 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 0, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                        gradientColor2 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 90, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                        gradientColor3 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 180, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                        gradientColor4 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 270, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                    }
                    if (!onlyShadow) {
                        if (position.currentMode.equalsIgnoreCase("Right")) {
                            //DrawHelper.drawGlow(translateX + 7.5D - leftdraw.getNumberValue(), translateY - 15.0D, width - 6, translateY + listOffset + 12.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
                            Panel.applyBloom(translateX - leftdraw.getNumberValue(), translateY - 1.0F, width - 6 - (translateX - 5f - leftdraw.getNumberValue()), translateY + listOffset + 12.0F - (translateY + 15f),14,new Color(31,31,31), new Color(31,31,31),new Color(31,31,31), new Color(31,31,31));
                            //GlowUtil.drawBlurredShadow((float) (translateX - leftdraw.getNumberValue()), (float) (translateY - 10.0D), (float) (width - 6 - translateX + 7.5D - leftdraw.getNumberValue()), (float) (15.0F + listOffset + 5.0D),14, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()),0);

                        } else {
                            DrawHelper.drawGlow(translateX + 0.5D, translateY - 15.0D, width + ClientHelper.getFontRender().getWidth(featureSuffix), translateY + listOffset + 10.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
                            Gui.drawRect(0, 0, 0, 0, 0);
                        }
                    }else{
                        if (position.currentMode.equalsIgnoreCase("Right")) {
                            DrawHelper.drawGlow(translateX + 7.5D - leftdraw.getNumberValue(), translateY - 15.0D, width - 6, translateY + listOffset + 12.0D, DrawHelper.setAlpha(new Color(1,1,1), glowAlpha.getCurrentIntValue()).getRGB());

                        } else {
                            DrawHelper.drawGlow(translateX + 0.5D, translateY - 15.0D, width + ClientHelper.getFontRender().getWidth(featureSuffix), translateY + listOffset + 10.0D, DrawHelper.setAlpha(new Color(1,1,1), glowAlpha.getCurrentIntValue()).getRGB());
                            Gui.drawRect(0, 0, 0, 0, 0);
                        }
                    }
                    y += listOffset;
                    GlStateManager.popMatrix();
                }
            }
        }
    }
    public static void drawGlow( ) {
        ScaledResolution event = new ScaledResolution(Minecraft.getMinecraft());
        float width = (position.currentMode.equalsIgnoreCase("Right") ? event.getScaledWidth() : 0) - (rightBorder.getBoolValue() ? borderWidth.getNumberValue() : 0);
        for (Feature feature : MoonWare.featureManager.getFeatureList()) {
            width = (position.currentMode.equalsIgnoreCase("Right") ? event.getScaledWidth() : 0);
        }
        float y = 1;
        String arraySort = sortMode.getCurrentMode();
        if (MoonWare.featureManager.getFeatureByClass(ArrayList.class).getState() && !Minecraft.gameSettings.showDebugInfo) {
            sortMethod(arraySort);
            for (Feature feature : MoonWare.featureManager.getFeatureList()) {
                ScreenHelper animationHelper = feature.getScreenHelper();
                String featureSuffix = suffix.getBoolValue() ? feature.getSuffix() : feature.getLabel();
                float listOffset = offset.getNumberValue();
                float length = !HUD.font.currentMode.equals("Minecraft") ? ClientHelper.getFontRender().getWidth(featureSuffix) : Minecraft.font.getStringWidth(featureSuffix);
                float featureX = (position.currentMode.equalsIgnoreCase("Right") ? (width - length) : 0 - length);
                boolean state = feature.getState() && feature.visible;


                if (state) {
                    animationHelper.interpolate(featureX, y, 4F * Minecraft.frameTime / 6);
                } else {
                    animationHelper.interpolate(width, y, 4F * Minecraft.frameTime / 6);
                }

                float translateY = animationHelper.getY() + 2;
                float translateX = position.currentMode.equalsIgnoreCase("Right") ? animationHelper.getX() - (rightBorder.getBoolValue() ? 2.5F : 1.6665F) - fontX.getNumberValue() : 3 + fontX.getCurrentValue();
                int color = 0;
                int colorCustom = onecolor.getColorValue();
                int colorCustom2 = twocolor.getColorValue();
                double time = ArrayList.time.getNumberValue();


                boolean visible = position.currentMode.equalsIgnoreCase("Right") ? animationHelper.getX() < width : feature.visible && feature.getState();

                if (visible) {
                    String mode = colorList.getOptions();
                    switch (mode.toLowerCase()) {
                        case "rainbow":
                            color = PaletteHelper.astolfoarray2(false, (int) y * 4).getRGB();
                            break;
                        case "astolfo":
                            color = PaletteHelper.astolfoarray2(false, (int) y * 4).getRGB();
                            break;
                        case "astolfo2":
                            color = ColorUtil.skyRainbow((int) time, (int) y * 4).getRGB();
                            break;
                        case "static":
                            color = new Color(colorCustom).getRGB();
                            break;
                        case "custom":
                            color = ColorUtil.interpolateColorsBackAndForth((int) time, (int) y, new Color(colorCustom), new Color(colorCustom2), true).getRGB();
                            break;
                        case "test":
                            color = ArrayListGlowPallete.getColor(y, (int) time);
                            break;
                        case "fade":
                            color = DrawHelper.fadeColorRich(new Color(colorCustom).getRGB(), new Color(colorCustom).darker().darker().getRGB(), (float) ((y / MoonWare.featureManager.getFeatureList().size()) * time));
                            break;
                        case "none":
                            color = new Color(255, 255, 255).getRGB();
                            break;
                        case "category":
                            color = feature.getType().getColor();
                            break;
                    }
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(position.currentMode.equalsIgnoreCase("Right") ? -x.getNumberValue() : x.get(), ArrayList.y.getNumberValue(), 1);

                    Color gradientColor1 = Color.WHITE, gradientColor2 = Color.WHITE, gradientColor3 = Color.WHITE, gradientColor4 = Color.WHITE;
                    if (!false) {
                        gradientColor1 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 0, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                        gradientColor2 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 90, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                        gradientColor3 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 180, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                        gradientColor4 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 270, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                    } else {
                        gradientColor1 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 0, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                        gradientColor2 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 90, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                        gradientColor3 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 180, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                        gradientColor4 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 270, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                    }
                    if (position.currentMode.equalsIgnoreCase("Right")) {
                        //DrawHelper.drawGlow(translateX + 7.5D, translateY - 15.0D, width - 6, translateY + listOffset + 12.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
                        Panel.applyBloom(translateX + 7.5F, translateY - 15.0F, width - 6, translateY + listOffset + 12.0F,1,new Color(31,31,31), new Color(31,31,31),new Color(31,31,31), new Color(31,31,31));
                    } else {
                        DrawHelper.drawGlow(translateX + 0.5D, translateY - 15.0D, width + ClientHelper.getFontRender().getWidth(featureSuffix), translateY + listOffset + 10.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
                        Gui.drawRect(0, 0, 0, 0, 0);
                    }

                    y += listOffset;
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    static void sortMethod(String arraySort) {
        MoonWare.featureManager.getFeatureList().sort(arraySort.equalsIgnoreCase("Alphabetical") ? Comparator.comparing(Feature::getLabel) : Comparator.comparingInt(module -> !HUD.font.currentMode.equals("Minecraft") ? -ClientHelper.getFontRender().getWidth(suffix.getBoolValue() ? module.getSuffix() : module.getLabel()) : -Minecraft.font.getStringWidth(suffix.getBoolValue() ? module.getSuffix() : module.getLabel())));
    }

    public static int getColor(float y, int time) {
        int color;
        if (HUD.useCustomColors.get()) {
            color = ColorUtil.interpolateColorsBackAndForth(time,(int)-y * 2,HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get()).getRGB();
        }else{
            color = ColorUtil.interpolateColorsBackAndForth(time,(int)-y * 2, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get()).getRGB();
        }
        return color;
    }
}
