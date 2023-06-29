package net.minecraft.entity.effect;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityLightningBolt extends EntityWeatherEffect
{
    /**
     * Declares which state the lightning bolt is in. Whether it's in the air, hit the ground, etc.
     */
    private int lightningState;

    /**
     * A random long that is used to change the vertex of the lightning rendered in RenderLightningBolt
     */
    public long boltVertex;

    /**
     * Determines the time before the EntityLightningBolt is destroyed. It is a random integer decremented over time.
     */
    private int boltLivingTime;
    private final boolean effectOnly;

    public EntityLightningBolt(World worldIn, double x, double y, double z, boolean effectOnlyIn)
    {
        super(worldIn);
        setLocationAndAngles(x, y, z, 0.0F, 0.0F);
        lightningState = 2;
        boltVertex = rand.nextLong();
        boltLivingTime = rand.nextInt(3) + 1;
        effectOnly = effectOnlyIn;
        BlockPos blockpos = new BlockPos(this);

        if (!effectOnlyIn && !worldIn.isRemote && worldIn.getGameRules().getBoolean("doFireTick") && (worldIn.getDifficulty() == EnumDifficulty.NORMAL || worldIn.getDifficulty() == EnumDifficulty.HARD) && worldIn.isAreaLoaded(blockpos, 10))
        {
            if (worldIn.getBlockState(blockpos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(worldIn, blockpos))
            {
                worldIn.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
            }

            for (int i = 0; i < 4; ++i)
            {
                BlockPos blockpos1 = blockpos.add(rand.nextInt(3) - 1, rand.nextInt(3) - 1, rand.nextInt(3) - 1);

                if (worldIn.getBlockState(blockpos1).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(worldIn, blockpos1))
                {
                    worldIn.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }

    public SoundCategory getSoundCategory()
    {
        return SoundCategory.WEATHER;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (lightningState == 2)
        {
            world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + rand.nextFloat() * 0.2F);
            world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + rand.nextFloat() * 0.2F);
        }

        --lightningState;

        if (lightningState < 0)
        {
            if (boltLivingTime == 0)
            {
                setDead();
            }
            else if (lightningState < -rand.nextInt(10))
            {
                --boltLivingTime;
                lightningState = 1;

                if (!effectOnly && !world.isRemote)
                {
                    boltVertex = rand.nextLong();
                    BlockPos blockpos = new BlockPos(this);

                    if (world.getGameRules().getBoolean("doFireTick") && world.isAreaLoaded(blockpos, 10) && world.getBlockState(blockpos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(world, blockpos))
                    {
                        world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                    }
                }
            }
        }

        if (lightningState >= 0)
        {
            if (world.isRemote)
            {
                world.setLastLightningBolt(2);
            }
            else if (!effectOnly)
            {
                double d0 = 3.0D;
                List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(posX - 3.0D, posY - 3.0D, posZ - 3.0D, posX + 3.0D, posY + 6.0D + 3.0D, posZ + 3.0D));

                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity = list.get(i);
                    entity.onStruckByLightning(this);
                }
            }
        }
    }

    protected void entityInit()
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
    }
}
