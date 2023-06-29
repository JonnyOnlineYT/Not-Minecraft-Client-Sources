package net.minecraft.entity.passive;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals
{
    protected Block spawnableBlock = Blocks.GRASS;
    private int inLove;
    private UUID playerInLove;

    public EntityAnimal(World worldIn)
    {
        super(worldIn);
    }

    protected void updateAITasks()
    {
        if (getGrowingAge() != 0)
        {
            inLove = 0;
        }

        super.updateAITasks();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (getGrowingAge() != 0)
        {
            inLove = 0;
        }

        if (inLove > 0)
        {
            --inLove;

            if (inLove % 10 == 0)
            {
                double d0 = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.spawnParticle(EnumParticleTypes.HEART, posX + (double)(rand.nextFloat() * width * 2.0F) - (double) width, posY + 0.5D + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double) width, d0, d1, d2);
            }
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
        else
        {
            inLove = 0;
            return super.attackEntityFrom(source, amount);
        }
    }

    public float getBlockPathWeight(BlockPos pos)
    {
        return world.getBlockState(pos.down()).getBlock() == spawnableBlock ? 10.0F : world.getLightBrightness(pos) - 0.5F;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("InLove", inLove);

        if (playerInLove != null)
        {
            compound.setUniqueId("LoveCause", playerInLove);
        }
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        return 0.14D;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        inLove = compound.getInteger("InLove");
        playerInLove = compound.hasUniqueId("LoveCause") ? compound.getUniqueId("LoveCause") : null;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getEntityBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        return world.getBlockState(blockpos.down()).getBlock() == spawnableBlock && world.getLight(blockpos) > 8 && super.getCanSpawnHere();
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval()
    {
        return 120;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return false;
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer player)
    {
        return 1 + world.rand.nextInt(3);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack.getItem() == Items.WHEAT;
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty())
        {
            if (isBreedingItem(itemstack) && getGrowingAge() == 0 && inLove <= 0)
            {
                consumeItemFromStack(player, itemstack);
                setInLove(player);
                return true;
            }

            if (isChild() && isBreedingItem(itemstack))
            {
                consumeItemFromStack(player, itemstack);
                ageUp((int)((float)(-getGrowingAge() / 20) * 0.1F), true);
                return true;
            }
        }

        return super.processInteract(player, hand);
    }

    /**
     * Decreases ItemStack size by one
     */
    protected void consumeItemFromStack(EntityPlayer player, ItemStack stack)
    {
        if (!player.capabilities.isCreativeMode)
        {
            stack.func_190918_g(1);
        }
    }

    public void setInLove(@Nullable EntityPlayer player)
    {
        inLove = 600;

        if (player != null)
        {
            playerInLove = player.getUniqueID();
        }

        world.setEntityState(this, (byte)18);
    }

    @Nullable
    public EntityPlayerMP func_191993_do()
    {
        if (playerInLove == null)
        {
            return null;
        }
        else
        {
            EntityPlayer entityplayer = world.getPlayerEntityByUUID(playerInLove);
            return entityplayer instanceof EntityPlayerMP ? (EntityPlayerMP)entityplayer : null;
        }
    }

    /**
     * Returns if the entity is currently in 'love mode'.
     */
    public boolean isInLove()
    {
        return inLove > 0;
    }

    public void resetInLove()
    {
        inLove = 0;
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        if (otherAnimal == this)
        {
            return false;
        }
        else if (otherAnimal.getClass() != getClass())
        {
            return false;
        }
        else
        {
            return isInLove() && otherAnimal.isInLove();
        }
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 18)
        {
            for (int i = 0; i < 7; ++i)
            {
                double d0 = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.spawnParticle(EnumParticleTypes.HEART, posX + (double)(rand.nextFloat() * width * 2.0F) - (double) width, posY + 0.5D + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double) width, d0, d1, d2);
            }
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }
}
