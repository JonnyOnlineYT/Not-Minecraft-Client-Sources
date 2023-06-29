package org.moonware.client.ui.components.draggable.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.feature.impl.hud.WaterMark;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.components.draggable.HudComponent;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;

import java.awt.*;

import static org.moonware.client.feature.impl.hud.HUD.*;

public class TimerHudComponent extends HudComponent {
    public float anim;
    public TimerHudComponent() {
        super(240, 250, 30, 40);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        setVisible(timerhud.get());
        super.draw(mouseX, mouseY, partialTick);
        //if (!testTimerMode.get()) return;
        GlStateManager.pushMatrix();
        float x = this.x - 195.5F +19, y = this.y + 16.5F + 6;
        if (!testTimerMode.get()) {
            x = this.x - 195.5F +19 - 15;
            this.width = 40;
            this.height = 45;
        }
        int max = 357;
        float fax = 0;
        if (timar >= 0 && timar <= 358) {
            fax = timar;
        }
        int dia = (int) calculatePercentage(timar, max);
        diap = 0;
        if (dia <= 100 && dia >= 0) {
            diap = dia;
        }
        float diap2 = AnimationHelper.calculateCompensation((float) max, fax, 6, 8);
        Color colorr;
        switch (colorList.currentMode) {
            case "Gradient": {
                for (int ii = (int) x; ii < x + 45; ii++) {
                    colorr = new Color(PaletteHelper.fadeColor(WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue(), ii));
                }
                break;
            }
            case "Client": {
                colorr = ClientHelper.getClientColor();
                break;
            }
            case "Rainbow": {
                colorr = PaletteHelper.rainbow(300, 1, 1);
                break;
            }
            case "Default": {
                colorr = WaterMark.logoMode.currentMode.equals("OneTap v2") ? new Color(161, 0, 255) : Color.RED;
            }
        }
        //RectHelper.drawRectWithGlow(x + 193.5, y - 20.5, x + 235, y + 27f, 5, 15, new Color(31, 31, 31, 180));
        Color analogous = ColorUtil.getAnalogousColor(new Color(onecolor.getColorValue()))[0];
        Color colorca = ColorUtil.interpolateColorsBackAndForth(15, 0, new Color(onecolor.getColorValue()), analogous, true);
        boolean HudMod = true;
        Color gradientColor1;
        Color gradientColor2;
        Color gradientColor3;
        Color gradientColor4;
        gradientColor1 = ColorUtil.interpolateColorsBackAndForth(13, 0, ClientHelper.getClientColor(), colorca, HudMod);
        gradientColor2 = ColorUtil.interpolateColorsBackAndForth(11, 30, colorca, ClientHelper.getClientColor(), HudMod);
        gradientColor3 = ColorUtil.interpolateColorsBackAndForth(17, 60, colorca, ClientHelper.getClientColor(), HudMod);
        gradientColor4 = ColorUtil.interpolateColorsBackAndForth(8, 90, ClientHelper.getClientColor(), colorca, HudMod);
        if (!testAstolfoColors.get()) {
            RoundedUtil.drawGradientRound((float) (x + 195.5), (float) (y - 16.5), 32, 35.5f + 3, 5, gradientColor1, gradientColor2, gradientColor3, gradientColor4);
        } else {
            if (testTimerMode.get()) {
                //RenderHelper2.drawRainbowRound(x + 180, y - 7.5, 100 / 1.5, 4,2,false, true,false,true,2,4);
            } else {
                RenderHelper2.drawRainbowRound(x + 195.5, y - 16.5, 32, 35.5, 5, true, true, true, true, 2, 4);
            }
        }
        //RenderHelper2.renderBlurredShadow(new Color(31, 31, 31, 180),x + 190.5, y - 23.5, 45, 47.5f + 3, 8);
        if (!testTimerMode.get()) {
            MWFont.MONTSERRAT_BOLD.get(14).drawCenter("Timer", (float) (x + 195.5 + 18), y - 14f, -1);
        } else {
            MWFont.MONTSERRAT_BOLD.get(14).drawCenter("Timer", x + 190 + (377 / 8) / 2, y - 14f, -1);
            float finalX1 = x;
            //RenderUtils2.drawShadow(7, 2, () -> MWFont.MONTSERRAT_BOLD.get(14).drawCenter("Timer", finalX1 + 190 + (377 / 8) / 2, y - 14f, -1));
        }
        String coold = String.format("%.1f ", Minecraft.player.getCooledAttackStrength(1));
        String diap3 = String.format("%.1f ", diap2);
        double cooldownPercentage = MathHelper.clamp(Minecraft.player.getCooledAttackStrength(1), 0.0, 1.0);
        double cooldownWidth = 357.0 * cooldownPercentage;
        timerBarWidth = AnimationHelper.calculateCompensation(timar, (float) timerBarWidth, 6, 24);
        float precentage = 0;
        float precentage2 = 0;
        int flat = 0;
        if (dia <= 100 && dia >= 0) {
            flat = dia;
        }
        if (flat <= 99 && flat >= 10) {
            precentage = 14.59f;
            precentage2 = 2.83f;
        } else if (flat < 9.99f) {
            precentage = 17.22f;
            precentage2 = 2.83f;
        }
        if (dia >= 100) {
            flat = 100;
        } else if (dia < 100 && dia >= 0) {
            flat = dia;
        }
        if (flat >= 100) {
            precentage = 10.83f;
            precentage2 = 2.83f;
            flat = 100;
        }
        if (!testTimerMode.get()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 212.0F, y + 6, 1.0);
            RectHelper.drawWexCircle(timerBarWidth / 357, String.valueOf(flat));
            GlStateManager.popMatrix();
        }
        double timW = timerBarWidth;
        anim = (float) Interpolator.linear(anim, timW, 2f / 20);
        if (testTimerMode.get()) {
            if (testTimerMode.get()) {
                if (timertestAstolfoColors.get()) {
                    RoundedUtil.drawRound(x + 190, (float) (y - 7.5), 365 / 8, 8, 1, new Color(31, 31, 31, 70));
                    float finalX = x;
                    //RenderUtils2.drawBlur(5, () -> RoundedUtil.drawRound(finalX + 190, (float) (y - 7.5), 365 / 8, 8, 1, new Color(31, 31, 31, 70)));
                    RenderHelper2.drawRainbowRound(x + 190, y - 7.5, (anim) / 8, 8, 1, false, true, false, true, 2, 4);
                    //RenderUtils2.drawShadow(27, 1, () -> RoundedUtil.drawRound(finalX + 190, (float) (y - 7.5), 365 / 8, 8, 1, new Color(31, 31, 31, 70)));
                } else {
                    RoundedUtil.drawRound(x + 190, (float) (y - 7.5), 367 / 8, 8, 1, new Color(31, 31, 31));
                    RoundedUtil.drawGradientHorizontal(x + 190, (float) (y - 7.5), (anim) / 8, 8, 1, timerColor.getColorc(), timerColor2.getColorc());
                }
                this.width = 70;
            }
            //RoundedUtil.drawRound(getX() - 250,getY() - 250,200,200,7,new Color(31,31,31,80));
            //RenderUtils2.drawShadowWithColor(7,1,new Color(255,20,20),() -> RoundedUtil.drawRound(getX() - 250,getY() - 250,200,200,7,new Color(31,31,31)));

            //RenderUtils2.drawBlur(5,() -> RoundedUtil.drawRound(getX() - 250,getY() - 250,200,200,7,new Color(31,31,31)));

            MWFont.MONTSERRAT_SEMIBOLD.get(13).drawCenter(flat + "%", x + 190 + (377 / 8) / 2, y - 5, new Color(221, 221, 221).getRGB());
            int wid = width;
        }
        GlStateManager.popMatrix();
    }
}
