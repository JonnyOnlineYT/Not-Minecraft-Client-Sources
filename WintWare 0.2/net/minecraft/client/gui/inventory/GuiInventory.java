package net.minecraft.client.gui.inventory;

import java.awt.Color;
import java.io.IOException;
import me.wintware.client.utils.animation.Stopwatch;
import me.wintware.client.utils.animation.Translate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;

public class GuiInventory extends InventoryEffectRenderer implements IRecipeShownListener {
   private float oldMouseX;
   Translate translate = new Translate(0.0F, 0.0F);
   private float oldMouseY;
   private GuiButtonImage field_192048_z;
   private final GuiRecipeBook field_192045_A = new GuiRecipeBook();
   private boolean field_192046_B;
   private boolean field_194031_B;
   public float rideHorse = 0.0F;
   private final Stopwatch animationStopwatch = new Stopwatch();
   private double hudHeight;
   public static float scaledNum;
   private int mainX;
   private int mainY;

   public GuiInventory(EntityPlayer player) {
      super(player.inventoryContainer);
      this.allowUserInput = true;
   }

   public void updateScreen() {
      if (this.mc.playerController.isInCreativeMode()) {
         this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.player));
      }

      this.field_192045_A.func_193957_d();
   }

   public void initGui() {
      scaledNum = 0.0F;
      this.rideHorse = 0.0F;
      this.buttonList.clear();
      if (this.mc.playerController.isInCreativeMode()) {
         this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.player));
      } else {
         super.initGui();
      }

      this.field_192046_B = width < 379;
      this.field_192045_A.func_194303_a(width, height, this.mc, this.field_192046_B, ((ContainerPlayer)this.inventorySlots).craftMatrix);
      this.guiLeft = this.field_192045_A.func_193011_a(this.field_192046_B, width, this.xSize);
      this.field_192048_z = new GuiButtonImage(10, this.guiLeft + 104, height / 2 - 22, 20, 18, 178, 0, 19, INVENTORY_BACKGROUND);
      this.buttonList.add(this.field_192048_z);
   }

   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      this.fontRendererObj.drawString(I18n.format("container.crafting"), 97.0F, 8.0F, 4210752);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.hasActivePotionEffects = !this.field_192045_A.func_191878_b();
      ScaledResolution sr = new ScaledResolution(this.mc);
      this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0, (new Color(0, 224, 255)).getRGB());
      ScaledResolution scaledResolution = new ScaledResolution(this.mc);
      GL11.glPushMatrix();
      GL11.glTranslatef((float)(scaledResolution.getScaledWidth() / 2), -1000.0F, 0.0F);
      GL11.glScaled(1.0D, 1.0D, 1.0D);
      GL11.glTranslatef((float)(-scaledResolution.getScaledWidth() / 2), 1000.0F, 0.0F);
      super.drawScreen(mouseX, mouseY, partialTicks);
      GL11.glPopMatrix();
      this.func_191948_b(mouseX, mouseY);
      this.field_192045_A.func_191876_c(this.guiLeft, this.guiTop, mouseX, mouseY);
      this.oldMouseX = (float)mouseX;
      this.oldMouseY = (float)mouseY;
      this.hudHeight = 0.0D;
   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
      int i = this.guiLeft;
      int j = this.guiTop;
      this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
      drawEntityOnScreen(i + 51, j + 75, 30, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.mc.player);
   }

   public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
      GlStateManager.enableColorMaterial();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)posX, (float)posY, 50.0F);
      GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      float f = ent.renderYawOffset;
      float f1 = ent.rotationYaw;
      float f2 = ent.rotationPitch;
      float f3 = ent.prevRotationYawHead;
      float f4 = ent.rotationYawHead;
      GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
      ent.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
      ent.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
      ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
      ent.rotationYawHead = ent.rotationYaw;
      ent.prevRotationYawHead = ent.rotationYaw;
      GlStateManager.translate(0.0F, 0.0F, 0.0F);
      RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
      rendermanager.setPlayerViewY(180.0F);
      rendermanager.setRenderShadow(false);
      rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
      rendermanager.setRenderShadow(true);
      ent.renderYawOffset = f;
      ent.rotationYaw = f1;
      ent.rotationPitch = f2;
      ent.prevRotationYawHead = f3;
      ent.rotationYawHead = f4;
      GlStateManager.popMatrix();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableRescaleNormal();
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
      return (!this.field_192046_B || !this.field_192045_A.func_191878_b()) && super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (!this.field_192045_A.func_191862_a(mouseX, mouseY, mouseButton) && (!this.field_192046_B || !this.field_192045_A.func_191878_b())) {
         super.mouseClicked(mouseX, mouseY, mouseButton);
      }

   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      if (this.field_194031_B) {
         this.field_194031_B = false;
      } else {
         super.mouseReleased(mouseX, mouseY, state);
      }

   }

   protected boolean func_193983_c(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_) {
      boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
      return this.field_192045_A.func_193955_c(p_193983_1_, p_193983_2_, this.guiLeft, this.guiTop, this.xSize, this.ySize) && flag;
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.id == 10) {
         this.field_192045_A.func_193014_a(this.field_192046_B, ((ContainerPlayer)this.inventorySlots).craftMatrix);
         this.field_192045_A.func_191866_a();
         this.guiLeft = this.field_192045_A.func_193011_a(this.field_192046_B, width, this.xSize);
         this.field_192048_z.func_191746_c(this.guiLeft + 104, height / 2 - 22);
         this.field_194031_B = true;
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (!this.field_192045_A.func_191859_a(typedChar, keyCode)) {
         super.keyTyped(typedChar, keyCode);
      }

   }

   protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
      super.handleMouseClick(slotIn, slotId, mouseButton, type);
      this.field_192045_A.func_191874_a(slotIn);
   }

   public void func_192043_J_() {
      this.field_192045_A.func_193948_e();
   }

   public void onGuiClosed() {
      this.field_192045_A.func_191871_c();
      super.onGuiClosed();
   }

   public GuiRecipeBook func_194310_f() {
      return this.field_192045_A;
   }
}
