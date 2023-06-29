package Celestial.utils.otherutils.gayutil;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class UIRender {
    public UIRender() {
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        double j;
        if (left < right) {
            j = left;
            left = right;
            right = j;
        }

        if (top < bottom) {
            j = top;
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
        GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
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
        GlStateManager.color(1.0F, 1.0F, 1.0F, f5);
        GlStateManager.bindTexture(id);
        a(f, f2, 0.0F, 0.0F, f3, f4, f3, f4);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
    }

    public static void a(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = 1.0F / f7;
        float f10 = 1.0F / f8;
        Tessellator bly2 = Tessellator.getInstance();
        BufferBuilder ali2 = bly2.getBuffer();
        ali2.begin(7, DefaultVertexFormats.POSITION_TEX);
        ali2.pos((double)f, (double)(f2 + f6), 0.0).tex((double)(f3 * f9), (double)((f4 + f6) * f10)).endVertex();
        ali2.pos((double)(f + f5), (double)(f2 + f6), 0.0).tex((double)((f3 + f5) * f9), (double)((f4 + f6) * f10)).endVertex();
        ali2.pos((double)(f + f5), (double)f2, 0.0).tex((double)((f3 + f5) * f9), (double)(f4 * f10)).endVertex();
        ali2.pos((double)f, (double)f2, 0.0).tex((double)(f3 * f9), (double)(f4 * f10)).endVertex();
        bly2.draw();
    }

    public static void bindTexture(int x, int y, int width, int height, int id) {
        GlStateManager.popMatrix();
        GlStateManager.bindTexture(id);
        Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0F, 0.0F, (float)width, (float)height, (float)width, (float)height);
        GlStateManager.disableBlend();
        GlStateManager.pushMatrix();
    }

    public static void setColor(int color) {
        GL11.glColor4ub((byte)(color >> 16 & 255), (byte)(color >> 8 & 255), (byte)(color & 255), (byte)(color >> 24 & 255));
    }

    public static void prepareScissorBox(int factor, float height, float x, float y, float x2, float y2) {
        GL11.glScissor((int)(x * (float)factor), (int)((height - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
    }

    public static void roundedBorder(float x, float y, float x2, float y2, float radius, int color) {
        enableGL2D();
        setColor(color);
        GlStateManager.glLineWidth(2.0F);
        GL11.glBegin(2);
        GL11.glVertex2d((double)x, (double)(y + radius));
        GL11.glVertex2f(x + radius, y);
        GL11.glVertex2f(x2 - radius, y);
        GL11.glVertex2f(x2, y + radius);
        GL11.glVertex2f(x2, y2 - radius);
        GL11.glVertex2f(x2 - radius, y2);
        GL11.glVertex2f(x + radius, y2);
        GL11.glVertex2f(x, y2 - radius);
        GL11.glEnd();
        disableGL2D();
    }

    public static void roundedBorder(float x, float y, float x2, float y2, float radius, float line, int color) {
        enableGL2D();
        setColor(color);
        GlStateManager.glLineWidth(line);
        GL11.glBegin(2);
        GL11.glVertex2d((double)x, (double)(y + radius));
        GL11.glVertex2f(x + radius, y);
        GL11.glVertex2f(x2 - radius, y);
        GL11.glVertex2f(x2, y + radius);
        GL11.glVertex2f(x2, y2 - radius);
        GL11.glVertex2f(x2 - radius, y2);
        GL11.glVertex2f(x + radius, y2);
        GL11.glVertex2f(x, y2 - radius);
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

    public static void drawCircle(float x, float y, float start, float end, float radius, boolean filled, Color color) {
        GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
        if (start > end) {
            float endOffset = end;
            end = start;
            start = endOffset;
        }

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        setColor(color.getRGB());
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0F);
        GL11.glBegin(3);

        float sin;
        float cos;
        float i;
        for(i = end; i >= start; i -= 4.0F) {
            cos = (float)(Math.cos((double)i * Math.PI / 180.0) * (double)radius * 1.0);
            sin = (float)(Math.sin((double)i * Math.PI / 180.0) * (double)radius * 1.0);
            GL11.glVertex2f(x + cos, y + sin);
        }

        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(2848);
        GL11.glBegin(filled ? 6 : 3);

        for(i = end; i >= start; i -= 4.0F) {
            cos = (float)Math.cos((double)i * Math.PI / 180.0) * radius;
            sin = (float)Math.sin((double)i * Math.PI / 180.0) * radius;
            GL11.glVertex2f(x + cos, y + sin);
        }

        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
