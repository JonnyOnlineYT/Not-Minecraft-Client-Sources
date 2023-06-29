package splash.utilities.visual;

import java.awt.Color;
import java.util.Random;

import splash.Splash;
import splash.client.modules.visual.UI;
import splash.client.modules.visual.UI.ArrayColor;
import splash.utilities.math.MathUtils;
import splash.utilities.system.ClientLogger;

/**
 * Author: Ice Created: 15:25, 31-May-20 Project: Client
 */
public class ColorUtilities {
	public static Color getRainbow(int speed, int offset) {
		UI ui = (UI) Splash.getInstance().getModuleManager().getModuleByClass(UI.class);
		float hue = (float) (((System.currentTimeMillis() * 1.5) + offset) % speed);
		return Color.getHSBColor(hue /= speed, ui.saturation.getValue().floatValue(), 1);
	}
	
	public static Color getRainbow(int speed, int offset, float hue) {
		return Color.getHSBColor(hue /= speed, 0.7F, 1);
	}
	
	public static Color getRainbowWithSaturation(int speed, final int offset, float saturation,
			float brightness) {
		float hue = (float) ((System.currentTimeMillis() + offset) % speed);
		hue /= speed;
		return Color.getHSBColor(hue, saturation, brightness);
	}

	public static int getGerman(float luminance) {
		Random random = new Random();
		final float hue = random.nextFloat();
		final float saturation = (random.nextInt(8000) + 500) / 10000f;
		return Color.getHSBColor(hue, saturation, 0.9f).getRGB();
	}

	public static Color getRainbowSaturation(int speed, int offset) {
		float hue = (float) ((System.currentTimeMillis() + offset) % speed);
		return Color.getHSBColor(hue /= speed, 1F, 1.0F);
	}
	
    public static Color fade(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)index / (float)count * 2.0F) % 2.0F - 1.0F);
        brightness = 0.5F + 0.5F * brightness;
        hsb[2] = brightness % 2.0F;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
    
	public static int astolfoColors(int yOffset, int yTotal) {
		UI ui = (UI) Splash.getInstance().getModuleManager().getModuleByClass(UI.class);
        float speed = 2900F;
        float hue = (float) (System.currentTimeMillis() % (int)speed) + ((yTotal - yOffset) * 9);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, ui.saturation.getValue().floatValue(), 1F);
	}
	
	public static int fadeColor(double delay, UI ui) {
		double fadeState = Math.sin(-1) / 5.0D;
		fadeState %= 740.0F;
		int ni;
		ni = Integer.valueOf((int) (180 + Math.sin(System.nanoTime() * 0.0000000035 + delay / 600) * 75));
		float hue = 0;
		if (ui.arrayColor.getValue().name().equalsIgnoreCase("Blue")) {
			hue = 125F / 255F;
		}

		if (ui.arrayColor.getValue().name().equalsIgnoreCase("Green")) {
			hue = 110F / 255F;
		}

		if (ui.arrayColor.getValue().name().equalsIgnoreCase("Purple")) {
			hue = 300F / 360F;
		}

		int c1 = Color.HSBtoRGB(hue, ui.saturation.getValue().floatValue(), ((float) ni) / 255F);

		return c1;

	}

	public static Color getGradientOffset(Color color1, Color color2, double offset) {
		if (offset > 1) {
			double left = offset % 1;
			int off = (int) offset;
			offset = off % 2 == 0 ? left : 1 - left;

		}
		double inverse_percent = 1 - offset;
		int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
		int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
		int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
		return new Color(redPart, greenPart, bluePart);
	}

	public static Color fadeColor(Color color, int index, int count) {
		float[] hsb = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
		float brightness = Math.abs(
				((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) index / (float) count * 2.0F) % 2.0F
						- 1.0F);
		brightness = 0.5F + 0.5F * brightness;
		hsb[2] = brightness % 2.0F;
		return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	}

}
