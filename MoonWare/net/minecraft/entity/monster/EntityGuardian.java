package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGuardian extends EntityMob
{
    private static final DataParameter<Boolean> field_190766_bz = EntityDataManager.createKey(EntityGuardian.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TARGET_ENTITY = EntityDataManager.createKey(EntityGuardian.class, DataSerializers.VARINT);
    protected float clientSideTailAnimation;
    protected float clientSideTailAnimationO;
    protected float clientSideTailAnimationSpeed;
    protected float clientSideSpikesAnimation;
    protected float clientSideSpikesAnimationO;
    private EntityLivingBase targetedEntity;
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;
    protected EntityAIWander wander;

    public EntityGuardian(World worldIn)
    {
        super(worldIn);
        experienceValue = 10;
        setSize(0.85F, 0.85F);
        moveHelper = new EntityGuardian.GuardianMoveHelper(this);
        clientSideTailAnimation = rand.nextFloat();
        clientSideTailAnimationO = clientSideTailAnimation;
    }

    protected void initEntityAI()
    {
        EntityAIMoveTowardsRestriction entityaimovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0D);
        wander = new EntityAIWander(this, 1.0D, 80);
        tasks.addTask(4, new EntityGuardian.AIGuardianAttack(this));
        tasks.addTask(5, entityaimovetowardsrestriction);
        tasks.addTask(7, wander);
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0F, 0.01F));
        tasks.addTask(9, new EntityAILookIdle(this));
        wander.setMutexBits(3);
        entityaimovetowardsrestriction.setMutexBits(3);
        targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, new EntityGuardian.GuardianTargetSelector(this)));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
    }

    public static void registerFixesGuardian(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityGuardian.class);
    }

    /**
     * Returns new PathNavigateGround instance
     */
    protected PathNavigate getNewNavigator(World worldIn)
    {
        return new PathNavigateSwimmer(this, worldIn);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(field_190766_bz, Boolean.valueOf(false));
        dataManager.register(TARGET_ENTITY, Integer.valueOf(0));
    }

    public boolean isMoving()
    {
        return dataManager.get(field_190766_bz).booleanValue();
    }

    private void setMoving(boolean moving)
    {
        dataManager.set(field_190766_bz, Boolean.valueOf(moving));
    }

    public int getAttackDuration()
    {
        return 80;
    }

    private void setTargetedEntity(int entityId)
    {
        dataManager.set(TARGET_ENTITY, Integer.valueOf(entityId));
    }

    public boolean hasTargetedEntity()
    {
        return dataManager.get(TARGET_ENTITY).intValue() != 0;
    }

    @Nullable
    public EntityLivingBase getTargetedEntity()
    {
        if (!hasTargetedEntity())
        {
            return null;
        }
        else if (world.isRemote)
        {
            if (targetedEntity != null)
            {
                return targetedEntity;
            }
            else
            {
                Entity entity = world.getEntityByID(dataManager.get(TARGET_ENTITY).intValue());

                if (entity instanceof EntityLivingBase)
                {
                    targetedEntity = (EntityLivingBase)entity;
                    return targetedEntity;
                }
                else
                {
                    return null;
                }
            }
        }
        else
        {
            return getAttackTarget();
        }
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);

        if (TARGET_ENTITY.equals(key))
        {
            clientSideAttackTime = 0;
            targetedEntity = null;
        }
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval()
    {
        return 160;
    }

    protected SoundEvent getAmbientSound()
    {
        return isInWater() ? SoundEvents.ENTITY_GUARDIAN_AMBIENT : SoundEvents.ENTITY_GUARDIAN_AMBIENT_LAND;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return isInWater() ? SoundEvents.ENTITY_GUARDIAN_HURT : SoundEvents.ENTITY_GUARDIAN_HURT_LAND;
    }

    protected SoundEvent getDeathSound()
    {
        return isInWater() ? SoundEvents.ENTITY_GUARDIAN_DEATH : SoundEvents.ENTITY_GUARDIAN_DEATH_LAND;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public float getEyeHeight()
    {
        return height * 0.5F;
    }

    public float getBlockPathWeight(BlockPos pos)
    {
        return world.getBlockState(pos).getMaterial() == Material.WATER ? 10.0F + world.getLightBrightness(pos) - 0.5F : super.getBlockPathWeight(pos);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (world.isRemote)
        {
            clientSideTailAnimationO = clientSideTailAnimation;

            if (!isInWater())
            {
                clientSideTailAnimationSpeed = 2.0F;

                if (motionY > 0.0D && clientSideTouchedGround && !isSilent())
                {
                    world.playSound(posX, posY, posZ, func_190765_dj(), getSoundCategory(), 1.0F, 1.0F, false);
                }

                clientSideTouchedGround = motionY < 0.0D && world.isBlockNormalCube((new BlockPos(this)).down(), false);
            }
            else if (isMoving())
            {
                if (clientSideTailAnimationSpeed < 0.5F)
                {
                    clientSideTailAnimationSpeed = 4.0F;
                }
                else
                {
                    clientSideTailAnimationSpeed += (0.5F - clientSideTailAnimationSpeed) * 0.1F;
                }
            }
            else
            {
                clientSideTailAnimationSpeed += (0.125F - clientSideTailAnimationSpeed) * 0.2F;
            }

            clientSideTailAnimation += clientSideTailAnimationSpeed;
            clientSideSpikesAnimationO = clientSideSpikesAnimation;

            if (!isInWater())
            {
                clientSideSpikesAnimation = rand.nextFloat();
            }
            else if (isMoving())
            {
                clientSideSpikesAnimation += (0.0F - clientSideSpikesAnimation) * 0.25F;
            }
            else
            {
                clientSideSpikesAnimation += (1.0F - clientSideSpikesAnimation) * 0.06F;
            }

            if (isMoving() && isInWater())
            {
                Vec3d vec3d = getLook(0.0F);

                for (int i = 0; i < 2; ++i)
                {
                    world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX + (rand.nextDouble() - 0.5D) * (double) width - vec3d.xCoord * 1.5D, posY + rand.nextDouble() * (double) height - vec3d.yCoord * 1.5D, posZ + (rand.nextDouble() - 0.5D) * (double) width - vec3d.zCoord * 1.5D, 0.0D, 0.0D, 0.0D);
                }
            }

            if (hasTargetedEntity())
            {
                if (clientSideAttackTime < getAttackDuration())
                {
                    ++clientSideAttackTime;
                }

                EntityLivingBase entitylivingbase = getTargetedEntity();

                if (entitylivingbase != null)
                {
                    getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);
                    getLookHelper().onUpdateLook();
                    double d5 = getAttackAnimationScale(0.0F);
                    double d0 = entitylivingbase.posX - posX;
                    double d1 = entitylivingbase.posY + (double)(entitylivingbase.height * 0.5F) - (posY + (double) getEyeHeight());
                    double d2 = entitylivingbase.posZ - posZ;
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    d0 = d0 / d3;
                    d1 = d1 / d3;
                    d2 = d2 / d3;
                    double d4 = rand.nextDouble();

                    while (d4 < d3)
                    {
                        d4 += 1.8D - d5 + rand.nextDouble() * (1.7D - d5);
                        world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX + d0 * d4, posY + d1 * d4 + (double) getEyeHeight(), posZ + d2 * d4, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }

        if (inWater)
        {
            setAir(300);
        }
        else if (onGround)
        {
            motionY += 0.5D;
            motionX += (rand.nextFloat() * 2.0F - 1.0F) * 0.4F;
            motionZ += (rand.nextFloat() * 2.0F - 1.0F) * 0.4F;
            rotationYaw = rand.nextFloat() * 360.0F;
            onGround = false;
            isAirBorne = true;
        }

        if (hasTargetedEntity())
        {
            rotationYaw = rotationYawHead;
        }

        super.onLivingUpdate();
    }

    protected SoundEvent func_190765_dj()
    {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }

    public float getTailAnimation(float p_175471_1_)
    {
        return clientSideTailAnimationO + (clientSideTailAnimation - clientSideTailAnimationO) * p_175471_1_;
    }

    public float getSpikesAnimation(float p_175469_1_)
    {
        return clientSideSpikesAnimationO + (clientSideSpikesAnimation - clientSideSpikesAnimationO) * p_175469_1_;
    }

    public float getAttackAnimationScale(float p_175477_1_)
    {
        return ((float) clientSideAttackTime + p_175477_1_) / (float) getAttackDuration();
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_GUARDIAN;
    }

    /**
     * Checks to make sure the light is not too bright where the mob is spawning
     */
    protected boolean isValidLightLevel()
    {
        return true;
    }

    /**
     * Checks that the entity is not colliding with any blocks / liquids
     */
    public boolean isNotColliding()
    {
        return world.checkNoEntityCollision(getEntityBoundingBox(), this) && world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty();
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return (rand.nextInt(20) == 0 || !world.canBlockSeeSky(new BlockPos(this))) && super.getCanSpawnHere();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!isMoving() && !source.isMagicDamage() && source.getSourceOfDamage() instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)source.getSourceOfDamage();

            if (!source.isExplosion())
            {
                entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0F);
            }
        }

        if (wander != null)
        {
            wander.makeUpdate();
        }

        return super.attackEntityFrom(source, amount);
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return 180;
    }

    public void func_191986_a(float p_191986_1_, float p_191986_2_, float p_191986_3_)
    {
        if (isServerWorld() && isInWater())
        {
            moveFlying(p_191986_1_, p_191986_2_, p_191986_3_, 0.1F);
            moveEntity(MoverType.SELF, motionX, motionY, motionZ);
            motionX *= 0.8999999761581421D;
            motionY *= 0.8999999761581421D;
            motionZ *= 0.8999999761581421D;

            if (!isMoving() && getAttackTarget() == null)
            {
                motionY -= 0.005D;
            }
        }
        else
        {
            super.func_191986_a(p_191986_1_, p_191986_2_, p_191986_3_);
        }
    }

    static class AIGuardianAttack extends EntityAIBase
    {
        private final EntityGuardian theEntity;
        private int tickCounter;
        private final boolean field_190881_c;

        public AIGuardianAttack(EntityGuardian guardian)
        {
            theEntity = guardian;
            field_190881_c = guardian instanceof EntityElderGuardian;
            setMutexBits(3);
        }

        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = theEntity.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        public boolean continueExecuting()
        {
            return super.continueExecuting() && (field_190881_c || theEntity.getDistanceSqToEntity(theEntity.getAttackTarget()) > 9.0D);
        }

        public void startExecuting()
        {
            tickCounter = -10;
            theEntity.getNavigator().clearPathEntity();
            theEntity.getLookHelper().setLookPositionWithEntity(theEntity.getAttackTarget(), 90.0F, 90.0F);
            theEntity.isAirBorne = true;
        }

        public void resetTask()
        {
            theEntity.setTargetedEntity(0);
            theEntity.setAttackTarget(null);
            theEntity.wander.makeUpdate();
        }

        public void updateTask()
        {
            EntityLivingBase entitylivingbase = theEntity.getAttackTarget();
            theEntity.getNavigator().clearPathEntity();
            theEntity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);

            if (!theEntity.canEntityBeSeen(entitylivingbase))
            {
                theEntity.setAttackTarget(null);
            }
            else
            {
                ++tickCounter;

                if (tickCounter == 0)
                {
                    theEntity.setTargetedEntity(theEntity.getAttackTarget().getEntityId());
                    theEntity.world.setEntityState(theEntity, (byte)21);
                }
                else if (tickCounter >= theEntity.getAttackDuration())
                {
                    float f = 1.0F;

                    if (theEntity.world.getDifficulty() == EnumDifficulty.HARD)
                    {
                        f += 2.0F;
                    }

                    if (field_190881_c)
                    {
                        f += 2.0F;
                    }

                    entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(theEntity, theEntity), f);
                    entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(theEntity), (float) theEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                    theEntity.setAttackTarget(null);
                }

                super.updateTask();
            }
        }
    }

    static class GuardianMoveHelper extends EntityMoveHelper
    {
        private final EntityGuardian entityGuardian;

        public GuardianMoveHelper(EntityGuardian guardian)
        {
            super(guardian);
            entityGuardian = guardian;
        }

        public void onUpdateMoveHelper()
        {
            if (action == EntityMoveHelper.Action.MOVE_TO && !entityGuardian.getNavigator().noPath())
            {
                double d0 = posX - entityGuardian.posX;
                double d1 = posY - entityGuardian.posY;
                double d2 = posZ - entityGuardian.posZ;
                double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 = d1 / d3;
                float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                entityGuardian.rotationYaw = limitAngle(entityGuardian.rotationYaw, f, 90.0F);
                entityGuardian.renderYawOffset = entityGuardian.rotationYaw;
                float f1 = (float)(speed * entityGuardian.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                entityGuardian.setAIMoveSpeed(entityGuardian.getAIMoveSpeed() + (f1 - entityGuardian.getAIMoveSpeed()) * 0.125F);
                double d4 = Math.sin((double)(entityGuardian.ticksExisted + entityGuardian.getEntityId()) * 0.5D) * 0.05D;
                double d5 = Math.cos(entityGuardian.rotationYaw * 0.017453292F);
                double d6 = Math.sin(entityGuardian.rotationYaw * 0.017453292F);
                entityGuardian.motionX += d4 * d5;
                entityGuardian.motionZ += d4 * d6;
                d4 = Math.sin((double)(entityGuardian.ticksExisted + entityGuardian.getEntityId()) * 0.75D) * 0.05D;
                entityGuardian.motionY += d4 * (d6 + d5) * 0.25D;
                entityGuardian.motionY += (double) entityGuardian.getAIMoveSpeed() * d1 * 0.1D;
                EntityLookHelper entitylookhelper = entityGuardian.getLookHelper();
                double d7 = entityGuardian.posX + d0 / d3 * 2.0D;
                double d8 = (double) entityGuardian.getEyeHeight() + entityGuardian.posY + d1 / d3;
                double d9 = entityGuardian.posZ + d2 / d3 * 2.0D;
                double d10 = entitylookhelper.getLookPosX();
                double d11 = entitylookhelper.getLookPosY();
                double d12 = entitylookhelper.getLookPosZ();

                if (!entitylookhelper.getIsLooking())
                {
                    d10 = d7;
                    d11 = d8;
                    d12 = d9;
                }

                entityGuardian.getLookHelper().setLookPosition(d10 + (d7 - d10) * 0.125D, d11 + (d8 - d11) * 0.125D, d12 + (d9 - d12) * 0.125D, 10.0F, 40.0F);
                entityGuardian.setMoving(true);
            }
            else
            {
                entityGuardian.setAIMoveSpeed(0.0F);
                entityGuardian.setMoving(false);
            }
        }
    }

    static class GuardianTargetSelector implements Predicate<EntityLivingBase>
    {
        private final EntityGuardian parentEntity;

        public GuardianTargetSelector(EntityGuardian guardian)
        {
            parentEntity = guardian;
        }

        public boolean apply(@Nullable EntityLivingBase p_apply_1_)
        {
            return (p_apply_1_ instanceof EntityPlayer || p_apply_1_ instanceof EntitySquid) && p_apply_1_.getDistanceSqToEntity(parentEntity) > 9.0D;
        }
    }
}
