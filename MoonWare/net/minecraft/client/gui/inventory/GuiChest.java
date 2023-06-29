package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Namespaced;
import org.lwjgl.input.Mouse;
import org.moonware.client.ui.button.ImageButton;

import java.awt.*;
import java.util.ArrayList;

public class GuiChest extends GuiContainer {
    /**
     * The ResourceLocation containing the chest GUI texture.
     */
    private static final Namespaced CHEST_GUI_TEXTURE = new Namespaced("textures/gui/container/generic_54.png");
    private final IInventory upperChestInventory;
    public final IInventory lowerChestInventory;

    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();

    /**
     * window height is calculated with these values; the more rows, the heigher
     */
    private final int inventoryRows;

    public GuiChest(IInventory upperInv, IInventory lowerInv) {
        super(new ContainerChest(upperInv, lowerInv, Minecraft.player));
        upperChestInventory = upperInv;
        lowerChestInventory = lowerInv;
        allowUserInput = false;
        int i = 222;
        int j = 114;
        inventoryRows = lowerInv.getSizeInventory() / 9;
        ySize = 114 + inventoryRows * 18;
    }

    @Override
    public void init() {
        int posY = (height - ySize) / 2 + 5;
        imageButtons.clear();
        imageButtons.add(new ImageButton(new Namespaced("moonware/stealer.png"), width / 2 + 35, posY - 30, 20, 20, "", 30));
        imageButtons.add(new ImageButton(new Namespaced("moonware/put.png"), width / 2 + 60, posY - 30, 20, 20, "", 31));
        super.init();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        for (ImageButton imageButton : imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }
        super.draw(mouseX, mouseY, partialTick);
        func_191948_b(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        font.drawString(lowerChestInventory.getDisplayName().asString(), 8, 6, 4210752);
        font.drawString(upperChestInventory.getDisplayName().asString(), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, inventoryRows * 18 + 17);
        drawTexturedModalRect(i, j + inventoryRows * 18 + 17, 0, 126, xSize, 96);
    }

    public int getInventoryRows() {
        return inventoryRows;
    }
}
