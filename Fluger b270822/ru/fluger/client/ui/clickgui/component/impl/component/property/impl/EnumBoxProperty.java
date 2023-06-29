/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package ru.fluger.client.ui.clickgui.component.impl.component.property.impl;

import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.ui.clickgui.component.Component;
import ru.fluger.client.ui.clickgui.component.impl.ExpandableComponent;
import ru.fluger.client.ui.clickgui.component.impl.component.property.PropertyComponent;

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
        return this.property;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        float x = (float)this.getX();
        float y = (float)this.getY();
        float width = this.getWidth();
        float height = this.getHeight();
        String selectedText = this.property.getCurrentMode();
        Color onecolor = new Color(ClickGui.color.getColor());
        float dropDownBoxY = y + 10.0f;
        boolean needScissor = (float)EnumBoxProperty.mc.fontRenderer.getStringWidth(selectedText) > width - 4.0f;
        int textColor = 0xFFFFFF;
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 0).getRGB());
        RectHelper.drawGradientRect(x, y, x + width, y + height, RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker().darker().darker().darker(), 120).getRGB());
        EnumBoxProperty.mc.smallfontRenderer.drawCenteredString(this.getName(), x + this.getWidth() / 2.0f, y + 3.0f, textColor);
        RectHelper.drawRect((double)x + 2.5, (double)dropDownBoxY + 0.5, (double)(x + this.getWidth()) - 2.5, dropDownBoxY + 10.0f, new Color(0, 0, 0, 0).getRGB());
        RectHelper.drawGradientRect((double)x + 2.5, (double)dropDownBoxY + 0.5, (double)(x + this.getWidth()) - 2.5, dropDownBoxY + 10.0f, RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker().darker().darker().darker().darker().darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker(), 120).getRGB());
        if (needScissor) {
            RenderHelper.scissorRect(x + 2.0f, dropDownBoxY + 2.0f, width - 5.0f, 10.0);
        }
        if (ClickGui.glow.getCurrentValue()) {
            EnumBoxProperty.mc.fontRenderer.drawCenteredBlurredString(selectedText, x + this.getWidth() / 2.0f, dropDownBoxY + 2.0f, 15, RenderHelper.injectAlpha(new Color(onecolor.getRGB()), 200), new Color(onecolor.brighter().getRGB()).getRGB());
        } else {
            EnumBoxProperty.mc.fontRenderer.drawCenteredStringWithShadow(selectedText, x + this.getWidth() / 2.0f, dropDownBoxY + 2.0f, new Color(onecolor.brighter().getRGB()).getRGB());
        }
        RenderHelper.drawArrow(x + width - 14.0f, dropDownBoxY + 2.0f, 1.3f, 1.5f, this.isExpanded(), new Color(229, 229, 223, 255).getRGB());
        if (needScissor) {
            GL11.glDisable((int)2929);
        }
        if (this.isExpanded()) {
            RectHelper.drawRect(x + 1.0f, y + height, x + width - 1.0f, y + (float)this.getHeightWithExpand(), new Color(25, 25, 25).getRGB());
            RectHelper.drawGradientRect(x + 1.0f, y + height, x + width - 1.0f, y + (float)this.getHeightWithExpand(), RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker().darker().darker().darker().darker().darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker(), 120).getRGB());
            this.handleRender(x, y + this.getHeight() + 2.0f, width, textColor);
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (this.isExpanded()) {
            this.handleClick(mouseX, mouseY, (int)this.getX(), (int)this.getY() + (int)this.getHeight() + 2, (int)this.getWidth());
        }
    }

    private void handleRender(float x, float y, float width, float textColor) {
        ListSetting setting = this.property;
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        for (String e : setting.getModes()) {
            if (setting.currentMode.equals(e)) {
                RectHelper.drawColorRect(x + 1.0f, y - 2.0f, x + width - 1.0f, y + 15.0f - 5.0f, new Color(color), new Color(color).darker(), new Color(color).darker().darker(), new Color(color).darker().darker().darker());
            }
            EnumBoxProperty.mc.fontRenderer.drawCenteredStringWithShadow(e, x + this.getWidth() / 2.0f, y + 1.0f, (int)textColor);
            y += 12.0f;
        }
    }

    private void handleClick(int mouseX, int mouseY, int x, int y, int width) {
        for (String e : this.property.getModes()) {
            if (mouseX >= x && mouseY >= y && mouseX <= x + width && (float)mouseY <= (float)y + 15.0f - 3.0f) {
                this.property.setCurrentMode(e);
            }
            y = (int)((float)y + 12.0f);
        }
    }

    @Override
    public int getHeightWithExpand() {
        return (int)(this.getHeight() + (float)this.property.getModes().toArray().length * 12.0f);
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
    }

    @Override
    public boolean canExpand() {
        return this.property.getModes().toArray().length > 1;
    }
}

