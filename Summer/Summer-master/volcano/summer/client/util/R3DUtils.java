package volcano.summer.client.util;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.friend.FriendManager;
import volcano.summer.client.modules.render.PlayerESP;

public class R3DUtils {
	public static void startDrawing() {
		GL11.glEnable(3042);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		Summer.mc.entityRenderer.setupCameraTransform(Summer.mc.timer.renderPartialTicks, 0);
	}

	public static void stopDrawing() {
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
	}

	public static void drawSphere(double red, double green, double blue, double alpha, double x, double y, double z,
			float size, int slices, int stacks, float lWidth) {
		Sphere sphere = new Sphere();

		enableDefaults();
		GL11.glColor4d(red, green, blue, alpha);
		GL11.glTranslated(x, y, z);
		GL11.glLineWidth(lWidth);
		sphere.setDrawStyle(100013);
		sphere.draw(size, slices, stacks);
		disableDefaults();
	}

	public static void enableDefaults() {
		Summer.mc.entityRenderer.func_175072_h();
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glDisable(2896);

		GL11.glDepthMask(false);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
	}

	public static void disableDefaults() {
		GL11.glPopMatrix();
		GL11.glDisable(2848);
		GL11.glDepthMask(true);

		GL11.glEnable(3553);
		GL11.glEnable(2896);
		GL11.glDisable(3042);
		Summer.mc.entityRenderer.func_180436_i();
	}

	public static void rectangle(double left, double top, double right, double bottom, int color) {
		if (left < right) {
			double var5 = left;
			left = right;
			right = var5;
		}
		if (top < bottom) {
			double var5 = top;
			top = bottom;
			bottom = var5;
		}
		float var11 = (color >> 24 & 0xFF) / 255.0F;
		float var6 = (color >> 16 & 0xFF) / 255.0F;
		float var7 = (color >> 8 & 0xFF) / 255.0F;
		float var8 = (color & 0xFF) / 255.0F;
		WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(var6, var7, var8, var11);
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(left, bottom, 0.0D);
		worldRenderer.addVertex(right, bottom, 0.0D);
		worldRenderer.addVertex(right, top, 0.0D);
		worldRenderer.addVertex(left, top, 0.0D);
		Tessellator.getInstance().draw();
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
	}

	public static void drawBoundingBox(AxisAlignedBB aabb) {
		WorldRenderer worldRenderer = Tessellator.instance.getWorldRenderer();
		Tessellator tessellator = Tessellator.instance;
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		tessellator.draw();
	}

	public static void drawOutlinedBoundingBox(AxisAlignedBB mask) {
		WorldRenderer var2 = Tessellator.instance.getWorldRenderer();
		Tessellator var1 = Tessellator.instance;
		var2.startDrawing(3);
		var2.addVertex(mask.minX, mask.minY, mask.minZ);
		var2.addVertex(mask.maxX, mask.minY, mask.minZ);
		var2.addVertex(mask.maxX, mask.minY, mask.maxZ);
		var2.addVertex(mask.minX, mask.minY, mask.maxZ);
		var2.addVertex(mask.minX, mask.minY, mask.minZ);
		var1.draw();
		var2.startDrawing(3);
		var2.addVertex(mask.minX, mask.maxY, mask.minZ);
		var2.addVertex(mask.maxX, mask.maxY, mask.minZ);
		var2.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		var2.addVertex(mask.minX, mask.maxY, mask.maxZ);
		var2.addVertex(mask.minX, mask.maxY, mask.minZ);
		var1.draw();
		var2.startDrawing(1);
		var2.addVertex(mask.minX, mask.minY, mask.minZ);
		var2.addVertex(mask.minX, mask.maxY, mask.minZ);
		var2.addVertex(mask.maxX, mask.minY, mask.minZ);
		var2.addVertex(mask.maxX, mask.maxY, mask.minZ);
		var2.addVertex(mask.maxX, mask.minY, mask.maxZ);
		var2.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		var2.addVertex(mask.minX, mask.minY, mask.maxZ);
		var2.addVertex(mask.minX, mask.maxY, mask.maxZ);
		var1.draw();
	}

