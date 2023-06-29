/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class ScaleUtils {
    private int scale;

    public ScaleUtils(int scale) {
        this.scale = scale;
    }

    public void pushScale() {
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        double scale = (double)rs.getScaleFactor() / Math.pow(rs.getScaleFactor(), 2.0);
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale * (double)this.scale, scale * (double)this.scale, scale * (double)this.scale);
    }

    public int calc(int value) {
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        return value * rs.getScaleFactor() / this.scale;
    }

    public void popScale() {
        GlStateManager.scale(this.scale, this.scale, this.scale);
        GlStateManager.popMatrix();
    }

    public int[] getMouse(int mouseX, int mouseY, ScaledResolution rs) {
        return new int[]{mouseX * rs.getScaleFactor() / this.scale, mouseY * rs.getScaleFactor() / this.scale};
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}

