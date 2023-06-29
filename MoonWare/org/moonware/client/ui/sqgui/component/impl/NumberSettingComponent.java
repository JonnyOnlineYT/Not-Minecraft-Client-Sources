package org.moonware.client.ui.sqgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.math.MathematicHelper;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render.GlowUtil;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.sqgui.component.Component;
import org.moonware.client.ui.sqgui.component.PropertyComponent;
import org.moonware.client.utils.MWFont;

import java.awt.*;


public class NumberSettingComponent extends Component implements PropertyComponent {

    public NumberSetting numberSetting;
    public float currentValueAnimate;
    private boolean sliding;
    private Feature feature;
    Minecraft mc = Minecraft.getMinecraft();
    public NumberSettingComponent(Component parent, NumberSetting numberSetting, int x, int y, int width, int height, Feature feature) {
        super(parent, numberSetting.getName(), x, y, width, height);
        this.numberSetting = numberSetting;
        this.feature = feature;
    }
    private  float currentCircleAnimation;
    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        GlStateManager.pushMatrix();
        GlStateManager.enable(GL11.GL_SCISSOR_TEST);
        RenderHelper.scissorRect(0, getY2() + 47, sr.getScaledWidth(), getY2() + 260);

        int x = getX() + 2;
        int y = getY();
        int width = getWidth() - 8;
        int height = getHeight();
        double min = numberSetting.getMinValue();
        double max = numberSetting.getMaxValue();
        boolean hovered = isHovered(mouseX, mouseY);

        if (this.sliding) {
            numberSetting.setValueNumber((float) MathematicHelper.round((double) (mouseX - x) * (max - min) / (double) width + min, numberSetting.getIncrement()));
            if (numberSetting.getNumberValue() > max) {
                numberSetting.setValueNumber((float) max);
            } else if (numberSetting.getNumberValue() < min) {
                numberSetting.setValueNumber((float) min);
            }
        }
        x = getX();
        y = getY();
        width  = getWidth();
        height = getHeight();
        float amountWidth = (float) (((numberSetting.getNumberValue()) - min) / (max - min));
        int color = 0;
        Color onecolor = new Color(ClickGui.color.getColorValue());
        Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
        double speed = ClickGui.speed.getNumberValue();
        switch (ClickGui.clickGuiColor.currentMode) {
            case "Client":
                color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1));
                break;
            case "Color Two":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1));
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

        currentValueAnimate = AnimationHelper.animation(currentValueAnimate, amountWidth, 0);
        float optionValue = (float) MathematicHelper.round(numberSetting.getNumberValue(), numberSetting.getIncrement());
        //RectHelper.drawRect(x -4.5f, y, x + width +5, y + height, new Color(0, 0, 0, 58).getRGB());
        RectHelper.drawRect(x + 3, y + 13, x + 3 + (width - 8), y + 15f, new Color(0, 0, 0, 119).getRGB());

        RoundedUtil.drawRound(x + 3, y + 13, currentValueAnimate * (width - 8), 1.9f, 0.2f, new Color(color));
        GlowUtil.drawBlurredShadow(x + 3, y + 13, currentValueAnimate * (width - 8), 1.9f,7, new Color(color),1);
        //RenderHelper.drawCircle(x + 8 + currentValueAnimate * (width - 8), y + 14F, 2, true, Color.WHITE);

        String valueString = "";

        NumberSetting.NumberType numberType = numberSetting.getType();

        switch (numberType) {
            case PERCENTAGE:
                valueString += '%';
                break;
            case MS:
                valueString += "ms";
                break;
            case DISTANCE:
                valueString += 'm';
            case SIZE:
                valueString += "SIZE";
            case APS:
                valueString += "APS";
                break;
            default:
                valueString = "";
        }

        MWFont.GREYCLIFFCF_MEDIUM.get(16).drawShadow(numberSetting.getName(), x + 2.0F, y + height / 2.5F - 9F, Color.white.getRGB());
        MWFont.GREYCLIFFCF_MEDIUM.get(16).drawShadow(optionValue + " " + valueString, x + width - MWFont.GREYCLIFFCF_MEDIUM.get(16).getWidth(optionValue + " " + valueString) - 5, y + height / 2.5F - 9F, Color.white.getRGB());

        if (hovered) {
            if (numberSetting.getDesc() != null) {
            }
        }
        GlStateManager.disable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (!sliding && button == 0 && isHovered(mouseX, mouseY)) {
            sliding = true;
        }
    }

    @Override
    public int getHeight() {
        return 29;
    }

    @Override
    public void onMouseRelease(int button) {
        sliding = false;
    }

    @Override
    public Setting getSetting() {
        return numberSetting;
    }
}
