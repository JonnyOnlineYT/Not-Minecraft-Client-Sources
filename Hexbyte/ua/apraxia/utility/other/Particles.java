package ua.apraxia.utility.other;


import ua.apraxia.utility.render.RenderUtility;

import java.awt.*;

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
        RenderUtility.drawFCircle((float) x , (float) y, 0, 360, 0.5f, true, new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) opacity));
        RenderUtility.drawFCircle((float) x , (float) y, 0, 360, 1, true, new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) opacity));
    }
    public void updatePosition() {
        x += deltaX * 2;
        y += deltaY * 2;
        deltaY *= 0.95;
        deltaX *= 0.95;
        opacity -= 2f;
        if (opacity < 1) opacity = 1;
    }

    public void init(final double x, final double y, final double deltaX, final double deltaY, final float size, final Color color) {
        this.x = x;
        this.y = y;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.size = size;
        this.opacity = 254;
        this.color = color;
    }

}