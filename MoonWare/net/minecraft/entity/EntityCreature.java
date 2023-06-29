package net.minecraft.entity;

import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityCreature extends EntityLiving
{
    public static final UUID FLEEING_SPEED_MODIFIER_UUID = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
    public static final AttributeModifier FLEEING_SPEED_MODIFIER = (new AttributeModifier(FLEEING_SPEED_MODIFIER_UUID, "Fleeing speed bonus", 2.0D, 2)).setSaved(false);
    private BlockPos homePosition = BlockPos.ORIGIN;

    /** If -1 there is no maximum distance */
    private float maximumHomeDistance = -1.0F;
    private final float restoreWaterCost = PathNodeType.WATER.getPriority();

    public EntityCreature(World worldIn)
    {
        super(worldIn);
    }

    public float getBlockPathWeight(BlockPos pos)
    {
        return 0.0F;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() && getBlockPathWeight(new BlockPos(posX, getEntityBoundingBox().minY, posZ)) >= 0.0F;
    }

    /**
     * if the entity got a PathEntity it returns true, else false
     */
    public boolean hasPath()
    {
        return !navigator.noPath();
    }

    public boolean isWithinHomeDistanceCurrentPosition()
    {
        return isWithinHomeDistanceFromPosition(new BlockPos(this));
    }

    public boolean isWithinHomeDistanceFromPosition(BlockPos pos)
    {
        if (maximumHomeDistance == -1.0F)
        {
            return true;
        }
        else
        {
            return homePosition.distanceSq(pos) < (double)(maximumHomeDistance * maximumHomeDistance);
        }
    }

    /**
     * Sets home position and max distance for it
     */
    public void setHomePosAndDistance(BlockPos pos, int distance)
    {
        homePosition = pos;
        maximumHomeDistance = (float)distance;
    }

    public BlockPos getHomePosition()
    {
        return homePosition;
    }

    public float getMaximumHomeDistance()
    {
        return maximumHomeDistance;
    }

    public void detachHome()
    {
        maximumHomeDistance = -1.0F;
    }

    /**
     * Returns whether a home area is defined for this entity.
     */
    public boolean hasHome()
    {
        return maximumHomeDistance != -1.0F;
    }

    /**
     * Applies logic related to leashes, for example dragging the entity or breaking the leash.
     */
    protected void updateLeashedState()
    {
        super.updateLeashedState();

        if (getLeashed() && getLeashedToEntity() != null && getLeashedToEntity().world == world)
        {
            Entity entity = getLeashedToEntity();
            setHomePosAndDistance(new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 5);
            float f = getDistanceToEntity(entity);

            if (this instanceof EntityTameable && ((EntityTameable)this).isSitting())
            {
                if (f > 10.0F)
                {
                    clearLeashed(true, true);
                }

                return;
            }

            onLeashDistance(f);

            if (f > 10.0F)
            {
                clearLeashed(true, true);
                tasks.disableControlFlag(1);
            }
            else if (f > 6.0F)
            {
                double d0 = (entity.posX - posX) / (double)f;
                double d1 = (entity.posY - posY) / (double)f;
                double d2 = (entity.posZ - posZ) / (double)f;
                motionX += d0 * Math.abs(d0) * 0.4D;
                motionY += d1 * Math.abs(d1) * 0.4D;
                motionZ += d2 * Math.abs(d2) * 0.4D;
            }
            else
            {
                tasks.enableControlFlag(1);
                float f1 = 2.0F;
                Vec3d vec3d = (new Vec3d(entity.posX - posX, entity.posY - posY, entity.posZ - posZ)).normalize().scale(Math.max(f - 2.0F, 0.0F));
                getNavigator().tryMoveToXYZ(posX + vec3d.xCoord, posY + vec3d.yCoord, posZ + vec3d.zCoord, func_190634_dg());
            }
        }
    }

    protected double func_190634_dg()
    {
        return 1.0D;
    }

    protected void onLeashDistance(float p_142017_1_)
    {
    }
}
