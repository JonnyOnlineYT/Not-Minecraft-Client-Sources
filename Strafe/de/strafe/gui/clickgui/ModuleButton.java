package de.strafe.gui.clickgui;

import de.strafe.modules.Module;
import de.strafe.utils.IMinecraft;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@RequiredArgsConstructor
public class ModuleButton implements IComponent, IMinecraft {

    private final int height = mc.fontRendererObj.FONT_HEIGHT;
    private final Module module;
    private final Panel panel;
    private final int index;
    private boolean waitingForInput;

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        final String text = waitingForInput ? Keyboard.getKeyName(module.getKey()) + " -> " + "..." : module.getName();
        Gui.drawRect(panel.getX(), getY(), panel.getX() + mc.fontRendererObj.getStringWidth(text), getY() + height, new Color(0, 0, 0, 150).getRGB());
        mc.fontRendererObj.drawStringWithShadow(text, panel.getX(), getY(), module.isToggled() ? Color.WHITE.getRGB() : Color.LIGHT_GRAY.getRGB());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (waitingForInput) {
            if (keyCode != Keyboard.KEY_ESCAPE) {
                module.key = keyCode;
            }
            waitingForInput = false;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (waitingForInput && mouseButton == 2) {
            waitingForInput = false;
        }

        if (mouseX >= panel.getX() && mouseX < panel.getX() + mc.fontRendererObj.getStringWidth(module.getName()) && mouseY >= getY() && mouseY < getY() + height) {
            if (mouseButton == 0) {
                module.toggle();
            } else if (mouseButton == 2) {
                waitingForInput = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    private int getY() {
        return panel.getY() + index * height;
    }
}
