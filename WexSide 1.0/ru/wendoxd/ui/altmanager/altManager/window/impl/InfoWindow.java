package ru.wendoxd.ui.altmanager.altManager.window.impl;

import ru.wendoxd.ui.altmanager.altManager.window.AltWindow;
import ru.wendoxd.ui.menu.utils.Bound2D;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;

public class InfoWindow extends AltWindow {

	private final Bound2D closeBound;
	protected String message, buttonText;

	public InfoWindow(float x, float y, float width, float height, String title, String message) {
		super(x, y, width, height, title);
		this.message = message;
		this.buttonText = "Cancel";
		this.closeBound = new Bound2D(0, 0, 0, 0, true);
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		super.draw(mouseX, mouseY);

		Fonts.MNTSB_14.drawString(message, x + (width / 2) - (Fonts.MNTSB_14.getStringWidth(message) / 2F),
				y + (height / 2) - 7, -1);

		closeBound.update(x + (width / 2) - 50, y + height - 18, x + (width / 2) + 50, y + height - 3);

		RenderUtils.drawRoundedRect(x + (width / 2) - 30, y + height - 19, x + (width / 2) + 30, y + height - 6,
				closeBound.inBound() ? RenderUtils.rgba(50, 120, 220, 200) : RenderUtils.rgba(66, 134, 245, 200), 2);
		Fonts.MNTSB_14.drawCenteredString(buttonText, x + (width / 2f), y + height - 14,
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
			if (closeBound.inBound()) {
				exit();
			}
		}
	}
}
