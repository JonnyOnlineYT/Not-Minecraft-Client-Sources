/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.clickgui.component.impl.panel.impl;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import ru.fluger.client.Fluger;
import ru.fluger.client.TextureEngine;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.helpers.Helper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.ui.clickgui.ClickGuiScreen;
import ru.fluger.client.ui.clickgui.Palette;
import ru.fluger.client.ui.clickgui.component.Component;
import ru.fluger.client.ui.clickgui.component.impl.ExpandableComponent;
import ru.fluger.client.ui.clickgui.component.impl.component.module.ModuleComponent;
import ru.fluger.client.ui.clickgui.component.impl.panel.DraggablePanel;

public final class CategoryPanel
extends DraggablePanel
implements Helper {
    public static final float HEADER_WIDTH = 110.0f;
    public static final float X_ITEM_OFFSET = 1.0f;
    public static final float ITEM_HEIGHT = 15.0f;
    public static final float HEADER_HEIGHT = 20.0f;
    private final List<Feature> modules;
    public Type category;
    private TextureEngine texture = new TextureEngine("nightmare/clickgui/" + this.getName().toLowerCase() + ".png", Fluger.scale, 32, 32);

    public CategoryPanel(Type category, float x, float y) {
        super(null, category.name(), x, y, 110.0f, 20.0f);
        float moduleY = 20.0f;
        this.modules = Collections.unmodifiableList(Fluger.instance.featureManager.getFeaturesForCategory(category));
        for (Feature module : this.modules) {
            this.components.add(new ModuleComponent((Component)this, module, 1.0f, moduleY, 110.0f, 15.0f));
            moduleY += 15.0f;
        }
        this.category = category;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        float height;
        super.drawComponent(scaledResolution, mouseX, mouseY);
        float x = (float)this.getX();
        float y = (float)this.getY();
        float width = this.getWidth();
        float headerHeight = height = this.getHeight();
        int heightWithExpand = this.getHeightWithExpand();
        if (ClickGui.backGroundBlur.getCurrentValue() && CategoryPanel.mc.gameSettings.ofFastRender) {
            CategoryPanel.mc.gameSettings.ofFastRender = false;
        }
        if (ClickGuiScreen.getInstance().getPalette() == Palette.DEFAULT) {
            headerHeight = this.isExpanded() ? (float)heightWithExpand : height;
        }
        float startAlpha = 1.0f;
        int size = 8;
        float left = x;
        float right = x + 120.0f;
        float bottom = y + 11.0f;
        float startAlpha1 = 0.14f;
        int size1 = 25;
        float left1 = x + 1.0f;
        float right1 = x + width;
        float bottom1 = y + headerHeight - 2.0f;
        float top1 = y + headerHeight - 2.0f;
        float top2 = y + 13.0f;
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        if (this.isExpanded()) {
            // empty if block
        }
        RectHelper.drawRect(x, y, x + width, y + headerHeight - 2.0f, RenderHelper.injectAlpha(new Color(color).darker(), 0).getRGB());
        RectHelper.drawGradientRect(x, y, x + width, y + headerHeight - 2.0f, RenderHelper.injectAlpha(new Color(color).darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(color).darker().darker().darker(), 120).getRGB());
        if (this.isExpanded()) {
            float moduleY = height;
            for (Component child : this.components) {
                ExpandableComponent expandableComponent;
                child.setY(moduleY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                float cHeight = child.getHeight();
                if (child instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)child).isExpanded()) {
                    cHeight = expandableComponent.getHeightWithExpand() + 5;
                }
                moduleY += cHeight;
            }
        }
        this.texture.bind((int)x, (int)y + 1);
        if (this.isExpanded()) {
            RectHelper.drawColorRect((double)left1 - 1.0, top1, right1, bottom1 + 2.0f, new Color(color).brighter(), new Color(color), new Color(color).darker(), new Color(color).darker().darker());
            if (ClickGui.glow.getCurrentValue()) {
                RenderHelper.renderBlurredShadow(new Color(color), (double)((int)left1 - 2), (double)((int)bottom1), (double)this.getWidth(), 4.0, 15);
            }
        }
        CategoryPanel.mc.robotoRegularFontRender.drawCenteredBlurredString(this.getName(), x + this.getWidth() / 2.0f, y + this.getHeight() / 2.0f - 4.0f, 30, new Color(Integer.MIN_VALUE), -1);
    }

    @Override
    public boolean canExpand() {
        return !this.modules.isEmpty();
    }

    @Override
    public int getHeightWithExpand() {
        float height = this.getHeight();
        if (this.isExpanded()) {
            for (Component child : this.components) {
                ExpandableComponent expandableComponent;
                float cHeight = child.getHeight();
                if (child instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)child).isExpanded()) {
                    cHeight = expandableComponent.getHeightWithExpand() + 5;
                }
                height += cHeight;
            }
        }
        return (int)height;
    }
}

