package ru.rockstar.api.utils.font;

import net.minecraft.client.MCFontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import ru.rockstar.api.utils.font.CFont.CharData;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class FontRenderer extends CFont {
	private final int[] colorCode = new int[32];
    protected CharData[] boldChars = new CharData[256];
    protected CharData[] italicChars = new CharData[256];
    protected CharData[] boldItalicChars = new CharData[256];
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;
    String colorcodeIdentifiers;

    public  FontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
    	super(font, antiAlias, fractionalMetrics);
        setupBoldItalicIDs();

        for (int index = 0; index < 32; index++) {
            int noClue = (index >> 3 & 0x1) * 85;
            int red = (index >> 2 & 0x1) * 170 + noClue;
            int green = (index >> 1 & 0x1) * 170 + noClue;
            int blue = (index & 0x1) * 170 + noClue;
            this.colorcodeIdentifiers = "0123456789abcdefklmnor\u0430\u0431\u0432\u0433\u0434\u0435\u0451\u0436\u0437\u0438\u0439\u043a\u043b\u043c\u043d\u043e\u043f\u0440\u0441\u0442";

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

    public void drawSmoothString(final String text, final double x2, final float y2, final int color) {
        this.drawString(text, x2, y2, color, false, 8.3f, true);
    }
    
    public void drawBlurredStringWithShadow(final String text, final double x, final double y, final int blurRadius, final Color blurColor, final int color) {
        GlStateManager.resetColor();
        DrawHelper.renderBlurredShadow((int)x, (int)y, this.getStringWidth(text), this.getFontHeight(), blurRadius, blurColor);
        this.drawStringWithShadow(text, (float)x, (float)y, color);
    }
    
    public void drawBlurredString(final String text, final double x, final double y, final int blurRadius, final Color blurColor, final int color) {
        GlStateManager.resetColor();
        DrawHelper.renderBlurredShadow((int)x, (int)y, this.getStringWidth(text), this.getFontHeight(), blurRadius, blurColor);
        this.drawString(text, (float)x, (float)y, color);
    }
    
    public void drawCenteredBlurredString(final String text, final double x, final double y, final int blurRadius, final Color blurColor, final int color) {
        GlStateManager.resetColor();
        DrawHelper.renderBlurredShadow((int)((int)x - this.getStringWidth(text) / 2.0f), (int)y, this.getStringWidth(text), this.getFontHeight(), blurRadius, blurColor);
        this.drawString(text, (float)(x - this.getStringWidth(text) / 2.0f), (float)y, color);
    }
    
    public void drawCenteredBlurredStringWithShadow(final String text, final double x, final double y, final int blurRadius, final Color blurColor, final int color) {
        GlStateManager.resetColor();
        DrawHelper.renderBlurredShadow((int)((int)x - this.getStringWidth(text) / 2.0f), (int)y, this.getStringWidth(text), this.getFontHeight(), blurRadius, blurColor);
        this.drawStringWithShadow(text, (float)(x - this.getStringWidth(text) / 2.0f), (float)y, color);
    }
    
    public void drawStringWithFade(final String s, final double x, final double y) {
        double updateX = x;
        for (int i = 0; i < s.length(); ++i) {
            final String str = new StringBuilder(String.valueOf(s.charAt(i))).toString();
            this.drawStringWithShadow(str, updateX, y, ClientHelper.getClientColor((float)(i - i + 1), (float)i, 0.7f, 30).getRGB());
            updateX += this.getStringWidth(new StringBuilder(String.valueOf(s.charAt(i))).toString()) + 0.4f;
        }
    }
    
    public static void drawStringWithOutline(final MCFontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        fontRenderer.drawString(text, x - 0.8f, y, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x + 0.8f, y, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y - 0.8f, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y + 0.8f, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y, color);
    }
    
    public static void drawStringWithOutline(final net.minecraft.client.gui.FontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        fontRenderer.drawString(text, x - 1.0f, y, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x + 1.0f, y, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y - 1.0f, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y + 1.0f, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y, color);
    }
    
    public static void drawCenteredStringWithOutline(final net.minecraft.client.gui.FontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        fontRenderer.drawCenteredString(text, x - 1.0f, y, Color.BLACK.getRGB());
        fontRenderer.drawCenteredString(text, x + 1.0f, y, Color.BLACK.getRGB());
        fontRenderer.drawCenteredString(text, x, y - 1.0f, Color.BLACK.getRGB());
        fontRenderer.drawCenteredString(text, x, y + 1.0f, Color.BLACK.getRGB());
        fontRenderer.drawCenteredString(text, x, y, color);
    }
    
    public static float drawCenteredStringWithShadow(final net.minecraft.client.gui.FontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        return (float)fontRenderer.drawString(text, x - fontRenderer.getStringWidth(text) / 2, y, color);
    }
    
    public void drawCenteredStringWithOutline(final MCFontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        this.drawCenteredString(text, x - 1.0f, y, Color.BLACK.getRGB());
        this.drawCenteredString(text, x + 1.0f, y, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y - 1.0f, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y + 1.0f, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y, color);
    }
    
    public float drawStringWithShadow(final String text, final double x, final double y, final int color) {
        final float shadowWidth = this.drawString(text, x + 0.9, y + 0.7, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }
    
    public float drawString(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x, y, color, false);
    }
    
    public float drawCenteredString(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x - this.getStringWidth(text) / 2.0f, y, color);
    }
    
    public float drawCenteredStringWithShadow(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x - this.getStringWidth(text) / 2, y, color);
    }
    
    public float drawString(final String text, double x, double y, int color, final boolean shadow, final float kerning, final boolean smooth) {
    	 if (text == null) {
             return 0;
         }

         if (shadow) {
             color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
         }

         CharData[] currentData = this.charData;
         float alpha = (float) (color >> 24 & 255) / 255f;
         boolean bold = false,
                 italic = false,
                 strikethrough = false,
                 underline = false;
         x = (x - 1) * 2;
         y = (y - 3) * 2;
         GL11.glPushMatrix();
         GlStateManager.scale(0.5, 0.5, 0.5);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
         DrawHelper.color(color);
         GlStateManager.enableTexture2D();
         GlStateManager.bindTexture(this.tex.getGlTextureId());
         DrawHelper.bindTexture(this.tex.getGlTextureId());
         if (smooth) {
             GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
             GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
         } else {
             GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
             GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
         }

         for (int index = 0; index < text.length(); index++) {
             char character = text.charAt(index);

             // If you need to apply color codes
             if (String.valueOf(character).equals("§")) {
                 int colorIndex = 21;

                 try {
                     colorIndex = colorcodeIdentifiers.indexOf(text.charAt(index + 1));
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

                 if (colorIndex < 16) {
                     bold = false;
                     italic = false;
                     underline = false;
                     strikethrough = false;
                     GlStateManager.bindTexture(this.tex.getGlTextureId());
                     currentData = this.charData;

                     if (colorIndex < 0) {
                         colorIndex = 15;
                     }

                     if (shadow) {
                         colorIndex += 16;
                     }

                     DrawHelper.color(this.colorCode[colorIndex], alpha);
                 } else {
                     switch (colorIndex) {
                         case 17:
                             bold = true;

                             if (italic) {
                                 GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                 currentData = this.boldItalicChars;
                             } else {
                                 GlStateManager.bindTexture(this.texBold.getGlTextureId());
                                 currentData = this.boldChars;
                             }
                             break;
                         case 18:
                             strikethrough = true;
                             break;
                         case 19:
                             underline = true;
                             break;
                         case 20:
                             italic = true;

                             if (bold) {
                                 GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                 currentData = this.boldItalicChars;
                             } else {
                                 GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                                 currentData = this.italicChars;
                             }
                             break;
                         default:
                             bold = false;
                             italic = false;
                             underline = false;
                             strikethrough = false;
                             DrawHelper.color(color);
                             GlStateManager.bindTexture(this.tex.getGlTextureId());
                             currentData = this.charData;
                             break;
                     }
                 }

                 ++index;
             } else if (character < currentData.length) {
                 drawLetter(x, y, currentData, strikethrough, underline, character);
                 x += currentData[character].width - kerning + this.charOffset;
             }
         }

         GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
         GL11.glPopMatrix();
         GL11.glColor4f(1, 1, 1, 1);
         GlStateManager.bindTexture(0);
         return (float) x / 2f;
    }
    
    private void drawLetter(final double x, final double y, final CharData[] currentData, final boolean strikethrough, final boolean underline, final char character) {
        GL11.glBegin(4);
        this.drawChar(currentData, character, (float)x, (float)y);
        GL11.glEnd();
        if (strikethrough) {
            this.drawLine(x, y + currentData[character].height / 2, x + currentData[character].width - 8.0, y + currentData[character].height / 2);
        }
        if (underline) {
            this.drawLine(x, y + currentData[character].height - 2.0, x + currentData[character].width - 8.0, y + currentData[character].height - 2.0);
        }
    }
    
    public float drawString(final String text, double x, double y, int color, final boolean shadow) {
    	 x -= 1.0;
         if (color == 0x20FFFFFF) {
             color = 0xFFFFFF;
         }
         if ((color & 0xFC000000) == 0) {
             color |= 0xFF000000;
         }
         if (shadow) {
             color = (color & 0xFCFCFC) >> 2 | color & new Color(20, 20, 20, 200).getRGB();
         }
         CharData[] currentData = this.charData;
         float alpha = (float) (color >> 24 & 0xFF) / 255.0f;
         boolean bold = false;
         boolean italic = false;
         boolean strikethrough = false;
         boolean underline = false;
         x *= 2;
         y = (y - 3) * 2;
         GL11.glPushMatrix();
         GlStateManager.scale(0.5, 0.5, 0.5);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
         GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
         int size = text.length();
         GlStateManager.enableTexture2D();
         GlStateManager.bindTexture(this.tex.getGlTextureId());
         GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.tex.getGlTextureId());
         int i = 0;
         while (i < size) {
             char character = text.charAt(i);
             if (String.valueOf(character).equals("�")) {
                 int colorIndex = 21;
                 try {
                     colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 if (colorIndex < 16) {
                     bold = false;
                     italic = false;
                     underline = false;
                     strikethrough = false;
                     GlStateManager.bindTexture(this.tex.getGlTextureId());
                     currentData = this.charData;
                     if (colorIndex < 0) {
                         colorIndex = 15;
                     }
                     if (shadow) {
                         colorIndex += 16;
                     }
                     int colorcode = this.colorCode[colorIndex];
                     GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0f, (colorcode >> 8 & 0xFF) / 255.0f, (colorcode & 0xFF) / 255.0f, alpha);
                 } else if (colorIndex == 17) {
                     bold = true;
                     if (italic) {
                         GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                         currentData = this.boldItalicChars;
                     } else {
                         GlStateManager.bindTexture(this.texBold.getGlTextureId());
                         currentData = this.boldChars;
                     }
                 } else if (colorIndex == 18) {
                     strikethrough = true;
                 } else if (colorIndex == 19) {
                     underline = true;
                 } else if (colorIndex == 20) {
                     italic = true;
                     if (bold) {
                         GlStateManager.bindTexture(texItalicBold.getGlTextureId());
                         currentData = boldItalicChars;
                     } else {
                         GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                         currentData = italicChars;
                     }
                 } else if (colorIndex == 21) {
                     bold = false;
                     italic = false;
                     underline = false;
                     strikethrough = false;
                     GlStateManager.color((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F, (color & 255) / 255F, alpha);
                     GlStateManager.bindTexture(this.tex.getGlTextureId());
                     currentData = this.charData;
                 }
                 ++i;
             } else if (character < currentData.length) {
                 GL11.glBegin(4);
                 this.drawChar(currentData, character, (float) x, (float) y);
                 GL11.glEnd();
                 if (strikethrough) {
                     this.drawLine(x, y + currentData[character].height / 2F, x + currentData[character].width - 8f, y + currentData[character].height / 2f, 1f);
                 }
                 if (underline) {
                     this.drawLine(x, y + currentData[character].height - 2.0, x + currentData[character].width - 8f, y + currentData[character].height - 2f, 1);
                 }
                 x += currentData[character].width - 8 + this.charOffset;
             }
             ++i;
         }
         GL11.glPopMatrix();
         return (float) (x / 2);
     }

    @Override
    public int getStringWidth(final String text) {
        int width = 0;
        CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        for (int size = text.length(), i = 0; i < size; ++i) {
            final char character = text.charAt(i);
            if (String.valueOf(character).equals("§")) {
                final int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                }
                else if (colorIndex == 17) {
                    bold = true;
                    currentData = (italic ? this.boldItalicChars : this.boldChars);
                }
                else if (colorIndex == 20) {
                    italic = true;
                    currentData = (bold ? this.boldItalicChars : this.italicChars);
                }
                else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i;
            }
            else if (character < currentData.length) {
                width += currentData[character].width - 8 + this.charOffset;
            }
        }
        return width / 2;
    }
    
    @Override
    public void setFont(final Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }
    
    @Override
    public void setAntiAlias(final boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }
    
    @Override
    public void setFractionalMetrics(final boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }
    
    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }
    
    public void drawSmoothStringWithShadow(final String text, final double x2, final float y2, final int color) {
        this.drawString(text, x2 + 0.5, y2 + 0.5f, color, true, 8.3f, true);
        this.drawString(text, x2, y2, color, false, 8.3f, true);
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
    
    private void drawLine(final double x2, final double y2, final double x1, final double y1) {
        GL11.glDisable(3553);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }
    
    public void drawStringWithOutline(final String text, final double x, final double y, final int color) {
        this.drawString(text, x - 0.5, y, Color.BLACK.getRGB(), false);
        this.drawString(text, x + 0.5, y, Color.BLACK.getRGB(), false);
        this.drawString(text, x, y - 0.5, Color.BLACK.getRGB(), false);
        this.drawString(text, x, y + 0.5, Color.BLACK.getRGB(), false);
        this.drawString(text, x, y, color, false);
    }
    
    public void drawCenteredStringWithOutline(final String text, final float x, final float y, final int color) {
        this.drawCenteredString(text, x - 0.5f, y, Color.BLACK.getRGB());
        this.drawCenteredString(text, x + 0.5f, y, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y - 0.5f, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y + 0.5f, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y, color);
    }
}