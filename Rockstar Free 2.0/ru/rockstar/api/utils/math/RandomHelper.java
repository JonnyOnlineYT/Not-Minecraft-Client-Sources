package ru.rockstar.api.utils.math;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomHelper {
    private static Random random;

    public static int randomNumber(int max, int min) {
        return Math.round(min + (float)Math.random() * (max - min));
    }

    public static float nextFloat(float startInclusive, float endInclusive) {
        if (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0F)
            return startInclusive;
        return (float)(startInclusive + (endInclusive - startInclusive) * Math.random());
    }

    public static double getRandomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max + 1.0D);
    }

    public static float randomFloat(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }
}
