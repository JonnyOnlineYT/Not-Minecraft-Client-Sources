package net.minecraft.client.gui.inventory;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiCrafting extends GuiContainer implements IRecipeShownListener {
   private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/crafting_table.png");
   private GuiButtonImage field_192049_w;
   private final GuiRecipeBook field_192050_x;
   private boolean field_193112_y;

   public GuiCrafting(InventoryPlayer playerInv, World worldIn) {
      this(playerInv, worldIn, BlockPos.ORIGIN);
   }

   public GuiCrafting(InventoryPlayer playerInv, World worldIn, BlockPos blockPosition) {
      super(new ContainerWorkbench(playerInv, worldIn, blockPosition));
      this.field_192050_x = new GuiRecipeBook();
   }

   public void initGui() {
      super.initGui();
      this.field_193112_y = width < 379;
      this.field_192050_x.func_194303_a(width, height, this.mc, this.field_193112_y, ((ContainerWorkbench)this.inventorySlots).craftMatrix);
      this.guiLeft = this.field_192050_x.func_193011_a(this.field_193112_y, width, this.xSize);
      this.field_192049_w = new GuiButtonImage(10, this.guiLeft + 5, height / 2 - 49, 20, 18, 0, 168, 19, CRAFTING_TABLE_GUI_TEXTURES);
      this.buttonList.add(this.field_192049_w);
   }

   public void updateScreen() {
      super.updateScreen();
      this.field_192050_x.func_193957_d();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      if (this.field_192050_x.func_191878_b() && this.field_193112_y) {
         this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
         this.field_192050_x.func_191861_a(mouseX, mouseY, partialTicks);
      } else {
         this.field_192050_x.func_191861_a(mouseX, mouseY, partialTicks);
         super.drawScreen(mouseX, mouseY, partialTicks);
         this.field_192050_x.func_191864_a(this.guiLeft, this.guiTop, true, partialTicks);
      }

      this.func_191948_b(mouseX, mouseY);
      this.field_192050_x.func_191876_c(this.guiLeft, this.guiTop, mouseX, mouseY);
   }

   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      this.fontRendererObj.drawString(I18n.format("container.crafting"), 28.0F, 6.0F, 4210752);
      this.fontRendererObj.drawString(I18n.format("container.inventory"), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURES);
      int i = this.guiLeft;
      int j = (height - this.ySize) / 2;
      this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
   }

   protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
      return (!this.field_193112_y || !this.field_192050_x.func_191878_b()) && super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (!this.field_192050_x.func_191862_a(mouseX, mouseY, mouseButton) && (!this.field_193112_y || !this.field_192050_x.func_191878_b())) {
         super.mouseClicked(mouseX, mouseY, mouseButton);
      }

   }

   protected boolean func_193983_c(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_) {
      boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
      return this.field_192050_x.func_193955_c(p_193983_1_, p_193983_2_, this.guiLeft, this.guiTop, this.xSize, this.ySize) && flag;
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.id == 10) {
         this.field_192050_x.func_193014_a(this.field_193112_y, ((ContainerWorkbench)this.inventorySlots).craftMatrix);
         this.field_192050_x.func_191866_a();
         this.guiLeft = this.field_192050_x.func_193011_a(this.field_193112_y, width, this.xSize);
         this.field_192049_w.func_191746_c(this.guiLeft + 5, height / 2 - 49);
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (!this.field_192050_x.func_191859_a(typedChar, keyCode)) {
         super.keyTyped(typedChar, keyCode);
      }

   }

   protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
      super.handleMouseClick(slotIn, slotId, mouseButton, type);
      this.field_192050_x.func_191874_a(slotIn);
   }

   public void func_192043_J_() {
      this.field_192050_x.func_193948_e();
   }

   public void onGuiClosed() {
      this.field_192050_x.func_191871_c();
      super.onGuiClosed();
   }

   public GuiRecipeBook func_194310_f() {
      return this.field_192050_x;
   }
}
