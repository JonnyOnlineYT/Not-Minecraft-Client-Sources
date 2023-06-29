package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Namespaced;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.Component;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TileEntity
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final RegistryNamespaced <Namespaced, Class <? extends TileEntity >> field_190562_f = new RegistryNamespaced <Namespaced, Class <? extends TileEntity >> ();

    /** the instance of the world the tile entity is in. */
    protected World world;
    protected BlockPos pos = BlockPos.ORIGIN;
    protected boolean tileEntityInvalid;
    private int blockMetadata = -1;

    /** the Block type that this TileEntity is contained within */
    protected Block blockType;

    private static void func_190560_a(String p_190560_0_, Class <? extends TileEntity > p_190560_1_)
    {
        field_190562_f.putObject(new Namespaced(p_190560_0_), p_190560_1_);
    }

    @Nullable
    public static Namespaced func_190559_a(Class <? extends TileEntity > p_190559_0_)
    {
        return field_190562_f.getNameForObject(p_190559_0_);
    }

    /**
     * Returns the worldObj for this tileEntity.
     */
    public World getWorld()
    {
        return world;
    }

    /**
     * Sets the worldObj for this tileEntity.
     */
    public void setWorldObj(World worldIn)
    {
        world = worldIn;
    }

    /**
     * Returns true if the worldObj isn't null.
     */
    public boolean hasWorldObj()
    {
        return world != null;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        return writeInternal(compound);
    }

    private NBTTagCompound writeInternal(NBTTagCompound compound)
    {
        Namespaced resourcelocation = field_190562_f.getNameForObject(getClass());

        if (resourcelocation == null)
        {
            throw new RuntimeException(getClass() + " is missing a mapping! This is a bug!");
        }
        else
        {
            compound.setString("id", resourcelocation.toString());
            compound.setInteger("x", pos.getX());
            compound.setInteger("y", pos.getY());
            compound.setInteger("z", pos.getZ());
            return compound;
        }
    }

    @Nullable
    public static TileEntity create(World worldIn, NBTTagCompound compound)
    {
        TileEntity tileentity = null;
        String s = compound.getString("id");

        try
        {
            Class <? extends TileEntity > oclass = field_190562_f.getObject(new Namespaced(s));

            if (oclass != null)
            {
                tileentity = oclass.newInstance();
            }
        }
        catch (Throwable throwable1)
        {
            LOGGER.error("Failed to create block entity {}", s, throwable1);
        }

        if (tileentity != null)
        {
            try
            {
                tileentity.setWorldCreate(worldIn);
                tileentity.readFromNBT(compound);
            }
            catch (Throwable throwable)
            {
                LOGGER.error("Failed to load data for block entity {}", s, throwable);
                tileentity = null;
            }
        }
        else
        {
            LOGGER.warn("Skipping BlockEntity with id {}", s);
        }

        return tileentity;
    }

    protected void setWorldCreate(World worldIn)
    {
    }

    public int getBlockMetadata()
    {
        if (blockMetadata == -1)
        {
            IBlockState iblockstate = world.getBlockState(pos);
            blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
        }

        return blockMetadata;
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
        if (world != null)
        {
            IBlockState iblockstate = world.getBlockState(pos);
            blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
            world.markChunkDirty(pos, this);

            if (getBlockType() != Blocks.AIR)
            {
                world.updateComparatorOutputLevel(pos, getBlockType());
            }
        }
    }

    /**
     * Returns the square of the distance between this entity and the passed in coordinates.
     */
    public double getDistanceSq(double x, double y, double z)
    {
        double d0 = (double) pos.getX() + 0.5D - x;
        double d1 = (double) pos.getY() + 0.5D - y;
        double d2 = (double) pos.getZ() + 0.5D - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double getMaxRenderDistanceSquared()
    {
        return 4096.0D;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    /**
     * Gets the block type at the location of this entity (client-only).
     */
    public Block getBlockType()
    {
        if (blockType == null && world != null)
        {
            blockType = world.getBlockState(pos).getBlock();
        }

        return blockType;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return null;
    }

    public NBTTagCompound getUpdateTag()
    {
        return writeInternal(new NBTTagCompound());
    }

    public boolean isInvalid()
    {
        return tileEntityInvalid;
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate()
    {
        tileEntityInvalid = true;
    }

    /**
     * validates a tile entity
     */
    public void validate()
    {
        tileEntityInvalid = false;
    }

    public boolean receiveClientEvent(int id, int type)
    {
        return false;
    }

    public void updateContainingBlockInfo()
    {
        blockType = null;
        blockMetadata = -1;
    }

    public void addInfoToCrashReport(CrashReportCategory reportCategory)
    {
        reportCategory.setDetail("Name", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return field_190562_f.getNameForObject(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
            }
        });

        if (world != null)
        {
            CrashReportCategory.addBlockInfo(reportCategory, pos, getBlockType(), getBlockMetadata());
            reportCategory.setDetail("Actual block type", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    int i = Block.getIdFromBlock(world.getBlockState(pos).getBlock());

                    try
                    {
                        return String.format("ID #%d (%s // %s)", i, Block.getBlockById(i).getUnlocalizedName(), Block.getBlockById(i).getClass().getCanonicalName());
                    }
                    catch (Throwable var3)
                    {
                        return "ID #" + i;
                    }
                }
            });
            reportCategory.setDetail("Actual block data value", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    IBlockState iblockstate = world.getBlockState(pos);
                    int i = iblockstate.getBlock().getMetaFromState(iblockstate);

                    if (i < 0)
                    {
                        return "Unknown? (Got " + i + ")";
                    }
                    else
                    {
                        String s = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
                        return String.format("%1$d / 0x%1$X / 0b%2$s", i, s);
                    }
                }
            });
        }
    }

    public void setPos(BlockPos posIn)
    {
        pos = posIn.toImmutable();
    }

    public boolean onlyOpsCanSetNbt()
    {
        return false;
    }

    @Nullable

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        return null;
    }

    public void rotate(Rotation p_189667_1_)
    {
    }

    public void mirror(Mirror p_189668_1_)
    {
    }

    static
    {
        func_190560_a("furnace", TileEntityFurnace.class);
        func_190560_a("chest", TileEntityChest.class);
        func_190560_a("ender_chest", TileEntityEnderChest.class);
        func_190560_a("jukebox", BlockJukebox.TileEntityJukebox.class);
        func_190560_a("dispenser", TileEntityDispenser.class);
        func_190560_a("dropper", TileEntityDropper.class);
        func_190560_a("sign", TileEntitySign.class);
        func_190560_a("mob_spawner", TileEntityMobSpawner.class);
        func_190560_a("noteblock", TileEntityNote.class);
        func_190560_a("piston", TileEntityPiston.class);
        func_190560_a("brewing_stand", TileEntityBrewingStand.class);
        func_190560_a("enchanting_table", TileEntityEnchantmentTable.class);
        func_190560_a("end_portal", TileEntityEndPortal.class);
        func_190560_a("beacon", TileEntityBeacon.class);
        func_190560_a("skull", TileEntitySkull.class);
        func_190560_a("daylight_detector", TileEntityDaylightDetector.class);
        func_190560_a("hopper", TileEntityHopper.class);
        func_190560_a("comparator", TileEntityComparator.class);
        func_190560_a("flower_pot", TileEntityFlowerPot.class);
        func_190560_a("banner", TileEntityBanner.class);
        func_190560_a("structure_block", TileEntityStructure.class);
        func_190560_a("end_gateway", TileEntityEndGateway.class);
        func_190560_a("command_block", TileEntityCommandBlock.class);
        func_190560_a("shulker_box", TileEntityShulkerBox.class);
        func_190560_a("bed", TileEntityBed.class);
    }
}
