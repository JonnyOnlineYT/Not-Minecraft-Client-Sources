package org.spray.infinity.ui.menu.widgets;

import java.awt.Color;

import org.lwjgl.glfw.GLFW;
import org.spray.infinity.font.IFont;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.render.Drawable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class WTextField {
	/**
	 * Stores text for the fill in
	 */
	private String fillText = "";

	/**
	 * Stores the current text in the text field
	 */
	private String text = "";

	/**
	 * Stores whether the cursor selected the text field and should listen for
	 * keystrokes
	 */
	private boolean focused = false;

	private int maxLength;

	private boolean onlyNum;

	/**
	 * Stores the color of the text field
	 */
	private int color;

	private double x;
	private double y;
	private double width;
	private double height;

	public WTextField(int color, boolean onlyNum) {
		this.setColor(color);
		this.onlyNum = onlyNum;
		maxLength = 80;
	}

	public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
		if (focused)
			Drawable.drawRectWH(matrices, x, y, width, height, 0xFF212F55);
		Drawable.drawRectWH(matrices, x + 1, y + 1, width - 2, height - 2, color);

		double diff = Math.max(IFont.legacy14.getStringWidth(getText()) + 4 - getWidth(), 0);

		if (!getText().isEmpty()) {
			IFont.legacy14.drawString(matrices, getText(), getX() + 2 - diff,
					getY() + (getHeight() - IFont.legacy14.getFontHeight()) / 2, -1);
		}

		if (!focused && text.isEmpty() && !fillText.isEmpty())
			IFont.legacy14.drawString(matrices, fillText, getX() + 2, getY() + (getHeight() - IFont.legacy14.getFontHeight()),
					-1);

		if (focused) {
			Drawable.drawRectWH(matrices, getX() + IFont.legacy14.getStringWidth(getText()) + 4 - diff,
					getY() + (getHeight() + 4) / 2, 4, 1,
					new Color(255, 255, 255, System.currentTimeMillis() % 2000 > 1000 ? 255 : 0).getRGB());
		}
	}

	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (Drawable.isHovered(mouseX, mouseY, x, y, width, height) && mouseButton == 0)
			focused = true;
		else
			focused = false;
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		if (focused) {
			if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
				if (getText().length() != 0)
					setText(getText().substring(0, getText().length() - 1));
			} else if (Helper.isPaste(keyCode)) {
				setText(getText() + Helper.MC.keyboard.getClipboard());
			}
		}
	}

	public void charTyped(Character typedChar, int keyCode) {
		if (focused) {
			if (onlyNum) {
				if (typedChar != null && text.length() < maxLength && filter(text, typedChar))
					setText(getText() + typedChar);
			} else {
				if (typedChar != null && text.length() < maxLength)
					setText(getText() + typedChar);
			}
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

	public void setColor(int color) {
		this.color = color;
	}

}