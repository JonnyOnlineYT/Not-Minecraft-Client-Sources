package org.moonware.client.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.render.RenderUtil;
import org.moonware.client.helpers.math.MathematicHelper;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.clickgui.component.Component;
import org.moonware.client.ui.clickgui.component.PropertyComponent;

import java.awt.*;

public class NumberSettingComponent extends Component implements PropertyComponent {

    public NumberSetting numberSetting;
    public float currentValueAnimate;
    public float currentflyAnim;
    public int currentflyAnimA;
    private boolean sliding;

    public NumberSettingComponent(Component parent, NumberSetting numberSetting, int x, int y, int width, int height) {
        super(parent, numberSetting.getName(), x, y, width, height);
        this.numberSetting = numberSetting;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);

        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        double min = numberSetting.getMinValue();
        double max = numberSetting.getMaxValue();
        boolean hovered = isHovered(mouseX, mouseY);
        if (ClickGui.colored.get())
            RectHelper.drawRect(x - 2,y,x + width + 2,y + (height),new Color(51,51,51).getRGB());
        if (sliding) {
            numberSetting.setValueNumber((float) MathematicHelper.round((double) (mouseX - x - 3) * (max - min) / (double) width + min, numberSetting.getIncrement()));
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
                color2 = new Color(PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1))) ;
                break;
            case "Fade":
                color2 = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1))) ;
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
        RectHelper.drawRect(x + 3, y + height - 5 + 0.5F, x + (width - 3), y + 13 - 0.5F, new Color(40, 39, 39).getRGB());




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

        MWFont.GREYCLIFFCF_MEDIUM.get(18).drawShadow(numberSetting.getName(), x + 2.0F, y + height / 2.5F - 4F, Color.WHITE.getRGB());
        //mc.circleregularSmall.drawStringWithShadow(optionValue + " " + valueString, x + width - mc.circleregularSmall.getStringWidth(optionValue + " " + valueString) - 5, y + height / 2.5F - 4F, Color.WHITE.getRGB());

        currentflyAnim = (float) Interpolator.linear(currentflyAnim, sliding ? -6 : 40,2f/15);
        currentflyAnimA = Interpolator.linear(currentflyAnimA, sliding ? 250 : 0, 2f / 25);
        RoundedUtil.drawRoundOutline(x + 5 + currentValueAnimate * (width - 8) - 10,y + height / 2.5F - 4F - 10 + currentflyAnim, 5 + MWFont.GREYCLIFFCF_MEDIUM.get(18).getWidth(String.valueOf(optionValue)), 10 + MWFont.GREYCLIFFCF_MEDIUM.get(18).getHeight(),3,0.3F, new Color(71,71,71, currentflyAnimA),new Color(11,11,11, currentflyAnimA));
        if (sliding)
            MWFont.GREYCLIFFCF_MEDIUM.get(16).drawCenter(String.valueOf(optionValue),x + 5 + currentValueAnimate * (width - 8) - 10 + (5 + MWFont.GREYCLIFFCF_MEDIUM.get(18).getWidth(String.valueOf(optionValue))) / 2,y + height / 2.5F - 4f - 10 + currentflyAnim + MWFont.GREYCLIFFCF_MEDIUM.get(18).getHeight(), new Color(231,231,231,currentflyAnimA).getRGB());

        if (hovered) {
            if (numberSetting.getDesc() != null) {
                RectHelper.drawBorderedRect(x + 120, y + height / 1.5F + 3.5F, x + 138 + Minecraft.font.getStringWidth(numberSetting.getDesc()) - 10, y + 3.5F, 0.5F, new Color(30, 30, 30, 255).getRGB(), color, true);
                Minecraft.font.drawString(numberSetting.getDesc(), x + 124, y + height / 1.5F - 6F, -1);
            }
        }
        if (ClickGui.глов.getBoolValue()) {
            //GlowUtil.drawBlurredGlow(x + 1.5f - ClickGui.glowStrengh.getNumberValue(), y + 11 - 0.7f, x + 5 + currentValueAnimate * (width - 8), y + 18F + (-0.7f), ClickGui.glowColornumber.getColor());
            //RectHelper.drawSmoothRect(x + 4 + currentValueAnimate * (width - 8),y + 11.5f, x + 3 + currentValueAnimate * (width - 8) + 3,y + 11.5f + 6.5F + (-0.7F),new Color(255,255,255,255).getRGB());
            //GlowUtil.drawBlurredGlow(x + 4 + currentValueAnimate * (width - 8) + 0.05f,y + 11.5F - 0.05f,x + 3 + currentValueAnimate * (width - 8) + 3 - 0.08f, y + 11.5F + 6.5f + 1.5F + (-0.7F) + 0.05f,new Color(255,255,255,255).getRGB());
            RenderHelper2.renderBlurredShadow(new Color(color),x + 3,y + 13, 5 + currentValueAnimate * (width - 8) + 1f, 2F, 7);
            RectHelper.drawRect(x + 3, y + 13, x + 5 + currentValueAnimate * (width - 8), y + 15F, color);
        }else {
            RectHelper.drawRect(x + 3, y + 13.5F, x + 5 + currentValueAnimate * (width - 8), y + 14.5F, -1);

            RenderUtil.drawGoodCircle(x + 5 + currentValueAnimate * (width - 8),y + 13.9F,2.6F,-1);
            //RenderUtils2.drawBlur(3,() -> RenderUtil.drawGoodCircle(x + 3,y + 13.9F,2.6F,-1));
            RenderUtils2.drawShadow(17,1,() -> RenderUtil.drawGoodCircle(x + 5 + currentValueAnimate * (width - 8),y + 13.9F,2.6F,-1));
            //RenderHelper2.drawRainbowRoundGui(x + 3,y + 13 + 1,  2 + currentValueAnimate * (width - 8), 1,1,true,true,false,true,2,4);
            //RenderHelper.drawCircle(x + 5 + currentValueAnimate * (width - 8), y + 14F, 2, true, new Color(color));
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (!sliding && button == 0 && isHovered(mouseX, mouseY)) {
            sliding = true;
        }
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
