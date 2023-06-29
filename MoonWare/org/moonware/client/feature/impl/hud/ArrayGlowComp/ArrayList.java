package org.moonware.client.feature.impl.hud.ArrayGlowComp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.PotionEffect;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.ScreenHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;
import java.util.List;

public class ArrayList extends Feature {

    public static ListSetting sortMode;
    public static ListSetting fontRenderType;
    public static ListSetting borderMode;
    public static BooleanSetting backGround;
    public static double PtranslateX;
    public static double PtranslateY;
    public static double Pwidthglow;
    public static double PlistOffset;
    public static double PwidthSSS;
    public static BooleanSetting onlyBinds = new BooleanSetting("Only Binds", false, () -> false);
    public static BooleanSetting backGroundGradient = new BooleanSetting("BackGround Gradient", false, () -> false);
    public static ColorSetting backGroundColor2 = new ColorSetting("BackGround Color Two", Color.BLACK.getRGB(), () -> false);
    public static ColorSetting backGroundColor = new ColorSetting("BackGround Color", Color.BLACK.getRGB(), () -> false);
    public static BooleanSetting border;
    public static BooleanSetting rightBorder;
    public static NumberSetting x;
    public static NumberSetting y;
    public static NumberSetting offset;
    public static NumberSetting size;
    public static NumberSetting borderWidth;
    public static NumberSetting rainbowSaturation;
    public static NumberSetting rainbowBright;
    public static NumberSetting fontX;
    public static NumberSetting fontY;
    public static BooleanSetting blur = new BooleanSetting("Blur", false, () -> backGround.getBoolValue());
    public static BooleanSetting suffix;
    public static BooleanSetting glowb;
    public static BooleanSetting glow;
    public static ColorSetting glowcol;
    public static ColorSetting glowbcol;
    public static ListSetting colorSuffixMode = new ListSetting("Suffix Mode Color", "Default", () -> false, "Astolfo", "Default", "Static", "Rainbow", "Custom", "Category");
    public static ColorSetting suffixColor = new ColorSetting("Suffix Color", Color.GRAY.getRGB(), () -> false);
    public static ListSetting position = new ListSetting("Position", "Right", () -> true, "Right", "Left");
    public static BooleanSetting glowText = new BooleanSetting("Glow with text", "Накладывает глов эффект на текст на аррай листе", true, () -> true);
    public static BooleanSetting removevisuals = new BooleanSetting("Remove Visuals", "Не показывать визуалы", false, () -> true);
    public static ColorSetting glowColor = new ColorSetting("Glow color", new Color(173, 95, 95, 124).getRGB(), () -> true);
    public static NumberSetting glowStrengh = new NumberSetting("glow strength horizontally", 0,-5,15,0.1F, () -> false);
    public static NumberSetting glowbMaxStrengh = new NumberSetting("BakckgroundglowY", 0,-15,15,0.1F, () -> false);
    public static NumberSetting glowStrenghver = new NumberSetting("glow strength vertically", 0,-5,15,0.1F, () -> false);
    public static ListSetting colorList = new ListSetting("Global Color", "Astolfo", () -> true, "Astolfo","Astolfo2", "Static", "Fade", "Rainbow", "Custom","TEST", "None", "Category");
    public static NumberSetting time = new NumberSetting("Color Time", 30, 1, 100, 1, () -> true);
    public static NumberSetting glowRadius = new NumberSetting("Glow Radius",6, 5,75,1,()-> false);
    public static ListSetting GlowColorMode = new ListSetting("Glow Color Mode", "Global", () -> false,"Global");
    public static ListSetting GlowPaletteMode = new ListSetting("Glow Palette Mode", "FixNoCustom", () -> false,"FixNoCustom", "NoFixWithCustom");
    public static NumberSetting glowAlpha = new NumberSetting("Glow Alpha", "Прозрачность глова, если будет слишком высокой, глов может багаться", 113, 0, 255,1, () -> true);
    public static ListSetting backgroundColorList = new ListSetting("Background Color", "Astolfo", () -> backGround.getBoolValue(), "Astolfo","Astolfo2", "Static", "Fade", "Rainbow", "Custom", "None", "Category");
    public static NumberSetting leftdraw = new NumberSetting("LeftDrawWidth",0,-15,4,0.01F,() -> backGround.get());
    public static ColorSetting onecolor = new ColorSetting("One Color", new Color(0xFFFFFF).getRGB(), () -> (colorList.currentMode.equals("Fade") || colorList.currentMode.equals("Custom") || colorList.currentMode.equals("Static") || backGround.get() && backgroundColorList.currentMode.equalsIgnoreCase("Fade") || backGround.get() && backgroundColorList.currentMode.equalsIgnoreCase("Custom") || backGround.get() && backgroundColorList.getCurrentMode().equalsIgnoreCase("Static")));
    public static ColorSetting twocolor = new ColorSetting("Two Color", new Color(0xFF0000).getRGB(), () -> colorList.currentMode.equals("Custom") || backGround.get() && backgroundColorList.getCurrentMode().equalsIgnoreCase("Custom"));
    public static BooleanSetting upborder = new BooleanSetting("Up Border",true,()->  false);
    public static String getSuff;
    public static BooleanSetting gradientText = new BooleanSetting("Gradient Text", "делает текст хоризонтально-градиентным, подстраивается под цвета в худе",false);
    public static NumberSetting range = new NumberSetting("Range",  18,1,45,1,() -> true);
    public static ListSetting backroundSMode = new ListSetting("ShadowMode", "Colored", () -> backGround.get(),"Colored", "Default");
    public static BooleanSetting bmask = new BooleanSetting("Masked",false,() -> backGround.get());
    public ArrayList() {
        super("ArrayList", "Показывает список всех включенных модулей", Type.Hud);

        /* COLOR SETTINGS */

        /* OTHER */

        getSuff = getSuffix();
        borderMode = new ListSetting("Border Mode", "Full", () -> border.getBoolValue(), "Full", "Single");
        sortMode = new ListSetting("ArrayList Sort", "Length", () -> false, "Length", "Alphabetical");
        fontRenderType = new ListSetting("FontRender Type", "Shadow", () -> true, "Default", "Shadow", "Outline");
        backGround = new BooleanSetting("Background", true, () -> true);
        glow = new BooleanSetting("Glow",true,() -> true);
        //glowbcol = new ColorSetting("Glow Color", new Color(197, 50, 50, 115).getRGB(), () -> glow.getBoolValue());
        glowb = new BooleanSetting("BackgroundShadow", true, () -> backGround.getBoolValue());
        glowbcol = new ColorSetting("Glow Color", new Color(35, 169, 122, 150).getRGB(), () -> glowb.getBoolValue());
        border = new BooleanSetting("Border", false, () -> false);
        rightBorder = new BooleanSetting("Right Border", true, () -> true);
        suffix = new BooleanSetting("Suffix", true, () -> true);
        //    alpha = new NumberSetting("BackgroundAlpha", 1, 1, 255, 1, () -> backGround.getCurrentValue() && !blur.getCurrentValue());
        //   bright = new NumberSetting("BackgroundBright", 255, 1, 255, 1, () -> backGround.getCurrentValue() && !blur.getCurrentValue());
        rainbowSaturation = new NumberSetting("Rainbow Saturation", 0.8F, 0.1F, 1F, 0.1F, () -> HUD.colorList.currentMode.equals("Rainbow") || colorSuffixMode.currentMode.equals("Rainbow"));
        rainbowBright = new NumberSetting("Rainbow Brightness", 1F, 0.1F, 1F, 0.1F, () -> HUD.colorList.currentMode.equals("Rainbow") || colorSuffixMode.currentMode.equals("Rainbow"));
        fontX = new NumberSetting("FontX", 0, -4, 20, 0.1F, () -> true);
        glowcol = new ColorSetting("Glow Color", new Color(0xFF0000).getRGB(), () -> glow.getBoolValue());
        fontY = new NumberSetting("FontY", 0, -15, 20, 0.01F, () -> true);
        x = new NumberSetting("ArrayList X", 0, 0, 300, 0.00001F, () -> !blur.getBoolValue());
        y = new NumberSetting("ArrayList Y", 0, 0, 300, 0.00001F, () -> !blur.getBoolValue());
        offset = new NumberSetting("Font Offset", 11, 1, 20, 0.5F, () -> true);
        borderWidth = new NumberSetting("Border Width", 1, 0, 10, 0.1F, () -> rightBorder.getBoolValue());
        addSettings(position,gradientText, range, upborder, leftdraw, sortMode, colorList, backgroundColorList, fontRenderType, colorSuffixMode, time, onecolor, twocolor, suffixColor, fontX, fontY, removevisuals, onlyBinds, glow, GlowColorMode, GlowPaletteMode, glowRadius, glowAlpha, border, rightBorder, suffix, borderWidth, backGround,backroundSMode,bmask, glowb, glowbMaxStrengh, glowStrengh, glowStrenghver, backGroundGradient, backGroundColor, backGroundColor2, rainbowSaturation, rainbowBright, x, y, offset);
    }

