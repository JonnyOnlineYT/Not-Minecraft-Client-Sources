package ru.rockstar.client.ui.espgble;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import ru.rockstar.Main;

public class GuiHudEditor extends GuiScreen {

	@Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawWorldBackground(0);
        mc.renderEngine.bindTexture(new ResourceLocation("rockstar/chel.png"));
        GlStateManager.color(255.0f, 255.0f, 255.0f);
        //Gui.drawScaledCustomSizeModalRect((int) 150, (int)100, 200.0f, 200.0f, (int)200, (int)200,(int) 200, (int)200, (float) 200, (float) 200);
        
        
        GuiInventory.drawEntityOnScreen(250, 260, 60, -1, -1, mc.player);
        
        
        
        
        for (final DraggableModule mod : Main.instance.espgbleManager.getMods()) {
            mod.render(mouseX, mouseY);
            if (mod.drag.isDragging()) {
                DraggableComponent.draggableModules.add(mod);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
