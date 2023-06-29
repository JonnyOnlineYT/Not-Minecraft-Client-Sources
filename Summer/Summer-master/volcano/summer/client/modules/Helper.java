package volcano.summer.client.modules;

import java.awt.Color;
import java.util.ArrayList;

import javax.vecmath.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class Helper {

	public static int windowWidth() {
		return new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight).getScaledWidth();
	}

	public static int windowHeight() {
		return new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight).getScaledHeight();
	}

	public static void drawBordered(double x, double y, double width, double height, double length, int innerColor,
			int outerColor) {
		Gui.drawRect(x, y, x + width, y + height, innerColor);
		Gui.drawRect(x - length, y, x, y + height, outerColor);
		Gui.drawRect(x - length, y - length, x + width, y, outerColor);
		Gui.drawRect(x + width, y - length, x + width + length, y + height + length, outerColor);
		Gui.drawRect(x - length, y + height, x + width, y + height + length, outerColor);
	}

	public static int withTransparency(int rgb, float alpha) {
		float r = (rgb >> 16 & 0xFF) / 255.0F;
		float g = (rgb >> 8 & 0xFF) / 255.0F;
		float b = (rgb >> 0 & 0xFF) / 255.0F;
		return new Color(r, g, b, alpha).getRGB();
	}

	public static ArrayList<Vector3f> vanillaTeleportPositions(double tpX, double tpY, double tpZ, double speed) {
		ArrayList<Vector3f> positions = new ArrayList();
		Minecraft mc = Minecraft.getMinecraft();
		double posX = tpX - mc.thePlayer.posX;
		double posY = tpY - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight() + 1.1D);
		double posZ = tpZ - mc.thePlayer.posZ;
		float yaw = (float) (Math.atan2(posZ, posX) * 180.0D / 3.141592653589793D - 90.0D);
		float pitch = (float) (-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0D / 3.141592653589793D);
		double tmpX = mc.thePlayer.posX;
		double tmpY = mc.thePlayer.posY;
		double tmpZ = mc.thePlayer.posZ;
		double steps = 1.0D;
		for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY,
				tpZ); d += speed) {
			steps += 1.0D;
		}
		for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY,
				tpZ); d += speed) {
			tmpX = mc.thePlayer.posX - Math.sin(getDirection(yaw)) * d;
			tmpZ = mc.thePlayer.posZ + Math.cos(getDirection(yaw)) * d;
			tmpY -= (mc.thePlayer.posY - tpY) / steps;
			positions.add(new Vector3f((float) tmpX, (float) tmpY, (float) tmpZ));
		}
		positions.add(new Vector3f((float) tpX, (float) tpY, (float) tpZ));

		return positions;
	}

	public static float[] getRotations(Entity entity) {
		double pX = Minecraft.getMinecraft().thePlayer.posX;
		double pY = Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
		double pZ = Minecraft.getMinecraft().thePlayer.posZ;
		double eX = entity.posX;
		double eY = entity.posY + entity.height / 2.0F;
		double eZ = entity.posZ;
		double dX = pX - eX;
		double dY = pY - eY;
		double dZ = pZ - eZ;
		double dH = Math.sqrt(Math.pow(dX, 2.0D) + Math.pow(dZ, 2.0D));
		double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0D;
		double pitch = Math.toDegrees(Math.atan2(dH, dY));
		return new float[] { (float) yaw, (float) (90.0D - pitch) };
	}

	public static float getDirection(float yaw) {
		if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0F) {
			yaw += 180.0F;
		}
		float forward = 1.0F;
		if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0F) {
			forward = -0.5F;
		} else if (Minecraft.getMinecraft().thePlayer.moveForward > 0.0F) {
			forward = 0.5F;
		}
		if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0.0F) {
			yaw -= 90.0F * forward;
		}
		if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0.0F) {
			yaw += 90.0F * forward;
		}
		yaw *= 0.017453292F;

		return yaw;
	}

	public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		double d0 = x1 - x2;
		double d1 = y1 - y2;
		double d2 = z1 - z2;
		return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
	}
}
