package net.minecraft.entity.item;

import com.google.common.base.Optional;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;

public class EntityEnderCrystal extends Entity
{
    private static final DataParameter<Optional<BlockPos>> BEAM_TARGET = EntityDataManager.createKey(EntityEnderCrystal.class, DataSerializers.OPTIONAL_BLOCK_POS);
    private static final DataParameter<Boolean> SHOW_BOTTOM = EntityDataManager.createKey(EntityEnderCrystal.class, DataSerializers.BOOLEAN);

    /** Used to create the rotation animation when rendering the crystal. */
    public int innerRotation;

    public EntityEnderCrystal(World worldIn)
    {
        super(worldIn);
        preventEntitySpawning = true;
        setSize(2.0F, 2.0F);
        innerRotation = rand.nextInt(100000);
    }

    public EntityEnderCrystal(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        setPosition(x, y, z);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        getDataManager().register(BEAM_TARGET, Optional.absent());
        getDataManager().register(SHOW_BOTTOM, Boolean.valueOf(true));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        ++innerRotation;

        if (!world.isRemote)
        {
            BlockPos blockpos = new BlockPos(this);

            if (world.provider instanceof WorldProviderEnd && world.getBlockState(blockpos).getBlock() != Blocks.FIRE)
            {
                world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        if (getBeamTarget() != null)
        {
            compound.setTag("BeamTarget", NBTUtil.createPosTag(getBeamTarget()));
        }

        compound.setBoolean("ShowBottom", shouldShowBottom());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        if (compound.hasKey("BeamTarget", 10))
        {
            setBeamTarget(NBTUtil.getPosFromTag(compound.getCompoundTag("BeamTarget")));
        }

        if (compound.hasKey("ShowBottom", 1))
        {
            setShowBottom(compound.getBoolean("ShowBottom"));
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
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
        else if (source.getEntity() instanceof EntityDragon)
        {
            return false;
        }
        else
        {
            if (!isDead && !world.isRemote)
            {
                setDead();

                if (!world.isRemote)
                {
                    if (!source.isExplosion())
                    {
                        world.createExplosion(null, posX, posY, posZ, 6.0F, true);
                    }

                    onCrystalDestroyed(source);
                }
            }

            return true;
        }
    }

    /**
     * Called by the /kill command.
     */
    public void onKillCommand()
    {
        onCrystalDestroyed(DamageSource.generic);
        super.onKillCommand();
    }

    private void onCrystalDestroyed(DamageSource source)
    {
        if (world.provider instanceof WorldProviderEnd)
        {
            WorldProviderEnd worldproviderend = (WorldProviderEnd) world.provider;
            DragonFightManager dragonfightmanager = worldproviderend.getDragonFightManager();

            if (dragonfightmanager != null)
            {
                dragonfightmanager.onCrystalDestroyed(this, source);
            }
        }
    }

    public void setBeamTarget(@Nullable BlockPos beamTarget)
    {
        getDataManager().set(BEAM_TARGET, Optional.fromNullable(beamTarget));
    }

    @Nullable
    public BlockPos getBeamTarget()
    {
        return (BlockPos)((Optional) getDataManager().get(BEAM_TARGET)).orNull();
    }

    public void setShowBottom(boolean showBottom)
    {
        getDataManager().set(SHOW_BOTTOM, Boolean.valueOf(showBottom));
    }

    public boolean shouldShowBottom()
    {
        return getDataManager().get(SHOW_BOTTOM).booleanValue();
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        return super.isInRangeToRenderDist(distance) || getBeamTarget() != null;
    }
}
