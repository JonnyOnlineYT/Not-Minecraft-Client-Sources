package net.minecraft.world;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObserver;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.pathfinding.PathWorldListener;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.loot.LootTableManager;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.render.EventRenderWorldLight;

public abstract class World implements IBlockAccess
{
    private int seaLevel = 63;

    /**
     * boolean; if true updates scheduled by scheduleBlockUpdate happen immediately
     */
    protected boolean scheduledUpdatesAreImmediate;
    public final List<Entity> loadedEntityList = Lists.newArrayList();
    protected final List<Entity> unloadedEntityList = Lists.newArrayList();
    public final List<TileEntity> loadedTileEntityList = Lists.newArrayList();
    public final List<TileEntity> tickableTileEntities = Lists.newArrayList();
    private final List<TileEntity> addedTileEntityList = Lists.newArrayList();
    private final List<TileEntity> tileEntitiesToBeRemoved = Lists.newArrayList();
    public final List<EntityPlayer> playerEntities = Lists.newArrayList();
    public final List<Entity> weatherEffects = Lists.newArrayList();
    protected final IntHashMap<Entity> entitiesById = new IntHashMap<Entity>();
    private final long cloudColour = 16777215L;

    /** How much light is subtracted from full daylight */
    private int skylightSubtracted;

    /**
     * Contains the current Linear Congruential Generator seed for block updates. Used with an A value of 3 and a C
     * value of 0x3c6ef35f, producing a highly planar series of values ill-suited for choosing random blocks in a
     * 16x128x16 field.
     */
    protected int updateLCG = (new Random()).nextInt();

    /**
     * magic number used to generate fast random numbers for 3d distribution within a chunk
     */
    protected final int DIST_HASH_MAGIC = 1013904223;
    protected float prevRainingStrength;
    protected float rainingStrength;
    protected float prevThunderingStrength;
    protected float thunderingStrength;

    /**
     * Set to 2 whenever a lightning bolt is generated in SSP. Decrements if > 0 in updateWeather(). Value appears to be
     * unused.
     */
    private int lastLightningBolt;

    /** RNG for World. */
    public final Random rand = new Random();

    /** The WorldProvider instance that World uses. */
    public final WorldProvider provider;
    protected PathWorldListener pathListener = new PathWorldListener();
    protected List<IWorldEventListener> eventListeners;

    /** Handles chunk operations and caching */
    protected IChunkProvider chunkProvider;
    protected final ISaveHandler saveHandler;

    /**
     * holds information about a world (size on disk, time, spawn point, seed, ...)
     */
    protected WorldInfo worldInfo;

    /**
     * if set, this flag forces a request to load a chunk to load the chunk rather than defaulting to the world's
     * chunkprovider's dummy if possible
     */
    protected boolean findingSpawnPoint;
    protected MapStorage mapStorage;
    protected VillageCollection villageCollectionObj;
    protected LootTableManager lootTable;
    protected AdvancementManager field_191951_C;
    protected FunctionManager field_193036_D;
    public final Profiler theProfiler;
    private final Calendar theCalendar;
    protected Scoreboard worldScoreboard;

    /**
     * True if the world is a 'slave' client; changes will not be saved or propagated from this world. For example,
     * server worlds have this set to false, client worlds have this set to true.
     */
    public final boolean isRemote;

    /** indicates if enemies are spawned or not */
    protected boolean spawnHostileMobs;

    /** A flag indicating whether we should spawn peaceful mobs. */
    protected boolean spawnPeacefulMobs;
    private boolean processingLoadedTiles;
    private final WorldBorder worldBorder;

    /**
     * is a temporary list of blocks and light values used when updating light levels. Holds up to 32x32x32 blocks (the
     * maximum influence of a light source.) Every element is a packed bit value: 0000000000LLLLzzzzzzyyyyyyxxxxxx. The
     * 4-bit L is a light level used when darkening blocks. 6-bit numbers x, y and z represent the block's offset from
     * the original block, plus 32 (i.e. value of 31 would mean a -1 offset
     */
    int[] lightUpdateBlockList;

    protected World(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client)
    {
        eventListeners = Lists.newArrayList(pathListener);
        theCalendar = Calendar.getInstance();
        worldScoreboard = new Scoreboard();
        spawnHostileMobs = true;
        spawnPeacefulMobs = true;
        lightUpdateBlockList = new int[32768];
        saveHandler = saveHandlerIn;
        theProfiler = profilerIn;
        worldInfo = info;
        provider = providerIn;
        isRemote = client;
        worldBorder = providerIn.createWorldBorder();
    }

    public World init()
    {
        return this;
    }

    public Biome getBiome(BlockPos pos)
    {
        if (isBlockLoaded(pos))
        {
            Chunk chunk = getChunkFromBlockCoords(pos);

            try
            {
                return chunk.getBiome(pos, provider.getBiomeProvider());
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting biome");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Coordinates of biome request");
                crashreportcategory.setDetail("Location", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return CrashReportCategory.getCoordinateInfo(pos);
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        else
        {
            return provider.getBiomeProvider().getBiome(pos, Biomes.PLAINS);
        }
    }

    public BiomeProvider getBiomeProvider()
    {
        return provider.getBiomeProvider();
    }

    /**
     * Creates the chunk provider for this world. Called in the constructor. Retrieves provider from worldProvider?
     */
    protected abstract IChunkProvider createChunkProvider();

    public void initialize(WorldSettings settings)
    {
        worldInfo.setServerInitialized(true);
    }

    @Nullable
    public MinecraftServer getInstanceServer()
    {
        return null;
    }

    /**
     * Sets a new spawn location by finding an uncovered block at a random (x,z) location in the chunk.
     */
    public void setInitialSpawnLocation()
    {
        setSpawnPoint(new BlockPos(8, 64, 8));
    }

    public IBlockState getGroundAboveSeaLevel(BlockPos pos)
    {
        BlockPos blockpos;

        for (blockpos = new BlockPos(pos.getX(), getSeaLevel(), pos.getZ()); !isAirBlock(blockpos.up()); blockpos = blockpos.up())
        {
        }

        return getBlockState(blockpos);
    }

    /**
     * Check if the given BlockPos has valid coordinates
     */
    private boolean isValid(BlockPos pos)
    {
        return !isOutsideBuildHeight(pos) && pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000;
    }

    private boolean isOutsideBuildHeight(BlockPos pos)
    {
        return pos.getY() < 0 || pos.getY() >= 256;
    }

    /**
     * Checks to see if an air block exists at the provided location. Note that this only checks to see if the blocks
     * material is set to air, meaning it is possible for non-vanilla blocks to still pass this check.
     */
    public boolean isAirBlock(BlockPos pos)
    {
        return getBlockState(pos).getMaterial() == Material.AIR;
    }

    public boolean isBlockLoaded(BlockPos pos)
    {
        return isBlockLoaded(pos, true);
    }

    public boolean isBlockLoaded(BlockPos pos, boolean allowEmpty)
    {
        return isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, allowEmpty);
    }

    public boolean isAreaLoaded(BlockPos center, int radius)
    {
        return isAreaLoaded(center, radius, true);
    }

    public boolean isAreaLoaded(BlockPos center, int radius, boolean allowEmpty)
    {
        return isAreaLoaded(center.getX() - radius, center.getY() - radius, center.getZ() - radius, center.getX() + radius, center.getY() + radius, center.getZ() + radius, allowEmpty);
    }

    public boolean isAreaLoaded(BlockPos from, BlockPos to)
    {
        return isAreaLoaded(from, to, true);
    }

    public boolean isAreaLoaded(BlockPos from, BlockPos to, boolean allowEmpty)
    {
        return isAreaLoaded(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), allowEmpty);
    }

    public boolean isAreaLoaded(StructureBoundingBox box)
    {
        return isAreaLoaded(box, true);
    }

    public boolean isAreaLoaded(StructureBoundingBox box, boolean allowEmpty)
    {
        return isAreaLoaded(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, allowEmpty);
    }

