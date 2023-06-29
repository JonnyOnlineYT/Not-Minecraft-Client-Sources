package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class EntityAIMoveToBlock extends EntityAIBase
{
    private final EntityCreature theEntity;
    private final double movementSpeed;

    /** Controls task execution delay */
    protected int runDelay;
    private int timeoutCounter;
    private int maxStayTicks;

    /** Block to move to */
    protected BlockPos destinationBlock = BlockPos.ORIGIN;
    private boolean isAboveDestination;
    private final int searchLength;

    public EntityAIMoveToBlock(EntityCreature creature, double speedIn, int length)
    {
        theEntity = creature;
        movementSpeed = speedIn;
        searchLength = length;
        setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (runDelay > 0)
        {
            --runDelay;
            return false;
        }
        else
        {
            runDelay = 200 + theEntity.getRNG().nextInt(200);
            return searchForDestination();
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return timeoutCounter >= -maxStayTicks && timeoutCounter <= 1200 && shouldMoveTo(theEntity.world, destinationBlock);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        theEntity.getNavigator().tryMoveToXYZ((double)((float) destinationBlock.getX()) + 0.5D, destinationBlock.getY() + 1, (double)((float) destinationBlock.getZ()) + 0.5D, movementSpeed);
        timeoutCounter = 0;
        maxStayTicks = theEntity.getRNG().nextInt(theEntity.getRNG().nextInt(1200) + 1200) + 1200;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (theEntity.getDistanceSqToCenter(destinationBlock.up()) > 1.0D)
        {
            isAboveDestination = false;
            ++timeoutCounter;

            if (timeoutCounter % 40 == 0)
            {
                theEntity.getNavigator().tryMoveToXYZ((double)((float) destinationBlock.getX()) + 0.5D, destinationBlock.getY() + 1, (double)((float) destinationBlock.getZ()) + 0.5D, movementSpeed);
            }
        }
        else
        {
            isAboveDestination = true;
            --timeoutCounter;
        }
    }

    protected boolean getIsAboveDestination()
    {
        return isAboveDestination;
    }

    /**
     * Searches and sets new destination block and returns true if a suitable block (specified in {@link
     * net.minecraft.entity.ai.EntityAIMoveToBlock#shouldMoveTo(World, BlockPos) EntityAIMoveToBlock#shouldMoveTo(World,
     * BlockPos)}) can be found.
     */
    private boolean searchForDestination()
    {
        int i = searchLength;
        int j = 1;
        BlockPos blockpos = new BlockPos(theEntity);

        for (int k = 0; k <= 1; k = k > 0 ? -k : 1 - k)
        {
            for (int l = 0; l < i; ++l)
            {
                for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1)
                {
                    for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1)
                    {
                        BlockPos blockpos1 = blockpos.add(i1, k - 1, j1);

                        if (theEntity.isWithinHomeDistanceFromPosition(blockpos1) && shouldMoveTo(theEntity.world, blockpos1))
                        {
                            destinationBlock = blockpos1;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Return true to set given position as destination
     */
    protected abstract boolean shouldMoveTo(World worldIn, BlockPos pos);
}
