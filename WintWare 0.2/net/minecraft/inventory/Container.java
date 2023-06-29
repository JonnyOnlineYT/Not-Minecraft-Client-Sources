package net.minecraft.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class Container {
   public NonNullList<ItemStack> inventoryItemStacks = NonNullList.func_191196_a();
   public List<Slot> inventorySlots = Lists.newArrayList();
   public int windowId;
   private short transactionID;
   private int dragMode = -1;
   private int dragEvent;
   private final Set<Slot> dragSlots = Sets.newHashSet();
   protected List<IContainerListener> listeners = Lists.newArrayList();
   private final Set<EntityPlayer> playerList = Sets.newHashSet();

   protected Slot addSlotToContainer(Slot slotIn) {
      slotIn.slotNumber = this.inventorySlots.size();
      this.inventorySlots.add(slotIn);
      this.inventoryItemStacks.add(ItemStack.field_190927_a);
      return slotIn;
   }

   public void addListener(IContainerListener listener) {
      if (this.listeners.contains(listener)) {
         throw new IllegalArgumentException("Listener already listening");
      } else {
         this.listeners.add(listener);
         listener.updateCraftingInventory(this, this.getInventory());
         this.detectAndSendChanges();
      }
   }

   public void removeListener(IContainerListener listener) {
      this.listeners.remove(listener);
   }

   public NonNullList<ItemStack> getInventory() {
      NonNullList<ItemStack> nonnulllist = NonNullList.func_191196_a();

      for(int i = 0; i < this.inventorySlots.size(); ++i) {
         nonnulllist.add(((Slot)this.inventorySlots.get(i)).getStack());
      }

      return nonnulllist;
   }

   public void detectAndSendChanges() {
      for(int i = 0; i < this.inventorySlots.size(); ++i) {
         ItemStack itemstack = ((Slot)this.inventorySlots.get(i)).getStack();
         ItemStack itemstack1 = (ItemStack)this.inventoryItemStacks.get(i);
         if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
            itemstack1 = itemstack.func_190926_b() ? ItemStack.field_190927_a : itemstack.copy();
            this.inventoryItemStacks.set(i, itemstack1);

            for(int j = 0; j < this.listeners.size(); ++j) {
               ((IContainerListener)this.listeners.get(j)).sendSlotContents(this, i, itemstack1);
            }
         }
      }

   }

   public boolean enchantItem(EntityPlayer playerIn, int id) {
      return false;
   }

   @Nullable
   public Slot getSlotFromInventory(IInventory inv, int slotIn) {
      for(int i = 0; i < this.inventorySlots.size(); ++i) {
         Slot slot = (Slot)this.inventorySlots.get(i);
         if (slot.isHere(inv, slotIn)) {
            return slot;
         }
      }

      return null;
   }

   public Slot getSlot(int slotId) {
      return (Slot)this.inventorySlots.get(slotId);
   }

   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
      Slot slot = (Slot)this.inventorySlots.get(index);
      return slot != null ? slot.getStack() : ItemStack.field_190927_a;
   }

   public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
      ItemStack itemstack = ItemStack.field_190927_a;
      InventoryPlayer inventoryplayer = player.inventory;
      ItemStack itemstack8;
      int k1;
      int k3;
      ItemStack itemstack11;
      if (clickTypeIn == ClickType.QUICK_CRAFT) {
         int j1 = this.dragEvent;
         this.dragEvent = getDragEvent(dragType);
         if ((j1 != 1 || this.dragEvent != 2) && j1 != this.dragEvent) {
            this.resetDrag();
         } else if (inventoryplayer.getItemStack().func_190926_b()) {
            this.resetDrag();
         } else if (this.dragEvent == 0) {
            this.dragMode = extractDragMode(dragType);
            if (isValidDragMode(this.dragMode, player)) {
               this.dragEvent = 1;
               this.dragSlots.clear();
            } else {
               this.resetDrag();
            }
         } else if (this.dragEvent == 1) {
            Slot slot7 = (Slot)this.inventorySlots.get(slotId);
            itemstack11 = inventoryplayer.getItemStack();
            if (slot7 != null && canAddItemToSlot(slot7, itemstack11, true) && slot7.isItemValid(itemstack11) && (this.dragMode == 2 || itemstack11.func_190916_E() > this.dragSlots.size()) && this.canDragIntoSlot(slot7)) {
               this.dragSlots.add(slot7);
            }
         } else if (this.dragEvent == 2) {
            if (!this.dragSlots.isEmpty()) {
               itemstack8 = inventoryplayer.getItemStack().copy();
               k1 = inventoryplayer.getItemStack().func_190916_E();
               Iterator var23 = this.dragSlots.iterator();

               label359:
               while(true) {
                  Slot slot8;
                  ItemStack itemstack13;
                  do {
                     do {
                        do {
                           do {
                              if (!var23.hasNext()) {
                                 itemstack8.func_190920_e(k1);
                                 inventoryplayer.setItemStack(itemstack8);
                                 break label359;
                              }

                              slot8 = (Slot)var23.next();
                              itemstack13 = inventoryplayer.getItemStack();
                           } while(slot8 == null);
                        } while(!canAddItemToSlot(slot8, itemstack13, true));
                     } while(!slot8.isItemValid(itemstack13));
                  } while(this.dragMode != 2 && itemstack13.func_190916_E() < this.dragSlots.size());

                  if (this.canDragIntoSlot(slot8)) {
                     ItemStack itemstack14 = itemstack8.copy();
                     int j3 = slot8.getHasStack() ? slot8.getStack().func_190916_E() : 0;
                     computeStackSize(this.dragSlots, this.dragMode, itemstack14, j3);
                     k3 = Math.min(itemstack14.getMaxStackSize(), slot8.getItemStackLimit(itemstack14));
                     if (itemstack14.func_190916_E() > k3) {
                        itemstack14.func_190920_e(k3);
                     }

                     k1 -= itemstack14.func_190916_E() - j3;
                     slot8.putStack(itemstack14);
                  }
               }
            }

            this.resetDrag();
         } else {
            this.resetDrag();
         }
      } else if (this.dragEvent != 0) {
         this.resetDrag();
      } else {
         Slot slot6;
         int k2;
         if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
               if (!inventoryplayer.getItemStack().func_190926_b()) {
                  if (dragType == 0) {
                     player.dropItem(inventoryplayer.getItemStack(), true);
                     inventoryplayer.setItemStack(ItemStack.field_190927_a);
                  }

                  if (dragType == 1) {
                     player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);
                  }
               }
            } else if (clickTypeIn == ClickType.QUICK_MOVE) {
               if (slotId < 0) {
                  return ItemStack.field_190927_a;
               }

               slot6 = (Slot)this.inventorySlots.get(slotId);
               if (slot6 == null || !slot6.canTakeStack(player)) {
                  return ItemStack.field_190927_a;
               }

               for(itemstack8 = this.transferStackInSlot(player, slotId); !itemstack8.func_190926_b() && ItemStack.areItemsEqual(slot6.getStack(), itemstack8); itemstack8 = this.transferStackInSlot(player, slotId)) {
                  itemstack = itemstack8.copy();
               }
            } else {
               if (slotId < 0) {
                  return ItemStack.field_190927_a;
               }

               slot6 = (Slot)this.inventorySlots.get(slotId);
               if (slot6 != null) {
                  itemstack8 = slot6.getStack();
                  itemstack11 = inventoryplayer.getItemStack();
                  if (!itemstack8.func_190926_b()) {
                     itemstack = itemstack8.copy();
                  }

                  if (itemstack8.func_190926_b()) {
                     if (!itemstack11.func_190926_b() && slot6.isItemValid(itemstack11)) {
                        k2 = dragType == 0 ? itemstack11.func_190916_E() : 1;
                        if (k2 > slot6.getItemStackLimit(itemstack11)) {
                           k2 = slot6.getItemStackLimit(itemstack11);
                        }

                        slot6.putStack(itemstack11.splitStack(k2));
                     }
                  } else if (slot6.canTakeStack(player)) {
                     if (itemstack11.func_190926_b()) {
                        if (itemstack8.func_190926_b()) {
                           slot6.putStack(ItemStack.field_190927_a);
                           inventoryplayer.setItemStack(ItemStack.field_190927_a);
                        } else {
                           k2 = dragType == 0 ? itemstack8.func_190916_E() : (itemstack8.func_190916_E() + 1) / 2;
                           inventoryplayer.setItemStack(slot6.decrStackSize(k2));
                           if (itemstack8.func_190926_b()) {
                              slot6.putStack(ItemStack.field_190927_a);
                           }

                           slot6.func_190901_a(player, inventoryplayer.getItemStack());
                        }
                     } else if (slot6.isItemValid(itemstack11)) {
                        if (itemstack8.getItem() == itemstack11.getItem() && itemstack8.getMetadata() == itemstack11.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack8, itemstack11)) {
                           k2 = dragType == 0 ? itemstack11.func_190916_E() : 1;
                           if (k2 > slot6.getItemStackLimit(itemstack11) - itemstack8.func_190916_E()) {
                              k2 = slot6.getItemStackLimit(itemstack11) - itemstack8.func_190916_E();
                           }

                           if (k2 > itemstack11.getMaxStackSize() - itemstack8.func_190916_E()) {
                              k2 = itemstack11.getMaxStackSize() - itemstack8.func_190916_E();
                           }

                           itemstack11.func_190918_g(k2);
                           itemstack8.func_190917_f(k2);
                        } else if (itemstack11.func_190916_E() <= slot6.getItemStackLimit(itemstack11)) {
                           slot6.putStack(itemstack11);
                           inventoryplayer.setItemStack(itemstack8);
                        }
                     } else if (itemstack8.getItem() == itemstack11.getItem() && itemstack11.getMaxStackSize() > 1 && (!itemstack8.getHasSubtypes() || itemstack8.getMetadata() == itemstack11.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack8, itemstack11) && !itemstack8.func_190926_b()) {
                        k2 = itemstack8.func_190916_E();
                        if (k2 + itemstack11.func_190916_E() <= itemstack11.getMaxStackSize()) {
                           itemstack11.func_190917_f(k2);
                           itemstack8 = slot6.decrStackSize(k2);
                           if (itemstack8.func_190926_b()) {
                              slot6.putStack(ItemStack.field_190927_a);
                           }

                           slot6.func_190901_a(player, inventoryplayer.getItemStack());
                        }
                     }
                  }

                  slot6.onSlotChanged();
               }
            }
         } else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
            slot6 = (Slot)this.inventorySlots.get(slotId);
            itemstack8 = inventoryplayer.getStackInSlot(dragType);
            itemstack11 = slot6.getStack();
            if (!itemstack8.func_190926_b() || !itemstack11.func_190926_b()) {
               if (itemstack8.func_190926_b()) {
                  if (slot6.canTakeStack(player)) {
                     inventoryplayer.setInventorySlotContents(dragType, itemstack11);
                     slot6.func_190900_b(itemstack11.func_190916_E());
                     slot6.putStack(ItemStack.field_190927_a);
                     slot6.func_190901_a(player, itemstack11);
                  }
               } else if (itemstack11.func_190926_b()) {
                  if (slot6.isItemValid(itemstack8)) {
                     k2 = slot6.getItemStackLimit(itemstack8);
                     if (itemstack8.func_190916_E() > k2) {
                        slot6.putStack(itemstack8.splitStack(k2));
                     } else {
                        slot6.putStack(itemstack8);
                        inventoryplayer.setInventorySlotContents(dragType, ItemStack.field_190927_a);
                     }
                  }
               } else if (slot6.canTakeStack(player) && slot6.isItemValid(itemstack8)) {
                  k2 = slot6.getItemStackLimit(itemstack8);
                  if (itemstack8.func_190916_E() > k2) {
                     slot6.putStack(itemstack8.splitStack(k2));
                     slot6.func_190901_a(player, itemstack11);
                     if (!inventoryplayer.addItemStackToInventory(itemstack11)) {
                        player.dropItem(itemstack11, true);
                     }
                  } else {
                     slot6.putStack(itemstack8);
                     inventoryplayer.setInventorySlotContents(dragType, itemstack11);
                     slot6.func_190901_a(player, itemstack11);
                  }
               }
            }
         } else if (clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack().func_190926_b() && slotId >= 0) {
            slot6 = (Slot)this.inventorySlots.get(slotId);
            if (slot6 != null && slot6.getHasStack()) {
               itemstack8 = slot6.getStack().copy();
               itemstack8.func_190920_e(itemstack8.getMaxStackSize());
               inventoryplayer.setItemStack(itemstack8);
            }
         } else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack().func_190926_b() && slotId >= 0) {
            slot6 = (Slot)this.inventorySlots.get(slotId);
            if (slot6 != null && slot6.getHasStack() && slot6.canTakeStack(player)) {
               itemstack8 = slot6.decrStackSize(dragType == 0 ? 1 : slot6.getStack().func_190916_E());
               slot6.func_190901_a(player, itemstack8);
               player.dropItem(itemstack8, true);
            }
         } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
            slot6 = (Slot)this.inventorySlots.get(slotId);
            itemstack8 = inventoryplayer.getItemStack();
            if (!itemstack8.func_190926_b() && (slot6 == null || !slot6.getHasStack() || !slot6.canTakeStack(player))) {
               k1 = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
               k2 = dragType == 0 ? 1 : -1;

               for(int k = 0; k < 2; ++k) {
                  for(int l = k1; l >= 0 && l < this.inventorySlots.size() && itemstack8.func_190916_E() < itemstack8.getMaxStackSize(); l += k2) {
                     Slot slot1 = (Slot)this.inventorySlots.get(l);
                     if (slot1.getHasStack() && canAddItemToSlot(slot1, itemstack8, true) && slot1.canTakeStack(player) && this.canMergeSlot(itemstack8, slot1)) {
                        ItemStack itemstack2 = slot1.getStack();
                        if (k != 0 || itemstack2.func_190916_E() != itemstack2.getMaxStackSize()) {
                           k3 = Math.min(itemstack8.getMaxStackSize() - itemstack8.func_190916_E(), itemstack2.func_190916_E());
                           ItemStack itemstack3 = slot1.decrStackSize(k3);
                           itemstack8.func_190917_f(k3);
                           if (itemstack3.func_190926_b()) {
                              slot1.putStack(ItemStack.field_190927_a);
                           }

                           slot1.func_190901_a(player, itemstack3);
                        }
                     }
                  }
               }
            }

            this.detectAndSendChanges();
         }
      }

      return itemstack;
   }

   public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
      return true;
   }

   public void onContainerClosed(EntityPlayer playerIn) {
      InventoryPlayer inventoryplayer = playerIn.inventory;
      if (!inventoryplayer.getItemStack().func_190926_b()) {
         playerIn.dropItem(inventoryplayer.getItemStack(), false);
         inventoryplayer.setItemStack(ItemStack.field_190927_a);
      }

   }

   protected void func_193327_a(EntityPlayer p_193327_1_, World p_193327_2_, IInventory p_193327_3_) {
      int i;
      if (!p_193327_1_.isEntityAlive() || p_193327_1_ instanceof EntityPlayerMP && ((EntityPlayerMP)p_193327_1_).func_193105_t()) {
         for(i = 0; i < p_193327_3_.getSizeInventory(); ++i) {
            p_193327_1_.dropItem(p_193327_3_.removeStackFromSlot(i), false);
         }
      } else {
         for(i = 0; i < p_193327_3_.getSizeInventory(); ++i) {
            p_193327_1_.inventory.func_191975_a(p_193327_2_, p_193327_3_.removeStackFromSlot(i));
         }
      }

   }

   public void onCraftMatrixChanged(IInventory inventoryIn) {
      this.detectAndSendChanges();
   }

   public void putStackInSlot(int slotID, ItemStack stack) {
      this.getSlot(slotID).putStack(stack);
   }

   public void func_190896_a(List<ItemStack> p_190896_1_) {
      for(int i = 0; i < p_190896_1_.size(); ++i) {
         this.getSlot(i).putStack((ItemStack)p_190896_1_.get(i));
      }

   }

   public void updateProgressBar(int id, int data) {
   }

   public short getNextTransactionID(InventoryPlayer invPlayer) {
      ++this.transactionID;
      return this.transactionID;
   }

   public boolean getCanCraft(EntityPlayer player) {
      return !this.playerList.contains(player);
   }

   public void setCanCraft(EntityPlayer player, boolean canCraft) {
      if (canCraft) {
         this.playerList.remove(player);
      } else {
         this.playerList.add(player);
      }

   }

   public abstract boolean canInteractWith(EntityPlayer var1);

   protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
      boolean flag = false;
      int i = startIndex;
      if (reverseDirection) {
         i = endIndex - 1;
      }

      Slot slot1;
      ItemStack itemstack;
      if (stack.isStackable()) {
         while(!stack.func_190926_b()) {
            if (reverseDirection) {
               if (i < startIndex) {
                  break;
               }
            } else if (i >= endIndex) {
               break;
            }

            slot1 = (Slot)this.inventorySlots.get(i);
            itemstack = slot1.getStack();
            if (!itemstack.func_190926_b() && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
               int j = itemstack.func_190916_E() + stack.func_190916_E();
               if (j <= stack.getMaxStackSize()) {
                  stack.func_190920_e(0);
                  itemstack.func_190920_e(j);
                  slot1.onSlotChanged();
                  flag = true;
               } else if (itemstack.func_190916_E() < stack.getMaxStackSize()) {
                  stack.func_190918_g(stack.getMaxStackSize() - itemstack.func_190916_E());
                  itemstack.func_190920_e(stack.getMaxStackSize());
                  slot1.onSlotChanged();
                  flag = true;
               }
            }

            if (reverseDirection) {
               --i;
            } else {
               ++i;
            }
         }
      }

      if (!stack.func_190926_b()) {
         if (reverseDirection) {
            i = endIndex - 1;
         } else {
            i = startIndex;
         }

         while(true) {
            if (reverseDirection) {
               if (i < startIndex) {
                  break;
               }
            } else if (i >= endIndex) {
               break;
            }

            slot1 = (Slot)this.inventorySlots.get(i);
            itemstack = slot1.getStack();
            if (itemstack.func_190926_b() && slot1.isItemValid(stack)) {
               if (stack.func_190916_E() > slot1.getSlotStackLimit()) {
                  slot1.putStack(stack.splitStack(slot1.getSlotStackLimit()));
               } else {
                  slot1.putStack(stack.splitStack(stack.func_190916_E()));
               }

               slot1.onSlotChanged();
               flag = true;
               break;
            }

            if (reverseDirection) {
               --i;
            } else {
               ++i;
            }
         }
      }

      return flag;
   }

   public static int extractDragMode(int eventButton) {
      return eventButton >> 2 & 3;
   }

   public static int getDragEvent(int clickedButton) {
      return clickedButton & 3;
   }

   public static int getQuickcraftMask(int p_94534_0_, int p_94534_1_) {
      return p_94534_0_ & 3 | (p_94534_1_ & 3) << 2;
   }

   public static boolean isValidDragMode(int dragModeIn, EntityPlayer player) {
      if (dragModeIn == 0) {
         return true;
      } else if (dragModeIn == 1) {
         return true;
      } else {
         return dragModeIn == 2 && player.capabilities.isCreativeMode;
      }
   }

   protected void resetDrag() {
      this.dragEvent = 0;
      this.dragSlots.clear();
   }

   public static boolean canAddItemToSlot(@Nullable Slot slotIn, ItemStack stack, boolean stackSizeMatters) {
      boolean flag = slotIn == null || !slotIn.getHasStack();
      if (!flag && stack.isItemEqual(slotIn.getStack()) && ItemStack.areItemStackTagsEqual(slotIn.getStack(), stack)) {
         return slotIn.getStack().func_190916_E() + (stackSizeMatters ? 0 : stack.func_190916_E()) <= stack.getMaxStackSize();
      } else {
         return flag;
      }
   }

   public static void computeStackSize(Set<Slot> dragSlotsIn, int dragModeIn, ItemStack stack, int slotStackSize) {
      switch(dragModeIn) {
      case 0:
         stack.func_190920_e(MathHelper.floor((float)stack.func_190916_E() / (float)dragSlotsIn.size()));
         break;
      case 1:
         stack.func_190920_e(1);
         break;
      case 2:
         stack.func_190920_e(stack.getItem().getItemStackLimit());
      }

      stack.func_190917_f(slotStackSize);
   }

   public boolean canDragIntoSlot(Slot slotIn) {
      return true;
   }

   public static int calcRedstone(@Nullable TileEntity te) {
      return te instanceof IInventory ? calcRedstoneFromInventory((IInventory)te) : 0;
   }

   public static int calcRedstoneFromInventory(@Nullable IInventory inv) {
      if (inv == null) {
         return 0;
      } else {
         int i = 0;
         float f = 0.0F;

         for(int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (!itemstack.func_190926_b()) {
               f += (float)itemstack.func_190916_E() / (float)Math.min(inv.getInventoryStackLimit(), itemstack.getMaxStackSize());
               ++i;
            }
         }

         f /= (float)inv.getSizeInventory();
         return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
      }
   }

   protected void func_192389_a(World p_192389_1_, EntityPlayer p_192389_2_, InventoryCrafting p_192389_3_, InventoryCraftResult p_192389_4_) {
      if (!p_192389_1_.isRemote) {
         EntityPlayerMP entityplayermp = (EntityPlayerMP)p_192389_2_;
         ItemStack itemstack = ItemStack.field_190927_a;
         IRecipe irecipe = CraftingManager.func_192413_b(p_192389_3_, p_192389_1_);
         if (irecipe != null && (irecipe.func_192399_d() || !p_192389_1_.getGameRules().getBoolean("doLimitedCrafting") || entityplayermp.func_192037_E().func_193830_f(irecipe))) {
            p_192389_4_.func_193056_a(irecipe);
            itemstack = irecipe.getCraftingResult(p_192389_3_);
         }

         p_192389_4_.setInventorySlotContents(0, itemstack);
         entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, itemstack));
      }

   }
}
