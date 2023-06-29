package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Namespaced;

public class GuiShulkerBox extends GuiContainer
{
    private static final Namespaced field_190778_u = new Namespaced("textures/gui/container/shulker_box.png");
    private final IInventory field_190779_v;
    private final InventoryPlayer field_190780_w;

    public GuiShulkerBox(InventoryPlayer p_i47233_1_, IInventory p_i47233_2_)
    {
        super(new ContainerShulkerBox(p_i47233_1_, p_i47233_2_, Minecraft.player));
        field_190780_w = p_i47233_1_;
        field_190779_v = p_i47233_2_;
        ++ySize;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        super.draw(mouseX, mouseY, partialTick);
        func_191948_b(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        font.drawString(field_190779_v.getDisplayName().asString(), 8, 6, 4210752);
        font.drawString(field_190780_w.getDisplayName().asString(), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(field_190778_u);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
    }
}
