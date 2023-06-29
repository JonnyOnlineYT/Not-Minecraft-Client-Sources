/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package ru.fluger.client.ui.csgo.element.imp.module;

import java.awt.Color;
import org.lwjgl.input.Keyboard;
import ru.fluger.client.Fluger;
import ru.fluger.client.TextureEngine;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.ui.csgo.element.Element;
import ru.fluger.client.ui.csgo.element.imp.ElementCategory;

public class ElementModule
extends Element {
    public Feature module;
    private boolean extended;
    private ElementCategory category;
    private boolean binding;
    TextureEngine texture = new TextureEngine("nightmare/clickgui/module.png", Fluger.scale, 24, 24);
    TextureEngine setting = new TextureEngine("nightmare/clickgui/setting.png", Fluger.scale, 16, 16);

    public ElementModule(ElementCategory category, Feature module, double width, double height) {
        this.module = module;
        this.category = category;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(int x, int y) {
        RectHelper.drawSmoothRect((float)this.x, (float)this.y, (double)((float)this.x) + this.width, (double)((float)this.y) + this.height, new Color(854792).getRGB());
        TextureEngine kek = new TextureEngine("nightmare/clickgui/kek.png", Fluger.scale, 24, 24);
        if (!this.binding) {
            this.texture.bind((int)this.x, (int)this.y + 5);
        } else {
            kek.bind((int)this.x, (int)this.y + 5);
        }
        if (!this.binding) {
            String bind = "(" + Keyboard.getKeyName((int)this.module.getBind()) + ")";
            if (Keyboard.getKeyName((int)this.module.getBind()).contains("NONE")) {
                bind = "";
            }
            if (this.module.state && ClickGui.glow.getCurrentValue()) {
                RenderHelper.drawBlurredShadow((float)this.x + 24.0f, (float)this.y + 10.0f, this.mc.smallfontRenderer.getStringWidth(this.module.getLabel()) + 1, this.mc.smallfontRenderer.getFontHeight() - 1, 10, new Color(750310));
            }
            this.mc.smallfontRenderer.drawString(this.module.getLabel(), (int)this.x + 25, (int)this.y + 10, this.module.state ? new Color(750310).getRGB() : -1);
            this.mc.smallfontRenderer.drawString(bind, (int)this.x + 25 + this.mc.smallfontRenderer.getStringWidth(this.module.getLabel()) + 2, (int)this.y + 10, new Color(40, 40, 40, 255).getRGB());
        } else {
            this.mc.smallfontRenderer.drawString("Press any key to bind", (int)this.x + 25, (int)this.y + 10, new Color(40, 40, 40, 255).getRGB());
        }
        if (this.module.getOptions().size() > 0) {
            this.setting.bind((int)this.x + (int)this.getWidth() - this.setting.getWidth() - 10, (int)this.y + 8);
        }
        super.render(x, y);
    }

    @Override
    public void keypressed(int i, int x, int y) {
        if (this.binding) {
            if (i == 211) {
                this.module.setBind(0);
                this.binding = false;
            } else {
                this.module.setBind(i);
                this.binding = false;
            }
        }
        super.keypressed(i, x, y);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        int a;
        if (button == 2 && this.collided(x, y)) {
            boolean bl = this.binding = !this.binding;
        }
        if (this.y < (double)(a = (int)((double)((int)(this.category.y + 40.0)) - this.getHeight() / 2.0)) || this.y > this.category.y + this.category.height - this.getHeight()) {
            return;
        }
        boolean colided_settings = this.collided(x, y, (int)this.x + (int)this.getWidth() - this.setting.getWidth() - 10, (int)this.y + 8, this.setting.getWidth(), this.setting.getHeight() / 2);
        if (this.collided(x, y) && button == 0 && !colided_settings) {
            this.module.toggle();
        }
        if (this != this.category.current && this.module.getOptions().size() > 0) {
            if (this.collided(x, y) && button == 0 && colided_settings) {
                this.category.panel.settings = null;
                this.category.current = this;
            } else if (this.collided(x, y) && button == 1) {
                this.category.panel.settings = null;
                this.category.current = this;
            }
            super.mouseClicked(x, y, button);
        }
    }
}

