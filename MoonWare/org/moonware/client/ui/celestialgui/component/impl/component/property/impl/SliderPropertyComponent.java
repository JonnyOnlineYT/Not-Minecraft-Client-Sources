/*
 * Decompiled with CFR 0.150.
 */
package org.moonware.client.ui.celestialgui.component.impl.component.property.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.math.MathematicHelper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.celestialgui.component.Component;
import org.moonware.client.ui.celestialgui.component.impl.component.property.PropertyComponent;

import java.awt.*;

public class SliderPropertyComponent
extends org.moonware.client.ui.celestialgui.component.Component
implements PropertyComponent {
    public NumberSetting doubleProperty;
    private float currentValueAnimate;
    private boolean sliding;
    public static boolean sliding2 = true;
    private final TimerHelper descTimer = new TimerHelper();

    public SliderPropertyComponent(Component parent, NumberSetting property, float x, float y, float width, float height) {
        super(parent, property.getName(), x, y, width, height);
        doubleProperty = property;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        float x = (float) getX();
        float y = (float) getY();
        float width = getWidth();
        float height = getHeight();
        double min = doubleProperty.getMinimum();
        double max = doubleProperty.getMaximum();
        boolean hovered = isHovered(mouseX, mouseY);
        if (sliding && sliding2) {
            doubleProperty.setCurrentValue((float)MathematicHelper.round((double)((float)mouseX - x) * (max - min) / (double)width + min, doubleProperty.getIncrement()));
            if ((double) doubleProperty.getCurrentValue() > max) {
                doubleProperty.setCurrentValue((float)max);
            } else if ((double) doubleProperty.getCurrentValue() < min) {
                doubleProperty.setCurrentValue((float)min);
            }
        }
        float optionValue = (float)MathematicHelper.round(doubleProperty.getCurrentValue(), doubleProperty.getIncrement());
        float amountWidth = (float)(((double) doubleProperty.getCurrentValue() - min) / (max - min));
        String optionValueStr = String.valueOf(optionValue);
        if (doubleProperty.getName().equalsIgnoreCase("Rotation Speed") && doubleProperty.getDesc().startsWith("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c") && doubleProperty.getCurrentValue() >= 5.0f) {
            optionValueStr = optionValue + " (Max speed)";
        }
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        currentValueAnimate = AnimationHelper.animation2(currentValueAnimate, amountWidth, (float)((double)0.001f * Minecraft.frameTime * (double)0.1f));
        currentValueAnimate = MathHelper.clamp(currentValueAnimate, 0.001f, currentValueAnimate);
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 160).getRGB());
        RectHelper.drawBorderedRect(x + 3.0f, y + height - 5.0f + -1.0f, x + (width - 3.0f), y + height - 2.0f, new Color(40, 39, 39).getRGB(), 2.0);
        RectHelper.drawGradientRect(x + 4.0f, y + height - 5.0f, x + width * currentValueAnimate - 4.0f, y + height - 3.0f, color, new Color(color).brighter().getRGB());
        if (ClickGui.glow.getCurrentValue()) {
            float widthValue = width * currentValueAnimate - 6.0f;
            if (widthValue <= 0.0f) {
                widthValue = 0.001f;
            }
            RenderHelper2.renderBlurredShadow(new Color(color).brighter(), x + 4.0f, y + height - 5.0f, widthValue, 3.0, 15);
        }
        MWFont.SF_UI_DISPLAY_REGULAR.get(14).drawShadow(doubleProperty.getName(), x + 2.0f, y + height / 2.5f - 3.0f, -1);
        MWFont.SF_UI_DISPLAY_REGULAR.get(14).drawShadow(optionValueStr, x + width - (float) MWFont.SF_UI_DISPLAY_REGULAR.get(14).getWidth(optionValueStr) - 5.0f, y + height / 2.5f - 3.0f, Color.GRAY.getRGB());
        if (hovered) {
            if (doubleProperty.getDesc() != null && descTimer.hasReached(250.0F)) {
                RectHelper.drawBorder(x + 120.0f, y + height / 1.5f + 3.5f, x + 138.0f + (float) Minecraft.font.getStringWidth(doubleProperty.getDesc()) - 5.0f, y + 3.5f, 0.5F, new Color(30, 30, 30, 255).getRGB(), color, true);
                Minecraft.font.drawStringWithShadow(doubleProperty.getDesc(), x + 124.0f, y + height / 1.5f - 5.0f, -1);
            }
        } else {
            descTimer.reset();
        }
        super.drawComponent(scaledResolution, mouseX, mouseY);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (!sliding && button == 0 && isHovered(mouseX, mouseY)) {
            sliding = true;
            sliding2 = true;
        }
    }

    @Override
    public void onMouseRelease(int button) {
        sliding = false;
        sliding2 = false;
    }

    @Override
    public Setting getProperty() {
        return doubleProperty;
    }
}

