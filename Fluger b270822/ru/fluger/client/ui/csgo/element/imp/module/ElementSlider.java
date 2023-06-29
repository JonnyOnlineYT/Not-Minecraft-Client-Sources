/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.csgo.element.imp.module;

import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import ru.fluger.client.UIRender;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.ui.csgo.element.Element;
import ru.fluger.client.ui.csgo.element.imp.flow.ElementSettings;

public class ElementSlider
extends Element {
    private ElementSettings parent;
    private NumberSetting setting;
    private boolean dragging = false;
    private double selected;
    private float sliderHeight;

    public ElementSlider(ElementSettings parent, NumberSetting setting) {
        this.parent = parent;
        this.setting = setting;
        this.width = parent.getWidth() - 10.0;
        this.selected = MathHelper.clamp((double)(this.setting.getCurrentValue() / this.setting.getMaximum()), 0.0, 1.0);
        this.height = 25.0;
    }

    @Override
    public void render(int x, int y) {
        this.sliderHeight = 4.0f;
        x = (int)((double)x - this.getX());
        double heightText = 15.0;
        double slider_width = this.getWidth() - 50.0;
        UIRender.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(854792).getRGB());
        UIRender.drawRect((float)this.x + 5.0f, (float)this.y + (float)heightText, (double)((float)this.x + 5.0f + (float)slider_width * (float)this.selected) - 0.15 - 2.0, (float)this.y + (float)heightText + this.sliderHeight, new Color(0x141414).getRGB());
        UIRender.roundedBorder((float)this.x + 5.0f, (float)this.y + (float)heightText, (float)this.x + (float)slider_width + 2.0f, (float)this.y + (float)heightText + this.sliderHeight, 1.3f, new Color(0x141414).getRGB());
        UIRender.drawCircle((float)((double)((float)this.x + (float)slider_width * (float)this.selected) - 0.15), (float)((double)((float)this.getY()) + heightText) + 2.0f, 0.0f, 360.0f, 3.0f, true, new Color(750310));
        double clamp = MathHelper.clamp((double)x / slider_width, 0.0, 1.0);
        this.mc.smallfontRenderer.drawString(this.setting.getName(), (int)this.x + 5, (int)this.y + 5, new Color(40, 40, 40, 255).getRGB());
        String display_max = "max " + this.setting.getMaximum();
        this.mc.smallfontRenderer.drawString(display_max, (int)this.x + (int)slider_width - this.mc.clickguismall.getStringWidth(display_max) + 8, (int)this.y + 8, new Color(40, 40, 40, 255).getRGB());
        if (this.dragging) {
            String current = this.setting.getCurrentValue() + "";
            this.mc.smallfontRenderer.drawString(current, (float)((double)((float)this.x + 2.5f + (float)slider_width * (float)this.selected) - 0.15) - (float)(this.mc.smallfontRenderer.getStringWidth(current) / 2) - 5.0f, (float)((double)((float)this.getY()) + heightText - 10.0) + 2.0f, -1);
        }
        if (this.dragging) {
            this.selected = clamp;
            double current = (double)this.setting.getMinimum() + clamp * (double)(this.setting.getMaximum() - this.setting.getMinimum());
            float round = 10.0f;
            this.setting.setCurrentValue((float)((double)Math.round(current * (double)round) / (double)round));
        }
        super.render(x, y);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (this.collided(x, y) && button == 0) {
            this.dragging = true;
        }
        super.mouseClicked(x, y, button);
    }

    @Override
    public void mouseRealesed(int x, int y, int button) {
        this.dragging = false;
        super.mouseRealesed(x, y, button);
    }
}

