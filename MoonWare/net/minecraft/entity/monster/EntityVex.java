package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityVex extends EntityMob
{
    protected static final DataParameter<Byte> field_190664_a = EntityDataManager.createKey(EntityVex.class, DataSerializers.BYTE);
    private EntityLiving field_190665_b;
    @Nullable
    private BlockPos field_190666_c;
    private boolean field_190667_bw;
    private int field_190668_bx;

    public EntityVex(World p_i47280_1_)
    {
        super(p_i47280_1_);
        isImmuneToFire = true;
        moveHelper = new EntityVex.AIMoveControl(this);
        setSize(0.4F, 0.8F);
        experienceValue = 3;
    }

    /**
     * Tries to move the entity towards the specified location.
     */
    public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_)
    {
        super.moveEntity(x, p_70091_2_, p_70091_4_, p_70091_6_);
        doBlockCollisions();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        noClip = true;
        super.onUpdate();
        noClip = false;
        setNoGravity(true);

        if (field_190667_bw && --field_190668_bx <= 0)
        {
            field_190668_bx = 20;
            attackEntityFrom(DamageSource.starve, 1.0F);
        }
    }

    protected void initEntityAI()
    {
        super.initEntityAI();
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(4, new EntityVex.AIChargeAttack());
        tasks.addTask(8, new EntityVex.AIMoveRandom());
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityVex.class));
        targetTasks.addTask(2, new EntityVex.AICopyOwnerTarget(this));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(field_190664_a, Byte.valueOf((byte)0));
    }

    public static void func_190663_b(DataFixer p_190663_0_)
    {
        EntityLiving.registerFixesMob(p_190663_0_, EntityVex.class);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("BoundX"))
        {
            field_190666_c = new BlockPos(compound.getInteger("BoundX"), compound.getInteger("BoundY"), compound.getInteger("BoundZ"));
        }

        if (compound.hasKey("LifeTicks"))
        {
            func_190653_a(compound.getInteger("LifeTicks"));
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (field_190666_c != null)
        {
            compound.setInteger("BoundX", field_190666_c.getX());
            compound.setInteger("BoundY", field_190666_c.getY());
            compound.setInteger("BoundZ", field_190666_c.getZ());
        }

        if (field_190667_bw)
        {
            compound.setInteger("LifeTicks", field_190668_bx);
        }
    }

    public EntityLiving func_190645_o()
    {
        return field_190665_b;
    }

    @Nullable
    public BlockPos func_190646_di()
    {
        return field_190666_c;
    }

    public void func_190651_g(@Nullable BlockPos p_190651_1_)
    {
        field_190666_c = p_190651_1_;
    }

    private boolean func_190656_b(int p_190656_1_)
    {
        int i = dataManager.get(field_190664_a).byteValue();
        return (i & p_190656_1_) != 0;
    }

    private void func_190660_a(int p_190660_1_, boolean p_190660_2_)
    {
        int i = dataManager.get(field_190664_a).byteValue();

        if (p_190660_2_)
        {
            i = i | p_190660_1_;
        }
        else
        {
            i = i & ~p_190660_1_;
        }

        dataManager.set(field_190664_a, Byte.valueOf((byte)(i & 255)));
    }

    public boolean func_190647_dj()
    {
        return func_190656_b(1);
    }

    public void func_190648_a(boolean p_190648_1_)
    {
        func_190660_a(1, p_190648_1_);
    }

    public void func_190658_a(EntityLiving p_190658_1_)
    {
        field_190665_b = p_190658_1_;
    }

    public void func_190653_a(int p_190653_1_)
    {
        field_190667_bw = true;
        field_190668_bx = p_190653_1_;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VEX_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.field_191188_ax;
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

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        setEquipmentBasedOnDifficulty(difficulty);
        setEnchantmentBasedOnDifficulty(difficulty);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
    }

    class AIChargeAttack extends EntityAIBase
    {
        public AIChargeAttack()
        {
            setMutexBits(1);
        }

        public boolean shouldExecute()
        {
            if (getAttackTarget() != null && !getMoveHelper().isUpdating() && rand.nextInt(7) == 0)
            {
                return getDistanceSqToEntity(getAttackTarget()) > 4.0D;
            }
            else
            {
                return false;
            }
        }

        public boolean continueExecuting()
        {
            return getMoveHelper().isUpdating() && func_190647_dj() && getAttackTarget() != null && getAttackTarget().isEntityAlive();
        }

        public void startExecuting()
        {
            EntityLivingBase entitylivingbase = getAttackTarget();
            Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
            moveHelper.setMoveTo(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 1.0D);
            func_190648_a(true);
            playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
        }

        public void resetTask()
        {
            func_190648_a(false);
        }

        public void updateTask()
        {
            EntityLivingBase entitylivingbase = getAttackTarget();

            if (getEntityBoundingBox().intersectsWith(entitylivingbase.getEntityBoundingBox()))
            {
                attackEntityAsMob(entitylivingbase);
                func_190648_a(false);
            }
            else
            {
                double d0 = getDistanceSqToEntity(entitylivingbase);

                if (d0 < 9.0D)
                {
                    Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
                    moveHelper.setMoveTo(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 1.0D);
                }
            }
        }
    }

    class AICopyOwnerTarget extends EntityAITarget
    {
        public AICopyOwnerTarget(EntityCreature p_i47231_2_)
        {
            super(p_i47231_2_, false);
        }

        public boolean shouldExecute()
        {
            return field_190665_b != null && field_190665_b.getAttackTarget() != null && isSuitableTarget(field_190665_b.getAttackTarget(), false);
        }

        public void startExecuting()
        {
            setAttackTarget(field_190665_b.getAttackTarget());
            super.startExecuting();
        }
    }

    class AIMoveControl extends EntityMoveHelper
    {
        public AIMoveControl(EntityVex p_i47230_2_)
        {
            super(p_i47230_2_);
        }

        public void onUpdateMoveHelper()
        {
            if (action == EntityMoveHelper.Action.MOVE_TO)
            {
                double d0 = posX - EntityVex.this.posX;
                double d1 = posY - EntityVex.this.posY;
                double d2 = posZ - EntityVex.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = MathHelper.sqrt(d3);

                if (d3 < getEntityBoundingBox().getAverageEdgeLength())
                {
                    action = EntityMoveHelper.Action.WAIT;
                    motionX *= 0.5D;
                    motionY *= 0.5D;
                    motionZ *= 0.5D;
                }
                else
                {
                    motionX += d0 / d3 * 0.05D * speed;
                    motionY += d1 / d3 * 0.05D * speed;
                    motionZ += d2 / d3 * 0.05D * speed;

                    if (getAttackTarget() == null)
                    {
                        rotationYaw = -((float)MathHelper.atan2(motionX, motionZ)) * (180F / (float)Math.PI);
                        renderYawOffset = rotationYaw;
                    }
                    else
                    {
                        double d4 = getAttackTarget().posX - EntityVex.this.posX;
                        double d5 = getAttackTarget().posZ - EntityVex.this.posZ;
                        rotationYaw = -((float)MathHelper.atan2(d4, d5)) * (180F / (float)Math.PI);
                        renderYawOffset = rotationYaw;
                    }
                }
            }
        }
    }

    class AIMoveRandom extends EntityAIBase
    {
        public AIMoveRandom()
        {
            setMutexBits(1);
        }

        public boolean shouldExecute()
        {
            return !getMoveHelper().isUpdating() && rand.nextInt(7) == 0;
        }

        public boolean continueExecuting()
        {
            return false;
        }

        public void updateTask()
        {
            BlockPos blockpos = func_190646_di();

            if (blockpos == null)
            {
                blockpos = new BlockPos(EntityVex.this);
            }

            for (int i = 0; i < 3; ++i)
            {
                BlockPos blockpos1 = blockpos.add(rand.nextInt(15) - 7, rand.nextInt(11) - 5, rand.nextInt(15) - 7);

                if (world.isAirBlock(blockpos1))
                {
                    moveHelper.setMoveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);

                    if (getAttackTarget() == null)
                    {
                        getLookHelper().setLookPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }
}
