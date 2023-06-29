package org.moonware.client.utils;

import lombok.experimental.UtilityClass;

/**
 * @deprecated Used {@link net.minecraft.util.math.MathHelper#lerp(float, float, float)}
 */
@Deprecated
@UtilityClass
public class Interpolator {
    public double linear(double startValue, double endValue, double fraction) {
        return startValue + (endValue - startValue) * fraction;
    }

    public int linear(int startValue, int endValue, double fraction) {
        return startValue + (int) Math.round((endValue - startValue) * fraction);
    }
}
