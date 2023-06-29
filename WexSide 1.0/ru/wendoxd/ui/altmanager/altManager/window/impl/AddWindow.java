package ru.wendoxd.ui.altmanager.altManager.window.impl;

import org.lwjgl.input.Keyboard;

import ru.wendoxd.ui.altmanager.altManager.text.GuiNameField;
import ru.wendoxd.ui.altmanager.altManager.text.GuiPasswordField;
import ru.wendoxd.ui.altmanager.altManager.window.AltWindow;
import ru.wendoxd.ui.menu.utils.Bound2D;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.pallete.PaletteHelper;

public class AddWindow extends AltWindow {

	protected GuiNameField login;
	protected GuiPasswordField password;

	private Bound2D addBound, closeBound;

	public AddWindow(float x, float y, float width, float height, String title) {
		super(x, y, width, height, title);

		addBound = new Bound2D(0, 0, 0, 0, true);
		closeBound = new Bound2D(0, 0, 0, 0, true);
		login = new GuiNameField(0, mc.fontRendererObj, (int) x - 10, (int) y, 170, 13);
		password = new GuiPasswordField(1, mc.fontRendererObj, (int) x - 10, (int) y, 170, 13);
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		super.draw(mouseX, mouseY);

		login.xPosition = (int) x + 25;
		login.yPosition = (int) ((int) y + height - 70);
		login.drawTextBox();
		password.xPosition = (int) (x + 25);
		password.yPosition = (int) ((int) y + height - 40);
		password.drawTextBox();

		addBound.update(x + 26, y + height - 19, x + 3 + (width / 2F) - 30, y + height - 6);
		closeBound.update(x + (width / 2F) + 27, y + height - 19, x + width - 27, y + height - 6);

		RenderUtils.drawRoundedRect(x + 26, y + height - 19, x + 3 + (width / 2F) - 30, y + height - 6,
				addBound.inBound() ? RenderUtils.rgba(0, 200, 0, 200) : PaletteHelper.darker(0, 200, 0, 200, 1.2F), 3);
		Fonts.MNTSB_14.drawCenteredString("Add", x + 2.5F + (((x + 3 + (width / 2F) - 5) - (x + 3)) / 2F),
				y + height - 14, addBound.inBound() ? -1 : RenderUtils.rgba(220, 220, 220, 255));

		RenderUtils.drawRoundedRect(x + (width / 2F) + 27, y + height - 19, x + width - 27, y + height - 6,
				closeBound.inBound() ? RenderUtils.rgba(200, 0, 0, 200) : PaletteHelper.darker(200, 0, 0, 200, 1.2F),
				3);
		Fonts.MNTSB_14.drawCenteredString("Cancel",
				x + (width / 2F) + 4.5F + (((x + width - 3) - (x + (width / 2F) + 7)) / 2F), y + height - 14,
				closeBound.inBound() ? -1 : RenderUtils.rgba(220, 220, 220, 255));

		Fonts.MNTSB_12.drawString("§7Почта §f/§7 Никнейм", x + 26, y + height - 75, -1);
		Fonts.MNTSB_12.drawString("§7Пароль " + "§f(§7только для лицензий§f)§f", x + 26, y + height - 45, -1);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		login.textboxKeyTyped(typedChar, keyCode);
		password.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void click(int mouseX, int mouseY, int mouseButton) {

		login.mouseClicked(mouseX, mouseY);
		password.mouseClicked(mouseX, mouseY);

		if (mouseButton == 0) {

			if (addBound.inBound()) {
				Keyboard.enableRepeatEvents(false);
				addAction();
			}

			if (closeBound.inBound()) {
				Keyboard.enableRepeatEvents(false);
				exit();
			}
		}
		super.click(mouseX, mouseY, mouseButton);
	}

	public void addAction() {
		exit();
	}
}
