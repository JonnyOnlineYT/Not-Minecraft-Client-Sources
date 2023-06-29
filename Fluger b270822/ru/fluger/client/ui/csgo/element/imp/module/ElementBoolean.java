/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.csgo.element.imp.module;

import java.awt.Color;
import ru.fluger.client.AnimationMath;
import ru.fluger.client.Fluger;
import ru.fluger.client.TextureEngine;
import ru.fluger.client.UIRender;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.ui.csgo.element.Element;
import ru.fluger.client.ui.csgo.element.imp.flow.ElementSettings;

public class ElementBoolean
extends Element {
    private ElementSettings parent;
    private BooleanSetting settings;
    public double animation;
    private TextureEngine texture = new TextureEngine("nightmare/clickgui/button40.png", Fluger.scale, 40, 40);

    public ElementBoolean(ElementSettings parent, BooleanSetting setting) {
        this.parent = parent;
        this.settings = setting;
        this.width = parent.getWidth() - 10.0;
        this.height = 15.0;
    }

    @Override
    public void render(int x, int y) {
        UIRender.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(854792).getRGB());
        this.mc.smallfontRenderer.drawString(this.settings.getName(), (int)this.x + 5, (int)this.y + 6, new Color(40, 40, 40, 255).getRGB());
        int checkbox_width = 13;
        int checkbox_height = 12;
        double max = this.settings.getCurrentValue() ? 5.0 : (double)checkbox_width;
        this.animation = AnimationMath.animate(max, this.animation, 0.04);
        this.texture.bind((int)this.x + (int)this.getWidth() - 30, (int)this.y - 2, 0.2f, 0.2f, 0.0f);
        UIRender.drawCircle((float)this.getX() + (float)this.getWidth() - (float)this.animation - 6.0f, (float)this.getY() + 8.0f, 0.0f, 360.0f, 2.5f, true, this.settings.getCurrentValue() ? new Color(750310) : new Color(-1));
        super.render(x, y);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (this.collided(x, y)) {
            this.settings.setValue(!this.settings.getCurrentValue());
        }
        super.mouseClicked(x, y, button);
    }
}

