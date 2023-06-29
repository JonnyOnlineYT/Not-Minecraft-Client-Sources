package org.moonware.client.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.ArrayList;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.ArrayListGlowPallete;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render2.RenderHelper2;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.moonware.client.feature.impl.hud.ArrayGlowComp.ArrayList.onecolor;
import static org.moonware.client.feature.impl.hud.ArrayGlowComp.ArrayList.twocolor;

public class CustomFont extends Gui {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,/\\:;?!@\"'%^&*()[]<>{}=+-_~`#№$АБВГДЕЁЖЗИЙКЛМНОПРСТОУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстоуфхцчшщъыьэюя";
    private static final int[] COLOR_CODE = new int[32];
    static {
        for (int index = 0; index < COLOR_CODE.length; index++) {
            int increment = (index >> 3 & 0x1) * 85;
            int red = (index >> 2 & 0x1) * 170 + increment;
            int green = (index >> 1 & 0x1) * 170 + increment;
            int blue = (index & 0x1) * 170 + increment;
            if (index == 6) red += 85;
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            COLOR_CODE[index] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
        }
    }

    private final Font font;
    private final Int2ObjectMap<CharData> data = new Int2ObjectArrayMap<>(CHARS.length());
    private final Int2ObjectMap<CharData> boldData = new Int2ObjectArrayMap<>(CHARS.length());
    private final Int2ObjectMap<CharData> italicData = new Int2ObjectArrayMap<>(CHARS.length());
    private final Int2ObjectMap<CharData> boldItalicData = new Int2ObjectArrayMap<>(CHARS.length());
    private DynamicTexture texture;
    private DynamicTexture textureBold;
    private DynamicTexture textureItalic;
    private DynamicTexture textureBoldItalic;
    protected int height;

    public CustomFont(Font font) {
        this.font = font;
        texture = getFont(font, data);
    }

