package net.minecraft.client.gui.inventory;

import com.google.common.collect.Sets;

import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Formatting;
import org.moonware.client.MoonWare;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.moonware.client.feature.impl.misc.ItemScroller;
import org.moonware.client.helpers.misc.TimerHelper;

public abstract class GuiContainer extends Screen
{
    /** The location of the inventory background texture */
    public static final Namespaced INVENTORY_BACKGROUND = new Namespaced("textures/gui/container/inventory.png");

    /** The X size of the inventory window in pixels. */
    protected int xSize = 176;

    /** The Y size of the inventory window in pixels. */
    protected int ySize = 166;

    /** A list of the players inventory slots */
    public Container inventorySlots;

    public static float animTicks;
    public static float addition;
    public static float lastTime;

    /**
     * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiLeft;

    /**
     * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiTop;

    /** holds the slot currently hovered */
    private Slot theSlot;

    /** Used when touchscreen is enabled. */
    private Slot clickedSlot;

    /** Used when touchscreen is enabled. */
    private boolean isRightMouseClick;

    /** Used when touchscreen is enabled */
    private ItemStack draggedStack = ItemStack.EMPTY;
    private int touchUpX;
    private int touchUpY;
    private Slot returningStackDestSlot;
    private long returningStackTime;

    /** Used when touchscreen is enabled */
    private ItemStack returningStack = ItemStack.EMPTY;
    private Slot currentDragTargetSlot;
    private long dragItemDropDelay;
    protected final Set<Slot> dragSplittingSlots = Sets.newHashSet();
    protected boolean dragSplitting;
    private int dragSplittingLimit;
    private int dragSplittingButton;
    private boolean ignoreMouseUp;
    private int dragSplittingRemnant;
    private long lastClickTime;
    private Slot lastClickSlot;
    private int lastClickButton;
    private boolean doubleClick;
    private ItemStack shiftClickedSlot = ItemStack.EMPTY;
    private final TimerHelper timerHelper = new TimerHelper();

