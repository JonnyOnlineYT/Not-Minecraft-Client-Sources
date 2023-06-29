package org.moonware.client.helpers.math;

import net.minecraft.client.Minecraft;
import org.moonware.client.helpers.Helper;

public class GCDFix implements Helper {

    public static float getFixedRotation(float rot) {
        return getDeltaMouse(rot) * getGCDValue();
    }

    public static float getGCDValue() {
        return (float) (getGCD() * 0.15);
    }

    public static float getGCD() {
        float f1;
        return (f1 = (float) (Minecraft.gameSettings.mouseSensitivity * 0.6 + 0.2)) * f1 * f1 * 8;
    }

    public static float getDeltaMouse(float delta) {
        return Math.round(delta / getGCDValue());
    }
}
