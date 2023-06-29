/*
 * Decompiled with CFR 0.150.
 */
package org.moonware.client.ui.celestialgui.component.impl.component.property.impl;

import net.minecraft.client.gui.ScaledResolution;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.ui.celestialgui.component.Component;
import org.moonware.client.ui.celestialgui.component.impl.ExpandableComponent;
import org.moonware.client.ui.celestialgui.component.impl.component.property.PropertyComponent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class EnumBoxProperty
extends ExpandableComponent
implements PropertyComponent {
    private final ListSetting property;

    public EnumBoxProperty(Component parent, ListSetting property, float x, float y, float width, float height) {
        super(parent, property.getName(), x, y, width, height);
        this.property = property;
    }

    @Override
    public Setting getProperty() {
        return property;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        float x = (float) getX();
        float y = (float) getY();
        float width = getWidth();
        float height = getHeight();
        String selectedText = property.getCurrentMode();
        float dropDownBoxY = y + 10.0f;
        boolean needScissor = (float) MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(selectedText) > width - 4.0f;
        int textColor = 0xFFFFFF;
        RectHelper.drawRect(x, y, x + width, y + height, new Color(15, 15, 15).getRGB());
        MWFont.SF_UI_DISPLAY_REGULAR.get(14).drawShadow(getName(), x + 2.0f, y + 3.0f, textColor);
        RectHelper.drawRect(x + 2.0f, dropDownBoxY, x + getWidth() - 2.0f, dropDownBoxY + 10.0f, new Color(30, 30, 30).getRGB());
        RectHelper.drawRect((double)x + 2.5, (double)dropDownBoxY + 0.5, (double)(x + getWidth()) - 2.5, dropDownBoxY + 10.0f, -12828863);
        if (needScissor) {
            RenderHelper2.scissorRect(x + 2.0f, dropDownBoxY + 2.0f, width - 5.0f, 10.0);
        }
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper2.renderBlurredShadow(new Color(229, 229, 223, 255).brighter(), x + 3.5f, dropDownBoxY + 2.0f, MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(selectedText) + 2, 5.0, 25);
        }
        MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawShadow(selectedText, x + 3.5f, dropDownBoxY + 2.0f, -1);
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper2.renderBlurredShadow(new Color(229, 229, 223, 255).brighter(), x + 105.0f, dropDownBoxY + 2.0f, 5.0, 5.0, 10);
        }
        RenderHelper2.drawArrow(x + 103.0f, dropDownBoxY + 2.0f, 1.3f, 1.5f, isExpanded(), new Color(229, 229, 223, 255).getRGB());
        if (needScissor) {
            GL11.glDisable(2929);
        }
        if (isExpanded()) {
            RectHelper.drawRect(x + 1.0f, y + height, x + width - 1.0f, y + (float) getHeightWithExpand(), new Color(30, 30, 30).getRGB());
            handleRender(x, y + getHeight() + 2.0f, width, textColor);
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (isExpanded()) {
            handleClick(mouseX, mouseY, (int) getX(), (int) getY() + (int) getHeight() + 2, (int) getWidth());
        }
    }

    private void handleRender(float x, float y, float width, float textColor) {
        ListSetting setting = property;
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        for (String e : setting.getModes()) {
            if (setting.currentMode.equals(e)) {
                if (ClickGui.glow.getCurrentValue()) {
                    RenderHelper2.renderBlurredShadow(Color.WHITE, (int)x + 1, (int)y + 2, 110.0, 7.0, 25);
                }
                RectHelper.drawColorRect(x + 1.0f, y - 2.0f, x + width - 1.0f, y + 15.0f - 5.0f, new Color(color), new Color(color).brighter(), new Color(color).brighter().brighter(), new Color(color).brighter().brighter().brighter());
            }
            MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawShadow(e, x + 1.0f + 2.0f, y + 1.0f, (int)textColor);
            y += 12.0f;
        }
    }

    private void handleClick(int mouseX, int mouseY, int x, int y, int width) {
        for (String e : property.getModes()) {
            if (mouseX >= x && mouseY >= y && mouseX <= x + width && (float)mouseY <= (float)y + 15.0f - 3.0f) {
                //this.property.setCurrentMode(e);
                property.setListMode(e);
            }
            y = (int)((float)y + 12.0f);
        }
    }

    @Override
    public int getHeightWithExpand() {
        return (int)(getHeight() + (float) property.getModes().toArray().length * 12.0f);
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
    }

    @Override
    public boolean canExpand() {
        return property.getModes().toArray().length > 1;
    }
}

