package org.spray.infinity.ui.menu.components.base;

import org.spray.infinity.features.Setting;

import net.minecraft.client.util.math.MatrixStack;

public abstract class AbstractElement {

	protected Setting setting;

	protected double animation;
	protected double stringAnimation;

	protected double x;
	protected double y;
	protected double width;
	protected double height;

	public AbstractElement(Setting setting) {
		this.setting = setting;
	}

	public abstract void render(MatrixStack matrices, int mouseX, int mouseY, float delta);

	public abstract void mouseClicked(double mouseX, double mouseY, int button);

	public abstract void mouseReleased(double mouseX, double mouseY, int button);

	public abstract void mouseScrolled(double d, double e, double amount);

	public abstract void keyPressed(int keyCode, int scanCode, int modifiers);

	public abstract void onClose();

	public abstract void tick();

	public abstract boolean isVisible();

	public void init() {
	}

	public void charTyped(char chr, int keyCode) {
	}

	public Setting getSetting() {
		return setting;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getAnimation() {
		return animation;
	}

	public void setAnimation(double animation) {
		this.animation = animation;
	}

	public double getStringAnimation() {
		return stringAnimation;
	}

	public void setStringAnimation(double stringAnimation) {
		this.stringAnimation = stringAnimation;
	}
}
