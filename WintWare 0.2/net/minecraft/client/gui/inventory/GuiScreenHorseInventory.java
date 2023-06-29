package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiScreenHorseInventory extends GuiContainer {
   private static final ResourceLocation HORSE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/horse.png");
   private final IInventory playerInventory;
   private final IInventory horseInventory;
   private final AbstractHorse horseEntity;
   private float mousePosx;
   private float mousePosY;

   public GuiScreenHorseInventory(IInventory playerInv, IInventory horseInv, AbstractHorse horse) {
      super(new ContainerHorseInventory(playerInv, horseInv, horse, Minecraft.getMinecraft().player));
      this.playerInventory = playerInv;
      this.horseInventory = horseInv;
      this.horseEntity = horse;
      this.allowUserInput = false;
   }

   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      this.fontRendererObj.drawString(this.horseInventory.getDisplayName().getUnformattedText(), 8.0F, 6.0F, 4210752);
      this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(HORSE_GUI_TEXTURES);
      int i = (width - this.xSize) / 2;
      int j = (height - this.ySize) / 2;
      this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
      if (this.horseEntity instanceof AbstractChestHorse) {
         AbstractChestHorse abstractchesthorse = (AbstractChestHorse)this.horseEntity;
         if (abstractchesthorse.func_190695_dh()) {
            this.drawTexturedModalRect(i + 79, j + 17, 0, this.ySize, abstractchesthorse.func_190696_dl() * 18, 54);
         }
      }

      if (this.horseEntity.func_190685_dA()) {
         this.drawTexturedModalRect(i + 7, j + 35 - 18, 18, this.ySize + 54, 18, 18);
      }

      if (this.horseEntity.func_190677_dK()) {
         if (this.horseEntity instanceof EntityLlama) {
            this.drawTexturedModalRect(i + 7, j + 35, 36, this.ySize + 54, 18, 18);
         } else {
            this.drawTexturedModalRect(i + 7, j + 35, 0, this.ySize + 54, 18, 18);
         }
      }

      GuiInventory.drawEntityOnScreen(i + 51, j + 60, 17, (float)(i + 51) - this.mousePosx, (float)(j + 75 - 50) - this.mousePosY, this.horseEntity);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.mousePosx = (float)mouseX;
      this.mousePosY = (float)mouseY;
      super.drawScreen(mouseX, mouseY, partialTicks);
      this.func_191948_b(mouseX, mouseY);
   }
}
