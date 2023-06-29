package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature
{
    private static final DataParameter<Boolean> BABY = EntityDataManager.createKey(EntityAgeable.class, DataSerializers.BOOLEAN);
    protected int growingAge;
    protected int forcedAge;
    protected int forcedAgeTimer;
    private float ageWidth = -1.0F;
    private float ageHeight;

    public EntityAgeable(World worldIn)
    {
        super(worldIn);
    }

    @Nullable
    public abstract EntityAgeable createChild(EntityAgeable ageable);

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.SPAWN_EGG)
        {
            if (!world.isRemote)
            {
                Class <? extends Entity > oclass = EntityList.REGISTRY.getObject(ItemMonsterPlacer.func_190908_h(itemstack));

                if (oclass != null && getClass() == oclass)
                {
                    EntityAgeable entityageable = createChild(this);

                    if (entityageable != null)
                    {
                        entityageable.setGrowingAge(-24000);
                        entityageable.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
                        world.spawnEntityInWorld(entityageable);

                        if (itemstack.hasDisplayName())
                        {
                            entityageable.setCustomNameTag(itemstack.getDisplayName());
                        }

                        if (!player.capabilities.isCreativeMode)
                        {
                            itemstack.func_190918_g(1);
                        }
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean func_190669_a(ItemStack p_190669_1_, Class <? extends Entity > p_190669_2_)
    {
        if (p_190669_1_.getItem() != Items.SPAWN_EGG)
        {
            return false;
        }
        else
        {
            Class <? extends Entity > oclass = EntityList.REGISTRY.getObject(ItemMonsterPlacer.func_190908_h(p_190669_1_));
            return oclass != null && p_190669_2_ == oclass;
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(BABY, Boolean.valueOf(false));
    }

    /**
     * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's
     * positive, it get's decremented each tick. Don't confuse this with EntityLiving.getAge. With a negative value the
     * Entity is considered a child.
     */
    public int getGrowingAge()
    {
        if (world.isRemote)
        {
            return dataManager.get(BABY).booleanValue() ? -1 : 1;
        }
        else
        {
            return growingAge;
        }
    }

    public void ageUp(int p_175501_1_, boolean p_175501_2_)
    {
        int i = getGrowingAge();
        int j = i;
        i = i + p_175501_1_ * 20;

        if (i > 0)
        {
            i = 0;

            if (j < 0)
            {
                onGrowingAdult();
            }
        }

        int k = i - j;
        setGrowingAge(i);

        if (p_175501_2_)
        {
            forcedAge += k;

            if (forcedAgeTimer == 0)
            {
                forcedAgeTimer = 40;
            }
        }

        if (getGrowingAge() == 0)
        {
            setGrowingAge(forcedAge);
        }
    }

    /**
     * "Adds the value of the parameter times 20 to the age of this entity. If the entity is an adult (if the entity's
     * age is greater than 0), it will have no effect."
     */
    public void addGrowth(int growth)
    {
        ageUp(growth, false);
    }

    /**
     * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's
     * positive, it get's decremented each tick. With a negative value the Entity is considered a child.
     */
    public void setGrowingAge(int age)
    {
        dataManager.set(BABY, Boolean.valueOf(age < 0));
        growingAge = age;
        setScaleForAge(isChild());
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Age", getGrowingAge());
        compound.setInteger("ForcedAge", forcedAge);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setGrowingAge(compound.getInteger("Age"));
        forcedAge = compound.getInteger("ForcedAge");
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (BABY.equals(key))
        {
            setScaleForAge(isChild());
        }

        super.notifyDataManagerChange(key);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (world.isRemote)
        {
            if (forcedAgeTimer > 0)
            {
                if (forcedAgeTimer % 4 == 0)
                {
                    world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, posX + (double)(rand.nextFloat() * width * 2.0F) - (double) width, posY + 0.5D + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double) width, 0.0D, 0.0D, 0.0D);
                }

                --forcedAgeTimer;
            }
        }
        else
        {
            int i = getGrowingAge();

            if (i < 0)
            {
                ++i;
                setGrowingAge(i);

                if (i == 0)
                {
                    onGrowingAdult();
                }
            }
            else if (i > 0)
            {
                --i;
                setGrowingAge(i);
            }
        }
    }

    /**
     * This is called when Entity's growing age timer reaches 0 (negative values are considered as a child, positive as
     * an adult)
     */
    protected void onGrowingAdult()
    {
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return getGrowingAge() < 0;
    }

    /**
     * "Sets the scale for an ageable entity according to the boolean parameter, which says if it's a child."
     */
    public void setScaleForAge(boolean child)
    {
        setScale(child ? 0.5F : 1.0F);
    }

    /**
     * Sets the width and height of the entity.
     */
    protected final void setSize(float width, float height)
    {
        boolean flag = ageWidth > 0.0F;
        ageWidth = width;
        ageHeight = height;

        if (!flag)
        {
            setScale(1.0F);
        }
    }

    protected final void setScale(float scale)
    {
        super.setSize(ageWidth * scale, ageHeight * scale);
    }
}
