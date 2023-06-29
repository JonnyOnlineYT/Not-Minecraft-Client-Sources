package de.strafe.gui.clickgui;

import de.strafe.Strafe;
import de.strafe.modules.Category;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClickGUI extends GuiScreen {
    private final List<Panel> panels = new ArrayList<>();
    private int y = 10;
    private int index;

    public ClickGUI() {
        Arrays.stream(Category.values()).forEach(category -> {
            final Panel panel = new Panel(category.getText(), 10, y);
            index = 1;
            Strafe.INSTANCE.moduleManager.modules.stream().filter(module -> module.getCategory().equals(category)).forEach(module -> {
                panel.getModuleButtons().add(new ModuleButton(module, panel, index++));
            });
            if (!panel.getModuleButtons().isEmpty()) {
                panels.add(panel);
                y += 20;
            }
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        panels.forEach(panel -> panel.drawScreen(mouseX, mouseY));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        panels.forEach(panel -> panel.keyTyped(typedChar, keyCode));
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, state));
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
