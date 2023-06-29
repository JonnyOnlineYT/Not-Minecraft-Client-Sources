package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITradePlayer extends EntityAIBase
{
    private final EntityVillager villager;

    public EntityAITradePlayer(EntityVillager villagerIn)
    {
        villager = villagerIn;
        setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!villager.isEntityAlive())
        {
            return false;
        }
        else if (villager.isInWater())
        {
            return false;
        }
        else if (!villager.onGround)
        {
            return false;
        }
        else if (villager.velocityChanged)
        {
            return false;
        }
        else
        {
            EntityPlayer entityplayer = villager.getCustomer();

            if (entityplayer == null)
            {
                return false;
            }
            else if (villager.getDistanceSqToEntity(entityplayer) > 16.0D)
            {
                return false;
            }
            else
            {
                return entityplayer.openContainer != null;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        villager.getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        villager.setCustomer(null);
    }
}
