package de.strafe.gui.dropdown;

import de.strafe.gui.dropdown.component.impl.ExpandableComponent;
import de.strafe.gui.dropdown.component.impl.panel.impl.CategoryPanel;
import de.strafe.modules.Category;
import de.strafe.gui.dropdown.component.Component;
import de.strafe.utils.RenderUtil;
import net.minecraft.client.gui.*;



import java.awt.*;
import java.util.*;
import java.io.*;
import java.util.List;



public final class ClickGui extends GuiScreen
{
    public static boolean escapeKeyInUse;
    private static ClickGui instance;
    private final List<Component> components;
    private final Palette palette;
    private Component selectedPanel;
    private ScaledResolution scaledResolution;

    public ClickGui() {
        this.components = new ArrayList<Component>();

        ClickGui.instance = this;
        this.palette = Palette.DEFAULT;
        int panelX = 2;
        for (final Category category : Category.values()) {
            final CategoryPanel panel = new CategoryPanel(category, panelX, 4);
            this.components.add(panel);
            panelX += panel.getWidth() + 7;
            this.selectedPanel = panel;
        }
    }
    
    public static ClickGui getInstance() {
        return ClickGui.instance;
    }
    
    public Palette getPalette() {
        return this.palette;
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        for (final Component component : this.components) {

            component.drawComponent(RenderUtil.getScaledResolution(), mouseX, mouseY);
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.selectedPanel.onKeyPress(keyCode);
        if (!ClickGui.escapeKeyInUse) {
            super.keyTyped(typedChar, keyCode);
        }
        ClickGui.escapeKeyInUse = false;
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        for (int i = this.components.size() - 1; i >= 0; --i) {
            final Component component = this.components.get(i);
            final int x = component.getX();
            final int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandableComponent) {
                final ExpandableComponent expandableComponent = (ExpandableComponent)component;
                if (expandableComponent.isExpanded()) {
                    cHeight = expandableComponent.getHeightWithExpand();
                }
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight) {
                (this.selectedPanel = component).onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.selectedPanel.onMouseRelease(state);
    }

    

}