    private DynamicTexture getFont(Font font, Int2ObjectMap<CharData> dataMap) {
        System.out.println(String.format("Loading font... (name=%s,size=%s,style=%s)", font.getFontName(), font.getSize(), font.getStyle()));
        BufferedImage bufferedImage = new BufferedImage(512, 512, 2);
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fontMetrics = g.getFontMetrics();
        int charHeight = 0;
        int positionX = 0;
        int positionY = 1;
        for (int i = 0; i < CHARS.length(); ++i) {
            char c = CHARS.charAt(i);
            CharData data = new CharData();
            Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(c), g);
            data.width = dimensions.getBounds().width + 8;
            data.height = dimensions.getBounds().height;
            if (positionX + data.width >= 512) {
                positionX = 0;
                positionY += charHeight;
                charHeight = 0;
            }
            if (data.height > charHeight) charHeight = data.height;
            data.storedX = positionX;
            data.storedY = positionY;
            if (data.height > height) height = data.height;
            dataMap.put(c, data);
            g.drawString(String.valueOf(c), positionX + 2, positionY + fontMetrics.getAscent());
            positionX += data.width;
        }
        return new DynamicTexture(bufferedImage);
    }

    public void drawBlurredStringWithShadow(String text, double x, double y, int blurRadius, Color blurColor, int color) {
        RenderHelper2.renderBlurredShadow(blurColor,(float)(int)x, (float)(int)y, (float) getWidth(text), (float) getHeight(), blurRadius);
        drawShadow(text, (float)x, (float)y, color);
    }

    public float draw(String text, double x, double y, int color) {
        return draw(text, x, y, color, false);
    }
    public void drawGradient(String string, double x, double y, int color) {
        float offset = 0;
        drawGradient(string,x,y,color,false);
    }
    public void drawGradientArray(String string, double x, double y, int color) {
        float offset = 0;
        drawGradientArray(string,x,y,color,false);
    }
    public float drawShadow(String text, double x, double y, int color) {
        float width = draw(text, x + 0.3D, y + 0.3D, color, true);
        return Math.max(width, draw(text, x, y, color, false));
    }

    public float drawCenter(String text, double x, double y, int color) {
        return draw(text, x - getWidth(text) / 2D, y, color);
    }

    public float drawCenterShadow(String text, double x, double y, int color) {
        float width = draw(text, x - getWidth(text) / 2D + 0.45D, y + 0.3D, color,true);
        return Math.max(width, draw(text, x - getWidth(text) / 2D, y, color, false));
    }

    public void drawOutline(String text, double x, double y, int color) {
        draw(text, x - 0.5, y, 0xFF000000, false);
        draw(text, x + 0.5, y, 0xFF000000, false);
        draw(text, x, y - 0.5, 0xFF000000, false);
        draw(text, x, y + 0.5, 0xFF000000, false);
        draw(text, x, y, color, false);
    }
    public float drawGradient(String text, double x, double y, int color, boolean shadow) {

        if (shadow) color = (color  & 0xFCFCFC) >> 2 | color & 0xC8141414;
        x -= 1.0;
        float alpha = (float) (color >> 24 & 0xFF) / 255F;
        x *= 2;
        y = (y - 3) * 2;
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int size = text.length();
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(texture.getGlTextureId());
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        Int2ObjectMap<CharData> currentData = data;
        for (int i = 0; i < size; i++) {
            if (HUD.useCustomColors.get()) {
                color = ColorUtil.interpolateColorsBackAndForth(22,(int) ((int)i * ArrayList.range.getNumberValue()),HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get()).getRGB();
            }else{
                color= ColorUtil.interpolateColorsBackAndForth(22, (int) ((int)i * ArrayList.range.getNumberValue()), RenderHelper2.oneColorGradient(), RenderHelper2.alternateColorGradient(), HUD.hueInterpolation.get()).getRGB();
            }

            if (shadow) color = (color  & 0xFCFCFC) >> 2 | color & 0xC8141414;
            GlStateManager.color((color >> 16 & 0xFF) / 255F, (color >> 8 & 0xFF) / 255F, (color & 0xFF) / 255F, alpha);
            char c = text.charAt(i);
            if (c == '§') {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                } catch (Exception ignored) {}
                if (colorIndex < 16) {
                    GlStateManager.bindTexture(texture.getGlTextureId());
                    if (colorIndex < 0) colorIndex = 15;
                    if (shadow) colorIndex += 16;
                    int rgb = COLOR_CODE[colorIndex];
                    GlStateManager.color((rgb >> 16 & 0xFF) / 255F, (rgb >> 8 & 0xFF) / 255F, (rgb & 0xFF) / 255F, alpha);
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        if (textureBoldItalic == null) textureBoldItalic = getFont(font.deriveFont(Font.BOLD | Font.ITALIC), boldItalicData);
                        GlStateManager.bindTexture(textureBoldItalic.getGlTextureId());
                        currentData = boldItalicData;
                    } else {
                        if (textureBold == null) textureBold = getFont(font.deriveFont(Font.BOLD), boldData);
                        GlStateManager.bindTexture(textureBold.getGlTextureId());
                        currentData = boldData;
                    }
                } else if (colorIndex == 18) {
                    strikethrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        if (textureBoldItalic == null) textureBoldItalic = getFont(font.deriveFont(Font.BOLD | Font.ITALIC), boldItalicData);
                        GlStateManager.bindTexture(textureBoldItalic.getGlTextureId());
                        currentData = boldItalicData;
                    } else {
                        if (textureItalic == null) textureItalic = getFont(font.deriveFont(Font.ITALIC), italicData);
                        GlStateManager.bindTexture(textureItalic.getGlTextureId());
                        currentData = italicData;
                    }
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.color((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F, (color & 255) / 255F, alpha);
                    GlStateManager.bindTexture(texture.getGlTextureId());
                    currentData = data;
                }
                i++;
                continue;
            }
            CharData data = currentData.get(c);
            if (data == null) continue;
            GL11.glBegin(GL11.GL_TRIANGLES);
            draw(data, x, y);
            GL11.glEnd();
            if (strikethrough) {
                drawHorizontalLine(x, x + data.width - 8F, y + data.height / 2F, color);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2d(x, y + data.height / 2F);
                GL11.glVertex2d(x + data.width - 8F, y + data.height / 2F);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            if (underline) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2d(x, y + data.height - 2F);
                GL11.glVertex2d(x + data.width - 8F, y + data.height - 2F);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            x += data.width - 8;
        }
        GL11.glPopMatrix();
        return (float) (x / 2);
    }
    public float drawGradientArray(String text, double x, double y, int color, boolean shadow) {

        if (shadow) color = (color  & 0xFCFCFC) >> 2 | color & 0xC8141414;
        x -= 1.0;
        float alpha = (float) (color >> 24 & 0xFF) / 255F;
        x *= 2;
        y = (y - 3) * 2;
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int size = text.length();
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(texture.getGlTextureId());
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        Int2ObjectMap<CharData> currentData = data;
        for (int i = 0; i < size; i++) {
            String mode = ArrayList.colorList.getOptions();
            int time = ArrayList.time.getCurrentIntValue();
            int colorCustom = onecolor.getColorValue();
            int colorCustom2 = twocolor.getColorValue();
            switch (mode.toLowerCase()) {
                case "astolfo":
                    color = PaletteHelper.astolfoarray2(false,  (int) ((int)  i * ArrayList.range.getNumberValue())).getRGB();
                    break;
                case "astolfo2":
                    color = ColorUtil.skyRainbow((int) time, (int) ((int)  i * ArrayList.range.getNumberValue())).getRGB();
                    break;
                case "static":
                    color = new Color(colorCustom).getRGB();
                    break;
                case "custom":
                    color = ColorUtil.interpolateColorsBackAndForth((int) time, (int) ((int) i * ArrayList.range.getNumberValue()), new Color(colorCustom), new Color(colorCustom2), true).getRGB();
                    break;
                case "test":
                    color = ArrayListGlowPallete.getColor(-i * ArrayList.range.getNumberValue(), (int) time);
                    break;
                case "fade":
                    color = ColorUtil.interpolateColorsBackAndForth((int) time, (int) ((int) i * ArrayList.range.getNumberValue()), new Color(colorCustom), new Color(colorCustom).darker().darker(), true).getRGB();
                    break;
                case "none":
                    color = new Color(255, 255, 255).getRGB();
                    break;
                case "category":
                    color = new Color(-1).getRGB();
                    break;
            }

            if (shadow) color = (color  & 0xFCFCFC) >> 2 | color & 0xC8141414;
            GlStateManager.color((color >> 16 & 0xFF) / 255F, (color >> 8 & 0xFF) / 255F, (color & 0xFF) / 255F, alpha);
            char c = text.charAt(i);
            if (c == '§') {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                } catch (Exception ignored) {}
                if (colorIndex < 16) {
                    GlStateManager.bindTexture(texture.getGlTextureId());
                    if (colorIndex < 0) colorIndex = 15;
                    if (shadow) colorIndex += 16;
                    int rgb = COLOR_CODE[colorIndex];
                    GlStateManager.color((rgb >> 16 & 0xFF) / 255F, (rgb >> 8 & 0xFF) / 255F, (rgb & 0xFF) / 255F, alpha);
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        if (textureBoldItalic == null) textureBoldItalic = getFont(font.deriveFont(Font.BOLD | Font.ITALIC), boldItalicData);
                        GlStateManager.bindTexture(textureBoldItalic.getGlTextureId());
                        currentData = boldItalicData;
                    } else {
                        if (textureBold == null) textureBold = getFont(font.deriveFont(Font.BOLD), boldData);
                        GlStateManager.bindTexture(textureBold.getGlTextureId());
                        currentData = boldData;
                    }
                } else if (colorIndex == 18) {
                    strikethrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        if (textureBoldItalic == null) textureBoldItalic = getFont(font.deriveFont(Font.BOLD | Font.ITALIC), boldItalicData);
                        GlStateManager.bindTexture(textureBoldItalic.getGlTextureId());
                        currentData = boldItalicData;
                    } else {
                        if (textureItalic == null) textureItalic = getFont(font.deriveFont(Font.ITALIC), italicData);
                        GlStateManager.bindTexture(textureItalic.getGlTextureId());
                        currentData = italicData;
                    }
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.color((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F, (color & 255) / 255F, alpha);
                    GlStateManager.bindTexture(texture.getGlTextureId());
                    currentData = data;
                }
                i++;
                continue;
            }
            CharData data = currentData.get(c);
            if (data == null) continue;
            GL11.glBegin(GL11.GL_TRIANGLES);
            draw(data, x, y);
            GL11.glEnd();
            if (strikethrough) {
                drawHorizontalLine(x, x + data.width - 8F, y + data.height / 2F, color);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2d(x, y + data.height / 2F);
                GL11.glVertex2d(x + data.width - 8F, y + data.height / 2F);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            if (underline) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2d(x, y + data.height - 2F);
                GL11.glVertex2d(x + data.width - 8F, y + data.height - 2F);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            x += data.width - 8;
        }
        GL11.glPopMatrix();
        return (float) (x / 2);
    }
    public float draw(String text, double x, double y, int color, boolean shadow) {
        if (shadow) color = (color  & 0xFCFCFC) >> 2 | color & 0xC8141414;
        x -= 1.0;
        float alpha = (float) (color >> 24 & 0xFF) / 255F;
        x *= 2;
        y = (y - 3) * 2;
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color((color >> 16 & 0xFF) / 255F, (color >> 8 & 0xFF) / 255F, (color & 0xFF) / 255F, alpha);
        int size = text.length();
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(texture.getGlTextureId());
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        Int2ObjectMap<CharData> currentData = data;
        for (int i = 0; i < size; i++) {
            char c = text.charAt(i);
            if (c == '§') {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                } catch (Exception ignored) {}
                if (colorIndex < 16) {
                    GlStateManager.bindTexture(texture.getGlTextureId());
                    if (colorIndex < 0) colorIndex = 15;
                    if (shadow) colorIndex += 16;
                    int rgb = COLOR_CODE[colorIndex];
                    GlStateManager.color((rgb >> 16 & 0xFF) / 255F, (rgb >> 8 & 0xFF) / 255F, (rgb & 0xFF) / 255F, alpha);
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        if (textureBoldItalic == null) textureBoldItalic = getFont(font.deriveFont(Font.BOLD | Font.ITALIC), boldItalicData);
                        GlStateManager.bindTexture(textureBoldItalic.getGlTextureId());
                        currentData = boldItalicData;
                    } else {
                        if (textureBold == null) textureBold = getFont(font.deriveFont(Font.BOLD), boldData);
                        GlStateManager.bindTexture(textureBold.getGlTextureId());
                        currentData = boldData;
                    }
                } else if (colorIndex == 18) {
                    strikethrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        if (textureBoldItalic == null) textureBoldItalic = getFont(font.deriveFont(Font.BOLD | Font.ITALIC), boldItalicData);
                        GlStateManager.bindTexture(textureBoldItalic.getGlTextureId());
                        currentData = boldItalicData;
                    } else {
                        if (textureItalic == null) textureItalic = getFont(font.deriveFont(Font.ITALIC), italicData);
                        GlStateManager.bindTexture(textureItalic.getGlTextureId());
                        currentData = italicData;
                    }
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.color((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F, (color & 255) / 255F, alpha);
                    GlStateManager.bindTexture(texture.getGlTextureId());
                    currentData = data;
                }
                i++;
                continue;
            }
            CharData data = currentData.get(c);
            if (data == null) continue;
            GL11.glBegin(GL11.GL_TRIANGLES);
            draw(data, x, y);
            GL11.glEnd();
            if (strikethrough) {
                drawHorizontalLine(x, x + data.width - 8F, y + data.height / 2F, color);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2d(x, y + data.height / 2F);
                GL11.glVertex2d(x + data.width - 8F, y + data.height / 2F);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            if (underline) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2d(x, y + data.height - 2F);
                GL11.glVertex2d(x + data.width - 8F, y + data.height - 2F);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            x += data.width - 8;
        }
        GL11.glPopMatrix();
        return (float) (x / 2);
    }

    public void draw(CharData c, double x, double y) {
        draw(x, y, c.width, c.height, c.storedX, c.storedY);
    }

    private void draw(double x, double y, double width, double height, double u, double v) {
        double tu = u / 512D;
        double tv = v / 512D;
        double tw = width / 512D;
        double th = height / 512D;
        GL11.glTexCoord2d(tu + tw, tv);
        GL11.glVertex2d(x + width, y);
        GL11.glTexCoord2d(tu, tv);
        GL11.glVertex2d(x, y);
        GL11.glTexCoord2d(tu, tv + th);
        GL11.glVertex2d(x, y + height);
        GL11.glTexCoord2d(tu, tv + th);
        GL11.glVertex2d(x, y + height);
        GL11.glTexCoord2d(tu + tw, tv + th);
        GL11.glVertex2d(x + width, y + height);
        GL11.glTexCoord2d(tu + tw, tv);
        GL11.glVertex2d(x + width, y);
    }

    public int getWidth(String text) {
        int width = 0;
        int size = text.length();
        boolean bold = false;
        boolean italic = false;
        Int2ObjectMap<CharData> currentData = data;
        for (int i = 0; i < size; i++) {
            char c = text.charAt(i);
            if (c == '§') {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                } catch (Exception ignored) {}
                if (colorIndex == 17) {
                    bold = true;
                    currentData = italic ? boldItalicData : boldData;
                } else if (colorIndex == 20) {
                    italic = true;
                    currentData = bold ? boldItalicData : boldData;
                } else if (colorIndex == 21) {
                    bold = italic = false;
                    currentData = data;
                }
                i++;
                continue;
            }
            CharData data = currentData.get(c);
            if (data == null) continue;
            width += data.width - 8;
        }
        return width / 2;
    }

    public int getHeight() {
        return (height - 8) / 2;
    }

    @Deprecated
    public float getMiddleOfBox(float boxHeight) {
        return boxHeight / 2f - getHeight() / 2f;
    }

    private static class CharData {
        public int width;
        public int height;
        public int storedX;
        public int storedY;
    }
}