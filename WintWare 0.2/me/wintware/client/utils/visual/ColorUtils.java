package me.wintware.client.utils.visual;

import java.awt.Color;
import java.text.NumberFormat;
import net.minecraft.entity.EntityLivingBase;

public class ColorUtils {
   public static int getColor(Color color) {
      return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
   }

   public static Color getColorWithOpacity(Color color, int alpha) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
   }

   public static Color fade(Color color) {
      return fade(color, 2, 100);
   }

   public static int color(int n, int n2, int n3, int n4) {
      int n4 = 255;
      return (new Color(n, n2, n3, n4)).getRGB();
   }

   public static Color getGradientOffset(Color color1, Color color2, double offset) {
      double inverse_percent;
      int redPart;
      if (offset > 1.0D) {
         inverse_percent = offset % 1.0D;
         redPart = (int)offset;
         offset = redPart % 2 == 0 ? inverse_percent : 1.0D - inverse_percent;
      }

      inverse_percent = 1.0D - offset;
      redPart = (int)((double)color1.getRed() * inverse_percent + (double)color2.getRed() * offset);
      int greenPart = (int)((double)color1.getGreen() * inverse_percent + (double)color2.getGreen() * offset);
      int bluePart = (int)((double)color1.getBlue() * inverse_percent + (double)color2.getBlue() * offset);
      return new Color(redPart, greenPart, bluePart);
   }

   public static int getColor(int red, int green, int blue) {
      return getColor(red, green, blue, 255);
   }

   public static int getColor(int red, int green, int blue, int alpha) {
      int color = 0;
      int color = color | alpha << 24;
      color |= red << 16;
      color |= green << 8;
      color |= blue;
      return color;
   }

   public static int getColor(int brightness) {
      return getColor(brightness, brightness, brightness, 255);
   }

   public static int getColor(int brightness, int alpha) {
      return getColor(brightness, brightness, brightness, alpha);
   }

   public static Color getHealthColor(float health, float maxHealth) {
      float[] fractions = new float[]{0.0F, 0.5F, 1.0F};
      Color[] colors = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
      float progress = health / maxHealth;
      return blendColors(fractions, colors, progress).brighter();
   }

   public static Color fade(Color color, int index, int count) {
      float[] hsb = new float[3];
      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
      float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)index / (float)count * 2.0F) % 2.0F - 1.0F);
      brightness = 0.5F + 0.5F * brightness;
      hsb[2] = brightness % 2.0F;
      return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
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

      Color color = null;

      try {
         color = new Color(red, green, blue);
      } catch (IllegalArgumentException var14) {
         NumberFormat var13 = NumberFormat.getNumberInstance();
      }

      return color;
   }

   public static int astolfo(int delay, float offset) {
      float speed = 3000.0F;

      float hue;
      for(hue = Math.abs((float)(System.currentTimeMillis() % (long)delay) + -offset / 21.0F * 2.0F); hue > speed; hue -= speed) {
      }

      hue /= speed;
      if ((double)hue > 0.5D) {
         hue = 0.5F - (hue - 0.5F);
      }

      hue += 0.5F;
      Color color = new Color(255, 0, 0);
      float[] hsb = new float[3];
      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
      return Color.HSBtoRGB(hue + (float)color.getRGB(), 0.5F, 1.0F);
   }

   public static int Yellowastolfo(int delay, float offset) {
      float speed = 3000.0F;

      float hue;
      for(hue = (float)(System.currentTimeMillis() % (long)delay) + offset * 55.0F; hue > speed; hue -= speed) {
      }

      hue /= speed;
      if ((double)hue > 0.6D) {
         hue = 0.6F - (hue - 0.6F);
      }

      hue += 0.6F;
      Color color = new Color(255, 0, 0);
      float[] hsb = new float[3];
      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
      return Color.HSBtoRGB(hue + (float)color.getRGB(), 0.5F, 1.0F);
   }
}
