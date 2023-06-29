package org.moonware.client.feature.impl.hud.ArrayGlowComp;

import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.combat.AntiBot;
import org.moonware.client.feature.impl.combat.HitBoxes;
import org.moonware.client.feature.impl.combat.WTap;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.feature.impl.player.AntiAFK;
import org.moonware.client.feature.impl.player.AntiPush;
import org.moonware.client.feature.impl.player.GuiWalk;
import org.moonware.client.feature.impl.visual.ChinaHat;
import org.moonware.client.feature.impl.visual.FogColor;
import org.moonware.client.feature.impl.visual.HitColor;
import org.moonware.client.helpers.render.GlowUtil;
import org.moonware.client.helpers.render2.RenderHelper2;

import java.awt.*;

import static org.moonware.client.feature.impl.hud.ArrayGlowComp.ArrayList.*;

public class ArrayGlow {
    public static void drawRightGlow() {
        Color colors = new Color(glowcol.getColorValue());
        double translateY = PtranslateY;
        double translateX = PtranslateX;
        double widthglow = Pwidthglow;
        double listOffset = PlistOffset;
        double width = PwidthSSS;
        RenderHelper2.renderBlurredShadow(new Color(colors.getRed(),colors.getGreen(),colors.getBlue(), 113),translateX - 2, translateY + fontY.getCurrentValue(),width, listOffset + 4, (int) glowRadius.getCurrentValue());
    }
    public static void drawDownGlow2(int mode) {
        Color colors = new Color(glowcol.getColorValue());
        double translateY = PtranslateY;
        double translateX = PtranslateX;
        double widthglow = Pwidthglow;
        double listOffset = PlistOffset;
        double width = PwidthSSS;
        if (mode == 1) {
            RenderHelper2.renderBlurredShadow(new Color(colors.getRed(), colors.getGreen(), colors.getBlue(), 113), translateX - 2, translateY + fontY.getCurrentValue() + 1.5F, width, listOffset + 4.8F, (int) glowRadius.getCurrentValue());
        }else if (mode == 2) {
            GlowUtil.drawBlurredGlow( translateX - 2, translateY + fontY.getCurrentValue() + 1.5F,(translateX - 2) + width,(translateY + fontY.getCurrentValue() + 1.5F) + listOffset + 4.8F,new Color(colors.getRed(), colors.getGreen(), colors.getBlue(), 113).getRGB());
        }
    }
    public static void drawDownGlow() {
        Color colors = new Color(glowcol.getColorValue());
        double translateY = PtranslateY;
        double translateX = PtranslateX;
        double widthglow = Pwidthglow;
        double listOffset = PlistOffset;
        double width = PwidthSSS;
            RenderHelper2.renderBlurredShadow(new Color(colors.getRed(), colors.getGreen(), colors.getBlue(), 113), translateX - 2, translateY + fontY.getCurrentValue() + 1.5F, width, listOffset + 4.8F, (int) glowRadius.getCurrentValue());
    }
    public static void drawDawnGlow(Color color) {
        Color colors = new Color(glowcol.getColorValue());
        double translateY = PtranslateY;
        double translateX = PtranslateX;
        double widthglow = Pwidthglow;
        double listOffset = PlistOffset;
        double width = PwidthSSS;
        if (GlowPaletteMode.getCurrentMode().equalsIgnoreCase("NoFixWithCustom")) {
            RenderHelper2.renderBlurredShadow(new Color(color.getRed(), color.getGreen(), color.getBlue(), 113), translateX - 2, translateY + fontY.getCurrentValue() - 2.5F, width + (rightBorder.getBoolValue() ? 4 : 0), listOffset + 4.8F, (int) glowRadius.getCurrentValue());
        }else{
            //fontX.setCurrentValue(0.0F);
            GlowUtil.drawBlurredGlow(  translateX - 2, translateY + fontY.getCurrentValue() - 2.5F,(translateX - 2) + width + (rightBorder.getBoolValue() ? 4.14F : 0), (translateY + fontY.getCurrentValue() - 2.5F) + listOffset + 4.8F,new Color(color.getRed(), color.getGreen(),color.getBlue(), glowAlpha.getCurrentIntValue()).getRGB());

            //RenderHelper2.renderBlurredShadow((float) (translateX - 2), (float) (translateY + fontY.getCurrentValue() - 2.5F), (float) (width + (rightBorder.getBoolValue() ? 4 : 0)), (float) (listOffset + 4.8F), (int) glowRadius.getCurrentValue(), new Color(color.getRed(), color.getGreen(),color.getBlue(), 113));
            //RenderHelper2.drawBlurredShadow((float) (translateX - 2.7F), (float) (translateY + fontY.getCurrentValue() + 1.5F), (float) (translateX - 2 + width), (float) (translateY + fontY.getCurrentValue() + 1.5F + listOffset + 4.8F), (int) glowRadius.getCurrentValue(), new Color(color.getRed(), color.getGreen(), color.getBlue(), GlowPaletteMode.getCurrentMode().equalsIgnoreCase("Global") ? 255 : 113));
        }

    }
    public static void drawDawnGlow(Color color, int mode) {
        Color colors = new Color(glowcol.getColorValue());
        double translateY = PtranslateY;
        double translateX = PtranslateX;
        double widthglow = Pwidthglow;
        double listOffset = PlistOffset;
        double width = PwidthSSS;
        GlowUtil.drawBlurredGlow(translateX - 2, translateY + fontY.getCurrentValue() - 2.5F,width + (rightBorder.getBoolValue() ? 4.14F : 0), listOffset + 4.8F,new Color(color.getRed(), color.getGreen(),color.getBlue(), glowAlpha.getCurrentIntValue()).getRGB());
    }

