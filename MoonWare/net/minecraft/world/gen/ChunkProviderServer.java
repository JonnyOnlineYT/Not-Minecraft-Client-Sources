package net.minecraft.world.gen;

import baritone.utils.accessor.IChunkProviderServer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkProviderServer implements IChunkProvider, IChunkProviderServer
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Set<Long> droppedChunksSet = Sets.newHashSet();
    private final IChunkGenerator chunkGenerator;
    private final IChunkLoader chunkLoader;
    private final Long2ObjectMap<Chunk> id2ChunkMap = new Long2ObjectOpenHashMap<Chunk>(8192);
    private final WorldServer worldObj;

    public ChunkProviderServer(WorldServer worldObjIn, IChunkLoader chunkLoaderIn, IChunkGenerator chunkGeneratorIn)
    {
        worldObj = worldObjIn;
        chunkLoader = chunkLoaderIn;
        chunkGenerator = chunkGeneratorIn;
    }

    public Collection<Chunk> getLoadedChunks()
    {
        return id2ChunkMap.values();
    }

    /**
     * Unloads a chunk
     */
    public void unload(Chunk chunkIn)
    {
        if (worldObj.provider.canDropChunk(chunkIn.xPosition, chunkIn.zPosition))
        {
            droppedChunksSet.add(Long.valueOf(ChunkPos.asLong(chunkIn.xPosition, chunkIn.zPosition)));
            chunkIn.unloaded = true;
        }
    }

    /**
     * marks all chunks for unload, ignoring those near the spawn
     */
    public void unloadAllChunks()
    {
        ObjectIterator objectiterator = id2ChunkMap.values().iterator();

        while (objectiterator.hasNext())
        {
            Chunk chunk = (Chunk)objectiterator.next();
            unload(chunk);
        }
    }

    @Nullable
    public Chunk getLoadedChunk(int x, int z)
    {
        long i = ChunkPos.asLong(x, z);
        Chunk chunk = id2ChunkMap.get(i);

        if (chunk != null)
        {
            chunk.unloaded = false;
        }

        return chunk;
    }

    @Nullable
    public Chunk loadChunk(int x, int z)
    {
        Chunk chunk = getLoadedChunk(x, z);

        if (chunk == null)
        {
            chunk = loadChunkFromFile(x, z);

            if (chunk != null)
            {
                id2ChunkMap.put(ChunkPos.asLong(x, z), chunk);
                chunk.onChunkLoad();
                chunk.populateChunk(this, chunkGenerator);
            }
        }

        return chunk;
    }

    public Chunk provideChunk(int x, int z)
    {
        Chunk chunk = loadChunk(x, z);

        if (chunk == null)
        {
            long i = ChunkPos.asLong(x, z);

            try
            {
                chunk = chunkGenerator.provideChunk(x, z);
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
                crashreportcategory.addCrashSection("Location", String.format("%d,%d", x, z));
                crashreportcategory.addCrashSection("Position hash", Long.valueOf(i));
                crashreportcategory.addCrashSection("Generator", chunkGenerator);
                throw new ReportedException(crashreport);
            }

            id2ChunkMap.put(i, chunk);
            chunk.onChunkLoad();
            chunk.populateChunk(this, chunkGenerator);
        }

        return chunk;
    }

    @Nullable
    private Chunk loadChunkFromFile(int x, int z)
    {
        try
        {
            Chunk chunk = chunkLoader.loadChunk(worldObj, x, z);

            if (chunk != null)
            {
                chunk.setLastSaveTime(worldObj.getTotalWorldTime());
                chunkGenerator.recreateStructures(chunk, x, z);
            }

            return chunk;
        }
        catch (Exception exception)
        {
            LOGGER.error("Couldn't load chunk", exception);
            return null;
        }
    }

    private void saveChunkExtraData(Chunk chunkIn)
    {
        try
        {
            chunkLoader.saveExtraChunkData(worldObj, chunkIn);
        }
        catch (Exception exception)
        {
            LOGGER.error("Couldn't save entities", exception);
        }
    }

    private void saveChunkData(Chunk chunkIn)
    {
        try
        {
            chunkIn.setLastSaveTime(worldObj.getTotalWorldTime());
            chunkLoader.saveChunk(worldObj, chunkIn);
        }
        catch (IOException ioexception)
        {
            LOGGER.error("Couldn't save chunk", ioexception);
        }
        catch (MinecraftException minecraftexception)
        {
            LOGGER.error("Couldn't save chunk; already in use by another instance of Minecraft?", minecraftexception);
        }
    }

    public boolean saveChunks(boolean p_186027_1_)
    {
        int i = 0;
        List<Chunk> list = Lists.newArrayList(id2ChunkMap.values());

        for (int j = 0; j < list.size(); ++j)
        {
            Chunk chunk = list.get(j);

            if (p_186027_1_)
            {
                saveChunkExtraData(chunk);
            }

            if (chunk.needsSaving(p_186027_1_))
            {
                saveChunkData(chunk);
                chunk.setModified(false);
                ++i;

                if (i == 24 && !p_186027_1_)
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    public void saveExtraData()
    {
        chunkLoader.saveExtraData();
    }

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    public void unloadQueuedChunks()
    {
        if (!worldObj.disableLevelSaving)
        {
            if (!droppedChunksSet.isEmpty())
            {
                Iterator<Long> iterator = droppedChunksSet.iterator();

                for (int i = 0; i < 100 && iterator.hasNext(); iterator.remove())
                {
                    Long olong = iterator.next();
                    Chunk chunk = id2ChunkMap.get(olong);

                    if (chunk != null && chunk.unloaded)
                    {
                        chunk.onChunkUnload();
                        saveChunkData(chunk);
                        saveChunkExtraData(chunk);
                        id2ChunkMap.remove(olong);
                        ++i;
                    }
                }
            }

            chunkLoader.chunkTick();
        }

    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave()
    {
        return !worldObj.disableLevelSaving;
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString()
    {
        return "ServerChunkCache: " + id2ChunkMap.size() + " Drop: " + droppedChunksSet.size();
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return chunkGenerator.getPossibleCreatures(creatureType, pos);
    }

    @Nullable
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_)
    {
        return chunkGenerator.getStrongholdGen(worldIn, structureName, position, p_180513_4_);
    }

    public boolean func_193413_a(World p_193413_1_, String p_193413_2_, BlockPos p_193413_3_)
    {
        return chunkGenerator.func_193414_a(p_193413_1_, p_193413_2_, p_193413_3_);
    }

    public int getLoadedChunkCount()
    {
        return id2ChunkMap.size();
    }

    /**
     * Checks to see if a chunk exists at x, z
     */
    public boolean chunkExists(int x, int z)
    {
        return id2ChunkMap.containsKey(ChunkPos.asLong(x, z));
    }

    public boolean func_191062_e(int p_191062_1_, int p_191062_2_)
    {
        return id2ChunkMap.containsKey(ChunkPos.asLong(p_191062_1_, p_191062_2_)) || chunkLoader.func_191063_a(p_191062_1_, p_191062_2_);
    }

    @Override
    public IChunkLoader getChunkLoader() {
        return chunkLoader;
    }
}
