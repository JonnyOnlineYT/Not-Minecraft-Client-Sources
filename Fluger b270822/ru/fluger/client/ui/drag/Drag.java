/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.drag;

import net.minecraft.client.Minecraft;
import ru.fluger.client.ui.csgo.IEventListener;

public class Drag
implements IEventListener {
    protected Minecraft mc = Minecraft.getMinecraft();
    public String name;
    public boolean dragging = false;
    public float x;
    public float y;
    public float width;
    public float height;
    public float x2;
    public float y2;

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void init() {
    }

    public boolean collided(int mouseX, int mouseY) {
        return (float)mouseX >= this.x && (float)mouseX <= this.x + this.width && (float)mouseY >= this.y && (float)mouseY <= this.y + this.height;
    }

    public boolean collided(int mouseX, int mouseY, double posX, double posY, float width, float height) {
        return (double)mouseX >= posX && (double)mouseX <= posX + (double)width && (double)mouseY >= posY && (double)mouseY <= posY + (double)height;
    }

    @Override
    public void handleMouseInput() {
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
    }

    @Override
    public void render(int x, int y) {
    }

    @Override
    public void mouseRealesed(int x, int y, int button) {
    }

    @Override
    public void keypressed(int i, int x, int y) {
    }
}

