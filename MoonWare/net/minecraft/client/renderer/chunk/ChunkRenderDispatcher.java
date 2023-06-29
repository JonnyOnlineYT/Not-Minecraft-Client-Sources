package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.primitives.Doubles;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.VertexBufferUploader;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkRenderDispatcher
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ThreadFactory THREAD_FACTORY = (new ThreadFactoryBuilder()).setNameFormat("Chunk Batcher %d").setDaemon(true).build();
    private final int countRenderBuilders;
    private final List<Thread> listWorkerThreads;
    private final List<ChunkRenderWorker> listThreadedWorkers;
    private final PriorityBlockingQueue<ChunkCompileTaskGenerator> queueChunkUpdates;
    private final BlockingQueue<RegionRenderCacheBuilder> queueFreeRenderBuilders;
    private final WorldVertexBufferUploader worldVertexUploader;
    private final VertexBufferUploader vertexUploader;
    private final Queue<ChunkRenderDispatcher.PendingUpload> queueChunkUploads;
    private final ChunkRenderWorker renderWorker;

    public ChunkRenderDispatcher()
    {
        this(-1);
    }

    public ChunkRenderDispatcher(int p_i7_1_)
    {
        listWorkerThreads = Lists.newArrayList();
        listThreadedWorkers = Lists.newArrayList();
        queueChunkUpdates = Queues.newPriorityBlockingQueue();
        worldVertexUploader = new WorldVertexBufferUploader();
        vertexUploader = new VertexBufferUploader();
        queueChunkUploads = Queues.newPriorityQueue();
        int i = Math.max(1, (int)((double)Runtime.getRuntime().maxMemory() * 0.3D) / 10485760);
        int j = Math.max(1, MathHelper.clamp(Runtime.getRuntime().availableProcessors(), 1, i / 5));

        if (p_i7_1_ < 0)
        {
            countRenderBuilders = MathHelper.clamp(j, 1, i);
        }
        else
        {
            countRenderBuilders = p_i7_1_;
        }

        if (j > 1)
        {
            for (int k = 0; k < j; ++k)
            {
                ChunkRenderWorker chunkrenderworker = new ChunkRenderWorker(this);
                Thread thread = THREAD_FACTORY.newThread(chunkrenderworker);
                thread.start();
                listThreadedWorkers.add(chunkrenderworker);
                listWorkerThreads.add(thread);
            }
        }

        queueFreeRenderBuilders = Queues.newArrayBlockingQueue(countRenderBuilders);

        for (int l = 0; l < countRenderBuilders; ++l)
        {
            queueFreeRenderBuilders.add(new RegionRenderCacheBuilder());
        }

        renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
    }

    public String getDebugInfo()
    {
        return listWorkerThreads.isEmpty() ? String.format("pC: %03d, single-threaded", queueChunkUpdates.size()) : String.format("pC: %03d, pU: %1d, aB: %1d", queueChunkUpdates.size(), queueChunkUploads.size(), queueFreeRenderBuilders.size());
    }

    public boolean runChunkUploads(long p_178516_1_)
    {
        boolean flag = false;

        while (true)
        {
            boolean flag1 = false;

            if (listWorkerThreads.isEmpty())
            {
                ChunkCompileTaskGenerator chunkcompiletaskgenerator = queueChunkUpdates.poll();

                if (chunkcompiletaskgenerator != null)
                {
                    try
                    {
                        renderWorker.processTask(chunkcompiletaskgenerator);
                        flag1 = true;
                    }
                    catch (InterruptedException var8)
                    {
                        LOGGER.warn("Skipped task due to interrupt");
                    }
                }
            }

            synchronized (queueChunkUploads)
            {
                if (!queueChunkUploads.isEmpty())
                {
                    (queueChunkUploads.poll()).uploadTask.run();
                    flag1 = true;
                    flag = true;
                }
            }

            if (p_178516_1_ == 0L || !flag1 || p_178516_1_ < System.nanoTime())
            {
                break;
            }
        }

        return flag;
    }

    public boolean updateChunkLater(RenderChunk chunkRenderer)
    {
        chunkRenderer.getLockCompileTask().lock();
        boolean flag;

        try
        {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();
            chunkcompiletaskgenerator.addFinishRunnable(new Runnable()
            {
                public void run()
                {
                    queueChunkUpdates.remove(chunkcompiletaskgenerator);
                }
            });
            boolean flag1 = queueChunkUpdates.offer(chunkcompiletaskgenerator);

            if (!flag1)
            {
                chunkcompiletaskgenerator.finish();
            }

            flag = flag1;
        }
        finally
        {
            chunkRenderer.getLockCompileTask().unlock();
        }

        return flag;
    }

    public boolean updateChunkNow(RenderChunk chunkRenderer)
    {
        chunkRenderer.getLockCompileTask().lock();
        boolean flag;

        try
        {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();

            try
            {
                renderWorker.processTask(chunkcompiletaskgenerator);
            }
            catch (InterruptedException var8)
            {
            }

            flag = true;
        }
        finally
        {
            chunkRenderer.getLockCompileTask().unlock();
        }

        return flag;
    }

    public void stopChunkUpdates()
    {
        clearChunkUpdates();
        List<RegionRenderCacheBuilder> list = Lists.newArrayList();

        while (list.size() != countRenderBuilders)
        {
            runChunkUploads(Long.MAX_VALUE);

            try
            {
                list.add(allocateRenderBuilder());
            }
            catch (InterruptedException var3)
            {
            }
        }

        queueFreeRenderBuilders.addAll(list);
    }

    public void freeRenderBuilder(RegionRenderCacheBuilder p_178512_1_)
    {
        queueFreeRenderBuilders.add(p_178512_1_);
    }

    public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException
    {
        return queueFreeRenderBuilders.take();
    }

    public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException
    {
        return queueChunkUpdates.take();
    }

    public boolean updateTransparencyLater(RenderChunk chunkRenderer)
    {
        chunkRenderer.getLockCompileTask().lock();
        boolean flag1;

        try
        {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskTransparency();

            if (chunkcompiletaskgenerator != null)
            {
                chunkcompiletaskgenerator.addFinishRunnable(new Runnable()
                {
                    public void run()
                    {
                        queueChunkUpdates.remove(chunkcompiletaskgenerator);
                    }
                });
                boolean flag2 = queueChunkUpdates.offer(chunkcompiletaskgenerator);
                return flag2;
            }

            boolean flag = true;
            flag1 = flag;
        }
        finally
        {
            chunkRenderer.getLockCompileTask().unlock();
        }

        return flag1;
    }

    public ListenableFuture<Object> uploadChunk(BlockRenderLayer p_188245_1_, BufferBuilder p_188245_2_, RenderChunk p_188245_3_, CompiledChunk p_188245_4_, double p_188245_5_)
    {
        if (Minecraft.getMinecraft().isThisThread())
        {
            if (OpenGlHelper.useVbo())
            {
                uploadVertexBuffer(p_188245_2_, p_188245_3_.getVertexBufferByLayer(p_188245_1_.ordinal()));
            }
            else
            {
                uploadDisplayList(p_188245_2_, ((ListedRenderChunk)p_188245_3_).getDisplayList(p_188245_1_, p_188245_4_), p_188245_3_);
            }

            p_188245_2_.setTranslation(0.0D, 0.0D, 0.0D);
            return Futures.immediateFuture(null);
        }
        else
        {
            ListenableFutureTask<Object> listenablefuturetask = ListenableFutureTask.create(new Runnable()
            {
                public void run()
                {
                    uploadChunk(p_188245_1_, p_188245_2_, p_188245_3_, p_188245_4_, p_188245_5_);
                }
            }, null);

            synchronized (queueChunkUploads)
            {
                queueChunkUploads.add(new ChunkRenderDispatcher.PendingUpload(listenablefuturetask, p_188245_5_));
                return listenablefuturetask;
            }
        }
    }

    private void uploadDisplayList(BufferBuilder p_178510_1_, int p_178510_2_, RenderChunk chunkRenderer)
    {
        GlStateManager.glNewList(p_178510_2_, 4864);
        GlStateManager.pushMatrix();
        chunkRenderer.multModelviewMatrix();
        worldVertexUploader.draw(p_178510_1_);
        GlStateManager.popMatrix();
        GlStateManager.glEndList();
    }

    private void uploadVertexBuffer(BufferBuilder p_178506_1_, VertexBuffer vertexBufferIn)
    {
        vertexUploader.setVertexBuffer(vertexBufferIn);
        vertexUploader.draw(p_178506_1_);
    }

    public void clearChunkUpdates()
    {
        while (!queueChunkUpdates.isEmpty())
        {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = queueChunkUpdates.poll();

            if (chunkcompiletaskgenerator != null)
            {
                chunkcompiletaskgenerator.finish();
            }
        }
    }

    public boolean hasChunkUpdates()
    {
        return queueChunkUpdates.isEmpty() && queueChunkUploads.isEmpty();
    }

    public void stopWorkerThreads()
    {
        clearChunkUpdates();

        for (ChunkRenderWorker chunkrenderworker : listThreadedWorkers)
        {
            chunkrenderworker.notifyToStop();
        }

        for (Thread thread : listWorkerThreads)
        {
            try
            {
                thread.interrupt();
                thread.join();
            }
            catch (InterruptedException interruptedexception)
            {
                LOGGER.warn("Interrupted whilst waiting for worker to die", interruptedexception);
            }
        }

        queueFreeRenderBuilders.clear();
    }

    public boolean hasNoFreeRenderBuilders()
    {
        return queueFreeRenderBuilders.isEmpty();
    }

    class PendingUpload implements Comparable<ChunkRenderDispatcher.PendingUpload>
    {
        private final ListenableFutureTask<Object> uploadTask;
        private final double distanceSq;

        public PendingUpload(ListenableFutureTask<Object> p_i46994_2_, double p_i46994_3_)
        {
            uploadTask = p_i46994_2_;
            distanceSq = p_i46994_3_;
        }

        public int compareTo(ChunkRenderDispatcher.PendingUpload p_compareTo_1_)
        {
            return Doubles.compare(distanceSq, p_compareTo_1_.distanceSq);
        }
    }
}
