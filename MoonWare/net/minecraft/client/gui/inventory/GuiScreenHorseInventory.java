package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Namespaced;

public class GuiScreenHorseInventory extends GuiContainer
{
    private static final Namespaced HORSE_GUI_TEXTURES = new Namespaced("textures/gui/container/horse.png");

    /** The player inventory bound to this GUI. */
    private final IInventory playerInventory;

    /** The horse inventory bound to this GUI. */
    private final IInventory horseInventory;

    /** The EntityHorse whose inventory is currently being accessed. */
    private final AbstractHorse horseEntity;

    /** The mouse x-position recorded during the last rendered frame. */
    private float mousePosx;

    /** The mouse y-position recorded during the last renderered frame. */
    private float mousePosY;

    public GuiScreenHorseInventory(IInventory playerInv, IInventory horseInv, AbstractHorse horse)
    {
        super(new ContainerHorseInventory(playerInv, horseInv, horse, Minecraft.player));
        playerInventory = playerInv;
        horseInventory = horseInv;
        horseEntity = horse;
        allowUserInput = false;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        font.drawString(horseInventory.getDisplayName().asString(), 8, 6, 4210752);
        font.drawString(playerInventory.getDisplayName().asString(), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(HORSE_GUI_TEXTURES);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);

        if (horseEntity instanceof AbstractChestHorse)
        {
            AbstractChestHorse abstractchesthorse = (AbstractChestHorse) horseEntity;

            if (abstractchesthorse.func_190695_dh())
            {
                drawTexturedModalRect(i + 79, j + 17, 0, ySize, abstractchesthorse.func_190696_dl() * 18, 54);
            }
        }

        if (horseEntity.func_190685_dA())
        {
            drawTexturedModalRect(i + 7, j + 35 - 18, 18, ySize + 54, 18, 18);
        }

        if (horseEntity.func_190677_dK())
        {
            if (horseEntity instanceof EntityLlama)
            {
                drawTexturedModalRect(i + 7, j + 35, 36, ySize + 54, 18, 18);
            }
            else
            {
                drawTexturedModalRect(i + 7, j + 35, 0, ySize + 54, 18, 18);
            }
        }

        GuiInventory.drawEntityOnScreen(i + 51, j + 60, 17, (float)(i + 51) - mousePosx, (float)(j + 75 - 50) - mousePosY, horseEntity);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        mousePosx = (float)mouseX;
        mousePosY = (float)mouseY;
        super.draw(mouseX, mouseY, partialTick);
        func_191948_b(mouseX, mouseY);
    }
}
