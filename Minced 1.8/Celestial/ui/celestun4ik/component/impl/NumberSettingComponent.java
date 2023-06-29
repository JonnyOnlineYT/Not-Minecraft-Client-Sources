package Celestial.ui.celestun4ik.component.impl;

import Celestial.Smertnix;
import Celestial.module.impl.Render.ClickGUI;
import Celestial.ui.celestun4ik.component.Component;
import Celestial.ui.celestun4ik.component.PropertyComponent;
import Celestial.ui.settings.Setting;
import Celestial.utils.math.AnimationHelper;
import Celestial.utils.math.MathematicHelper;
import Celestial.utils.render.RenderUtils;
import Celestial.utils.render.RoundedUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;


public class NumberSettingComponent extends Celestial.ui.celestun4ik.component.Component implements PropertyComponent {

    public NumberSetting numberSetting;
    public float currentValueAnimate = 0f;
    private boolean sliding;
    Minecraft mc = Minecraft.getMinecraft();
    public NumberSettingComponent(Component parent, NumberSetting numberSetting, int x, int y, int width, int height) {
        super(parent, numberSetting.getName(), x, y, width, height);
        this.numberSetting = numberSetting;
    }
    getScaledWidth(), 239);
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        double min = numberSetting.getMinValue();
        double max = numberSetting.getMaxValue();
        boolean hovered = isHovered(mouseX, mouseY);

        if (this.sliding) {
            numberSetting.setValueNumber((float) MathematicHelper.round((double) (mouseX - x + 20) * (max - min) / (double) width + min, numberSetting.getIncrement()));
            if (numberSetting.getNumberValue() > max) {
                numberSetting.setValueNumber((float) max);
            } else if (numberSetting.getNumberValue() < min) {
                numberSetting.setValueNumber((float) min);
            }
        }

        float amountWidth = (float) (((numberSetting.getNumberValue()) - (min)) / (max - (min)));
        Color onecolor = new Color(ClickGUI.color.getColorValue());
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
        currentValueAnimate = AnimationHelper.animation(currentValueAnimate, amountWidth, 0);
        float optionValue = (float) MathematicHelper.round(numberSetting.getNumberValue(), numberSetting.getIncrement());


        RenderUtils.drawRect(x + 13, y + 13.5, x + 5 + 1 * (width - 30), y + 15F, 0);

        mc.mntsb_13.drawString("" + String.format("%.1f", Float.valueOf("" + min)), x - 1, y + height- 11, Smertnix.instance.featureManager.getFeature(parent.getName()).isEnabled() ? -1 : 0);

        mc.mntsb_13.drawString("" + String.format("%.1f", Float.valueOf("" + max)), x + width - mc.mntsb_13.getStringWidth("" + String.format("%.1f", Float.valueOf("" + max))) - 4, y + height- 11, Smertnix.instance.featureManager.getFeature(parent.getName()).isEnabled() ? -1 : 0);
 y + 15F, Smertnix.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : new Color(ClickGUI.color.getColorValue()).getRGB(), Smertnix.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : new Color(ClickGUI.color.getColorValue()).darker().getRGB());

                RenderUtils.drawRect(x, y, x, y, Smertnix.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : new Color(ClickGUI.color.getColorValue()).darker().getRGB());
                `isEnabled() ? Color.WHITE : new Color(ClickGUI.color.getColorValue()).darker());

                RoundedUtil.drawRound((int) (x + 3 + currentValueAnimate * (width - 32)), (int) (y + 11.7f), 5, 5, 2f, Smertnix.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE : new Color(ClickGUI.color.getColorValue()).darker());

                RoundedUtil.drawRound((int) (x + 4 + currentValueAnimate * (width - 32)), (int) (y + 12.7f), 3, 3, 1, Smertnix.instance.featureManager.getFeature(parent.getName()).isEnabled() ? new Color(ClickGUI.color.getColorValue()).darker() : Color.WHITE);

                mc.mntsb_15.drawString(numberSetting.getName(), x - 1, y + height / 2.5F - 7F, Smertnix.instance.featureManager.getFeature(parent.getName()).isEnabled() ? -1 : 0);
                mc.mntsb_14.drawString(optionValue + " " + valueString, x + currentValueAnimate * (width - (mc.mntsb_14.getStringWidth(optionValue + " " + valueString)/ 2) - 30), y + height / 2.5F - 4F + 14, Smertnix.instance.featureManager.getFeature(parent.getName()).isEnabled() ? -1 : 0);

                if (hovered) {
            if (numberSetting.getDesc() != null) {
                Renderx + width + 20, y + height / 1.5F + 4.5F, x + width + 25 + mc.rubik_18.getStringWidth(numberSetting.getDesc()), y + 5.5F, 0);
                mc.mntsb_15.drawString(numberSetting.getDesc(), x + width + 22, y + height / 1.35F - 5F, -1);
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
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
