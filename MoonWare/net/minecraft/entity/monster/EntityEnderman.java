package net.minecraft.entity.monster;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityEnderman extends EntityMob
{
    private static final UUID ATTACKING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier ATTACKING_SPEED_BOOST = (new AttributeModifier(ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.15000000596046448D, 0)).setSaved(false);
    private static final Set<Block> CARRIABLE_BLOCKS = Sets.newIdentityHashSet();
    private static final DataParameter<Optional<IBlockState>> CARRIED_BLOCK = EntityDataManager.createKey(EntityEnderman.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    private static final DataParameter<Boolean> SCREAMING = EntityDataManager.createKey(EntityEnderman.class, DataSerializers.BOOLEAN);
    private int lastCreepySound;
    private int targetChangeTime;

    public EntityEnderman(World worldIn)
    {
        super(worldIn);
        setSize(0.6F, 2.9F);
        stepHeight = 1.0F;
        setPathPriority(PathNodeType.WATER, -1.0F);
    }

    protected void initEntityAI()
    {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
        tasks.addTask(10, new EntityEnderman.AIPlaceBlock(this));
        tasks.addTask(11, new EntityEnderman.AITakeBlock(this));
        targetTasks.addTask(1, new EntityEnderman.AIFindPlayer(this));
        targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new Predicate<EntityEndermite>()
        {
            public boolean apply(@Nullable EntityEndermite p_apply_1_)
            {
                return p_apply_1_.isSpawnedByPlayer();
            }
        }));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
        super.setAttackTarget(entitylivingbaseIn);
        IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (entitylivingbaseIn == null)
        {
            targetChangeTime = 0;
            dataManager.set(SCREAMING, Boolean.valueOf(false));
            iattributeinstance.removeModifier(ATTACKING_SPEED_BOOST);
        }
        else
        {
            targetChangeTime = ticksExisted;
            dataManager.set(SCREAMING, Boolean.valueOf(true));

            if (!iattributeinstance.hasModifier(ATTACKING_SPEED_BOOST))
            {
                iattributeinstance.applyModifier(ATTACKING_SPEED_BOOST);
            }
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(CARRIED_BLOCK, Optional.absent());
        dataManager.register(SCREAMING, Boolean.valueOf(false));
    }

    public void playEndermanSound()
    {
        if (ticksExisted >= lastCreepySound + 400)
        {
            lastCreepySound = ticksExisted;

            if (!isSilent())
            {
                world.playSound(posX, posY + (double) getEyeHeight(), posZ, SoundEvents.ENTITY_ENDERMEN_STARE, getSoundCategory(), 2.5F, 1.0F, false);
            }
        }
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (SCREAMING.equals(key) && isScreaming() && world.isRemote)
        {
            playEndermanSound();
        }

        super.notifyDataManagerChange(key);
    }

    public static void registerFixesEnderman(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityEnderman.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        IBlockState iblockstate = getHeldBlockState();

        if (iblockstate != null)
        {
            compound.setShort("carried", (short)Block.getIdFromBlock(iblockstate.getBlock()));
            compound.setShort("carriedData", (short)iblockstate.getBlock().getMetaFromState(iblockstate));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        IBlockState iblockstate;

        if (compound.hasKey("carried", 8))
        {
            iblockstate = Block.getBlockFromName(compound.getString("carried")).getStateFromMeta(compound.getShort("carriedData") & 65535);
        }
        else
        {
            iblockstate = Block.getBlockById(compound.getShort("carried")).getStateFromMeta(compound.getShort("carriedData") & 65535);
        }

        if (iblockstate == null || iblockstate.getBlock() == null || iblockstate.getMaterial() == Material.AIR)
        {
            iblockstate = null;
        }

        setHeldBlockState(iblockstate);
    }

    /**
     * Checks to see if this enderman should be attacking this player
     */
    private boolean shouldAttackPlayer(EntityPlayer player)
    {
        ItemStack itemstack = player.inventory.armorInventory.get(3);

        if (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN))
        {
            return false;
        }
        else
        {
            Vec3d vec3d = player.getLook(1.0F).normalize();
            Vec3d vec3d1 = new Vec3d(posX - player.posX, getEntityBoundingBox().minY + (double) getEyeHeight() - (player.posY + (double)player.getEyeHeight()), posZ - player.posZ);
            double d0 = vec3d1.lengthVector();
            vec3d1 = vec3d1.normalize();
            double d1 = vec3d.dotProduct(vec3d1);
            return d1 > 1.0D - 0.025D / d0 && player.canEntityBeSeen(this);
        }
    }

    public float getEyeHeight()
    {
        return 2.55F;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (world.isRemote)
        {
            for (int i = 0; i < 2; ++i)
            {
                world.spawnParticle(EnumParticleTypes.PORTAL, posX + (rand.nextDouble() - 0.5D) * (double) width, posY + rand.nextDouble() * (double) height - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double) width, (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);
            }
        }

        isJumping = false;
        super.onLivingUpdate();
    }

    protected void updateAITasks()
    {
        if (isWet())
        {
            attackEntityFrom(DamageSource.drown, 1.0F);
        }

        if (world.isDaytime() && ticksExisted >= targetChangeTime + 600)
        {
            float f = getBrightness();

            if (f > 0.5F && world.canSeeSky(new BlockPos(this)) && rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F)
            {
                setAttackTarget(null);
                teleportRandomly();
            }
        }

        super.updateAITasks();
    }

    /**
     * Teleport the enderman to a random nearby position
     */
    protected boolean teleportRandomly()
    {
        double d0 = posX + (rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = posY + (double)(rand.nextInt(64) - 32);
        double d2 = posZ + (rand.nextDouble() - 0.5D) * 64.0D;
        return teleportTo(d0, d1, d2);
    }

    /**
     * Teleport the enderman to another entity
     */
    protected boolean teleportToEntity(Entity p_70816_1_)
    {
        Vec3d vec3d = new Vec3d(posX - p_70816_1_.posX, getEntityBoundingBox().minY + (double)(height / 2.0F) - p_70816_1_.posY + (double)p_70816_1_.getEyeHeight(), posZ - p_70816_1_.posZ);
        vec3d = vec3d.normalize();
        double d0 = 16.0D;
        double d1 = posX + (rand.nextDouble() - 0.5D) * 8.0D - vec3d.xCoord * 16.0D;
        double d2 = posY + (double)(rand.nextInt(16) - 8) - vec3d.yCoord * 16.0D;
        double d3 = posZ + (rand.nextDouble() - 0.5D) * 8.0D - vec3d.zCoord * 16.0D;
        return teleportTo(d1, d2, d3);
    }

    /**
     * Teleport the enderman
     */
    private boolean teleportTo(double x, double y, double z)
    {
        boolean flag = attemptTeleport(x, y, z);

        if (flag)
        {
            world.playSound(null, prevPosX, prevPosY, prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, getSoundCategory(), 1.0F, 1.0F);
            playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
    }

    protected SoundEvent getAmbientSound()
    {
        return isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : SoundEvents.ENTITY_ENDERMEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_ENDERMEN_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ENDERMEN_DEATH;
    }

    /**
     * Drop the equipment for this entity.
     */
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier)
    {
        super.dropEquipment(wasRecentlyHit, lootingModifier);
        IBlockState iblockstate = getHeldBlockState();

        if (iblockstate != null)
        {
            Item item = Item.getItemFromBlock(iblockstate.getBlock());
            int i = item.getHasSubtypes() ? iblockstate.getBlock().getMetaFromState(iblockstate) : 0;
            entityDropItem(new ItemStack(item, 1, i), 0.0F);
        }
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_ENDERMAN;
    }

    /**
     * Sets this enderman's held block state
     */
    public void setHeldBlockState(@Nullable IBlockState state)
    {
        dataManager.set(CARRIED_BLOCK, Optional.fromNullable(state));
    }

    @Nullable

    /**
     * Gets this enderman's held block state
     */
    public IBlockState getHeldBlockState()
    {
        return (IBlockState)((Optional) dataManager.get(CARRIED_BLOCK)).orNull();
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
        else if (source instanceof EntityDamageSourceIndirect)
        {
            for (int i = 0; i < 64; ++i)
            {
                if (teleportRandomly())
                {
                    return true;
                }
            }

            return false;
        }
        else
        {
            boolean flag = super.attackEntityFrom(source, amount);

            if (source.isUnblockable() && rand.nextInt(10) != 0)
            {
                teleportRandomly();
            }

            return flag;
        }
    }

    public boolean isScreaming()
    {
        return dataManager.get(SCREAMING).booleanValue();
    }

    static
    {
        CARRIABLE_BLOCKS.add(Blocks.GRASS);
        CARRIABLE_BLOCKS.add(Blocks.DIRT);
        CARRIABLE_BLOCKS.add(Blocks.SAND);
        CARRIABLE_BLOCKS.add(Blocks.GRAVEL);
        CARRIABLE_BLOCKS.add(Blocks.YELLOW_FLOWER);
        CARRIABLE_BLOCKS.add(Blocks.RED_FLOWER);
        CARRIABLE_BLOCKS.add(Blocks.BROWN_MUSHROOM);
        CARRIABLE_BLOCKS.add(Blocks.RED_MUSHROOM);
        CARRIABLE_BLOCKS.add(Blocks.TNT);
        CARRIABLE_BLOCKS.add(Blocks.CACTUS);
        CARRIABLE_BLOCKS.add(Blocks.CLAY);
        CARRIABLE_BLOCKS.add(Blocks.PUMPKIN);
        CARRIABLE_BLOCKS.add(Blocks.MELON_BLOCK);
        CARRIABLE_BLOCKS.add(Blocks.MYCELIUM);
        CARRIABLE_BLOCKS.add(Blocks.NETHERRACK);
    }

    static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        private final EntityEnderman enderman;
        private EntityPlayer player;
        private int aggroTime;
        private int teleportTime;

        public AIFindPlayer(EntityEnderman p_i45842_1_)
        {
            super(p_i45842_1_, EntityPlayer.class, false);
            enderman = p_i45842_1_;
        }

        public boolean shouldExecute()
        {
            double d0 = getTargetDistance();
            player = enderman.world.getNearestAttackablePlayer(enderman.posX, enderman.posY, enderman.posZ, d0, d0, null, new Predicate<EntityPlayer>()
            {
                public boolean apply(@Nullable EntityPlayer p_apply_1_)
                {
                    return p_apply_1_ != null && enderman.shouldAttackPlayer(p_apply_1_);
                }
            });
            return player != null;
        }

        public void startExecuting()
        {
            aggroTime = 5;
            teleportTime = 0;
        }

        public void resetTask()
        {
            player = null;
            super.resetTask();
        }

        public boolean continueExecuting()
        {
            if (player != null)
            {
                if (!enderman.shouldAttackPlayer(player))
                {
                    return false;
                }
                else
                {
                    enderman.faceEntity(player, 10.0F, 10.0F);
                    return true;
                }
            }
            else
            {
                return targetEntity != null && targetEntity.isEntityAlive() || super.continueExecuting();
            }
        }

        public void updateTask()
        {
            if (player != null)
            {
                if (--aggroTime <= 0)
                {
                    targetEntity = player;
                    player = null;
                    super.startExecuting();
                }
            }
            else
            {
                if (targetEntity != null)
                {
                    if (enderman.shouldAttackPlayer(targetEntity))
                    {
                        if (targetEntity.getDistanceSqToEntity(enderman) < 16.0D)
                        {
                            enderman.teleportRandomly();
                        }

                        teleportTime = 0;
                    }
                    else if (targetEntity.getDistanceSqToEntity(enderman) > 256.0D && teleportTime++ >= 30 && enderman.teleportToEntity(targetEntity))
                    {
                        teleportTime = 0;
                    }
                }

                super.updateTask();
            }
        }
    }

    static class AIPlaceBlock extends EntityAIBase
    {
        private final EntityEnderman enderman;

        public AIPlaceBlock(EntityEnderman p_i45843_1_)
        {
            enderman = p_i45843_1_;
        }

        public boolean shouldExecute()
        {
            if (enderman.getHeldBlockState() == null)
            {
                return false;
            }
            else if (!enderman.world.getGameRules().getBoolean("mobGriefing"))
            {
                return false;
            }
            else
            {
                return enderman.getRNG().nextInt(2000) == 0;
            }
        }

        public void updateTask()
        {
            Random random = enderman.getRNG();
            World world = enderman.world;
            int i = MathHelper.floor(enderman.posX - 1.0D + random.nextDouble() * 2.0D);
            int j = MathHelper.floor(enderman.posY + random.nextDouble() * 2.0D);
            int k = MathHelper.floor(enderman.posZ - 1.0D + random.nextDouble() * 2.0D);
            BlockPos blockpos = new BlockPos(i, j, k);
            IBlockState iblockstate = world.getBlockState(blockpos);
            IBlockState iblockstate1 = world.getBlockState(blockpos.down());
            IBlockState iblockstate2 = enderman.getHeldBlockState();

            if (iblockstate2 != null && canPlaceBlock(world, blockpos, iblockstate2.getBlock(), iblockstate, iblockstate1))
            {
                world.setBlockState(blockpos, iblockstate2, 3);
                enderman.setHeldBlockState(null);
            }
        }

        private boolean canPlaceBlock(World p_188518_1_, BlockPos p_188518_2_, Block p_188518_3_, IBlockState p_188518_4_, IBlockState p_188518_5_)
        {
            if (!p_188518_3_.canPlaceBlockAt(p_188518_1_, p_188518_2_))
            {
                return false;
            }
            else if (p_188518_4_.getMaterial() != Material.AIR)
            {
                return false;
            }
            else if (p_188518_5_.getMaterial() == Material.AIR)
            {
                return false;
            }
            else
            {
                return p_188518_5_.isFullCube();
            }
        }
    }

    static class AITakeBlock extends EntityAIBase
    {
        private final EntityEnderman enderman;

        public AITakeBlock(EntityEnderman p_i45841_1_)
        {
            enderman = p_i45841_1_;
        }

        public boolean shouldExecute()
        {
            if (enderman.getHeldBlockState() != null)
            {
                return false;
            }
            else if (!enderman.world.getGameRules().getBoolean("mobGriefing"))
            {
                return false;
            }
            else
            {
                return enderman.getRNG().nextInt(20) == 0;
            }
        }

        public void updateTask()
        {
            Random random = enderman.getRNG();
            World world = enderman.world;
            int i = MathHelper.floor(enderman.posX - 2.0D + random.nextDouble() * 4.0D);
            int j = MathHelper.floor(enderman.posY + random.nextDouble() * 3.0D);
            int k = MathHelper.floor(enderman.posZ - 2.0D + random.nextDouble() * 4.0D);
            BlockPos blockpos = new BlockPos(i, j, k);
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            RayTraceResult raytraceresult = world.rayTraceBlocks(new Vec3d((float)MathHelper.floor(enderman.posX) + 0.5F, (float)j + 0.5F, (float)MathHelper.floor(enderman.posZ) + 0.5F), new Vec3d((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F), false, true, false);
            boolean flag = raytraceresult != null && raytraceresult.getBlockPos().equals(blockpos);

            if (CARRIABLE_BLOCKS.contains(block) && flag)
            {
                enderman.setHeldBlockState(iblockstate);
                world.setBlockToAir(blockpos);
            }
        }
    }
}
