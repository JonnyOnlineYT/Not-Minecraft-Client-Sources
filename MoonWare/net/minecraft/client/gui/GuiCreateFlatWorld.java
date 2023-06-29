package net.minecraft.client.gui;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;

public class GuiCreateFlatWorld extends Screen
{
    private final GuiCreateWorld createWorldGui;
    private FlatGeneratorInfo theFlatGeneratorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();

    /** The title given to the flat world currently in creation */
    private String flatWorldTitle;

    /** The text used to identify the material for a layer */
    private String materialText;

    /** The text used to identify the height of a layer */
    private String heightText;
    private GuiCreateFlatWorld.Details createFlatWorldListSlotGui;

    /** The (unused and permenantly hidden) add layer button */
    private ButtonWidget addLayerButton;

    /** The (unused and permenantly hidden) edit layer button */
    private ButtonWidget editLayerButton;

    /** The remove layer button */
    private ButtonWidget removeLayerButton;

    public GuiCreateFlatWorld(GuiCreateWorld createWorldGuiIn, String preset)
    {
        createWorldGui = createWorldGuiIn;
        setPreset(preset);
    }

    /**
     * Gets the superflat preset in the text format described on the Superflat article on the Minecraft Wiki
     */
    public String getPreset()
    {
        return theFlatGeneratorInfo.toString();
    }

