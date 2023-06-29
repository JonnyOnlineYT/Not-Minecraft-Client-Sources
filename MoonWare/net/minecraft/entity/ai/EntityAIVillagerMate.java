package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityAIVillagerMate extends EntityAIBase
{
    private final EntityVillager villagerObj;
    private EntityVillager mate;
    private final World worldObj;
    private int matingTimeout;
    Village villageObj;

    public EntityAIVillagerMate(EntityVillager villagerIn)
    {
        villagerObj = villagerIn;
        worldObj = villagerIn.world;
        setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (villagerObj.getGrowingAge() != 0)
        {
            return false;
        }
        else if (villagerObj.getRNG().nextInt(500) != 0)
        {
            return false;
        }
        else
        {
            villageObj = worldObj.getVillageCollection().getNearestVillage(new BlockPos(villagerObj), 0);

            if (villageObj == null)
            {
                return false;
            }
            else if (checkSufficientDoorsPresentForNewVillager() && villagerObj.getIsWillingToMate(true))
            {
                Entity entity = worldObj.findNearestEntityWithinAABB(EntityVillager.class, villagerObj.getEntityBoundingBox().expand(8.0D, 3.0D, 8.0D), villagerObj);

                if (entity == null)
                {
                    return false;
                }
                else
                {
                    mate = (EntityVillager)entity;
                    return mate.getGrowingAge() == 0 && mate.getIsWillingToMate(true);
                }
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        matingTimeout = 300;
        villagerObj.setMating(true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        villageObj = null;
        mate = null;
        villagerObj.setMating(false);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return matingTimeout >= 0 && checkSufficientDoorsPresentForNewVillager() && villagerObj.getGrowingAge() == 0 && villagerObj.getIsWillingToMate(false);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        --matingTimeout;
        villagerObj.getLookHelper().setLookPositionWithEntity(mate, 10.0F, 30.0F);

        if (villagerObj.getDistanceSqToEntity(mate) > 2.25D)
        {
            villagerObj.getNavigator().tryMoveToEntityLiving(mate, 0.25D);
        }
        else if (matingTimeout == 0 && mate.isMating())
        {
            giveBirth();
        }

        if (villagerObj.getRNG().nextInt(35) == 0)
        {
            worldObj.setEntityState(villagerObj, (byte)12);
        }
    }

    private boolean checkSufficientDoorsPresentForNewVillager()
    {
        if (!villageObj.isMatingSeason())
        {
            return false;
        }
        else
        {
            int i = (int)((double)((float) villageObj.getNumVillageDoors()) * 0.35D);
            return villageObj.getNumVillagers() < i;
        }
    }

    private void giveBirth()
    {
        EntityVillager entityvillager = villagerObj.createChild(mate);
        mate.setGrowingAge(6000);
        villagerObj.setGrowingAge(6000);
        mate.setIsWillingToMate(false);
        villagerObj.setIsWillingToMate(false);
        entityvillager.setGrowingAge(-24000);
        entityvillager.setLocationAndAngles(villagerObj.posX, villagerObj.posY, villagerObj.posZ, 0.0F, 0.0F);
        worldObj.spawnEntityInWorld(entityvillager);
        worldObj.setEntityState(entityvillager, (byte)12);
    }
}
