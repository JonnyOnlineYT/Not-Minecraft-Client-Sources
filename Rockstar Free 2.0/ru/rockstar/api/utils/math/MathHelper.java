package ru.rockstar.api.utils.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

public class MathHelper {
    private static final Random random = new Random();

    public static double getRandomInRange(double max, double min) {
        return min + (max - min) * random.nextDouble();
    }

    public static BigDecimal round(float f, int times) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(times, 4);
        return bd;
    }

    public static int getRandomInRange(int max, int min) {
        return (int)(min + (max - min) * random.nextDouble());
    }

    public static boolean isEven(int number) {
        return (number % 2 == 0);
    }

    public static double roundToPlace(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double preciseRound(double value, double precision) {
        double scale = Math.pow(10.0D, precision);
        return Math.round(value * scale) / scale;
    }

    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    public static int randomize(int max, int min) {
        return -min + (int)(Math.random() * (max - -min + 1));
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0D / inc;
        return Math.round(val * one) / one;
    }

    public static boolean isInteger(Double variable) {
        return (variable.doubleValue() == Math.floor(variable.doubleValue()) && !Double.isInfinite(variable.doubleValue()));
    }

    public static float[] constrainAngle(float[] vector) {
        vector[0] = vector[0] % 360.0F;
        vector[1] = vector[1] % 360.0F;
        while (vector[0] <= -180.0F)
            vector[0] = vector[0] + 360.0F;
        while (vector[1] <= -180.0F)
            vector[1] = vector[1] + 360.0F;
        while (vector[0] > 180.0F)
            vector[0] = vector[0] - 360.0F;
        while (vector[1] > 180.0F)
            vector[1] = vector[1] - 360.0F;
        return vector;
    }

    public static double randomize(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max)
            scaled = max;
        double shifted = scaled + min;
        if (shifted > max)
            shifted = max;
        return shifted;
    }

    public static double roundToDecimalPlace(double value, double inc) {
        double halfOfInc = inc / 2.0D;
        double floored = Math.floor(value / inc) * inc;
        if (value >= floored + halfOfInc)
            return (new BigDecimal(Math.ceil(value / inc) * inc, MathContext.DECIMAL64))
                    .stripTrailingZeros()
                    .doubleValue();
        return (new BigDecimal(floored, MathContext.DECIMAL64))
                .stripTrailingZeros()
                .doubleValue();
    }

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public static float clamp(float val, float min, float max) {
        if (val <= min)
            val = min;
        if (val >= max)
            val = max;
        return val;
    }
}
