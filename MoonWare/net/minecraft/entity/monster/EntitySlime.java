package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySlime extends EntityLiving implements IMob
{
    private static final DataParameter<Integer> SLIME_SIZE = EntityDataManager.createKey(EntitySlime.class, DataSerializers.VARINT);
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean wasOnGround;

    public EntitySlime(World worldIn)
    {
        super(worldIn);
        moveHelper = new EntitySlime.SlimeMoveHelper(this);
    }

    protected void initEntityAI()
    {
        tasks.addTask(1, new EntitySlime.AISlimeFloat(this));
        tasks.addTask(2, new EntitySlime.AISlimeAttack(this));
        tasks.addTask(3, new EntitySlime.AISlimeFaceRandom(this));
        tasks.addTask(5, new EntitySlime.AISlimeHop(this));
        targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
        targetTasks.addTask(3, new EntityAIFindEntityNearest(this, EntityIronGolem.class));
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(SLIME_SIZE, Integer.valueOf(1));
    }

    protected void setSlimeSize(int size, boolean p_70799_2_)
    {
        dataManager.set(SLIME_SIZE, Integer.valueOf(size));
        setSize(0.51000005F * (float)size, 0.51000005F * (float)size);
        setPosition(posX, posY, posZ);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(size * size);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2F + 0.1F * (float)size);

        if (p_70799_2_)
        {
            setHealth(getMaxHealth());
        }

        experienceValue = size;
    }

    /**
     * Returns the size of the slime.
     */
    public int getSlimeSize()
    {
        return dataManager.get(SLIME_SIZE).intValue();
    }

    public static void registerFixesSlime(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntitySlime.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Size", getSlimeSize() - 1);
        compound.setBoolean("wasOnGround", wasOnGround);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        int i = compound.getInteger("Size");

        if (i < 0)
        {
            i = 0;
        }

        setSlimeSize(i + 1, false);
        wasOnGround = compound.getBoolean("wasOnGround");
    }

    public boolean isSmallSlime()
    {
        return getSlimeSize() <= 1;
    }

    protected EnumParticleTypes getParticleType()
    {
        return EnumParticleTypes.SLIME;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (!world.isRemote && world.getDifficulty() == EnumDifficulty.PEACEFUL && getSlimeSize() > 0)
        {
            isDead = true;
        }

        squishFactor += (squishAmount - squishFactor) * 0.5F;
        prevSquishFactor = squishFactor;
        super.onUpdate();

        if (onGround && !wasOnGround)
        {
            int i = getSlimeSize();

            for (int j = 0; j < i * 8; ++j)
            {
                float f = rand.nextFloat() * ((float)Math.PI * 2F);
                float f1 = rand.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float)i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float)i * 0.5F * f1;
                World world = this.world;
                EnumParticleTypes enumparticletypes = getParticleType();
                double d0 = posX + (double)f2;
                double d1 = posZ + (double)f3;
                world.spawnParticle(enumparticletypes, d0, getEntityBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D);
            }

            playSound(getSquishSound(), getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            squishAmount = -0.5F;
        }
        else if (!onGround && wasOnGround)
        {
            squishAmount = 1.0F;
        }

        wasOnGround = onGround;
        alterSquishAmount();
    }

    protected void alterSquishAmount()
    {
        squishAmount *= 0.6F;
    }

    /**
     * Gets the amount of time the slime needs to wait between jumps.
     */
    protected int getJumpDelay()
    {
        return rand.nextInt(20) + 10;
    }

    protected EntitySlime createInstance()
    {
        return new EntitySlime(world);
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (SLIME_SIZE.equals(key))
        {
            int i = getSlimeSize();
            setSize(0.51000005F * (float)i, 0.51000005F * (float)i);
            rotationYaw = rotationYawHead;
            renderYawOffset = rotationYawHead;

            if (isInWater() && rand.nextInt(20) == 0)
            {
                resetHeight();
            }
        }

        super.notifyDataManagerChange(key);
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        int i = getSlimeSize();

        if (!world.isRemote && i > 1 && getHealth() <= 0.0F)
        {
            int j = 2 + rand.nextInt(3);

            for (int k = 0; k < j; ++k)
            {
                float f = ((float)(k % 2) - 0.5F) * (float)i / 4.0F;
                float f1 = ((float)(k / 2) - 0.5F) * (float)i / 4.0F;
                EntitySlime entityslime = createInstance();

                if (hasCustomName())
                {
                    entityslime.setCustomNameTag(getCustomNameTag());
                }

                if (isNoDespawnRequired())
                {
                    entityslime.enablePersistence();
                }

                entityslime.setSlimeSize(i / 2, true);
                entityslime.setLocationAndAngles(posX + (double)f, posY + 0.5D, posZ + (double)f1, rand.nextFloat() * 360.0F, 0.0F);
                world.spawnEntityInWorld(entityslime);
            }
        }

        super.setDead();
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void applyEntityCollision(Entity entityIn)
    {
        super.applyEntityCollision(entityIn);

        if (entityIn instanceof EntityIronGolem && canDamagePlayer())
        {
            dealDamage((EntityLivingBase)entityIn);
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (canDamagePlayer())
        {
            dealDamage(entityIn);
        }
    }

    protected void dealDamage(EntityLivingBase entityIn)
    {
        int i = getSlimeSize();

        if (canEntityBeSeen(entityIn) && getDistanceSqToEntity(entityIn) < 0.6D * (double)i * 0.6D * (double)i && entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getAttackStrength()))
        {
            playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            applyEnchantments(this, entityIn);
        }
    }

    public float getEyeHeight()
    {
        return 0.625F * height;
    }

    /**
     * Indicates weather the slime is able to damage the player (based upon the slime's size)
     */
    protected boolean canDamagePlayer()
    {
        return !isSmallSlime();
    }

    /**
     * Gets the amount of damage dealt to the player when "attacked" by the slime.
     */
    protected int getAttackStrength()
    {
        return getSlimeSize();
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_HURT : SoundEvents.ENTITY_SLIME_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_DEATH : SoundEvents.ENTITY_SLIME_DEATH;
    }

    protected SoundEvent getSquishSound()
    {
        return isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH;
    }

    protected Item getDropItem()
    {
        return getSlimeSize() == 1 ? Items.SLIME_BALL : null;
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return getSlimeSize() == 1 ? LootTableList.ENTITIES_SLIME : LootTableList.EMPTY;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        BlockPos blockpos = new BlockPos(MathHelper.floor(posX), 0, MathHelper.floor(posZ));
        Chunk chunk = world.getChunkFromBlockCoords(blockpos);

        if (world.getWorldInfo().getTerrainType() == WorldType.FLAT && rand.nextInt(4) != 1)
        {
            return false;
        }
        else
        {
            if (world.getDifficulty() != EnumDifficulty.PEACEFUL)
            {
                Biome biome = world.getBiome(blockpos);

                if (biome == Biomes.SWAMPLAND && posY > 50.0D && posY < 70.0D && rand.nextFloat() < 0.5F && rand.nextFloat() < world.getCurrentMoonPhaseFactor() && world.getLightFromNeighbors(new BlockPos(this)) <= rand.nextInt(8))
                {
                    return super.getCanSpawnHere();
                }

                if (rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 && posY < 40.0D)
                {
                    return super.getCanSpawnHere();
                }
            }

            return false;
        }
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F * (float) getSlimeSize();
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return 0;
    }

    /**
     * Returns true if the slime makes a sound when it jumps (based upon the slime's size)
     */
    protected boolean makesSoundOnJump()
    {
        return getSlimeSize() > 0;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
        motionY = 0.41999998688697815D;
        isAirBorne = true;
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        int i = rand.nextInt(3);

        if (i < 2 && rand.nextFloat() < 0.5F * difficulty.getClampedAdditionalDifficulty())
        {
            ++i;
        }

        int j = 1 << i;
        setSlimeSize(j, true);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    protected SoundEvent getJumpSound()
    {
        return isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_JUMP : SoundEvents.ENTITY_SLIME_JUMP;
    }

    static class AISlimeAttack extends EntityAIBase
    {
        private final EntitySlime slime;
        private int growTieredTimer;

        public AISlimeAttack(EntitySlime slimeIn)
        {
            slime = slimeIn;
            setMutexBits(2);
        }

        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = slime.getAttackTarget();

            if (entitylivingbase == null)
            {
                return false;
            }
            else if (!entitylivingbase.isEntityAlive())
            {
                return false;
            }
            else
            {
                return !(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).capabilities.disableDamage;
            }
        }

        public void startExecuting()
        {
            growTieredTimer = 300;
            super.startExecuting();
        }

        public boolean continueExecuting()
        {
            EntityLivingBase entitylivingbase = slime.getAttackTarget();

            if (entitylivingbase == null)
            {
                return false;
            }
            else if (!entitylivingbase.isEntityAlive())
            {
                return false;
            }
            else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage)
            {
                return false;
            }
            else
            {
                return --growTieredTimer > 0;
            }
        }

        public void updateTask()
        {
            slime.faceEntity(slime.getAttackTarget(), 10.0F, 10.0F);
            ((EntitySlime.SlimeMoveHelper) slime.getMoveHelper()).setDirection(slime.rotationYaw, slime.canDamagePlayer());
        }
    }

    static class AISlimeFaceRandom extends EntityAIBase
    {
        private final EntitySlime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public AISlimeFaceRandom(EntitySlime slimeIn)
        {
            slime = slimeIn;
            setMutexBits(2);
        }

        public boolean shouldExecute()
        {
            return slime.getAttackTarget() == null && (slime.onGround || slime.isInWater() || slime.isInLava() || slime.isPotionActive(MobEffects.LEVITATION));
        }

        public void updateTask()
        {
            if (--nextRandomizeTime <= 0)
            {
                nextRandomizeTime = 40 + slime.getRNG().nextInt(60);
                chosenDegrees = (float) slime.getRNG().nextInt(360);
            }

            ((EntitySlime.SlimeMoveHelper) slime.getMoveHelper()).setDirection(chosenDegrees, false);
        }
    }

    static class AISlimeFloat extends EntityAIBase
    {
        private final EntitySlime slime;

        public AISlimeFloat(EntitySlime slimeIn)
        {
            slime = slimeIn;
            setMutexBits(5);
            ((PathNavigateGround)slimeIn.getNavigator()).setCanSwim(true);
        }

        public boolean shouldExecute()
        {
            return slime.isInWater() || slime.isInLava();
        }

        public void updateTask()
        {
            if (slime.getRNG().nextFloat() < 0.8F)
            {
                slime.getJumpHelper().setJumping();
            }

            ((EntitySlime.SlimeMoveHelper) slime.getMoveHelper()).setSpeed(1.2D);
        }
    }

    static class AISlimeHop extends EntityAIBase
    {
        private final EntitySlime slime;

        public AISlimeHop(EntitySlime slimeIn)
        {
            slime = slimeIn;
            setMutexBits(5);
        }

        public boolean shouldExecute()
        {
            return true;
        }

        public void updateTask()
        {
            ((EntitySlime.SlimeMoveHelper) slime.getMoveHelper()).setSpeed(1.0D);
        }
    }

    static class SlimeMoveHelper extends EntityMoveHelper
    {
        private float yRot;
        private int jumpDelay;
        private final EntitySlime slime;
        private boolean isAggressive;

        public SlimeMoveHelper(EntitySlime slimeIn)
        {
            super(slimeIn);
            slime = slimeIn;
            yRot = 180.0F * slimeIn.rotationYaw / (float)Math.PI;
        }

        public void setDirection(float p_179920_1_, boolean p_179920_2_)
        {
            yRot = p_179920_1_;
            isAggressive = p_179920_2_;
        }

        public void setSpeed(double speedIn)
        {
            speed = speedIn;
            action = EntityMoveHelper.Action.MOVE_TO;
        }

        public void onUpdateMoveHelper()
        {
            entity.rotationYaw = limitAngle(entity.rotationYaw, yRot, 90.0F);
            entity.rotationYawHead = entity.rotationYaw;
            entity.renderYawOffset = entity.rotationYaw;

            if (action != EntityMoveHelper.Action.MOVE_TO)
            {
                entity.func_191989_p(0.0F);
            }
            else
            {
                action = EntityMoveHelper.Action.WAIT;

                if (entity.onGround)
                {
                    entity.setAIMoveSpeed((float)(speed * entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));

                    if (jumpDelay-- <= 0)
                    {
                        jumpDelay = slime.getJumpDelay();

                        if (isAggressive)
                        {
                            jumpDelay /= 3;
                        }

                        slime.getJumpHelper().setJumping();

                        if (slime.makesSoundOnJump())
                        {
                            slime.playSound(slime.getJumpSound(), slime.getSoundVolume(), ((slime.getRNG().nextFloat() - slime.getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                        }
                    }
                    else
                    {
                        slime.moveStrafing = 0.0F;
                        slime.field_191988_bg = 0.0F;
                        entity.setAIMoveSpeed(0.0F);
                    }
                }
                else
                {
                    entity.setAIMoveSpeed((float)(speed * entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                }
            }
        }
    }
}
