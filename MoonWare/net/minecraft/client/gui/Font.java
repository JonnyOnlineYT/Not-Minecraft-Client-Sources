package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Formatting;
import optifine.Config;
import optifine.CustomColors;
import optifine.FontUtils;
import optifine.GlBlendState;
import org.apache.commons.io.IOUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.misc.StreamerMode;
import org.moonware.client.helpers.render.GlowUtil;
import org.moonware.client.helpers.render2.RenderHelper2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class Font implements IResourceManagerReloadListener {
    private static final Namespaced[] UNICODE_PAGE_LOCATIONS = new Namespaced[256];
    public int[] charWidth = new int[256];
    public int height = 9;
    private final byte[] unicodeWidth = new byte[65536];
    private final int[] colorCode = new int[32];
    public Namespaced location;

    /**
     * The RenderEngine used to load and setup glyph textures.
     */
    private final TextureManager renderEngine;

    /**
     * Current X coordinate at which to draw the next character.
     */
    public float posX;

    /**
     * Current Y coordinate at which to draw the next character.
     */
    public float posY;

    /**
     * If true, strings should be rendered with Unicode fonts instead of the default.png font
     */
    private boolean unicodeFlag;

    /**
     * If true, the Unicode Bidirectional Algorithm should be run before rendering any string.
     */
    private boolean bidiFlag;

    /**
     * Used to specify new red value for the current color.
     */
    private float red;

    /**
     * Used to specify new blue value for the current color.
     */
    private float blue;

    /**
     * Used to specify new green value for the current color.
     */
    private float green;

    /**
     * Used to speify new alpha value for the current color.
     */
    private float alpha;

    /**
     * Text color of the currently rendering string.
     */
    private int textColor;

    /**
     * Set if the "k" style (random) is active in currently rendering string
     */
    private boolean randomStyle;

    /**
     * Set if the "l" style (bold) is active in currently rendering string
     */
    private boolean boldStyle;

    /**
     * Set if the "o" style (italic) is active in currently rendering string
     */
    private boolean italicStyle;

    /**
     * Set if the "n" style (underlined) is active in currently rendering string
     */
    private boolean underlineStyle;

    /**
     * Set if the "m" style (strikethrough) is active in currently rendering string
     */
    private boolean strikethroughStyle;
    public GameSettings gameSettings;
    public Namespaced locationFontTextureBase;
    public boolean enabled = true;
    public float offsetBold = 1.0F;
    private float[] charWidthFloat = new float[256];
    private boolean blend;
    private GlBlendState oldBlendState = new GlBlendState();

    public Font(GameSettings gameSettingsIn, Namespaced location, TextureManager textureManagerIn, boolean unicode) {
        gameSettings = gameSettingsIn;
        locationFontTextureBase = location;
        this.location = location;
        renderEngine = textureManagerIn;
        unicodeFlag = unicode;
        this.location = FontUtils.getHdFontLocation(locationFontTextureBase);
        bindTexture(this.location);

        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i >> 0 & 1) * 170 + j;

            if (i == 6) {
                k += 85;
            }

            if (gameSettingsIn.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }

        readGlyphSizes();
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        location = FontUtils.getHdFontLocation(locationFontTextureBase);

        for (int i = 0; i < UNICODE_PAGE_LOCATIONS.length; ++i) {
            UNICODE_PAGE_LOCATIONS[i] = null;
        }

        readFontTexture();
        readGlyphSizes();
    }

    private void readFontTexture() {
        IResource iresource = null;
        BufferedImage bufferedimage;

        try {
            iresource = getResource(location);
            bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
        } catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        } finally {
            IOUtils.closeQuietly(iresource);
        }

        Properties props = FontUtils.readFontProperties(location);
        blend = FontUtils.readBoolean(props, "blend", false);
        int imgWidth = bufferedimage.getWidth();
        int imgHeight = bufferedimage.getHeight();
        int charW = imgWidth / 16;
        int charH = imgHeight / 16;
        float kx = (float) imgWidth / 128.0F;
        float boldScaleFactor = Config.limit(kx, 1.0F, 2.0F);
        offsetBold = 1.0F / boldScaleFactor;
        float offsetBoldConfig = FontUtils.readFloat(props, "offsetBold", -1.0F);

        if (offsetBoldConfig >= 0.0F) {
            offsetBold = offsetBoldConfig;
        }

        int[] aint = new int[imgWidth * imgHeight];
        bufferedimage.getRGB(0, 0, imgWidth, imgHeight, aint, 0, imgWidth);

        for (int i1 = 0; i1 < 256; ++i1) {
            int j1 = i1 % 16;
            int k1 = i1 / 16;
            int l1 = 0;

            for (l1 = charW - 1; l1 >= 0; --l1) {
                int i2 = j1 * charW + l1;
                boolean flag = true;

                for (int j2 = 0; j2 < charH && flag; ++j2) {
                    int k2 = (k1 * charH + j2) * imgWidth;
                    int l2 = aint[i2 + k2];
                    int i3 = l2 >> 24 & 255;

                    if (i3 > 16) {
                        flag = false;
                    }
                }

                if (!flag) {
                    break;
                }
            }

            if (i1 == 65) {
                i1 = i1;
            }

            if (i1 == 32) {
                if (charW <= 8) {
                    l1 = (int) (2.0F * kx);
                } else {
                    l1 = (int) (1.5F * kx);
                }
            }

            charWidthFloat[i1] = (float) (l1 + 1) / kx + 1.0F;
        }

        FontUtils.readCustomCharWidths(props, charWidthFloat);

        for (int j3 = 0; j3 < charWidth.length; ++j3) {
            charWidth[j3] = Math.round(charWidthFloat[j3]);
        }
    }

    private void readGlyphSizes() {
        IResource iresource = null;

        try {
            iresource = getResource(new Namespaced("font/glyph_sizes.bin"));
            iresource.getInputStream().read(unicodeWidth);
        } catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        } finally {
            IOUtils.closeQuietly(iresource);
        }
    }

    /**
     * Render the given char
     */
    private float renderChar(char ch, boolean italic) {
        if (ch != ' ' && ch != 160) {
            int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(ch);
            return i != -1 && !unicodeFlag ? renderDefaultChar(i, italic) : renderUnicodeChar(ch, italic);
        } else {
            return !unicodeFlag ? charWidthFloat[ch] : 4.0F;
        }
    }

    /**
     * Render a single character with the default.png font at current (posX,posY) location...
     */
    private float renderDefaultChar(int ch, boolean italic) {
        int i = ch % 16 * 8;
        int j = ch / 16 * 8;
        int k = italic ? 1 : 0;
        bindTexture(location);
        float f = charWidthFloat[ch];
        float f1 = 7.99F;
        GlStateManager.glBegin(5);
        GlStateManager.glTexCoord2f((float) i / 128.0F, (float) j / 128.0F);
        GlStateManager.glVertex3f(posX + (float) k, posY, 0.0F);
        GlStateManager.glTexCoord2f((float) i / 128.0F, ((float) j + 7.99F) / 128.0F);
        GlStateManager.glVertex3f(posX - (float) k, posY + 7.99F, 0.0F);
        GlStateManager.glTexCoord2f(((float) i + f1 - 1.0F) / 128.0F, (float) j / 128.0F);
        GlStateManager.glVertex3f(posX + f1 - 1.0F + (float) k, posY, 0.0F);
        GlStateManager.glTexCoord2f(((float) i + f1 - 1.0F) / 128.0F, ((float) j + 7.99F) / 128.0F);
        GlStateManager.glVertex3f(posX + f1 - 1.0F - (float) k, posY + 7.99F, 0.0F);
        GlStateManager.glEnd();
        return f;
    }

    private Namespaced getUnicodePageLocation(int page) {
        if (UNICODE_PAGE_LOCATIONS[page] == null) {
            UNICODE_PAGE_LOCATIONS[page] = new Namespaced(String.format("textures/font/unicode_page_%02x.png", page));
            UNICODE_PAGE_LOCATIONS[page] = FontUtils.getHdFontLocation(UNICODE_PAGE_LOCATIONS[page]);
        }

        return UNICODE_PAGE_LOCATIONS[page];
    }

    /**
     * Load one of the /font/glyph_XX.png into a new GL texture and store the texture ID in glyphTextureName array.
     */
    private void loadGlyphTexture(int page) {
        bindTexture(getUnicodePageLocation(page));
    }

    /**
     * Render a single Unicode character at current (posX,posY) location using one of the /font/glyph_XX.png files...
     */
    private float renderUnicodeChar(char ch, boolean italic) {
        int i = unicodeWidth[ch] & 255;

        if (i == 0) {
            return 0.0F;
        } else {
            int j = ch / 256;
            loadGlyphTexture(j);
            int k = i >>> 4;
            int l = i & 15;
            float f = (float) k;
            float f1 = (float) (l + 1);
            float f2 = (float) (ch % 16 * 16) + f;
            float f3 = (float) ((ch & 255) / 16 * 16);
            float f4 = f1 - f - 0.02F;
            float f5 = italic ? 1.0F : 0.0F;
            GlStateManager.glBegin(5);
            GlStateManager.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
            GlStateManager.glVertex3f(posX + f5, posY, 0.0F);
            GlStateManager.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
            GlStateManager.glVertex3f(posX - f5, posY + 7.99F, 0.0F);
            GlStateManager.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
            GlStateManager.glVertex3f(posX + f4 / 2.0F + f5, posY, 0.0F);
            GlStateManager.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
            GlStateManager.glVertex3f(posX + f4 / 2.0F - f5, posY + 7.99F, 0.0F);
            GlStateManager.glEnd();
            return (f1 - f) / 2.0F + 1.0F;
        }
    }

    /**
     * Draws the specified string with a shadow.
     */
    public int drawStringWithShadow(String text, float x, float y, int color) {
        return drawString(text, x, y, color, true);
    }

    /**
     * Draws the specified string.
     */
    public int drawString(String text, float x, float y, int color) {
        return !enabled ? 0 : drawString(text, x, y, color, false);
    }

    public void drawStringWithOutline(String text, float x, float y, int color) {
        drawString(text, x - 0.5F, y, Color.BLACK.getRGB());
        drawString(text, x + 0.5F, y, Color.BLACK.getRGB());
        drawString(text, x, y - 0.5F, Color.BLACK.getRGB());
        drawString(text, x, y + 0.5F, Color.BLACK.getRGB());
        drawString(text, x, y, color);
    }


    /**
     * Draws the specified string.
     */
    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        enableAlpha();

        if (blend) {
            GlStateManager.getBlendState(oldBlendState);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        }

        resetStyles();
        int i;

        if (dropShadow) {
            i = renderString(text, x + 1.0F, y + 1.0F, color, true);
            i = Math.max(i, renderString(text, x, y, color, false));
        } else {
            i = renderString(text, x, y, color, false);
        }

        if (blend) {
            GlStateManager.setBlendState(oldBlendState);
        }

        return i;
    }

    /**
     * Reset all style flag fields in the class to false; called at the start of string rendering
     */
    private void resetStyles() {
        randomStyle = false;
        boldStyle = false;
        italicStyle = false;
        underlineStyle = false;
        strikethroughStyle = false;
    }

    /**
     * Render a single line string at the current (posX,posY) and update posX
     */
    private void renderStringAtPos(String text, boolean shadow) {
        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);

            if (c0 == 167 && i + 1 < text.length()) {
                int l = "0123456789abcdefklmnor".indexOf(String.valueOf(text.charAt(i + 1)).toLowerCase(Locale.ROOT).charAt(0));

                if (l < 16) {
                    randomStyle = false;
                    boldStyle = false;
                    strikethroughStyle = false;
                    underlineStyle = false;
                    italicStyle = false;

                    if (l < 0 || l > 15) {
                        l = 15;
                    }

                    if (shadow) {
                        l += 16;
                    }

                    int i1 = colorCode[l];

                    if (Config.isCustomColors()) {
                        i1 = CustomColors.getTextColor(l, i1);
                    }

                    textColor = i1;
                    setColor((float) (i1 >> 16) / 255.0F, (float) (i1 >> 8 & 255) / 255.0F, (float) (i1 & 255) / 255.0F, alpha);
                } else if (l == 16) {
                    randomStyle = true;
                } else if (l == 17) {
                    boldStyle = true;
                } else if (l == 18) {
                    strikethroughStyle = true;
                } else if (l == 19) {
                    underlineStyle = true;
                } else if (l == 20) {
                    italicStyle = true;
                } else if (l == 21) {
                    randomStyle = false;
                    boldStyle = false;
                    strikethroughStyle = false;
                    underlineStyle = false;
                    italicStyle = false;
                    setColor(red, blue, green, alpha);
                }

                ++i;
            } else {
                int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);

                if (randomStyle && j != -1) {
                    int k = getCharWidth(c0);
                    char c1;

                    while (true) {
                        j = Minecraft.RANDOM.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length());
                        c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j);

                        if (k == getCharWidth(c1)) {
                            break;
                        }
                    }

                    c0 = c1;
                }

                float f1 = j != -1 && !unicodeFlag ? offsetBold : 0.5F;
                boolean flag = (c0 == 0 || j == -1 || unicodeFlag) && shadow;

                if (flag) {
                    posX -= f1;
                    posY -= f1;
                }

                float f = renderChar(c0, italicStyle);

                if (flag) {
                    posX += f1;
                    posY += f1;
                }

                if (boldStyle) {
                    posX += f1;

                    if (flag) {
                        posX -= f1;
                        posY -= f1;
                    }

                    renderChar(c0, italicStyle);
                    posX -= f1;

                    if (flag) {
                        posX += f1;
                        posY += f1;
                    }

                    f += f1;
                }

                doDraw(f);
            }
        }
    }

    protected void doDraw(float p_doDraw_1_) {
        if (strikethroughStyle) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.disableTexture2D();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
            bufferbuilder.pos(posX, posY + (float) (height / 2), 0.0D).endVertex();
            bufferbuilder.pos(posX + p_doDraw_1_, posY + (float) (height / 2), 0.0D).endVertex();
            bufferbuilder.pos(posX + p_doDraw_1_, posY + (float) (height / 2) - 1.0F, 0.0D).endVertex();
            bufferbuilder.pos(posX, posY + (float) (height / 2) - 1.0F, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }

        if (underlineStyle) {
            Tessellator tessellator1 = Tessellator.getInstance();
            BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
            GlStateManager.disableTexture2D();
            bufferbuilder1.begin(7, DefaultVertexFormats.POSITION);
            int i = underlineStyle ? -1 : 0;
            bufferbuilder1.pos(posX + (float) i, posY + (float) height, 0.0D).endVertex();
            bufferbuilder1.pos(posX + p_doDraw_1_, posY + (float) height, 0.0D).endVertex();
            bufferbuilder1.pos(posX + p_doDraw_1_, posY + (float) height - 1.0F, 0.0D).endVertex();
            bufferbuilder1.pos(posX + (float) i, posY + (float) height - 1.0F, 0.0D).endVertex();
            tessellator1.draw();
            GlStateManager.enableTexture2D();
        }

        posX += p_doDraw_1_;
    }

    /**
     * Render string either left or right aligned depending on bidiFlag
     */
    private int renderStringAligned(String text, int x, int y, int width, int color, boolean dropShadow) {
        if (bidiFlag) {
            int i = getStringWidth(text);
            x = x + width - i;
        }

        return renderString(text, (float) x, (float) y, color, dropShadow);
    }

    /**
     * Render single line string by setting GL color, current (posX,posY), and calling renderStringAtPos()
     */
    private int renderString(String text, float x, float y, int color, boolean dropShadow) {
        if (text == null) {
            return 0;
        } else {
            if ((color & -67108864) == 0) {
                color |= -16777216;
            }

            if (dropShadow) {
                color = (color & 16579836) >> 2 | color & -16777216;
            }

            red = (float) (color >> 16 & 255) / 255.0F;
            blue = (float) (color >> 8 & 255) / 255.0F;
            green = (float) (color & 255) / 255.0F;
            alpha = (float) (color >> 24 & 255) / 255.0F;
            setColor(red, blue, green, alpha);
            posX = x;
            posY = y;
            if (MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue()) {
                text = text.replaceAll(Minecraft.session.getUsername(), Formatting.LIGHT_PURPLE + "" + Formatting.ITALIC + "Protected" + Formatting.RESET);
            }
            renderStringAtPos(text, dropShadow);
            return (int) posX;
        }
    }

    /**
     * Returns the width of this string. Equivalent of FontMetrics.stringWidth(String s).
     */
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        } else {
            float f = 0.0F;
            boolean flag = false;

            for (int i = 0; i < text.length(); ++i) {
                char c0 = text.charAt(i);
                float f1 = getCharWidthFloat(c0);

                if (f1 < 0.0F && i < text.length() - 1) {
                    ++i;
                    c0 = text.charAt(i);

                    if (c0 != 'l' && c0 != 'L') {
                        if (c0 == 'r' || c0 == 'R') {
                            flag = false;
                        }
                    } else {
                        flag = true;
                    }

                    f1 = 0.0F;
                }

                f += f1;

                if (flag && f1 > 0.0F) {
                    f += unicodeFlag ? 1.0F : offsetBold;
                }
            }

            return Math.round(f);
        }
    }

    /**
     * Returns the width of this character as rendered.
     */
    public int getCharWidth(char character) {
        return Math.round(getCharWidthFloat(character));
    }

    private float getCharWidthFloat(char p_getCharWidthFloat_1_) {
        if (p_getCharWidthFloat_1_ == 167) {
            return -1.0F;
        } else if (p_getCharWidthFloat_1_ != ' ' && p_getCharWidthFloat_1_ != 160) {
            int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(p_getCharWidthFloat_1_);

            if (p_getCharWidthFloat_1_ > 0 && i != -1 && !unicodeFlag) {
                return charWidthFloat[i];
            } else if (unicodeWidth[p_getCharWidthFloat_1_] != 0) {
                int j = unicodeWidth[p_getCharWidthFloat_1_] & 255;
                int k = j >>> 4;
                int l = j & 15;
                ++l;
                return (float) ((l - k) / 2 + 1);
            } else {
                return 0.0F;
            }
        } else {
            return charWidthFloat[32];
        }
    }

    /**
     * Trims a string to fit a specified Width.
     */
    public String trimStringToWidth(String text, int width) {
        return trimStringToWidth(text, width, false);
    }

    /**
     * Trims a string to a specified width, and will reverse it if par3 is set.
     */
    public String trimStringToWidth(String text, int width, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        float f = 0.0F;
        int i = reverse ? text.length() - 1 : 0;
        int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;

        for (int k = i; k >= 0 && k < text.length() && f < (float) width; k += j) {
            char c0 = text.charAt(k);
            float f1 = getCharWidthFloat(c0);

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

            if (f > (float) width) {
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

    /**
     * Remove all newline characters from the end of the string
     */
    private String trimStringNewline(String text) {
        while (text != null && text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }

        return text;
    }

    /**
     * Splits and draws a String with wordwrap (maximum length is parameter k)
     */
    public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
        if (blend) {
            GlStateManager.getBlendState(oldBlendState);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        }

        resetStyles();
        this.textColor = textColor;
        str = trimStringNewline(str);
        renderSplitString(str, x, y, wrapWidth, false);

        if (blend) {
            GlStateManager.setBlendState(oldBlendState);
        }
    }

    /**
     * Perform actual work of rendering a multi-line string with wordwrap and with darker drop shadow color if flag is
     * set
     */
    private void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow) {
        for (String s : split(str, wrapWidth)) {
            renderStringAligned(s, x, y, wrapWidth, textColor, addShadow);
            y += height;
        }
    }

    /**
     * Returns the width of the wordwrapped String (maximum length is parameter k)
     */
    public int splitStringWidth(String str, int maxLength) {
        return height * split(str, maxLength).size();
    }

    /**
     * Set unicodeFlag controlling whether strings should be rendered with Unicode fonts instead of the default.png
     * font.
     */
    public void setUnicodeFlag(boolean unicodeFlagIn) {
        unicodeFlag = unicodeFlagIn;
    }

    /**
     * Get unicodeFlag controlling whether strings should be rendered with Unicode fonts instead of the default.png
     * font.
     */
    public boolean getUnicodeFlag() {
        return unicodeFlag;
    }

    /**
     * Set bidiFlag to control if the Unicode Bidirectional Algorithm should be run before rendering any string.
     */
    public void setBidiFlag(boolean bidiFlagIn) {
        bidiFlag = bidiFlagIn;
    }

    public List<String> split(String str, int wrapWidth) {
        return Arrays.asList(wrap(str, wrapWidth).split("\n"));
    }

    /**
     * Inserts newline and formatting into a string to wrap it within the specified width.
     */
    String wrap(String str, int wrapWidth) {
        if (str.length() <= 1) {
            return str;
        } else {
            int i = sizeStringToWidth(str, wrapWidth);

            if (str.length() <= i) {
                return str;
            } else {
                String s = str.substring(0, i);
                char c0 = str.charAt(i);
                boolean flag = c0 == ' ' || c0 == '\n';
                String s1 = getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
                return s + "\n" + wrap(s1, wrapWidth);
            }
        }
    }

    /**
     * Determines how many characters from the string will fit into the specified width.
     */
    private int sizeStringToWidth(String str, int wrapWidth) {
        int i = str.length();
        float f = 0.0F;
        int j = 0;
        int k = -1;

        for (boolean flag = false; j < i; ++j) {
            char c0 = str.charAt(j);

            switch (c0) {
                case '\n':
                    --j;
                    break;

                case ' ':
                    k = j;

                default:
                    f += getCharWidthFloat(c0);

                    if (flag) {
                        ++f;
                    }

                    break;

                case '\u00a7':
                    if (j < i - 1) {
                        ++j;
                        char c1 = str.charAt(j);

                        if (c1 != 'l' && c1 != 'L') {
                            if (c1 == 'r' || c1 == 'R' || isFormatColor(c1)) {
                                flag = false;
                            }
                        } else {
                            flag = true;
                        }
                    }
            }

            if (c0 == '\n') {
                ++j;
                k = j;
                break;
            }

            if (Math.round(f) > wrapWidth) {
                break;
            }
        }

        return j != i && k != -1 && k < j ? k : j;
    }

    /**
     * Checks if the char code is a hexadecimal character, used to set colour.
     */
    private static boolean isFormatColor(char colorChar) {
        return colorChar >= '0' && colorChar <= '9' || colorChar >= 'a' && colorChar <= 'f' || colorChar >= 'A' && colorChar <= 'F';
    }

    /**
     * Checks if the char code is O-K...lLrRk-o... used to set special formatting.
     */
    private static boolean isFormatSpecial(char formatChar) {
        return formatChar >= 'k' && formatChar <= 'o' || formatChar >= 'K' && formatChar <= 'O' || formatChar == 'r' || formatChar == 'R';
    }

    /**
     * Digests a string for nonprinting formatting characters then returns a string containing only that formatting.
     */
    public static String getFormatFromString(String text) {
        String s = "";
        int i = -1;
        int j = text.length();

        while ((i = text.indexOf(167, i + 1)) != -1) {
            if (i < j - 1) {
                char c0 = text.charAt(i + 1);

                if (isFormatColor(c0)) {
                    s = "\u00a7" + c0;
                } else if (isFormatSpecial(c0)) {
                    s = s + "\u00a7" + c0;
                }
            }
        }

        return s;
    }

    /**
     * Get bidiFlag that controls if the Unicode Bidirectional Algorithm should be run before rendering any string
     */
    public boolean getBidiFlag() {
        return bidiFlag;
    }

    public int getColorCode(char character) {
        int i = "0123456789abcdef".indexOf(character);

        if (i >= 0 && i < colorCode.length) {
            int j = colorCode[i];

            if (Config.isCustomColors()) {
                j = CustomColors.getTextColor(i, j);
            }

            return j;
        } else {
            return 16777215;
        }
    }

    protected void setColor(float p_setColor_1_, float p_setColor_2_, float p_setColor_3_, float p_setColor_4_) {
        GlStateManager.color(p_setColor_1_, p_setColor_2_, p_setColor_3_, p_setColor_4_);
    }

    protected void enableAlpha() {
        GlStateManager.enableAlpha();
    }

    protected void bindTexture(Namespaced p_bindTexture_1_) {
        renderEngine.bindTexture(p_bindTexture_1_);
    }

    protected IResource getResource(Namespaced p_getResource_1_) throws IOException {
        return Minecraft.getResourceManager().getResource(p_getResource_1_);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawStringWithShadow(text, x - getStringWidth(text) / 2F, y, color);
    }

    public void drawCenteredBlurredStringWithShadow(String text, double x, double y, int blurRadius, Color blurColor, int color) {
        RenderHelper2.renderBlurredShadow(blurColor, (float) (int) ((int) x - getStringWidth(text) / 2.0f), (float) (int) y, (float) getStringWidth(text), (float) getFontHeight(), blurRadius);
        drawStringWithShadow(text, (float) (x - getStringWidth(text) / 2.0f), (float) y, color);
    }

    public void drawBlurredStringWithOutline(String text, double x, double y, int blurRadius, Color blurColor, int color) {
        RenderHelper2.renderBlurredShadow(blurColor, (float) (int) x, (float) (int) y, (float) getStringWidth(text), (float) getFontHeight(), blurRadius);
        drawStringWithOutline(text, (float) x, (float) y, color);
    }

    public void drawBlurredStringWithShadow(String text, double x, double y, int blurRadius, Color blurColor, int color) {
        RenderHelper2.renderBlurredShadow(blurColor, (float) (int) x, (float) (int) y, (float) getStringWidth(text), (float) getFontHeight(), blurRadius);
        drawStringWithShadow(text, (float) x, (float) y, color);
    }

    public void drawCenteredBlurredString(String text, double x, double y, int blurRadius, Color blurColor, int color) {
        RenderHelper2.renderBlurredShadow(blurColor, (float) (int) ((int) x - getStringWidth(text) / 2.0f), (float) (int) y, (float) getStringWidth(text), (float) getFontHeight(), blurRadius);
        drawString(text, (float) (x - getStringWidth(text) / 2.0f), (float) y, color);
    }

    public void drawBlurredString(String text, double x, double y, int blurRadius, Color blurColor, int color) {
        RenderHelper2.renderBlurredShadow(blurColor, (float) (int) x, (float) (int) y, (float) getStringWidth(text), (float) getFontHeight(), blurRadius);
        drawString(text, (float) x, (float) y, color);
    }

    public void drawBlurredString2(String text, double x, double y, int blurRadius, Color blurColor, int color) {
        Gui.drawRect(0, 0, 0, 0, 0);
        GlowUtil.drawBlurredGlow((float) x + 7.5F, (float) y - 15.5F, (float) x + getStringWidth(text), (int) y + (float) getFontHeight() + 11, blurColor.getRGB());
        drawString(text, (float) x, (float) y, color);
    }

    public void drawBlurredStringWithShadow2(String text, double x, double y, int blurRadius, Color blurColor, int color) {
        Gui.drawRect(0, 0, 0, 0, 0);
        GlowUtil.drawBlurredGlow((float) x + 1.5F, (float) y - 16.1F, (int) x + getStringWidth(text), (int) y + getFontHeight() + 16, blurColor.getRGB());
        drawStringWithShadow(text, (float) x, (float) y, color);
    }

    public void drawBlurredStringWithOutline2(String text, double x, double y, int blurRadius, Color blurColor, int color) {
        Gui.drawRect(0, 0, 0, 0, 0);
        GlowUtil.drawBlurredGlow((float) x - 0.5F, (float) y - 14.1F, (int) x + getStringWidth(text), (int) y + (float) getFontHeight() + 14, blurColor.getRGB());
        drawStringWithOutline(text, (float) x, (float) y, color);
    }


    public int getStringHeight(String text) {
        return height;
    }

    public int getFontHeight() {
        return height;
    }
}
