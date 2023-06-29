package de.strafe.gui.dropdown;

import java.awt.*;


public enum Palette
{
    DEFAULT(new Color(-13619152), new Color(-13619152).darker(), new Color(-13619152), new Color(-14474461).darker(), new Color(-14079703), new Color(Color.RED.getRGB()), new Color(Color.RED.getRGB()), new Color(16777215));
    
    private final Color panelBackgroundColor;
    private final Color hoveredBackgroundColor;
    private final Color secondaryBackgroundColor;
    private final Color hoveredSecondaryBackgroundColor;
    private final Color panelHeaderColor;
    private final Color enabledModuleColor;
    private final Color disabledModuleColor;
    private final Color panelHeaderTextColor;
    
    private Palette(final Color panelBackgroundColor, final Color hoveredBackgroundColor, final Color secondaryBackgroundColor, final Color hoveredSecondaryBackgroundColor, final Color panelHeaderColor, final Color enabledModuleColor, final Color disabledModuleColor, final Color panelHeaderTextColor) {
        this.panelBackgroundColor = panelBackgroundColor;
        this.hoveredBackgroundColor = hoveredBackgroundColor;
        this.secondaryBackgroundColor = secondaryBackgroundColor;
        this.hoveredSecondaryBackgroundColor = hoveredSecondaryBackgroundColor;
        this.panelHeaderColor = panelHeaderColor;
        this.enabledModuleColor = enabledModuleColor;
        this.disabledModuleColor = disabledModuleColor;
        this.panelHeaderTextColor = panelHeaderTextColor;
    }
    
    public Color getHoveredSecondaryBackgroundColor() {
        return this.hoveredSecondaryBackgroundColor;
    }
    
    public Color getHoveredBackgroundColor() {
        return this.hoveredBackgroundColor;
    }
    
    public Color getSecondaryBackgroundColor() {
        return this.secondaryBackgroundColor;
    }
    
    public Color getPanelHeaderTextColor() {
        return this.panelHeaderTextColor;
    }
    
    public Color getPanelBackgroundColor() {
        return this.panelBackgroundColor;
    }
    
    public Color getPanelHeaderColor() {
        return this.panelHeaderColor;
    }
    
    public Color getEnabledModuleColor() {
        return this.enabledModuleColor;
    }
    
    public Color getDisabledModuleColor() {
        return this.disabledModuleColor;
    }
}
