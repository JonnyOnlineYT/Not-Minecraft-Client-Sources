package ru.wendoxd.utils.visual.pallete;

import net.minecraft.entity.EntityLivingBase;
import ru.wendoxd.utils.visual.RenderUtils;

public class PaletteHelper {

	public static int getHealthColor(float health, float maxHealth) {
		return HSBtoRGB(Math.max(0, Math.min(health, maxHealth) / maxHealth) / 3, 1, 0.8f) | 0xFF000000;
	}

	public static String getHealthStr(EntityLivingBase entity) {
		String str = "";
		int health = (int) entity.getHealth();
		if (health <= entity.getMaxHealth() * 0.25) {
			str = "§4";
		} else if (health <= entity.getMaxHealth() * 0.5) {
			str = "§6";
		} else if (health <= entity.getMaxHealth() * 0.75) {
			str = "§e";
		} else if (health <= entity.getMaxHealth()) {
			str = "§a";
		}
		return str;
	}

	public static int darker(int r, int g, int b, int a, float factor) {
		return RenderUtils.rgba(Math.max(r * factor, 0), Math.max(g * factor, 0), Math.max(b * factor, 0), a);
	}

	public static int rainbow(float phase) {
		float speed = 3000;
		float hue = (System.currentTimeMillis() % (int) speed) + phase * 3000;
		if (hue > speed) {
			hue -= speed;
		}
		hue /= speed;
		if (hue > 0.5F) {
			hue = 0.5F - (hue - 0.5F);
		}
		hue += 0.5F;
		return HSBtoRGB(hue, 0.7f, 1);
	}

	public static int rainbow(float phase, float brightness) {
		float speed = 3000;
		float hue = (System.currentTimeMillis() % (int) speed) + phase * 3000;
		if (hue > speed) {
			hue -= speed;
		}
		hue /= speed;
		if (hue > 0.5F) {
			hue = 0.5F - (hue - 0.5F);
		}
		hue += 0.5F;
		return HSBtoRGB(hue, 0.7f, brightness);
	}

	public static int HSBtoRGB(float hue, float saturation, float brightness) {
		int r = 0, g = 0, b = 0;
		if (saturation == 0) {
			r = g = b = (int) (brightness * 255.0f + 0.5f);
		} else {
			float h = (hue - (float) Math.floor(hue)) * 6.0f;
			float f = h - (float) Math.floor(h);
			float p = brightness * (1.0f - saturation);
			float q = brightness * (1.0f - saturation * f);
			float t = brightness * (1.0f - (saturation * (1.0f - f)));
			switch ((int) h) {
			case 0:
				r = (int) (brightness * 255.0f + 0.5f);
				g = (int) (t * 255.0f + 0.5f);
				b = (int) (p * 255.0f + 0.5f);
				break;
			case 1:
				r = (int) (q * 255.0f + 0.5f);
				g = (int) (brightness * 255.0f + 0.5f);
				b = (int) (p * 255.0f + 0.5f);
				break;
			case 2:
				r = (int) (p * 255.0f + 0.5f);
				g = (int) (brightness * 255.0f + 0.5f);
				b = (int) (t * 255.0f + 0.5f);
				break;
			case 3:
				r = (int) (p * 255.0f + 0.5f);
				g = (int) (q * 255.0f + 0.5f);
				b = (int) (brightness * 255.0f + 0.5f);
				break;
			case 4:
				r = (int) (t * 255.0f + 0.5f);
				g = (int) (p * 255.0f + 0.5f);
				b = (int) (brightness * 255.0f + 0.5f);
				break;
			case 5:
				r = (int) (brightness * 255.0f + 0.5f);
				g = (int) (p * 255.0f + 0.5f);
				b = (int) (q * 255.0f + 0.5f);
				break;
			}
		}
		return 0xff000000 | (r << 16) | (g << 8) | (b);
	}
}