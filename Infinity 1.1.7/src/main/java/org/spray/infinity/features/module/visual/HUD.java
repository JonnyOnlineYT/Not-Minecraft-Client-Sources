package org.spray.infinity.features.module.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.font.IFont;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.mixin.IMinecraftClient;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MathAssist;
import org.spray.infinity.utils.entity.EntityUtil;
import org.spray.infinity.utils.render.Drawable;
import org.spray.infinity.utils.render.RenderUtil;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

@ModuleInfo(category = Category.VISUAL, desc = "Ingame Infinity Hud", key = -2, name = "HUD", visible = true)
public class HUD extends Module {

	public Setting watermark = new Setting(this, "WaterMark", true);
	private Setting name = new Setting(this, "Profile Name", true).setVisible(() -> watermark.isToggle());
	private Setting fps = new Setting(this, "FPS", true).setVisible(() -> watermark.isToggle());
	private Setting ping = new Setting(this, "Ping", true).setVisible(() -> watermark.isToggle());

	// Array List
	private Setting array = new Setting(this, "Arraylist", true);
	private Setting listPosition = new Setting(this, "List Position", "Top",
			new ArrayList<>(Arrays.asList("Top", "Below"))).setVisible(() -> array.isToggle());
	private Setting transparencyBG = new Setting(this, "Transparency", 200D, 0D, 255D)
			.setVisible(() -> array.isToggle());
	private Setting offsetY = new Setting(this, "Offset Y", 1.7D, 0D, 5D).setVisible(() -> array.isToggle());
	private Setting colorMode = new Setting(this, "Color Mode", "Pulse",
			new ArrayList<>(Arrays.asList("Rainbow", "Pulse", "Custom"))).setVisible(() -> array.isToggle());

	private Setting gradient = new Setting(this, "Gradient", 140D, 0D, 300D)
			.setVisible(() -> array.isToggle() && colorMode.getCurrentMode().equalsIgnoreCase("Rainbow")
					|| array.isToggle() && colorMode.getCurrentMode().equalsIgnoreCase("Pulse"));
	private Setting rainbowDelay = new Setting(this, "Rainbow Delay", 14D, 1D, 30D)
			.setVisible(() -> array.isToggle() && colorMode.getCurrentMode().equalsIgnoreCase("Rainbow"));

	private Setting arrayColor = new Setting(this, "Array Color", new Color(44, 112, 160))
			.setVisible(() -> array.isToggle() && colorMode.getCurrentMode().equalsIgnoreCase("Custom")
					|| colorMode.getCurrentMode().equalsIgnoreCase("Pulse"));

	private Setting coordinates = new Setting(this, "Coordinates", true);

	private Setting netherCoords = new Setting(this, "Nether Coordinates", false);

	@Override
	public void onRender(MatrixStack matrices, float tick, int width, int height) {
		if (watermark.isToggle())
			markRender(matrices, tick, width, height);

		if (array.isToggle())
			listRender(matrices, tick, width, height);

		boolean belowPosition = array.isToggle() && listPosition.getCurrentMode().equalsIgnoreCase("Below");

		double playerX = MathAssist.round(Helper.getPlayer().getX(), 1);
		double playerY = MathAssist.round(Helper.getPlayer().getY(), 1);
		double playerZ = MathAssist.round(Helper.getPlayer().getZ(), 1);

		if (coordinates.isToggle()) {

			String coords = Formatting.BLUE + "x" + Formatting.WHITE + ": " + playerX + Formatting.BLUE + " y"
					+ Formatting.WHITE + ": " + playerY + Formatting.BLUE + " z" + Formatting.WHITE + ": " + playerZ;

			double countY = Helper.MC.currentScreen instanceof ChatScreen ? 23 : 11;
			double posY = belowPosition ? 1 : height - countY;
			double posX = width - IFont.legacy15.getWidthIgnoreChar(coords);

			IFont.legacy15.drawStringWithShadow(matrices, coords, posX + 44, posY, 0xFFFFFFFF);
		}

		if (netherCoords.isToggle()) {
			playerX = MathAssist.round(playerX / 8, 1);
			playerY = MathAssist.round(playerY / 8, 1);
			playerZ = MathAssist.round(playerZ / 8, 1);

			String netherCoords = Formatting.RED + "x" + Formatting.WHITE + ": " + playerX + Formatting.RED + " y"
					+ Formatting.WHITE + ": " + playerY + Formatting.RED + " z" + Formatting.WHITE + ": " + playerZ;
			double countY = Helper.MC.currentScreen instanceof ChatScreen && !coordinates.isToggle() ? 23
					: Helper.MC.currentScreen instanceof ChatScreen && coordinates.isToggle() ? 34
							: coordinates.isToggle() ? 22 : 11;
			double posY = belowPosition && coordinates.isToggle() ? 11 : belowPosition ? 1 : height - countY;
			double posX = width - IFont.legacy15.getWidthIgnoreChar(netherCoords);

			IFont.legacy15.drawStringWithShadow(matrices, netherCoords, posX + 43, posY, 0xFFFFFFFF);
		}
	}

