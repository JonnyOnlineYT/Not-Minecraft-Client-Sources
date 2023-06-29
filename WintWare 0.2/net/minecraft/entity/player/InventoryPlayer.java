package net.minecraft.entity.player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class InventoryPlayer implements IInventory {
   public final NonNullList<ItemStack> mainInventory;
   public final NonNullList<ItemStack> armorInventory;
   public final NonNullList<ItemStack> offHandInventory;
   private final List<NonNullList<ItemStack>> allInventories;
   public int currentItem;
   public EntityPlayer player;
   private ItemStack itemStack;
   private int field_194017_h;

   public InventoryPlayer(EntityPlayer playerIn) {
      this.mainInventory = NonNullList.func_191197_a(36, ItemStack.field_190927_a);
      this.armorInventory = NonNullList.func_191197_a(4, ItemStack.field_190927_a);
      this.offHandInventory = NonNullList.func_191197_a(1, ItemStack.field_190927_a);
      this.allInventories = Arrays.asList(this.mainInventory, this.armorInventory, this.offHandInventory);
      this.itemStack = ItemStack.field_190927_a;
      this.player = playerIn;
   }

   public ItemStack getCurrentItem() {
      return isHotbar(this.currentItem) ? (ItemStack)this.mainInventory.get(this.currentItem) : ItemStack.field_190927_a;
   }

   public static int getHotbarSize() {
      return 9;
   }

   private boolean canMergeStacks(ItemStack stack1, ItemStack stack2) {
      return !stack1.func_190926_b() && this.stackEqualExact(stack1, stack2) && stack1.isStackable() && stack1.func_190916_E() < stack1.getMaxStackSize() && stack1.func_190916_E() < this.getInventoryStackLimit();
   }

   private boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
      return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2);
   }

   public int getFirstEmptyStack() {
      for(int i = 0; i < this.mainInventory.size(); ++i) {
         if (((ItemStack)this.mainInventory.get(i)).func_190926_b()) {
            return i;
         }
      }

      return -1;
   }

   public void setPickedItemStack(ItemStack stack) {
      int i = this.getSlotFor(stack);
      if (isHotbar(i)) {
         this.currentItem = i;
      } else if (i == -1) {
         this.currentItem = this.getBestHotbarSlot();
         if (!((ItemStack)this.mainInventory.get(this.currentItem)).func_190926_b()) {
            int j = this.getFirstEmptyStack();
            if (j != -1) {
               this.mainInventory.set(j, this.mainInventory.get(this.currentItem));
            }
         }

         this.mainInventory.set(this.currentItem, stack);
      } else {
         this.pickItem(i);
      }

   }

   public void pickItem(int index) {
      this.currentItem = this.getBestHotbarSlot();
      ItemStack itemstack = (ItemStack)this.mainInventory.get(this.currentItem);
      this.mainInventory.set(this.currentItem, this.mainInventory.get(index));
      this.mainInventory.set(index, itemstack);
   }

   public static boolean isHotbar(int index) {
      return index >= 0 && index < 9;
   }

   public int getSlotFor(ItemStack stack) {
      for(int i = 0; i < this.mainInventory.size(); ++i) {
         if (!((ItemStack)this.mainInventory.get(i)).func_190926_b() && this.stackEqualExact(stack, (ItemStack)this.mainInventory.get(i))) {
            return i;
         }
      }

      return -1;
   }

   public int func_194014_c(ItemStack p_194014_1_) {
      for(int i = 0; i < this.mainInventory.size(); ++i) {
         ItemStack itemstack = (ItemStack)this.mainInventory.get(i);
         if (!((ItemStack)this.mainInventory.get(i)).func_190926_b() && this.stackEqualExact(p_194014_1_, (ItemStack)this.mainInventory.get(i)) && !((ItemStack)this.mainInventory.get(i)).isItemDamaged() && !itemstack.isItemEnchanted() && !itemstack.hasDisplayName()) {
            return i;
         }
      }

      return -1;
   }

   public int getBestHotbarSlot() {
      int k;
      int l;
      for(k = 0; k < 9; ++k) {
         l = (this.currentItem + k) % 9;
         if (((ItemStack)this.mainInventory.get(l)).func_190926_b()) {
            return l;
         }
      }

      for(k = 0; k < 9; ++k) {
         l = (this.currentItem + k) % 9;
         if (!((ItemStack)this.mainInventory.get(l)).isItemEnchanted()) {
            return l;
         }
      }

      return this.currentItem;
   }

   public void changeCurrentItem(int direction) {
      if (direction > 0) {
         direction = 1;
      }

      if (direction < 0) {
         direction = -1;
      }

      for(this.currentItem -= direction; this.currentItem < 0; this.currentItem += 9) {
      }

      while(this.currentItem >= 9) {
         this.currentItem -= 9;
      }

   }

   public int clearMatchingItems(@Nullable Item itemIn, int metadataIn, int removeCount, @Nullable NBTTagCompound itemNBT) {
      int i = 0;

      int l;
      for(l = 0; l < this.getSizeInventory(); ++l) {
         ItemStack itemstack = this.getStackInSlot(l);
         if (!itemstack.func_190926_b() && (itemIn == null || itemstack.getItem() == itemIn) && (metadataIn <= -1 || itemstack.getMetadata() == metadataIn) && (itemNBT == null || NBTUtil.areNBTEquals(itemNBT, itemstack.getTagCompound(), true))) {
            int k = removeCount <= 0 ? itemstack.func_190916_E() : Math.min(removeCount - i, itemstack.func_190916_E());
            i += k;
            if (removeCount != 0) {
               itemstack.func_190918_g(k);
               if (itemstack.func_190926_b()) {
                  this.setInventorySlotContents(l, ItemStack.field_190927_a);
               }

               if (removeCount > 0 && i >= removeCount) {
                  return i;
               }
            }
         }
      }

      if (!this.itemStack.func_190926_b()) {
         if (itemIn != null && this.itemStack.getItem() != itemIn) {
            return i;
         }

         if (metadataIn > -1 && this.itemStack.getMetadata() != metadataIn) {
            return i;
         }

         if (itemNBT != null && !NBTUtil.areNBTEquals(itemNBT, this.itemStack.getTagCompound(), true)) {
            return i;
         }

         l = removeCount <= 0 ? this.itemStack.func_190916_E() : Math.min(removeCount - i, this.itemStack.func_190916_E());
         i += l;
         if (removeCount != 0) {
            this.itemStack.func_190918_g(l);
            if (this.itemStack.func_190926_b()) {
               this.itemStack = ItemStack.field_190927_a;
            }

            if (removeCount > 0 && i >= removeCount) {
               return i;
            }
         }
      }

      return i;
   }

   private int storePartialItemStack(ItemStack itemStackIn) {
      int i = this.storeItemStack(itemStackIn);
      if (i == -1) {
         i = this.getFirstEmptyStack();
      }

      return i == -1 ? itemStackIn.func_190916_E() : this.func_191973_d(i, itemStackIn);
   }

   private int func_191973_d(int p_191973_1_, ItemStack p_191973_2_) {
      Item item = p_191973_2_.getItem();
      int i = p_191973_2_.func_190916_E();
      ItemStack itemstack = this.getStackInSlot(p_191973_1_);
      if (itemstack.func_190926_b()) {
         itemstack = new ItemStack(item, 0, p_191973_2_.getMetadata());
         if (p_191973_2_.hasTagCompound()) {
            itemstack.setTagCompound(p_191973_2_.getTagCompound().copy());
         }

         this.setInventorySlotContents(p_191973_1_, itemstack);
      }

      int j = i;
      if (i > itemstack.getMaxStackSize() - itemstack.func_190916_E()) {
         j = itemstack.getMaxStackSize() - itemstack.func_190916_E();
      }

      if (j > this.getInventoryStackLimit() - itemstack.func_190916_E()) {
         j = this.getInventoryStackLimit() - itemstack.func_190916_E();
      }

      if (j == 0) {
         return i;
      } else {
         i -= j;
         itemstack.func_190917_f(j);
         itemstack.func_190915_d(5);
         return i;
      }
   }

   public int storeItemStack(ItemStack itemStackIn) {
      if (this.canMergeStacks(this.getStackInSlot(this.currentItem), itemStackIn)) {
         return this.currentItem;
      } else if (this.canMergeStacks(this.getStackInSlot(40), itemStackIn)) {
         return 40;
      } else {
         for(int i = 0; i < this.mainInventory.size(); ++i) {
            if (this.canMergeStacks((ItemStack)this.mainInventory.get(i), itemStackIn)) {
               return i;
            }
         }

         return -1;
      }
   }

   public void decrementAnimations() {
      Iterator var1 = this.allInventories.iterator();

      while(var1.hasNext()) {
         NonNullList<ItemStack> nonnulllist = (NonNullList)var1.next();

         for(int i = 0; i < nonnulllist.size(); ++i) {
            if (!((ItemStack)nonnulllist.get(i)).func_190926_b()) {
               ((ItemStack)nonnulllist.get(i)).updateAnimation(this.player.world, this.player, i, this.currentItem == i);
            }
         }
      }

   }

   public boolean addItemStackToInventory(ItemStack itemStackIn) {
      return this.func_191971_c(-1, itemStackIn);
   }

   public boolean func_191971_c(int p_191971_1_, final ItemStack p_191971_2_) {
      if (p_191971_2_.func_190926_b()) {
         return false;
      } else {
         try {
            if (p_191971_2_.isItemDamaged()) {
               if (p_191971_1_ == -1) {
                  p_191971_1_ = this.getFirstEmptyStack();
               }

               if (p_191971_1_ >= 0) {
                  this.mainInventory.set(p_191971_1_, p_191971_2_.copy());
                  ((ItemStack)this.mainInventory.get(p_191971_1_)).func_190915_d(5);
                  p_191971_2_.func_190920_e(0);
                  return true;
               } else if (this.player.capabilities.isCreativeMode) {
                  p_191971_2_.func_190920_e(0);
                  return true;
               } else {
                  return false;
               }
            } else {
               int i;
               do {
                  i = p_191971_2_.func_190916_E();
                  if (p_191971_1_ == -1) {
                     p_191971_2_.func_190920_e(this.storePartialItemStack(p_191971_2_));
                  } else {
                     p_191971_2_.func_190920_e(this.func_191973_d(p_191971_1_, p_191971_2_));
                  }
               } while(!p_191971_2_.func_190926_b() && p_191971_2_.func_190916_E() < i);

               if (p_191971_2_.func_190916_E() == i && this.player.capabilities.isCreativeMode) {
                  p_191971_2_.func_190920_e(0);
                  return true;
               } else {
                  return p_191971_2_.func_190916_E() < i;
               }
            }
         } catch (Throwable var6) {
            CrashReport crashreport = CrashReport.makeCrashReport(var6, "Adding item to inventory");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
            crashreportcategory.addCrashSection("Item ID", Item.getIdFromItem(p_191971_2_.getItem()));
            crashreportcategory.addCrashSection("Item data", p_191971_2_.getMetadata());
            crashreportcategory.setDetail("Item name", new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  return p_191971_2_.getDisplayName();
               }
            });
            throw new ReportedException(crashreport);
         }
      }
   }

   public void func_191975_a(World p_191975_1_, ItemStack p_191975_2_) {
      if (!p_191975_1_.isRemote) {
         while(!p_191975_2_.func_190926_b()) {
            int i = this.storeItemStack(p_191975_2_);
            if (i == -1) {
               i = this.getFirstEmptyStack();
            }

            if (i == -1) {
               this.player.dropItem(p_191975_2_, false);
               break;
            }

            int j = p_191975_2_.getMaxStackSize() - this.getStackInSlot(i).func_190916_E();
            if (this.func_191971_c(i, p_191975_2_.splitStack(j))) {
               ((EntityPlayerMP)this.player).connection.sendPacket(new SPacketSetSlot(-2, i, this.getStackInSlot(i)));
            }
         }
      }

   }

   public ItemStack decrStackSize(int index, int count) {
      List<ItemStack> list = null;

      NonNullList nonnulllist;
      for(Iterator var4 = this.allInventories.iterator(); var4.hasNext(); index -= nonnulllist.size()) {
         nonnulllist = (NonNullList)var4.next();
         if (index < nonnulllist.size()) {
            list = nonnulllist;
            break;
         }
      }

      return list != null && !((ItemStack)list.get(index)).func_190926_b() ? ItemStackHelper.getAndSplit(list, index, count) : ItemStack.field_190927_a;
   }

   public void deleteStack(ItemStack stack) {
      Iterator var2 = this.allInventories.iterator();

      while(true) {
         while(var2.hasNext()) {
            NonNullList<ItemStack> nonnulllist = (NonNullList)var2.next();

            for(int i = 0; i < nonnulllist.size(); ++i) {
               if (nonnulllist.get(i) == stack) {
                  nonnulllist.set(i, ItemStack.field_190927_a);
                  break;
               }
            }
         }

         return;
      }
   }

   public ItemStack removeStackFromSlot(int index) {
      NonNullList<ItemStack> nonnulllist = null;

      NonNullList nonnulllist1;
      for(Iterator var3 = this.allInventories.iterator(); var3.hasNext(); index -= nonnulllist1.size()) {
         nonnulllist1 = (NonNullList)var3.next();
         if (index < nonnulllist1.size()) {
            nonnulllist = nonnulllist1;
            break;
         }
      }

      if (nonnulllist != null && !((ItemStack)nonnulllist.get(index)).func_190926_b()) {
         ItemStack itemstack = (ItemStack)nonnulllist.get(index);
         nonnulllist.set(index, ItemStack.field_190927_a);
         return itemstack;
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public void setInventorySlotContents(int index, ItemStack stack) {
      NonNullList<ItemStack> nonnulllist = null;

      NonNullList nonnulllist1;
      for(Iterator var4 = this.allInventories.iterator(); var4.hasNext(); index -= nonnulllist1.size()) {
         nonnulllist1 = (NonNullList)var4.next();
         if (index < nonnulllist1.size()) {
            nonnulllist = nonnulllist1;
            break;
         }
      }

      if (nonnulllist != null) {
         nonnulllist.set(index, stack);
      }

   }

   public float getStrVsBlock(IBlockState state) {
      float f = 1.0F;
      if (!((ItemStack)this.mainInventory.get(this.currentItem)).func_190926_b()) {
         f *= ((ItemStack)this.mainInventory.get(this.currentItem)).getStrVsBlock(state);
      }

      return f;
   }

   public NBTTagList writeToNBT(NBTTagList nbtTagListIn) {
      int k;
      NBTTagCompound nbttagcompound2;
      for(k = 0; k < this.mainInventory.size(); ++k) {
         if (!((ItemStack)this.mainInventory.get(k)).func_190926_b()) {
            nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.setByte("Slot", (byte)k);
            ((ItemStack)this.mainInventory.get(k)).writeToNBT(nbttagcompound2);
            nbtTagListIn.appendTag(nbttagcompound2);
         }
      }

      for(k = 0; k < this.armorInventory.size(); ++k) {
         if (!((ItemStack)this.armorInventory.get(k)).func_190926_b()) {
            nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.setByte("Slot", (byte)(k + 100));
            ((ItemStack)this.armorInventory.get(k)).writeToNBT(nbttagcompound2);
            nbtTagListIn.appendTag(nbttagcompound2);
         }
      }

      for(k = 0; k < this.offHandInventory.size(); ++k) {
         if (!((ItemStack)this.offHandInventory.get(k)).func_190926_b()) {
            nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.setByte("Slot", (byte)(k + 150));
            ((ItemStack)this.offHandInventory.get(k)).writeToNBT(nbttagcompound2);
            nbtTagListIn.appendTag(nbttagcompound2);
         }
      }

      return nbtTagListIn;
   }

   public void readFromNBT(NBTTagList nbtTagListIn) {
      this.mainInventory.clear();
      this.armorInventory.clear();
      this.offHandInventory.clear();

      for(int i = 0; i < nbtTagListIn.tagCount(); ++i) {
         NBTTagCompound nbttagcompound = nbtTagListIn.getCompoundTagAt(i);
         int j = nbttagcompound.getByte("Slot") & 255;
         ItemStack itemstack = new ItemStack(nbttagcompound);
         if (!itemstack.func_190926_b()) {
            if (j >= 0 && j < this.mainInventory.size()) {
               this.mainInventory.set(j, itemstack);
            } else if (j >= 100 && j < this.armorInventory.size() + 100) {
               this.armorInventory.set(j - 100, itemstack);
            } else if (j >= 150 && j < this.offHandInventory.size() + 150) {
               this.offHandInventory.set(j - 150, itemstack);
            }
         }
      }

   }

   public int getSizeInventory() {
      return this.mainInventory.size() + this.armorInventory.size() + this.offHandInventory.size();
   }

   public boolean func_191420_l() {
      Iterator var1 = this.mainInventory.iterator();

      ItemStack itemstack2;
      do {
         if (!var1.hasNext()) {
            var1 = this.armorInventory.iterator();

            do {
               if (!var1.hasNext()) {
                  var1 = this.offHandInventory.iterator();

                  do {
                     if (!var1.hasNext()) {
                        return true;
                     }

                     itemstack2 = (ItemStack)var1.next();
                  } while(itemstack2.func_190926_b());

                  return false;
               }

               itemstack2 = (ItemStack)var1.next();
            } while(itemstack2.func_190926_b());

            return false;
         }

         itemstack2 = (ItemStack)var1.next();
      } while(itemstack2.func_190926_b());

      return false;
   }

   public ItemStack getStackInSlot(int index) {
      List<ItemStack> list = null;

      NonNullList nonnulllist;
      for(Iterator var3 = this.allInventories.iterator(); var3.hasNext(); index -= nonnulllist.size()) {
         nonnulllist = (NonNullList)var3.next();
         if (index < nonnulllist.size()) {
            list = nonnulllist;
            break;
         }
      }

      return list == null ? ItemStack.field_190927_a : (ItemStack)list.get(index);
   }

   public String getName() {
      return "container.inventory";
   }

   public boolean hasCustomName() {
      return false;
   }

   public ITextComponent getDisplayName() {
      return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean canHarvestBlock(IBlockState state) {
      if (state.getMaterial().isToolNotRequired()) {
         return true;
      } else {
         ItemStack itemstack = this.getStackInSlot(this.currentItem);
         return !itemstack.func_190926_b() ? itemstack.canHarvestBlock(state) : false;
      }
   }

   public ItemStack armorItemInSlot(int slotIn) {
      return (ItemStack)this.armorInventory.get(slotIn);
   }

   public void damageArmor(float damage) {
      damage /= 4.0F;
      if (damage < 1.0F) {
         damage = 1.0F;
      }

      for(int i = 0; i < this.armorInventory.size(); ++i) {
         ItemStack itemstack = (ItemStack)this.armorInventory.get(i);
         if (itemstack.getItem() instanceof ItemArmor) {
            itemstack.damageItem((int)damage, this.player);
         }
      }

   }

   public void dropAllItems() {
      Iterator var1 = this.allInventories.iterator();

      while(var1.hasNext()) {
         List<ItemStack> list = (List)var1.next();

         for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = (ItemStack)list.get(i);
            if (!itemstack.func_190926_b()) {
               this.player.dropItem(itemstack, true, false);
               list.set(i, ItemStack.field_190927_a);
            }
         }
      }

   }

   public void markDirty() {
      ++this.field_194017_h;
   }

   public int func_194015_p() {
      return this.field_194017_h;
   }

   public void setItemStack(ItemStack itemStackIn) {
      this.itemStack = itemStackIn;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public boolean isUsableByPlayer(EntityPlayer player) {
      if (this.player.isDead) {
         return false;
      } else {
         return player.getDistanceSqToEntity(this.player) <= 64.0D;
      }
   }

   public boolean hasItemStack(ItemStack itemStackIn) {
      Iterator var2 = this.allInventories.iterator();

      while(var2.hasNext()) {
         List<ItemStack> list = (List)var2.next();
         Iterator iterator = list.iterator();

         while(iterator.hasNext()) {
            ItemStack itemstack = (ItemStack)iterator.next();
            if (!itemstack.func_190926_b() && itemstack.isItemEqual(itemStackIn)) {
               return true;
            }
         }
      }

      return false;
   }

   public void openInventory(EntityPlayer player) {
   }

   public void closeInventory(EntityPlayer player) {
   }

   public boolean isItemValidForSlot(int index, ItemStack stack) {
      return true;
   }

   public void copyInventory(InventoryPlayer playerInventory) {
      for(int i = 0; i < this.getSizeInventory(); ++i) {
         this.setInventorySlotContents(i, playerInventory.getStackInSlot(i));
      }

      this.currentItem = playerInventory.currentItem;
   }

   public int getField(int id) {
      return 0;
   }

   public void setField(int id, int value) {
   }

   public int getFieldCount() {
      return 0;
   }

   public void clear() {
      Iterator var1 = this.allInventories.iterator();

      while(var1.hasNext()) {
         List<ItemStack> list = (List)var1.next();
         list.clear();
      }

   }

   public void func_194016_a(RecipeItemHelper p_194016_1_, boolean p_194016_2_) {
      Iterator var3 = this.mainInventory.iterator();

      while(var3.hasNext()) {
         ItemStack itemstack = (ItemStack)var3.next();
         p_194016_1_.func_194112_a(itemstack);
      }

      if (p_194016_2_) {
         p_194016_1_.func_194112_a((ItemStack)this.offHandInventory.get(0));
      }

   }
}
