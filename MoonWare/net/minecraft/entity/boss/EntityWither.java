package net.minecraft.entity.boss;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWither extends EntityMob implements IRangedAttackMob
{
    private static final DataParameter<Integer> FIRST_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SECOND_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> THIRD_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer>[] HEAD_TARGETS = new DataParameter[] {FIRST_HEAD_TARGET, SECOND_HEAD_TARGET, THIRD_HEAD_TARGET};
    private static final DataParameter<Integer> INVULNERABILITY_TIME = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
    private final float[] xRotationHeads = new float[2];
    private final float[] yRotationHeads = new float[2];
    private final float[] xRotOHeads = new float[2];
    private final float[] yRotOHeads = new float[2];
    private final int[] nextHeadUpdate = new int[2];
    private final int[] idleHeadUpdates = new int[2];

    /** Time before the Wither tries to break blocks */
    private int blockBreakCounter;
    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
    private static final Predicate<Entity> NOT_UNDEAD = new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_apply_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD && ((EntityLivingBase)p_apply_1_).func_190631_cK();
        }
    };

    public EntityWither(World worldIn)
    {
        super(worldIn);
        setHealth(getMaxHealth());
        setSize(0.9F, 3.5F);
        isImmuneToFire = true;
        ((PathNavigateGround) getNavigator()).setCanSwim(true);
        experienceValue = 50;
    }

    protected void initEntityAI()
    {
        tasks.addTask(0, new EntityWither.AIDoNothing());
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 40, 20.0F));
        tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(7, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, NOT_UNDEAD));
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(FIRST_HEAD_TARGET, Integer.valueOf(0));
        dataManager.register(SECOND_HEAD_TARGET, Integer.valueOf(0));
        dataManager.register(THIRD_HEAD_TARGET, Integer.valueOf(0));
        dataManager.register(INVULNERABILITY_TIME, Integer.valueOf(0));
    }

    public static void registerFixesWither(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityWither.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Invul", getInvulTime());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setInvulTime(compound.getInteger("Invul"));

        if (hasCustomName())
        {
            bossInfo.setName(getDisplayName());
        }
    }

    /**
     * Sets the custom name tag for this entity
     */
    public void setCustomNameTag(String name)
    {
        super.setCustomNameTag(name);
        bossInfo.setName(getDisplayName());
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_WITHER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_WITHER_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_WITHER_DEATH;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        motionY *= 0.6000000238418579D;

        if (!world.isRemote && getWatchedTargetId(0) > 0)
        {
            Entity entity = world.getEntityByID(getWatchedTargetId(0));

            if (entity != null)
            {
                if (posY < entity.posY || !isArmored() && posY < entity.posY + 5.0D)
                {
                    if (motionY < 0.0D)
                    {
                        motionY = 0.0D;
                    }

                    motionY += (0.5D - motionY) * 0.6000000238418579D;
                }

                double d0 = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                double d3 = d0 * d0 + d1 * d1;

                if (d3 > 9.0D)
                {
                    double d5 = MathHelper.sqrt(d3);
                    motionX += (d0 / d5 * 0.5D - motionX) * 0.6000000238418579D;
                    motionZ += (d1 / d5 * 0.5D - motionZ) * 0.6000000238418579D;
                }
            }
        }

        if (motionX * motionX + motionZ * motionZ > 0.05000000074505806D)
        {
            rotationYaw = (float)MathHelper.atan2(motionZ, motionX) * (180F / (float)Math.PI) - 90.0F;
        }

        super.onLivingUpdate();

        for (int i = 0; i < 2; ++i)
        {
            yRotOHeads[i] = yRotationHeads[i];
            xRotOHeads[i] = xRotationHeads[i];
        }

        for (int j = 0; j < 2; ++j)
        {
            int k = getWatchedTargetId(j + 1);
            Entity entity1 = null;

            if (k > 0)
            {
                entity1 = world.getEntityByID(k);
            }

            if (entity1 != null)
            {
                double d11 = getHeadX(j + 1);
                double d12 = getHeadY(j + 1);
                double d13 = getHeadZ(j + 1);
                double d6 = entity1.posX - d11;
                double d7 = entity1.posY + (double)entity1.getEyeHeight() - d12;
                double d8 = entity1.posZ - d13;
                double d9 = MathHelper.sqrt(d6 * d6 + d8 * d8);
                float f = (float)(MathHelper.atan2(d8, d6) * (180D / Math.PI)) - 90.0F;
                float f1 = (float)(-(MathHelper.atan2(d7, d9) * (180D / Math.PI)));
                xRotationHeads[j] = rotlerp(xRotationHeads[j], f1, 40.0F);
                yRotationHeads[j] = rotlerp(yRotationHeads[j], f, 10.0F);
            }
            else
            {
                yRotationHeads[j] = rotlerp(yRotationHeads[j], renderYawOffset, 10.0F);
            }
        }

        boolean flag = isArmored();

        for (int l = 0; l < 3; ++l)
        {
            double d10 = getHeadX(l);
            double d2 = getHeadY(l);
            double d4 = getHeadZ(l);
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d10 + rand.nextGaussian() * 0.30000001192092896D, d2 + rand.nextGaussian() * 0.30000001192092896D, d4 + rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);

            if (flag && world.rand.nextInt(4) == 0)
            {
                world.spawnParticle(EnumParticleTypes.SPELL_MOB, d10 + rand.nextGaussian() * 0.30000001192092896D, d2 + rand.nextGaussian() * 0.30000001192092896D, d4 + rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D);
            }
        }

        if (getInvulTime() > 0)
        {
            for (int i1 = 0; i1 < 3; ++i1)
            {
                world.spawnParticle(EnumParticleTypes.SPELL_MOB, posX + rand.nextGaussian(), posY + (double)(rand.nextFloat() * 3.3F), posZ + rand.nextGaussian(), 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
            }
        }
    }

    protected void updateAITasks()
    {
        if (getInvulTime() > 0)
        {
            int j1 = getInvulTime() - 1;

            if (j1 <= 0)
            {
                world.newExplosion(this, posX, posY + (double) getEyeHeight(), posZ, 7.0F, false, world.getGameRules().getBoolean("mobGriefing"));
                world.playBroadcastSound(1023, new BlockPos(this), 0);
            }

            setInvulTime(j1);

            if (ticksExisted % 10 == 0)
            {
                heal(10.0F);
            }
        }
        else
        {
            super.updateAITasks();

            for (int i = 1; i < 3; ++i)
            {
                if (ticksExisted >= nextHeadUpdate[i - 1])
                {
                    nextHeadUpdate[i - 1] = ticksExisted + 10 + rand.nextInt(10);

                    if (world.getDifficulty() == EnumDifficulty.NORMAL || world.getDifficulty() == EnumDifficulty.HARD)
                    {
                        int j3 = i - 1;
                        int k3 = idleHeadUpdates[i - 1];
                        idleHeadUpdates[j3] = idleHeadUpdates[i - 1] + 1;

                        if (k3 > 15)
                        {
                            float f = 10.0F;
                            float f1 = 5.0F;
                            double d0 = MathHelper.nextDouble(rand, posX - 10.0D, posX + 10.0D);
                            double d1 = MathHelper.nextDouble(rand, posY - 5.0D, posY + 5.0D);
                            double d2 = MathHelper.nextDouble(rand, posZ - 10.0D, posZ + 10.0D);
                            launchWitherSkullToCoords(i + 1, d0, d1, d2, true);
                            idleHeadUpdates[i - 1] = 0;
                        }
                    }

                    int k1 = getWatchedTargetId(i);

                    if (k1 > 0)
                    {
                        Entity entity = world.getEntityByID(k1);

                        if (entity != null && entity.isEntityAlive() && getDistanceSqToEntity(entity) <= 900.0D && canEntityBeSeen(entity))
                        {
                            if (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.disableDamage)
                            {
                                updateWatchedTargetId(i, 0);
                            }
                            else
                            {
                                launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
                                nextHeadUpdate[i - 1] = ticksExisted + 40 + rand.nextInt(20);
                                idleHeadUpdates[i - 1] = 0;
                            }
                        }
                        else
                        {
                            updateWatchedTargetId(i, 0);
                        }
                    }
                    else
                    {
                        List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expand(20.0D, 8.0D, 20.0D), Predicates.and(NOT_UNDEAD, EntitySelectors.NOT_SPECTATING));

                        for (int j2 = 0; j2 < 10 && !list.isEmpty(); ++j2)
                        {
                            EntityLivingBase entitylivingbase = list.get(rand.nextInt(list.size()));

                            if (entitylivingbase != this && entitylivingbase.isEntityAlive() && canEntityBeSeen(entitylivingbase))
                            {
                                if (entitylivingbase instanceof EntityPlayer)
                                {
                                    if (!((EntityPlayer)entitylivingbase).capabilities.disableDamage)
                                    {
                                        updateWatchedTargetId(i, entitylivingbase.getEntityId());
                                    }
                                }
                                else
                                {
                                    updateWatchedTargetId(i, entitylivingbase.getEntityId());
                                }

                                break;
                            }

                            list.remove(entitylivingbase);
                        }
                    }
                }
            }

            if (getAttackTarget() != null)
            {
                updateWatchedTargetId(0, getAttackTarget().getEntityId());
            }
            else
            {
                updateWatchedTargetId(0, 0);
            }

            if (blockBreakCounter > 0)
            {
                --blockBreakCounter;

                if (blockBreakCounter == 0 && world.getGameRules().getBoolean("mobGriefing"))
                {
                    int i1 = MathHelper.floor(posY);
                    int l1 = MathHelper.floor(posX);
                    int i2 = MathHelper.floor(posZ);
                    boolean flag = false;

                    for (int k2 = -1; k2 <= 1; ++k2)
                    {
                        for (int l2 = -1; l2 <= 1; ++l2)
                        {
                            for (int j = 0; j <= 3; ++j)
                            {
                                int i3 = l1 + k2;
                                int k = i1 + j;
                                int l = i2 + l2;
                                BlockPos blockpos = new BlockPos(i3, k, l);
                                IBlockState iblockstate = world.getBlockState(blockpos);
                                Block block = iblockstate.getBlock();

                                if (iblockstate.getMaterial() != Material.AIR && canDestroyBlock(block))
                                {
                                    flag = world.destroyBlock(blockpos, true) || flag;
                                }
                            }
                        }
                    }

                    if (flag)
                    {
                        world.playEvent(null, 1022, new BlockPos(this), 0);
                    }
                }
            }

            if (ticksExisted % 20 == 0)
            {
                heal(1.0F);
            }

            bossInfo.setPercent(getHealth() / getMaxHealth());
        }
    }

    public static boolean canDestroyBlock(Block blockIn)
    {
        return blockIn != Blocks.BEDROCK && blockIn != Blocks.END_PORTAL && blockIn != Blocks.END_PORTAL_FRAME && blockIn != Blocks.COMMAND_BLOCK && blockIn != Blocks.REPEATING_COMMAND_BLOCK && blockIn != Blocks.CHAIN_COMMAND_BLOCK && blockIn != Blocks.BARRIER && blockIn != Blocks.STRUCTURE_BLOCK && blockIn != Blocks.STRUCTURE_VOID && blockIn != Blocks.PISTON_EXTENSION && blockIn != Blocks.END_GATEWAY;
    }

    /**
     * Initializes this Wither's explosion sequence and makes it invulnerable. Called immediately after spawning.
     */
    public void ignite()
    {
        setInvulTime(220);
        setHealth(getMaxHealth() / 3.0F);
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb()
    {
    }

    /**
     * Add the given player to the list of players tracking this entity. For instance, a player may track a boss in
     * order to view its associated boss bar.
     */
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
        bossInfo.addPlayer(player);
    }

    /**
     * Removes the given player from the list of players tracking this entity. See {@link Entity#addTrackingPlayer} for
     * more information on tracking.
     */
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        bossInfo.removePlayer(player);
    }

    private double getHeadX(int p_82214_1_)
    {
        if (p_82214_1_ <= 0)
        {
            return posX;
        }
        else
        {
            float f = (renderYawOffset + (float)(180 * (p_82214_1_ - 1))) * 0.017453292F;
            float f1 = MathHelper.cos(f);
            return posX + (double)f1 * 1.3D;
        }
    }

    private double getHeadY(int p_82208_1_)
    {
        return p_82208_1_ <= 0 ? posY + 3.0D : posY + 2.2D;
    }

    private double getHeadZ(int p_82213_1_)
    {
        if (p_82213_1_ <= 0)
        {
            return posZ;
        }
        else
        {
            float f = (renderYawOffset + (float)(180 * (p_82213_1_ - 1))) * 0.017453292F;
            float f1 = MathHelper.sin(f);
            return posZ + (double)f1 * 1.3D;
        }
    }

    private float rotlerp(float p_82204_1_, float p_82204_2_, float p_82204_3_)
    {
        float f = MathHelper.wrapDegrees(p_82204_2_ - p_82204_1_);

        if (f > p_82204_3_)
        {
            f = p_82204_3_;
        }

        if (f < -p_82204_3_)
        {
            f = -p_82204_3_;
        }

        return p_82204_1_ + f;
    }

    private void launchWitherSkullToEntity(int p_82216_1_, EntityLivingBase p_82216_2_)
    {
        launchWitherSkullToCoords(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + (double)p_82216_2_.getEyeHeight() * 0.5D, p_82216_2_.posZ, p_82216_1_ == 0 && rand.nextFloat() < 0.001F);
    }

    /**
     * Launches a Wither skull toward (par2, par4, par6)
     */
    private void launchWitherSkullToCoords(int p_82209_1_, double x, double y, double z, boolean invulnerable)
    {
        world.playEvent(null, 1024, new BlockPos(this), 0);
        double d0 = getHeadX(p_82209_1_);
        double d1 = getHeadY(p_82209_1_);
        double d2 = getHeadZ(p_82209_1_);
        double d3 = x - d0;
        double d4 = y - d1;
        double d5 = z - d2;
        EntityWitherSkull entitywitherskull = new EntityWitherSkull(world, this, d3, d4, d5);

        if (invulnerable)
        {
            entitywitherskull.setInvulnerable(true);
        }

        entitywitherskull.posY = d1;
        entitywitherskull.posX = d0;
        entitywitherskull.posZ = d2;
        world.spawnEntityInWorld(entitywitherskull);
    }

    /**
     * Attack the specified entity using a ranged attack.
     *  
     * @param distanceFactor How far the target is, normalized and clamped between 0.1 and 1.0
     */
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
        launchWitherSkullToEntity(0, target);
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
        else if (source != DamageSource.drown && !(source.getEntity() instanceof EntityWither))
        {
            if (getInvulTime() > 0 && source != DamageSource.outOfWorld)
            {
                return false;
            }
            else
            {
                if (isArmored())
                {
                    Entity entity = source.getSourceOfDamage();

                    if (entity instanceof EntityArrow)
                    {
                        return false;
                    }
                }

                Entity entity1 = source.getEntity();

                if (entity1 != null && !(entity1 instanceof EntityPlayer) && entity1 instanceof EntityLivingBase && ((EntityLivingBase)entity1).getCreatureAttribute() == getCreatureAttribute())
                {
                    return false;
                }
                else
                {
                    if (blockBreakCounter <= 0)
                    {
                        blockBreakCounter = 20;
                    }

                    for (int i = 0; i < idleHeadUpdates.length; ++i)
                    {
                        idleHeadUpdates[i] += 3;
                    }

                    return super.attackEntityFrom(source, amount);
                }
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        EntityItem entityitem = dropItem(Items.NETHER_STAR, 1);

        if (entityitem != null)
        {
            entityitem.setNoDespawn();
        }
    }

    /**
     * Makes the entity despawn if requirements are reached
     */
    protected void despawnEntity()
    {
        entityAge = 0;
    }

    public int getBrightnessForRender()
    {
        return 15728880;
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    /**
     * adds a PotionEffect to the entity
     */
    public void addPotionEffect(PotionEffect potioneffectIn)
    {
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6000000238418579D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
        getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }

    public float getHeadYRotation(int p_82207_1_)
    {
        return yRotationHeads[p_82207_1_];
    }

    public float getHeadXRotation(int p_82210_1_)
    {
        return xRotationHeads[p_82210_1_];
    }

    public int getInvulTime()
    {
        return dataManager.get(INVULNERABILITY_TIME).intValue();
    }

    public void setInvulTime(int time)
    {
        dataManager.set(INVULNERABILITY_TIME, Integer.valueOf(time));
    }

    /**
     * Returns the target entity ID if present, or -1 if not @param par1 The target offset, should be from 0-2
     */
    public int getWatchedTargetId(int head)
    {
        return dataManager.get(HEAD_TARGETS[head]).intValue();
    }

    /**
     * Updates the target entity ID
     */
    public void updateWatchedTargetId(int targetOffset, int newId)
    {
        dataManager.set(HEAD_TARGETS[targetOffset], Integer.valueOf(newId));
    }

    /**
     * Returns whether the wither is armored with its boss armor or not by checking whether its health is below half of
     * its maximum.
     */
    public boolean isArmored()
    {
        return getHealth() <= getMaxHealth() / 2.0F;
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected boolean canBeRidden(Entity entityIn)
    {
        return false;
    }

    /**
     * Returns false if this Entity is a boss, true otherwise.
     */
    public boolean isNonBoss()
    {
        return false;
    }

    public void setSwingingArms(boolean swingingArms)
    {
    }

    class AIDoNothing extends EntityAIBase
    {
        public AIDoNothing()
        {
            setMutexBits(7);
        }

        public boolean shouldExecute()
        {
            return getInvulTime() > 0;
        }
    }
}
