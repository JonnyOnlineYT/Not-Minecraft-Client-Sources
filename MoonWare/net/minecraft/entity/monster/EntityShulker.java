package net.minecraft.entity.monster;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityShulker extends EntityGolem implements IMob
{
    private static final UUID COVERED_ARMOR_BONUS_ID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
    private static final AttributeModifier COVERED_ARMOR_BONUS_MODIFIER = (new AttributeModifier(COVERED_ARMOR_BONUS_ID, "Covered armor bonus", 20.0D, 0)).setSaved(false);
    protected static final DataParameter<EnumFacing> ATTACHED_FACE = EntityDataManager.createKey(EntityShulker.class, DataSerializers.FACING);
    protected static final DataParameter<Optional<BlockPos>> ATTACHED_BLOCK_POS = EntityDataManager.createKey(EntityShulker.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Byte> PEEK_TICK = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> field_190770_bw = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
    public static final EnumDyeColor field_190771_bx = EnumDyeColor.PURPLE;
    private float prevPeekAmount;
    private float peekAmount;
    private BlockPos currentAttachmentPosition;
    private int clientSideTeleportInterpolation;

    public EntityShulker(World worldIn)
    {
        super(worldIn);
        setSize(1.0F, 1.0F);
        prevRenderYawOffset = 180.0F;
        renderYawOffset = 180.0F;
        isImmuneToFire = true;
        currentAttachmentPosition = null;
        experienceValue = 5;
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        renderYawOffset = 180.0F;
        prevRenderYawOffset = 180.0F;
        rotationYaw = 180.0F;
        prevRotationYaw = 180.0F;
        rotationYawHead = 180.0F;
        prevRotationYawHead = 180.0F;
        return super.onInitialSpawn(difficulty, livingdata);
    }

    protected void initEntityAI()
    {
        tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(4, new EntityShulker.AIAttack());
        tasks.addTask(7, new EntityShulker.AIPeek());
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        targetTasks.addTask(2, new EntityShulker.AIAttackNearest(this));
        targetTasks.addTask(3, new EntityShulker.AIDefenseAttack(this));
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public SoundCategory getSoundCategory()
    {
        return SoundCategory.HOSTILE;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SHULKER_AMBIENT;
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound()
    {
        if (!isClosed())
        {
            super.playLivingSound();
        }
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SHULKER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return isClosed() ? SoundEvents.ENTITY_SHULKER_HURT_CLOSED : SoundEvents.ENTITY_SHULKER_HURT;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(ATTACHED_FACE, EnumFacing.DOWN);
        dataManager.register(ATTACHED_BLOCK_POS, Optional.absent());
        dataManager.register(PEEK_TICK, Byte.valueOf((byte)0));
        dataManager.register(field_190770_bw, Byte.valueOf((byte) field_190771_bx.getMetadata()));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
    }

    protected EntityBodyHelper createBodyHelper()
    {
        return new EntityShulker.BodyHelper(this);
    }

    public static void registerFixesShulker(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityShulker.class);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        dataManager.set(ATTACHED_FACE, EnumFacing.getFront(compound.getByte("AttachFace")));
        dataManager.set(PEEK_TICK, Byte.valueOf(compound.getByte("Peek")));
        dataManager.set(field_190770_bw, Byte.valueOf(compound.getByte("Color")));

        if (compound.hasKey("APX"))
        {
            int i = compound.getInteger("APX");
            int j = compound.getInteger("APY");
            int k = compound.getInteger("APZ");
            dataManager.set(ATTACHED_BLOCK_POS, Optional.of(new BlockPos(i, j, k)));
        }
        else
        {
            dataManager.set(ATTACHED_BLOCK_POS, Optional.absent());
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setByte("AttachFace", (byte) dataManager.get(ATTACHED_FACE).getIndex());
        compound.setByte("Peek", dataManager.get(PEEK_TICK).byteValue());
        compound.setByte("Color", dataManager.get(field_190770_bw).byteValue());
        BlockPos blockpos = getAttachmentPos();

        if (blockpos != null)
        {
            compound.setInteger("APX", blockpos.getX());
            compound.setInteger("APY", blockpos.getY());
            compound.setInteger("APZ", blockpos.getZ());
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        BlockPos blockpos = (BlockPos)((Optional) dataManager.get(ATTACHED_BLOCK_POS)).orNull();

        if (blockpos == null && !world.isRemote)
        {
            blockpos = new BlockPos(this);
            dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
        }

        if (isRiding())
        {
            blockpos = null;
            float f = getRidingEntity().rotationYaw;
            rotationYaw = f;
            renderYawOffset = f;
            prevRenderYawOffset = f;
            clientSideTeleportInterpolation = 0;
        }
        else if (!world.isRemote)
        {
            IBlockState iblockstate = world.getBlockState(blockpos);

            if (iblockstate.getMaterial() != Material.AIR)
            {
                if (iblockstate.getBlock() == Blocks.PISTON_EXTENSION)
                {
                    EnumFacing enumfacing = iblockstate.getValue(BlockDirectional.FACING);

                    if (world.isAirBlock(blockpos.offset(enumfacing)))
                    {
                        blockpos = blockpos.offset(enumfacing);
                        dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
                    }
                    else
                    {
                        tryTeleportToNewPosition();
                    }
                }
                else if (iblockstate.getBlock() == Blocks.PISTON_HEAD)
                {
                    EnumFacing enumfacing3 = iblockstate.getValue(BlockDirectional.FACING);

                    if (world.isAirBlock(blockpos.offset(enumfacing3)))
                    {
                        blockpos = blockpos.offset(enumfacing3);
                        dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
                    }
                    else
                    {
                        tryTeleportToNewPosition();
                    }
                }
                else
                {
                    tryTeleportToNewPosition();
                }
            }

            BlockPos blockpos1 = blockpos.offset(getAttachmentFacing());

            if (!world.isBlockNormalCube(blockpos1, false))
            {
                boolean flag = false;

                for (EnumFacing enumfacing1 : EnumFacing.values())
                {
                    blockpos1 = blockpos.offset(enumfacing1);

                    if (world.isBlockNormalCube(blockpos1, false))
                    {
                        dataManager.set(ATTACHED_FACE, enumfacing1);
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    tryTeleportToNewPosition();
                }
            }

            BlockPos blockpos2 = blockpos.offset(getAttachmentFacing().getOpposite());

            if (world.isBlockNormalCube(blockpos2, false))
            {
                tryTeleportToNewPosition();
            }
        }

        float f1 = (float) getPeekTick() * 0.01F;
        prevPeekAmount = peekAmount;

        if (peekAmount > f1)
        {
            peekAmount = MathHelper.clamp(peekAmount - 0.05F, f1, 1.0F);
        }
        else if (peekAmount < f1)
        {
            peekAmount = MathHelper.clamp(peekAmount + 0.05F, 0.0F, f1);
        }

        if (blockpos != null)
        {
            if (world.isRemote)
            {
                if (clientSideTeleportInterpolation > 0 && currentAttachmentPosition != null)
                {
                    --clientSideTeleportInterpolation;
                }
                else
                {
                    currentAttachmentPosition = blockpos;
                }
            }

            posX = (double)blockpos.getX() + 0.5D;
            posY = blockpos.getY();
            posZ = (double)blockpos.getZ() + 0.5D;
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;
            lastTickPosX = posX;
            lastTickPosY = posY;
            lastTickPosZ = posZ;
            double d3 = 0.5D - (double)MathHelper.sin((0.5F + peekAmount) * (float)Math.PI) * 0.5D;
            double d4 = 0.5D - (double)MathHelper.sin((0.5F + prevPeekAmount) * (float)Math.PI) * 0.5D;
            double d5 = d3 - d4;
            double d0 = 0.0D;
            double d1 = 0.0D;
            double d2 = 0.0D;
            EnumFacing enumfacing2 = getAttachmentFacing();

            switch (enumfacing2)
            {
                case DOWN:
                    setEntityBoundingBox(new AxisAlignedBB(posX - 0.5D, posY, posZ - 0.5D, posX + 0.5D, posY + 1.0D + d3, posZ + 0.5D));
                    d1 = d5;
                    break;

                case UP:
                    setEntityBoundingBox(new AxisAlignedBB(posX - 0.5D, posY - d3, posZ - 0.5D, posX + 0.5D, posY + 1.0D, posZ + 0.5D));
                    d1 = -d5;
                    break;

                case NORTH:
                    setEntityBoundingBox(new AxisAlignedBB(posX - 0.5D, posY, posZ - 0.5D, posX + 0.5D, posY + 1.0D, posZ + 0.5D + d3));
                    d2 = d5;
                    break;

                case SOUTH:
                    setEntityBoundingBox(new AxisAlignedBB(posX - 0.5D, posY, posZ - 0.5D - d3, posX + 0.5D, posY + 1.0D, posZ + 0.5D));
                    d2 = -d5;
                    break;

                case WEST:
                    setEntityBoundingBox(new AxisAlignedBB(posX - 0.5D, posY, posZ - 0.5D, posX + 0.5D + d3, posY + 1.0D, posZ + 0.5D));
                    d0 = d5;
                    break;

                case EAST:
                    setEntityBoundingBox(new AxisAlignedBB(posX - 0.5D - d3, posY, posZ - 0.5D, posX + 0.5D, posY + 1.0D, posZ + 0.5D));
                    d0 = -d5;
            }

            if (d5 > 0.0D)
            {
                List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox());

                if (!list.isEmpty())
                {
                    for (Entity entity : list)
                    {
                        if (!(entity instanceof EntityShulker) && !entity.noClip)
                        {
                            entity.moveEntity(MoverType.SHULKER, d0, d1, d2);
                        }
                    }
                }
            }
        }
    }

    /**
     * Tries to move the entity towards the specified location.
     */
    public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_)
    {
        if (x == MoverType.SHULKER_BOX)
        {
            tryTeleportToNewPosition();
        }
        else
        {
            super.moveEntity(x, p_70091_2_, p_70091_4_, p_70091_6_);
        }
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double x, double y, double z)
    {
        super.setPosition(x, y, z);

        if (dataManager != null && ticksExisted != 0)
        {
            Optional<BlockPos> optional = dataManager.get(ATTACHED_BLOCK_POS);
            Optional<BlockPos> optional1 = Optional.of(new BlockPos(x, y, z));

            if (!optional1.equals(optional))
            {
                dataManager.set(ATTACHED_BLOCK_POS, optional1);
                dataManager.set(PEEK_TICK, Byte.valueOf((byte)0));
                isAirBorne = true;
            }
        }
    }

    protected boolean tryTeleportToNewPosition()
    {
        if (!isAIDisabled() && isEntityAlive())
        {
            BlockPos blockpos = new BlockPos(this);

            for (int i = 0; i < 5; ++i)
            {
                BlockPos blockpos1 = blockpos.add(8 - rand.nextInt(17), 8 - rand.nextInt(17), 8 - rand.nextInt(17));

                if (blockpos1.getY() > 0 && world.isAirBlock(blockpos1) && world.func_191503_g(this) && world.getCollisionBoxes(this, new AxisAlignedBB(blockpos1)).isEmpty())
                {
                    boolean flag = false;

                    for (EnumFacing enumfacing : EnumFacing.values())
                    {
                        if (world.isBlockNormalCube(blockpos1.offset(enumfacing), false))
                        {
                            dataManager.set(ATTACHED_FACE, enumfacing);
                            flag = true;
                            break;
                        }
                    }

                    if (flag)
                    {
                        playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1.0F, 1.0F);
                        dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos1));
                        dataManager.set(PEEK_TICK, Byte.valueOf((byte)0));
                        setAttackTarget(null);
                        return true;
                    }
                }
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevRenderYawOffset = 180.0F;
        renderYawOffset = 180.0F;
        rotationYaw = 180.0F;
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (ATTACHED_BLOCK_POS.equals(key) && world.isRemote && !isRiding())
        {
            BlockPos blockpos = getAttachmentPos();

            if (blockpos != null)
            {
                if (currentAttachmentPosition == null)
                {
                    currentAttachmentPosition = blockpos;
                }
                else
                {
                    clientSideTeleportInterpolation = 6;
                }

                posX = (double)blockpos.getX() + 0.5D;
                posY = blockpos.getY();
                posZ = (double)blockpos.getZ() + 0.5D;
                prevPosX = posX;
                prevPosY = posY;
                prevPosZ = posZ;
                lastTickPosX = posX;
                lastTickPosY = posY;
                lastTickPosZ = posZ;
            }
        }

        super.notifyDataManagerChange(key);
    }

    /**
     * Set the position and rotation values directly without any clamping.
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        newPosRotationIncrements = 0;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (isClosed())
        {
            Entity entity = source.getSourceOfDamage();

            if (entity instanceof EntityArrow)
            {
                return false;
            }
        }

        if (super.attackEntityFrom(source, amount))
        {
            if ((double) getHealth() < (double) getMaxHealth() * 0.5D && rand.nextInt(4) == 0)
            {
                tryTeleportToNewPosition();
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isClosed()
    {
        return getPeekTick() == 0;
    }

    @Nullable

    /**
     * Returns the collision bounding box for this entity
     */
    public AxisAlignedBB getCollisionBoundingBox()
    {
        return isEntityAlive() ? getEntityBoundingBox() : null;
    }

    public EnumFacing getAttachmentFacing()
    {
        return dataManager.get(ATTACHED_FACE);
    }

    @Nullable
    public BlockPos getAttachmentPos()
    {
        return (BlockPos)((Optional) dataManager.get(ATTACHED_BLOCK_POS)).orNull();
    }

    public void setAttachmentPos(@Nullable BlockPos pos)
    {
        dataManager.set(ATTACHED_BLOCK_POS, Optional.fromNullable(pos));
    }

    public int getPeekTick()
    {
        return dataManager.get(PEEK_TICK).byteValue();
    }

    /**
     * Applies or removes armor modifier
     */
    public void updateArmorModifier(int p_184691_1_)
    {
        if (!world.isRemote)
        {
            getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(COVERED_ARMOR_BONUS_MODIFIER);

            if (p_184691_1_ == 0)
            {
                getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(COVERED_ARMOR_BONUS_MODIFIER);
                playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0F, 1.0F);
            }
            else
            {
                playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0F, 1.0F);
            }
        }

        dataManager.set(PEEK_TICK, Byte.valueOf((byte)p_184691_1_));
    }

    public float getClientPeekAmount(float p_184688_1_)
    {
        return prevPeekAmount + (peekAmount - prevPeekAmount) * p_184688_1_;
    }

    public int getClientTeleportInterp()
    {
        return clientSideTeleportInterpolation;
    }

    public BlockPos getOldAttachPos()
    {
        return currentAttachmentPosition;
    }

    public float getEyeHeight()
    {
        return 0.5F;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return 180;
    }

    public int getHorizontalFaceSpeed()
    {
        return 180;
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void applyEntityCollision(Entity entityIn)
    {
    }

    public float getCollisionBorderSize()
    {
        return 0.0F;
    }

    public boolean isAttachedToBlock()
    {
        return currentAttachmentPosition != null && getAttachmentPos() != null;
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_SHULKER;
    }

    public EnumDyeColor func_190769_dn()
    {
        return EnumDyeColor.byMetadata(dataManager.get(field_190770_bw).byteValue());
    }

    class AIAttack extends EntityAIBase
    {
        private int attackTime;

        public AIAttack()
        {
            setMutexBits(3);
        }

        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = getAttackTarget();

            if (entitylivingbase != null && entitylivingbase.isEntityAlive())
            {
                return world.getDifficulty() != EnumDifficulty.PEACEFUL;
            }
            else
            {
                return false;
            }
        }

        public void startExecuting()
        {
            attackTime = 20;
            updateArmorModifier(100);
        }

        public void resetTask()
        {
            updateArmorModifier(0);
        }

        public void updateTask()
        {
            if (world.getDifficulty() != EnumDifficulty.PEACEFUL)
            {
                --attackTime;
                EntityLivingBase entitylivingbase = getAttackTarget();
                getLookHelper().setLookPositionWithEntity(entitylivingbase, 180.0F, 180.0F);
                double d0 = getDistanceSqToEntity(entitylivingbase);

                if (d0 < 400.0D)
                {
                    if (attackTime <= 0)
                    {
                        attackTime = 20 + rand.nextInt(10) * 20 / 2;
                        EntityShulkerBullet entityshulkerbullet = new EntityShulkerBullet(world, EntityShulker.this, entitylivingbase, getAttachmentFacing().getAxis());
                        world.spawnEntityInWorld(entityshulkerbullet);
                        playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 2.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    }
                }
                else
                {
                    setAttackTarget(null);
                }

                super.updateTask();
            }
        }
    }

    class AIAttackNearest extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        public AIAttackNearest(EntityShulker shulker)
        {
            super(shulker, EntityPlayer.class, true);
        }

        public boolean shouldExecute()
        {
            return world.getDifficulty() != EnumDifficulty.PEACEFUL && super.shouldExecute();
        }

        protected AxisAlignedBB getTargetableArea(double targetDistance)
        {
            EnumFacing enumfacing = ((EntityShulker) taskOwner).getAttachmentFacing();

            if (enumfacing.getAxis() == EnumFacing.Axis.X)
            {
                return taskOwner.getEntityBoundingBox().expand(4.0D, targetDistance, targetDistance);
            }
            else
            {
                return enumfacing.getAxis() == EnumFacing.Axis.Z ? taskOwner.getEntityBoundingBox().expand(targetDistance, targetDistance, 4.0D) : taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
            }
        }
    }

    static class AIDefenseAttack extends EntityAINearestAttackableTarget<EntityLivingBase>
    {
        public AIDefenseAttack(EntityShulker shulker)
        {
            super(shulker, EntityLivingBase.class, 10, true, false, new Predicate<EntityLivingBase>()
            {
                public boolean apply(@Nullable EntityLivingBase p_apply_1_)
                {
                    return p_apply_1_ instanceof IMob;
                }
            });
        }

        public boolean shouldExecute()
        {
            return taskOwner.getTeam() != null && super.shouldExecute();
        }

        protected AxisAlignedBB getTargetableArea(double targetDistance)
        {
            EnumFacing enumfacing = ((EntityShulker) taskOwner).getAttachmentFacing();

            if (enumfacing.getAxis() == EnumFacing.Axis.X)
            {
                return taskOwner.getEntityBoundingBox().expand(4.0D, targetDistance, targetDistance);
            }
            else
            {
                return enumfacing.getAxis() == EnumFacing.Axis.Z ? taskOwner.getEntityBoundingBox().expand(targetDistance, targetDistance, 4.0D) : taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
            }
        }
    }

    class AIPeek extends EntityAIBase
    {
        private int peekTime;

        private AIPeek()
        {
        }

        public boolean shouldExecute()
        {
            return getAttackTarget() == null && rand.nextInt(40) == 0;
        }

        public boolean continueExecuting()
        {
            return getAttackTarget() == null && peekTime > 0;
        }

        public void startExecuting()
        {
            peekTime = 20 * (1 + rand.nextInt(3));
            updateArmorModifier(30);
        }

        public void resetTask()
        {
            if (getAttackTarget() == null)
            {
                updateArmorModifier(0);
            }
        }

        public void updateTask()
        {
            --peekTime;
        }
    }

    class BodyHelper extends EntityBodyHelper
    {
        public BodyHelper(EntityLivingBase p_i47062_2_)
        {
            super(p_i47062_2_);
        }

        public void updateRenderAngles()
        {
        }
    }
}
