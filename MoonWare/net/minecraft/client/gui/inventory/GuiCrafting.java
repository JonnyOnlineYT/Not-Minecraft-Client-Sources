package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Slot;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiCrafting extends GuiContainer implements IRecipeShownListener
{
    private static final Namespaced CRAFTING_TABLE_GUI_TEXTURES = new Namespaced("textures/gui/container/crafting_table.png");
    private GuiButtonImage field_192049_w;
    private final GuiRecipeBook field_192050_x;
    private boolean field_193112_y;

    public GuiCrafting(InventoryPlayer playerInv, World worldIn)
    {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }

    public GuiCrafting(InventoryPlayer playerInv, World worldIn, BlockPos blockPosition)
    {
        super(new ContainerWorkbench(playerInv, worldIn, blockPosition));
        field_192050_x = new GuiRecipeBook();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        super.init();
        field_193112_y = width < 379;
        field_192050_x.func_194303_a(width, height, minecraft, field_193112_y, ((ContainerWorkbench) inventorySlots).craftMatrix);
        guiLeft = field_192050_x.func_193011_a(field_193112_y, width, xSize);
        field_192049_w = new GuiButtonImage(10, guiLeft + 5, height / 2 - 49, 20, 18, 0, 168, 19, CRAFTING_TABLE_GUI_TEXTURES);
        widgets.add(field_192049_w);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        super.update();
        field_192050_x.func_193957_d();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();

        if (field_192050_x.func_191878_b() && field_193112_y)
        {
            drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
            field_192050_x.func_191861_a(mouseX, mouseY, partialTick);
        }
        else
        {
            field_192050_x.func_191861_a(mouseX, mouseY, partialTick);
            super.draw(mouseX, mouseY, partialTick);
            field_192050_x.func_191864_a(guiLeft, guiTop, true, partialTick);
        }

        func_191948_b(mouseX, mouseY);
        field_192050_x.func_191876_c(guiLeft, guiTop, mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        font.drawString(I18n.format("container.crafting"), 28, 6, 4210752);
        font.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURES);
        int i = guiLeft;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
    }

    /**
     * Test if the 2D point is in a rectangle (relative to the GUI). Args : rectX, rectY, rectWidth, rectHeight, pointX,
     * pointY
     */
    protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY)
    {
        return (!field_193112_y || !field_192050_x.func_191878_b()) && super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        if (!field_192050_x.func_191862_a(mouseX, mouseY, button))
        {
            if (!field_193112_y || !field_192050_x.func_191878_b())
            {
                super.mousePressed(mouseX, mouseY, button);
            }
        }
    }

    protected boolean func_193983_c(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_)
    {
        boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + xSize || p_193983_2_ >= p_193983_4_ + ySize;
        return field_192050_x.func_193955_c(p_193983_1_, p_193983_2_, guiLeft, guiTop, xSize, ySize) && flag;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.id == 10)
        {
            field_192050_x.func_193014_a(field_193112_y, ((ContainerWorkbench) inventorySlots).craftMatrix);
            field_192050_x.func_191866_a();
            guiLeft = field_192050_x.func_193011_a(field_193112_y, width, xSize);
            field_192049_w.func_191746_c(guiLeft + 5, height / 2 - 49);
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (!field_192050_x.func_191859_a(c, key))
        {
            super.keyPressed(key, c);
        }
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    public void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type)
    {
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
        field_192050_x.func_191874_a(slotIn);
    }

    public void func_192043_J_()
    {
        field_192050_x.func_193948_e();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onClosed()
    {
        field_192050_x.func_191871_c();
        super.onClosed();
    }

    public GuiRecipeBook func_194310_f()
    {
        return field_192050_x;
    }
}
