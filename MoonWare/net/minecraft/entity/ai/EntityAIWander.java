package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.Vec3d;

public class EntityAIWander extends EntityAIBase
{
    protected final EntityCreature entity;
    protected double xPosition;
    protected double yPosition;
    protected double zPosition;
    protected final double speed;
    protected int executionChance;
    protected boolean mustUpdate;

    public EntityAIWander(EntityCreature creatureIn, double speedIn)
    {
        this(creatureIn, speedIn, 120);
    }

    public EntityAIWander(EntityCreature creatureIn, double speedIn, int chance)
    {
        entity = creatureIn;
        speed = speedIn;
        executionChance = chance;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!mustUpdate)
        {
            if (entity.getAge() >= 100)
            {
                return false;
            }

            if (entity.getRNG().nextInt(executionChance) != 0)
            {
                return false;
            }
        }

        Vec3d vec3d = func_190864_f();

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            xPosition = vec3d.xCoord;
            yPosition = vec3d.yCoord;
            zPosition = vec3d.zCoord;
            mustUpdate = false;
            return true;
        }
    }

    @Nullable
    protected Vec3d func_190864_f()
    {
        return RandomPositionGenerator.findRandomTarget(entity, 10, 7);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !entity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        entity.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, speed);
    }

    /**
     * Makes task to bypass chance
     */
    public void makeUpdate()
    {
        mustUpdate = true;
    }

    /**
     * Changes task random possibility for execution
     */
    public void setExecutionChance(int newchance)
    {
        executionChance = newchance;
    }
}
