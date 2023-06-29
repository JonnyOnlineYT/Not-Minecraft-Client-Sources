/*
 * Decompiled with CFR 0.150.
 */
package org.moonware.client.ui.celestialgui.component.impl.component.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.celestialgui.ClickGuiScreen;
import org.moonware.client.ui.celestialgui.component.Component;
import org.moonware.client.ui.celestialgui.component.impl.ExpandableComponent;
import org.moonware.client.ui.celestialgui.component.impl.component.property.PropertyComponent;
import org.moonware.client.ui.celestialgui.component.impl.component.property.impl.*;

import java.awt.*;

public final class ModuleComponent
        extends ExpandableComponent
        implements Helper {
    private final Feature module;
    private boolean binding;
    private final TimerHelper descTimer = new TimerHelper();
    private float moduleHeightAnim;
    private boolean canRenderArrow;
    int alpha;

    public ModuleComponent(Component parent, Feature module, float x, float y, float width, float height) {
        super(parent, module.getLabel(), x, y, width, height);
        this.module = module;
        float propertyX = 1.0f;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new BooleanPropertyComponent(this, (BooleanSetting)setting, propertyX, height, width - 2.0f, 17.0f));
                continue;
            }
            if (setting instanceof ColorSetting) {
                components.add(new ColorPropertyComponent(this, (ColorSetting)setting, propertyX, height, width - 2.0f, 15.0f));
                continue;
            }
            if (setting instanceof NumberSetting) {
                components.add(new SliderPropertyComponent(this, (NumberSetting)setting, propertyX, height, width - 2.0f, 15.0f));
                continue;
            }
            if (!(setting instanceof ListSetting)) continue;
            components.add(new EnumBoxProperty(this, (ListSetting)setting, propertyX, height, width - 2.0f, 22.0f));
        }
        components.add(new VisibleComponent(module, this, propertyX, height, width - 2.0f, 15.0f));
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (moduleHeightAnim < 4.0f) {
            moduleHeightAnim = 4.0f;
        }
        double x = getX();
        double y = getY() - 1.0;
        float width = getWidth();
        float height = getHeight();
        float childY = 15.0f;
        for (Component child : components) {
            ExpandableComponent expandableComponent;
            if (child == null) continue;
            float cHeight = child.getHeight();
            if (!(child instanceof VisibleComponent)) {
                PropertyComponent propertyComponent;
                canRenderArrow = true;
                if (child instanceof PropertyComponent && !(propertyComponent = (PropertyComponent) child).getProperty().isVisible()) continue;
            }
            if (child instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)child).isExpanded()) {
                cHeight = expandableComponent.getHeightWithExpand();
            }
            if (!isExpanded()) continue;
            child.setY(childY);
            child.drawComponent(scaledResolution, mouseX, mouseY);
            childY += cHeight;
        }
        if (!ClickGuiScreen.search.getText().isEmpty() && !module.getLabel().toLowerCase().contains(ClickGuiScreen.search.getText().toLowerCase())) {
            return;
        }
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        if (canRenderArrow && ClickGui.arrows.getCurrentValue()) {
            RenderHelper2.drawArrow((float)x + width - 10.0f, (float)y + height / 2.0f + 1.0f - moduleHeightAnim, 1.0f, 1.5f, isExpanded(), -1);
        }
        if (isHovered(mouseX, mouseY)) {
            alpha = (int)AnimationHelper.animation(alpha, 255.0f, 3.0f);
            alpha = MathHelper.clamp(alpha, 0, 255);
            double a = x;
            double b = y;
            if (descTimer.hasReached(150.0)) {
                RenderHelper2.renderBlurredShadow(Color.BLACK, (int)x + 104, (int)y + (int)height / 2 + 4, 18 + Minecraft.font.getStringWidth(module.getDesc()), 6.0, 20);
                RectHelper.drawSmoothRect((float)x + 106.0f, (float)y + height / 1.5f + 4.5f, x + 119.0 + (double) Minecraft.font.getStringWidth(module.getDesc()), y + 4.0, new Color(30, 30, 30, alpha).getRGB());
                Minecraft.font.drawStringWithShadow(module.getDesc(), (float)x + 110.0f, (float)y + height / 1.5f - 5.0f, new Color(255, 255, 255, alpha).getRGB());
            }
            x = a;
            y = b;
        } else {
            alpha = 0;
            descTimer.reset();
        }
        if (module.getState() && ClickGui.glow.getCurrentValue()) {
            RenderHelper2.renderBlurredShadow(new Color(color), (float)((int)x + (int)width - 62) - (float) MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(getName()) / 2.0f + 1.0f, (float)((int)y) + (float)((int)height) / 2.0f - 2.0f - moduleHeightAnim, MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(getName()) + 3, MWFont.SF_UI_DISPLAY_REGULAR.get(18).getHeight() + 3, (int)ClickGui.glowRadius.getCurrentValue());
        }
        moduleHeightAnim = isHovered(mouseX, mouseY) ? (float)((double) moduleHeightAnim + (double)0.2f * Minecraft.frameTime * (double)0.1f) : (float)((double) moduleHeightAnim - (double)0.2f * Minecraft.frameTime * (double)0.1f);
        moduleHeightAnim = MathHelper.clamp(moduleHeightAnim, 4.0f, 6.0f);
        String name = binding ? "Press a key..." : getName();
        MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawCenter(name, (float)x + width - 60.0f, (float)y + height / 2.0f - moduleHeightAnim, module.getState() ? new Color(color).brighter().getRGB() : -1);
    }

    @Override
    public boolean canExpand() {
        return !components.isEmpty();
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        switch (button) {
            case 0: {
                module.toggle();
                break;
            }
            case 2: {
                binding = !binding;
            }
        }
    }

    @Override
    public void onKeyPress(int keyCode) {
        if (binding) {
            ClickGuiScreen.escapeKeyInUse = true;
            module.setBind(keyCode == 1 ? 0 : keyCode);
            binding = false;
        }
    }

    @Override
    public int getHeightWithExpand() {
        float height = getHeight();
        if (isExpanded()) {
            for (Component child : components) {
                ExpandableComponent expandableComponent;
                PropertyComponent propertyComponent;
                float cHeight = child.getHeight();
                if (!(child instanceof VisibleComponent) && child instanceof PropertyComponent && !(propertyComponent = (PropertyComponent) child).getProperty().isVisible()) continue;
                if (child instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)child).isExpanded()) {
                    cHeight = expandableComponent.getHeightWithExpand();
                }
                height += cHeight;
            }
        }
        return (int)height;
    }
}

