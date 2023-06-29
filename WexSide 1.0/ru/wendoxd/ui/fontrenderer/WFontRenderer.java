package ru.wendoxd.ui.fontrenderer;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.visual.shaders.ShaderShell;

import java.awt.*;

public class WFontRenderer extends WFont {

	private final int[] colorCode = new int[32];
	protected WFont.CharData[] boldChars = new WFont.CharData[1110];
	protected WFont.CharData[] italicChars = new WFont.CharData[1110];
	protected WFont.CharData[] boldItalicChars = new WFont.CharData[1110];

	public WFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
		super(font, antiAlias, fractionalMetrics);

		for (int index = 0; index < 32; index++) {
			int noClue = (index >> 3 & 0x1) * 85;
			int red = (index >> 2 & 0x1) * 170 + noClue;
			int green = (index >> 1 & 0x1) * 170 + noClue;
			int blue = (index & 0x1) * 170 + noClue;

			if (index == 6) {
				red += 85;
			}

			if (index >= 16) {
				red /= 4;
				green /= 4;
				blue /= 4;
			}

			this.colorCode[index] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
		}
	}

	public void drawSubstringDefault(String text, float x, float y, int color, float maxWidth) {
		ShaderShell.FONTRENDERER_SUBSTRING.attach();
		ShaderShell.FONTRENDERER_SUBSTRING.set4F("inColor", (color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F,
				(color & 255) / 255F, (color >> 24 & 255) / 255F);
		ShaderShell.FONTRENDERER_SUBSTRING.set1F("width", maxWidth);
		ShaderShell.FONTRENDERER_SUBSTRING.set1F("maxWidth", (x + maxWidth) * 2);
		drawString(text, x, y, color);
		ShaderShell.FONTRENDERER_SUBSTRING.detach();
	}

	public void drawSubstring(String text, float x, float y, int color, float maxWidth) {
		ShaderShell.FONTRENDERER_SUBSTRING.attach();
		ShaderShell.FONTRENDERER_SUBSTRING.set4F("inColor", (color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F,
				(color & 255) / 255F, (color >> 24 & 255) / 255F);
		ShaderShell.FONTRENDERER_SUBSTRING.set1F("width", maxWidth);
		ShaderShell.FONTRENDERER_SUBSTRING.set1F("maxWidth", (MenuAPI.menuWindow.getX() + x + maxWidth) * 2);
		drawString(text, x, y, color);
		ShaderShell.FONTRENDERER_SUBSTRING.detach();
	}

	public static void drawStringWithOutline(WFontRenderer fontRenderer, String text, float x, float y, int color) {
		fontRenderer.drawString(text, x - 0.8F, y, Color.BLACK.getRGB());
		fontRenderer.drawString(text, x + 0.8F, y, Color.BLACK.getRGB());
		fontRenderer.drawString(text, x, y - 0.8F, Color.BLACK.getRGB());
		fontRenderer.drawString(text, x, y + 0.8F, Color.BLACK.getRGB());
		fontRenderer.drawString(text, x, y, color);
	}

	public void drawCenteredStringWithOutline(WFontRenderer fontRenderer, String text, float x, float y, int color) {
		drawCenteredString(text, x - 1, y, Color.BLACK.getRGB());
		drawCenteredString(text, x + 1, y, Color.BLACK.getRGB());
		drawCenteredString(text, x, y - 1, Color.BLACK.getRGB());
		drawCenteredString(text, x, y + 1, Color.BLACK.getRGB());
		drawCenteredString(text, x, y, color);
	}

	public float drawStringWithShadow(String text, double x, double y, int color) {
		float shadowWidth = this.drawString(text, (double) x + 0.5D, (double) y + 0.5D, color, true);
		return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
	}

	public float drawString(String text, float x, float y, int color) {
		return drawString(text, x, y, color, false);
	}

	public float drawCenteredString(String text, float x, float y, int color) {
		return drawString(text, x - getStringWidth(text) / 2F, y, color);
	}

	public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
		return drawStringWithShadow(text, x - getStringWidth(text) / 2F, y, color);
	}

	public float drawString(String text, double x, double y, int color, boolean shadow) {
		try {
			if (text == null) {
				return 0;
			}
			x -= 1;
			text = fixStr(text);
			GL11.glColor4f(1, 1, 1, 1);
			GlStateManager.color(1, 1, 1, 1);
			GL11.glEnable(GL11.GL_BLEND);

			if (text == null) {
				return 0;
			}

			if (color == 553648127) {
				color = 16777215;
			}

			if ((color & 0xFC000000) == 0) {
				color |= -16777216;
			}

			if (shadow) {
				color = (color & 0xFCFCFC) >> 2 | color & new Color(20, 20, 20, 200).getRGB();
			}

			float alpha = (color >> 24 & 0xFF) / 255.0F;
			x *= 2.0D;
			y = (y - 3.0D) * 2.0D;

			GL11.glPushMatrix();
			GlStateManager.scale(0.5, 0.5, 0.5);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			GlStateManager.color((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F, (color & 255) / 255F, alpha);
			GlStateManager.enableTexture2D();
			GlStateManager.bindTexture(tex.getGlTextureId());
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getGlTextureId());
			for (int i = 0; i < text.length(); i++) {
				char character = text.charAt(i);
				if (String.valueOf(character).equals("\247")) {
					char next = text.charAt(i + 1);
					int colorIndex = "0123456789abcdefklmnor".indexOf(next);
					if (next == 'r') {
						GlStateManager.color((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F,
								(color & 255) / 255F, alpha);
					} else if (colorIndex < 16) {
						GlStateManager.bindTexture(tex.getGlTextureId());

						if (colorIndex < 0) {
							colorIndex = 15;
						}

						if (shadow) {
							colorIndex += 16;
						}

						int colorcode = this.colorCode[colorIndex];
						GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F,
								(colorcode & 0xFF) / 255.0F, alpha);
					}
					i++;
				} else if (character < charData.length) {
					GL11.glBegin(GL11.GL_TRIANGLES);
					drawChar(charData, character, (float) x, (float) y);
					GL11.glEnd();
					x += charData[character].width - 8 + charOffset;
				}
			}

			GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
			GL11.glPopMatrix();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (float) x / 2;
	}

	public String fixStr(String input) {
		StringBuilder builder = new StringBuilder();
		char[] buffer = input.toCharArray();
		for (char c : buffer) {
			if ((int) c < this.charData.length && this.charData[(int) c] != null)
				builder.append(c);
		}
		return builder.toString();
	}

	@Override
	public int getStringWidth(String text) {
		int x = 0;
		if (text == null)
			return 0;
		text = fixStr(text);
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			if (character == '§') {
				i++;
			} else if (character < charData.length) {
				x += charData[character].width - 8 + charOffset;
			}
		}
		return x / 2;
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
	}

	@Override
	public void setAntiAlias(boolean antiAlias) {
		super.setAntiAlias(antiAlias);
	}

	@Override
	public void setFractionalMetrics(boolean fractionalMetrics) {
		super.setFractionalMetrics(fractionalMetrics);
	}

	private void drawLine(double x, double y, double x1, double y1, float width) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(width);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x1, y1);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void drawStringWithOutline(String text, double x, double y, int color) {
		drawString(text, x - 0.5, y, Color.BLACK.getRGB(), false);
		drawString(text, x + 0.5F, y, Color.BLACK.getRGB(), false);
		drawString(text, x, y - 0.5F, Color.BLACK.getRGB(), false);
		drawString(text, x, y + 0.5F, Color.BLACK.getRGB(), false);
		drawString(text, x, y, color, false);
	}

	public void drawCenteredStringWithOutline(String text, float x, float y, int color) {
		drawCenteredString(text, x - 0.5F, y, Color.BLACK.getRGB());
		drawCenteredString(text, x + 0.5F, y, Color.BLACK.getRGB());
		drawCenteredString(text, x, y - 0.5F, Color.BLACK.getRGB());
		drawCenteredString(text, x, y + 0.5F, Color.BLACK.getRGB());
		drawCenteredString(text, x, y, color);
	}
}