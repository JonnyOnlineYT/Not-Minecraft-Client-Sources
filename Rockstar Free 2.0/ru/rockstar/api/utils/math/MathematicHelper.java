package ru.rockstar.api.utils.math;

import net.minecraft.util.math.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.FloatBuffer;

public class MathematicHelper {

    public static BigDecimal round(float f, int times) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(times, RoundingMode.HALF_UP);
        return bd;
    }

    public static int getMiddle(int old, int newValue) {
        return (old + newValue) / 2;
    }

    public static double round(double num, double increment) {
        double v = (double) Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float checkAngle(float one, float two, float three) {
        float f = MathHelper.wrapDegrees(one - two);
        if (f < -three) {
            f = -three;
        }
        if (f >= three) {
            f = three;
        }
        return one - f;
    }
    
    public static float lerp(final float a, final float b, final float f) {
        return a + f * (b - a);
    }

    public static float clamp(float val, float min, float max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }

    public static float randomizeFloat(float min, float max) {
        return (float) (min + (max - min) * Math.random());
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }
}