    private static boolean getState(String feature) {
        if (feature == "HUD") {
            return MoonWare.featureManager.getFeatureByClass(HUD.class).getState();
        }
        if (feature == "WTap") {
            return MoonWare.featureManager.getFeatureByClass(WTap.class).getState();
        }
        if (feature == "AntiBot") {
            return MoonWare.featureManager.getFeatureByClass(AntiBot.class).getState();
        }
        if (feature == "AntiAFK") {
            return MoonWare.featureManager.getFeatureByClass(AntiAFK.class).getState();
        }
        if (feature == "HitColor") {
            return MoonWare.featureManager.getFeatureByClass(HitColor.class).getState();
        }
        if (feature == "GuiWalk") {
            return MoonWare.featureManager.getFeatureByClass(GuiWalk.class).getState();
        }
        if (feature == "FogColor") {
            return MoonWare.featureManager.getFeatureByClass(FogColor.class).getState();
        }
        if (feature == "AntiPush") {
            return MoonWare.featureManager.getFeatureByClass(AntiPush.class).getState();
        }
        if (feature == "HitBoxes") {
            return MoonWare.featureManager.getFeatureByClass(HitBoxes.class).getState();
        }
        if (feature == "ChinaHat") {
            return MoonWare.featureManager.getFeatureByClass(ChinaHat.class).getState();
        }
        if (feature == "FullBright") {
            return MoonWare.featureManager.getFeatureByClass(HUD.class).getState();
        }
        return false;
    }

    private static boolean getRightPalette() {
        if (getState("HUD")) {
            return true;
        }else if (getState("WTap")) {
            return true;
        }
        if (getState("AntiBot") && !getState("HUD") && !getState("WTap")) {
            return true;
        }
        if (getState("AntiAFK") && !getState("HUD") && !getState("WTap") && !getState("AntiBot")) {
            return  true;
        }
        if (!getState("HUD") && !getState("WTap") && !getState("AntiBot") && !getState("AntiAFK") && getState("HitColor")) {
            return true;
        }
        return !getState("HUD") && !getState("WTap") && !getState("AntiBot") && !getState("AntiAFK") && !getState("HitColor") && getState("GuiWalk");
    }
}
