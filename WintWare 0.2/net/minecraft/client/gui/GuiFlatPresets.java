package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockTallGrass;
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

public class GuiFlatPresets extends GuiScreen {
   private static final List<GuiFlatPresets.LayerItem> FLAT_WORLD_PRESETS = Lists.newArrayList();
   private final GuiCreateFlatWorld parentScreen;
   private String presetsTitle;
   private String presetsShare;
   private String listText;
   private GuiFlatPresets.ListSlot list;
   private GuiButton btnSelect;
   private GuiTextField export;

   public GuiFlatPresets(GuiCreateFlatWorld p_i46318_1_) {
      this.parentScreen = p_i46318_1_;
   }

   public void initGui() {
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      this.presetsTitle = I18n.format("createWorld.customize.presets.title");
      this.presetsShare = I18n.format("createWorld.customize.presets.share");
      this.listText = I18n.format("createWorld.customize.presets.list");
      this.export = new GuiTextField(2, this.fontRendererObj, 50, 40, width - 100, 20);
      this.list = new GuiFlatPresets.ListSlot();
      this.export.setMaxStringLength(1230);
      this.export.setText(this.parentScreen.getPreset());
      this.btnSelect = this.addButton(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format("createWorld.customize.presets.select")));
      this.buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
      this.updateButtonValidity();
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.list.handleMouseInput();
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.export.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (!this.export.textboxKeyTyped(typedChar, keyCode)) {
         super.keyTyped(typedChar, keyCode);
      }

   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.id == 0 && this.hasValidSelection()) {
         this.parentScreen.setPreset(this.export.getText());
         this.mc.displayGuiScreen(this.parentScreen);
      } else if (button.id == 1) {
         this.mc.displayGuiScreen(this.parentScreen);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.list.drawScreen(mouseX, mouseY, partialTicks);
      drawCenteredString(this.fontRendererObj, this.presetsTitle, width / 2, 8, 16777215);
      this.drawString(this.fontRendererObj, this.presetsShare, 50, 30, 10526880);
      this.drawString(this.fontRendererObj, this.listText, 50, 70, 10526880);
      this.export.drawTextBox();
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void updateScreen() {
      this.export.updateCursorCounter();
      super.updateScreen();
   }

   public void updateButtonValidity() {
      this.btnSelect.enabled = this.hasValidSelection();
   }

   private boolean hasValidSelection() {
      return this.list.selected > -1 && this.list.selected < FLAT_WORLD_PRESETS.size() || this.export.getText().length() > 1;
   }

   private static void registerPreset(String name, Item icon, Biome biome, List<String> features, FlatLayerInfo... layers) {
      registerPreset(name, icon, 0, biome, features, layers);
   }

   private static void registerPreset(String name, Item icon, int iconMetadata, Biome biome, List<String> features, FlatLayerInfo... layers) {
      FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();

      for(int i = layers.length - 1; i >= 0; --i) {
         flatgeneratorinfo.getFlatLayers().add(layers[i]);
      }

      flatgeneratorinfo.setBiome(Biome.getIdForBiome(biome));
      flatgeneratorinfo.updateLayers();
      Iterator var9 = features.iterator();

      while(var9.hasNext()) {
         String s = (String)var9.next();
         flatgeneratorinfo.getWorldFeatures().put(s, Maps.newHashMap());
      }

      FLAT_WORLD_PRESETS.add(new GuiFlatPresets.LayerItem(icon, iconMetadata, name, flatgeneratorinfo.toString()));
   }

   static {
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

   class ListSlot extends GuiSlot {
      public int selected = -1;

      public ListSlot() {
         super(GuiFlatPresets.this.mc, GuiFlatPresets.width, GuiFlatPresets.height, 80, GuiFlatPresets.height - 37, 24);
      }

      private void renderIcon(int p_178054_1_, int p_178054_2_, Item icon, int iconMetadata) {
         this.blitSlotBg(p_178054_1_ + 1, p_178054_2_ + 1);
         GlStateManager.enableRescaleNormal();
         RenderHelper.enableGUIStandardItemLighting();
         GuiFlatPresets.this.itemRender.renderItemIntoGUI(new ItemStack(icon, 1, icon.getHasSubtypes() ? iconMetadata : 0), p_178054_1_ + 2, p_178054_2_ + 2);
         RenderHelper.disableStandardItemLighting();
         GlStateManager.disableRescaleNormal();
      }

      private void blitSlotBg(int p_148173_1_, int p_148173_2_) {
         this.blitSlotIcon(p_148173_1_, p_148173_2_, 0, 0);
      }

      private void blitSlotIcon(int p_148171_1_, int p_148171_2_, int p_148171_3_, int p_148171_4_) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(Gui.STAT_ICONS);
         float f = 0.0078125F;
         float f1 = 0.0078125F;
         int i = true;
         int j = true;
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
         bufferbuilder.pos((double)(p_148171_1_ + 0), (double)(p_148171_2_ + 18), (double)GuiFlatPresets.this.zLevel).tex((double)((float)(p_148171_3_ + 0) * 0.0078125F), (double)((float)(p_148171_4_ + 18) * 0.0078125F)).endVertex();
         bufferbuilder.pos((double)(p_148171_1_ + 18), (double)(p_148171_2_ + 18), (double)GuiFlatPresets.this.zLevel).tex((double)((float)(p_148171_3_ + 18) * 0.0078125F), (double)((float)(p_148171_4_ + 18) * 0.0078125F)).endVertex();
         bufferbuilder.pos((double)(p_148171_1_ + 18), (double)(p_148171_2_ + 0), (double)GuiFlatPresets.this.zLevel).tex((double)((float)(p_148171_3_ + 18) * 0.0078125F), (double)((float)(p_148171_4_ + 0) * 0.0078125F)).endVertex();
         bufferbuilder.pos((double)(p_148171_1_ + 0), (double)(p_148171_2_ + 0), (double)GuiFlatPresets.this.zLevel).tex((double)((float)(p_148171_3_ + 0) * 0.0078125F), (double)((float)(p_148171_4_ + 0) * 0.0078125F)).endVertex();
         tessellator.draw();
      }

      protected int getSize() {
         return GuiFlatPresets.FLAT_WORLD_PRESETS.size();
      }

      protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
         this.selected = slotIndex;
         GuiFlatPresets.this.updateButtonValidity();
         GuiFlatPresets.this.export.setText(((GuiFlatPresets.LayerItem)GuiFlatPresets.FLAT_WORLD_PRESETS.get(GuiFlatPresets.this.list.selected)).generatorInfo);
      }

      protected boolean isSelected(int slotIndex) {
         return slotIndex == this.selected;
      }

      protected void drawBackground() {
      }

      protected void drawSlot(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
         GuiFlatPresets.LayerItem guiflatpresets$layeritem = (GuiFlatPresets.LayerItem)GuiFlatPresets.FLAT_WORLD_PRESETS.get(p_192637_1_);
         this.renderIcon(p_192637_2_, p_192637_3_, guiflatpresets$layeritem.icon, guiflatpresets$layeritem.iconMetadata);
         GuiFlatPresets.this.fontRendererObj.drawString(guiflatpresets$layeritem.name, (float)(p_192637_2_ + 18 + 5), (float)(p_192637_3_ + 6), 16777215);
      }
   }

   static class LayerItem {
      public Item icon;
      public int iconMetadata;
      public String name;
      public String generatorInfo;

      public LayerItem(Item iconIn, int iconMetadataIn, String nameIn, String generatorInfoIn) {
         this.icon = iconIn;
         this.iconMetadata = iconMetadataIn;
         this.name = nameIn;
         this.generatorInfo = generatorInfoIn;
      }
   }
}
