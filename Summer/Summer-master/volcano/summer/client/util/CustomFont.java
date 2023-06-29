package volcano.summer.client.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class CustomFont {
	public static CustomFont INSTANCE;
	private Font theFont;
	private Graphics2D theGraphics;
	private FontMetrics theMetrics;
	private float fontSize;
	private int startChar;
	private int endChar;
	private float[] xPos;
	private float[] yPos;
	private BufferedImage bufferedImage;
	private float extraSpacing = 0.0F;
	private DynamicTexture dynamicTexture;
	private ResourceLocation resourceLocation;
	private final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OG]");
	private final Pattern patternUnsupported = Pattern.compile("(?i)\\u00A7[K-O]");

	public CustomFont() {
		INSTANCE = this;
		this.italicChars = new CharData[256];
		this.boldItalicChars = new CharData[256];
		this.colorCode = new int[32];
	}

	public CustomFont(Object font, float size) {
		this(font, size, 0.0F);
	}

	public CustomFont(Object font) {
		this(font, 18.0F, 0.0F);
	}

	protected CharData[] boldChars;
	protected CharData[] italicChars;
	protected CharData[] boldItalicChars;
	private final int[] colorCode;
	private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
	protected DynamicTexture texBold;
	protected DynamicTexture texItalic;
	protected DynamicTexture texItalicBold;

	public CustomFont(Object font, float size, float spacing) {
		this.fontSize = size;
		this.startChar = 32;
		this.endChar = 255;
		this.extraSpacing = spacing;
		this.xPos = new float[this.endChar - this.startChar];
		this.yPos = new float[this.endChar - this.startChar];
		setupGraphics2D();
		createFont(font, size);
		this.imgSize = 512.0f;
		this.charData = new CharData[256];
		this.fontHeight = -1;
		this.charOffset = 0;
		this.boldChars = new CharData[256];
		this.italicChars = new CharData[256];
		this.boldItalicChars = new CharData[256];
		this.colorCode = new int[32];
	}

	private final void setupGraphics2D() {
		this.bufferedImage = new BufferedImage(256, 256, 2);
		this.theGraphics = ((Graphics2D) this.bufferedImage.getGraphics());
		this.theGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}

	private final void createFont(Object font, float size) {
		try {
			if ((font instanceof Font)) {
				this.theFont = ((Font) font);
			} else if ((font instanceof File)) {
				this.theFont = Font.createFont(0, (File) font).deriveFont(size);
			} else if ((font instanceof InputStream)) {
				this.theFont = Font.createFont(0, (InputStream) font).deriveFont(size);
			} else if ((font instanceof String)) {
				this.theFont = new Font((String) font, 0, Math.round(size));
			} else {
				this.theFont = new Font("Verdana", 0, Math.round(size));
			}
			this.theGraphics.setFont(this.theFont);
		} catch (Exception e) {
			e.printStackTrace();
			this.theFont = new Font("Verdana", 0, Math.round(size));
			this.theGraphics.setFont(this.theFont);
		}
		this.theGraphics.setColor(new Color(255, 255, 255, 0));
		this.theGraphics.fillRect(0, 0, 256, 256);
		this.theGraphics.setColor(Color.white);
		this.theMetrics = this.theGraphics.getFontMetrics();

		float x = 5.0F;
		float y = 5.0F;
		for (int i = this.startChar; i < this.endChar; i++) {
			this.theGraphics.drawString(Character.toString((char) i), x, y + this.theMetrics.getAscent());
			this.xPos[(i - this.startChar)] = x;
			this.yPos[(i - this.startChar)] = (y - this.theMetrics.getMaxDescent());
			x += this.theMetrics.stringWidth(Character.toString((char) i)) + 2.0F;
			if (x >= 250 - this.theMetrics.getMaxAdvance()) {
				x = 5.0F;
				y += this.theMetrics.getMaxAscent() + this.theMetrics.getMaxDescent() + this.fontSize / 2.0F;
			}
		}
		this.resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(
				"font" + font.toString() + size, this.dynamicTexture = new DynamicTexture(this.bufferedImage));
	}

	public final void drawString(String text, float x, float y, FontType fontType, int color, int color2) {
		GL11.glPushMatrix();
		text = stripUnsupported(text);

		GL11.glEnable(3042);
		GL11.glEnable(3042);
		GL11.glEnable(2848);
		GL11.glDisable(3553);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glDisable(3008);
		GL11.glBlendFunc(770, 771);
		GL11.glShadeModel(7425);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		String text2 = stripControlCodes(text);
		switch (fontType.ordinal()) {
		case 1:
			drawer(text2, x + 0.5F, y, color2);
			drawer(text2, x - 0.5F, y, color2);
			drawer(text2, x, y + 0.5F, color2);
			drawer(text2, x, y - 0.5F, color2);
			break;
		case 2:
			drawer(text2, x + 0.5F, y + 0.5F, color2);
			break;
		case 3:
			drawer(text2, x + 0.5F, y + 1.0F, color2);
			break;
		case 4:
			drawer(text2, x, y + 0.5F, color2);
			break;
		case 5:
			drawer(text2, x, y - 0.5F, color2);
		}
		drawer(text, x, y, color);
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		GL11.glShadeModel(7424);
		GL11.glDisable(3042);
		GL11.glEnable(3008);
		GL11.glEnable(3553);
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glPopMatrix();
	}

	public void drawCenteredString(String text, float x, float y, int color) {
		drawString(text, x - getStringWidth(text) / 2.0F, y, FontType.SHADOW_THIN, color);
	}

	public final void drawString(String text, float x, float y, FontType fontType, int color) {
		drawString(text, x, y, fontType, color, -1157627904);
	}

	public int drawString(String text, float x, float y, int color) {
		return (int) this.drawString(text, x, y, color, false);
	}

	protected class CharData {
		public int width;
		public int height;
		public int storedX;
		public int storedY;
	}

	private float imgSize;
	protected CharData[] charData;
	protected Font font;
	protected boolean antiAlias;
	protected boolean fractionalMetrics;
	protected int fontHeight;
	protected int charOffset;
	protected DynamicTexture tex;

	public float drawString(final String text, double x, double y, int color, final boolean shadow) {
		--x;
		if (text == null) {
			return 0.0f;
		}
		if (color == 553648127) {
			color = 16777215;
		}
		if ((color & 0xFC000000) == 0x0) {
			color |= 0xFF000000;
		}
		if (shadow) {
			color = ((color & 0xFCFCFC) >> 2 | (color & 0xFF000000));
		}
		CharData[] currentData = this.charData;
		final float alpha = (color >> 24 & 0xFF) / 255.0f;
		boolean randomCase = false;
		boolean bold = false;
		boolean italic = false;
		boolean strikethrough = false;
		boolean underline = false;
		final boolean render = true;
		x *= 2.0;
		y = (y - 3.0) * 2.0;
		if (render) {
			GL11.glPushMatrix();
			GlStateManager.scale(0.5, 0.5, 0.5);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f,
					alpha);
			final int size = text.length();
			GlStateManager.func_179098_w();
			GlStateManager.func_179144_i(this.tex.getGlTextureId());
			GL11.glBindTexture(3553, this.tex.getGlTextureId());
			for (int i = 0; i < size; ++i) {
				final char character = text.charAt(i);
				if (character == '§' && i < size) {
					int colorIndex = 21;
					try {
						colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (colorIndex < 16) {
						bold = false;
						italic = false;
						randomCase = false;
						underline = false;
						strikethrough = false;
						GlStateManager.func_179144_i(this.tex.getGlTextureId());
						currentData = this.charData;
						if (colorIndex < 0 || colorIndex > 15) {
							colorIndex = 15;
						}
						if (shadow) {
							colorIndex += 16;
						}
						final int colorcode = this.colorCode[colorIndex];
						GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0f, (colorcode >> 8 & 0xFF) / 255.0f,
								(colorcode & 0xFF) / 255.0f, alpha);
					} else if (colorIndex == 16) {
						randomCase = true;
					} else if (colorIndex == 17) {
						bold = true;
						if (italic) {
							GlStateManager.func_179144_i(this.texItalicBold.getGlTextureId());
							currentData = this.boldItalicChars;
						} else {
							GlStateManager.func_179144_i(this.texBold.getGlTextureId());
							currentData = this.boldChars;
						}
					} else if (colorIndex == 18) {
						strikethrough = true;
					} else if (colorIndex == 19) {
						underline = true;
					} else if (colorIndex == 20) {
						italic = true;
						if (bold) {
							GlStateManager.func_179144_i(this.texItalicBold.getGlTextureId());
							currentData = this.boldItalicChars;
						} else {
							GlStateManager.func_179144_i(this.texItalic.getGlTextureId());
							currentData = this.italicChars;
						}
					} else if (colorIndex == 21) {
						bold = false;
						italic = false;
						randomCase = false;
						underline = false;
						strikethrough = false;
						GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f,
								(color & 0xFF) / 255.0f, alpha);
						GlStateManager.func_179144_i(this.tex.getGlTextureId());
						currentData = this.charData;
					}
					++i;
				} else if (character < currentData.length && character >= '\0') {
					GL11.glBegin(4);
					this.drawChar(character, (float) x, (float) y);
					GL11.glEnd();
					if (strikethrough) {
						this.drawLine(x, y + currentData[character].height / 2, x + currentData[character].width - 8.0,
								y + currentData[character].height / 2, 1.0f);
					}
					if (underline) {
						this.drawLine(x, y + currentData[character].height - 2.0,
								x + currentData[character].width - 8.0, y + currentData[character].height - 2.0, 1.0f);
					}
					x += currentData[character].width - 8 + this.charOffset;
				}
			}
			GL11.glHint(3155, 4352);
			GL11.glPopMatrix();
		}
		setColor(Color.WHITE);
		return (float) x / 2.0f;
	}

	public static void setColor(Color c) {
		GL11.glColor4d(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	}

	private void drawLine(final double x, final double y, final double x1, final double y1, final float width) {
		GL11.glDisable(3553);
		GL11.glLineWidth(width);
		GL11.glBegin(1);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x1, y1);
		GL11.glEnd();
		GL11.glEnable(3553);
	}

	private final void drawer(String text, float x, float y, int color) {
		x *= 2.0F;
		y *= 2.0F;
		GL11.glEnable(3553);
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);

		float alpha = (color >> 24 & 0xFF) / 255.0F;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
		float startX = x;
		for (int i = 0; i < text.length(); i++) {
			if ((text.charAt(i) == '§') && (i + 1 < text.length())) {
				char oneMore = Character.toLowerCase(text.charAt(i + 1));
				if (oneMore == 'n') {
					y += this.theMetrics.getAscent() + 2;
					x = startX;
				}
				int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
				if (colorCode < 16) {
					try {
						int newColor = Minecraft.getMinecraft().fontRendererObj.colorCode[colorCode];
						GL11.glColor4f((newColor >> 16) / 255.0F, (newColor >> 8 & 0xFF) / 255.0F,
								(newColor & 0xFF) / 255.0F, alpha);
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				} else if (oneMore == 'f') {
					GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
				} else if (oneMore == 'r') {
					GL11.glColor4f(red, green, blue, alpha);
				} else if (oneMore == 'g') {
					GL11.glColor4f(0.47F, 0.67F, 0.27F, alpha);
				}
				i++;
			} else {
				try {
					char c = text.charAt(i);
					drawChar(c, x, y);
					x += getStringWidth(Character.toString(c)) * 2.0F;
				} catch (ArrayIndexOutOfBoundsException indexException) {
					char c = text.charAt(i);
					System.out.println("Can't draw character: " + c + " (" + Character.getNumericValue(c) + ")");
				}
			}
		}
	}

	public final float getStringWidth(String text) {
		return (float) (getBounds(text).getWidth() + this.extraSpacing) / 2.0F;
	}

	public final float getStringHeight(String text) {
		return (float) getBounds(text).getHeight() / 2.0F;
	}

	private final Rectangle2D getBounds(String text) {
		return this.theMetrics.getStringBounds(text, this.theGraphics);
	}

	private final void drawChar(char character, float x, float y) throws ArrayIndexOutOfBoundsException {
		Rectangle2D bounds = this.theMetrics.getStringBounds(Character.toString(character), this.theGraphics);
		drawTexturedModalRect(x, y, this.xPos[(character - this.startChar)], this.yPos[(character - this.startChar)],
				(float) bounds.getWidth(), (float) bounds.getHeight() + this.theMetrics.getMaxDescent() + 1.0F);
	}

	private final List listFormattedStringToWidth(String s, int width) {
		return Arrays.asList(wrapFormattedStringToWidth(s, width).split("\n"));
	}

	private final String wrapFormattedStringToWidth(String s, float width) {
		int wrapWidth = sizeStringToWidth(s, width);
		if (s.length() <= wrapWidth) {
			return s;
		}
		String split = s.substring(0, wrapWidth);
		String split2 = getFormatFromString(split)
				+ s.substring(wrapWidth + ((s.charAt(wrapWidth) == ' ') || (s.charAt(wrapWidth) == '\n') ? 1 : 0));
		try {
			return split + "\n" + wrapFormattedStringToWidth(split2, width);
		} catch (Exception e) {
			System.out.println("Cannot wrap string to width.");
		}
		return "";
	}

	private final int sizeStringToWidth(String par1Str, float par2) {
		int var3 = par1Str.length();
		float var4 = 0.0F;
		int var5 = 0;
		int var6 = -1;
		for (boolean var7 = false; var5 < var3; var5++) {
			char var8 = par1Str.charAt(var5);
			switch (var8) {
			case '\n':
				var5--;
				break;
			case '§':
				if (var5 < var3 - 1) {
					var5++;
					char var9 = par1Str.charAt(var5);
					if ((var9 != 'l') && (var9 != 'L')) {
						if ((var9 == 'r') || (var9 == 'R') || (isFormatColor(var9))) {
							var7 = false;
						}
					} else {
						var7 = true;
					}
				}
				break;
			case ' ':
				var6 = var5;
			case '-':
				var6 = var5;
			case '_':
				var6 = var5;
			case ':':
				var6 = var5;
			default:
				String text = String.valueOf(var8);
				var4 += getStringWidth(text);
				if (var7) {
					var4 += 1.0F;
				}
				break;
			}
			if (var8 == '\n') {
				var5++;
				var6 = var5;
			} else {
				if (var4 > par2) {
					break;
				}
			}
		}
		return (var5 != var3) && (var6 != -1) && (var6 < var5) ? var6 : var5;
	}

	private final String getFormatFromString(String par0Str) {
		String var1 = "";
		int var2 = -1;
		int var3 = par0Str.length();
		while ((var2 = par0Str.indexOf('§', var2 + 1)) != -1) {
			if (var2 < var3 - 1) {
				char var4 = par0Str.charAt(var2 + 1);
				if (isFormatColor(var4)) {
					var1 = "§" + var4;
				} else if (isFormatSpecial(var4)) {
					var1 = var1 + "§" + var4;
				}
			}
		}
		return var1;
	}

	private final boolean isFormatColor(char par0) {
		return ((par0 >= '0') && (par0 <= '9')) || ((par0 >= 'a') && (par0 <= 'f')) || ((par0 >= 'A') && (par0 <= 'F'));
	}

	private final boolean isFormatSpecial(char par0) {
		return ((par0 >= 'k') && (par0 <= 'o')) || ((par0 >= 'K') && (par0 <= 'O')) || (par0 == 'r') || (par0 == 'R');
	}

	private final void drawTexturedModalRect(float x, float y, float u, float v, float width, float height) {
		float scale = 0.0039063F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.startDrawingQuads();
		renderer.addVertexWithUV(x + 0.0F, y + height, 0.0D, (u + 0.0F) * scale, (v + height) * scale);
		renderer.addVertexWithUV(x + width, y + height, 0.0D, (u + width) * scale, (v + height) * scale);
		renderer.addVertexWithUV(x + width, y + 0.0F, 0.0D, (u + width) * scale, (v + 0.0F) * scale);
		renderer.addVertexWithUV(x + 0.0F, y + 0.0F, 0.0D, (u + 0.0F) * scale, (v + 0.0F) * scale);
		tessellator.draw();
	}

	public final String stripControlCodes(String s) {
		return this.patternControlCode.matcher(s).replaceAll("");
	}

	public final String stripUnsupported(String s) {
		return this.patternUnsupported.matcher(s).replaceAll("");
	}

	public final Graphics2D getGraphics() {
		return this.theGraphics;
	}

	public final Font getFont() {
		return this.theFont;
	}

	public static enum FontType {
		NORMAL, SHADOW_THICK, SHADOW_THIN, OUTLINE_THIN, EMBOSS_TOP, EMBOSS_BOTTOM;
	}

	public int drawCenteredStringWithShadow(String text, float x, float y, int color) {
		x -= this.getStringWidth(text) / 2;
		y -= this.getStringHeight(text) / 2;
		final float shadowWidth = this.drawString(text, x + 1.0, y + 1.0, color, true);
		return (int) Math.max(shadowWidth, this.drawString(text, x, y, color, false));
	}

}
