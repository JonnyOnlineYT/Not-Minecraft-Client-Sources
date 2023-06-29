package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.INpc;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Progress;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ReportedException;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageCollection;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedDataCallableSave;
import net.minecraft.world.storage.loot.LootTableManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldServer extends World implements IThreadListener
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final MinecraftServer mcServer;

    /** The entity tracker for this server world. */
    private final EntityTracker theEntityTracker;

    /** The player chunk map for this server world. */
    private final PlayerChunkMap thePlayerManager;
    private final Set<NextTickListEntry> pendingTickListEntriesHashSet = Sets.newHashSet();
    private final TreeSet<NextTickListEntry> pendingTickListEntriesTreeSet = new TreeSet<NextTickListEntry>();
    private final Map<UUID, Entity> entitiesByUuid = Maps.newHashMap();

    /** Whether level saving is disabled or not */
    public boolean disableLevelSaving;

    /** is false if there are no players */
    private boolean allPlayersSleeping;
    private int updateEntityTick;

    /**
     * the teleporter to use when the entity is being transferred into the dimension
     */
    private final Teleporter worldTeleporter;
    private final WorldEntitySpawner entitySpawner = new WorldEntitySpawner();
    protected final VillageSiege villageSiege = new VillageSiege(this);
    private final WorldServer.ServerBlockEventList[] blockEventQueue = {new ServerBlockEventList(), new ServerBlockEventList()};
    private int blockEventCacheIndex;
    private final List<NextTickListEntry> pendingTickListEntriesThisTick = Lists.newArrayList();

    public WorldServer(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo info, int dimensionId, Profiler profilerIn)
    {
        super(saveHandlerIn, info, DimensionType.getById(dimensionId).createDimension(), profilerIn, false);
        mcServer = server;
        theEntityTracker = new EntityTracker(this);
        thePlayerManager = new PlayerChunkMap(this);
        provider.registerWorld(this);
        chunkProvider = createChunkProvider();
        worldTeleporter = new Teleporter(this);
        calculateInitialSkylight();
        calculateInitialWeather();
        getWorldBorder().setSize(server.getMaxWorldSize());
    }

    public World init()
    {
        mapStorage = new MapStorage(saveHandler);
        String s = VillageCollection.fileNameForProvider(provider);
        VillageCollection villagecollection = (VillageCollection) mapStorage.getOrLoadData(VillageCollection.class, s);

        if (villagecollection == null)
        {
            villageCollectionObj = new VillageCollection(this);
            mapStorage.setData(s, villageCollectionObj);
        }
        else
        {
            villageCollectionObj = villagecollection;
            villageCollectionObj.setWorldsForAll(this);
        }

        worldScoreboard = new ServerScoreboard(mcServer);
        ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData) mapStorage.getOrLoadData(ScoreboardSaveData.class, "scoreboard");

        if (scoreboardsavedata == null)
        {
            scoreboardsavedata = new ScoreboardSaveData();
            mapStorage.setData("scoreboard", scoreboardsavedata);
        }

        scoreboardsavedata.setScoreboard(worldScoreboard);
        ((ServerScoreboard) worldScoreboard).addDirtyRunnable(new WorldSavedDataCallableSave(scoreboardsavedata));
        lootTable = new LootTableManager(new File(new File(saveHandler.getWorldDirectory(), "data"), "loot_tables"));
        field_191951_C = new AdvancementManager(new File(new File(saveHandler.getWorldDirectory(), "data"), "advancements"));
        field_193036_D = new FunctionManager(new File(new File(saveHandler.getWorldDirectory(), "data"), "functions"), mcServer);
        getWorldBorder().setCenter(worldInfo.getBorderCenterX(), worldInfo.getBorderCenterZ());
        getWorldBorder().setDamageAmount(worldInfo.getBorderDamagePerBlock());
        getWorldBorder().setDamageBuffer(worldInfo.getBorderSafeZone());
        getWorldBorder().setWarningDistance(worldInfo.getBorderWarningDistance());
        getWorldBorder().setWarningTime(worldInfo.getBorderWarningTime());

        if (worldInfo.getBorderLerpTime() > 0L)
        {
            getWorldBorder().setTransition(worldInfo.getBorderSize(), worldInfo.getBorderLerpTarget(), worldInfo.getBorderLerpTime());
        }
        else
        {
            getWorldBorder().setTransition(worldInfo.getBorderSize());
        }

        return this;
    }

    /**
     * Runs a single tick for the world
     */
    public void tick()
    {
        super.tick();

        if (getWorldInfo().isHardcoreModeEnabled() && getDifficulty() != EnumDifficulty.HARD)
        {
            getWorldInfo().setDifficulty(EnumDifficulty.HARD);
        }

        provider.getBiomeProvider().cleanupCache();

        if (areAllPlayersAsleep())
        {
            if (getGameRules().getBoolean("doDaylightCycle"))
            {
                long i = worldInfo.getWorldTime() + 24000L;
                worldInfo.setWorldTime(i - i % 24000L);
            }

            wakeAllPlayers();
        }

        theProfiler.startSection("mobSpawner");

        if (getGameRules().getBoolean("doMobSpawning") && worldInfo.getTerrainType() != WorldType.DEBUG_WORLD)
        {
            entitySpawner.findChunksForSpawning(this, spawnHostileMobs, spawnPeacefulMobs, worldInfo.getWorldTotalTime() % 400L == 0L);
        }

        theProfiler.endStartSection("chunkSource");
        chunkProvider.unloadQueuedChunks();
        int j = calculateSkylightSubtracted(1.0F);

        if (j != getSkylightSubtracted())
        {
            setSkylightSubtracted(j);
        }

        worldInfo.setWorldTotalTime(worldInfo.getWorldTotalTime() + 1L);

        if (getGameRules().getBoolean("doDaylightCycle"))
        {
            worldInfo.setWorldTime(worldInfo.getWorldTime() + 1L);
        }

        theProfiler.endStartSection("tickPending");
        tickUpdates(false);
        theProfiler.endStartSection("tickBlocks");
        updateBlocks();
        theProfiler.endStartSection("chunkMap");
        thePlayerManager.tick();
        theProfiler.endStartSection("village");
        villageCollectionObj.tick();
        villageSiege.tick();
        theProfiler.endStartSection("portalForcer");
        worldTeleporter.removeStalePortalLocations(getTotalWorldTime());
        theProfiler.endSection();
        sendQueuedBlockEvents();
    }

    @Nullable
    public Biome.SpawnListEntry getSpawnListEntryForTypeAt(EnumCreatureType creatureType, BlockPos pos)
    {
        List<Biome.SpawnListEntry> list = getChunkProvider().getPossibleCreatures(creatureType, pos);
        return list != null && !list.isEmpty() ? WeightedRandom.getRandomItem(rand, list) : null;
    }

    public boolean canCreatureTypeSpawnHere(EnumCreatureType creatureType, Biome.SpawnListEntry spawnListEntry, BlockPos pos)
    {
        List<Biome.SpawnListEntry> list = getChunkProvider().getPossibleCreatures(creatureType, pos);
        return list != null && !list.isEmpty() && list.contains(spawnListEntry);
    }

    /**
     * Updates the flag that indicates whether or not all players in the world are sleeping.
     */
    public void updateAllPlayersSleepingFlag()
    {
        allPlayersSleeping = false;

        if (!playerEntities.isEmpty())
        {
            int i = 0;
            int j = 0;

            for (EntityPlayer entityplayer : playerEntities)
            {
                if (entityplayer.isSpectator())
                {
                    ++i;
                }
                else if (entityplayer.isPlayerSleeping())
                {
                    ++j;
                }
            }

            allPlayersSleeping = j > 0 && j >= playerEntities.size() - i;
        }
    }

    protected void wakeAllPlayers()
    {
        allPlayersSleeping = false;

        for (EntityPlayer entityplayer : playerEntities.stream().filter(EntityPlayer::isPlayerSleeping).collect(Collectors.toList()))
        {
            entityplayer.wakeUpPlayer(false, false, true);
        }

        if (getGameRules().getBoolean("doWeatherCycle"))
        {
            resetRainAndThunder();
        }
    }

    /**
     * Clears the current rain and thunder weather states.
     */
    private void resetRainAndThunder()
    {
        worldInfo.setRainTime(0);
        worldInfo.setRaining(false);
        worldInfo.setThunderTime(0);
        worldInfo.setThundering(false);
    }

    /**
     * Checks if all players in this world are sleeping.
     */
    public boolean areAllPlayersAsleep()
    {
        if (allPlayersSleeping && !isRemote)
        {
            for (EntityPlayer entityplayer : playerEntities)
            {
                if (!entityplayer.isSpectator() && !entityplayer.isPlayerFullyAsleep())
                {
                    return false;
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Sets a new spawn location by finding an uncovered block at a random (x,z) location in the chunk.
     */
    public void setInitialSpawnLocation()
    {
        if (worldInfo.getSpawnY() <= 0)
        {
            worldInfo.setSpawnY(getSeaLevel() + 1);
        }

        int i = worldInfo.getSpawnX();
        int j = worldInfo.getSpawnZ();
        int k = 0;

        while (getGroundAboveSeaLevel(new BlockPos(i, 0, j)).getMaterial() == Material.AIR)
        {
            i += rand.nextInt(8) - rand.nextInt(8);
            j += rand.nextInt(8) - rand.nextInt(8);
            ++k;

            if (k == 10000)
            {
                break;
            }
        }

        worldInfo.setSpawnX(i);
        worldInfo.setSpawnZ(j);
    }

    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty)
    {
        return getChunkProvider().chunkExists(x, z);
    }

    protected void playerCheckLight()
    {
        theProfiler.startSection("playerCheckLight");

        if (!playerEntities.isEmpty())
        {
            int i = rand.nextInt(playerEntities.size());
            EntityPlayer entityplayer = playerEntities.get(i);
            int j = MathHelper.floor(entityplayer.posX) + rand.nextInt(11) - 5;
            int k = MathHelper.floor(entityplayer.posY) + rand.nextInt(11) - 5;
            int l = MathHelper.floor(entityplayer.posZ) + rand.nextInt(11) - 5;
            checkLight(new BlockPos(j, k, l));
        }

        theProfiler.endSection();
    }

    protected void updateBlocks()
    {
        playerCheckLight();

        if (worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
        {
            Iterator<Chunk> iterator1 = thePlayerManager.getChunkIterator();

            while (iterator1.hasNext())
            {
                iterator1.next().onTick(false);
            }
        }
        else
        {
            int i = getGameRules().getInt("randomTickSpeed");
            boolean flag = isRaining();
            boolean flag1 = isThundering();
            theProfiler.startSection("pollingChunks");

            for (Iterator<Chunk> iterator = thePlayerManager.getChunkIterator(); iterator.hasNext(); theProfiler.endSection())
            {
                theProfiler.startSection("getChunk");
                Chunk chunk = iterator.next();
                int j = chunk.xPosition * 16;
                int k = chunk.zPosition * 16;
                theProfiler.endStartSection("checkNextLight");
                chunk.enqueueRelightChecks();
                theProfiler.endStartSection("tickChunk");
                chunk.onTick(false);
                theProfiler.endStartSection("thunder");

                if (flag && flag1 && rand.nextInt(100000) == 0)
                {
                    updateLCG = updateLCG * 3 + 1013904223;
                    int l = updateLCG >> 2;
                    BlockPos blockpos = adjustPosToNearbyEntity(new BlockPos(j + (l & 15), 0, k + (l >> 8 & 15)));

                    if (isRainingAt(blockpos))
                    {
                        DifficultyInstance difficultyinstance = getDifficultyForLocation(blockpos);

                        if (getGameRules().getBoolean("doMobSpawning") && rand.nextDouble() < (double)difficultyinstance.getAdditionalDifficulty() * 0.01D)
                        {
                            EntitySkeletonHorse entityskeletonhorse = new EntitySkeletonHorse(this);
                            entityskeletonhorse.func_190691_p(true);
                            entityskeletonhorse.setGrowingAge(0);
                            entityskeletonhorse.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                            spawnEntityInWorld(entityskeletonhorse);
                            addWeatherEffect(new EntityLightningBolt(this, blockpos.getX(), blockpos.getY(), blockpos.getZ(), true));
                        }
                        else
                        {
                            addWeatherEffect(new EntityLightningBolt(this, blockpos.getX(), blockpos.getY(), blockpos.getZ(), false));
                        }
                    }
                }

                theProfiler.endStartSection("iceandsnow");

                if (rand.nextInt(16) == 0)
                {
                    updateLCG = updateLCG * 3 + 1013904223;
                    int j2 = updateLCG >> 2;
                    BlockPos blockpos1 = getPrecipitationHeight(new BlockPos(j + (j2 & 15), 0, k + (j2 >> 8 & 15)));
                    BlockPos blockpos2 = blockpos1.down();

                    if (canBlockFreezeNoWater(blockpos2))
                    {
                        setBlockState(blockpos2, Blocks.ICE.getDefaultState());
                    }

                    if (flag && canSnowAt(blockpos1, true))
                    {
                        setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState());
                    }

                    if (flag && getBiome(blockpos2).canRain())
                    {
                        getBlockState(blockpos2).getBlock().fillWithRain(this, blockpos2);
                    }
                }

                theProfiler.endStartSection("tickBlocks");

                if (i > 0)
                {
                    for (ExtendedBlockStorage extendedblockstorage : chunk.getBlockStorageArray())
                    {
                        if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && extendedblockstorage.getNeedsRandomTick())
                        {
                            for (int i1 = 0; i1 < i; ++i1)
                            {
                                updateLCG = updateLCG * 3 + 1013904223;
                                int j1 = updateLCG >> 2;
                                int k1 = j1 & 15;
                                int l1 = j1 >> 8 & 15;
                                int i2 = j1 >> 16 & 15;
                                IBlockState iblockstate = extendedblockstorage.get(k1, i2, l1);
                                Block block = iblockstate.getBlock();
                                theProfiler.startSection("randomTick");

                                if (block.getTickRandomly())
                                {
                                    block.randomTick(this, new BlockPos(k1 + j, i2 + extendedblockstorage.getYLocation(), l1 + k), iblockstate, rand);
                                }

                                theProfiler.endSection();
                            }
                        }
                    }
                }
            }

            theProfiler.endSection();
        }
    }

    protected BlockPos adjustPosToNearbyEntity(BlockPos pos)
    {
        BlockPos blockpos = getPrecipitationHeight(pos);
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockpos, new BlockPos(blockpos.getX(), getHeight(), blockpos.getZ()))).expandXyz(3.0D);
        List<EntityLivingBase> list = getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, new com.google.common.base.Predicate<EntityLivingBase>()
        {
            public boolean apply(@Nullable EntityLivingBase p_apply_1_)
            {
                return p_apply_1_ != null && p_apply_1_.isEntityAlive() && canSeeSky(p_apply_1_.getPosition());
            }
        });

        if (!list.isEmpty())
        {
            return list.get(rand.nextInt(list.size())).getPosition();
        }
        else
        {
            if (blockpos.getY() == -1)
            {
                blockpos = blockpos.up(2);
            }

            return blockpos;
        }
    }

    public boolean isBlockTickPending(BlockPos pos, Block blockType)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockType);
        return pendingTickListEntriesThisTick.contains(nextticklistentry);
    }

    /**
     * Returns true if the identified block is scheduled to be updated.
     */
    public boolean isUpdateScheduled(BlockPos pos, Block blk)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blk);
        return pendingTickListEntriesHashSet.contains(nextticklistentry);
    }

    public void scheduleUpdate(BlockPos pos, Block blockIn, int delay)
    {
        updateBlockTick(pos, blockIn, delay, 0);
    }

    public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority)
    {
        Material material = blockIn.getDefaultState().getMaterial();

        if (scheduledUpdatesAreImmediate && material != Material.AIR)
        {
            if (blockIn.requiresUpdates())
            {
                if (isAreaLoaded(pos.add(-8, -8, -8), pos.add(8, 8, 8)))
                {
                    IBlockState iblockstate = getBlockState(pos);

                    if (iblockstate.getMaterial() != Material.AIR && iblockstate.getBlock() == blockIn)
                    {
                        iblockstate.getBlock().updateTick(this, pos, iblockstate, rand);
                    }
                }

                return;
            }

            delay = 1;
        }

        NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockIn);

        if (isBlockLoaded(pos))
        {
            if (material != Material.AIR)
            {
                nextticklistentry.setScheduledTime((long)delay + worldInfo.getWorldTotalTime());
                nextticklistentry.setPriority(priority);
            }

            if (!pendingTickListEntriesHashSet.contains(nextticklistentry))
            {
                pendingTickListEntriesHashSet.add(nextticklistentry);
                pendingTickListEntriesTreeSet.add(nextticklistentry);
            }
        }
    }

    public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockIn);
        nextticklistentry.setPriority(priority);
        Material material = blockIn.getDefaultState().getMaterial();

        if (material != Material.AIR)
        {
            nextticklistentry.setScheduledTime((long)delay + worldInfo.getWorldTotalTime());
        }

        if (!pendingTickListEntriesHashSet.contains(nextticklistentry))
        {
            pendingTickListEntriesHashSet.add(nextticklistentry);
            pendingTickListEntriesTreeSet.add(nextticklistentry);
        }
    }

    /**
     * Updates (and cleans up) entities and tile entities
     */
    public void updateEntities()
    {
        if (playerEntities.isEmpty())
        {
            if (updateEntityTick++ >= 300)
            {
                return;
            }
        }
        else
        {
            resetUpdateEntityTick();
        }

        provider.onWorldUpdateEntities();
        super.updateEntities();
    }

    protected void tickPlayers()
    {
        super.tickPlayers();
        theProfiler.endStartSection("players");

        for (int i = 0; i < playerEntities.size(); ++i)
        {
            Entity entity = playerEntities.get(i);
            Entity entity1 = entity.getRidingEntity();

            if (entity1 != null)
            {
                if (!entity1.isDead && entity1.isPassenger(entity))
                {
                    continue;
                }

                entity.dismountRidingEntity();
            }

            theProfiler.startSection("tick");

            if (!entity.isDead)
            {
                try
                {
                    updateEntity(entity);
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
                    entity.addEntityCrashInfo(crashreportcategory);
                    throw new ReportedException(crashreport);
                }
            }

            theProfiler.endSection();
            theProfiler.startSection("remove");

            if (entity.isDead)
            {
                int j = entity.chunkCoordX;
                int k = entity.chunkCoordZ;

                if (entity.addedToChunk && isChunkLoaded(j, k, true))
                {
                    getChunkFromChunkCoords(j, k).removeEntity(entity);
                }

                loadedEntityList.remove(entity);
                onEntityRemoved(entity);
            }

            theProfiler.endSection();
        }
    }

    /**
     * Resets the updateEntityTick field to 0
     */
    public void resetUpdateEntityTick()
    {
        updateEntityTick = 0;
    }

    /**
     * Runs through the list of updates to run and ticks them
     */
    public boolean tickUpdates(boolean p_72955_1_)
    {
        if (worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
        {
            return false;
        }
        else
        {
            int i = pendingTickListEntriesTreeSet.size();

            if (i != pendingTickListEntriesHashSet.size())
            {
                throw new IllegalStateException("TickNextTick list out of synch");
            }
            else
            {
                if (i > 65536)
                {
                    i = 65536;
                }

                theProfiler.startSection("cleaning");

                for (int j = 0; j < i; ++j)
                {
                    NextTickListEntry nextticklistentry = pendingTickListEntriesTreeSet.first();

                    if (!p_72955_1_ && nextticklistentry.scheduledTime > worldInfo.getWorldTotalTime())
                    {
                        break;
                    }

                    pendingTickListEntriesTreeSet.remove(nextticklistentry);
                    pendingTickListEntriesHashSet.remove(nextticklistentry);
                    pendingTickListEntriesThisTick.add(nextticklistentry);
                }

                theProfiler.endSection();
                theProfiler.startSection("ticking");
                Iterator<NextTickListEntry> iterator = pendingTickListEntriesThisTick.iterator();

                while (iterator.hasNext())
                {
                    NextTickListEntry nextticklistentry1 = iterator.next();
                    iterator.remove();
                    int k = 0;

                    if (isAreaLoaded(nextticklistentry1.position.add(0, 0, 0), nextticklistentry1.position.add(0, 0, 0)))
                    {
                        IBlockState iblockstate = getBlockState(nextticklistentry1.position);

                        if (iblockstate.getMaterial() != Material.AIR && Block.isEqualTo(iblockstate.getBlock(), nextticklistentry1.getBlock()))
                        {
                            try
                            {
                                iblockstate.getBlock().updateTick(this, nextticklistentry1.position, iblockstate, rand);
                            }
                            catch (Throwable throwable)
                            {
                                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while ticking a block");
                                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
                                CrashReportCategory.addBlockInfo(crashreportcategory, nextticklistentry1.position, iblockstate);
                                throw new ReportedException(crashreport);
                            }
                        }
                    }
                    else
                    {
                        scheduleUpdate(nextticklistentry1.position, nextticklistentry1.getBlock(), 0);
                    }
                }

                theProfiler.endSection();
                pendingTickListEntriesThisTick.clear();
                return !pendingTickListEntriesTreeSet.isEmpty();
            }
        }
    }

    @Nullable
    public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunkIn, boolean p_72920_2_)
    {
        ChunkPos chunkpos = chunkIn.getChunkCoordIntPair();
        int i = (chunkpos.chunkXPos << 4) - 2;
        int j = i + 16 + 2;
        int k = (chunkpos.chunkZPos << 4) - 2;
        int l = k + 16 + 2;
        return getPendingBlockUpdates(new StructureBoundingBox(i, 0, k, j, 256, l), p_72920_2_);
    }

    @Nullable
    public List<NextTickListEntry> getPendingBlockUpdates(StructureBoundingBox structureBB, boolean p_175712_2_)
    {
        List<NextTickListEntry> list = null;

        for (int i = 0; i < 2; ++i)
        {
            Iterator<NextTickListEntry> iterator;

            if (i == 0)
            {
                iterator = pendingTickListEntriesTreeSet.iterator();
            }
            else
            {
                iterator = pendingTickListEntriesThisTick.iterator();
            }

            while (iterator.hasNext())
            {
                NextTickListEntry nextticklistentry = iterator.next();
                BlockPos blockpos = nextticklistentry.position;

                if (blockpos.getX() >= structureBB.minX && blockpos.getX() < structureBB.maxX && blockpos.getZ() >= structureBB.minZ && blockpos.getZ() < structureBB.maxZ)
                {
                    if (p_175712_2_)
                    {
                        if (i == 0)
                        {
                            pendingTickListEntriesHashSet.remove(nextticklistentry);
                        }

                        iterator.remove();
                    }

                    if (list == null)
                    {
                        list = Lists.newArrayList();
                    }

                    list.add(nextticklistentry);
                }
            }
        }

        return list;
    }

    /**
     * Updates the entity in the world if the chunk the entity is in is currently loaded or its forced to update.
     */
    public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate)
    {
        if (!canSpawnAnimals() && (entityIn instanceof EntityAnimal || entityIn instanceof EntityWaterMob))
        {
            entityIn.setDead();
        }

        if (!canSpawnNPCs() && entityIn instanceof INpc)
        {
            entityIn.setDead();
        }

        super.updateEntityWithOptionalForce(entityIn, forceUpdate);
    }

    private boolean canSpawnNPCs()
    {
        return mcServer.getCanSpawnNPCs();
    }

    private boolean canSpawnAnimals()
    {
        return mcServer.getCanSpawnAnimals();
    }

    /**
     * Creates the chunk provider for this world. Called in the constructor. Retrieves provider from worldProvider?
     */
    protected IChunkProvider createChunkProvider()
    {
        IChunkLoader ichunkloader = saveHandler.getChunkLoader(provider);
        return new ChunkProviderServer(this, ichunkloader, provider.createChunkGenerator());
    }

    public boolean isBlockModifiable(EntityPlayer player, BlockPos pos)
    {
        return !mcServer.isBlockProtected(this, pos, player) && getWorldBorder().contains(pos);
    }

    public void initialize(WorldSettings settings)
    {
        if (!worldInfo.isInitialized())
        {
            try
            {
                createSpawnPosition(settings);

                if (worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
                {
                    setDebugWorldSettings();
                }

                super.initialize(settings);
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception initializing level");

                try
                {
                    addWorldInfoToCrashReport(crashreport);
                }
                catch (Throwable var5)
                {
                }

                throw new ReportedException(crashreport);
            }

            worldInfo.setServerInitialized(true);
        }
    }

    private void setDebugWorldSettings()
    {
        worldInfo.setMapFeaturesEnabled(false);
        worldInfo.setAllowCommands(true);
        worldInfo.setRaining(false);
        worldInfo.setThundering(false);
        worldInfo.setCleanWeatherTime(1000000000);
        worldInfo.setWorldTime(6000L);
        worldInfo.setGameType(GameType.SPECTATOR);
        worldInfo.setHardcore(false);
        worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
        worldInfo.setDifficultyLocked(true);
        getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
    }

    /**
     * creates a spawn position at random within 256 blocks of 0,0
     */
    private void createSpawnPosition(WorldSettings settings)
    {
        if (!provider.canRespawnHere())
        {
            worldInfo.setSpawn(BlockPos.ORIGIN.up(provider.getAverageGroundLevel()));
        }
        else if (worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
        {
            worldInfo.setSpawn(BlockPos.ORIGIN.up());
        }
        else
        {
            findingSpawnPoint = true;
            BiomeProvider biomeprovider = provider.getBiomeProvider();
            List<Biome> list = biomeprovider.getBiomesToSpawnIn();
            Random random = new Random(getSeed());
            BlockPos blockpos = biomeprovider.findBiomePosition(0, 0, 256, list, random);
            int i = 8;
            int j = provider.getAverageGroundLevel();
            int k = 8;

            if (blockpos != null)
            {
                i = blockpos.getX();
                k = blockpos.getZ();
            }
            else
            {
                LOGGER.warn("Unable to find spawn biome");
            }

            int l = 0;

            while (!provider.canCoordinateBeSpawn(i, k))
            {
                i += random.nextInt(64) - random.nextInt(64);
                k += random.nextInt(64) - random.nextInt(64);
                ++l;

                if (l == 1000)
                {
                    break;
                }
            }

            worldInfo.setSpawn(new BlockPos(i, j, k));
            findingSpawnPoint = false;

            if (settings.isBonusChestEnabled())
            {
                createBonusChest();
            }
        }
    }

    /**
     * Creates the bonus chest in the world.
     */
    protected void createBonusChest()
    {
        WorldGeneratorBonusChest worldgeneratorbonuschest = new WorldGeneratorBonusChest();

        for (int i = 0; i < 10; ++i)
        {
            int j = worldInfo.getSpawnX() + rand.nextInt(6) - rand.nextInt(6);
            int k = worldInfo.getSpawnZ() + rand.nextInt(6) - rand.nextInt(6);
            BlockPos blockpos = getTopSolidOrLiquidBlock(new BlockPos(j, 0, k)).up();

            if (worldgeneratorbonuschest.generate(this, rand, blockpos))
            {
                break;
            }
        }
    }

    @Nullable

    /**
     * Returns null for anything other than the End
     */
    public BlockPos getSpawnCoordinate()
    {
        return provider.getSpawnCoordinate();
    }

    /**
     * Saves all chunks to disk while updating progress bar.
     */
    public void saveAllChunks(boolean p_73044_1_, @Nullable Progress progressCallback) throws MinecraftException
    {
        ChunkProviderServer chunkproviderserver = getChunkProvider();

        if (chunkproviderserver.canSave())
        {
            if (progressCallback != null)
            {
                progressCallback.setTitle("Saving level");
            }

            saveLevel();

            if (progressCallback != null)
            {
                progressCallback.setTitle("Saving chunks");
            }

            chunkproviderserver.saveChunks(p_73044_1_);

            for (Chunk chunk : Lists.newArrayList(chunkproviderserver.getLoadedChunks()))
            {
                if (chunk != null && !thePlayerManager.contains(chunk.xPosition, chunk.zPosition))
                {
                    chunkproviderserver.unload(chunk);
                }
            }
        }
    }

    /**
     * saves chunk data - currently only called during execution of the Save All command
     */
    public void saveChunkData()
    {
        ChunkProviderServer chunkproviderserver = getChunkProvider();

        if (chunkproviderserver.canSave())
        {
            chunkproviderserver.saveExtraData();
        }
    }

    /**
     * Saves the chunks to disk.
     */
    protected void saveLevel() throws MinecraftException
    {
        checkSessionLock();

        for (WorldServer worldserver : mcServer.worldServers)
        {
            if (worldserver instanceof WorldServerMulti)
            {
                ((WorldServerMulti)worldserver).saveAdditionalData();
            }
        }

        worldInfo.setBorderSize(getWorldBorder().getDiameter());
        worldInfo.getBorderCenterX(getWorldBorder().getCenterX());
        worldInfo.getBorderCenterZ(getWorldBorder().getCenterZ());
        worldInfo.setBorderSafeZone(getWorldBorder().getDamageBuffer());
        worldInfo.setBorderDamagePerBlock(getWorldBorder().getDamageAmount());
        worldInfo.setBorderWarningDistance(getWorldBorder().getWarningDistance());
        worldInfo.setBorderWarningTime(getWorldBorder().getWarningTime());
        worldInfo.setBorderLerpTarget(getWorldBorder().getTargetSize());
        worldInfo.setBorderLerpTime(getWorldBorder().getTimeUntilTarget());
        saveHandler.saveWorldInfoWithPlayer(worldInfo, mcServer.getPlayerList().getHostPlayerData());
        mapStorage.saveAllData();
    }

    /**
     * Called when an entity is spawned in the world. This includes players.
     */
    public boolean spawnEntityInWorld(Entity entityIn)
    {
        return canAddEntity(entityIn) && super.spawnEntityInWorld(entityIn);
    }

    public void loadEntities(Collection<Entity> entityCollection)
    {
        for (Entity entity : Lists.newArrayList(entityCollection))
        {
            if (canAddEntity(entity))
            {
                loadedEntityList.add(entity);
                onEntityAdded(entity);
            }
        }
    }

    private boolean canAddEntity(Entity entityIn)
    {
        if (entityIn.isDead)
        {
            LOGGER.warn("Tried to add entity {} but it was marked as removed already", EntityList.func_191301_a(entityIn));
            return false;
        }
        else
        {
            UUID uuid = entityIn.getUniqueID();

            if (entitiesByUuid.containsKey(uuid))
            {
                Entity entity = entitiesByUuid.get(uuid);

                if (unloadedEntityList.contains(entity))
                {
                    unloadedEntityList.remove(entity);
                }
                else
                {
                    if (!(entityIn instanceof EntityPlayer))
                    {
                        LOGGER.warn("Keeping entity {} that already exists with UUID {}", EntityList.func_191301_a(entity), uuid.toString());
                        return false;
                    }

                    LOGGER.warn("Force-added player with duplicate UUID {}", uuid.toString());
                }

                removeEntityDangerously(entity);
            }

            return true;
        }
    }

    protected void onEntityAdded(Entity entityIn)
    {
        super.onEntityAdded(entityIn);
        entitiesById.addKey(entityIn.getEntityId(), entityIn);
        entitiesByUuid.put(entityIn.getUniqueID(), entityIn);
        Entity[] aentity = entityIn.getParts();

        if (aentity != null)
        {
            for (Entity entity : aentity)
            {
                entitiesById.addKey(entity.getEntityId(), entity);
            }
        }
    }

    protected void onEntityRemoved(Entity entityIn)
    {
        super.onEntityRemoved(entityIn);
        entitiesById.removeObject(entityIn.getEntityId());
        entitiesByUuid.remove(entityIn.getUniqueID());
        Entity[] aentity = entityIn.getParts();

        if (aentity != null)
        {
            for (Entity entity : aentity)
            {
                entitiesById.removeObject(entity.getEntityId());
            }
        }
    }

    /**
     * adds a lightning bolt to the list of lightning bolts in this world.
     */
    public boolean addWeatherEffect(Entity entityIn)
    {
        if (super.addWeatherEffect(entityIn))
        {
            mcServer.getPlayerList().sendToAllNearExcept(null, entityIn.posX, entityIn.posY, entityIn.posZ, 512.0D, provider.getDimensionType().getId(), new SPacketSpawnGlobalEntity(entityIn));
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * sends a Packet 38 (Entity Status) to all tracked players of that entity
     */
    public void setEntityState(Entity entityIn, byte state)
    {
        getEntityTracker().sendToTrackingAndSelf(entityIn, new SPacketEntityStatus(entityIn, state));
    }

    /**
     * gets the world's chunk provider
     */
    public ChunkProviderServer getChunkProvider()
    {
        return (ChunkProviderServer)super.getChunkProvider();
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    public Explosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking)
    {
        Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
        explosion.doExplosionA();
        explosion.doExplosionB(false);

        if (!isSmoking)
        {
            explosion.clearAffectedBlockPositions();
        }

        for (EntityPlayer entityplayer : playerEntities)
        {
            if (entityplayer.getDistanceSq(x, y, z) < 4096.0D)
            {
                ((EntityPlayerMP)entityplayer).connection.sendPacket(new SPacketExplosion(x, y, z, strength, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(entityplayer)));
            }
        }

        return explosion;
    }

    public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam)
    {
        BlockEventData blockeventdata = new BlockEventData(pos, blockIn, eventID, eventParam);

        for (BlockEventData blockeventdata1 : blockEventQueue[blockEventCacheIndex])
        {
            if (blockeventdata1.equals(blockeventdata))
            {
                return;
            }
        }

        blockEventQueue[blockEventCacheIndex].add(blockeventdata);
    }

    private void sendQueuedBlockEvents()
    {
        while (!blockEventQueue[blockEventCacheIndex].isEmpty())
        {
            int i = blockEventCacheIndex;
            blockEventCacheIndex ^= 1;

            for (BlockEventData blockeventdata : blockEventQueue[i])
            {
                if (fireBlockEvent(blockeventdata))
                {
                    mcServer.getPlayerList().sendToAllNearExcept(null, blockeventdata.getPosition().getX(), blockeventdata.getPosition().getY(), blockeventdata.getPosition().getZ(), 64.0D, provider.getDimensionType().getId(), new SPacketBlockAction(blockeventdata.getPosition(), blockeventdata.getBlock(), blockeventdata.getEventID(), blockeventdata.getEventParameter()));
                }
            }

            blockEventQueue[i].clear();
        }
    }

    private boolean fireBlockEvent(BlockEventData event)
    {
        IBlockState iblockstate = getBlockState(event.getPosition());
        return iblockstate.getBlock() == event.getBlock() && iblockstate.onBlockEventReceived(this, event.getPosition(), event.getEventID(), event.getEventParameter());
    }

    /**
     * Syncs all changes to disk and wait for completion.
     */
    public void flush()
    {
        saveHandler.flush();
    }

    /**
     * Updates all weather states.
     */
    protected void updateWeather()
    {
        boolean flag = isRaining();
        super.updateWeather();

        if (prevRainingStrength != rainingStrength)
        {
            mcServer.getPlayerList().sendPacketToAllPlayersInDimension(new SPacketChangeGameState(7, rainingStrength), provider.getDimensionType().getId());
        }

        if (prevThunderingStrength != thunderingStrength)
        {
            mcServer.getPlayerList().sendPacketToAllPlayersInDimension(new SPacketChangeGameState(8, thunderingStrength), provider.getDimensionType().getId());
        }

        if (flag != isRaining())
        {
            if (flag)
            {
                mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(2, 0.0F));
            }
            else
            {
                mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(1, 0.0F));
            }

            mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(7, rainingStrength));
            mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(8, thunderingStrength));
        }
    }

    @Nullable
    public MinecraftServer getInstanceServer()
    {
        return mcServer;
    }

    /**
     * Gets the entity tracker for this server world.
     */
    public EntityTracker getEntityTracker()
    {
        return theEntityTracker;
    }

    /**
     * Gets the player chunk map for this server world.
     */
    public PlayerChunkMap getPlayerChunkMap()
    {
        return thePlayerManager;
    }

    public Teleporter getDefaultTeleporter()
    {
        return worldTeleporter;
    }

    public TemplateManager getStructureTemplateManager()
    {
        return saveHandler.getStructureTemplateManager();
    }

    /**
     * Spawns the desired particle and sends the necessary packets to the relevant connected players.
     */
    public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, int numberOfParticles, double xOffset, double yOffset, double zOffset, double particleSpeed, int... particleArguments)
    {
        spawnParticle(particleType, false, xCoord, yCoord, zCoord, numberOfParticles, xOffset, yOffset, zOffset, particleSpeed, particleArguments);
    }

    /**
     * Spawns the desired particle and sends the necessary packets to the relevant connected players.
     */
    public void spawnParticle(EnumParticleTypes particleType, boolean longDistance, double xCoord, double yCoord, double zCoord, int numberOfParticles, double xOffset, double yOffset, double zOffset, double particleSpeed, int... particleArguments)
    {
        SPacketParticles spacketparticles = new SPacketParticles(particleType, longDistance, (float)xCoord, (float)yCoord, (float)zCoord, (float)xOffset, (float)yOffset, (float)zOffset, (float)particleSpeed, numberOfParticles, particleArguments);

        for (int i = 0; i < playerEntities.size(); ++i)
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP) playerEntities.get(i);
            sendPacketWithinDistance(entityplayermp, longDistance, xCoord, yCoord, zCoord, spacketparticles);
        }
    }

    public void spawnParticle(EntityPlayerMP player, EnumParticleTypes particle, boolean longDistance, double x, double y, double z, int count, double xOffset, double yOffset, double zOffset, double speed, int... arguments)
    {
        Packet<?> packet = new SPacketParticles(particle, longDistance, (float)x, (float)y, (float)z, (float)xOffset, (float)yOffset, (float)zOffset, (float)speed, count, arguments);
        sendPacketWithinDistance(player, longDistance, x, y, z, packet);
    }

    private void sendPacketWithinDistance(EntityPlayerMP player, boolean longDistance, double x, double y, double z, Packet<?> packetIn)
    {
        BlockPos blockpos = player.getPosition();
        double d0 = blockpos.distanceSq(x, y, z);

        if (d0 <= 1024.0D || longDistance && d0 <= 262144.0D)
        {
            player.connection.sendPacket(packetIn);
        }
    }

    @Nullable
    public Entity getEntityFromUuid(UUID uuid)
    {
        return entitiesByUuid.get(uuid);
    }

    public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule)
    {
        return mcServer.addScheduledTask(runnableToSchedule);
    }

    public boolean isThisThread()
    {
        return mcServer.isThisThread();
    }

    @Nullable
    public BlockPos func_190528_a(String p_190528_1_, BlockPos p_190528_2_, boolean p_190528_3_)
    {
        return getChunkProvider().getStrongholdGen(this, p_190528_1_, p_190528_2_, p_190528_3_);
    }

    public AdvancementManager func_191952_z()
    {
        return field_191951_C;
    }

    public FunctionManager func_193037_A()
    {
        return field_193036_D;
    }

    static class ServerBlockEventList extends ArrayList<BlockEventData>
    {
        private ServerBlockEventList()
        {
        }
    }
}
