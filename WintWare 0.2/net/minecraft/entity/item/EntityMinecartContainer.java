package net.minecraft.entity.item;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public abstract class EntityMinecartContainer extends EntityMinecart implements ILockableContainer, ILootContainer {
   private NonNullList<ItemStack> minecartContainerItems;
   private boolean dropContentsWhenDead;
   private ResourceLocation lootTable;
   private long lootTableSeed;

   public EntityMinecartContainer(World worldIn) {
      super(worldIn);
      this.minecartContainerItems = NonNullList.func_191197_a(36, ItemStack.field_190927_a);
      this.dropContentsWhenDead = true;
   }

   public EntityMinecartContainer(World worldIn, double x, double y, double z) {
      super(worldIn, x, y, z);
      this.minecartContainerItems = NonNullList.func_191197_a(36, ItemStack.field_190927_a);
      this.dropContentsWhenDead = true;
   }

   public void killMinecart(DamageSource source) {
      super.killMinecart(source);
      if (this.world.getGameRules().getBoolean("doEntityDrops")) {
         InventoryHelper.dropInventoryItems(this.world, (Entity)this, this);
      }

   }

   public boolean func_191420_l() {
      Iterator var1 = this.minecartContainerItems.iterator();

      ItemStack itemstack;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         itemstack = (ItemStack)var1.next();
      } while(itemstack.func_190926_b());

      return false;
   }

   public ItemStack getStackInSlot(int index) {
      this.addLoot((EntityPlayer)null);
      return (ItemStack)this.minecartContainerItems.get(index);
   }

   public ItemStack decrStackSize(int index, int count) {
      this.addLoot((EntityPlayer)null);
      return ItemStackHelper.getAndSplit(this.minecartContainerItems, index, count);
   }

   public ItemStack removeStackFromSlot(int index) {
      this.addLoot((EntityPlayer)null);
      ItemStack itemstack = (ItemStack)this.minecartContainerItems.get(index);
      if (itemstack.func_190926_b()) {
         return ItemStack.field_190927_a;
      } else {
         this.minecartContainerItems.set(index, ItemStack.field_190927_a);
         return itemstack;
      }
   }

   public void setInventorySlotContents(int index, ItemStack stack) {
      this.addLoot((EntityPlayer)null);
      this.minecartContainerItems.set(index, stack);
      if (!stack.func_190926_b() && stack.func_190916_E() > this.getInventoryStackLimit()) {
         stack.func_190920_e(this.getInventoryStackLimit());
      }

   }

   public void markDirty() {
   }

   public boolean isUsableByPlayer(EntityPlayer player) {
      if (this.isDead) {
         return false;
      } else {
         return player.getDistanceSqToEntity(this) <= 64.0D;
      }
   }

   public void openInventory(EntityPlayer player) {
   }

   public void closeInventory(EntityPlayer player) {
   }

   public boolean isItemValidForSlot(int index, ItemStack stack) {
      return true;
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   @Nullable
   public Entity changeDimension(int dimensionIn) {
      this.dropContentsWhenDead = false;
      return super.changeDimension(dimensionIn);
   }

   public void setDead() {
      if (this.dropContentsWhenDead) {
         InventoryHelper.dropInventoryItems(this.world, (Entity)this, this);
      }

      super.setDead();
   }

   public void setDropItemsWhenDead(boolean dropWhenDead) {
      this.dropContentsWhenDead = dropWhenDead;
   }

   public static void func_190574_b(DataFixer p_190574_0_, Class<?> p_190574_1_) {
      EntityMinecart.registerFixesMinecart(p_190574_0_, p_190574_1_);
      p_190574_0_.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(p_190574_1_, new String[]{"Items"}));
   }

   protected void writeEntityToNBT(NBTTagCompound compound) {
      super.writeEntityToNBT(compound);
      if (this.lootTable != null) {
         compound.setString("LootTable", this.lootTable.toString());
         if (this.lootTableSeed != 0L) {
            compound.setLong("LootTableSeed", this.lootTableSeed);
         }
      } else {
         ItemStackHelper.func_191282_a(compound, this.minecartContainerItems);
      }

   }

   protected void readEntityFromNBT(NBTTagCompound compound) {
      super.readEntityFromNBT(compound);
      this.minecartContainerItems = NonNullList.func_191197_a(this.getSizeInventory(), ItemStack.field_190927_a);
      if (compound.hasKey("LootTable", 8)) {
         this.lootTable = new ResourceLocation(compound.getString("LootTable"));
         this.lootTableSeed = compound.getLong("LootTableSeed");
      } else {
         ItemStackHelper.func_191283_b(compound, this.minecartContainerItems);
      }

   }

   public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
      if (!this.world.isRemote) {
         player.displayGUIChest(this);
      }

      return true;
   }

   protected void applyDrag() {
      float f = 0.98F;
      if (this.lootTable == null) {
         int i = 15 - Container.calcRedstoneFromInventory(this);
         f += (float)i * 0.001F;
      }

      this.motionX *= (double)f;
      this.motionY *= 0.0D;
      this.motionZ *= (double)f;
   }

   public int getField(int id) {
      return 0;
   }

   public void setField(int id, int value) {
   }

   public int getFieldCount() {
      return 0;
   }

   public boolean isLocked() {
      return false;
   }

   public void setLockCode(LockCode code) {
   }

   public LockCode getLockCode() {
      return LockCode.EMPTY_CODE;
   }

   public void addLoot(@Nullable EntityPlayer player) {
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

   public void clear() {
      this.addLoot((EntityPlayer)null);
      this.minecartContainerItems.clear();
   }

   public void setLootTable(ResourceLocation lootTableIn, long lootTableSeedIn) {
      this.lootTable = lootTableIn;
      this.lootTableSeed = lootTableSeedIn;
   }

   public ResourceLocation getLootTable() {
      return this.lootTable;
   }
}
