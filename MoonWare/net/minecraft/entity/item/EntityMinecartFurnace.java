package net.minecraft.entity.item;

import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityMinecartFurnace extends EntityMinecart
{
    private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(EntityMinecartFurnace.class, DataSerializers.BOOLEAN);
    private int fuel;
    public double pushX;
    public double pushZ;

    public EntityMinecartFurnace(World worldIn)
    {
        super(worldIn);
    }

    public EntityMinecartFurnace(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static void registerFixesMinecartFurnace(DataFixer fixer)
    {
        EntityMinecart.registerFixesMinecart(fixer, EntityMinecartFurnace.class);
    }

    public EntityMinecart.Type getType()
    {
        return EntityMinecart.Type.FURNACE;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(POWERED, Boolean.valueOf(false));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (fuel > 0)
        {
            --fuel;
        }

        if (fuel <= 0)
        {
            pushX = 0.0D;
            pushZ = 0.0D;
        }

        setMinecartPowered(fuel > 0);

        if (isMinecartPowered() && rand.nextInt(4) == 0)
        {
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY + 0.8D, posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * Get's the maximum speed for a minecart
     */
    protected double getMaximumSpeed()
    {
        return 0.2D;
    }

    public void killMinecart(DamageSource source)
    {
        super.killMinecart(source);

        if (!source.isExplosion() && world.getGameRules().getBoolean("doEntityDrops"))
        {
            entityDropItem(new ItemStack(Blocks.FURNACE, 1), 0.0F);
        }
    }

    protected void moveAlongTrack(BlockPos pos, IBlockState state)
    {
        super.moveAlongTrack(pos, state);
        double d0 = pushX * pushX + pushZ * pushZ;

        if (d0 > 1.0E-4D && motionX * motionX + motionZ * motionZ > 0.001D)
        {
            d0 = MathHelper.sqrt(d0);
            pushX /= d0;
            pushZ /= d0;

            if (pushX * motionX + pushZ * motionZ < 0.0D)
            {
                pushX = 0.0D;
                pushZ = 0.0D;
            }
            else
            {
                double d1 = d0 / getMaximumSpeed();
                pushX *= d1;
                pushZ *= d1;
            }
        }
    }

    protected void applyDrag()
    {
        double d0 = pushX * pushX + pushZ * pushZ;

        if (d0 > 1.0E-4D)
        {
            d0 = MathHelper.sqrt(d0);
            pushX /= d0;
            pushZ /= d0;
            double d1 = 1.0D;
            motionX *= 0.800000011920929D;
            motionY *= 0.0D;
            motionZ *= 0.800000011920929D;
            motionX += pushX * 1.0D;
            motionZ += pushZ * 1.0D;
        }
        else
        {
            motionX *= 0.9800000190734863D;
            motionY *= 0.0D;
            motionZ *= 0.9800000190734863D;
        }

        super.applyDrag();
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand stack)
    {
        ItemStack itemstack = player.getHeldItem(stack);

        if (itemstack.getItem() == Items.COAL && fuel + 3600 <= 32000)
        {
            if (!player.capabilities.isCreativeMode)
            {
                itemstack.func_190918_g(1);
            }

            fuel += 3600;
        }

        pushX = posX - player.posX;
        pushZ = posZ - player.posZ;
        return true;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setDouble("PushX", pushX);
        compound.setDouble("PushZ", pushZ);
        compound.setShort("Fuel", (short) fuel);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        pushX = compound.getDouble("PushX");
        pushZ = compound.getDouble("PushZ");
        fuel = compound.getShort("Fuel");
    }

    protected boolean isMinecartPowered()
    {
        return dataManager.get(POWERED).booleanValue();
    }

    protected void setMinecartPowered(boolean p_94107_1_)
    {
        dataManager.set(POWERED, Boolean.valueOf(p_94107_1_));
    }

    public IBlockState getDefaultDisplayTile()
    {
        return (isMinecartPowered() ? Blocks.LIT_FURNACE : Blocks.FURNACE).getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH);
    }
}
