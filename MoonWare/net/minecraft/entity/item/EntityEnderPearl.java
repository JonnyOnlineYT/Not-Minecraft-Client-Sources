package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEnderPearl extends EntityThrowable
{
    private EntityLivingBase thrower;

    public EntityEnderPearl(World worldIn)
    {
        super(worldIn);
    }

    public EntityEnderPearl(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
        thrower = throwerIn;
    }

    public EntityEnderPearl(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static void registerFixesEnderPearl(DataFixer fixer)
    {
        EntityThrowable.registerFixesThrowable(fixer, "ThrownEnderpearl");
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        EntityLivingBase entitylivingbase = getThrower();

        if (result.entityHit != null)
        {
            if (result.entityHit == thrower)
            {
                return;
            }

            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, entitylivingbase), 0.0F);
        }

        if (result.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos blockpos = result.getBlockPos();
            TileEntity tileentity = world.getTileEntity(blockpos);

            if (tileentity instanceof TileEntityEndGateway)
            {
                TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)tileentity;

                if (entitylivingbase != null)
                {
                    if (entitylivingbase instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.field_192124_d.func_192193_a((EntityPlayerMP)entitylivingbase, world.getBlockState(blockpos));
                    }

                    tileentityendgateway.teleportEntity(entitylivingbase);
                    setDead();
                    return;
                }

                tileentityendgateway.teleportEntity(this);
                return;
            }
        }

        for (int i = 0; i < 32; ++i)
        {
            world.spawnParticle(EnumParticleTypes.PORTAL, posX, posY + rand.nextDouble() * 2.0D, posZ, rand.nextGaussian(), 0.0D, rand.nextGaussian());
        }

        if (!world.isRemote)
        {
            if (entitylivingbase instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)entitylivingbase;

                if (entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.world == world && !entityplayermp.isPlayerSleeping())
                {
                    if (rand.nextFloat() < 0.05F && world.getGameRules().getBoolean("doMobSpawning"))
                    {
                        EntityEndermite entityendermite = new EntityEndermite(world);
                        entityendermite.setSpawnedByPlayer(true);
                        entityendermite.setLocationAndAngles(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, entitylivingbase.rotationYaw, entitylivingbase.rotationPitch);
                        world.spawnEntityInWorld(entityendermite);
                    }

                    if (entitylivingbase.isRiding())
                    {
                        entitylivingbase.dismountRidingEntity();
                    }

                    entitylivingbase.setPositionAndUpdate(posX, posY, posZ);
                    entitylivingbase.fallDistance = 0.0F;
                    entitylivingbase.attackEntityFrom(DamageSource.fall, 5.0F);
                }
            }
            else if (entitylivingbase != null)
            {
                entitylivingbase.setPositionAndUpdate(posX, posY, posZ);
                entitylivingbase.fallDistance = 0.0F;
            }

            setDead();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        EntityLivingBase entitylivingbase = getThrower();

        if (entitylivingbase != null && entitylivingbase instanceof EntityPlayer && !entitylivingbase.isEntityAlive())
        {
            setDead();
        }
        else
        {
            super.onUpdate();
        }
    }

    @Nullable
    public Entity changeDimension(int dimensionIn)
    {
        if (thrower.dimension != dimensionIn)
        {
            thrower = null;
        }

        return super.changeDimension(dimensionIn);
    }
}
