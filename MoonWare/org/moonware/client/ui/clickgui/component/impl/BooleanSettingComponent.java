package org.moonware.client.ui.clickgui.component.impl;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.render.RenderUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.ui.clickgui.component.Component;
import org.moonware.client.ui.clickgui.component.PropertyComponent;

import java.awt.*;

public class BooleanSettingComponent extends org.moonware.client.ui.clickgui.component.Component implements PropertyComponent {

    public BooleanSetting booleanSetting;
    public float textHoverAnimate;
    public float leftRectAnimation;
    public float rightRectAnimation;

    public BooleanSettingComponent(Component parent, BooleanSetting booleanSetting, int x, int y, int width, int height) {
        super(parent, booleanSetting.getName(), x, y, width, height);
        this.booleanSetting = booleanSetting;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (booleanSetting.isVisible()) {

            int x = getX();
            int y = getY();
            int width = getWidth();
            int height = getHeight();
            int middleHeight = getHeight() / 2;
            boolean hovered = isHovered(mouseX, mouseY);
            if (ClickGui.colored.get())
                RectHelper.drawRect(x -2,y,x + width + 2,y + height,new Color(51,51,51).getRGB());
            int color = 0;
            Color colors = Color.WHITE;
            Color colorss = Color.WHITE;
            Color colorsss = Color.WHITE;
            Color onecolor = new Color(ClickGui.color.getColorValue());
            Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
            double speed = ClickGui.speed.getNumberValue();
            switch (ClickGui.clickGuiColor.currentMode) {
                case "Client":
                    color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                    break;
                case "Fade":
                    color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1));
                    break;
                case "Color Two":
                    color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1));
                    break;
                case "Astolfo":
                    color = PaletteHelper.astolfo(true, y).getRGB();
                    break;
                case "Static":
                    color = onecolor.getRGB();
                    break;
                case "Rainbow":
                    color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                    break;
            }
            switch (ClickGui.clickGuiColor.currentMode) {
                case "Client":
                    colors = new Color(PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1))) ;
                    break;
                case "Fade":
                    colors = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1))) ;
                    break;
                case "Color Two":
                    colors = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1))) ;
                    break;
                case "Astolfo":
                    colors = new Color(PaletteHelper.astolfo(true, y).getRGB()) ;
                    break;
                case "Static":
                    colors = new Color(onecolor.getRGB());
                    break;
                case "Rainbow":
                    colors = new Color(PaletteHelper.rainbow(300, 1, 1).getRGB()) ;
                    break;
            }switch (ClickGui.clickGuiColor.currentMode) {
                case "Client":
                    colorss = new Color(PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1))) ;
                    break;
                case "Fade":
                    colorss = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1))) ;
                    break;
                case "Color Two":
                    colorss = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1))) ;
                    break;
                case "Astolfo":
                    colorss = new Color(PaletteHelper.astolfo(true, y).getRGB()) ;
                    break;
                case "Static":
                    colorss = new Color(onecolor.getRGB());
                    break;
                case "Rainbow":
                    colorss = new Color(PaletteHelper.rainbow(300, 1, 1).getRGB()) ;
                    break;
            }

            switch (ClickGui.booleanbackcolormode.currentMode) {
                case "Client":
                    colorsss = new Color(PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1))) ;
                    break;
                case "Fade":
                    colorsss = new Color(PaletteHelper.fadeColor(ClickGui.booleanbackcolor.getColorValue(), new Color(ClickGui.booleanbackcolor.getColorValue()).darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1))) ;
                    break;
                case "Color Two":
                    colorsss = new Color(PaletteHelper.fadeColor(ClickGui.booleanbackcolor.getColorValue(), ClickGui.booleanbackcolor2.getColorValue(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1))) ;
                    break;
                case "Astolfo":
                    colorsss = new Color(PaletteHelper.astolfo(true, y).getRGB()) ;
                    break;
                case "Static":
                    colorsss = new Color(ClickGui.booleanbackcolor.getColorValue());
                    break;
                case "Rainbow":
                    colorsss = new Color(PaletteHelper.rainbow(300, 1, 1).getRGB()) ;
                    break;
            }
            MWFont.GREYCLIFFCF_MEDIUM.get(18).draw(getName(), x + 3, y + middleHeight - 2 - 2, -1);
            textHoverAnimate = AnimationHelper.animation(textHoverAnimate, hovered ? 2.3f : 2, 0);
            leftRectAnimation = AnimationHelper.animation(leftRectAnimation, booleanSetting.getBoolValue() ? 8 : 18, 0);
            rightRectAnimation = AnimationHelper.animation(rightRectAnimation, (booleanSetting.getBoolValue() ? 160 : 200), 0);
            //RectHelper.drawGlow(x + width - 22.5f, y + 9.5f, x + width - 22.5f, y + 9.5f, color);
            GlStateManager.pushMatrix();
            GlStateManager.pushMatrix();
            //RenderHelper2.renderBlurredShadow(new Color(31,31,31),x + width - 17 - 5f - 10 +1,y + 11,24 + 5-6,22-11, 6);
            RoundedUtil.drawRound(x + width - 17 - 5f - 10 + 1 + 5 + 5 - 2,y + 11 + 5 - 1 - 3 - 10,24 + 5 - 6 - 5 + 2, 22 - 11 - 5 + 2, 3.8F,new Color(255, 255, 255, 255));
            RenderUtils2.drawBlurredShadow(x + width - 17 - 5f - 10 + 1 + 5 + 5 - 2,y + 11 + 5 - 1 - 3 - 10,24 + 5 - 6 - 5 + 2, 22 - 11 - 5 + 2, 9,new Color(255, 255, 255, 255));

            //RoundedUtil.drawRoundOutline(x + width - 17 - 5f - 10 + 1 + 5 + 5,y + 11 + 5 - 1 - 5,24 + 5 - 6 - 5, 22 - 11 - 5 + 2, 3F, 1, booleanSetting.getBoolValue() ? colorsss : new Color(133, 133, 133, 255),new Color(255,255,255,100));

            //RenderHelper.drawImage(new Namespaced("moonware/crug.png"), x + width - leftRectAnimation - 10, y + 5 + 5, 12,12,Color.white);
            RenderUtil.drawGoodCircle((int) (x + width - leftRectAnimation), y + 5 +14 - 3 - 10, 4,  new Color((int) rightRectAnimation, (int) rightRectAnimation, (int) rightRectAnimation).getRGB());
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && isHovered(mouseX, mouseY) && booleanSetting.isVisible()) {
            booleanSetting.setBoolValue(!booleanSetting.getBoolValue());
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public Setting getSetting() {
        return booleanSetting;
    }
}
