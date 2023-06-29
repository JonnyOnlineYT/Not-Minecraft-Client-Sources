package volcano.summer.client.util;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import volcano.summer.base.Summer;

public class EntityUtils {

	public static EntityUtils y;

	public EntityUtils() {
		y = this;
	}

	public static float getPitch(Entity ent) {
		double x = ent.posX - Summer.mc.thePlayer.posX;
		double y = ent.posY - Summer.mc.thePlayer.posY;
		double z = ent.posZ - Summer.mc.thePlayer.posZ;
		y /= Summer.mc.thePlayer.getDistanceToEntity(ent);
		double pitch = Math.asin(y) * 57.29577951308232D;
		pitch = -pitch;
		return (float) pitch;
	}

	public static float getYaw(Entity ent) {
		double x = ent.posX - Summer.mc.thePlayer.posX;
		double y = ent.posY - Summer.mc.thePlayer.posY;
		double z = ent.posZ - Summer.mc.thePlayer.posZ;
		double yaw = Math.atan2(x, z) * 57.29577951308232D;
		yaw = -yaw;
		return (float) yaw;
	}

	public static void drawRect(final double left, final double top, final double right, final double bottom,
			final int color) {
		final float alpha = (color >> 24 & 0xFF) / 255.0f;
		final float red = (color >> 16 & 0xFF) / 255.0f;
		final float green = (color >> 8 & 0xFF) / 255.0f;
		final float blue = (color & 0xFF) / 255.0f;
		final Tessellator var9 = Tessellator.getInstance();
		final WorldRenderer var10 = var9.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		var10.startDrawingQuads();
		var10.addVertex(left, bottom, 0.0);
		var10.addVertex(right, bottom, 0.0);
		var10.addVertex(right, top, 0.0);
		var10.addVertex(left, top, 0.0);
		var9.draw();
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
	}

	public static void drawOutlinedBoundingBox(final AxisAlignedBB axisAlignedBB) {
		final WorldRenderer worldRenderer = Tessellator.instance.getWorldRenderer();
		final Tessellator instance = Tessellator.instance;
		worldRenderer.startDrawing(3);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		instance.draw();
		worldRenderer.startDrawing(3);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		instance.draw();
		worldRenderer.startDrawing(1);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		instance.draw();
	}

	public static void drawFilledBox(final AxisAlignedBB axisAlignedBB) {
		final WorldRenderer worldRenderer = Tessellator.instance.getWorldRenderer();
		final Tessellator instance = Tessellator.instance;
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		instance.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		instance.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		instance.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
		instance.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		instance.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		worldRenderer.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		instance.draw();
	}

	public static void drawBorderedRect(final double left, final double top, final double right, final double bottom,
			final int borderColor, final int color) {
		drawBorderedRect(left, top, right, bottom, 1.0f, borderColor, color);
	}

