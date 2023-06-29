package ua.apraxia.utility.other;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import ua.apraxia.modules.impl.display.ModuleList;
import ua.apraxia.utility.font.Fonts;
import ua.apraxia.utility.render.ColorUtilityAlt;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ua.apraxia.modules.impl.display.ModuleList.*;
import static ua.apraxia.modules.impl.render.ChinaHat.saturation;
import static ua.apraxia.utility.math.MathUtility.*;


public class ColorUtility {
    public static void glColor(Color color) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static Color skyRainbow(int speed, int index, float saturation) {
        int angle = (int) ((System.currentTimeMillis() / speed + index) % 360);
        float hue = angle / 360f;
        return Color.getHSBColor((double) ((float) ((angle %= 360.0) / 360.0)) < 0.5 ? -((float) (angle / 360.0))
                : (float) (angle / 360.0),  saturation, 1.0F);
    }

    public static Color getClientColor(float yStep, float yStepFull, int speed) {
        Color color = Color.white;
        Color onecolor = new Color(ModuleList.color.color);
        Color twoColor = new Color(coloralt.color);
        double time = 10;

        float yDist = (float) 4;
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Fonts.sfbolt16.getFontHeight() + 5;
        }
        if (mode228.is("Rainbow")) {
            color = ColorUtilityAlt.rainbow((int) (yStep * time), ModuleList.saturation.value, brightness.value);
        } else if (mode228.is("Astolfo")) {
            color = ColorUtility.skyRainbow(speed, (int) yStep, ModuleList.saturation.value);
        } else if (mode228.is("Custom")) { //Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yStep / 10) / 60
            color = ColorUtility.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yStep / 10) / 60 + speed);
        } else if (mode228.is("Fade")) {
            color = ColorUtility.interpolateColorsBackAndForth((int) speed, (int) ((int) yStep), new Color(ModuleList.color.color), new Color(ModuleList.color.color).darker().darker().darker(), false);
        }
        return color;
    }

    public static int rainbow(int delay, double speed) {
        double rainbow = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / speed);
        return Color.getHSBColor((float)(-((rainbow %= 360.0) / 360.0)), 0.9f, 1.0f).getRGB();
    }

    public static Color TwoColoreffect(Color cl1, Color cl2, double speed) {
        double thing = speed / 4.0 % 1.0;
        float val = MathHelper.clamp((float) Math.sin(Math.PI * 6 * thing) / 2.0f + 0.5f, 0.0f, 1.0f);
        return new Color(lerp((float) cl1.getRed() / 255.0f, (float) cl2.getRed() / 255.0f, val),
                lerp((float) cl1.getGreen() / 255.0f, (float) cl2.getGreen() / 255.0f, val),
                lerp((float) cl1.getBlue() / 255.0f, (float) cl2.getBlue() / 255.0f, val));
    }

    public static int fade(Color color, int delay) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((float)(System.currentTimeMillis() % 2000L + (long)delay) / 1000.0f % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    public static Color fade2(int speed, int index, Color color, float alpha) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        angle = (angle > 180 ? 360 - angle : angle) + 180;
        Color colorHSB = new Color(Color.HSBtoRGB(hsb[0], hsb[1], (float)angle / 360.0f));
        return new Color(colorHSB.getRed(), colorHSB.getGreen(), colorHSB.getBlue(), Math.max(0, Math.min(255, (int)(alpha * 255.0f))));
    }

    public static Color astolfoRainbow(int offset) {
        float hue;
        float speed = 3000.0f;
        for (hue = (float)(System.currentTimeMillis() % (long)((int)speed) + (long)offset); hue > speed; hue -= speed) {
        }
        if ((double)(hue /= speed) > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        return Color.getHSBColor(hue += 0.5f, 0.4f, 1.0f);
    }

    public static Color mixColors(Color color1, Color color2, double percent) {
        double inverse_percent = 1.0 - percent;
        int redPart = (int)((double)color1.getRed() * percent + (double)color2.getRed() * inverse_percent);
        int greenPart = (int)((double)color1.getGreen() * percent + (double)color2.getGreen() * inverse_percent);
        int bluePart = (int)((double)color1.getBlue() * percent + (double)color2.getBlue() * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }

    public static void glColor(int hex, float alpha) {
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha / 255.0f);
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static Color[] getAnalogousColor(Color color) {
        Color[] colors = new Color[2];
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float degree = 0.083333336f;
        float newHueAdded = hsb[0] + degree;
        colors[0] = new Color(Color.HSBtoRGB(newHueAdded, hsb[1], hsb[2]));
        float newHueSubtracted = hsb[0] - degree;
        colors[1] = new Color(Color.HSBtoRGB(newHueSubtracted, hsb[1], hsb[2]));
        return colors;
    }


    public static Color hslToRGB(float[] hsl) {
        float red;
        float green;
        float blue;
        if (hsl[1] == 0.0f) {
            blue = 1.0f;
            green = 1.0f;
            red = 1.0f;
        } else {
            float q = (double)hsl[2] < 0.5 ? hsl[2] * (1.0f + hsl[1]) : hsl[2] + hsl[1] - hsl[2] * hsl[1];
            float p = 2.0f * hsl[2] - q;
            red = ColorUtility.hueToRGB(p, q, hsl[0] + 0.33333334f);
            green = ColorUtility.hueToRGB(p, q, hsl[0]);
            blue = ColorUtility.hueToRGB(p, q, hsl[0] - 0.33333334f);
        }
        return new Color((int)(red *= 255.0f), (int)(green *= 255.0f), (int)(blue *= 255.0f));
    }

    public static float hueToRGB(float p, float q, float t) {
        float newT = t;
        if (newT < 0.0f) {
            newT += 1.0f;
        }
        if (newT > 1.0f) {
            newT -= 1.0f;
        }
        if (newT < 0.16666667f) {
            return p + (q - p) * 6.0f * newT;
        }
        if (newT < 0.5f) {
            return q;
        }
        if (newT < 0.6666667f) {
            return p + (q - p) * (0.6666667f - newT) * 6.0f;
        }
        return p;
    }

    public static float[] rgbToHSL(Color rgb) {
        float red = (float)rgb.getRed() / 255.0f;
        float green = (float)rgb.getGreen() / 255.0f;
        float blue = (float)rgb.getBlue() / 255.0f;
        float max = Math.max(Math.max(red, green), blue);
        float min2 = Math.min(Math.min(red, green), blue);
        float c = (max + min2) / 2.0f;
        float[] hsl = new float[]{c, c, c};
        if (max == min2) {
            hsl[1] = 0.0f;
            hsl[0] = 0.0f;
        } else {
            float d = max - min2;
            float f = hsl[1] = (double)hsl[2] > 0.5 ? d / (2.0f - max - min2) : d / (max + min2);
            if (max == red) {
                hsl[0] = (green - blue) / d + (float)(green < blue ? 6 : 0);
            } else if (max == blue) {
                hsl[0] = (blue - red) / d + 2.0f;
            } else if (max == green) {
                hsl[0] = (red - green) / d + 4.0f;
            }
            hsl[0] = hsl[0] / 6.0f;
        }
        return hsl;
    }

    public static int applyOpacity(int color, float opacity) {
        Color old = new Color(color);
        return ColorUtility.applyOpacity(old, opacity).getRGB();
    }

    public static Color applyOpacity(Color color, float opacity) {
        opacity = Math.min(1.0f, Math.max(0.0f, opacity));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * opacity));
    }

    public static Color darker(Color color, float FACTOR) {
        return new Color(Math.max((int)((float)color.getRed() * FACTOR), 0), Math.max((int)((float)color.getGreen() * FACTOR), 0), Math.max((int)((float)color.getBlue() * FACTOR), 0), color.getAlpha());
    }

    public static Color brighter(Color color, float FACTOR) {
        int r = color.getRed();
        int g2 = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();
        int i = (int)(1.0 / (1.0 - (double)FACTOR));
        if (r == 0 && g2 == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if (r > 0 && r < i) {
            r = i;
        }
        if (g2 > 0 && g2 < i) {
            g2 = i;
        }
        if (b > 0 && b < i) {
            b = i;
        }
        return new Color(Math.min((int)((float)r / FACTOR), 255), Math.min((int)((float)g2 / FACTOR), 255), Math.min((int)((float)b / FACTOR), 255), alpha);
    }

    public static Color averageColor(BufferedImage bi, int width, int height, int pixelStep) {
        int[] color = new int[3];
        for (int x = 0; x < width; x += pixelStep) {
            for (int y = 0; y < height; y += pixelStep) {
                Color pixel = new Color(bi.getRGB(x, y));
                color[0] = color[0] + pixel.getRed();
                color[1] = color[1] + pixel.getGreen();
                color[2] = color[2] + pixel.getBlue();
            }
        }
        int num = width * height / (pixelStep * pixelStep);
        return new Color(color[0] / num, color[1] / num, color[2] / num);
    }

    public static Color rainbow(int speed, int index, float saturation, float brightness, float opacity) {
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        float hue = (float)angle / 360.0f;
        Color color = new Color(Color.HSBtoRGB(hue, saturation, brightness));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int)(opacity * 255.0f))));
    }


    public static Color fade(int speed, int index, Color color, float alpha) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        angle = (angle > 180 ? 360 - angle : angle) + 180;
        Color colorHSB = new Color(Color.HSBtoRGB(hsb[0], hsb[1], (float)angle / 360.0f));
        return new Color(colorHSB.getRed(), colorHSB.getGreen(), colorHSB.getBlue(), Math.max(0, Math.min(255, (int)(alpha * 255.0f))));
    }

    private static float getAnimationEquation(int index, int speed) {
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        return (float)((angle > 180 ? 360 - angle : angle) + 180) / 360.0f;
    }

    public static int[] createColorArray(int color) {
        return new int[]{ColorUtility.bitChangeColor(color, 16), ColorUtility.bitChangeColor(color, 8), ColorUtility.bitChangeColor(color, 0), ColorUtility.bitChangeColor(color, 24)};
    }

    public static int getOppositeColor(int color) {
        int R = ColorUtility.bitChangeColor(color, 0);
        int G = ColorUtility.bitChangeColor(color, 8);
        int B = ColorUtility.bitChangeColor(color, 16);
        int A2 = ColorUtility.bitChangeColor(color, 24);
        R = 255 - R;
        G = 255 - G;
        B = 255 - B;
        return R + (G << 8) + (B << 16) + (A2 << 24);
    }

    private static int bitChangeColor(int color, int bitChange) {
        return color >> bitChange & 0xFF;
    }


    public static Color interpolateColorsBackAndForth(int speed, int index, Color start, Color end, boolean trueColor) {
        int angle = (int) (((System.currentTimeMillis()) / speed + index) % 360);
        angle = (angle >= 180 ? 360 - angle : angle) * 2;
        return trueColor ? ColorUtility.interpolateColorHue(start, end, angle / 360f) : ColorUtility.interpolateColorC(start, end, angle / 360f);
    }

    public static Color interpolateColorC(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));
        return new Color(interpolateInt(color1.getRed(), color2.getRed(), amount),
                interpolateInt(color1.getGreen(), color2.getGreen(), amount),
                interpolateInt(color1.getBlue(), color2.getBlue(), amount),
                interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }

    public static Color interpolateColorHue(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));

        float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
        float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);

        Color resultColor = Color.getHSBColor(interpolateFloat(color1HSB[0], color2HSB[0], amount),
                interpolateFloat(color1HSB[1], color2HSB[1], amount), interpolateFloat(color1HSB[2], color2HSB[2], amount));

        return new Color(resultColor.getRed(), resultColor.getGreen(), resultColor.getBlue(),
                interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }
}

