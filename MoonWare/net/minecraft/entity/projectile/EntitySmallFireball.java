package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySmallFireball extends EntityFireball
{
    public EntitySmallFireball(World worldIn)
    {
        super(worldIn);
        setSize(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
        setSize(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        setSize(0.3125F, 0.3125F);
    }

    public static void registerFixesSmallFireball(DataFixer fixer)
    {
        EntityFireball.registerFixesFireball(fixer, "SmallFireball");
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        if (!world.isRemote)
        {
            if (result.entityHit != null)
            {
                if (!result.entityHit.isImmuneToFire())
                {
                    boolean flag = result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, shootingEntity), 5.0F);

                    if (flag)
                    {
                        applyEnchantments(shootingEntity, result.entityHit);
                        result.entityHit.setFire(5);
                    }
                }
            }
            else
            {
                boolean flag1 = true;

                if (shootingEntity != null && shootingEntity instanceof EntityLiving)
                {
                    flag1 = world.getGameRules().getBoolean("mobGriefing");
                }

                if (flag1)
                {
                    BlockPos blockpos = result.getBlockPos().offset(result.sideHit);

                    if (world.isAirBlock(blockpos))
                    {
                        world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                    }
                }
            }

            setDead();
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }
}
