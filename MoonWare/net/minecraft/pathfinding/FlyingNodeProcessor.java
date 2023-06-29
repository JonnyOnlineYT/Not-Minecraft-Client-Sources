package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class FlyingNodeProcessor extends WalkNodeProcessor
{
    public void initProcessor(IBlockAccess sourceIn, EntityLiving mob)
    {
        super.initProcessor(sourceIn, mob);
        avoidsWater = mob.getPathPriority(PathNodeType.WATER);
    }

    /**
     * This method is called when all nodes have been processed and PathEntity is created.
     *  {@link net.minecraft.world.pathfinder.WalkNodeProcessor WalkNodeProcessor} uses this to change its field {@link
     * net.minecraft.world.pathfinder.WalkNodeProcessor#avoidsWater avoidsWater}
     */
    public void postProcess()
    {
        entity.setPathPriority(PathNodeType.WATER, avoidsWater);
        super.postProcess();
    }

    public PathPoint getStart()
    {
        int i;

        if (getCanSwim() && entity.isInWater())
        {
            i = (int) entity.getEntityBoundingBox().minY;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(entity.posX), i, MathHelper.floor(entity.posZ));

            for (Block block = blockaccess.getBlockState(blockpos$mutableblockpos).getBlock(); block == Blocks.FLOWING_WATER || block == Blocks.WATER; block = blockaccess.getBlockState(blockpos$mutableblockpos).getBlock())
            {
                ++i;
                blockpos$mutableblockpos.setPos(MathHelper.floor(entity.posX), i, MathHelper.floor(entity.posZ));
            }
        }
        else
        {
            i = MathHelper.floor(entity.getEntityBoundingBox().minY + 0.5D);
        }

        BlockPos blockpos1 = new BlockPos(entity);
        PathNodeType pathnodetype1 = func_192558_a(entity, blockpos1.getX(), i, blockpos1.getZ());

        if (entity.getPathPriority(pathnodetype1) < 0.0F)
        {
            Set<BlockPos> set = Sets.newHashSet();
            set.add(new BlockPos(entity.getEntityBoundingBox().minX, i, entity.getEntityBoundingBox().minZ));
            set.add(new BlockPos(entity.getEntityBoundingBox().minX, i, entity.getEntityBoundingBox().maxZ));
            set.add(new BlockPos(entity.getEntityBoundingBox().maxX, i, entity.getEntityBoundingBox().minZ));
            set.add(new BlockPos(entity.getEntityBoundingBox().maxX, i, entity.getEntityBoundingBox().maxZ));

            for (BlockPos blockpos : set)
            {
                PathNodeType pathnodetype = func_192559_a(entity, blockpos);

                if (entity.getPathPriority(pathnodetype) >= 0.0F)
                {
                    return super.openPoint(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                }
            }
        }

        return super.openPoint(blockpos1.getX(), i, blockpos1.getZ());
    }

    /**
     * Returns PathPoint for given coordinates
     */
    public PathPoint getPathPointToCoords(double x, double y, double z)
    {
        return super.openPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance)
    {
        int i = 0;
        PathPoint pathpoint = openPoint(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1);
        PathPoint pathpoint1 = openPoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord);
        PathPoint pathpoint2 = openPoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord);
        PathPoint pathpoint3 = openPoint(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1);
        PathPoint pathpoint4 = openPoint(currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord);
        PathPoint pathpoint5 = openPoint(currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord);

        if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint;
        }

        if (pathpoint1 != null && !pathpoint1.visited && pathpoint1.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint1;
        }

        if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint2;
        }

        if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint3;
        }

        if (pathpoint4 != null && !pathpoint4.visited && pathpoint4.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint4;
        }

        if (pathpoint5 != null && !pathpoint5.visited && pathpoint5.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint5;
        }

        boolean flag = pathpoint3 == null || pathpoint3.costMalus != 0.0F;
        boolean flag1 = pathpoint == null || pathpoint.costMalus != 0.0F;
        boolean flag2 = pathpoint2 == null || pathpoint2.costMalus != 0.0F;
        boolean flag3 = pathpoint1 == null || pathpoint1.costMalus != 0.0F;
        boolean flag4 = pathpoint4 == null || pathpoint4.costMalus != 0.0F;
        boolean flag5 = pathpoint5 == null || pathpoint5.costMalus != 0.0F;

        if (flag && flag3)
        {
            PathPoint pathpoint6 = openPoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord - 1);

            if (pathpoint6 != null && !pathpoint6.visited && pathpoint6.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint6;
            }
        }

        if (flag && flag2)
        {
            PathPoint pathpoint7 = openPoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord - 1);

            if (pathpoint7 != null && !pathpoint7.visited && pathpoint7.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint7;
            }
        }

        if (flag1 && flag3)
        {
            PathPoint pathpoint8 = openPoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord + 1);

            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint8;
            }
        }

        if (flag1 && flag2)
        {
            PathPoint pathpoint9 = openPoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord + 1);

            if (pathpoint9 != null && !pathpoint9.visited && pathpoint9.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint9;
            }
        }

        if (flag && flag4)
        {
            PathPoint pathpoint10 = openPoint(currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord - 1);

            if (pathpoint10 != null && !pathpoint10.visited && pathpoint10.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint10;
            }
        }

        if (flag1 && flag4)
        {
            PathPoint pathpoint11 = openPoint(currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord + 1);

            if (pathpoint11 != null && !pathpoint11.visited && pathpoint11.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint11;
            }
        }

        if (flag2 && flag4)
        {
            PathPoint pathpoint12 = openPoint(currentPoint.xCoord + 1, currentPoint.yCoord + 1, currentPoint.zCoord);

            if (pathpoint12 != null && !pathpoint12.visited && pathpoint12.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint12;
            }
        }

        if (flag3 && flag4)
        {
            PathPoint pathpoint13 = openPoint(currentPoint.xCoord - 1, currentPoint.yCoord + 1, currentPoint.zCoord);

            if (pathpoint13 != null && !pathpoint13.visited && pathpoint13.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint13;
            }
        }

        if (flag && flag5)
        {
            PathPoint pathpoint14 = openPoint(currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord - 1);

            if (pathpoint14 != null && !pathpoint14.visited && pathpoint14.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint14;
            }
        }

        if (flag1 && flag5)
        {
            PathPoint pathpoint15 = openPoint(currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord + 1);

            if (pathpoint15 != null && !pathpoint15.visited && pathpoint15.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint15;
            }
        }

        if (flag2 && flag5)
        {
            PathPoint pathpoint16 = openPoint(currentPoint.xCoord + 1, currentPoint.yCoord - 1, currentPoint.zCoord);

            if (pathpoint16 != null && !pathpoint16.visited && pathpoint16.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint16;
            }
        }

        if (flag3 && flag5)
        {
            PathPoint pathpoint17 = openPoint(currentPoint.xCoord - 1, currentPoint.yCoord - 1, currentPoint.zCoord);

            if (pathpoint17 != null && !pathpoint17.visited && pathpoint17.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint17;
            }
        }

        return i;
    }

    @Nullable

    /**
     * Returns a mapped point or creates and adds one
     */
    protected PathPoint openPoint(int x, int y, int z)
    {
        PathPoint pathpoint = null;
        PathNodeType pathnodetype = func_192558_a(entity, x, y, z);
        float f = entity.getPathPriority(pathnodetype);

        if (f >= 0.0F)
        {
            pathpoint = super.openPoint(x, y, z);
            pathpoint.nodeType = pathnodetype;
            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);

            if (pathnodetype == PathNodeType.WALKABLE)
            {
                ++pathpoint.costMalus;
            }
        }

        return pathpoint;
    }

    public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z, EntityLiving entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn)
    {
        EnumSet<PathNodeType> enumset = EnumSet.noneOf(PathNodeType.class);
        PathNodeType pathnodetype = PathNodeType.BLOCKED;
        BlockPos blockpos = new BlockPos(entitylivingIn);
        pathnodetype = func_193577_a(blockaccessIn, x, y, z, xSize, ySize, zSize, canBreakDoorsIn, canEnterDoorsIn, enumset, pathnodetype, blockpos);

        if (enumset.contains(PathNodeType.FENCE))
        {
            return PathNodeType.FENCE;
        }
        else
        {
            PathNodeType pathnodetype1 = PathNodeType.BLOCKED;

            for (PathNodeType pathnodetype2 : enumset)
            {
                if (entitylivingIn.getPathPriority(pathnodetype2) < 0.0F)
                {
                    return pathnodetype2;
                }

                if (entitylivingIn.getPathPriority(pathnodetype2) >= entitylivingIn.getPathPriority(pathnodetype1))
                {
                    pathnodetype1 = pathnodetype2;
                }
            }

            if (pathnodetype == PathNodeType.OPEN && entitylivingIn.getPathPriority(pathnodetype1) == 0.0F)
            {
                return PathNodeType.OPEN;
            }
            else
            {
                return pathnodetype1;
            }
        }
    }

    public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z)
    {
        PathNodeType pathnodetype = getPathNodeTypeRaw(blockaccessIn, x, y, z);

        if (pathnodetype == PathNodeType.OPEN && y >= 1)
        {
            Block block = blockaccessIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
            PathNodeType pathnodetype1 = getPathNodeTypeRaw(blockaccessIn, x, y - 1, z);

            if (pathnodetype1 != PathNodeType.DAMAGE_FIRE && block != Blocks.MAGMA && pathnodetype1 != PathNodeType.LAVA)
            {
                if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS)
                {
                    pathnodetype = PathNodeType.DAMAGE_CACTUS;
                }
                else
                {
                    pathnodetype = pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.WATER ? PathNodeType.WALKABLE : PathNodeType.OPEN;
                }
            }
            else
            {
                pathnodetype = PathNodeType.DAMAGE_FIRE;
            }
        }

        pathnodetype = func_193578_a(blockaccessIn, x, y, z, pathnodetype);
        return pathnodetype;
    }

    private PathNodeType func_192559_a(EntityLiving p_192559_1_, BlockPos p_192559_2_)
    {
        return func_192558_a(p_192559_1_, p_192559_2_.getX(), p_192559_2_.getY(), p_192559_2_.getZ());
    }

    private PathNodeType func_192558_a(EntityLiving p_192558_1_, int p_192558_2_, int p_192558_3_, int p_192558_4_)
    {
        return getPathNodeType(blockaccess, p_192558_2_, p_192558_3_, p_192558_4_, p_192558_1_, entitySizeX, entitySizeY, entitySizeZ, getCanBreakDoors(), getCanEnterDoors());
    }
}