	public static void FilledESP(AxisAlignedBB aa) {
		Tessellator ts = Tessellator.getInstance();
		WorldRenderer wr = ts.getWorldRenderer();

		wr.startDrawingQuads();
		wr.addVertex(aa.minX, aa.minY, aa.minZ);
		wr.addVertex(aa.minX, aa.maxY, aa.minZ);
		wr.addVertex(aa.maxX, aa.minY, aa.minZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		wr.addVertex(aa.maxX, aa.minY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.minX, aa.minY, aa.maxZ);
		wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
		ts.draw();
		wr.startDrawingQuads();
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		wr.addVertex(aa.maxX, aa.minY, aa.minZ);
		wr.addVertex(aa.minX, aa.maxY, aa.minZ);
		wr.addVertex(aa.minX, aa.minY, aa.minZ);
		wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.minX, aa.minY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.minY, aa.maxZ);
		ts.draw();
		wr.startDrawingQuads();
		wr.addVertex(aa.minX, aa.maxY, aa.minZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.minX, aa.maxY, aa.minZ);
		wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		ts.draw();
		wr.startDrawingQuads();
		wr.addVertex(aa.minX, aa.minY, aa.minZ);
		wr.addVertex(aa.maxX, aa.minY, aa.minZ);
		wr.addVertex(aa.maxX, aa.minY, aa.maxZ);
		wr.addVertex(aa.minX, aa.minY, aa.maxZ);
		wr.addVertex(aa.minX, aa.minY, aa.minZ);
		wr.addVertex(aa.minX, aa.minY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.minY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.minY, aa.minZ);
		ts.draw();
		wr.startDrawingQuads();
		wr.addVertex(aa.minX, aa.minY, aa.minZ);
		wr.addVertex(aa.minX, aa.maxY, aa.minZ);
		wr.addVertex(aa.minX, aa.minY, aa.maxZ);
		wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.minY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.minY, aa.minZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		ts.draw();
		wr.startDrawingQuads();
		wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.minX, aa.minY, aa.maxZ);
		wr.addVertex(aa.minX, aa.maxY, aa.minZ);
		wr.addVertex(aa.minX, aa.minY, aa.minZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		wr.addVertex(aa.maxX, aa.minY, aa.minZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.minY, aa.maxZ);
		ts.draw();
	}

	public static void setColor(final Color c) {
		GL11.glColor4d(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f);
	}

	public static void drawOutlinedBox(AxisAlignedBB box) {
		if (box == null) {
			return;
		}
		GL11.glBegin(3);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glEnd();
		GL11.glBegin(3);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glEnd();
		GL11.glBegin(1);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glEnd();
	}

