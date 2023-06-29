package net.minecraft.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

public class RayTraceResult
{
    private BlockPos blockPos;

    public RayTraceResult.Type typeOfHit;
    public EnumFacing sideHit;

    /** The vector position of the hit */
    public Vec3d hitVec;

    /** The hit entity */
    public Entity entityHit;

    public RayTraceResult(Vec3d hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn)
    {
        this(RayTraceResult.Type.BLOCK, hitVecIn, sideHitIn, blockPosIn);
    }

    public RayTraceResult(Vec3d hitVecIn, EnumFacing sideHitIn)
    {
        this(RayTraceResult.Type.BLOCK, hitVecIn, sideHitIn, BlockPos.ORIGIN);
    }

    public RayTraceResult(Entity entityIn)
    {
        this(entityIn, new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ));
    }

    public RayTraceResult(RayTraceResult.Type typeIn, Vec3d hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn)
    {
        typeOfHit = typeIn;
        blockPos = blockPosIn;
        sideHit = sideHitIn;
        hitVec = new Vec3d(hitVecIn.xCoord, hitVecIn.yCoord, hitVecIn.zCoord);
    }

    public RayTraceResult(Entity entityHitIn, Vec3d hitVecIn)
    {
        typeOfHit = RayTraceResult.Type.ENTITY;
        entityHit = entityHitIn;
        hitVec = hitVecIn;
    }

    public BlockPos getBlockPos()
    {
        return blockPos;
    }

    public String toString()
    {
        return "HitResult{type=" + typeOfHit + ", blockpos=" + blockPos + ", f=" + sideHit + ", pos=" + hitVec + ", entity=" + entityHit + '}';
    }

    public enum Type
    {
        MISS,
        BLOCK,
        ENTITY
    }
}
