/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package ru.fluger.client.ui.drag.imp;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.feature.impl.hud.HUD;
import ru.fluger.client.helpers.palette.PaletteHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.ui.drag.Drag;

public class Timer
extends Drag {
    public float animWidth;
    public float animatedCircleEnd;

    public Timer() {
        this.setWidth(153.0f);
        this.setHeight(42.0f);
        this.name = "Timer";
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (ru.fluger.client.feature.impl.movement.Timer.smart.getCurrentValue()) {
            float end;
            this.animWidth = (float)RenderHelper.interpolate(100.0f - HUD.ticks * 4.0f, this.animWidth, 0.05f);
            int x = (int)this.x;
            int y = (int)this.y;
            RectHelper.drawSmoothRect(this.x, this.y, this.x + 40.0f, this.y + 43.0f, new Color(736117).getRGB());
            RenderHelper.renderBlurredShadow(new Color(0, 140, 255, 200).darker(), (double)(this.x - 5.0f), (double)(this.y - 2.0f), 40.0, 43.0, 43);
            this.mc.rubik_18.drawCenteredString("Timer", x + 20, y + 14 - 10, -1);
            this.drawCircle(x + 15 + 5, (double)y + 23.5, 11.5, -5.0f, 360.0f, Color.DARK_GRAY.darker().getRGB(), 5.5f);
            float coef = this.animWidth / 100.0f;
            this.animatedCircleEnd = end = coef * 360.0f;
            this.drawCircle(x + 15 + 5, (double)y + 23.5, 11.5, -5.0f, this.animatedCircleEnd, PaletteHelper.astolfoColors(1, 1), 5.5f);
            this.mc.rubik_15.drawCenteredString(Math.round(this.animWidth) + "%", x + 20, y + 22, -1);
        }
    }

    private void drawCircle(double x, double y, double radius, float startAngle, float endAngle, int color, float lineWidth) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        for (int i = (int)((double)startAngle / 360.0 * 100.0); i <= (int)((double)endAngle / 360.0 * 100.0); ++i) {
            double angle = Math.PI * 2 * (double)i / 100.0 + Math.toRadians(180.0);
            if (color == 1337) {
                RenderHelper.color(PaletteHelper.astolfoColors(i * 5, 1));
            } else {
                RenderHelper.color(color);
            }
            GL11.glVertex2d((double)(x + Math.sin(angle) * radius), (double)(y + Math.cos(angle) * radius));
        }
        GL11.glEnd();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GL11.glDisable((int)2848);
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    }
}

