package net.minecraft.entity.passive;

import java.util.Calendar;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
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
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityBat extends EntityAmbientCreature
{
    private static final DataParameter<Byte> HANGING = EntityDataManager.createKey(EntityBat.class, DataSerializers.BYTE);

    /** Coordinates of where the bat spawned. */
    private BlockPos spawnPosition;

    public EntityBat(World worldIn)
    {
        super(worldIn);
        setSize(0.5F, 0.9F);
        setIsBatHanging(true);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(HANGING, Byte.valueOf((byte)0));
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.1F;
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    protected float getSoundPitch()
    {
        return super.getSoundPitch() * 0.95F;
    }

    @Nullable
    public SoundEvent getAmbientSound()
    {
        return getIsBatHanging() && rand.nextInt(4) != 0 ? null : SoundEvents.ENTITY_BAT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_BAT_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_BAT_DEATH;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return false;
    }

    protected void collideWithEntity(Entity entityIn)
    {
    }

    protected void collideWithNearbyEntities()
    {
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
    }

    public boolean getIsBatHanging()
    {
        return (dataManager.get(HANGING).byteValue() & 1) != 0;
    }

    public void setIsBatHanging(boolean isHanging)
    {
        byte b0 = dataManager.get(HANGING).byteValue();

        if (isHanging)
        {
            dataManager.set(HANGING, Byte.valueOf((byte)(b0 | 1)));
        }
        else
        {
            dataManager.set(HANGING, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (getIsBatHanging())
        {
            motionX = 0.0D;
            motionY = 0.0D;
            motionZ = 0.0D;
            posY = (double)MathHelper.floor(posY) + 1.0D - (double) height;
        }
        else
        {
            motionY *= 0.6000000238418579D;
        }
    }

    protected void updateAITasks()
    {
        super.updateAITasks();
        BlockPos blockpos = new BlockPos(this);
        BlockPos blockpos1 = blockpos.up();

        if (getIsBatHanging())
        {
            if (world.getBlockState(blockpos1).isNormalCube())
            {
                if (rand.nextInt(200) == 0)
                {
                    rotationYawHead = (float) rand.nextInt(360);
                }

                if (world.getNearestPlayerNotCreative(this, 4.0D) != null)
                {
                    setIsBatHanging(false);
                    world.playEvent(null, 1025, blockpos, 0);
                }
            }
            else
            {
                setIsBatHanging(false);
                world.playEvent(null, 1025, blockpos, 0);
            }
        }
        else
        {
            if (spawnPosition != null && (!world.isAirBlock(spawnPosition) || spawnPosition.getY() < 1))
            {
                spawnPosition = null;
            }

            if (spawnPosition == null || rand.nextInt(30) == 0 || spawnPosition.distanceSq((int) posX, (int) posY, (int) posZ) < 4.0D)
            {
                spawnPosition = new BlockPos((int) posX + rand.nextInt(7) - rand.nextInt(7), (int) posY + rand.nextInt(6) - 2, (int) posZ + rand.nextInt(7) - rand.nextInt(7));
            }

            double d0 = (double) spawnPosition.getX() + 0.5D - posX;
            double d1 = (double) spawnPosition.getY() + 0.1D - posY;
            double d2 = (double) spawnPosition.getZ() + 0.5D - posZ;
            motionX += (Math.signum(d0) * 0.5D - motionX) * 0.10000000149011612D;
            motionY += (Math.signum(d1) * 0.699999988079071D - motionY) * 0.10000000149011612D;
            motionZ += (Math.signum(d2) * 0.5D - motionZ) * 0.10000000149011612D;
            float f = (float)(MathHelper.atan2(motionZ, motionX) * (180D / Math.PI)) - 90.0F;
            float f1 = MathHelper.wrapDegrees(f - rotationYaw);
            field_191988_bg = 0.5F;
            rotationYaw += f1;

            if (rand.nextInt(100) == 0 && world.getBlockState(blockpos1).isNormalCube())
            {
                setIsBatHanging(true);
            }
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
    }

    /**
     * Return whether this entity should NOT trigger a pressure plate or a tripwire.
     */
    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
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
        else
        {
            if (!world.isRemote && getIsBatHanging())
            {
                setIsBatHanging(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    public static void registerFixesBat(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityBat.class);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        dataManager.set(HANGING, Byte.valueOf(compound.getByte("BatFlags")));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setByte("BatFlags", dataManager.get(HANGING).byteValue());
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        BlockPos blockpos = new BlockPos(posX, getEntityBoundingBox().minY, posZ);

        if (blockpos.getY() >= world.getSeaLevel())
        {
            return false;
        }
        else
        {
            int i = world.getLightFromNeighbors(blockpos);
            int j = 4;

            if (isDateAroundHalloween(world.getCurrentDate()))
            {
                j = 7;
            }
            else if (rand.nextBoolean())
            {
                return false;
            }

            return i <= rand.nextInt(j) && super.getCanSpawnHere();
        }
    }

    private boolean isDateAroundHalloween(Calendar p_175569_1_)
    {
        return p_175569_1_.get(2) + 1 == 10 && p_175569_1_.get(5) >= 20 || p_175569_1_.get(2) + 1 == 11 && p_175569_1_.get(5) <= 3;
    }

    public float getEyeHeight()
    {
        return height / 2.0F;
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_BAT;
    }
}
