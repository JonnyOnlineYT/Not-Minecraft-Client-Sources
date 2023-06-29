package org.spray.heaven.font;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.util.StringUtil;
import org.spray.heaven.util.render.Drawable;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

/**
 * FontRendering by KamiBlue team. Ported and Recoded - sprayD
 * 
 */
public class FontRenderer {

	private Tessellator tessellator;
	private BufferBuilder buffer;

	private FontGlyphs[] glyphArray;
	private FontGlyphs currentVariant;
	private Color currentColor;

	private String[] fallbackFonts;
	private float size = 1f;
	private float gap = 0f;

	public FontGlyphs[] getGlyphArray() {
		return glyphArray;
	}

	public void reloadFonts(String name, boolean local) {
		int i = 0;

		for (int i2 = glyphArray.length; i < i2; ++i) {
			glyphArray[i].destroy();
			glyphArray[i] = this.loadFont(name, i, local);
		}

	}

	private FontGlyphs loadFont(String fontName, int index, boolean local) {
		TextProperties.Style style = TextProperties.Style.values()[index];

		Font font;

		try {
			if (local)
				font = Font.createFont(Font.TRUETYPE_FONT, FontRenderer.class.getResourceAsStream(fontName))
						.deriveFont(style.getStyleConst(), 32);
			else
				font = new Font(fontName, style.getStyleConst(), 32);
		} catch (Exception e) {
			e.printStackTrace();
			font = this.getSansSerifFont(style.getStyleConst());
		}

		Font fallbackFont;
		try {
			fallbackFont = new Font(this.getFallbackFont(), style.getStyleConst(), 32);
		} catch (Exception e) {
			e.printStackTrace();
			fallbackFont = this.getSansSerifFont(style.getStyleConst());
		}

		return new FontGlyphs(style, font, fallbackFont);
	}

	private String getFallbackFont() {
		int index = 0;

		String fallbackFont;
		while (true) {
			if (index >= fallbackFonts.length) {
				fallbackFont = null;
				break;
			}

			fallbackFont = fallbackFonts[index];

			++index;
		}

		return fallbackFont;
	}

	private Font getSansSerifFont(int style) {
		return new Font("SansSerif", style, 32);
	}

	public void drawBlurredCenteredString(String text, double x, double y, int blurRadius, Color blurColor, int color) {
		drawBlurredString(text, x - getStringWidth(text) / 2, y, blurRadius, blurColor, color);
	}

	public void drawBlurredString(String text, double x, double y, int blurRadius, Color blurColor, int color) {
		Drawable.drawBlurredShadow((int) x, (int) y, (int) getStringWidth(text), (int) getFontHeight(), blurRadius,
				blurColor);
		drawString(text, x, y, false, color, 1f);
	}

	public int drawString(String text, double x, double y, boolean shadow, int color, float scale) {
		return drawString(text, (float) x, (float) y, shadow, color, scale);
	}
	
	public int drawString(String text, double x, double y, boolean shadow, int color) {
		return drawString(text, (float) x, (float) y, shadow, color, 1f);
	}

	public int drawString(String text, double x, double y, int color, float scale) {
		return drawString(text, x, y, false, color, scale);
	}

	public int drawString(String text, double x, double y, int color) {
		return drawString(text, x, y, false, color, 1f);
	}

	public void drawCenteredString(String text, double x, double y, int color, float scale) {
		drawString(text, x - getStringWidth(text, scale) / 2, y, color, scale);
	}
	
	public void drawCenteredStringWithShadow(String text, double x, double y, int color, float scale) {
		drawString(text, x - getStringWidth(text, scale) / 2, y, true, color, scale);
	}

	public void drawStringWithShadow(String text, double x, double y, int color, float scale) {
		drawString(text, x, y, true, color, scale);
	}

	public void drawCenteredString(String text, double x, double y, int color) {
		drawString(text, x - getStringWidth(text) / 2, y, color, 1f);
	}

