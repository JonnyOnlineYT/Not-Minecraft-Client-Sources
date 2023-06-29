package ru.fluger.client;

import java.awt.Color;
import java.text.NumberFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ColorUtils {
   public static Color astolfo(boolean clickgui, int yOffset) {
      float speed = 1500.0F;
      float hue = (float)(System.currentTimeMillis() % 1500L + (long)yOffset);
      if (hue > 1500.0F) {
         hue -= 1500.0F;
      }

      hue /= 1500.0F;
      if (hue > 0.5F) {
         hue = 0.5F - (hue - 0.5F);
      }

      hue += 0.5F;
      return Color.getHSBColor(hue, 0.4F, 1.0F);
   }

   public static int rainbow(float phase) {
      float speed = 3000.0F;
      float hue = (float)(System.currentTimeMillis() % 3000L) + phase * 3000.0F;
      if (hue > 3000.0F) {
         hue -= 3000.0F;
      }

      hue /= 3000.0F;
      if (hue > 0.5F) {
         hue = 0.5F - (hue - 0.5F);
      }

      hue += 0.5F;
      return HSBtoRGB(hue, 0.7F, 1.0F);
   }

   public static Color fade2(int speed, int index, Color color, float alpha) {
      float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), (float[])null);
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      angle = (angle > 180 ? 360 - angle : angle) + 180;
      Color colorHSB = new Color(Color.HSBtoRGB(hsb[0], hsb[1], (float)angle / 360.0F));
      return new Color(colorHSB.getRed(), colorHSB.getGreen(), colorHSB.getBlue(), Math.max(0, Math.min(255, (int)(alpha * 255.0F))));
   }

   public static int setAlpha(int color, int alpha) {
      Color c = new Color(color);
      return (new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha)).getRGB();
   }

   public static int HSBtoRGB(float hue, float saturation, float brightness) {
      int r = 0;
      int g = 0;
      int b = 0;
      if (saturation == 0.0F) {
         g = r = b = (int)(brightness * 255.0F + 0.5F);
      } else {
         float h = (hue - (float)Math.floor((double)hue)) * 6.0F;
         float f = h - (float)Math.floor((double)h);
         float p = brightness * (1.0F - saturation);
         float q = brightness * (1.0F - saturation * f);
         float t = brightness * (1.0F - saturation * (1.0F - f));
         switch ((int)h) {
            case 0:
               r = (int)(brightness * 255.0F + 0.5F);
               g = (int)(t * 255.0F + 0.5F);
               b = (int)(p * 255.0F + 0.5F);
               break;
            case 1:
               r = (int)(q * 255.0F + 0.5F);
               g = (int)(brightness * 255.0F + 0.5F);
               b = (int)(p * 255.0F + 0.5F);
               break;
            case 2:
               r = (int)(p * 255.0F + 0.5F);
               g = (int)(brightness * 255.0F + 0.5F);
               b = (int)(t * 255.0F + 0.5F);
               break;
            case 3:
               r = (int)(p * 255.0F + 0.5F);
               g = (int)(q * 255.0F + 0.5F);
               b = (int)(brightness * 255.0F + 0.5F);
               break;
            case 4:
               r = (int)(t * 255.0F + 0.5F);
               g = (int)(p * 255.0F + 0.5F);
               b = (int)(brightness * 255.0F + 0.5F);
               break;
            case 5:
               r = (int)(brightness * 255.0F + 0.5F);
               g = (int)(p * 255.0F + 0.5F);
               b = (int)(q * 255.0F + 0.5F);
         }
      }

      return -16777216 | r << 16 | g << 8 | b;
   }

   public static Color astolfoColors45(float yDist, float yTotal, float saturation, float speedt) {
      float speed = 1800.0F;

      float hue;
      for(hue = (float)(System.currentTimeMillis() % (long)((int)speed)) + (yTotal - yDist) * speedt; hue > speed; hue -= speed) {
      }

      hue /= speed;
      if ((double)hue > 0.5) {
         hue = 0.5F - (hue - 0.5F);
      }

      hue += 0.5F;
      return Color.getHSBColor(hue, saturation, 1.0F);
   }

   public static Color interpolateColorsBackAndForth(int speed, int index, Color start, Color end, boolean trueColor) {
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      angle = (angle >= 180 ? 360 - angle : angle) * 2;
      return trueColor ? interpolateColorHue(start, end, (float)angle / 360.0F) : interpolateColorC(start, end, (float)angle / 360.0F);
   }

   public static Color interpolateColorHue(Color color1, Color color2, float amount) {
      amount = Math.min(1.0F, Math.max(0.0F, amount));
      float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), (float[])null);
      float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), (float[])null);
      Color resultColor = Color.getHSBColor(interpolateFloat(color1HSB[0], color2HSB[0], (double)amount), interpolateFloat(color1HSB[1], color2HSB[1], (double)amount), interpolateFloat(color1HSB[2], color2HSB[2], (double)amount));
      return new Color(resultColor.getRed(), resultColor.getGreen(), resultColor.getBlue(), interpolateInt(color1.getAlpha(), color2.getAlpha(), (double)amount));
   }

   public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
      return interpolate((double)oldValue, (double)newValue, (double)((float)interpolationValue)).floatValue();
   }

   public static Color astolfoColors(float speed, int yOffset) {
      float hue;
      for(hue = (float)(System.currentTimeMillis() % (long)((int)speed) + (long)yOffset); hue > speed; hue -= speed) {
      }

      if ((double)(hue /= speed) > 0.5) {
         hue = 0.5F - (hue - 0.5F);
      }

      return Color.getHSBColor(hue += 0.5F, 0.4F, 1.0F);
   }

   public static int getHealthColor(float health, float maxHealth) {
      return Color.HSBtoRGB(Math.max(0.0F, Math.min(health, maxHealth) / maxHealth) / 3.0F, 1.0F, 0.8F) | -16777216;
   }

   public static Color interpolateColorC(Color color1, Color color2, float amount) {
      amount = Math.min(1.0F, Math.max(0.0F, amount));
      return new Color(interpolateInt(color1.getRed(), color2.getRed(), (double)amount), interpolateInt(color1.getGreen(), color2.getGreen(), (double)amount), interpolateInt(color1.getBlue(), color2.getBlue(), (double)amount), interpolateInt(color1.getAlpha(), color2.getAlpha(), (double)amount));
   }

   public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
      return interpolate((double)oldValue, (double)newValue, (double)((float)interpolationValue)).intValue();
   }

   public static Color[] getAnalogousColor(Color color) {
      Color[] colors = new Color[2];
      float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), (float[])null);
      float degree = 0.083333336F;
      float newHueAdded = hsb[0] + 0.083333336F;
      colors[0] = new Color(Color.HSBtoRGB(newHueAdded, hsb[1], hsb[2]));
      float newHueSubtracted = hsb[0] - 0.083333336F;
      colors[1] = new Color(Color.HSBtoRGB(newHueSubtracted, hsb[1], hsb[2]));
      return colors;
   }

   public static int applyOpacity(int color, float opacity) {
      Color old = new Color(color);
      return applyOpacity(old, opacity).getRGB();
   }

   public static Color applyOpacity(Color color, float opacity) {
      opacity = Math.min(1.0F, Math.max(0.0F, opacity));
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * opacity));
   }

   public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
      return oldValue + (newValue - oldValue) * interpolationValue;
   }

   public static Color getHealthColor(EntityLivingBase entityLivingBase) {
      float health = entityLivingBase.getHealth();
      float[] fractions = new float[]{0.0F, 0.15F, 0.55F, 0.7F, 0.9F};
      Color[] colors = new Color[]{new Color(133, 0, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN};
      float progress = health / entityLivingBase.getMaxHealth();
      return health >= 0.0F ? blendColors(fractions, colors, progress).brighter() : colors[0];
   }

   public static Color blendColors(float[] fractions, Color[] colors, float progress) {
      if (fractions == null) {
         throw new IllegalArgumentException("Fractions can't be null");
      } else if (colors == null) {
         throw new IllegalArgumentException("Colours can't be null");
      } else if (fractions.length != colors.length) {
         throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
      } else {
         int[] indicies = getFractionIndicies(fractions, progress);
         float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
         Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
         float max = range[1] - range[0];
         float value = progress - range[0];
         float weight = value / max;
         return blend(colorRange[0], colorRange[1], (double)(1.0F - weight));
      }
   }

   public static int[] getFractionIndicies(float[] fractions, float progress) {
      int[] range = new int[2];

      int startPoint;
      for(startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
      }

      if (startPoint >= fractions.length) {
         startPoint = fractions.length - 1;
      }

      range[0] = startPoint - 1;
      range[1] = startPoint;
      return range;
   }

   public static Color blend(Color color1, Color color2, double ratio) {
      float r = (float)ratio;
      float ir = 1.0F - r;
      float[] rgb1 = new float[3];
      float[] rgb2 = new float[3];
      color1.getColorComponents(rgb1);
      color2.getColorComponents(rgb2);
      float red = rgb1[0] * r + rgb2[0] * ir;
      float green = rgb1[1] * r + rgb2[1] * ir;
      float blue = rgb1[2] * r + rgb2[2] * ir;
      if (red < 0.0F) {
         red = 0.0F;
      } else if (red > 255.0F) {
         red = 255.0F;
      }

      if (green < 0.0F) {
         green = 0.0F;
      } else if (green > 255.0F) {
         green = 255.0F;
      }

      if (blue < 0.0F) {
         blue = 0.0F;
      } else if (blue > 255.0F) {
         blue = 255.0F;
      }

      Color color3 = null;

      try {
         color3 = new Color(red, green, blue);
      } catch (IllegalArgumentException var13) {
         NumberFormat.getNumberInstance();
      }

      return color3;
   }

   public static Color TwoColorEffect(Color cl1, Color cl2, double speed) {
      double thing = speed / 4.0 % 1.0;
      float val = MathHelper.clamp((float)Math.sin(18.84955592153876 * thing) / 2.0F + 0.5F, 0.0F, 1.0F);
      return new Color(lerp((float)cl1.getRed() / 255.0F, (float)cl2.getRed() / 255.0F, val), lerp((float)cl1.getGreen() / 255.0F, (float)cl2.getGreen() / 255.0F, val), lerp((float)cl1.getBlue() / 255.0F, (float)cl2.getBlue() / 255.0F, val));
   }

   public static float lerp(float a, float b, float f) {
      return a + f * (b - a);
   }

   public static int fadeColor(int startColor, int endColor, float progress) {
      if (progress > 1.0F) {
         progress = 1.0F - progress % 1.0F;
      }

      return fade(startColor, endColor, progress);
   }

   public static Color rainbowCol(float yDist, float yTotal, float saturation, float speedt) {
      float speed = 1800.0F;

      float hue;
      for(hue = (float)(System.currentTimeMillis() % (long)((int)speed)) + (yTotal - yDist) * speedt; hue > speed; hue -= speed) {
      }

      hue /= speed;
      if (hue > 5.0F) {
         hue = 5.0F - (hue - 5.0F);
      }

      hue += 5.0F;
      return Color.getHSBColor(hue, saturation, 1.0F);
   }

   public static Color fade(int speed, int index, Color color, float alpha) {
      float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), (float[])null);
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      angle = (angle > 180 ? 360 - angle : angle) + 180;
      Color colorHSB = new Color(Color.HSBtoRGB(hsb[0], hsb[1], (float)angle / 360.0F));
      return new Color(colorHSB.getRed(), colorHSB.getGreen(), colorHSB.getBlue(), Math.max(0, Math.min(255, (int)(alpha * 255.0F))));
   }

   public static int fade(int startColor, int endColor, float progress) {
      float invert = 1.0F - progress;
      int r = (int)((float)(startColor >> 16 & 255) * invert + (float)(endColor >> 16 & 255) * progress);
      int g = (int)((float)(startColor >> 8 & 255) * invert + (float)(endColor >> 8 & 255) * progress);
      int b = (int)((float)(startColor & 255) * invert + (float)(endColor & 255) * progress);
      int a = (int)((float)(startColor >> 24 & 255) * invert + (float)(endColor >> 24 & 255) * progress);
      return (a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | b & 255;
   }

   public static Color astolfo(float yDist, float yTotal) {
      float speed = 3500.0F;

      float hue;
      for(hue = (float)(System.currentTimeMillis() % (long)((int)speed)) + (yTotal - yDist) * 12.0F; hue > speed; hue -= speed) {
      }

      hue /= speed;
      if ((double)hue > 0.5) {
         hue = 0.5F - (hue - 0.5F);
      }

      hue += 0.5F;
      return new Color(hue, 0.4F, 1.0F);
   }

   public static Color astolfo(float yDist, float yTotal, float saturation, float speedt) {
      float speed = 1800.0F;

      float hue;
      for(hue = (float)(System.currentTimeMillis() % (long)((int)speed)) + (yTotal - yDist) * speedt; hue > speed; hue -= speed) {
      }

      hue /= speed;
      if ((double)hue > 0.5) {
         hue = 0.5F - (hue - 0.5F);
      }

      hue += 0.5F;
      return Color.getHSBColor(hue, saturation, 1.0F);
   }

   public static int getColor(int red, int green, int blue) {
      return getColor(red, green, blue, 255);
   }

   public static int getColor(int red, int green, int blue, int alpha) {
      int color = 0;
      color |= alpha << 24;
      color |= red << 16;
      color |= green << 8;
      return color | blue;
   }

   public static int getColor(Color color) {
      return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
   }

   public static int getColor(int bright) {
      return getColor(bright, bright, bright, 255);
   }

   public static int getColor(int brightness, int alpha) {
      return getColor(brightness, brightness, brightness, alpha);
   }

   public static Color rainbow(int speed, int index, float saturation, float brightness, float opacity) {
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      float hue = (float)angle / 360.0F;
      Color color = new Color(Color.HSBtoRGB(hue, saturation, brightness));
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int)(opacity * 255.0F))));
   }

   public static Color rainbow(int delay, float saturation, float brightness) {
      double rainbow = Math.ceil((double)((System.currentTimeMillis() + (long)delay) / 16L));
      rainbow %= 360.0;
      return Color.getHSBColor((float)(rainbow / 360.0), saturation, brightness);
   }
}
