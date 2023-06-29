package org.moonware.client.qol.via.platform;

import com.viaversion.viaversion.ViaAPIBase;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.platform.PlatformTask;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.libs.gson.JsonObject;
import net.minecraft.client.Minecraft;
import org.moonware.client.qol.via.MWVia;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MWViaPlatform implements ViaPlatform<UUID> {
    private final ViaAPI<UUID> api = new ViaAPIBase<UUID>() {};
    private final MWViaConfig config = new MWViaConfig();
    private final Logger logger = Logger.getLogger("ViaVersion");

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public String getPlatformName() {
        return "MoonWare";
    }

    @Override
    public String getPlatformVersion() {
        return "1.12.2";
    }

    @Override
    public String getPluginVersion() {
        return "4.4.2";
    }

    @Override
    public PlatformTask<?> runAsync(Runnable runnable) {
        return new MWViaPlatformTask(CompletableFuture
                .runAsync(runnable, MWViaPlatformTask.ASYNC_EXEC)
                .exceptionally(ex -> {
                    if (ex instanceof CancellationException) return null;
                    ex.printStackTrace();
                    return null;
                })
        );
    }

    @Override
    public PlatformTask<?> runSync(Runnable runnable) {
        return new MWViaPlatformTask(MWViaPlatformTask.EVENT_LOOP.submit(runnable).addListener(future -> {
            if (!future.isCancelled() && future.cause() != null) future.cause().printStackTrace();
        }));
    }

    @Override
    public PlatformTask runSync(Runnable runnable, long ticks) {
        return new MWViaPlatformTask(MWViaPlatformTask.EVENT_LOOP.schedule(() -> runSync(runnable), ticks *
                50, TimeUnit.MILLISECONDS).addListener(future -> {
            if (!future.isCancelled() && future.cause() != null) future.cause().printStackTrace();
        }));
    }

    @Override
    public PlatformTask runRepeatingSync(Runnable runnable, long ticks) {
        return new MWViaPlatformTask(MWViaPlatformTask.EVENT_LOOP.scheduleAtFixedRate(() -> runSync(runnable),
                0, ticks * 50, TimeUnit.MILLISECONDS).addListener(future -> {
            if (!future.isCancelled() && future.cause() != null) future.cause().printStackTrace();
        }));
    }

    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        return new ViaCommandSender[0];
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
    }

    @Override
    public boolean kickPlayer(UUID uuid, String message) {
        return false;
    }

    @Override
    public boolean isPluginEnabled() {
        return true;
    }

    @Override
    public ViaAPI<UUID> getApi() {
        return api;
    }

    @Override
    public ViaVersionConfig getConf() {
        return config;
    }

    @Override
    public ConfigurationProvider getConfigurationProvider() {
        return config;
    }

    @Override
    public File getDataFolder() {
        return MWVia.VIA_FOLDER;
    }

    @Override
    public void onReload() {}

    @Override
    public JsonObject getDump() {
        return new JsonObject();
    }

    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }

    @Override
    public boolean hasPlugin(String name) {
        return name.equalsIgnoreCase("ViaVersion") || name.equalsIgnoreCase("ViaBackwards");
    }
}
