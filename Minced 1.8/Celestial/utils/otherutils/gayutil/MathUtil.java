package Celestial.utils.otherutils.gayutil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

public class MathUtil
{
    public static int getRandomInRange(int min, int max) {
        /* 10 */     return (int)(Math.random() * (max - min) + min);
    }

    public static float getRandomInRange(float min, float max) {
        /* 14 */     SecureRandom random = new SecureRandom();
        /* 15 */     return random.nextFloat() * (max - min) + min;
    }

    public static double getRandomInRange(double min, double max) {
        /* 19 */     SecureRandom random = new SecureRandom();
        /* 20 */     return random.nextDouble() * (max - min) + min;
    }

    public static double lerp(double old, double newVal, double amount) {
        /* 24 */     return (1.0D - amount) * old + amount * newVal;
    }

    public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
        /* 28 */     return Double.valueOf(oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
        /* 32 */     return interpolate(oldValue, newValue, (float)interpolationValue).floatValue();
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
        /* 36 */     return interpolate(oldValue, newValue, (float)interpolationValue).intValue();
    }

    public static float calculateGaussianValue(float x, float sigma) {
        /* 40 */     double PI = 3.141592653D;
        /* 41 */     double output = 1.0D / Math.sqrt(2.0D * PI * (sigma * sigma));
        /* 42 */     return (float)(output * Math.exp(-(x * x) / 2.0D * (sigma * sigma)));
    }

    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    public static double round(double num, double increment) {
        /* 50 */     BigDecimal bd = new BigDecimal(num);
        /* 51 */     bd = bd.setScale((int)increment, RoundingMode.HALF_UP);
        /* 52 */     return bd.doubleValue();
    }

    public static double round(double value, int places) {
        /* 56 */     if (places < 0) {
            /* 57 */       throw new IllegalArgumentException();
        }
        /* 59 */     BigDecimal bd = new BigDecimal(value);
        /* 60 */     bd = bd.setScale(places, RoundingMode.HALF_UP);
        /* 61 */     return bd.doubleValue();
    }

    public static float getRandomFloat(float max, float min) {
        /* 65 */     SecureRandom random = new SecureRandom();
        /* 66 */     return random.nextFloat() * (max - min) + min;
    }
}