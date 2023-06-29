package ru.wendoxd.utils.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import ru.wendoxd.utils.visual.shaders.ShaderShell;

import javax.vecmath.Vector3d;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class RenderUtils {

	private static final Minecraft mc = Minecraft.getMinecraft();
	private final static Frustum frustum = new Frustum();

	public static void drawEntityBox(Entity entity, int red, float green, float blue, float alpha) {
		double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks
				- mc.getRenderManager().renderPosX;
		double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks
				- mc.getRenderManager().renderPosY;
		double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks
				- mc.getRenderManager().renderPosZ;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox();
		AxisAlignedBB aabb2 = new AxisAlignedBB(axisAlignedBB.minX - entity.posX + x - 0.05,
				axisAlignedBB.minY - entity.posY + y, axisAlignedBB.minZ - entity.posZ + z - 0.05,
				axisAlignedBB.maxX - entity.posX + x + 0.05, axisAlignedBB.maxY - entity.posY + y + 0.15,
				axisAlignedBB.maxZ - entity.posZ + z + 0.05);
		GlStateManager.color(red / 255F, green / 255F, blue / 255F, alpha / 255F);
		drawColorBox(aabb2);
		GlStateManager.color(0, 0, 0, 0.50F);
		drawSelectionBoundingBox(aabb2);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawBlockBox(BlockPos blockPos, int red, float green, float blue, float alpha) {
		double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * mc.timer.renderPartialTicks;
		double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * mc.timer.renderPartialTicks;
		double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * mc.timer.renderPartialTicks;
		mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 2);
		GL11.glPushMatrix();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(2);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GlStateManager.color(red / 255F, green / 255F, blue / 255F, alpha / 255F);
		drawSelectionBoundingBox(mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos)
				.expandXyz(0.0020000000949949026D).offset(-x, -y, -z));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawSelectionBoundingBox(AxisAlignedBB axisalignedbb) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		tessellator.draw();
		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		tessellator.draw();
		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		tessellator.draw();
		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		tessellator.draw();
		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		tessellator.draw();
		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		tessellator.draw();
		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		tessellator.draw();

		GL11.glPointSize(2);
		buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		tessellator.draw();
		GL11.glPointSize(2);
		buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		tessellator.draw();
		GL11.glPointSize(2);
		buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		tessellator.draw();
		GL11.glPointSize(2);
		buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		tessellator.draw();
		GL11.glPointSize(2);
		buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		tessellator.draw();
		GL11.glPointSize(2);
		buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawColorBox(AxisAlignedBB axisalignedbb) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		tessellator.draw();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		tessellator.draw();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		tessellator.draw();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		tessellator.draw();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		tessellator.draw();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
		buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawRect(float xStart, float yStart, float xEnd, float yEnd, int color) {
		float alpha = ((float) (color >> 24 & 0xFF) / 255F);
		float red = (float) (color >> 16 & 0xFF) / 255F;
		float green = (float) (color >> 8 & 0xFF) / 255F;
		float blue = (float) (color & 0xFF) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glPushMatrix();
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(xEnd, yStart);
		GL11.glVertex2d(xStart, yStart);
		GL11.glVertex2d(xStart, yEnd);
		GL11.glVertex2d(xEnd, yEnd);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GlStateManager.resetColor();
		GlStateManager.color(1, 1, 1, 1);
		GL11.glColor4f(1, 1, 1, 1);
	}

	public static void drawRect(double xStart, double yStart, double xEnd, double yEnd, int color) {
		drawRect((float) xStart, (float) yStart, (float) xEnd, (float) yEnd, color);
	}

	public static void drawRoundedRect(float startX, float startY, float endX, float endY, int color, float radius) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
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
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(endX, startY);
		GL11.glVertex2d(startX, startY);
		GL11.glVertex2d(startX, endY);
		GL11.glVertex2d(endX, endY);
		GL11.glEnd();
		ShaderShell.ROUNDED_RECT.detach();
		GL11.glPopMatrix();
	}

	public static Vector3d vectorTo2D(int scaleFactor, double x, double y, double z) {
		float xPos = (float) x;
		float yPos = (float) y;
		float zPos = (float) z;
		IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
		FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
		FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
		FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
		if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector))
			return new Vector3d((vector.get(0) / scaleFactor), ((Display.getHeight() - vector.get(1)) / scaleFactor),
					vector.get(2));
		return null;
	}

	public static int rgba(int r, int g, int b, int a) {
		return a << 24 | r << 16 | g << 8 | b;
	}

	public static double absSinAnimation(double input) {
		return Math.abs(1 + Math.sin(input)) / 2;
	}

	public static int rgba(double r, double g, double b, double a) {
		return rgba((int) r, (int) g, (int) b, (int) a);
	}

	public static void drawCircle(float x, float y, float width, float radius, float glow, int color) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		float alpha = ((float) (color >> 24 & 0xFF) / 255F);
		float red = (float) (color >> 16 & 0xFF) / 255F;
		float green = (float) (color >> 8 & 0xFF) / 255F;
		float blue = (float) (color & 0xFF) / 255F;
		ShaderShell.CIRCLE_SHADER.attach();
		ShaderShell.CIRCLE_SHADER.set4F("color", red, green, blue, alpha);
		ShaderShell.CIRCLE_SHADER.set2F("resolution", Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight);
		ShaderShell.CIRCLE_SHADER.set2F("start", x * 2, y * 2);
		ShaderShell.CIRCLE_SHADER.set2F("end", x * 2 + width * 2, y * 2 + width * 2);
		ShaderShell.CIRCLE_SHADER.set1F("radius", radius);
		ShaderShell.CIRCLE_SHADER.set1F("glow", glow);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x + width, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y + width);
		GL11.glVertex2d(x + width, y + width);
		GL11.glEnd();
		ShaderShell.CIRCLE_SHADER.detach();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void scissorRect(float x, float y, float width, double height) {
		ScaledResolution sr = new ScaledResolution(mc);
		int factor = sr.getScaleFactor();
		GL11.glScissor((int) (x * (float) factor), (int) (((float) sr.getScaledHeight() - height) * (float) factor),
				(int) ((width - x) * (float) factor), (int) ((height - y) * (float) factor));
	}

	public static void drawArrow(float x, float y, boolean up, int color, float scale) {
		float alpha = ((float) (color >> 24 & 0xFF) / 255F);
		float red = (float) (color >> 16 & 0xFF) / 255F;
		float green = (float) (color >> 8 & 0xFF) / 255F;
		float blue = (float) (color & 0xFF) / 255F;
		GL11.glPushMatrix();
		x /= scale;
		y /= scale;
		GL11.glScalef(scale, scale, 0);
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
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
		GL11.glPopMatrix();
	}

	public static void sizeAnimation(double width, double height, double animation) {
		GL11.glTranslated(width / 2, height / 2, 0);
		GL11.glScaled(animation, animation, 1);
		GL11.glTranslated(-width / 2, -height / 2, 0);
	}

	public static boolean isInViewFrustum(Entity entity) {
		return (isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck);
	}

	private static boolean isInViewFrustum(AxisAlignedBB bb) {
		Entity current = mc.getRenderViewEntity();
		if (current != null) {
			frustum.setPosition(current.posX, current.posY, current.posZ);
		}
		return frustum.isBoundingBoxInFrustum(bb);
	}
}
