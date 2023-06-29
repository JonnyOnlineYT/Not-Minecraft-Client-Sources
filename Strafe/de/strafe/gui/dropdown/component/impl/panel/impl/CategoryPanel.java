package de.strafe.gui.dropdown.component.impl.panel.impl;


import java.awt.*;
import java.util.*;
import java.util.List;

import de.strafe.Strafe;
import de.strafe.font.Fonts;
import de.strafe.font.MCFontRenderer;
import de.strafe.gui.dropdown.component.impl.ExpandableComponent;
import de.strafe.gui.dropdown.component.impl.component.module.ModuleComponent;
import de.strafe.gui.dropdown.component.impl.panel.DraggablePanel;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.utils.StringUtils;
import de.strafe.gui.dropdown.component.Component;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;


public final class CategoryPanel extends DraggablePanel
{
    public static final int HEADER_WIDTH = 120;
    public static final int X_ITEM_OFFSET = 1;
    public static final int ITEM_HEIGHT = 15;
    public static final int HEADER_HEIGHT = 17;
    private final List<Module> modules;
    MCFontRenderer font;

    public CategoryPanel(final Category category, final int x, final int y) {
        super((Component)null, StringUtils.upperSnakeCaseToPascal(category.name()), x, y, 120, 17);
        int moduleY = 17;
        this.font = new MCFontRenderer(Fonts.fontFromTTF(new ResourceLocation("Strafe/fonts/normal.ttf"), 18.0F, 0), true, true);
        this.modules = Collections.unmodifiableList((List<? extends Module>) Strafe.INSTANCE.moduleManager.getModules(category));
        for (final Module module : this.modules) {
            this.children.add(new ModuleComponent((Component)this, module, 1, moduleY, 118, 15));
            moduleY += 15;
            this.getChildren().sort(Comparator.comparingDouble(Component::getX));
        }
    }
    
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        final int x = this.getX();
        final int y = this.getY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int panelHeaderCol = new Color(7, 7, 7).getRGB();
        int headerHeight = height;
        final int heightWithExpand = this.getHeightWithExpand();
        headerHeight = (this.isExpanded() ? (heightWithExpand + 1) : height);


        Gui.drawRect((float)x, (float)y, (float)(x + width), (float)(y + headerHeight), panelHeaderCol);
        font.drawStringWithShadow(this.getName(), x + width / 2.0f - font.getStringWidth(this.getName()) / 2.0f - 1.0f, y + 8.5f - 4.0f, -1);
        if (this.isExpanded()) {
            int moduleY = height;
            for (final Component child : this.children) {
                child.setY(moduleY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                int cHeight = child.getHeight();
                if (child instanceof ExpandableComponent) {
                    final ExpandableComponent expandableComponent = (ExpandableComponent)child;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand();
                    }
                }
                moduleY += cHeight;
            }
        }
    }


    public boolean canExpand() {
        return !this.modules.isEmpty();
    }
    
    public int getHeightWithExpand() {
        int height = this.getHeight();
        if (this.isExpanded()) {
            for (final Component child : this.children) {
                int cHeight = child.getHeight();
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
