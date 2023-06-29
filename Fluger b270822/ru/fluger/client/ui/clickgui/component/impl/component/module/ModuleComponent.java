/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.clickgui.component.impl.component.module;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.helpers.Helper;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.helpers.render.AnimationHelper;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.ui.clickgui.ClickGuiScreen;
import ru.fluger.client.ui.clickgui.component.Component;
import ru.fluger.client.ui.clickgui.component.impl.ExpandableComponent;
import ru.fluger.client.ui.clickgui.component.impl.component.property.PropertyComponent;
import ru.fluger.client.ui.clickgui.component.impl.component.property.impl.BooleanPropertyComponent;
import ru.fluger.client.ui.clickgui.component.impl.component.property.impl.ColorPropertyComponent;
import ru.fluger.client.ui.clickgui.component.impl.component.property.impl.EnumBoxProperty;
import ru.fluger.client.ui.clickgui.component.impl.component.property.impl.SliderPropertyComponent;
import ru.fluger.client.ui.clickgui.component.impl.component.property.impl.VisibleComponent;

public final class ModuleComponent
extends ExpandableComponent
implements Helper {
    private final Feature module;
    private boolean binding;
    private final TimerHelper descTimer = new TimerHelper();
    private float moduleHeightAnim;
    private boolean canRenderArrow;
    int alpha = 0;

    public ModuleComponent(Component parent, Feature module, float x, float y, float width, float height) {
        super(parent, module.getLabel(), x, y, width, height);
        this.module = module;
        float propertyX = 1.0f;
        for (Setting setting : module.getOptions()) {
            if (setting instanceof BooleanSetting) {
                this.components.add(new BooleanPropertyComponent((Component)this, (BooleanSetting)setting, propertyX, height, width - 2.0f, 17.0f));
                continue;
            }
            if (setting instanceof ColorSetting) {
                this.components.add(new ColorPropertyComponent((Component)this, (ColorSetting)setting, propertyX, height, width - 2.0f, 15.0f));
                continue;
            }
            if (setting instanceof NumberSetting) {
                this.components.add(new SliderPropertyComponent((Component)this, (NumberSetting)setting, propertyX, height, width - 2.0f, 15.0f));
                continue;
            }
            if (!(setting instanceof ListSetting)) continue;
            this.components.add(new EnumBoxProperty((Component)this, (ListSetting)setting, propertyX, height, width - 2.0f, 22.0f));
        }
        this.components.add(new VisibleComponent(module, this, propertyX, height, width - 2.0f, 15.0f));
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        String name;
        if (this.moduleHeightAnim < 4.0f) {
            this.moduleHeightAnim = 4.0f;
        }
        double x = this.getX();
        double y = this.getY() - 1.0;
        float width = this.getWidth();
        float height = this.getHeight();
        float childY = 15.0f;
        for (Component child : this.components) {
            ExpandableComponent expandableComponent;
            if (child == null) continue;
            float cHeight = child.getHeight();
            if (!(child instanceof VisibleComponent)) {
                PropertyComponent propertyComponent;
                this.canRenderArrow = true;
                if (child instanceof PropertyComponent && !(propertyComponent = (PropertyComponent)((Object)child)).getProperty().isVisible()) continue;
            }
            if (child instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)child).isExpanded()) {
                cHeight = expandableComponent.getHeightWithExpand();
            }
            if (!this.isExpanded()) continue;
            child.setY(childY);
            child.drawComponent(scaledResolution, mouseX, mouseY);
            childY += cHeight;
        }
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        ModuleComponent.mc.robotoRegularFontRender.drawString(this.isExpanded() ? "*" : "+", (float)x + width - 10.0f, (float)y + height / 2.0f - 1.0f - this.moduleHeightAnim, -1);
        this.moduleHeightAnim = this.isHovered(mouseX, mouseY) ? (float)((double)this.moduleHeightAnim + (double)0.2f * Minecraft.frameTime * (double)0.1f) : (float)((double)this.moduleHeightAnim - (double)0.2f * Minecraft.frameTime * (double)0.1f);
        this.moduleHeightAnim = MathHelper.clamp(this.moduleHeightAnim, 4.0f, 6.0f);
        String string = name = this.binding ? "Press a key..." : this.getName();
        if (!ClickGui.glow.getCurrentValue() || !this.module.getState()) {
            ModuleComponent.mc.rubik_18.drawCenteredString(name, (float)x + width - 60.0f, (float)y + height / 2.0f - this.moduleHeightAnim, this.module.getState() ? new Color(color).brighter().getRGB() : Color.GRAY.brighter().getRGB());
        }
        if (this.isHovered(mouseX, mouseY)) {
            this.alpha = (int)AnimationHelper.animation(this.alpha, 255.0f, 3.0f);
            this.alpha = MathHelper.clamp(this.alpha, 0, 255);
            if (this.descTimer.hasReached(150.0)) {
                // empty if block
            }
        } else {
            this.alpha = 0;
            this.descTimer.reset();
        }
    }

    @Override
    public boolean canExpand() {
        return !this.components.isEmpty();
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        switch (button) {
            case 0: {
                this.module.toggle();
                break;
            }
            case 2: {
                this.binding = !this.binding;
            }
        }
    }

    @Override
    public void onKeyPress(int keyCode) {
        if (this.binding) {
            ClickGuiScreen.escapeKeyInUse = true;
            this.module.setBind(keyCode == 1 ? 0 : keyCode);
            this.binding = false;
        }
    }

    @Override
    public int getHeightWithExpand() {
        float height = this.getHeight();
        if (this.isExpanded()) {
            for (Component child : this.components) {
                ExpandableComponent expandableComponent;
                PropertyComponent propertyComponent;
                float cHeight = child.getHeight();
                if (!(child instanceof VisibleComponent) && child instanceof PropertyComponent && !(propertyComponent = (PropertyComponent)((Object)child)).getProperty().isVisible()) continue;
                if (child instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)child).isExpanded()) {
                    cHeight = expandableComponent.getHeightWithExpand();
                }
                height += cHeight;
            }
        }
        return (int)height;
    }
}

