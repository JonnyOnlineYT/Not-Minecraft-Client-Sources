package org.spray.infinity.ui.menu.components.elements;

import org.spray.infinity.features.Setting;
import org.spray.infinity.font.IFont;
import org.spray.infinity.ui.menu.components.base.AbstractElement;
import org.spray.infinity.ui.menu.components.window.ColorPickerWindow;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.render.Drawable;
import org.spray.infinity.utils.render.RenderUtil;

import net.minecraft.client.util.math.MatrixStack;

public class CheckBoxElement extends AbstractElement {

	private boolean hovered;
	private double move;

	public CheckBoxElement(Setting setting) {
		super(setting);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		hovered = Drawable.isHovered(mouseX, mouseY, x + width - 15, y + 5, 20, 13);

		move = setting.isToggle() ? RenderUtil.animate(move, 10) : RenderUtil.animate(move, 0);
		Drawable.drawHRoundedRect(matrices, x + width - 10, y + 5, 10, 10,
				setting.isToggle() ? 0xFF101E2E : 0xFF191919);
		Drawable.drawCircle(matrices, x + width - 10 + move, y + 10, 7, setting.isToggle() ? 0xFF60B9CF : 0xFF505151);

		IFont.legacy14.drawString(matrices, this.setting.getName(), x + 1, y + 5,
				setting.isToggle() ? 0xFFFFFFFF : 0xFFC4BFBF);

		if (setting.getColor() == null)
			return;

		int color = setting.getColor().getRGB();
		Drawable.drawCircle(matrices, x + width - 37, y + 7, 4, color);
		Drawable.drawCircle(matrices, x + width - 24, y + 7, 4, color);

		Drawable.drawCircle(matrices, x + width - 37, y + 13, 4, color);
		Drawable.drawCircle(matrices, x + width - 24, y + 13, 4, color);

		Drawable.drawRectWH(matrices, x + width - 39, y + 7, 17, 6, color);
		Drawable.drawRectWH(matrices, x + width - 37, y + 5, 13, 10, color);
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (Drawable.isHovered(mouseX, mouseY, x + width - 40, y + 4, 19, 12) && setting.getColor() != null)
			Helper.openScreen(new ColorPickerWindow(Helper.MC.currentScreen, setting));
		else if (hovered && button == 0)
			setting.setToggle(!setting.isToggle());

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

	public double getMove() {
		return move;
	}

	public void setMove(double move) {
		this.move = move;
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		// TODO Auto-generated method stub

	}
}
