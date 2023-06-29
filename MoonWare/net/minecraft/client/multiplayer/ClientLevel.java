package net.minecraft.client.multiplayer;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.ChunkEvent;
import baritone.api.event.events.type.EventState;
import com.google.common.collect.Sets;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;
import optifine.Config;
import optifine.CustomGuis;
import optifine.DynamicLights;
import optifine.PlayerControllerOF;
import optifine.Reflector;

public class ClientLevel extends World
{
    /** The packets that need to be sent to the server. */
    private final NetHandlerPlayClient connection;

    /** The ChunkProviderClient instance */
    private ChunkProviderClient clientChunkProvider;
    private final Set<Entity> entityList = Sets.newHashSet();
    private final Set<Entity> entitySpawnQueue = Sets.newHashSet();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Set<ChunkPos> previousActiveChunkSet = Sets.newHashSet();
    private int ambienceTicks;
    protected Set<ChunkPos> viewableChunks;
    private int playerChunkX = Integer.MIN_VALUE;
    private int playerChunkY = Integer.MIN_VALUE;
    private boolean playerUpdate;

    public ClientLevel(NetHandlerPlayClient netHandler, WorldSettings settings, int dimension, EnumDifficulty difficulty, Profiler profilerIn)
    {
        super(new SaveHandlerMP(), new WorldInfo(settings, "MpServer"), makeWorldProvider(dimension), profilerIn, true);
        ambienceTicks = rand.nextInt(12000);
        viewableChunks = Sets.newHashSet();
        connection = netHandler;
        getWorldInfo().setDifficulty(difficulty);
        provider.registerWorld(this);
        setSpawnPoint(new BlockPos(8, 64, 8));
        chunkProvider = createChunkProvider();
        mapStorage = new SaveDataMemoryStorage();
        calculateInitialSkylight();
        calculateInitialWeather();
        Reflector.call(this, Reflector.ForgeWorld_initCapabilities);
        Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, this);

