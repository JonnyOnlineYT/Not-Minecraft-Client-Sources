package org.moonware.client.helpers.Utils.blur;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.moonware.client.helpers.Utils.MathUtils;
import org.moonware.client.helpers.Utils.ShaderUtil;
import org.moonware.client.helpers.Utils.Utils;
import org.moonware.client.helpers.Utils.render.RenderUtil;
import org.moonware.client.helpers.render.rect.DrawHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

import static net.minecraft.client.renderer.OpenGlHelper.glUniform1;

public class BloomUtil implements Utils {

    public static ShaderUtil gaussianBloom = new ShaderUtil("moonware/shaders/bloom.frag");

    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);
    public static void renderBlur(int sourceTexture, int radius, int offset) {
        framebuffer = RenderUtil.createFrameBuffer(framebuffer);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weightBuffer.put(MathUtils.calculateGaussianValue(i, radius));
        }
        weightBuffer.rewind();

        RenderUtil.setAlphaLimit(0.0F);

        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        gaussianBloom.init();
        setupUniforms(radius, offset, 0, weightBuffer);
        RenderUtil.bindTexture(sourceTexture);
        ShaderUtil.drawQuads();
        gaussianBloom.unload();
        framebuffer.unbindFramebuffer();


        Minecraft.getFramebuffer().bindFramebuffer(true);

        gaussianBloom.init();
        setupUniforms(radius, 0, offset, weightBuffer);
        GL13.glActiveTexture(GL13.GL_TEXTURE16);
        RenderUtil.bindTexture(sourceTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        RenderUtil.bindTexture(framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        gaussianBloom.unload();

        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();

        GlStateManager.bindTexture(0);
    }
    public static void paintDropShadow(Graphics g, int x, int y, int width,
                                       int height, int offset) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int xOff = x + offset;
        int yOff = y + offset;
        g2.setColor(new Color(45, 45, 45, 80));
        g2.fillRoundRect(xOff, yOff, width, height, offset, offset);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
    }
    public static void blurSpot(int x, int y, int width, int height, int blurRadius, int iterations) {
        ScaledResolution sr = new ScaledResolution(Utils.mc);
        double scale = 1.0 / (double) sr.getScaleFactor();
        int imageDownscale = 2;
        int imageWidth = (width *= sr.getScaleFactor()) / imageDownscale;
        int imageHeight = (height *= sr.getScaleFactor()) / imageDownscale;
        int bpp = 3;
        int identifier = x * y * width * height * blurRadius + width + height + blurRadius + x + y;
        GL11.glEnable(3553);
        GL11.glDisable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3042);
        int texId = -1;
        DrawHelper.shadowCache = new HashMap<Integer, Integer>();
        DrawHelper.blurSpotCache = new HashMap<Integer, Integer>();
        if (DrawHelper.blurSpotCache.containsKey(identifier)) {
            texId = DrawHelper.blurSpotCache.get(identifier);
            GlStateManager.bindTexture(texId);
        } else {
            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp).order(ByteOrder.nativeOrder());
            GL11.glReadPixels(x, Minecraft.height - y - height, width, height, 6407, 5121, buffer);
            BufferedImage original = new BufferedImage(width, height, 1);
            for (int xIndex = 0; xIndex < width; ++xIndex) {
                for (int yIndex = 0; yIndex < height; ++yIndex) {
                    int i = (xIndex + width * yIndex) * bpp;
                    int r = buffer.get(i) & 0xFF;
                    int g = buffer.get(i + 1) & 0xFF;
                    int b = buffer.get(i + 2) & 0xFF;
                    original.setRGB(xIndex, height - (yIndex + 1), 0xFF000000 | r << 16 | g << 8 | b);
                }
            }
            BufferedImage image = new BufferedImage(imageWidth, imageHeight, original.getType());
            Graphics g = image.getGraphics();
            g.drawImage(original, 0, 0, imageWidth, imageHeight, null);
            g.dispose();
            DrawHelper.blurSpotCache.put(identifier, texId);
        }
        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, scale);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(x, y + height);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(x + width, y + height);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glDisable(3553);
    }
    public static void drawBloom(Color color, int radius, int offset, FloatBuffer weightBuffer) {
        gaussianBloom.init();
        setupUniforms(color, radius, offset, 0, weightBuffer);
        RenderUtil.bindTexture(framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        gaussianBloom.unload();
    }
    public static void setupUniforms(int radius, int directionX, int directionY, FloatBuffer weights) {
        gaussianBloom.setUniformi("inTexture", 0);
        gaussianBloom.setUniformi("textureToCheck", 16);
        gaussianBloom.setUniformf("radius", radius);
        gaussianBloom.setUniformf("texelSize", 1.0F / (float) Minecraft.width, 1.0F / (float) Minecraft.height);
        gaussianBloom.setUniformf("direction", directionX, directionY);
        glUniform1(gaussianBloom.getUniform("weights"), weights);
    }
    public static void setupUniforms(Color color, int radius, int directionX, int directionY, FloatBuffer weights) {
        gaussianBloom.setUniformi("inTexture", 0);
        gaussianBloom.setUniformi("textureToCheck", 16);
        gaussianBloom.setUniformf("radius", radius);
        gaussianBloom.setUniformf("texelSize", 1.0F / (float) Minecraft.getScaledRoundedWidth(), 1.0F / (float) Minecraft.getScaledRoundedWidth());
        gaussianBloom.setUniformf("direction", directionX, directionY);
        gaussianBloom.setUniformf("red", color.getRed() / 255F);
        gaussianBloom.setUniformf("green", color.getGreen() / 255F);
        gaussianBloom.setUniformf("blue", color.getBlue() / 255F);
        glUniform1(gaussianBloom.getUniform("weights"), weights);
    }
}