	public void drawVCenteredString(String text, double x, double y, double height, int color) {
		drawString(text, x, y + (height - getFontHeight()) / 2, color, 1f);
	}
	
	public void drawVCenteredString(String text, double x, double y, double height, boolean shadow, int color) {
		drawString(text, x, y + (height - getFontHeight()) / 2, shadow, color, 1f);
	}

	public void drawVCenteredStringWithShadow(String text, double x, double y, double height, int color) {
		drawStringWithShadow(text, x, y + (height - getFontHeight()) / 2, color, 1f);
	}

	public void drawVCenteredString(String text, double x, double y, double height, int color, float scale) {
		drawString(text, x, y + (height - getFontHeight(scale)) / 2, color, scale);
	}

	public void drawVCenteredStringWithShadow(String text, double x, double y, double height, int color, float scale) {
		drawStringWithShadow(text, x, y + (height - getFontHeight(scale)) / 2, color, scale);
	}

	public void drawHVCenteredString(String text, double x, double y, double height, int color, float scale) {
		drawVCenteredString(text, x - getStringWidth(text, scale) / 2, y, height, color, scale);
	}

	public void drawHVCenteredStringWithShadow(String text, double x, double y, double height, int color, float scale) {
		drawVCenteredStringWithShadow(text, x - getStringWidth(text, scale) / 2, y, height, color, scale);
	}

	public void drawHVCenteredString(String text, double x, double y, double height, int color) {
		drawVCenteredString(text, x - getStringWidth(text) / 2, y, height, color, 1f);
	}

	public void drawHVCenteredStringWithShadow(String text, double x, double y, double height, int color) {
		drawHVCenteredStringWithShadow(text, x, y, height, color, 1f);
	}

	public int drawStringWithShadow(String text, double x, double y, int color) {
		return drawString(text, x, y, true, color, 1f);
	}

