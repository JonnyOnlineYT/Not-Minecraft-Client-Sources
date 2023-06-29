//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.clickgui.component;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.gui.FontRenderer;
import java.util.Iterator;
import black.nigger.wildclient.clickgui.component.components.Button;
import black.nigger.wildclient.module.Module;
import black.nigger.wildclient.Wild;
import black.nigger.wildclient.module.Category;
import java.util.ArrayList;

public class Frame
{
    public ArrayList<Component> components;
    public Category category;
    private boolean open;
    private int width;
    private int y;
    private int x;
    private int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;
    
    public Frame(final Category cat) {
        this.components = new ArrayList<Component>();
        this.category = cat;
        this.width = 100;
        this.x = 5;
        this.y = 5;
        this.barHeight = 18;
        this.dragX = 0;
        this.open = false;
        this.isDragging = false;
        int tY = this.barHeight;
        for (final Module mod : Wild.instance.moduleManager.getModulesInCategory(this.category)) {
            final Button modButton = new Button(mod, this, tY);
            this.components.add(modButton);
            tY += 12;
        }
    }
    
    public ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public void setX(final int newX) {
        this.x = newX;
    }
    
    public void setY(final int newY) {
        this.y = newY;
    }
    
    public void setDrag(final boolean drag) {
        this.isDragging = drag;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public void renderFrame(final FontRenderer fontRenderer) {
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, new Color(0, 0, 0, 255).getRGB());
        GL11.glPushMatrix();
        fontRenderer.drawStringWithShadow(this.category.name(), (float)(this.x + 2 + 5), (float)(this.y + 5), 4251856);
        fontRenderer.drawStringWithShadow(this.open ? "V" : "<", (float)(this.x + this.width - 10 + 3), this.y + 2.5f + 2.0f, 4251856);
        GL11.glPopMatrix();
        if (this.open && !this.components.isEmpty()) {
            Gui.drawRect(this.x, this.y + this.barHeight, this.x + 1, this.y + this.barHeight + 12 * this.components.size(), new Color(255, 255, 255, 196).getRGB());
            Gui.drawRect(this.x, this.y + this.barHeight + 12 * this.components.size(), this.x + this.width, this.y + this.barHeight + 12 * this.components.size() + 1, new Color(0, 0, 0, 150).getRGB());
            Gui.drawRect(this.x + this.width, this.y + this.barHeight, this.x + this.width - 1, this.y + this.barHeight + 12 * this.components.size(), new Color(0, 0, 0, 150).getRGB());
            for (final Component component : this.components) {
                component.renderComponent();
            }
        }
    }
    
    public void refresh() {
        int off = this.barHeight;
        for (final Component comp : this.components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
    }
    
    public boolean isWithinHeader(final int x, final int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }
}
