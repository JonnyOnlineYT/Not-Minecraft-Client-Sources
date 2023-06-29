package org.spray.heaven.util;

import java.nio.FloatBuffer;

public class MathUtil {

    public static int ceilToPOT(int valueIn) {
        int i = valueIn;
        i = (--i | i >> 1);
        i |= i >> 2;
        i |= i >> 4;
        i |= i >> 8;
        i |= i >> 16;
        return ++i;
    }

    public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
        return interpolate(oldValue, newValue, (float) interpolationValue).floatValue();
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
    }

    public static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static double round(double wert, int stellen) {
        return Math.round(wert * Math.pow(10.0D, stellen)) / Math.pow(10.0D, stellen);
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static float random(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    public static float coerceAtMost(float value, float max) {
        return Math.min(value, max);
    }

    public static float coerceAtLeast(float value, float min) {
        return Math.max(value, min);
    }

    public static double easeInQuart(double amount) {
        return amount * amount * amount * amount;
    }

    public static double easeInOutQuad(double x) {
        return x < 0.5 ? (2 * x * x) : 1 - Math.pow(-2 * x + 2, 2) / 2;
    }
    
    public static double easeInOutQuart(double x) {
        return (x < 0.5) ? 8 * x * x * x * x : 1 - Math.pow(-2 * x + 2, 4) / 2;
    }

    public static double incremental(double val, double inc) {
        double one = 1.0 / inc;
        return Math.round(val * one) / one;
    }

    public static float gcd(float a, float b) {
        return a - (a % b);
    }

    // linear interpolation
    public static float lerp(float f, float st, float en) {
        return st + f * (en - st);
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }

}
