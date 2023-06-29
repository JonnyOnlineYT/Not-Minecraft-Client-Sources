package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityBlaze extends EntityMob
{
    /** Random offset used in floating behaviour */
    private float heightOffset = 0.5F;

    /** ticks until heightOffset is randomized */
    private int heightOffsetUpdateTime;
    private static final DataParameter<Byte> ON_FIRE = EntityDataManager.createKey(EntityBlaze.class, DataSerializers.BYTE);

    public EntityBlaze(World worldIn)
    {
        super(worldIn);
        setPathPriority(PathNodeType.WATER, -1.0F);
        setPathPriority(PathNodeType.LAVA, 8.0F);
        setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
        isImmuneToFire = true;
        experienceValue = 10;
    }

    public static void registerFixesBlaze(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityBlaze.class);
    }

    protected void initEntityAI()
    {
        tasks.addTask(4, new EntityBlaze.AIFireballAttack(this));
        tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(ON_FIRE, Byte.valueOf((byte)0));
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_BLAZE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_BLAZE_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_BLAZE_DEATH;
    }

    public int getBrightnessForRender()
    {
        return 15728880;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness()
    {
        return 1.0F;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (!onGround && motionY < 0.0D)
        {
            motionY *= 0.6D;
        }

        if (world.isRemote)
        {
            if (rand.nextInt(24) == 0 && !isSilent())
            {
                world.playSound(posX + 0.5D, posY + 0.5D, posZ + 0.5D, SoundEvents.ENTITY_BLAZE_BURN, getSoundCategory(), 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
            }

            for (int i = 0; i < 2; ++i)
            {
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX + (rand.nextDouble() - 0.5D) * (double) width, posY + rand.nextDouble() * (double) height, posZ + (rand.nextDouble() - 0.5D) * (double) width, 0.0D, 0.0D, 0.0D);
            }
        }

        super.onLivingUpdate();
    }

    protected void updateAITasks()
    {
        if (isWet())
        {
            attackEntityFrom(DamageSource.drown, 1.0F);
        }

        --heightOffsetUpdateTime;

        if (heightOffsetUpdateTime <= 0)
        {
            heightOffsetUpdateTime = 100;
            heightOffset = 0.5F + (float) rand.nextGaussian() * 3.0F;
        }

        EntityLivingBase entitylivingbase = getAttackTarget();

        if (entitylivingbase != null && entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() > posY + (double) getEyeHeight() + (double) heightOffset)
        {
            motionY += (0.30000001192092896D - motionY) * 0.30000001192092896D;
            isAirBorne = true;
        }

        super.updateAITasks();
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning()
    {
        return isCharged();
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_BLAZE;
    }

    public boolean isCharged()
    {
        return (dataManager.get(ON_FIRE).byteValue() & 1) != 0;
    }

    public void setOnFire(boolean onFire)
    {
        byte b0 = dataManager.get(ON_FIRE).byteValue();

        if (onFire)
        {
            b0 = (byte)(b0 | 1);
        }
        else
        {
            b0 = (byte)(b0 & -2);
        }

        dataManager.set(ON_FIRE, Byte.valueOf(b0));
    }

    /**
     * Checks to make sure the light is not too bright where the mob is spawning
     */
    protected boolean isValidLightLevel()
    {
        return true;
    }

    static class AIFireballAttack extends EntityAIBase
    {
        private final EntityBlaze blaze;
        private int attackStep;
        private int attackTime;

        public AIFireballAttack(EntityBlaze blazeIn)
        {
            blaze = blazeIn;
            setMutexBits(3);
        }

        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = blaze.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        public void startExecuting()
        {
            attackStep = 0;
        }

        public void resetTask()
        {
            blaze.setOnFire(false);
        }

        public void updateTask()
        {
            --attackTime;
            EntityLivingBase entitylivingbase = blaze.getAttackTarget();
            double d0 = blaze.getDistanceSqToEntity(entitylivingbase);

            if (d0 < 4.0D)
            {
                if (attackTime <= 0)
                {
                    attackTime = 20;
                    blaze.attackEntityAsMob(entitylivingbase);
                }

                blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }
            else if (d0 < func_191523_f() * func_191523_f())
            {
                double d1 = entitylivingbase.posX - blaze.posX;
                double d2 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (blaze.posY + (double)(blaze.height / 2.0F));
                double d3 = entitylivingbase.posZ - blaze.posZ;

                if (attackTime <= 0)
                {
                    ++attackStep;

                    if (attackStep == 1)
                    {
                        attackTime = 60;
                        blaze.setOnFire(true);
                    }
                    else if (attackStep <= 4)
                    {
                        attackTime = 6;
                    }
                    else
                    {
                        attackTime = 100;
                        attackStep = 0;
                        blaze.setOnFire(false);
                    }

                    if (attackStep > 1)
                    {
                        float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                        blaze.world.playEvent(null, 1018, new BlockPos((int) blaze.posX, (int) blaze.posY, (int) blaze.posZ), 0);

                        for (int i = 0; i < 1; ++i)
                        {
                            EntitySmallFireball entitysmallfireball = new EntitySmallFireball(blaze.world, blaze, d1 + blaze.getRNG().nextGaussian() * (double)f, d2, d3 + blaze.getRNG().nextGaussian() * (double)f);
                            entitysmallfireball.posY = blaze.posY + (double)(blaze.height / 2.0F) + 0.5D;
                            blaze.world.spawnEntityInWorld(entitysmallfireball);
                        }
                    }
                }

                blaze.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            }
            else
            {
                blaze.getNavigator().clearPathEntity();
                blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }

            super.updateTask();
        }

        private double func_191523_f()
        {
            IAttributeInstance iattributeinstance = blaze.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
            return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
        }
    }
}
