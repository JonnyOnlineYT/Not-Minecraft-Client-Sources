package volcano.summer.client.util;

import java.awt.Color;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

public enum Colors {
	BLACK(

			'0', 0), DARK_BLUE(

					'1', 1), DARK_GREEN(

							'2', 2), DARK_AQUA(

									'3', 3), DARK_RED(

											'4', 4), DARK_PURPLE(

													'5', 5), GOLD(

															'6', 6), GRAY(

																	'7', 7), DARK_GRAY(

																			'8', 8), BLUE(

																					'9', 9), GREEN(

																							'a', 10), AQUA(

																									'b', 11), RED(

																											'c',
																											12), LIGHT_PURPLE(

																													'd',
																													13), YELLOW(

																															'e',
																															14), WHITE(

																																	'f',
																																	15), MAGIC(

																																			'k',
																																			16,
																																			true), BOLD(

																																					'l',
																																					17,
																																					true), STRIKETHROUGH(

																																							'm',
																																							18,
																																							true), UNDERLINE(

																																									'n',
																																									19,
																																									true), ITALIC(

																																											'o',
																																											20,
																																											true), RESET(

																																													'r',
																																													21);

	public static final char COLOR_CHAR = '§';
	private static Color color = Color.YELLOW;
	private static final Pattern STRIP_COLOR_PATTERN;
	private final int intCode;
	private final char code;
	private final boolean isFormat;
	private final String toString;
	private static final Map<Integer, Colors> BY_ID;
	private static final Map<Character, Colors> BY_CHAR;

	private Colors(char code, int intCode) {
		this(code, intCode, false);
	}

	private Colors(char code, int intCode, boolean isFormat) {
		this.code = code;
		this.intCode = intCode;
		this.isFormat = isFormat;
		this.toString = new String(new char[] { '§', code });
	}

	public char getChar() {
		return this.code;
	}

	@Override
	public String toString() {
		return this.toString;
	}

	public boolean isFormat() {
		return this.isFormat;
	}

	public boolean isColor() {
		return (!this.isFormat) && (this != RESET);
	}

	public static Colors getByChar(char code) {
		return BY_CHAR.get(Character.valueOf(code));
	}

