package org.spray.infinity.ui.menu.components.elements;

import org.spray.infinity.features.Setting;
import org.spray.infinity.font.IFont;
import org.spray.infinity.ui.menu.components.base.AbstractElement;
import org.spray.infinity.utils.render.Drawable;

import net.minecraft.client.util.math.MatrixStack;

public class ComboElement extends AbstractElement {

	private int counter;

	public ComboElement(Setting setting) {
		super(setting);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		Drawable.drawRectWH(matrices, (float) (x), (float) (y), (float) (width), (float) (height), 0x40090C13);
		IFont.legacy14.drawString(matrices, setting.getName(), x + 4, y + 5, -1);
		IFont.legacy14.drawString(matrices, setting.getCurrentMode(),
				x + width - 10 - IFont.legacy14.getStringWidth(setting.getCurrentMode()), y + 5, -1);
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (!Drawable.isHovered(mouseX, mouseY, x, y, width, height))
			return;
		int size = setting.getModes().size();

		if (button == 0) {
			if (counter + 1 > size - 1)
				counter = 0;
			else
				counter++;

			setting.setCurrentMode(setting.getModes().get(counter));
		} else if (button == 1) {
			if (counter == 0)
				counter = size - 1;
			else
				counter--;

			setting.setCurrentMode(setting.getModes().get(counter));
		}
	}

	@Override
	public boolean isVisible() {
		return setting.isVisible();
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseScrolled(double d, double e, double amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		// TODO Auto-generated method stub

	}
}
