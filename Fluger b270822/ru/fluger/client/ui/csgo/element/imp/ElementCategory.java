/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package ru.fluger.client.ui.csgo.element.imp;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.Fluger;
import ru.fluger.client.TextureEngine;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.helpers.render.AnimationHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.ui.csgo.element.Element;
import ru.fluger.client.ui.csgo.element.imp.flow.FlowPanel;
import ru.fluger.client.ui.csgo.element.imp.module.ElementModule;

public class ElementCategory
extends Element {
    private Type type;
    public boolean extended;
    private float scroll;
    public float smooth;
    boolean can_scroll = false;
    public List<ElementModule> elements = new ArrayList<ElementModule>();
    public FlowPanel panel;
    public ElementModule current;

    public ElementCategory(FlowPanel panel, Type type, double width, double height) {
        this.type = type;
        this.width = width;
        this.height = height;
        this.panel = panel;
        for (Feature f : Fluger.instance.featureManager.features) {
            if (f.getType() != type) continue;
            ElementModule module = new ElementModule(this, f, this.getWidth() - 20.0, 25.0);
            this.elements.add(module);
        }
    }

    @Override
    public void render(int x, int y) {
        TextureEngine texture = new TextureEngine("nightmare/clickgui/" + this.type.name.toLowerCase() + ".png", Fluger.scale, 32, 32);
        RectHelper.drawSmoothRect((float)this.x, (float)this.y, (double)((float)this.x) + this.width, (double)((float)this.y) + this.height, new Color(657670).getRGB());
        texture.bind((int)this.x, (int)this.y + 10);
        this.mc.clickguismall.drawString(this.type.name, (int)this.x + 30, (int)this.y + 12, -1);
        this.mc.smallfontRenderer.drawString(this.type.discription, (int)this.x + 30, (int)this.y + 22, new Color(40, 40, 40, 255).getRGB());
        this.can_scroll = this.collided(x, y);
        if (this.extended && this == this.panel.current) {
            if (this.current != null) {
                double width = this.panel.oldWidth * 3.0 - 100.0;
                this.panel.setWidth(width);
                this.panel.header.setWidth(width);
            }
            GL11.glEnable((int)3089);
            int offset = 0;
            for (ElementModule module : this.elements) {
                module.x = this.panel.x + 20.0 + this.width;
                module.y = this.panel.y + this.panel.header.getHeight() + 10.0 + (double)this.smooth + 40.0 + (double)offset;
                offset = (int)((double)offset + (module.getHeight() + 2.0));
            }
            RenderHelper.scissorRectScale((float)this.panel.x + 20.0f, (float)this.panel.y + (float)this.panel.header.getHeight() + 10.0f + 40.0f, (float)this.x + (float)this.width, this.y + this.panel.height - 40.0 - 4.0);
            for (ElementModule module : this.elements) {
                module.render(x, y);
            }
            GL11.glDisable((int)3089);
            if (this.smooth != this.scroll) {
                float speed = (float)(250.0 * Fluger.deltaTime());
                this.smooth = AnimationHelper.animation(this.smooth, this.scroll, speed);
            }
            int max = offset + 45;
            if (offset <= 260) {
                this.setHeight(max);
            } else {
                this.setHeight(260.0);
            }
        } else {
            this.setHeight(35.0);
        }
    }

    @Override
    public void handleMouseInput() {
        if (!this.can_scroll) {
            return;
        }
        if (Mouse.hasWheel()) {
            int mouse = Mouse.getDWheel();
            if (mouse > 0) {
                this.scroll += 20.0f;
            } else if (mouse < 0) {
                this.scroll -= 20.0f;
            }
        }
        if (this.scroll >= 0.0f) {
            this.scroll = 0.0f;
        }
        for (Element element : this.elements) {
            element.handleMouseInput();
        }
        super.handleMouseInput();
    }

    @Override
    public void keypressed(int i, int x, int y) {
        for (ElementModule e : this.elements) {
            e.keypressed(i, x, y);
        }
        super.keypressed(i, x, y);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (this.extended) {
            for (ElementModule e : this.elements) {
                if (!e.collided(x, y)) continue;
                e.mouseClicked(x, y, button);
            }
        }
        super.mouseClicked(x, y, button);
    }
}

