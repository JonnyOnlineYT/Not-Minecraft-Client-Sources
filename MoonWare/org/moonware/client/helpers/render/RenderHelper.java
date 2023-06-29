package org.moonware.client.helpers.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.moonware.client.helpers.Helper;

import javax.vecmath.Matrix4f;
import java.awt.*;
import java.util.HashMap;

public class RenderHelper implements Helper {

    public static Frustum frustum = new Frustum();
    private static ShaderGroup blurShader;
    private static Framebuffer buffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;

    public static void scissorRect(float x, float y, float width, double height) {
        ScaledResolution sr = new ScaledResolution(Helper.mc);
        int factor = sr.getScaleFactor();
        GL11.glScissor((int) (x * (float) factor), (int) (((float) sr.getScaledHeight() - height) * (float) factor), (int) ((width - x) * (float) factor), (int) ((height - y) * (float) factor));
    }
    public static void inShaderFBO() {
        try {
            blurShader = new ShaderGroup(Minecraft.getTextureManager(), Minecraft.getResourceManager(), Minecraft.getFramebuffer(), null);
            blurShader.createBindFramebuffers(Minecraft.width, Minecraft.height);
            buffer = blurShader.mainFramebuffer;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void renderTriangle(int color) {
        GL11.glRotatef(270.0f, 0.0f, 0.0f, 1.0f);
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(4, DefaultVertexFormats.POSITION);
        buffer.pos(0.0, 4.0, 0.0).endVertex();
        buffer.pos(4.0, -4.0, 0.0).endVertex();
        buffer.pos(-4.0, -4.0, 0.0).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glRotatef(-270.0f, 0.0f, 0.0f, 1.0f);
    }
    private static void shaderConfigFix(float intensity, float blurWidth, float blurHeight) {
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
    }
    public static void blurAreaBoarder(float x, float f, float width, float height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution scale = new ScaledResolution(Helper.mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            inShaderFBO();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glScissor((int)(x * (float)factor), (int)((float) Minecraft.height - f * (float)factor - height * (float)factor) + 1, (int)(width * (float)factor), (int)(height * (float)factor));
        GL11.glEnable(3089);
        shaderConfigFix(intensity, blurWidth, blurHeight);
        buffer.bindFramebuffer(true);
        blurShader.loadShaderGroup(Minecraft.timer.renderPartialTicks);
        Minecraft.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
    }

    public static void blurAreaBoarder(int x, int y, int width, int height, float intensity) {
        ScaledResolution scale = new ScaledResolution(Helper.mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            inShaderFBO();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glScissor(x * factor, Minecraft.height - y * factor - height * factor, width * factor, height * factor);
        GL11.glEnable(3089);
        shaderConfigFix(intensity, 1.0f, 0.0f);
        buffer.bindFramebuffer(true);
        blurShader.loadShaderGroup(Minecraft.timer.renderPartialTicks);
        Minecraft.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
    }
    public static void renderBlur(int x, int y, int width, int height, int blurWidth, int blurHeight, int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        blurAreaBoarder(x, y, width, height, blurRadius, blurWidth, blurHeight);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }

    public static void renderBlur(int x, int y, int width, int height, int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        blurAreaBoarder(x, y, width, height, blurRadius);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }
    public static void renderBlurredShadow(Color color, double x, double y, double width, double height, int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        //RenderHelper.renderBlurredShadow(color, x, y, width, height, blurRadius);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }
    public static void drawCone(float radius, float height, int segments, boolean flag) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        if (!flag) {
            GL11.glEnable(2884);
        }
        float[][] verteces = new float[segments][3];
        float[] topVertex = {0.0f, 0.0f, 0.0f};
        for (i = 0; i < segments; ++i) {
            verteces[i][0] = (float)Math.cos(Math.PI * 2 / (double)segments * (double)i) * radius;
            verteces[i][1] = -height;
            verteces[i][2] = (float)Math.sin(Math.PI * 2 / (double)segments * (double)i) * radius;
        }
        GL11.glBegin(9);
        for (i = 0; i < segments; ++i) {
            GL11.glVertex3f(verteces[i][0], verteces[i][1], verteces[i][2]);
        }
        GL11.glEnd();
        for (i = 0; i < segments; ++i) {
            GL11.glBegin(4);
            GL11.glVertex3f(verteces[i][0], verteces[i][1], verteces[i][2]);
            GL11.glVertex3f(topVertex[0], topVertex[1], topVertex[2]);
            GL11.glVertex3f(verteces[(i + 1) % segments][0], verteces[(i + 1) % segments][1], verteces[(i + 1) % segments][2]);
            GL11.glEnd();
        }
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
    }

    public static int darker(int color, float factor) {
        int r = (int) ((float) (color >> 16 & 0xFF) * factor);
        int g = (int) ((float) (color >> 8 & 0xFF) * factor);
        int b = (int) ((float) (color & 0xFF) * factor);
        int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF | (a & 0xFF) << 24;
    }

    public static void setColor(int color) {
        GL11.glColor4ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF), (byte) (color >> 24 & 0xFF));
    }

    public static void setColor(Color color, float alpha) {
        float red = color.getRed() / 255F;
        float green = color.getGreen() / 255F;
        float blue = color.getBlue() / 255F;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void drawEntityBox(Entity entity, Color color, boolean fullBox, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GlStateManager.glLineWidth(2);
        GlStateManager.disableTexture2D();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.depthMask(false);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Minecraft.timer.renderPartialTicks - RenderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Minecraft.timer.renderPartialTicks - RenderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Minecraft.timer.renderPartialTicks - RenderManager.renderPosZ;
        AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(axisAlignedBB.minX - entity.posX + x - 0.05, axisAlignedBB.minY - entity.posY + y, axisAlignedBB.minZ - entity.posZ + z - 0.05, axisAlignedBB.maxX - entity.posX + x + 0.05, axisAlignedBB.maxY - entity.posY + y + 0.15, axisAlignedBB.maxZ - entity.posZ + z + 0.05);
        GlStateManager.glLineWidth(2.0F);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha);
        if (fullBox) {
            drawColorBox(axisAlignedBB2, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha);
            GlStateManager.color(0, 0, 0, 0.50F);
        }
        drawSelectionBoundingBox(axisAlignedBB2);
        GlStateManager.glLineWidth(2);
        GlStateManager.enableTexture2D();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(3, DefaultVertexFormats.POSITION);
        builder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        builder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        builder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        builder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        builder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        builder.begin(3, DefaultVertexFormats.POSITION);
        builder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        builder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        builder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        builder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        builder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        builder.begin(1, DefaultVertexFormats.POSITION);
        builder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        builder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        builder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        builder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        builder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        builder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        builder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        builder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawCircle3D(Entity entity, double radius, float partialTicks, int points, float width, int color) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(width);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - RenderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - RenderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - RenderManager.renderPosZ;
        setColor(color);
        for (int i = 0; i <= points; i++) {
            GL11.glVertex3d(x + radius * Math.cos(i * MathHelper.PI2 / points), y, z + radius * Math.sin(i * MathHelper.PI2 / points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
    public static void color(int hexColor) {
        float red = (float)(hexColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(hexColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hexColor & 0xFF) / 255.0f;
        float alpha = (float)(hexColor >> 24 & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void drawArrow(float x, float y, float scale, float width, boolean up, int hexColor) {
        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, scale);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        color(hexColor);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x /= scale, (y /= scale) + (float)(up ? 4 : 0));
        GL11.glVertex2d(x + 3.0f, y + (float)(up ? 0 : 4));
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex2d(x + 3.0f, y + (float)(up ? 0 : 4));
        GL11.glVertex2d(x + 6.0f, y + (float)(up ? 4 : 0));
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    public static void color(float red, float green, float blue, float alpha) {
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void color(float red, float green, float blue) {
        color(red, green, blue, 1.0f);
    }

    public static void color(Color color) {
        if (color == null) {
            color = Color.white;
        }
        color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }
    public static void drawArrow(float x, float y, boolean up, int hexColor) {
        GL11.glPushMatrix();
        GlStateManager.scale(0.8, 0.8, 1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        setColor(hexColor);
        GL11.glLineWidth(2);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x, y + (up ? 4 : 0));
        GL11.glVertex2d(x + 3, y + (up ? 0 : 4));
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x + 3, y + (up ? 0 : 4));
        GL11.glVertex2d(x + 6, y + (up ? 4 : 0));
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.scale(2, 2, 1);
        GL11.glPopMatrix();
    }

    public static void drawImage(Namespaced namespaced, float x, float y, float width, float height, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        setColor(color.getRGB());
        Minecraft.getTextureManager().bindTexture(namespaced);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
    public static void drawImageNotif(Namespaced namespaced, float x, float y, float width, float height) {
        Minecraft.getTextureManager().bindTexture(namespaced);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
    }

    public static void renderItem(ItemStack itemStack, int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.enableDepth();
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getRenderItem().renderItemOverlays(Minecraft.font, itemStack, x, y);
        Minecraft.getRenderItem().renderItemIntoGUI(itemStack, x, y);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableDepth();
    }

    public static void drawCircle(float x, float y, float start, float end, float radius, boolean filled, Color color) {
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
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (i = end; i >= start; i -= 4) {
            cos = (float) (Math.cos(i * Math.PI / 180) * radius * 1);
            sin = (float) (Math.sin(i * Math.PI / 180) * radius * 1);
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBegin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_STRIP);
        for (i = end; i >= start; i -= 4) {
            cos = (float) Math.cos(i * Math.PI / 180) * radius;
            sin = (float) Math.sin(i * Math.PI / 180) * radius;
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCircle(float x, float y, float radius, boolean filled, Color color) {
        drawCircle(x, y, 0, 360, radius, filled, color);
    }

    public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder buffer = ts.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
    }

    public static boolean isInViewFrustum(Entity entity) {
        return (isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck);
    }

    private static boolean isInViewFrustum(AxisAlignedBB bb) {
        Entity current = Minecraft.getRenderViewEntity();
        if (current != null) {
            frustum.setPosition(current.posX, current.posY, current.posZ);
        }
        return frustum.isBoundingBoxInFrustum(bb);
    }

    public static void blockEsp(BlockPos blockPos, Color color, boolean outline) {
        double x = blockPos.getX() - RenderManager.renderPosX;
        double y = blockPos.getY() - RenderManager.renderPosY;
        double z = blockPos.getZ() - RenderManager.renderPosZ;
        GL11.glPushMatrix();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.15F);
        drawColorBox(new AxisAlignedBB(x, y, z, x + 1, y + 1.0, z + 1), 0F, 0F, 0F, 0F);
        if (outline) {
            GlStateManager.color(0, 0, 0, 0.5F);
            drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1));
        }
        GL11.glLineWidth(2);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void blockEspFrame(BlockPos blockPos, float red, float green, float blue) {
        double x = blockPos.getX() - RenderManager.renderPosX;
        double y = blockPos.getY() - RenderManager.renderPosY;
        double z = blockPos.getZ() - RenderManager.renderPosZ;
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GlStateManager.color(red, green, blue, 1);
        drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawTriangle(float x, float y, float size, float vector, int color) {
        GlStateManager.translate(x, y, 0);
        GlStateManager.rotate(180 + vector, 0, 0, 1);

        setColor(color);
        GlStateManager.enable(GL11.GL_BLEND);
        GlStateManager.disable(GL11.GL_TEXTURE_2D);
        GlStateManager.enable(GL11.GL_LINE_SMOOTH);
        GlStateManager.hint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.glLineWidth(1);
        GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);

        GlStateManager.glVertex2f(0, (size));
        GlStateManager.glVertex2f((1 * size), -(size));
        GlStateManager.glVertex2f(-(1 * size), -(size));

        GlStateManager.glEnd();

        GlStateManager.glLineWidth(3);
        GlStateManager.glBegin(GL11.GL_LINE_LOOP);
        setColor(new Color(color).darker().getRGB());
        GlStateManager.glVertex2f(0, (size));
        GlStateManager.glVertex2f((1 * size), -(size));
        GlStateManager.glVertex2f(-(1 * size), -(size));

        GlStateManager.glEnd();

        GlStateManager.disable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enable(GL11.GL_TEXTURE_2D);
        GlStateManager.disable(GL11.GL_BLEND);
        GlStateManager.resetColor();
        GlStateManager.rotate(-180 - vector, 0, 0, 1);
        GlStateManager.translate(-x, -y, 0);
    }
}
