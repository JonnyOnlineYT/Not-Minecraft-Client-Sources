package ru.rockstar.client.ui.draggable;

import net.minecraft.client.gui.Gui;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

public class DraggableComponent {
	private final int width;
    private final int height;
    private int x;
    private int y;
    private int color;
    private int lastX;
    private int lastY;
    public static ArrayList<DraggableModule> draggableModules;
    private boolean dragging;
    private boolean canRender;
    
    public DraggableComponent(final int x, final int y, final int width, final int height, final int color) {
        this.canRender = true;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    public boolean isDragging() {
        return this.dragging;
    }
    
    public boolean isCanRender() {
        return this.canRender;
    }
    
    public void setCanRender(final boolean canRender) {
        this.canRender = canRender;
    }
    
    public int getXPosition() {
        return this.x;
    }
    
    public void setXPosition(final int x) {
        this.x = x;
    }
    
    public int getYPosition() {
        return this.y;
    }
    
    public void setYPosition(final int y) {
        this.y = y;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        return mouseX >= this.getXPosition() && mouseX <= this.getXPosition() + this.getWidth() && mouseY >= this.getYPosition() - this.getHeight() / 4 && mouseY <= this.getYPosition() - this.getHeight() / 4 + this.getHeight();
    }
    
    public void draw(final int mouseX, final int mouseY) {
        if (this.canRender) {
            this.draggingFix(mouseX, mouseY);
            Gui.drawRect(this.getXPosition(), this.getYPosition() - this.getHeight() / 4, this.getXPosition() + this.getWidth(), this.getYPosition() + this.getHeight(), this.getColor());
            final boolean mouseOverX = mouseX >= this.getXPosition() && mouseX <= this.getXPosition() + this.getWidth();
            final boolean mouseOverY = mouseY >= this.getYPosition() - this.getHeight() / 4 && mouseY <= this.getYPosition() - this.getHeight() / 4 + this.getHeight();
            if (mouseOverX && mouseOverY) {
                if (Mouse.isButtonDown(0)) {
                    if (!this.dragging && DraggableComponent.draggableModules.size() <= 1) {
                        this.lastX = this.x - mouseX;
                        this.lastY = this.y - mouseY;
                        this.dragging = true;
                    }
                }
                else {
                    DraggableComponent.draggableModules.clear();
                }
            }
        }
    }
    
    private void draggingFix(final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.x = mouseX + this.lastX;
            this.y = mouseY + this.lastY;
            if (!Mouse.isButtonDown(0)) {
                this.dragging = false;
            }
        }
    }
    
    static {
        DraggableComponent.draggableModules = new ArrayList<DraggableModule>();
    }
}