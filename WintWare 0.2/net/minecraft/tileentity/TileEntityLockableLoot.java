package net.minecraft.tileentity;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public abstract class TileEntityLockableLoot extends TileEntityLockable implements ILootContainer {
   protected ResourceLocation lootTable;
   protected long lootTableSeed;
   protected String field_190577_o;

   protected boolean checkLootAndRead(NBTTagCompound compound) {
      if (compound.hasKey("LootTable", 8)) {
         this.lootTable = new ResourceLocation(compound.getString("LootTable"));
         this.lootTableSeed = compound.getLong("LootTableSeed");
         return true;
      } else {
         return false;
      }
   }

   protected boolean checkLootAndWrite(NBTTagCompound compound) {
      if (this.lootTable != null) {
         compound.setString("LootTable", this.lootTable.toString());
         if (this.lootTableSeed != 0L) {
            compound.setLong("LootTableSeed", this.lootTableSeed);
         }

         return true;
      } else {
         return false;
      }
   }

   public void fillWithLoot(@Nullable EntityPlayer player) {
      if (this.lootTable != null) {
         LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);
         this.lootTable = null;
         Random random;
         if (this.lootTableSeed == 0L) {
            random = new Random();
         } else {
            random = new Random(this.lootTableSeed);
         }

         LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world);
         if (player != null) {
            lootcontext$builder.withLuck(player.getLuck());
         }

         loottable.fillInventory(this, random, lootcontext$builder.build());
      }

   }

   public ResourceLocation getLootTable() {
      return this.lootTable;
   }

   public void setLootTable(ResourceLocation p_189404_1_, long p_189404_2_) {
      this.lootTable = p_189404_1_;
      this.lootTableSeed = p_189404_2_;
   }

   public boolean hasCustomName() {
      return this.field_190577_o != null && !this.field_190577_o.isEmpty();
   }

   public void func_190575_a(String p_190575_1_) {
      this.field_190577_o = p_190575_1_;
   }

   public ItemStack getStackInSlot(int index) {
      this.fillWithLoot((EntityPlayer)null);
      return (ItemStack)this.func_190576_q().get(index);
   }

   public ItemStack decrStackSize(int index, int count) {
      this.fillWithLoot((EntityPlayer)null);
      ItemStack itemstack = ItemStackHelper.getAndSplit(this.func_190576_q(), index, count);
      if (!itemstack.func_190926_b()) {
         this.markDirty();
      }

      return itemstack;
   }

   public ItemStack removeStackFromSlot(int index) {
      this.fillWithLoot((EntityPlayer)null);
      return ItemStackHelper.getAndRemove(this.func_190576_q(), index);
   }

   public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
      this.fillWithLoot((EntityPlayer)null);
      this.func_190576_q().set(index, stack);
      if (stack.func_190916_E() > this.getInventoryStackLimit()) {
         stack.func_190920_e(this.getInventoryStackLimit());
      }

      this.markDirty();
   }

   public boolean isUsableByPlayer(EntityPlayer player) {
      if (this.world.getTileEntity(this.pos) != this) {
         return false;
      } else {
         return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
      }
   }

   public void openInventory(EntityPlayer player) {
   }

   public void closeInventory(EntityPlayer player) {
   }

   public boolean isItemValidForSlot(int index, ItemStack stack) {
      return true;
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
      this.fillWithLoot((EntityPlayer)null);
      this.func_190576_q().clear();
   }

   protected abstract NonNullList<ItemStack> func_190576_q();
}
