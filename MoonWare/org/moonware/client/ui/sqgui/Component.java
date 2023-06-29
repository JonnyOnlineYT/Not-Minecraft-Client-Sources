package org.moonware.client.ui.sqgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.input.Mouse;
import org.moonware.client.feature.impl.Type;

public class Component extends Screen {
    public int x;
    public int y;
    public int y2;
    public int width;
    public int height;
    public Type type;
    public Component(int x , int y,int y2, int width,int height) {
        this.x = x;
        this.y = y;
        this.y2 = y2;
        this.width = width;
        this.height = height;
        this.type = null;
    }
    public Component(int x , int y, int y2, int width, int height, Type type) {
        this.x = x;
        this.y = y;
        this.y2 = y2;
        this.width = width;
        this.height = height;
        this.type = type;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getY2() {
        return y2;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setY2(int y2) {
        this.y2 = y2;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    @Override
    public void draw(int mouseX, int mouseY,float pa) {
        int x = Mouse.getEventX() * Minecraft.width / Minecraft.width;
        int y = Minecraft.height - Mouse.getEventY() * Minecraft.width / Minecraft.height - 1;
        int button = Mouse.getEventButton();
        if (Mouse.getEventButtonState()) {
            mousePressed(x, y, button);
        } else if (button != -1) {
            mouseReleased(x, y, button);
        }
    }
    public void mousePressed(int mouseX, int mouseY, int button) {
    }
    public void mouseReleased(int mouseX, int mouseY, int button) {
    }

}