	public int drawString(String text, float posXIn, float posYIn, boolean drawShadow, int colorIn, float scale) {
		double posX = 0.0D;
		double posY = 0.0D;
		GlStateManager.disableOutlineMode();
		GlStateManager.enableTexture2D();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GL11.glPushMatrix();
		GL11.glTranslatef(posXIn, posYIn, 0.0F);
		GL11.glScalef(size * scale, size * scale, 1.0F);
		GL11.glTranslatef(0.0F, 0.0F, 0.0F);
		this.resetStyle();
		CharSequence chr = (CharSequence) text;

		for (int index = 0; index < chr.length(); ++index) {
			char var12 = chr.charAt(index);
			if (!this.checkStyleCode(text, index)) {
				FontGlyphs.GlyphChunk chunk = currentVariant.getChunk(var12);
				FontGlyphs.CharInfo charInfo = currentVariant.getCharInfo(var12);
				int color = currentColor.equals(new Color(255, 255, 255)) ? colorIn : currentColor.getRGB();

		        float f3 = (float)(color >> 24 & 255) / 255.0F;
		        float f = (float)(color >> 16 & 255) / 255.0F;
		        float f1 = (float)(color >> 8 & 255) / 255.0F;
		        float f2 = (float)(color & 255) / 255.0F;
				GlStateManager.bindTexture(chunk.getTextureId());
				GL11.glTexParameteri(3553, 10242, 33071);
				GL11.glTexParameteri(3553, 10243, 33071);
				GL11.glTexParameteri(3553, 10240, 9728);
				GL11.glTexParameteri(3553, 10241, 9987);
				GL11.glTexParameterf(3553, 34049, 2.0f * 0.5f - 1.25f);
				if (var12 == '\n') {
					posY += (double) (currentVariant.getFontHeight() * 0);
					posX = 0.0D;
				} else {
					if (drawShadow) {
						Color shadowColor = this.getShadowColor(new Color(color));
						GL11.glColor4f(shadowColor.getRed() / 255.0f, shadowColor.getGreen() / 255.0f,
								shadowColor.getBlue() / 255.0f, shadowColor.getAlpha() / 255.0f);
						this.drawQuad(posX + 2.6D, posY + 2.6D, charInfo);
					}

					GL11.glColor4f(f, f1, f2, f3);
					this.drawQuad(posX, posY, charInfo);
					posX += charInfo.getWidth() + gap;
				}
			}
		}

		this.resetStyle();
		GL11.glPopMatrix();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.bindTexture(0);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 1000);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LOD, 1000);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_LOD, -1000);
		GlStateManager.color(1f, 1f, 1f, 1f);
		return (int) posX;
	}

	private Color getShadowColor(Color color) {
		return new Color((int) ((float) color.getRed() * 0.1F), (int) ((float) color.getGreen() * 0.1F),
				(int) ((float) color.getBlue() * 0.1F), (int) ((float) color.getAlpha() * 0.9F));
	}

	private void drawQuad(double posX, double posY, FontGlyphs.CharInfo charInfo) {
		buffer.begin(5, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(posX, posY, 0.0D).tex(charInfo.getU1(), charInfo.getV1()).endVertex();
		buffer.pos(posX, posY + charInfo.getHeight(), 0.0D).tex(charInfo.getU1(), charInfo.getV2()).endVertex();
		buffer.pos(posX + charInfo.getWidth(), posY, 0.0D).tex(charInfo.getU2(), charInfo.getV1()).endVertex();
		buffer.pos(posX + charInfo.getWidth(), charInfo.getHeight(), 0.0D).tex(charInfo.getU2(), charInfo.getV2())
				.endVertex();
		tessellator.draw();
	}

	public float getFontHeight(float scale) {
		return glyphArray[0].getFontHeight() * size * scale;
	}

	public float getFontHeight() {
		return getFontHeight(1f);
	}

	public float getStringWidth(String text, float scale) {
		double width = 0.0D;
		this.resetStyle();
		CharSequence chr = (CharSequence) text;

		for (int index = 0; index < chr.length(); ++index) {
			if (!this.checkStyleCode(text, index)) {
				width += currentVariant.getCharInfo(chr.charAt(index)).getWidth() + gap;
			}
		}
		this.resetStyle();
		return (float) (width * size * scale);
	}

	public float getStringWidth(String text) {
		return getStringWidth(text, 1f);
	}

	public String trimStringToWidth(String text, int width) {
		return this.trimStringToWidth(text, width, false);
	}

	public String trimStringToWidth(String text, int width, boolean reverse) {
		StringBuilder stringbuilder = new StringBuilder();

		float f = 0.0F;
		int i = reverse ? text.length() - 1 : 0;
		int j = reverse ? -1 : 1;

		boolean flag = false;
		boolean flag1 = false;

		for (int k = i; k >= 0 && k < text.length() && f < (float) width; k += j) {
			char c0 = text.charAt(k);
			float f1 = getStringWidth(Character.toString(c0));

			if (flag) {
				flag = false;

				if (c0 != 'l' && c0 != 'L') {
					if (c0 == 'r' || c0 == 'R') {
						flag1 = false;
					}
				} else {
					flag1 = true;
				}
			} else if (f1 < 0.0F) {
				flag = true;
			} else {
				f += f1;

				if (flag1) {
					++f;
				}
			}

			if (f > width) {
				break;
			}

			if (reverse) {
				stringbuilder.insert(0, c0);
			} else {
				stringbuilder.append(c0);
			}
		}

		return stringbuilder.toString();
	}

	private void resetStyle() {
		currentVariant = glyphArray[0];
		currentColor = Color.WHITE;
	}

	private boolean checkStyleCode(String text, int index) {
		Character character = StringUtil.getOrNull((CharSequence) text, index - 1);
		if (character != null) {
			if (character == 167) {
				return true;
			}
		}

		character = StringUtil.getOrNull((CharSequence) text, index);
		if (character != null) {
			if (character == 167) {
				Character var3;
				char chr;
				label182: {
					var3 = StringUtil.getOrNull((CharSequence) text, index + 1);
					chr = TextProperties.Style.REGULAR.getCodeChar();
					if (var3 != null) {
						if (var3 == chr) {
							currentVariant = glyphArray[0];
							break label182;
						}
					}

					chr = TextProperties.Style.BOLD.getCodeChar();
					if (var3 != null) {
						if (var3 == chr) {
							currentVariant = glyphArray[1];
							break label182;
						}
					}

					chr = TextProperties.Style.ITALIC.getCodeChar();
					if (var3 != null) {
						if (var3 == chr) {
							currentVariant = glyphArray[2];
						}
					}
				}

				Color color;
				label183: {
					var3 = StringUtil.getOrNull((CharSequence) text, index + 1);
					chr = TextFormatting.BLACK.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(0, 0, 0);
							break label183;
						}
					}

					chr = TextFormatting.DARK_BLUE.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(0, 0, 170);
							break label183;
						}
					}

					chr = TextFormatting.DARK_GREEN.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(0, 170, 0);
							break label183;
						}
					}

					chr = TextFormatting.DARK_AQUA.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(0, 170, 170);
							break label183;
						}
					}

					chr = TextFormatting.DARK_RED.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(170, 0, 0);
							break label183;
						}
					}

					chr = TextFormatting.DARK_PURPLE.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(170, 0, 170);
							break label183;
						}
					}

					chr = TextFormatting.GOLD.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(250, 170, 0);
							break label183;
						}
					}

					chr = TextFormatting.GRAY.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(170, 170, 170);
							break label183;
						}
					}

					chr = TextFormatting.DARK_GRAY.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(85, 85, 85);
							break label183;
						}
					}

					chr = TextFormatting.BLUE.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(85, 85, 255);
							break label183;
						}
					}

					chr = TextFormatting.GREEN.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(85, 255, 85);
							break label183;
						}
					}

					chr = TextFormatting.AQUA.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(85, 255, 255);
							break label183;
						}
					}

					chr = TextFormatting.RED.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(255, 85, 85);
							break label183;
						}
					}

					chr = TextFormatting.LIGHT_PURPLE.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(255, 85, 255);
							break label183;
						}
					}

					chr = TextFormatting.YELLOW.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(255, 255, 85);
							break label183;
						}
					}

					chr = TextFormatting.WHITE.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(254, 255, 255);
							break label183;
						}
					}

					chr = TextFormatting.RESET.toString().charAt(1);
					if (var3 != null) {
						if (var3 == chr) {
							color = new Color(255, 255, 255);
							break label183;
						}
					}

					color = currentColor;
				}

				currentColor = color;
				return true;
			}
		}

		return false;
	}

	public FontRenderer(String fontName, float size, float gap, boolean local) {
		tessellator = Tessellator.getInstance();
		buffer = tessellator.getBuffer();
		currentColor = new Color(255, 255, 255);
		this.size = size;
		this.gap = gap;

		fallbackFonts = new String[] { "Umba Sans", "Noto Sans JP", "Noto Sans CJK JP", "Noto Sans CJK JP",
				"Noto Sans CJK KR", "Noto Sans CJK SC", "Noto Sans CJK TC", "Source Han Sans", "Source Han Sans HC",
				"Source Han Sans SC", "Source Han Sans TC", "Source Han Sans K", "MS Gothic", "Meiryo", "Yu Gothic",
				"Hiragino Sans GB W3", "Hiragino Kaku Gothic Pro W3", "Hiragino Kaku Gothic ProN W3", "Osaka",
				"TakaoPGothic", "IPAPGothic" };

		byte index = 3;
		FontGlyphs[] fontGlyphs = new FontGlyphs[index];

		for (int i = 0; i < index; ++i) {
			fontGlyphs[i] = loadFont(fontName, i, local);
		}

		glyphArray = fontGlyphs;
		currentVariant = glyphArray[0];
	}

	public float getSize() {
		return size;
	}

}