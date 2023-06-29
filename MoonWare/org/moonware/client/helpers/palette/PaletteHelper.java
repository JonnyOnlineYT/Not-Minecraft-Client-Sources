package org.moonware.client.helpers.palette;

import net.minecraft.util.math.MathHelper;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.ArrayList;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.helpers.Helper;

import java.awt.*;

public class PaletteHelper implements Helper {

    public static String color(int r, int g, int b) {
        return '#' + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
    }

    public static Color rainbow2(int delay, float saturation, float brightness) {
        double rainbow = Math.ceil(System.currentTimeMillis() / (long)delay);
        return Color.getHSBColor((float)((rainbow %= 360.0) / 360.0), saturation, brightness);
    }
    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 4:
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 5:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                    break;
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b);
    }
    public static Color astolfo(float speed, int yOffset) {
        float hue;
        for (hue = (float)(System.currentTimeMillis() % (long)((int)speed) + (long)yOffset); hue > speed; hue -= speed) {
        }
        if ((double)(hue /= speed) > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        return Color.getHSBColor(hue += 0.5f, 0.4f, 1.0f);
    }

    public static int getColor(Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    public static Color rainbowC(int delay, float saturation, float brightness) {
        double rainbow = Math.ceil((System.currentTimeMillis() + (long)delay) / 16L);
        return Color.getHSBColor((float)((rainbow %= 360.0) / 360.0), saturation, brightness);
    }
    public static int getColor(int bright) {
        return getColor(bright, bright, bright, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        color |= blue;
        return color;
    }

    public static int getColor(int brightness, int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }

    public static Color rainbow(int delay, float saturation, float brightness) {
        double rainbow = Math.ceil((System.currentTimeMillis() + delay) / 16);
        rainbow %= 360;
        return Color.getHSBColor((float) (rainbow / 360), saturation, brightness);
    }
    public static int rainbow(float phase) {
        float speed = 3000;
        float hue = (System.currentTimeMillis() % (int) speed) + phase * 3000;
        if (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5F) {
            hue = 0.5F - (hue - 0.5F);
        }
        hue += 0.5F;
        return HSBtoRGB(hue, 0.7f, 1);
    }
    public static int rainbowWex(float phase, float brightness) {
        float speed = 3000;
        float hue = (System.currentTimeMillis() % (int) speed) + phase * 3000;
        if (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5F) {
            hue = 0.5F - (hue - 0.5F);
        }
        hue += 0.5F;
        return HSBtoRGB(hue, 0.7f, brightness);
    }

    public static int fadeColor(int startColor, int endColor, float progress) {
        if (progress > 1) {
            progress = 1 - progress % 1;
        }
        return fade(startColor, endColor, progress);
    }
    public static Color TwoColorEffect(Color color, Color color2, double speed) {
        double thing = speed / 4.0 % 1.0;
        float clamp = MathHelper.clamp((float)(Math.sin(Math.PI * 6 * thing) / 2.0 + 0.5), 0.0f, 1.0f);
        return new Color(MathHelper.lerp((float)color.getRed() / 255.0f, (float)color2.getRed() / 255.0f, clamp), MathHelper.lerp((float)color.getGreen() / 255.0f, (float)color2.getGreen() / 255.0f, clamp), MathHelper.lerp((float)color.getBlue() / 255.0f, (float)color2.getBlue() / 255.0f, clamp));
    }

    public static int fade(int startColor, int endColor, float progress) {
        float invert = 1.0f - progress;
        int r = (int) ((startColor >> 16 & 0xFF) * invert + (endColor >> 16 & 0xFF) * progress);
        int g = (int) ((startColor >> 8 & 0xFF) * invert + (endColor >> 8 & 0xFF) * progress);
        int b = (int) ((startColor & 0xFF) * invert + (endColor & 0xFF) * progress);
        int a = (int) ((startColor >> 24 & 0xFF) * invert + (endColor >> 24 & 0xFF) * progress);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public static Color astolfo(boolean clickgui, int yOffset) {
        float speed = clickgui ? ClickGui.speed.getNumberValue() * 100 : HUD.time.getNumberValue() * 100;
        float hue = (System.currentTimeMillis() % (int) speed) + yOffset;
        if (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5F) {
            hue = 0.5F - (hue - 0.5F);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, 0.4F, 1F);
    }
    public static Color astolfo(float yDist, float yTotal, float saturation, float speedt) {
        float speed;
        float hue;
        for (speed = 1800.0f, hue = System.currentTimeMillis() % (int)speed + (yTotal - yDist) * speedt; hue > speed; hue -= speed) {}
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5f;
        return Color.getHSBColor(hue, saturation, 1.0f);
    }

    public static Color astolfoarray2(boolean clickgui, int yOffset) {
        float speed = ArrayList.time.getNumberValue() * 100;
        float hue = (System.currentTimeMillis() % (int) speed) + yOffset;
        if (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5F) {
            hue = 0.5F - (hue - 0.5F);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, 0.4F, 1F);
    }


}
