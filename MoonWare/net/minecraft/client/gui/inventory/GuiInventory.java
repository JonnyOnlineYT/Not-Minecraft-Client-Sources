package net.minecraft.client.gui.inventory;

import java.awt.*;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.Namespaced;
import org.lwjgl.input.Mouse;
import org.moonware.client.ui.button.ImageButton;

public class GuiInventory extends InventoryEffectRenderer implements IRecipeShownListener
{
    /** The old x position of the mouse pointer */
    private float oldMouseX;

    /** The old y position of the mouse pointer */
    private float oldMouseY;
    private GuiButtonImage field_192048_z;
    private final GuiRecipeBook field_192045_A = new GuiRecipeBook();
    private boolean field_192046_B;
    private boolean field_194031_B;

    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();

    public GuiInventory(EntityPlayer player)
    {
        super(player.inventoryContainer);
        allowUserInput = true;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        if (Minecraft.playerController.isInCreativeMode())
        {
            Minecraft.openScreen(new GuiContainerCreative(Minecraft.player));
        }

        field_192045_A.func_193957_d();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        widgets.clear();

        if (Minecraft.playerController.isInCreativeMode())
        {
            Minecraft.openScreen(new GuiContainerCreative(Minecraft.player));
        }
        else
        {
            super.init();
        }

        field_192046_B = width < 379;
        field_192045_A.func_194303_a(width, height, minecraft, field_192046_B, ((ContainerPlayer) inventorySlots).craftMatrix);
        guiLeft = field_192045_A.func_193011_a(field_192046_B, width, xSize);
        field_192048_z = new GuiButtonImage(10, guiLeft + 104, height / 2 - 22, 20, 18, 178, 0, 19, GuiContainer.INVENTORY_BACKGROUND);
        widgets.add(field_192048_z);
        imageButtons.clear();
        imageButtons.add(new ImageButton(new Namespaced("moonware/trashcan.png"), guiLeft + 153, height / 2 - 106, 20, 20, "", 32));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        font.drawString(I18n.format("container.crafting"), 97, 8, 4210752);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        for (ImageButton imageButton : imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }
        hasActivePotionEffects = !field_192045_A.func_191878_b();

        if (field_192045_A.func_191878_b() && field_192046_B)
        {
            drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
            field_192045_A.func_191861_a(mouseX, mouseY, partialTick);
        }
        else
        {
            field_192045_A.func_191861_a(mouseX, mouseY, partialTick);
            super.draw(mouseX, mouseY, partialTick);
            field_192045_A.func_191864_a(guiLeft, guiTop, false, partialTick);
        }

        func_191948_b(mouseX, mouseY);
        field_192045_A.func_191876_c(guiLeft, guiTop, mouseX, mouseY);
        oldMouseX = (float)mouseX;
        oldMouseY = (float)mouseY;
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
        int i = guiLeft;
        int j = guiTop;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
        drawEntityOnScreen(i + 51, j + 75, 30, (float)(i + 51) - oldMouseX, (float)(j + 75 - 50) - oldMouseY, Minecraft.player);
    }

    /**
     * Draws an entity on the screen looking toward the cursor.
     */
    public static void drawEntityOnScreen(float posX, float posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float)Math.atan(mouseX / 40.0F) * 20.0F;
        ent.rotationYaw = (float)Math.atan(mouseX / 40.0F) * 40.0F;
        ent.rotationPitch = -((float)Math.atan(mouseY / 40.0F)) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Test if the 2D point is in a rectangle (relative to the GUI). Args : rectX, rectY, rectWidth, rectHeight, pointX,
     * pointY
     */
    protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY)
    {
        return (!field_192046_B || !field_192045_A.func_191878_b()) && super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        if (!field_192045_A.func_191862_a(mouseX, mouseY, button))
        {
            if (!field_192046_B || !field_192045_A.func_191878_b())
            {
                super.mousePressed(mouseX, mouseY, button);
            }
        }
    }

    /**
     * Called when a mouse button is released.
     */
    public void mouseReleased(int mouseX, int mouseY, int button)
    {
        if (field_194031_B)
        {
            field_194031_B = false;
        }
        else
        {
            super.mouseReleased(mouseX, mouseY, button);
        }
    }

    protected boolean func_193983_c(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_)
    {
        boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + xSize || p_193983_2_ >= p_193983_4_ + ySize;
        return field_192045_A.func_193955_c(p_193983_1_, p_193983_2_, guiLeft, guiTop, xSize, ySize) && flag;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.id == 10)
        {
            field_192045_A.func_193014_a(field_192046_B, ((ContainerPlayer) inventorySlots).craftMatrix);
            field_192045_A.func_191866_a();
            guiLeft = field_192045_A.func_193011_a(field_192046_B, width, xSize);
            field_192048_z.func_191746_c(guiLeft + 104, height / 2 - 22);
            field_194031_B = true;
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (!field_192045_A.func_191859_a(c, key))
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
        field_192045_A.func_191874_a(slotIn);
    }

    public void func_192043_J_()
    {
        field_192045_A.func_193948_e();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onClosed()
    {
        field_192045_A.func_191871_c();
        super.onClosed();
    }

    public GuiRecipeBook func_194310_f()
    {
        return field_192045_A;
    }
}
