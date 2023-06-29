package org.moonware.client.ui.sqgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render.GlowUtil;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.ui.sqgui.component.Component;
import org.moonware.client.ui.sqgui.component.PropertyComponent;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;

import java.awt.*;


public class BooleanSettingComponent extends Component implements PropertyComponent {

    public BooleanSetting booleanSetting;
    public float textHoverAnimate;
    public float leftRectAnimation;
    public float rightRectAnimation;
    private Feature feature;
    Minecraft mc = Minecraft.getMinecraft();
    public BooleanSettingComponent(Component parent, BooleanSetting booleanSetting, int x, int y, int width, int height, Feature feature) {
        super(parent, booleanSetting.getName(), x, y, width, height);
        this.booleanSetting = booleanSetting;
        this.feature = feature;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (booleanSetting.isVisible()) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            GlStateManager.enable(GL11.GL_SCISSOR_TEST);
            RenderHelper2.scissorRect(0, getY2() + 47, sr.getScaledWidth(), getY2() + 260);

            int x = getX();
            int y = getY();
            int width = getWidth();
            int height = getHeight();
            int middleHeight = getHeight() / 2;
            boolean hovered = isHovered(mouseX, mouseY);
            int color = 0;
            Color colors = Color.WHITE;
            Color colorss = Color.WHITE;
            Color colorsss = Color.WHITE;
            Color onecolor = new Color(ClickGui.color.getColorValue());
            Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
            double speed = ClickGui.speed.getNumberValue();
            MWFont.GREYCLIFFCF_MEDIUM.get(16).drawShadow(getName(), getX() + 2, getY() +  3, -1);

            textHoverAnimate = AnimationHelper.animation(textHoverAnimate, hovered ? 2.3f : 2, 0);
            leftRectAnimation = AnimationHelper.animation(leftRectAnimation, !booleanSetting.getBoolValue() ? 9 : 17, 0);
            this.rightRectAnimation = (float) Interpolator.linear(this.rightRectAnimation, (booleanSetting.getBoolValue() ? 230 : 170), 2f/20);
            //RectHelper.drawGlow(x + width - 22.5f, y + 9.5f, x + width - 22.5f, y + 9.5f, color);
            GlStateManager.pushMatrix();
            GlStateManager.pushMatrix();
            //RenderHelper2.renderBlurredShadow(new Color(31,31,31),x + width - 17 - 5f - 10 +1,y + 11,24 + 5-6,22-11, 6);
            RoundedUtil.drawRound(getX() + width - 17 - 5f - 10 + 1 + 5 + 5 - 2,getY() + 11 + 5 - 1 - 3 - 10 - 2 + 1.5f,24 + 5 - 6 - 5 + 2, 16 - 3- 3, 3.8F,Color.black);
            GlowUtil.drawBlurredShadow(getX() + width - 17 - 5f - 10 + 1 + 5 + 5 - 2,getY() + 11 + 5 - 1 - 3 - 10 - 2 + 1.5f,24 + 5 - 6 - 5 + 2, 16 - 3- 3, 7,Color.black,0);

            //RoundedUtil.drawRoundOutline(x + width - 17 - 5f - 10 + 1 + 5 + 5,y + 11 + 5 - 1 - 5,24 + 5 - 6 - 5, 22 - 11 - 5 + 2, 3F, 1, booleanSetting.getBoolValue() ? colorsss : new Color(133, 133, 133, 255),new Color(255,255,255,100));

            //RenderHelper.drawImage(new Namespaced("moonware/crug.png"), x + width - leftRectAnimation - 10, y + 5 + 5, 12,12,Color.white);
            //RenderUtil.drawGoodCircle((int) (x + width - leftRectAnimation), y + 5 +14 - 3 - 10- 2, 4,  new Color(ClickGui.color.getColor()).getRGB());
            GlStateManager.enable(GL11.GL_BLEND);
            RoundedUtil.drawRound(getX() + width - 17 - 5f - 10 + 1 + leftRectAnimation,getY() + 11 + 5 - 1 - 3 - 10 - 2 + 1 + 1.5f,10, 14 - 3- 3, 2.8F,ClickGui.color.getColorc());
            GlowUtil.drawBlurredShadow(getX() + width - 17 - 5f - 10 + 1 + leftRectAnimation,getY() + 11 + 5 - 1 - 3 - 10 - 2 + 1 + 1.5f,10, 14 - 3- 3, 7,ClickGui.color.getColorc(),0);

            GlStateManager.disable(GL11.GL_BLEND);
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
            GlStateManager.disable(GL11.GL_SCISSOR_TEST);
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && isHovered(mouseX, mouseY) && booleanSetting.isVisible()) {
            booleanSetting.setBoolValue(!booleanSetting.getBoolValue());
        }
    }

    @Override
    public Setting getSetting() {
        return booleanSetting;
    }
}
