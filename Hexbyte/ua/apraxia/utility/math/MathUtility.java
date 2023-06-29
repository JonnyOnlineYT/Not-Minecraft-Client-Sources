//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ua.apraxia.utility.math;

import net.minecraft.util.math.MathHelper;
import ua.apraxia.utility.Utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtility implements Utility {
    public static final float PI = 3.1415927F;
    public static final float PI2 = 6.2831855F;

    public MathUtility() {
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (double)(sigma * sigma));
        return (float)(output * Math.exp((double)(-(x * x)) / (2.0 * (double)(sigma * sigma))));
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

    public static int intRandom(int max, int min)
    {
        return (int)(Math.random() * (double)(max - min)) + min;
    }

    public static float rotate(float from, float to, float minstep, float maxstep) {

        float f = wrapDegrees(to - from) * clamp(0.6f, 0, 1);

        if (f < 0){
            f = clamp(f, -maxstep, -minstep);
        }
        else {
            f = clamp(f, minstep, maxstep);
        }
        if(Math.abs(f) > Math.abs(wrapDegrees(to - from)))
            return to;

        return from + f;
    }


    public static int getMiddle(int old, int newValue) {
        return (old + newValue) / 2;
    }


    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        } else {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }
    public static Double interpolate1(double oldValue, double newValue, double interpolationValue){
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue){
        return (float) interpolate(oldValue, newValue, (float) interpolationValue);
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue){
        return (int) interpolate(oldValue, newValue, (float) interpolationValue);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static short clamp(short value, short min, short max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }
    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }


    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }

    public static long clamp(long value, long min, long max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }

    public static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }

    public static float lerp(double start, double end, double step) {
        return (float)(start + (end - start) * step);
    }

    public static float wrapDegrees(float value) {
        value %= 360.0F;
        if (value >= 180.0F) {
            value -= 360.0F;
        }

        if (value < -180.0F) {
            value += 360.0F;
        }

        return value;
    }

    public static double wrapDegrees(double value) {
        value %= 360.0;
        if (value >= 180.0) {
            value -= 360.0;
        }

        if (value < -180.0) {
            value += 360.0;
        }

        return value;
    }
}
