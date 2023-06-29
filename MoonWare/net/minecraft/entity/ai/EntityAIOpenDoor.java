package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAIOpenDoor extends EntityAIDoorInteract
{
    /** If the entity close the door */
    boolean closeDoor;

    /**
     * The temporisation before the entity close the door (in ticks, always 20 = 1 second)
     */
    int closeDoorTemporisation;

    public EntityAIOpenDoor(EntityLiving entitylivingIn, boolean shouldClose)
    {
        super(entitylivingIn);
        theEntity = entitylivingIn;
        closeDoor = shouldClose;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return closeDoor && closeDoorTemporisation > 0 && super.continueExecuting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        closeDoorTemporisation = 20;
        doorBlock.toggleDoor(theEntity.world, doorPosition, true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        if (closeDoor)
        {
            doorBlock.toggleDoor(theEntity.world, doorPosition, false);
        }
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        --closeDoorTemporisation;
        super.updateTask();
    }
}
