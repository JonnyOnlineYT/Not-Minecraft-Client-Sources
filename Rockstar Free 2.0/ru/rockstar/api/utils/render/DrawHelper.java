package ru.rockstar.api.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ru.rockstar.api.utils.Helper;
import ru.rockstar.api.utils.combat.KillAuraHelper;
import ru.rockstar.api.utils.combat.RangeHelper;
import ru.rockstar.api.utils.shader.ShaderShell;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.features.impl.display.ArrayList;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.features.impl.visuals.TargetESP;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class DrawHelper implements Helper {

    private static Minecraft mc = Minecraft.getMinecraft();
    private static int time;
    private static float animtest;
    private static boolean anim;
    private static int test;
    private static float alpheble;
    public static long delta = 0L;
    protected static float zLevel;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static Framebuffer buffer;
    private static HashMap<Integer, Integer> shadowCache;
    private static ResourceLocation shader;

    private static final Frustum frustrum = new Frustum();
    public static int getColor2(Color color) {
        return DrawHelper.getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public static void bindTexture(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }
    
    public static void color(int color) {
        color(color, (float) (color >> 24 & 255) / 255.0F);
    }
    
    public static void drawCircledFace(String username, int x, int y, int width, int height) {
        try {
            bindFace(username);
            drawScaledCustomSizeModalCircle(x, y, 8f, 8f, 8, 8, width, height, 64f, 64f);
            drawScaledCustomSizeModalCircle(x, y, 40f, 8f, 8, 8, width, height, 64f, 64f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void bindFace(String username) throws IOException {
        AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(username), username)
                .loadTexture(Minecraft.getMinecraft().getResourceManager());
        Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(username));
    }
    
    public static void drawScaledCustomSizeModalCircle(int x, int y, float u, float v, int uWidth, int vHeight,
            int width, int height, float tileWidth, float tileHeight) {
    		float f = 1.0F / tileWidth;
    		float f1 = 1.0F / tileHeight;
    		Tessellator tessellator = Tessellator.getInstance();
    		BufferBuilder bufferbuilder = tessellator.getBuffer();
    		bufferbuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
    		float xRadius = width / 2f;
    		float yRadius = height / 2f;
    		float uRadius = (((u + (float) uWidth) * f) - (u * f)) / 2f;
    		float vRadius = (((v + (float) vHeight) * f1) - (v * f1)) / 2f;
    		for (int i = 0; i <= 360; i += 10) {
    			double xPosOffset = Math.sin(i * Math.PI / 180.0D);
    			double yPosOffset = Math.cos(i * Math.PI / 180.0D);
    			bufferbuilder.pos(x + xRadius + xPosOffset * xRadius, y + yRadius + yPosOffset * yRadius, 0)
    			.tex(u * f + uRadius + xPosOffset * uRadius, v * f1 + vRadius + yPosOffset * vRadius).endVertex();
    		}
    		tessellator.draw();
    }
    
    public static void drawGradientRoundedRect(double x, double y, double width, double height, float radius, Color color, Color color2) {

        float x2 = (float) (x + ((radius / 2F) + 0.5F));
        float y2 = (float) (y + ((radius / 2F) + 0.5F));
        float width2 = (float) (width - ((radius / 2F) + 0.5F));
        float height2 = (float) (height - ((radius / 2F) + 0.5F));

        drawRect(x2, y2, x2 + width2, y2 + height2, color.getRGB());

        polygon(x, y, radius * 2, 360, true, color);
        polygon(x + width2 - radius + 1.2, y, radius * 2, 360, true, color2);

        polygon(x + width2 - radius + 1.2, y + height2 - radius + 1, radius * 2, 360, true, color2);
        polygon(x, y + height2 - radius + 1, radius * 2, 360, true, color);

        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
        drawGradientSideways(x2 - radius / 2 - 0.5F, y2 + radius / 2, x2 + width2, y2 + height2 - radius / 2, color.getRGB(), color2.getRGB());
        drawGradientSideways(x2, y2 + radius / 2, x2 + width2 + radius / 2 + 0.5f, y2 + height2 - radius / 2, color.getRGB(), color.getRGB());
        
        drawGradientSideways(x2 + 10, y2 + radius / 2, x2 + width2 + radius / 2 + 0.5f, y2 + height2 - radius / 2, color2.getRGB(), color2.getRGB());
        
        drawGradientSideways(x2 + radius / 2, y2 - radius / 2 - 0.5F, x2 + width2 - radius / 2, y + height2 - radius / 2, color.getRGB(), color2.getRGB());
        drawGradientSideways(x2 + radius / 2, y2, x2 + width2 - radius / 2, y2 + height2 + radius / 2 + 0.5f, color.getRGB(), color2.getRGB());
    }
    
    public static void drawGradientOutlinedRoundedRect(double x, double y, double width, double height, float radius, float expand, Color color, Color color2) {
    	drawGradientRoundedRect(x,y,width,height,radius,color.brighter(),color2.brighter());
    	
    	float e = expand;
    	
    	drawGradientRoundedRect(x + e,y + e,width - e - e /1.5f ,height - e - e /1.5f,radius - 1, color,color2);
    }
    
    public static void drawGradientOutlinedDepthShadowRoundedRectWithGlow(float x, float y, float width, float height, float radius, float expand, Color color, Color color2, Color shadowColor) {
    	drawGradientRoundedRect(x,y,width,height,radius,color.brighter(),color2.brighter());
    	
    	float e = expand;
    	
    	float e2 = expand + 0.2f;
    	
    	drawGradientRoundedRect(x + e2,y + e2,width - e2 - e2 /1.5f,height - e2 - e2 /1.5f,radius - 1, shadowColor,shadowColor);
    	
    	drawGradientRoundedRect(x + e,y + e,width - e - e /1.5f,height - e - e /1.5f,radius - 1, color,color2);
    }
    
    public static void drawGradientOutlinedRoundedRectWithGlow(float x, float y, float width, float height, float radius, float expand, int glowIntencivity, Color color, Color color2) {
    	RenderUtils.drawBlurredShadow(x + 1,y,width + 3,height + 3.5f, glowIntencivity,color2.brighter());
    	
    	drawGradientRoundedRect(x,y,width,height,radius,color.brighter(),color2.brighter());
    	
    	float e = expand;
    	
    	drawGradientRoundedRect(x + e,y + e,width - e - e /1.5f,height - e - e /1.5f,radius - 1, color,color2);
    }
    
    public static void drawGradientOutlinedDepthShadowRoundedRectWithGlow(float x, float y, float width, float height, float radius, float expand, int glowIntencivity, Color color, Color color2, Color shadowColor) {
    	RenderUtils.drawBlurredShadow(x + 1,y,width + 3,height + 3.5f, glowIntencivity,color2.brighter());
    	
    	drawGradientRoundedRect(x,y,width,height,radius,color.brighter(),color2.brighter());
    	
    	float e = expand;
    	
    	float e2 = expand + 0.5f;
    	
    	drawGradientRoundedRect(x + e,y + e,width - e - e /1.5f,height - e - e /1.5f,radius - 1, shadowColor,shadowColor);
    	
    	drawGradientRoundedRect(x + e2,y + e2,width - e2 - e2 /1.5f,height - e2 - e2 /1.5f,radius - 1, color,color2);
    }

    public static void color(int color, float alpha) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, alpha);
    }


    public static void drawTriangle(float x, float y, float size, float vector, int color) {
        GL11.glTranslated(x, y, 0.0D);
        GL11.glRotatef(180.0F + vector, 0.0F, 0.0F, 1.0F);
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0F);
        GL11.glBegin(6);
        GL11.glVertex2d(0.0D, size);
        GL11.glVertex2d((1.0F * size), -size);
        GL11.glVertex2d(-(1.0F * size), -size);
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glRotatef(-180.0F - vector, 0.0F, 0.0F, 1.0F);
        GL11.glTranslated(-x, -y, 0.0D);
    }

    public static void drawTriangle(float x, float y, float width, float height, int firstColor, int secondColor) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        enableSmoothLine(1F);

        // fill.
        GL11.glBegin(9);
        glColor(firstColor, 1.0F);
        GL11.glVertex2f(x, y - 2);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x, y - 2);
        GL11.glEnd();

        GL11.glBegin(9);
        glColor(secondColor, 1.0F);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width * 2, y - 2);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();

        // line.
        GL11.glBegin(3);
        glColor(firstColor, 1.0F);
        GL11.glVertex2f(x, y - 2);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x, y - 2);
        GL11.glEnd();

        GL11.glBegin(3);
        glColor(secondColor, 1.0F);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width * 2, y - 2);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();

        disableSmoothLine();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
    public static void drawVerticalGradientSmoothRect(float left, float top, float right, float bottom, int color, int color2) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        gui.drawGradientRect((int) left, (int)top,(int) right,(int) bottom, color, color2);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        gui.drawGradientRect((int)left * 2 - 1, (int)top * 2,(int) left * 2,(int) bottom * 2 - 1, color, color2);
        gui.drawGradientRect((int)left * 2, (int)top * 2 - 1, (int)right * 2, (int)top * 2, color, color2);
        gui.drawGradientRect((int)right * 2,(int) top * 2,(int) right * 2 + 1, (int)bottom * 2 - 1, color, color2);
        gui.drawGradientRect((int)left * 2, (int)bottom * 2 - 1, (int)right * 2, (int)bottom * 2, color, color2);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glScalef(2F, 2F, 2F);
    }
    
    public static void drawCompleteImage(double posX, double posY, double width, double height) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) posX, (float) posY, 0.0f);
        GL11.glColor4f(1,1,1,1);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, (float) height, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f((float) width, (float) height, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f((float) width, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
    
    public static void scissorRect(final float x, final float y, final float width, final double height) {
        final ScaledResolution sr = new ScaledResolution(mc);
        final int factor = sr.getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)(((float)sr.getScaledHeight() - height) * (float)factor), (int)((width - x) * factor), (int)((height - y) * (float)factor));
    }
    
    public static void renderBlurredShadow(double x, double y, double width, double height, final int blurRadius, final Color color) {
        GlStateManager.alphaFunc(516, 0.01f);
        width += blurRadius * 2;
        height += blurRadius * 2;
        x -= blurRadius;
        y -= blurRadius;
        final float _X = (float)(x - 0.25);
        final float _Y = (float)(y + 0.25);
        final int identifier = (int)(width * height + width + color.hashCode() * blurRadius + blurRadius);
        GL11.glEnable(3553);
        GL11.glDisable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3042);
        int texId = -1;
        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);
            GlStateManager.bindTexture(texId);
        }
        else {
            width = MathHelper.clamp(width, 0.01, width);
            height = MathHelper.clamp(height, 0.01, height);
            final BufferedImage original = new BufferedImage((int)width, (int)height, 2);
            final Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRect(blurRadius, blurRadius, (int)width - blurRadius * 2, (int)height - blurRadius * 2);
            g.dispose();
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(_X, _Y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(_X, _Y + (int)height);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(_X + (int)width, _Y + (int)height);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(_X + (int)width, _Y);
        GL11.glEnd();
        GL11.glDisable(3553);
    }
    
    public static void drawCircle(final float x, final float y, final float radius, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glLineWidth(1.0f);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    public static void drawCircle3D(final Entity entity, final double radius, final float partialTicks, final int points, final float width, final int color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().renderPosZ;
        setColor(color);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos(i * 6.2831855f / points), y, z + radius * Math.sin(i * 6.2831855f / points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    
    public static void drawCircle3D(final TileEntity entity, final double radius, final float partialTicks, final int points, final float width, final int color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        final double x = entity.getPos().getX() - mc.getRenderManager().renderPosX;
        final double y = entity.getPos().getY() - mc.getRenderManager().renderPosY;
        final double z = entity.getPos().getZ() - mc.getRenderManager().renderPosZ;
        setColor(color);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos(i * 6.2831855f / points), y, z + radius * Math.sin(i * 6.2831855f / points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    public static void drawEntityBox(Entity entity, Color color, boolean fullBox, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GlStateManager.glLineWidth(2);
        GlStateManager.disableTexture2D();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.depthMask(false);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;
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
    public static Color injectAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    public static void drawOutlineRect(float x, float y, float width, float height, Color color, Color colorTwo) {
        drawRect(x, y, x + width, y + height, color.getRGB());
        int colorRgb = colorTwo.getRGB();
        drawRect(x - 1, y, x, y + height, colorRgb);
        drawRect(x + width, y, x + width + 1, y + height, colorRgb);
        drawRect(x - 1, y - 1, x + width + 1, y, colorRgb);
        drawRect(x - 1, y + height, x + width + 1, y + height + 1, colorRgb);
    }
    public static void drawGlowRoundedRect(float startX, float startY, float endX, float endY, int color, float radius, float force) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        float alpha = ((float) (color >> 24 & 0xFF) / 255F);
        float red = (float) (color >> 16 & 0xFF) / 255F;
        float green = (float) (color >> 8 & 0xFF) / 255F;
        float blue = (float) (color & 0xFF) / 255F;
        ShaderShell.ROUNDED_RECT.attach();
        ShaderShell.ROUNDED_RECT.set4F("color", red, green, blue, alpha);
        ShaderShell.ROUNDED_RECT.set2F("resolution", Minecraft.getMinecraft().displayWidth,
                Minecraft.getMinecraft().displayHeight);
        ShaderShell.ROUNDED_RECT.set2F("center", (startX + (endX - startX) / 2) * 2,
                (startY + (endY - startY) / 2) * 2);
        ShaderShell.ROUNDED_RECT.set2F("dst", (endX - startX - radius) * 2, (endY - startY - radius) * 2);
        ShaderShell.ROUNDED_RECT.set1F("radius", radius);
        ShaderShell.ROUNDED_RECT.set1F("force", force);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(endX, startY);
        GL11.glVertex2d(startX, startY);
        GL11.glVertex2d(startX, endY);
        GL11.glVertex2d(endX, endY);
        GL11.glEnd();
        ShaderShell.ROUNDED_RECT.detach();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
    public static void drawGlColorRect(final float x, final float y, final float x1, final float y1, final int color) {
        enableGL2D();
        glColor(color);
        drawRect(x, y, x1, y1, color);
        disableGL2D();
    }
    public static void drawHLine(float x, float y, final float x1, final int y1) {
        if (y < x) {
            final float var5 = x;
            x = y;
            y = var5;
        }
        drawGlColorRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }
    public static void drawVLine(float x, float y, float x1, int y1)
    {
      if (x1 < y)
      {
        float var5 = y;
        y = x1;
        x1 = var5;
      }
      drawGlColorRect(x, y + 1.0F, x + 1.0F, x1, y1);
    }
    public static void drawInternalRoundedRect2( float x,  float y, float x1,  float y1,  int borderC,  int insideC) {
     	 enableGL2D();
     	    x *= 2.0F;
     	    y *= 2.0F;
     	    x1 *= 2.0F;
     	    y1 *= 2.0F;
     	    GL11.glScalef(0.5F, 0.5F, 0.5F);
    	   drawVLine(x, y + 5.5F, y1 - 6.5F, borderC);
     	    drawVLine(x + 2, y + 3F, y1 - 4F, borderC);
     	    
     	  //drawVLine(x1, y + 5.5F, y1 - 6.5F, borderC);
   	    drawVLine(x1 - 3, y + 3F, y1 - 4F, borderC);
     	    
     	    drawVLine(x1 - 1.0F, y + 5.5F, y1 - 6.5F, borderC);
     	    
     	    drawHLine(x + 6.5F, x1 - 7.5F, y, borderC);
     	    drawHLine(x + 4F, x1 - 4.5F, y + 2, borderC);
     	   /* drawHLine(x + 3.0F, x1 - 4.0F, y1, 0xaaafafaf);
     	    drawHLine(x + 4.0F, x1 - 5.0F, y1+0.5f, 0x80cfcfcf);
     	    drawHLine(x + 6.0F, x1 - 7.0F, y1+1, 0x20e7e7e7);*/
     	    //GlStateManager.enableAlpha();
     	    //TOP RIGHT
     	    drawHLine(x1 - 5.5f, x1 - 6.5F, y, insideC);
     	    drawHLine(x1 - 1, x1 - 1.0F, y + 5.5f, insideC);
     	    
     	    //TOP LEFT
     	    drawHLine(x + 4.5F, x + 5.5F, y, insideC);
     	    drawVLine(x + 0.0F, y + 3.5f, y + 6.5f, insideC);
     	    
     	    //BOTTOM LEFT
     	//    drawVLine(x + 1.5F, y1 - 4f, y1 - 7.5f, borderC);
     	 //   drawHLine(x + 4.5F, x + 5.5F, y1  - 2.5f, borderC);
     	    
     	  drawVLine(x + 0F, y1 - 7.5f, y1 - 5f, insideC);
       drawVLine(x + 5.5F, y1 - 2.5f, y1 - 0.0f, insideC);
     	    
     	  //BOTTOM RIGHT
     	   drawHLine(x1 - 1, x1 - 1.0F, y1 - 6.5f, insideC);
     	    drawHLine(x1 - 5.5f, x1 - 6.5F, y1 - 1, insideC);
     	    
     	  drawHLine(x + 4F, x1 - 4.5F, y1 - 2.5F, borderC);
     	    drawHLine(x + 6.5F, x1 - 8.0F, y1 - 1.0F, borderC);
     	    
     	    //drawHLine(x + 2.0F, x + 1.0F, y + 1.0F, borderC);
     	    //drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
     	   // drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
     	   // drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
     	    
     	    
     	    drawGlColorRect(x + 3F, y + 3F, x1 - 3F, y1 - 3F, borderC);
     	    GL11.glScalef(2.0F, 2.0F, 2.0F);
     	    disableGL2D();
     }
    
    public static void drawRoundedRectWithShadow( float x,  float y, float x1,  float y1,  int borderC,  int insideC) {
      	 enableGL2D();
      	    x *= 2.0F;
      	    y *= 2.0F;
      	    x1 *= 2.0F;
      	    y1 *= 2.0F;
      	    GL11.glScalef(0.5F, 0.5F, 0.5F);
      	    //drawInternalRoundedRect2(x - 01.5F, y-01.6F, x1+1.6F, y1+1.5F, 0xff606060, 0x90606060);
      	    drawInternalRoundedRect2(x - 02.5F, y-01.6F, x1+2.6F, y1+2.5F, 0x30505050, 0x10505050);
      	    drawInternalRoundedRect2(x - 01.5F, y-01.6F, x1+1.6F, y1+1.5F, 0x50505050, 0x30606060);
      	    drawInternalRoundedRect2(x - 0.5F, y-0.6F, x1+0.6F, y1+0.5F, 0x60505050, 0x50505050);
      	    drawVLine(x, y + 2.0F, y1 - 3.0F, borderC);
      	    drawVLine(x1 - 1.0F, y + 2.0F, y1 - 3.0F, borderC);
      	    drawHLine(x + 3.0F, x1 - 4.0F, y, borderC);
      	   // GlStateManager.enableBlend();
      		//  GlStateManager.disableAlpha();
      	   /* drawHLine(x + 3.0F, x1 - 4.0F, y1, 0xaaafafaf);
      	    drawHLine(x + 4.0F, x1 - 5.0F, y1+0.5f, 0x80cfcfcf);
      	    drawHLine(x + 6.0F, x1 - 7.0F, y1+1, 0x20e7e7e7);*/
      		 
      	    //GlStateManager.enableAlpha();
      	    //TOP RIGHT
      	    drawHLine(x1 - 3, x1 - 3.0F, y, insideC);
      	    drawHLine(x1 - 1, x1 - 1.0F, y + 2, insideC);
      	    
      	    //TOP LEFT
      	    drawHLine(x + 2.0F, x + 2.0F, y, insideC);
      	    drawHLine(x + 0.0F, x + 0.0F, y + 2, insideC);
      	    
      	    //BOTTOM LEFT
      	    drawHLine(x + 0.0F, x + 0.0F, y1 - 3, insideC);
      	    drawHLine(x + 2.0F, x + 2.0F, y1 - 1, insideC);
      	    
      	  //BOTTOM RIGHT
      	    drawHLine(x1 - 1, x1 - 1.0F, y1 - 3, insideC);
      	    drawHLine(x1 - 3, x1 - 3.0F, y1 - 1, insideC);
      	    
      	    drawHLine(x + 3.0F, x1 - 4.0F, y1 - 1.0F, borderC);
      	    //drawHLine(x + 2.0F, x + 1.0F, y + 1.0F, borderC);
      	    //drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
      	   // drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
      	   // drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
      	    drawGlColorRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, borderC);
      	    GL11.glScalef(2.0F, 2.0F, 2.0F);
      	    disableGL2D();
      }
    public static void scissorRect1(float x, float y, float width, double height) {
        ScaledResolution sr = new ScaledResolution(mc);
        int factor = sr.getScaleFactor();
        GL11.glScissor((int) (x * (float) factor), (int) (((float) sr.getScaledHeight() - height) * (float) factor), (int) ((width - x) * (float) factor), (int) ((height - y) * (float) factor));
    }
    public static void drawSkeetButton(float x, float y, float right, float bottom) {
        drawSmoothRect(x - 31.0f, y - 43.0f, right + 31.0f, bottom - 30.0f, new Color(0, 0, 0, 255).getRGB());
        drawSmoothRect(x - 30.5f, y - 42.5f, right + 30.5f, bottom - 30.5f, new Color(45, 45, 45, 255).getRGB());
        DrawHelper.drawGradientRect((int) x - 30, (int) y - 42, right + 30, bottom - 31, new Color(48, 48, 48, 255).getRGB(), new Color(19, 19, 19, 255).getRGB());
    }
    public static void drawBorder(float left, float top, float right, float bottom, float borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        drawRect(left - (!borderIncludedInBounds ? borderWidth : 0), top - (!borderIncludedInBounds ? borderWidth : 0), right + (!borderIncludedInBounds ? borderWidth : 0), bottom + (!borderIncludedInBounds ? borderWidth : 0), borderColor);
        drawRect(left + (borderIncludedInBounds ? borderWidth : 0), top + (borderIncludedInBounds ? borderWidth : 0), right - ((borderIncludedInBounds ? borderWidth : 0)), bottom - ((borderIncludedInBounds ? borderWidth : 0)), insideColor);
    }

    public static void drawSkeetRectWithoutBorder(float x, float y, float right, float bottom) {
        drawSmoothRect1(x - 41f, y - 61f, right + 41f, bottom + 61f, new Color(48, 48, 48, 255).getRGB());
        drawSmoothRect1(x - 40.0f, y - 60.0f, right + 40.0f, bottom + 60.0f, new Color(17, 17, 17, 255).getRGB());
    }
    public static void drawSmoothRectBetter(float x, float y, float width, float height, int color) {
        drawSmoothRect1(x, y, x + width, y + height, color);
    }
    public static void drawGradientRectBetter(float x, float y, float width, float height, int color, int color2) {
        drawGradientRect1(x, y, x + width, y + height, color, color2);
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
        bufferbuilder.pos(left, top, gui.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, gui.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(right, bottom, gui.zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, top, gui.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    public static void drawSmoothRect1(float left, float top, float right, float bottom, int color) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        drawRect(left, top, right, bottom, color);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawRect(left * 2 - 1, top * 2, left * 2, bottom * 2 - 1, color);
        drawRect(left * 2, top * 2 - 1, right * 2, top * 2, color);
        drawRect(right * 2, top * 2, right * 2 + 1, bottom * 2 - 1, color);
        drawRect(left * 2, bottom * 2 - 1, right * 2, bottom * 2, color);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glScalef(2F, 2F, 2F);
    }
    public static int darker(int color, float factor) {
        int r = (int) ((float) (color >> 16 & 0xFF) * factor);
        int g = (int) ((float) (color >> 8 & 0xFF) * factor);
        int b = (int) ((float) (color & 0xFF) * factor);
        int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF | (a & 0xFF) << 24;
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
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(width);
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


    public static Color astolfo(boolean clickgui, int yOffset) {
        float speed = clickgui ? ClickGUI.speed.getNumberValue() * 100 : 2 * 100;
        float hue = (System.currentTimeMillis() % (int) speed) + yOffset;
        if (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5F) {
            hue = 0.5F - (hue - 0.5F);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, 0.4F, 1F);
    }
    public static int fadeColor(int startColor, int endColor, float progress) {
        if (progress > 1) {
            progress = 1 - progress % 1;
        }
        return fade(startColor, endColor, progress);
    }

    public static void glColor(final int red, final int green, final int blue, final int alpha) {
        GlStateManager.color(red / 255F, green / 255F, blue / 255F, alpha / 255F);
    }
    public static int rainbowNew(int delay, float saturation, float brightness) {
        double rainbow = Math.ceil((System.currentTimeMillis() + delay) / 16);
        rainbow %= 360.0D;
        return Color.getHSBColor((float) (rainbow / 360.0D), saturation, brightness).getRGB();
    }
    public static void drawRectWithEdge(double x, double y, double width, double height, Color color, Color color2) {
        drawRect(x, y, x + width, y + height, color.getRGB());
        int c = color2.getRGB();
        drawRect(x - 1.0D, y, x, y + height, c);
        drawRect(x + width, y, x + width + 1.0D, y + height, c);
        drawRect(x - 1.0D, y - 1.0D, x + width + 1.0D, y, c);
        drawRect(x - 1.0D, y + height, x + width + 1.0D, y + height + 1.0D, c);
    }

    public static void glColor(final Color color) {
        final float red = color.getRed() / 255F;
        final float green = color.getGreen() / 255F;
        final float blue = color.getBlue() / 255F;
        final float alpha = color.getAlpha() / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(final Color color, final int alpha) {
        glColor(color, alpha/255F);
    }

    public static void glColor(final Color color, final float alpha) {
        final float red = color.getRed() / 255F;
        final float green = color.getGreen() / 255F;
        final float blue = color.getBlue() / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }


    public static void glColor(final int hex, final int alpha) {
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha / 255F);
    }

    public static void glColor(final int hex, final float alpha) {
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }
    public static void startSmooth() {
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
    }
    public static int getColor1(int brightness) {
        return getColor(brightness, brightness, brightness, 255);
    }
    public static void drawRect2(double x, double y, double width, double height, int color) {
        drawRect(x, y, x + width, y + height, color);
    }
    public static void blockEsp(BlockPos blockPos, Color color, boolean outline) {
        double x = blockPos.getX() - mc.getRenderManager().renderPosX;
        double y = blockPos.getY() - mc.getRenderManager().renderPosY;
        double z = blockPos.getZ() - mc.getRenderManager().renderPosZ;
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
    public static void drawBoundingBox(AxisAlignedBB axisalignedbb) {
        GL11.glBegin(7);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        GL11.glEnd();
    }
    public static void drawLinesAroundPlayer(Entity entity, double radius, float partialTicks, int points, float width, int color) {
        GL11.glPushMatrix();
        enableGL2D3();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - renderManager.viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - renderManager.viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - renderManager.viewerPosZ;
        color228(color);
        for (int i = 0; i <= points; i++)
            GL11.glVertex3d(x + radius * Math.cos(i * 6.283185307179586D / points), y, z + radius * Math.sin(i * 6.283185307179586D / points));
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        disableGL2D3();
        GL11.glPopMatrix();
    }
    public static void enableGL2D3() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    public static void disableGL2D3() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void color228(int color) {
        GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(color >> 24 & 0xFF));
    }
    public static void drawEntityOnScreen(double posX, double posY, double scale, float mouseX, float mouseY, EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)-scale, (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float)Math.atan((mouseX / 40.0F)) * 20.0F;
        ent.rotationYaw = (float)Math.atan((mouseX / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float)Math.atan((mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity((Entity)ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    public static void endSmooth() {
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glEnable(2832);
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    public static void enableStandardItemLighting() {
        GlStateManager.enableLighting();
        GlStateManager.enableLight(0);
        GlStateManager.enableLight(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(1032, 5634);
        GlStateManager.glLight(16384, 4611, setColorBuffer(LIGHT0_POS.xCoord, LIGHT0_POS.yCoord, LIGHT0_POS.zCoord, 0.0D));
        float f = 0.6F;
        GlStateManager.glLight(16384, 4609, setColorBuffer(0.6F, 0.6F, 0.6F, 1.0F));
        GlStateManager.glLight(16384, 4608, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GlStateManager.glLight(16384, 4610, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GlStateManager.glLight(16385, 4611, setColorBuffer(LIGHT1_POS.xCoord, LIGHT1_POS.yCoord, LIGHT1_POS.zCoord, 0.0D));
        GlStateManager.glLight(16385, 4609, setColorBuffer(0.6F, 0.6F, 0.6F, 1.0F));
        GlStateManager.glLight(16385, 4608, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GlStateManager.glLight(16385, 4610, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GlStateManager.shadeModel(7424);
        float f1 = 0.4F;
        GlStateManager.glLightModel(2899, setColorBuffer(0.4F, 0.4F, 0.4F, 1.0F));
    }

    private static final Vec3d LIGHT0_POS = (new Vec3d(0.20000000298023224D, 1.0D, -0.699999988079071D)).normalize();

    private static final Vec3d LIGHT1_POS = (new Vec3d(-0.20000000298023224D, 1.0D, 0.699999988079071D)).normalize();

    private static FloatBuffer setColorBuffer(double p_74517_0_, double p_74517_2_, double p_74517_4_, double p_74517_6_) {
        return setColorBuffer((float)p_74517_0_, (float)p_74517_2_, (float)p_74517_4_, (float)p_74517_6_);
    }
    public static void enableGUIStandardItemLighting() {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(165.0F, 1.0F, 0.0F, 0.0F);
        enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public static void disableStandardItemLighting() {
        GlStateManager.disableLighting();
        GlStateManager.disableLight(0);
        GlStateManager.disableLight(1);
        GlStateManager.disableColorMaterial();
    }
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
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

    public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        setColor(color.getRGB());
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public static void setColor(int color) {
        GL11.glColor4ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF), (byte) (color >> 24 & 0xFF));
    }
    public static void drawRectWithGlow(double X, double Y, double Width, double Height, double GlowRange, double GlowMultiplier, Color color) {
        for (float i = 1; i < GlowRange; i += 0.5f) {
           drawRoundedRect99(X - (GlowRange - i), Y - (GlowRange - i), Width + (GlowRange - i), Height + (GlowRange - i), DrawHelper.injectAlpha(color, (int) (Math.round(i * GlowMultiplier))).getRGB());
        }
    }
    public static void drawRoundedRect99(double x, double y, double x1, double y1, int insideC) {
       drawRect(x + 0.5, y, x1 - 0.5, y + 0.5, insideC);
       drawRect(x + 0.5, y1 - 0.5, x1 - 0.5, y1, insideC);
       drawRect(x, y + 0.5, x1, y1 - 0.5, insideC);
    }
    public static void drawGlow(final double x, final double y, final double x1, final double y1, final int color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        drawVGradientRect((float) (int) x, (float) (int) y, (float) (int) x1, (float) (int) (y + (y1 - y) / 2.0), DrawHelper.setAlpha(new Color(color), 0).getRGB(), color);
        drawVGradientRect((float) (int) x, (float) (int) (y + (y1 - y) / 2.0), (float) (int) x1, (float) (int) y1, color, DrawHelper.setAlpha(new Color(color), 0).getRGB());
        final int radius = (int) ((y1 - y) / 2.0);
        drawPolygonPart(x, y + (y1 - y) / 2.0, radius, 0, color, DrawHelper.setAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x, y + (y1 - y) / 2.0, radius, 1, color, DrawHelper.setAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x1, y + (y1 - y) / 2.0, radius, 2, color, DrawHelper.setAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x1, y + (y1 - y) / 2.0, radius, 3, color, DrawHelper.setAlpha(new Color(color), 0).getRGB());
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    public static void drawPolygonPart(final double x, final double y, final int radius, final int part, final int color, final int endcolor) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final float alpha2 = (endcolor >> 24 & 0xFF) / 255.0f;
        final float red2 = (endcolor >> 16 & 0xFF) / 255.0f;
        final float green2 = (endcolor >> 8 & 0xFF) / 255.0f;
        final float blue2 = (endcolor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
        final double TWICE_PI = 6.283185307179586;
        for (int i = part * 90; i <= part * 90 + 90; ++i) {
            final double angle = 6.283185307179586 * i / 360.0 + Math.toRadians(180.0);
            bufferbuilder.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0.0).color(red2, green2, blue2, alpha2).endVertex();
        }
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    public static void drawVGradientRect(final float left, final float top, final float right, final float bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) right, (double) top, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos((double) left, (double) top, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos((double) left, (double) bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }




    public static void drawBorderedRect(double left, double top, double right, double bottom, double borderWidth,
                                        int insideColor, int borderColor, boolean borderIncludedInBounds) {
        DrawHelper.drawRect(left - (!borderIncludedInBounds ? borderWidth : 0.0),
                top - (!borderIncludedInBounds ? borderWidth : 0.0),
                right + (!borderIncludedInBounds ? borderWidth : 0.0),
                bottom + (!borderIncludedInBounds ? borderWidth : 0.0), borderColor);
        DrawHelper.drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0),
                top + (borderIncludedInBounds ? borderWidth : 0.0),
                right - (borderIncludedInBounds ? borderWidth : 0.0),
                bottom - (borderIncludedInBounds ? borderWidth : 0.0), insideColor);
    }
    
    public static void drawTribleGradient(float x, float y, float width, float height, Color oneColor, Color twoColor, Color threeColor) {
    	drawGradientRect1(x, y, x + width / 2, y + height, oneColor.getRGB(), twoColor.getRGB());
    	
    	drawGradientRect1(x + width / 2, y, x + width, y + height, twoColor.getRGB(), threeColor.getRGB());
    	
    }

    public static Color astolfoColors1(float yDist, float yTotal) {
        float speed = 3500f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * 12;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return new Color(hue, 0.4f, 1);
    }
    public static final void color(Color color) {
        if (color == null)
            color = Color.white;
        color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }
    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }
    public static final void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    public static void staticJelloCircle() {
        if (KillAuraHelper.canAttack(KillAura.target) && KillAura.target.getHealth() > 0 && mc.player.getDistanceToEntity(KillAura.target) <= (float) RangeHelper.getRange() && !KillAura.target.isDead) {
            double height = 0.8 * (1 + Math.sin(2 * Math.PI * (time * .3)));
            double width = KillAura.target.width;

            final double x = KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosX;
            final double y = KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosY;
            final double z = KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosZ;

            GlStateManager.enableBlend();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GL11.glLineWidth(1.2f);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
            {
                for (int j = 0; j < 361; j++) {
                    DrawHelper.color(setAlpha(DrawHelper.astolfoColors45(j - j + 1, j,  50, 10), (int) (255 * (1.3 - height))));
                    double x1 = x + Math.cos(Math.toRadians(j)) * 0.7;
                    double z1 = z - Math.sin(Math.toRadians(j)) * 0.7;
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(j)) * width, y + 0.05, z - Math.sin(Math.toRadians(j)) * width);
                    DrawHelper.color(setAlpha(DrawHelper.astolfoColors45(j - j + 1, j, (float) 50, 10), 0));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(j)) * width, y + 0.05 + (0.13 * height), z - Math.sin(Math.toRadians(j)) * width);
                }
            }
            GL11.glEnd();
            GL11.glBegin(GL_LINE_LOOP);
            {
                for (int j = 0; j < 365; j++) {
                    DrawHelper.setColor(DrawHelper.astolfoColors45(j - j + 15, j, (float) 50, 10).getRGB());

                    GL11.glVertex3d(x + Math.cos(Math.toRadians(j)) * width, y + 0.05, z - Math.sin(Math.toRadians(j)) * width);
                }
            }
            GL11.glEnd();
            GlStateManager.enableAlpha();
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.resetColor();
        }
    }

    public static void staticJelloCircle1() {
            double height = 0.8 * (1 + Math.sin(2 * Math.PI * (time * .3)));
            double width = mc.player.width;

        final double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosX;
        final double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosY;
        final double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosZ;

            GlStateManager.enableBlend();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GL11.glLineWidth(1.2f);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
            {
                for (int j = 0; j < 361; j++) {
                    DrawHelper.color(setAlpha(ClientHelper.getClientColor(), (int) (255 * (1.3 - height))));
                    double x1 = x + Math.cos(Math.toRadians(j)) * 0.7;
                    double z1 = z - Math.sin(Math.toRadians(j)) * 0.7;
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(j)) * width, y + 0.1, z - Math.sin(Math.toRadians(j)) * width);
                    DrawHelper.color(setAlpha(ClientHelper.getClientColor(), 0));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(j)) * width, y + 0.1 + (0.13 * height), z - Math.sin(Math.toRadians(j)) * width);
                }
            }
            GL11.glEnd();
            GL11.glBegin(GL_LINE_LOOP);
            {
                    for (int j = 0; j < 365; j++) {
                        DrawHelper.color(ClientHelper.getClientColor());
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(j)) * width, y + 0.1, z - Math.sin(Math.toRadians(j)) * width);
                    }
            }
            GL11.glEnd();
            GlStateManager.enableAlpha();
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.resetColor();
        }

    public static void drawRoundedRect(float left, float top, float right, float bottom, int smooth, Color color) {
        Gui.drawRect(((int) left + smooth), (int) top, ((int) right - smooth), (int) bottom, color.getRGB());
        Gui.drawRect((int) left, ((int) top + smooth), (int) right, ((int) bottom - smooth), color.getRGB());
        drawFilledCircle((int) left + smooth, (int) top + smooth, smooth, color);
        drawFilledCircle((int) right - smooth, (int) top + smooth, smooth, color);
        drawFilledCircle((int) right - smooth, (int) bottom - smooth, smooth, color);
        drawFilledCircle((int) left + smooth, (int) bottom - smooth, smooth, color);
    }

    public static void drawRoundedRect(double x, double y, double width, double height, float radius, Color color) {

        float x2 = (float) (x + ((radius / 2F) + 0.5F));
        float y2 = (float) (y + ((radius / 2F) + 0.5F));
        float width2 = (float) (width - ((radius / 2F) + 0.5F));
        float height2 = (float) (height - ((radius / 2F) + 0.5F));

        drawRect(x2, y2, x2 + width2, y2 + height2, color.getRGB());

        polygon(x, y, radius * 2, 360, true, color);
        polygon(x + width2 - radius + 1.2, y, radius * 2, 360, true, color);

        polygon(x + width2 - radius + 1.2, y + height2 - radius + 1, radius * 2, 360, true, color);
        polygon(x, y + height2 - radius + 1, radius * 2, 360, true, color);

        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
        drawRect(x2 - radius / 2 - 0.5F, y2 + radius / 2, x2 + width2, y2 + height2 - radius / 2, color.getRGB());
        drawRect(x2, y2 + radius / 2, x2 + width2 + radius / 2 + 0.5f, y2 + height2 - radius / 2, color.getRGB());
        drawRect(x2 + radius / 2, y2 - radius / 2 - 0.5F, x2 + width2 - radius / 2, y + height2 - radius / 2, color.getRGB());
        drawRect(x2 + radius / 2, y2, x2 + width2 - radius / 2, y2 + height2 + radius / 2 + 0.5f, color.getRGB());
    }
    
    public static void polygon(double x, double y, double sideLength, double amountOfSides, boolean filled, Color color) {
        sideLength /= 2;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.disableAlpha();
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
        if (!filled) {
            GL11.glLineWidth(1);
        }
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBegin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_STRIP);

        for (double i = 0; i <= amountOfSides; i++) {
            double angle = i * (Math.PI * 2) / amountOfSides;
            GL11.glVertex2d(x + (sideLength * Math.cos(angle)) + sideLength, y + (sideLength * Math.sin(angle)) + sideLength);
        }

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableAlpha();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public static final void drawSmoothRect(float left, float top, float right, float bottom, int color) {
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        DrawHelper.drawRect(left, top, right, bottom, color);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        DrawHelper.drawRect(left * 2.0f - 1.0f, top * 2.0f, left * 2.0f, bottom * 2.0f - 1.0f, color);
        DrawHelper.drawRect(left * 2.0f, top * 2.0f - 1.0f, right * 2.0f, top * 2.0f, color);
        DrawHelper.drawRect(right * 2.0f, top * 2.0f, right * 2.0f + 1.0f, bottom * 2.0f - 1.0f, color);
        DrawHelper.drawRect(left * 2.0f, bottom * 2.0f - 1.0f, right * 2.0f, bottom * 2.0f, color);
        GL11.glDisable(3042);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }

    public static void drawFilledCircle(int xx, int yy, float radius, Color color) {
        int sections = 50;
        double dAngle = 6.283185307179586D / sections;
        GL11.glPushAttrib(8192);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < sections; i++) {
            float x = (float) (radius * Math.sin(i * dAngle));
            float y = (float) (radius * Math.cos(i * dAngle));
            GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F,
                    color.getAlpha() / 255.0F);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnd();
        GL11.glPopAttrib();
    }

    public static Color setAlpha(Color color, int alpha) {
        alpha = (int) MathHelper.clamp(alpha, 0, 255);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    public static Color getColorWithOpacity(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    public static void renderItem(ItemStack itemStack, int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.enableDepth();
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, itemStack, x, y);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableDepth();
    }

    public static void drawRoundedRect1(double x, double y, double x1, double y1, int insideC) {
        DrawHelper.drawRect(x + 0.5, y, x1 - 0.5, y + 0.5, insideC);
        DrawHelper.drawRect(x + 0.5, y1 - 0.5, x1 - 0.5, y1, insideC);
        DrawHelper.drawRect(x, y + 0.5, x1, y1 - 0.5, insideC);
    }
    public static void drawRoundedRect2(double x, double y, double x1, double y1, int borderC, int insideC) {
        DrawHelper.drawRect(x + 0.5, y, x1 - 0.5, y + 0.5, insideC);
        DrawHelper.drawRect(x + 0.5, y1 - 0.5, x1 - 0.5, y1, insideC);
        DrawHelper.drawRect(x, y + 0.5, x1, y1 - 0.5, insideC);
    }


    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
        int factor = scale.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((scale.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
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
        bufferbuilder.pos((double) e2, (double) e, (double) zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) d, (double) e, (double) zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) d, (double) g, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double) e2, (double) g, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return (isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck);
    }

    private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static void putVertex3d(Vec3d vec) {
        GL11.glVertex3d(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public static Vec3d getRenderPos(double x, double y, double z) {

        x = x - mc.getRenderManager().renderPosX;
        y = y - mc.getRenderManager().renderPosY;
        z = z - mc.getRenderManager().renderPosZ;

        return new Vec3d(x, y, z);
    }


    public static void drawNewRect(double left, double top, double right, double bottom, int color) {
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
        bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static int color(int n, int n2, int n3, int n4) {
        n4 = 255;
        return new Color(n, n2, n3, n4).getRGB();
    }
    
    public static int rainbow(int delay, double speed) {
		double rainbow = Math.ceil((System.currentTimeMillis() + delay) / speed);
		rainbow %= 360.0D;
		return Color.getHSBColor((float) -((rainbow / 360.0F)), 0.9F, 1.0F).getRGB();
	}

    public static Color rainbow(int delay, float saturation, float brightness) {
        double rainbow = Math.ceil((System.currentTimeMillis() + delay) / 16);
        rainbow %= 360;
        return Color.getHSBColor((float) (rainbow / 360), saturation, brightness);
    }
    public static Color getRainbow(final int offset, final int speed) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 0.7f, 1f);
    }

    public static Color rainbow2(int delay, float saturation, float brightness) {
        double rainbow = Math.ceil((System.currentTimeMillis() + delay) / 16);
        rainbow %= 360.0D;
        return Color.getHSBColor((float) (rainbow / 360.0D), saturation, brightness);
    }
    public static int getHealthColor(float health, float maxHealth) {
        return Color.HSBtoRGB(Math.max(0.0F, Math.min(health, maxHealth) / maxHealth) / 3, 1, 0.8f) | 0xFF000000;
    }
    public static Color getHealthColor(EntityLivingBase entityLivingBase) {
        float health = entityLivingBase.getHealth();
        float[] fractions = new float[]{0.0f, 0.15f, 0.55f, 0.7f, 0.9f};
        Color[] colors = new Color[]{new Color(133, 0, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN};
        float progress = health / entityLivingBase.getMaxHealth();
        return health >= 0.0f ? DrawHelper.blendColors(fractions, colors, progress).brighter() : colors[0];
    }

    public static Color getHealthColor2(float health, float maxHealth) {
        float[] fractions = { 0.0F, 0.5F, 1.0F };
        Color[] colors = { new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN };
        float progress = health / maxHealth;
        return blendColors(fractions, colors, progress).brighter();
    }
    public static int getRandomColor() {
        char[] letters = "012345678".toCharArray();
        String color = "0x";
        for (int i = 0; i < 6; ++i) {
            color = color + letters[new Random().nextInt(letters.length)];
        }
        return Integer.decode(color);
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float) c.getRed();
        float g = 0.003921569f * (float) c.getGreen();
        float b = 0.003921569f * (float) c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static Color getGradientOffset(Color color1, Color color2, double offset) {
        if (offset > 1.0) {
            double left = offset % 1.0;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1.0 - left;
        }
        double inverse_percent = 1.0 - offset;
        int redPart = (int) ((double) color1.getRed() * inverse_percent + (double) color2.getRed() * offset);
        int greenPart = (int) ((double) color1.getGreen() * inverse_percent + (double) color2.getGreen() * offset);
        int bluePart = (int) ((double) color1.getBlue() * inverse_percent + (double) color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    public static class Colors
    {
        public static final int WHITE;
        public static final int BLACK;
        public static final int RED;
        public static final int GREEN;
        public static final int BLUE;
        public static final int ORANGE;
        public static final int PURPLE;
        public static final int GRAY;
        public static final int DARK_RED;
        public static final int YELLOW;
        public static final int RAINBOW = Integer.MIN_VALUE;

        static {
            WHITE = DrawHelper.toRGBA(255, 255, 255, 255);
            BLACK = DrawHelper.toRGBA(0, 0, 0, 255);
            RED = DrawHelper.toRGBA(255, 0, 0, 255);
            GREEN = DrawHelper.toRGBA(0, 255, 0, 255);
            BLUE = DrawHelper.toRGBA(0, 0, 255, 255);
            ORANGE = DrawHelper.toRGBA(255, 128, 0, 255);
            PURPLE = DrawHelper.toRGBA(163, 73, 163, 255);
            GRAY = DrawHelper.toRGBA(127, 127, 127, 255);
            DARK_RED = DrawHelper.toRGBA(64, 0, 0, 255);
            YELLOW = DrawHelper.toRGBA(255, 255, 0, 255);
        }
    }
    public static int toRGBA(final int r, final int g, final int b, final int a) {
        return (r << 16) + (g << 8) + (b << 0) + (a << 24);
    }

    public static int toRGBA(final float r, final float g, final float b, final float a) {
        return toRGBA((int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f), (int)(a * 255.0f));
    }

    public static int getColor(int red, int green, int blue) {
        return DrawHelper.getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }

    public static int getColor(Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    public static int getColor(int bright) {
        return getColor(bright, bright, bright, 255);
    }
    public static int getColor(int brightness, int alpha) {
        return DrawHelper.getColor(brightness, brightness, brightness, alpha);
    }

    public static Color fade(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(
                ((float) (System.currentTimeMillis() % 2000L) / 1000.0f + (float) index / (float) count * 2.0f) % 2.0f
                        - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
    public static int fade(int startColor, int endColor, float progress) {
        float invert = 1.0f - progress;
        int r = (int) ((startColor >> 16 & 0xFF) * invert + (endColor >> 16 & 0xFF) * progress);
        int g = (int) ((startColor >> 8 & 0xFF) * invert + (endColor >> 8 & 0xFF) * progress);
        int b = (int) ((startColor & 0xFF) * invert + (endColor & 0xFF) * progress);
        int a = (int) ((startColor >> 24 & 0xFF) * invert + (endColor >> 24 & 0xFF) * progress);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length != colors.length) {
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
        int[] indicies = DrawHelper.getFractionIndicies(fractions, progress);
        float[] range = new float[] { fractions[indicies[0]], fractions[indicies[1]] };
        Color[] colorRange = new Color[] { colors[indicies[0]], colors[indicies[1]] };
        float max = range[1] - range[0];
        float value = progress - range[0];
        float weight = value / max;
        return DrawHelper.blend(colorRange[0], colorRange[1], 1.0f - weight);
    }

    public static int[] getFractionIndicies(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float) ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color = null;
        try {
            color = new Color(red, green, blue);
        } catch (IllegalArgumentException exp) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
        }
        return color;
    }

    public static int astolfo(int yOffset, int yTotal) {
        float speed = ArrayList.time.getNumberValue() * 1000;
        float hue = (System.currentTimeMillis() % (int) speed) + ((yOffset - yTotal) * 9L);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, 0.6F, 1F);
    }

    public static Color TwoColoreffect(Color cl1, Color cl2, double speed) {
    	double thing = speed / 4.0 % 1.0;
        float val = MathHelper.clamp((float) Math.sin(Math.PI * 6 * thing) / 2.0f + 0.5f, 0.0f, 1.0f);
        return new Color(lerp((float) cl1.getRed() / 255.0f, (float) cl2.getRed() / 255.0f, val),
                lerp((float) cl1.getGreen() / 255.0f, (float) cl2.getGreen() / 255.0f, val),
                lerp((float) cl1.getBlue() / 255.0f, (float) cl2.getBlue() / 255.0f, val));
    }

    public static int astolfoColors(int yOffset, int yTotal) {
        float hue;
        float speed = 2900.0f;
        for (hue = (float) (System.currentTimeMillis() % (long) ((int) speed))
                + (float) ((yTotal - yOffset) * 9); hue > speed; hue -= speed) {
        }
        if ((double) (hue /= speed) > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        return Color.HSBtoRGB(hue += 0.5f, 0.5f, 1.0f);
    }
    
    public static int cosmoColors(int yOffset, int yTotal) {
        float hue;
        float speed = 2900.0f;
        for (hue = (float) (System.currentTimeMillis() % (long) ((int) speed))
                + (float) ((yTotal - yOffset) * 9); hue > speed; hue -= speed) {
        }
        if ((double) (hue /= speed) > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        return Color.HSBtoRGB(hue += 0.5f, 0.5f, 1.0f);
    }

    public static Color astolfoColor(int yOffset, int yTotal) {
        float speed = 2900F;
        float hue = (float) (System.currentTimeMillis() % (int)speed) + ((yTotal - yOffset) * 9);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return new Color(hue, 0.5f, 1F);
    }
    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (col1 >> 24 & 255) / 255.0f;
        float f1 = (col1 >> 16 & 255) / 255.0f;
        float f2 = (col1 >> 8 & 255) / 255.0f;
        float f3 = (col1 & 255) / 255.0f;
        float f4 = (col2 >> 24 & 255) / 255.0f;
        float f5 = (col2 >> 16 & 255) / 255.0f;
        float f6 = (col2 >> 8 & 255) / 255.0f;
        float f7 = (col2 & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }


    public static int getTeamColor(Entity entityIn) {
      int i = -1;
        if (entityIn.getDisplayName().getUnformattedText().equalsIgnoreCase("пїЅf[пїЅcRпїЅf]пїЅc" + entityIn.getName())) {
            i = getColor(new Color(255, 60, 60));
        } else if (entityIn.getDisplayName().getUnformattedText().equalsIgnoreCase("пїЅf[пїЅ9BпїЅf]пїЅ9" + entityIn.getName())) {
            i = getColor(new Color(60, 60, 255));
        } else if (entityIn.getDisplayName().getUnformattedText().equalsIgnoreCase("пїЅf[пїЅeYпїЅf]пїЅe" + entityIn.getName())) {
            i = getColor(new Color(255, 255, 60));
        } else if (entityIn.getDisplayName().getUnformattedText().equalsIgnoreCase("пїЅf[пїЅaGпїЅf]пїЅa" + entityIn.getName())) {
            i = getColor(new Color(60, 255, 60));
        } else {
            i = getColor(new Color(255, 255, 255));
        }

        return i;
    }
    public static Color astolfoColors12(int yOffset, int yTotal) {
        float speed = 2900F;
        float hue = (float) (System.currentTimeMillis() % (int) speed) + ((yTotal - yOffset) * 9);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return new Color(hue, 0.5f, 1F);
    }
    public static int astolfoColors4(float yDist, float yTotal, float saturation) {
        float speed = 1800f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * 12;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, saturation, 1F);
    }

    public static Color astolfoColors5(float yDist, float yTotal, float saturation, float speedt) {
        float speed = 1800f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * speedt;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, saturation, 1F);
    }
    public static Color rainbowCol(float yDist, float yTotal, float saturation, float speedt) {
        float speed = 1800f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * speedt;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 5) {
            hue = 5 - (hue - 5);
        }
        hue += 5;
        return Color.getHSBColor(hue, saturation, 1F);
    }
    public static Color astolfoColors45(float yDist, float yTotal, float saturation, float speedt) {
        float speed = 1800f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * speedt;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, saturation, 1F);
    }

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }


    public static void blockEspFrame(BlockPos blockPos, float red, float green, float blue) {
       double x = (double)blockPos.getX() - mc.getRenderManager().renderPosX;
       double y = (double)blockPos.getY() - mc.getRenderManager().renderPosY;
       double z = (double)blockPos.getZ() - mc.getRenderManager().renderPosZ;
       GL11.glBlendFunc(770, 771);
       GL11.glEnable(3042);
       GL11.glLineWidth(2.0F);
       GL11.glDisable(3553);
       GL11.glDisable(2929);
       GL11.glDepthMask(false);
       GlStateManager.color(red, green, blue, 1.0F);
       drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
       GL11.glEnable(3553);
       GL11.glEnable(2929);
       GL11.glDepthMask(true);
       GL11.glDisable(3042);
    }
    
    static {
        shadowCache = new HashMap<Integer, Integer>();
        shader = new ResourceLocation("shaders/post/blur.json");
    }

}

