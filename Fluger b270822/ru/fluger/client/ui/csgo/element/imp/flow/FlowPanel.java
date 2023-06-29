/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.csgo.element.imp.flow;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import ru.fluger.client.Fluger;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.ui.csgo.element.Element;
import ru.fluger.client.ui.csgo.element.imp.ElementCategory;
import ru.fluger.client.ui.csgo.element.imp.ElementHeader;
import ru.fluger.client.ui.csgo.element.imp.flow.ElementSettings;

public class FlowPanel {
    protected Minecraft mc = Minecraft.getMinecraft();
    public ElementHeader header = new ElementHeader(this);
    private List<Element> elements = new ArrayList<Element>();
    public ElementSettings settings;
    public double x;
    public double y;
    public double x2;
    public double y2;
    public double width;
    public double height;
    public boolean dragging;
    private float scroll;
    public boolean extended;
    public ElementCategory current = null;
    public double oldWidth;
    public double oldHeight;
    boolean collided = false;

    public FlowPanel(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.oldWidth = this.getWidth();
        this.oldHeight = this.getHeight();
        this.setup();
    }

    private void setup() {
        this.header.setWidth(this.getWidth());
        this.header.setHeight(25.0);
        for (Type type : Type.values()) {
            ElementCategory category = new ElementCategory(this, type, this.getWidth() - 20.0, 35.0);
            if (this.elements.contains(category)) continue;
            this.elements.add(category);
        }
    }

    public void render(int x, int y) {
        RectHelper.drawSmoothRect((float)this.x, (float)this.y, this.x + this.getWidth(), this.y + this.getHeight(), new Color(854792).getRGB());
        this.header.render(x, y);
        boolean height_button = false;
        for (Element e : this.elements) {
            e.render(x, y);
        }
        if (this.dragging) {
            this.x = (double)x + this.x2;
            this.y = (double)y + this.y2;
        }
        if (this.x <= 1.0) {
            this.x = 1.0;
        }
        if (this.y <= 1.0) {
            this.y = 1.0;
        }
        ScaledResolution rs = new ScaledResolution(this.mc);
        int maxX = (int)((double)Fluger.scale.calc(rs.getScaledWidth()) - this.getWidth()) - 1;
        int maxY = (int)((double)Fluger.scale.calc(rs.getScaledHeight()) - this.getHeight()) - 1;
        if (this.x >= (double)maxX) {
            this.x = maxX;
        }
        if (this.y >= (double)maxY) {
            this.y = maxY;
        }
        this.collided = false;
        if (this.settings != null && this.settings.collided(x, y)) {
            this.collided = true;
        }
        for (Element e : this.elements) {
            if (!(e instanceof ElementCategory)) continue;
            ElementCategory cat = (ElementCategory)e;
            if (cat.collided(x, y)) {
                this.collided = true;
            }
            for (Element element : cat.elements) {
                if (!element.collided(x, y)) continue;
                this.collided = true;
            }
            if (cat.current == null || !cat.current.collided(x, y)) continue;
            this.collided = true;
        }
        if (this.header.collided(x, y)) {
            this.collided = false;
        }
        this.positions();
        if (this.settings != null && this.current != null && this.current.current != null) {
            this.settings.render(x, y);
        }
    }

    private void positions() {
        this.header.setX(this.getX());
        this.header.setY(this.getY());
        if (this.extended && this.current.current == null) {
            this.setWidth(this.oldWidth * 2.0 - 15.0);
            this.header.setWidth(this.oldWidth * 2.0 - 15.0);
        }
        if (this.current != null & this.settings != null) {
            this.settings.x = this.current.x + this.current.getWidth() + 4.0;
            this.settings.y = this.current.y;
            this.settings.setWidth(this.current.getWidth() / 2.0 + 42.0);
        }
        if (this.settings == null && this.current != null && this.current.current != null && this.current.current.module.getOptions().size() > 0) {
            this.settings = new ElementSettings(this.current.current.module, this.current.getWidth() / 2.0 + 42.0, this.current.getHeight());
            this.settings.x = this.current.x + this.current.getWidth() + 4.0;
            this.settings.y = this.current.y;
            this.settings.setWidth(this.current.getWidth() / 2.0 + 42.0);
        }
        int offset = 0;
        for (Element e : this.elements) {
            ElementCategory category;
            if (!(e instanceof ElementCategory) || (category = (ElementCategory)e) == this.current) continue;
            category.extended = false;
            category.x = this.x + 10.0;
            category.y = this.y + this.header.getHeight() + 10.0 + (double)offset;
            offset = (int)((double)offset + (category.getHeight() + 2.0));
        }
        if (this.extended && this.current != null) {
            int x = (int)(this.x + this.current.getWidth() + 15.0);
            int y = (int)(this.y + this.header.getHeight() + 10.0);
            this.current.x = x;
            this.current.y = y;
        }
    }

    public void mouse(int x, int y, int state) {
        if (this.collided(x, y) && state == 0 && !this.collided) {
            this.x2 = this.x - (double)x;
            this.y2 = this.y - (double)y;
            this.dragging = true;
        }
        for (Element e : this.elements) {
            ElementCategory cat;
            if (e == this.current || !e.collided(x, y)) continue;
            this.current = cat = (ElementCategory)e;
            cat.current = null;
            this.extended = true;
            this.current.extended = true;
        }
        if (this.current != null) {
            this.current.mouseClicked(x, y, state);
        }
        this.header.mouseClicked(x, y, state);
        if (this.settings != null && this.current != null && this.current.current != null) {
            this.settings.mouseClicked(x, y, state);
        }
    }

    public void handleMouse() {
        for (Element e : this.elements) {
            e.handleMouseInput();
        }
        if (this.settings != null && this.current != null && this.current.current != null) {
            this.settings.handleMouseInput();
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        for (Element e : this.elements) {
            e.keypressed(keyCode, typedChar, keyCode);
        }
    }

    public void realesed(int x, int y, int button) {
        this.dragging = false;
        if (this.settings != null && this.current != null && this.current.current != null) {
            this.settings.mouseRealesed(x, y, button);
        }
    }

    public boolean collided(int mouseX, int mouseY) {
        return (double)mouseX >= this.x && (double)mouseX <= this.x + this.width && (double)mouseY >= this.y && (double)mouseY <= this.y + this.height;
    }

    public boolean collided(int mouseX, int mouseY, double posX, double posY, float width, float height) {
        return (double)mouseX >= posX && (double)mouseX <= posX + (double)width && (double)mouseY >= posY && (double)mouseY <= posY + (double)height;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }
}

