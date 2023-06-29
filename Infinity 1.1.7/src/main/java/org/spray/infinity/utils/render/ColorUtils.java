package org.spray.infinity.utils.render;

import java.awt.Color;

public class ColorUtils {

	public static int SELECT = new Color(33, 170, 47).getRGB();
	public static int BG = new Color(23, 23, 23).getRGB();
	public static int TOGGLE = new Color(123, 123, 123).getRGB();
	public static int CHECK_BG = new Color(123, 123, 123).getRGB();
	public static int CHECK_TOGGLE = new Color(123, 233, 123).getRGB();
	public static int dragPanelColor = 0XFF112B52;
	public static int openColor = 0xFFAAA5A4;
	public static int backGroundLine = 0xFF252525;
	public static int lineColor = 0xFF545454;
	public static int sliderColor = -1;
	public static int shadow = 0xFF0F0F0F;
	public static int booleanToogle = 0xFF079922;
	// Night
	public static int backNight = 0xFF1D1C1C;

	public static Color blend(Color color1, Color color2, double ratio) {
		float r = (float) ratio;
		float ir = (float) 1.0 - r;

		float red = color1.getRed() * r + color2.getRed() * ir;
		float green = color1.getGreen() * r + color2.getGreen() * ir;
		float blue = color1.getBlue() * r + color2.getBlue() * ir;
		float alpha = color1.getAlpha() * r + color2.getAlpha() * ir;

		red = Math.min(255f, Math.max(0f, red));
		green = Math.min(255f, Math.max(0f, green));
		blue = Math.min(255f, Math.max(0f, blue));
		alpha = Math.min(255f, Math.max(0f, alpha));

		Color color = null;
		try {
			color = new Color((int) red, (int) green, (int) blue, (int) alpha);
		} catch (IllegalArgumentException exp) {
			exp.printStackTrace();
		}
		return color;
	}
}
