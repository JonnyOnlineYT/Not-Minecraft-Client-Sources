package optifine;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public enum BlockDir
{
    DOWN(EnumFacing.DOWN),
    UP(EnumFacing.UP),
    NORTH(EnumFacing.NORTH),
    SOUTH(EnumFacing.SOUTH),
    WEST(EnumFacing.WEST),
    EAST(EnumFacing.EAST),
    NORTH_WEST(EnumFacing.NORTH, EnumFacing.WEST),
    NORTH_EAST(EnumFacing.NORTH, EnumFacing.EAST),
    SOUTH_WEST(EnumFacing.SOUTH, EnumFacing.WEST),
    SOUTH_EAST(EnumFacing.SOUTH, EnumFacing.EAST),
    DOWN_NORTH(EnumFacing.DOWN, EnumFacing.NORTH),
    DOWN_SOUTH(EnumFacing.DOWN, EnumFacing.SOUTH),
    UP_NORTH(EnumFacing.UP, EnumFacing.NORTH),
    UP_SOUTH(EnumFacing.UP, EnumFacing.SOUTH),
    DOWN_WEST(EnumFacing.DOWN, EnumFacing.WEST),
    DOWN_EAST(EnumFacing.DOWN, EnumFacing.EAST),
    UP_WEST(EnumFacing.UP, EnumFacing.WEST),
    UP_EAST(EnumFacing.UP, EnumFacing.EAST);

    private EnumFacing facing1;
    private EnumFacing facing2;

    BlockDir(EnumFacing p_i12_3_)
    {
        facing1 = p_i12_3_;
    }

    BlockDir(EnumFacing p_i13_3_, EnumFacing p_i13_4_)
    {
        facing1 = p_i13_3_;
        facing2 = p_i13_4_;
    }

    public EnumFacing getFacing1()
    {
        return facing1;
    }

    public EnumFacing getFacing2()
    {
        return facing2;
    }

    BlockPos offset(BlockPos p_offset_1_)
    {
        p_offset_1_ = p_offset_1_.offset(facing1, 1);

        if (facing2 != null)
        {
            p_offset_1_ = p_offset_1_.offset(facing2, 1);
        }

        return p_offset_1_;
    }

    public int getOffsetX()
    {
        int i = facing1.getFrontOffsetX();

        if (facing2 != null)
        {
            i += facing2.getFrontOffsetX();
        }

        return i;
    }

    public int getOffsetY()
    {
        int i = facing1.getFrontOffsetY();

        if (facing2 != null)
        {
            i += facing2.getFrontOffsetY();
        }

        return i;
    }

    public int getOffsetZ()
    {
        int i = facing1.getFrontOffsetZ();

        if (facing2 != null)
        {
            i += facing2.getFrontOffsetZ();
        }

        return i;
    }

    public boolean isDouble()
    {
        return facing2 != null;
    }
}
