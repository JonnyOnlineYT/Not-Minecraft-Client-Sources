/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.csgo.element.imp;

import java.awt.Color;
import ru.fluger.client.Fluger;
import ru.fluger.client.TextureEngine;
import ru.fluger.client.UIRender;
import ru.fluger.client.ui.csgo.element.Element;
import ru.fluger.client.ui.csgo.element.imp.flow.FlowPanel;

public class ElementHeader
extends Element {
    TextureEngine back = new TextureEngine("nightmare/clickgui/return.png", Fluger.scale, 32, 32);
    private FlowPanel panel;

    public ElementHeader(FlowPanel panel) {
        this.panel = panel;
    }

    @Override
    public void render(int x, int y) {
        UIRender.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(657670).getRGB());
        if (this.panel.extended) {
            this.back.bind((int)this.x + 5, (int)this.y + 4);
        }
        this.mc.ubuntuFontRender18.drawCenteredString("Fluger", (int)this.x + (int)this.getWidth() / 2 - 14, (int)this.y + (int)this.getHeight() / 2 - 3, -1);
        this.mc.ubuntuFontRender18.drawCenteredString("Client", (int)this.x + (int)this.getWidth() / 2 + 14, (int)this.y + (int)this.getHeight() / 2 - 3, new Color(750310).getRGB());
        super.render(x, y);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (this.collided(x, y, this.back.getX(), this.back.getY(), this.back.getWidth(), this.back.getHeight()) && this.panel.extended && button == 0) {
            if (this.panel.current.current != null) {
                this.panel.settings = null;
                this.panel.current.current = null;
                this.panel.setWidth(this.panel.oldWidth * 2.0);
                this.panel.header.setWidth(this.panel.oldWidth * 2.0);
            } else {
                this.panel.current.extended = false;
                this.panel.current = null;
                this.panel.extended = false;
                this.panel.setWidth(this.panel.oldWidth);
                this.setWidth(this.panel.oldWidth);
            }
        }
        super.mouseClicked(x, y, button);
    }
}

