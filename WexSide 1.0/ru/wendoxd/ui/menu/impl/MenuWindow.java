package ru.wendoxd.ui.menu.impl;

import ru.wendoxd.ui.menu.utils.Bound2D;

public abstract class MenuWindow {

    private int x, y, dragX, dragY;
    private boolean dragging;

    public abstract void draw();

    public abstract boolean click(int x, int y, int mb);

    public abstract boolean isActive();

    public abstract void onAnimation();

    public abstract Bound2D getBound();

    public abstract int width();

    public abstract int height();

    public abstract void update(boolean open);

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getDragX() {
        return this.dragX;
    }

    public int getDragY() {
        return this.dragY;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
        if (getBound().interp()) {
            getBound().offset(x, y);
        }
    }

    public void setDragXY(int dragX, int dragY) {
        this.dragX = dragX;
        this.dragY = dragY;
    }
}
