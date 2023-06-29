package ru.rockstar.client.ui.csgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import ru.rockstar.api.utils.math.MathematicHelper;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.ui.csgui.component.Component;
import ru.rockstar.client.ui.csgui.component.PropertyComponent;
import ru.rockstar.client.ui.settings.Setting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import java.awt.*;


public class NumberSettingComponent extends Component implements PropertyComponent {

    public NumberSetting numberSetting;
    public float currentValueAnimate = 0f;
    private boolean sliding;
    Minecraft mc = Minecraft.getMinecraft();
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

        if (this.sliding) {
            numberSetting.setValueNumber((float) MathematicHelper.round((double) (mouseX - x) * (max - min) / (double) width + min, numberSetting.getIncrement()));
            if (numberSetting.getNumberValue() > max) {
                numberSetting.setValueNumber((float) max);
            } else if (numberSetting.getNumberValue() < min) {
                numberSetting.setValueNumber((float) min);
            }
        }

        float amountWidth = (float) (((numberSetting.getNumberValue()) - min) / (max - min));
        int color = 0;
        Color onecolor = new Color(ClickGUI.color.getColorValue());
        Color twocolor = new Color(ClickGUI.colorTwo.getColorValue());
        double speed = ClickGUI.speed.getNumberValue();
        switch (ClickGUI.clickGuiColor.currentMode) {
            case "Client":
                color = DrawHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = DrawHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1));
                break;
            case "Color Two":
                color = DrawHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = DrawHelper.astolfo(true, y).getRGB();
                break;
            case "Static":
                color = onecolor.getRGB();
                break;
            case "Rainbow":
                color = DrawHelper.rainbow(300, 1, 1).getRGB();
                break;
        }

        currentValueAnimate = AnimationHelper.animation(currentValueAnimate, amountWidth, 0);
        float optionValue = (float) MathematicHelper.round(numberSetting.getNumberValue(), numberSetting.getIncrement());
        DrawHelper.drawRect(x + 3, y + height - 5, x + (width - 3), y + 13, new Color(40, 39, 39).getRGB());

        DrawHelper.drawRect(x + 3, y + 13.5, x + 5 + currentValueAnimate * (width - 9), y + 15F, color);
        DrawHelper.drawFilledCircle((int) (x + 5 + currentValueAnimate * (width - 8)), (int) (y + 14F), 2F,  new Color(255,255,255));

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

        mc.neverlose500_13  .drawString(numberSetting.getName(), x + 2.0F, y + height / 2.5F - 4F, Color.lightGray.getRGB());
        mc.neverlose500_15.drawString(optionValue + " " + valueString, x + width - mc.neverlose500_16.getStringWidth(optionValue + " " + valueString) - 5, y + height / 2.5F - 4F, Color.GRAY.getRGB());

        if (hovered) {
            if (numberSetting.getDesc() != null) {
                DrawHelper.drawNewRect(x + 120, y + height / 1.5F + 3.5F, x + 138 + mc.fontRendererObj.getStringWidth(numberSetting.getDesc()) - 10, y + 3.5F,  new Color(30, 30, 30, 255).getRGB());
                mc.fontRendererObj.drawStringWithShadow(numberSetting.getDesc(), x + 124, y + height / 1.5F - 6F, -1);
            }
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
        this.sliding = false;
    }

    @Override
    public Setting getSetting() {
        return numberSetting;
    }
}
