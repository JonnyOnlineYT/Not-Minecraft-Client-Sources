package org.spray.heaven.via;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import io.netty.channel.EventLoop;
import io.netty.channel.local.LocalEventLoopGroup;

import org.apache.logging.log4j.LogManager;
import org.spray.heaven.via.loader.VRBackwardsLoader;
import org.spray.heaven.via.loader.VRProviderLoader;
import org.spray.heaven.via.loader.VRRewindLoader;
import org.spray.heaven.via.platform.VRInjector;
import org.spray.heaven.via.platform.VRPlatform;
import org.spray.heaven.via.utils.JLoggerToLog4j;
import org.spray.heaven.via.utils.ProtocolUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

public class ViaMCP {
	public final static int PROTOCOL_VERSION = 340;
	private static final ViaMCP instance = new ViaMCP();

	public static ViaMCP getInstance() {
		return instance;
	}

	private final Logger jLogger = new JLoggerToLog4j(LogManager.getLogger("ViaMCP"));
	private final CompletableFuture<Void> INIT_FUTURE = new CompletableFuture<>();

	private ExecutorService ASYNC_EXEC;
	private EventLoop EVENT_LOOP;

	private File file;
	private int version;
	private String lastServer;

	public float stateValue;
	public String CURRENT_VERSION;

	public void start() {
		ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ViaMCP-%d").build();
		ASYNC_EXEC = Executors.newFixedThreadPool(8, factory);

		EVENT_LOOP = new LocalEventLoopGroup(1, factory).next();
		EVENT_LOOP.submit(INIT_FUTURE::join);

		setVersion(PROTOCOL_VERSION);
		stateValue = 0.4f;
		CURRENT_VERSION = ProtocolUtils.getProtocolName(getVersion());
		this.file = new File("ViaMCP");
		if (this.file.mkdir()) {
			this.getjLogger().info("Creating ViaMCP Folder");
		}

		Via.init(ViaManagerImpl.builder().injector(new VRInjector()).loader(new VRProviderLoader())
				.platform(new VRPlatform(file)).build());

		MappingDataLoader.enableMappingsCache();
		((ViaManagerImpl) Via.getManager()).init();

		new VRBackwardsLoader(file);
		new VRRewindLoader(file);

		INIT_FUTURE.complete(null);
	}

	public Logger getjLogger() {
		return jLogger;
	}

	public CompletableFuture<Void> getInitFuture() {
		return INIT_FUTURE;
	}

	public ExecutorService getAsyncExecutor() {
		return ASYNC_EXEC;
	}

	public EventLoop getEventLoop() {
		return EVENT_LOOP;
	}

	public File getFile() {
		return file;
	}

	public String getLastServer() {
		return lastServer;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setLastServer(String lastServer) {
		this.lastServer = lastServer;
	}
}
