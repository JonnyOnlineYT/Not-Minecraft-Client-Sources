package org.moonware.client.qol.via.platform;

import com.viaversion.viaversion.api.platform.PlatformTask;
import io.netty.channel.EventLoop;
import io.netty.channel.local.LocalEventLoopGroup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MWViaPlatformTask implements PlatformTask<Future<?>> {
    public static final ExecutorService ASYNC_EXEC = Executors.newFixedThreadPool(8, runnable -> new Thread(runnable, "Via"));
    public static final EventLoop EVENT_LOOP = new LocalEventLoopGroup(1, runnable -> new Thread(runnable, "ViaMCP")).next();
    private final Future<?> object;

    public MWViaPlatformTask(Future<?> object) {
        this.object = object;
    }

    @Override
    public Future<?> getObject() {
        return object;
    }

    @Override
    public void cancel() {
        object.cancel(false);
    }
}
