package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class EntityAIRunAroundLikeCrazy extends EntityAIBase
{
    private final AbstractHorse horseHost;
    private final double speed;
    private double targetX;
    private double targetY;
    private double targetZ;

    public EntityAIRunAroundLikeCrazy(AbstractHorse horse, double speedIn)
    {
        horseHost = horse;
        speed = speedIn;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!horseHost.isTame() && horseHost.isBeingRidden())
        {
            Vec3d vec3d = RandomPositionGenerator.findRandomTarget(horseHost, 5, 4);

            if (vec3d == null)
            {
                return false;
            }
            else
            {
                targetX = vec3d.xCoord;
                targetY = vec3d.yCoord;
                targetZ = vec3d.zCoord;
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        horseHost.getNavigator().tryMoveToXYZ(targetX, targetY, targetZ, speed);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !horseHost.isTame() && !horseHost.getNavigator().noPath() && horseHost.isBeingRidden();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (!horseHost.isTame() && horseHost.getRNG().nextInt(50) == 0)
        {
            Entity entity = horseHost.getPassengers().get(0);

            if (entity == null)
            {
                return;
            }

            if (entity instanceof EntityPlayer)
            {
                int i = horseHost.getTemper();
                int j = horseHost.func_190676_dC();

                if (j > 0 && horseHost.getRNG().nextInt(j) < i)
                {
                    horseHost.setTamedBy((EntityPlayer)entity);
                    return;
                }

                horseHost.increaseTemper(5);
            }

            horseHost.removePassengers();
            horseHost.func_190687_dF();
            horseHost.world.setEntityState(horseHost, (byte)6);
        }
    }
}
