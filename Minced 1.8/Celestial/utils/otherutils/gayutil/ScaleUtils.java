package Celestial.utils.otherutils.gayutil;

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
        GlStateManager.scale((double)(scale * (double)this.scale), (double)(scale * (double)this.scale), (double)(scale * (double)this.scale));
    }

    public int calc(int value) {
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        return value * rs.getScaleFactor() / this.scale;
    }

    public void popScale() {
        GlStateManager.scale((float)this.scale, (float)this.scale, (float)this.scale);
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