        if (Minecraft.playerController != null && Minecraft.playerController.getClass() == PlayerControllerMP.class)
        {
            Minecraft.playerController = new PlayerControllerOF(mc, netHandler);
            CustomGuis.setPlayerControllerOF((PlayerControllerOF) Minecraft.playerController);
        }
    }

    private static WorldProvider makeWorldProvider(int p_makeWorldProvider_0_)
    {
        return Reflector.DimensionManager_createProviderFor.exists() ? (WorldProvider)Reflector.call(Reflector.DimensionManager_createProviderFor, p_makeWorldProvider_0_) : DimensionType.getById(p_makeWorldProvider_0_).createDimension();
    }

    /**
     * Runs a single tick for the world
     */
    public void tick()
    {
        super.tick();
        setTotalWorldTime(getTotalWorldTime() + 1L);

        if (getGameRules().getBoolean("doDaylightCycle"))
        {
            setWorldTime(getWorldTime() + 1L);
        }

        theProfiler.startSection("reEntryProcessing");

        for (int i = 0; i < 10 && !entitySpawnQueue.isEmpty(); ++i)
        {
            Entity entity = entitySpawnQueue.iterator().next();
            entitySpawnQueue.remove(entity);

            if (!loadedEntityList.contains(entity))
            {
                spawnEntityInWorld(entity);
            }
        }

        theProfiler.endStartSection("chunkCache");
        clientChunkProvider.unloadQueuedChunks();
        theProfiler.endStartSection("blocks");
        updateBlocks();
        theProfiler.endSection();
    }

    /**
     * Invalidates an AABB region of blocks from the receive queue, in the event that the block has been modified
     * client-side in the intervening 80 receive ticks.
     */
    public void invalidateBlockReceiveRegion(int x1, int y1, int z1, int x2, int y2, int z2)
    {
    }

    /**
     * Creates the chunk provider for this world. Called in the constructor. Retrieves provider from worldProvider?
     */
    protected IChunkProvider createChunkProvider()
    {
        clientChunkProvider = new ChunkProviderClient(this);
        return clientChunkProvider;
    }

    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty)
    {
        return allowEmpty || !getChunkProvider().provideChunk(x, z).isEmpty();
    }

    protected void buildChunkCoordList()
    {
        int i = MathHelper.floor(Minecraft.player.posX / 16.0D);
        int j = MathHelper.floor(Minecraft.player.posZ / 16.0D);

        if (i != playerChunkX || j != playerChunkY)
        {
            playerChunkX = i;
            playerChunkY = j;
            viewableChunks.clear();
            int k = Minecraft.gameSettings.renderDistance;
            theProfiler.startSection("buildList");
            int l = MathHelper.floor(Minecraft.player.posX / 16.0D);
            int i1 = MathHelper.floor(Minecraft.player.posZ / 16.0D);

            for (int j1 = -k; j1 <= k; ++j1)
            {
                for (int k1 = -k; k1 <= k; ++k1)
                {
                    viewableChunks.add(new ChunkPos(j1 + l, k1 + i1));
                }
            }

            theProfiler.endSection();
        }
    }

    protected void updateBlocks()
    {
        buildChunkCoordList();

        if (ambienceTicks > 0)
        {
            --ambienceTicks;
        }

        previousActiveChunkSet.retainAll(viewableChunks);

        if (previousActiveChunkSet.size() == viewableChunks.size())
        {
            previousActiveChunkSet.clear();
        }

        int i = 0;

        for (ChunkPos chunkpos : viewableChunks)
        {
            if (!previousActiveChunkSet.contains(chunkpos))
            {
                int j = chunkpos.chunkXPos * 16;
                int k = chunkpos.chunkZPos * 16;
                theProfiler.startSection("getChunk");
                Chunk chunk = getChunkFromChunkCoords(chunkpos.chunkXPos, chunkpos.chunkZPos);
                playMoodSoundAndCheckLight(j, k, chunk);
                theProfiler.endSection();
                previousActiveChunkSet.add(chunkpos);
                ++i;

                if (i >= 10)
                {
                    return;
                }
            }
        }
    }

    public void doPreChunk(int chunkX, int chunkZ, boolean loadChunk)
    {

        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().world() == this) {
                ibaritone.getGameEventHandler().onChunkEvent(
                        new ChunkEvent(
                                EventState.PRE,
                                loadChunk ? ChunkEvent.Type.LOAD : ChunkEvent.Type.UNLOAD,
                                chunkX,
                                chunkZ
                        )
                );
            }
        }

        if (loadChunk)
        {
            clientChunkProvider.loadChunk(chunkX, chunkZ);
        }
        else
        {
            clientChunkProvider.unloadChunk(chunkX, chunkZ);
            markBlockRangeForRenderUpdate(chunkX * 16, 0, chunkZ * 16, chunkX * 16 + 15, 256, chunkZ * 16 + 15);
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().world() == this) {
                ibaritone.getGameEventHandler().onChunkEvent(
                        new ChunkEvent(
                                EventState.POST,
                                loadChunk ? ChunkEvent.Type.LOAD : ChunkEvent.Type.UNLOAD,
                                chunkX,
                                chunkZ
                        )
                );
            }
        }
    }

    /**
     * Called when an entity is spawned in the world. This includes players.
     */
    public boolean spawnEntityInWorld(Entity entityIn)
    {
        boolean flag = super.spawnEntityInWorld(entityIn);
        entityList.add(entityIn);

        if (flag)
        {
            if (entityIn instanceof EntityMinecart)
            {
                Minecraft.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart)entityIn));
            }
        }
        else
        {
            entitySpawnQueue.add(entityIn);
        }

        return flag;
    }

    /**
     * Schedule the entity for removal during the next tick. Marks the entity dead in anticipation.
     */
    public void removeEntity(Entity entityIn)
    {
        super.removeEntity(entityIn);
        entityList.remove(entityIn);
    }

    protected void onEntityAdded(Entity entityIn)
    {
        super.onEntityAdded(entityIn);

        entitySpawnQueue.remove(entityIn);
    }

    protected void onEntityRemoved(Entity entityIn)
    {
        super.onEntityRemoved(entityIn);

        if (entityList.contains(entityIn))
        {
            if (entityIn.isEntityAlive())
            {
                entitySpawnQueue.add(entityIn);
            }
            else
            {
                entityList.remove(entityIn);
            }
        }
    }

    /**
     * Add an ID to Entity mapping to entityHashSet
     */
    public void addEntityToWorld(int entityID, Entity entityToSpawn)
    {
        Entity entity = getEntityByID(entityID);

        if (entity != null)
        {
            removeEntity(entity);
        }

        entityList.add(entityToSpawn);
        entityToSpawn.setEntityId(entityID);

        if (!spawnEntityInWorld(entityToSpawn))
        {
            entitySpawnQueue.add(entityToSpawn);
        }

        entitiesById.addKey(entityID, entityToSpawn);
    }

    @Nullable

    /**
     * Returns the Entity with the given ID, or null if it doesn't exist in this World.
     */
    public Entity getEntityByID(int id)
    {
        return id == Minecraft.player.getEntityId() ? Minecraft.player : super.getEntityByID(id);
    }

    public Entity removeEntityFromWorld(int entityID)
    {
        Entity entity = entitiesById.removeObject(entityID);

        if (entity != null)
        {
            entityList.remove(entity);
            removeEntity(entity);
        }

        return entity;
    }

    @Deprecated
    public boolean invalidateRegionAndSetBlock(BlockPos pos, IBlockState state)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        invalidateBlockReceiveRegion(i, j, k, i, j, k);
        return super.setBlockState(pos, state, 3);
    }

    /**
     * If on MP, sends a quitting packet.
     */
    public void quit()
    {
        connection.getNetworkManager().closeChannel(new TextComponent("Quitting"));
    }

    /**
     * Updates all weather states.
     */
    protected void updateWeather()
    {
    }

    protected void playMoodSoundAndCheckLight(int p_147467_1_, int p_147467_2_, Chunk chunkIn)
    {
        super.playMoodSoundAndCheckLight(p_147467_1_, p_147467_2_, chunkIn);

        if (ambienceTicks == 0)
        {
            EntityPlayerSP entityplayersp = Minecraft.player;

            if (entityplayersp == null)
            {
                return;
            }

            if (Math.abs(entityplayersp.chunkCoordX - chunkIn.xPosition) > 1 || Math.abs(entityplayersp.chunkCoordZ - chunkIn.zPosition) > 1)
            {
                return;
            }

            updateLCG = updateLCG * 3 + 1013904223;
            int i = updateLCG >> 2;
            int j = i & 15;
            int k = i >> 8 & 15;
            int l = i >> 16 & 255;
            l = l / 2;

            if (entityplayersp.posY > 160.0D)
            {
                l += 128;
            }
            else if (entityplayersp.posY > 96.0D)
            {
                l += 64;
            }

            BlockPos blockpos = new BlockPos(j + p_147467_1_, l, k + p_147467_2_);
            IBlockState iblockstate = chunkIn.getBlockState(blockpos);
            j = j + p_147467_1_;
            k = k + p_147467_2_;
            double d0 = Minecraft.player.getDistanceSq((double)j + 0.5D, (double)l + 0.5D, (double)k + 0.5D);

            if (d0 < 4.0D)
            {
                return;
            }

            if (d0 > 255.0D)
            {
                return;
            }

            if (iblockstate.getMaterial() == Material.AIR && getLight(blockpos) <= rand.nextInt(8) && getLightFor(EnumSkyBlock.SKY, blockpos) <= 0)
            {
                playSound((double)j + 0.5D, (double)l + 0.5D, (double)k + 0.5D, SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, 0.7F, 0.8F + rand.nextFloat() * 0.2F, false);
                ambienceTicks = rand.nextInt(12000) + 6000;
            }
        }
    }

    public void doVoidFogParticles(int posX, int posY, int posZ)
    {
        int i = 32;
        Random random = new Random();
        ItemStack itemstack = Minecraft.player.getHeldItemMainhand();

        if (itemstack == null || Block.getBlockFromItem(itemstack.getItem()) != Blocks.BARRIER)
        {
            itemstack = Minecraft.player.getHeldItemOffhand();
        }

        boolean flag = Minecraft.playerController.getCurrentGameType() == GameType.CREATIVE && !itemstack.isEmpty() && itemstack.getItem() == Item.getItemFromBlock(Blocks.BARRIER);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j = 0; j < 667; ++j)
        {
            showBarrierParticles(posX, posY, posZ, 16, random, flag, blockpos$mutableblockpos);
            showBarrierParticles(posX, posY, posZ, 32, random, flag, blockpos$mutableblockpos);
        }
    }

    public void showBarrierParticles(int p_184153_1_, int p_184153_2_, int p_184153_3_, int p_184153_4_, Random random, boolean p_184153_6_, BlockPos.MutableBlockPos pos)
    {
        int i = p_184153_1_ + rand.nextInt(p_184153_4_) - rand.nextInt(p_184153_4_);
        int j = p_184153_2_ + rand.nextInt(p_184153_4_) - rand.nextInt(p_184153_4_);
        int k = p_184153_3_ + rand.nextInt(p_184153_4_) - rand.nextInt(p_184153_4_);
        pos.setPos(i, j, k);
        IBlockState iblockstate = getBlockState(pos);
        iblockstate.getBlock().randomDisplayTick(iblockstate, this, pos, random);

        if (p_184153_6_ && iblockstate.getBlock() == Blocks.BARRIER)
        {
            spawnParticle(EnumParticleTypes.BARRIER, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, 0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * also releases skins.
     */
    public void removeAllEntities()
    {
        loadedEntityList.removeAll(unloadedEntityList);

        for (int i = 0; i < unloadedEntityList.size(); ++i)
        {
            Entity entity = unloadedEntityList.get(i);
            int j = entity.chunkCoordX;
            int k = entity.chunkCoordZ;

            if (entity.addedToChunk && isChunkLoaded(j, k, true))
            {
                getChunkFromChunkCoords(j, k).removeEntity(entity);
            }
        }

        for (int i1 = 0; i1 < unloadedEntityList.size(); ++i1)
        {
            onEntityRemoved(unloadedEntityList.get(i1));
        }

        unloadedEntityList.clear();

        for (int j1 = 0; j1 < loadedEntityList.size(); ++j1)
        {
            Entity entity1 = loadedEntityList.get(j1);
            Entity entity2 = entity1.getRidingEntity();

            if (entity2 != null)
            {
                if (!entity2.isDead && entity2.isPassenger(entity1))
                {
                    continue;
                }

                entity1.dismountRidingEntity();
            }

            if (entity1.isDead)
            {
                int k1 = entity1.chunkCoordX;
                int l = entity1.chunkCoordZ;

                if (entity1.addedToChunk && isChunkLoaded(k1, l, true))
                {
                    getChunkFromChunkCoords(k1, l).removeEntity(entity1);
                }

                loadedEntityList.remove(j1--);
                onEntityRemoved(entity1);
            }
        }
    }

    /**
     * Adds some basic stats of the world to the given crash report.
     */
    public CrashReportCategory addWorldInfoToCrashReport(CrashReport report)
    {
        CrashReportCategory crashreportcategory = super.addWorldInfoToCrashReport(report);
        crashreportcategory.setDetail("Forced entities", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return entityList.size() + " total; " + entityList;
            }
        });
        crashreportcategory.setDetail("Retry entities", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return entitySpawnQueue.size() + " total; " + entitySpawnQueue;
            }
        });
        crashreportcategory.setDetail("Server brand", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return Minecraft.player.getServerBrand();
            }
        });
        crashreportcategory.setDetail("Server type", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return Minecraft.getIntegratedServer() == null ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
            }
        });
        return crashreportcategory;
    }

    public void playSound(@Nullable EntityPlayer player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch)
    {
        if (player == Minecraft.player)
        {
            playSound(x, y, z, soundIn, category, volume, pitch, false);
        }
    }

    public void playSound(BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay)
    {
        playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, soundIn, category, volume, pitch, distanceDelay);
    }

    public void playSound(double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay)
    {
        double d0 = Minecraft.getRenderViewEntity().getDistanceSq(x, y, z);
        PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(soundIn, category, volume, pitch, (float)x, (float)y, (float)z);

        if (distanceDelay && d0 > 100.0D)
        {
            double d1 = Math.sqrt(d0) / 40.0D;
            Minecraft.getSoundHandler().playDelayedSound(positionedsoundrecord, (int)(d1 * 20.0D));
        }
        else
        {
            Minecraft.getSoundHandler().playSound(positionedsoundrecord);
        }
    }

    public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, @Nullable NBTTagCompound compund)
    {
        Minecraft.effectRenderer.addEffect(new ParticleFirework.Starter(this, x, y, z, motionX, motionY, motionZ, Minecraft.effectRenderer, compund));
    }

    public void sendPacketToServer(Packet<?> packetIn)
    {
        connection.sendPacket(packetIn);
    }

    public void setWorldScoreboard(Scoreboard scoreboardIn)
    {
        worldScoreboard = scoreboardIn;
    }

    /**
     * Sets the world time.
     */
    public void setWorldTime(long time)
    {
        if (time < 0L)
        {
            time = -time;
            getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
        }
        else
        {
            getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
        }

        super.setWorldTime(time);
    }

    /**
     * gets the world's chunk provider
     */
    public ChunkProviderClient getChunkProvider()
    {
        return (ChunkProviderClient)super.getChunkProvider();
    }

    public int getCombinedLight(BlockPos pos, int lightValue)
    {
        int i = super.getCombinedLight(pos, lightValue);

        if (Config.isDynamicLights())
        {
            i = DynamicLights.getCombinedLight(pos, i);
        }

        return i;
    }

    /**
     * Sets the block state at a given location. Flag 1 will cause a block update. Flag 2 will send the change to
     * clients (you almost always want this). Flag 4 prevents the block from being re-rendered, if this is a client
     * world. Flags can be added together.
     */
    public boolean setBlockState(BlockPos pos, IBlockState newState, int flags)
    {
        playerUpdate = isPlayerActing();
        boolean flag = super.setBlockState(pos, newState, flags);
        playerUpdate = false;
        return flag;
    }

    private boolean isPlayerActing()
    {
        if (Minecraft.playerController instanceof PlayerControllerOF)
        {
            PlayerControllerOF playercontrollerof = (PlayerControllerOF) Minecraft.playerController;
            return playercontrollerof.isActing();
        }
        else
        {
            return false;
        }
    }

    public boolean isPlayerUpdate()
    {
        return playerUpdate;
    }
}
