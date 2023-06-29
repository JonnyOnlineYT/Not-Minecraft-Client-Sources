package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityRabbit extends EntityAnimal
{
    private static final DataParameter<Integer> RABBIT_TYPE = EntityDataManager.createKey(EntityRabbit.class, DataSerializers.VARINT);
    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int currentMoveTypeDuration;
    private int carrotTicks;

    public EntityRabbit(World worldIn)
    {
        super(worldIn);
        setSize(0.4F, 0.5F);
        jumpHelper = new EntityRabbit.RabbitJumpHelper(this);
        moveHelper = new EntityRabbit.RabbitMoveHelper(this);
        setMovementSpeed(0.0D);
    }

    protected void initEntityAI()
    {
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(1, new EntityRabbit.AIPanic(this, 2.2D));
        tasks.addTask(2, new EntityAIMate(this, 0.8D));
        tasks.addTask(3, new EntityAITempt(this, 1.0D, Items.CARROT, false));
        tasks.addTask(3, new EntityAITempt(this, 1.0D, Items.GOLDEN_CARROT, false));
        tasks.addTask(3, new EntityAITempt(this, 1.0D, Item.getItemFromBlock(Blocks.YELLOW_FLOWER), false));
        tasks.addTask(4, new EntityRabbit.AIAvoidEntity(this, EntityPlayer.class, 8.0F, 2.2D, 2.2D));
        tasks.addTask(4, new EntityRabbit.AIAvoidEntity(this, EntityWolf.class, 10.0F, 2.2D, 2.2D));
        tasks.addTask(4, new EntityRabbit.AIAvoidEntity(this, EntityMob.class, 4.0F, 2.2D, 2.2D));
        tasks.addTask(5, new EntityRabbit.AIRaidFarm(this));
        tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
        tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
    }

    protected float getJumpUpwardsMotion()
    {
        if (!isCollidedHorizontally && (!moveHelper.isUpdating() || moveHelper.getY() <= posY + 0.5D))
        {
            Path path = navigator.getPath();

            if (path != null && path.getCurrentPathIndex() < path.getCurrentPathLength())
            {
                Vec3d vec3d = path.getPosition(this);

                if (vec3d.yCoord > posY + 0.5D)
                {
                    return 0.5F;
                }
            }

            return moveHelper.getSpeed() <= 0.6D ? 0.2F : 0.3F;
        }
        else
        {
            return 0.5F;
        }
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
        super.jump();
        double d0 = moveHelper.getSpeed();

        if (d0 > 0.0D)
        {
            double d1 = motionX * motionX + motionZ * motionZ;

            if (d1 < 0.010000000000000002D)
            {
                moveFlying(0.0F, 0.0F, 1.0F, 0.1F);
            }
        }

        if (!world.isRemote)
        {
            world.setEntityState(this, (byte)1);
        }
    }

    public float setJumpCompletion(float p_175521_1_)
    {
        return jumpDuration == 0 ? 0.0F : ((float) jumpTicks + p_175521_1_) / (float) jumpDuration;
    }

    public void setMovementSpeed(double newSpeed)
    {
        getNavigator().setSpeed(newSpeed);
        moveHelper.setMoveTo(moveHelper.getX(), moveHelper.getY(), moveHelper.getZ(), newSpeed);
    }

    public void setJumping(boolean jumping)
    {
        super.setJumping(jumping);

        if (jumping)
        {
            playSound(getJumpSound(), getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }
    }

    public void startJumping()
    {
        setJumping(true);
        jumpDuration = 10;
        jumpTicks = 0;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(RABBIT_TYPE, Integer.valueOf(0));
    }

    public void updateAITasks()
    {
        if (currentMoveTypeDuration > 0)
        {
            --currentMoveTypeDuration;
        }

        if (carrotTicks > 0)
        {
            carrotTicks -= rand.nextInt(3);

            if (carrotTicks < 0)
            {
                carrotTicks = 0;
            }
        }

        if (onGround)
        {
            if (!wasOnGround)
            {
                setJumping(false);
                checkLandingDelay();
            }

            if (getRabbitType() == 99 && currentMoveTypeDuration == 0)
            {
                EntityLivingBase entitylivingbase = getAttackTarget();

                if (entitylivingbase != null && getDistanceSqToEntity(entitylivingbase) < 16.0D)
                {
                    calculateRotationYaw(entitylivingbase.posX, entitylivingbase.posZ);
                    moveHelper.setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, moveHelper.getSpeed());
                    startJumping();
                    wasOnGround = true;
                }
            }

            EntityRabbit.RabbitJumpHelper entityrabbit$rabbitjumphelper = (EntityRabbit.RabbitJumpHelper) jumpHelper;

            if (!entityrabbit$rabbitjumphelper.getIsJumping())
            {
                if (moveHelper.isUpdating() && currentMoveTypeDuration == 0)
                {
                    Path path = navigator.getPath();
                    Vec3d vec3d = new Vec3d(moveHelper.getX(), moveHelper.getY(), moveHelper.getZ());

                    if (path != null && path.getCurrentPathIndex() < path.getCurrentPathLength())
                    {
                        vec3d = path.getPosition(this);
                    }

                    calculateRotationYaw(vec3d.xCoord, vec3d.zCoord);
                    startJumping();
                }
            }
            else if (!entityrabbit$rabbitjumphelper.canJump())
            {
                enableJumpControl();
            }
        }

        wasOnGround = onGround;
    }

    /**
     * Attempts to create sprinting particles if the entity is sprinting and not in water.
     */
    public void spawnRunningParticles()
    {
    }

    private void calculateRotationYaw(double x, double z)
    {
        rotationYaw = (float)(MathHelper.atan2(z - posZ, x - posX) * (180D / Math.PI)) - 90.0F;
    }

    private void enableJumpControl()
    {
        ((EntityRabbit.RabbitJumpHelper) jumpHelper).setCanJump(true);
    }

    private void disableJumpControl()
    {
        ((EntityRabbit.RabbitJumpHelper) jumpHelper).setCanJump(false);
    }

    private void updateMoveTypeDuration()
    {
        if (moveHelper.getSpeed() < 2.2D)
        {
            currentMoveTypeDuration = 10;
        }
        else
        {
            currentMoveTypeDuration = 1;
        }
    }

    private void checkLandingDelay()
    {
        updateMoveTypeDuration();
        disableJumpControl();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (jumpTicks != jumpDuration)
        {
            ++jumpTicks;
        }
        else if (jumpDuration != 0)
        {
            jumpTicks = 0;
            jumpDuration = 0;
            setJumping(false);
        }
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
    }

    public static void registerFixesRabbit(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityRabbit.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("RabbitType", getRabbitType());
        compound.setInteger("MoreCarrotTicks", carrotTicks);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setRabbitType(compound.getInteger("RabbitType"));
        carrotTicks = compound.getInteger("MoreCarrotTicks");
    }

    protected SoundEvent getJumpSound()
    {
        return SoundEvents.ENTITY_RABBIT_JUMP;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_RABBIT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_RABBIT_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_RABBIT_DEATH;
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        if (getRabbitType() == 99)
        {
            playSound(SoundEvents.ENTITY_RABBIT_ATTACK, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0F);
        }
        else
        {
            return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
        }
    }

    public SoundCategory getSoundCategory()
    {
        return getRabbitType() == 99 ? SoundCategory.HOSTILE : SoundCategory.NEUTRAL;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return !isEntityInvulnerable(source) && super.attackEntityFrom(source, amount);
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_RABBIT;
    }

    private boolean isRabbitBreedingItem(Item itemIn)
    {
        return itemIn == Items.CARROT || itemIn == Items.GOLDEN_CARROT || itemIn == Item.getItemFromBlock(Blocks.YELLOW_FLOWER);
    }

    public EntityRabbit createChild(EntityAgeable ageable)
    {
        EntityRabbit entityrabbit = new EntityRabbit(world);
        int i = getRandomRabbitType();

        if (rand.nextInt(20) != 0)
        {
            if (ageable instanceof EntityRabbit && rand.nextBoolean())
            {
                i = ((EntityRabbit)ageable).getRabbitType();
            }
            else
            {
                i = getRabbitType();
            }
        }

        entityrabbit.setRabbitType(i);
        return entityrabbit;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack stack)
    {
        return isRabbitBreedingItem(stack.getItem());
    }

    public int getRabbitType()
    {
        return dataManager.get(RABBIT_TYPE).intValue();
    }

    public void setRabbitType(int rabbitTypeId)
    {
        if (rabbitTypeId == 99)
        {
            getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
            tasks.addTask(4, new EntityRabbit.AIEvilAttack(this));
            targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
            targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
            targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityWolf.class, true));

            if (!hasCustomName())
            {
                setCustomNameTag(I18n.translateToLocal("entity.KillerBunny.name"));
            }
        }

        dataManager.set(RABBIT_TYPE, Integer.valueOf(rabbitTypeId));
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        int i = getRandomRabbitType();
        boolean flag = false;

        if (livingdata instanceof EntityRabbit.RabbitTypeData)
        {
            i = ((EntityRabbit.RabbitTypeData)livingdata).typeData;
            flag = true;
        }
        else
        {
            livingdata = new EntityRabbit.RabbitTypeData(i);
        }

        setRabbitType(i);

        if (flag)
        {
            setGrowingAge(-24000);
        }

        return livingdata;
    }

    private int getRandomRabbitType()
    {
        Biome biome = world.getBiome(new BlockPos(this));
        int i = rand.nextInt(100);

        if (biome.isSnowyBiome())
        {
            return i < 80 ? 1 : 3;
        }
        else if (biome instanceof BiomeDesert)
        {
            return 4;
        }
        else
        {
            return i < 50 ? 0 : (i < 90 ? 5 : 2);
        }
    }

    /**
     * Returns true if {@link net.minecraft.entity.passive.EntityRabbit#carrotTicks carrotTicks} has reached zero
     */
    private boolean isCarrotEaten()
    {
        return carrotTicks == 0;
    }

    protected void createEatingParticles()
    {
        BlockCarrot blockcarrot = (BlockCarrot)Blocks.CARROTS;
        IBlockState iblockstate = blockcarrot.withAge(blockcarrot.getMaxAge());
        world.spawnParticle(EnumParticleTypes.BLOCK_DUST, posX + (double)(rand.nextFloat() * width * 2.0F) - (double) width, posY + 0.5D + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double) width, 0.0D, 0.0D, 0.0D, Block.getStateId(iblockstate));
        carrotTicks = 40;
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 1)
        {
            createRunningParticles();
            jumpDuration = 10;
            jumpTicks = 0;
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    static class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T>
    {
        private final EntityRabbit entityInstance;

        public AIAvoidEntity(EntityRabbit rabbit, Class<T> p_i46403_2_, float p_i46403_3_, double p_i46403_4_, double p_i46403_6_)
        {
            super(rabbit, p_i46403_2_, p_i46403_3_, p_i46403_4_, p_i46403_6_);
            entityInstance = rabbit;
        }

        public boolean shouldExecute()
        {
            return entityInstance.getRabbitType() != 99 && super.shouldExecute();
        }
    }

    static class AIEvilAttack extends EntityAIAttackMelee
    {
        public AIEvilAttack(EntityRabbit rabbit)
        {
            super(rabbit, 1.4D, true);
        }

        protected double getAttackReachSqr(EntityLivingBase attackTarget)
        {
            return 4.0F + attackTarget.width;
        }
    }

    static class AIPanic extends EntityAIPanic
    {
        private final EntityRabbit theEntity;

        public AIPanic(EntityRabbit rabbit, double speedIn)
        {
            super(rabbit, speedIn);
            theEntity = rabbit;
        }

        public void updateTask()
        {
            super.updateTask();
            theEntity.setMovementSpeed(speed);
        }
    }

    static class AIRaidFarm extends EntityAIMoveToBlock
    {
        private final EntityRabbit rabbit;
        private boolean wantsToRaid;
        private boolean canRaid;

        public AIRaidFarm(EntityRabbit rabbitIn)
        {
            super(rabbitIn, 0.699999988079071D, 16);
            rabbit = rabbitIn;
        }

        public boolean shouldExecute()
        {
            if (runDelay <= 0)
            {
                if (!rabbit.world.getGameRules().getBoolean("mobGriefing"))
                {
                    return false;
                }

                canRaid = false;
                wantsToRaid = rabbit.isCarrotEaten();
                wantsToRaid = true;
            }

            return super.shouldExecute();
        }

        public boolean continueExecuting()
        {
            return canRaid && super.continueExecuting();
        }

        public void updateTask()
        {
            super.updateTask();
            rabbit.getLookHelper().setLookPosition((double) destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1, (double) destinationBlock.getZ() + 0.5D, 10.0F, (float) rabbit.getVerticalFaceSpeed());

            if (getIsAboveDestination())
            {
                World world = rabbit.world;
                BlockPos blockpos = destinationBlock.up();
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (canRaid && block instanceof BlockCarrot)
                {
                    Integer integer = iblockstate.getValue(BlockCrops.AGE);

                    if (integer.intValue() == 0)
                    {
                        world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
                        world.destroyBlock(blockpos, true);
                    }
                    else
                    {
                        world.setBlockState(blockpos, iblockstate.withProperty(BlockCrops.AGE, Integer.valueOf(integer.intValue() - 1)), 2);
                        world.playEvent(2001, blockpos, Block.getStateId(iblockstate));
                    }

                    rabbit.createEatingParticles();
                }

                canRaid = false;
                runDelay = 10;
            }
        }

        protected boolean shouldMoveTo(World worldIn, BlockPos pos)
        {
            Block block = worldIn.getBlockState(pos).getBlock();

            if (block == Blocks.FARMLAND && wantsToRaid && !canRaid)
            {
                pos = pos.up();
                IBlockState iblockstate = worldIn.getBlockState(pos);
                block = iblockstate.getBlock();

                if (block instanceof BlockCarrot && ((BlockCarrot)block).isMaxAge(iblockstate))
                {
                    canRaid = true;
                    return true;
                }
            }

            return false;
        }
    }

    public class RabbitJumpHelper extends EntityJumpHelper
    {
        private final EntityRabbit theEntity;
        private boolean canJump;

        public RabbitJumpHelper(EntityRabbit rabbit)
        {
            super(rabbit);
            theEntity = rabbit;
        }

        public boolean getIsJumping()
        {
            return isJumping;
        }

        public boolean canJump()
        {
            return canJump;
        }

        public void setCanJump(boolean canJumpIn)
        {
            canJump = canJumpIn;
        }

        public void doJump()
        {
            if (isJumping)
            {
                theEntity.startJumping();
                isJumping = false;
            }
        }
    }

    static class RabbitMoveHelper extends EntityMoveHelper
    {
        private final EntityRabbit theEntity;
        private double nextJumpSpeed;

        public RabbitMoveHelper(EntityRabbit rabbit)
        {
            super(rabbit);
            theEntity = rabbit;
        }

        public void onUpdateMoveHelper()
        {
            if (theEntity.onGround && !theEntity.isJumping && !((EntityRabbit.RabbitJumpHelper) theEntity.jumpHelper).getIsJumping())
            {
                theEntity.setMovementSpeed(0.0D);
            }
            else if (isUpdating())
            {
                theEntity.setMovementSpeed(nextJumpSpeed);
            }

            super.onUpdateMoveHelper();
        }

        public void setMoveTo(double x, double y, double z, double speedIn)
        {
            if (theEntity.isInWater())
            {
                speedIn = 1.5D;
            }

            super.setMoveTo(x, y, z, speedIn);

            if (speedIn > 0.0D)
            {
                nextJumpSpeed = speedIn;
            }
        }
    }

    public static class RabbitTypeData implements IEntityLivingData
    {
        public int typeData;

        public RabbitTypeData(int type)
        {
            typeData = type;
        }
    }
}