    static Feature getNextEnabledFeature(List<Feature> features, int index) {
        for (int i = index; i < features.size(); i++) {
            Feature feature = features.get(i);
            if (feature.getState() && feature.visible) {
                if (!feature.getSuffix().equals("ClickGui") && feature.visible) {
                    return feature;
                }
            }
        }
        return null;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = gradientText.get() ? "Gradient" : ArrayList.colorList.getCurrentMode();
        setSuffix(mode);
        upborder.setValue(false);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        sortMode.setCurrentMode("Length");
        float width = (position.currentMode.equalsIgnoreCase("Right") ? event.getResolution().getScaledWidth() : 0) - (rightBorder.getBoolValue() ? borderWidth.getNumberValue() : 0);
        for (Feature feature : MoonWare.featureManager.getFeatureList()) {
            width = (position.currentMode.equalsIgnoreCase("Right") ? event.getResolution().getScaledWidth() : 0) - (0);
        }
        int yTotal = 0;
        yTotal += ClientHelper.getFontRender().getHeight() + 3;
        float y = 1;
        String arraySort = sortMode.getCurrentMode();
        if (MoonWare.featureManager.getFeatureByClass(ArrayList.class).getState() && !Minecraft.gameSettings.showDebugInfo) {
            ArrayListGlowPallete.sortMethod(arraySort);
            if (backGround.get()) {
                BackgroundComp.draw();
            }


            //offset.setCurrentValue(5.0F);
            for (Feature feature : MoonWare.featureManager.getFeatureList()) {
                float widthglow = (rightBorder.getBoolValue() ? borderWidth.getNumberValue() : 0) + ClientHelper.getFontRender().getWidth(feature.getLabel()) + (suffix.getBoolValue() ? ClientHelper.getFontRender().getWidth(getSuffix()) : 0);


                ScreenHelper animationHelper = feature.getScreenHelper();
                String featureSuffix = suffix.getBoolValue() ? feature.getSuffix() : feature.getLabel();
                float listOffset = offset.getNumberValue();
                float length = !HUD.font.currentMode.equals("Minecraft") ? ClientHelper.getFontRender().getWidth(featureSuffix) : Minecraft.font.getStringWidth(featureSuffix);
                float featureX = (position.currentMode.equalsIgnoreCase("Right") ? (width - length) : 0 - length);
                boolean state = onlyBinds.get() ? feature.getBind() == 0 && feature.getState() && feature.visible : feature.getState() && feature.visible;


                if (state) {
                    animationHelper.interpolate(featureX, y, 4F * Minecraft.frameTime / 6);
                } else {
                    animationHelper.interpolate(width, y, 4F * Minecraft.frameTime / 6);
                }

                float yPotion = 2;


                for (PotionEffect potionEffect : Minecraft.player.getActivePotionEffects()) {
                    if (potionEffect.getPotion().isBeneficial()) {
                        yPotion = 26;
                    }
                    if (potionEffect.getPotion().isBadEffect()) {
                        yPotion = 26 * 2;
                    }
                }

                yPotion = 2;
                float translateY = animationHelper.getY() + yPotion;
                float translateX = position.currentMode.equalsIgnoreCase("Right") ? animationHelper.getX() - (rightBorder.getBoolValue() ? 2.5F : 1.6665F) - fontX.getNumberValue() : 3 + fontX.getCurrentValue();
                int color = 0;
                int colorCustom = onecolor.getColorValue();
                int colorCustom2 = twocolor.getColorValue();
                double time = ArrayList.time.getNumberValue();


                boolean visible = position.currentMode.equalsIgnoreCase("Right") ? animationHelper.getX() < width : feature.visible && feature.getState();

                if (removevisuals.getBoolValue()) {
                    if (feature.getState() && feature.getType() == Type.Visuals) {
                        if (feature.visible != false) {
                            feature.visible = !feature.visible;
                        }
                    }
                } else {
                    if (feature.getState() && feature.getType() == Type.Visuals) {
                        feature.visible = true;
                    }
                }
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
                    int colorFuffix = 0;
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(position.currentMode.equalsIgnoreCase("Right") ? -x.getNumberValue() : x.get(), ArrayList.y.getNumberValue(), 1);

                    Feature nextFeature = null;
                    int index = MoonWare.featureManager.getFeatureList().indexOf(feature) + 1;

                    if (MoonWare.featureManager.getFeatureList().size() > index) {
                        nextFeature = getNextEnabledFeature(MoonWare.featureManager.getFeatureList(), index);
                    }


                    if (border.getBoolValue() && borderMode.currentMode.equals("Full")) {
                        RectHelper.drawRect(translateX - 3.5, translateY - 3 + fontY.getNumberValue(), translateX - 2, translateY + listOffset - 3 + fontY.getNumberValue(), color);
                    }
                    Color colorss = new Color(color);
                    int colors = new Color(colorss.getRed(), colorss.getGreen(), colorss.getBlue(), glowAlpha.getCurrentIntValue()).getRGB();
                    if (!gradientText.get()) {
                        if (!glow.getBoolValue() || GlowPaletteMode.getCurrentMode().equalsIgnoreCase("FixNoCustom") || GlowPaletteMode.getCurrentMode().equalsIgnoreCase("NNoCustom")) {
                            if (!HUD.font.currentMode.equals("Minecraft")) {
                                String modeArrayFont = HUD.font.getOptions();
                                float yOffset = modeArrayFont.equalsIgnoreCase("Verdana") ? 0.5f : modeArrayFont.equalsIgnoreCase("Comfortaa") ? 3 : modeArrayFont.equalsIgnoreCase("CircleRegular") ? 0.5f : modeArrayFont.equalsIgnoreCase("Arial") ? 1.3f : modeArrayFont.equalsIgnoreCase("Kollektif") ? 0.9f : modeArrayFont.equalsIgnoreCase("Product Sans") ? 0.5f : modeArrayFont.equalsIgnoreCase("RaleWay") ? 0.3f : modeArrayFont.equalsIgnoreCase("LucidaConsole") ? 3f : modeArrayFont.equalsIgnoreCase("Lato") ? 1.2f : modeArrayFont.equalsIgnoreCase("Open Sans") ? 0.5f : modeArrayFont.equalsIgnoreCase("SF UI") ? 1.3f : 2f;
                                if (!HUD.font.currentMode.equals("Minecraft") && fontRenderType.currentMode.equals("Shadow")) {
                                    if (suffix.getBoolValue()) {
                                        ClientHelper.getFontRender().drawShadow(feature.getSuffix(), translateX, translateY + yOffset + fontY.getNumberValue(), colorFuffix);
                                    }
                                    ClientHelper.getFontRender().drawShadow(feature.getLabel(), translateX, translateY + yOffset + fontY.getNumberValue(), color);
                                } else if (!HUD.font.currentMode.equals("Minecraft") && fontRenderType.currentMode.equals("Default")) {
                                    if (suffix.getBoolValue()) {
                                        ClientHelper.getFontRender().draw(feature.getSuffix(), translateX, translateY + yOffset + fontY.getNumberValue(), colorFuffix);
                                    }
                                    ClientHelper.getFontRender().draw(feature.getLabel(), translateX, translateY + yOffset + fontY.getNumberValue(), color);
                                } else if (!HUD.font.currentMode.equals("Minecraft") && fontRenderType.currentMode.equals("Outline")) {
                                    if (suffix.getBoolValue()) {
                                        ClientHelper.getFontRender().drawOutline(feature.getSuffix(), translateX, translateY + yOffset + fontY.getNumberValue(), colorFuffix);
                                    }
                                    ClientHelper.getFontRender().drawOutline(feature.getLabel(), translateX, translateY + yOffset + fontY.getNumberValue(), color);
                                }
                            } else if (fontRenderType.currentMode.equals("Shadow")) {
                                if (suffix.getBoolValue()) {
                                    Minecraft.font.drawStringWithShadow(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), colorFuffix);
                                }
                                Minecraft.font.drawStringWithShadow(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);
                            } else if (fontRenderType.currentMode.equals("Default")) {
                                if (suffix.getBoolValue()) {
                                    Minecraft.font.drawString(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), colorFuffix);
                                }
                                Minecraft.font.drawString(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);
                            } else if (fontRenderType.currentMode.equals("Outline")) {
                                if (suffix.getBoolValue()) {
                                    Minecraft.font.drawStringWithOutline(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), colorFuffix);
                                }
                                Minecraft.font.drawStringWithOutline(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);

                            }
                        }
                    }else{
                        String label = feature.getLabel();
                        if (!glow.getBoolValue() || GlowPaletteMode.getCurrentMode().equalsIgnoreCase("FixNoCustom") || GlowPaletteMode.getCurrentMode().equalsIgnoreCase("NNoCustom")) {
                            if (!HUD.font.currentMode.equals("Minecraft")) {
                                String modeArrayFont = HUD.font.getOptions();
                                float yOffset = modeArrayFont.equalsIgnoreCase("Verdana") ? 0.5f : modeArrayFont.equalsIgnoreCase("Comfortaa") ? 3 : modeArrayFont.equalsIgnoreCase("CircleRegular") ? 0.5f : modeArrayFont.equalsIgnoreCase("Arial") ? 1.3f : modeArrayFont.equalsIgnoreCase("Kollektif") ? 0.9f : modeArrayFont.equalsIgnoreCase("Product Sans") ? 0.5f : modeArrayFont.equalsIgnoreCase("RaleWay") ? 0.3f : modeArrayFont.equalsIgnoreCase("LucidaConsole") ? 3f : modeArrayFont.equalsIgnoreCase("Lato") ? 1.2f : modeArrayFont.equalsIgnoreCase("Open Sans") ? 0.5f : modeArrayFont.equalsIgnoreCase("SF UI") ? 1.3f : 2f;
                                if (!HUD.font.currentMode.equals("Minecraft") && fontRenderType.currentMode.equals("Shadow")) {
                                    if (suffix.getBoolValue()) {
                                        ClientHelper.getFontRender().drawShadow(label, translateX, translateY + yOffset + fontY.getNumberValue(), colorFuffix);
                                    }
                                    ClientHelper.getFontRender().drawShadow(label, translateX, translateY + yOffset + fontY.getNumberValue(), color);
                                } else if (!HUD.font.currentMode.equals("Minecraft") && fontRenderType.currentMode.equals("Default")) {
                                    if (suffix.getBoolValue()) {
                                        ClientHelper.getFontRender().drawGradientArray(label, translateX, translateY + yOffset + fontY.getNumberValue(), colorFuffix);
                                    }
                                    ClientHelper.getFontRender().drawGradientArray(label, translateX, translateY + yOffset + fontY.getNumberValue(), color);
                                } else if (!HUD.font.currentMode.equals("Minecraft") && fontRenderType.currentMode.equals("Outline")) {
                                    if (suffix.getBoolValue()) {
                                        ClientHelper.getFontRender().drawOutline(feature.getSuffix(), translateX, translateY + yOffset + fontY.getNumberValue(), colorFuffix);
                                    }
                                    ClientHelper.getFontRender().drawOutline(feature.getLabel(), translateX, translateY + yOffset + fontY.getNumberValue(), color);
                                }
                            } else if (fontRenderType.currentMode.equals("Shadow")) {
                                if (suffix.getBoolValue()) {
                                    Minecraft.font.drawStringWithShadow(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), colorFuffix);
                                }
                                Minecraft.font.drawStringWithShadow(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);
                            } else if (fontRenderType.currentMode.equals("Default")) {
                                if (suffix.getBoolValue()) {
                                    Minecraft.font.drawString(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), colorFuffix);
                                }
                                Minecraft.font.drawString(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);
                            } else if (fontRenderType.currentMode.equals("Outline")) {
                                if (suffix.getBoolValue()) {
                                    Minecraft.font.drawStringWithOutline(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), colorFuffix);
                                }
                                Minecraft.font.drawStringWithOutline(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);

                            }
                        }
                    }
                    if (glow.get()) {
                        if (position.currentMode.equalsIgnoreCase("Right")) {
                            DrawHelper.drawGlow(translateX + 7.5D, translateY - 15.0D, width - 6, translateY + listOffset + 12.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());

                        } else {
                            DrawHelper.drawGlow(translateX + 0.5D, translateY - 15.0D, width + ClientHelper.getFontRender().getWidth(featureSuffix), translateY + listOffset + 10.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
                            Gui.drawRect(0, 0, 0, 0, 0);
                        }
                    }
                    y += listOffset;
                    if (rightBorder.getBoolValue()) {
                        float checkY = border.getBoolValue() ? 0 : 0.6F;
                        Gui.drawRect(0, 0, 0, 0, 0);
                        RectHelper.drawRect(width, translateY, width + borderWidth.getNumberValue(), translateY + listOffset + 1, color);
                        Gui.drawRect(0, 0, 0, 0, 0);
                    }

                    GlStateManager.popMatrix();


                }
            }
            if (glow.get() && false)
                ArrayListGlowPallete.drawGlow();
        }
    }

    @EventTarget
    public void onEnable() {
        super.onEnable();
    }
}