package org.moonware.client.feature.impl.hud.ArrayGlowComp;

import net.minecraft.client.Minecraft;
import org.moonware.client.helpers.palette.PaletteHelper;

public class AstolfoAnimation {
    private double value, prevValue;

    public void update() {
        prevValue = value;
        value -= 0.01;
    }

    public int getColor(double offset) {
        double hue = ((prevValue
                + (value - prevValue) * Minecraft.getMinecraft().getRenderPartialTicks()) + offset) % 1.;
        if (hue > 0.5F) {
            hue = 0.5F - (hue - 0.5F);
        }
        hue += 0.5F;
        return PaletteHelper.HSBtoRGB((float) hue, 0.5f, 1);
    }
}
