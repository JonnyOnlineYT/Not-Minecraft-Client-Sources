package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPotion extends EntityThrowable
{
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityPotion.class, DataSerializers.OPTIONAL_ITEM_STACK);
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Predicate<EntityLivingBase> field_190546_d = new Predicate<EntityLivingBase>()
    {
        public boolean apply(@Nullable EntityLivingBase p_apply_1_)
        {
            return func_190544_c(p_apply_1_);
        }
    };

    public EntityPotion(World worldIn)
    {
        super(worldIn);
    }

    public EntityPotion(World worldIn, EntityLivingBase throwerIn, ItemStack potionDamageIn)
    {
        super(worldIn, throwerIn);
        setItem(potionDamageIn);
    }

    public EntityPotion(World worldIn, double x, double y, double z, ItemStack potionDamageIn)
    {
        super(worldIn, x, y, z);

        if (!potionDamageIn.isEmpty())
        {
            setItem(potionDamageIn);
        }
    }

    protected void entityInit()
    {
        getDataManager().register(ITEM, ItemStack.EMPTY);
    }

    public ItemStack getPotion()
    {
        ItemStack itemstack = getDataManager().get(ITEM);

        if (itemstack.getItem() != Items.SPLASH_POTION && itemstack.getItem() != Items.LINGERING_POTION)
        {
            if (world != null)
            {
                LOGGER.error("ThrownPotion entity {} has no item?!", getEntityId());
            }

            return new ItemStack(Items.SPLASH_POTION);
        }
        else
        {
            return itemstack;
        }
    }

    public void setItem(ItemStack stack)
    {
        getDataManager().set(ITEM, stack);
        getDataManager().setDirty(ITEM);
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    protected float getGravityVelocity()
    {
        return 0.05F;
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        if (!world.isRemote)
        {
            ItemStack itemstack = getPotion();
            PotionType potiontype = PotionUtils.getPotionFromItem(itemstack);
            List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);
            boolean flag = potiontype == PotionTypes.WATER && list.isEmpty();

            if (result.typeOfHit == RayTraceResult.Type.BLOCK && flag)
            {
                BlockPos blockpos = result.getBlockPos().offset(result.sideHit);
                extinguishFires(blockpos, result.sideHit);

                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                {
                    extinguishFires(blockpos.offset(enumfacing), enumfacing);
                }
            }

            if (flag)
            {
                func_190545_n();
            }
            else if (!list.isEmpty())
            {
                if (isLingering())
                {
                    func_190542_a(itemstack, potiontype);
                }
                else
                {
                    func_190543_a(result, list);
                }
            }

            int i = potiontype.hasInstantEffect() ? 2007 : 2002;
            world.playEvent(i, new BlockPos(this), PotionUtils.func_190932_c(itemstack));
            setDead();
        }
    }

    private void func_190545_n()
    {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D);
        List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, field_190546_d);

        if (!list.isEmpty())
        {
            for (EntityLivingBase entitylivingbase : list)
            {
                double d0 = getDistanceSqToEntity(entitylivingbase);

                if (d0 < 16.0D && func_190544_c(entitylivingbase))
                {
                    entitylivingbase.attackEntityFrom(DamageSource.drown, 1.0F);
                }
            }
        }
    }

    private void func_190543_a(RayTraceResult p_190543_1_, List<PotionEffect> p_190543_2_)
    {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D);
        List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

        if (!list.isEmpty())
        {
            for (EntityLivingBase entitylivingbase : list)
            {
                if (entitylivingbase.canBeHitWithPotion())
                {
                    double d0 = getDistanceSqToEntity(entitylivingbase);

                    if (d0 < 16.0D)
                    {
                        double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                        if (entitylivingbase == p_190543_1_.entityHit)
                        {
                            d1 = 1.0D;
                        }

                        for (PotionEffect potioneffect : p_190543_2_)
                        {
                            Potion potion = potioneffect.getPotion();

                            if (potion.isInstant())
                            {
                                potion.affectEntity(this, getThrower(), entitylivingbase, potioneffect.getAmplifier(), d1);
                            }
                            else
                            {
                                int i = (int)(d1 * (double)potioneffect.getDuration() + 0.5D);

                                if (i > 20)
                                {
                                    entitylivingbase.addPotionEffect(new PotionEffect(potion, i, potioneffect.getAmplifier(), potioneffect.getIsAmbient(), potioneffect.doesShowParticles()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void func_190542_a(ItemStack p_190542_1_, PotionType p_190542_2_)
    {
        EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(world, posX, posY, posZ);
        entityareaeffectcloud.setOwner(getThrower());
        entityareaeffectcloud.setRadius(3.0F);
        entityareaeffectcloud.setRadiusOnUse(-0.5F);
        entityareaeffectcloud.setWaitTime(10);
        entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float)entityareaeffectcloud.getDuration());
        entityareaeffectcloud.setPotion(p_190542_2_);

        for (PotionEffect potioneffect : PotionUtils.getFullEffectsFromItem(p_190542_1_))
        {
            entityareaeffectcloud.addEffect(new PotionEffect(potioneffect));
        }

        NBTTagCompound nbttagcompound = p_190542_1_.getTagCompound();

        if (nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99))
        {
            entityareaeffectcloud.setColor(nbttagcompound.getInteger("CustomPotionColor"));
        }

        world.spawnEntityInWorld(entityareaeffectcloud);
    }

    private boolean isLingering()
    {
        return getPotion().getItem() == Items.LINGERING_POTION;
    }

    private void extinguishFires(BlockPos pos, EnumFacing p_184542_2_)
    {
        if (world.getBlockState(pos).getBlock() == Blocks.FIRE)
        {
            world.extinguishFire(null, pos.offset(p_184542_2_), p_184542_2_.getOpposite());
        }
    }

    public static void registerFixesPotion(DataFixer fixer)
    {
        EntityThrowable.registerFixesThrowable(fixer, "ThrownPotion");
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityPotion.class, "Potion"));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        ItemStack itemstack = new ItemStack(compound.getCompoundTag("Potion"));

        if (itemstack.isEmpty())
        {
            setDead();
        }
        else
        {
            setItem(itemstack);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        ItemStack itemstack = getPotion();

        if (!itemstack.isEmpty())
        {
            compound.setTag("Potion", itemstack.writeToNBT(new NBTTagCompound()));
        }
    }

    private static boolean func_190544_c(EntityLivingBase p_190544_0_)
    {
        return p_190544_0_ instanceof EntityEnderman || p_190544_0_ instanceof EntityBlaze;
    }
}
