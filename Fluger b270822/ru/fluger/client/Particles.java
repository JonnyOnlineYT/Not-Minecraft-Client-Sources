/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client;

import java.awt.Color;
import net.minecraft.client.Minecraft;

public class Particles {
    public double x;
    public double y;
    public double deltaX;
    public double deltaY;
    public float size;
    public double opacity;
    public String text;
    public Color color;

    public void render2D() {
        Minecraft.getMinecraft().smallfontRenderer.drawBlurredStringWithShadow("*", this.x, this.y, 10, this.color, -1);
    }

    public void updatePosition() {
        this.x += this.deltaX * 2.0;
        this.y += this.deltaY * 2.0;
        this.deltaY *= 0.93;
        this.deltaX *= 0.93;
        this.opacity -= 2.0;
        if (this.opacity < 1.0) {
            this.opacity = 1.0;
        }
    }

    public void init(double x, double y, double deltaX, double deltaY, float size, Color color) {
        this.x = x;
        this.y = y;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.size = size;
        this.opacity = 254.0;
        this.color = color;
    }
}

