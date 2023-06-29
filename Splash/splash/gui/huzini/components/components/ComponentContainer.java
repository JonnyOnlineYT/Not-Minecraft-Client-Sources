package splash.gui.huzini.components.components;

import net.minecraft.client.gui.Gui;
import splash.Splash;
import splash.gui.huzini.Frame;
import splash.gui.huzini.components.Component;
import splash.utilities.system.MouseLocation;

import java.awt.*;
import java.util.ArrayList;

public class ComponentContainer {

    private final String label;
    private boolean selected;
    private int x, y, width, height;
    private final Frame parent;
    private final ArrayList<Component> components = new ArrayList<>();

    public ComponentContainer(Frame parent, String label, int x, int y, int width, int height) {
        this.parent = parent;
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        final int realHeight = 12;
        int color = new Color(39, 39, 39, 185).getRGB();
        Gui.drawRect(getParent().getX() + x, getParent().getY() + y, getParent().getX() + x + width, getParent().getY() + y + realHeight, new Color(color).darker().getRGB());
        Splash.getInstance().getFontRenderer().drawStringWithShadow(label, getParent().getX() + x + 2, getParent().getY() + y + 2, 0xFFFFFFFF);
        String renderedText = selected ? "^" : "";
        Splash.getInstance().getFontRenderer().drawStringWithShadow(renderedText, getParent().getX() + x + width - 9, getParent().getY() + y + 4, 0xFFFFFFFF);
        if (this.isSelected()) {
            int newHeight = 12;
            int newY = 13;
            for (Component component : getComponents()) {
                component.draw(mouseX, mouseY, partialTicks);
                component.setY(newY);
                newY += component.getHeight() + 1;
                newHeight += component.getHeight() + 1;
            }
            setHeight(newHeight);
        } else {
            setHeight(realHeight);
        }
    }

    public void mouseClicked(int x, int y, int mouseButton) {
        if (MouseLocation.isOver(x, y, getParent().getX() + this.x, getParent().getY() + this.y, getParent().getX() + this.x + width, getParent().getY() + this.y + 12)) {
            setSelected(!isSelected());
        }
        if (this.isSelected()) {
            components.forEach(component -> component.mouseClicked(x, y, mouseButton));
        }
    }

    public void mouseReleased(int x, int y, int mouseButton) {
        if (this.isSelected()) {
            components.forEach(component -> component.mouseReleased(x, y, mouseButton));
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        components.forEach(component -> component.keyTyped(typedChar, keyCode));
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public Frame getParent() {
        return parent;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLabel() {
        return label;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
