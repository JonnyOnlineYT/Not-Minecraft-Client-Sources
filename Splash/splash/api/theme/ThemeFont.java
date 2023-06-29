package splash.api.theme;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import splash.Splash;
import splash.api.theme.type.FontType;
import splash.client.managers.cfont.CFontRenderer;

/**
 * Author: Ice
 * Created: 01:06, 13-Jun-20
 * Project: Client
 */
public class ThemeFont {

    private FontType fontType;
    private FontRenderer minecraftFontRenderer = Minecraft.getMinecraft().fontRendererObj;
    private CFontRenderer customFontRenderer = Splash.getInstance().getFontRenderer();

    public ThemeFont(FontType fontType) {
        this.fontType = fontType;
    }

    public FontType getFontType() {
        return fontType;
    }

    public FontRenderer getMinecraftFontRenderer() {
        return minecraftFontRenderer;
    }

    public CFontRenderer getCustomFontRenderer() {
        return customFontRenderer;
    }

    public void drawString(String text, float x, float y, int color) {
        switch(getFontType()) {
            case MINEMAN: {
                getMinecraftFontRenderer().drawString(text, (int)x, (int)y, color);
                break;
            }
            case CUSTOM: {
                getCustomFontRenderer().drawCenteredStringWithShadow(text, x, y, color);
                break;
            }
        }
    }

    public void drawStringWithShadow(String text, float x, float y, int color) {
        switch(getFontType()) {
            case MINEMAN: {
                getMinecraftFontRenderer().drawStringWithShadow(text, x, y, color);
                break;
            }
            case CUSTOM: {
                getCustomFontRenderer().drawStringWithShadow(text, x, y, color);
                break;
            }
        }
    }

    public void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        switch(getFontType()) {
            case MINEMAN: {

                break;
            }
            case CUSTOM: {
                getCustomFontRenderer().drawCenteredStringWithShadow(text, x, y, color);
                break;
            }
        }
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        switch(getFontType()) {
            case MINEMAN: {
                break;
            }
            case CUSTOM: {
                getCustomFontRenderer().drawCenteredString(text, x, y, color);
                break;
            }
        }
    }

    public int getStringWidth(String text) {
      if(getFontType() == FontType.CUSTOM) {
          return getCustomFontRenderer().getStringWidth(text);
      }
      if(getFontType() == FontType.MINEMAN) {
          return getMinecraftFontRenderer().getStringWidth(text);
      }
      return 0;
    }

    public int getFontHeight() {
        if(getFontType() == FontType.CUSTOM) {
            return getCustomFontRenderer().getHeight();
        }
        if(getFontType() == FontType.MINEMAN) {
            return getMinecraftFontRenderer().FONT_HEIGHT;
        }
        return 0;
    }

}
