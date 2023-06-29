package org.moonware.client.feature.impl.hud.ArrayGlowComp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.PotionEffect;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.ScreenHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render.rect.RectHelper;

import java.awt.*;

import static org.moonware.client.feature.impl.hud.ArrayGlowComp.ArrayList.*;
import static org.moonware.client.helpers.Helper.mc;

public class BackgroundComp {
    public static void draw() {
        ScaledResolution event = new ScaledResolution(Minecraft.getMinecraft());
        float width = (position.currentMode.equalsIgnoreCase("Right") ? event.getScaledWidth() : 0) - (rightBorder.getBoolValue() ? borderWidth.getNumberValue() : 0);
        for (Feature feature : MoonWare.featureManager.getFeatureList()) {
            width = (position.currentMode.equalsIgnoreCase("Right") ? event.getScaledWidth() : 0) - (0);
        }
        int yTotal = 0;
        yTotal += ClientHelper.getFontRender().getHeight() + 3;
        float y = 1;
        //RenderHelper2.renderBlurredShadow2();
        String arraySort = sortMode.getCurrentMode();
        if (MoonWare.featureManager.getFeatureByClass(ArrayList.class).getState() && !Minecraft.gameSettings.showDebugInfo) {
            ArrayListGlowPallete.sortMethod(arraySort);

            if (glowb.get() && !bmask.get()) {
                ArrayListGlowPallete.drawGlow(backroundSMode.currentMode.equals("Default"));
            }
            for (Feature feature : MoonWare.featureManager.getFeatureList()) {
                float widthglow = (rightBorder.getBoolValue() ? borderWidth.getNumberValue() : 0) + ClientHelper.getFontRender().getWidth(feature.getLabel()) + (suffix.getBoolValue() ? ClientHelper.getFontRender().getWidth(getSuff) : 0);


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

                //if (glow.getBoolValue()) {
                //    RectHelper.drawGlow(translateX - 2.5f, translateY - 7.5f, width, translateY + 1f + listOffset, glowcol.getColorValue());
                //}
                if (visible) {
                    if (backGround.getBoolValue()) {
                        String modee = backgroundColorList.getOptions();
                        switch (modee.toLowerCase()) {
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
                                color = new Color(1, 1, 1).getRGB();
                                break;
                            case "category":
                                color = feature.getType().getColor();
                                break;
                        }

                        GlStateManager.pushMatrix();
                        GlStateManager.translate(position.currentMode.equalsIgnoreCase("Right") ?-x.getNumberValue() : x.get(), ArrayList.y.getNumberValue(), 1);
                        if (backGroundGradient.getBoolValue() && !glowb.getBoolValue()) {
                            RectHelper.drawGradientRect(translateX - 2, translateY - 2.5f,position.currentMode.equalsIgnoreCase("Right") ? width : width + 3, translateY + listOffset + 2.5f, backGroundColor.getColorValue(), backGroundColor2.getColorValue());
                        }
//                        if (glowb.getBoolValue()) {
//                            if (position.getCurrentMode().equalsIgnoreCase("Right"))
//                                DrawHelper.drawGlow(translateX + 0.5D, translateY - 15.0D, width, translateY + listOffset + 10.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
//                            else
//                                DrawHelper.drawGlow(translateX + 0.5D, translateY - 15.0D, width + ClientHelper.getFontRender().getStringWidth(featureSuffix), translateY + listOffset + 10.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
//
//                        }
                        Color sss = new Color(backGroundColor.getColorValue());
                        Gui.drawRect(0,0,0,0,0);
                        float checkY = border.getBoolValue() ? 0 : 0.6F;
                        if (position.getCurrentMode().equalsIgnoreCase("Right")) {
                            GlStateManager.pushMatrix();
                            Gui.drawRect(0,0,0,0,0);
                            //RectHelper.drawRect(translateX - leftdraw.get(), translateY, width, translateY + listOffset,DrawHelper.getColorWithOpacity(new Color(color),glowAlpha.getCurrentIntValue()).getRGB());
                            //RoundedUtil.drawRound((float) (translateX - leftdraw.get()),translateY, (float) (width - (translateX -leftdraw.get())), listOffset - 1.35F, 0.5F, true,DrawHelper.getColorWithOpacity(new Color(color),77));

                            RectHelper.drawRect(translateX  -leftdraw.get(), translateY, width - leftdraw.get(), translateY + listOffset, DrawHelper.getColorWithOpacity(new Color(color),77).getRGB());

                            int finalColor = color;
                            float finalWidth = width;
                            //RenderHelper2.renderBlur((int) (translateX - leftdraw.get()) - 15, (int) translateY, (int) width, (int) (translateY + listOffset),1);
                            GlStateManager.popMatrix();
                            Gui.drawRect(0,0,0,0,0);
                            //RenderHelper2.blurAreaBoarder((int) (translateX - leftdraw.get()), (int) translateY, (int) width, (int) (translateY + listOffset),new Color(color).getRGB());

                        }else
                            RectHelper.drawRect(width, translateY, width + ClientHelper.getFontRender().getWidth(featureSuffix) + leftdraw.get(), translateY + listOffset,DrawHelper.getColorWithOpacity(new Color(color),77).getRGB());
                        Gui.drawRect(0,0,0,0,0);
                        GlStateManager.popMatrix();
                    }
                }
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
                    if (glow.getCurrentValue()) {
                        //GlowUtil.drawBlurredGlow(translateX - glowStrengh.getNumberValue(), translateY - 8.5f - glowbMaxStrengh.getNumberValue() - 7 - 7.7f, width, translateY + listOffset - 1.5f + glowStrenghver.getNumberValue() + glowbMaxStrengh.getNumberValue() + 6 + 11.7f - 7.7f, glowcol.getColorValue());
                    }
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
                    ScaledResolution sr = new ScaledResolution(mc);
                    //RoundedUtil.drawRound(0,0,sr.getScaledWidth(),sr.getScaledHeight(),1,true,new Color(31,31,31,120));

                    Feature nextFeature = null;
                    int index = MoonWare.featureManager.getFeatureList().indexOf(feature) + 1;

                    if (MoonWare.featureManager.getFeatureList().size() > index) {
                        nextFeature = getNextEnabledFeature(MoonWare.featureManager.getFeatureList(), index);
                    }


                    Color colorss = new Color(color);
                    int colors = new Color(colorss.getRed(), colorss.getGreen(), colorss.getBlue(), glowAlpha.getCurrentIntValue()).getRGB();
                    Gui.drawRect(0, 0, 0, 0, 0);
//                    if (position.currentMode.equalsIgnoreCase("Right")) {
//                        Gui.drawRect(0, 0, 0, 0, 0);
//                        DrawHelper.drawGlow(translateX + 4.5D, translateY - 15.0D, width, translateY + listOffset + 12.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
//                        Gui.drawRect(0, 0, 0, 0, 0);
//                    } else {
//                        Gui.drawRect(0, 0, 0, 0, 0);
//                        DrawHelper.drawGlow(translateX + 0.5D, translateY - 15.0D, width + ClientHelper.getFontRender().getStringWidth(featureSuffix), translateY + listOffset + 10.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
//                        Gui.drawRect(0, 0, 0, 0, 0);
//                    }
                    Gui.drawRect(0, 0, 0, 0, 0);

                    y += listOffset;
                    if (rightBorder.getBoolValue()) {
                        float checkY = border.getBoolValue() ? 0 : 0.6F;
                        Gui.drawRect(0, 0, 0, 0, 0);
                        //RectHelper.drawRect(width, translateY, width + borderWidth.getNumberValue(), translateY + listOffset, color);
                        Gui.drawRect(0, 0, 0, 0, 0);
                    }
                    Gui.drawRect(0, 0, 0, 0, 0);
                    GlStateManager.popMatrix();
                }
            }
        }

