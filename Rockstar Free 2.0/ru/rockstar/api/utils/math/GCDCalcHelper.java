package ru.rockstar.api.utils.math;

import ru.rockstar.api.utils.Helper;

public class GCDCalcHelper implements Helper
{
    public static float getFixedRotation(final float rot) {
        return getDeltaMouse(rot) * getGCDValue();
    }
    
    public static float getGCDValue() {
        return (float)(getGCD() * 0.15);
    }
    
    public static float getGCD() {
        final float f1;
        return (f1 = (float)(GCDCalcHelper.mc.gameSettings.mouseSensitivity * 0.6 + 0.2)) * f1 * f1 * 8.0f;
    }
    
    public static float getDeltaMouse(final float delta) {
        return (float)Math.round(delta / getGCDValue());
    }
}
