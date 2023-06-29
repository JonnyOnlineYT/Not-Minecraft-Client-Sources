package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIFollowGolem extends EntityAIBase
{
    private final EntityVillager theVillager;
    private EntityIronGolem theGolem;
    private int takeGolemRoseTick;
    private boolean tookGolemRose;

    public EntityAIFollowGolem(EntityVillager theVillagerIn)
    {
        theVillager = theVillagerIn;
        setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (theVillager.getGrowingAge() >= 0)
        {
            return false;
        }
        else if (!theVillager.world.isDaytime())
        {
            return false;
        }
        else
        {
            List<EntityIronGolem> list = theVillager.world.getEntitiesWithinAABB(EntityIronGolem.class, theVillager.getEntityBoundingBox().expand(6.0D, 2.0D, 6.0D));

            if (list.isEmpty())
            {
                return false;
            }
            else
            {
                for (EntityIronGolem entityirongolem : list)
                {
                    if (entityirongolem.getHoldRoseTick() > 0)
                    {
                        theGolem = entityirongolem;
                        break;
                    }
                }

                return theGolem != null;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return theGolem.getHoldRoseTick() > 0;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        takeGolemRoseTick = theVillager.getRNG().nextInt(320);
        tookGolemRose = false;
        theGolem.getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        theGolem = null;
        theVillager.getNavigator().clearPathEntity();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        theVillager.getLookHelper().setLookPositionWithEntity(theGolem, 30.0F, 30.0F);

        if (theGolem.getHoldRoseTick() == takeGolemRoseTick)
        {
            theVillager.getNavigator().tryMoveToEntityLiving(theGolem, 0.5D);
            tookGolemRose = true;
        }

        if (tookGolemRose && theVillager.getDistanceSqToEntity(theGolem) < 4.0D)
        {
            theGolem.setHoldingRose(false);
            theVillager.getNavigator().clearPathEntity();
        }
    }
}
