package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveIndoors extends EntityAIBase
{
    private final EntityCreature entityObj;
    private VillageDoorInfo doorInfo;
    private int insidePosX = -1;
    private int insidePosZ = -1;

    public EntityAIMoveIndoors(EntityCreature entityObjIn)
    {
        entityObj = entityObjIn;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        BlockPos blockpos = new BlockPos(entityObj);

        if ((!entityObj.world.isDaytime() || entityObj.world.isRaining() && !entityObj.world.getBiome(blockpos).canRain()) && entityObj.world.provider.isNether())
        {
            if (entityObj.getRNG().nextInt(50) != 0)
            {
                return false;
            }
            else if (insidePosX != -1 && entityObj.getDistanceSq(insidePosX, entityObj.posY, insidePosZ) < 4.0D)
            {
                return false;
            }
            else
            {
                Village village = entityObj.world.getVillageCollection().getNearestVillage(blockpos, 14);

                if (village == null)
                {
                    return false;
                }
                else
                {
                    doorInfo = village.getDoorInfo(blockpos);
                    return doorInfo != null;
                }
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !entityObj.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        insidePosX = -1;
        BlockPos blockpos = doorInfo.getInsideBlockPos();
        int i = blockpos.getX();
        int j = blockpos.getY();
        int k = blockpos.getZ();

        if (entityObj.getDistanceSq(blockpos) > 256.0D)
        {
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(entityObj, 14, 3, new Vec3d((double)i + 0.5D, j, (double)k + 0.5D));

            if (vec3d != null)
            {
                entityObj.getNavigator().tryMoveToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 1.0D);
            }
        }
        else
        {
            entityObj.getNavigator().tryMoveToXYZ((double)i + 0.5D, j, (double)k + 0.5D, 1.0D);
        }
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        insidePosX = doorInfo.getInsideBlockPos().getX();
        insidePosZ = doorInfo.getInsideBlockPos().getZ();
        doorInfo = null;
    }
}