	public static void drawBorderedRect(final double left, final double top, final double right, final double bottom,
			final float borderWidth, final int borderColor, final int color) {
		final float alpha = (borderColor >> 24 & 0xFF) / 255.0f;
		final float red = (borderColor >> 16 & 0xFF) / 255.0f;
		final float green = (borderColor >> 8 & 0xFF) / 255.0f;
		final float blue = (borderColor & 0xFF) / 255.0f;
		GlStateManager.pushMatrix();
		drawRect(left, top, right, bottom, color);
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		if (borderWidth == 1.0f) {
			GL11.glEnable(2848);
		}
		GL11.glLineWidth(borderWidth);
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(1);
		worldRenderer.addVertex(left, top, 0.0);
		worldRenderer.addVertex(left, bottom, 0.0);
		worldRenderer.addVertex(right, bottom, 0.0);
		worldRenderer.addVertex(right, top, 0.0);
		worldRenderer.addVertex(left, top, 0.0);
		worldRenderer.addVertex(right, top, 0.0);
		worldRenderer.addVertex(left, bottom, 0.0);
		worldRenderer.addVertex(right, bottom, 0.0);
		tessellator.draw();
		GL11.glLineWidth(2.0f);
		if (borderWidth == 1.0f) {
			GL11.glDisable(2848);
		}
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static Block getBlock(double offset) {
		return getBlock(Summer.mc.thePlayer.getEntityBoundingBox().offset(0.0D, offset, 0.0D));
	}

	public static Block getBlock(AxisAlignedBB bb) {
		int y = (int) bb.minY;
		for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; z++) {
				Block block = Summer.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null) {
					return block;
				}
			}
		}
		return null;
	}

	public static float[] getBlockRotations(int x, int y, int z, EnumFacing facing) {
		final Entity temp = new EntitySnowball(Summer.mc.theWorld);
		temp.posX = x + 0.5;
		temp.posY = y + 0.5;
		temp.posZ = z + 0.5;
		Entity entity = temp;
		entity.posX += facing.getDirectionVec().getX() * 0.25;
		entity.posY += facing.getDirectionVec().getY() * 0.25;
		entity.posZ += facing.getDirectionVec().getZ() * 0.25;
		return getAngles(temp);
	}

	public static float[] getAngles(Entity e) {
		return new float[] { getYawChangeToEntity(e) + Summer.mc.thePlayer.rotationYaw,
				getPitchChangeToEntity(e) + Summer.mc.thePlayer.rotationPitch };
	}

	public static float getYawChangeToEntity(Entity entity) {
		Minecraft mc = Minecraft.getMinecraft();
		double deltaX = entity.posX - mc.thePlayer.posX;
		double deltaZ = entity.posZ - mc.thePlayer.posZ;
		double yawToEntity;
		if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
			yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else {
			if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
				yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
			} else {
				yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
			}
		}

		return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yawToEntity));
	}

	public static float getPitchChangeToEntity(Entity entity) {
		Minecraft mc = Minecraft.getMinecraft();
		double deltaX = entity.posX - mc.thePlayer.posX;
		double deltaZ = entity.posZ - mc.thePlayer.posZ;
		double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - 0.4 - mc.thePlayer.posY;
		double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);

		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));

		return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitchToEntity);
	}

	public static void pre() {
		GL11.glDisable(2929);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
	}

	public static void post() {
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glColor3d(1.0, 1.0, 1.0);
	}

	public static double interpolate(final double newPos, final double oldPos) {
		return oldPos + (newPos - oldPos) * Summer.mc.timer.renderPartialTicks;
	}

	public static float[] getRotations(Entity ent) {
		double x = ent.posX;
		double z = ent.posZ;
		double y = ent.posY + ent.getEyeHeight() / 5.0F;
		return getRotationFromPosition(x, z, y);
	}

	public static float[] getRotationsAura(Entity ent) {

		double x = ent.posX;

		double z = ent.posZ;

		double y = ent.posY + ent.getEyeHeight() / 4.0F;

		return getRotationFromPositionaura(x, z, y);

	}

	public static float[] getRotationFromPositionaura(double x, double z, double y) {

		double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;

		double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;

		double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 0.6D;

		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);

		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;

		float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);

		return new float[] { yaw, pitch };

	}

	public static float[] getRotationsBlock(BlockPos pos) {
		Minecraft mc = Minecraft.getMinecraft();
		Entity p = mc.thePlayer;
		double d0 = pos.getX() - mc.thePlayer.posX;
		double d1 = pos.getY() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
		double d2 = pos.getZ() - mc.thePlayer.posZ;
		double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		float f = (float) (MathHelper.func_181159_b(d2, d0) * 180.0D / Math.PI) - 90.0F;
		float f1 = (float) (-(MathHelper.func_181159_b(d1, d3) * 180.0D / Math.PI));
		return new float[] { f, f1 };
	}

	public static float[] getRotationFromPosition(double x, double z, double y) {
		double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
		double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
		double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 0.6D;
		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
		return new float[] { yaw, pitch };
	}

	public static void circleOutline(double x, double y, double radius, int color) {
		float red = (color >> 24 & 0xFF) / 255.0f;
		float green = (color >> 16 & 0xFF) / 255.0f;
		float blue = (color >> 8 & 0xFF) / 255.0f;
		float alpha = (color & 0xFF) / 255.0f;
		Tessellator tessellator = Tessellator.getInstance();
		GlStateManager.pushMatrix();
		GlStateManager.func_179090_x();
		GlStateManager.enableBlend();
		GlStateManager.color(green, blue, alpha, red);
		GL11.glEnable(2848);
		GL11.glEnable(2881);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
		GL11.glBegin(3);
		for (int i = 0; i <= 360; ++i) {
			final double x2 = Math.sin(i * 3.141592653589793 / 180.0) * radius;
			final double y2 = Math.cos(i * 3.141592653589793 / 180.0) * radius;
			GL11.glVertex2d(x + x2, y + y2);
		}
		GL11.glEnd();
		GL11.glDisable(2848);
		GL11.glDisable(2881);
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int outer, int inner) {
		drawRect(x, y, x2, y2, inner);
		float f = (outer >> 24 & 0xFF) / 255.0F;
		float f1 = (outer >> 16 & 0xFF) / 255.0F;
		float f2 = (outer >> 8 & 0xFF) / 255.0F;
		float f3 = (outer & 0xFF) / 255.0F;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(1);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glColor4f(0.0F, 255.0F, 0.0F, 0.0F);
	}

	public static void drawRect(float g, float h, float i, float j, int col1) {
		float f = (col1 >> 24 & 0xFF) / 255.0F;
		float f1 = (col1 >> 16 & 0xFF) / 255.0F;
		float f2 = (col1 >> 8 & 0xFF) / 255.0F;
		float f3 = (col1 & 0xFF) / 255.0F;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(7);
		GL11.glVertex2d(i, h);
		GL11.glVertex2d(g, h);
		GL11.glVertex2d(g, j);
		GL11.glVertex2d(i, j);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static int drawString(String text, int x, int y, int color) {
		return Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, color);
	}

	public static void setColor(Color c) {
		GL11.glColor4f(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, c.getAlpha() / 255.0F);
	}

	public static void drawCenteredString(String text, int x, int y, int color) {
		Minecraft.getMinecraft().fontRendererObj.drawString(text,
				x - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2, y, color);
	}

	public static int getRainbow(int speed, int offset) {
		float hue = (System.currentTimeMillis() + offset) % speed;
		hue /= speed;
		return Color.getHSBColor(hue, 1.0F, 1.0F).getRGB();
	}

	public static void drawBetterBorderedRect(int x, float y, int x1, int y1, float size, int borderC, int insideC) {
		drawRect(x, y, x1, y1, insideC);
		drawRect(x, y, x1, y + size, borderC);
		drawRect(x, y1, x1, y1 + size, borderC);
		drawRect(x1, y, x1 + size, y1 + size, borderC);
		drawRect(x, y, x + size, y1 + size, borderC);
	}

	private static float getItemDamage(ItemStack itemStack) {
		Multimap multimap = itemStack.getAttributeModifiers();
		if (!multimap.isEmpty()) {
			Iterator iterator = multimap.entries().iterator();
			if (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
				double damage;
				if (attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2) {
					damage = attributeModifier.getAmount();
				} else {
					damage = attributeModifier.getAmount() * 100.0D;
				}

				if (attributeModifier.getAmount() > 1.0D) {
					return 1.0F + (float) damage;
				}

				return 1.0F;
			}
		}

		return 1.0F;
	}

	public static int getBestWeapon(Entity target) {
		int originalSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
		byte weaponSlot = -1;
		float weaponDamage = 1.0F;

		for (byte slot = 0; slot < 9; ++slot) {
			Minecraft.getMinecraft().thePlayer.inventory.currentItem = slot;
			ItemStack itemStack = Minecraft.getMinecraft().thePlayer.getHeldItem();
			if (itemStack != null) {
				float damage = getItemDamage(itemStack);
				damage += EnchantmentHelper.getEnchantmentModifierLiving(Minecraft.getMinecraft().thePlayer,
						(EntityLivingBase) target);
				if (damage > weaponDamage) {
					weaponDamage = damage;
					weaponSlot = slot;
				}
			}
		}

		if (weaponSlot != -1) {
			return weaponSlot;
		} else {
			return originalSlot;
		}
	}

	public static Block getBlock(BlockPos pos) {
		return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
	}

	public static void blockESPBox(BlockPos blockPos) {
		double x = blockPos.getX();
		double y = blockPos.getY();
		double z = blockPos.getZ();
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glColor4d(0, 1, 0, 0.15F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		if (getBlock(blockPos).equals(Blocks.air)) {
			GL11.glColor4d(0, 1, 0, 0.5F);
		} else {
			GL11.glColor4d(1, 0, 0, 0.5F);
		}

		RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}
}
