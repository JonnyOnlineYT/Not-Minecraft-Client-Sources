package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityOcelot extends EntityTameable
{
    private static final DataParameter<Integer> OCELOT_VARIANT = EntityDataManager.createKey(EntityOcelot.class, DataSerializers.VARINT);
    private EntityAIAvoidEntity<EntityPlayer> avoidEntity;

    /**
     * The tempt AI task for this mob, used to prevent taming while it is fleeing.
     */
    private EntityAITempt aiTempt;

    public EntityOcelot(World worldIn)
    {
        super(worldIn);
        setSize(0.6F, 0.7F);
    }

    protected void initEntityAI()
    {
        aiSit = new EntityAISit(this);
        aiTempt = new EntityAITempt(this, 0.6D, Items.FISH, true);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, aiSit);
        tasks.addTask(3, aiTempt);
        tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
        tasks.addTask(6, new EntityAIOcelotSit(this, 0.8D));
        tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
        tasks.addTask(8, new EntityAIOcelotAttack(this));
        tasks.addTask(9, new EntityAIMate(this, 0.8D));
        tasks.addTask(10, new EntityAIWanderAvoidWater(this, 0.8D, 1.0000001E-5F));
        tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        targetTasks.addTask(1, new EntityAITargetNonTamed(this, EntityChicken.class, false, null));
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(OCELOT_VARIANT, Integer.valueOf(0));
    }

    public void updateAITasks()
    {
        if (getMoveHelper().isUpdating())
        {
            double d0 = getMoveHelper().getSpeed();

            if (d0 == 0.6D)
            {
                setSneaking(true);
                setSprinting(false);
            }
            else if (d0 == 1.33D)
            {
                setSneaking(false);
                setSprinting(true);
            }
            else
            {
                setSneaking(false);
                setSprinting(false);
            }
        }
        else
        {
            setSneaking(false);
            setSprinting(false);
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return !isTamed() && ticksExisted > 2400;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    public static void registerFixesOcelot(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityOcelot.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("CatType", getTameSkin());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setTameSkin(compound.getInteger("CatType"));
    }

    @Nullable
    protected SoundEvent getAmbientSound()
    {
        if (isTamed())
        {
            if (isInLove())
            {
                return SoundEvents.ENTITY_CAT_PURR;
            }
            else
            {
                return rand.nextInt(4) == 0 ? SoundEvents.ENTITY_CAT_PURREOW : SoundEvents.ENTITY_CAT_AMBIENT;
            }
        }
        else
        {
            return null;
        }
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_CAT_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_CAT_DEATH;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            if (aiSit != null)
            {
                aiSit.setSitting(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_OCELOT;
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (isTamed())
        {
            if (isOwner(player) && !world.isRemote && !isBreedingItem(itemstack))
            {
                aiSit.setSitting(!isSitting());
            }
        }
        else if ((aiTempt == null || aiTempt.isRunning()) && itemstack.getItem() == Items.FISH && player.getDistanceSqToEntity(this) < 9.0D)
        {
            if (!player.capabilities.isCreativeMode)
            {
                itemstack.func_190918_g(1);
            }

            if (!world.isRemote)
            {
                if (rand.nextInt(3) == 0)
                {
                    func_193101_c(player);
                    setTameSkin(1 + world.rand.nextInt(3));
                    playTameEffect(true);
                    aiSit.setSitting(true);
                    world.setEntityState(this, (byte)7);
                }
                else
                {
                    playTameEffect(false);
                    world.setEntityState(this, (byte)6);
                }
            }

            return true;
        }

        return super.processInteract(player, hand);
    }

    public EntityOcelot createChild(EntityAgeable ageable)
    {
        EntityOcelot entityocelot = new EntityOcelot(world);

        if (isTamed())
        {
            entityocelot.setOwnerId(getOwnerId());
            entityocelot.setTamed(true);
            entityocelot.setTameSkin(getTameSkin());
        }

        return entityocelot;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack.getItem() == Items.FISH;
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        if (otherAnimal == this)
        {
            return false;
        }
        else if (!isTamed())
        {
            return false;
        }
        else if (!(otherAnimal instanceof EntityOcelot))
        {
            return false;
        }
        else
        {
            EntityOcelot entityocelot = (EntityOcelot)otherAnimal;

            if (!entityocelot.isTamed())
            {
                return false;
            }
            else
            {
                return isInLove() && entityocelot.isInLove();
            }
        }
    }

    public int getTameSkin()
    {
        return dataManager.get(OCELOT_VARIANT).intValue();
    }

    public void setTameSkin(int skinId)
    {
        dataManager.set(OCELOT_VARIANT, Integer.valueOf(skinId));
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return world.rand.nextInt(3) != 0;
    }

    /**
     * Checks that the entity is not colliding with any blocks / liquids
     */
    public boolean isNotColliding()
    {
        if (world.checkNoEntityCollision(getEntityBoundingBox(), this) && world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(getEntityBoundingBox()))
        {
            BlockPos blockpos = new BlockPos(posX, getEntityBoundingBox().minY, posZ);

            if (blockpos.getY() < world.getSeaLevel())
            {
                return false;
            }

            IBlockState iblockstate = world.getBlockState(blockpos.down());
            Block block = iblockstate.getBlock();

            return block == Blocks.GRASS || iblockstate.getMaterial() == Material.LEAVES;
        }

        return false;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        if (hasCustomName())
        {
            return getCustomNameTag();
        }
        else
        {
            return isTamed() ? I18n.translateToLocal("entity.Cat.name") : super.getName();
        }
    }

    protected void setupTamedAI()
    {
        if (avoidEntity == null)
        {
            avoidEntity = new EntityAIAvoidEntity<EntityPlayer>(this, EntityPlayer.class, 16.0F, 0.8D, 1.33D);
        }

        tasks.removeTask(avoidEntity);

        if (!isTamed())
        {
            tasks.addTask(4, avoidEntity);
        }
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (getTameSkin() == 0 && world.rand.nextInt(7) == 0)
        {
            for (int i = 0; i < 2; ++i)
            {
                EntityOcelot entityocelot = new EntityOcelot(world);
                entityocelot.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                entityocelot.setGrowingAge(-24000);
                world.spawnEntityInWorld(entityocelot);
            }
        }

        return livingdata;
    }
}
