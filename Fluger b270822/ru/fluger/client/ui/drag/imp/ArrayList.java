/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.drag.imp;

import java.awt.Color;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import ru.fluger.client.Fluger;
import ru.fluger.client.UIRender;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.helpers.palette.PaletteHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.RoundedUtil;
import ru.fluger.client.ui.drag.Drag;

public class ArrayList
extends Drag {
    List<Feature> list = new java.util.ArrayList<Feature>();

    public ArrayList() {
        this.name = "ArrayList";
    }

    @Override
    public void init() {
        ScaledResolution rs = new ScaledResolution(this.mc);
        int width = Fluger.scale.calc(rs.getScaledWidth());
        this.x = (float)width - this.width;
        this.y = 0.0f;
    }

    @Override
    public void render(int mx, int my) {
        int stringWidth;
        ru.fluger.client.feature.impl.hud.ArrayList module = (ru.fluger.client.feature.impl.hud.ArrayList)Fluger.instance.featureManager.getFeatureByClass(ru.fluger.client.feature.impl.hud.ArrayList.class);
        if (!module.state) {
            return;
        }
        ScaledResolution rs = new ScaledResolution(this.mc);
        int width = Fluger.scale.calc(rs.getScaledWidth());
        int height = Fluger.scale.calc(rs.getScaledHeight());
        boolean reverse = this.x > (float)(width / 2);
        boolean reverseY = this.y > (float)(height / 2);
        int offset = 0;
        int yTotal = 0;
        for (int i = 0; i < this.list.size(); ++i) {
            yTotal += this.mc.mntsb.getFontHeight() + 3;
        }
        if (ru.fluger.client.feature.impl.hud.ArrayList.mode.index == 0) {
            for (Feature f : Fluger.instance.featureManager.features) {
                if (!f.isVisible() || !f.state) continue;
                if (!reverse) {
                    stringWidth = this.mc.mntsb.getStringWidth(f.getLabel()) + 3;
                    RoundedUtil.drawGradientRound(this.x, this.y + (float)offset, stringWidth, 8.0f, 0.0f, new Color(40, 50, 220), new Color(40, 40, 255), new Color(90, 0, 200), new Color(100, 0, 255));
                    RoundedUtil.drawRound(this.x - 3.5f, this.y + (float)offset, 2.0f, 8.0f, 0.0f, new Color(-1));
                    this.mc.mntsb.drawString(f.getLabel(), this.x + 1.5f, this.y + 2.0f + (float)offset, -1);
                }
                if (reverse) {
                    stringWidth = this.mc.mntsb.getStringWidth(f.getLabel()) + 3;
                    RoundedUtil.drawGradientRound(this.x - (float)stringWidth + this.width, this.y + (float)offset, stringWidth, 8.0f, 0.0f, new Color(40, 50, 220), new Color(40, 40, 255), new Color(90, 0, 200), new Color(100, 0, 255));
                    RoundedUtil.drawRound(this.x + 1.5f + this.width, this.y + (float)offset, 2.0f, 8.0f, 0.0f, new Color(-1));
                    this.mc.mntsb.drawString(f.getLabel(), this.x + this.width + 1.5f - (float)stringWidth, this.y + 2.0f + (float)offset, -1);
                }
                offset += 8;
            }
        }
        if (ru.fluger.client.feature.impl.hud.ArrayList.mode.index == 1) {
            for (Feature f : Fluger.instance.featureManager.features) {
                if (!f.isVisible() || !f.state) continue;
                if (!reverse) {
                    stringWidth = this.mc.mntsb.getStringWidth(f.getLabel()) + 3;
                    if (ru.fluger.client.feature.impl.hud.ArrayList.glowing.getCurrentValue()) {
                        RenderHelper.drawBlurredShadow(this.x - 3.0f, this.y + (float)offset, stringWidth + 4, 10.0f, module.glow_strength.getCurrentValueInt(), module.colorMode.index == 0 ? PaletteHelper.astolfo(offset, yTotal, module.saturation.getCurrentValue(), module.rainbowSpeed.getCurrentValue()) : new Color(module.firstColor.color).darker());
                    }
                    UIRender.drawRect(this.x, this.y + (float)offset, this.x + (float)stringWidth + 1.0f, this.y + (float)offset + 8.0f, module.colorMode.index == 0 ? PaletteHelper.astolfo(offset, yTotal, module.saturation.getCurrentValue(), module.rainbowSpeed.getCurrentValue()).getRGB() : new Color(module.firstColor.color).darker().getRGB());
                    UIRender.drawRect(this.x - 2.0f, this.y + (float)offset, this.x + 1.0f, this.y + (float)offset + 8.0f, -1);
                    this.mc.mntsb.drawString(f.getLabel(), this.x + 3.0f, this.y + 2.0f + (float)offset, -1);
                }
                if (reverse) {
                    stringWidth = this.mc.mntsb.getStringWidth(f.getLabel()) + 3;
                    if (ru.fluger.client.feature.impl.hud.ArrayList.glowing.getCurrentValue()) {
                        RenderHelper.drawBlurredShadow(this.x + this.width - (float)stringWidth, this.y + (float)offset, stringWidth + 5, 10.0f, module.glow_strength.getCurrentValueInt(), module.colorMode.index == 0 ? PaletteHelper.astolfo(offset, yTotal, module.saturation.getCurrentValue(), module.rainbowSpeed.getCurrentValue()) : new Color(module.firstColor.color).darker());
                    }
                    UIRender.drawRect(this.x - (float)stringWidth + this.width, this.y + (float)offset, this.x + this.width + 1.0f, this.y + (float)offset + 8.0f, module.colorMode.index == 0 ? PaletteHelper.astolfo(offset, yTotal, module.saturation.getCurrentValue(), module.rainbowSpeed.getCurrentValue()).darker().getRGB() : new Color(module.firstColor.color).darker().getRGB());
                    UIRender.drawRect(this.x + 1.0f + this.width, this.y + (float)offset, this.x + this.width + 4.0f, this.y + (float)offset + 8.0f, -1);
                    this.mc.mntsb.drawString(f.getLabel(), this.x + this.width + 2.0f - (float)stringWidth, this.y + 2.0f + (float)offset, -1);
                }
                offset += 8;
            }
        }
        this.setHeight(offset);
        this.setWidth(this.getElementWidth());
    }

    public int getElementWidth() {
        this.list.clear();
        for (Feature f : Fluger.instance.featureManager.features) {
            if (!f.state || !f.visible) continue;
            this.list.add(f);
        }
        int width = this.mc.mntsb.getStringWidth(this.list.get(0).getLabel());
        return width;
    }
}

