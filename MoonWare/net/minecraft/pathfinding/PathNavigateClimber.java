package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PathNavigateClimber extends PathNavigateGround
{
    /** Current path navigation target */
    private BlockPos targetPosition;

    public PathNavigateClimber(EntityLiving entityLivingIn, World worldIn)
    {
        super(entityLivingIn, worldIn);
    }

    /**
     * Returns path to given BlockPos
     */
    public Path getPathToPos(BlockPos pos)
    {
        targetPosition = pos;
        return super.getPathToPos(pos);
    }

    /**
     * Returns the path to the given EntityLiving. Args : entity
     */
    public Path getPathToEntityLiving(Entity entityIn)
    {
        targetPosition = new BlockPos(entityIn);
        return super.getPathToEntityLiving(entityIn);
    }

    /**
     * Try to find and set a path to EntityLiving. Returns true if successful. Args : entity, speed
     */
    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn)
    {
        Path path = getPathToEntityLiving(entityIn);

        if (path != null)
        {
            return setPath(path, speedIn);
        }
        else
        {
            targetPosition = new BlockPos(entityIn);
            speed = speedIn;
            return true;
        }
    }

    public void onUpdateNavigation()
    {
        if (!noPath())
        {
            super.onUpdateNavigation();
        }
        else
        {
            if (targetPosition != null)
            {
                double d0 = theEntity.width * theEntity.width;

                if (theEntity.getDistanceSqToCenter(targetPosition) >= d0 && (theEntity.posY <= (double) targetPosition.getY() || theEntity.getDistanceSqToCenter(new BlockPos(targetPosition.getX(), MathHelper.floor(theEntity.posY), targetPosition.getZ())) >= d0))
                {
                    theEntity.getMoveHelper().setMoveTo(targetPosition.getX(), targetPosition.getY(), targetPosition.getZ(), speed);
                }
                else
                {
                    targetPosition = null;
                }
            }
        }
    }
}
