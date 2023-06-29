package org.moonware.client.helpers.math;

import net.minecraft.util.math.MathHelper;

import java.util.Random;
import java.util.UUID;

public class SHelper {
    public static float EaseOutBack2(float start, float end, float value) {
        float s = 1.70158f;
        end = MathHelper.wrapDegrees(end - start);
        value = (value) - 1;
        return end * ((value) * value * ((s + 1) * value + s) + 1) + start;
    }
    public static float Rotate(float from, float to, float minstep, float maxstep) {
        float f = MathHelper.wrapDegrees(to - from) * MathHelper.clamp(0.6f, 0, 1);

        if (f < 0){
            f = MathHelper.clamp(f, -maxstep, -minstep);
        }
        else {
            f = MathHelper.clamp(f, minstep, maxstep);
        }
        if(Math.abs(f) > Math.abs(MathHelper.wrapDegrees(to - from)))
            return to;

        return from + f;
    }

    public static double easeInOutQuad(double x, int step) {
        return (x < 0.5) ? 2 * x * x : 1 - Math.pow((-2 * x + 2), step) / 2;
    }
}
