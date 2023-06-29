package net.minecraft.client.gui.inventory;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiChest extends GuiContainer {
   private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
   private final IInventory upperChestInventory;
   public final IInventory lowerChestInventory;
   public double scalling;
   private final int inventoryRows;

   public GuiChest(IInventory upperInv, IInventory lowerInv) {
      super(new ContainerChest(upperInv, lowerInv, Minecraft.getMinecraft().player));
      this.upperChestInventory = upperInv;
      this.lowerChestInventory = lowerInv;
      this.scalling = 0.0D;
      this.allowUserInput = false;
      int i = true;
      int j = true;
      this.inventoryRows = lowerInv.getSizeInventory() / 9;
      this.ySize = 114 + this.inventoryRows * 18;
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      ScaledResolution sr = new ScaledResolution(this.mc);
      this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0, (new Color(0, 224, 255)).getRGB());
      this.drawDefaultBackground();
      new ScaledResolution(this.mc);
      super.drawScreen(mouseX, mouseY, partialTicks);
      this.func_191948_b(mouseX, mouseY);
   }

   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      this.fontRendererObj.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8.0F, 6.0F, 4210752);
      this.fontRendererObj.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
      int i = (width - this.xSize) / 2;
      int j = (height - this.ySize) / 2;
      this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
      this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
   }
}
