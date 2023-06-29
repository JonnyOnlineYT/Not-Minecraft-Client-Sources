package org.spray.heaven.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.main.Wrapper;

import java.io.IOException;
import java.util.Random;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class RenderUtil {

    public static float DELTA;

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != Wrapper.MC.displayWidth ||
                framebuffer.framebufferHeight != Wrapper.MC.displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(Wrapper.MC.displayWidth, Wrapper.MC.displayHeight, true);
        }
        return framebuffer;
    }
    
    public static double interpolate (double current, double old, double scale) {
        return old + (current - old) * scale;
    }
    public static void bindTexture(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public static void drawFace(EntityLivingBase entity, int x, int y, int width, int height) {
//		if (entity instanceof EntityPlayer) {
        drawFace(entity.getName(), x, y, width, height);
//		} else {
//			if (Wrapper.MC.getRenderManager().renderRender.bindEntity(entity)) {
//			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//			Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, 8.0F, 8, 8, width, height, 64.0F, 64.0F);
//			}
//		}
    }

    public static void drawFace(String username, int x, int y, int width, int height) {
        try {
            bindFace(username);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, 8.0F, 8, 8, width, height, 64.0F, 64.0F);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c, final int quality) {
        final float f = ((c >> 24) & 0xff) / 255F;
        final float f1 = ((c >> 16) & 0xff) / 255F;
        final float f2 = ((c >> 8) & 0xff) / 255F;
        final float f3 = (c & 0xff) / 255F;

        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / quality; i++) {
            final double x2 = Math.sin(((i * quality * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * quality * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
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

    public static float scrollAnimate(float endPoint, float current, float speed) {
        boolean shouldContinueAnimation = endPoint > current;
        if (speed < 0.0f) {
            speed = 0.0f;
        } else if (speed > 1.0f) {
            speed = 1.0f;
        }

        float dif = Math.max(endPoint, current) - Math.min(endPoint, current);
        float factor = dif * speed;
        return current + (shouldContinueAnimation ? factor : -factor);
    }


    public static double animate(double value, double target, double speed, boolean minedelta) {
        double c = value + (target - value) / (3 + speed * DELTA);
        double v = value
                + ((target - value)) / (2 + speed);
        return minedelta ? v : c;
    }

    public static double animate(double value, double target, boolean minedelta) {
        return animate(value, target, 1, minedelta);
    }

    public static double animate(double value, double target, double speed) {
        return animate(value, target, speed, false);
    }

    public static double animate(double value, double target) {
        return animate(value, target, 1, false);
    }

    public static Vec3d getRenderPos(Entity entity) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Wrapper.MC.getRenderPartialTicks()
                - Wrapper.MC.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Wrapper.MC.getRenderPartialTicks()
                - Wrapper.MC.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Wrapper.MC.getRenderPartialTicks()
                - Wrapper.MC.getRenderManager().viewerPosZ;

        return new Vec3d(x, y, z);
    }

    public static void renderStatsHealth(EntityLivingBase entity, int x, int y, int width) {
        Wrapper.MC.getTextureManager().bindTexture(Gui.ICONS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Random rand = new Random();
        IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        int i = MathHelper.ceil(entity.getHealth());
        int i2 = Math.max(10 - (y - 2), 3);
        float f = (float) iattributeinstance.getAttributeValue();
        int k1 = MathHelper.ceil(entity.getAbsorptionAmount());
        for (int j5 = MathHelper.ceil((f + (float) k1) / 2.0F) - 1; j5 >= 0; --j5) {
            int k5 = 16;

            if (entity.isPotionActive(MobEffects.POISON)) {
                k5 += 36;
            } else if (entity.isPotionActive(MobEffects.WITHER)) {
                k5 += 72;
            }

            int i4 = 0;

            // if (flag)
            // {
            // i4 = 1;
            // }

            int j4 = MathHelper.ceil((float) (j5 + 1) / 10.0F) - 1;
            int k4 = x + j5 % 10 * 8;
            int l4 = y - j4 * i2;

            if (i <= 4) {
                l4 += rand.nextInt(2);
            }

            int i5 = 0;

            if (entity.world.getWorldInfo().isHardcoreModeEnabled()) {
                i5 = 5;
            }

            Drawable.drawTexturedModalRect(k4, l4, 16 + i4 * 9, 9 * i5, 9, 9);

            if (k1 > 0) {
                if (k1 % 2 == 1) {
                    Drawable.drawTexturedModalRect(k4, l4, k5 + 153, 9 * i5, 9, 9);
                    --k1;
                } else {
                    Drawable.drawTexturedModalRect(k4, l4, k5 + 144, 9 * i5, 9, 9);
                    k1 -= 2;
                }
            } else {
                if (j5 * 2 + 1 < i) {
                    Drawable.drawTexturedModalRect(k4, l4, k5 + 36, 9 * i5, 9, 9);
                }

                if (j5 * 2 + 1 == i) {
                    Drawable.drawTexturedModalRect(k4, l4, k5 + 45, 9 * i5, 9, 9);
                }
            }
        }
    }

    public static void drawStack(ItemStack stack, boolean overlay, int x, int y) {
        GlStateManager.enableDepth();
        Wrapper.MC.getRenderItem().zLevel = 200F;
        Wrapper.MC.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        if (overlay)
            Wrapper.MC.getRenderItem().renderItemOverlays(Wrapper.MC.fontRendererObj, stack, x, y);
        Wrapper.MC.getRenderItem().zLevel = 0F;
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
    }

    public static void scale() {
        switch (Wrapper.MC.gameSettings.guiScale) {
            case 0:
                GlStateManager.scale(0.5, 0.5, 0.5);
                break;
            case 1:
                GlStateManager.scale(2, 2, 2);
                break;
            case 3:
                GlStateManager.scale(0.6666666666666667, 0.6666666666666667, 0.6666666666666667);
                break;
        }
    }

    public static void prepare() {
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        //	GL11.glEnable(GL32.GL_DEPTH_CLAMP);
    }

}
