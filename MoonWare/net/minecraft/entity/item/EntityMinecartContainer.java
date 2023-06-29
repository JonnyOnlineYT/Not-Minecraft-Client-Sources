package net.minecraft.entity.item;

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
import net.minecraft.util.Namespaced;
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

public abstract class EntityMinecartContainer extends EntityMinecart implements ILockableContainer, ILootContainer
{
    private NonNullList<ItemStack> minecartContainerItems = NonNullList.func_191197_a(36, ItemStack.EMPTY);

    /**
     * When set to true, the minecart will drop all items when setDead() is called. When false (such as when travelling
     * dimensions) it preserves its contents.
     */
    private boolean dropContentsWhenDead = true;
    private Namespaced lootTable;
    private long lootTableSeed;

    public EntityMinecartContainer(World worldIn)
    {
        super(worldIn);
    }

    public EntityMinecartContainer(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public void killMinecart(DamageSource source)
    {
        super.killMinecart(source);

        if (world.getGameRules().getBoolean("doEntityDrops"))
        {
            InventoryHelper.dropInventoryItems(world, this, this);
        }
    }

    public boolean func_191420_l()
    {
        for (ItemStack itemstack : minecartContainerItems)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index)
    {
        addLoot(null);
        return minecartContainerItems.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        addLoot(null);
        return ItemStackHelper.getAndSplit(minecartContainerItems, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        addLoot(null);
        ItemStack itemstack = minecartContainerItems.get(index);

        if (itemstack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            minecartContainerItems.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        addLoot(null);
        minecartContainerItems.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit())
        {
            stack.func_190920_e(getInventoryStackLimit());
        }
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (isDead)
        {
            return false;
        }
        else
        {
            return player.getDistanceSqToEntity(this) <= 64.0D;
        }
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Nullable
    public Entity changeDimension(int dimensionIn)
    {
        dropContentsWhenDead = false;
        return super.changeDimension(dimensionIn);
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        if (dropContentsWhenDead)
        {
            InventoryHelper.dropInventoryItems(world, this, this);
        }

        super.setDead();
    }

    /**
     * Sets whether this entity should drop its items when setDead() is called. This applies to container minecarts.
     */
    public void setDropItemsWhenDead(boolean dropWhenDead)
    {
        dropContentsWhenDead = dropWhenDead;
    }

    public static void func_190574_b(DataFixer p_190574_0_, Class<?> p_190574_1_)
    {
        EntityMinecart.registerFixesMinecart(p_190574_0_, p_190574_1_);
        p_190574_0_.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(p_190574_1_, "Items"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (lootTable != null)
        {
            compound.setString("LootTable", lootTable.toString());

            if (lootTableSeed != 0L)
            {
                compound.setLong("LootTableSeed", lootTableSeed);
            }
        }
        else
        {
            ItemStackHelper.func_191282_a(compound, minecartContainerItems);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        minecartContainerItems = NonNullList.func_191197_a(getSizeInventory(), ItemStack.EMPTY);

        if (compound.hasKey("LootTable", 8))
        {
            lootTable = new Namespaced(compound.getString("LootTable"));
            lootTableSeed = compound.getLong("LootTableSeed");
        }
        else
        {
            ItemStackHelper.func_191283_b(compound, minecartContainerItems);
        }
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand stack)
    {
        if (!world.isRemote)
        {
            player.displayGUIChest(this);
        }

        return true;
    }

    protected void applyDrag()
    {
        float f = 0.98F;

        if (lootTable == null)
        {
            int i = 15 - Container.calcRedstoneFromInventory(this);
            f += (float)i * 0.001F;
        }

        motionX *= f;
        motionY *= 0.0D;
        motionZ *= f;
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value)
    {
    }

    public int getFieldCount()
    {
        return 0;
    }

    public boolean isLocked()
    {
        return false;
    }

    public void setLockCode(LockCode code)
    {
    }

    public LockCode getLockCode()
    {
        return LockCode.EMPTY_CODE;
    }

    /**
     * Adds loot to the minecart's contents.
     */
    public void addLoot(@Nullable EntityPlayer player)
    {
        if (lootTable != null)
        {
            LootTable loottable = world.getLootTableManager().getLootTableFromLocation(lootTable);
            lootTable = null;
            Random random;

            if (lootTableSeed == 0L)
            {
                random = new Random();
            }
            else
            {
                random = new Random(lootTableSeed);
            }

            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) world);

            if (player != null)
            {
                lootcontext$builder.withLuck(player.getLuck());
            }

            loottable.fillInventory(this, random, lootcontext$builder.build());
        }
    }

    public void clear()
    {
        addLoot(null);
        minecartContainerItems.clear();
    }

    public void setLootTable(Namespaced lootTableIn, long lootTableSeedIn)
    {
        lootTable = lootTableIn;
        lootTableSeed = lootTableSeedIn;
    }

    public Namespaced getLootTable()
    {
        return lootTable;
    }
}
