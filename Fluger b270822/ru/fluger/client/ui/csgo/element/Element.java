/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.csgo.element;

import net.minecraft.client.Minecraft;
import ru.fluger.client.ui.csgo.IEventListener;

public class Element
implements IEventListener {
    protected Minecraft mc = Minecraft.getMinecraft();
    public double x;
    public double y;
    public double width;
    public double height;

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

    public boolean collided(int mouseX, int mouseY) {
        return (double)mouseX >= this.x && (double)mouseX <= this.x + this.width && (double)mouseY >= this.y && (double)mouseY <= this.y + this.height;
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

