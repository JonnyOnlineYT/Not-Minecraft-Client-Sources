/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package ru.fluger.client;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class UIRender {
    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void bind(float f, float f2, float f3, float f4, float f5, int id) {
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, f5);
        GlStateManager.bindTexture(id);
        UIRender.a(f, f2, 0.0f, 0.0f, f3, f4, f3, f4);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
    }

    public static void a(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = 1.0f / f7;
        float f10 = 1.0f / f8;
        Tessellator bly2 = Tessellator.getInstance();
        BufferBuilder ali2 = bly2.getBuffer();
        ali2.begin(7, DefaultVertexFormats.POSITION_TEX);
        ali2.pos(f, f2 + f6, 0.0).tex(f3 * f9, (f4 + f6) * f10).endVertex();
        ali2.pos(f + f5, f2 + f6, 0.0).tex((f3 + f5) * f9, (f4 + f6) * f10).endVertex();
        ali2.pos(f + f5, f2, 0.0).tex((f3 + f5) * f9, f4 * f10).endVertex();
        ali2.pos(f, f2, 0.0).tex(f3 * f9, f4 * f10).endVertex();
        bly2.draw();
    }

    public static void bindTexture(int x, int y, int width, int height, int id) {
        GlStateManager.popMatrix();
        GlStateManager.bindTexture(id);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GlStateManager.disableBlend();
        GlStateManager.pushMatrix();
    }

    public static void setColor(int color) {
        GL11.glColor4ub((byte)((byte)(color >> 16 & 0xFF)), (byte)((byte)(color >> 8 & 0xFF)), (byte)((byte)(color & 0xFF)), (byte)((byte)(color >> 24 & 0xFF)));
    }

    public static void prepareScissorBox(int factor, float height, float x, float y, float x2, float y2) {
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)((height - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }

    public static void roundedBorder(float x, float y, float x2, float y2, float radius, int color) {
        float left = x;
        float top = y;
        float bottom = y2;
        float right = x2;
        UIRender.enableGL2D();
        UIRender.setColor(color);
        GlStateManager.glLineWidth(2.0f);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)left, (double)(top + radius));
        GL11.glVertex2f((float)(left + radius), (float)top);
        GL11.glVertex2f((float)(right - radius), (float)top);
        GL11.glVertex2f((float)right, (float)(top + radius));
        GL11.glVertex2f((float)right, (float)(bottom - radius));
        GL11.glVertex2f((float)(right - radius), (float)bottom);
        GL11.glVertex2f((float)(left + radius), (float)bottom);
        GL11.glVertex2f((float)left, (float)(bottom - radius));
        GL11.glEnd();
        UIRender.disableGL2D();
    }

    public static void roundedBorder(float x, float y, float x2, float y2, float radius, float line, int color) {
        float left = x;
        float top = y;
        float bottom = y2;
        float right = x2;
        UIRender.enableGL2D();
        UIRender.setColor(color);
        GlStateManager.glLineWidth(line);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)left, (double)(top + radius));
        GL11.glVertex2f((float)(left + radius), (float)top);
        GL11.glVertex2f((float)(right - radius), (float)top);
        GL11.glVertex2f((float)right, (float)(top + radius));
        GL11.glVertex2f((float)right, (float)(bottom - radius));
        GL11.glVertex2f((float)(right - radius), (float)bottom);
        GL11.glVertex2f((float)(left + radius), (float)bottom);
        GL11.glVertex2f((float)left, (float)(bottom - radius));
        GL11.glEnd();
        UIRender.disableGL2D();
    }

    public static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static void drawCircle(float x, float y, float start, float end, float radius, boolean filled, Color color) {
        float sin;
        float cos;
        float i;
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
        if (start > end) {
            float endOffset = end;
            end = start;
            start = endOffset;
        }
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        UIRender.setColor(color.getRGB());
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)3);
        for (i = end; i >= start; i -= 4.0f) {
            cos = (float)(Math.cos((double)i * Math.PI / 180.0) * (double)radius * 1.0);
            sin = (float)(Math.sin((double)i * Math.PI / 180.0) * (double)radius * 1.0);
            GL11.glVertex2f((float)(x + cos), (float)(y + sin));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)(filled ? 6 : 3));
        for (i = end; i >= start; i -= 4.0f) {
            cos = (float)Math.cos((double)i * Math.PI / 180.0) * radius;
            sin = (float)Math.sin((double)i * Math.PI / 180.0) * radius;
            GL11.glVertex2f((float)(x + cos), (float)(y + sin));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}

