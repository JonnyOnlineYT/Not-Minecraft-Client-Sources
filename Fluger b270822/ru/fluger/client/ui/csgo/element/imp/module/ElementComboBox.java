/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.csgo.element.imp.module;

import java.awt.Color;
import ru.fluger.client.UIRender;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.ui.csgo.element.Element;
import ru.fluger.client.ui.csgo.element.imp.flow.ElementSettings;

public class ElementComboBox
extends Element {
    private ElementSettings parent;
    private ListSetting property;
    public boolean exteneded;

    public ElementComboBox(ElementSettings parent, ListSetting property) {
        this.parent = parent;
        this.property = property;
        this.width = parent.getWidth() - 10.0;
        this.height = 20.0;
    }

    @Override
    public void render(int mx, int my) {
        float x = (float)this.getX();
        float y = (float)this.getY();
        float width = (float)this.getWidth();
        float height = (float)this.getHeight();
        String selectedText = this.property.getCurrentMode();
        Color onecolor = new Color(750310);
        float dropDownBoxY = y + 10.0f;
        boolean needScissor = (float)this.mc.fontRenderer.getStringWidth(selectedText) > width - 4.0f;
        int textColor = 0xFFFFFF;
        UIRender.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(854792).getRGB());
        this.mc.smallfontRenderer.drawCenteredString(this.property.getName(), (float)((double)x + this.getWidth() / 2.0), y + 3.0f, textColor);
        this.mc.fontRenderer.drawCenteredStringWithShadow(selectedText, (float)((double)x + this.getWidth() / 2.0), dropDownBoxY + 1.0f, new Color(750310).getRGB());
        RenderHelper.drawArrow(x + width - 14.0f, dropDownBoxY - 3.0f, 1.3f, 1.5f, this.exteneded, new Color(229, 229, 223, 255).getRGB());
        if (this.exteneded) {
            RectHelper.drawRect(x + 1.0f, y + height, x + width - 1.0f, y + (float)this.getHeightWithExpand(), new Color(25, 25, 25).getRGB());
            this.handleRender(x, (float)((double)y + this.getHeight() + 2.0), width, textColor);
        }
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (this.collided(x, y) && button == 1) {
            boolean bl = this.exteneded = !this.exteneded;
        }
        if (this.exteneded) {
            this.handleClick(x, y, (int)this.getX(), (int)this.getY() + (int)this.getHeight() + 2, (int)this.getWidth());
        }
    }

    private void handleRender(float x, float y, float width, float textColor) {
        ListSetting setting = this.property;
        Color onecolor = new Color(-1);
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        for (String e : setting.getModes()) {
            if (setting.currentMode.equals(e)) {
                RectHelper.drawRect(x + 1.0f, y - 2.0f, x + width - 1.0f, y + 15.0f - 5.0f, new Color(750310).getRGB());
            }
            this.mc.fontRenderer.drawCenteredStringWithShadow(e, (float)((double)x + this.getWidth() / 2.0), y + 1.0f, (int)textColor);
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

    public int getHeightWithExpand() {
        return (int)(this.getHeight() + (double)((float)this.property.getModes().toArray().length * 12.0f));
    }

    public boolean canExpand() {
        return this.property.getModes().toArray().length > 1;
    }
}

