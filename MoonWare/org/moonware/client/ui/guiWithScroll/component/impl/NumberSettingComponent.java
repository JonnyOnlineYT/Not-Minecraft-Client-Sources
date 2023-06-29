package org.moonware.client.ui.guiWithScroll.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.math.MathematicHelper;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.guiWithScroll.component.Component;
import org.moonware.client.ui.guiWithScroll.component.PropertyComponent;

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
        String mode = "Rockstar New";
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (mode.equalsIgnoreCase("Rockstar New")) {
            GlStateManager.pushMatrix();
            GlStateManager.enable(GL11.GL_SCISSOR_TEST);
            RenderHelper.scissorRect(0, getY2() + 47, sr.getScaledWidth(), getY2() + 260);
        }
        int x = getX() + 4 + MWFont.MONTSERRAT_BOLD.get(10).getWidth(String.valueOf(numberSetting.getMinValue()));
        int xx = getX();
        int y = (int) (getY());
        int width = getWidth() - 14 - MWFont.MONTSERRAT_BOLD.get(10).getWidth(String.valueOf(numberSetting.getMaxValue())) - MWFont.MONTSERRAT_BOLD.get(10).getWidth(String.valueOf(numberSetting.getMinValue())) / 2;
        int wwidth = getWidth();
        int height = getHeight();
        double min = numberSetting.getMinValue();
        double max = numberSetting.getMaxValue();
        boolean hovered = isHovered(mouseX, mouseY);

        if (sliding) {
            numberSetting.setValueNumber((float) MathematicHelper.round((double) (mouseX - x) * (max - min) / (double) width + min, numberSetting.getIncrement()));
            if (numberSetting.getNumberValue() > max) {
                numberSetting.setValueNumber((float) max);
            } else if (numberSetting.getNumberValue() < min) {
                numberSetting.setValueNumber((float) min);
            }
        }

        float amountWidth = (float) (((numberSetting.getNumberValue()) - min) / (max - min));
        int color = 0;
        Color color2 = Color.WHITE;
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
        switch (ClickGui.clickGuiColor.currentMode) {
            case "Client":
                color2 = new Color(PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1)));
                break;
            case "Fade":
                color2 = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1)));
                break;
            case "Color Two":
                color2 = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1)));
                break;
            case "Astolfo":
                color2 = PaletteHelper.astolfo(true, y);
                break;
            case "Static":
                color2 = onecolor;
                break;
            case "Rainbow":
                color2 = PaletteHelper.rainbow(300, 1, 1);
                break;
        }

        currentValueAnimate = AnimationHelper.animation(currentValueAnimate, amountWidth, 0);
        float optionValue = (float) MathematicHelper.round(numberSetting.getNumberValue(), numberSetting.getIncrement());
        //RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 160).getRGB());
        //RectHelper.drawRect(x + 3, y + height - 5, x + (width - 3), y + 13, new Color(40, 39, 39).getRGB());


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

        MWFont.MONTSERRAT_BOLD.get(12).draw(numberSetting.getName(), xx + 0.5F, y + height / 2.5F - 5F, feature.getState() ? -1 : new Color(0, 0, 0).getRGB());
        //RoundedUtil.drawRound(x, y + 13 + 1.5F, (width) - 4, 0.5F,1,!feature.getState() ? new Color(42, 42, 42) : new Color(-1));
        RectHelper.drawRect(x, y + 13 + 1.5F, x + (width) - 4, y + 13 + 1.5F + 0.5F, !feature.getState() ? new Color(42, 42, 42).getRGB() : new Color(-1).getRGB());
        currentCircleAnimation = AnimationHelper.calculateCompensation(18 - 5, currentCircleAnimation, 0, 1);
        //RoundedUtil.drawRound(x + 3, y + 13, 4 + currentValueAnimate * (width - 8) + numberSetting.getMinValue() == numberSetting.getNumberValue() ? 42 : 0, 3, 1,new Color(91,91,91));
        DrawHelper.drawRect2(x + 3, y + 13, x + 3 + 4 + currentValueAnimate * (width - 8) + numberSetting.getMinValue() == numberSetting.getNumberValue() ? 42 : 0, y + 13 + 3, new Color(91, 91, 91).getRGB());
        RectHelper.drawCircle(x + 3 + currentValueAnimate * (width - 8), y + 14.8F, 0, 395, (33 - 5) / 10, 4, true, !feature.getState() ? new Color(91, 91, 91) : new Color(-1));
        RectHelper.drawCircle(x + 3 + currentValueAnimate * (width - 8), y + 14.8F, 0, 395, currentCircleAnimation / 10, 4, true, new Color(ClickGui.color.getColor()));
        MWFont.MONTSERRAT_BOLD.get(9).draw(String.valueOf(numberSetting.getNumberValue()), x + 3 + currentValueAnimate * (width - 8) - 5, y + 22.8F, !feature.getState() ? new Color(1,1,1).getRGB() : -1);
        MWFont.MONTSERRAT_BOLD.get(10).draw(String.valueOf(numberSetting.getMinValue()), x - 2 - MWFont.MONTSERRAT_BOLD.get(10).getWidth(String.valueOf(numberSetting.getMinValue())), y + 14.8F, !feature.getState() ? new Color(1,1,1).getRGB() : -1);
        MWFont.MONTSERRAT_BOLD.get(10).draw(String.valueOf(numberSetting.getMaxValue()), x + width - 2, y + 14.8F, !feature.getState() ? new Color(1,1,1).getRGB() : -1);

        if (mode.equalsIgnoreCase("Rockstar New")) {
            GlStateManager.disable(GL11.GL_SCISSOR_TEST);
            GlStateManager.popMatrix();
        }
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