	private void listRender(MatrixStack matrices, float tick, int width, int height) {
		List<Module> arrayList = new ArrayList<>();

		Infinity.getModuleManager().getModules().forEach(module -> {
			arrayList.add(module);
		});

		arrayList.sort((a, b) -> Float.compare(IFont.legacy15.getWidthIgnoreChar(b.getRenderName()),
				IFont.legacy15.getWidthIgnoreChar(a.getRenderName())));

		float offsetY = 0;
		int counter[] = { 0 };

		double posY = listPosition.getCurrentMode().equalsIgnoreCase("Top") ? 2
				: (listPosition.getCurrentMode().equalsIgnoreCase("Below"))
						? height - IFont.legacy15.getFontHeight() - 2
						: 2;

		int color = -1;

		for (Module module : arrayList) {
			boolean enabled = module.isEnabled() && module.isVisible();

			switch (colorMode.getCurrentMode()) {
			case "Rainbow":
				color = rainbow((int) (counter[0] * gradient.getCurrentValueDouble()),
						rainbowDelay.getCurrentValueDouble());
				break;
			case "Pulse":
				color = fade(arrayColor.getColor(), (int) (counter[0] * gradient.getCurrentValueDouble()));
				break;
			case "Custom":
				color = arrayColor.getColor().getRGB();
				break;
			}

			module.animY = (float) (enabled ? Math.min(9 + this.offsetY.getCurrentValueDouble(), module.animY + 5)
					: !enabled ? Math.max(0, module.animY - 5) : 9 + this.offsetY.getCurrentValueDouble());
			float posX = width - IFont.legacy15.getWidthIgnoreChar(module.getRenderName()) + 6;

			if (enabled) {
				Drawable.drawRectWH(matrices, posX - 1, posY + offsetY, width, 9 + this.offsetY.getCurrentValueDouble(),
						new Color(0, 0, 0, (int) transparencyBG.getCurrentValueDouble()).getRGB());
				IFont.legacy15.drawString(matrices, module.getRenderName(), posX,
						posY + offsetY + (this.offsetY.getCurrentValueDouble() / 2), color);

				counter[0]++;
			}

			if (listPosition.getCurrentMode().equalsIgnoreCase("Top"))
				offsetY += module.animY;
			else if (listPosition.getCurrentMode().equalsIgnoreCase("Below"))
				offsetY -= module.animY;
		}
	}

	private void markRender(MatrixStack matrices, float tick, int width, int height) {
		String playerPing = EntityUtil.getPing(Helper.getPlayer()) + " ms";
		String watermark = Infinity.NAME.toUpperCase();
		String fpsDebug = ((IMinecraftClient) Helper.MC).getFps() + " fps";

		int firstPos = IFont.legacy15.getStringWidth(watermark) + 23;
		int secondPos = firstPos + IFont.legacy15.getStringWidth(
				ping.isToggle() && !name.isToggle() ? playerPing : Helper.MC.getSession().getUsername()) + 7;
		int thirdPos = secondPos + IFont.legacy15.getStringWidth(playerPing) + 7;

		int xw = IFont.legacy15.getStringWidth(watermark) + 21;

		if (name.isToggle())
			xw += IFont.legacy15.getStringWidth(Helper.MC.getSession().getUsername()) + 7;
		if (ping.isToggle())
			xw += IFont.legacy15.getStringWidth(playerPing) + 7;
		if (fps.isToggle())
			xw += IFont.legacy15.getStringWidth(fpsDebug) + 7;

		Drawable.drawRectWH(matrices, 1.5, 2, xw, IFont.legacy15.getFontHeight() + 3, new Color(2, 2, 2, 210).getRGB());

		if (name.isToggle()) {
			IFont.legacy15.drawString(matrices, Helper.MC.getSession().getUsername(), firstPos + 3, 3.5, 0xFFFFFFFF);
			Drawable.drawRectWH(matrices, firstPos, 4, 0.5, IFont.legacy15.getFontHeight() - 1, 0x80FFFFFF);
		}

		if (ping.isToggle()) {
			IFont.legacy15.drawString(matrices, playerPing, name.isToggle() ? secondPos + 3 : firstPos + 3, 3.5,
					0xFFFFFFFF);
			Drawable.drawRectWH(matrices, name.isToggle() ? secondPos : firstPos, 4, 0.5,
					IFont.legacy15.getFontHeight() - 1, 0x80FFFFFF);
		}

		if (fps.isToggle()) {
			IFont.legacy15.drawString(matrices, fpsDebug, name.isToggle() && ping.isToggle() ? thirdPos + 3
					: name.isToggle() || ping.isToggle() ? secondPos + 3 : firstPos + 3, 3.5, 0xFFFFFFFF);
			Drawable.drawRectWH(matrices,
					name.isToggle() && ping.isToggle() ? thirdPos
							: name.isToggle() || ping.isToggle() ? secondPos : firstPos,
					4, 0.5, IFont.legacy15.getFontHeight() - 1, 0x80FFFFFF);
		}

		Drawable.drawRectWH(matrices, 16, 4, 0.5, IFont.legacy15.getFontHeight() - 1, 0x80FFFFFF);

		RenderUtil.drawTexture(matrices, new Identifier("infinity", "textures/game/alogo64x.png"), 4.5, 3, 9, 10);
		IFont.legacy15.drawString(matrices, watermark, 19, 3.7, 0xFFFFFFFF);
	}

	private int rainbow(int delay, double speed) {
		double rainbow = Math.ceil((System.currentTimeMillis() + delay) / speed);
		rainbow %= 360.0D;
		return Color.getHSBColor((float) -((rainbow / 360.0F)), 0.9F, 0.7F).getRGB();
	}

	private int fade(Color color, int delay) {
		float[] hsb = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
		float brightness = Math.abs(((float) (System.currentTimeMillis() % 2000L + delay) / 1000.0F) % 2.0F - 1.0F);
		brightness = 0.5F + 0.5F * brightness;
		hsb[2] = brightness % 2.0F;
		return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
	}
}