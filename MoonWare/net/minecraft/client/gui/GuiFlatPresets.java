package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import org.lwjgl.input.Keyboard;

public class GuiFlatPresets extends Screen
{
    private static final List<GuiFlatPresets.LayerItem> FLAT_WORLD_PRESETS = Lists.newArrayList();

    /** The parent GUI */
    private final GuiCreateFlatWorld parentScreen;
    private String presetsTitle;
    private String presetsShare;
    private String listText;
    private GuiFlatPresets.ListSlot list;
    private ButtonWidget btnSelect;
    private GuiTextField export;

    public GuiFlatPresets(GuiCreateFlatWorld p_i46318_1_)
    {
        parentScreen = p_i46318_1_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        widgets.clear();
        Keyboard.enableRepeatEvents(true);
        presetsTitle = I18n.format("createWorld.customize.presets.title");
        presetsShare = I18n.format("createWorld.customize.presets.share");
        listText = I18n.format("createWorld.customize.presets.list");
        export = new GuiTextField(2, font, 50, 40, width - 100, 20);
        list = new GuiFlatPresets.ListSlot();
        export.setMaxStringLength(1230);
        export.setText(parentScreen.getPreset());
        btnSelect = addButton(new ButtonWidget(0, width / 2 - 155, height - 28, 150, 20, I18n.format("createWorld.customize.presets.select")));
        widgets.add(new ButtonWidget(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
        updateButtonValidity();
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
        list.handleMouseInput();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        export.mouseClicked(mouseX, mouseY, button);
        super.mousePressed(mouseX, mouseY, button);
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (!export.textboxKeyTyped(c, key))
        {
            super.keyPressed(key, c);
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.id == 0 && hasValidSelection())
        {
            parentScreen.setPreset(export.getText());
            Minecraft.openScreen(parentScreen);
        }
        else if (button.id == 1)
        {
            Minecraft.openScreen(parentScreen);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        list.drawScreen(mouseX, mouseY, partialTick);
        drawCenteredString(font, presetsTitle, width / 2, 8, 16777215);
        drawString(font, presetsShare, 50, 30, 10526880);
        drawString(font, listText, 50, 70, 10526880);
        export.drawTextBox();
        super.draw(mouseX, mouseY, partialTick);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        export.update();
        super.update();
    }

    public void updateButtonValidity()
    {
        btnSelect.enabled = hasValidSelection();
    }

    private boolean hasValidSelection()
    {
        return list.selected > -1 && list.selected < FLAT_WORLD_PRESETS.size() || export.getText().length() > 1;
    }

    private static void registerPreset(String name, Item icon, Biome biome, List<String> features, FlatLayerInfo... layers)
    {
        registerPreset(name, icon, 0, biome, features, layers);
    }

    private static void registerPreset(String name, Item icon, int iconMetadata, Biome biome, List<String> features, FlatLayerInfo... layers)
    {
        FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();

        for (int i = layers.length - 1; i >= 0; --i)
        {
            flatgeneratorinfo.getFlatLayers().add(layers[i]);
        }

        flatgeneratorinfo.setBiome(Biome.getIdForBiome(biome));
        flatgeneratorinfo.updateLayers();

        for (String s : features)
        {
            flatgeneratorinfo.getWorldFeatures().put(s, Maps.newHashMap());
        }

        FLAT_WORLD_PRESETS.add(new GuiFlatPresets.LayerItem(icon, iconMetadata, name, flatgeneratorinfo.toString()));
    }

    static
    {
        registerPreset(I18n.format("createWorld.customize.preset.classic_flat"), Item.getItemFromBlock(Blocks.GRASS), Biomes.PLAINS, Arrays.asList("village"), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(2, Blocks.DIRT), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.tunnelers_dream"), Item.getItemFromBlock(Blocks.STONE), Biomes.EXTREME_HILLS, Arrays.asList("biome_1", "dungeon", "decoration", "stronghold", "mineshaft"), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(230, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.water_world"), Items.WATER_BUCKET, Biomes.DEEP_OCEAN, Arrays.asList("biome_1", "oceanmonument"), new FlatLayerInfo(90, Blocks.WATER), new FlatLayerInfo(5, Blocks.SAND), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(5, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.overworld"), Item.getItemFromBlock(Blocks.TALLGRASS), BlockTallGrass.EnumType.GRASS.getMeta(), Biomes.PLAINS, Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake"), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.snowy_kingdom"), Item.getItemFromBlock(Blocks.SNOW_LAYER), Biomes.ICE_PLAINS, Arrays.asList("village", "biome_1"), new FlatLayerInfo(1, Blocks.SNOW_LAYER), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.bottomless_pit"), Items.FEATHER, Biomes.PLAINS, Arrays.asList("village", "biome_1"), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(2, Blocks.COBBLESTONE));
        registerPreset(I18n.format("createWorld.customize.preset.desert"), Item.getItemFromBlock(Blocks.SAND), Biomes.DESERT, Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon"), new FlatLayerInfo(8, Blocks.SAND), new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.redstone_ready"), Items.REDSTONE, Biomes.DESERT, Collections.emptyList(), new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.the_void"), Item.getItemFromBlock(Blocks.BARRIER), Biomes.VOID, Arrays.asList("decoration"), new FlatLayerInfo(1, Blocks.AIR));
    }

    static class LayerItem
    {
        public Item icon;
        public int iconMetadata;
        public String name;
        public String generatorInfo;

        public LayerItem(Item iconIn, int iconMetadataIn, String nameIn, String generatorInfoIn)
        {
            icon = iconIn;
            iconMetadata = iconMetadataIn;
            name = nameIn;
            generatorInfo = generatorInfoIn;
        }
    }

    class ListSlot extends GuiSlot
    {
        public int selected = -1;

        public ListSlot()
        {
            super(minecraft, GuiFlatPresets.this.width, GuiFlatPresets.this.height, 80, GuiFlatPresets.this.height - 37, 24);
        }

        private void renderIcon(int p_178054_1_, int p_178054_2_, Item icon, int iconMetadata)
        {
            blitSlotBg(p_178054_1_ + 1, p_178054_2_ + 1);
            GlStateManager.enableRescaleNormal();
            RenderHelper.enableGUIStandardItemLighting();
            renderItem.renderItemIntoGUI(new ItemStack(icon, 1, icon.getHasSubtypes() ? iconMetadata : 0), p_178054_1_ + 2, p_178054_2_ + 2);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
        }

        private void blitSlotBg(int p_148173_1_, int p_148173_2_)
        {
            blitSlotIcon(p_148173_1_, p_148173_2_, 0, 0);
        }

        private void blitSlotIcon(int p_148171_1_, int p_148171_2_, int p_148171_3_, int p_148171_4_)
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
            bufferbuilder.pos(p_148171_1_ + 0, p_148171_2_ + 18, zLevel).tex((float)(p_148171_3_ + 0) * 0.0078125F, (float)(p_148171_4_ + 18) * 0.0078125F).endVertex();
            bufferbuilder.pos(p_148171_1_ + 18, p_148171_2_ + 18, zLevel).tex((float)(p_148171_3_ + 18) * 0.0078125F, (float)(p_148171_4_ + 18) * 0.0078125F).endVertex();
            bufferbuilder.pos(p_148171_1_ + 18, p_148171_2_ + 0, zLevel).tex((float)(p_148171_3_ + 18) * 0.0078125F, (float)(p_148171_4_ + 0) * 0.0078125F).endVertex();
            bufferbuilder.pos(p_148171_1_ + 0, p_148171_2_ + 0, zLevel).tex((float)(p_148171_3_ + 0) * 0.0078125F, (float)(p_148171_4_ + 0) * 0.0078125F).endVertex();
            tessellator.draw();
        }

        protected int getSize()
        {
            return FLAT_WORLD_PRESETS.size();
        }

        protected void elementClicked(int i, boolean doubleClick, int mouseX, int mouseY)
        {
            selected = i;
            updateButtonValidity();
            export.setText((FLAT_WORLD_PRESETS.get(list.selected)).generatorInfo);
        }

        protected boolean isSelected(int slotIndex)
        {
            return slotIndex == selected;
        }

        protected void drawBackground()
        {
        }

        protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_)
        {
            GuiFlatPresets.LayerItem guiflatpresets$layeritem = FLAT_WORLD_PRESETS.get(p_192637_1_);
            renderIcon(p_192637_2_, p_192637_3_, guiflatpresets$layeritem.icon, guiflatpresets$layeritem.iconMetadata);
            font.drawString(guiflatpresets$layeritem.name, p_192637_2_ + 18 + 5, p_192637_3_ + 6, 16777215);
        }
    }
}
