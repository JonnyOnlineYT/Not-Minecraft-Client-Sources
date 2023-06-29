package ru.rockstar.client.ui.draggable;

import net.minecraft.client.gui.GuiScreen;
import ru.rockstar.Main;

public class GuiHudEditor extends GuiScreen {

	@Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawWorldBackground(0);
        for (final DraggableModule mod : Main.instance.draggableManager.getMods()) {
            mod.render(mouseX, mouseY);
            if (mod.drag.isDragging()) {
                DraggableComponent.draggableModules.add(mod);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
