package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.EnumDifficulty;

public class EntityAIBreakDoor extends EntityAIDoorInteract
{
    private int breakingTime;
    private int previousBreakProgress = -1;

    public EntityAIBreakDoor(EntityLiving entityIn)
    {
        super(entityIn);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!super.shouldExecute())
        {
            return false;
        }
        else if (!theEntity.world.getGameRules().getBoolean("mobGriefing"))
        {
            return false;
        }
        else
        {
            BlockDoor blockdoor = doorBlock;
            return !BlockDoor.isOpen(theEntity.world, doorPosition);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();
        breakingTime = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        double d0 = theEntity.getDistanceSq(doorPosition);
        boolean flag;

        if (breakingTime <= 240)
        {
            BlockDoor blockdoor = doorBlock;

            if (!BlockDoor.isOpen(theEntity.world, doorPosition) && d0 < 4.0D)
            {
                flag = true;
                return flag;
            }
        }

        flag = false;
        return flag;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        super.resetTask();
        theEntity.world.sendBlockBreakProgress(theEntity.getEntityId(), doorPosition, -1);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        super.updateTask();

        if (theEntity.getRNG().nextInt(20) == 0)
        {
            theEntity.world.playEvent(1019, doorPosition, 0);
        }

        ++breakingTime;
        int i = (int)((float) breakingTime / 240.0F * 10.0F);

        if (i != previousBreakProgress)
        {
            theEntity.world.sendBlockBreakProgress(theEntity.getEntityId(), doorPosition, i);
            previousBreakProgress = i;
        }

        if (breakingTime == 240 && theEntity.world.getDifficulty() == EnumDifficulty.HARD)
        {
            theEntity.world.setBlockToAir(doorPosition);
            theEntity.world.playEvent(1021, doorPosition, 0);
            theEntity.world.playEvent(2001, doorPosition, Block.getIdFromBlock(doorBlock));
        }
    }
}
