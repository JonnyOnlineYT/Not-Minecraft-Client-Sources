package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIDefendVillage;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityIronGolem extends EntityGolem
{
    protected static final DataParameter<Byte> PLAYER_CREATED = EntityDataManager.createKey(EntityIronGolem.class, DataSerializers.BYTE);

    /** deincrements, and a distance-to-home check is done at 0 */
    private int homeCheckTimer;
    @Nullable
    Village villageObj;
    private int attackTimer;
    private int holdRoseTick;

    public EntityIronGolem(World worldIn)
    {
        super(worldIn);
        setSize(1.4F, 2.7F);
    }

    protected void initEntityAI()
    {
        tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
        tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
        tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        tasks.addTask(5, new EntityAILookAtVillager(this));
        tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIDefendVillage(this));
        targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, false, true, new Predicate<EntityLiving>()
        {
            public boolean apply(@Nullable EntityLiving p_apply_1_)
            {
                return p_apply_1_ != null && IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_) && !(p_apply_1_ instanceof EntityCreeper);
            }
        }));
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(PLAYER_CREATED, Byte.valueOf((byte)0));
    }

    protected void updateAITasks()
    {
        if (--homeCheckTimer <= 0)
        {
            homeCheckTimer = 70 + rand.nextInt(50);
            villageObj = world.getVillageCollection().getNearestVillage(new BlockPos(this), 32);

            if (villageObj == null)
            {
                detachHome();
            }
            else
            {
                BlockPos blockpos = villageObj.getCenter();
                setHomePosAndDistance(blockpos, (int)((float) villageObj.getVillageRadius() * 0.6F));
            }
        }

        super.updateAITasks();
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    /**
     * Decrements the entity's air supply when underwater
     */
    protected int decreaseAirSupply(int air)
    {
        return air;
    }

    protected void collideWithEntity(Entity entityIn)
    {
        if (entityIn instanceof IMob && !(entityIn instanceof EntityCreeper) && getRNG().nextInt(20) == 0)
        {
            setAttackTarget((EntityLivingBase)entityIn);
        }

        super.collideWithEntity(entityIn);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (attackTimer > 0)
        {
            --attackTimer;
        }

        if (holdRoseTick > 0)
        {
            --holdRoseTick;
        }

        if (motionX * motionX + motionZ * motionZ > 2.500000277905201E-7D && rand.nextInt(5) == 0)
        {
            int i = MathHelper.floor(posX);
            int j = MathHelper.floor(posY - 0.20000000298023224D);
            int k = MathHelper.floor(posZ);
            IBlockState iblockstate = world.getBlockState(new BlockPos(i, j, k));

            if (iblockstate.getMaterial() != Material.AIR)
            {
                world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX + ((double) rand.nextFloat() - 0.5D) * (double) width, getEntityBoundingBox().minY + 0.1D, posZ + ((double) rand.nextFloat() - 0.5D) * (double) width, 4.0D * ((double) rand.nextFloat() - 0.5D), 0.5D, ((double) rand.nextFloat() - 0.5D) * 4.0D, Block.getStateId(iblockstate));
            }
        }
    }

    /**
     * Returns true if this entity can attack entities of the specified class.
     */
    public boolean canAttackClass(Class <? extends EntityLivingBase > cls)
    {
        if (isPlayerCreated() && EntityPlayer.class.isAssignableFrom(cls))
        {
            return false;
        }
        else
        {
            return cls != EntityCreeper.class && super.canAttackClass(cls);
        }
    }

    public static void registerFixesIronGolem(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityIronGolem.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("PlayerCreated", isPlayerCreated());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setPlayerCreated(compound.getBoolean("PlayerCreated"));
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        attackTimer = 10;
        world.setEntityState(this, (byte)4);
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7 + rand.nextInt(15)));

        if (flag)
        {
            entityIn.motionY += 0.4000000059604645D;
            applyEnchantments(this, entityIn);
        }

        playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 4)
        {
            attackTimer = 10;
            playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        }
        else if (id == 11)
        {
            holdRoseTick = 400;
        }
        else if (id == 34)
        {
            holdRoseTick = 0;
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    public Village getVillage()
    {
        return villageObj;
    }

    public int getAttackTimer()
    {
        return attackTimer;
    }

    public void setHoldingRose(boolean p_70851_1_)
    {
        if (p_70851_1_)
        {
            holdRoseTick = 400;
            world.setEntityState(this, (byte)11);
        }
        else
        {
            holdRoseTick = 0;
            world.setEntityState(this, (byte)34);
        }
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_IRONGOLEM_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_IRONGOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_IRON_GOLEM;
    }

    public int getHoldRoseTick()
    {
        return holdRoseTick;
    }

    public boolean isPlayerCreated()
    {
        return (dataManager.get(PLAYER_CREATED).byteValue() & 1) != 0;
    }

    public void setPlayerCreated(boolean playerCreated)
    {
        byte b0 = dataManager.get(PLAYER_CREATED).byteValue();

        if (playerCreated)
        {
            dataManager.set(PLAYER_CREATED, Byte.valueOf((byte)(b0 | 1)));
        }
        else
        {
            dataManager.set(PLAYER_CREATED, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause)
    {
        if (!isPlayerCreated() && attackingPlayer != null && villageObj != null)
        {
            villageObj.modifyPlayerReputation(attackingPlayer.getName(), -5);
        }

        super.onDeath(cause);
    }
}
