package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveThroughVillage extends EntityAIBase
{
    private final EntityCreature theEntity;
    private final double movementSpeed;

    /** The PathNavigate of our entity. */
    private Path entityPathNavigate;
    private VillageDoorInfo doorInfo;
    private final boolean isNocturnal;
    private final List<VillageDoorInfo> doorList = Lists.newArrayList();

    public EntityAIMoveThroughVillage(EntityCreature theEntityIn, double movementSpeedIn, boolean isNocturnalIn)
    {
        theEntity = theEntityIn;
        movementSpeed = movementSpeedIn;
        isNocturnal = isNocturnalIn;
        setMutexBits(1);

        if (!(theEntityIn.getNavigator() instanceof PathNavigateGround))
        {
            throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        resizeDoorList();

        if (isNocturnal && theEntity.world.isDaytime())
        {
            return false;
        }
        else
        {
            Village village = theEntity.world.getVillageCollection().getNearestVillage(new BlockPos(theEntity), 0);

            if (village == null)
            {
                return false;
            }
            else
            {
                doorInfo = findNearestDoor(village);

                if (doorInfo == null)
                {
                    return false;
                }
                else
                {
                    PathNavigateGround pathnavigateground = (PathNavigateGround) theEntity.getNavigator();
                    boolean flag = pathnavigateground.getEnterDoors();
                    pathnavigateground.setBreakDoors(false);
                    entityPathNavigate = pathnavigateground.getPathToPos(doorInfo.getDoorBlockPos());
                    pathnavigateground.setBreakDoors(flag);

                    if (entityPathNavigate != null)
                    {
                        return true;
                    }
                    else
                    {
                        Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(theEntity, 10, 7, new Vec3d(doorInfo.getDoorBlockPos().getX(), doorInfo.getDoorBlockPos().getY(), doorInfo.getDoorBlockPos().getZ()));

                        if (vec3d == null)
                        {
                            return false;
                        }
                        else
                        {
                            pathnavigateground.setBreakDoors(false);
                            entityPathNavigate = theEntity.getNavigator().getPathToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
                            pathnavigateground.setBreakDoors(flag);
                            return entityPathNavigate != null;
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (theEntity.getNavigator().noPath())
        {
            return false;
        }
        else
        {
            float f = theEntity.width + 4.0F;
            return theEntity.getDistanceSq(doorInfo.getDoorBlockPos()) > (double)(f * f);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        theEntity.getNavigator().setPath(entityPathNavigate, movementSpeed);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        if (theEntity.getNavigator().noPath() || theEntity.getDistanceSq(doorInfo.getDoorBlockPos()) < 16.0D)
        {
            doorList.add(doorInfo);
        }
    }

    private VillageDoorInfo findNearestDoor(Village villageIn)
    {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;

        for (VillageDoorInfo villagedoorinfo1 : villageIn.getVillageDoorInfoList())
        {
            int j = villagedoorinfo1.getDistanceSquared(MathHelper.floor(theEntity.posX), MathHelper.floor(theEntity.posY), MathHelper.floor(theEntity.posZ));

            if (j < i && !doesDoorListContain(villagedoorinfo1))
            {
                villagedoorinfo = villagedoorinfo1;
                i = j;
            }
        }

        return villagedoorinfo;
    }

    private boolean doesDoorListContain(VillageDoorInfo doorInfoIn)
    {
        for (VillageDoorInfo villagedoorinfo : doorList)
        {
            if (doorInfoIn.getDoorBlockPos().equals(villagedoorinfo.getDoorBlockPos()))
            {
                return true;
            }
        }

        return false;
    }

    private void resizeDoorList()
    {
        if (doorList.size() > 15)
        {
            doorList.remove(0);
        }
    }
}
