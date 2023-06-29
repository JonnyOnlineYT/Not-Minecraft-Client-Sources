package org.moonware.client.ui.guiWithScroll.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.render.RenderUtil;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.ui.guiWithScroll.component.Component;
import org.moonware.client.ui.guiWithScroll.component.PropertyComponent;
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
        return 14;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (booleanSetting.isVisible()) {
        	String mode = "Rockstar New";
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            if (mode.equalsIgnoreCase("Rockstar New")) {
            	GlStateManager.enable(GL11.GL_SCISSOR_TEST);
                RenderHelper2.scissorRect(0, getY2() + 47, sr.getScaledWidth(), getY2() + 260);
            }
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
            if (feature.getState())
                MWFont.MONTSERRAT_BOLD.get(12).draw(getName(), x + 2, y +  3, -1);
            else{
                MWFont.MONTSERRAT_BOLD.get(12).draw(getName(), x + 2, y + 3, new Color(1,1,1).getRGB());
            }
            textHoverAnimate = AnimationHelper.animation(textHoverAnimate, hovered ? 2.3f : 2, 0);
            leftRectAnimation = AnimationHelper.animation(leftRectAnimation, booleanSetting.getBoolValue() ? 8 : 18, 0);
            this.rightRectAnimation = (float) Interpolator.linear(this.rightRectAnimation, (booleanSetting.getBoolValue() ? 230 : 170), 2f/20);
            //RectHelper.drawGlow(x + width - 22.5f, y + 9.5f, x + width - 22.5f, y + 9.5f, color);
            GlStateManager.pushMatrix();
            GlStateManager.pushMatrix();
            //RenderHelper2.renderBlurredShadow(new Color(31,31,31),x + width - 17 - 5f - 10 +1,y + 11,24 + 5-6,22-11, 6);
            RenderUtils2.drawBlurredShadow(x + width - 17 - 5f - 10 + 1 + 5 + 5 - 2,y + 11 + 5 - 1 - 3 - 10 - 2,24 + 5 - 6 - 5 + 2, 22 - 11 - 5 + 2, 5,new Color(0, 0, 0, 255));
            RoundedUtil.drawRound(x + width - 17 - 5f - 10 + 1 + 5 + 5 - 2,y + 11 + 5 - 1 - 3 - 10 - 2,24 + 5 - 6 - 5 + 2, 22 - 11 - 5 + 2, 3.8F,!booleanSetting.get() ? new Color(210, 210, 210, 255) : new Color(ClickGui.color.getColor()));

            //RoundedUtil.drawRoundOutline(x + width - 17 - 5f - 10 + 1 + 5 + 5,y + 11 + 5 - 1 - 5,24 + 5 - 6 - 5, 22 - 11 - 5 + 2, 3F, 1, booleanSetting.getBoolValue() ? colorsss : new Color(133, 133, 133, 255),new Color(255,255,255,100));

            //RenderHelper.drawImage(new Namespaced("moonware/crug.png"), x + width - leftRectAnimation - 10, y + 5 + 5, 12,12,Color.white);
            RenderUtil.drawGoodCircle((int) (x + width - leftRectAnimation), y + 5 +14 - 3 - 10- 2, 4,  new Color((int) rightRectAnimation, (int) rightRectAnimation, (int) rightRectAnimation).getRGB());
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
            if (mode.equalsIgnoreCase("Rockstar New")) {
                GlStateManager.disable(GL11.GL_SCISSOR_TEST);
            }
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
