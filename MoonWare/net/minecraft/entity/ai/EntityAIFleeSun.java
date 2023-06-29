package net.minecraft.entity.ai;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIFleeSun extends EntityAIBase
{
    private final EntityCreature theCreature;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private final double movementSpeed;
    private final World theWorld;

    public EntityAIFleeSun(EntityCreature theCreatureIn, double movementSpeedIn)
    {
        theCreature = theCreatureIn;
        movementSpeed = movementSpeedIn;
        theWorld = theCreatureIn.world;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!theWorld.isDaytime())
        {
            return false;
        }
        else if (!theCreature.isBurning())
        {
            return false;
        }
        else if (!theWorld.canSeeSky(new BlockPos(theCreature.posX, theCreature.getEntityBoundingBox().minY, theCreature.posZ)))
        {
            return false;
        }
        else if (!theCreature.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty())
        {
            return false;
        }
        else
        {
            Vec3d vec3d = findPossibleShelter();

            if (vec3d == null)
            {
                return false;
            }
            else
            {
                shelterX = vec3d.xCoord;
                shelterY = vec3d.yCoord;
                shelterZ = vec3d.zCoord;
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !theCreature.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        theCreature.getNavigator().tryMoveToXYZ(shelterX, shelterY, shelterZ, movementSpeed);
    }

    @Nullable
    private Vec3d findPossibleShelter()
    {
        Random random = theCreature.getRNG();
        BlockPos blockpos = new BlockPos(theCreature.posX, theCreature.getEntityBoundingBox().minY, theCreature.posZ);

        for (int i = 0; i < 10; ++i)
        {
            BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

            if (!theWorld.canSeeSky(blockpos1) && theCreature.getBlockPathWeight(blockpos1) < 0.0F)
            {
                return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
            }
        }

        return null;
    }
}
