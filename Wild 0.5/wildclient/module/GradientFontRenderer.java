//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.FontRenderer;

public class GradientFontRenderer extends FontRenderer
{
    public GradientFontRenderer(final GameSettings gameSettingsIn, final ResourceLocation location, final TextureManager textureManagerIn, final boolean unicode) {
        super(gameSettingsIn, location, textureManagerIn, unicode);
    }
    
    public int drawGradientString(final String text, final float x, final float y, final int topColor, final int bottomColor, final boolean dropShadow) {
        this.enableAlpha();
        int i;
        if (dropShadow) {
            i = this.renderGradientString(text, x + 1.0f, y + 1.0f, topColor, bottomColor, true);
            i = Math.max(i, this.renderGradientString(text, x, y, topColor, bottomColor, false));
        }
        else {
            i = this.renderGradientString(text, x, y, topColor, bottomColor, false);
        }
        return i;
    }
    
    private int renderGradientString(final String text, final float x, final float y, int topColor, int bottomColor, final boolean dropShadow) {
        if (text == null) {
            return 0;
        }
        if ((topColor & 0xFC000000) == 0x0) {
            topColor |= 0xFF000000;
        }
        if ((bottomColor & 0xFC000000) == 0x0) {
            bottomColor |= 0xFF000000;
        }
        if (dropShadow) {
            topColor = ((topColor & 0xFCFCFC) >> 2 | (topColor & 0xFF000000));
            bottomColor = ((bottomColor & 0xFCFCFC) >> 2 | (bottomColor & 0xFF000000));
        }
        this.posX = x;
        this.posY = y;
        this.renderGradientStringAtPos(text, dropShadow, topColor, bottomColor);
        return (int)this.posX;
    }
    
    private void renderGradientStringAtPos(final String text, final boolean shadow, final int topColor, final int bottomColor) {
        for (int i = 0; i < text.length(); ++i) {
            final char c0 = text.charAt(i);
            final int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".indexOf(c0);
            final float f1 = (j == -1) ? 0.5f : 1.0f;
            final boolean flag = (c0 == '\0' || j == -1) && shadow;
            if (flag) {
                this.posX -= f1;
                this.posY -= f1;
            }
            final float f2 = this.renderGradientChar(c0, topColor, bottomColor);
            if (flag) {
                this.posX += f1;
                this.posY += f1;
            }
            this.doDraw(f2);
        }
    }
    
    private float renderGradientChar(final char ch, final int topColor, final int bottomColor) {
        if (ch == ' ') {
            return 4.0f;
        }
        if (ch == ' ') {
            return 4.0f;
        }
        final int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".indexOf(ch);
        if (i != -1) {
            return this.renderGradientDefaultChar(i, topColor, bottomColor);
        }
        throw new RuntimeException("Unrecognized char: " + ch);
    }
    
    protected float renderGradientDefaultChar(final int ch, final int topColor, final int bottomColor) {
        final float topAlpha = (topColor >> 24 & 0xFF) / 255.0f;
        final float topRed = (topColor >> 16 & 0xFF) / 255.0f;
        final float topGreen = (topColor >> 8 & 0xFF) / 255.0f;
        final float topBlue = (topColor & 0xFF) / 255.0f;
        final float bottomAlpha = (bottomColor >> 24 & 0xFF) / 255.0f;
        final float bottomRed = (bottomColor >> 16 & 0xFF) / 255.0f;
        final float bottomGreen = (bottomColor >> 8 & 0xFF) / 255.0f;
        final float bottomBlue = (bottomColor & 0xFF) / 255.0f;
        final float charXPos = ch % 16 * 8.0f;
        final float charYPos = ch / 16 * 8.0f;
        this.bindTexture(this.locationFontTexture);
        final int charWidth = this.charWidth[ch];
        final float width = charWidth - 0.01f;
        GlStateManager.shadeModel(7425);
        GlStateManager.glBegin(7);
        GlStateManager.color(topRed, topGreen, topBlue, topAlpha);
        GlStateManager.glTexCoord2f(charXPos / 128.0f, charYPos / 128.0f);
        GlStateManager.glVertex3f(this.posX, this.posY, 0.0f);
        GlStateManager.color(bottomRed, bottomGreen, bottomBlue, bottomAlpha);
        GlStateManager.glTexCoord2f(charXPos / 128.0f, (charYPos + 7.99f) / 128.0f);
        GlStateManager.glVertex3f(this.posX, this.posY + 7.99f, 0.0f);
        GlStateManager.color(bottomRed, bottomGreen, bottomBlue, bottomAlpha);
        GlStateManager.glTexCoord2f((charXPos + width - 1.0f) / 128.0f, (charYPos + 7.99f) / 128.0f);
        GlStateManager.glVertex3f(this.posX + width - 1.0f, this.posY + 7.99f, 0.0f);
        GlStateManager.color(topRed, topGreen, topBlue, topAlpha);
        GlStateManager.glTexCoord2f((charXPos + width - 1.0f) / 128.0f, charYPos / 128.0f);
        GlStateManager.glVertex3f(this.posX + width - 1.0f, this.posY, 0.0f);
        GlStateManager.glEnd();
        GlStateManager.shadeModel(7424);
        return (float)charWidth;
    }
}
