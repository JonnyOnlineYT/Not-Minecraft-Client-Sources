package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityAIBeg extends EntityAIBase
{
    private final EntityWolf theWolf;
    private EntityPlayer thePlayer;
    private final World worldObject;
    private final float minPlayerDistance;
    private int timeoutCounter;

    public EntityAIBeg(EntityWolf wolf, float minDistance)
    {
        theWolf = wolf;
        worldObject = wolf.world;
        minPlayerDistance = minDistance;
        setMutexBits(2);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        thePlayer = worldObject.getClosestPlayerToEntity(theWolf, minPlayerDistance);
        return thePlayer != null && hasPlayerGotBoneInHand(thePlayer);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (!thePlayer.isEntityAlive())
        {
            return false;
        }
        else if (theWolf.getDistanceSqToEntity(thePlayer) > (double)(minPlayerDistance * minPlayerDistance))
        {
            return false;
        }
        else
        {
            return timeoutCounter > 0 && hasPlayerGotBoneInHand(thePlayer);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        theWolf.setBegging(true);
        timeoutCounter = 40 + theWolf.getRNG().nextInt(40);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        theWolf.setBegging(false);
        thePlayer = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        theWolf.getLookHelper().setLookPosition(thePlayer.posX, thePlayer.posY + (double) thePlayer.getEyeHeight(), thePlayer.posZ, 10.0F, (float) theWolf.getVerticalFaceSpeed());
        --timeoutCounter;
    }

    /**
     * Gets if the Player has the Bone in the hand.
     */
    private boolean hasPlayerGotBoneInHand(EntityPlayer player)
    {
        for (EnumHand enumhand : EnumHand.values())
        {
            ItemStack itemstack = player.getHeldItem(enumhand);

            if (theWolf.isTamed() && itemstack.getItem() == Items.BONE)
            {
                return true;
            }

            if (theWolf.isBreedingItem(itemstack))
            {
                return true;
            }
        }

        return false;
    }
}