        if (bmask.get()) {
            StencilUtil.initStencilToWrite();
            drawRunnable();
            StencilUtil.readStencilBuffer(2);
            if (glowb.get())
                ArrayListGlowPallete.drawGlow(backroundSMode.currentMode.equals("Default"));
            StencilUtil.uninitStencilBuffer();
        }
        //RenderUtils2.drawShadow(7,2, BackgroundComp::drawRunnable);
    }

    public static void drawRunnable() {
        ScaledResolution event = new ScaledResolution(Minecraft.getMinecraft());
        float width = (position.currentMode.equalsIgnoreCase("Right") ? event.getScaledWidth() : 0) - (rightBorder.getBoolValue() ? borderWidth.getNumberValue() : 0);
        for (Feature feature : MoonWare.featureManager.getFeatureList()) {
            width = (position.currentMode.equalsIgnoreCase("Right") ? event.getScaledWidth() : 0) - (0);
        }
        int yTotal = 0;
        yTotal += ClientHelper.getFontRender().getHeight() + 3;
        float y = 1;
        //RenderHelper2.renderBlurredShadow2();
        String arraySort = sortMode.getCurrentMode();
        if (MoonWare.featureManager.getFeatureByClass(ArrayList.class).getState() && !Minecraft.gameSettings.showDebugInfo) {
            ArrayListGlowPallete.sortMethod(arraySort);

            for (Feature feature : MoonWare.featureManager.getFeatureList()) {
                float widthglow = (rightBorder.getBoolValue() ? borderWidth.getNumberValue() : 0) + ClientHelper.getFontRender().getWidth(feature.getLabel()) + (suffix.getBoolValue() ? ClientHelper.getFontRender().getWidth(getSuff) : 0);


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

                //if (glow.getBoolValue()) {
                //    RectHelper.drawGlow(translateX - 2.5f, translateY - 7.5f, width, translateY + 1f + listOffset, glowcol.getColorValue());
                //}
                if (visible) {
                    if (backGround.getBoolValue()) {
                        String modee = backgroundColorList.getOptions();
                        switch (modee.toLowerCase()) {
                            case "rainbow":
                                color = PaletteHelper.rainbow((int) (y * time), rainbowSaturation.getNumberValue(), rainbowBright.getNumberValue()).getRGB();
                                break;
                            case "astolfo":
                                color = PaletteHelper.astolfoarray2(false, (int) y * 4).getRGB();
                                break;
                            case "astolfo2":
                                color = DrawHelper.astolfoColors45(y, (float) (yTotal),1.0F, 14.0F).getRGB();
                                break;
                            case "static":
                                color = new Color(colorCustom).getRGB();
                                break;
                            case "custom":
                                color = PaletteHelper.fadeColor(new Color(colorCustom).getRGB(), new Color(colorCustom2).getRGB(), (float) Math.abs(((((System.currentTimeMillis() / time) / time) + y * 6L / 61 * 2) % 2)));
                                break;
                            case "fade":
                                color = PaletteHelper.fadeColor(new Color(colorCustom).getRGB(), new Color(colorCustom).darker().darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / time) / time) + y * 6L / 60 * 2) % 2)));
                                break;
                            case "none":
                                color = new Color(1,1,1).getRGB();
                                break;
                            case "category":
                                color = feature.getType().getColor();
                                break;
                        }

                        GlStateManager.pushMatrix();
                        GlStateManager.translate(position.currentMode.equalsIgnoreCase("Right") ?-x.getNumberValue() : x.get(), ArrayList.y.getNumberValue(), 1);
                        if (backGroundGradient.getBoolValue() && !glowb.getBoolValue()) {
                            RectHelper.drawGradientRect(translateX - 2, translateY - 2.5f,position.currentMode.equalsIgnoreCase("Right") ? width : width + 3, translateY + listOffset + 2.5f, backGroundColor.getColorValue(), backGroundColor2.getColorValue());
                        }
