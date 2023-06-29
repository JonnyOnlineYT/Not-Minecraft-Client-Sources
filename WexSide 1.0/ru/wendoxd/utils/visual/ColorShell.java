package ru.wendoxd.utils.visual;

import net.minecraft.util.math.MathHelper;

public class ColorShell {

	private int r, g, b, a;
	private boolean rainbow;

	public ColorShell() {
		this.r = 230;
		this.g = 230;
		this.b = 230;
		this.a = 255;
	}

	public static int HSBtoRGB(float hue) {
		int r = 0, g = 0, b = 0;
		float h = (hue - (float) Math.floor(hue)) * 6.0f;
		float f = h - (float) Math.floor(h);
		float p = 1 * (1.0f - 1);
		float q = 1 * (1.0f - 1 * f);
		float t = 1 * (1.0f - (1 * (1.0f - f)));
		switch ((int) h) {
		case 0:
			r = (int) (1 * 255.0f + 0.5f);
			g = (int) (t * 255.0f + 0.5f);
			b = (int) (p * 255.0f + 0.5f);
			break;
		case 1:
			r = (int) (q * 255.0f + 0.5f);
			g = (int) (1 * 255.0f + 0.5f);
			b = (int) (p * 255.0f + 0.5f);
			break;
		case 2:
			r = (int) (p * 255.0f + 0.5f);
			g = (int) (1 * 255.0f + 0.5f);
			b = (int) (t * 255.0f + 0.5f);
			break;
		case 3:
			r = (int) (p * 255.0f + 0.5f);
			g = (int) (q * 255.0f + 0.5f);
			b = (int) (1 * 255.0f + 0.5f);
			break;
		case 4:
			r = (int) (t * 255.0f + 0.5f);
			g = (int) (p * 255.0f + 0.5f);
			b = (int) (1 * 255.0f + 0.5f);
			break;
		case 5:
			r = (int) (1 * 255.0f + 0.5f);
			g = (int) (p * 255.0f + 0.5f);
			b = (int) (q * 255.0f + 0.5f);
			break;
		}
		return 0xff000000 | (r << 16) | (g << 8) | (b);
	}

	public ColorShell setRGB(int r, int g, int b) {
		return this.setRGBA(r, g, b, this.a);
	}

	public ColorShell setRGBA(int r, int g, int b, int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}

	public void changeRed(int value) {
		this.r = r + value;
		this.normalize();
	}

	public void changeGreen(int value) {
		this.g = g + value;
		this.normalize();
	}

	public void changeBlue(int value) {
		this.b = b + value;
		this.normalize();
	}

	public void changeAlpha(int value) {
		this.a = a + value;
		this.normalize();
	}

	public int getRedDefault() {
		return this.r;
	}

	public int getGreenDefault() {
		return this.g;
	}

	public int getBlueDefault() {
		return this.b;
	}

	public int getRed() {
		if (this.isRainbow())
			this.updateRainbow();
		return this.r;
	}

	public int getGreen() {
		this.updateRainbow();
		return this.g;
	}

	public int getBlue() {
		this.updateRainbow();
		return this.b;
	}

	public int getAlpha() {
		return this.a;
	}

	public boolean isRainbow() {
		return this.rainbow;
	}

	public ColorShell setRainbow(boolean rainbow) {
		this.rainbow = rainbow;
		return this;
	}

	public void normalize() {
		this.r = MathHelper.clamp(this.r, 0, 255);
		this.g = MathHelper.clamp(this.g, 0, 255);
		this.b = MathHelper.clamp(this.b, 0, 255);
		this.a = MathHelper.clamp(this.a, 0, 255);
	}

	public void updateRainbow() {
		if (!isRainbow()) {
			return;
		}
		double rainbow = Math.ceil(System.currentTimeMillis() / 16);
		rainbow %= 360;
		int color = HSBtoRGB((float) (rainbow / 360F));
		this.r = (color >> 16) & 255;
		this.g = (color >> 8) & 255;
		this.b = color & 255;
	}

	public void updateRainbow(long add) {
		double rainbow = Math.ceil((System.currentTimeMillis() + add) / 16);
		rainbow %= 360;
		int color = HSBtoRGB((float) (rainbow / 360F));
		this.r = (color >> 16) & 255;
		this.g = (color >> 8) & 255;
		this.b = color & 255;
	}

	public int build() {
		this.updateRainbow();
		return RenderUtils.rgba(r, g, b, a);
	}
}
