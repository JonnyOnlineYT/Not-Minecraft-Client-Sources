package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySpider extends EntityMob
{
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntitySpider.class, DataSerializers.BYTE);

    public EntitySpider(World worldIn)
    {
        super(worldIn);
        setSize(1.4F, 0.9F);
    }

    public static void registerFixesSpider(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntitySpider.class);
    }

    protected void initEntityAI()
    {
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        tasks.addTask(4, new EntitySpider.AISpiderAttack(this));
        tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntitySpider.AISpiderTarget(this, EntityPlayer.class));
        targetTasks.addTask(3, new EntitySpider.AISpiderTarget(this, EntityIronGolem.class));
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return height * 0.5F;
    }

    /**
     * Returns new PathNavigateGround instance
     */
    protected PathNavigate getNewNavigator(World worldIn)
    {
        return new PathNavigateClimber(this, worldIn);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(CLIMBING, Byte.valueOf((byte)0));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!world.isRemote)
        {
            setBesideClimbableBlock(isCollidedHorizontally);
        }
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_SPIDER;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return isBesideClimbableBlock();
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb()
    {
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn)
    {
        return potioneffectIn.getPotion() != MobEffects.POISON && super.isPotionApplicable(potioneffectIn);
    }

    /**
     * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns false. The WatchableObject is updated using
     * setBesideClimableBlock.
     */
    public boolean isBesideClimbableBlock()
    {
        return (dataManager.get(CLIMBING).byteValue() & 1) != 0;
    }

    /**
     * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is
     * false.
     */
    public void setBesideClimbableBlock(boolean climbing)
    {
        byte b0 = dataManager.get(CLIMBING).byteValue();

        if (climbing)
        {
            b0 = (byte)(b0 | 1);
        }
        else
        {
            b0 = (byte)(b0 & -2);
        }

        dataManager.set(CLIMBING, Byte.valueOf(b0));
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (world.rand.nextInt(100) == 0)
        {
            EntitySkeleton entityskeleton = new EntitySkeleton(world);
            entityskeleton.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            entityskeleton.onInitialSpawn(difficulty, null);
            world.spawnEntityInWorld(entityskeleton);
            entityskeleton.startRiding(this);
        }

        if (livingdata == null)
        {
            livingdata = new EntitySpider.GroupData();

            if (world.getDifficulty() == EnumDifficulty.HARD && world.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty())
            {
                ((EntitySpider.GroupData)livingdata).setRandomEffect(world.rand);
            }
        }

        if (livingdata instanceof EntitySpider.GroupData)
        {
            Potion potion = ((EntitySpider.GroupData)livingdata).effect;

            if (potion != null)
            {
                addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE));
            }
        }

        return livingdata;
    }

    public float getEyeHeight()
    {
        return 0.65F;
    }

    static class AISpiderAttack extends EntityAIAttackMelee
    {
        public AISpiderAttack(EntitySpider spider)
        {
            super(spider, 1.0D, true);
        }

        public boolean continueExecuting()
        {
            float f = attacker.getBrightness();

            if (f >= 0.5F && attacker.getRNG().nextInt(100) == 0)
            {
                attacker.setAttackTarget(null);
                return false;
            }
            else
            {
                return super.continueExecuting();
            }
        }

        protected double getAttackReachSqr(EntityLivingBase attackTarget)
        {
            return 4.0F + attackTarget.width;
        }
    }

    static class AISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>
    {
        public AISpiderTarget(EntitySpider spider, Class<T> classTarget)
        {
            super(spider, classTarget, true);
        }

        public boolean shouldExecute()
        {
            float f = taskOwner.getBrightness();
            return !(f >= 0.5F) && super.shouldExecute();
        }
    }

    public static class GroupData implements IEntityLivingData
    {
        public Potion effect;

        public void setRandomEffect(Random rand)
        {
            int i = rand.nextInt(5);

            if (i <= 1)
            {
                effect = MobEffects.SPEED;
            }
            else if (i <= 2)
            {
                effect = MobEffects.STRENGTH;
            }
            else if (i <= 3)
            {
                effect = MobEffects.REGENERATION;
            }
            else if (i <= 4)
            {
                effect = MobEffects.INVISIBILITY;
            }
        }
    }
}
