package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;

public class EntityMoveHelper
{
    /** The EntityLiving that is being moved */
    protected final EntityLiving entity;
    protected double posX;
    protected double posY;
    protected double posZ;

    /** The speed at which the entity should move */
    protected double speed;
    protected float moveForward;
    protected float moveStrafe;
    public EntityMoveHelper.Action action = EntityMoveHelper.Action.WAIT;

    public EntityMoveHelper(EntityLiving entitylivingIn)
    {
        entity = entitylivingIn;
    }

    public boolean isUpdating()
    {
        return action == EntityMoveHelper.Action.MOVE_TO;
    }

    public double getSpeed()
    {
        return speed;
    }

    /**
     * Sets the speed and location to move to
     */
    public void setMoveTo(double x, double y, double z, double speedIn)
    {
        posX = x;
        posY = y;
        posZ = z;
        speed = speedIn;
        action = EntityMoveHelper.Action.MOVE_TO;
    }

    public void strafe(float forward, float strafe)
    {
        action = EntityMoveHelper.Action.STRAFE;
        moveForward = forward;
        moveStrafe = strafe;
        speed = 0.25D;
    }

    public void read(EntityMoveHelper that)
    {
        action = that.action;
        posX = that.posX;
        posY = that.posY;
        posZ = that.posZ;
        speed = Math.max(that.speed, 1.0D);
        moveForward = that.moveForward;
        moveStrafe = that.moveStrafe;
    }

    public void onUpdateMoveHelper()
    {
        if (action == EntityMoveHelper.Action.STRAFE)
        {
            float f = (float) entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
            float f1 = (float) speed * f;
            float f2 = moveForward;
            float f3 = moveStrafe;
            float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);

            if (f4 < 1.0F)
            {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 = f2 * f4;
            f3 = f3 * f4;
            float f5 = MathHelper.sin(entity.rotationYaw * 0.017453292F);
            float f6 = MathHelper.cos(entity.rotationYaw * 0.017453292F);
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            PathNavigate pathnavigate = entity.getNavigator();

            if (pathnavigate != null)
            {
                NodeProcessor nodeprocessor = pathnavigate.getNodeProcessor();

                if (nodeprocessor != null && nodeprocessor.getPathNodeType(entity.world, MathHelper.floor(entity.posX + (double)f7), MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ + (double)f8)) != PathNodeType.WALKABLE)
                {
                    moveForward = 1.0F;
                    moveStrafe = 0.0F;
                    f1 = f;
                }
            }

            entity.setAIMoveSpeed(f1);
            entity.func_191989_p(moveForward);
            entity.setMoveStrafing(moveStrafe);
            action = EntityMoveHelper.Action.WAIT;
        }
        else if (action == EntityMoveHelper.Action.MOVE_TO)
        {
            action = EntityMoveHelper.Action.WAIT;
            double d0 = posX - entity.posX;
            double d1 = posZ - entity.posZ;
            double d2 = posY - entity.posY;
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;

            if (d3 < 2.500000277905201E-7D)
            {
                entity.func_191989_p(0.0F);
                return;
            }

            float f9 = (float)(MathHelper.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
            entity.rotationYaw = limitAngle(entity.rotationYaw, f9, 90.0F);
            entity.setAIMoveSpeed((float)(speed * entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));

            if (d2 > (double) entity.stepHeight && d0 * d0 + d1 * d1 < (double)Math.max(1.0F, entity.width))
            {
                entity.getJumpHelper().setJumping();
                action = EntityMoveHelper.Action.JUMPING;
            }
        }
        else if (action == EntityMoveHelper.Action.JUMPING)
        {
            entity.setAIMoveSpeed((float)(speed * entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));

            if (entity.onGround)
            {
                action = EntityMoveHelper.Action.WAIT;
            }
        }
        else
        {
            entity.func_191989_p(0.0F);
        }
    }

    /**
     * Limits the given angle to a upper and lower limit.
     */
    protected float limitAngle(float p_75639_1_, float p_75639_2_, float p_75639_3_)
    {
        float f = MathHelper.wrapDegrees(p_75639_2_ - p_75639_1_);

        if (f > p_75639_3_)
        {
            f = p_75639_3_;
        }

        if (f < -p_75639_3_)
        {
            f = -p_75639_3_;
        }

        float f1 = p_75639_1_ + f;

        if (f1 < 0.0F)
        {
            f1 += 360.0F;
        }
        else if (f1 > 360.0F)
        {
            f1 -= 360.0F;
        }

        return f1;
    }

    public double getX()
    {
        return posX;
    }

    public double getY()
    {
        return posY;
    }

    public double getZ()
    {
        return posZ;
    }

    public enum Action
    {
        WAIT,
        MOVE_TO,
        STRAFE,
        JUMPING
    }
}
