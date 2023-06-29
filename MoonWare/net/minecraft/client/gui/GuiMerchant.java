package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Component;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiMerchant extends GuiContainer
{
    private static final Logger LOGGER = LogManager.getLogger();

    /** The GUI texture for the villager merchant GUI. */
    private static final Namespaced MERCHANT_GUI_TEXTURE = new Namespaced("textures/gui/container/villager.png");

    /** The current IMerchant instance in use for this specific merchant. */
    private final IMerchant merchant;

    /** The button which proceeds to the next available merchant recipe. */
    private GuiMerchant.MerchantButton nextButton;

    /** Returns to the previous Merchant recipe if one is applicable. */
    private GuiMerchant.MerchantButton previousButton;

    /**
     * The integer value corresponding to the currently selected merchant recipe.
     */
    private int selectedMerchantRecipe;

    /** The chat component utilized by this GuiMerchant instance. */
    private final Component chatComponent;

    public GuiMerchant(InventoryPlayer p_i45500_1_, IMerchant p_i45500_2_, World worldIn)
    {
        super(new ContainerMerchant(p_i45500_1_, p_i45500_2_, worldIn));
        merchant = p_i45500_2_;
        chatComponent = p_i45500_2_.getDisplayName();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        super.init();
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        nextButton = addButton(new MerchantButton(1, i + 120 + 27, j + 24 - 1, true));
        previousButton = addButton(new MerchantButton(2, i + 36 - 19, j + 24 - 1, false));
        nextButton.enabled = false;
        previousButton.enabled = false;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = chatComponent.asString();
        font.drawString(s, xSize / 2 - font.getStringWidth(s) / 2, 6, 4210752);
        font.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        super.update();
        MerchantRecipeList merchantrecipelist = merchant.getRecipes(Minecraft.player);

        if (merchantrecipelist != null)
        {
            nextButton.enabled = selectedMerchantRecipe < merchantrecipelist.size() - 1;
            previousButton.enabled = selectedMerchantRecipe > 0;
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        boolean flag = false;

        if (button == nextButton)
        {
            ++selectedMerchantRecipe;
            MerchantRecipeList merchantrecipelist = merchant.getRecipes(Minecraft.player);

            if (merchantrecipelist != null && selectedMerchantRecipe >= merchantrecipelist.size())
            {
                selectedMerchantRecipe = merchantrecipelist.size() - 1;
            }

            flag = true;
        }
        else if (button == previousButton)
        {
            --selectedMerchantRecipe;

            if (selectedMerchantRecipe < 0)
            {
                selectedMerchantRecipe = 0;
            }

            flag = true;
        }

        if (flag)
        {
            ((ContainerMerchant) inventorySlots).setCurrentRecipeIndex(selectedMerchantRecipe);
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(selectedMerchantRecipe);
            minecraft.getConnection().sendPacket(new CPacketCustomPayload("MC|TrSel", packetbuffer));
        }
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
        MerchantRecipeList merchantrecipelist = merchant.getRecipes(Minecraft.player);

        if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
        {
            int k = selectedMerchantRecipe;

            if (k < 0 || k >= merchantrecipelist.size())
            {
                return;
            }

            MerchantRecipe merchantrecipe = merchantrecipelist.get(k);

            if (merchantrecipe.isRecipeDisabled())
            {
                Minecraft.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableLighting();
                drawTexturedModalRect(guiLeft + 83, guiTop + 21, 212, 0, 28, 21);
                drawTexturedModalRect(guiLeft + 83, guiTop + 51, 212, 0, 28, 21);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        super.draw(mouseX, mouseY, partialTick);
        MerchantRecipeList merchantrecipelist = merchant.getRecipes(Minecraft.player);

        if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
        {
            int i = (width - xSize) / 2;
            int j = (height - ySize) / 2;
            int k = selectedMerchantRecipe;
            MerchantRecipe merchantrecipe = merchantrecipelist.get(k);
            ItemStack itemstack = merchantrecipe.getItemToBuy();
            ItemStack itemstack1 = merchantrecipe.getSecondItemToBuy();
            ItemStack itemstack2 = merchantrecipe.getItemToSell();
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            renderItem.zLevel = 100.0F;
            renderItem.renderItemAndEffectIntoGUI(itemstack, i + 36, j + 24);
            renderItem.renderItemOverlays(font, itemstack, i + 36, j + 24);

            if (!itemstack1.isEmpty())
            {
                renderItem.renderItemAndEffectIntoGUI(itemstack1, i + 62, j + 24);
                renderItem.renderItemOverlays(font, itemstack1, i + 62, j + 24);
            }

            renderItem.renderItemAndEffectIntoGUI(itemstack2, i + 120, j + 24);
            renderItem.renderItemOverlays(font, itemstack2, i + 120, j + 24);
            renderItem.zLevel = 0.0F;
            GlStateManager.disableLighting();

            if (isPointInRegion(36, 24, 16, 16, mouseX, mouseY) && !itemstack.isEmpty())
            {
                drawTooltip(itemstack, mouseX, mouseY);
            }
            else if (!itemstack1.isEmpty() && isPointInRegion(62, 24, 16, 16, mouseX, mouseY) && !itemstack1.isEmpty())
            {
                drawTooltip(itemstack1, mouseX, mouseY);
            }
            else if (!itemstack2.isEmpty() && isPointInRegion(120, 24, 16, 16, mouseX, mouseY) && !itemstack2.isEmpty())
            {
                drawTooltip(itemstack2, mouseX, mouseY);
            }
            else if (merchantrecipe.isRecipeDisabled() && (isPointInRegion(83, 21, 28, 21, mouseX, mouseY) || isPointInRegion(83, 51, 28, 21, mouseX, mouseY)))
            {
                drawTooltip(I18n.format("merchant.deprecated"), mouseX, mouseY);
            }

            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
        }

        func_191948_b(mouseX, mouseY);
    }

    public IMerchant getMerchant()
    {
        return merchant;
    }

    static class MerchantButton extends ButtonWidget
    {
        private final boolean forward;

        public MerchantButton(int buttonID, int x, int y, boolean p_i1095_4_)
        {
            super(buttonID, x, y, 12, 19, "");
            forward = p_i1095_4_;
        }

        public void draw(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float partialTick)
        {
            if (visible)
            {
                Minecraft.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                boolean flag = p_191745_2_ >= x && p_191745_3_ >= y && p_191745_2_ < x + width && p_191745_3_ < y + height;
                int i = 0;
                int j = 176;

                if (!enabled)
                {
                    j += width * 2;
                }
                else if (flag)
                {
                    j += width;
                }

                if (!forward)
                {
                    i += height;
                }

                drawTexturedModalRect(x, y, j, i, width, height);
            }
        }
    }
}
