package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import org.lwjgl.input.Keyboard;

public class GuiScreenCustomizePresets extends GuiScreen {
   private static final List<GuiScreenCustomizePresets.Info> PRESETS = Lists.newArrayList();
   private GuiScreenCustomizePresets.ListPreset list;
   private GuiButton select;
   private GuiTextField export;
   private final GuiCustomizeWorldScreen parent;
   protected String title = "Customize World Presets";
   private String shareText;
   private String listText;

   public GuiScreenCustomizePresets(GuiCustomizeWorldScreen p_i45524_1_) {
      this.parent = p_i45524_1_;
   }

   public void initGui() {
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      this.title = I18n.format("createWorld.customize.custom.presets.title");
      this.shareText = I18n.format("createWorld.customize.presets.share");
      this.listText = I18n.format("createWorld.customize.presets.list");
      this.export = new GuiTextField(2, this.fontRendererObj, 50, 40, width - 100, 20);
      this.list = new GuiScreenCustomizePresets.ListPreset();
      this.export.setMaxStringLength(2000);
      this.export.setText(this.parent.saveValues());
      this.select = this.addButton(new GuiButton(0, width / 2 - 102, height - 27, 100, 20, I18n.format("createWorld.customize.presets.select")));
      this.buttonList.add(new GuiButton(1, width / 2 + 3, height - 27, 100, 20, I18n.format("gui.cancel")));
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
      switch(button.id) {
      case 0:
         this.parent.loadValues(this.export.getText());
         this.mc.displayGuiScreen(this.parent);
         break;
      case 1:
         this.mc.displayGuiScreen(this.parent);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.list.drawScreen(mouseX, mouseY, partialTicks);
      drawCenteredString(this.fontRendererObj, this.title, width / 2, 8, 16777215);
      this.drawString(this.fontRendererObj, this.shareText, 50, 30, 10526880);
      this.drawString(this.fontRendererObj, this.listText, 50, 70, 10526880);
      this.export.drawTextBox();
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void updateScreen() {
      this.export.updateCursorCounter();
      super.updateScreen();
   }

   public void updateButtonValidity() {
      this.select.enabled = this.hasValidSelection();
   }

   private boolean hasValidSelection() {
      return this.list.selected > -1 && this.list.selected < PRESETS.size() || this.export.getText().length() > 1;
   }

   static {
      ChunkGeneratorSettings.Factory chunkgeneratorsettings$factory = ChunkGeneratorSettings.Factory.jsonToFactory("{ \"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":5000.0, \"mainNoiseScaleY\":1000.0, \"mainNoiseScaleZ\":5000.0, \"baseSize\":8.5, \"stretchY\":8.0, \"biomeDepthWeight\":2.0, \"biomeDepthOffset\":0.5, \"biomeScaleWeight\":2.0, \"biomeScaleOffset\":0.375, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":255 }");
      ResourceLocation resourcelocation = new ResourceLocation("textures/gui/presets/water.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.waterWorld"), resourcelocation, chunkgeneratorsettings$factory));
      chunkgeneratorsettings$factory = ChunkGeneratorSettings.Factory.jsonToFactory("{\"coordinateScale\":3000.0, \"heightScale\":6000.0, \"upperLimitScale\":250.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":10.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }");
      resourcelocation = new ResourceLocation("textures/gui/presets/isles.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.isleLand"), resourcelocation, chunkgeneratorsettings$factory));
      chunkgeneratorsettings$factory = ChunkGeneratorSettings.Factory.jsonToFactory("{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":5000.0, \"mainNoiseScaleY\":1000.0, \"mainNoiseScaleZ\":5000.0, \"baseSize\":8.5, \"stretchY\":5.0, \"biomeDepthWeight\":2.0, \"biomeDepthOffset\":1.0, \"biomeScaleWeight\":4.0, \"biomeScaleOffset\":1.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }");
      resourcelocation = new ResourceLocation("textures/gui/presets/delight.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.caveDelight"), resourcelocation, chunkgeneratorsettings$factory));
      chunkgeneratorsettings$factory = ChunkGeneratorSettings.Factory.jsonToFactory("{\"coordinateScale\":738.41864, \"heightScale\":157.69133, \"upperLimitScale\":801.4267, \"lowerLimitScale\":1254.1643, \"depthNoiseScaleX\":374.93652, \"depthNoiseScaleZ\":288.65228, \"depthNoiseScaleExponent\":1.2092624, \"mainNoiseScaleX\":1355.9908, \"mainNoiseScaleY\":745.5343, \"mainNoiseScaleZ\":1183.464, \"baseSize\":1.8758626, \"stretchY\":1.7137525, \"biomeDepthWeight\":1.7553768, \"biomeDepthOffset\":3.4701107, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":2.535211, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }");
      resourcelocation = new ResourceLocation("textures/gui/presets/madness.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.mountains"), resourcelocation, chunkgeneratorsettings$factory));
      chunkgeneratorsettings$factory = ChunkGeneratorSettings.Factory.jsonToFactory("{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":1000.0, \"mainNoiseScaleY\":3000.0, \"mainNoiseScaleZ\":1000.0, \"baseSize\":8.5, \"stretchY\":10.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":20 }");
      resourcelocation = new ResourceLocation("textures/gui/presets/drought.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.drought"), resourcelocation, chunkgeneratorsettings$factory));
      chunkgeneratorsettings$factory = ChunkGeneratorSettings.Factory.jsonToFactory("{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":2.0, \"lowerLimitScale\":64.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":12.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":6 }");
      resourcelocation = new ResourceLocation("textures/gui/presets/chaos.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.caveChaos"), resourcelocation, chunkgeneratorsettings$factory));
      chunkgeneratorsettings$factory = ChunkGeneratorSettings.Factory.jsonToFactory("{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":12.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":true, \"seaLevel\":40 }");
      resourcelocation = new ResourceLocation("textures/gui/presets/luck.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.goodLuck"), resourcelocation, chunkgeneratorsettings$factory));
   }

   class ListPreset extends GuiSlot {
      public int selected = -1;

      public ListPreset() {
         super(GuiScreenCustomizePresets.this.mc, GuiScreenCustomizePresets.width, GuiScreenCustomizePresets.height, 80, GuiScreenCustomizePresets.height - 32, 38);
      }

      protected int getSize() {
         return GuiScreenCustomizePresets.PRESETS.size();
      }

      protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
         this.selected = slotIndex;
         GuiScreenCustomizePresets.this.updateButtonValidity();
         GuiScreenCustomizePresets.this.export.setText(((GuiScreenCustomizePresets.Info)GuiScreenCustomizePresets.PRESETS.get(GuiScreenCustomizePresets.this.list.selected)).settings.toString());
      }

      protected boolean isSelected(int slotIndex) {
         return slotIndex == this.selected;
      }

      protected void drawBackground() {
      }

      private void blitIcon(int p_178051_1_, int p_178051_2_, ResourceLocation p_178051_3_) {
         int i = p_178051_1_ + 5;
         GuiScreenCustomizePresets var10000 = GuiScreenCustomizePresets.this;
         GuiScreenCustomizePresets.drawHorizontalLine(i - 1, i + 32, p_178051_2_ - 1, -2039584);
         var10000 = GuiScreenCustomizePresets.this;
         GuiScreenCustomizePresets.drawHorizontalLine(i - 1, i + 32, p_178051_2_ + 32, -6250336);
         var10000 = GuiScreenCustomizePresets.this;
         GuiScreenCustomizePresets.drawVerticalLine(i - 1, p_178051_2_ - 1, p_178051_2_ + 32, -2039584);
         var10000 = GuiScreenCustomizePresets.this;
         GuiScreenCustomizePresets.drawVerticalLine(i + 32, p_178051_2_ - 1, p_178051_2_ + 32, -6250336);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(p_178051_3_);
         int j = true;
         int k = true;
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
         bufferbuilder.pos((double)(i + 0), (double)(p_178051_2_ + 32), 0.0D).tex(0.0D, 1.0D).endVertex();
         bufferbuilder.pos((double)(i + 32), (double)(p_178051_2_ + 32), 0.0D).tex(1.0D, 1.0D).endVertex();
         bufferbuilder.pos((double)(i + 32), (double)(p_178051_2_ + 0), 0.0D).tex(1.0D, 0.0D).endVertex();
         bufferbuilder.pos((double)(i + 0), (double)(p_178051_2_ + 0), 0.0D).tex(0.0D, 0.0D).endVertex();
         tessellator.draw();
      }

      protected void drawSlot(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
         GuiScreenCustomizePresets.Info guiscreencustomizepresets$info = (GuiScreenCustomizePresets.Info)GuiScreenCustomizePresets.PRESETS.get(p_192637_1_);
         this.blitIcon(p_192637_2_, p_192637_3_, guiscreencustomizepresets$info.texture);
         GuiScreenCustomizePresets.this.fontRendererObj.drawString(guiscreencustomizepresets$info.name, (float)(p_192637_2_ + 32 + 10), (float)(p_192637_3_ + 14), 16777215);
      }
   }

   static class Info {
      public String name;
      public ResourceLocation texture;
      public ChunkGeneratorSettings.Factory settings;

      public Info(String nameIn, ResourceLocation textureIn, ChunkGeneratorSettings.Factory settingsIn) {
         this.name = nameIn;
         this.texture = textureIn;
         this.settings = settingsIn;
      }
   }
}