    private boolean isAreaLoaded(int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd, boolean allowEmpty)
    {
        if (yEnd >= 0 && yStart < 256)
        {
            xStart = xStart >> 4;
            zStart = zStart >> 4;
            xEnd = xEnd >> 4;
            zEnd = zEnd >> 4;

            for (int i = xStart; i <= xEnd; ++i)
            {
                for (int j = zStart; j <= zEnd; ++j)
                {
                    if (!isChunkLoaded(i, j, allowEmpty))
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected abstract boolean isChunkLoaded(int x, int z, boolean allowEmpty);

    public Chunk getChunkFromBlockCoords(BlockPos pos)
    {
        return getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4);
    }

    /**
     * Gets the chunk at the specified location.
     */
    public Chunk getChunkFromChunkCoords(int chunkX, int chunkZ)
    {
        return chunkProvider.provideChunk(chunkX, chunkZ);
    }

    public boolean func_190526_b(int p_190526_1_, int p_190526_2_)
    {
        return isChunkLoaded(p_190526_1_, p_190526_2_, false) || chunkProvider.func_191062_e(p_190526_1_, p_190526_2_);
    }

    /**
     * Sets the block state at a given location. Flag 1 will cause a block update. Flag 2 will send the change to
     * clients (you almost always want this). Flag 4 prevents the block from being re-rendered, if this is a client
     * world. Flags can be added together.
     */
    public boolean setBlockState(BlockPos pos, IBlockState newState, int flags)
    {
        if (isOutsideBuildHeight(pos))
        {
            return false;
        }
        else if (!isRemote && worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
        {
            return false;
        }
        else
        {
            Chunk chunk = getChunkFromBlockCoords(pos);
            Block block = newState.getBlock();
            IBlockState iblockstate = chunk.setBlockState(pos, newState);

            if (iblockstate == null)
            {
                return false;
            }
            else
            {
                if (newState.getLightOpacity() != iblockstate.getLightOpacity() || newState.getLightValue() != iblockstate.getLightValue())
                {
                    theProfiler.startSection("checkLight");
                    checkLight(pos);
                    theProfiler.endSection();
                }

                if ((flags & 2) != 0 && (!isRemote || (flags & 4) == 0) && chunk.isPopulated())
                {
                    notifyBlockUpdate(pos, iblockstate, newState, flags);
                }

                if (!isRemote && (flags & 1) != 0)
                {
                    notifyNeighborsRespectDebug(pos, iblockstate.getBlock(), true);

                    if (newState.hasComparatorInputOverride())
                    {
                        updateComparatorOutputLevel(pos, block);
                    }
                }
                else if (!isRemote && (flags & 16) == 0)
                {
                    func_190522_c(pos, block);
                }

                return true;
            }
        }
    }

    public boolean setBlockToAir(BlockPos pos)
    {
        return setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }

    /**
     * Sets a block to air, but also plays the sound and particles and can spawn drops
     */
    public boolean destroyBlock(BlockPos pos, boolean dropBlock)
    {
        IBlockState iblockstate = getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (iblockstate.getMaterial() == Material.AIR)
        {
            return false;
        }
        else
        {
            playEvent(2001, pos, Block.getStateId(iblockstate));

            if (dropBlock)
            {
                block.dropBlockAsItem(this, pos, iblockstate, 0);
            }

            return setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    /**
     * Convenience method to update the block on both the client and server
     */
    public boolean setBlockState(BlockPos pos, IBlockState state)
    {
        return setBlockState(pos, state, 3);
    }

    public void notifyBlockUpdate(BlockPos pos, IBlockState oldState, IBlockState newState, int flags)
    {
        for (int i = 0; i < eventListeners.size(); ++i)
        {
            eventListeners.get(i).notifyBlockUpdate(this, pos, oldState, newState, flags);
        }
    }

    public void notifyNeighborsRespectDebug(BlockPos pos, Block blockType, boolean p_175722_3_)
    {
        if (worldInfo.getTerrainType() != WorldType.DEBUG_WORLD)
        {
            notifyNeighborsOfStateChange(pos, blockType, p_175722_3_);
        }
    }

    /**
     * marks a vertical line of blocks as dirty
     */
    public void markBlocksDirtyVertical(int x1, int z1, int x2, int z2)
    {
        if (x2 > z2)
        {
            int i = z2;
            z2 = x2;
            x2 = i;
        }

        if (provider.isNether())
        {
            for (int j = x2; j <= z2; ++j)
            {
                checkLightFor(EnumSkyBlock.SKY, new BlockPos(x1, j, z1));
            }
        }

        markBlockRangeForRenderUpdate(x1, x2, z1, x1, z2, z1);
    }

    public void markBlockRangeForRenderUpdate(BlockPos rangeMin, BlockPos rangeMax)
    {
        markBlockRangeForRenderUpdate(rangeMin.getX(), rangeMin.getY(), rangeMin.getZ(), rangeMax.getX(), rangeMax.getY(), rangeMax.getZ());
    }

    /**
     * Notifies all listening IWorldEventListeners of an update within the given bounds.
     */
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        for (int i = 0; i < eventListeners.size(); ++i)
        {
            eventListeners.get(i).markBlockRangeForRenderUpdate(x1, y1, z1, x2, y2, z2);
        }
    }

    public void func_190522_c(BlockPos p_190522_1_, Block p_190522_2_)
    {
        func_190529_b(p_190522_1_.west(), p_190522_2_, p_190522_1_);
        func_190529_b(p_190522_1_.east(), p_190522_2_, p_190522_1_);
        func_190529_b(p_190522_1_.down(), p_190522_2_, p_190522_1_);
        func_190529_b(p_190522_1_.up(), p_190522_2_, p_190522_1_);
        func_190529_b(p_190522_1_.north(), p_190522_2_, p_190522_1_);
        func_190529_b(p_190522_1_.south(), p_190522_2_, p_190522_1_);
    }

    public void notifyNeighborsOfStateChange(BlockPos pos, Block blockType, boolean p_175685_3_)
    {
        func_190524_a(pos.west(), blockType, pos);
        func_190524_a(pos.east(), blockType, pos);
        func_190524_a(pos.down(), blockType, pos);
        func_190524_a(pos.up(), blockType, pos);
        func_190524_a(pos.north(), blockType, pos);
        func_190524_a(pos.south(), blockType, pos);

        if (p_175685_3_)
        {
            func_190522_c(pos, blockType);
        }
    }

    public void notifyNeighborsOfStateExcept(BlockPos pos, Block blockType, EnumFacing skipSide)
    {
        if (skipSide != EnumFacing.WEST)
        {
            func_190524_a(pos.west(), blockType, pos);
        }

        if (skipSide != EnumFacing.EAST)
        {
            func_190524_a(pos.east(), blockType, pos);
        }

        if (skipSide != EnumFacing.DOWN)
        {
            func_190524_a(pos.down(), blockType, pos);
        }

        if (skipSide != EnumFacing.UP)
        {
            func_190524_a(pos.up(), blockType, pos);
        }

        if (skipSide != EnumFacing.NORTH)
        {
            func_190524_a(pos.north(), blockType, pos);
        }

        if (skipSide != EnumFacing.SOUTH)
        {
            func_190524_a(pos.south(), blockType, pos);
        }
    }

    public void func_190524_a(BlockPos p_190524_1_, Block p_190524_2_, BlockPos p_190524_3_)
    {
        if (!isRemote)
        {
            IBlockState iblockstate = getBlockState(p_190524_1_);

            try
            {
                iblockstate.neighborChanged(this, p_190524_1_, p_190524_2_, p_190524_3_);
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
                crashreportcategory.setDetail("Source block type", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        try
                        {
                            return String.format("ID #%d (%s // %s)", Block.getIdFromBlock(p_190524_2_), p_190524_2_.getUnlocalizedName(), p_190524_2_.getClass().getCanonicalName());
                        }
                        catch (Throwable var2)
                        {
                            return "ID #" + Block.getIdFromBlock(p_190524_2_);
                        }
                    }
                });
                CrashReportCategory.addBlockInfo(crashreportcategory, p_190524_1_, iblockstate);
                throw new ReportedException(crashreport);
            }
        }
    }

    public void func_190529_b(BlockPos p_190529_1_, Block p_190529_2_, BlockPos p_190529_3_)
    {
        if (!isRemote)
        {
            IBlockState iblockstate = getBlockState(p_190529_1_);

            if (iblockstate.getBlock() == Blocks.OBSERVER)
            {
                try
                {
                    ((BlockObserver)iblockstate.getBlock()).func_190962_b(iblockstate, this, p_190529_1_, p_190529_2_, p_190529_3_);
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
                    crashreportcategory.setDetail("Source block type", new ICrashReportDetail<String>()
                    {
                        public String call() throws Exception
                        {
                            try
                            {
                                return String.format("ID #%d (%s // %s)", Block.getIdFromBlock(p_190529_2_), p_190529_2_.getUnlocalizedName(), p_190529_2_.getClass().getCanonicalName());
                            }
                            catch (Throwable var2)
                            {
                                return "ID #" + Block.getIdFromBlock(p_190529_2_);
                            }
                        }
                    });
                    CrashReportCategory.addBlockInfo(crashreportcategory, p_190529_1_, iblockstate);
                    throw new ReportedException(crashreport);
                }
            }
        }
    }

    public boolean isBlockTickPending(BlockPos pos, Block blockType)
    {
        return false;
    }

    public boolean canSeeSky(BlockPos pos)
    {
        return getChunkFromBlockCoords(pos).canSeeSky(pos);
    }

    public boolean canBlockSeeSky(BlockPos pos)
    {
        if (pos.getY() >= getSeaLevel())
        {
            return canSeeSky(pos);
        }
        else
        {
            BlockPos blockpos = new BlockPos(pos.getX(), getSeaLevel(), pos.getZ());

            if (!canSeeSky(blockpos))
            {
                return false;
            }
            else
            {
                for (BlockPos blockpos1 = blockpos.down(); blockpos1.getY() > pos.getY(); blockpos1 = blockpos1.down())
                {
                    IBlockState iblockstate = getBlockState(blockpos1);

                    if (iblockstate.getLightOpacity() > 0 && !iblockstate.getMaterial().isLiquid())
                    {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public int getLight(BlockPos pos)
    {
        if (pos.getY() < 0)
        {
            return 0;
        }
        else
        {
            if (pos.getY() >= 256)
            {
                pos = new BlockPos(pos.getX(), 255, pos.getZ());
            }

            return getChunkFromBlockCoords(pos).getLightSubtracted(pos, 0);
        }
    }

    public int getLightFromNeighbors(BlockPos pos)
    {
        return getLight(pos, true);
    }

    public int getLight(BlockPos pos, boolean checkNeighbors)
    {
        if (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000)
        {
            if (checkNeighbors && getBlockState(pos).useNeighborBrightness())
            {
                int i1 = getLight(pos.up(), false);
                int i = getLight(pos.east(), false);
                int j = getLight(pos.west(), false);
                int k = getLight(pos.south(), false);
                int l = getLight(pos.north(), false);

                if (i > i1)
                {
                    i1 = i;
                }

                if (j > i1)
                {
                    i1 = j;
                }

                if (k > i1)
                {
                    i1 = k;
                }

                if (l > i1)
                {
                    i1 = l;
                }

                return i1;
            }
            else if (pos.getY() < 0)
            {
                return 0;
            }
            else
            {
                if (pos.getY() >= 256)
                {
                    pos = new BlockPos(pos.getX(), 255, pos.getZ());
                }

                Chunk chunk = getChunkFromBlockCoords(pos);
                return chunk.getLightSubtracted(pos, skylightSubtracted);
            }
        }
        else
        {
            return 15;
        }
    }

    /**
     * Returns the position at this x, z coordinate in the chunk with y set to the value from the height map.
     */
    public BlockPos getHeight(BlockPos pos)
    {
        return new BlockPos(pos.getX(), getHeight(pos.getX(), pos.getZ()), pos.getZ());
    }

    /**
     * Returns, from the height map, the height of the highest block at this x and z coordinate.
     */
    public int getHeight(int x, int z)
    {
        int i;

        if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000)
        {
            if (isChunkLoaded(x >> 4, z >> 4, true))
            {
                i = getChunkFromChunkCoords(x >> 4, z >> 4).getHeightValue(x & 15, z & 15);
            }
            else
            {
                i = 0;
            }
        }
        else
        {
            i = getSeaLevel() + 1;
        }

        return i;
    }

    @Deprecated

    /**
     * Gets the lowest height of the chunk where sunlight directly reaches
     */
    public int getChunksLowestHorizon(int x, int z)
    {
        if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000)
        {
            if (!isChunkLoaded(x >> 4, z >> 4, true))
            {
                return 0;
            }
            else
            {
                Chunk chunk = getChunkFromChunkCoords(x >> 4, z >> 4);
                return chunk.getLowestHeight();
            }
        }
        else
        {
            return getSeaLevel() + 1;
        }
    }

    public int getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos)
    {
        if (!provider.isNether() && type == EnumSkyBlock.SKY)
        {
            return 0;
        }
        else
        {
            if (pos.getY() < 0)
            {
                pos = new BlockPos(pos.getX(), 0, pos.getZ());
            }

            if (!isValid(pos))
            {
                return type.defaultLightValue;
            }
            else if (!isBlockLoaded(pos))
            {
                return type.defaultLightValue;
            }
            else if (getBlockState(pos).useNeighborBrightness())
            {
                int i1 = getLightFor(type, pos.up());
                int i = getLightFor(type, pos.east());
                int j = getLightFor(type, pos.west());
                int k = getLightFor(type, pos.south());
                int l = getLightFor(type, pos.north());

                if (i > i1)
                {
                    i1 = i;
                }

                if (j > i1)
                {
                    i1 = j;
                }

                if (k > i1)
                {
                    i1 = k;
                }

                if (l > i1)
                {
                    i1 = l;
                }

                return i1;
            }
            else
            {
                Chunk chunk = getChunkFromBlockCoords(pos);
                return chunk.getLightFor(type, pos);
            }
        }
    }

    public int getLightFor(EnumSkyBlock type, BlockPos pos)
    {
        if (pos.getY() < 0)
        {
            pos = new BlockPos(pos.getX(), 0, pos.getZ());
        }

        if (!isValid(pos))
        {
            return type.defaultLightValue;
        }
        else if (!isBlockLoaded(pos))
        {
            return type.defaultLightValue;
        }
        else
        {
            Chunk chunk = getChunkFromBlockCoords(pos);
            return chunk.getLightFor(type, pos);
        }
    }

    public void setLightFor(EnumSkyBlock type, BlockPos pos, int lightValue)
    {
        if (isValid(pos))
        {
            if (isBlockLoaded(pos))
            {
                Chunk chunk = getChunkFromBlockCoords(pos);
                chunk.setLightFor(type, pos, lightValue);
                notifyLightSet(pos);
            }
        }
    }

    public void notifyLightSet(BlockPos pos)
    {
        for (int i = 0; i < eventListeners.size(); ++i)
        {
            eventListeners.get(i).notifyLightSet(pos);
        }
    }

    public int getCombinedLight(BlockPos pos, int lightValue)
    {
        int i = getLightFromNeighborsFor(EnumSkyBlock.SKY, pos);
        int j = getLightFromNeighborsFor(EnumSkyBlock.BLOCK, pos);

        if (j < lightValue)
        {
            j = lightValue;
        }

        return i << 20 | j << 4;
    }

    public float getLightBrightness(BlockPos pos)
    {
        return provider.getLightBrightnessTable()[getLightFromNeighbors(pos)];
    }

    public IBlockState getBlockState(BlockPos pos)
    {
        if (isOutsideBuildHeight(pos))
        {
            return Blocks.AIR.getDefaultState();
        }
        else
        {
            Chunk chunk = getChunkFromBlockCoords(pos);
            return chunk.getBlockState(pos);
        }
    }

    /**
     * Checks whether its daytime by seeing if the light subtracted from the skylight is less than 4
     */
    public boolean isDaytime()
    {
        return skylightSubtracted < 4;
    }

    @Nullable

    /**
     * ray traces all blocks, including non-collideable ones
     */
    public RayTraceResult rayTraceBlocks(Vec3d start, Vec3d end)
    {
        return rayTraceBlocks(start, end, false, false, false);
    }

    @Nullable
    public RayTraceResult rayTraceBlocks(Vec3d start, Vec3d end, boolean stopOnLiquid)
    {
        return rayTraceBlocks(start, end, stopOnLiquid, false, false);
    }

    @Nullable

    /**
     * Performs a raycast against all blocks in the world. Args : Vec1, Vec2, stopOnLiquid,
     * ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock
     */
    public RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
    {
        if (!Double.isNaN(vec31.xCoord) && !Double.isNaN(vec31.yCoord) && !Double.isNaN(vec31.zCoord))
        {
            if (!Double.isNaN(vec32.xCoord) && !Double.isNaN(vec32.yCoord) && !Double.isNaN(vec32.zCoord))
            {
                int i = MathHelper.floor(vec32.xCoord);
                int j = MathHelper.floor(vec32.yCoord);
                int k = MathHelper.floor(vec32.zCoord);
                int l = MathHelper.floor(vec31.xCoord);
                int i1 = MathHelper.floor(vec31.yCoord);
                int j1 = MathHelper.floor(vec31.zCoord);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(this, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid))
                {
                    RayTraceResult raytraceresult = iblockstate.collisionRayTrace(this, blockpos, vec31, vec32);

                    if (raytraceresult != null)
                    {
                        return raytraceresult;
                    }
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord))
                    {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k)
                    {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l)
                    {
                        d0 = (double)l + 1.0D;
                    }
                    else if (i < l)
                    {
                        d0 = (double)l + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (j > i1)
                    {
                        d1 = (double)i1 + 1.0D;
                    }
                    else if (j < i1)
                    {
                        d1 = (double)i1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (k > j1)
                    {
                        d2 = (double)j1 + 1.0D;
                    }
                    else if (k < j1)
                    {
                        d2 = (double)j1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.xCoord - vec31.xCoord;
                    double d7 = vec32.yCoord - vec31.yCoord;
                    double d8 = vec32.zCoord - vec31.zCoord;

                    if (flag2)
                    {
                        d3 = (d0 - vec31.xCoord) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - vec31.yCoord) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - vec31.zCoord) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.yCoord + d7 * d3, vec31.zCoord + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.xCoord + d6 * d4, d1, vec31.zCoord + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.xCoord + d6 * d5, vec31.yCoord + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(this, blockpos) != Block.NULL_AABB)
                    {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid))
                        {
                            RayTraceResult raytraceresult1 = iblockstate1.collisionRayTrace(this, blockpos, vec31, vec32);

                            if (raytraceresult1 != null)
                            {
                                return raytraceresult1;
                            }
                        }
                        else
                        {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Plays the specified sound for a player at the center of the given block position.
     */
    public void playSound(@Nullable EntityPlayer player, BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch)
    {
        playSound(player, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, soundIn, category, volume, pitch);
    }

    public void playSound(@Nullable EntityPlayer player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch)
    {
        for (int i = 0; i < eventListeners.size(); ++i)
        {
            eventListeners.get(i).playSoundToAllNearExcept(player, soundIn, category, x, y, z, volume, pitch);
        }
    }

    public void playSound(double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay)
    {
    }

    public void playRecord(BlockPos blockPositionIn, @Nullable SoundEvent soundEventIn)
    {
        for (int i = 0; i < eventListeners.size(); ++i)
        {
            eventListeners.get(i).playRecord(soundEventIn, blockPositionIn);
        }
    }

    public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters)
    {
        spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
    }

    public void func_190523_a(int p_190523_1_, double p_190523_2_, double p_190523_4_, double p_190523_6_, double p_190523_8_, double p_190523_10_, double p_190523_12_, int... p_190523_14_)
    {
        for (int i = 0; i < eventListeners.size(); ++i)
        {
            eventListeners.get(i).func_190570_a(p_190523_1_, false, true, p_190523_2_, p_190523_4_, p_190523_6_, p_190523_8_, p_190523_10_, p_190523_12_, p_190523_14_);
        }
    }

    public void spawnParticle(EnumParticleTypes particleType, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters)
    {
        spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange() || ignoreRange, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
    }

    private void spawnParticle(int particleID, boolean ignoreRange, double xCood, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters)
    {
        for (int i = 0; i < eventListeners.size(); ++i)
        {
            eventListeners.get(i).spawnParticle(particleID, ignoreRange, xCood, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
        }
    }

    /**
     * adds a lightning bolt to the list of lightning bolts in this world.
     */
    public boolean addWeatherEffect(Entity entityIn)
    {
        weatherEffects.add(entityIn);
        return true;
    }

    /**
     * Called when an entity is spawned in the world. This includes players.
     */
    public boolean spawnEntityInWorld(Entity entityIn)
    {
        int i = MathHelper.floor(entityIn.posX / 16.0D);
        int j = MathHelper.floor(entityIn.posZ / 16.0D);
        boolean flag = entityIn.forceSpawn;

        if (entityIn instanceof EntityPlayer)
        {
            flag = true;
        }

        if (!flag && !isChunkLoaded(i, j, false))
        {
            return false;
        }
        else
        {
            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                playerEntities.add(entityplayer);
                updateAllPlayersSleepingFlag();
            }

            getChunkFromChunkCoords(i, j).addEntity(entityIn);
            loadedEntityList.add(entityIn);
            onEntityAdded(entityIn);
            return true;
        }
    }

    protected void onEntityAdded(Entity entityIn)
    {
        for (int i = 0; i < eventListeners.size(); ++i)
        {
            eventListeners.get(i).onEntityAdded(entityIn);
        }
    }

    protected void onEntityRemoved(Entity entityIn)
    {
        for (int i = 0; i < eventListeners.size(); ++i)
        {
            eventListeners.get(i).onEntityRemoved(entityIn);
        }
    }

    /**
     * Schedule the entity for removal during the next tick. Marks the entity dead in anticipation.
     */
    public void removeEntity(Entity entityIn)
    {
        if (entityIn.isBeingRidden())
        {
            entityIn.removePassengers();
        }

        if (entityIn.isRiding())
        {
            entityIn.dismountRidingEntity();
        }

        entityIn.setDead();

        if (entityIn instanceof EntityPlayer)
        {
            playerEntities.remove(entityIn);
            updateAllPlayersSleepingFlag();
            onEntityRemoved(entityIn);
        }
    }

    /**
     * Do NOT use this method to remove normal entities- use normal removeEntity
     */
    public void removeEntityDangerously(Entity entityIn)
    {
        entityIn.setDropItemsWhenDead(false);
        entityIn.setDead();

        if (entityIn instanceof EntityPlayer)
        {
            playerEntities.remove(entityIn);
            updateAllPlayersSleepingFlag();
        }

        int i = entityIn.chunkCoordX;
        int j = entityIn.chunkCoordZ;

        if (entityIn.addedToChunk && isChunkLoaded(i, j, true))
        {
            getChunkFromChunkCoords(i, j).removeEntity(entityIn);
        }

        loadedEntityList.remove(entityIn);
        onEntityRemoved(entityIn);
    }

    /**
     * Add a world event listener
     */
    public void addEventListener(IWorldEventListener listener)
    {
        eventListeners.add(listener);
    }

    /**
     * Remove a world event listener
     */
    public void removeEventListener(IWorldEventListener listener)
    {
        eventListeners.remove(listener);
    }

    private boolean func_191504_a(@Nullable Entity p_191504_1_, AxisAlignedBB p_191504_2_, boolean p_191504_3_, @Nullable List<AxisAlignedBB> p_191504_4_)
    {
        int i = MathHelper.floor(p_191504_2_.minX) - 1;
        int j = MathHelper.ceil(p_191504_2_.maxX) + 1;
        int k = MathHelper.floor(p_191504_2_.minY) - 1;
        int l = MathHelper.ceil(p_191504_2_.maxY) + 1;
        int i1 = MathHelper.floor(p_191504_2_.minZ) - 1;
        int j1 = MathHelper.ceil(p_191504_2_.maxZ) + 1;
        WorldBorder worldborder = getWorldBorder();
        boolean flag = p_191504_1_ != null && p_191504_1_.isOutsideBorder();
        boolean flag1 = p_191504_1_ != null && func_191503_g(p_191504_1_);
        IBlockState iblockstate = Blocks.STONE.getDefaultState();
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        try
        {
            for (int k1 = i; k1 < j; ++k1)
            {
                for (int l1 = i1; l1 < j1; ++l1)
                {
                    boolean flag2 = k1 == i || k1 == j - 1;
                    boolean flag3 = l1 == i1 || l1 == j1 - 1;

                    if ((!flag2 || !flag3) && isBlockLoaded(blockpos$pooledmutableblockpos.setPos(k1, 64, l1)))
                    {
                        for (int i2 = k; i2 < l; ++i2)
                        {
                            if (!flag2 && !flag3 || i2 != l - 1)
                            {
                                if (p_191504_3_)
                                {
                                    if (k1 < -30000000 || k1 >= 30000000 || l1 < -30000000 || l1 >= 30000000)
                                    {
                                        boolean lvt_21_2_ = true;
                                        return lvt_21_2_;
                                    }
                                }
                                else if (p_191504_1_ != null && flag == flag1)
                                {
                                    p_191504_1_.setOutsideBorder(!flag1);
                                }

                                blockpos$pooledmutableblockpos.setPos(k1, i2, l1);
                                IBlockState iblockstate1;

                                if (!p_191504_3_ && !worldborder.contains(blockpos$pooledmutableblockpos) && flag1)
                                {
                                    iblockstate1 = iblockstate;
                                }
                                else
                                {
                                    iblockstate1 = getBlockState(blockpos$pooledmutableblockpos);
                                }

                                iblockstate1.addCollisionBoxToList(this, blockpos$pooledmutableblockpos, p_191504_2_, p_191504_4_, p_191504_1_, false);

                                if (p_191504_3_ && !p_191504_4_.isEmpty())
                                {
                                    boolean flag5 = true;
                                    return flag5;
                                }
                            }
                        }
                    }
                }
            }
        }
        finally
        {
            blockpos$pooledmutableblockpos.release();
        }

        return !p_191504_4_.isEmpty();
    }

    public List<AxisAlignedBB> getCollisionBoxes(@Nullable Entity entityIn, AxisAlignedBB aabb)
    {
        List<AxisAlignedBB> list = Lists.newArrayList();
        func_191504_a(entityIn, aabb, false, list);

        if (entityIn != null)
        {
            List<Entity> list1 = getEntitiesWithinAABBExcludingEntity(entityIn, aabb.expandXyz(0.25D));

            for (int i = 0; i < list1.size(); ++i)
            {
                Entity entity = list1.get(i);

                if (!entityIn.isRidingSameEntity(entity))
                {
                    AxisAlignedBB axisalignedbb = entity.getCollisionBoundingBox();

                    if (axisalignedbb != null && axisalignedbb.intersectsWith(aabb))
                    {
                        list.add(axisalignedbb);
                    }

                    axisalignedbb = entityIn.getCollisionBox(entity);

                    if (axisalignedbb != null && axisalignedbb.intersectsWith(aabb))
                    {
                        list.add(axisalignedbb);
                    }
                }
            }
        }

        return list;
    }

    public boolean func_191503_g(Entity p_191503_1_)
    {
        double d0 = worldBorder.minX();
        double d1 = worldBorder.minZ();
        double d2 = worldBorder.maxX();
        double d3 = worldBorder.maxZ();

        if (p_191503_1_.isOutsideBorder())
        {
            ++d0;
            ++d1;
            --d2;
            --d3;
        }
        else
        {
            --d0;
            --d1;
            ++d2;
            ++d3;
        }

        return p_191503_1_.posX > d0 && p_191503_1_.posX < d2 && p_191503_1_.posZ > d1 && p_191503_1_.posZ < d3;
    }

    /**
     * Returns true if the given bbox collides with any block.
     */
    public boolean collidesWithAnyBlock(AxisAlignedBB bbox)
    {
        return func_191504_a(null, bbox, true, Lists.newArrayList());
    }

    /**
     * Returns the amount of skylight subtracted for the current time
     */
    public int calculateSkylightSubtracted(float partialTicks)
    {
        float f = getCelestialAngle(partialTicks);
        float f1 = 1.0F - (MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 = (float)((double)f1 * (1.0D - (double)(getRainStrength(partialTicks) * 5.0F) / 16.0D));
        f1 = (float)((double)f1 * (1.0D - (double)(getThunderStrength(partialTicks) * 5.0F) / 16.0D));
        f1 = 1.0F - f1;
        return (int)(f1 * 11.0F);
    }

    /**
     * Returns the sun brightness - checks time of day, rain and thunder
     */
    public float getSunBrightness(float p_72971_1_)
    {
        float f = getCelestialAngle(p_72971_1_);
        float f1 = 1.0F - (MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.2F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 = (float)((double)f1 * (1.0D - (double)(getRainStrength(p_72971_1_) * 5.0F) / 16.0D));
        f1 = (float)((double)f1 * (1.0D - (double)(getThunderStrength(p_72971_1_) * 5.0F) / 16.0D));
        return f1 * 0.8F + 0.2F;
    }

    /**
     * Calculates the color for the skybox
     */
    public Vec3d getSkyColor(Entity entityIn, float partialTicks)
    {
        float f = getCelestialAngle(partialTicks);
        float f1 = MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        int i = MathHelper.floor(entityIn.posX);
        int j = MathHelper.floor(entityIn.posY);
        int k = MathHelper.floor(entityIn.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        Biome biome = getBiome(blockpos);
        float f2 = biome.getFloatTemperature(blockpos);
        int l = biome.getSkyColorByTemp(f2);
        float f3 = (float)(l >> 16 & 255) / 255.0F;
        float f4 = (float)(l >> 8 & 255) / 255.0F;
        float f5 = (float)(l & 255) / 255.0F;
        f3 = f3 * f1;
        f4 = f4 * f1;
        f5 = f5 * f1;
        float f6 = getRainStrength(partialTicks);

        if (f6 > 0.0F)
        {
            float f7 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
            float f8 = 1.0F - f6 * 0.75F;
            f3 = f3 * f8 + f7 * (1.0F - f8);
            f4 = f4 * f8 + f7 * (1.0F - f8);
            f5 = f5 * f8 + f7 * (1.0F - f8);
        }

        float f10 = getThunderStrength(partialTicks);

        if (f10 > 0.0F)
        {
            float f11 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
            float f9 = 1.0F - f10 * 0.75F;
            f3 = f3 * f9 + f11 * (1.0F - f9);
            f4 = f4 * f9 + f11 * (1.0F - f9);
            f5 = f5 * f9 + f11 * (1.0F - f9);
        }

        if (lastLightningBolt > 0)
        {
            float f12 = (float) lastLightningBolt - partialTicks;

            if (f12 > 1.0F)
            {
                f12 = 1.0F;
            }

            f12 = f12 * 0.45F;
            f3 = f3 * (1.0F - f12) + 0.8F * f12;
            f4 = f4 * (1.0F - f12) + 0.8F * f12;
            f5 = f5 * (1.0F - f12) + 1.0F * f12;
        }

        return new Vec3d(f3, f4, f5);
    }

    /**
     * calls calculateCelestialAngle
     */
    public float getCelestialAngle(float partialTicks)
    {
        return provider.calculateCelestialAngle(worldInfo.getWorldTime(), partialTicks);
    }

    public int getMoonPhase()
    {
        return provider.getMoonPhase(worldInfo.getWorldTime());
    }

    /**
     * gets the current fullness of the moon expressed as a float between 1.0 and 0.0, in steps of .25
     */
    public float getCurrentMoonPhaseFactor()
    {
        return WorldProvider.MOON_PHASE_FACTORS[provider.getMoonPhase(worldInfo.getWorldTime())];
    }

    /**
     * Return getCelestialAngle()*2*PI
     */
    public float getCelestialAngleRadians(float partialTicks)
    {
        float f = getCelestialAngle(partialTicks);
        return f * ((float)Math.PI * 2F);
    }

    public Vec3d getCloudColour(float partialTicks)
    {
        float f = getCelestialAngle(partialTicks);
        float f1 = MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        float f2 = 1.0F;
        float f3 = 1.0F;
        float f4 = 1.0F;
        float f5 = getRainStrength(partialTicks);

        if (f5 > 0.0F)
        {
            float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
            float f7 = 1.0F - f5 * 0.95F;
            f2 = f2 * f7 + f6 * (1.0F - f7);
            f3 = f3 * f7 + f6 * (1.0F - f7);
            f4 = f4 * f7 + f6 * (1.0F - f7);
        }

        f2 = f2 * (f1 * 0.9F + 0.1F);
        f3 = f3 * (f1 * 0.9F + 0.1F);
        f4 = f4 * (f1 * 0.85F + 0.15F);
        float f9 = getThunderStrength(partialTicks);

        if (f9 > 0.0F)
        {
            float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
            float f8 = 1.0F - f9 * 0.95F;
            f2 = f2 * f8 + f10 * (1.0F - f8);
            f3 = f3 * f8 + f10 * (1.0F - f8);
            f4 = f4 * f8 + f10 * (1.0F - f8);
        }

        return new Vec3d(f2, f3, f4);
    }

    /**
     * Returns vector(ish) with R/G/B for fog
     */
    public Vec3d getFogColor(float partialTicks)
    {
        float f = getCelestialAngle(partialTicks);
        return provider.getFogColor(f, partialTicks);
    }

    public BlockPos getPrecipitationHeight(BlockPos pos)
    {
        return getChunkFromBlockCoords(pos).getPrecipitationHeight(pos);
    }

    /**
     * Finds the highest block on the x and z coordinate that is solid or liquid, and returns its y coord.
     */
    public BlockPos getTopSolidOrLiquidBlock(BlockPos pos)
    {
        Chunk chunk = getChunkFromBlockCoords(pos);
        BlockPos blockpos;
        BlockPos blockpos1;

        for (blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos.getY() >= 0; blockpos = blockpos1)
        {
            blockpos1 = blockpos.down();
            Material material = chunk.getBlockState(blockpos1).getMaterial();

            if (material.blocksMovement() && material != Material.LEAVES)
            {
                break;
            }
        }

        return blockpos;
    }

    /**
     * How bright are stars in the sky
     */
    public float getStarBrightness(float partialTicks)
    {
        float f = getCelestialAngle(partialTicks);
        float f1 = 1.0F - (MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.25F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        return f1 * f1 * 0.5F;
    }

    /**
     * Returns true if the identified block is scheduled to be updated.
     */
    public boolean isUpdateScheduled(BlockPos pos, Block blk)
    {
        return true;
    }

    public void scheduleUpdate(BlockPos pos, Block blockIn, int delay)
    {
    }

    public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority)
    {
    }

    public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority)
    {
    }

    /**
     * Updates (and cleans up) entities and tile entities
     */
    public void updateEntities()
    {
        theProfiler.startSection("entities");
        theProfiler.startSection("global");

        for (int i = 0; i < weatherEffects.size(); ++i)
        {
            Entity entity = weatherEffects.get(i);

            try
            {
                ++entity.ticksExisted;
                entity.onUpdate();
            }
            catch (Throwable throwable2)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Ticking entity");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");

                if (entity == null)
                {
                    crashreportcategory.addCrashSection("Entity", "~~NULL~~");
                }
                else
                {
                    entity.addEntityCrashInfo(crashreportcategory);
                }

                throw new ReportedException(crashreport);
            }

            if (entity.isDead)
            {
                weatherEffects.remove(i--);
            }
        }

        theProfiler.endStartSection("remove");
        loadedEntityList.removeAll(unloadedEntityList);

        for (int k = 0; k < unloadedEntityList.size(); ++k)
        {
            Entity entity1 = unloadedEntityList.get(k);
            int j = entity1.chunkCoordX;
            int k1 = entity1.chunkCoordZ;

            if (entity1.addedToChunk && isChunkLoaded(j, k1, true))
            {
                getChunkFromChunkCoords(j, k1).removeEntity(entity1);
            }
        }

        for (int l = 0; l < unloadedEntityList.size(); ++l)
        {
            onEntityRemoved(unloadedEntityList.get(l));
        }

        unloadedEntityList.clear();
        tickPlayers();
        theProfiler.endStartSection("regular");

        for (int i1 = 0; i1 < loadedEntityList.size(); ++i1)
        {
            Entity entity2 = loadedEntityList.get(i1);
            Entity entity3 = entity2.getRidingEntity();

            if (entity3 != null)
            {
                if (!entity3.isDead && entity3.isPassenger(entity2))
                {
                    continue;
                }

                entity2.dismountRidingEntity();
            }

            theProfiler.startSection("tick");

            if (!entity2.isDead && !(entity2 instanceof EntityPlayerMP))
            {
                try
                {
                    updateEntity(entity2);
                }
                catch (Throwable throwable1)
                {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Ticking entity");
                    CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Entity being ticked");
                    entity2.addEntityCrashInfo(crashreportcategory1);
                    throw new ReportedException(crashreport1);
                }
            }

            theProfiler.endSection();
            theProfiler.startSection("remove");

            if (entity2.isDead)
            {
                int l1 = entity2.chunkCoordX;
                int i2 = entity2.chunkCoordZ;

                if (entity2.addedToChunk && isChunkLoaded(l1, i2, true))
                {
                    getChunkFromChunkCoords(l1, i2).removeEntity(entity2);
                }

                loadedEntityList.remove(i1--);
                onEntityRemoved(entity2);
            }

            theProfiler.endSection();
        }

        theProfiler.endStartSection("blockEntities");

        if (!tileEntitiesToBeRemoved.isEmpty())
        {
            tickableTileEntities.removeAll(tileEntitiesToBeRemoved);
            loadedTileEntityList.removeAll(tileEntitiesToBeRemoved);
            tileEntitiesToBeRemoved.clear();
        }

        processingLoadedTiles = true;
        Iterator<TileEntity> iterator = tickableTileEntities.iterator();

        while (iterator.hasNext())
        {
            TileEntity tileentity = iterator.next();

            if (!tileentity.isInvalid() && tileentity.hasWorldObj())
            {
                BlockPos blockpos = tileentity.getPos();

                if (isBlockLoaded(blockpos) && worldBorder.contains(blockpos))
                {
                    try
                    {
                        theProfiler.func_194340_a(() ->
                        {
                            return String.valueOf(TileEntity.func_190559_a(tileentity.getClass()));
                        });
                        ((ITickable)tileentity).update();
                        theProfiler.endSection();
                    }
                    catch (Throwable throwable)
                    {
                        CrashReport crashreport2 = CrashReport.makeCrashReport(throwable, "Ticking block entity");
                        CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Block entity being ticked");
                        tileentity.addInfoToCrashReport(crashreportcategory2);
                        throw new ReportedException(crashreport2);
                    }
                }
            }

            if (tileentity.isInvalid())
            {
                iterator.remove();
                loadedTileEntityList.remove(tileentity);

                if (isBlockLoaded(tileentity.getPos()))
                {
                    getChunkFromBlockCoords(tileentity.getPos()).removeTileEntity(tileentity.getPos());
                }
            }
        }

        processingLoadedTiles = false;
        theProfiler.endStartSection("pendingBlockEntities");

        if (!addedTileEntityList.isEmpty())
        {
            for (int j1 = 0; j1 < addedTileEntityList.size(); ++j1)
            {
                TileEntity tileentity1 = addedTileEntityList.get(j1);

                if (!tileentity1.isInvalid())
                {
                    if (!loadedTileEntityList.contains(tileentity1))
                    {
                        addTileEntity(tileentity1);
                    }

                    if (isBlockLoaded(tileentity1.getPos()))
                    {
                        Chunk chunk = getChunkFromBlockCoords(tileentity1.getPos());
                        IBlockState iblockstate = chunk.getBlockState(tileentity1.getPos());
                        chunk.addTileEntity(tileentity1.getPos(), tileentity1);
                        notifyBlockUpdate(tileentity1.getPos(), iblockstate, iblockstate, 3);
                    }
                }
            }

            addedTileEntityList.clear();
        }

        theProfiler.endSection();
        theProfiler.endSection();
    }

    protected void tickPlayers()
    {
    }

    public boolean addTileEntity(TileEntity tile)
    {
        boolean flag = loadedTileEntityList.add(tile);

        if (flag && tile instanceof ITickable)
        {
            tickableTileEntities.add(tile);
        }

        if (isRemote)
        {
            BlockPos blockpos1 = tile.getPos();
            IBlockState iblockstate1 = getBlockState(blockpos1);
            notifyBlockUpdate(blockpos1, iblockstate1, iblockstate1, 2);
        }

        return flag;
    }

    public void addTileEntities(Collection<TileEntity> tileEntityCollection)
    {
        if (processingLoadedTiles)
        {
            addedTileEntityList.addAll(tileEntityCollection);
        }
        else
        {
            for (TileEntity tileentity2 : tileEntityCollection)
            {
                addTileEntity(tileentity2);
            }
        }
    }

    /**
     * Forcefully updates the entity.
     */
    public void updateEntity(Entity ent)
    {
        updateEntityWithOptionalForce(ent, true);
    }

    /**
     * Updates the entity in the world if the chunk the entity is in is currently loaded or its forced to update.
     */
    public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate)
    {
        if (!(entityIn instanceof EntityPlayer))
        {
            int j2 = MathHelper.floor(entityIn.posX);
            int k2 = MathHelper.floor(entityIn.posZ);
            int l2 = 32;

            if (forceUpdate && !isAreaLoaded(j2 - 32, 0, k2 - 32, j2 + 32, 0, k2 + 32, true))
            {
                return;
            }
        }

        entityIn.lastTickPosX = entityIn.posX;
        entityIn.lastTickPosY = entityIn.posY;
        entityIn.lastTickPosZ = entityIn.posZ;
        entityIn.prevRotationYaw = entityIn.rotationYaw;
        entityIn.prevRotationPitch = entityIn.rotationPitch;

        if (forceUpdate && entityIn.addedToChunk)
        {
            ++entityIn.ticksExisted;

            if (entityIn.isRiding())
            {
                entityIn.updateRidden();
            }
            else
            {
                entityIn.onUpdate();
            }
        }

        theProfiler.startSection("chunkCheck");

        if (Double.isNaN(entityIn.posX) || Double.isInfinite(entityIn.posX))
        {
            entityIn.posX = entityIn.lastTickPosX;
        }

        if (Double.isNaN(entityIn.posY) || Double.isInfinite(entityIn.posY))
        {
            entityIn.posY = entityIn.lastTickPosY;
        }

        if (Double.isNaN(entityIn.posZ) || Double.isInfinite(entityIn.posZ))
        {
            entityIn.posZ = entityIn.lastTickPosZ;
        }

        if (Double.isNaN(entityIn.rotationPitch) || Double.isInfinite(entityIn.rotationPitch))
        {
            entityIn.rotationPitch = entityIn.prevRotationPitch;
        }

        if (Double.isNaN(entityIn.rotationYaw) || Double.isInfinite(entityIn.rotationYaw))
        {
            entityIn.rotationYaw = entityIn.prevRotationYaw;
        }

        int i3 = MathHelper.floor(entityIn.posX / 16.0D);
        int j3 = MathHelper.floor(entityIn.posY / 16.0D);
        int k3 = MathHelper.floor(entityIn.posZ / 16.0D);

        if (!entityIn.addedToChunk || entityIn.chunkCoordX != i3 || entityIn.chunkCoordY != j3 || entityIn.chunkCoordZ != k3)
        {
            if (entityIn.addedToChunk && isChunkLoaded(entityIn.chunkCoordX, entityIn.chunkCoordZ, true))
            {
                getChunkFromChunkCoords(entityIn.chunkCoordX, entityIn.chunkCoordZ).removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
            }

            if (!entityIn.setPositionNonDirty() && !isChunkLoaded(i3, k3, true))
            {
                entityIn.addedToChunk = false;
            }
            else
            {
                getChunkFromChunkCoords(i3, k3).addEntity(entityIn);
            }
        }

        theProfiler.endSection();

        if (forceUpdate && entityIn.addedToChunk)
        {
            for (Entity entity4 : entityIn.getPassengers())
            {
                if (!entity4.isDead && entity4.getRidingEntity() == entityIn)
                {
                    updateEntity(entity4);
                }
                else
                {
                    entity4.dismountRidingEntity();
                }
            }
        }
    }

    /**
     * Returns true if there are no solid, live entities in the specified AxisAlignedBB
     */
    public boolean checkNoEntityCollision(AxisAlignedBB bb)
    {
        return checkNoEntityCollision(bb, null);
    }

    /**
     * Returns true if there are no solid, live entities in the specified AxisAlignedBB, excluding the given entity
     */
    public boolean checkNoEntityCollision(AxisAlignedBB bb, @Nullable Entity entityIn)
    {
        List<Entity> list = getEntitiesWithinAABBExcludingEntity(null, bb);

        for (int j2 = 0; j2 < list.size(); ++j2)
        {
            Entity entity4 = list.get(j2);

            if (!entity4.isDead && entity4.preventEntitySpawning && entity4 != entityIn && (entityIn == null || entity4.isRidingSameEntity(entityIn)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if there are any blocks in the region constrained by an AxisAlignedBB
     */
    public boolean checkBlockCollision(AxisAlignedBB bb)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3)
        {
            for (int i4 = l2; i4 < i3; ++i4)
            {
                for (int j4 = j3; j4 < k3; ++j4)
                {
                    IBlockState iblockstate1 = getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));

                    if (iblockstate1.getMaterial() != Material.AIR)
                    {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        return false;
    }

    /**
     * Checks if any of the blocks within the aabb are liquids.
     */
    public boolean containsAnyLiquid(AxisAlignedBB bb)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3)
        {
            for (int i4 = l2; i4 < i3; ++i4)
            {
                for (int j4 = j3; j4 < k3; ++j4)
                {
                    IBlockState iblockstate1 = getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));

                    if (iblockstate1.getMaterial().isLiquid())
                    {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        return false;
    }

    public boolean isFlammableWithin(AxisAlignedBB bb)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);

        if (isAreaLoaded(j2, l2, j3, k2, i3, k3, true))
        {
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

            for (int l3 = j2; l3 < k2; ++l3)
            {
                for (int i4 = l2; i4 < i3; ++i4)
                {
                    for (int j4 = j3; j4 < k3; ++j4)
                    {
                        Block block = getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4)).getBlock();

                        if (block == Blocks.FIRE || block == Blocks.FLOWING_LAVA || block == Blocks.LAVA)
                        {
                            blockpos$pooledmutableblockpos.release();
                            return true;
                        }
                    }
                }
            }

            blockpos$pooledmutableblockpos.release();
        }

        return false;
    }

    /**
     * handles the acceleration of an object whilst in water. Not sure if it is used elsewhere.
     */
    public boolean handleMaterialAcceleration(AxisAlignedBB bb, Material materialIn, Entity entityIn)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);

        if (!isAreaLoaded(j2, l2, j3, k2, i3, k3, true))
        {
            return false;
        }
        else
        {
            boolean flag = false;
            Vec3d vec3d = Vec3d.ZERO;
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

            for (int l3 = j2; l3 < k2; ++l3)
            {
                for (int i4 = l2; i4 < i3; ++i4)
                {
                    for (int j4 = j3; j4 < k3; ++j4)
                    {
                        blockpos$pooledmutableblockpos.setPos(l3, i4, j4);
                        IBlockState iblockstate1 = getBlockState(blockpos$pooledmutableblockpos);
                        Block block = iblockstate1.getBlock();

                        if (iblockstate1.getMaterial() == materialIn)
                        {
                            double d0 = (float)(i4 + 1) - BlockLiquid.getLiquidHeightPercent(iblockstate1.getValue(BlockLiquid.LEVEL).intValue());

                            if ((double)i3 >= d0)
                            {
                                flag = true;
                                vec3d = block.modifyAcceleration(this, blockpos$pooledmutableblockpos, entityIn, vec3d);
                            }
                        }
                    }
                }
            }

            blockpos$pooledmutableblockpos.release();

            if (vec3d.lengthVector() > 0.0D && entityIn.isPushedByWater())
            {
                vec3d = vec3d.normalize();
                double d1 = 0.014D;
                entityIn.motionX += vec3d.xCoord * 0.014D;
                entityIn.motionY += vec3d.yCoord * 0.014D;
                entityIn.motionZ += vec3d.zCoord * 0.014D;
            }

            return flag;
        }
    }

    /**
     * Returns true if the given bounding box contains the given material
     */
    public boolean isMaterialInBB(AxisAlignedBB bb, Material materialIn)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3)
        {
            for (int i4 = l2; i4 < i3; ++i4)
            {
                for (int j4 = j3; j4 < k3; ++j4)
                {
                    if (getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4)).getMaterial() == materialIn)
                    {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        return false;
    }

    /**
     * Creates an explosion in the world.
     */
    public Explosion createExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isSmoking)
    {
        return newExplosion(entityIn, x, y, z, strength, false, isSmoking);
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    public Explosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking)
    {
        Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

    /**
     * Gets the percentage of real blocks within within a bounding box, along a specified vector.
     */
    public float getBlockDensity(Vec3d vec, AxisAlignedBB bb)
    {
        double d0 = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;

        if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D)
        {
            int j2 = 0;
            int k2 = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0))
            {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1))
                {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2))
                    {
                        double d5 = bb.minX + (bb.maxX - bb.minX) * (double)f;
                        double d6 = bb.minY + (bb.maxY - bb.minY) * (double)f1;
                        double d7 = bb.minZ + (bb.maxZ - bb.minZ) * (double)f2;

                        if (rayTraceBlocks(new Vec3d(d5 + d3, d6, d7 + d4), vec) == null)
                        {
                            ++j2;
                        }

                        ++k2;
                    }
                }
            }

            return (float)j2 / (float)k2;
        }
        else
        {
            return 0.0F;
        }
    }

    /**
     * Attempts to extinguish a fire
     */
    public boolean extinguishFire(@Nullable EntityPlayer player, BlockPos pos, EnumFacing side)
    {
        pos = pos.offset(side);

        if (getBlockState(pos).getBlock() == Blocks.FIRE)
        {
            playEvent(player, 1009, pos, 0);
            setBlockToAir(pos);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * This string is 'All: (number of loaded entities)' Viewable by press ing F3
     */
    public String getDebugLoadedEntities()
    {
        return "All: " + loadedEntityList.size();
    }

    /**
     * Returns the name of the current chunk provider, by calling chunkprovider.makeString()
     */
    public String getProviderName()
    {
        return chunkProvider.makeString();
    }

    @Nullable
    public TileEntity getTileEntity(BlockPos pos)
    {
        if (isOutsideBuildHeight(pos))
        {
            return null;
        }
        else
        {
            TileEntity tileentity2 = null;

            if (processingLoadedTiles)
            {
                tileentity2 = getPendingTileEntityAt(pos);
            }

            if (tileentity2 == null)
            {
                tileentity2 = getChunkFromBlockCoords(pos).getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
            }

            if (tileentity2 == null)
            {
                tileentity2 = getPendingTileEntityAt(pos);
            }

            return tileentity2;
        }
    }

    @Nullable
    private TileEntity getPendingTileEntityAt(BlockPos p_189508_1_)
    {
        for (int j2 = 0; j2 < addedTileEntityList.size(); ++j2)
        {
            TileEntity tileentity2 = addedTileEntityList.get(j2);

            if (!tileentity2.isInvalid() && tileentity2.getPos().equals(p_189508_1_))
            {
                return tileentity2;
            }
        }

        return null;
    }

    public void setTileEntity(BlockPos pos, @Nullable TileEntity tileEntityIn)
    {
        if (!isOutsideBuildHeight(pos))
        {
            if (tileEntityIn != null && !tileEntityIn.isInvalid())
            {
                if (processingLoadedTiles)
                {
                    tileEntityIn.setPos(pos);
                    Iterator<TileEntity> iterator1 = addedTileEntityList.iterator();

                    while (iterator1.hasNext())
                    {
                        TileEntity tileentity2 = iterator1.next();

                        if (tileentity2.getPos().equals(pos))
                        {
                            tileentity2.invalidate();
                            iterator1.remove();
                        }
                    }

                    addedTileEntityList.add(tileEntityIn);
                }
                else
                {
                    getChunkFromBlockCoords(pos).addTileEntity(pos, tileEntityIn);
                    addTileEntity(tileEntityIn);
                }
            }
        }
    }

    public void removeTileEntity(BlockPos pos)
    {
        TileEntity tileentity2 = getTileEntity(pos);

        if (tileentity2 != null && processingLoadedTiles)
        {
            tileentity2.invalidate();
            addedTileEntityList.remove(tileentity2);
        }
        else
        {
            if (tileentity2 != null)
            {
                addedTileEntityList.remove(tileentity2);
                loadedTileEntityList.remove(tileentity2);
                tickableTileEntities.remove(tileentity2);
            }

            getChunkFromBlockCoords(pos).removeTileEntity(pos);
        }
    }

    /**
     * Adds the specified TileEntity to the pending removal list.
     */
    public void markTileEntityForRemoval(TileEntity tileEntityIn)
    {
        tileEntitiesToBeRemoved.add(tileEntityIn);
    }

    public boolean isBlockFullCube(BlockPos pos)
    {
        AxisAlignedBB axisalignedbb = getBlockState(pos).getCollisionBoundingBox(this, pos);
        return axisalignedbb != Block.NULL_AABB && axisalignedbb.getAverageEdgeLength() >= 1.0D;
    }

    /**
     * Checks if a block's material is opaque, and that it takes up a full cube
     */
    public boolean isBlockNormalCube(BlockPos pos, boolean _default)
    {
        if (isOutsideBuildHeight(pos))
        {
            return false;
        }
        else
        {
            Chunk chunk1 = chunkProvider.getLoadedChunk(pos.getX() >> 4, pos.getZ() >> 4);

            if (chunk1 != null && !chunk1.isEmpty())
            {
                IBlockState iblockstate1 = getBlockState(pos);
                return iblockstate1.getMaterial().isOpaque() && iblockstate1.isFullCube();
            }
            else
            {
                return _default;
            }
        }
    }

    /**
     * Called on construction of the World class to setup the initial skylight values
     */
    public void calculateInitialSkylight()
    {
        int j2 = calculateSkylightSubtracted(1.0F);

        if (j2 != skylightSubtracted)
        {
            skylightSubtracted = j2;
        }
    }

    /**
     * first boolean for hostile mobs and second for peaceful mobs
     */
    public void setAllowedSpawnTypes(boolean hostile, boolean peaceful)
    {
        spawnHostileMobs = hostile;
        spawnPeacefulMobs = peaceful;
    }

    /**
     * Runs a single tick for the world
     */
    public void tick()
    {
        updateWeather();
    }

    /**
     * Called from World constructor to set rainingStrength and thunderingStrength
     */
    protected void calculateInitialWeather()
    {
        if (worldInfo.isRaining())
        {
            rainingStrength = 1.0F;

            if (worldInfo.isThundering())
            {
                thunderingStrength = 1.0F;
            }
        }
    }

    /**
     * Updates all weather states.
     */
    protected void updateWeather()
    {
        if (provider.isNether())
        {
            if (!isRemote)
            {
                boolean flag = getGameRules().getBoolean("doWeatherCycle");

                if (flag)
                {
                    int j2 = worldInfo.getCleanWeatherTime();

                    if (j2 > 0)
                    {
                        --j2;
                        worldInfo.setCleanWeatherTime(j2);
                        worldInfo.setThunderTime(worldInfo.isThundering() ? 1 : 2);
                        worldInfo.setRainTime(worldInfo.isRaining() ? 1 : 2);
                    }

                    int k2 = worldInfo.getThunderTime();

                    if (k2 <= 0)
                    {
                        if (worldInfo.isThundering())
                        {
                            worldInfo.setThunderTime(rand.nextInt(12000) + 3600);
                        }
                        else
                        {
                            worldInfo.setThunderTime(rand.nextInt(168000) + 12000);
                        }
                    }
                    else
                    {
                        --k2;
                        worldInfo.setThunderTime(k2);

                        if (k2 <= 0)
                        {
                            worldInfo.setThundering(!worldInfo.isThundering());
                        }
                    }

                    int l2 = worldInfo.getRainTime();

                    if (l2 <= 0)
                    {
                        if (worldInfo.isRaining())
                        {
                            worldInfo.setRainTime(rand.nextInt(12000) + 12000);
                        }
                        else
                        {
                            worldInfo.setRainTime(rand.nextInt(168000) + 12000);
                        }
                    }
                    else
                    {
                        --l2;
                        worldInfo.setRainTime(l2);

                        if (l2 <= 0)
                        {
                            worldInfo.setRaining(!worldInfo.isRaining());
                        }
                    }
                }

                prevThunderingStrength = thunderingStrength;

                if (worldInfo.isThundering())
                {
                    thunderingStrength = (float)((double) thunderingStrength + 0.01D);
                }
                else
                {
                    thunderingStrength = (float)((double) thunderingStrength - 0.01D);
                }

                thunderingStrength = MathHelper.clamp(thunderingStrength, 0.0F, 1.0F);
                prevRainingStrength = rainingStrength;

                if (worldInfo.isRaining())
                {
                    rainingStrength = (float)((double) rainingStrength + 0.01D);
                }
                else
                {
                    rainingStrength = (float)((double) rainingStrength - 0.01D);
                }

                rainingStrength = MathHelper.clamp(rainingStrength, 0.0F, 1.0F);
            }
        }
    }

    protected void playMoodSoundAndCheckLight(int p_147467_1_, int p_147467_2_, Chunk chunkIn)
    {
        chunkIn.enqueueRelightChecks();
    }

    protected void updateBlocks()
    {
    }

    public void immediateBlockTick(BlockPos pos, IBlockState state, Random random)
    {
        scheduledUpdatesAreImmediate = true;
        state.getBlock().updateTick(this, pos, state, random);
        scheduledUpdatesAreImmediate = false;
    }

    public boolean canBlockFreezeWater(BlockPos pos)
    {
        return canBlockFreeze(pos, false);
    }

    public boolean canBlockFreezeNoWater(BlockPos pos)
    {
        return canBlockFreeze(pos, true);
    }

    /**
     * Checks to see if a given block is both water and cold enough to freeze.
     */
    public boolean canBlockFreeze(BlockPos pos, boolean noWaterAdj)
    {
        Biome biome = getBiome(pos);
        float f = biome.getFloatTemperature(pos);

        if (f >= 0.15F)
        {
            return false;
        }
        else
        {
            if (pos.getY() >= 0 && pos.getY() < 256 && getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
            {
                IBlockState iblockstate1 = getBlockState(pos);
                Block block = iblockstate1.getBlock();

                if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && iblockstate1.getValue(BlockLiquid.LEVEL).intValue() == 0)
                {
                    if (!noWaterAdj)
                    {
                        return true;
                    }

                    boolean flag = isWater(pos.west()) && isWater(pos.east()) && isWater(pos.north()) && isWater(pos.south());

                    return !flag;
                }
            }

            return false;
        }
    }

    private boolean isWater(BlockPos pos)
    {
        return getBlockState(pos).getMaterial() == Material.WATER;
    }

    /**
     * Checks to see if a given block can accumulate snow from it snowing
     */
    public boolean canSnowAt(BlockPos pos, boolean checkLight)
    {
        Biome biome = getBiome(pos);
        float f = biome.getFloatTemperature(pos);

        if (f >= 0.15F)
        {
            return false;
        }
        else if (!checkLight)
        {
            return true;
        }
        else
        {
            if (pos.getY() >= 0 && pos.getY() < 256 && getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
            {
                IBlockState iblockstate1 = getBlockState(pos);

                return iblockstate1.getMaterial() == Material.AIR && Blocks.SNOW_LAYER.canPlaceBlockAt(this, pos);
            }

            return false;
        }
    }

    public boolean checkLight(BlockPos pos)
    {
        boolean flag = false;

        if (provider.isNether())
        {
            flag |= checkLightFor(EnumSkyBlock.SKY, pos);
        }

        flag = flag | checkLightFor(EnumSkyBlock.BLOCK, pos);
        return flag;
    }

    /**
     * gets the light level at the supplied position
     */
    private int getRawLight(BlockPos pos, EnumSkyBlock lightType)
    {
        if (lightType == EnumSkyBlock.SKY && canSeeSky(pos))
        {
            return 15;
        }
        else
        {
            IBlockState iblockstate1 = getBlockState(pos);
            int j2 = lightType == EnumSkyBlock.SKY ? 0 : iblockstate1.getLightValue();
            int k2 = iblockstate1.getLightOpacity();

            if (k2 >= 15 && iblockstate1.getLightValue() > 0)
            {
                k2 = 1;
            }

            if (k2 < 1)
            {
                k2 = 1;
            }

            if (k2 >= 15)
            {
                return 0;
            }
            else if (j2 >= 14)
            {
                return j2;
            }
            else
            {
                BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

                try
                {
                    for (EnumFacing enumfacing : EnumFacing.values())
                    {
                        blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing);
                        int l2 = getLightFor(lightType, blockpos$pooledmutableblockpos) - k2;

                        if (l2 > j2)
                        {
                            j2 = l2;
                        }

                        if (j2 >= 14)
                        {
                            int i3 = j2;
                            return i3;
                        }
                    }

                    return j2;
                }
                finally
                {
                    blockpos$pooledmutableblockpos.release();
                }
            }
        }
    }

    public boolean checkLightFor(EnumSkyBlock lightType, BlockPos pos)
    {

        EventRenderWorldLight event = new EventRenderWorldLight(lightType, pos);
        EventManager.call(event);
        if (event.isCancelled()) {
            return false;
        }

        if (!isAreaLoaded(pos, 17, false))
        {
            return false;
        }
        else
        {
            int j2 = 0;
            int k2 = 0;
            theProfiler.startSection("getBrightness");
            int l2 = getLightFor(lightType, pos);
            int i3 = getRawLight(pos, lightType);
            int j3 = pos.getX();
            int k3 = pos.getY();
            int l3 = pos.getZ();

            if (i3 > l2)
            {
                lightUpdateBlockList[k2++] = 133152;
            }
            else if (i3 < l2)
            {
                lightUpdateBlockList[k2++] = 133152 | l2 << 18;

                while (j2 < k2)
                {
                    int i4 = lightUpdateBlockList[j2++];
                    int j4 = (i4 & 63) - 32 + j3;
                    int k4 = (i4 >> 6 & 63) - 32 + k3;
                    int l4 = (i4 >> 12 & 63) - 32 + l3;
                    int i5 = i4 >> 18 & 15;
                    BlockPos blockpos1 = new BlockPos(j4, k4, l4);
                    int j5 = getLightFor(lightType, blockpos1);

                    if (j5 == i5)
                    {
                        setLightFor(lightType, blockpos1, 0);

                        if (i5 > 0)
                        {
                            int k5 = MathHelper.abs(j4 - j3);
                            int l5 = MathHelper.abs(k4 - k3);
                            int i6 = MathHelper.abs(l4 - l3);

                            if (k5 + l5 + i6 < 17)
                            {
                                BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

                                for (EnumFacing enumfacing : EnumFacing.values())
                                {
                                    int j6 = j4 + enumfacing.getFrontOffsetX();
                                    int k6 = k4 + enumfacing.getFrontOffsetY();
                                    int l6 = l4 + enumfacing.getFrontOffsetZ();
                                    blockpos$pooledmutableblockpos.setPos(j6, k6, l6);
                                    int i7 = Math.max(1, getBlockState(blockpos$pooledmutableblockpos).getLightOpacity());
                                    j5 = getLightFor(lightType, blockpos$pooledmutableblockpos);

                                    if (j5 == i5 - i7 && k2 < lightUpdateBlockList.length)
                                    {
                                        lightUpdateBlockList[k2++] = j6 - j3 + 32 | k6 - k3 + 32 << 6 | l6 - l3 + 32 << 12 | i5 - i7 << 18;
                                    }
                                }

                                blockpos$pooledmutableblockpos.release();
                            }
                        }
                    }
                }

                j2 = 0;
            }

            theProfiler.endSection();
            theProfiler.startSection("checkedPosition < toCheckCount");

            while (j2 < k2)
            {
                int j7 = lightUpdateBlockList[j2++];
                int k7 = (j7 & 63) - 32 + j3;
                int l7 = (j7 >> 6 & 63) - 32 + k3;
                int i8 = (j7 >> 12 & 63) - 32 + l3;
                BlockPos blockpos2 = new BlockPos(k7, l7, i8);
                int j8 = getLightFor(lightType, blockpos2);
                int k8 = getRawLight(blockpos2, lightType);

                if (k8 != j8)
                {
                    setLightFor(lightType, blockpos2, k8);

                    if (k8 > j8)
                    {
                        int l8 = Math.abs(k7 - j3);
                        int i9 = Math.abs(l7 - k3);
                        int j9 = Math.abs(i8 - l3);
                        boolean flag = k2 < lightUpdateBlockList.length - 6;

                        if (l8 + i9 + j9 < 17 && flag)
                        {
                            if (getLightFor(lightType, blockpos2.west()) < k8)
                            {
                                lightUpdateBlockList[k2++] = k7 - 1 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                            }

                            if (getLightFor(lightType, blockpos2.east()) < k8)
                            {
                                lightUpdateBlockList[k2++] = k7 + 1 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                            }

                            if (getLightFor(lightType, blockpos2.down()) < k8)
                            {
                                lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 - 1 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                            }

                            if (getLightFor(lightType, blockpos2.up()) < k8)
                            {
                                lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 + 1 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                            }

                            if (getLightFor(lightType, blockpos2.north()) < k8)
                            {
                                lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - 1 - l3 + 32 << 12);
                            }

                            if (getLightFor(lightType, blockpos2.south()) < k8)
                            {
                                lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 + 1 - l3 + 32 << 12);
                            }
                        }
                    }
                }
            }

            theProfiler.endSection();
            return true;
        }
    }

    /**
     * Runs through the list of updates to run and ticks them
     */
    public boolean tickUpdates(boolean p_72955_1_)
    {
        return false;
    }

    @Nullable
    public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunkIn, boolean p_72920_2_)
    {
        return null;
    }

    @Nullable
    public List<NextTickListEntry> getPendingBlockUpdates(StructureBoundingBox structureBB, boolean p_175712_2_)
    {
        return null;
    }

    public List<Entity> getEntitiesWithinAABBExcludingEntity(@Nullable Entity entityIn, AxisAlignedBB bb)
    {
        return getEntitiesInAABBexcluding(entityIn, bb, EntitySelectors.NOT_SPECTATING);
    }

    public List<Entity> getEntitiesInAABBexcluding(@Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate <? super Entity > predicate)
    {
        List<Entity> list = Lists.newArrayList();
        int j2 = MathHelper.floor((boundingBox.minX - 2.0D) / 16.0D);
        int k2 = MathHelper.floor((boundingBox.maxX + 2.0D) / 16.0D);
        int l2 = MathHelper.floor((boundingBox.minZ - 2.0D) / 16.0D);
        int i3 = MathHelper.floor((boundingBox.maxZ + 2.0D) / 16.0D);

        for (int j3 = j2; j3 <= k2; ++j3)
        {
            for (int k3 = l2; k3 <= i3; ++k3)
            {
                if (isChunkLoaded(j3, k3, true))
                {
                    getChunkFromChunkCoords(j3, k3).getEntitiesWithinAABBForEntity(entityIn, boundingBox, list, predicate);
                }
            }
        }

        return list;
    }

    public <T extends Entity> List<T> getEntities(Class <? extends T > entityType, Predicate <? super T > filter)
    {
        List<T> list = Lists.newArrayList();

        for (Entity entity4 : loadedEntityList)
        {
            if (entityType.isAssignableFrom(entity4.getClass()) && filter.apply((T)entity4))
            {
                list.add((T)entity4);
            }
        }

        return list;
    }

    public <T extends Entity> List<T> getPlayers(Class <? extends T > playerType, Predicate <? super T > filter)
    {
        List<T> list = Lists.newArrayList();

        for (Entity entity4 : playerEntities)
        {
            if (playerType.isAssignableFrom(entity4.getClass()) && filter.apply((T)entity4))
            {
                list.add((T)entity4);
            }
        }

        return list;
    }

    public <T extends Entity> List<T> getEntitiesWithinAABB(Class <? extends T > classEntity, AxisAlignedBB bb)
    {
        return getEntitiesWithinAABB(classEntity, bb, EntitySelectors.NOT_SPECTATING);
    }

    public <T extends Entity> List<T> getEntitiesWithinAABB(Class <? extends T > clazz, AxisAlignedBB aabb, @Nullable Predicate <? super T > filter)
    {
        int j2 = MathHelper.floor((aabb.minX - 2.0D) / 16.0D);
        int k2 = MathHelper.ceil((aabb.maxX + 2.0D) / 16.0D);
        int l2 = MathHelper.floor((aabb.minZ - 2.0D) / 16.0D);
        int i3 = MathHelper.ceil((aabb.maxZ + 2.0D) / 16.0D);
        List<T> list = Lists.newArrayList();

        for (int j3 = j2; j3 < k2; ++j3)
        {
            for (int k3 = l2; k3 < i3; ++k3)
            {
                if (isChunkLoaded(j3, k3, true))
                {
                    getChunkFromChunkCoords(j3, k3).getEntitiesOfTypeWithinAAAB(clazz, aabb, list, filter);
                }
            }
        }

        return list;
    }

    @Nullable
    public <T extends Entity> T findNearestEntityWithinAABB(Class <? extends T > entityType, AxisAlignedBB aabb, T closestTo)
    {
        List<T> list = getEntitiesWithinAABB(entityType, aabb);
        T t = null;
        double d0 = Double.MAX_VALUE;

        for (int j2 = 0; j2 < list.size(); ++j2)
        {
            T t1 = list.get(j2);

            if (t1 != closestTo && EntitySelectors.NOT_SPECTATING.apply(t1))
            {
                double d1 = closestTo.getDistanceSqToEntity(t1);

                if (d1 <= d0)
                {
                    t = t1;
                    d0 = d1;
                }
            }
        }

        return t;
    }

    @Nullable

    /**
     * Returns the Entity with the given ID, or null if it doesn't exist in this World.
     */
    public Entity getEntityByID(int id)
    {
        return entitiesById.lookup(id);
    }

    public List<Entity> getLoadedEntityList()
    {
        return loadedEntityList;
    }

    public void markChunkDirty(BlockPos pos, TileEntity unusedTileEntity)
    {
        if (isBlockLoaded(pos))
        {
            getChunkFromBlockCoords(pos).setChunkModified();
        }
    }

    /**
     * Counts how many entities of an entity class exist in the world.
     */
    public int countEntities(Class<?> entityType)
    {
        int j2 = 0;

        for (Entity entity4 : loadedEntityList)
        {
            if ((!(entity4 instanceof EntityLiving) || !((EntityLiving)entity4).isNoDespawnRequired()) && entityType.isAssignableFrom(entity4.getClass()))
            {
                ++j2;
            }
        }

        return j2;
    }

    public void loadEntities(Collection<Entity> entityCollection)
    {
        loadedEntityList.addAll(entityCollection);

        for (Entity entity4 : entityCollection)
        {
            onEntityAdded(entity4);
        }
    }

    public void unloadEntities(Collection<Entity> entityCollection)
    {
        unloadedEntityList.addAll(entityCollection);
    }

    public boolean mayPlace(Block p_190527_1_, BlockPos p_190527_2_, boolean p_190527_3_, EnumFacing p_190527_4_, @Nullable Entity p_190527_5_)
    {
        IBlockState iblockstate1 = getBlockState(p_190527_2_);
        AxisAlignedBB axisalignedbb = p_190527_3_ ? null : p_190527_1_.getDefaultState().getCollisionBoundingBox(this, p_190527_2_);

        if (axisalignedbb != Block.NULL_AABB && !checkNoEntityCollision(axisalignedbb.offset(p_190527_2_), p_190527_5_))
        {
            return false;
        }
        else if (iblockstate1.getMaterial() == Material.CIRCUITS && p_190527_1_ == Blocks.ANVIL)
        {
            return true;
        }
        else
        {
            return iblockstate1.getMaterial().isReplaceable() && p_190527_1_.canPlaceBlockOnSide(this, p_190527_2_, p_190527_4_);
        }
    }

    public int getSeaLevel()
    {
        return seaLevel;
    }

    /**
     * Warning this value may not be respected in all cases as it is still hardcoded in many places.
     */
    public void setSeaLevel(int seaLevelIn)
    {
        seaLevel = seaLevelIn;
    }

    public int getStrongPower(BlockPos pos, EnumFacing direction)
    {
        return getBlockState(pos).getStrongPower(this, pos, direction);
    }

    public WorldType getWorldType()
    {
        return worldInfo.getTerrainType();
    }

    /**
     * Returns the single highest strong power out of all directions using getStrongPower(BlockPos, EnumFacing)
     */
    public int getStrongPower(BlockPos pos)
    {
        int j2 = 0;
        j2 = Math.max(j2, getStrongPower(pos.down(), EnumFacing.DOWN));

        if (j2 >= 15)
        {
            return j2;
        }
        else
        {
            j2 = Math.max(j2, getStrongPower(pos.up(), EnumFacing.UP));

            if (j2 >= 15)
            {
                return j2;
            }
            else
            {
                j2 = Math.max(j2, getStrongPower(pos.north(), EnumFacing.NORTH));

                if (j2 >= 15)
                {
                    return j2;
                }
                else
                {
                    j2 = Math.max(j2, getStrongPower(pos.south(), EnumFacing.SOUTH));

                    if (j2 >= 15)
                    {
                        return j2;
                    }
                    else
                    {
                        j2 = Math.max(j2, getStrongPower(pos.west(), EnumFacing.WEST));

                        if (j2 >= 15)
                        {
                            return j2;
                        }
                        else
                        {
                            j2 = Math.max(j2, getStrongPower(pos.east(), EnumFacing.EAST));
                            return j2;
                        }
                    }
                }
            }
        }
    }

    public boolean isSidePowered(BlockPos pos, EnumFacing side)
    {
        return getRedstonePower(pos, side) > 0;
    }

    public int getRedstonePower(BlockPos pos, EnumFacing facing)
    {
        IBlockState iblockstate1 = getBlockState(pos);
        return iblockstate1.isNormalCube() ? getStrongPower(pos) : iblockstate1.getWeakPower(this, pos, facing);
    }

    public boolean isBlockPowered(BlockPos pos)
    {
        if (getRedstonePower(pos.down(), EnumFacing.DOWN) > 0)
        {
            return true;
        }
        else if (getRedstonePower(pos.up(), EnumFacing.UP) > 0)
        {
            return true;
        }
        else if (getRedstonePower(pos.north(), EnumFacing.NORTH) > 0)
        {
            return true;
        }
        else if (getRedstonePower(pos.south(), EnumFacing.SOUTH) > 0)
        {
            return true;
        }
        else if (getRedstonePower(pos.west(), EnumFacing.WEST) > 0)
        {
            return true;
        }
        else
        {
            return getRedstonePower(pos.east(), EnumFacing.EAST) > 0;
        }
    }

    /**
     * Checks if the specified block or its neighbors are powered by a neighboring block. Used by blocks like TNT and
     * Doors.
     */
    public int isBlockIndirectlyGettingPowered(BlockPos pos)
    {
        int j2 = 0;

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            int k2 = getRedstonePower(pos.offset(enumfacing), enumfacing);

            if (k2 >= 15)
            {
                return 15;
            }

            if (k2 > j2)
            {
                j2 = k2;
            }
        }

        return j2;
    }

    @Nullable

    /**
     * Gets the closest player to the entity within the specified distance.
     */
    public EntityPlayer getClosestPlayerToEntity(Entity entityIn, double distance)
    {
        return getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance, false);
    }

    @Nullable
    public EntityPlayer getNearestPlayerNotCreative(Entity entityIn, double distance)
    {
        return getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance, true);
    }

    @Nullable
    public EntityPlayer getClosestPlayer(double posX, double posY, double posZ, double distance, boolean spectator)
    {
        Predicate<Entity> predicate = spectator ? EntitySelectors.CAN_AI_TARGET : EntitySelectors.NOT_SPECTATING;
        return func_190525_a(posX, posY, posZ, distance, predicate);
    }

    @Nullable
    public EntityPlayer func_190525_a(double p_190525_1_, double p_190525_3_, double p_190525_5_, double p_190525_7_, Predicate<Entity> p_190525_9_)
    {
        double d0 = -1.0D;
        EntityPlayer entityplayer = null;

        for (int j2 = 0; j2 < playerEntities.size(); ++j2)
        {
            EntityPlayer entityplayer1 = playerEntities.get(j2);

            if (p_190525_9_.apply(entityplayer1))
            {
                double d1 = entityplayer1.getDistanceSq(p_190525_1_, p_190525_3_, p_190525_5_);

                if ((p_190525_7_ < 0.0D || d1 < p_190525_7_ * p_190525_7_) && (d0 == -1.0D || d1 < d0))
                {
                    d0 = d1;
                    entityplayer = entityplayer1;
                }
            }
        }

        return entityplayer;
    }

    public boolean isAnyPlayerWithinRangeAt(double x, double y, double z, double range)
    {
        for (int j2 = 0; j2 < playerEntities.size(); ++j2)
        {
            EntityPlayer entityplayer = playerEntities.get(j2);

            if (EntitySelectors.NOT_SPECTATING.apply(entityplayer))
            {
                double d0 = entityplayer.getDistanceSq(x, y, z);

                if (range < 0.0D || d0 < range * range)
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    public EntityPlayer getNearestAttackablePlayer(Entity entityIn, double maxXZDistance, double maxYDistance)
    {
        return getNearestAttackablePlayer(entityIn.posX, entityIn.posY, entityIn.posZ, maxXZDistance, maxYDistance, null, null);
    }

    @Nullable
    public EntityPlayer getNearestAttackablePlayer(BlockPos pos, double maxXZDistance, double maxYDistance)
    {
        return getNearestAttackablePlayer((float)pos.getX() + 0.5F, (float)pos.getY() + 0.5F, (float)pos.getZ() + 0.5F, maxXZDistance, maxYDistance, null, null);
    }

    @Nullable
    public EntityPlayer getNearestAttackablePlayer(double posX, double posY, double posZ, double maxXZDistance, double maxYDistance, @Nullable Function<EntityPlayer, Double> playerToDouble, @Nullable Predicate<EntityPlayer> p_184150_12_)
    {
        double d0 = -1.0D;
        EntityPlayer entityplayer = null;

        for (int j2 = 0; j2 < playerEntities.size(); ++j2)
        {
            EntityPlayer entityplayer1 = playerEntities.get(j2);

            if (!entityplayer1.capabilities.disableDamage && entityplayer1.isEntityAlive() && !entityplayer1.isSpectator() && (p_184150_12_ == null || p_184150_12_.apply(entityplayer1)))
            {
                double d1 = entityplayer1.getDistanceSq(posX, entityplayer1.posY, posZ);
                double d2 = maxXZDistance;

                if (entityplayer1.isSneaking())
                {
                    d2 = maxXZDistance * 0.800000011920929D;
                }

                if (entityplayer1.isInvisible())
                {
                    float f = entityplayer1.getArmorVisibility();

                    if (f < 0.1F)
                    {
                        f = 0.1F;
                    }

                    d2 *= 0.7F * f;
                }

                if (playerToDouble != null)
                {
                    d2 *= MoreObjects.firstNonNull(playerToDouble.apply(entityplayer1), Double.valueOf(1.0D)).doubleValue();
                }

                if ((maxYDistance < 0.0D || Math.abs(entityplayer1.posY - posY) < maxYDistance * maxYDistance) && (maxXZDistance < 0.0D || d1 < d2 * d2) && (d0 == -1.0D || d1 < d0))
                {
                    d0 = d1;
                    entityplayer = entityplayer1;
                }
            }
        }

        return entityplayer;
    }

    @Nullable

    /**
     * Find a player by name in this world.
     */
    public EntityPlayer getPlayerEntityByName(String name)
    {
        for (int j2 = 0; j2 < playerEntities.size(); ++j2)
        {
            EntityPlayer entityplayer = playerEntities.get(j2);

            if (name.equals(entityplayer.getName()))
            {
                return entityplayer;
            }
        }

        return null;
    }

    @Nullable
    public EntityPlayer getPlayerEntityByUUID(UUID uuid)
    {
        for (int j2 = 0; j2 < playerEntities.size(); ++j2)
        {
            EntityPlayer entityplayer = playerEntities.get(j2);

            if (uuid.equals(entityplayer.getUniqueID()))
            {
                return entityplayer;
            }
        }

        return null;
    }

    /**
     * If on MP, sends a quitting packet.
     */
    public void quit()
    {
    }

    /**
     * Checks whether the session lock file was modified by another process
     */
    public void checkSessionLock() throws MinecraftException
    {
        saveHandler.checkSessionLock();
    }

    public void setTotalWorldTime(long worldTime)
    {
        worldInfo.setWorldTotalTime(worldTime);
    }

    /**
     * gets the random world seed
     */
    public long getSeed()
    {
        return worldInfo.getSeed();
    }

    public long getTotalWorldTime()
    {
        return worldInfo.getWorldTotalTime();
    }

    public long getWorldTime()
    {
        return worldInfo.getWorldTime();
    }

    /**
     * Sets the world time.
     */
    public void setWorldTime(long time)
    {
        worldInfo.setWorldTime(time);
    }

    /**
     * Gets the spawn point in the world
     */
    public BlockPos getSpawnPoint()
    {
        BlockPos blockpos1 = new BlockPos(worldInfo.getSpawnX(), worldInfo.getSpawnY(), worldInfo.getSpawnZ());

        if (!getWorldBorder().contains(blockpos1))
        {
            blockpos1 = getHeight(new BlockPos(getWorldBorder().getCenterX(), 0.0D, getWorldBorder().getCenterZ()));
        }

        return blockpos1;
    }

    public void setSpawnPoint(BlockPos pos)
    {
        worldInfo.setSpawn(pos);
    }

    /**
     * spwans an entity and loads surrounding chunks
     */
    public void joinEntityInSurroundings(Entity entityIn)
    {
        int j2 = MathHelper.floor(entityIn.posX / 16.0D);
        int k2 = MathHelper.floor(entityIn.posZ / 16.0D);
        int l2 = 2;

        for (int i3 = -2; i3 <= 2; ++i3)
        {
            for (int j3 = -2; j3 <= 2; ++j3)
            {
                getChunkFromChunkCoords(j2 + i3, k2 + j3);
            }
        }

        if (!loadedEntityList.contains(entityIn))
        {
            loadedEntityList.add(entityIn);
        }
    }

    public boolean isBlockModifiable(EntityPlayer player, BlockPos pos)
    {
        return true;
    }

    /**
     * sends a Packet 38 (Entity Status) to all tracked players of that entity
     */
    public void setEntityState(Entity entityIn, byte state)
    {
    }

    /**
     * gets the world's chunk provider
     */
    public IChunkProvider getChunkProvider()
    {
        return chunkProvider;
    }

    public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam)
    {
        getBlockState(pos).onBlockEventReceived(this, pos, eventID, eventParam);
    }

    /**
     * Returns this world's current save handler
     */
    public ISaveHandler getSaveHandler()
    {
        return saveHandler;
    }

    /**
     * Returns the world's WorldInfo object
     */
    public WorldInfo getWorldInfo()
    {
        return worldInfo;
    }

    /**
     * Gets the GameRules instance.
     */
    public GameRules getGameRules()
    {
        return worldInfo.getGameRulesInstance();
    }

    /**
     * Updates the flag that indicates whether or not all players in the world are sleeping.
     */
    public void updateAllPlayersSleepingFlag()
    {
    }

    public float getThunderStrength(float delta)
    {
        return (prevThunderingStrength + (thunderingStrength - prevThunderingStrength) * delta) * getRainStrength(delta);
    }

    /**
     * Sets the strength of the thunder.
     */
    public void setThunderStrength(float strength)
    {
        prevThunderingStrength = strength;
        thunderingStrength = strength;
    }

    /**
     * Returns rain strength.
     */
    public float getRainStrength(float delta)
    {
        return prevRainingStrength + (rainingStrength - prevRainingStrength) * delta;
    }

    /**
     * Sets the strength of the rain.
     */
    public void setRainStrength(float strength)
    {
        prevRainingStrength = strength;
        rainingStrength = strength;
    }

    /**
     * Returns true if the current thunder strength (weighted with the rain strength) is greater than 0.9
     */
    public boolean isThundering()
    {
        return (double) getThunderStrength(1.0F) > 0.9D;
    }

    /**
     * Returns true if the current rain strength is greater than 0.2
     */
    public boolean isRaining()
    {
        return (double) getRainStrength(1.0F) > 0.2D;
    }

    /**
     * Check if precipitation is currently happening at a position
     */
    public boolean isRainingAt(BlockPos strikePosition)
    {
        if (!isRaining())
        {
            return false;
        }
        else if (!canSeeSky(strikePosition))
        {
            return false;
        }
        else if (getPrecipitationHeight(strikePosition).getY() > strikePosition.getY())
        {
            return false;
        }
        else
        {
            Biome biome = getBiome(strikePosition);

            if (biome.getEnableSnow())
            {
                return false;
            }
            else
            {
                return !canSnowAt(strikePosition, false) && biome.canRain();
            }
        }
    }

    public boolean isBlockinHighHumidity(BlockPos pos)
    {
        Biome biome = getBiome(pos);
        return biome.isHighHumidity();
    }

    @Nullable
    public MapStorage getMapStorage()
    {
        return mapStorage;
    }

    /**
     * Assigns the given String id to the given MapDataBase using the MapStorage, removing any existing ones of the same
     * id.
     */
    public void setItemData(String dataID, WorldSavedData worldSavedDataIn)
    {
        mapStorage.setData(dataID, worldSavedDataIn);
    }

    @Nullable

    /**
     * Loads an existing MapDataBase corresponding to the given String id from disk using the MapStorage, instantiating
     * the given Class, or returns null if none such file exists.
     */
    public WorldSavedData loadItemData(Class <? extends WorldSavedData > clazz, String dataID)
    {
        return mapStorage.getOrLoadData(clazz, dataID);
    }

    /**
     * Returns an unique new data id from the MapStorage for the given prefix and saves the idCounts map to the
     * 'idcounts' file.
     */
    public int getUniqueDataId(String key)
    {
        return mapStorage.getUniqueDataId(key);
    }

    public void playBroadcastSound(int id, BlockPos pos, int data)
    {
        for (int j2 = 0; j2 < eventListeners.size(); ++j2)
        {
            eventListeners.get(j2).broadcastSound(id, pos, data);
        }
    }

    public void playEvent(int type, BlockPos pos, int data)
    {
        playEvent(null, type, pos, data);
    }

    public void playEvent(@Nullable EntityPlayer player, int type, BlockPos pos, int data)
    {
        try
        {
            for (int j2 = 0; j2 < eventListeners.size(); ++j2)
            {
                eventListeners.get(j2).playEvent(player, type, pos, data);
            }
        }
        catch (Throwable throwable3)
        {
            CrashReport crashreport3 = CrashReport.makeCrashReport(throwable3, "Playing level event");
            CrashReportCategory crashreportcategory3 = crashreport3.makeCategory("Level event being played");
            crashreportcategory3.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(pos));
            crashreportcategory3.addCrashSection("Event source", player);
            crashreportcategory3.addCrashSection("Event type", Integer.valueOf(type));
            crashreportcategory3.addCrashSection("Event data", Integer.valueOf(data));
            throw new ReportedException(crashreport3);
        }
    }

    /**
     * Returns maximum world height.
     */
    public int getHeight()
    {
        return 256;
    }

    /**
     * Returns current world height.
     */
    public int getActualHeight()
    {
        return provider.getHasNoSky() ? 128 : 256;
    }

    /**
     * puts the World Random seed to a specific state dependant on the inputs
     */
    public Random setRandomSeed(int p_72843_1_, int p_72843_2_, int p_72843_3_)
    {
        long j2 = (long)p_72843_1_ * 341873128712L + (long)p_72843_2_ * 132897987541L + getWorldInfo().getSeed() + (long)p_72843_3_;
        rand.setSeed(j2);
        return rand;
    }

    /**
     * Returns horizon height for use in rendering the sky.
     */
    public double getHorizon()
    {
        return worldInfo.getTerrainType() == WorldType.FLAT ? 0.0D : 63.0D;
    }

    /**
     * Adds some basic stats of the world to the given crash report.
     */
    public CrashReportCategory addWorldInfoToCrashReport(CrashReport report)
    {
        CrashReportCategory crashreportcategory3 = report.makeCategoryDepth("Affected level", 1);
        crashreportcategory3.addCrashSection("Level name", worldInfo == null ? "????" : worldInfo.getWorldName());
        crashreportcategory3.setDetail("All players", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return playerEntities.size() + " total; " + playerEntities;
            }
        });
        crashreportcategory3.setDetail("Chunk stats", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return chunkProvider.makeString();
            }
        });

        try
        {
            worldInfo.addToCrashReport(crashreportcategory3);
        }
        catch (Throwable throwable3)
        {
            crashreportcategory3.addCrashSectionThrowable("Level Data Unobtainable", throwable3);
        }

        return crashreportcategory3;
    }

    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress)
    {
        for (int j2 = 0; j2 < eventListeners.size(); ++j2)
        {
            IWorldEventListener iworldeventlistener = eventListeners.get(j2);
            iworldeventlistener.sendBlockBreakProgress(breakerId, pos, progress);
        }
    }

    /**
     * returns a calendar object containing the current date
     */
    public Calendar getCurrentDate()
    {
        if (getTotalWorldTime() % 600L == 0L)
        {
            theCalendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
        }

        return theCalendar;
    }

    public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, @Nullable NBTTagCompound compund)
    {
    }

    public Scoreboard getScoreboard()
    {
        return worldScoreboard;
    }

    public void updateComparatorOutputLevel(BlockPos pos, Block blockIn)
    {
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos blockpos1 = pos.offset(enumfacing);

            if (isBlockLoaded(blockpos1))
            {
                IBlockState iblockstate1 = getBlockState(blockpos1);

                if (Blocks.UNPOWERED_COMPARATOR.isSameDiode(iblockstate1))
                {
                    iblockstate1.neighborChanged(this, blockpos1, blockIn, pos);
                }
                else if (iblockstate1.isNormalCube())
                {
                    blockpos1 = blockpos1.offset(enumfacing);
                    iblockstate1 = getBlockState(blockpos1);

                    if (Blocks.UNPOWERED_COMPARATOR.isSameDiode(iblockstate1))
                    {
                        iblockstate1.neighborChanged(this, blockpos1, blockIn, pos);
                    }
                }
            }
        }
    }

    public DifficultyInstance getDifficultyForLocation(BlockPos pos)
    {
        long j2 = 0L;
        float f = 0.0F;

        if (isBlockLoaded(pos))
        {
            f = getCurrentMoonPhaseFactor();
            j2 = getChunkFromBlockCoords(pos).getInhabitedTime();
        }

        return new DifficultyInstance(getDifficulty(), getWorldTime(), j2, f);
    }

    public EnumDifficulty getDifficulty()
    {
        return getWorldInfo().getDifficulty();
    }

    public int getSkylightSubtracted()
    {
        return skylightSubtracted;
    }

    public void setSkylightSubtracted(int newSkylightSubtracted)
    {
        skylightSubtracted = newSkylightSubtracted;
    }

    public int getLastLightningBolt()
    {
        return lastLightningBolt;
    }

    public void setLastLightningBolt(int lastLightningBoltIn)
    {
        lastLightningBolt = lastLightningBoltIn;
    }

    public VillageCollection getVillageCollection()
    {
        return villageCollectionObj;
    }

    public WorldBorder getWorldBorder()
    {
        return worldBorder;
    }

    /**
     * Returns true if the chunk is located near the spawn point
     */
    public boolean isSpawnChunk(int x, int z)
    {
        BlockPos blockpos1 = getSpawnPoint();
        int j2 = x * 16 + 8 - blockpos1.getX();
        int k2 = z * 16 + 8 - blockpos1.getZ();
        int l2 = 128;
        return j2 >= -128 && j2 <= 128 && k2 >= -128 && k2 <= 128;
    }

    public void sendPacketToServer(Packet<?> packetIn)
    {
        throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
    }

    public LootTableManager getLootTableManager()
    {
        return lootTable;
    }

    @Nullable
    public BlockPos func_190528_a(String p_190528_1_, BlockPos p_190528_2_, boolean p_190528_3_)
    {
        return null;
    }
}
