package net.minecraft.client.gui.inventory;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiShulkerBox extends GuiContainer {
   private static final ResourceLocation field_190778_u = new ResourceLocation("textures/gui/container/shulker_box.png");
   private final IInventory field_190779_v;
   private final InventoryPlayer field_190780_w;
   double scalling;

   public GuiShulkerBox(InventoryPlayer p_i47233_1_, IInventory p_i47233_2_) {
      super(new ContainerShulkerBox(p_i47233_1_, p_i47233_2_, Minecraft.getMinecraft().player));
      this.field_190780_w = p_i47233_1_;
      this.scalling = 0.0D;
      this.field_190779_v = p_i47233_2_;
      ++this.ySize;
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      ScaledResolution sr = new ScaledResolution(this.mc);
      this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0, (new Color(0, 224, 255)).getRGB());
      this.drawDefaultBackground();
      super.drawScreen(mouseX, mouseY, partialTicks);
      GL11.glPopMatrix();
      this.func_191948_b(mouseX, mouseY);
   }

   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      this.fontRendererObj.drawStringWithShadow(this.field_190779_v.getDisplayName().getUnformattedText(), 8.0F, 6.0F, 4210752);
      this.fontRendererObj.drawStringWithShadow(this.field_190780_w.getDisplayName().getUnformattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(field_190778_u);
      int i = (width - this.xSize) / 2;
      int j = (height - this.ySize) / 2;
      this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
   }
}
