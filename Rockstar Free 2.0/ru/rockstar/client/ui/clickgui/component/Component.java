package ru.rockstar.client.ui.clickgui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.impl.display.ClickGUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class Component  {

    public final Component parent;
    protected final List<Component> components = new ArrayList<>();
    private final String name;
    private int x;
    private int y;
    protected int y2;
    private int width;
    private int height;

    public Component(Component parent, String name, int x, int y, int width, int height) {
        this.parent = parent;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public Component(Component parent, String name, int x, int y, int width, int height, int y2) {
        this.parent = parent;
        this.name = name;
        this.x = x;
        this.y = y;
        this.y2 = y2;
        this.width = width;
        this.height = height;
    }

    public Component getParent() {
        return parent;
    }

    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        String mode = ClickGUI.style.getOptions();
 
    	if (mode.equalsIgnoreCase("Rockstar New")) {
        	GlStateManager.pushMatrix();
        	GlStateManager.enable(GL11.GL_SCISSOR_TEST);
        	DrawHelper.drawNewRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
            DrawHelper.scissorRect(0, getY2() + 47, getX() + 98, getY2() + 253);
        }
        for (Component child : components) {
            child.drawComponent(scaledResolution, mouseX, mouseY);
        }
        if (mode.equalsIgnoreCase("Rockstar New")) {
      		
      		GlStateManager.disable(GL11.GL_SCISSOR_TEST);
      		GlStateManager.popMatrix();
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

    public int getX() {
        Component familyMember = parent;
        int familyTreeX = x;

        while (familyMember != null) {
            familyTreeX += familyMember.x;
            familyMember = familyMember.parent;
        }

        return familyTreeX;
    }

    public void setX(int x) {
        this.x = x;
    }

    protected boolean isHovered(int mouseX, int mouseY) {
        int x;
        int y;
        return mouseX >= (x = getX()) && mouseY >= (y = getY()) && mouseX < x + getWidth() && mouseY < y + getHeight();
    }

    public int getY() {
        Component familyMember = parent;
        int familyTreeY = y;

        while (familyMember != null) {
            familyTreeY += familyMember.y;
            familyMember = familyMember.parent;
        }

        return familyTreeY;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public int getY2() {
        Component familyMember = parent;
        int familyTreeY = y2;

        while (familyMember != null) {
            familyTreeY += familyMember.y2;
            familyMember = familyMember.parent;
        }

        return familyTreeY;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height + 4;
    }

    public void setHeight(int height) {
        this.height = height + 4;
    }

    public List<Component> getComponents() {
        return components;
    }
}
