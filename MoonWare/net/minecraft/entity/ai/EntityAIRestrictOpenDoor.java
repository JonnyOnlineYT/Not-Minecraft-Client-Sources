package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIRestrictOpenDoor extends EntityAIBase
{
    private final EntityCreature entityObj;
    private VillageDoorInfo frontDoor;

    public EntityAIRestrictOpenDoor(EntityCreature creatureIn)
    {
        entityObj = creatureIn;

        if (!(creatureIn.getNavigator() instanceof PathNavigateGround))
        {
            throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (entityObj.world.isDaytime())
        {
            return false;
        }
        else
        {
            BlockPos blockpos = new BlockPos(entityObj);
            Village village = entityObj.world.getVillageCollection().getNearestVillage(blockpos, 16);

            if (village == null)
            {
                return false;
            }
            else
            {
                frontDoor = village.getNearestDoor(blockpos);

                if (frontDoor == null)
                {
                    return false;
                }
                else
                {
                    return (double) frontDoor.getDistanceToInsideBlockSq(blockpos) < 2.25D;
                }
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (entityObj.world.isDaytime())
        {
            return false;
        }
        else
        {
            return !frontDoor.getIsDetachedFromVillageFlag() && frontDoor.isInsideSide(new BlockPos(entityObj));
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        ((PathNavigateGround) entityObj.getNavigator()).setBreakDoors(false);
        ((PathNavigateGround) entityObj.getNavigator()).setEnterDoors(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        ((PathNavigateGround) entityObj.getNavigator()).setBreakDoors(true);
        ((PathNavigateGround) entityObj.getNavigator()).setEnterDoors(true);
        frontDoor = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        frontDoor.incrementDoorOpeningRestrictionCounter();
    }
}
