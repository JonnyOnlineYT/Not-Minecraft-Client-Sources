package ru.wendoxd.utils.visual.hud;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.ScaledResolution;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class DraggableElement {
	private int posX, posY, dragX, dragY, width, height;
	private boolean dragging;

	public void tick(ScaledResolution res) {
		if (dragging) {
			if (!Mouse.isButtonDown(0)) {
				dragging = false;
				return;
			}
			posX = MenuAPI.mouseX - dragX;
			posY = MenuAPI.mouseY - dragY;
			if (posX + width > res.getScaledWidth()) {
				posX = res.getScaledWidth() - width;
			}
			if (posY + height > res.getScaledHeight()) {
				posY = res.getScaledHeight() - height;
			}
			if (posX < 0) {
				posX = 0;
			}
			if (posY < 0) {
				posY = 0;
			}
		}
	}

	public void mouseClicked() {
		if (MenuAPI.mouseX > posX && MenuAPI.mouseY > posY && MenuAPI.mouseX < posX + width
				&& MenuAPI.mouseY < posY + height) {
			dragX = MenuAPI.mouseX - posX;
			dragY = MenuAPI.mouseY - posY;
			dragging = true;
		}
	}

	public int getX() {
		return this.posX;
	}

	public int getY() {
		return this.posY;
	}

	public void setX(int x) {
		this.posX = x;
	}

	public void setY(int y) {
		this.posY = y;
	}

	public void update(int width, int height) {
		this.updateWidth(width);
		this.updateHeight(height);
	}

	public void updateWidth(int width) {
		this.width = width;
	}

	public void updateHeight(int height) {
		this.height = height;
	}
}
