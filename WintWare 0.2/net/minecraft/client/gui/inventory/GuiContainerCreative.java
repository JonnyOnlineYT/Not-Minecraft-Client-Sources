package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiContainerCreative extends InventoryEffectRenderer {
   private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
   private static final InventoryBasic basicInventory = new InventoryBasic("tmp", true, 45);
   private static int selectedTabIndex;
   private float currentScroll;
   private boolean isScrolling;
   private boolean wasClicking;
   private GuiTextField searchField;
   private List<Slot> originalSlots;
   private Slot destroyItemSlot;
   private boolean clearSearch;
   private CreativeCrafting listener;

   public GuiContainerCreative(EntityPlayer player) {
      super(new GuiContainerCreative.ContainerCreative(player));
      player.openContainer = this.inventorySlots;
      this.allowUserInput = true;
      this.ySize = 136;
      this.xSize = 195;
   }

   public void updateScreen() {
      if (!this.mc.playerController.isInCreativeMode()) {
         this.mc.displayGuiScreen(new GuiInventory(this.mc.player));
      }

   }

   protected void handleMouseClick(@Nullable Slot slotIn, int slotId, int mouseButton, ClickType type) {
      this.clearSearch = true;
      boolean flag = type == ClickType.QUICK_MOVE;
      type = slotId == -999 && type == ClickType.PICKUP ? ClickType.THROW : type;
      ItemStack itemstack5;
      InventoryPlayer inventoryplayer;
      if (slotIn == null && selectedTabIndex != CreativeTabs.INVENTORY.getTabIndex() && type != ClickType.QUICK_CRAFT) {
         inventoryplayer = this.mc.player.inventory;
         if (!inventoryplayer.getItemStack().func_190926_b()) {
            if (mouseButton == 0) {
               this.mc.player.dropItem(inventoryplayer.getItemStack(), true);
               this.mc.playerController.sendPacketDropItem(inventoryplayer.getItemStack());
               inventoryplayer.setItemStack(ItemStack.field_190927_a);
            }

            if (mouseButton == 1) {
               itemstack5 = inventoryplayer.getItemStack().splitStack(1);
               this.mc.player.dropItem(itemstack5, true);
               this.mc.playerController.sendPacketDropItem(itemstack5);
            }
         }
      } else {
         if (slotIn != null && !slotIn.canTakeStack(this.mc.player)) {
            return;
         }

         if (slotIn == this.destroyItemSlot && flag) {
            for(int j = 0; j < this.mc.player.inventoryContainer.getInventory().size(); ++j) {
               this.mc.playerController.sendSlotPacket(ItemStack.field_190927_a, j);
            }
         } else {
            ItemStack itemstack3;
            if (selectedTabIndex == CreativeTabs.INVENTORY.getTabIndex()) {
               if (slotIn == this.destroyItemSlot) {
                  this.mc.player.inventory.setItemStack(ItemStack.field_190927_a);
               } else if (type == ClickType.THROW && slotIn != null && slotIn.getHasStack()) {
                  itemstack3 = slotIn.decrStackSize(mouseButton == 0 ? 1 : slotIn.getStack().getMaxStackSize());
                  itemstack5 = slotIn.getStack();
                  this.mc.player.dropItem(itemstack3, true);
                  this.mc.playerController.sendPacketDropItem(itemstack3);
                  this.mc.playerController.sendSlotPacket(itemstack5, ((GuiContainerCreative.CreativeSlot)slotIn).slot.slotNumber);
               } else if (type == ClickType.THROW && !this.mc.player.inventory.getItemStack().func_190926_b()) {
                  this.mc.player.dropItem(this.mc.player.inventory.getItemStack(), true);
                  this.mc.playerController.sendPacketDropItem(this.mc.player.inventory.getItemStack());
                  this.mc.player.inventory.setItemStack(ItemStack.field_190927_a);
               } else {
                  this.mc.player.inventoryContainer.slotClick(slotIn == null ? slotId : ((GuiContainerCreative.CreativeSlot)slotIn).slot.slotNumber, mouseButton, type, this.mc.player);
                  this.mc.player.inventoryContainer.detectAndSendChanges();
               }
            } else {
               ItemStack itemstack2;
               if (type != ClickType.QUICK_CRAFT && slotIn.inventory == basicInventory) {
                  inventoryplayer = this.mc.player.inventory;
                  itemstack5 = inventoryplayer.getItemStack();
                  ItemStack itemstack7 = slotIn.getStack();
                  if (type == ClickType.SWAP) {
                     if (!itemstack7.func_190926_b() && mouseButton >= 0 && mouseButton < 9) {
                        itemstack2 = itemstack7.copy();
                        itemstack2.func_190920_e(itemstack2.getMaxStackSize());
                        this.mc.player.inventory.setInventorySlotContents(mouseButton, itemstack2);
                        this.mc.player.inventoryContainer.detectAndSendChanges();
                     }

                     return;
                  }

                  if (type == ClickType.CLONE) {
                     if (inventoryplayer.getItemStack().func_190926_b() && slotIn.getHasStack()) {
                        itemstack2 = slotIn.getStack().copy();
                        itemstack2.func_190920_e(itemstack2.getMaxStackSize());
                        inventoryplayer.setItemStack(itemstack2);
                     }

                     return;
                  }

                  if (type == ClickType.THROW) {
                     if (!itemstack7.func_190926_b()) {
                        itemstack2 = itemstack7.copy();
                        itemstack2.func_190920_e(mouseButton == 0 ? 1 : itemstack2.getMaxStackSize());
                        this.mc.player.dropItem(itemstack2, true);
                        this.mc.playerController.sendPacketDropItem(itemstack2);
                     }

                     return;
                  }

                  if (!itemstack5.func_190926_b() && !itemstack7.func_190926_b() && itemstack5.isItemEqual(itemstack7) && ItemStack.areItemStackTagsEqual(itemstack5, itemstack7)) {
                     if (mouseButton == 0) {
                        if (flag) {
                           itemstack5.func_190920_e(itemstack5.getMaxStackSize());
                        } else if (itemstack5.func_190916_E() < itemstack5.getMaxStackSize()) {
                           itemstack5.func_190917_f(1);
                        }
                     } else {
                        itemstack5.func_190918_g(1);
                     }
                  } else if (!itemstack7.func_190926_b() && itemstack5.func_190926_b()) {
                     inventoryplayer.setItemStack(itemstack7.copy());
                     itemstack5 = inventoryplayer.getItemStack();
                     if (flag) {
                        itemstack5.func_190920_e(itemstack5.getMaxStackSize());
                     }
                  } else if (mouseButton == 0) {
                     inventoryplayer.setItemStack(ItemStack.field_190927_a);
                  } else {
                     inventoryplayer.getItemStack().func_190918_g(1);
                  }
               } else if (this.inventorySlots != null) {
                  itemstack3 = slotIn == null ? ItemStack.field_190927_a : this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                  this.inventorySlots.slotClick(slotIn == null ? slotId : slotIn.slotNumber, mouseButton, type, this.mc.player);
                  if (Container.getDragEvent(mouseButton) == 2) {
                     for(int k = 0; k < 9; ++k) {
                        this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + k).getStack(), 36 + k);
                     }
                  } else if (slotIn != null) {
                     itemstack5 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                     this.mc.playerController.sendSlotPacket(itemstack5, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
                     int i = 45 + mouseButton;
                     if (type == ClickType.SWAP) {
                        this.mc.playerController.sendSlotPacket(itemstack3, i - this.inventorySlots.inventorySlots.size() + 9 + 36);
                     } else if (type == ClickType.THROW && !itemstack3.func_190926_b()) {
                        itemstack2 = itemstack3.copy();
                        itemstack2.func_190920_e(mouseButton == 0 ? 1 : itemstack2.getMaxStackSize());
                        this.mc.player.dropItem(itemstack2, true);
                        this.mc.playerController.sendPacketDropItem(itemstack2);
                     }

                     this.mc.player.inventoryContainer.detectAndSendChanges();
                  }
               }
            }
         }
      }

   }

   protected void updateActivePotionEffects() {
      int i = this.guiLeft;
      super.updateActivePotionEffects();
      if (this.searchField != null && this.guiLeft != i) {
         this.searchField.xPosition = this.guiLeft + 82;
      }

   }

   public void initGui() {
      if (this.mc.playerController.isInCreativeMode()) {
         super.initGui();
         this.buttonList.clear();
         Keyboard.enableRepeatEvents(true);
         this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 80, this.fontRendererObj.FONT_HEIGHT);
         this.searchField.setMaxStringLength(50);
         this.searchField.setEnableBackgroundDrawing(false);
         this.searchField.setVisible(false);
         this.searchField.setTextColor(16777215);
         int i = selectedTabIndex;
         selectedTabIndex = -1;
         this.setCurrentCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY[i]);
         this.listener = new CreativeCrafting(this.mc);
         this.mc.player.inventoryContainer.addListener(this.listener);
      } else {
         this.mc.displayGuiScreen(new GuiInventory(this.mc.player));
      }

   }

   public void onGuiClosed() {
      super.onGuiClosed();
      if (this.mc.player != null && this.mc.player.inventory != null) {
         this.mc.player.inventoryContainer.removeListener(this.listener);
      }

      Keyboard.enableRepeatEvents(false);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (selectedTabIndex != CreativeTabs.SEARCH.getTabIndex()) {
         if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
            this.setCurrentCreativeTab(CreativeTabs.SEARCH);
         } else {
            super.keyTyped(typedChar, keyCode);
         }
      } else {
         if (this.clearSearch) {
            this.clearSearch = false;
            this.searchField.setText("");
         }

         if (!this.checkHotbarKeys(keyCode)) {
            if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
               this.updateCreativeSearch();
            } else {
               super.keyTyped(typedChar, keyCode);
            }
         }
      }

   }

   private void updateCreativeSearch() {
      GuiContainerCreative.ContainerCreative guicontainercreative$containercreative = (GuiContainerCreative.ContainerCreative)this.inventorySlots;
      guicontainercreative$containercreative.itemList.clear();
      if (this.searchField.getText().isEmpty()) {
         Iterator var2 = Item.REGISTRY.iterator();

         while(var2.hasNext()) {
            Item item = (Item)var2.next();
            item.getSubItems(CreativeTabs.SEARCH, guicontainercreative$containercreative.itemList);
         }
      } else {
         guicontainercreative$containercreative.itemList.addAll(this.mc.func_193987_a(SearchTreeManager.field_194011_a).func_194038_a(this.searchField.getText().toLowerCase(Locale.ROOT)));
      }

      this.currentScroll = 0.0F;
      guicontainercreative$containercreative.scrollTo(0.0F);
   }

   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex];
      if (creativetabs.drawInForegroundOfTab()) {
         GlStateManager.disableBlend();
         this.fontRendererObj.drawString(I18n.format(creativetabs.getTranslatedTabLabel()), 8.0F, 6.0F, 4210752);
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (mouseButton == 0) {
         int i = mouseX - this.guiLeft;
         int j = mouseY - this.guiTop;
         CreativeTabs[] var6 = CreativeTabs.CREATIVE_TAB_ARRAY;
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            CreativeTabs creativetabs = var6[var8];
            if (this.isMouseOverTab(creativetabs, i, j)) {
               return;
            }
         }
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      if (state == 0) {
         int i = mouseX - this.guiLeft;
         int j = mouseY - this.guiTop;
         CreativeTabs[] var6 = CreativeTabs.CREATIVE_TAB_ARRAY;
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            CreativeTabs creativetabs = var6[var8];
            if (this.isMouseOverTab(creativetabs, i, j)) {
               this.setCurrentCreativeTab(creativetabs);
               return;
            }
         }
      }

      super.mouseReleased(mouseX, mouseY, state);
   }

   private boolean needsScrollBars() {
      return selectedTabIndex != CreativeTabs.INVENTORY.getTabIndex() && CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex].shouldHidePlayerInventory() && ((GuiContainerCreative.ContainerCreative)this.inventorySlots).canScroll();
   }

   private void setCurrentCreativeTab(CreativeTabs tab) {
      int i = selectedTabIndex;
      selectedTabIndex = tab.getTabIndex();
      GuiContainerCreative.ContainerCreative guicontainercreative$containercreative = (GuiContainerCreative.ContainerCreative)this.inventorySlots;
      this.dragSplittingSlots.clear();
      guicontainercreative$containercreative.itemList.clear();
      if (tab == CreativeTabs.field_192395_m) {
         for(int j = 0; j < 9; ++j) {
            HotbarSnapshot hotbarsnapshot = this.mc.field_191950_u.func_192563_a(j);
            if (hotbarsnapshot.isEmpty()) {
               for(int k = 0; k < 9; ++k) {
                  if (k == j) {
                     ItemStack itemstack = new ItemStack(Items.PAPER);
                     itemstack.func_190925_c("CustomCreativeLock");
                     String s = GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindsHotbar[j].getKeyCode());
                     String s1 = GameSettings.getKeyDisplayString(this.mc.gameSettings.field_193629_ap.getKeyCode());
                     itemstack.setStackDisplayName((new TextComponentTranslation("inventory.hotbarInfo", new Object[]{s1, s})).getUnformattedText());
                     guicontainercreative$containercreative.itemList.add(itemstack);
                  } else {
                     guicontainercreative$containercreative.itemList.add(ItemStack.field_190927_a);
                  }
               }
            } else {
               guicontainercreative$containercreative.itemList.addAll(hotbarsnapshot);
            }
         }
      } else if (tab != CreativeTabs.SEARCH) {
         tab.displayAllRelevantItems(guicontainercreative$containercreative.itemList);
      }

      if (tab == CreativeTabs.INVENTORY) {
         Container container = this.mc.player.inventoryContainer;
         if (this.originalSlots == null) {
            this.originalSlots = guicontainercreative$containercreative.inventorySlots;
         }

         guicontainercreative$containercreative.inventorySlots = Lists.newArrayList();

         for(int l = 0; l < container.inventorySlots.size(); ++l) {
            Slot slot = new GuiContainerCreative.CreativeSlot((Slot)container.inventorySlots.get(l), l);
            guicontainercreative$containercreative.inventorySlots.add(slot);
            int i1;
            int k1;
            int i2;
            if (l >= 5 && l < 9) {
               i1 = l - 5;
               k1 = i1 / 2;
               i2 = i1 % 2;
               slot.xDisplayPosition = 54 + k1 * 54;
               slot.yDisplayPosition = 6 + i2 * 27;
            } else if (l >= 0 && l < 5) {
               slot.xDisplayPosition = -2000;
               slot.yDisplayPosition = -2000;
            } else if (l == 45) {
               slot.xDisplayPosition = 35;
               slot.yDisplayPosition = 20;
            } else if (l < container.inventorySlots.size()) {
               i1 = l - 9;
               k1 = i1 % 9;
               i2 = i1 / 9;
               slot.xDisplayPosition = 9 + k1 * 18;
               if (l >= 36) {
                  slot.yDisplayPosition = 112;
               } else {
                  slot.yDisplayPosition = 54 + i2 * 18;
               }
            }
         }

         this.destroyItemSlot = new Slot(basicInventory, 0, 173, 112);
         guicontainercreative$containercreative.inventorySlots.add(this.destroyItemSlot);
      } else if (i == CreativeTabs.INVENTORY.getTabIndex()) {
         guicontainercreative$containercreative.inventorySlots = this.originalSlots;
         this.originalSlots = null;
      }

      if (this.searchField != null) {
         if (tab == CreativeTabs.SEARCH) {
            this.searchField.setVisible(true);
            this.searchField.setCanLoseFocus(false);
            this.searchField.setFocused(true);
            this.searchField.setText("");
            this.updateCreativeSearch();
         } else {
            this.searchField.setVisible(false);
            this.searchField.setCanLoseFocus(true);
            this.searchField.setFocused(false);
         }
      }

      this.currentScroll = 0.0F;
      guicontainercreative$containercreative.scrollTo(0.0F);
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      int i = Mouse.getEventDWheel();
      if (i != 0 && this.needsScrollBars()) {
         int j = (((GuiContainerCreative.ContainerCreative)this.inventorySlots).itemList.size() + 9 - 1) / 9 - 5;
         if (i > 0) {
            i = 1;
         }

         if (i < 0) {
            i = -1;
         }

         this.currentScroll = (float)((double)this.currentScroll - (double)i / (double)j);
         this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
         ((GuiContainerCreative.ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      ScaledResolution sr = new ScaledResolution(this.mc);
      this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0, (new Color(0, 224, 255)).getRGB());
      boolean flag = Mouse.isButtonDown(0);
      int i = this.guiLeft;
      int j = this.guiTop;
      int k = i + 175;
      int l = j + 18;
      int i1 = k + 14;
      int j1 = l + 112;
      if (!this.wasClicking && flag && mouseX >= k && mouseY >= l && mouseX < i1 && mouseY < j1) {
         this.isScrolling = this.needsScrollBars();
      }

      if (!flag) {
         this.isScrolling = false;
      }

      this.wasClicking = flag;
      if (this.isScrolling) {
         this.currentScroll = ((float)(mouseY - l) - 7.5F) / ((float)(j1 - l) - 15.0F);
         this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
         ((GuiContainerCreative.ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
      CreativeTabs[] var12 = CreativeTabs.CREATIVE_TAB_ARRAY;
      int var13 = var12.length;

      for(int var14 = 0; var14 < var13; ++var14) {
         CreativeTabs creativetabs = var12[var14];
         if (this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY)) {
            break;
         }
      }

      if (this.destroyItemSlot != null && selectedTabIndex == CreativeTabs.INVENTORY.getTabIndex() && this.isPointInRegion(this.destroyItemSlot.xDisplayPosition, this.destroyItemSlot.yDisplayPosition, 16, 16, mouseX, mouseY)) {
         this.drawCreativeTabHoveringText(I18n.format("inventory.binSlot"), mouseX, mouseY);
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableLighting();
      this.func_191948_b(mouseX, mouseY);
   }

   protected void renderToolTip(ItemStack stack, int x, int y) {
      if (selectedTabIndex == CreativeTabs.SEARCH.getTabIndex()) {
         List<String> list = stack.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
         CreativeTabs creativetabs = stack.getItem().getCreativeTab();
         if (creativetabs == null && stack.getItem() == Items.ENCHANTED_BOOK) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
            if (map.size() == 1) {
               Enchantment enchantment = (Enchantment)map.keySet().iterator().next();
               CreativeTabs[] var8 = CreativeTabs.CREATIVE_TAB_ARRAY;
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  CreativeTabs creativetabs1 = var8[var10];
                  if (creativetabs1.hasRelevantEnchantmentType(enchantment.type)) {
                     creativetabs = creativetabs1;
                     break;
                  }
               }
            }
         }

         if (creativetabs != null) {
            list.add(1, "" + TextFormatting.BOLD + TextFormatting.BLUE + I18n.format(creativetabs.getTranslatedTabLabel()));
         }

         for(int i = 0; i < list.size(); ++i) {
            if (i == 0) {
               list.set(i, stack.getRarity().rarityColor + (String)list.get(i));
            } else {
               list.set(i, TextFormatting.GRAY + (String)list.get(i));
            }
         }

         this.drawHoveringText(list, x, y);
      } else {
         super.renderToolTip(stack, x, y);
      }

   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      RenderHelper.enableGUIStandardItemLighting();
      CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex];
      CreativeTabs[] var5 = CreativeTabs.CREATIVE_TAB_ARRAY;
      int j = var5.length;

      int k;
      for(k = 0; k < j; ++k) {
         CreativeTabs creativetabs1 = var5[k];
         this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
         if (creativetabs1.getTabIndex() != selectedTabIndex) {
            this.drawTab(creativetabs1);
         }
      }

      this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
      this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
      this.searchField.drawTextBox();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      int i = this.guiLeft + 175;
      j = this.guiTop + 18;
      k = j + 112;
      this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
      if (creativetabs.shouldHidePlayerInventory()) {
         this.drawTexturedModalRect(i, j + (int)((float)(k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
      }

      this.drawTab(creativetabs);
      if (creativetabs == CreativeTabs.INVENTORY) {
         GuiInventory.drawEntityOnScreen(this.guiLeft + 88, this.guiTop + 45, 20, (float)(this.guiLeft + 88 - mouseX), (float)(this.guiTop + 45 - 30 - mouseY), this.mc.player);
      }

   }

   protected boolean isMouseOverTab(CreativeTabs tab, int mouseX, int mouseY) {
      int i = tab.getTabColumn();
      int j = 28 * i;
      int k = 0;
      if (tab.func_192394_m()) {
         j = this.xSize - 28 * (6 - i) + 2;
      } else if (i > 0) {
         j += i;
      }

      int k;
      if (tab.isTabInFirstRow()) {
         k = k - 32;
      } else {
         k = k + this.ySize;
      }

      return mouseX >= j && mouseX <= j + 28 && mouseY >= k && mouseY <= k + 32;
   }

   protected boolean renderCreativeInventoryHoveringText(CreativeTabs tab, int mouseX, int mouseY) {
      int i = tab.getTabColumn();
      int j = 28 * i;
      int k = 0;
      if (tab.func_192394_m()) {
         j = this.xSize - 28 * (6 - i) + 2;
      } else if (i > 0) {
         j += i;
      }

      int k;
      if (tab.isTabInFirstRow()) {
         k = k - 32;
      } else {
         k = k + this.ySize;
      }

      if (this.isPointInRegion(j + 3, k + 3, 23, 27, mouseX, mouseY)) {
         this.drawCreativeTabHoveringText(I18n.format(tab.getTranslatedTabLabel()), mouseX, mouseY);
         return true;
      } else {
         return false;
      }
   }

   protected void drawTab(CreativeTabs tab) {
      boolean flag = tab.getTabIndex() == selectedTabIndex;
      boolean flag1 = tab.isTabInFirstRow();
      int i = tab.getTabColumn();
      int j = i * 28;
      int k = 0;
      int l = this.guiLeft + 28 * i;
      int i1 = this.guiTop;
      int j1 = true;
      if (flag) {
         k += 32;
      }

      if (tab.func_192394_m()) {
         l = this.guiLeft + this.xSize - 28 * (6 - i);
      } else if (i > 0) {
         l += i;
      }

      if (flag1) {
         i1 -= 28;
      } else {
         k += 64;
         i1 += this.ySize - 4;
      }

      GlStateManager.disableLighting();
      this.drawTexturedModalRect(l, i1, j, k, 28, 32);
      this.zLevel = 100.0F;
      this.itemRender.zLevel = 100.0F;
      l += 6;
      i1 = i1 + 8 + (flag1 ? 1 : -1);
      GlStateManager.enableLighting();
      GlStateManager.enableRescaleNormal();
      ItemStack itemstack = tab.getIconItemStack();
      this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i1);
      this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, l, i1);
      GlStateManager.disableLighting();
      this.itemRender.zLevel = 0.0F;
      this.zLevel = 0.0F;
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.id == 1) {
         this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
      }

   }

   public int getSelectedTabIndex() {
      return selectedTabIndex;
   }

   public static void func_192044_a(Minecraft p_192044_0_, int p_192044_1_, boolean p_192044_2_, boolean p_192044_3_) {
      EntityPlayerSP entityplayersp = p_192044_0_.player;
      CreativeSettings creativesettings = p_192044_0_.field_191950_u;
      HotbarSnapshot hotbarsnapshot = creativesettings.func_192563_a(p_192044_1_);
      int j;
      if (p_192044_2_) {
         for(j = 0; j < InventoryPlayer.getHotbarSize(); ++j) {
            ItemStack itemstack = ((ItemStack)hotbarsnapshot.get(j)).copy();
            entityplayersp.inventory.setInventorySlotContents(j, itemstack);
            p_192044_0_.playerController.sendSlotPacket(itemstack, 36 + j);
         }

         entityplayersp.inventoryContainer.detectAndSendChanges();
      } else if (p_192044_3_) {
         for(j = 0; j < InventoryPlayer.getHotbarSize(); ++j) {
            hotbarsnapshot.set(j, entityplayersp.inventory.getStackInSlot(j).copy());
         }

         String s = GameSettings.getKeyDisplayString(p_192044_0_.gameSettings.keyBindsHotbar[p_192044_1_].getKeyCode());
         String s1 = GameSettings.getKeyDisplayString(p_192044_0_.gameSettings.field_193630_aq.getKeyCode());
         p_192044_0_.ingameGUI.setRecordPlaying((ITextComponent)(new TextComponentTranslation("inventory.hotbarSaved", new Object[]{s1, s})), false);
         creativesettings.func_192564_b();
      }

   }

   static {
      selectedTabIndex = CreativeTabs.BUILDING_BLOCKS.getTabIndex();
   }

   static class LockedSlot extends Slot {
      public LockedSlot(IInventory p_i47453_1_, int p_i47453_2_, int p_i47453_3_, int p_i47453_4_) {
         super(p_i47453_1_, p_i47453_2_, p_i47453_3_, p_i47453_4_);
      }

      public boolean canTakeStack(EntityPlayer playerIn) {
         if (super.canTakeStack(playerIn) && this.getHasStack()) {
            return this.getStack().getSubCompound("CustomCreativeLock") == null;
         } else {
            return !this.getHasStack();
         }
      }
   }

   class CreativeSlot extends Slot {
      private final Slot slot;

      public CreativeSlot(Slot p_i46313_2_, int p_i46313_3_) {
         super(p_i46313_2_.inventory, p_i46313_3_, 0, 0);
         this.slot = p_i46313_2_;
      }

      public ItemStack func_190901_a(EntityPlayer p_190901_1_, ItemStack p_190901_2_) {
         this.slot.func_190901_a(p_190901_1_, p_190901_2_);
         return p_190901_2_;
      }

      public boolean isItemValid(ItemStack stack) {
         return this.slot.isItemValid(stack);
      }

      public ItemStack getStack() {
         return this.slot.getStack();
      }

      public boolean getHasStack() {
         return this.slot.getHasStack();
      }

      public void putStack(ItemStack stack) {
         this.slot.putStack(stack);
      }

      public void onSlotChanged() {
         this.slot.onSlotChanged();
      }

      public int getSlotStackLimit() {
         return this.slot.getSlotStackLimit();
      }

      public int getItemStackLimit(ItemStack stack) {
         return this.slot.getItemStackLimit(stack);
      }

      @Nullable
      public String getSlotTexture() {
         return this.slot.getSlotTexture();
      }

      public ItemStack decrStackSize(int amount) {
         return this.slot.decrStackSize(amount);
      }

      public boolean isHere(IInventory inv, int slotIn) {
         return this.slot.isHere(inv, slotIn);
      }

      public boolean canBeHovered() {
         return this.slot.canBeHovered();
      }

      public boolean canTakeStack(EntityPlayer playerIn) {
         return this.slot.canTakeStack(playerIn);
      }
   }

   public static class ContainerCreative extends Container {
      public NonNullList<ItemStack> itemList = NonNullList.func_191196_a();

      public ContainerCreative(EntityPlayer player) {
         InventoryPlayer inventoryplayer = player.inventory;

         int k;
         for(k = 0; k < 5; ++k) {
            for(int j = 0; j < 9; ++j) {
               this.addSlotToContainer(new GuiContainerCreative.LockedSlot(GuiContainerCreative.basicInventory, k * 9 + j, 9 + j * 18, 18 + k * 18));
            }
         }

         for(k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(inventoryplayer, k, 9 + k * 18, 112));
         }

         this.scrollTo(0.0F);
      }

      public boolean canInteractWith(EntityPlayer playerIn) {
         return true;
      }

      public void scrollTo(float p_148329_1_) {
         int i = (this.itemList.size() + 9 - 1) / 9 - 5;
         int j = (int)((double)(p_148329_1_ * (float)i) + 0.5D);
         if (j < 0) {
            j = 0;
         }

         for(int k = 0; k < 5; ++k) {
            for(int l = 0; l < 9; ++l) {
               int i1 = l + (k + j) * 9;
               if (i1 >= 0 && i1 < this.itemList.size()) {
                  GuiContainerCreative.basicInventory.setInventorySlotContents(l + k * 9, (ItemStack)this.itemList.get(i1));
               } else {
                  GuiContainerCreative.basicInventory.setInventorySlotContents(l + k * 9, ItemStack.field_190927_a);
               }
            }
         }

      }

      public boolean canScroll() {
         return this.itemList.size() > 45;
      }

      public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
         if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size()) {
            Slot slot = (Slot)this.inventorySlots.get(index);
            if (slot != null && slot.getHasStack()) {
               slot.putStack(ItemStack.field_190927_a);
            }
         }

         return ItemStack.field_190927_a;
      }

      public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
         return slotIn.yDisplayPosition > 90;
      }

      public boolean canDragIntoSlot(Slot slotIn) {
         return slotIn.inventory instanceof InventoryPlayer || slotIn.yDisplayPosition > 90 && slotIn.xDisplayPosition <= 162;
      }
   }
}
