package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityChest extends TileEntityLockableLoot implements ITickable
{
    private NonNullList<ItemStack> chestContents = NonNullList.func_191197_a(27, ItemStack.EMPTY);

    /** Determines if the check for adjacent chests has taken place. */
    public boolean adjacentChestChecked;

    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityChest adjacentChestZNeg;

    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityChest adjacentChestXPos;

    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityChest adjacentChestXNeg;

    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityChest adjacentChestZPos;

    /** The current angle of the lid (between 0 and 1) */
    public float lidAngle;

    /** The angle of the lid last tick */
    public float prevLidAngle;

    /** The number of players currently using this chest */
    public int numPlayersUsing;

    /** Server sync counter (once per 20 ticks) */
    private int ticksSinceSync;
    private BlockChest.Type cachedChestType;

    public TileEntityChest()
    {
    }

    public TileEntityChest(BlockChest.Type typeIn)
    {
        cachedChestType = typeIn;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 27;
    }

    public boolean func_191420_l()
    {
        for (ItemStack itemstack : chestContents)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return hasCustomName() ? field_190577_o : "container.chest";
    }

    public static void registerFixesChest(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityChest.class, "Items"));
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        chestContents = NonNullList.func_191197_a(getSizeInventory(), ItemStack.EMPTY);

        if (!checkLootAndRead(compound))
        {
            ItemStackHelper.func_191283_b(compound, chestContents);
        }

        if (compound.hasKey("CustomName", 8))
        {
            field_190577_o = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (!checkLootAndWrite(compound))
        {
            ItemStackHelper.func_191282_a(compound, chestContents);
        }

        if (hasCustomName())
        {
            compound.setString("CustomName", field_190577_o);
        }

        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        adjacentChestChecked = false;
    }

    @SuppressWarnings("incomplete-switch")
    private void setNeighbor(TileEntityChest chestTe, EnumFacing side)
    {
        if (chestTe.isInvalid())
        {
            adjacentChestChecked = false;
        }
        else if (adjacentChestChecked)
        {
            switch (side)
            {
                case NORTH:
                    if (adjacentChestZNeg != chestTe)
                    {
                        adjacentChestChecked = false;
                    }

                    break;

                case SOUTH:
                    if (adjacentChestZPos != chestTe)
                    {
                        adjacentChestChecked = false;
                    }

                    break;

                case EAST:
                    if (adjacentChestXPos != chestTe)
                    {
                        adjacentChestChecked = false;
                    }

                    break;

                case WEST:
                    if (adjacentChestXNeg != chestTe)
                    {
                        adjacentChestChecked = false;
                    }
            }
        }
    }

    /**
     * Performs the check for adjacent chests to determine if this chest is double or not.
     */
    public void checkForAdjacentChests()
    {
        if (!adjacentChestChecked)
        {
            adjacentChestChecked = true;
            adjacentChestXNeg = getAdjacentChest(EnumFacing.WEST);
            adjacentChestXPos = getAdjacentChest(EnumFacing.EAST);
            adjacentChestZNeg = getAdjacentChest(EnumFacing.NORTH);
            adjacentChestZPos = getAdjacentChest(EnumFacing.SOUTH);
        }
    }

    @Nullable
    protected TileEntityChest getAdjacentChest(EnumFacing side)
    {
        BlockPos blockpos = pos.offset(side);

        if (isChestAt(blockpos))
        {
            TileEntity tileentity = world.getTileEntity(blockpos);

            if (tileentity instanceof TileEntityChest)
            {
                TileEntityChest tileentitychest = (TileEntityChest)tileentity;
                tileentitychest.setNeighbor(this, side.getOpposite());
                return tileentitychest;
            }
        }

        return null;
    }

    private boolean isChestAt(BlockPos posIn)
    {
        if (world == null)
        {
            return false;
        }
        else
        {
            Block block = world.getBlockState(posIn).getBlock();
            return block instanceof BlockChest && ((BlockChest)block).chestType == getChestType();
        }
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        checkForAdjacentChests();
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        ++ticksSinceSync;

        if (!world.isRemote && numPlayersUsing != 0 && (ticksSinceSync + i + j + k) % 200 == 0)
        {
            numPlayersUsing = 0;
            float f = 5.0F;

            for (EntityPlayer entityplayer : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((float)i - 5.0F, (float)j - 5.0F, (float)k - 5.0F, (float)(i + 1) + 5.0F, (float)(j + 1) + 5.0F, (float)(k + 1) + 5.0F)))
            {
                if (entityplayer.openContainer instanceof ContainerChest)
                {
                    IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();

                    if (iinventory == this || iinventory instanceof InventoryLargeChest && ((InventoryLargeChest)iinventory).isPartOfLargeChest(this))
                    {
                        ++numPlayersUsing;
                    }
                }
            }
        }

        prevLidAngle = lidAngle;
        float f1 = 0.1F;

        if (numPlayersUsing > 0 && lidAngle == 0.0F && adjacentChestZNeg == null && adjacentChestXNeg == null)
        {
            double d1 = (double)i + 0.5D;
            double d2 = (double)k + 0.5D;

            if (adjacentChestZPos != null)
            {
                d2 += 0.5D;
            }

            if (adjacentChestXPos != null)
            {
                d1 += 0.5D;
            }

            world.playSound(null, d1, (double)j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F)
        {
            float f2 = lidAngle;

            if (numPlayersUsing > 0)
            {
                lidAngle += 0.1F;
            }
            else
            {
                lidAngle -= 0.1F;
            }

            if (lidAngle > 1.0F)
            {
                lidAngle = 1.0F;
            }

            float f3 = 0.5F;

            if (lidAngle < 0.5F && f2 >= 0.5F && adjacentChestZNeg == null && adjacentChestXNeg == null)
            {
                double d3 = (double)i + 0.5D;
                double d0 = (double)k + 0.5D;

                if (adjacentChestZPos != null)
                {
                    d0 += 0.5D;
                }

                if (adjacentChestXPos != null)
                {
                    d3 += 0.5D;
                }

                world.playSound(null, d3, (double)j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (lidAngle < 0.0F)
            {
                lidAngle = 0.0F;
            }
        }
    }

    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    public void openInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (numPlayersUsing < 0)
            {
                numPlayersUsing = 0;
            }

            ++numPlayersUsing;
            world.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
            world.notifyNeighborsOfStateChange(pos, getBlockType(), false);

            if (getChestType() == BlockChest.Type.TRAP)
            {
                world.notifyNeighborsOfStateChange(pos.down(), getBlockType(), false);
            }
        }
    }

    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator() && getBlockType() instanceof BlockChest)
        {
            --numPlayersUsing;
            world.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
            world.notifyNeighborsOfStateChange(pos, getBlockType(), false);

            if (getChestType() == BlockChest.Type.TRAP)
            {
                world.notifyNeighborsOfStateChange(pos.down(), getBlockType(), false);
            }
        }
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate()
    {
        super.invalidate();
        updateContainingBlockInfo();
        checkForAdjacentChests();
    }

    public BlockChest.Type getChestType()
    {
        if (cachedChestType == null)
        {
            if (world == null || !(getBlockType() instanceof BlockChest))
            {
                return BlockChest.Type.BASIC;
            }

            cachedChestType = ((BlockChest) getBlockType()).chestType;
        }

        return cachedChestType;
    }

    public String getGuiID()
    {
        return "minecraft:chest";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        fillWithLoot(playerIn);
        return new ContainerChest(playerInventory, this, playerIn);
    }

    protected NonNullList<ItemStack> func_190576_q()
    {
        return chestContents;
    }
}
