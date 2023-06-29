package splash.gui.huzini;

import net.minecraft.client.gui.Gui;
import splash.Splash;
import splash.gui.huzini.components.components.ComponentContainer;
import splash.utilities.system.MouseLocation;

import java.awt.*;
import java.util.ArrayList;

public class Frame {

    private final String label;
    private int x, y, width, height;
    private boolean dragging;
    private int lastKnownX, lastKnownY;
    private final ArrayList<ComponentContainer> containers = new ArrayList<>();
    private ComponentContainer current;

    public Frame(String label, int x, int y, int width, int height) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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

    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            this.setX(mouseX + this.lastKnownX);
            this.setY(mouseY + this.lastKnownY);
        }
        int i = 0;
        int color = Splash.getInstance().getClientColor();
        Gui.drawRect(x, y, x + width, y + 12, color);
        Gui.drawRect(x, y + 12, x + width, y + 12 + height - 12, new Color(0, 0, 0, 255).darker().getRGB());
        Splash.getInstance().getFontRenderer().drawStringWithShadow(label, x + 3, y + 2, 0xFFFFFFFF);
        int newY = 14;
        for (ComponentContainer container : containers) {
            container.setY(newY);
            newY += container.getHeight() + 1;
        }
        containers.forEach(container -> container.draw(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(int x, int y, int mouseButton) {
        if (MouseLocation.isOver(x, y, getX(), getY(), getX() + getWidth(), getY() + 12)) {
            if (mouseButton == 0) {
                dragging = true;
                this.lastKnownX = (getX() - x);
                this.lastKnownY = (getY() - y);
            }
        }
        containers.forEach(componentContainer -> componentContainer.mouseClicked(x, y, mouseButton));
    }

    public void mouseReleased(int x, int y, int mouseButton) {
        if (mouseButton == 0) {
            dragging = false;
        }

        containers.forEach(componentContainer -> componentContainer.mouseReleased(x, y, mouseButton));

    }

    public void keyTyped(char typedChar, int keyCode) {
        containers.forEach(componentContainer -> componentContainer.keyTyped(typedChar, keyCode));
    }

    public ArrayList<ComponentContainer> getContainers() {
        return containers;
    }
}
