package net.minecraft.entity;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

public abstract class EntityHanging extends Entity
{
    private static final Predicate<Entity> IS_HANGING_ENTITY = new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_ instanceof EntityHanging;
        }
    };
    private int tickCounter1;
    protected BlockPos hangingPosition;
    @Nullable

    /** The direction the entity is facing */
    public EnumFacing facingDirection;

    public EntityHanging(World worldIn)
    {
        super(worldIn);
        setSize(0.5F, 0.5F);
    }

    public EntityHanging(World worldIn, BlockPos hangingPositionIn)
    {
        this(worldIn);
        hangingPosition = hangingPositionIn;
    }

    protected void entityInit()
    {
    }

    /**
     * Updates facing and bounding box based on it
     */
    protected void updateFacingWithBoundingBox(EnumFacing facingDirectionIn)
    {
        Validate.notNull(facingDirectionIn);
        Validate.isTrue(facingDirectionIn.getAxis().isHorizontal());
        facingDirection = facingDirectionIn;
        rotationYaw = (float)(facingDirection.getHorizontalIndex() * 90);
        prevRotationYaw = rotationYaw;
        updateBoundingBox();
    }

    /**
     * Updates the entity bounding box based on current facing
     */
    protected void updateBoundingBox()
    {
        if (facingDirection != null)
        {
            double d0 = (double) hangingPosition.getX() + 0.5D;
            double d1 = (double) hangingPosition.getY() + 0.5D;
            double d2 = (double) hangingPosition.getZ() + 0.5D;
            double d3 = 0.46875D;
            double d4 = offs(getWidthPixels());
            double d5 = offs(getHeightPixels());
            d0 = d0 - (double) facingDirection.getFrontOffsetX() * 0.46875D;
            d2 = d2 - (double) facingDirection.getFrontOffsetZ() * 0.46875D;
            d1 = d1 + d5;
            EnumFacing enumfacing = facingDirection.rotateYCCW();
            d0 = d0 + d4 * (double)enumfacing.getFrontOffsetX();
            d2 = d2 + d4 * (double)enumfacing.getFrontOffsetZ();
            posX = d0;
            posY = d1;
            posZ = d2;
            double d6 = getWidthPixels();
            double d7 = getHeightPixels();
            double d8 = getWidthPixels();

            if (facingDirection.getAxis() == EnumFacing.Axis.Z)
            {
                d8 = 1.0D;
            }
            else
            {
                d6 = 1.0D;
            }

            d6 = d6 / 32.0D;
            d7 = d7 / 32.0D;
            d8 = d8 / 32.0D;
            setEntityBoundingBox(new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
        }
    }

    private double offs(int p_190202_1_)
    {
        return p_190202_1_ % 32 == 0 ? 0.5D : 0.0D;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (tickCounter1++ == 100 && !world.isRemote)
        {
            tickCounter1 = 0;

            if (!isDead && !onValidSurface())
            {
                setDead();
                onBroken(null);
            }
        }
    }

    /**
     * checks to make sure painting can be placed there
     */
    public boolean onValidSurface()
    {
        if (!world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty())
        {
            return false;
        }
        else
        {
            int i = Math.max(1, getWidthPixels() / 16);
            int j = Math.max(1, getHeightPixels() / 16);
            BlockPos blockpos = hangingPosition.offset(facingDirection.getOpposite());
            EnumFacing enumfacing = facingDirection.rotateYCCW();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k = 0; k < i; ++k)
            {
                for (int l = 0; l < j; ++l)
                {
                    int i1 = (i - 1) / -2;
                    int j1 = (j - 1) / -2;
                    blockpos$mutableblockpos.setPos(blockpos).move(enumfacing, k + i1).move(EnumFacing.UP, l + j1);
                    IBlockState iblockstate = world.getBlockState(blockpos$mutableblockpos);

                    if (!iblockstate.getMaterial().isSolid() && !BlockRedstoneDiode.isDiode(iblockstate))
                    {
                        return false;
                    }
                }
            }

            return world.getEntitiesInAABBexcluding(this, getEntityBoundingBox(), IS_HANGING_ENTITY).isEmpty();
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
     * Called when a player attacks an entity. If this returns true the attack will not happen.
     */
    public boolean hitByEntity(Entity entityIn)
    {
        return entityIn instanceof EntityPlayer && attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) entityIn), 0.0F);
    }

    /**
     * Gets the horizontal facing direction of this Entity.
     */
    public EnumFacing getHorizontalFacing()
    {
        return facingDirection;
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
            if (!isDead && !world.isRemote)
            {
                setDead();
                setBeenAttacked();
                onBroken(source.getEntity());
            }

            return true;
        }
    }

    /**
     * Tries to move the entity towards the specified location.
     */
    public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_)
    {
        if (!world.isRemote && !isDead && p_70091_2_ * p_70091_2_ + p_70091_4_ * p_70091_4_ + p_70091_6_ * p_70091_6_ > 0.0D)
        {
            setDead();
            onBroken(null);
        }
    }

    /**
     * Adds to the current velocity of the entity.
     */
    public void addVelocity(double x, double y, double z)
    {
        if (!world.isRemote && !isDead && x * x + y * y + z * z > 0.0D)
        {
            setDead();
            onBroken(null);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setByte("Facing", (byte) facingDirection.getHorizontalIndex());
        BlockPos blockpos = getHangingPosition();
        compound.setInteger("TileX", blockpos.getX());
        compound.setInteger("TileY", blockpos.getY());
        compound.setInteger("TileZ", blockpos.getZ());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        hangingPosition = new BlockPos(compound.getInteger("TileX"), compound.getInteger("TileY"), compound.getInteger("TileZ"));
        updateFacingWithBoundingBox(EnumFacing.getHorizontal(compound.getByte("Facing")));
    }

    public abstract int getWidthPixels();

    public abstract int getHeightPixels();

    /**
     * Called when this entity is broken. Entity parameter may be null.
     */
    public abstract void onBroken(Entity brokenEntity);

    public abstract void playPlaceSound();

    /**
     * Drops an item at the position of the entity.
     */
    public EntityItem entityDropItem(ItemStack stack, float offsetY)
    {
        EntityItem entityitem = new EntityItem(world, posX + (double)((float) facingDirection.getFrontOffsetX() * 0.15F), posY + (double)offsetY, posZ + (double)((float) facingDirection.getFrontOffsetZ() * 0.15F), stack);
        entityitem.setDefaultPickupDelay();
        world.spawnEntityInWorld(entityitem);
        return entityitem;
    }

    protected boolean shouldSetPosAfterLoading()
    {
        return false;
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double x, double y, double z)
    {
        hangingPosition = new BlockPos(x, y, z);
        updateBoundingBox();
        isAirBorne = true;
    }

    public BlockPos getHangingPosition()
    {
        return hangingPosition;
    }

    @SuppressWarnings("incomplete-switch")

    /**
     * Transforms the entity's current yaw with the given Rotation and returns it. This does not have a side-effect.
     */
    public float getRotatedYaw(Rotation transformRotation)
    {
        if (facingDirection != null && facingDirection.getAxis() != EnumFacing.Axis.Y)
        {
            switch (transformRotation)
            {
                case CLOCKWISE_180:
                    facingDirection = facingDirection.getOpposite();
                    break;

                case COUNTERCLOCKWISE_90:
                    facingDirection = facingDirection.rotateYCCW();
                    break;

                case CLOCKWISE_90:
                    facingDirection = facingDirection.rotateY();
            }
        }

        float f = MathHelper.wrapDegrees(rotationYaw);

        switch (transformRotation)
        {
            case CLOCKWISE_180:
                return f + 180.0F;

            case COUNTERCLOCKWISE_90:
                return f + 90.0F;

            case CLOCKWISE_90:
                return f + 270.0F;

            default:
                return f;
        }
    }

    /**
     * Transforms the entity's current yaw with the given Mirror and returns it. This does not have a side-effect.
     */
    public float getMirroredYaw(Mirror transformMirror)
    {
        return getRotatedYaw(transformMirror.toRotation(facingDirection));
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
    }
}
