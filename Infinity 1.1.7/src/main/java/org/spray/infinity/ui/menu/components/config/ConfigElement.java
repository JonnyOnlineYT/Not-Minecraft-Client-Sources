package org.spray.infinity.ui.menu.components.config;

import org.spray.infinity.font.IFont;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.ui.menu.ClickMenu;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.StringUtil;
import org.spray.infinity.utils.file.config.Config;
import org.spray.infinity.utils.render.Drawable;
import org.spray.infinity.utils.render.RenderUtil;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ConfigElement {

	private Config config;

	private double x;
	private double y;
	private double width;
	private double height;

	private double dscale;
	private double escale;

	public ConfigElement(Config config) {
		this.config = config;
	}

	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		Drawable.drawRectWH(matrices, x + 1, y + 1, width - 2, height, 0xFF1F5A96);
		Drawable.drawRectWH(matrices, x + 1, y + 1, width - 2, height - 1, 0xFF171B37);

		Drawable.drawRectWH(matrices, x + width - 80, y + 6, 75, 20,
				Drawable.isHovered(mouseX, mouseY, x + width - 85, y + 6, 75, 20) ? 0xFF55B9C8 : 0xFF41A5B4);
		IFont.legacy15.drawString(matrices, "Load", x + width - 50, y + 11, -1);

		dscale = Drawable.isHovered(mouseX, mouseY, x + width - 95, y + 11, 10, 10) ? RenderUtil.animate(dscale, 1.2)
				: RenderUtil.animate(dscale, 1);
		escale = Drawable.isHovered(mouseX, mouseY, x + width - 110, y + 11, 10, 10) ? RenderUtil.animate(escale, 1.2)
				: RenderUtil.animate(escale, 1);

		matrices.push();

		double dx = x + width - 95;
		double dy = y + 11;

		matrices.translate(dx + 5, dy + 5, 0);
		matrices.scale((float) dscale, (float) dscale, 1f);
		matrices.translate(-dx - 5, -dy - 5, 0);

		RenderUtil.drawTexture(matrices, new Identifier("infinity", "textures/icons/save.png"), dx, dy, 10, 10);

		matrices.pop();

		matrices.push();

		double ex = x + width - 110;
		double ey = y + 11;

		matrices.translate(ex + 5, ey + 5, 0);
		matrices.scale((float) escale, (float) escale, 1f);
		matrices.translate(-ex - 5, -ey - 5, 0);

		RenderUtil.drawTexture(matrices, new Identifier("infinity", "textures/icons/delete.png"), x + width - 110,
				y + 11, 10, 10);

		matrices.pop();

		String name = StringUtil.replaceNull(config.getName());

		if (name.length() > 38)
			name = name.substring(0, 38) + "...";

		IFont.legacy16.drawString(matrices, name, x + 4, y + 5, -1);
		IFont.legacy13.drawString(matrices, "Date: " + Formatting.BLUE + StringUtil.replaceNull(config.getDate()),
				x + 9, y + 19, -1);
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button != 0)
			return false;

		if (Drawable.isHovered(mouseX, mouseY, x + width - 85, y + 6, 75, 20)) {
			config.load();

			Infinity.MENU = new ClickMenu();
			Helper.openScreen(Infinity.MENU);
			Helper.message(config.getName() + " config " + Formatting.BLUE + "loaded");
			return true;
		}

		if (Drawable.isHovered(mouseX, mouseY, x + width - 95, y + 11, 10, 10)) {
			config.save();
			Helper.message(config.getName() + " config " + Formatting.BLUE + "saved");
			return true;
		}

		if (Drawable.isHovered(mouseX, mouseY, x + width - 110, y + 11, 10, 10)) {
			Infinity.getConfigManager().delete(config);
			Helper.message(config.getName() + " config " + Formatting.BLUE + "deleted");
			return true;
		}
		return false;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

}