//                        if (glowb.getBoolValue()) {
//                            if (position.getCurrentMode().equalsIgnoreCase("Right"))
//                                DrawHelper.drawGlow(translateX + 0.5D, translateY - 15.0D, width, translateY + listOffset + 10.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
//                            else
//                                DrawHelper.drawGlow(translateX + 0.5D, translateY - 15.0D, width + ClientHelper.getFontRender().getStringWidth(featureSuffix), translateY + listOffset + 10.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
//
//                        }
                        Color sss = new Color(backGroundColor.getColorValue());
                        Gui.drawRect(0,0,0,0,0);
                        float checkY = border.getBoolValue() ? 0 : 0.6F;
                        if (position.getCurrentMode().equalsIgnoreCase("Right")) {
                            GlStateManager.pushMatrix();
                            Gui.drawRect(0,0,0,0,0);
                            //RectHelper.drawRect(translateX - leftdraw.get(), translateY, width, translateY + listOffset,DrawHelper.getColorWithOpacity(new Color(color),glowAlpha.getCurrentIntValue()).getRGB());
                            //RoundedUtil.drawRound((float) (translateX - leftdraw.get()),translateY, (float) (width - (translateX -leftdraw.get())), listOffset - 1.35F, 0.5F, true,DrawHelper.getColorWithOpacity(new Color(color),77));
                            RectHelper.drawRect(translateX  -leftdraw.get(), translateY, width - leftdraw.get(), translateY + listOffset, DrawHelper.getColorWithOpacity(new Color(color),77).getRGB());

                            int finalColor = color;
                            float finalWidth = width;
                            //RenderHelper2.renderBlur((int) (translateX - leftdraw.get()) - 15, (int) translateY, (int) width, (int) (translateY + listOffset),1);
                            GlStateManager.popMatrix();
                            Gui.drawRect(0,0,0,0,0);
                            //RenderHelper2.blurAreaBoarder((int) (translateX - leftdraw.get()), (int) translateY, (int) width, (int) (translateY + listOffset),new Color(color).getRGB());

                        }else
                            RectHelper.drawRect(width, translateY, width + ClientHelper.getFontRender().getWidth(featureSuffix) + leftdraw.get(), translateY + listOffset,DrawHelper.getColorWithOpacity(new Color(color),77).getRGB());
                        Gui.drawRect(0,0,0,0,0);
                        GlStateManager.popMatrix();
                    }
                }
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
                    if (glow.getCurrentValue()) {
                        //GlowUtil.drawBlurredGlow(translateX - glowStrengh.getNumberValue(), translateY - 8.5f - glowbMaxStrengh.getNumberValue() - 7 - 7.7f, width, translateY + listOffset - 1.5f + glowStrenghver.getNumberValue() + glowbMaxStrengh.getNumberValue() + 6 + 11.7f - 7.7f, glowcol.getColorValue());
                    }
                    switch (mode.toLowerCase()) {
                        case "rainbow":
                            color = PaletteHelper.rainbow((int) (y * time), rainbowSaturation.getNumberValue(), rainbowBright.getNumberValue()).getRGB();
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
                            color = PaletteHelper.fadeColor(new Color(colorCustom).getRGB(), new Color(colorCustom2).getRGB(), (float) Math.abs(((((System.currentTimeMillis() / time) / time) + y * 6L / 61 * 2) % 2)));
                            break;
                        case "fade":
                            color = PaletteHelper.fadeColor(new Color(colorCustom).getRGB(), new Color(colorCustom).darker().darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / time) / time) + y * 6L / 61 * 2) % 2)));
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
                    ScaledResolution sr = new ScaledResolution(mc);
                    //RoundedUtil.drawRound(0,0,sr.getScaledWidth(),sr.getScaledHeight(),1,true,new Color(31,31,31,120));

                    Feature nextFeature = null;
                    int index = MoonWare.featureManager.getFeatureList().indexOf(feature) + 1;

                    if (MoonWare.featureManager.getFeatureList().size() > index) {
                        nextFeature = getNextEnabledFeature(MoonWare.featureManager.getFeatureList(), index);
                    }


                    Color colorss = new Color(color);
                    int colors = new Color(colorss.getRed(), colorss.getGreen(), colorss.getBlue(), glowAlpha.getCurrentIntValue()).getRGB();
                    Gui.drawRect(0, 0, 0, 0, 0);
