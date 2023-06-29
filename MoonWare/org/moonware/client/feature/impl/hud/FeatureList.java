package org.moonware.client.feature.impl.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.render.ScreenHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;
import java.util.Comparator;

public class FeatureList extends Feature {

    public final ListSetting fontRenderType;
    public final ListSetting borderMode;

    public BooleanSetting backGround;
    public final ColorSetting backGroundColor = new ColorSetting("BackGround Color", Color.BLACK.getRGB(), () -> backGround.getBoolValue());
    public BooleanSetting border;
    public final BooleanSetting rightBorder;
    public NumberSetting xOffset;
    public NumberSetting yOffset;
    public final NumberSetting offset;
    public NumberSetting size;
    public final NumberSetting borderWidth;
    public final NumberSetting rainbowSaturation;
    public final NumberSetting rainbowBright;
    public final NumberSetting fontX;
    public final ListSetting backgroundMode;
    public final NumberSetting fontY;
    public BooleanSetting blur = new BooleanSetting("Blur", false, () -> backGround.getBoolValue());
    public final BooleanSetting suffix;
    public static ListSetting colorList;
    public static NumberSetting colortime = new NumberSetting("Color Time", 30, 1, 100, 1, () -> colorList.currentMode.equalsIgnoreCase("Custom"));
    public BooleanSetting shadow;
    public final NumberSetting shadowRadius;
    public final ColorSetting shadowColor;
    public static ColorSetting onecolor = new ColorSetting("One Color", new Color(0xFFFFFF).getRGB(), () -> colorList.currentMode.equals("Custom"));
    public static ColorSetting twocolor = new ColorSetting("Two Color", new Color(0xFF0000).getRGB(), () -> colorList.currentMode.equals("Custom"));

    public FeatureList() {
        super("ArrayList", "���������� ������ ���� ���������� �������", Type.Hud);
        colorList = new ListSetting("ArrayList Color", "Astolfo", () -> true, "Custom", "Rainbow", "Astolfo");
        borderMode = new ListSetting("Border Mode", "Full", () -> border.getBoolValue(), "Full", "Single");
        fontRenderType = new ListSetting("FontRender Type", "Shadow", () -> true, "Default", "Shadow", "Outline");
        backGround = new BooleanSetting("Background", true, () -> true);
        backgroundMode = new ListSetting("Background Mode", "Rect", () -> backGround.getBoolValue(), "Rect", "Smooth Rect");
        shadow = new BooleanSetting("Shadow", false, () -> true);
        shadowRadius = new NumberSetting("Shadow Radius", 1, 1, 10, 1, () -> shadow.getBoolValue());
        shadowColor = new ColorSetting("Shadow Color", new Color(0xFFFFFF).getRGB(), () -> shadow.getBoolValue());
        border = new BooleanSetting("Border", true, () -> true);
        rightBorder = new BooleanSetting("Right Border", true, () -> true);
        suffix = new BooleanSetting("Suffix", true, () -> true);
        rainbowSaturation = new NumberSetting("Rainbow Saturation", 0.8F, 0.1F, 1F, 0.1F, () -> colorList.currentMode.equals("Rainbow"));
        rainbowBright = new NumberSetting("Rainbow Brightness", 1F, 0.1F, 1F, 0.1F, () -> colorList.currentMode.equals("Rainbow"));
        fontX = new NumberSetting("FontX", 0, -4, 20, 0.1F, () -> true);
        fontY = new NumberSetting("FontY", 0, -4, 20, 0.01F, () -> true);
        xOffset = new NumberSetting("FeatureList X", 0, 0, 500, 1, () -> !blur.getBoolValue());
        yOffset = new NumberSetting("FeatureList Y", 0, 0, 500, 1, () -> !blur.getBoolValue());
        offset = new NumberSetting("Font Offset", 11, 7, 20, 0.5F, () -> true);
        borderWidth = new NumberSetting("Border Width", 1, 0, 10, 0.1F, () -> rightBorder.getBoolValue());
        addSettings(colorList, onecolor, twocolor, fontRenderType, borderMode, fontX, fontY, border, rightBorder, suffix, borderWidth, backGround, backgroundMode, backGroundColor, shadow, shadowRadius, shadowColor, colortime, rainbowSaturation, rainbowBright, xOffset, yOffset, offset);
    }


