package org.spray.infinity.via;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;
import org.spray.infinity.via.platform.VRInjector;
import org.spray.infinity.via.platform.VRLoader;
import org.spray.infinity.via.platform.VRPlatform;
import org.spray.infinity.via.protocol.ViaFabricHostnameProtocol;
import org.spray.infinity.via.util.JLoggerToLog4j;
import org.spray.infinity.via.util.ProtocolSorter;
import org.spray.infinity.via.util.ProtocolUtils;

import com.google.common.collect.Range;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import net.fabricmc.loader.api.FabricLoader;

public class ViaFabric {

	public static ViaFabric INSTANCE = new ViaFabric();
	public static int CLIENT_VERSION_ID = 755;
	public static String CURRENT_VERSION;
	public static double stateValue;

	private int version;

	public static final Logger JLOGGER = new JLoggerToLog4j(LogManager.getLogger("ViaFabric"));
	public static ExecutorService ASYNC_EXECUTOR;
	public static EventLoop EVENT_LOOP;
	public static CompletableFuture<Void> INIT_FUTURE = new CompletableFuture<>();

	public void onInitialize() {
		ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ViaFabric-%d").build();
		ASYNC_EXECUTOR = Executors.newFixedThreadPool(8, factory);
		EVENT_LOOP = new DefaultEventLoop(factory);
		EVENT_LOOP.submit(INIT_FUTURE::join);
		stateValue = ProtocolSorter.getProtocolVersions().size();

		setVersion(CLIENT_VERSION_ID);
		CURRENT_VERSION = ProtocolUtils.getProtocolName(getVersion());

		Via.init(ViaManagerImpl.builder().injector(new VRInjector()).loader(new VRLoader()).platform(new VRPlatform())
				.build());

		FabricLoader.getInstance().getModContainer("viabackwards")
				.ifPresent(mod -> MappingDataLoader.enableMappingsCache());

		((ViaManagerImpl) Via.getManager()).init();

		Via.getManager().getProtocolManager().registerBaseProtocol(ViaFabricHostnameProtocol.INSTANCE,
				Range.lessThan(Integer.MIN_VALUE));
		ProtocolVersion.register(-2, "AUTO");

		FabricLoader.getInstance().getEntrypoints("viafabric:via_api_initialized", Runnable.class)
				.forEach(Runnable::run);

		INIT_FUTURE.complete(null);
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}
}
