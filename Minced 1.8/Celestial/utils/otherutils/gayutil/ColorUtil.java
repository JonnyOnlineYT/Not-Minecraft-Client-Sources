package Celestial.utils.otherutils.gayutil;

import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorUtil
{
    public static void glColor(Color color) {
        /*  13 */     float red = color.getRed() / 255.0F;
        /*  14 */     float green = color.getGreen() / 255.0F;
        /*  15 */     float blue = color.getBlue() / 255.0F;
        /*  16 */     float alpha = color.getAlpha() / 255.0F;

        /*  18 */     GlStateManager.color(red, green, blue, alpha);
    }
    public static int rainbow(int delay, double speed) {
        /*  21 */     double rainbow = Math.ceil((System.currentTimeMillis() + delay) / speed);
        /*  22 */     rainbow %= 360.0D;
        /*  23 */     return Color.getHSBColor((float)-(rainbow / 360.0D), 0.9F, 1.0F).getRGB();
    }
    public static int fade(Color color, int delay) {
        /*  26 */     float[] hsb = new float[3];
        /*  27 */     Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        /*  28 */     float brightness = Math.abs((float)(System.currentTimeMillis() % 2000L + delay) / 1000.0F % 2.0F - 1.0F);
        /*  29 */     brightness = 0.5F + 0.5F * brightness;
        /*  30 */     hsb[2] = brightness % 2.0F;
        /*  31 */     return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }
    public static Color fade2(int speed, int index, Color color, float alpha) {
        /*  34 */     float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        /*  35 */     int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        /*  36 */     angle = ((angle > 180) ? (360 - angle) : angle) + 180;

        /*  38 */     Color colorHSB = new Color(Color.HSBtoRGB(hsb[0], hsb[1], angle / 360.0F));

        /*  40 */     return new Color(colorHSB.getRed(), colorHSB.getGreen(), colorHSB.getBlue(),
                /*  41 */         Math.max(0, Math.min(255, (int)(alpha * 255.0F))));
    }
    public static Color astolfoRainbow(int offset) {
        /*  44 */     float speed = 3000.0F;
        /*  45 */     float hue = (float)(System.currentTimeMillis() % (int)speed + offset);
        /*  46 */     while (hue > speed) {
            /*  47 */       hue -= speed;
        }
        /*  49 */     hue /= speed;
        /*  50 */     if (hue > 0.5D) {
            /*  51 */       hue = 0.5F - hue - 0.5F;
        }
        /*  53 */     hue += 0.5F;
        /*  54 */     return Color.getHSBColor(hue, 0.4F, 1.0F);
    }
    public static Color mixColors(Color color1, Color color2, double percent) {
        /*  57 */     double inverse_percent = 1.0D - percent;
        /*  58 */     int redPart = (int)(color1.getRed() * percent + color2.getRed() * inverse_percent);
        /*  59 */     int greenPart = (int)(color1.getGreen() * percent + color2.getGreen() * inverse_percent);
        /*  60 */     int bluePart = (int)(color1.getBlue() * percent + color2.getBlue() * inverse_percent);
        /*  61 */     return new Color(redPart, greenPart, bluePart);
    }
    public static void glColor(int hex, float alpha) {
        /*  64 */     float red = (hex >> 16 & 0xFF) / 255.0F;
        /*  65 */     float green = (hex >> 8 & 0xFF) / 255.0F;
        /*  66 */     float blue = (hex & 0xFF) / 255.0F;

        /*  68 */     GlStateManager.color(red, green, blue, alpha / 255.0F);
    }

    public static void glColor(int hex) {
        /*  72 */     float alpha = (hex >> 24 & 0xFF) / 255.0F;
        /*  73 */     float red = (hex >> 16 & 0xFF) / 255.0F;
        /*  74 */     float green = (hex >> 8 & 0xFF) / 255.0F;
        /*  75 */     float blue = (hex & 0xFF) / 255.0F;

        /*  77 */     GlStateManager.color(red, green, blue, alpha);
    }

    public static Color[] getAnalogousColor(Color color) {
        /*  81 */     Color[] colors = new Color[2];
        /*  82 */     float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

        /*  84 */     float degree = 0.083333336F;

        /*  86 */     float newHueAdded = hsb[0] + degree;
        /*  87 */     colors[0] = new Color(Color.HSBtoRGB(newHueAdded, hsb[1], hsb[2]));

        /*  89 */     float newHueSubtracted = hsb[0] - degree;

        /*  91 */     colors[1] = new Color(Color.HSBtoRGB(newHueSubtracted, hsb[1], hsb[2]));

        /*  93 */     return colors;
    }
    public static Color skyRainbow(int speed, int index) {
        /*  96 */     int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        /*  97 */     float hue = angle / 360.0F;
        /*  98 */     return Color.getHSBColor(((float)((angle = (int)(angle % 360.0D)) / 360.0D) < 0.5D) ? -((float)(angle / 360.0D)) : (float)(angle / 360.0D), 0.5F, 1.0F);
    }




    public static Color hslToRGB(float[] hsl) {
        float red, green, blue;
        /* 106 */     if (hsl[1] == 0.0F) {
            /* 107 */       red = green = blue = 1.0F;
        } else {
            /* 109 */       float q = (hsl[2] < 0.5D) ? (hsl[2] * (1.0F + hsl[1])) : (hsl[2] + hsl[1] - hsl[2] * hsl[1]);
            /* 110 */       float p = 2.0F * hsl[2] - q;

            /* 112 */       red = hueToRGB(p, q, hsl[0] + 0.33333334F);
            /* 113 */       green = hueToRGB(p, q, hsl[0]);
            /* 114 */       blue = hueToRGB(p, q, hsl[0] - 0.33333334F);
        }

        /* 117 */     red *= 255.0F;
        /* 118 */     green *= 255.0F;
        /* 119 */     blue *= 255.0F;

        /* 121 */     return new Color((int)red, (int)green, (int)blue);
    }


    public static float hueToRGB(float p, float q, float t) {
        /* 126 */     float newT = t;
        /* 127 */     if (newT < 0.0F) newT++;
        /* 128 */     if (newT > 1.0F) newT--;
        /* 129 */     if (newT < 0.16666667F) return p + (q - p) * 6.0F * newT;
        /* 130 */     if (newT < 0.5F) return q;
        /* 131 */     if (newT < 0.6666667F) return p + (q - p) * (0.6666667F - newT) * 6.0F;
        /* 132 */     return p;
    }

    public static float[] rgbToHSL(Color rgb) {
        /* 136 */     float red = rgb.getRed() / 255.0F;
        /* 137 */     float green = rgb.getGreen() / 255.0F;
        /* 138 */     float blue = rgb.getBlue() / 255.0F;

        /* 140 */     float max = Math.max(Math.max(red, green), blue);
        /* 141 */     float min = Math.min(Math.min(red, green), blue);
        /* 142 */     float c = (max + min) / 2.0F;
        /* 143 */     float[] hsl = { c, c, c };

        /* 145 */     if (max == min) {
            /* 146 */       hsl[1] = 0.0F; hsl[0] = 0.0F;
        } else {
            /* 148 */       float d = max - min;
            /* 149 */       hsl[1] = (hsl[2] > 0.5D) ? (d / (2.0F - max - min)) : (d / (max + min));

            /* 151 */       if (max == red) {
                /* 152 */         //hsl[0] = (green - blue) / d + ((green < blue) ? 6 : false);
                /* 153 */       } else if (max == blue) {
                /* 154 */         hsl[0] = (blue - red) / d + 2.0F;
                /* 155 */       } else if (max == green) {
                /* 156 */         hsl[0] = (red - green) / d + 4.0F;
            }
            /* 158 */       hsl[0] = hsl[0] / 6.0F;
        }
        /* 160 */     return hsl;
    }


    public static Color imitateTransparency(Color backgroundColor, Color accentColor, float percentage) {
        /* 165 */     return new Color(interpolateColor(backgroundColor, accentColor, 255.0F * percentage / 255.0F));
    }

    public static int applyOpacity(int color, float opacity) {
        /* 169 */     Color old = new Color(color);
        /* 170 */     return applyOpacity(old, opacity).getRGB();
    }


    public static Color applyOpacity(Color color, float opacity) {
        /* 175 */     opacity = Math.min(1.0F, Math.max(0.0F, opacity));
        /* 176 */     return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha() * opacity));
    }

    public static Color darker(Color color, float FACTOR) {
        /* 180 */     return new Color(Math.max((int)(color.getRed() * FACTOR), 0),
                /* 181 */         Math.max((int)(color.getGreen() * FACTOR), 0),
                /* 182 */         Math.max((int)(color.getBlue() * FACTOR), 0), color
/* 183 */         .getAlpha());
    }

    public static Color brighter(Color color, float FACTOR) {
        /* 187 */     int r = color.getRed();
        /* 188 */     int g = color.getGreen();
        /* 189 */     int b = color.getBlue();
        /* 190 */     int alpha = color.getAlpha();






        /* 197 */     int i = (int)(1.0D / (1.0D - FACTOR));
        /* 198 */     if (r == 0 && g == 0 && b == 0) {
            /* 199 */       return new Color(i, i, i, alpha);
        }
        /* 201 */     if (r > 0 && r < i) r = i;
        /* 202 */     if (g > 0 && g < i) g = i;
        /* 203 */     if (b > 0 && b < i) b = i;

        /* 205 */     return new Color(Math.min((int)(r / FACTOR), 255),
                /* 206 */         Math.min((int)(g / FACTOR), 255),
                /* 207 */         Math.min((int)(b / FACTOR), 255), alpha);
    }






    public static Color averageColor(BufferedImage bi, int width, int height, int pixelStep) {
        /* 216 */     int[] color = new int[3]; int x;
        /* 217 */     for (x = 0; x < width; x += pixelStep) {
            /* 218 */       int y; for (y = 0; y < height; y += pixelStep) {
                /* 219 */         Color pixel = new Color(bi.getRGB(x, y));
                /* 220 */         color[0] = color[0] + pixel.getRed();
                /* 221 */         color[1] = color[1] + pixel.getGreen();
                /* 222 */         color[2] = color[2] + pixel.getBlue();
            }
        }
        /* 225 */     int num = width * height / pixelStep * pixelStep;
        /* 226 */     return new Color(color[0] / num, color[1] / num, color[2] / num);
    }

    public static Color rainbow(int speed, int index, float saturation, float brightness, float opacity) {
        /* 230 */     int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        /* 231 */     float hue = angle / 360.0F;
        /* 232 */     Color color = new Color(Color.HSBtoRGB(hue, saturation, brightness));
        /* 233 */     return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int)(opacity * 255.0F))));
    }

    public static Color interpolateColorsBackAndForth(int speed, int index, Color start, Color end, boolean trueColor) {
        /* 237 */     int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        /* 238 */     angle = ((angle >= 180) ? (360 - angle) : angle) * 2;
        /* 239 */     return trueColor ? interpolateColorHue(start, end, angle / 360.0F) : interpolateColorC(start, end, angle / 360.0F);
    }


    public static int interpolateColor(Color color1, Color color2, float amount) {
        /* 244 */     amount = Math.min(1.0F, Math.max(0.0F, amount));
        /* 245 */     return interpolateColorC(color1, color2, amount).getRGB();
    }

    public static int interpolateColor(int color1, int color2, float amount) {
        /* 249 */     amount = Math.min(1.0F, Math.max(0.0F, amount));
        /* 250 */     Color cColor1 = new Color(color1);
        /* 251 */     Color cColor2 = new Color(color2);
        /* 252 */     return interpolateColorC(cColor1, cColor2, amount).getRGB();
    }

    public static Color interpolateColorC(Color color1, Color color2, float amount) {
        /* 256 */     amount = Math.min(1.0F, Math.max(0.0F, amount));
        /* 257 */     return new Color(MathUtil.interpolateInt(color1.getRed(), color2.getRed(), amount),
                /* 258 */         MathUtil.interpolateInt(color1.getGreen(), color2.getGreen(), amount),
                /* 259 */         MathUtil.interpolateInt(color1.getBlue(), color2.getBlue(), amount),
                /* 260 */         MathUtil.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }

    public static Color interpolateColorHue(Color color1, Color color2, float amount) {
        /* 264 */     amount = Math.min(1.0F, Math.max(0.0F, amount));

        /* 266 */     float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
        /* 267 */     float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);

        /* 269 */     Color resultColor = Color.getHSBColor(MathUtil.interpolateFloat(color1HSB[0], color2HSB[0], amount),
                /* 270 */         MathUtil.interpolateFloat(color1HSB[1], color2HSB[1], amount), MathUtil.interpolateFloat(color1HSB[2], color2HSB[2], amount));

        /* 272 */     return new Color(resultColor.getRed(), resultColor.getGreen(), resultColor.getBlue(),
                /* 273 */         MathUtil.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }



    public static Color fade(int speed, int index, Color color, float alpha) {
        /* 279 */     float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        /* 280 */     int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        /* 281 */     angle = ((angle > 180) ? (360 - angle) : angle) + 180;

        /* 283 */     Color colorHSB = new Color(Color.HSBtoRGB(hsb[0], hsb[1], angle / 360.0F));

        /* 285 */     return new Color(colorHSB.getRed(), colorHSB.getGreen(), colorHSB.getBlue(), Math.max(0, Math.min(255, (int)(alpha * 255.0F))));
    }


    private static float getAnimationEquation(int index, int speed) {
        /* 290 */     int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        /* 291 */     return (((angle > 180) ? (360 - angle) : angle) + 180) / 360.0F;
    }

    public static int[] createColorArray(int color) {
        /* 295 */     return new int[] { bitChangeColor(color, 16), bitChangeColor(color, 8), bitChangeColor(color, 0), bitChangeColor(color, 24) };
    }

    public static int getOppositeColor(int color) {
        /* 299 */     int R = bitChangeColor(color, 0);
        /* 300 */     int G = bitChangeColor(color, 8);
        /* 301 */     int B = bitChangeColor(color, 16);
        /* 302 */     int A = bitChangeColor(color, 24);
        /* 303 */     R = 255 - R;
        /* 304 */     G = 255 - G;
        /* 305 */     B = 255 - B;
        /* 306 */     return R + (G << 8) + (B << 16) + (A << 24);
    }

    private static int bitChangeColor(int color, int bitChange) {
        /* 310 */     return color >> bitChange & 0xFF;
    }
}

