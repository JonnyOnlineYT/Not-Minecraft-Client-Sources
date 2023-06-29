package ru.rockstar.client.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import ru.rockstar.api.utils.math.MathematicHelper;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.ui.clickgui.ClickGuiScreen;
import ru.rockstar.client.ui.clickgui.component.Component;
import ru.rockstar.client.ui.clickgui.component.PropertyComponent;
import ru.rockstar.client.ui.settings.Setting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import java.awt.*;

import org.lwjgl.opengl.GL11;

import javafx.animation.Interpolator;


public class NumberSettingComponent extends Component implements PropertyComponent {

    public NumberSetting numberSetting;
    public float currentValueAnimate = 0f;
    public float circleAnimate = 0f;
    private boolean sliding;
    Minecraft mc = Minecraft.getMinecraft();
    public NumberSettingComponent(Component parent, NumberSetting numberSetting, int x, int y, int width, int height) {
        super(parent, numberSetting.getName(), x, y, width, height);
        this.numberSetting = numberSetting;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        String mode = ClickGUI.style.getOptions();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (mode.equalsIgnoreCase("Rockstar New")) {
        	GlStateManager.pushMatrix();
        	GlStateManager.enable(GL11.GL_SCISSOR_TEST);
        	DrawHelper.scissorRect(0, getY2() + 47, getX() + 98, getY2() + 253);
        }
        int x = getX();
        boolean a = ClickGuiScreen.progress >= 0.9f;
        int y = a ? ((getY())) : (- 1000);
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
        this.circleAnimate = (float)Interpolator.LINEAR.interpolate((double)this.circleAnimate, hovered ? 2.3f : 2, 0.01);
        currentValueAnimate = amountWidth;
        float optionValue = (float) MathematicHelper.round(numberSetting.getNumberValue(), numberSetting.getIncrement());

        Color l1 = ClickGUI.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor.brighter() : new Color(color).brighter();
        Color l2 = ClickGUI.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor : new Color(color);
    	
        RoundedUtil.drawGradientRound(x + 2, (float) (y + 13.5) - 2, x + 7 + (width - 9) - (x + 3), (float) (y + 15F + 2.5f - ((y + 13.5) - 1)), 2, l1, l1, l2, l2);
       
        RoundedUtil.drawRound(x + 3.5f, (float) (y + 13.5) - 1, x + 4 + currentValueAnimate * (width - 9) - (x + 3), (float) (y + 15F + 0.5f - ((y + 13.5) - 1)), 1, new Color(50, 50, 50));

        
      //  DrawHelper.drawFilledCircle((int) (x + 5 + currentValueAnimate * (width - 8)), (int) (y + 14F), hovered ? 3F : 2,  new Color(255,255,255));

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
        
        mc.mntsb.drawString(optionValue + " " + valueString, x + currentValueAnimate * width - mc.neverlose500_16.getStringWidth(optionValue + " " + valueString) + 11, y + height / 2.5F - 4F + 15, Color.WHITE.getRGB());


        mc.mntsb.drawString(numberSetting.getName(), x + 2.0F, y + height / 2.5F - 6F, Color.WHITE.getRGB());

        if (hovered) {
            if (numberSetting.getDesc() != null) {
                DrawHelper.drawNewRect(x + 120, y + height / 1.5F + 3.5F, x + 138 + mc.fontRendererObj.getStringWidth(numberSetting.getDesc()) - 10, y + 3.5F,  new Color(30, 30, 30, 255).getRGB());
                mc.fontRendererObj.drawStringWithShadow(numberSetting.getDesc(), x + 124, y + height / 1.5F - 6F, -1);
            }
        }
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
    public void onMouseRelease(int button) {
        this.sliding = false;
    }

    @Override
    public Setting getSetting() {
        return numberSetting;
    }
}