//                    if (position.currentMode.equalsIgnoreCase("Right")) {
//                        Gui.drawRect(0, 0, 0, 0, 0);
//                        DrawHelper.drawGlow(translateX + 4.5D, translateY - 15.0D, width, translateY + listOffset + 12.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
//                        Gui.drawRect(0, 0, 0, 0, 0);
//                    } else {
//                        Gui.drawRect(0, 0, 0, 0, 0);
//                        DrawHelper.drawGlow(translateX + 0.5D, translateY - 15.0D, width + ClientHelper.getFontRender().getStringWidth(featureSuffix), translateY + listOffset + 10.0D, DrawHelper.setAlpha(new Color(color), glowAlpha.getCurrentIntValue()).getRGB());
//                        Gui.drawRect(0, 0, 0, 0, 0);
//                    }
                    Gui.drawRect(0, 0, 0, 0, 0);

                    y += listOffset;
                    if (rightBorder.getBoolValue()) {
                        float checkY = border.getBoolValue() ? 0 : 0.6F;
                        Gui.drawRect(0, 0, 0, 0, 0);
                        //RectHelper.drawRect(width, translateY, width + borderWidth.getNumberValue(), translateY + listOffset, color);
                        Gui.drawRect(0, 0, 0, 0, 0);
                    }
                    Gui.drawRect(0, 0, 0, 0, 0);
                    GlStateManager.popMatrix();
                }
            }
        }
    }
}
