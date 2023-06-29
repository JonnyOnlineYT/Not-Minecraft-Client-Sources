/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package ru.fluger.client.ui.csgo.element.imp.flow;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.Fluger;
import ru.fluger.client.TextureEngine;
import ru.fluger.client.UIRender;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.helpers.render.AnimationHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.ui.csgo.element.Element;
import ru.fluger.client.ui.csgo.element.imp.module.ElementBoolean;
import ru.fluger.client.ui.csgo.element.imp.module.ElementColorPicker;
import ru.fluger.client.ui.csgo.element.imp.module.ElementComboBox;
import ru.fluger.client.ui.csgo.element.imp.module.ElementSlider;

public class ElementSettings
extends Element {
    public Feature module;
    private TextureEngine texture = new TextureEngine("nightmare/clickgui/make.png", Fluger.scale, 32, 32);
    private float scroll;
    private float smooth;
    boolean can_scroll = false;
    public List<Element> settings = new ArrayList<Element>();

    public ElementSettings(Feature module, double width, double height) {
        this.module = module;
        this.width = width;
        this.height = height;
        this.setup();
    }

    public void setup() {
        this.module.getOptions().stream().filter(o -> o instanceof BooleanSetting).map(o -> (BooleanSetting)o).forEach(o -> this.settings.add(new ElementBoolean(this, (BooleanSetting)o)));
        this.module.getOptions().stream().filter(o -> o instanceof NumberSetting).map(o -> (NumberSetting)o).forEach(o -> this.settings.add(new ElementSlider(this, (NumberSetting)o)));
        this.module.getOptions().stream().filter(o -> o instanceof ColorSetting).map(o -> (ColorSetting)o).forEach(o -> this.settings.add(new ElementColorPicker(this, (ColorSetting)o)));
        this.module.getOptions().stream().filter(o -> o instanceof ListSetting).map(o -> (ListSetting)o).forEach(o -> this.settings.add(new ElementComboBox(this, (ListSetting)o)));
    }

    @Override
    public void render(int x, int y) {
        UIRender.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(657670).getRGB());
        this.texture.bind((int)this.x, (int)this.y + 10);
        this.mc.clickguismall.drawString(this.module.getLabel() + "", (int)this.x + this.texture.getWidth() - 5, (int)this.y + 12, -1);
        this.mc.smallfontRenderer.drawString("Totaly options: " + this.module.getOptions().size(), (int)this.x + this.texture.getWidth() - 5, (int)this.y + 21, new Color(40, 40, 40, 255).getRGB());
        GL11.glEnable((int)3089);
        RenderHelper.scissorRectScale((float)this.x, (float)this.y + 40.0f, (float)this.x + (float)this.width, this.y + this.height - 4.0);
        this.can_scroll = this.collided(x, y);
        if (this.smooth != this.scroll) {
            float speed = (float)(250.0 * Fluger.deltaTime());
            this.smooth = AnimationHelper.animation(this.smooth, this.scroll, speed);
        }
        int offset = 0;
        for (Element e : this.settings) {
            e.x = this.x + 5.0;
            e.y = this.y + (double)this.texture.getHeight() + 8.0 + (double)offset + (double)this.smooth;
            e.render(x, y);
            if (e instanceof ElementComboBox) {
                ElementComboBox box = (ElementComboBox)e;
                if (box.exteneded) {
                    offset = (int)((double)offset + ((double)box.getHeightWithExpand() - box.getHeight()));
                }
            }
            if (e instanceof ElementColorPicker) {
                ElementColorPicker picker = (ElementColorPicker)e;
                if (picker.extended) {
                    offset = (int)((double)offset + ((double)picker.getHeightWithExpand() - picker.getHeight()));
                }
            }
            offset = (int)((double)offset + (e.getHeight() + 1.0));
        }
        GL11.glDisable((int)3089);
        super.render(x, y);
    }

    @Override
    public void handleMouseInput() {
        if (!this.can_scroll) {
            return;
        }
        if (Mouse.hasWheel()) {
            int mouse = Mouse.getDWheel();
            if (mouse > 0) {
                this.scroll += 15.0f;
            } else if (mouse < 0) {
                this.scroll -= 15.0f;
            }
        }
        if (this.scroll >= 0.0f) {
            this.scroll = 0.0f;
        }
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        for (Element e : this.settings) {
            int a = (int)((double)((int)(this.y + (double)this.texture.getHeight() + 8.0)) - e.getHeight() / 2.0);
            if (!(e.y > (double)a)) continue;
            e.mouseClicked(x, y, button);
        }
        super.mouseClicked(x, y, button);
    }

    @Override
    public void mouseRealesed(int x, int y, int button) {
        for (Element e : this.settings) {
            e.mouseRealesed(x, y, button);
        }
        super.mouseRealesed(x, y, button);
    }
}

