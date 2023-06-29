package de.strafe.utils;


import net.minecraft.client.renderer.GlStateManager;

public class ColorUtils {

    public static int getChromaColor(final int n, final int n2) {
        return java.awt.Color.HSBtoRGB((System.currentTimeMillis() - n * 10 - n2 * 10) % 2000L / 2000.0f, 0.8f, 0.8f);
    }

    public static void glColor(int hex) {
       float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
    }
}
