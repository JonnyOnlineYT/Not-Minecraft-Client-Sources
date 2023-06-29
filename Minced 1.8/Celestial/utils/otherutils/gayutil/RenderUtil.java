package Celestial.utils.otherutils.gayutil;

import Celestial.utils.render.GaussianBlur;
import Celestial.utils.render.StencilUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;

public class RenderUtil {
    public static Minecraft mc = Minecraft.getMinecraft();
    private static final Frustum frustrum = new Frustum();
    private static HashMap<Integer, Integer> shadowCache2 = new HashMap();
    public static Framebuffer bloomFramebuffer = new Framebuffer(1, 1, false);
    private static final HashMap<Double, Integer> shadowCache = new HashMap();

    public static void drawFilledCircleNoGL(int x, int y, double r, int c, int quality) {
        float f = (float)(c >> 24 & 255) / 255.0F;
        float f1 = (float)(c >> 16 & 255) / 255.0F;
        float f2 = (float)(c >> 8 & 255) / 255.0F;
        float f3 = (float)(c & 255) / 255.0F;
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(6);

        for(int i = 0; i <= 360 / quality; ++i) {
            double x2 = Math.sin((double)(i * quality) * 3.141592653589793D / 180.0D) * r;
            double y2 = Math.cos((double)(i * quality) * 3.141592653589793D / 180.0D) * r;
            GL11.glVertex2d((double)x + x2, (double)y + y2);
        }

        GL11.glEnd();
    }

    public static void drawFace(float d, float y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            ResourceLocation skin = target.getLocationSkin();
            Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
            GL11.glEnable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawScaledCustomSizeModalRect(d, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(3042);
        } catch (Exception var12) {
            var12.printStackTrace();
        }

    }

    public static int setAlpha(Color c, int alpha) {
        return (new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha)).getRGB();
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static void blockEsp(BlockPos blockPos, Color c) {
        double x = (double)blockPos.getX() - Minecraft.getMinecraft().getRenderManager().renderPosX;
        double y = (double)blockPos.getY() - Minecraft.getMinecraft().getRenderManager().renderPosY;
        double z = (double)blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().renderPosZ;
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4d((double)((float)c.getRed() / 255.0F), (double)((float)c.getGreen() / 255.0F), (double)((float)c.getBlue() / 255.0F), 0.15D);
        drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), 0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glColor4d(0.0D, 0.0D, 0.0D, 0.5D);
        drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
        GL11.glLineWidth(2.0F);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void renderItem(EntityItem blockPos, Color c, float pt) {
        double x = blockPos.posX - 0.25D - Minecraft.getMinecraft().getRenderManager().renderPosX;
        double y = blockPos.posY - Minecraft.getMinecraft().getRenderManager().renderPosY;
        double z = blockPos.posZ - 0.25D - Minecraft.getMinecraft().getRenderManager().renderPosZ;
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4d((double)((float)c.getRed() / 255.0F), (double)((float)c.getGreen() / 255.0F), (double)((float)c.getBlue() / 255.0F), 0.15D);
        drawColorBox(new AxisAlignedBB(x, y, z, x + 0.5D, y + 0.5D, z + 0.5D), 0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glColor4d(0.0D, 0.0D, 0.0D, 0.5D);
        drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 0.5D, y + 0.5D, z + 0.5D));
        GL11.glLineWidth(2.0F);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
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


