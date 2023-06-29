package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGhast extends EntityFlying implements IMob
{
    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(EntityGhast.class, DataSerializers.BOOLEAN);

    /** The explosion radius of spawned fireballs. */
    private int explosionStrength = 1;

    public EntityGhast(World worldIn)
    {
        super(worldIn);
        setSize(4.0F, 4.0F);
        isImmuneToFire = true;
        experienceValue = 5;
        moveHelper = new EntityGhast.GhastMoveHelper(this);
    }

    protected void initEntityAI()
    {
        tasks.addTask(5, new EntityGhast.AIRandomFly(this));
        tasks.addTask(7, new EntityGhast.AILookAround(this));
        tasks.addTask(7, new EntityGhast.AIFireballAttack(this));
        targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
    }

    public boolean isAttacking()
    {
        return dataManager.get(ATTACKING).booleanValue();
    }

    public void setAttacking(boolean attacking)
    {
        dataManager.set(ATTACKING, Boolean.valueOf(attacking));
    }

    public int getFireballStrength()
    {
        return explosionStrength;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!world.isRemote && world.getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            setDead();
        }
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
        else if (source.getSourceOfDamage() instanceof EntityLargeFireball && source.getEntity() instanceof EntityPlayer)
        {
            super.attackEntityFrom(source, 1000.0F);
            return true;
        }
        else
        {
            return super.attackEntityFrom(source, amount);
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(ATTACKING, Boolean.valueOf(false));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0D);
    }

    public SoundCategory getSoundCategory()
    {
        return SoundCategory.HOSTILE;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_GHAST_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_GHAST_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_GHAST_DEATH;
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_GHAST;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 10.0F;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return rand.nextInt(20) == 0 && super.getCanSpawnHere() && world.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    public static void registerFixesGhast(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityGhast.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("ExplosionPower", explosionStrength);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("ExplosionPower", 99))
        {
            explosionStrength = compound.getInteger("ExplosionPower");
        }
    }

    public float getEyeHeight()
    {
        return 2.6F;
    }

    static class AIFireballAttack extends EntityAIBase
    {
        private final EntityGhast parentEntity;
        public int attackTimer;

        public AIFireballAttack(EntityGhast ghast)
        {
            parentEntity = ghast;
        }

        public boolean shouldExecute()
        {
            return parentEntity.getAttackTarget() != null;
        }

        public void startExecuting()
        {
            attackTimer = 0;
        }

        public void resetTask()
        {
            parentEntity.setAttacking(false);
        }

        public void updateTask()
        {
            EntityLivingBase entitylivingbase = parentEntity.getAttackTarget();
            double d0 = 64.0D;

            if (entitylivingbase.getDistanceSqToEntity(parentEntity) < 4096.0D && parentEntity.canEntityBeSeen(entitylivingbase))
            {
                World world = parentEntity.world;
                ++attackTimer;

                if (attackTimer == 10)
                {
                    world.playEvent(null, 1015, new BlockPos(parentEntity), 0);
                }

                if (attackTimer == 20)
                {
                    double d1 = 4.0D;
                    Vec3d vec3d = parentEntity.getLook(1.0F);
                    double d2 = entitylivingbase.posX - (parentEntity.posX + vec3d.xCoord * 4.0D);
                    double d3 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (0.5D + parentEntity.posY + (double)(parentEntity.height / 2.0F));
                    double d4 = entitylivingbase.posZ - (parentEntity.posZ + vec3d.zCoord * 4.0D);
                    world.playEvent(null, 1016, new BlockPos(parentEntity), 0);
                    EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, parentEntity, d2, d3, d4);
                    entitylargefireball.explosionPower = parentEntity.getFireballStrength();
                    entitylargefireball.posX = parentEntity.posX + vec3d.xCoord * 4.0D;
                    entitylargefireball.posY = parentEntity.posY + (double)(parentEntity.height / 2.0F) + 0.5D;
                    entitylargefireball.posZ = parentEntity.posZ + vec3d.zCoord * 4.0D;
                    world.spawnEntityInWorld(entitylargefireball);
                    attackTimer = -40;
                }
            }
            else if (attackTimer > 0)
            {
                --attackTimer;
            }

            parentEntity.setAttacking(attackTimer > 10);
        }
    }

    static class AILookAround extends EntityAIBase
    {
        private final EntityGhast parentEntity;

        public AILookAround(EntityGhast ghast)
        {
            parentEntity = ghast;
            setMutexBits(2);
        }

        public boolean shouldExecute()
        {
            return true;
        }

        public void updateTask()
        {
            if (parentEntity.getAttackTarget() == null)
            {
                parentEntity.rotationYaw = -((float)MathHelper.atan2(parentEntity.motionX, parentEntity.motionZ)) * (180F / (float)Math.PI);
                parentEntity.renderYawOffset = parentEntity.rotationYaw;
            }
            else
            {
                EntityLivingBase entitylivingbase = parentEntity.getAttackTarget();
                double d0 = 64.0D;

                if (entitylivingbase.getDistanceSqToEntity(parentEntity) < 4096.0D)
                {
                    double d1 = entitylivingbase.posX - parentEntity.posX;
                    double d2 = entitylivingbase.posZ - parentEntity.posZ;
                    parentEntity.rotationYaw = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
                    parentEntity.renderYawOffset = parentEntity.rotationYaw;
                }
            }
        }
    }

    static class AIRandomFly extends EntityAIBase
    {
        private final EntityGhast parentEntity;

        public AIRandomFly(EntityGhast ghast)
        {
            parentEntity = ghast;
            setMutexBits(1);
        }

        public boolean shouldExecute()
        {
            EntityMoveHelper entitymovehelper = parentEntity.getMoveHelper();

            if (!entitymovehelper.isUpdating())
            {
                return true;
            }
            else
            {
                double d0 = entitymovehelper.getX() - parentEntity.posX;
                double d1 = entitymovehelper.getY() - parentEntity.posY;
                double d2 = entitymovehelper.getZ() - parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        public boolean continueExecuting()
        {
            return false;
        }

        public void startExecuting()
        {
            Random random = parentEntity.getRNG();
            double d0 = parentEntity.posX + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = parentEntity.posY + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = parentEntity.posZ + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
        }
    }

    static class GhastMoveHelper extends EntityMoveHelper
    {
        private final EntityGhast parentEntity;
        private int courseChangeCooldown;

        public GhastMoveHelper(EntityGhast ghast)
        {
            super(ghast);
            parentEntity = ghast;
        }

        public void onUpdateMoveHelper()
        {
            if (action == EntityMoveHelper.Action.MOVE_TO)
            {
                double d0 = posX - parentEntity.posX;
                double d1 = posY - parentEntity.posY;
                double d2 = posZ - parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (courseChangeCooldown-- <= 0)
                {
                    courseChangeCooldown += parentEntity.getRNG().nextInt(5) + 2;
                    d3 = MathHelper.sqrt(d3);

                    if (isNotColliding(posX, posY, posZ, d3))
                    {
                        parentEntity.motionX += d0 / d3 * 0.1D;
                        parentEntity.motionY += d1 / d3 * 0.1D;
                        parentEntity.motionZ += d2 / d3 * 0.1D;
                    }
                    else
                    {
                        action = EntityMoveHelper.Action.WAIT;
                    }
                }
            }
        }

        private boolean isNotColliding(double x, double y, double z, double p_179926_7_)
        {
            double d0 = (x - parentEntity.posX) / p_179926_7_;
            double d1 = (y - parentEntity.posY) / p_179926_7_;
            double d2 = (z - parentEntity.posZ) / p_179926_7_;
            AxisAlignedBB axisalignedbb = parentEntity.getEntityBoundingBox();

            for (int i = 1; (double)i < p_179926_7_; ++i)
            {
                axisalignedbb = axisalignedbb.offset(d0, d1, d2);

                if (!parentEntity.world.getCollisionBoxes(parentEntity, axisalignedbb).isEmpty())
                {
                    return false;
                }
            }

            return true;
        }
    }
}