    public GuiContainer(Container inventorySlotsIn)
    {
        inventorySlots = inventorySlotsIn;
        ignoreMouseUp = true;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        super.init();
        Minecraft.player.openContainer = inventorySlots;
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        addition = lastTime < Minecraft.timer.renderPartialTicks ? Minecraft.timer.renderPartialTicks - lastTime : (lastTime != Minecraft.timer.renderPartialTicks ? 1 - lastTime + Minecraft.timer.renderPartialTicks : 0);
        if (animTicks < 20) {
            animTicks = animTicks + Math.min(addition / 1.5F * (20.5F - animTicks), 20.0F - animTicks);
        }

        GlStateManager.translate(width / 2F, height / 2F, 1);
        GlStateManager.scale(animTicks * 0.05F, animTicks * 0.05F, animTicks * 0.05F);
        GlStateManager.translate(-width / 2F, -height / 2F, 1F);
        lastTime = Minecraft.timer.renderPartialTicks;

        int i = guiLeft;
        int j = guiTop;
        drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        super.draw(mouseX, mouseY, partialTick);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)i, (float)j, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        theSlot = null;
        int k = 240;
        int l = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i1 = 0; i1 < inventorySlots.inventorySlots.size(); ++i1)
        {
            Slot slot = inventorySlots.inventorySlots.get(i1);

            if (slot.canBeHovered())
            {
                drawSlot(slot);
            }

            if (isMouseOverSlot(slot, mouseX, mouseY) && slot.canBeHovered())
            {

                if (MoonWare.featureManager.getFeatureByClass(ItemScroller.class).getState()) {
                    if (Mouse.isButtonDown(0) && Keyboard.isKeyDown(Minecraft.gameSettings.keyBindSneak.getKeyCode())) {
                        if (Minecraft.screen != null) {
                            if (timerHelper.hasReached(ItemScroller.scrollerDelay.getNumberValue())) {
                                handleMouseClick(slot, slot.slotNumber, 0, ClickType.QUICK_MOVE);
                                timerHelper.reset();
                            }
                        }
                    }
                }

                theSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                int j1 = slot.xDisplayPosition;
                int k1 = slot.yDisplayPosition;
                GlStateManager.colorMask(true, true, true, false);
                drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }

        RenderHelper.disableStandardItemLighting();
        drawGuiContainerForegroundLayer(mouseX, mouseY);
        RenderHelper.enableGUIStandardItemLighting();
        InventoryPlayer inventoryplayer = Minecraft.player.inventory;
        ItemStack itemstack = draggedStack.isEmpty() ? inventoryplayer.getItemStack() : draggedStack;

        if (!itemstack.isEmpty())
        {
            int j2 = 8;
            int k2 = draggedStack.isEmpty() ? 8 : 16;
            String s = null;

            if (!draggedStack.isEmpty() && isRightMouseClick)
            {
                itemstack = itemstack.copy();
                itemstack.func_190920_e(MathHelper.ceil((float)itemstack.getCount() / 2.0F));
            }
            else if (dragSplitting && dragSplittingSlots.size() > 1)
            {
                itemstack = itemstack.copy();
                itemstack.func_190920_e(dragSplittingRemnant);

                if (itemstack.isEmpty())
                {
                    s = "" + Formatting.YELLOW + "0";
                }
            }

            drawItemStack(itemstack, mouseX - i - 8, mouseY - j - k2, s);
        }

        if (!returningStack.isEmpty())
        {
            float f = (float)(Minecraft.getSystemTime() - returningStackTime) / 100.0F;

            if (f >= 1.0F)
            {
                f = 1.0F;
                returningStack = ItemStack.EMPTY;
            }

            int l2 = returningStackDestSlot.xDisplayPosition - touchUpX;
            int i3 = returningStackDestSlot.yDisplayPosition - touchUpY;
            int l1 = touchUpX + (int)((float)l2 * f);
            int i2 = touchUpY + (int)((float)i3 * f);
            drawItemStack(returningStack, l1, i2, null);
        }

        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
    }

    protected void func_191948_b(int p_191948_1_, int p_191948_2_)
    {
        if (Minecraft.player.inventory.getItemStack().isEmpty() && theSlot != null && theSlot.getHasStack())
        {
            drawTooltip(theSlot.getStack(), p_191948_1_, p_191948_2_);
        }
    }

    /**
     * Draws an ItemStack.
     *  
     * The z index is increased by 32 (and not decreased afterwards), and the item is then rendered at z=200.
     */
    private void drawItemStack(ItemStack stack, int x, int y, String altText)
    {
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        zLevel = 200.0F;
        renderItem.zLevel = 200.0F;
        renderItem.renderItemAndEffectIntoGUI(stack, x, y);
        renderItem.renderItemOverlayIntoGUI(font, stack, x, y - (draggedStack.isEmpty() ? 0 : 8), altText);
        zLevel = 0.0F;
        renderItem.zLevel = 0.0F;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected abstract void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY);

    /**
     * Draws the given slot: any item in it, the slot's background, the hovered highlight, etc.
     */
    private void drawSlot(Slot slotIn)
    {
        int i = slotIn.xDisplayPosition;
        int j = slotIn.yDisplayPosition;
        ItemStack itemstack = slotIn.getStack();
        boolean flag = false;
        boolean flag1 = slotIn == clickedSlot && !draggedStack.isEmpty() && !isRightMouseClick;
        ItemStack itemstack1 = Minecraft.player.inventory.getItemStack();
        String s = null;

        if (slotIn == clickedSlot && !draggedStack.isEmpty() && isRightMouseClick && !itemstack.isEmpty())
        {
            itemstack = itemstack.copy();
            itemstack.func_190920_e(itemstack.getCount() / 2);
        }
        else if (dragSplitting && dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty())
        {
            if (dragSplittingSlots.size() == 1)
            {
                return;
            }

            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && inventorySlots.canDragIntoSlot(slotIn))
            {
                itemstack = itemstack1.copy();
                flag = true;
                Container.computeStackSize(dragSplittingSlots, dragSplittingLimit, itemstack, slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
                int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));

                if (itemstack.getCount() > k)
                {
                    s = Formatting.YELLOW.toString() + k;
                    itemstack.func_190920_e(k);
                }
            }
            else
            {
                dragSplittingSlots.remove(slotIn);
                updateDragSplitting();
            }
        }

        zLevel = 100.0F;
        renderItem.zLevel = 100.0F;

        if (itemstack.isEmpty() && slotIn.canBeHovered())
        {
            String s1 = slotIn.getSlotTexture();

            if (s1 != null)
            {
                TextureAtlasSprite textureatlassprite = Minecraft.getTextureMapBlocks().getAtlasSprite(s1);
                GlStateManager.disableLighting();
                Minecraft.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
                GlStateManager.enableLighting();
                flag1 = true;
            }
        }

        if (!flag1)
        {
            if (flag)
            {
                Gui.drawRect(i, j, i + 16, j + 16, -2130706433);
            }

            GlStateManager.enableDepth();
            renderItem.renderItemAndEffectIntoGUI(Minecraft.player, itemstack, i, j);
            renderItem.renderItemOverlayIntoGUI(font, itemstack, i, j, s);
        }

        renderItem.zLevel = 0.0F;
        zLevel = 0.0F;
    }

    private void updateDragSplitting()
    {
        ItemStack itemstack = Minecraft.player.inventory.getItemStack();

        if (!itemstack.isEmpty() && dragSplitting)
        {
            if (dragSplittingLimit == 2)
            {
                dragSplittingRemnant = itemstack.getMaxStackSize();
            }
            else
            {
                dragSplittingRemnant = itemstack.getCount();

                for (Slot slot : dragSplittingSlots)
                {
                    ItemStack itemstack1 = itemstack.copy();
                    ItemStack itemstack2 = slot.getStack();
                    int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
                    Container.computeStackSize(dragSplittingSlots, dragSplittingLimit, itemstack1, i);
                    int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));

                    if (itemstack1.getCount() > j)
                    {
                        itemstack1.func_190920_e(j);
                    }

                    dragSplittingRemnant -= itemstack1.getCount() - i;
                }
            }
        }
    }

    /**
     * Returns the slot at the given coordinates or null if there is none.
     */
    private Slot getSlotAtPosition(int x, int y)
    {
        for (int i = 0; i < inventorySlots.inventorySlots.size(); ++i)
        {
            Slot slot = inventorySlots.inventorySlots.get(i);

            if (isMouseOverSlot(slot, x, y) && slot.canBeHovered())
            {
                return slot;
            }
        }

        return null;
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);
        boolean flag = button == Minecraft.gameSettings.keyBindPickBlock.getKeyCode() + 100;
        Slot slot = getSlotAtPosition(mouseX, mouseY);
        long i = Minecraft.getSystemTime();
        doubleClick = lastClickSlot == slot && i - lastClickTime < 250L && lastClickButton == button;
        ignoreMouseUp = false;

        if (button == 0 || button == 1 || flag)
        {
            int j = guiLeft;
            int k = guiTop;
            boolean flag1 = func_193983_c(mouseX, mouseY, j, k);
            int l = -1;

            if (slot != null)
            {
                l = slot.slotNumber;
            }

            if (flag1)
            {
                l = -999;
            }

            if (l != -1)
            {
                if (!dragSplitting)
                {
                    if (Minecraft.player.inventory.getItemStack().isEmpty())
                    {
                        if (button == Minecraft.gameSettings.keyBindPickBlock.getKeyCode() + 100)
                        {
                            handleMouseClick(slot, l, button, ClickType.CLONE);
                        }
                        else
                        {
                            boolean flag2 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                            ClickType clicktype = ClickType.PICKUP;

                            if (flag2)
                            {
                                shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
                                clicktype = ClickType.QUICK_MOVE;
                            }
                            else if (l == -999)
                            {
                                clicktype = ClickType.THROW;
                            }

                            handleMouseClick(slot, l, button, clicktype);
                        }

                        ignoreMouseUp = true;
                    }
                    else
                    {
                        dragSplitting = true;
                        dragSplittingButton = button;
                        dragSplittingSlots.clear();

                        if (button == 0)
                        {
                            dragSplittingLimit = 0;
                        }
                        else if (button == 1)
                        {
                            dragSplittingLimit = 1;
                        }
                        else if (button == Minecraft.gameSettings.keyBindPickBlock.getKeyCode() + 100)
                        {
                            dragSplittingLimit = 2;
                        }
                    }
                }
            }
        }

        lastClickSlot = slot;
        lastClickTime = i;
        lastClickButton = button;
    }

    protected boolean func_193983_c(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_)
    {
        return p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + xSize || p_193983_2_ >= p_193983_4_ + ySize;
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        Slot slot = getSlotAtPosition(mouseX, mouseY);
        ItemStack itemstack = Minecraft.player.inventory.getItemStack();

        if (dragSplitting && slot != null && !itemstack.isEmpty() && (itemstack.getCount() > dragSplittingSlots.size() || dragSplittingLimit == 2) && Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && inventorySlots.canDragIntoSlot(slot))
        {
            dragSplittingSlots.add(slot);
            updateDragSplitting();
        }
    }

    /**
     * Called when a mouse button is released.
     */
    public void mouseReleased(int mouseX, int mouseY, int button)
    {
        Slot slot = getSlotAtPosition(mouseX, mouseY);
        int i = guiLeft;
        int j = guiTop;
        boolean flag = func_193983_c(mouseX, mouseY, i, j);
        int k = -1;

        if (slot != null)
        {
            k = slot.slotNumber;
        }

        if (flag)
        {
            k = -999;
        }

        if (doubleClick && slot != null && button == 0 && inventorySlots.canMergeSlot(ItemStack.EMPTY, slot))
        {
            if (Screen.hasShiftDown())
            {
                if (!shiftClickedSlot.isEmpty())
                {
                    for (Slot slot2 : inventorySlots.inventorySlots)
                    {
                        if (slot2 != null && slot2.canTakeStack(Minecraft.player) && slot2.getHasStack() && slot2.inventory == slot.inventory && Container.canAddItemToSlot(slot2, shiftClickedSlot, true))
                        {
                            handleMouseClick(slot2, slot2.slotNumber, button, ClickType.QUICK_MOVE);
                        }
                    }
                }
            }
            else
            {
                handleMouseClick(slot, k, button, ClickType.PICKUP_ALL);
            }

            doubleClick = false;
            lastClickTime = 0L;
        }
        else
        {
            if (dragSplitting && dragSplittingButton != button)
            {
                dragSplitting = false;
                dragSplittingSlots.clear();
                ignoreMouseUp = true;
                return;
            }

            if (ignoreMouseUp)
            {
                ignoreMouseUp = false;
                return;
            }

           if (dragSplitting && !dragSplittingSlots.isEmpty())
            {
                handleMouseClick(null, -999, Container.getQuickcraftMask(0, dragSplittingLimit), ClickType.QUICK_CRAFT);

                for (Slot slot1 : dragSplittingSlots)
                {
                    handleMouseClick(slot1, slot1.slotNumber, Container.getQuickcraftMask(1, dragSplittingLimit), ClickType.QUICK_CRAFT);
                }

                handleMouseClick(null, -999, Container.getQuickcraftMask(2, dragSplittingLimit), ClickType.QUICK_CRAFT);
            }
            else if (!Minecraft.player.inventory.getItemStack().isEmpty())
            {
                if (button == Minecraft.gameSettings.keyBindPickBlock.getKeyCode() + 100)
                {
                    handleMouseClick(slot, k, button, ClickType.CLONE);
                }
                else
                {
                    boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));

                    if (flag1)
                    {
                        shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
                    }

                    handleMouseClick(slot, k, button, flag1 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
                }
            }
        }

        if (Minecraft.player.inventory.getItemStack().isEmpty())
        {
            lastClickTime = 0L;
        }

        dragSplitting = false;
    }

    /**
     * Returns whether the mouse is over the given slot.
     */
    private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY)
    {
        return isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
    }

    /**
     * Test if the 2D point is in a rectangle (relative to the GUI). Args : rectX, rectY, rectWidth, rectHeight, pointX,
     * pointY
     */
    protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY)
    {
        int i = guiLeft;
        int j = guiTop;
        pointX = pointX - i;
        pointY = pointY - j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    public void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type)
    {
        if (slotIn != null)
        {
            slotId = slotIn.slotNumber;
        }

        Minecraft.playerController.windowClick(inventorySlots.windowId, slotId, mouseButton, type, Minecraft.player);
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (key == 1 || key == Minecraft.gameSettings.keyBindInventory.getKeyCode())
        {
            Minecraft.player.closeScreen();
        }

        checkHotbarKeys(key);

        if (theSlot != null && theSlot.getHasStack())
        {
            if (key == Minecraft.gameSettings.keyBindPickBlock.getKeyCode())
            {
                handleMouseClick(theSlot, theSlot.slotNumber, 0, ClickType.CLONE);
            }
            else if (key == Minecraft.gameSettings.keyBindDrop.getKeyCode())
            {
                handleMouseClick(theSlot, theSlot.slotNumber, Screen.hasControlDown() ? 1 : 0, ClickType.THROW);
            }
        }
    }

    /**
     * Checks whether a hotbar key (to swap the hovered item with an item in the hotbar) has been pressed. If so, it
     * swaps the given items.
     * Returns true if a hotbar key was pressed.
     */
    protected boolean checkHotbarKeys(int keyCode)
    {
        if (Minecraft.player.inventory.getItemStack().isEmpty() && theSlot != null)
        {
            for (int i = 0; i < 9; ++i)
            {
                if (keyCode == Minecraft.gameSettings.keyBindsHotbar[i].getKeyCode())
                {
                    handleMouseClick(theSlot, theSlot.slotNumber, i, ClickType.SWAP);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onClosed()
    {
        animTicks = 0;
        addition = 0;
        lastTime = 0;

        if (Minecraft.player != null)
        {
            inventorySlots.onContainerClosed(Minecraft.player);
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean pauses()
    {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        super.update();

        if (!Minecraft.player.isEntityAlive() || Minecraft.player.isDead)
        {
            Minecraft.player.closeScreen();
        }
    }
}
