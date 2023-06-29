package org.moonware.client.ui.celestialgui.component.impl.panel.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.moonware.client.utils.FontStorage;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.celestialgui.ClickGuiScreen;
import org.moonware.client.ui.celestialgui.Palette;
import org.moonware.client.ui.celestialgui.SorterHelper;
import org.moonware.client.ui.celestialgui.component.Component;
import org.moonware.client.ui.celestialgui.component.impl.ExpandableComponent;
import org.moonware.client.ui.celestialgui.component.impl.component.module.ModuleComponent;
import org.moonware.client.ui.celestialgui.component.impl.panel.DraggablePanel;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public final class CategoryPanel
        extends DraggablePanel
        implements Helper {
    public static final float HEADER_WIDTH = 120.0f;
    public static final float X_ITEM_OFFSET = 1.0f;
    public static final float ITEM_HEIGHT = 15.0f;
    public static final float HEADER_HEIGHT = 17.0f;
    private final List<Feature> modules;
    public Type category;

    public CategoryPanel(Type category, float x, float y) {
        super(null, category.name(), x, y, 120.0f, 17.0f);
        float moduleY = 17.0f;
        modules = Collections.unmodifiableList(MoonWare.featureManager.getFeaturesForCategory(category));
        for (Feature module : modules) {
            components.add(new ModuleComponent(this, module, 1.0f, moduleY, 118.0f, 15.0f));
            moduleY += 15.0f;
        }
        this.category = category;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        float height;
        super.drawComponent(scaledResolution, mouseX, mouseY);
        components.sort(new SorterHelper());
        float x = (float) getX();
        float y = (float) getY();
        float width = getWidth();
        float headerHeight = height = getHeight();
        int heightWithExpand = getHeightWithExpand();
        if ((ClickGui.backGroundBlur.getCurrentValue() || ClickGui.panelMode.currentMode.equals("Blur")) && Minecraft.gameSettings.ofFastRender) {
            Minecraft.gameSettings.ofFastRender = false;
        }
        if (ClickGuiScreen.getInstance().getPalette() == Palette.DEFAULT) {
            headerHeight = isExpanded() ? (float)heightWithExpand : height;
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
        if (isExpanded()) {
            RectHelper.renderShadowVertical(Color.BLACK, 3.0f, startAlpha1, size1, right1 - 1.0f, top2, bottom1 + 3.0f, true, false);
            RectHelper.renderShadowVertical(Color.BLACK, 3.0f, startAlpha1, size1, (double)left1 - 0.3, top2, bottom1 + 3.0f, false, false);
            RectHelper.renderShadowHorizontal(Color.BLACK, 3.0f, (double)startAlpha - 0.15, size, bottom, left, right, false, false);
        }
        if (ClickGui.panelMode.currentMode.equals("Blur") && ClickGui.scale.getCurrentValue() == 1.0f) {
            RenderHelper2.renderBlur((int)x, (int)y, (int)width, (int)headerHeight - 2, 15);
        } else if (ClickGui.panelMode.currentMode.equals("Rect")) {
            RectHelper.drawRect(x, y, x + width, y + headerHeight - 2.0f, ClickGui.rectColor.getColorValue());
        }
        if (isExpanded()) {
            float moduleY = height;
            for (Component child : components) {
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
        RectHelper.drawColorRect(x - 3.0f, y, x + 123.0f, y + 15.0f, new Color(15, 15, 15), new Color(15, 15, 15).brighter(), new Color(15, 15, 15).brighter(), new Color(15, 15, 15).brighter().brighter());
        RectHelper.drawRectBetter(x + 1.0f, y - -3.0f, 15.0, 1.0, Color.WHITE.getRGB());
        RectHelper.drawRectBetter(x + 1.0f, y - -7.0f, 10.0, 1.0, Color.WHITE.getRGB());
        RectHelper.drawRectBetter(x + 1.0f, y - -11.0f, 15.0, 1.0, Color.WHITE.getRGB());
        if (isExpanded()) {
            RectHelper.drawColorRect((double)left1 - 1.5, top1, right1 + 1.0f, bottom1 + 3.0f, new Color(color).brighter(), new Color(color), new Color(color).darker(), new Color(color).darker().darker());
            if (ClickGui.glow.getCurrentValue()) {
                RenderHelper2.renderBlurredShadow(new Color(color), (int)left1 - 2, (int)bottom1, 125.0, 4.0, 15);
            }
        }
        FontStorage.robotoRegularFontRender.drawShadow(getName(), x + 22.0f, y + 8.5f - 4.0f, -1);
    }

    @Override
    public boolean canExpand() {
        return !modules.isEmpty();
    }

    @Override
    public int getHeightWithExpand() {
        float height = getHeight();
        if (isExpanded()) {
            for (Component child : components) {
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

