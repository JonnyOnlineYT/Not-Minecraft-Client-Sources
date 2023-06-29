package net.minecraft.entity.projectile;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityLlamaSpit extends Entity implements IProjectile
{
    public EntityLlama field_190539_a;
    private NBTTagCompound field_190540_b;

    public EntityLlamaSpit(World p_i47272_1_)
    {
        super(p_i47272_1_);
    }

    public EntityLlamaSpit(World p_i47273_1_, EntityLlama p_i47273_2_)
    {
        super(p_i47273_1_);
        field_190539_a = p_i47273_2_;
        setPosition(p_i47273_2_.posX - (double)(p_i47273_2_.width + 1.0F) * 0.5D * (double)MathHelper.sin(p_i47273_2_.renderYawOffset * 0.017453292F), p_i47273_2_.posY + (double)p_i47273_2_.getEyeHeight() - 0.10000000149011612D, p_i47273_2_.posZ + (double)(p_i47273_2_.width + 1.0F) * 0.5D * (double)MathHelper.cos(p_i47273_2_.renderYawOffset * 0.017453292F));
        setSize(0.25F, 0.25F);
    }

    public EntityLlamaSpit(World p_i47274_1_, double p_i47274_2_, double p_i47274_4_, double p_i47274_6_, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_)
    {
        super(p_i47274_1_);
        setPosition(p_i47274_2_, p_i47274_4_, p_i47274_6_);

        for (int i = 0; i < 7; ++i)
        {
            double d0 = 0.4D + 0.1D * (double)i;
            p_i47274_1_.spawnParticle(EnumParticleTypes.SPIT, p_i47274_2_, p_i47274_4_, p_i47274_6_, p_i47274_8_ * d0, p_i47274_10_, p_i47274_12_ * d0);
        }

        motionX = p_i47274_8_;
        motionY = p_i47274_10_;
        motionZ = p_i47274_12_;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (field_190540_b != null)
        {
            func_190537_j();
        }

        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        Vec3d vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
        RayTraceResult raytraceresult = world.rayTraceBlocks(vec3d, vec3d1);
        vec3d = new Vec3d(posX, posY, posZ);
        vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

        if (raytraceresult != null)
        {
            vec3d1 = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
        }

        Entity entity = func_190538_a(vec3d, vec3d1);

        if (entity != null)
        {
            raytraceresult = new RayTraceResult(entity);
        }

        if (raytraceresult != null)
        {
            func_190536_a(raytraceresult);
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float f = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)(MathHelper.atan2(motionX, motionZ) * (180D / Math.PI));

        for (rotationPitch = (float)(MathHelper.atan2(motionY, f) * (180D / Math.PI)); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F)
        {
        }

        while (rotationPitch - prevRotationPitch >= 180.0F)
        {
            prevRotationPitch += 360.0F;
        }

        while (rotationYaw - prevRotationYaw < -180.0F)
        {
            prevRotationYaw -= 360.0F;
        }

        while (rotationYaw - prevRotationYaw >= 180.0F)
        {
            prevRotationYaw += 360.0F;
        }

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
        float f1 = 0.99F;
        float f2 = 0.06F;

        if (!world.isMaterialInBB(getEntityBoundingBox(), Material.AIR))
        {
            setDead();
        }
        else if (isInWater())
        {
            setDead();
        }
        else
        {
            motionX *= 0.9900000095367432D;
            motionY *= 0.9900000095367432D;
            motionZ *= 0.9900000095367432D;

            if (!hasNoGravity())
            {
                motionY -= 0.05999999865889549D;
            }

            setPosition(posX, posY, posZ);
        }
    }

    /**
     * Updates the velocity of the entity to a new value.
     */
    public void setVelocity(double x, double y, double z)
    {
        motionX = x;
        motionY = y;
        motionZ = z;

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            rotationPitch = (float)(MathHelper.atan2(y, f) * (180D / Math.PI));
            rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            prevRotationPitch = rotationPitch;
            prevRotationYaw = rotationYaw;
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
        }
    }

    @Nullable
    private Entity func_190538_a(Vec3d p_190538_1_, Vec3d p_190538_2_)
    {
        Entity entity = null;
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(motionX, motionY, motionZ).expandXyz(1.0D));
        double d0 = 0.0D;

        for (Entity entity1 : list)
        {
            if (entity1 != field_190539_a)
            {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz(0.30000001192092896D);
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(p_190538_1_, p_190538_2_);

                if (raytraceresult != null)
                {
                    double d1 = p_190538_1_.squareDistanceTo(raytraceresult.hitVec);

                    if (d1 < d0 || d0 == 0.0D)
                    {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x + rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        y = y + rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        z = z + rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        motionX = x;
        motionY = y;
        motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        rotationPitch = (float)(MathHelper.atan2(y, f1) * (180D / Math.PI));
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
    }

    public void func_190536_a(RayTraceResult p_190536_1_)
    {
        if (p_190536_1_.entityHit != null && field_190539_a != null)
        {
            p_190536_1_.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, field_190539_a).setProjectile(), 1.0F);
        }

        if (!world.isRemote)
        {
            setDead();
        }
    }

    protected void entityInit()
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        if (compound.hasKey("Owner", 10))
        {
            field_190540_b = compound.getCompoundTag("Owner");
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        if (field_190539_a != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            UUID uuid = field_190539_a.getUniqueID();
            nbttagcompound.setUniqueId("OwnerUUID", uuid);
            compound.setTag("Owner", nbttagcompound);
        }
    }

    private void func_190537_j()
    {
        if (field_190540_b != null && field_190540_b.hasUniqueId("OwnerUUID"))
        {
            UUID uuid = field_190540_b.getUniqueId("OwnerUUID");

            for (EntityLlama entityllama : world.getEntitiesWithinAABB(EntityLlama.class, getEntityBoundingBox().expandXyz(15.0D)))
            {
                if (entityllama.getUniqueID().equals(uuid))
                {
                    field_190539_a = entityllama;
                    break;
                }
            }
        }

        field_190540_b = null;
    }
}
