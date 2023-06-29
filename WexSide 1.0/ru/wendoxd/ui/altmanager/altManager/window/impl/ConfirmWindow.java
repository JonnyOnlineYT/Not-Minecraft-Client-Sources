package ru.wendoxd.ui.altmanager.altManager.window.impl;

import ru.wendoxd.ui.altmanager.altManager.window.AltWindow;
import ru.wendoxd.ui.menu.utils.Bound2D;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.pallete.PaletteHelper;

public class ConfirmWindow extends AltWindow {

	private final Bound2D confirmBound, closeBound;
	protected String message;

	public ConfirmWindow(float x, float y, float width, float height, String title, String message) {
		super(x, y, width, height, title);
		this.message = message;
		this.confirmBound = new Bound2D(0, 0, 0, 0, true);
		this.closeBound = new Bound2D(0, 0, 0, 0, true);
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		super.draw(mouseX, mouseY);

		Fonts.MNTSB_14.drawCenteredString(message, x + (width / 2F), y + (height / 2F) - 7, -1);

		confirmBound.update(x + 30, y + height - 19, x + 3 + (width / 2F) - 30, y + height - 6);
		closeBound.update(x + (width / 2f) + 25, y + height - 19, x + width - 20, y + height - 6);

		RenderUtils.drawRoundedRect(x + 30, y + height - 19, x + 3 + (width / 2f) - 30, y + height - 6,
				confirmBound.inBound() ? RenderUtils.rgba(0, 200, 0, 200) : PaletteHelper.darker(0, 200, 0, 200, 1.2F),
				3);

		float acceptWidth = (x + 3 + (width / 2F) - 5) - (x + 3);
		Fonts.MNTSB_14.drawCenteredString("Confirm", x + 3 + (acceptWidth / 2F), y + height - 14,
				confirmBound.inBound() ? -1 : RenderUtils.rgba(220, 220, 220, 255));

		RenderUtils.drawRoundedRect(x + (width / 2F) + 25, y + height - 19, x + width - 20, y + height - 6,
				closeBound.inBound() ? RenderUtils.rgba(200, 0, 0, 200) : PaletteHelper.darker(200, 0, 0, 200, 1.2F),
				3);
		float cancelWidth = (x + width - 3) - (x + (width / 2f) + 7);
		Fonts.MNTSB_14.drawCenteredString("Cancel", x + (width / 2f) + 7 + (cancelWidth / 2f), y + height - 14,
				closeBound.inBound() ? -1 : RenderUtils.rgba(220, 220, 220, 255));
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void click(int mouseX, int mouseY, int mouseButton) {
		super.click(mouseX, mouseY, mouseButton);

		if (mouseButton == 0) {
			if (confirmBound.inBound()) {
				confirmAction();
			}

			if (closeBound.inBound()) {
				exit();
			}
		}
	}

	public void confirmAction() {
		exit();
	}
}
