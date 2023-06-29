package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSourceImpl implements IBlockSource
{
    private final World worldObj;
    private final BlockPos pos;

    public BlockSourceImpl(World worldIn, BlockPos posIn)
    {
        worldObj = worldIn;
        pos = posIn;
    }

    public World getWorld()
    {
        return worldObj;
    }

    public double getX()
    {
        return (double) pos.getX() + 0.5D;
    }

    public double getY()
    {
        return (double) pos.getY() + 0.5D;
    }

    public double getZ()
    {
        return (double) pos.getZ() + 0.5D;
    }

    public BlockPos getBlockPos()
    {
        return pos;
    }

    /**
     * Gets the block state of this position and returns it.
     *  @return Block state in this position
     */
    public IBlockState getBlockState()
    {
        return worldObj.getBlockState(pos);
    }

    public <T extends TileEntity> T getBlockTileEntity()
    {
        return (T) worldObj.getTileEntity(pos);
    }
}
