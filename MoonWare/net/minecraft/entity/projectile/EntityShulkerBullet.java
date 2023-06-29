package net.minecraft.entity.projectile;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityShulkerBullet extends Entity
{
    private EntityLivingBase owner;
    private Entity target;
    @Nullable
    private EnumFacing direction;
    private int steps;
    private double targetDeltaX;
    private double targetDeltaY;
    private double targetDeltaZ;
    @Nullable
    private UUID ownerUniqueId;
    private BlockPos ownerBlockPos;
    @Nullable
    private UUID targetUniqueId;
    private BlockPos targetBlockPos;

    public EntityShulkerBullet(World worldIn)
    {
        super(worldIn);
        setSize(0.3125F, 0.3125F);
        noClip = true;
    }

    public SoundCategory getSoundCategory()
    {
        return SoundCategory.HOSTILE;
    }

    public EntityShulkerBullet(World worldIn, double x, double y, double z, double motionXIn, double motionYIn, double motionZIn)
    {
        this(worldIn);
        setLocationAndAngles(x, y, z, rotationYaw, rotationPitch);
        motionX = motionXIn;
        motionY = motionYIn;
        motionZ = motionZIn;
    }

    public EntityShulkerBullet(World worldIn, EntityLivingBase ownerIn, Entity targetIn, EnumFacing.Axis p_i46772_4_)
    {
        this(worldIn);
        owner = ownerIn;
        BlockPos blockpos = new BlockPos(ownerIn);
        double d0 = (double)blockpos.getX() + 0.5D;
        double d1 = (double)blockpos.getY() + 0.5D;
        double d2 = (double)blockpos.getZ() + 0.5D;
        setLocationAndAngles(d0, d1, d2, rotationYaw, rotationPitch);
        target = targetIn;
        direction = EnumFacing.UP;
        selectNextMoveDirection(p_i46772_4_);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        if (owner != null)
        {
            BlockPos blockpos = new BlockPos(owner);
            NBTTagCompound nbttagcompound = NBTUtil.createUUIDTag(owner.getUniqueID());
            nbttagcompound.setInteger("X", blockpos.getX());
            nbttagcompound.setInteger("Y", blockpos.getY());
            nbttagcompound.setInteger("Z", blockpos.getZ());
            compound.setTag("Owner", nbttagcompound);
        }

        if (target != null)
        {
            BlockPos blockpos1 = new BlockPos(target);
            NBTTagCompound nbttagcompound1 = NBTUtil.createUUIDTag(target.getUniqueID());
            nbttagcompound1.setInteger("X", blockpos1.getX());
            nbttagcompound1.setInteger("Y", blockpos1.getY());
            nbttagcompound1.setInteger("Z", blockpos1.getZ());
            compound.setTag("Target", nbttagcompound1);
        }

        if (direction != null)
        {
            compound.setInteger("Dir", direction.getIndex());
        }

        compound.setInteger("Steps", steps);
        compound.setDouble("TXD", targetDeltaX);
        compound.setDouble("TYD", targetDeltaY);
        compound.setDouble("TZD", targetDeltaZ);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        steps = compound.getInteger("Steps");
        targetDeltaX = compound.getDouble("TXD");
        targetDeltaY = compound.getDouble("TYD");
        targetDeltaZ = compound.getDouble("TZD");

        if (compound.hasKey("Dir", 99))
        {
            direction = EnumFacing.getFront(compound.getInteger("Dir"));
        }

        if (compound.hasKey("Owner", 10))
        {
            NBTTagCompound nbttagcompound = compound.getCompoundTag("Owner");
            ownerUniqueId = NBTUtil.getUUIDFromTag(nbttagcompound);
            ownerBlockPos = new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z"));
        }

        if (compound.hasKey("Target", 10))
        {
            NBTTagCompound nbttagcompound1 = compound.getCompoundTag("Target");
            targetUniqueId = NBTUtil.getUUIDFromTag(nbttagcompound1);
            targetBlockPos = new BlockPos(nbttagcompound1.getInteger("X"), nbttagcompound1.getInteger("Y"), nbttagcompound1.getInteger("Z"));
        }
    }

    protected void entityInit()
    {
    }

    private void setDirection(@Nullable EnumFacing directionIn)
    {
        direction = directionIn;
    }

    private void selectNextMoveDirection(@Nullable EnumFacing.Axis p_184569_1_)
    {
        double d0 = 0.5D;
        BlockPos blockpos;

        if (target == null)
        {
            blockpos = (new BlockPos(this)).down();
        }
        else
        {
            d0 = (double) target.height * 0.5D;
            blockpos = new BlockPos(target.posX, target.posY + d0, target.posZ);
        }

        double d1 = (double)blockpos.getX() + 0.5D;
        double d2 = (double)blockpos.getY() + d0;
        double d3 = (double)blockpos.getZ() + 0.5D;
        EnumFacing enumfacing = null;

        if (blockpos.distanceSqToCenter(posX, posY, posZ) >= 4.0D)
        {
            BlockPos blockpos1 = new BlockPos(this);
            List<EnumFacing> list = Lists.newArrayList();

            if (p_184569_1_ != EnumFacing.Axis.X)
            {
                if (blockpos1.getX() < blockpos.getX() && world.isAirBlock(blockpos1.east()))
                {
                    list.add(EnumFacing.EAST);
                }
                else if (blockpos1.getX() > blockpos.getX() && world.isAirBlock(blockpos1.west()))
                {
                    list.add(EnumFacing.WEST);
                }
            }

            if (p_184569_1_ != EnumFacing.Axis.Y)
            {
                if (blockpos1.getY() < blockpos.getY() && world.isAirBlock(blockpos1.up()))
                {
                    list.add(EnumFacing.UP);
                }
                else if (blockpos1.getY() > blockpos.getY() && world.isAirBlock(blockpos1.down()))
                {
                    list.add(EnumFacing.DOWN);
                }
            }

            if (p_184569_1_ != EnumFacing.Axis.Z)
            {
                if (blockpos1.getZ() < blockpos.getZ() && world.isAirBlock(blockpos1.south()))
                {
                    list.add(EnumFacing.SOUTH);
                }
                else if (blockpos1.getZ() > blockpos.getZ() && world.isAirBlock(blockpos1.north()))
                {
                    list.add(EnumFacing.NORTH);
                }
            }

            enumfacing = EnumFacing.random(rand);

            if (list.isEmpty())
            {
                for (int i = 5; !world.isAirBlock(blockpos1.offset(enumfacing)) && i > 0; --i)
                {
                    enumfacing = EnumFacing.random(rand);
                }
            }
            else
            {
                enumfacing = list.get(rand.nextInt(list.size()));
            }

            d1 = posX + (double)enumfacing.getFrontOffsetX();
            d2 = posY + (double)enumfacing.getFrontOffsetY();
            d3 = posZ + (double)enumfacing.getFrontOffsetZ();
        }

        setDirection(enumfacing);
        double d6 = d1 - posX;
        double d7 = d2 - posY;
        double d4 = d3 - posZ;
        double d5 = MathHelper.sqrt(d6 * d6 + d7 * d7 + d4 * d4);

        if (d5 == 0.0D)
        {
            targetDeltaX = 0.0D;
            targetDeltaY = 0.0D;
            targetDeltaZ = 0.0D;
        }
        else
        {
            targetDeltaX = d6 / d5 * 0.15D;
            targetDeltaY = d7 / d5 * 0.15D;
            targetDeltaZ = d4 / d5 * 0.15D;
        }

        isAirBorne = true;
        steps = 10 + rand.nextInt(5) * 10;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (!world.isRemote && world.getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            setDead();
        }
        else
        {
            super.onUpdate();

            if (!world.isRemote)
            {
                if (target == null && targetUniqueId != null)
                {
                    for (EntityLivingBase entitylivingbase : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(targetBlockPos.add(-2, -2, -2), targetBlockPos.add(2, 2, 2))))
                    {
                        if (entitylivingbase.getUniqueID().equals(targetUniqueId))
                        {
                            target = entitylivingbase;
                            break;
                        }
                    }

                    targetUniqueId = null;
                }

                if (owner == null && ownerUniqueId != null)
                {
                    for (EntityLivingBase entitylivingbase1 : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ownerBlockPos.add(-2, -2, -2), ownerBlockPos.add(2, 2, 2))))
                    {
                        if (entitylivingbase1.getUniqueID().equals(ownerUniqueId))
                        {
                            owner = entitylivingbase1;
                            break;
                        }
                    }

                    ownerUniqueId = null;
                }

                if (target == null || !target.isEntityAlive() || target instanceof EntityPlayer && ((EntityPlayer) target).isSpectator())
                {
                    if (!hasNoGravity())
                    {
                        motionY -= 0.04D;
                    }
                }
                else
                {
                    targetDeltaX = MathHelper.clamp(targetDeltaX * 1.025D, -1.0D, 1.0D);
                    targetDeltaY = MathHelper.clamp(targetDeltaY * 1.025D, -1.0D, 1.0D);
                    targetDeltaZ = MathHelper.clamp(targetDeltaZ * 1.025D, -1.0D, 1.0D);
                    motionX += (targetDeltaX - motionX) * 0.2D;
                    motionY += (targetDeltaY - motionY) * 0.2D;
                    motionZ += (targetDeltaZ - motionZ) * 0.2D;
                }

                RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, false, owner);

                if (raytraceresult != null)
                {
                    bulletHit(raytraceresult);
                }
            }

            setPosition(posX + motionX, posY + motionY, posZ + motionZ);
            ProjectileHelper.rotateTowardsMovement(this, 0.5F);

            if (world.isRemote)
            {
                world.spawnParticle(EnumParticleTypes.END_ROD, posX - motionX, posY - motionY + 0.15D, posZ - motionZ, 0.0D, 0.0D, 0.0D);
            }
            else if (target != null && !target.isDead)
            {
                if (steps > 0)
                {
                    --steps;

                    if (steps == 0)
                    {
                        selectNextMoveDirection(direction == null ? null : direction.getAxis());
                    }
                }

                if (direction != null)
                {
                    BlockPos blockpos = new BlockPos(this);
                    EnumFacing.Axis enumfacing$axis = direction.getAxis();

                    if (world.isBlockNormalCube(blockpos.offset(direction), false))
                    {
                        selectNextMoveDirection(enumfacing$axis);
                    }
                    else
                    {
                        BlockPos blockpos1 = new BlockPos(target);

                        if (enumfacing$axis == EnumFacing.Axis.X && blockpos.getX() == blockpos1.getX() || enumfacing$axis == EnumFacing.Axis.Z && blockpos.getZ() == blockpos1.getZ() || enumfacing$axis == EnumFacing.Axis.Y && blockpos.getY() == blockpos1.getY())
                        {
                            selectNextMoveDirection(enumfacing$axis);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning()
    {
        return false;
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        return distance < 16384.0D;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness()
    {
        return 1.0F;
    }

    public int getBrightnessForRender()
    {
        return 15728880;
    }

    protected void bulletHit(RayTraceResult result)
    {
        if (result.entityHit == null)
        {
            ((WorldServer) world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX, posY, posZ, 2, 0.2D, 0.2D, 0.2D, 0.0D);
            playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
        }
        else
        {
            boolean flag = result.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, owner).setProjectile(), 4.0F);

            if (flag)
            {
                applyEnchantments(owner, result.entityHit);

                if (result.entityHit instanceof EntityLivingBase)
                {
                    ((EntityLivingBase)result.entityHit).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 200));
                }
            }
        }

        setDead();
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
        if (!world.isRemote)
        {
            playSound(SoundEvents.ENTITY_SHULKER_BULLET_HURT, 1.0F, 1.0F);
            ((WorldServer) world).spawnParticle(EnumParticleTypes.CRIT, posX, posY, posZ, 15, 0.2D, 0.2D, 0.2D, 0.0D);
            setDead();
        }

        return true;
    }
}
