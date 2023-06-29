package ru.wendoxd.ui.altmanager.altManager.window;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import ru.wendoxd.ui.menu.utils.Bound2D;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.lib.RectHelper;
import ru.wendoxd.utils.visual.RenderUtils;

public class AltWindow {

	private final Bound2D dragBound;
	public float x, y, dragX, dragY, width, height;
	public String title;
	public boolean exit;
	protected Minecraft mc = Minecraft.getMinecraft();

	public AltWindow(float x, float y, float width, float height, String title) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.title = title;
		this.dragBound = new Bound2D(0, 0, 0, 0, true);
		this.makeCenter();
	}

	public void draw(int mouseX, int mouseY) {
		dragBound.update(x, y, x + width, y + 16);
		drag(mouseX, mouseY);
		RectHelper.renderShadow(x, y - 6, width, height + 4, RenderUtils.rgba(14, 14, 14, 255), 3);
		Fonts.MNTSB_14.drawString(title, x + (width / 2F) - (Fonts.MNTSB_14.getStringWidth(title) / 2F), y + 5, -1);
	}

	public void keyTyped(char typedChar, int keyCode) {
	}

	public void click(int mouseX, int mouseY, int mouseButton) {
	}

	private void makeCenter() {
		ScaledResolution sr = new ScaledResolution(mc);
		x = sr.getScaledWidth() / 2F - (width / 2F);
		y = sr.getScaledHeight() / 2F - (height / 2F);
	}

	private void drag(int mx, int my) {
		ScaledResolution sr = new ScaledResolution(mc);

		if (Mouse.isButtonDown(0) && dragBound.inBound()) {
			if (dragX == 0 && dragY == 0) {
				dragX = MathHelper.clamp(mx - x, 0, sr.getScaledWidth() - width);
				dragY = MathHelper.clamp(my - y, 0, sr.getScaledHeight() - height);
			} else {
				x = MathHelper.clamp(mx - dragX, 0, sr.getScaledWidth() - width);
				y = MathHelper.clamp(my - dragY, 0, sr.getScaledHeight() - height);
			}
		} else if (dragX != 0 || dragY != 0) {
			dragX = 0;
			dragY = 0;
		}
	}

	public void exit() {
		exit = true;
	}
}