    @EventTarget
    public void onUpdate(EventUpdate event) {
        setSuffix(colorList.getCurrentMode());
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        float width = event.getResolution().getScaledWidth() - (rightBorder.getBoolValue() ? borderWidth.getNumberValue() : 0);
        float y = 1;
        int yTotal = 0;
        for (int i = 0; i < MoonWare.featureManager.getFeatureList().size(); ++i) {
            yTotal += ClientHelper.getFontRender().getHeight() + 3;
        }
        if (MoonWare.featureManager.getFeatureByClass(FeatureList.class).getState() && !Minecraft.gameSettings.showDebugInfo) {
            MoonWare.featureManager.getFeatureList().sort(Comparator.comparingInt(module -> !HUD.font.getCurrentMode().equalsIgnoreCase("Minecraft") ? -ClientHelper.getFontRender().getWidth(suffix.getBoolValue() ? module.getSuffix() : module.getLabel()) : -Minecraft.font.getStringWidth(suffix.getBoolValue() ? module.getSuffix() : module.getLabel())));
            for (Feature feature : MoonWare.featureManager.getFeatureList()) {
                ScreenHelper animationHelper = feature.getScreenHelper();
                String featureSuffix = suffix.getBoolValue() ? feature.getSuffix() : feature.getLabel();
                float listOffset = offset.getNumberValue();
                float length = !HUD.font.getCurrentMode().equalsIgnoreCase("Minecraft") ? ClientHelper.getFontRender().getWidth(featureSuffix) : Minecraft.font.getStringWidth(featureSuffix);
                float featureX = width - length;
                boolean state = feature.getState() && feature.visible;

                if (state) {
                    animationHelper.interpolate(featureX, y, 4F * Minecraft.frameTime / 4);
                } else {
                    animationHelper.interpolate(width, y, 4F * Minecraft.frameTime / 4);
                }


                float translateY = animationHelper.getY();
                float translateX = animationHelper.getX() - (rightBorder.getBoolValue() ? 2.5F : 1.5F) - fontX.getNumberValue();
                int color = 0;
                int colorCustom = onecolor.getColorValue();
                int colorCustom2 = twocolor.getColorValue();
                double time = colortime.getNumberValue();
                String mode = colorList.getOptions();
                boolean visible = animationHelper.getX() < width;

                if (visible) {
                    switch (mode.toLowerCase()) {
                        case "rainbow":
                            color = DrawHelper.rainbow((int) (y * time), rainbowSaturation.getNumberValue(), rainbowBright.getNumberValue());
                            break;
                        case "astolfo":
                            color = DrawHelper.astolfoColors45(y, yTotal, 0.5f, 5).getRGB();
                            break;
                        case "custom":
                            color = DrawHelper.fadeColorRich(new Color(colorCustom).getRGB(), new Color(colorCustom2).getRGB(), (float) Math.abs(((((System.currentTimeMillis() / time) / time) + y * 6L / 61 * 2) % 2)));
                            break;
                    }

                    GlStateManager.pushMatrix();
                    GlStateManager.translate(-xOffset.getNumberValue(), yOffset.getNumberValue(), 1);

                    Feature nextFeature = null;
//                    int index = Main.instance.featureDirector.getFeatureList().indexOf(feature) + 1;

//                    if (Main.instance.featureDirector.getFeatureList().size() > index) {
//                        nextFeature = getNextEnabledFeature(Main.instance.featureDirector.getFeatureList(), index);
//                    }


                    if (border.getBoolValue() && borderMode.currentMode.equals("Full")) {
                        DrawHelper.drawRect(translateX - 3.5, translateY - 1, translateX - 2, translateY + listOffset - 1, color);
                    }

                    if (nextFeature != null && borderMode.currentMode.equals("Full")) {
                        String name = suffix.getBoolValue() ? nextFeature.getSuffix() : nextFeature.getLabel();
                        float font = !HUD.font.getCurrentMode().equalsIgnoreCase("Minecraft") ? ClientHelper.getFontRender().getWidth(name) : Minecraft.font.getStringWidth(name);
                        float dif = (length - font);
                        if (border.getBoolValue() && borderMode.currentMode.equals("Full")) {
                            DrawHelper.drawRect(translateX - 3.5, translateY + listOffset + 1, translateX - 2 + dif, translateY + listOffset - 1, color);
                        }
                    } else {
                        if (border.getBoolValue() && borderMode.currentMode.equals("Full")) {
                            DrawHelper.drawRect(translateX - 3.5, translateY + listOffset + 1, width, translateY + listOffset - 1, color);
                        }
                    }

                    if (borderMode.currentMode.equals("Single") && border.getBoolValue()) {
                        DrawHelper.drawRect(translateX - 3.5, translateY - 1, translateX - 2, translateY + listOffset - 1, color);
                    }

                    if (backGround.getBoolValue() && backgroundMode.currentMode.equalsIgnoreCase("Smooth Rect")) {
                        DrawHelper.drawSmoothRect(translateX - 2, translateY - 1, width, (float) (translateY + listOffset - 1.5), backGroundColor.getColorValue());
                    }
                    if (backGround.getBoolValue() && backgroundMode.currentMode.equalsIgnoreCase("Rect")) {
                        DrawHelper.drawRect(translateX - 2, translateY - 1, width, translateY + listOffset - 1, backGroundColor.getColorValue());
                    }
                    if (!HUD.font.getCurrentMode().equalsIgnoreCase("Minecraft")) {
                        String modeArrayFont = HUD.font.getOptions();
                        float yOffset = modeArrayFont.equalsIgnoreCase("Lato") ? 1.2f : modeArrayFont.equalsIgnoreCase("Myseo") ? 0.5f : modeArrayFont.equalsIgnoreCase("URWGeometric") ? 0.5f : modeArrayFont.equalsIgnoreCase("Roboto Regular") ? 0.5f : modeArrayFont.equalsIgnoreCase("SFUI") ? 1.3f : 2f;
                        if (!HUD.font.getCurrentMode().equalsIgnoreCase("Minecraft") && fontRenderType.currentMode.equals("Shadow")) {
                            if (suffix.getBoolValue()) {
                                ClientHelper.getFontRender().drawShadow(feature.getSuffix(), translateX, translateY + yOffset + fontY.getNumberValue(), new Color(192, 192, 192).getRGB());
                            }
                            ClientHelper.getFontRender().drawShadow(feature.getLabel(), translateX, translateY + yOffset + fontY.getNumberValue(), color);
                        } else if (!HUD.font.getCurrentMode().equalsIgnoreCase("Minecraft") && fontRenderType.currentMode.equals("Default")) {
                            if (suffix.getBoolValue()) {
                                ClientHelper.getFontRender().draw(feature.getSuffix(), translateX, translateY + yOffset + fontY.getNumberValue(), new Color(192, 192, 192).getRGB());
                            }
                            ClientHelper.getFontRender().draw(feature.getLabel(), translateX, translateY + yOffset + fontY.getNumberValue(), color);
                        } else if (!HUD.font.getCurrentMode().equalsIgnoreCase("Minecraft") && fontRenderType.currentMode.equals("Outline")) {
                            if (suffix.getBoolValue()) {
                                ClientHelper.getFontRender().drawOutline(feature.getSuffix(), translateX, translateY + yOffset + fontY.getNumberValue(), new Color(192, 192, 192).getRGB());
                            }
                            ClientHelper.getFontRender().drawOutline(feature.getLabel(), translateX, translateY + yOffset + fontY.getNumberValue(), color);
                        }
                    } else if (fontRenderType.currentMode.equals("Shadow")) {
                        if (suffix.getBoolValue()) {
                            Minecraft.font.drawStringWithShadow(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), new Color(192, 192, 192).getRGB());
                        }
                        Minecraft.font.drawStringWithShadow(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);
                    } else if (fontRenderType.currentMode.equals("Default")) {
                        if (suffix.getBoolValue()) {
                            Minecraft.font.drawString(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), new Color(192, 192, 192).getRGB());
                        }
                        Minecraft.font.drawString(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);
                    } else if (fontRenderType.currentMode.equals("Outline")) {
                        if (suffix.getBoolValue()) {
                            Minecraft.font.drawStringWithOutline(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), new Color(192, 192, 192).getRGB());
                        }
                        Minecraft.font.drawStringWithOutline(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);
                    }
                    if (shadow.getBoolValue()) {
                        DrawHelper.drawNewRect(translateX - 5 - shadowRadius.getNumberValue(), translateY - 3, width + 2, translateY + listOffset + 2, new Color(255, 255, 255, 0).getRGB());
                        DrawHelper.drawGlowRoundedRect(translateX - 5 - shadowRadius.getNumberValue(), translateY - 3, width + 2, translateY + listOffset + 2, shadowColor.getColorValue(), 10, 8);
                    }
                    y += listOffset;

                    if (rightBorder.getBoolValue()) {
                        float checkY = border.getBoolValue() ? 0 : 0.6F;
                        DrawHelper.drawRect(width, translateY - 1, width + borderWidth.getNumberValue(), translateY + listOffset - checkY, color);
                    }

                    GlStateManager.popMatrix();
                }
            }
        }
    }
}
