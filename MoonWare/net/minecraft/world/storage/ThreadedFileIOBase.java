package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public class ThreadedFileIOBase implements Runnable
{
    /** Instance of ThreadedFileIOBase */
    private static final ThreadedFileIOBase INSTANCE = new ThreadedFileIOBase();
    private final List<IThreadedFileIO> threadedIOQueue = Collections.synchronizedList(Lists.newArrayList());
    private volatile long writeQueuedCounter;
    private volatile long savedIOCounter;
    private volatile boolean isThreadWaiting;

    private ThreadedFileIOBase()
    {
        Thread thread = new Thread(this, "File IO Thread");
        thread.setPriority(1);
        thread.start();
    }

    /**
     * Retrieves an instance of the threadedFileIOBase.
     */
    public static ThreadedFileIOBase getThreadedIOInstance()
    {
        return INSTANCE;
    }

    public void run()
    {
        while (true)
        {
            processQueue();
        }
    }

    /**
     * Process the items that are in the queue
     */
    private void processQueue()
    {
        for (int i = 0; i < threadedIOQueue.size(); ++i)
        {
            IThreadedFileIO ithreadedfileio = threadedIOQueue.get(i);
            boolean flag = ithreadedfileio.writeNextIO();

            if (!flag)
            {
                threadedIOQueue.remove(i--);
                ++savedIOCounter;
            }

            try
            {
                Thread.sleep(isThreadWaiting ? 0L : 10L);
            }
            catch (InterruptedException interruptedexception1)
            {
                interruptedexception1.printStackTrace();
            }
        }

        if (threadedIOQueue.isEmpty())
        {
            try
            {
                Thread.sleep(25L);
            }
            catch (InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }
    }

    /**
     * threaded io
     */
    public void queueIO(IThreadedFileIO fileIo)
    {
        if (!threadedIOQueue.contains(fileIo))
        {
            ++writeQueuedCounter;
            threadedIOQueue.add(fileIo);
        }
    }

    public void waitForFinish() throws InterruptedException
    {
        isThreadWaiting = true;

        while (writeQueuedCounter != savedIOCounter)
        {
            Thread.sleep(10L);
        }

        isThreadWaiting = false;
    }
}
