package org.moonware.client.helpers.render;

import com.jhlabs.image.GaussianFilter;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render.rect.RectHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class GlowUtil implements Helper {

    static final HashMap<Integer, Integer> shadowCache = new HashMap<>();
    public static void drawBlurredShadow(float x, float y, float width, float height, final int blurRadius, final Color color, float glowScaling) {

        x -= glowScaling;
        y -= glowScaling;
        width += glowScaling * 2;
        height += glowScaling * 2;

        glPushMatrix();
        GlStateManager.alphaFunc(GL_GREATER, 0.001f);


        width = width + blurRadius * 2;
        height = height + blurRadius * 2;
        x = x - blurRadius;
        y = y - blurRadius;

        final float _X = x - 0.25f;
        final float _Y = y + 0.25f;

        final int identifier = (int) (width * height + width + color.hashCode() * blurRadius + blurRadius);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        glEnable(GL_ALPHA_TEST);
        GlStateManager.enableBlend();

        int texId = -1;
        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);

            GlStateManager.bindTexture(texId);
        } else {
            if (width <= 0) width = 1;
            if (height <= 0) height = 1;
            final BufferedImage original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);

            final Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRect(blurRadius, blurRadius, (int) (width - blurRadius * 2), (int) (height - blurRadius * 2));
            g.dispose();

            final GaussianFilter op = new GaussianFilter(blurRadius);

            final BufferedImage blurred = op.filter(original, null);


            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);

            shadowCache.put(identifier, texId);
        }

        glColor4f(1f, 1f, 1f, 1f);

        glTranslatef(_X, _Y, 0);

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); // top left
        glVertex2f(0, 0);

        glTexCoord2f(0, 1); // bottom left
        glVertex2f(0, 0 + height);

        glTexCoord2f(1, 1); // bottom right
        glVertex2f(0 + width, 0 + height);

        glTexCoord2f(1, 0); // top right
        glVertex2f(0 + width, 0);
        glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        glEnable(GL_CULL_FACE);
        glPopMatrix();
        GlStateManager.resetColor();
    }

    public static long delta;

    public static void drawBlurredGlow(double x, double y, double x1, double y1, int color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        drawHGradient((float) (int) x + 0.28, (float) (int) y, (float) (int) x1, (float) (int) (y + (y1 - y) / 2.0), DrawHelper.setAlpha(new Color(color), 0).getRGB(), DrawHelper.setAlpha(new Color(color), 70).getRGB());
        drawHGradient((float) (int) x + 0.28, (float) (int) (y + (y1 - y) / 2.0), (float) (int) x1, (float) (int) y1, DrawHelper.setAlpha(new Color(color), 70).getRGB(), DrawHelper.setAlpha(new Color(color), 0).getRGB());
        int radius = (int) ((y1 - y) / 2.0);
        drawPolygon(x, y + (y1 - y) / 2.0, radius, 0, RectHelper.setAlpha(new Color(color), 70).getRGB(), DrawHelper.setAlpha(new Color(color), 0).getRGB());
        drawPolygon(x, y + (y1 - y) / 2.0, radius, 1, DrawHelper.setAlpha(new Color(color), 70).getRGB(), DrawHelper.setAlpha(new Color(color), 0).getRGB());
        drawPolygon(x1, y + (y1 - y) / 2.0, radius, 2, DrawHelper.setAlpha(new Color(color), 70).getRGB(), DrawHelper.setAlpha(new Color(color), 0).getRGB());
        drawPolygon(x1, y + (y1 - y) / 2.0, radius, 3, DrawHelper.setAlpha(new Color(color), 70).getRGB(), DrawHelper.setAlpha(new Color(color), 0).getRGB());
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

        public static void drawPolygon ( double x, double y, int radius, int part,
        int color, int endcolor){
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

        public static void drawHGradient ( double d, double e, double e2, double g, int startColor, int endColor){
            float f = (float) (startColor >> 24 & 255) / 255.0F;
            float f1 = (float) (startColor >> 16 & 255) / 255.0F;
            float f2 = (float) (startColor >> 8 & 255) / 255.0F;
            float f3 = (float) (startColor & 255) / 255.0F;
            float f4 = (float) (endColor >> 24 & 255) / 255.0F;
            float f5 = (float) (endColor >> 16 & 255) / 255.0F;
            float f6 = (float) (endColor >> 8 & 255) / 255.0F;
            float f7 = (float) (endColor & 255) / 255.0F;
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.shadeModel(7425);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(e2, e, Helper.gui.zLevel).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(d, e, Helper.gui.zLevel).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(d, g, Helper.gui.zLevel).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(e2, g, Helper.gui.zLevel).color(f5, f6, f7, f4).endVertex();
            tessellator.draw();
            GlStateManager.shadeModel(7424);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
        }
}
