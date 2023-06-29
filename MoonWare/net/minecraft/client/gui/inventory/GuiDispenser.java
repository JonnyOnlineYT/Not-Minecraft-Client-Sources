package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Namespaced;

public class GuiDispenser extends GuiContainer
{
    private static final Namespaced DISPENSER_GUI_TEXTURES = new Namespaced("textures/gui/container/dispenser.png");

    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;

    /** The inventory contained within the corresponding Dispenser. */
    public IInventory dispenserInventory;

    public GuiDispenser(InventoryPlayer playerInv, IInventory dispenserInv)
    {
        super(new ContainerDispenser(playerInv, dispenserInv));
        playerInventory = playerInv;
        dispenserInventory = dispenserInv;
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
        String s = dispenserInventory.getDisplayName().asString();
        font.drawString(s, xSize / 2 - font.getStringWidth(s) / 2, 6, 4210752);
        font.drawString(playerInventory.getDisplayName().asString(), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(DISPENSER_GUI_TEXTURES);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
    }
}
