package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Namespaced;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class GuiRepair extends GuiContainer implements IContainerListener
{
    private static final Namespaced ANVIL_RESOURCE = new Namespaced("textures/gui/container/anvil.png");
    private final ContainerRepair anvil;
    private GuiTextField nameField;
    private final InventoryPlayer playerInventory;

    public GuiRepair(InventoryPlayer inventoryIn, World worldIn)
    {
        super(new ContainerRepair(inventoryIn, worldIn, Minecraft.player));
        playerInventory = inventoryIn;
        anvil = (ContainerRepair) inventorySlots;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        super.init();
        Keyboard.enableRepeatEvents(true);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        nameField = new GuiTextField(0, font, i + 62, j + 24, 103, 12);
        nameField.setTextColor(-1);
        nameField.setDisabledTextColour(-1);
        nameField.setEnableBackgroundDrawing(false);
        nameField.setMaxStringLength(35);
        inventorySlots.removeListener(this);
        inventorySlots.addListener(this);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onClosed()
    {
        super.onClosed();
        Keyboard.enableRepeatEvents(false);
        inventorySlots.removeListener(this);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        font.drawString(I18n.format("container.repair"), 60, 6, 4210752);

        if (anvil.maximumCost > 0)
        {
            int i = 8453920;
            boolean flag = true;
            String s = I18n.format("container.repair.cost", anvil.maximumCost);

            if (anvil.maximumCost >= 40 && !Minecraft.player.capabilities.isCreativeMode)
            {
                s = I18n.format("container.repair.expensive");
                i = 16736352;
            }
            else if (!anvil.getSlot(2).getHasStack())
            {
                flag = false;
            }
            else if (!anvil.getSlot(2).canTakeStack(playerInventory.player))
            {
                i = 16736352;
            }

            if (flag)
            {
                int j = -16777216 | (i & 16579836) >> 2 | i & -16777216;
                int k = xSize - 8 - font.getStringWidth(s);
                int l = 67;

                if (font.getUnicodeFlag())
                {
                    Gui.drawRect(k - 3, 65, xSize - 7, 77, -16777216);
                    Gui.drawRect(k - 2, 66, xSize - 8, 76, -12895429);
                }
                else
                {
                    font.drawString(s, k, 68, j);
                    font.drawString(s, k + 1, 67, j);
                    font.drawString(s, k + 1, 68, j);
                }

                font.drawString(s, k, 67, i);
            }
        }

        GlStateManager.enableLighting();
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (nameField.textboxKeyTyped(c, key))
        {
            renameItem();
        }
        else
        {
            super.keyPressed(key, c);
        }
    }

    private void renameItem()
    {
        String s = nameField.getText();
        Slot slot = anvil.getSlot(0);

        if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().getDisplayName()))
        {
            s = "";
        }

        anvil.updateItemName(s);
        Minecraft.player.connection.sendPacket(new CPacketCustomPayload("MC|ItemName", (new PacketBuffer(Unpooled.buffer())).writeString(s)));
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);
        nameField.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        super.draw(mouseX, mouseY, partialTick);
        func_191948_b(mouseX, mouseY);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        nameField.drawTextBox();
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(ANVIL_RESOURCE);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
        drawTexturedModalRect(i + 59, j + 20, 0, ySize + (anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

        if ((anvil.getSlot(0).getHasStack() || anvil.getSlot(1).getHasStack()) && !anvil.getSlot(2).getHasStack())
        {
            drawTexturedModalRect(i + 99, j + 45, xSize, 0, 28, 21);
        }
    }

    /**
     * update the crafting window inventory with the items in the list
     */
    public void updateCraftingInventory(Container containerToSend, NonNullList<ItemStack> itemsList)
    {
        sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
    }

    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot.
     */
    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
    {
        if (slotInd == 0)
        {
            nameField.setText(stack.isEmpty() ? "" : stack.getDisplayName());
            nameField.setEnabled(!stack.isEmpty());

            if (!stack.isEmpty())
            {
                renameItem();
            }
        }
    }

    /**
     * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
     * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
     * value. Both are truncated to shorts in non-local SMP.
     */
    public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue)
    {
    }

    public void sendAllWindowProperties(Container containerIn, IInventory inventory)
    {
    }
}
