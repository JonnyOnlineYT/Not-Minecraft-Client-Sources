package net.minecraft.client.gui.inventory;

import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public abstract class GuiContainer extends GuiScreen {
   public static final ResourceLocation INVENTORY_BACKGROUND = new ResourceLocation("textures/gui/container/inventory.png");
   protected int xSize = 176;
   protected int ySize = 166;
   public Container inventorySlots;
   protected int guiLeft;
   protected int guiTop;
   private Slot theSlot;
   private Slot clickedSlot;
   private boolean isRightMouseClick;
   private ItemStack draggedStack;
   private int touchUpX;
   private int touchUpY;
   private Slot returningStackDestSlot;
   private long returningStackTime;
   private ItemStack returningStack;
   private Slot currentDragTargetSlot;
   private long dragItemDropDelay;
   protected final Set<Slot> dragSplittingSlots;
   protected boolean dragSplitting;
   private int dragSplittingLimit;
   private int dragSplittingButton;
   private boolean ignoreMouseUp;
   private int dragSplittingRemnant;
   private long lastClickTime;
   private Slot lastClickSlot;
   private int lastClickButton;
   private boolean doubleClick;
   private ItemStack shiftClickedSlot;

   public GuiContainer(Container inventorySlotsIn) {
      this.draggedStack = ItemStack.field_190927_a;
      this.returningStack = ItemStack.field_190927_a;
      this.dragSplittingSlots = Sets.newHashSet();
      this.shiftClickedSlot = ItemStack.field_190927_a;
      this.inventorySlots = inventorySlotsIn;
      this.ignoreMouseUp = true;
   }

   public void initGui() {
      super.initGui();
      this.mc.player.openContainer = this.inventorySlots;
      this.guiLeft = (width - this.xSize) / 2;
      this.guiTop = (height - this.ySize) / 2;
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      int i = this.guiLeft;
      int j = this.guiTop;
      this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
      GlStateManager.disableRescaleNormal();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      super.drawScreen(mouseX, mouseY, partialTicks);
      RenderHelper.enableGUIStandardItemLighting();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)i, (float)j, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableRescaleNormal();
      this.theSlot = null;
      int k = true;
      int l = true;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

      int l2;
      for(int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
         Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i1);
         if (slot.canBeHovered()) {
            this.drawSlot(slot);
         }

         if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.canBeHovered()) {
            this.theSlot = slot;
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int j1 = slot.xDisplayPosition;
            l2 = slot.yDisplayPosition;
            GlStateManager.colorMask(true, true, true, false);
            this.drawGradientRect(j1, l2, j1 + 16, l2 + 16, -2130706433, -2130706433);
            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
         }
      }

      RenderHelper.disableStandardItemLighting();
      this.drawGuiContainerForegroundLayer(mouseX, mouseY);
      RenderHelper.enableGUIStandardItemLighting();
      InventoryPlayer inventoryplayer = this.mc.player.inventory;
      ItemStack itemstack = this.draggedStack.func_190926_b() ? inventoryplayer.getItemStack() : this.draggedStack;
      if (!itemstack.func_190926_b()) {
         int j2 = true;
         l2 = this.draggedStack.func_190926_b() ? 8 : 16;
         String s = null;
         if (!this.draggedStack.func_190926_b() && this.isRightMouseClick) {
            itemstack = itemstack.copy();
            itemstack.func_190920_e(MathHelper.ceil((float)itemstack.func_190916_E() / 2.0F));
         } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
            itemstack = itemstack.copy();
            itemstack.func_190920_e(this.dragSplittingRemnant);
            if (itemstack.func_190926_b()) {
               s = "" + TextFormatting.YELLOW + "0";
            }
         }

         this.drawItemStack(itemstack, mouseX - i - 8, mouseY - j - l2, s);
      }

      if (!this.returningStack.func_190926_b()) {
         float f = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;
         if (f >= 1.0F) {
            f = 1.0F;
            this.returningStack = ItemStack.field_190927_a;
         }

         l2 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
         int i3 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
         int l1 = this.touchUpX + (int)((float)l2 * f);
         int i2 = this.touchUpY + (int)((float)i3 * f);
         this.drawItemStack(this.returningStack, l1, i2, (String)null);
      }

      GlStateManager.popMatrix();
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      RenderHelper.enableStandardItemLighting();
   }

   protected void func_191948_b(int p_191948_1_, int p_191948_2_) {
      if (this.mc.player.inventory.getItemStack().func_190926_b() && this.theSlot != null && this.theSlot.getHasStack()) {
         this.renderToolTip(this.theSlot.getStack(), p_191948_1_, p_191948_2_);
      }

   }

   private void drawItemStack(ItemStack stack, int x, int y, String altText) {
      GlStateManager.translate(0.0F, 0.0F, 32.0F);
      this.zLevel = 200.0F;
      this.itemRender.zLevel = 200.0F;
      this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
      this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, stack, x, y - (this.draggedStack.func_190926_b() ? 0 : 8), altText);
      this.zLevel = 0.0F;
      this.itemRender.zLevel = 0.0F;
   }

   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
   }

   protected abstract void drawGuiContainerBackgroundLayer(float var1, int var2, int var3);

   private void drawSlot(Slot slotIn) {
      int i = slotIn.xDisplayPosition;
      int j = slotIn.yDisplayPosition;
      ItemStack itemstack = slotIn.getStack();
      boolean flag = false;
      boolean flag1 = slotIn == this.clickedSlot && !this.draggedStack.func_190926_b() && !this.isRightMouseClick;
      ItemStack itemstack1 = this.mc.player.inventory.getItemStack();
      String s = null;
      if (slotIn == this.clickedSlot && !this.draggedStack.func_190926_b() && this.isRightMouseClick && !itemstack.func_190926_b()) {
         itemstack = itemstack.copy();
         itemstack.func_190920_e(itemstack.func_190916_E() / 2);
      } else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.func_190926_b()) {
         if (this.dragSplittingSlots.size() == 1) {
            return;
         }

         if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
            itemstack = itemstack1.copy();
            flag = true;
            Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack().func_190926_b() ? 0 : slotIn.getStack().func_190916_E());
            int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));
            if (itemstack.func_190916_E() > k) {
               s = TextFormatting.YELLOW.toString() + k;
               itemstack.func_190920_e(k);
            }
         } else {
            this.dragSplittingSlots.remove(slotIn);
            this.updateDragSplitting();
         }
      }

      this.zLevel = 100.0F;
      this.itemRender.zLevel = 100.0F;
      if (itemstack.func_190926_b() && slotIn.canBeHovered()) {
         String s1 = slotIn.getSlotTexture();
         if (s1 != null) {
            TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite(s1);
            GlStateManager.disableLighting();
            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
            GlStateManager.enableLighting();
            flag1 = true;
         }
      }

      if (!flag1) {
         if (flag) {
            drawRect((double)i, (double)j, (double)(i + 16), (double)(j + 16), -2130706433);
         }

         GlStateManager.enableDepth();
         this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
         this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, itemstack, i, j, s);
      }

      this.itemRender.zLevel = 0.0F;
      this.zLevel = 0.0F;
   }

   private void updateDragSplitting() {
      ItemStack itemstack = this.mc.player.inventory.getItemStack();
      if (!itemstack.func_190926_b() && this.dragSplitting) {
         if (this.dragSplittingLimit == 2) {
            this.dragSplittingRemnant = itemstack.getMaxStackSize();
         } else {
            this.dragSplittingRemnant = itemstack.func_190916_E();

            ItemStack itemstack1;
            int i;
            for(Iterator var2 = this.dragSplittingSlots.iterator(); var2.hasNext(); this.dragSplittingRemnant -= itemstack1.func_190916_E() - i) {
               Slot slot = (Slot)var2.next();
               itemstack1 = itemstack.copy();
               ItemStack itemstack2 = slot.getStack();
               i = itemstack2.func_190926_b() ? 0 : itemstack2.func_190916_E();
               Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
               int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));
               if (itemstack1.func_190916_E() > j) {
                  itemstack1.func_190920_e(j);
               }
            }
         }
      }

   }

   private Slot getSlotAtPosition(int x, int y) {
      for(int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i) {
         Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i);
         if (this.isMouseOverSlot(slot, x, y) && slot.canBeHovered()) {
            return slot;
         }
      }

      return null;
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      boolean flag = mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
      Slot slot = this.getSlotAtPosition(mouseX, mouseY);
      long i = Minecraft.getSystemTime();
      this.doubleClick = this.lastClickSlot == slot && i - this.lastClickTime < 250L && this.lastClickButton == mouseButton;
      this.ignoreMouseUp = false;
      if (mouseButton == 0 || mouseButton == 1 || flag) {
         int j = this.guiLeft;
         int k = this.guiTop;
         boolean flag1 = this.func_193983_c(mouseX, mouseY, j, k);
         int l = -1;
         if (slot != null) {
            l = slot.slotNumber;
         }

         if (flag1) {
            l = -999;
         }

         if (this.mc.gameSettings.touchscreen && flag1 && this.mc.player.inventory.getItemStack().func_190926_b()) {
            this.mc.displayGuiScreen((GuiScreen)null);
            return;
         }

         if (l != -1) {
            if (this.mc.gameSettings.touchscreen) {
               if (slot != null && slot.getHasStack()) {
                  this.clickedSlot = slot;
                  this.draggedStack = ItemStack.field_190927_a;
                  this.isRightMouseClick = mouseButton == 1;
               } else {
                  this.clickedSlot = null;
               }
            } else if (!this.dragSplitting) {
               if (this.mc.player.inventory.getItemStack().func_190926_b()) {
                  if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                     this.handleMouseClick(slot, l, mouseButton, ClickType.CLONE);
                  } else {
                     boolean flag2 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                     ClickType clicktype = ClickType.PICKUP;
                     if (flag2) {
                        this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.field_190927_a;
                        clicktype = ClickType.QUICK_MOVE;
                     } else if (l == -999) {
                        clicktype = ClickType.THROW;
                     }

                     this.handleMouseClick(slot, l, mouseButton, clicktype);
                  }

                  this.ignoreMouseUp = true;
               } else {
                  this.dragSplitting = true;
                  this.dragSplittingButton = mouseButton;
                  this.dragSplittingSlots.clear();
                  if (mouseButton == 0) {
                     this.dragSplittingLimit = 0;
                  } else if (mouseButton == 1) {
                     this.dragSplittingLimit = 1;
                  } else if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                     this.dragSplittingLimit = 2;
                  }
               }
            }
         }
      }

      this.lastClickSlot = slot;
      this.lastClickTime = i;
      this.lastClickButton = mouseButton;
   }

   protected boolean func_193983_c(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_) {
      return p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
   }

   protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      Slot slot = this.getSlotAtPosition(mouseX, mouseY);
      ItemStack itemstack = this.mc.player.inventory.getItemStack();
      if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
         if (clickedMouseButton == 0 || clickedMouseButton == 1) {
            if (this.draggedStack.func_190926_b()) {
               if (slot != this.clickedSlot && !this.clickedSlot.getStack().func_190926_b()) {
                  this.draggedStack = this.clickedSlot.getStack().copy();
               }
            } else if (this.draggedStack.func_190916_E() > 1 && slot != null && Container.canAddItemToSlot(slot, this.draggedStack, false)) {
               long i = Minecraft.getSystemTime();
               if (this.currentDragTargetSlot == slot) {
                  if (i - this.dragItemDropDelay > 500L) {
                     this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                     this.handleMouseClick(slot, slot.slotNumber, 1, ClickType.PICKUP);
                     this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                     this.dragItemDropDelay = i + 750L;
                     this.draggedStack.func_190918_g(1);
                  }
               } else {
                  this.currentDragTargetSlot = slot;
                  this.dragItemDropDelay = i;
               }
            }
         }
      } else if (this.dragSplitting && slot != null && !itemstack.func_190926_b() && (itemstack.func_190916_E() > this.dragSplittingSlots.size() || this.dragSplittingLimit == 2) && Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && this.inventorySlots.canDragIntoSlot(slot)) {
         this.dragSplittingSlots.add(slot);
         this.updateDragSplitting();
      }

   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      Slot slot = this.getSlotAtPosition(mouseX, mouseY);
      int i = this.guiLeft;
      int j = this.guiTop;
      boolean flag = this.func_193983_c(mouseX, mouseY, i, j);
      int k = -1;
      if (slot != null) {
         k = slot.slotNumber;
      }

      if (flag) {
         k = -999;
      }

      Slot slot1;
      Iterator var11;
      if (this.doubleClick && slot != null && state == 0 && this.inventorySlots.canMergeSlot(ItemStack.field_190927_a, slot)) {
         if (isShiftKeyDown()) {
            if (!this.shiftClickedSlot.func_190926_b()) {
               var11 = this.inventorySlots.inventorySlots.iterator();

               while(var11.hasNext()) {
                  slot1 = (Slot)var11.next();
                  if (slot1 != null && slot1.canTakeStack(this.mc.player) && slot1.getHasStack() && slot1.inventory == slot.inventory && Container.canAddItemToSlot(slot1, this.shiftClickedSlot, true)) {
                     this.handleMouseClick(slot1, slot1.slotNumber, state, ClickType.QUICK_MOVE);
                  }
               }
            }
         } else {
            this.handleMouseClick(slot, k, state, ClickType.PICKUP_ALL);
         }

         this.doubleClick = false;
         this.lastClickTime = 0L;
      } else {
         if (this.dragSplitting && this.dragSplittingButton != state) {
            this.dragSplitting = false;
            this.dragSplittingSlots.clear();
            this.ignoreMouseUp = true;
            return;
         }

         if (this.ignoreMouseUp) {
            this.ignoreMouseUp = false;
            return;
         }

         boolean flag1;
         if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
            if (state == 0 || state == 1) {
               if (this.draggedStack.func_190926_b() && slot != this.clickedSlot) {
                  this.draggedStack = this.clickedSlot.getStack();
               }

               flag1 = Container.canAddItemToSlot(slot, this.draggedStack, false);
               if (k != -1 && !this.draggedStack.func_190926_b() && flag1) {
                  this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
                  this.handleMouseClick(slot, k, 0, ClickType.PICKUP);
                  if (this.mc.player.inventory.getItemStack().func_190926_b()) {
                     this.returningStack = ItemStack.field_190927_a;
                  } else {
                     this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
                     this.touchUpX = mouseX - i;
                     this.touchUpY = mouseY - j;
                     this.returningStackDestSlot = this.clickedSlot;
                     this.returningStack = this.draggedStack;
                     this.returningStackTime = Minecraft.getSystemTime();
                  }
               } else if (!this.draggedStack.func_190926_b()) {
                  this.touchUpX = mouseX - i;
                  this.touchUpY = mouseY - j;
                  this.returningStackDestSlot = this.clickedSlot;
                  this.returningStack = this.draggedStack;
                  this.returningStackTime = Minecraft.getSystemTime();
               }

               this.draggedStack = ItemStack.field_190927_a;
               this.clickedSlot = null;
            }
         } else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
            this.handleMouseClick((Slot)null, -999, Container.getQuickcraftMask(0, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
            var11 = this.dragSplittingSlots.iterator();

            while(var11.hasNext()) {
               slot1 = (Slot)var11.next();
               this.handleMouseClick(slot1, slot1.slotNumber, Container.getQuickcraftMask(1, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
            }

            this.handleMouseClick((Slot)null, -999, Container.getQuickcraftMask(2, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
         } else if (!this.mc.player.inventory.getItemStack().func_190926_b()) {
            if (state == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
               this.handleMouseClick(slot, k, state, ClickType.CLONE);
            } else {
               flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
               if (flag1) {
                  this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.field_190927_a;
               }

               this.handleMouseClick(slot, k, state, flag1 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
            }
         }
      }

      if (this.mc.player.inventory.getItemStack().func_190926_b()) {
         this.lastClickTime = 0L;
      }

      this.dragSplitting = false;
   }

   private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
      return this.isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
   }

   protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
      int i = this.guiLeft;
      int j = this.guiTop;
      pointX -= i;
      pointY -= j;
      return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
   }

   protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
      if (slotIn != null) {
         slotId = slotIn.slotNumber;
      }

      this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, mouseButton, type, this.mc.player);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
         this.mc.player.closeScreen();
      }

      this.checkHotbarKeys(keyCode);
      if (this.theSlot != null && this.theSlot.getHasStack()) {
         if (keyCode == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
            this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, ClickType.CLONE);
         } else if (keyCode == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
            this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
         }
      }

   }

   protected boolean checkHotbarKeys(int keyCode) {
      if (this.mc.player.inventory.getItemStack().func_190926_b() && this.theSlot != null) {
         for(int i = 0; i < 9; ++i) {
            if (keyCode == this.mc.gameSettings.keyBindsHotbar[i].getKeyCode()) {
               this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, i, ClickType.SWAP);
               return true;
            }
         }
      }

      return false;
   }

   public void onGuiClosed() {
      if (this.mc.player != null) {
         this.inventorySlots.onContainerClosed(this.mc.player);
      }

   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public void updateScreen() {
      super.updateScreen();
      if (!this.mc.player.isEntityAlive() || this.mc.player.isDead) {
         this.mc.player.closeScreen();
      }

   }
}