    /**
     * Sets the superflat preset. Invalid or null values will result in the default superflat preset being used.
     */
    public void setPreset(String preset)
    {
        theFlatGeneratorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(preset);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        widgets.clear();
        flatWorldTitle = I18n.format("createWorld.customize.flat.title");
        materialText = I18n.format("createWorld.customize.flat.tile");
        heightText = I18n.format("createWorld.customize.flat.height");
        createFlatWorldListSlotGui = new GuiCreateFlatWorld.Details();
        addLayerButton = addButton(new ButtonWidget(2, width / 2 - 154, height - 52, 100, 20, I18n.format("createWorld.customize.flat.addLayer") + " (NYI)"));
        editLayerButton = addButton(new ButtonWidget(3, width / 2 - 50, height - 52, 100, 20, I18n.format("createWorld.customize.flat.editLayer") + " (NYI)"));
        removeLayerButton = addButton(new ButtonWidget(4, width / 2 - 155, height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer")));
        widgets.add(new ButtonWidget(0, width / 2 - 155, height - 28, 150, 20, I18n.format("gui.done")));
        widgets.add(new ButtonWidget(5, width / 2 + 5, height - 52, 150, 20, I18n.format("createWorld.customize.presets")));
        widgets.add(new ButtonWidget(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
        addLayerButton.visible = false;
        editLayerButton.visible = false;
        theFlatGeneratorInfo.updateLayers();
        onLayersChanged();
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
        createFlatWorldListSlotGui.handleMouseInput();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        int i = theFlatGeneratorInfo.getFlatLayers().size() - createFlatWorldListSlotGui.selectedLayer - 1;

        if (button.id == 1)
        {
            Minecraft.openScreen(createWorldGui);
        }
        else if (button.id == 0)
        {
            createWorldGui.chunkProviderSettingsJson = getPreset();
            Minecraft.openScreen(createWorldGui);
        }
        else if (button.id == 5)
        {
            Minecraft.openScreen(new GuiFlatPresets(this));
        }
        else if (button.id == 4 && hasSelectedLayer())
        {
            theFlatGeneratorInfo.getFlatLayers().remove(i);
            createFlatWorldListSlotGui.selectedLayer = Math.min(createFlatWorldListSlotGui.selectedLayer, theFlatGeneratorInfo.getFlatLayers().size() - 1);
        }

        theFlatGeneratorInfo.updateLayers();
        onLayersChanged();
    }

    /**
     * Would update whether or not the edit and remove buttons are enabled, but is currently disabled and always
     * disables the buttons (which are invisible anyways)
     */
    public void onLayersChanged()
    {
        boolean flag = hasSelectedLayer();
        removeLayerButton.enabled = flag;
        editLayerButton.enabled = flag;
        editLayerButton.enabled = false;
        addLayerButton.enabled = false;
    }

    /**
     * Returns whether there is a valid layer selection
     */
    private boolean hasSelectedLayer()
    {
        return createFlatWorldListSlotGui.selectedLayer > -1 && createFlatWorldListSlotGui.selectedLayer < theFlatGeneratorInfo.getFlatLayers().size();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        createFlatWorldListSlotGui.drawScreen(mouseX, mouseY, partialTick);
        drawCenteredString(font, flatWorldTitle, width / 2, 8, 16777215);
        int i = width / 2 - 92 - 16;
        drawString(font, materialText, i, 32, 16777215);
        drawString(font, heightText, i + 2 + 213 - font.getStringWidth(heightText), 32, 16777215);
        super.draw(mouseX, mouseY, partialTick);
    }

    class Details extends GuiSlot
    {
        public int selectedLayer = -1;

        public Details()
        {
            super(minecraft, GuiCreateFlatWorld.this.width, GuiCreateFlatWorld.this.height, 43, GuiCreateFlatWorld.this.height - 60, 24);
        }

        private void drawItem(int x, int z, ItemStack itemToDraw)
        {
            drawItemBackground(x + 1, z + 1);
            GlStateManager.enableRescaleNormal();

            if (!itemToDraw.isEmpty())
            {
                RenderHelper.enableGUIStandardItemLighting();
                renderItem.renderItemIntoGUI(itemToDraw, x + 2, z + 2);
                RenderHelper.disableStandardItemLighting();
            }

            GlStateManager.disableRescaleNormal();
        }

        private void drawItemBackground(int x, int y)
        {
            drawItemBackground(x, y, 0, 0);
        }

        private void drawItemBackground(int x, int z, int textureX, int textureY)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getTextureManager().bindTexture(Gui.STAT_ICONS);
            float f = 0.0078125F;
            float f1 = 0.0078125F;
            int i = 18;
            int j = 18;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(x + 0, z + 18, zLevel).tex((float)(textureX + 0) * 0.0078125F, (float)(textureY + 18) * 0.0078125F).endVertex();
            bufferbuilder.pos(x + 18, z + 18, zLevel).tex((float)(textureX + 18) * 0.0078125F, (float)(textureY + 18) * 0.0078125F).endVertex();
            bufferbuilder.pos(x + 18, z + 0, zLevel).tex((float)(textureX + 18) * 0.0078125F, (float)(textureY + 0) * 0.0078125F).endVertex();
            bufferbuilder.pos(x + 0, z + 0, zLevel).tex((float)(textureX + 0) * 0.0078125F, (float)(textureY + 0) * 0.0078125F).endVertex();
            tessellator.draw();
        }

        protected int getSize()
        {
            return theFlatGeneratorInfo.getFlatLayers().size();
        }

        protected void elementClicked(int i, boolean doubleClick, int mouseX, int mouseY)
        {
            selectedLayer = i;
            onLayersChanged();
        }

        protected boolean isSelected(int slotIndex)
        {
            return slotIndex == selectedLayer;
        }

        protected void drawBackground()
        {
        }

        protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_)
        {
            FlatLayerInfo flatlayerinfo = theFlatGeneratorInfo.getFlatLayers().get(theFlatGeneratorInfo.getFlatLayers().size() - p_192637_1_ - 1);
            IBlockState iblockstate = flatlayerinfo.getLayerMaterial();
            Block block = iblockstate.getBlock();
            Item item = Item.getItemFromBlock(block);

            if (item == Items.field_190931_a)
            {
                if (block != Blocks.WATER && block != Blocks.FLOWING_WATER)
                {
                    if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA)
                    {
                        item = Items.LAVA_BUCKET;
                    }
                }
                else
                {
                    item = Items.WATER_BUCKET;
                }
            }

            ItemStack itemstack = new ItemStack(item, 1, item.getHasSubtypes() ? block.getMetaFromState(iblockstate) : 0);
            String s = item.getItemStackDisplayName(itemstack);
            drawItem(p_192637_2_, p_192637_3_, itemstack);
            font.drawString(s, p_192637_2_ + 18 + 5, p_192637_3_ + 3, 16777215);
            String s1;

            if (p_192637_1_ == 0)
            {
                s1 = I18n.format("createWorld.customize.flat.layer.top", flatlayerinfo.getLayerCount());
            }
            else if (p_192637_1_ == theFlatGeneratorInfo.getFlatLayers().size() - 1)
            {
                s1 = I18n.format("createWorld.customize.flat.layer.bottom", flatlayerinfo.getLayerCount());
            }
            else
            {
                s1 = I18n.format("createWorld.customize.flat.layer", flatlayerinfo.getLayerCount());
            }

            font.drawString(s1, p_192637_2_ + 2 + 213 - font.getStringWidth(s1), p_192637_3_ + 3, 16777215);
        }

        protected int getScrollBarX()
        {
            return width - 70;
        }
    }
}
