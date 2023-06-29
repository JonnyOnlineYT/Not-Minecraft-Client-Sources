package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;

public class ChunkCompileTaskGenerator implements Comparable<ChunkCompileTaskGenerator>
{
    private final RenderChunk renderChunk;
    private final ReentrantLock lock = new ReentrantLock();
    private final List<Runnable> listFinishRunnables = Lists.newArrayList();
    private final ChunkCompileTaskGenerator.Type type;
    private final double distanceSq;
    private RegionRenderCacheBuilder regionRenderCacheBuilder;
    private CompiledChunk compiledChunk;
    private ChunkCompileTaskGenerator.Status status = ChunkCompileTaskGenerator.Status.PENDING;
    private boolean finished;

    public ChunkCompileTaskGenerator(RenderChunk p_i46560_1_, ChunkCompileTaskGenerator.Type p_i46560_2_, double p_i46560_3_)
    {
        renderChunk = p_i46560_1_;
        type = p_i46560_2_;
        distanceSq = p_i46560_3_;
    }

    public ChunkCompileTaskGenerator.Status getStatus()
    {
        return status;
    }

    public RenderChunk getRenderChunk()
    {
        return renderChunk;
    }

    public CompiledChunk getCompiledChunk()
    {
        return compiledChunk;
    }

    public void setCompiledChunk(CompiledChunk compiledChunkIn)
    {
        compiledChunk = compiledChunkIn;
    }

    public RegionRenderCacheBuilder getRegionRenderCacheBuilder()
    {
        return regionRenderCacheBuilder;
    }

    public void setRegionRenderCacheBuilder(RegionRenderCacheBuilder regionRenderCacheBuilderIn)
    {
        regionRenderCacheBuilder = regionRenderCacheBuilderIn;
    }

    public void setStatus(ChunkCompileTaskGenerator.Status statusIn)
    {
        lock.lock();

        try
        {
            status = statusIn;
        }
        finally
        {
            lock.unlock();
        }
    }

    public void finish()
    {
        lock.lock();

        try
        {
            if (type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK && status != ChunkCompileTaskGenerator.Status.DONE)
            {
                renderChunk.setNeedsUpdate(false);
            }

            finished = true;
            status = ChunkCompileTaskGenerator.Status.DONE;

            for (Runnable runnable : listFinishRunnables)
            {
                runnable.run();
            }
        }
        finally
        {
            lock.unlock();
        }
    }

    public void addFinishRunnable(Runnable runnable)
    {
        lock.lock();

        try
        {
            listFinishRunnables.add(runnable);

            if (finished)
            {
                runnable.run();
            }
        }
        finally
        {
            lock.unlock();
        }
    }

    public ReentrantLock getLock()
    {
        return lock;
    }

    public ChunkCompileTaskGenerator.Type getType()
    {
        return type;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public int compareTo(ChunkCompileTaskGenerator p_compareTo_1_)
    {
        return Doubles.compare(distanceSq, p_compareTo_1_.distanceSq);
    }

    public double getDistanceSq()
    {
        return distanceSq;
    }

    public enum Status
    {
        PENDING,
        COMPILING,
        UPLOADING,
        DONE
    }

    public enum Type
    {
        REBUILD_CHUNK,
        RESORT_TRANSPARENCY
    }
}
