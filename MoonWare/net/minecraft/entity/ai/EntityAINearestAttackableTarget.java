package net.minecraft.entity.ai;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAINearestAttackableTarget<T extends EntityLivingBase> extends EntityAITarget
{
    protected final Class<T> targetClass;
    private final int targetChance;

    /** Instance of EntityAINearestAttackableTargetSorter. */
    protected final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
    protected final Predicate <? super T > targetEntitySelector;
    protected T targetEntity;

    public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight)
    {
        this(creature, classTarget, checkSight, false);
    }

    public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby)
    {
        this(creature, classTarget, 10, checkSight, onlyNearby, null);
    }

    public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, @Nullable Predicate <? super T > targetSelector)
    {
        super(creature, checkSight, onlyNearby);
        targetClass = classTarget;
        targetChance = chance;
        theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(creature);
        setMutexBits(1);
        targetEntitySelector = new Predicate<T>()
        {
            public boolean apply(@Nullable T p_apply_1_)
            {
                if (p_apply_1_ == null)
                {
                    return false;
                }
                else if (targetSelector != null && !targetSelector.apply(p_apply_1_))
                {
                    return false;
                }
                else
                {
                    return EntitySelectors.NOT_SPECTATING.apply(p_apply_1_) && isSuitableTarget(p_apply_1_, false);
                }
            }
        };
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (targetChance > 0 && taskOwner.getRNG().nextInt(targetChance) != 0)
        {
            return false;
        }
        else if (targetClass != EntityPlayer.class && targetClass != EntityPlayerMP.class)
        {
            List<T> list = taskOwner.world.getEntitiesWithinAABB(targetClass, getTargetableArea(getTargetDistance()), targetEntitySelector);

            if (list.isEmpty())
            {
                return false;
            }
            else
            {
                Collections.sort(list, theNearestAttackableTargetSorter);
                targetEntity = list.get(0);
                return true;
            }
        }
        else
        {
            targetEntity = (T) taskOwner.world.getNearestAttackablePlayer(taskOwner.posX, taskOwner.posY + (double) taskOwner.getEyeHeight(), taskOwner.posZ, getTargetDistance(), getTargetDistance(), new Function<EntityPlayer, Double>()
            {
                @Nullable
                public Double apply(@Nullable EntityPlayer p_apply_1_)
                {
                    ItemStack itemstack = p_apply_1_.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                    if (itemstack.getItem() == Items.SKULL)
                    {
                        int i = itemstack.getItemDamage();
                        boolean flag = taskOwner instanceof EntitySkeleton && i == 0;
                        boolean flag1 = taskOwner instanceof EntityZombie && i == 2;
                        boolean flag2 = taskOwner instanceof EntityCreeper && i == 4;

                        if (flag || flag1 || flag2)
                        {
                            return 0.5D;
                        }
                    }

                    return 1.0D;
                }
            }, (Predicate<EntityPlayer>) targetEntitySelector);
            return targetEntity != null;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance)
    {
        return taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        taskOwner.setAttackTarget(targetEntity);
        super.startExecuting();
    }

    public static class Sorter implements Comparator<Entity>
    {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn)
        {
            theEntity = theEntityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_)
        {
            double d0 = theEntity.getDistanceSqToEntity(p_compare_1_);
            double d1 = theEntity.getDistanceSqToEntity(p_compare_2_);

            if (d0 < d1)
            {
                return -1;
            }
            else
            {
                return d0 > d1 ? 1 : 0;
            }
        }
    }
}
