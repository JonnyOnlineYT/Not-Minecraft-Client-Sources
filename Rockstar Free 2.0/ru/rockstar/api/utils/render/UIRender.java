// 
// Decompiled by Procyon v0.5.36
// 

package ru.rockstar.api.utils.render;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

public class UIRender
{
    public static void drawRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f4, f5, f6, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void bind(final float f, final float f2, final float f3, final float f4, final float f5, final int id) {
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, f5);
        GlStateManager.bindTexture(id);
        a(f, f2, 0.0f, 0.0f, f3, f4, f3, f4);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
    }
    
    public static void a(final float f, final float f2, final float f3, final float f4, final float f5, final float f6, final float f7, final float f8) {
        final float f9 = 1.0f / f7;
        final float f10 = 1.0f / f8;
        final Tessellator bly2 = Tessellator.getInstance();
        final BufferBuilder ali2 = bly2.getBuffer();
        ali2.begin(7, DefaultVertexFormats.POSITION_TEX);
        ali2.pos((double)f, (double)(f2 + f6), 0.0).tex((double)(f3 * f9), (double)((f4 + f6) * f10)).endVertex();
        ali2.pos((double)(f + f5), (double)(f2 + f6), 0.0).tex((double)((f3 + f5) * f9), (double)((f4 + f6) * f10)).endVertex();
        ali2.pos((double)(f + f5), (double)f2, 0.0).tex((double)((f3 + f5) * f9), (double)(f4 * f10)).endVertex();
        ali2.pos((double)f, (double)f2, 0.0).tex((double)(f3 * f9), (double)(f4 * f10)).endVertex();
        bly2.draw();
    }
    
    public static void bindTexture(final int x, final int y, final int width, final int height, final int id) {
        GlStateManager.popMatrix();
        GlStateManager.bindTexture(id);
        Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 0.0f, (float)width, (float)height, (float)width, (float)height);
        GlStateManager.disableBlend();
        GlStateManager.pushMatrix();
    }
    
    public static void setColor(final int color) {
        GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(color >> 24 & 0xFF));
    }
    
    public static void prepareScissorBox(final int factor, final float height, final float x, final float y, final float x2, final float y2) {
        GL11.glScissor((int)(x * factor), (int)((height - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
    }
    
    public static void roundedBorder(final float x, final float y, final float x2, final float y2, final float radius, final int color) {
        final float left = x;
        final float top = y;
        final float bottom = y2;
        final float right = x2;
        enableGL2D();
        setColor(color);
        GlStateManager.glLineWidth(2.0f);
        GL11.glBegin(2);
        GL11.glVertex2d((double)left, (double)(top + radius));
        GL11.glVertex2f(left + radius, top);
        GL11.glVertex2f(right - radius, top);
        GL11.glVertex2f(right, top + radius);
        GL11.glVertex2f(right, bottom - radius);
        GL11.glVertex2f(right - radius, bottom);
        GL11.glVertex2f(left + radius, bottom);
        GL11.glVertex2f(left, bottom - radius);
        GL11.glEnd();
        disableGL2D();
    }
    
    public static void roundedBorder(final float x, final float y, final float x2, final float y2, final float radius, final float line, final int color) {
        final float left = x;
        final float top = y;
        final float bottom = y2;
        final float right = x2;
        enableGL2D();
        setColor(color);
        GlStateManager.glLineWidth(line);
        GL11.glBegin(2);
        GL11.glVertex2d((double)left, (double)(top + radius));
        GL11.glVertex2f(left + radius, top);
        GL11.glVertex2f(right - radius, top);
        GL11.glVertex2f(right, top + radius);
        GL11.glVertex2f(right, bottom - radius);
        GL11.glVertex2f(right - radius, bottom);
        GL11.glVertex2f(left + radius, bottom);
        GL11.glVertex2f(left, bottom - radius);
        GL11.glEnd();
        disableGL2D();
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void drawCircle(final float x, final float y, float start, float end, final float radius, final boolean filled, final Color color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
        if (start > end) {
            final float endOffset = end;
            end = start;
            start = endOffset;
        }
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        setColor(color.getRGB());
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(3);
        for (float i = end; i >= start; i -= 4.0f) {
            final float cos = (float)(Math.cos(i * 3.141592653589793 / 180.0) * radius * 1.0);
            final float sin = (float)(Math.sin(i * 3.141592653589793 / 180.0) * radius * 1.0);
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(2848);
        GL11.glBegin(filled ? 6 : 3);
        for (float i = end; i >= start; i -= 4.0f) {
            final float cos = (float)Math.cos(i * 3.141592653589793 / 180.0) * radius;
            final float sin = (float)Math.sin(i * 3.141592653589793 / 180.0) * radius;
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
