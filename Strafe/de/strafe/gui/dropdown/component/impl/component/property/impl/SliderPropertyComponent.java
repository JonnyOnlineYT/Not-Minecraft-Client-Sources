package de.strafe.gui.dropdown.component.impl.component.property.impl;


import de.strafe.font.Fonts;
import de.strafe.font.MCFontRenderer;
import de.strafe.gui.dropdown.component.impl.component.property.PropertyComponent;
import de.strafe.settings.impl.NumberSetting;
import de.strafe.utils.OGLUtils;
import net.minecraft.util.*;
import net.minecraft.client.gui.*;
import de.strafe.gui.dropdown.component.Component;
import java.awt.*;
import java.math.BigDecimal;


public final class SliderPropertyComponent extends Component implements PropertyComponent
{
    private static final int SLIDER_COLOR;
    private final NumberSetting doubleProperty;
    private boolean sliding;
    MCFontRenderer font;
    public SliderPropertyComponent(final Component parent, final NumberSetting property, final int x, final int y, final int width, final int height) {
        super(parent, property.name, x, y, width, height);
        this.doubleProperty = property;
        this.font = new MCFontRenderer(Fonts.fontFromTTF(new ResourceLocation("Strafe/fonts/normal.ttf"), 18.0F, 0), true, true);
    }
    
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        final int x = this.getX();
        final int y = this.getY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        final double min = this.doubleProperty.getMinimum();
        final double max = this.doubleProperty.getMaximum();
        final Double dValue = this.doubleProperty.getValue();
        double value;
        value = dValue;

        final double sliderBackground = (max - min) / (this.doubleProperty.getMaximum() - min);
        final double sliderPercentage = (value - min) / (this.doubleProperty.getMaximum() - min);
        final boolean hovered = this.isHovered(mouseX, mouseY);
        if (this.sliding) {
            if (hovered) {
                this.doubleProperty.setValue(MathHelper.clamp_double(this.roundToFirstDecimalPlace((mouseX - x) * (max - min) / width + min), min, max));
            }
            else {
                this.sliding = false;
            }
        }
        final String name = this.getName();
        final int middleHeight = this.getHeight() / 2;
        String valueString;
        valueString = Double.toString(value);
        final float valueWidth = font.getStringWidth(valueString) + 2.0f;
        final float overflowWidth = font.getStringWidth(name) + 3.0f - (width - valueWidth);
        final boolean needOverflowBox = overflowWidth > 0.0f;
        final boolean showOverflowBox = hovered && needOverflowBox;
        final boolean needScissorBox = needOverflowBox && !hovered;
        Gui.drawRect(x - (showOverflowBox ? overflowWidth : 0.0f), (float)y, (float)(x + width), (float)(y + height), this.getSecondaryBackgroundColor(hovered));
        Gui.drawRect((double)x, (double)y + 14, x + width * sliderBackground, (double)(y + height) - 4, new Color(29, 29, 29).getRGB());
        Gui.drawRect((double)x, (double)y + 14, x + width * sliderPercentage, (double)(y + height) - 4, SliderPropertyComponent.SLIDER_COLOR);

        if (needScissorBox) {
            OGLUtils.startScissorBox(scaledResolution, x, y, (int)(width - valueWidth - 4.0f), height);
        }
        font.drawStringWithShadow(name, x + 2 - (showOverflowBox ? overflowWidth : 0.0f), (float)(y + middleHeight - 6), -1);
        if (needScissorBox) {
            OGLUtils.endScissorBox();
        }
       font.drawStringWithShadow(valueString, x + width - valueWidth, (float)(y + middleHeight - 6), -1);
    }
    
    private double roundToFirstDecimalPlace(final double value) {
        final double inc = this.doubleProperty.getIncrement();
        final double halfOfInc = inc / 2.0;
        final double floored = Math.floor(value / inc) * inc;
        if (value >= floored + halfOfInc) {
            return new BigDecimal(Math.ceil(value / inc) * inc).setScale(2, 4).doubleValue();
        }
        return new BigDecimal(floored).setScale(2, 4).doubleValue();
    }
    
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        if (!this.sliding && button == 0 && this.isHovered(mouseX, mouseY)) {
            this.sliding = true;
        }
    }
    
    public void onMouseRelease(final int button) {
        this.sliding = false;
    }
    
    public NumberSetting getProperty() {
        return this.doubleProperty;
    }
    
    static {
        SLIDER_COLOR =  new Color(106,90,205).getRGB();
    }
}
