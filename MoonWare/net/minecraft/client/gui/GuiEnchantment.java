package net.minecraft.client.gui;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Formatting;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import org.lwjgl.util.glu.Project;

public class GuiEnchantment extends GuiContainer
{
    /** The ResourceLocation containing the Enchantment GUI texture location */
    private static final Namespaced ENCHANTMENT_TABLE_GUI_TEXTURE = new Namespaced("textures/gui/container/enchanting_table.png");

    /**
     * The ResourceLocation containing the texture for the Book rendered above the enchantment table
     */
    private static final Namespaced ENCHANTMENT_TABLE_BOOK_TEXTURE = new Namespaced("textures/entity/enchanting_table_book.png");

    /**
     * The ModelBook instance used for rendering the book on the Enchantment table
     */
    private static final ModelBook MODEL_BOOK = new ModelBook();

    /** The player inventory currently bound to this GuiEnchantment instance. */
    private final InventoryPlayer playerInventory;

    /** A Random instance for use with the enchantment gui */
    private final Random random = new Random();
    private final ContainerEnchantment container;
    public int ticks;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.EMPTY;
    private final IWorldNameable nameable;

    public GuiEnchantment(InventoryPlayer inventory, World worldIn, IWorldNameable nameable)
    {
        super(new ContainerEnchantment(inventory, worldIn));
        playerInventory = inventory;
        container = (ContainerEnchantment) inventorySlots;
        this.nameable = nameable;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        font.drawString(nameable.getDisplayName().asString(), 12, 5, 4210752);
        font.drawString(playerInventory.getDisplayName().asString(), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        super.update();
        tickBook();
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;

        for (int k = 0; k < 3; ++k)
        {
            int l = mouseX - (i + 60);
            int i1 = mouseY - (j + 14 + 19 * k);

            if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && container.enchantItem(Minecraft.player, k))
            {
                Minecraft.playerController.sendEnchantPacket(container.windowId, k);
            }
        }
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        ScaledResolution scaledresolution = new ScaledResolution(minecraft);
        GlStateManager.viewport((scaledresolution.getScaledWidth() - 320) / 2 * scaledresolution.getScaleFactor(), (scaledresolution.getScaledHeight() - 240) / 2 * scaledresolution.getScaleFactor(), 320 * scaledresolution.getScaleFactor(), 240 * scaledresolution.getScaleFactor());
        GlStateManager.translate(-0.34F, 0.23F, 0.0F);
        Project.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
        float f = 1.0F;
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(0.0F, 3.3F, -16.0F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        float f1 = 5.0F;
        GlStateManager.scale(5.0F, 5.0F, 5.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
        GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
        float f2 = oOpen + (open - oOpen) * partialTicks;
        GlStateManager.translate((1.0F - f2) * 0.2F, (1.0F - f2) * 0.1F, (1.0F - f2) * 0.25F);
        GlStateManager.rotate(-(1.0F - f2) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        float f3 = oFlip + (flip - oFlip) * partialTicks + 0.25F;
        float f4 = oFlip + (flip - oFlip) * partialTicks + 0.75F;
        f3 = (f3 - (float)MathHelper.fastFloor(f3)) * 1.6F - 0.3F;
        f4 = (f4 - (float)MathHelper.fastFloor(f4)) * 1.6F - 0.3F;

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        if (f4 < 0.0F)
        {
            f4 = 0.0F;
        }

        if (f3 > 1.0F)
        {
            f3 = 1.0F;
        }

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        GlStateManager.enableRescaleNormal();
        MODEL_BOOK.render(null, 0.0F, f3, f4, f2, 0.0F, 0.0625F);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode(5889);
        GlStateManager.viewport(0, 0, Minecraft.width, Minecraft.height);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        EnchantmentNameParts.getInstance().reseedRandomGenerator(container.xpSeed);
        int k = container.getLapisAmount();

        for (int l = 0; l < 3; ++l)
        {
            int i1 = i + 60;
            int j1 = i1 + 20;
            zLevel = 0.0F;
            Minecraft.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
            int k1 = container.enchantLevels[l];
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (k1 == 0)
            {
                drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
            }
            else
            {
                String s = "" + k1;
                int l1 = 86 - font.getStringWidth(s);
                String s1 = EnchantmentNameParts.getInstance().generateNewRandomName(font, l1);
                Font fontrenderer = Minecraft.galacticFont;
                int i2 = 6839882;

                if ((k < l + 1 || Minecraft.player.experienceLevel < k1) && !Minecraft.player.capabilities.isCreativeMode)
                {
                    drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
                    drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * l, 239, 16, 16);
                    fontrenderer.drawSplitString(s1, j1, j + 16 + 19 * l, l1, (i2 & 16711422) >> 1);
                    i2 = 4226832;
                }
                else
                {
                    int j2 = mouseX - (i + 60);
                    int k2 = mouseY - (j + 14 + 19 * l);

                    if (j2 >= 0 && k2 >= 0 && j2 < 108 && k2 < 19)
                    {
                        drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 204, 108, 19);
                        i2 = 16777088;
                    }
                    else
                    {
                        drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 166, 108, 19);
                    }

                    drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * l, 223, 16, 16);
                    fontrenderer.drawSplitString(s1, j1, j + 16 + 19 * l, l1, i2);
                    i2 = 8453920;
                }

                fontrenderer = Minecraft.font;
                fontrenderer.drawStringWithShadow(s, (float)(j1 + 86 - fontrenderer.getStringWidth(s)), (float)(j + 16 + 19 * l + 7), i2);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        partialTick = minecraft.func_193989_ak();
        drawDefaultBackground();
        super.draw(mouseX, mouseY, partialTick);
        func_191948_b(mouseX, mouseY);
        boolean flag = Minecraft.player.capabilities.isCreativeMode;
        int i = container.getLapisAmount();

        for (int j = 0; j < 3; ++j)
        {
            int k = container.enchantLevels[j];
            Enchantment enchantment = Enchantment.getEnchantmentByID(container.enchantClue[j]);
            int l = container.worldClue[j];
            int i1 = j + 1;

            if (isPointInRegion(60, 14 + 19 * j, 108, 17, mouseX, mouseY) && k > 0 && l >= 0 && enchantment != null)
            {
                List<String> list = Lists.newArrayList();
                list.add("" + Formatting.WHITE + Formatting.ITALIC + I18n.format("container.enchant.clue", enchantment.getTranslatedName(l)));

                if (!flag)
                {
                    list.add("");

                    if (Minecraft.player.experienceLevel < k)
                    {
                        list.add(Formatting.RED + I18n.format("container.enchant.level.requirement", container.enchantLevels[j]));
                    }
                    else
                    {
                        String s;

                        if (i1 == 1)
                        {
                            s = I18n.format("container.enchant.lapis.one");
                        }
                        else
                        {
                            s = I18n.format("container.enchant.lapis.many", i1);
                        }

                        Formatting textformatting = i >= i1 ? Formatting.GRAY : Formatting.RED;
                        list.add(textformatting + "" + s);

                        if (i1 == 1)
                        {
                            s = I18n.format("container.enchant.level.one");
                        }
                        else
                        {
                            s = I18n.format("container.enchant.level.many", i1);
                        }

                        list.add(Formatting.GRAY + "" + s);
                    }
                }

                drawTooltip(list, mouseX, mouseY);
                break;
            }
        }
    }

    public void tickBook()
    {
        ItemStack itemstack = inventorySlots.getSlot(0).getStack();

        if (!ItemStack.areItemStacksEqual(itemstack, last))
        {
            last = itemstack;

            while (true)
            {
                flipT += (float)(random.nextInt(4) - random.nextInt(4));

                if (flip > flipT + 1.0F || flip < flipT - 1.0F)
                {
                    break;
                }
            }
        }

        ++ticks;
        oFlip = flip;
        oOpen = open;
        boolean flag = false;

        for (int i = 0; i < 3; ++i)
        {
            if (container.enchantLevels[i] != 0)
            {
                flag = true;
            }
        }

        if (flag)
        {
            open += 0.2F;
        }
        else
        {
            open -= 0.2F;
        }

        open = MathHelper.clamp(open, 0.0F, 1.0F);
        float f1 = (flipT - flip) * 0.4F;
        float f = 0.2F;
        f1 = MathHelper.clamp(f1, -0.2F, 0.2F);
        flipA += (f1 - flipA) * 0.9F;
        flip += flipA;
    }
}
