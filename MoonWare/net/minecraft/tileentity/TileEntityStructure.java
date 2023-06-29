package net.minecraft.tileentity;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Namespaced;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class TileEntityStructure extends TileEntity
{
    private String name = "";
    private String author = "";
    private String metadata = "";
    private BlockPos position = new BlockPos(0, 1, 0);
    private BlockPos size = BlockPos.ORIGIN;
    private Mirror mirror = Mirror.NONE;
    private Rotation rotation = Rotation.NONE;
    private TileEntityStructure.Mode mode = TileEntityStructure.Mode.DATA;
    private boolean ignoreEntities = true;
    private boolean powered;
    private boolean showAir;
    private boolean showBoundingBox = true;
    private float integrity = 1.0F;
    private long seed;

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setString("name", name);
        compound.setString("author", author);
        compound.setString("metadata", metadata);
        compound.setInteger("posX", position.getX());
        compound.setInteger("posY", position.getY());
        compound.setInteger("posZ", position.getZ());
        compound.setInteger("sizeX", size.getX());
        compound.setInteger("sizeY", size.getY());
        compound.setInteger("sizeZ", size.getZ());
        compound.setString("rotation", rotation.toString());
        compound.setString("mirror", mirror.toString());
        compound.setString("mode", mode.toString());
        compound.setBoolean("ignoreEntities", ignoreEntities);
        compound.setBoolean("powered", powered);
        compound.setBoolean("showair", showAir);
        compound.setBoolean("showboundingbox", showBoundingBox);
        compound.setFloat("integrity", integrity);
        compound.setLong("seed", seed);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        setName(compound.getString("name"));
        author = compound.getString("author");
        metadata = compound.getString("metadata");
        int i = MathHelper.clamp(compound.getInteger("posX"), -32, 32);
        int j = MathHelper.clamp(compound.getInteger("posY"), -32, 32);
        int k = MathHelper.clamp(compound.getInteger("posZ"), -32, 32);
        position = new BlockPos(i, j, k);
        int l = MathHelper.clamp(compound.getInteger("sizeX"), 0, 32);
        int i1 = MathHelper.clamp(compound.getInteger("sizeY"), 0, 32);
        int j1 = MathHelper.clamp(compound.getInteger("sizeZ"), 0, 32);
        size = new BlockPos(l, i1, j1);

        try
        {
            rotation = Rotation.valueOf(compound.getString("rotation"));
        }
        catch (IllegalArgumentException var11)
        {
            rotation = Rotation.NONE;
        }

        try
        {
            mirror = Mirror.valueOf(compound.getString("mirror"));
        }
        catch (IllegalArgumentException var10)
        {
            mirror = Mirror.NONE;
        }

        try
        {
            mode = TileEntityStructure.Mode.valueOf(compound.getString("mode"));
        }
        catch (IllegalArgumentException var9)
        {
            mode = TileEntityStructure.Mode.DATA;
        }

        ignoreEntities = compound.getBoolean("ignoreEntities");
        powered = compound.getBoolean("powered");
        showAir = compound.getBoolean("showair");
        showBoundingBox = compound.getBoolean("showboundingbox");

        if (compound.hasKey("integrity"))
        {
            integrity = compound.getFloat("integrity");
        }
        else
        {
            integrity = 1.0F;
        }

        seed = compound.getLong("seed");
        updateBlockState();
    }

    private void updateBlockState()
    {
        if (world != null)
        {
            BlockPos blockpos = getPos();
            IBlockState iblockstate = world.getBlockState(blockpos);

            if (iblockstate.getBlock() == Blocks.STRUCTURE_BLOCK)
            {
                world.setBlockState(blockpos, iblockstate.withProperty(BlockStructure.MODE, mode), 2);
            }
        }
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, 7, getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    public boolean usedBy(EntityPlayer player)
    {
        if (!player.canUseCommandBlock())
        {
            return false;
        }
        else
        {
            if (player.getEntityWorld().isRemote)
            {
                player.openEditStructure(this);
            }

            return true;
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String nameIn)
    {
        String s = nameIn;

        for (char c0 : ChatAllowedCharacters.ILLEGAL_STRUCTURE_CHARACTERS)
        {
            s = s.replace(c0, '_');
        }

        name = s;
    }

    public void createdBy(EntityLivingBase p_189720_1_)
    {
        if (!StringUtils.isNullOrEmpty(p_189720_1_.getName()))
        {
            author = p_189720_1_.getName();
        }
    }

    public BlockPos getPosition()
    {
        return position;
    }

    public void setPosition(BlockPos posIn)
    {
        position = posIn;
    }

    public BlockPos getStructureSize()
    {
        return size;
    }

    public void setSize(BlockPos sizeIn)
    {
        size = sizeIn;
    }

    public Mirror getMirror()
    {
        return mirror;
    }

    public void setMirror(Mirror mirrorIn)
    {
        mirror = mirrorIn;
    }

    public Rotation getRotation()
    {
        return rotation;
    }

    public void setRotation(Rotation rotationIn)
    {
        rotation = rotationIn;
    }

    public String getMetadata()
    {
        return metadata;
    }

    public void setMetadata(String metadataIn)
    {
        metadata = metadataIn;
    }

    public TileEntityStructure.Mode getMode()
    {
        return mode;
    }

    public void setMode(TileEntityStructure.Mode modeIn)
    {
        mode = modeIn;
        IBlockState iblockstate = world.getBlockState(getPos());

        if (iblockstate.getBlock() == Blocks.STRUCTURE_BLOCK)
        {
            world.setBlockState(getPos(), iblockstate.withProperty(BlockStructure.MODE, modeIn), 2);
        }
    }

    public void nextMode()
    {
        switch (getMode())
        {
            case SAVE:
                setMode(TileEntityStructure.Mode.LOAD);
                break;

            case LOAD:
                setMode(TileEntityStructure.Mode.CORNER);
                break;

            case CORNER:
                setMode(TileEntityStructure.Mode.DATA);
                break;

            case DATA:
                setMode(TileEntityStructure.Mode.SAVE);
        }
    }

    public boolean ignoresEntities()
    {
        return ignoreEntities;
    }

    public void setIgnoresEntities(boolean ignoreEntitiesIn)
    {
        ignoreEntities = ignoreEntitiesIn;
    }

    public float getIntegrity()
    {
        return integrity;
    }

    public void setIntegrity(float integrityIn)
    {
        integrity = integrityIn;
    }

    public long getSeed()
    {
        return seed;
    }

    public void setSeed(long seedIn)
    {
        seed = seedIn;
    }

    public boolean detectSize()
    {
        if (mode != TileEntityStructure.Mode.SAVE)
        {
            return false;
        }
        else
        {
            BlockPos blockpos = getPos();
            int i = 80;
            BlockPos blockpos1 = new BlockPos(blockpos.getX() - 80, 0, blockpos.getZ() - 80);
            BlockPos blockpos2 = new BlockPos(blockpos.getX() + 80, 255, blockpos.getZ() + 80);
            List<TileEntityStructure> list = getNearbyCornerBlocks(blockpos1, blockpos2);
            List<TileEntityStructure> list1 = filterRelatedCornerBlocks(list);

            if (list1.size() < 1)
            {
                return false;
            }
            else
            {
                StructureBoundingBox structureboundingbox = calculateEnclosingBoundingBox(blockpos, list1);

                if (structureboundingbox.maxX - structureboundingbox.minX > 1 && structureboundingbox.maxY - structureboundingbox.minY > 1 && structureboundingbox.maxZ - structureboundingbox.minZ > 1)
                {
                    position = new BlockPos(structureboundingbox.minX - blockpos.getX() + 1, structureboundingbox.minY - blockpos.getY() + 1, structureboundingbox.minZ - blockpos.getZ() + 1);
                    size = new BlockPos(structureboundingbox.maxX - structureboundingbox.minX - 1, structureboundingbox.maxY - structureboundingbox.minY - 1, structureboundingbox.maxZ - structureboundingbox.minZ - 1);
                    markDirty();
                    IBlockState iblockstate = world.getBlockState(blockpos);
                    world.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
    }

    private List<TileEntityStructure> filterRelatedCornerBlocks(List<TileEntityStructure> p_184415_1_)
    {
        Iterable<TileEntityStructure> iterable = Iterables.filter(p_184415_1_, new Predicate<TileEntityStructure>()
        {
            public boolean apply(@Nullable TileEntityStructure p_apply_1_)
            {
                return p_apply_1_.mode == TileEntityStructure.Mode.CORNER && name.equals(p_apply_1_.name);
            }
        });
        return Lists.newArrayList(iterable);
    }

    private List<TileEntityStructure> getNearbyCornerBlocks(BlockPos p_184418_1_, BlockPos p_184418_2_)
    {
        List<TileEntityStructure> list = Lists.newArrayList();

        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(p_184418_1_, p_184418_2_))
        {
            IBlockState iblockstate = world.getBlockState(blockpos$mutableblockpos);

            if (iblockstate.getBlock() == Blocks.STRUCTURE_BLOCK)
            {
                TileEntity tileentity = world.getTileEntity(blockpos$mutableblockpos);

                if (tileentity != null && tileentity instanceof TileEntityStructure)
                {
                    list.add((TileEntityStructure)tileentity);
                }
            }
        }

        return list;
    }

    private StructureBoundingBox calculateEnclosingBoundingBox(BlockPos p_184416_1_, List<TileEntityStructure> p_184416_2_)
    {
        StructureBoundingBox structureboundingbox;

        if (p_184416_2_.size() > 1)
        {
            BlockPos blockpos = p_184416_2_.get(0).getPos();
            structureboundingbox = new StructureBoundingBox(blockpos, blockpos);
        }
        else
        {
            structureboundingbox = new StructureBoundingBox(p_184416_1_, p_184416_1_);
        }

        for (TileEntityStructure tileentitystructure : p_184416_2_)
        {
            BlockPos blockpos1 = tileentitystructure.getPos();

            if (blockpos1.getX() < structureboundingbox.minX)
            {
                structureboundingbox.minX = blockpos1.getX();
            }
            else if (blockpos1.getX() > structureboundingbox.maxX)
            {
                structureboundingbox.maxX = blockpos1.getX();
            }

            if (blockpos1.getY() < structureboundingbox.minY)
            {
                structureboundingbox.minY = blockpos1.getY();
            }
            else if (blockpos1.getY() > structureboundingbox.maxY)
            {
                structureboundingbox.maxY = blockpos1.getY();
            }

            if (blockpos1.getZ() < structureboundingbox.minZ)
            {
                structureboundingbox.minZ = blockpos1.getZ();
            }
            else if (blockpos1.getZ() > structureboundingbox.maxZ)
            {
                structureboundingbox.maxZ = blockpos1.getZ();
            }
        }

        return structureboundingbox;
    }

    public void writeCoordinates(ByteBuf buf)
    {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public boolean save()
    {
        return save(true);
    }

    public boolean save(boolean p_189712_1_)
    {
        if (mode == TileEntityStructure.Mode.SAVE && !world.isRemote && !StringUtils.isNullOrEmpty(name))
        {
            BlockPos blockpos = getPos().add(position);
            WorldServer worldserver = (WorldServer) world;
            MinecraftServer minecraftserver = world.getInstanceServer();
            TemplateManager templatemanager = worldserver.getStructureTemplateManager();
            Template template = templatemanager.getTemplate(minecraftserver, new Namespaced(name));
            template.takeBlocksFromWorld(world, blockpos, size, !ignoreEntities, Blocks.STRUCTURE_VOID);
            template.setAuthor(author);
            return !p_189712_1_ || templatemanager.writeTemplate(minecraftserver, new Namespaced(name));
        }
        else
        {
            return false;
        }
    }

    public boolean load()
    {
        return load(true);
    }

    public boolean load(boolean p_189714_1_)
    {
        if (mode == TileEntityStructure.Mode.LOAD && !world.isRemote && !StringUtils.isNullOrEmpty(name))
        {
            BlockPos blockpos = getPos();
            BlockPos blockpos1 = blockpos.add(position);
            WorldServer worldserver = (WorldServer) world;
            MinecraftServer minecraftserver = world.getInstanceServer();
            TemplateManager templatemanager = worldserver.getStructureTemplateManager();
            Template template = templatemanager.get(minecraftserver, new Namespaced(name));

            if (template == null)
            {
                return false;
            }
            else
            {
                if (!StringUtils.isNullOrEmpty(template.getAuthor()))
                {
                    author = template.getAuthor();
                }

                BlockPos blockpos2 = template.getSize();
                boolean flag = size.equals(blockpos2);

                if (!flag)
                {
                    size = blockpos2;
                    markDirty();
                    IBlockState iblockstate = world.getBlockState(blockpos);
                    world.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
                }

                if (p_189714_1_ && !flag)
                {
                    return false;
                }
                else
                {
                    PlacementSettings placementsettings = (new PlacementSettings()).setMirror(mirror).setRotation(rotation).setIgnoreEntities(ignoreEntities).setChunk(null).setReplacedBlock(null).setIgnoreStructureBlock(false);

                    if (integrity < 1.0F)
                    {
                        placementsettings.setIntegrity(MathHelper.clamp(integrity, 0.0F, 1.0F)).setSeed(Long.valueOf(seed));
                    }

                    template.addBlocksToWorldChunk(world, blockpos1, placementsettings);
                    return true;
                }
            }
        }
        else
        {
            return false;
        }
    }

    public void unloadStructure()
    {
        WorldServer worldserver = (WorldServer) world;
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        templatemanager.remove(new Namespaced(name));
    }

    public boolean isStructureLoadable()
    {
        if (mode == TileEntityStructure.Mode.LOAD && !world.isRemote)
        {
            WorldServer worldserver = (WorldServer) world;
            MinecraftServer minecraftserver = world.getInstanceServer();
            TemplateManager templatemanager = worldserver.getStructureTemplateManager();
            return templatemanager.get(minecraftserver, new Namespaced(name)) != null;
        }
        else
        {
            return false;
        }
    }

    public boolean isPowered()
    {
        return powered;
    }

    public void setPowered(boolean poweredIn)
    {
        powered = poweredIn;
    }

    public boolean showsAir()
    {
        return showAir;
    }

    public void setShowAir(boolean showAirIn)
    {
        showAir = showAirIn;
    }

    public boolean showsBoundingBox()
    {
        return showBoundingBox;
    }

    public void setShowBoundingBox(boolean showBoundingBoxIn)
    {
        showBoundingBox = showBoundingBoxIn;
    }

    @Nullable

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        return new TranslatableComponent("structure_block.hover." + mode.modeName, mode == Mode.DATA ? metadata : name);
    }

    public enum Mode implements IStringSerializable
    {
        SAVE("save", 0),
        LOAD("load", 1),
        CORNER("corner", 2),
        DATA("data", 3);

        private static final TileEntityStructure.Mode[] MODES = new TileEntityStructure.Mode[values().length];
        private final String modeName;
        private final int modeId;

        Mode(String modeNameIn, int modeIdIn)
        {
            modeName = modeNameIn;
            modeId = modeIdIn;
        }

        public String getName()
        {
            return modeName;
        }

        public int getModeId()
        {
            return modeId;
        }

        public static TileEntityStructure.Mode getById(int id)
        {
            return id >= 0 && id < MODES.length ? MODES[id] : MODES[0];
        }

        static {
            for (TileEntityStructure.Mode tileentitystructure$mode : values())
            {
                MODES[tileentitystructure$mode.getModeId()] = tileentitystructure$mode;
            }
        }
    }
}
