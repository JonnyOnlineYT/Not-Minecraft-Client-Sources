package net.minecraft.block.state;

import com.google.common.base.Predicate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWorldState
{
    private final World world;
    private final BlockPos pos;
    private final boolean forceLoad;
    private IBlockState state;
    private TileEntity tileEntity;
    private boolean tileEntityInitialized;

    public BlockWorldState(World worldIn, BlockPos posIn, boolean forceLoadIn)
    {
        world = worldIn;
        pos = posIn;
        forceLoad = forceLoadIn;
    }

    /**
     * Gets the block state as currently held, or (if it has not gotten it from the world) loads it from the world.
     *  This will only look up the state from the world if {@link #forceLoad} is true or the block position is
     * loaded.
     */
    public IBlockState getBlockState()
    {
        if (state == null && (forceLoad || world.isBlockLoaded(pos)))
        {
            state = world.getBlockState(pos);
        }

        return state;
    }

    @Nullable

    /**
     * Gets the tile entity as currently held, or (if it has not gotten it from the world) loads it from the world.
     */
    public TileEntity getTileEntity()
    {
        if (tileEntity == null && !tileEntityInitialized)
        {
            tileEntity = world.getTileEntity(pos);
            tileEntityInitialized = true;
        }

        return tileEntity;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public static Predicate<BlockWorldState> hasState(Predicate<IBlockState> predicatesIn)
    {
        return new Predicate<BlockWorldState>()
        {
            public boolean apply(@Nullable BlockWorldState p_apply_1_)
            {
                return p_apply_1_ != null && predicatesIn.apply(p_apply_1_.getBlockState());
            }
        };
    }
}
