package de.strafe.gui.clickgui;

import de.strafe.utils.IMinecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Panel implements IComponent, IMinecraft {
    private final List<ModuleButton> moduleButtons = new ArrayList<>();
    private final String panelName;
    private final int height = mc.fontRendererObj.FONT_HEIGHT;
    private int x, y, clickedX, clickedY;
    private boolean extended, dragging;

    public Panel(String panelName, int x, int y) {
        this.panelName = panelName;
        this.x = x;
        this.y = y;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        if (dragging) {
            x = clickedX + mouseX;
            y = clickedY + mouseY;
        }
        Gui.drawRect(x, y, x + mc.fontRendererObj.getStringWidth(panelName), y + mc.fontRendererObj.FONT_HEIGHT, new Color(0, 0, 0, 150).getRGB());
        mc.fontRendererObj.drawStringWithShadow(panelName, x, y, Color.WHITE.getRGB());
        if (extended) {
            moduleButtons.forEach(moduleButton -> moduleButton.drawScreen(mouseX, mouseY));
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (extended) {
            moduleButtons.forEach(moduleButton -> moduleButton.keyTyped(typedChar, keyCode));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseX >= x && mouseX < x + mc.fontRendererObj.getStringWidth(panelName) && mouseY >= y && mouseY < y + height) {
            if (mouseButton == 0) {
                clickedX = x - mouseX;
                clickedY = y - mouseY;
                dragging = true;
            } else if (mouseButton == 1) {
                extended = !extended;
            }
        }
        if (extended) {
            moduleButtons.forEach(moduleButton -> moduleButton.mouseClicked(mouseX, mouseY, mouseButton));
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        if (extended) {
            moduleButtons.forEach(moduleButton -> moduleButton.mouseReleased(mouseX, mouseY, state));
        }
    }

    public List<ModuleButton> getModuleButtons() {
        return moduleButtons;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
