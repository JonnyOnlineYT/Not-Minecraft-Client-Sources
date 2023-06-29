package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Namespaced;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFallingBlock extends Entity
{
    private IBlockState fallTile;
    public int fallTime;
    public boolean shouldDropItem = true;
    private boolean canSetAsBlock;
    private boolean hurtEntities;
    private int fallHurtMax = 40;
    private float fallHurtAmount = 2.0F;
    public NBTTagCompound tileEntityData;
    protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityFallingBlock.class, DataSerializers.BLOCK_POS);

    public EntityFallingBlock(World worldIn)
    {
        super(worldIn);
    }

    public EntityFallingBlock(World worldIn, double x, double y, double z, IBlockState fallingBlockState)
    {
        super(worldIn);
        fallTile = fallingBlockState;
        preventEntitySpawning = true;
        setSize(0.98F, 0.98F);
        setPosition(x, y + (double)((1.0F - height) / 2.0F), z);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
        setOrigin(new BlockPos(this));
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }

    public void setOrigin(BlockPos p_184530_1_)
    {
        dataManager.set(ORIGIN, p_184530_1_);
    }

    public BlockPos getOrigin()
    {
        return dataManager.get(ORIGIN);
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
        dataManager.register(ORIGIN, BlockPos.ORIGIN);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        Block block = fallTile.getBlock();

        if (fallTile.getMaterial() == Material.AIR)
        {
            setDead();
        }
        else
        {
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;

            if (fallTime++ == 0)
            {
                BlockPos blockpos = new BlockPos(this);

                if (world.getBlockState(blockpos).getBlock() == block)
                {
                    world.setBlockToAir(blockpos);
                }
                else if (!world.isRemote)
                {
                    setDead();
                    return;
                }
            }

            if (!hasNoGravity())
            {
                motionY -= 0.03999999910593033D;
            }

            moveEntity(MoverType.SELF, motionX, motionY, motionZ);

            if (!world.isRemote)
            {
                BlockPos blockpos1 = new BlockPos(this);
                boolean flag = fallTile.getBlock() == Blocks.field_192444_dS;
                boolean flag1 = flag && world.getBlockState(blockpos1).getMaterial() == Material.WATER;
                double d0 = motionX * motionX + motionY * motionY + motionZ * motionZ;

                if (flag && d0 > 1.0D)
                {
                    RayTraceResult raytraceresult = world.rayTraceBlocks(new Vec3d(prevPosX, prevPosY, prevPosZ), new Vec3d(posX, posY, posZ), true);

                    if (raytraceresult != null && world.getBlockState(raytraceresult.getBlockPos()).getMaterial() == Material.WATER)
                    {
                        blockpos1 = raytraceresult.getBlockPos();
                        flag1 = true;
                    }
                }

                if (!onGround && !flag1)
                {
                    if (fallTime > 100 && !world.isRemote && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || fallTime > 600)
                    {
                        if (shouldDropItem && world.getGameRules().getBoolean("doEntityDrops"))
                        {
                            entityDropItem(new ItemStack(block, 1, block.damageDropped(fallTile)), 0.0F);
                        }

                        setDead();
                    }
                }
                else
                {
                    IBlockState iblockstate = world.getBlockState(blockpos1);

                    if (!flag1 && BlockFalling.canFallThrough(world.getBlockState(new BlockPos(posX, posY - 0.009999999776482582D, posZ))))
                    {
                        onGround = false;
                        return;
                    }

                    motionX *= 0.699999988079071D;
                    motionZ *= 0.699999988079071D;
                    motionY *= -0.5D;

                    if (iblockstate.getBlock() != Blocks.PISTON_EXTENSION)
                    {
                        setDead();

                        if (!canSetAsBlock)
                        {
                            if (world.mayPlace(block, blockpos1, true, EnumFacing.UP, null) && (flag1 || !BlockFalling.canFallThrough(world.getBlockState(blockpos1.down()))) && world.setBlockState(blockpos1, fallTile, 3))
                            {
                                if (block instanceof BlockFalling)
                                {
                                    ((BlockFalling)block).onEndFalling(world, blockpos1, fallTile, iblockstate);
                                }

                                if (tileEntityData != null && block instanceof ITileEntityProvider)
                                {
                                    TileEntity tileentity = world.getTileEntity(blockpos1);

                                    if (tileentity != null)
                                    {
                                        NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());

                                        for (String s : tileEntityData.getKeySet())
                                        {
                                            NBTBase nbtbase = tileEntityData.getTag(s);

                                            if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s))
                                            {
                                                nbttagcompound.setTag(s, nbtbase.copy());
                                            }
                                        }

                                        tileentity.readFromNBT(nbttagcompound);
                                        tileentity.markDirty();
                                    }
                                }
                            }
                            else if (shouldDropItem && world.getGameRules().getBoolean("doEntityDrops"))
                            {
                                entityDropItem(new ItemStack(block, 1, block.damageDropped(fallTile)), 0.0F);
                            }
                        }
                        else if (block instanceof BlockFalling)
                        {
                            ((BlockFalling)block).func_190974_b(world, blockpos1);
                        }
                    }
                }
            }

            motionX *= 0.9800000190734863D;
            motionY *= 0.9800000190734863D;
            motionZ *= 0.9800000190734863D;
        }
    }

    public void fall(float distance, float damageMultiplier)
    {
        Block block = fallTile.getBlock();

        if (hurtEntities)
        {
            int i = MathHelper.ceil(distance - 1.0F);

            if (i > 0)
            {
                List<Entity> list = Lists.newArrayList(world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox()));
                boolean flag = block == Blocks.ANVIL;
                DamageSource damagesource = flag ? DamageSource.anvil : DamageSource.fallingBlock;

                for (Entity entity : list)
                {
                    entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor((float)i * fallHurtAmount), fallHurtMax));
                }

                if (flag && (double) rand.nextFloat() < 0.05000000074505806D + (double)i * 0.05D)
                {
                    int j = fallTile.getValue(BlockAnvil.DAMAGE).intValue();
                    ++j;

                    if (j > 2)
                    {
                        canSetAsBlock = true;
                    }
                    else
                    {
                        fallTile = fallTile.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(j));
                    }
                }
            }
        }
    }

    public static void registerFixesFallingBlock(DataFixer fixer)
    {
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        Block block = fallTile != null ? fallTile.getBlock() : Blocks.AIR;
        Namespaced resourcelocation = Block.REGISTRY.getNameForObject(block);
        compound.setString("Block", resourcelocation == null ? "" : resourcelocation.toString());
        compound.setByte("Data", (byte)block.getMetaFromState(fallTile));
        compound.setInteger("Time", fallTime);
        compound.setBoolean("DropItem", shouldDropItem);
        compound.setBoolean("HurtEntities", hurtEntities);
        compound.setFloat("FallHurtAmount", fallHurtAmount);
        compound.setInteger("FallHurtMax", fallHurtMax);

        if (tileEntityData != null)
        {
            compound.setTag("TileEntityData", tileEntityData);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        int i = compound.getByte("Data") & 255;

        if (compound.hasKey("Block", 8))
        {
            fallTile = Block.getBlockFromName(compound.getString("Block")).getStateFromMeta(i);
        }
        else if (compound.hasKey("TileID", 99))
        {
            fallTile = Block.getBlockById(compound.getInteger("TileID")).getStateFromMeta(i);
        }
        else
        {
            fallTile = Block.getBlockById(compound.getByte("Tile") & 255).getStateFromMeta(i);
        }

        fallTime = compound.getInteger("Time");
        Block block = fallTile.getBlock();

        if (compound.hasKey("HurtEntities", 99))
        {
            hurtEntities = compound.getBoolean("HurtEntities");
            fallHurtAmount = compound.getFloat("FallHurtAmount");
            fallHurtMax = compound.getInteger("FallHurtMax");
        }
        else if (block == Blocks.ANVIL)
        {
            hurtEntities = true;
        }

        if (compound.hasKey("DropItem", 99))
        {
            shouldDropItem = compound.getBoolean("DropItem");
        }

        if (compound.hasKey("TileEntityData", 10))
        {
            tileEntityData = compound.getCompoundTag("TileEntityData");
        }

        if (block == null || block.getDefaultState().getMaterial() == Material.AIR)
        {
            fallTile = Blocks.SAND.getDefaultState();
        }
    }

    public World getWorldObj()
    {
        return world;
    }

    public void setHurtEntities(boolean p_145806_1_)
    {
        hurtEntities = p_145806_1_;
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    public boolean canRenderOnFire()
    {
        return false;
    }

    public void addEntityCrashInfo(CrashReportCategory category)
    {
        super.addEntityCrashInfo(category);

        if (fallTile != null)
        {
            Block block = fallTile.getBlock();
            category.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(block)));
            category.addCrashSection("Immitating block data", Integer.valueOf(block.getMetaFromState(fallTile)));
        }
    }

    @Nullable
    public IBlockState getBlock()
    {
        return fallTile;
    }

    public boolean ignoreItemEntityData()
    {
        return true;
    }
}
