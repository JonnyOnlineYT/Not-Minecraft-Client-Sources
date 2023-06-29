package org.spray.infinity.ui.menu.components.elements;

import org.spray.infinity.features.Setting;
import org.spray.infinity.font.IFont;
import org.spray.infinity.ui.menu.components.base.AbstractElement;
import org.spray.infinity.ui.menu.widgets.WTextField;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.render.Drawable;

import net.minecraft.client.util.math.MatrixStack;

public class SliderElement extends AbstractElement {

	protected WTextField valueField;
	protected boolean dragging;
	protected boolean hovered;

	public SliderElement(Setting setting) {
		super(setting);
		Helper.MC.keyboard.setRepeatEvents(true);
		valueField = new WTextField(0xFF0B1427, true);
		valueField.setMaxLength(12);
	}

	@Override
	public void init() {
	}

	public String getRenderValue() {
		return null;
	}

	public void tick() {
	}

	public void setValue(int mouseX, double x, double width) {
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.hovered = Drawable.isHovered(mouseX, mouseY, x + 55, y, width - 75, height);

		String sname = setting.getName();
		String setstrg = String.valueOf(String.valueOf(sname.substring(0, 1).toUpperCase()))
				+ sname.substring(1, sname.length());

		if (animation < 0)
			animation = 0;
		else if (animation > 1)
			animation = 1;
		
		Drawable.drawRectWH(matrices, x + 55, y + 10, width - 75, 1, 0xFF0C1535);
		Drawable.horizontalGradient(matrices, x + 55, y + 10, x + 55 + ((width - 75) * this.animation), y + 11,
				0xFF6612E2, 0xFF6A9CD3);

		Drawable.drawCircle(matrices, (float) (x + 55 + (width - 75) * animation), (float) (y + 10), 7, 0xFFCCD6C8);

		valueField.setX((int) (x + width - 15));
		valueField.setY((int) (y + 4));
		valueField.setWidth((int) (30));
		valueField.setHeight(14);

		valueField.render(matrices, mouseX, mouseY, delta);

		
		IFont.legacy14.drawString(matrices, setstrg, x + 1, y + 6, -1);
		
		if (this.dragging)
		this.setValue(mouseX, x + 55, width - 75);
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0 && hovered) {
			this.dragging = true;
		}
		valueField.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		this.dragging = false;
	}

	@Override
	public void charTyped(char chr, int keyCode) {
		valueField.charTyped(chr, keyCode);
		super.charTyped(chr, keyCode);
	}

	@Override
	public boolean isVisible() {
		return this.setting.isVisible();
	}

	@Override
	public void mouseScrolled(double d, double e, double amount) {
	}

	@Override
	public void onClose() {
		this.dragging = false;
		valueField.onClose();
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		valueField.keyPressed(keyCode, scanCode, modifiers);
	}

	public WTextField getValueField() {
		return valueField;
	}
}
