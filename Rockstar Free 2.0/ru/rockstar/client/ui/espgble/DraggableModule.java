package ru.rockstar.client.ui.espgble;


import java.awt.*;

import net.minecraft.client.Minecraft;
import ru.rockstar.api.utils.Helper;

public class DraggableModule implements Helper {
	public String name;
    public int x;
    public int y;
    public DraggableComponent drag;
    protected Minecraft mc;
    
    public DraggableModule(final String name, final int x, final int y) {
        this.mc = Minecraft.getMinecraft();
        this.name = name;
        this.x = x;
        this.y = y;
        this.drag = new DraggableComponent(this.x, this.y, this.getWidth(), this.getHeight(), new Color(255, 255, 255, 0).getRGB());
    }
    
    public void draw() {
    }
    
    public void render(final int mouseX, final int mouseY) {
        this.drag.draw(mouseX, mouseY);
    }
    
    public int getX() {
        return this.drag.getXPosition();
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getY() {
        return this.drag.getYPosition();
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getX2() {
        return this.x;
    }
    
    public void setX2(final int x) {
        this.drag.setXPosition(x);
    }
    
    public int getY2() {
        return this.y;
    }
    
    public void setY2(final int y) {
        this.drag.setYPosition(y);
    }
    
    public int getWidth() {
        return 50;
    }
    
    public int getHeight() {
        return 50;
    }
}
