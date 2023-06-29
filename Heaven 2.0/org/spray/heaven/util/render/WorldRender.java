package org.spray.heaven.util.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.BlockUtil;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class WorldRender {

    public static void drawBlockBox(BlockPos blockPos, Color color, float lineWidth) {
        final RenderManager renderManager = Wrapper.MC.getRenderManager();

        double x = blockPos.getX() - renderManager.getRenderPosX();
        double y = blockPos.getY() - renderManager.getRenderPosY();
        double z = blockPos.getZ() - renderManager.getRenderPosZ();

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        Block block = BlockUtil.getBlock(blockPos);

        EntityPlayer player = Wrapper.getPlayer();

        final double posX = player.lastTickPosX
                + (player.posX - player.lastTickPosX) * (double) Wrapper.MC.getRenderPartialTicks();
        final double posY = player.lastTickPosY
                + (player.posY - player.lastTickPosY) * (double) Wrapper.MC.getRenderPartialTicks();
        final double posZ = player.lastTickPosZ
                + (player.posZ - player.lastTickPosZ) * (double) Wrapper.MC.getRenderPartialTicks();
        axisAlignedBB = block.getSelectedBoundingBox(BlockUtil.getState(blockPos), Wrapper.getWorld(), blockPos)
                .expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)
                .offset(-posX, -posY, -posZ);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);

        glLineWidth(lineWidth);
        glEnable(GL_LINE_SMOOTH);
        ColorUtil.glColor(color);

        drawSelectionBoundingBox(axisAlignedBB);

        GlStateManager.resetColor();
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION);

        // Lower Rectangle
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();

        // Upper Rectangle
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();

        // Upper Rectangle
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();

        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();

        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();

        tessellator.draw();
    }

    public static void drawFilledBox(AxisAlignedBB bb, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int) 770, (int) 771, (int) 0, (int) 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean) false);
        float alpha = (float) (color >> 24 & 0xFF) / 255.0f;
        float red = (float) (color >> 16 & 0xFF) / 255.0f;
        float green = (float) (color >> 8 & 0xFF) / 255.0f;
        float blue = (float) (color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.depthMask((boolean) true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawGradientFilledBox(AxisAlignedBB bb, Color startColor, Color endColor) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int) 770, (int) 771, (int) 0, (int) 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean) false);
        float alpha = (float) endColor.getAlpha() / 255.0f;
        float red = (float) endColor.getRed() / 255.0f;
        float green = (float) endColor.getGreen() / 255.0f;
        float blue = (float) endColor.getBlue() / 255.0f;
        float alpha1 = (float) startColor.getAlpha() / 255.0f;
        float red1 = (float) startColor.getRed() / 255.0f;
        float green1 = (float) startColor.getGreen() / 255.0f;
        float blue1 = (float) startColor.getBlue() / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.depthMask((boolean) true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawTracer(Entity entity, Color color) {
        Vec3d pos = RenderUtil.getRenderPos(entity);
        Vec3d eyeVector = new Vec3d(0.0, 0.0, 70.0)
                .rotatePitch((float) (-Math.toRadians(Wrapper.getPlayer().rotationPitch)))
                .rotateYaw((float) (-Math.toRadians(Wrapper.getPlayer().rotationYaw)));
        GL11.glLineWidth(1f);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL32.GL_DEPTH_CLAMP);
        glDisable(GL_DEPTH_TEST);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        ColorUtil.glColor(color);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(eyeVector.xCoord, Wrapper.getPlayer().getEyeHeight() + eyeVector.yCoord, eyeVector.zCoord);
        GL11.glVertex3d(pos.xCoord, pos.yCoord, pos.zCoord);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL32.GL_DEPTH_CLAMP);
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.color(1f, 1f, 1f);
        glLineWidth(1f);
    }

    public static void drawCircle(BlockPos pos, double radius, float width, int color) {
        final RenderManager renderManager = Wrapper.MC.getRenderManager();
        drawCircle(pos.getX() - renderManager.getRenderPosX(), pos.getY() - renderManager.getRenderPosY(),
                pos.getZ() - renderManager.getRenderPosZ(), radius, width, color);
    }

    public static void drawCircle(Entity entity, double radius, float width, int color) {
        Vec3d position = RenderUtil.getRenderPos(entity);
        drawCircle(position.xCoord, position.yCoord, position.zCoord, radius, width, color);
    }

    public static void drawCircle(double x, double y, double z, double radius, float width, int color) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(width);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        ColorUtil.glColor(color);
        for (int i = 0; i <= 360; i++) {
            GL11.glVertex3d(x + radius * Math.cos(i * MathHelper.PI2 / 360), y,
                    z + radius * Math.sin(i * MathHelper.PI2 / 360));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
        GlStateManager.resetColor();
    }

    public static void drawBar(Entity entity, float yaw, double marginX, double value, double maxValue, int color) {
        double inc = (entity.height + 0.1) / maxValue;
        double end = inc * ((value > maxValue) ? maxValue : value);

        float ticks = Wrapper.MC.getRenderPartialTicks();
        double renderPosX = Wrapper.MC.getRenderManager().viewerPosX;
        double renderPosY = Wrapper.MC.getRenderManager().viewerPosY;
        double renderPosZ = Wrapper.MC.getRenderManager().viewerPosZ;
        double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ticks) - renderPosX;
        double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ticks) - renderPosY;
        double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ticks) - renderPosZ;

        float playerViewY = Wrapper.MC.getRenderManager().playerViewY;
        float playerViewX = Wrapper.MC.getRenderManager().playerViewX;
        boolean thirdPersonView = Wrapper.MC.getRenderManager().options.thirdPersonView == 2;
        float width = 0.02f;
        GL11.glPushMatrix();

        GlStateManager.translate(xPos, yPos, zPos);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (thirdPersonView ? -0.1 : 0.1) * playerViewX, 0.0F, 1.0F, 0.0F);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL11.GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        ColorUtil.glColor(color);
        drawFilledBox(
                new AxisAlignedBB((entity.width) + marginX, 0.0D, 0.0D, (entity.width + marginX) + width, entity.height + 0.1, 0.0D), 0xFF414141);
        drawFilledBox(
                new AxisAlignedBB((entity.width) + marginX, 0.0D, 0.0D, (entity.width + marginX) + width, end, 0.0D), color);

        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        glEnable(GL11.GL_LIGHTING);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        GlStateManager.resetColor();
        GL11.glPopMatrix();
    }

    public static void draw2D(Entity entity, float yaw, int color) {
        float ticks = Wrapper.MC.getRenderPartialTicks();
        double renderPosX = Wrapper.MC.getRenderManager().viewerPosX;
        double renderPosY = Wrapper.MC.getRenderManager().viewerPosY;
        double renderPosZ = Wrapper.MC.getRenderManager().viewerPosZ;
        double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ticks) - renderPosX;
        double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ticks) - renderPosY;
        double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ticks) - renderPosZ;

        float playerViewY = Wrapper.MC.getRenderManager().playerViewY;
        float playerViewX = Wrapper.MC.getRenderManager().playerViewX;
        boolean thirdPersonView = Wrapper.MC.getRenderManager().options.thirdPersonView == 2;

        AxisAlignedBB box = entity.getEntityBoundingBox();
        GL11.glPushMatrix();

		GlStateManager.translate(xPos, yPos, zPos);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (thirdPersonView ? -0.1 : 0.1) * playerViewX, 0.0F, 1.0F, 0.0F);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL11.GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glLineWidth(1.0F);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        ColorUtil.glColor(color);
        drawSelectionBoundingBox(
                new AxisAlignedBB(-(entity.width), 0.0D, 0.0D, entity.width, entity.height + 0.1, 0.0D));
  //      drawSelectionBoundingBox(new AxisAlignedBB(box.minX - entity.posX + xPos, box.minY - entity.posY + yPos,
  //             box.minZ - entity.posZ + zPos, box.maxX - entity.posX + xPos, box.maxY - entity.posY - yPos,
  //              box.maxZ - entity.posZ + zPos));
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL11.GL_LIGHTING);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        GlStateManager.resetColor();
        GL11.glPopMatrix();
    }
}
