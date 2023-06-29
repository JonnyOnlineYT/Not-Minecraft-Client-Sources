package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEgg extends EntityThrowable
{
    public EntityEgg(World worldIn)
    {
        super(worldIn);
    }

    public EntityEgg(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public EntityEgg(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static void registerFixesEgg(DataFixer fixer)
    {
        EntityThrowable.registerFixesThrowable(fixer, "ThrownEgg");
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 3)
        {
            double d0 = 0.08D;

            for (int i = 0; i < 8; ++i)
            {
                world.spawnParticle(EnumParticleTypes.ITEM_CRACK, posX, posY, posZ, ((double) rand.nextFloat() - 0.5D) * 0.08D, ((double) rand.nextFloat() - 0.5D) * 0.08D, ((double) rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(Items.EGG));
            }
        }
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null)
        {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0.0F);
        }

        if (!world.isRemote)
        {
            if (rand.nextInt(8) == 0)
            {
                int i = 1;

                if (rand.nextInt(32) == 0)
                {
                    i = 4;
                }

                for (int j = 0; j < i; ++j)
                {
                    EntityChicken entitychicken = new EntityChicken(world);
                    entitychicken.setGrowingAge(-24000);
                    entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    world.spawnEntityInWorld(entitychicken);
                }
            }

            world.setEntityState(this, (byte)3);
            setDead();
        }
    }
}
