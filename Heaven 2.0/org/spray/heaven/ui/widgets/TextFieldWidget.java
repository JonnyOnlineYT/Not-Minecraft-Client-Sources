package org.spray.heaven.ui.widgets;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.font.FontRenderer;
import org.spray.heaven.util.render.Drawable;

import java.awt.*;

public class TextFieldWidget {

	private FontRenderer font;

	private String fillText = "";

	private String text = "";

	private boolean locked = false;
	private boolean focused = false;
	private boolean borders = true;
	private boolean background = true;
	private boolean onlynumber;

	private int maxLength;

	private int color;

	private double x;
	private double y;
	private double width;
	private double height;

	private boolean obfedText;

	public TextFieldWidget(FontRenderer font) {
		this.setColor(0xFF0A0A0A);
		this.font = font;
		maxLength = 80;
		obfedText = false;
	}

	public void render(int mouseX, int mouseY, float partialTicks) {
		if (!locked) {
			if (borders && background)
				Drawable.drawBorderedRect(x + 1, y + 1, width - 2, height - 2, 1, 0xFFBEBBBB, color);
			else if (background)
				Drawable.drawRectWH(x, y, width, height, color);

			if (!focused && text.isEmpty() && !fillText.isEmpty())
				font.drawString(fillText, getX() + 3, getY() + (getHeight() / 2) - (font.getFontHeight() / 2),
						0xFF868686);
		}

		double diff = Math.max(font.getStringWidth(getText()) + 4 - getWidth(), 0);

		String text = obfedText ? getText().replaceAll("(?s).", "*") : getText();
		double diffY = obfedText ? 1.5 : 0;

		Drawable.startScissor(x, y, width, height);
		if (!getText().isEmpty()) {
			font.drawString(text, getX() + 3 - diff, getY() + (getHeight() / 2) - (font.getFontHeight() / 2) + diffY,
					-1);
		}
		Drawable.stopScissor();
		if (focused)
			Drawable.drawRectWH(getX() + font.getStringWidth(text) + 4 - diff, getY() + (getHeight() + 4) / 2, 4, 1,
					new Color(255, 255, 255, System.currentTimeMillis() % 2000 > 1000 ? 255 : 0).getRGB());

	}

	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (Drawable.isHovered(mouseX, mouseY, x, y, width, height) && mouseButton == 0)
			focused = true;
		else
			focused = false;
	}

	public void keyTyped(Character typedChar, int keyCode) {
		if (!focused)
			return;

		if (keyCode == Keyboard.KEY_BACK) {
			if (getText().length() != 0)
				setText(getText().substring(0, getText().length() - 1));
		} else if (GuiScreen.isKeyComboCtrlV(keyCode))
			setText(getText() + GuiScreen.getClipboardString());
		else if (typedChar != null && text.length() < maxLength
				&& ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
			if (onlynumber && filter(text, typedChar))
				setText(getText() + typedChar);
			else
				setText(getText() + typedChar);
		}
	}

	public void onClose() {
		focused = false;
	}

	private boolean filter(String text, char c) {
		boolean good;
		boolean validate = true;

		if (c == '-' && text.isEmpty()) {
			good = true;
			validate = false;
		} else if (c == '.' && !text.contains(".")) {
			good = true;
			if (text.isEmpty())
				validate = false;
		} else
			good = Character.isDigit(c);

		if (good && validate) {
			try {
				Double.parseDouble(text + c);
			} catch (NumberFormatException ignored) {
				good = false;
			}
		}

		return good;
	}

	public TextFieldWidget setBackground(boolean state) {
		this.background = state;
		return this;
	}
	
	public TextFieldWidget setLocked(boolean state) {
		this.locked = state;
		return this;
	}

	public TextFieldWidget setOnlyNumber(boolean state) {
		this.onlynumber = state;
		return this;
	}

	public TextFieldWidget setBorders(boolean borders) {
		this.borders = borders;
		return this;
	}

	public TextFieldWidget setColor(int color) {
		this.color = color;
		return this;
	}

	public TextFieldWidget setObfedText(boolean obf) {
		this.obfedText = obf;
		return this;
	}

	public boolean isFocused() {
		return focused;
	}

	/**
	 * Getter for text in field
	 *
	 * @return text in field
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter for text in field
	 *
	 * @param value text
	 */
	public void setText(String value) {
		text = value;
	}

	/**
	 * Sets the fill text
	 *
	 * @param value text
	 */
	public void setFillText(String value) {
		fillText = value;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getColor() {
		return color;
	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}

}