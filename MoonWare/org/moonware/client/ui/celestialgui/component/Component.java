/*
 * Decompiled with CFR 0.150.
 */
package org.moonware.client.ui.celestialgui.component;

import net.minecraft.client.gui.ScaledResolution;
import org.moonware.client.helpers.Helper;

import java.util.ArrayList;
import java.util.List;

public class Component
        implements Helper {
    public final Component parent;
    protected final List<Component> components = new ArrayList<Component>();
    private final String name;
    private double x;
    private double y;
    private float width;
    private float height;

    public Component(Component parent, String name, float x, float y, float width, float height) {
        this.parent = parent;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Component getParent() {
        return parent;
    }

    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        for (Component child : components) {
            child.drawComponent(scaledResolution, mouseX, mouseY);
        }
    }

    public void onMouseClick(int mouseX, int mouseY, int button) {
        for (Component child : components) {
            child.onMouseClick(mouseX, mouseY, button);
        }
    }

    public void onMouseRelease(int button) {
        for (Component child : components) {
            child.onMouseRelease(button);
        }
    }

    public void onKeyPress(int keyCode) {
        for (Component child : components) {
            child.onKeyPress(keyCode);
        }
    }

    public String getName() {
        return name;
    }

    public double getX() {
        Component familyMember = parent;
        double familyTreeX = x;
        while (familyMember != null) {
            familyTreeX += familyMember.x;
            familyMember = familyMember.parent;
        }
        return familyTreeX;
    }

    public void setX(float x) {
        this.x = x;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected boolean isHovered(int mouseX, int mouseY) {
        int x;
        int y;
        return mouseX >= (x = (int) getX()) && mouseY >= (y = (int) getY()) && mouseX < x + getWidth() && mouseY < y + getHeight();
    }

    public double getY() {
        Component familyMember = parent;
        double familyTreeY = y;
        while (familyMember != null) {
            familyTreeY += familyMember.y + 0.01;
            familyMember = familyMember.parent;
        }
        return familyTreeY;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Component> getComponents() {
        return components;
    }
}