	public static int getColor(final Color color) {
		return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public static int getColor(final int brightness) {
		return getColor(brightness, brightness, brightness, 255);
	}

	public static int getColor(final int brightness, final int alpha) {
		return getColor(brightness, brightness, brightness, alpha);
	}

	public static int getColor(final int red, final int green, final int blue) {
		return getColor(red, green, blue, 255);
	}

	public static Colors getByChar(String code) {
		Validate.notNull(code, "Code cannot be null", new Object[0]);
		Validate.isTrue(code.length() > 0, "Code must have at least one char", new Object[0]);
		return BY_CHAR.get(Character.valueOf(code.charAt(0)));
	}

	public static String stripColor(String input) {
		if (input == null) {
			return null;
		}
		return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
		char[] b = textToTranslate.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if ((b[i] == altColorChar) && ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[(i + 1)]) > -1)) {
				b[i] = '§';
				b[(i + 1)] = Character.toLowerCase(b[(i + 1)]);
			}
		}
		return new String(b);
	}

	public static String getLastColors(String input) {
		String result = "";
		int length = input.length();
		for (int index = length - 1; index > -1; index--) {
			char section = input.charAt(index);
			if ((section == '§') && (index < length - 1)) {
				char c = input.charAt(index + 1);
				Colors color = getByChar(c);
				if (color != null) {
					result = color.toString() + result;
					if ((color.isColor()) || (color.equals(RESET))) {
						break;
					}
				}
			}
		}
		return result;
	}

	static {
		STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");

		BY_ID = Maps.newHashMap();
		BY_CHAR = Maps.newHashMap();
		Colors[] arrayOfChatColor;
		int j = (arrayOfChatColor = values()).length;
		for (int i = 0; i < j; i++) {
			Colors color = arrayOfChatColor[i];
			BY_ID.put(Integer.valueOf(color.intCode), color);
			BY_CHAR.put(Character.valueOf(color.code), color);
		}
	}

	public static float[] getColorFromIntRGBA(final int color) {
		final float r = (color & 0xFF000000) / 255.0f;
		final float g = (color & 0xFF0000) / 255.0f;
		final float b = (color & 0xFF00) / 255.0f;
		final float a = (color & 0xFF) / 255.0f;
		return new float[] { r, g, b, a };
	}

	public static Color getRainbow(long offset, float fade) {
		float hue = (System.nanoTime() + offset) / 5.0E9F % 1.0F;
		long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()),
				16);
		Color c = new Color((int) color);
		return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade,
				c.getAlpha() / 255.0F);
	}

	public static Color rainbow(final long offset, final float fade) {
		final float hue = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
		final long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0f, 1.0f))), 16);
		final Color c = new Color((int) color);
		return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade,
				c.getAlpha() / 255.0f);
	}

	public static Color getColor() {
		return color;
	}

	public static int getColor(final int red, final int green, final int blue, final int alpha) {
		int color = 0;
		color |= alpha << 24;
		color |= red << 16;
		color |= green << 8;
		color |= blue;
		return color;
	}

	public static float[] getRGBA(int color) {
		if ((color & 0xFC000000) == 0x0) {
			color |= 0xFF000000;
		}
		final float a = (color >> 24 & 0xFF) / 255.0f;
		final float r = (color >> 16 & 0xFF) / 255.0f;
		final float g = (color >> 8 & 0xFF) / 255.0f;
		final float b = (color & 0xFF) / 255.0f;
		return new float[] { r, g, b, a };
	}

	public static Color blend(Color color1, Color color2, float ratio) {
		if (ratio < 0.0F) {
			return color2;
		}
		if (ratio > 1.0F) {
			return color1;
		}
		float ratio2 = 1.0F - ratio;
		float[] rgb1 = new float[3];
		float[] rgb2 = new float[3];
		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);
		Color color = new Color(rgb1[0] * ratio + rgb2[0] * ratio2, rgb1[1] * ratio + rgb2[1] * ratio2,
				rgb1[2] * ratio + rgb2[2] * ratio2);
		return color;
	}

	public static Color rainbowEffect(long offset, float fade) {
		float hue = (System.nanoTime() + offset) / 1.0E10F % 1.0F;
		long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()),
				16);
		Color c = new Color((int) color);
		return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade,
				c.getAlpha() / 255.0F);
	}

	public static int getHexColor(EnumColor color) {
		if (color == EnumColor.Black) {
			return -16777216;
		}
		if (color == EnumColor.DarkBlue) {
			return -16777046;
		}
		if (color == EnumColor.DarkGreen) {
			return -16733696;
		}
		if (color == EnumColor.DarkAqua) {
			return -16733526;
		}
		if (color == EnumColor.DarkRed) {
			return -5636096;
		}
		if (color == EnumColor.DarkPurple) {
			return -5635926;
		}
		if (color == EnumColor.Gold) {
			return 43520;
		}
		if (color == EnumColor.Gray) {
			return -5592406;
		}
		if (color == EnumColor.DarkGray) {
			return -11184811;
		}
		if (color == EnumColor.Blue) {
			return -11184641;
		}
		if (color == EnumColor.Green) {
			return -11141291;
		}
		if (color == EnumColor.Aqua) {
			return -11141121;
		}
		if (color == EnumColor.Red) {
			return -43691;
		}
		if (color == EnumColor.Purple) {
			return -43521;
		}
		if (color == EnumColor.Yellow) {
			return 65365;
		}
		if (color == EnumColor.White) {
			return -1;
		}
		return -16777216;
	}

	public static Color glColor(int color, float alpha) {
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
		return new Color(red, green, blue, alpha);
	}

	public static void glColor(Color color) {
		GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F,
				color.getAlpha() / 255.0F);
	}

	public static Color glColor(int hex) {
		float alpha = (hex >> 24 & 0xFF) / 256.0F;
		float red = (hex >> 16 & 0xFF) / 255.0F;
		float green = (hex >> 8 & 0xFF) / 255.0F;
		float blue = (hex & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
		return new Color(red, green, blue, alpha);
	}
}
