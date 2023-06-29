package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIOcelotSit extends EntityAIMoveToBlock
{
    private final EntityOcelot ocelot;

    public EntityAIOcelotSit(EntityOcelot ocelotIn, double p_i45315_2_)
    {
        super(ocelotIn, p_i45315_2_, 8);
        ocelot = ocelotIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return ocelot.isTamed() && !ocelot.isSitting() && super.shouldExecute();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();
        ocelot.getAISit().setSitting(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        super.resetTask();
        ocelot.setSitting(false);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        super.updateTask();
        ocelot.getAISit().setSitting(false);

        if (!getIsAboveDestination())
        {
            ocelot.setSitting(false);
        }
        else if (!ocelot.isSitting())
        {
            ocelot.setSitting(true);
        }
    }

    /**
     * Return true to set given position as destination
     */
    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        if (!worldIn.isAirBlock(pos.up()))
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (block == Blocks.CHEST)
            {
                TileEntity tileentity = worldIn.getTileEntity(pos);

                return tileentity instanceof TileEntityChest && ((TileEntityChest) tileentity).numPlayersUsing < 1;
            }
            else
            {
                if (block == Blocks.LIT_FURNACE)
                {
                    return true;
                }

                return block == Blocks.BED && iblockstate.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD;
            }
        }
    }
}
