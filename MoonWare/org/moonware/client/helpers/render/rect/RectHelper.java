package org.moonware.client.helpers.render.rect;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.impl.combat.particle.zalupa.Utils.RenderUtils;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.AstolfoAnimation;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.helpers.Helper;

import java.awt.*;

import static net.minecraft.util.math.MathHelper.clampedLerp;
import static org.lwjgl.opengl.GL11.*;

public class RectHelper implements Helper {

    public static long delta;

    public static void glColor(int hex, float alpha) {
        float red = (hex >> 16 & 0xFF) / 255F;
        float green = (hex >> 8 & 0xFF) / 255F;
        float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }
    public static void glColor(Color color) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    public static void drawColorRect(double left, double top, double right, double bottom, Color color1, Color color2, Color color3, Color color4) {
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glShadeModel(7425);
        glPushMatrix();
        glBegin(7);
        glColor(color2);
        glVertex2d(left, bottom);
        glColor(color3);
        glVertex2d(right, bottom);
        glColor(color4);
        glVertex2d(right, top);
        glColor(color1);
        glVertex2d(left, top);
        glEnd();
        glPopMatrix();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        glShadeModel(7424);
        Gui.drawRect(0, 0, 0, 0, 0);
    }
    public static void drawMinecraftRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 300.0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, 300.0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 300.0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, bottom, 300.0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void disableSmoothLine() {
        glEnable(3553);
        glEnable(2929);
        glDisable(3042);
        glEnable(3008);
        glDepthMask(true);
        glCullFace(1029);
        glDisable(2848);
        glHint(3154, 4352);
        glHint(3155, 4352);
    }
    public static Color TwoColoreffect(Color cl1, Color cl2, double speed) {
        double thing = speed / 4.0 % 1.0;
        float val = MathHelper.clamp((float) Math.sin(Math.PI * 6 * thing) / 2.0f + 0.5f, 0.0f, 1.0f);
        return new Color((int) clampedLerp((float) cl1.getRed() / 255.0f, (float) cl2.getRed() / 255.0f, val),
                (int) clampedLerp((float) cl1.getGreen() / 255.0f, (float) cl2.getGreen() / 255.0f, val),
                (int) clampedLerp((float) cl1.getBlue() / 255.0f, (float) cl2.getBlue() / 255.0f, val));
    }
    public static void drawGlow(double x, double y, double x1, double y1, int color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        drawVGradientRect((float) (int) x, (float) (int) y, (float) (int) x1, (float) (int) (y + (y1 - y) / 2.0), setAlpha(new Color(color), 0).getRGB(), color);
        drawVGradientRect((float) (int) x, (float) (int) (y + (y1 - y) / 2.0), (float) (int) x1, (float) (int) y1, color, setAlpha(new Color(color), 0).getRGB());
        int radius = (int) ((y1 - y) / 2.0);
        drawPolygonPart(x, y + (y1 - y) / 2.0, radius, 0, color, setAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x, y + (y1 - y) / 2.0, radius, 1, color, setAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x1, y + (y1 - y) / 2.0, radius, 2, color, setAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x1, y + (y1 - y) / 2.0, radius, 3, color, setAlpha(new Color(color), 0).getRGB());
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawVGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (startColor >> 24 & 0xFF) / 255.0f;
        float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        float f4 = (startColor & 0xFF) / 255.0f;
        float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(left, top, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(left, bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    public static void drawPolygonPart(double x, double y, int radius, int part, int color, int endcolor) {
        float alpha = (color >> 24 & 0xFF) / 255.0f;
        float red = (color >> 16 & 0xFF) / 255.0f;
        float green = (color >> 8 & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;
        float alpha2 = (endcolor >> 24 & 0xFF) / 255.0f;
        float red2 = (endcolor >> 16 & 0xFF) / 255.0f;
        float green2 = (endcolor >> 8 & 0xFF) / 255.0f;
        float blue2 = (endcolor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
        final double TWICE_PI = 6.283185307179586;
        for (int i = part * 90; i <= part * 90 + 90; ++i) {
            double angle = 6.283185307179586 * i / 360.0 + Math.toRadians(180.0);
            bufferbuilder.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0.0).color(red2, green2, blue2, alpha2).endVertex();
        }
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    public static AstolfoAnimation astolfo = new AstolfoAnimation();
    public static float[] getRG(double input) {
        return new float[] { 255 - 255 * (float) input, 255 * (float) input, 100 * (float) input };
    }
    public static void drawWexCircle(double offset,String precentage) {
        glDisable(GL_TEXTURE_2D);
        boolean oldState = glIsEnabled(GL_BLEND);
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        GL11.glLineWidth(5.5f);
        glColor4f(0.1f, 0.1f, 0.1f, 0.5f);
        glBegin(GL_LINE_STRIP);
        for (int i = 0; i < 360; i++) {
            double x = Math.cos(Math.toRadians(i)) * 11;
            double z = Math.sin(Math.toRadians(i)) * 11;
            glVertex2d(x, z);
        }
        glEnd();
        glBegin(GL_LINE_STRIP);
        for (int i = -90; i < -90 + (360 * offset); i++) {
            Color colorShell = HUD.timerColor.getColorc();
            float red = colorShell.getRed();
            float green = colorShell.getGreen();
            float blue = colorShell.getBlue();
            if (2 == 1) {
                float[] buffer = getRG(offset);
                red = buffer[0];
                green = buffer[1];
                blue = buffer[2];
            } else if (1 == 1) {
                double stage = (i + 90) / 360.;
                int clr = astolfo.getColor(stage);
                red = ((clr >> 16) & 255);
                green = ((clr >> 8) & 255);
                blue = ((clr & 255));
            }
            glColor4f(red / 255f, green / 255f, blue / 255f, 1);
            double x = Math.cos(Math.toRadians(i)) * 11;
            double z = Math.sin(Math.toRadians(i)) * 11;
            glVertex2d(x, z);
        }
        glEnd();
        glDisable(GL_LINE_SMOOTH);
        if (!oldState)
            glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_FLAT);
        glColor4f(1, 1, 1, 1);
        int prec = (int) (offset * 100);
        if (prec > 100) {
            prec = 100;
        }
        if (prec < 0) {
            prec = 0;
        }
        MWFont.MONTSERRAT_BOLD.get(12).drawCenter((int) (offset * 100) + "%", 0.3f, -0.75f,
                RenderUtils.rgba(235, 235, 235, 255));
        //mc.motBold12.drawCenteredString(name, 0, -19f, RenderUtils.rgba(200, 200, 200, 255));
        astolfo.update();
    }
    public static void drawCircle(float x, float y, float start, float end, float radius,float width, boolean filled, Color color) {
        float sin;
        float cos;
        float i;
        GlStateManager.color(0, 0, 0, 0);

        float endOffset;
        if (start > end) {
            endOffset = end;
            end = start;
            start = endOffset;
        }

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        setColor(color.getRGB());
        glEnable(GL_LINE_SMOOTH);
        GL11.glLineWidth(width);
        glBegin(GL_LINE_STRIP);
        for (i = end; i >= start; i -= 4) {
            cos = (float) (Math.cos(i * Math.PI / 180) * radius * 1);
            sin = (float) (Math.sin(i * Math.PI / 180) * radius * 1);
            glVertex2f(x + cos, y + sin);
        }
        glEnd();
        glDisable(GL_LINE_SMOOTH);

        glEnable(GL_LINE_SMOOTH);
        glBegin(filled ? GL_TRIANGLE_FAN : GL_LINE_STRIP);
        for (i = end; i >= start; i -= 4) {
            cos = (float) Math.cos(i * Math.PI / 180) * radius;
            sin = (float) Math.sin(i * Math.PI / 180) * radius;
            glVertex2f(x + cos, y + sin);
        }
        glEnd();
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static Color setAlpha(Color color, int alpha) {
        alpha = MathHelper.clamp(alpha, 0, 255);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void enableSmoothLine(float width) {
        glDisable(3008);
        glEnable(3042);
        glBlendFunc(770, 771);
        glDisable(3553);
        glDisable(2929);
        glDepthMask(false);
        glEnable(2884);
        glEnable(2848);
        glHint(3154, 4354);
        glHint(3155, 4354);
        GL11.glLineWidth(width);
    }

    public static int setColor(int colorHex) {
        float alpha = (colorHex >> 24 & 0xFF) / 255.0F;
        float red = (colorHex >> 16 & 0xFF) / 255.0F;
        float green = (colorHex >> 8 & 0xFF) / 255.0F;
        float blue = (colorHex & 0xFF) / 255.0F;
        glColor4f(red, green, blue, (alpha == 0.0F) ? 1.0F : alpha);
        return colorHex;
    }

    public static void drawGradientRect2(double left, double top, double right, double bottom, int color, int color2) {
        float f = (float) (color >> 24 & 255) / 255.0F;
        float f1 = (float) (color >> 16 & 255) / 255.0F;
        float f2 = (float) (color >> 8 & 255) / 255.0F;
        float f3 = (float) (color & 255) / 255.0F;
        float f4 = (float) (color2 >> 24 & 255) / 255.0F;
        float f5 = (float) (color2 >> 16 & 255) / 255.0F;
        float f6 = (float) (color2 >> 8 & 255) / 255.0F;
        float f7 = (float) (color2 & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(left, top, Helper.gui.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, Helper.gui.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(right, bottom, Helper.gui.zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, top, Helper.gui.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawRoundedRect99(double x, double y, double x1, double y1, int insideC) {
        drawRect(x + 0.5, y, x1 - 0.5, y + 0.5, insideC);
        drawRect(x + 0.5, y1 - 0.5, x1 - 0.5, y1, insideC);
        drawRect(x, y + 0.5, x1, y1 - 0.5, insideC);
    }

    public static Color injectAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }


    public static void drawRectBetter(double x, double y, double width, double height, int color) {
        drawRect(x, y, x + width, y + height, color);
    }

    public static void drawGradientRectBetter(float x, float y, float width, float height, int color, int color2) {
        drawGradientRect(x, y, x + width, y + height, color, color2);
    }


    public static void drawSmoothRectBetter(float x, float y, float width, float height, int color) {
        drawSmoothRect1(x, y, x + width, y + height, color, 7, 4);
    }

    @Deprecated
    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right)
        {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, top, 0.0D).endVertex();
        bufferbuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawSmoothRect1(float left, float top, float right, float bottom, int color, int i, int i1) {
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        drawRect(left, top, right, bottom, color);
        glScalef(0.5f, 0.5f, 0.5f);
        drawRect(left * 2 - 1, top * 2, left * 2, bottom * 2 - 1, color);
        drawRect(left * 2, top * 2 - 1, right * 2, top * 2, color);
        drawRect(right * 2, top * 2, right * 2 + 1, bottom * 2 - 1, color);
        drawRect(left * 2, bottom * 2 - 1, right * 2, bottom * 2, color);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        glScalef(2F, 2F, 2F);
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, int color, int color2) {
        float f = (float) (color >> 24 & 255) / 255.0F;
        float f1 = (float) (color >> 16 & 255) / 255.0F;
        float f2 = (float) (color >> 8 & 255) / 255.0F;
        float f3 = (float) (color & 255) / 255.0F;
        float f4 = (float) (color2 >> 24 & 255) / 255.0F;
        float f5 = (float) (color2 >> 16 & 255) / 255.0F;
        float f6 = (float) (color2 >> 8 & 255) / 255.0F;
        float f7 = (float) (color2 & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(left, top, Helper.gui.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, Helper.gui.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(right, bottom, Helper.gui.zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, top, Helper.gui.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    public static void drawSkeetButton(float x, float y, float right, float bottom) {
        drawSmoothRect1(x - 31.0f, y - 43.0f, right + 31.0f, bottom - 30.0f, new Color(0, 0, 0, 255).getRGB(), 7, 4);
        drawSmoothRect1(x - 30.5f, y - 42.5f, right + 30.5f, bottom - 30.5f, new Color(45, 45, 45, 255).getRGB(), 7, 4);
        Helper.gui.drawGradientRect((int) x - 30, (int) y - 42, right + 30, bottom - 31, new Color(48, 48, 48, 255).getRGB(), new Color(19, 19, 19, 255).getRGB());
    }

    public static void drawBorderedRect(float left, float top, float right, float bottom, float borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        drawRect(left - (!borderIncludedInBounds ? borderWidth : 0), top - (!borderIncludedInBounds ? borderWidth : 0), right + (!borderIncludedInBounds ? borderWidth : 0), bottom + (!borderIncludedInBounds ? borderWidth : 0), borderColor);
        drawRect(left + (borderIncludedInBounds ? borderWidth : 0), top + (borderIncludedInBounds ? borderWidth : 0), right - ((borderIncludedInBounds ? borderWidth : 0)), bottom - ((borderIncludedInBounds ? borderWidth : 0)), insideColor);
    }
    public static void drawBorderedRect(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        drawRect(left - (!borderIncludedInBounds ? borderWidth : 0.0), top - (!borderIncludedInBounds ? borderWidth : 0.0), right + (!borderIncludedInBounds ? borderWidth : 0.0), bottom + (!borderIncludedInBounds ? borderWidth : 0.0), borderColor);
        drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0), top + (borderIncludedInBounds ? borderWidth : 0.0), right - (borderIncludedInBounds ? borderWidth : 0.0), bottom - (borderIncludedInBounds ? borderWidth : 0.0), insideColor);
    }
    public static void drawBorderedRect(double x, double y, double x2, double y2, double width, int color1, int color2) {
        drawRect(x, y, x2, y2, color2);
        drawBorderedRect(x, y, x2, y2, color1, width);
    }
    public static void drawHLine(double x, double y, double x1, double y1, float width, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        glPushMatrix();
        GL11.glLineWidth(width);
        glBegin(3);
        glVertex2d(x, y);
        glVertex2d(x1, y1);
        glEnd();
        GL11.glLineWidth(1.0f);
        glPopMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    public static void drawBorder(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        drawRect(left - (!borderIncludedInBounds ? borderWidth : 0.0), top - (!borderIncludedInBounds ? borderWidth : 0.0), right + (!borderIncludedInBounds ? borderWidth : 0.0), bottom + (!borderIncludedInBounds ? borderWidth : 0.0), borderColor);
        drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0), top + (borderIncludedInBounds ? borderWidth : 0.0), right - (borderIncludedInBounds ? borderWidth : 0.0), bottom - (borderIncludedInBounds ? borderWidth : 0.0), insideColor);
    }

    public static void drawRectWithEdge(double x, double y, double width, double height, Color color, Color colorTwo) {
        drawRect(x, y, x + width, y + height, color.getRGB());
        int colorRgb = colorTwo.getRGB();
        drawRect(x - 1.0, y, x, y + height, colorRgb);
        drawRect(x + width, y, x + width + 1.0, y + height, colorRgb);
        drawRect(x - 1.0, y - 1.0, x + width + 1.0, y, colorRgb);
        drawRect(x - 1.0, y + height, x + width + 1.0, y + height + 1.0, colorRgb);
    }

    public static void drawBorderedRect(double x, double y, double width, double height, int color, double lwidth) {
        drawHLine(x, y, width, y, (float)lwidth, color);
        drawHLine(width, y, width, height, (float)lwidth, color);
        drawHLine(x, height, width, height, (float)lwidth, color);
        drawHLine(x, height, x, y, (float)lwidth, color);
    }

    public static void drawBorder(float left, float top, float right, float bottom, float borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        drawRect(left - (!borderIncludedInBounds ? borderWidth : 0), top - (!borderIncludedInBounds ? borderWidth : 0), right + (!borderIncludedInBounds ? borderWidth : 0), bottom + (!borderIncludedInBounds ? borderWidth : 0), borderColor);
        drawRect(left + (borderIncludedInBounds ? borderWidth : 0), top + (borderIncludedInBounds ? borderWidth : 0), right - ((borderIncludedInBounds ? borderWidth : 0)), bottom - ((borderIncludedInBounds ? borderWidth : 0)), insideColor);
    }

    public static void drawOutlineRect(float x, float y, float width, float height, Color color, Color colorTwo) {
        drawRect(x, y, x + width, y + height, color.getRGB());
        int colorRgb = colorTwo.getRGB();
        drawRect(x - 1, y, x, y + height, colorRgb);
        drawRect(x + width, y, x + width + 1, y + height, colorRgb);
        drawRect(x - 1, y - 1, x + width + 1, y, colorRgb);
        drawRect(x - 1, y + height, x + width + 1, y + height + 1, colorRgb);
    }

    public static void drawSmoothRect(float left, float top, float right, float bottom, int color) {
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        drawRect(left, top, right, bottom, color);
        glScalef(0.5f, 0.5f, 0.5f);
        drawRect(left * 2 - 1, top * 2, left * 2, bottom * 2 - 1, color);
        drawRect(left * 2, top * 2 - 1, right * 2, top * 2, color);
        drawRect(right * 2, top * 2, right * 2 + 1, bottom * 2 - 1, color);
        drawRect(left * 2, bottom * 2 - 1, right * 2, bottom * 2, color);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        glScalef(2F, 2F, 2F);
    }
    public static void drawSmoothRect(float left, float top, double right, double bottom, int color) {
        glEnable(3042);
        glEnable(2848);
        drawRect(left, top, right, bottom, color);
        glScalef(0.5f, 0.5f, 0.5f);
        drawRect(left * 2.0f - 1.0f, top * 2.0f, left * 2.0f, bottom * 2.0 - 1.0, color);
        drawRect(left * 2.0f, top * 2.0f - 1.0f, right * 2.0, top * 2.0f, color);
        drawRect(right * 2.0, top * 2.0f, right * 2.0 + 1.0, bottom * 2.0 - 1.0, color);
        drawRect(left * 2.0f, bottom * 2.0 - 1.0, right * 2.0, bottom * 2.0, color);
        glDisable(2848);
        glDisable(3042);
        glScalef(2.0f, 2.0f, 2.0f);
    }
    public static void drawGradientRect1(double left, double top, double right, double bottom, int color, int color2) {
        float f = (float) (color >> 24 & 255) / 255.0F;
        float f1 = (float) (color >> 16 & 255) / 255.0F;
        float f2 = (float) (color >> 8 & 255) / 255.0F;
        float f3 = (float) (color & 255) / 255.0F;
        float f4 = (float) (color2 >> 24 & 255) / 255.0F;
        float f5 = (float) (color2 >> 16 & 255) / 255.0F;
        float f6 = (float) (color2 >> 8 & 255) / 255.0F;
        float f7 = (float) (color2 & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(left, top, Helper.gui.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, Helper.gui.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(right, bottom, Helper.gui.zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, top, Helper.gui.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    public static void drawRectWithGlow(double X, double Y, double Width, double Height, double GlowRange, double GlowMultiplier, Color color) {
        for (float i = 1; i < GlowRange; i += 0.5f) {
            drawRoundedRect99(X - (GlowRange - i), Y - (GlowRange - i), Width + (GlowRange - i), Height + (GlowRange - i), injectAlpha(color, (int) (Math.round(i * GlowMultiplier))).getRGB());
        }
    }

    public static void renderShadowVertical(Color c, float lineWidth, double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges) {
        glEnable(3042);
        glDisable(3553);
        glDisable(2884);
        renderShadowVertical(lineWidth, startAlpha, size, posX, posY1, posY2, right, edges, (float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f);
        glDisable(3042);
        glEnable(3553);
        glEnable(2884);
        glEnable(3008);
        glEnable(3553);
        glEnable(3042);
    }

    public static void renderShadowVertical(float lineWidth, double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges, float red, float green, float blue) {
        double alpha = startAlpha;
        GlStateManager.alphaFunc(516, 0.0f);
        GL11.glLineWidth(lineWidth);
        if (right) {
            for (double x = 0.5; x < (double)size; x += 0.5) {
                glColor4d(red, green, blue, alpha);
                glBegin(1);
                glVertex2d(posX + x, posY1 - (edges ? x : 0.0));
                glVertex2d(posX + x, posY2 + (edges ? x : 0.0));
                glEnd();
                alpha = startAlpha - x / (double)size;
            }
        } else {
            for (double x = 0.0; x < (double)size; x += 0.5) {
                glColor4d(red, green, blue, alpha);
                glBegin(1);
                glVertex2d(posX - x, posY1 - (edges ? x : 0.0));
                glVertex2d(posX - x, posY2 + (edges ? x : 0.0));
                glEnd();
                alpha = startAlpha - x / (double)size;
            }
        }
    }

    public static void renderShadowHorizontal(Color c, float lineWidth, double startAlpha, int size, double posY, double posX1, double posX2, boolean up, boolean edges) {
        glEnable(3042);
        glDisable(3553);
        glDisable(2884);
        renderShadowHorizontal(lineWidth, startAlpha, size, posY, posX1, posX2, up, edges, (float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f);
        glDisable(3042);
        glEnable(3553);
        glEnable(2884);
        glEnable(3008);
        glEnable(3553);
        glEnable(3042);
    }

    public static void renderShadowHorizontal(float lineWidth, double startAlpha, int size, double posY, double posX1, double posX2, boolean up, boolean edges, float red, float green, float blue) {
        double alpha = startAlpha;
        GlStateManager.alphaFunc(516, 0.0f);
        GL11.glLineWidth(lineWidth);
        if (!up) {
            for (double y = 0.0; y < (double)size; y += 0.5) {
                glColor4d(red, green, blue, alpha);
                glBegin(1);
                glVertex2d(posX1 - (edges ? y : 0.0), posY + y);
                glVertex2d(posX2 + (edges ? y : 0.0), posY + y);
                glEnd();
                alpha = startAlpha - y / (double)size;
            }
        } else {
            for (double y = 0.5; y < (double)size; y += 0.5) {
                glColor4d(red, green, blue, alpha);
                glBegin(1);
                glVertex2d(posX1 - (edges ? y : 0.0) - 0.5, posY - y);
                glVertex2d(posX2 + (edges ? y : 0.0) - 0.5, posY - y);
                glEnd();
                alpha = startAlpha - y / (double)size;
            }
        }
    }
    public static void drawBorderedRect1(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
        drawRect(x, y, x + width, y + height, color);
        drawRect(x, y, x + width, y + lineSize, borderColor);
        drawRect(x, y, x + lineSize, y + height, borderColor);
        drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
        drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
    }
}
