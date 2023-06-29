package org.moonware.client.feature.impl.visual.anim;

public class MathUtil {
    public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
        return interpolate(oldValue, newValue, (float) interpolationValue).floatValue();
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
    }
    public static float random(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }
    public static double easeInOutQuart(double x) {
        return (x < 0.5) ? 8 * x * x * x * x : 1 - Math.pow(-2 * x + 2, 4) / 2;
    }
    public static double round(double wert, int stellen) {
        return Math.round(wert * Math.pow(10.0D, stellen)) / Math.pow(10.0D, stellen);
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

}