	public static void renderCrosses(AxisAlignedBB box) {
		GL11.glBegin(1);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);

		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);

		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);

		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glEnd();
	}

	public static void drawBox(AxisAlignedBB box) {
		if (box == null) {
			return;
		}
		GL11.glBegin(7);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glEnd();
		GL11.glBegin(7);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glEnd();

		GL11.glBegin(7);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glEnd();
		GL11.glBegin(7);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glEnd();

		GL11.glBegin(7);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glEnd();
		GL11.glBegin(7);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
		GL11.glEnd();

		GL11.glBegin(7);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glEnd();
		GL11.glBegin(7);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
		GL11.glEnd();

		GL11.glBegin(7);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glEnd();

		GL11.glBegin(7);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glEnd();

		GL11.glBegin(7);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glEnd();

		GL11.glBegin(7);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glEnd();
	}

	public static void drawFilledBox(AxisAlignedBB mask) {
		WorldRenderer worldRenderer = Tessellator.instance.getWorldRenderer();
		Tessellator tessellator = Tessellator.instance;
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
		worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
		worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
		worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
		tessellator.draw();
	}

	public static void drawChestESP(BlockPos blockPos, boolean echest) {
		double d = blockPos.getX() - Minecraft.getMinecraft().getRenderManager().renderPosX;
		double d1 = blockPos.getY() - Minecraft.getMinecraft().getRenderManager().renderPosY;
		double d2 = blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		if (echest) {
			GL11.glColor4d(0.5D, 0.0D, 1.0D, 0.10010000318288803D);
		} else {
			GL11.glColor4d(1.0D, 0.5D, 0.0D, 0.10010000318288803D);
		}
		FilledESP(new AxisAlignedBB(d + 0.06D, d1, d2 + 0.06D, d + 0.94D, d1 + 0.88D, d2 + 0.94D));
		if (echest) {
			GL11.glColor4d(0.5D, 0.0D, 1.0D, 1.0D);
		} else {
			GL11.glColor4d(1.0D, 0.5D, 0.0D, 1.0D);
		}
		GL11.glLineWidth(1.0F);
		ProtocolEntityBox(new AxisAlignedBB(d + 0.06D, d1, d2 + 0.06D, d + 0.94D, d1 + 0.88D, d2 + 0.94D));
		GL11.glDisable(2848);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}

	public static void ProtocolEntityBox(AxisAlignedBB aa) {
		Tessellator ts = Tessellator.getInstance();
		WorldRenderer wr = ts.getWorldRenderer();
		wr.startDrawing(3);
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		wr.addVertex(aa.minX, aa.maxY, aa.minZ);
		wr.addVertex(aa.minX, aa.minY, aa.minZ);
		wr.addVertex(aa.maxX, aa.minY, aa.minZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.minX, aa.minY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.minY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.maxX, aa.minY, aa.minZ);
		wr.addVertex(aa.maxX, aa.minY, aa.maxZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.minX, aa.minY, aa.minZ);
		wr.addVertex(aa.minX, aa.minY, aa.maxZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.minX, aa.maxY, aa.minZ);
		wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.minX, aa.minY, aa.minZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.minY, aa.minZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.maxX, aa.minY, aa.minZ);
		wr.addVertex(aa.minX, aa.minY, aa.maxZ);
		ts.draw();
	}

	public static ScaledResolution getScaledRes() {
		ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		return scaledRes;
	}

	public static int getScaledHeight() {
		return getScaledRes().getScaledHeight();
	}

	public static int getScaledWidth() {
		return getScaledRes().getScaledWidth();
	}

	public static void drawLines(AxisAlignedBB bb) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(2);
		worldRenderer.addVertex(bb.minX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawing(2);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawing(2);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.maxZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawing(2);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.minZ);
		tessellator.draw();
		worldRenderer.startDrawing(2);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.minY, bb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawing(2);
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
		tessellator.draw();
	}

	public static void RenderLivingEntityBox(Entity entity, float partialTicks, boolean otherMode) {
		GlStateManager.pushMatrix();
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(1.5F);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glDisable(2896);
		GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
		double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
				- RenderManager.renderPosX;
		double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
				- RenderManager.renderPosY;
		double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
				- RenderManager.renderPosZ;
		boolean isPlayer = entity instanceof EntityPlayer;
		int bordercolor = 15261919;
		if ((entity != Minecraft.thePlayer) && ((entity instanceof EntityPlayer))
				&& (!((EntityPlayer) entity).isInvisible())) {
			if (FriendManager.isFriend(entity.getName())) {
				bordercolor = -11184641;
			} else if (PlayerESP.rainbow.getValue()) {
				bordercolor = PlayerESP.rainbow(2000000L, 1.0F).getRGB();
			}
		}
		Colors.glColor(bordercolor, 1.0F);
		if (otherMode) {
			RenderGlobal.drawOutlinedBoundingBox(
					new AxisAlignedBB(entity.boundingBox.minX - (isPlayer ? 0.12D : 0.0D) - entity.posX + posX,
							entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2D : entity.posY) + posY,
							entity.boundingBox.minZ - (isPlayer ? 0.12D : 0.0D) - entity.posZ + posZ,
							entity.boundingBox.maxX + (isPlayer ? 0.12D : 0.0D) - entity.posX + posX,
							entity.boundingBox.maxY + (isPlayer ? 0.2D : 0.0D)
									- (entity.isSneaking() ? entity.posY + 0.2D : entity.posY) + posY,
							entity.boundingBox.maxZ + (isPlayer ? 0.12D : 0.0D) - entity.posZ + posZ),
					-1);
			drawLines(new AxisAlignedBB(entity.boundingBox.minX - (isPlayer ? 0.12D : 0.0D) - entity.posX + posX,
					entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2D : entity.posY) + posY,
					entity.boundingBox.minZ - (isPlayer ? 0.12D : 0.0D) - entity.posZ + posZ,
					entity.boundingBox.maxX + (isPlayer ? 0.12D : 0.0D) - entity.posX + posX,
					entity.boundingBox.maxY + (isPlayer ? 0.2D : 0.0D)
							- (entity.isSneaking() ? entity.posY + 0.2D : entity.posY) + posY,
					entity.boundingBox.maxZ + (isPlayer ? 0.12D : 0.0D) - entity.posZ + posZ));
		} else {
			drawOutlinedBox(new AxisAlignedBB(entity.boundingBox.minX - (isPlayer ? 0.12D : 0.0D) - entity.posX + posX,
					entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2D : entity.posY) + posY,
					entity.boundingBox.minZ - (isPlayer ? 0.12D : 0.0D) - entity.posZ + posZ,
					entity.boundingBox.maxX + (isPlayer ? 0.12D : 0.0D) - entity.posX + posX,
					entity.boundingBox.maxY + (isPlayer ? 0.2D : 0.0D)
							- (entity.isSneaking() ? entity.posY + 0.2D : entity.posY) + posY,
					entity.boundingBox.maxZ + (isPlayer ? 0.12D : 0.0D) - entity.posZ + posZ));
			Colors.glColor(bordercolor, 0.15F);
			drawBoundingBox(new AxisAlignedBB(entity.boundingBox.minX - (isPlayer ? 0.12D : 0.0D) - entity.posX + posX,
					entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2D : entity.posY) + posY,
					entity.boundingBox.minZ - (isPlayer ? 0.12D : 0.0D) - entity.posZ + posZ,
					entity.boundingBox.maxX + (isPlayer ? 0.12D : 0.0D) - entity.posX + posX,
					entity.boundingBox.maxY + (isPlayer ? 0.2D : 0.0D)
							- (entity.isSneaking() ? entity.posY + 0.2D : entity.posY) + posY,
					entity.boundingBox.maxZ + (isPlayer ? 0.12D : 0.0D) - entity.posZ + posZ));
		}
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GlStateManager.popMatrix();
	}
}