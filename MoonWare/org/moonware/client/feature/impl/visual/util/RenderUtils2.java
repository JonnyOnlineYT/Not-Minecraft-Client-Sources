package org.moonware.client.feature.impl.visual.util;

import com.jhlabs.image.GaussianFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.Utils.blur.BloomUtil;
import org.moonware.client.helpers.Utils.blur.GaussianBlur;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils2 implements Helper {
    protected static float zLevel;
    public static Frustum frustum = new Frustum();
    private static ShaderGroup blurShader;
    private static Framebuffer buffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static Namespaced shader;
    private static HashMap<Integer, Integer> shadowCache = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> blurSpotCache;

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }
    public static void bindFrameBufferP(double x, double y, double width, double height, Framebuffer framebuffer) {
        glBindTexture(GL_TEXTURE_2D, framebuffer.framebufferTexture);
        drawQuadP(x, y, width, height);
    }

    public static void drawQuadP(double x, double y, double width, double height) {
        glBegin(GL_QUADS);

        glTexCoord2d(0, 1);
        glVertex2d(x, y);

        glTexCoord2d(0, 0);
        glVertex2d(x, y + height);

        glTexCoord2d(1, 0);
        glVertex2d(x + width, y + height);

        glTexCoord2d(1, 1);
        glVertex2d(x + width, y);

        glEnd();
    }
    public static void inShaderFBO() {
        try {
            blurShader = new ShaderGroup(Minecraft.getTextureManager(), Minecraft.getResourceManager(), Minecraft.getFramebuffer(), shader);
            blurShader.createBindFramebuffers(Minecraft.width, Minecraft.height);
            buffer = blurShader.mainFramebuffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void renderShadowVertical(float lineWidth, double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges, float red, float green, float blue)
    {
        double alpha = startAlpha;
        GlStateManager.alphaFunc(516, 0.0f);
        glLineWidth(lineWidth);

        if (right)
        {
            for (double x = 0.5; x < (double)size; x += 0.5)
            {
                glColor4d(red, green, blue, alpha);
                glBegin(GL_LINES);
                glVertex2d(posX + x, posY1 - (edges ? x : 0.0));
                glVertex2d(posX + x, posY2 + (edges ? x : 0.0));
                glEnd();
                alpha = startAlpha - x / (double)size;
            }
        }
        else
        {
            for (double x = 0.0; x < (double)size; x += 0.5)
            {
                glColor4d(red, green, blue, alpha);
                glBegin(GL_LINES);
                glVertex2d(posX - x, posY1 - (edges ? x : 0.0));
                glVertex2d(posX - x, posY2 + (edges ? x : 0.0));
                glEnd();
                alpha = startAlpha - x / (double)size;
            }
        }
    }
    public static void drawColorRect(double left, double top, double right, double bottom, Color color1, Color color2, Color color3, Color color4)
    {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glShadeModel(GL_SMOOTH);
        glPushMatrix();
        glBegin(GL_QUADS);
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
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glShadeModel(GL_FLAT);
        Gui.drawRect(0, 0, 0, 0, 0);
    }

    public static void renderShadowVertical(Color c, float lineWidth, double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges)
    {
        GlStateManager.resetColor();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        renderShadowVertical(lineWidth, startAlpha, size, posX, posY1, posY2, right, edges, (float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f);
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glEnable(GL_ALPHA_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
    }
    public static void setColor(int color) {
        glColor4ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF), (byte) (color >> 24 & 0xFF));
    }
    public static void drawCone(float radius, float height, int segments, boolean flag) {
        int i;
        glPushMatrix();
        glDisable(3553);
        glDisable(2884);
        if (!flag) {
            glEnable(2884);
        }
        float[][] verteces = new float[segments][3];
        float[] topVertex = {0.0f, 0.0f, 0.0f};
        for (i = 0; i < segments; ++i) {
            verteces[i][0] = (float)Math.cos(Math.PI * 2 / (double)segments * (double)i) * radius;
            verteces[i][1] = -height;
            verteces[i][2] = (float)Math.sin(Math.PI * 2 / (double)segments * (double)i) * radius;
        }
        glBegin(9);
        for (i = 0; i < segments; ++i) {
            glVertex3f(verteces[i][0], verteces[i][1], verteces[i][2]);
        }
        glEnd();
        for (i = 0; i < segments; ++i) {
            glBegin(4);
            glVertex3f(verteces[i][0], verteces[i][1], verteces[i][2]);
            glVertex3f(topVertex[0], topVertex[1], topVertex[2]);
            glVertex3f(verteces[(i + 1) % segments][0], verteces[(i + 1) % segments][1], verteces[(i + 1) % segments][2]);
            glEnd();
        }
        glEnable(3553);
        glEnable(2884);
        glPopMatrix();
    }
    public static void drawBorderedRect1(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
        drawRect(x, y, x + width, y + height, color);
        drawRect(x, y, x + width, y + lineSize, borderColor);
        drawRect(x, y, x + lineSize, y + height, borderColor);
        drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
        drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
    }

    public static void renderBlur(int x, int y, int width, int height, int blurWidth, int blurHeight, int blurRadius) {
        glEnable(3042);
        glDisable(3553);
        glDisable(2884);
        blurAreaBoarder(x, y, width, height, blurRadius, blurWidth, blurHeight);
        glDisable(3042);
        glEnable(3553);
        glEnable(2884);
        glEnable(3008);
        glEnable(3553);
        glEnable(3042);
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
        glScissor((int) (x * (float) factor), (int) ((float) Minecraft.height - f * (float) factor - height * (float) factor) + 1, (int) (width * (float) factor), (int) (height * (float) factor));
        glEnable(3089);
        shaderConfigFix(intensity, blurWidth, blurHeight);
        buffer.bindFramebuffer(true);
        blurShader.loadShaderGroup(Minecraft.timer.renderPartialTicks);
        Minecraft.getFramebuffer().bindFramebuffer(true);
        glDisable(3089);
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
        glScissor(x * factor, Minecraft.height - y * factor - height * factor, width * factor, height * factor);
        glEnable(3089);
        shaderConfigFix(intensity, 1.0f, 0.0f);
        buffer.bindFramebuffer(true);
        blurShader.loadShaderGroup(Minecraft.timer.renderPartialTicks);
        Minecraft.getFramebuffer().bindFramebuffer(true);
        glDisable(3089);
    }

    private static void shaderConfigFix(float intensity, float blurWidth, float blurHeight) {
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
    }

    public static void renderBlur(int x, int y, int width, int height, int blurRadius) {
        glEnable(3042);
        glDisable(3553);
        glDisable(2884);
        blurAreaBoarder(x, y, width, height, blurRadius);
        glDisable(3042);
        glEnable(3553);
        glEnable(2884);
        glEnable(3008);
        glEnable(3553);
        glEnable(3042);
    }

    public static void bindTexture(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * .01));
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        glColor4f(red, green, blue, alpha);
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glPushMatrix();
        glLineWidth(1.0F);
        glBegin(9);
        for (int i = 0; i <= 360; i++)
            glVertex2d(x + Math.sin(i * Math.PI / 180.0D) * radius, y + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glPopMatrix();
        glEnable(3553);
        glDisable(2848);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void connectPoints(float xOne, float yOne, float xTwo, float yTwo) {
        glPushMatrix();
        glEnable(2848);
        glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(3042);
        glLineWidth(0.5F);
        glBegin(1);
        glVertex2f(xOne, yOne);
        glVertex2f(xTwo, yTwo);
        glEnd();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glDisable(2848);
        glEnable(3553);
        glPopMatrix();
    }

    public static void renderBreadCrumbs(List<Vec3d> vec3s) {

        GlStateManager.disableDepth();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        int i = 0;
        try {
            for (Vec3d v : vec3s) {

                i++;

                boolean draw = true;

                double x = v.xCoord - RenderManager.renderPosX;
                double y = v.yCoord - RenderManager.renderPosY;
                double z = v.zCoord - RenderManager.renderPosZ;

                double distanceFromPlayer = Minecraft.player.getDistance(v.xCoord, v.yCoord - 1, v.zCoord);
                int quality = (int) (distanceFromPlayer * 4 + 10);

                if (quality > 350)
                    quality = 350;

                if (i % 10 != 0 && distanceFromPlayer > 25) {
                    draw = false;
                }

                if (i % 3 == 0 && distanceFromPlayer > 15) {
                    draw = false;
                }

                if (draw) {

                    glPushMatrix();
                    glTranslated(x, y, z);

                    final float scale = 0.04f;
                    glScalef(-scale, -scale, -scale);

                    glRotated(-(Minecraft.getRenderManager()).playerViewY, 0.0D, 1.0D, 0.0D);
                    glRotated((Minecraft.getRenderManager()).playerViewX, 1.0D, 0.0D, 0.0D);

//                    Color c = Color.WHITE;
//                    Color firstcolor = new Color(BreadCrumbs.onecolor.getColorValue());
//                    switch (BreadCrumbs.colorMode.currentMode) {
//                        case "Client":
//                            c = ClientHelper.getClientColor();
//                            break;
//                        case "Astolfo":
//                            c = ColorUtils.astolfo(i - i + 1, i, BreadCrumbs.saturation.getNumberValue(), 10);
//                            break;
//                        case "Rainbow":
//                            c = ColorUtils.rainbow(i * 16, 0.5f, 1.0f);
//                            break;
//                        case "Pulse":
//                            c = ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F * (i / 16) / 60);
//                            break;
//
//                        case "Custom":
//                            c = ColorUtils.TwoColoreffect(new Color(BreadCrumbs.onecolor.getColorValue()), new Color(BreadCrumbs.twocolor.getColorValue()), Math.abs(System.currentTimeMillis() / 10) / 100.0 + 3.0F * (i / 16) / 60);
//                            break;
//                        case "Static":
//                            c = firstcolor;
//                            break;
//                    }
//                    drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);
//
//                    if (distanceFromPlayer < 4)
//                        drawFilledCircleNoGL(0, 0, 1.4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 50).hashCode(), quality);
//
//                    if (distanceFromPlayer < 20)
//                        drawFilledCircleNoGL(0, 0, 2.3, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), quality);
//
//                    glScalef(0.8f, 0.8f, 0.8f);

                    glPopMatrix();

                }

            }
        } catch (ConcurrentModificationException ignored) {
        }

        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        GlStateManager.enableDepth();

        glColor3d(255, 255, 255);
    }

    public static void drawFilledCircleNoGL(int x, int y, double r, int c, int quality) {
        float f = ((c >> 24) & 0xff) / 255F;
        float f1 = ((c >> 16) & 0xff) / 255F;
        float f2 = ((c >> 8) & 0xff) / 255F;
        float f3 = (c & 0xff) / 255F;

        glColor4f(f1, f2, f3, f);
        glBegin(GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / quality; i++) {
            double x2 = Math.sin(((i * quality * Math.PI) / 180)) * r;
            double y2 = Math.cos(((i * quality * Math.PI) / 180)) * r;
            glVertex2d(x + x2, y + y2);
        }

        glEnd();
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
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(2);
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

    public static void drawCircle(float x, float y, float radius, boolean filled, Color color) {
        drawCircle(x, y, 0, 360, radius, filled, color);
    }
    public static void blockEsp(BlockPos blockPos, Color c, boolean outline, double length, double length2) {
        double x = (double)blockPos.getX() - RenderManager.renderPosX;
        double y = (double)blockPos.getY() - RenderManager.renderPosY;
        double z = (double)blockPos.getZ() - RenderManager.renderPosZ;
        glPushMatrix();
        glBlendFunc(770, 771);
        glEnable(3042);
        glLineWidth(2.0f);
        glDisable(3553);
        glDisable(2929);
        glDepthMask(false);
        GlStateManager.color((float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f, 0.15f);
        drawColorBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length), 0.0f, 0.0f, 0.0f, 0.0f);
        if (outline) {
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.5f);
            drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length));
        }
        glLineWidth(2.0f);
        glEnable(3553);
        glEnable(2929);
        glDepthMask(true);
        glDisable(3042);
        glPopMatrix();
    }

    public static void blockEsp(BlockPos blockPos, Color color, boolean outline) {
        double x = blockPos.getX() - RenderManager.renderPosX;
        double y = blockPos.getY() - RenderManager.renderPosY;
        double z = blockPos.getZ() - RenderManager.renderPosZ;
        glPushMatrix();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glLineWidth(2);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.15F);
        drawColorBox(new AxisAlignedBB(x, y, z, x + 1, y + 1.0, z + 1), 0F, 0F, 0F, 0F);
        if (outline) {
            GlStateManager.color(0, 0, 0, 0.5F);
            drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1));
        }
        glLineWidth(2);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
        GlStateManager.resetColor();
        glPopMatrix();
    }

    public static void blockEspFrame(BlockPos blockPos, float red, float green, float blue) {
        double x = blockPos.getX() - RenderManager.renderPosX;
        double y = blockPos.getY() - RenderManager.renderPosY;
        double z = blockPos.getZ() - RenderManager.renderPosZ;
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glLineWidth(2);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        GlStateManager.color(red, green, blue, 1);
        drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1));
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public static void drawCircle3D(Entity entity, double radius, float partialTicks, int points, float width, int color) {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glDisable(GL_DEPTH_TEST);
        glLineWidth(width);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        glBegin(GL_LINE_STRIP);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - RenderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - RenderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - RenderManager.renderPosZ;
        setColor(color);
        for (int i = 0; i <= points; i++) {
            glVertex3d(x + radius * Math.cos(i * net.minecraft.util.math.MathHelper.PI2 / points), y, z + radius * Math.sin(i * net.minecraft.util.math.MathHelper.PI2 / points));
        }
        glEnd();
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    public static void drawTriangle(float x, float y, float size, float vector, int color) {
        glTranslated(x, y, 0.0D);
        glRotatef(180.0F + vector, 0.0F, 0.0F, 1.0F);
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
        glEnable(3042);
        glDisable(3553);
        glEnable(2848);
        glBlendFunc(770, 771);
        glLineWidth(1.0F);
        glBegin(6);
        glVertex2d(0.0D, size);
        glVertex2d((1.0F * size), -size);
        glVertex2d(-(1.0F * size), -size);
        glEnd();
        glDisable(2848);
        glEnable(3553);
        glDisable(3042);
        glRotatef(-180.0F - vector, 0.0F, 0.0F, 1.0F);
        glTranslated(-x, -y, 0.0D);
    }

    public static void drawTriangle(float x, float y, float width, float height, int firstColor, int secondColor) {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        enableSmoothLine(1F);

        // fill.
        glBegin(9);
        glColor(firstColor, 1.0F);
        glVertex2f(x, y - 2);
        glVertex2f(x + width, y + height);
        glVertex2f(x + width, y);
        glVertex2f(x, y - 2);
        glEnd();

        glBegin(9);
        glColor(secondColor, 1.0F);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x + width * 2, y - 2);
        glVertex2f(x + width, y);
        glEnd();

        // line.
        glBegin(3);
        glColor(firstColor, 1.0F);
        glVertex2f(x, y - 2);
        glVertex2f(x + width, y + height);
        glVertex2f(x + width, y);
        glVertex2f(x, y - 2);
        glEnd();

        glBegin(3);
        glColor(secondColor, 1.0F);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x + width * 2, y - 2);
        glVertex2f(x + width, y);
        glEnd();

        disableSmoothLine();
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    public static void glColor(Color color) {
        float red = color.getRed() / 255F;
        float green = color.getGreen() / 255F;
        float blue = color.getBlue() / 255F;
        float alpha = color.getAlpha() / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(Color color, int alpha) {
        glColor(color, alpha / 255F);
    }

    public static void glColor(Color color, float alpha) {
        float red = color.getRed() / 255F;
        float green = color.getGreen() / 255F;
        float blue = color.getBlue() / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static final void color(double red, double green, double blue, double alpha) {
        glColor4d(red, green, blue, alpha);
    }

    public static final void color(Color color) {
        if (color == null)
            color = Color.white;
        color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }

    public static void color(int color) {
        color(color, (float) (color >> 24 & 255) / 255.0F);
    }

    public static void color(int color, float alpha) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, alpha);
    }

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        glColor4f(red, green, blue, alpha);
    }

    public static void glColor(int hex, int alpha) {
        float red = (hex >> 16 & 0xFF) / 255F;
        float green = (hex >> 8 & 0xFF) / 255F;
        float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha / 255F);
    }

    public static void glColor(int hex, float alpha) {
        float red = (hex >> 16 & 0xFF) / 255F;
        float green = (hex >> 8 & 0xFF) / 255F;
        float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
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
        glLineWidth(width);
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

    public static void drawEntityBox(Entity entity, Color color, boolean fullBox, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        GlStateManager.glLineWidth(2);
        GlStateManager.disableTexture2D();
        glDisable(GL_DEPTH_TEST);
        GlStateManager.depthMask(false);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Minecraft.timer.renderPartialTicks - RenderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Minecraft.timer.renderPartialTicks - RenderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Minecraft.timer.renderPartialTicks - RenderManager.renderPosZ;
        AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(axisAlignedBB.minX - entity.posX + x - 0.05, axisAlignedBB.minY - entity.posY + y, axisAlignedBB.minZ - entity.posZ + z - 0.05, axisAlignedBB.maxX - entity.posX + x + 0.05, axisAlignedBB.maxY - entity.posY + y + 0.15, axisAlignedBB.maxZ - entity.posZ + z + 0.05);
        GlStateManager.glLineWidth(2.0F);
        glEnable(GL_LINE_SMOOTH);
        GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha);
        if (fullBox) {
            drawColorBox(axisAlignedBB2, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha);
            GlStateManager.color(0, 0, 0, 0.50F);
        }
        drawSelectionBoundingBox(axisAlignedBB2);
        GlStateManager.glLineWidth(2);
        GlStateManager.enableTexture2D();
        glEnable(GL_DEPTH_TEST);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder vb = ts.getBuffer();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
    }

    public static void renderItem(ItemStack itemStack, int x, int y) {
        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();

        Minecraft.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        Minecraft.getRenderItem().renderItemOverlays(Minecraft.font, itemStack, x, y);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableDepth();
    }

    public static void scissorRect(float x, float y, float width, double height) {
        ScaledResolution sr = new ScaledResolution(Helper.mc);
        int factor = sr.getScaleFactor();
        glScissor((int) (x * (float) factor), (int) (((float) sr.getScaledHeight() - height) * (float) factor), (int) ((width - x) * (float) factor), (int) ((height - y) * (float) factor));
    }

    public static void drawBorderedRect(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        drawRect(left - (!borderIncludedInBounds ? borderWidth : 0.0), top - (!borderIncludedInBounds ? borderWidth : 0.0), right + (!borderIncludedInBounds ? borderWidth : 0.0), bottom + (!borderIncludedInBounds ? borderWidth : 0.0), borderColor);
        drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0), top + (borderIncludedInBounds ? borderWidth : 0.0), right - (borderIncludedInBounds ? borderWidth : 0.0), bottom - (borderIncludedInBounds ? borderWidth : 0.0), insideColor);
    }
    public static void drawBorderedRect(double x, double y, double x2, double y2, double width, int color1, int color2) {
        drawRect(x, y, x2, y2, color2);
        drawBorderedRect(x, y, x2, y2, color1, width);
    }

    public static void drawBorderedRect(double x, double y, double width, double height, int color, double lwidth) {
        drawHLine(x, y, width, y, (float)lwidth, color);
        drawHLine(width, y, width, height, (float)lwidth, color);
        drawHLine(x, height, width, height, (float)lwidth, color);
        drawHLine(x, height, x, y, (float)lwidth, color);
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
        glLineWidth(width);
        glBegin(3);
        glVertex2d(x, y);
        glVertex2d(x1, y1);
        glEnd();
        glLineWidth(1.0f);
        glPopMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    public static boolean isInViewFrustum(Entity entity) {
        return (isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck);
    }

    public static void drawGradientRected(float f, float sY, double width, double height, int colour1, int colour2) {
        Helper.gui.drawGradientRect(f, sY, f + width, sY + height, colour1, colour2);
    }

    public static void drawRect2(double x, double y, double width, double height, int color) {
        drawRect(x, y, x + width, y + height, color);
    }

    public static void drawHead(EntityLivingBase player, double x, double y, double width, double height, Color color) {
        GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
        Minecraft.getTextureManager().bindTexture(Minecraft.getMinecraft().getConnection().getPlayerInfo(player.getUniqueID()).getLocationSkin());
        Gui.drawScaledCustomSizeModalRect((float) x, (float) y, 8, 8, 8, 8, (int) width, (int) (height), 64, 64);
    }

    private static Framebuffer bloomFramebuffer = new Framebuffer(1, 1, false);

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != Minecraft.width || framebuffer.framebufferHeight != Minecraft.height) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(Minecraft.width, Minecraft.height, true);
        }
        return framebuffer;
    }

    public static void stuffToBlur(boolean bloom) {

        // Gui.drawRect2(40, 40, 400, 40, -1);

    }

    public static void drawShadow(float radius, float offset, Runnable data) {
        bloomFramebuffer = createFrameBuffer(bloomFramebuffer);
        bloomFramebuffer.framebufferClear();
        bloomFramebuffer.bindFramebuffer(true);
        data.run();
        stuffToBlur(true);
        bloomFramebuffer.unbindFramebuffer();
        BloomUtil.renderBlur(bloomFramebuffer.framebufferTexture, (int) radius, (int) offset);

    }
    public static void setupToRender() {
        bloomFramebuffer = createFrameBuffer(bloomFramebuffer);
        bloomFramebuffer.framebufferClear();
        bloomFramebuffer.bindFramebuffer(true);

    }
    public static void unsetupToRender() {
        stuffToBlur(true);
        bloomFramebuffer.unbindFramebuffer();
        //BloomUtil.renderBlur(bloomFramebuffer.framebufferTexture, (int) radius, (int) offset);

    }
    public static void drawShadowWithColor(float radius, float offset,Color color, Runnable data) {
        glPushMatrix();
        GlStateManager.color(color.getRed(),color.getGreen(),color.getBlue());
        bloomFramebuffer = createFrameBuffer(bloomFramebuffer);
        bloomFramebuffer.framebufferClear();
        bloomFramebuffer.bindFramebuffer(true);
        data.run();
        stuffToBlur(true);
        bloomFramebuffer.unbindFramebuffer();
        BloomUtil.renderBlur(bloomFramebuffer.framebufferTexture, (int) radius, (int) offset);
        GlStateManager.popMatrix();
    }
    public static void drawBlur(float radius, Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }

    private static boolean isInViewFrustum(AxisAlignedBB bb) {
        Entity current = Minecraft.getRenderViewEntity();
        if (current != null) {
            frustum.setPosition(current.posX, current.posY, current.posZ);
        }
        return frustum.isBoundingBoxInFrustum(bb);
    }

    public static void drawFilledCircle(float xx, float yy, float radius, Color color) {
        int sections = 50;
        double dAngle = 6.283185307179586D / sections;
        glPushAttrib(8192);
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glBegin(6);
        for (int i = 0; i < sections; i++) {
            float x = (float) (radius * Math.sin(i * dAngle));
            float y = (float) (radius * Math.cos(i * dAngle));
            glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F,
                    color.getAlpha() / 255.0F);
            glVertex2f(xx + x, yy + y);
        }
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glEnd();
        glPopAttrib();
    }

    public static Color injectAlpha(Color color, int alpha) {
        alpha = MathHelper.clamp(alpha, 0, 255);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void drawImage(Namespaced namespaced, float x, float y, float width, float height, Color color) {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        setColor(color.getRGB());
        Minecraft.getTextureManager().bindTexture(namespaced);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public static void drawImage(Namespaced namespaced, float x, float y, float width, float height) {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        glColor4f(0.6f, 0.6f, 0.6f, 1);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        Minecraft.getTextureManager().bindTexture(namespaced);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        GlStateManager.resetColor();

    }

    public static void drawGradientRect(double d, double e, double e2, double g, int startColor, int endColor) {
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
        bufferbuilder.pos(e2, e, zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(d, e, zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(d, g, zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(e2, g, zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawSmoothRect(double left, double top, double right, double bottom, int color) {
        GlStateManager.resetColor();
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        drawRect(left, top, right, bottom, color);
        glScalef(0.5f, 0.5f, 0.5f);
        drawRect(left * 2.0f - 1.0f, top * 2.0f, left * 2.0f, bottom * 2.0f - 1.0f, color);
        drawRect(left * 2.0f, top * 2.0f - 1.0f, right * 2.0f, top * 2.0f, color);
        drawRect(right * 2.0f, top * 2.0f, right * 2.0f + 1.0f, bottom * 2.0f - 1.0f, color);
        glDisable(GL_BLEND);
        glScalef(2.0f, 2.0f, 2.0f);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        GlStateManager.resetColor();
        float f = (col1 >> 24 & 255) / 255.0f;
        float f1 = (col1 >> 16 & 255) / 255.0f;
        float f2 = (col1 >> 8 & 255) / 255.0f;
        float f3 = (col1 & 255) / 255.0f;
        float f4 = (col2 >> 24 & 255) / 255.0f;
        float f5 = (col2 >> 16 & 255) / 255.0f;
        float f6 = (col2 >> 8 & 255) / 255.0f;
        float f7 = (col2 & 255) / 255.0f;
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glShadeModel(7425);
        glPushMatrix();
        glBegin(7);
        glColor4f(f1, f2, f3, f);
        glVertex2d(left, top);
        glVertex2d(left, bottom);
        glColor4f(f5, f6, f7, f4);
        glVertex2d(right, bottom);
        glVertex2d(right, top);
        glEnd();
        glPopMatrix();
        glEnable(3553);
        glDisable(3042);
    }

    public static void drawOutlineRect(float x, float y, float width, float height, Color color, Color colorTwo) {
        drawRect(x, y, x + width, y + height, color.getRGB());
        int colorRgb = colorTwo.getRGB();
        drawRect(x - 1, y, x, y + height, colorRgb);
        drawRect(x + width, y, x + width + 1, y + height, colorRgb);
        drawRect(x - 1, y - 1, x + width + 1, y, colorRgb);
        drawRect(x - 1, y + height, x + width + 1, y + height + 1, colorRgb);
    }

    public static void drawBlurredShadow(float x, float y, float width, float height, int blurRadius, Color color) {
        glPushMatrix();
        GlStateManager.alphaFunc(GL_GREATER, 0.01f);

        width = width + blurRadius * 2;
        height = height + blurRadius * 2;
        x = x - blurRadius;
        y = y - blurRadius;

        float _X = x - 0.25f;
        float _Y = y + 0.25f;

        int identifier = (int) (width * height + width + color.hashCode() * blurRadius + blurRadius);

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
            BufferedImage original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);

            Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRect(blurRadius, blurRadius, (int) (width - blurRadius * 2), (int) (height - blurRadius * 2));
            g.dispose();

            GaussianFilter op = new GaussianFilter(blurRadius);

            BufferedImage blurred = op.filter(original, null);


            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);

            shadowCache.put(identifier, texId);
        }

        glColor4f(1f, 1f, 1f, 1f);

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); // top left
        glVertex2f(_X, _Y);

        glTexCoord2f(0, 1); // bottom left
        glVertex2f(_X, _Y + height);

        glTexCoord2f(1, 1); // bottom right
        glVertex2f(_X + width, _Y + height);

        glTexCoord2f(1, 0); // top right
        glVertex2f(_X + width, _Y);
        glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();

        glEnable(GL_CULL_FACE);
        glPopMatrix();
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        GlStateManager.resetColor();
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
        float f3 = (float) (color >> 24 & 0xFF) / 255.0f;
        float f = (float) (color >> 16 & 0xFF) / 255.0f;
        float f1 = (float) (color >> 8 & 0xFF) / 255.0f;
        float f2 = (float) (color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(left, bottom, 0.0).endVertex();
        vertexbuffer.pos(right, bottom, 0.0).endVertex();
        vertexbuffer.pos(right, top, 0.0).endVertex();
        vertexbuffer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    static {
        shader = new Namespaced("shaders/post/blur.json");
        blurSpotCache = new HashMap();
    }

    public static void roundedBorder(float x, float y, float x2, float y2, float radius, int color) {
        float left = x;
        float top = y;
        float bottom = y2;
        float right = x2;
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(true);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        setColor(color);
        GlStateManager.glLineWidth(2);
        glBegin(GL_LINE_LOOP);
        glVertex2d(left, top + radius);
        glVertex2f(left + radius, top);
        glVertex2f(right - radius, top);
        glVertex2f(right, top + radius);
        glVertex2f(right, bottom - radius);
        glVertex2f(right - radius, bottom);
        glVertex2f(left + radius, bottom);
        glVertex2f(left, bottom - radius);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
    }
}