    public static void blur(Runnable data, float radius) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(3);
        GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }

    public static void horizontalGradient(double x1, double y1, double x2, double y2, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x1, y1, 0.0D).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x1, y2, 0.0D).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x2, y2, 0.0D).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(x2, y1, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawHG(double x1, double y1, double width, double height, int startColor, int endColor) {
        horizontalGradient(x1, y1, x1 + width, y1 + height, startColor, endColor);
    }

    private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = mc.getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer != null && framebuffer.framebufferWidth == mc.displayWidth && framebuffer.framebufferHeight == mc.displayHeight) {
            return framebuffer;
        } else {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }

            return new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        }
    }

    public static void drawTexture(ResourceLocation texture, double x, double y, double width, double height, Color color) {
        mc.getTextureManager().bindTexture(texture);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(4, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferBuilder.pos(x + width, y, 0.0D).tex(1.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(x, y, 0.0D).tex(0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(x, y + height, 0.0D).tex(0.0D, 1.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(x, y + height, 0.0D).tex(0.0D, 1.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(x + width, y + height, 0.0D).tex(1.0D, 1.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(x + width, y, 0.0D).tex(1.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        draw(true);
    }

    public static void draw(boolean texture) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GL11.glEnable(2848);
        GlStateManager.shadeModel(7425);
        if (texture) {
            GlStateManager.enableTexture2D();
        } else {
            GlStateManager.disableTexture2D();
        }

        Tessellator.getInstance().draw();
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GL11.glDisable(2848);
    }

    public static void scale(float x, float y, float scale, Runnable data) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0F);
        GL11.glScalef(scale, scale, 1.0F);
        GL11.glTranslatef(-x, -y, 0.0F);
        data.run();
        GL11.glPopMatrix();
    }

    public static void scaleStart(float x, float y, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0.0F);
        GlStateManager.scale(scale, scale, 1.0F);
        GlStateManager.translate(-x, -y, 0.0F);
    }

    public static void scaleEnd() {
        GlStateManager.popMatrix();
    }

    public static double animate(double endPoint, double current, double speed) {
        boolean shouldContinueAnimation = endPoint > current;
        if (speed < 0.0D) {
            speed = 0.0D;
        } else if (speed > 1.0D) {
            speed = 1.0D;
        }

        double dif = Math.max(endPoint, current) - Math.min(endPoint, current);
        double factor = dif * speed;
        return current + (shouldContinueAnimation ? factor : -factor);
    }

    public static void drawCircleNotSmooth(double x, double y, double radius, double sigment, int color) {
        GL11.glPushMatrix();
        radius /= 2.0D;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        GL11.glEnable(2848);
        color(color);
        GL11.glBegin(6);

        for(double i = 0.0D; i <= sigment; ++i) {
            double angle = i * 0.01745D;
            GL11.glVertex2d(x + radius * Math.cos(angle) + radius, y + radius * Math.sin(angle) + radius);
            GL11.glVertex2d(x + (radius - 1.0D) * Math.cos(angle) + radius, y + (radius - 1.0D) * Math.sin(angle) + radius);
        }

        GL11.glEnd();
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void start() {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }

    public static void start3d() {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(5890);
        GL11.glDisable(2884);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }

    public static void stop() {
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        color(Color.white);
    }

    public static void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    public void color(double red, double green, double blue) {
        color(red, green, blue, 1.0D);
    }

    public static void color(Color color) {
        if (color == null) {
            color = Color.white;
        }

        color((double)((float)color.getRed() / 255.0F), (double)((float)color.getGreen() / 255.0F), (double)((float)color.getBlue() / 255.0F), (double)((float)color.getAlpha() / 255.0F));
    }

    public static void drawColoredCircle(double x, double y, double radius, float brightness) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        GL11.glShadeModel(7425);
        GlStateManager.blendFunc(770, 771);
        GL11.glBegin(3);

        for(int i = 0; i < 360; ++i) {
            color(Color.HSBtoRGB((float)i / 360.0F, 0.0F, brightness));
            GL11.glVertex2d(x, y);
            color(Color.HSBtoRGB((float)i / 360.0F, 1.0F, brightness));
            GL11.glVertex2d(x + Math.sin(Math.toRadians((double)i)) * radius, y + Math.cos(Math.toRadians((double)i)) * radius);
        }

        GL11.glEnd();
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glShadeModel(7424);
        GL11.glPopMatrix();
    }

    public static void drawRectWH(float left, float top, float right, float bottom, int color) {
        drawRect(left, top, left + right, top + bottom, color);
    }

    public static void drawRect(float left, float top, float right, float bottom, int color) {
        float f3;
        if (left < right) {
            f3 = left;
            left = right;
            right = f3;
        }

        if (top < bottom) {
            f3 = top;
            top = bottom;
            bottom = f3;
        }

        f3 = (float)(color >> 24 & 255) / 255.0F;
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
        bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawVGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glEnd();
        GL11.glPopMatrix();
        GlStateManager.resetColor();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void vgd(float left, float top, float right, float bottom, int startColor, int endColor) {
        drawVGradientRect(left, top, left + right, top + bottom, startColor, endColor);
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


    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, (float)((double)limit * 0.01D));
    }

    public static void betterCircle(double x, double y, double size, int color) {
        int circle = 360;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glBegin(6);
        color(color);

        int i;
        double x2;
        double y2;
        for(i = 0; i <= circle; ++i) {
            x2 = Math.sin((double)i * 3.141592653589793D / 180.0D) * size;
            y2 = Math.cos((double)i * 3.141592653589793D / 180.0D) * size;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
        GL11.glEnable(2848);
        GL11.glBegin(3);
        color(color);

        for(i = 0; i <= circle; ++i) {
            x2 = Math.sin((double)i * 3.141592653589793D / 180.0D) * size;
            y2 = Math.cos((double)i * 3.141592653589793D / 180.0D) * size;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopMatrix();
        GlStateManager.resetColor();
    }

    public static void enableSmoothLine(float width) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(width);
    }

    public static void drawTriangle(float x, float y, float width, float height, int firstColor, int secondColor) {
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        enableSmoothLine(1.0F);
        GL11.glBegin(9);
        color(firstColor);
        GL11.glVertex2f(x, y - 2.0F);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x, y - 2.0F);
        GL11.glEnd();
        GL11.glBegin(9);
        color(secondColor);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width * 2.0F, y - 2.0F);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        GL11.glBegin(3);
        color(firstColor);
        GL11.glVertex2f(x, y - 2.0F);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x, y - 2.0F);
        GL11.glEnd();
        GL11.glBegin(3);
        color(secondColor);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width * 2.0F, y - 2.0F);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        disableSmoothLine();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glPopMatrix();
    }

    public static void disableSmoothLine() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void color(int color, float alpha) {
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        GlStateManager.color(r, g, b, alpha);
    }

    public static void color(int color) {
        color(color, (float)(color >> 24 & 255) / 255.0F);
    }

    public static void bindTexture(int texture) {
        GL11.glBindTexture(3553, texture);
    }

    public static void resetColor() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static boolean isHovered(float mouseX, float mouseY, float x, float y, float width, float height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, (int)width, (int)height, width, height);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableDepth();
    }

    public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height, Color c) {
        GlStateManager.color((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, (float)c.getAlpha() / 255.0F);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, (int)width, (int)height, width, height);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableDepth();
    }
}
