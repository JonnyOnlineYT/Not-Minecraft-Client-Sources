package org.moonware.client.feature.impl.hud.ArrayGlowComp;

import net.minecraft.util.math.MathHelper;
import org.moonware.client.feature.impl.combat.particle.zalupa.Utils.RenderUtils;

public class ColorShell {

    private int r, g, b, a;
    private boolean rainbow;

    public ColorShell() {
        r = 230;
        g = 230;
        b = 230;
        a = 255;
    }

    public static int HSBtoRGB(float hue) {
        int r = 0, g = 0, b = 0;
        float h = (hue - (float) Math.floor(hue)) * 6.0f;
        float f = h - (float) Math.floor(h);
        float p = 1 * (1.0f - 1);
        float q = 1 * (1.0f - 1 * f);
        float t = 1 * (1.0f - (1 * (1.0f - f)));
        switch ((int) h) {
            case 0:
                r = (int) (1 * 255.0f + 0.5f);
                g = (int) (t * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
                break;
            case 1:
                r = (int) (q * 255.0f + 0.5f);
                g = (int) (1 * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
                break;
            case 2:
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (1 * 255.0f + 0.5f);
                b = (int) (t * 255.0f + 0.5f);
                break;
            case 3:
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (q * 255.0f + 0.5f);
                b = (int) (1 * 255.0f + 0.5f);
                break;
            case 4:
                r = (int) (t * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (1 * 255.0f + 0.5f);
                break;
            case 5:
                r = (int) (1 * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (q * 255.0f + 0.5f);
                break;
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b);
    }

    public ColorShell setRGB(int r, int g, int b) {
        return setRGBA(r, g, b, a);
    }

    public ColorShell setRGBA(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    public void changeRed(int value) {
        r = r + value;
        normalize();
    }

    public void changeGreen(int value) {
        g = g + value;
        normalize();
    }

    public void changeBlue(int value) {
        b = b + value;
        normalize();
    }

    public void changeAlpha(int value) {
        a = a + value;
        normalize();
    }

    public int getRedDefault() {
        return r;
    }

    public int getGreenDefault() {
        return g;
    }

    public int getBlueDefault() {
        return b;
    }

    public int getRed() {
        if (isRainbow())
            updateRainbow();
        return r;
    }

    public int getGreen() {
        updateRainbow();
        return g;
    }

    public int getBlue() {
        updateRainbow();
        return b;
    }

    public int getAlpha() {
        return a;
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public ColorShell setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
        return this;
    }

    public void normalize() {
        r = MathHelper.clamp(r, 0, 255);
        g = MathHelper.clamp(g, 0, 255);
        b = MathHelper.clamp(b, 0, 255);
        a = MathHelper.clamp(a, 0, 255);
    }

    public void updateRainbow() {
        if (!isRainbow()) {
            return;
        }
        double rainbow = Math.ceil(System.currentTimeMillis() / 16);
        rainbow %= 360;
        int color = HSBtoRGB((float) (rainbow / 360F));
        r = (color >> 16) & 255;
        g = (color >> 8) & 255;
        b = color & 255;
    }

    public void updateRainbow(long add) {
        double rainbow = Math.ceil((System.currentTimeMillis() + add) / 16);
        rainbow %= 360;
        int color = HSBtoRGB((float) (rainbow / 360F));
        r = (color >> 16) & 255;
        g = (color >> 8) & 255;
        b = color & 255;
    }

    public int build() {
        updateRainbow();
        return RenderUtils.rgba(r, g, b, a);
    }
}