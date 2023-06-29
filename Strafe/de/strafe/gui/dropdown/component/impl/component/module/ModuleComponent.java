package de.strafe.gui.dropdown.component.impl.component.module;

import java.awt.*;
import java.util.List;

import de.strafe.font.Fonts;
import de.strafe.font.MCFontRenderer;
import de.strafe.gui.dropdown.component.impl.ExpandableComponent;
import de.strafe.gui.dropdown.component.impl.component.property.PropertyComponent;
import de.strafe.gui.dropdown.component.impl.component.property.impl.BooleanPropertyComponent;
import de.strafe.gui.dropdown.component.impl.component.property.impl.EnumBoxProperty;
import de.strafe.gui.dropdown.component.impl.component.property.impl.SliderPropertyComponent;
import de.strafe.modules.Module;
import de.strafe.settings.Setting;
import de.strafe.gui.dropdown.component.Component;
import de.strafe.settings.impl.BooleanSetting;
import de.strafe.settings.impl.ModeSetting;
import de.strafe.settings.impl.NumberSetting;
import de.strafe.utils.RenderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;


public final class ModuleComponent extends ExpandableComponent
{
    private final Module module;
    MCFontRenderer font;
    private boolean binding;
    
    public ModuleComponent(final Component parent, final Module module, final int x, final int y, final int width, final int height) {
        super(parent, module.getName(), x, y, width, height);
        this.font = new MCFontRenderer(Fonts.fontFromTTF(new ResourceLocation("Strafe/fonts/normal.ttf"), 18.0F, 0), true, true);
        this.module = module;
        final List<Setting> properties = module.getSettings();
        final int propertyX = 1;
        int propertyY = height;
        for (final Setting property : properties) {
            Component component = null;
            if (property instanceof BooleanSetting) {
                component = new BooleanPropertyComponent(this, (BooleanSetting)property, propertyX, propertyY, width - 2, 15);
            }
            else if (property instanceof NumberSetting) {
                component = new SliderPropertyComponent(this, (NumberSetting)property, propertyX, propertyY, width - 2, 15);
            }
            else if (property instanceof ModeSetting) {
                component = new EnumBoxProperty(this, (ModeSetting)property, propertyX, propertyY, width - 2, 22);
            }
            if (component != null) {
                this.children.add(component);
                propertyY += component.getHeight();
            }
        }
    }
    
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        GlStateManager.resetColor();
        final int x = this.getX();
        final int y = this.getY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        if (this.isExpanded()) {
            int childY = 15;
            for (final Component child : this.children) {

                int cHeight = child.getHeight();
                if (child instanceof PropertyComponent) {
                    final PropertyComponent propertyComponent = (PropertyComponent)child;
                    if (!propertyComponent.getProperty().isAvailable()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    final ExpandableComponent expandableComponent = (ExpandableComponent)child;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand();
                    }
                }
                child.setY(childY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                childY += cHeight;
            }
        }
        int moduleColor = 0;
        moduleColor = (this.module.isToggled() ?  new Color(255, 0, 0).getRGB() : -1);
        Gui.drawRect((float)x, (float)y, (float)(x + width), (float)(y + height), this.getBackgroundColor(this.isHovered(mouseX, mouseY)));
        Gui.drawRect((float)x, (float)y, (float)(x + width), (float)(y + height), this.getBackgroundEnabledColor(this.module.isToggled()));
        font.drawStringWithShadow(this.binding ? "Press a key to Bind" : this.getName(), (float)(x + 2), y + height / 2.0f - 4.0f, moduleColor);
        if (this.canExpand()) {
         /*RenderUtil.drawRect((float)(x + width - 10), y + height / 2.0f - 2.0f, 6.0f, this.isExpanded());*/
        }
    }
    
    @Override
    public boolean canExpand() {
        return !this.children.isEmpty();
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
        switch (button) {
            case 0: {
                this.module.toggle();
                break;
            }
            case 2: {
                this.binding = !this.binding;
                break;
            }
        }
    }
    
    public void onKeyPress(final int keyCode) {
        if (this.binding) {
            this.module.setKey((keyCode == 1) ? 0 : keyCode);
            this.binding = false;
        }
    }
    
    @Override
    public int getHeightWithExpand() {
        int height = this.getHeight();
        if (this.isExpanded()) {
            for (final Component child : this.children) {
                int cHeight = child.getHeight();
                if (child instanceof PropertyComponent) {
                    final PropertyComponent propertyComponent = (PropertyComponent)child;
                    if (!propertyComponent.getProperty().isAvailable()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    final ExpandableComponent expandableComponent = (ExpandableComponent)child;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand();
                    }
                }
                height += cHeight;
            }
        }
        return height;
    }



}
