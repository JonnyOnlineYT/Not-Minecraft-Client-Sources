package net.minecraft.entity.item;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityXPOrb extends Entity
{
    /**
     * A constantly increasing value that RenderXPOrb uses to control the colour shifting (Green / yellow)
     */
    public int xpColor;

    /** The age of the XP orb in ticks. */
    public int xpOrbAge;
    public int delayBeforeCanPickup;

    /** The health of this XP orb. */
    private int xpOrbHealth = 5;

    /** This is how much XP this orb has. */
    private int xpValue;

    /** The closest EntityPlayer to this orb. */
    private EntityPlayer closestPlayer;

    /** Threshold color for tracking players */
    private int xpTargetColor;

    public EntityXPOrb(World worldIn, double x, double y, double z, int expValue)
    {
        super(worldIn);
        setSize(0.5F, 0.5F);
        setPosition(x, y, z);
        rotationYaw = (float)(Math.random() * 360.0D);
        motionX = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F;
        motionY = (float)(Math.random() * 0.2D) * 2.0F;
        motionZ = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F;
        xpValue = expValue;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public EntityXPOrb(World worldIn)
    {
        super(worldIn);
        setSize(0.25F, 0.25F);
    }

    protected void entityInit()
    {
    }

    public int getBrightnessForRender()
    {
        float f = 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightnessForRender();
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int)(f * 15.0F * 16.0F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (delayBeforeCanPickup > 0)
        {
            --delayBeforeCanPickup;
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (!hasNoGravity())
        {
            motionY -= 0.029999999329447746D;
        }

        if (world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA)
        {
            motionY = 0.20000000298023224D;
            motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
            motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
            playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + rand.nextFloat() * 0.4F);
        }

        pushOutOfBlocks(posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, posZ);
        double d0 = 8.0D;

        if (xpTargetColor < xpColor - 20 + getEntityId() % 100)
        {
            if (closestPlayer == null || closestPlayer.getDistanceSqToEntity(this) > 64.0D)
            {
                closestPlayer = world.getClosestPlayerToEntity(this, 8.0D);
            }

            xpTargetColor = xpColor;
        }

        if (closestPlayer != null && closestPlayer.isSpectator())
        {
            closestPlayer = null;
        }

        if (closestPlayer != null)
        {
            double d1 = (closestPlayer.posX - posX) / 8.0D;
            double d2 = (closestPlayer.posY + (double) closestPlayer.getEyeHeight() / 2.0D - posY) / 8.0D;
            double d3 = (closestPlayer.posZ - posZ) / 8.0D;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;

            if (d5 > 0.0D)
            {
                d5 = d5 * d5;
                motionX += d1 / d4 * d5 * 0.1D;
                motionY += d2 / d4 * d5 * 0.1D;
                motionZ += d3 / d4 * d5 * 0.1D;
            }
        }

        moveEntity(MoverType.SELF, motionX, motionY, motionZ);
        float f = 0.98F;

        if (onGround)
        {
            f = world.getBlockState(new BlockPos(MathHelper.floor(posX), MathHelper.floor(getEntityBoundingBox().minY) - 1, MathHelper.floor(posZ))).getBlock().slipperiness * 0.98F;
        }

        motionX *= f;
        motionY *= 0.9800000190734863D;
        motionZ *= f;

        if (onGround)
        {
            motionY *= -0.8999999761581421D;
        }

        ++xpColor;
        ++xpOrbAge;

        if (xpOrbAge >= 6000)
        {
            setDead();
        }
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        return world.handleMaterialAcceleration(getEntityBoundingBox(), Material.WATER, this);
    }

    /**
     * Will deal the specified amount of fire damage to the entity if the entity isn't immune to fire damage.
     */
    protected void dealFireDamage(int amount)
    {
        attackEntityFrom(DamageSource.inFire, (float)amount);
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
            setBeenAttacked();
            xpOrbHealth = (int)((float) xpOrbHealth - amount);

            if (xpOrbHealth <= 0)
            {
                setDead();
            }

            return false;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setShort("Health", (short) xpOrbHealth);
        compound.setShort("Age", (short) xpOrbAge);
        compound.setShort("Value", (short) xpValue);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        xpOrbHealth = compound.getShort("Health");
        xpOrbAge = compound.getShort("Age");
        xpValue = compound.getShort("Value");
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (!world.isRemote)
        {
            if (delayBeforeCanPickup == 0 && entityIn.xpCooldown == 0)
            {
                entityIn.xpCooldown = 2;
                entityIn.onItemPickup(this, 1);
                ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, entityIn);

                if (!itemstack.isEmpty() && itemstack.isItemDamaged())
                {
                    int i = Math.min(xpToDurability(xpValue), itemstack.getItemDamage());
                    xpValue -= durabilityToXp(i);
                    itemstack.setItemDamage(itemstack.getItemDamage() - i);
                }

                if (xpValue > 0)
                {
                    entityIn.addExperience(xpValue);
                }

                setDead();
            }
        }
    }

    private int durabilityToXp(int durability)
    {
        return durability / 2;
    }

    private int xpToDurability(int xp)
    {
        return xp * 2;
    }

    /**
     * Returns the XP value of this XP orb.
     */
    public int getXpValue()
    {
        return xpValue;
    }

    /**
     * Returns a number from 1 to 10 based on how much XP this orb is worth. This is used by RenderXPOrb to determine
     * what texture to use.
     */
    public int getTextureByXP()
    {
        if (xpValue >= 2477)
        {
            return 10;
        }
        else if (xpValue >= 1237)
        {
            return 9;
        }
        else if (xpValue >= 617)
        {
            return 8;
        }
        else if (xpValue >= 307)
        {
            return 7;
        }
        else if (xpValue >= 149)
        {
            return 6;
        }
        else if (xpValue >= 73)
        {
            return 5;
        }
        else if (xpValue >= 37)
        {
            return 4;
        }
        else if (xpValue >= 17)
        {
            return 3;
        }
        else if (xpValue >= 7)
        {
            return 2;
        }
        else
        {
            return xpValue >= 3 ? 1 : 0;
        }
    }

    /**
     * Get a fragment of the maximum experience points value for the supplied value of experience points value.
     */
    public static int getXPSplit(int expValue)
    {
        if (expValue >= 2477)
        {
            return 2477;
        }
        else if (expValue >= 1237)
        {
            return 1237;
        }
        else if (expValue >= 617)
        {
            return 617;
        }
        else if (expValue >= 307)
        {
            return 307;
        }
        else if (expValue >= 149)
        {
            return 149;
        }
        else if (expValue >= 73)
        {
            return 73;
        }
        else if (expValue >= 37)
        {
            return 37;
        }
        else if (expValue >= 17)
        {
            return 17;
        }
        else if (expValue >= 7)
        {
            return 7;
        }
        else
        {
            return expValue >= 3 ? 3 : 1;
        }
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }
}
