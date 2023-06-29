package net.minecraft.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public abstract class PathNavigate
{
    protected EntityLiving theEntity;
    protected World worldObj;
    @Nullable

    /** The PathEntity being followed. */
    protected Path currentPath;
    protected double speed;

    /**
     * The number of blocks (extra) +/- in each axis that get pulled out as cache for the pathfinder's search space
     */
    private final IAttributeInstance pathSearchRange;

    /** Time, in number of ticks, following the current path */
    protected int totalTicks;

    /**
     * The time when the last position check was done (to detect successful movement)
     */
    private int ticksAtLastPos;

    /**
     * Coordinates of the entity's position last time a check was done (part of monitoring getting 'stuck')
     */
    private Vec3d lastPosCheck = Vec3d.ZERO;
    private Vec3d timeoutCachedNode = Vec3d.ZERO;
    private long timeoutTimer;
    private long lastTimeoutCheck;
    private double timeoutLimit;
    protected float maxDistanceToWaypoint = 0.5F;
    protected boolean tryUpdatePath;
    private long lastTimeUpdated;
    protected NodeProcessor nodeProcessor;
    private BlockPos targetPos;
    private final PathFinder pathFinder;

    public PathNavigate(EntityLiving entitylivingIn, World worldIn)
    {
        theEntity = entitylivingIn;
        worldObj = worldIn;
        pathSearchRange = entitylivingIn.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        pathFinder = getPathFinder();
    }

    protected abstract PathFinder getPathFinder();

    /**
     * Sets the speed
     */
    public void setSpeed(double speedIn)
    {
        speed = speedIn;
    }

    /**
     * Gets the maximum distance that the path finding will search in.
     */
    public float getPathSearchRange()
    {
        return (float) pathSearchRange.getAttributeValue();
    }

    /**
     * Returns true if path can be changed by {@link net.minecraft.pathfinding.PathNavigate#onUpdateNavigation()
     * onUpdateNavigation()}
     */
    public boolean canUpdatePathOnTimeout()
    {
        return tryUpdatePath;
    }

    public void updatePath()
    {
        if (worldObj.getTotalWorldTime() - lastTimeUpdated > 20L)
        {
            if (targetPos != null)
            {
                currentPath = null;
                currentPath = getPathToPos(targetPos);
                lastTimeUpdated = worldObj.getTotalWorldTime();
                tryUpdatePath = false;
            }
        }
        else
        {
            tryUpdatePath = true;
        }
    }

    @Nullable

    /**
     * Returns the path to the given coordinates. Args : x, y, z
     */
    public final Path getPathToXYZ(double x, double y, double z)
    {
        return getPathToPos(new BlockPos(x, y, z));
    }

    @Nullable

    /**
     * Returns path to given BlockPos
     */
    public Path getPathToPos(BlockPos pos)
    {
        if (!canNavigate())
        {
            return null;
        }
        else if (currentPath != null && !currentPath.isFinished() && pos.equals(targetPos))
        {
            return currentPath;
        }
        else
        {
            targetPos = pos;
            float f = getPathSearchRange();
            worldObj.theProfiler.startSection("pathfind");
            BlockPos blockpos = new BlockPos(theEntity);
            int i = (int)(f + 8.0F);
            ChunkCache chunkcache = new ChunkCache(worldObj, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
            Path path = pathFinder.findPath(chunkcache, theEntity, targetPos, f);
            worldObj.theProfiler.endSection();
            return path;
        }
    }

    @Nullable

    /**
     * Returns the path to the given EntityLiving. Args : entity
     */
    public Path getPathToEntityLiving(Entity entityIn)
    {
        if (!canNavigate())
        {
            return null;
        }
        else
        {
            BlockPos blockpos = new BlockPos(entityIn);

            if (currentPath != null && !currentPath.isFinished() && blockpos.equals(targetPos))
            {
                return currentPath;
            }
            else
            {
                targetPos = blockpos;
                float f = getPathSearchRange();
                worldObj.theProfiler.startSection("pathfind");
                BlockPos blockpos1 = (new BlockPos(theEntity)).up();
                int i = (int)(f + 16.0F);
                ChunkCache chunkcache = new ChunkCache(worldObj, blockpos1.add(-i, -i, -i), blockpos1.add(i, i, i), 0);
                Path path = pathFinder.findPath(chunkcache, theEntity, entityIn, f);
                worldObj.theProfiler.endSection();
                return path;
            }
        }
    }

    /**
     * Try to find and set a path to XYZ. Returns true if successful. Args : x, y, z, speed
     */
    public boolean tryMoveToXYZ(double x, double y, double z, double speedIn)
    {
        return setPath(getPathToXYZ(x, y, z), speedIn);
    }

    /**
     * Try to find and set a path to EntityLiving. Returns true if successful. Args : entity, speed
     */
    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn)
    {
        Path path = getPathToEntityLiving(entityIn);
        return path != null && setPath(path, speedIn);
    }

    /**
     * Sets a new path. If it's diferent from the old path. Checks to adjust path for sun avoiding, and stores start
     * coords. Args : path, speed
     */
    public boolean setPath(@Nullable Path pathentityIn, double speedIn)
    {
        if (pathentityIn == null)
        {
            currentPath = null;
            return false;
        }
        else
        {
            if (!pathentityIn.isSamePath(currentPath))
            {
                currentPath = pathentityIn;
            }

            removeSunnyPath();

            if (currentPath.getCurrentPathLength() <= 0)
            {
                return false;
            }
            else
            {
                speed = speedIn;
                Vec3d vec3d = getEntityPosition();
                ticksAtLastPos = totalTicks;
                lastPosCheck = vec3d;
                return true;
            }
        }
    }

    @Nullable

    /**
     * gets the actively used PathEntity
     */
    public Path getPath()
    {
        return currentPath;
    }

    public void onUpdateNavigation()
    {
        ++totalTicks;

        if (tryUpdatePath)
        {
            updatePath();
        }

        if (!noPath())
        {
            if (canNavigate())
            {
                pathFollow();
            }
            else if (currentPath != null && currentPath.getCurrentPathIndex() < currentPath.getCurrentPathLength())
            {
                Vec3d vec3d = getEntityPosition();
                Vec3d vec3d1 = currentPath.getVectorFromIndex(theEntity, currentPath.getCurrentPathIndex());

                if (vec3d.yCoord > vec3d1.yCoord && !theEntity.onGround && MathHelper.floor(vec3d.xCoord) == MathHelper.floor(vec3d1.xCoord) && MathHelper.floor(vec3d.zCoord) == MathHelper.floor(vec3d1.zCoord))
                {
                    currentPath.setCurrentPathIndex(currentPath.getCurrentPathIndex() + 1);
                }
            }

            func_192876_m();

            if (!noPath())
            {
                Vec3d vec3d2 = currentPath.getPosition(theEntity);
                BlockPos blockpos = (new BlockPos(vec3d2)).down();
                AxisAlignedBB axisalignedbb = worldObj.getBlockState(blockpos).getBoundingBox(worldObj, blockpos);
                vec3d2 = vec3d2.subtract(0.0D, 1.0D - axisalignedbb.maxY, 0.0D);
                theEntity.getMoveHelper().setMoveTo(vec3d2.xCoord, vec3d2.yCoord, vec3d2.zCoord, speed);
            }
        }
    }

    protected void func_192876_m()
    {
    }

    protected void pathFollow()
    {
        Vec3d vec3d = getEntityPosition();
        int i = currentPath.getCurrentPathLength();

        for (int j = currentPath.getCurrentPathIndex(); j < currentPath.getCurrentPathLength(); ++j)
        {
            if ((double) currentPath.getPathPointFromIndex(j).yCoord != Math.floor(vec3d.yCoord))
            {
                i = j;
                break;
            }
        }

        maxDistanceToWaypoint = theEntity.width > 0.75F ? theEntity.width / 2.0F : 0.75F - theEntity.width / 2.0F;
        Vec3d vec3d1 = currentPath.getCurrentPos();

        if (MathHelper.abs((float)(theEntity.posX - (vec3d1.xCoord + 0.5D))) < maxDistanceToWaypoint && MathHelper.abs((float)(theEntity.posZ - (vec3d1.zCoord + 0.5D))) < maxDistanceToWaypoint && Math.abs(theEntity.posY - vec3d1.yCoord) < 1.0D)
        {
            currentPath.setCurrentPathIndex(currentPath.getCurrentPathIndex() + 1);
        }

        int k = MathHelper.ceil(theEntity.width);
        int l = MathHelper.ceil(theEntity.height);
        int i1 = k;

        for (int j1 = i - 1; j1 >= currentPath.getCurrentPathIndex(); --j1)
        {
            if (isDirectPathBetweenPoints(vec3d, currentPath.getVectorFromIndex(theEntity, j1), k, l, i1))
            {
                currentPath.setCurrentPathIndex(j1);
                break;
            }
        }

        checkForStuck(vec3d);
    }

    /**
     * Checks if entity haven't been moved when last checked and if so, clears current {@link
     * net.minecraft.pathfinding.PathEntity}
     */
    protected void checkForStuck(Vec3d positionVec3)
    {
        if (totalTicks - ticksAtLastPos > 100)
        {
            if (positionVec3.squareDistanceTo(lastPosCheck) < 2.25D)
            {
                clearPathEntity();
            }

            ticksAtLastPos = totalTicks;
            lastPosCheck = positionVec3;
        }

        if (currentPath != null && !currentPath.isFinished())
        {
            Vec3d vec3d = currentPath.getCurrentPos();

            if (vec3d.equals(timeoutCachedNode))
            {
                timeoutTimer += System.currentTimeMillis() - lastTimeoutCheck;
            }
            else
            {
                timeoutCachedNode = vec3d;
                double d0 = positionVec3.distanceTo(timeoutCachedNode);
                timeoutLimit = theEntity.getAIMoveSpeed() > 0.0F ? d0 / (double) theEntity.getAIMoveSpeed() * 1000.0D : 0.0D;
            }

            if (timeoutLimit > 0.0D && (double) timeoutTimer > timeoutLimit * 3.0D)
            {
                timeoutCachedNode = Vec3d.ZERO;
                timeoutTimer = 0L;
                timeoutLimit = 0.0D;
                clearPathEntity();
            }

            lastTimeoutCheck = System.currentTimeMillis();
        }
    }

    /**
     * If null path or reached the end
     */
    public boolean noPath()
    {
        return currentPath == null || currentPath.isFinished();
    }

    /**
     * sets active PathEntity to null
     */
    public void clearPathEntity()
    {
        currentPath = null;
    }

    protected abstract Vec3d getEntityPosition();

    /**
     * If on ground or swimming and can swim
     */
    protected abstract boolean canNavigate();

    /**
     * Returns true if the entity is in water or lava, false otherwise
     */
    protected boolean isInLiquid()
    {
        return theEntity.isInWater() || theEntity.isInLava();
    }

    /**
     * Trims path data from the end to the first sun covered block
     */
    protected void removeSunnyPath()
    {
        if (currentPath != null)
        {
            for (int i = 0; i < currentPath.getCurrentPathLength(); ++i)
            {
                PathPoint pathpoint = currentPath.getPathPointFromIndex(i);
                PathPoint pathpoint1 = i + 1 < currentPath.getCurrentPathLength() ? currentPath.getPathPointFromIndex(i + 1) : null;
                IBlockState iblockstate = worldObj.getBlockState(new BlockPos(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord));
                Block block = iblockstate.getBlock();

                if (block == Blocks.CAULDRON)
                {
                    currentPath.setPoint(i, pathpoint.cloneMove(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord));

                    if (pathpoint1 != null && pathpoint.yCoord >= pathpoint1.yCoord)
                    {
                        currentPath.setPoint(i + 1, pathpoint1.cloneMove(pathpoint1.xCoord, pathpoint.yCoord + 1, pathpoint1.zCoord));
                    }
                }
            }
        }
    }

    /**
     * Checks if the specified entity can safely walk to the specified location.
     */
    protected abstract boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ);

    public boolean canEntityStandOnPos(BlockPos pos)
    {
        return worldObj.getBlockState(pos.down()).isFullBlock();
    }

    public NodeProcessor getNodeProcessor()
    {
        return nodeProcessor;
    }
}
