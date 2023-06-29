package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.MathHelper;

public class EntityFlyHelper extends EntityMoveHelper
{
    public EntityFlyHelper(EntityLiving p_i47418_1_)
    {
        super(p_i47418_1_);
    }

    public void onUpdateMoveHelper()
    {
        if (action == EntityMoveHelper.Action.MOVE_TO)
        {
            action = EntityMoveHelper.Action.WAIT;
            entity.setNoGravity(true);
            double d0 = posX - entity.posX;
            double d1 = posY - entity.posY;
            double d2 = posZ - entity.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 < 2.500000277905201E-7D)
            {
                entity.setMoveForward(0.0F);
                entity.func_191989_p(0.0F);
                return;
            }

            float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            entity.rotationYaw = limitAngle(entity.rotationYaw, f, 10.0F);
            float f1;

            if (entity.onGround)
            {
                f1 = (float)(speed * entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
            }
            else
            {
                f1 = (float)(speed * entity.getEntityAttribute(SharedMonsterAttributes.field_193334_e).getAttributeValue());
            }

            entity.setAIMoveSpeed(f1);
            double d4 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            float f2 = (float)(-(MathHelper.atan2(d1, d4) * (180D / Math.PI)));
            entity.rotationPitch = limitAngle(entity.rotationPitch, f2, 10.0F);
            entity.setMoveForward(d1 > 0.0D ? f1 : -f1);
        }
        else
        {
            entity.setNoGravity(false);
            entity.setMoveForward(0.0F);
            entity.func_191989_p(0.0F);
        }
    }
}
