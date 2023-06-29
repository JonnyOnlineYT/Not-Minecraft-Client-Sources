package org.moonware.client.feature.impl.hud;

import net.minecraft.client.Minecraft;
import org.moonware.client.utils.MWFont;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.render.rect.RectHelper;

import java.awt.*;

public class Keystrokes extends Feature {

    public int lastA;
    public int lastW;
    public int lastS;
    public int lastD;
    public long deltaAnim;

    public Keystrokes() {
        super("Keystrokes", "Показывает нажатые клавиши", Type.Hud);
        deltaAnim = 0;
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        boolean A = Minecraft.gameSettings.keyBindLeft.pressed;
        boolean W = Minecraft.gameSettings.keyBindForward.pressed;
        boolean S = Minecraft.gameSettings.keyBindBack.pressed;
        boolean D = Minecraft.gameSettings.keyBindRight.pressed;
        int alphaA = A ? 255 : 0;
        int alphaW = W ? 255 : 0;
        int alphaS = S ? 255 : 0;
        int alphaD = D ? 255 : 0;
        float diff;

        if (lastA != alphaA) {
            diff = alphaA - lastA;
            lastA = (int) (lastA + diff / 40);
        }

        if (lastW != alphaW) {
            diff = alphaW - lastW;
            lastW = (int) (lastW + diff / 40);
        }

        if (lastS != alphaS) {
            diff = alphaS - lastS;
            lastS = (int) (lastS + diff / 40);
        }

        if (lastD != alphaD) {
            diff = alphaD - lastD;
            lastD = (int) (lastD + diff / 40);
        }


        if (!HUD.blur.getBoolValue()) {
            RectHelper.drawRect(5.0F, 49.0F, 25.0F, 69.0F, (new Color(lastA, lastA, lastA, 150)).getRGB());
        } else {
            MoonWare.blurUtil.blur(5.0F, 49.0F, 25.0F, 69.0F, 30);
        }
        MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawCenter("A", 15.0F, 56.0F, ClientHelper.getClientColor().getRGB());

        if (!HUD.blur.getBoolValue()) {
            RectHelper.drawRect(27.0F, 27.0F, 47.0F, 47.0F, (new Color(lastW, lastW, lastW, 150)).getRGB());
        } else {
            MoonWare.blurUtil.blur(27.0F, 27.0F, 47.0F, 47.0F, 30);
        }
        MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawCenter("W", 37.0F, 34.0F, ClientHelper.getClientColor().getRGB());

        if (!HUD.blur.getBoolValue()) {
            RectHelper.drawRect(27.0F, 49.0F, 47.0F, 69.0F, (new Color(lastS, lastS, lastS, 150)).getRGB());
        } else {
            MoonWare.blurUtil.blur(27.0F, 49.0F, 47.0F, 69.0F, 30);
        }
        MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawCenter("S", 37.0F, 56.0F, ClientHelper.getClientColor().getRGB());
        if (!HUD.blur.getBoolValue()) {
            RectHelper.drawRect(49.0F, 49.0F, 69.0F, 69.0F, (new Color(lastD, lastD, lastD, 150)).getRGB());
        } else {
            MoonWare.blurUtil.blur(49.0F, 49.0F, 69.0F, 69.0F, 30);
        }

        MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawCenter("D", 59.0F, 56.0F, ClientHelper.getClientColor().getRGB());
    }
}
