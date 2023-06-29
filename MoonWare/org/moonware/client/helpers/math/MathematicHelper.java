package org.moonware.client.helpers.math;

import net.minecraft.util.math.MathHelper;
import org.moonware.client.helpers.Helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

public class MathematicHelper implements Helper {

    public static BigDecimal round(float f, int times) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(times, RoundingMode.HALF_UP);
        return bd;
    }

    public static int middleRandomize(int max, int min) {
        return (int)(Math.random() * (double)(max - min)) + min;
    }

    public static int spikeRandomize(int max, int min) {
        return -min + (int)(Math.random() * (double)(max - -min + 1));
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
}
