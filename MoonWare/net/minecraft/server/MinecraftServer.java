package net.minecraft.server;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.ServerPingData;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.*;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.*;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public abstract class MinecraftServer implements ICommandSender, Runnable, IThreadListener
{
    private static final Logger LOG = LogManager.getLogger();
    public static final File USER_CACHE_FILE = new File("usercache.json");
    private final ISaveFormat anvilConverterForAnvilFile;
    private final File anvilFile;
    private final List<ITickable> tickables = Lists.newArrayList();
    public final ICommandManager commandManager;
    public final Profiler theProfiler = new Profiler();
    private final NetworkSystem networkSystem;
    private final ServerPingData statusResponse = new ServerPingData();
    private final Random random = new Random();
    private final DataFixer dataFixer;
    private int serverPort = -1;
    public WorldServer[] worldServers;
    private PlayerList playerList;
    private boolean serverRunning = true;
    private boolean serverStopped;
    private int tickCounter;
    public String currentTask;
    public int percentDone;
    private boolean onlineMode;
    private boolean field_190519_A;
    private boolean canSpawnAnimals;
    private boolean canSpawnNPCs;
    private boolean pvpEnabled;
    private boolean allowFlight;
    private String motd;
    private int buildLimit;
    private int maxPlayerIdleMinutes;
    public final long[] tickTimeArray = new long[100];
    public long[][] timeOfLastDimensionTick;
    private KeyPair serverKeyPair;
    private String serverOwner;
    private String folderName;
    private String worldName;
    private boolean enableBonusChest;
    private String resourcePackUrl = "";
    private String resourcePackHash = "";
    private boolean serverIsRunning;
    private String userMessage;
    private boolean startProfiling;
    private boolean isGamemodeForced;
    private final YggdrasilAuthenticationService authService;
    private final MinecraftSessionService sessionService;
    private final GameProfileRepository profileRepo;
    private final PlayerProfileCache profileCache;
    private long nanoTimeSinceStatusRefresh;
    public final Queue < FutureTask<? >> futureTaskQueue = Queues.newArrayDeque();
    private Thread serverThread;
    private long currentTime = getCurrentTimeMillis();
    private boolean worldIconSet;

    public MinecraftServer(File anvilFileIn, DataFixer dataFixerIn, YggdrasilAuthenticationService authServiceIn, MinecraftSessionService sessionServiceIn, GameProfileRepository profileRepoIn, PlayerProfileCache profileCacheIn) {
        authService = authServiceIn;
        sessionService = sessionServiceIn;
        profileRepo = profileRepoIn;
        profileCache = profileCacheIn;
        anvilFile = anvilFileIn;
        networkSystem = new NetworkSystem(this);
        commandManager = createNewCommandManager();
        anvilConverterForAnvilFile = new AnvilSaveConverter(anvilFileIn, dataFixerIn);
        dataFixer = dataFixerIn;
    }

    public ServerCommandManager createNewCommandManager()
    {
        return new ServerCommandManager(this);
    }

    /**
     * Initialises the server and starts it.
     */
    public abstract boolean startServer() throws IOException;

    public void convertMapIfNeeded(String worldNameIn)
    {
        if (getActiveAnvilConverter().isOldMapFormat(worldNameIn))
        {
            LOG.info("Converting map!");
            setUserMessage("menu.convertingLevel");
            getActiveAnvilConverter().convertMapFormat(worldNameIn, new Progress() {
                private long startTime = System.currentTimeMillis();
                @Override
                public void setTitle(String title) {}

                @Override
                public void setText(String text) {}

                @Override
                public void setProgress(int progress) {
                    percentDone = progress;
                    if (System.currentTimeMillis() - startTime >= 1000L) {
                        startTime = System.currentTimeMillis();
                        LOG.info("Converting... {}%", progress);
                    }
                }

                @Override
                public void done() {}
            });
        }
    }

    /**
     * Typically "menu.convertingLevel", "menu.loadingLevel" or others.
     */
    protected synchronized void setUserMessage(String message)
    {
        userMessage = message;
    }

    @Nullable

    public synchronized String getUserMessage()
    {
        return userMessage;
    }

    public void loadAllWorlds(String saveName, String worldNameIn, long seed, WorldType type, String generatorOptions)
    {
        convertMapIfNeeded(saveName);
        setUserMessage("menu.loadingLevel");
        worldServers = new WorldServer[3];
        timeOfLastDimensionTick = new long[worldServers.length][100];
        ISaveHandler isavehandler = anvilConverterForAnvilFile.getSaveLoader(saveName, true);
        setResourcePackFromWorld(getFolderName(), isavehandler);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        WorldSettings worldsettings;

        if (worldinfo == null)
        {
            worldsettings = new WorldSettings(seed, getGameType(), canStructuresSpawn(), isHardcore(), type);
            worldsettings.setGeneratorOptions(generatorOptions);

            if (enableBonusChest)
            {
                worldsettings.enableBonusChest();
            }

            worldinfo = new WorldInfo(worldsettings, worldNameIn);
        }
        else
        {
            worldinfo.setWorldName(worldNameIn);
            worldsettings = new WorldSettings(worldinfo);
        }

        for (int i = 0; i < worldServers.length; ++i)
        {
            int j = 0;

            if (i == 1)
            {
                j = -1;
            }

            if (i == 2)
            {
                j = 1;
            }

            if (i == 0)
            {
                worldServers[i] = (WorldServer)(new WorldServer(this, isavehandler, worldinfo, j, theProfiler)).init();
                worldServers[i].initialize(worldsettings);
            }
            else
            {
                worldServers[i] = (WorldServer)(new WorldServerMulti(this, isavehandler, j, worldServers[0], theProfiler)).init();
            }

            worldServers[i].addEventListener(new ServerWorldEventHandler(this, worldServers[i]));

            if (!isSinglePlayer())
            {
                worldServers[i].getWorldInfo().setGameType(getGameType());
            }
        }

        playerList.setPlayerManager(worldServers);
        setDifficultyForAllWorlds(getDifficulty());
        initialWorldChunkLoad();
    }

    public void initialWorldChunkLoad()
    {
        int i = 16;
        int j = 4;
        int k = 192;
        int l = 625;
        int i1 = 0;
        setUserMessage("menu.generatingTerrain");
        int j1 = 0;
        LOG.info("Preparing start region for level 0");
        WorldServer worldserver = worldServers[0];
        BlockPos blockpos = worldserver.getSpawnPoint();
        long k1 = getCurrentTimeMillis();

        for (int l1 = -192; l1 <= 192 && isServerRunning(); l1 += 16)
        {
            for (int i2 = -192; i2 <= 192 && isServerRunning(); i2 += 16)
            {
                percentDone = i1 * 100 / 625;
                long j2 = getCurrentTimeMillis();

                if (j2 - k1 > 1000L)
                {
                    outputPercentRemaining("Preparing spawn area", i1 * 100 / 625);
                    k1 = j2;
                }

                ++i1;
                worldserver.getChunkProvider().provideChunk(blockpos.getX() + l1 >> 4, blockpos.getZ() + i2 >> 4);
            }
        }

        clearCurrentTask();
    }

    public void setResourcePackFromWorld(String worldNameIn, ISaveHandler saveHandlerIn)
    {
        File file1 = new File(saveHandlerIn.getWorldDirectory(), "resources.zip");

        if (file1.isFile())
        {
            try
            {
                setResourcePack("level://" + URLEncoder.encode(worldNameIn, StandardCharsets.UTF_8.toString()) + "/" + "resources.zip", "");
            }
            catch (UnsupportedEncodingException var5)
            {
                LOG.warn("Something went wrong url encoding {}", worldNameIn);
            }
        }
    }

    public abstract boolean canStructuresSpawn();

    public abstract GameType getGameType();

    /**
     * Get the server's difficulty
     */
    public abstract EnumDifficulty getDifficulty();

    /**
     * Defaults to false.
     */
    public abstract boolean isHardcore();

    public abstract int getOpPermissionLevel();

    /**
     * Get if RCON command events should be broadcast to ops
     */
    public abstract boolean shouldBroadcastRconToOps();

    /**
     * Get if console command events should be broadcast to ops
     */
    public abstract boolean shouldBroadcastConsoleToOps();

    /**
     * Used to display a percent remaining given text and the percentage.
     */
    protected void outputPercentRemaining(String message, int percent)
    {
        currentTask = message;
        percentDone = percent;
        LOG.info("{}: {}%", message, Integer.valueOf(percent));
    }

    /**
     * Set current task to null and set its percentage to 0.
     */
    protected void clearCurrentTask()
    {
        currentTask = null;
        percentDone = 0;
    }

    /**
     * par1 indicates if a log message should be output.
     */
    public void saveAllWorlds(boolean isSilent)
    {
        for (WorldServer worldserver : worldServers)
        {
            if (worldserver != null)
            {
                if (!isSilent)
                {
                    LOG.info("Saving chunks for level '{}'/{}", worldserver.getWorldInfo().getWorldName(), worldserver.provider.getDimensionType().getName());
                }

                try
                {
                    worldserver.saveAllChunks(true, null);
                }
                catch (MinecraftException minecraftexception)
                {
                    LOG.warn(minecraftexception.getMessage());
                }
            }
        }
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    public void stopServer()
    {
        LOG.info("Stopping server");

        if (getNetworkSystem() != null)
        {
            getNetworkSystem().terminateEndpoints();
        }

        if (playerList != null)
        {
            LOG.info("Saving players");
            playerList.saveAllPlayerData();
            playerList.removeAllPlayers();
        }

        if (worldServers != null)
        {
            LOG.info("Saving worlds");

            for (WorldServer worldserver : worldServers)
            {
                if (worldserver != null)
                {
                    worldserver.disableLevelSaving = false;
                }
            }

            saveAllWorlds(false);

            for (WorldServer worldserver1 : worldServers)
            {
                if (worldserver1 != null)
                {
                    worldserver1.flush();
                }
            }
        }
    }

    public boolean isServerRunning()
    {
        return serverRunning;
    }

    /**
     * Sets the serverRunning variable to false, in order to get the server to shut down.
     */
    public void initiateShutdown()
    {
        serverRunning = false;
    }

    public void run()
    {
        try
        {
            if (startServer())
            {
                currentTime = getCurrentTimeMillis();
                long i = 0L;
                statusResponse.setDescription(new TextComponent(motd));
                statusResponse.setVersion(new ServerPingData.Version("1.12.2", 340));
                applyServerIconToResponse(statusResponse);

                while (serverRunning)
                {
                    long k = getCurrentTimeMillis();
                    long j = k - currentTime;
                    if (j < 0L)
                    {
                        LOG.warn("Time ran backwards! Did the system time change?");
                        j = 0L;
                    }

                    i += j;
                    currentTime = k;

                    if (worldServers[0].areAllPlayersAsleep())
                    {
                        tick();
                        i = 0L;
                    }
                    else
                    {
                        while (i > 50L)
                        {
                            i -= 50L;
                            tick();
                        }
                    }

                    Thread.sleep(Math.max(1L, 50L - i));
                    serverIsRunning = true;
                }
            }
            else
            {
                finalTick(null);
            }
        }
        catch (Throwable throwable1)
        {
            LOG.error("Encountered an unexpected exception", throwable1);
            CrashReport crashreport = null;

            if (throwable1 instanceof ReportedException)
            {
                crashreport = addServerInfoToCrashReport(((ReportedException)throwable1).getCrashReport());
            }
            else
            {
                crashreport = addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", throwable1));
            }

            File file1 = new File(new File(getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (crashreport.saveToFile(file1))
            {
                LOG.error("This crash report has been saved to: {}", file1.getAbsolutePath());
            }
            else
            {
                LOG.error("We were unable to save this crash report to disk.");
            }

            finalTick(crashreport);
        }
        finally
        {
            try
            {
                serverStopped = true;
                stopServer();
            }
            catch (Throwable throwable)
            {
                LOG.error("Exception stopping the server", throwable);
            }
            finally
            {
                systemExitNow();
            }
        }
    }

    public void applyServerIconToResponse(ServerPingData response)
    {
        File file1 = getFile("server-icon.png");

        if (!file1.exists())
        {
            file1 = getActiveAnvilConverter().getFile(getFolderName(), "icon.png");
        }

        if (file1.isFile())
        {
            ByteBuf bytebuf = Unpooled.buffer();

            try
            {
                BufferedImage bufferedimage = ImageIO.read(file1);
                Preconditions.checkState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
                Preconditions.checkState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
                ImageIO.write(bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
                ByteBuf bytebuf1 = Base64.encode(bytebuf);
                response.setFavicon("data:image/png;base64," + bytebuf1.toString(StandardCharsets.UTF_8));
            }
            catch (Exception exception)
            {
                LOG.error("Couldn't load server icon", exception);
            }
            finally
            {
                bytebuf.release();
            }
        }
    }

    public boolean isWorldIconSet()
    {
        worldIconSet = worldIconSet || getWorldIconFile().isFile();
        return worldIconSet;
    }

    public File getWorldIconFile()
    {
        return getActiveAnvilConverter().getFile(getFolderName(), "icon.png");
    }

    public File getDataDirectory()
    {
        return new File(".");
    }

    /**
     * Called on exit from the main run() loop.
     */
    public void finalTick(CrashReport report)
    {
    }

    /**
     * Directly calls System.exit(0), instantly killing the program.
     */
    public void systemExitNow()
    {
    }

    /**
     * Main function called by run() every loop.
     */
    public void tick()
    {
        long i = System.nanoTime();
        ++tickCounter;

        if (startProfiling)
        {
            startProfiling = false;
            theProfiler.profilingEnabled = true;
            theProfiler.clearProfiling();
        }

        theProfiler.startSection("root");
        updateTimeLightAndEntities();

        if (i - nanoTimeSinceStatusRefresh >= 5000000000L)
        {
            nanoTimeSinceStatusRefresh = i;
            statusResponse.setPlayers(new ServerPingData.Players(getMaxPlayers(), getCurrentPlayerCount()));
            GameProfile[] agameprofile = new GameProfile[Math.min(getCurrentPlayerCount(), 12)];
            int j = MathHelper.getInt(random, 0, getCurrentPlayerCount() - agameprofile.length);

            for (int k = 0; k < agameprofile.length; ++k)
            {
                agameprofile[k] = playerList.getPlayerList().get(j + k).getGameProfile();
            }

            Collections.shuffle(Arrays.asList(agameprofile));
            statusResponse.getPlayers().setPlayers(agameprofile);
        }

        if (tickCounter % 900 == 0)
        {
            theProfiler.startSection("save");
            playerList.saveAllPlayerData();
            saveAllWorlds(true);
            theProfiler.endSection();
        }

        theProfiler.startSection("tallying");
        tickTimeArray[tickCounter % 100] = System.nanoTime() - i;
        theProfiler.endSection();
        theProfiler.endSection();
    }

    public void updateTimeLightAndEntities()
    {
        theProfiler.startSection("jobs");

        synchronized (futureTaskQueue)
        {
            while (!futureTaskQueue.isEmpty())
            {
                OS.runTask(futureTaskQueue.poll(), LOG);
            }
        }

        theProfiler.endStartSection("levels");

        for (int j = 0; j < worldServers.length; ++j)
        {
            long i = System.nanoTime();

            if (j == 0 || getAllowNether())
            {
                WorldServer worldserver = worldServers[j];
                theProfiler.func_194340_a(() ->
                {
                    return worldserver.getWorldInfo().getWorldName();
                });

                if (tickCounter % 20 == 0)
                {
                    theProfiler.startSection("timeSync");
                    playerList.sendPacketToAllPlayersInDimension(new SPacketTimeUpdate(worldserver.getTotalWorldTime(), worldserver.getWorldTime(), worldserver.getGameRules().getBoolean("doDaylightCycle")), worldserver.provider.getDimensionType().getId());
                    theProfiler.endSection();
                }

                theProfiler.startSection("tick");

                try
                {
                    worldserver.tick();
                }
                catch (Throwable throwable1)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Exception ticking world");
                    worldserver.addWorldInfoToCrashReport(crashreport);
                    throw new ReportedException(crashreport);
                }

                try
                {
                    worldserver.updateEntities();
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Exception ticking world entities");
                    worldserver.addWorldInfoToCrashReport(crashreport1);
                    throw new ReportedException(crashreport1);
                }

                theProfiler.endSection();
                theProfiler.startSection("tracker");
                worldserver.getEntityTracker().updateTrackedEntities();
                theProfiler.endSection();
                theProfiler.endSection();
            }

            timeOfLastDimensionTick[j][tickCounter % 100] = System.nanoTime() - i;
        }

        theProfiler.endStartSection("connection");
        getNetworkSystem().networkTick();
        theProfiler.endStartSection("players");
        playerList.onTick();
        theProfiler.endStartSection("commandFunctions");
        func_193030_aL().update();
        theProfiler.endStartSection("tickables");

        for (int k = 0; k < tickables.size(); ++k)
        {
            tickables.get(k).update();
        }

        theProfiler.endSection();
    }

    public boolean getAllowNether()
    {
        return true;
    }

    public void startServerThread()
    {
        serverThread = new Thread(this, "Server thread");
        serverThread.start();
    }

    /**
     * Returns a File object from the specified string.
     */
    public File getFile(String fileName)
    {
        return new File(getDataDirectory(), fileName);
    }

    /**
     * Logs the message with a level of WARN.
     */
    public void logWarning(String msg)
    {
        LOG.warn(msg);
    }

    /**
     * Gets the worldServer by the given dimension.
     */
    public WorldServer worldServerForDimension(int dimension)
    {
        if (dimension == -1)
        {
            return worldServers[1];
        }
        else
        {
            return dimension == 1 ? worldServers[2] : worldServers[0];
        }
    }

    /**
     * Returns the server's Minecraft version as string.
     */
    public String getInstanceVersion()
    {
        return "1.12.2";
    }

    /**
     * Returns the number of players currently on the server.
     */
    public int getCurrentPlayerCount()
    {
        return playerList.getCurrentPlayerCount();
    }

    /**
     * Returns the maximum number of players allowed on the server.
     */
    public int getMaxPlayers()
    {
        return playerList.getMaxPlayers();
    }

    /**
     * Returns an array of the usernames of all the connected players.
     */
    public String[] getAllUsernames()
    {
        return playerList.getAllUsernames();
    }

    /**
     * Returns an array of the GameProfiles of all the connected players
     */
    public GameProfile[] getGameProfiles()
    {
        return playerList.getAllProfiles();
    }

    public String getServerModName()
    {
        return "vanilla";
    }

    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    public CrashReport addServerInfoToCrashReport(CrashReport report)
    {
        report.getCategory().setDetail("Profiler Position", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return theProfiler.profilingEnabled ? theProfiler.getNameOfLastSection() : "N/A (disabled)";
            }
        });

        if (playerList != null)
        {
            report.getCategory().setDetail("Player Count", new ICrashReportDetail<String>()
            {
                public String call()
                {
                    return playerList.getCurrentPlayerCount() + " / " + playerList.getMaxPlayers() + "; " + playerList.getPlayerList();
                }
            });
        }

        return report;
    }

    public List<String> getTabCompletions(ICommandSender sender, String input, @Nullable BlockPos pos, boolean hasTargetBlock)
    {
        List<String> list = Lists.newArrayList();
        boolean flag = input.startsWith("/");

        if (flag)
        {
            input = input.substring(1);
        }

        if (!flag && !hasTargetBlock)
        {
            String[] astring = input.split(" ", -1);
            String s2 = astring[astring.length - 1];

            for (String s1 : playerList.getAllUsernames())
            {
                if (CommandBase.doesStringStartWith(s2, s1))
                {
                    list.add(s1);
                }
            }

            return list;
        }
        else
        {
            boolean flag1 = !input.contains(" ");
            List<String> list1 = commandManager.getTabCompletionOptions(sender, input, pos);

            if (!list1.isEmpty())
            {
                for (String s : list1)
                {
                    if (flag1 && !hasTargetBlock)
                    {
                        list.add("/" + s);
                    }
                    else
                    {
                        list.add(s);
                    }
                }
            }

            return list;
        }
    }

    public boolean isAnvilFileSet()
    {
        return anvilFile != null;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return "Server";
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void addChatMessage(Component component)
    {
        LOG.info(component.asString());
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
        return true;
    }

    public ICommandManager getCommandManager()
    {
        return commandManager;
    }

    /**
     * Gets KeyPair instanced in MinecraftServer.
     */
    public KeyPair getKeyPair()
    {
        return serverKeyPair;
    }

    /**
     * Returns the username of the server owner (for integrated servers)
     */
    public String getServerOwner()
    {
        return serverOwner;
    }

    /**
     * Sets the username of the owner of this server (in the case of an integrated server)
     */
    public void setServerOwner(String owner)
    {
        serverOwner = owner;
    }

    public boolean isSinglePlayer()
    {
        return serverOwner != null;
    }

    public String getFolderName()
    {
        return folderName;
    }

    public void setFolderName(String name)
    {
        folderName = name;
    }

    public void setWorldName(String worldNameIn)
    {
        worldName = worldNameIn;
    }

    public String getWorldName()
    {
        return worldName;
    }

    public void setKeyPair(KeyPair keyPair)
    {
        serverKeyPair = keyPair;
    }

    public void setDifficultyForAllWorlds(EnumDifficulty difficulty)
    {
        for (WorldServer worldserver1 : worldServers)
        {
            if (worldserver1 != null)
            {
                if (worldserver1.getWorldInfo().isHardcoreModeEnabled())
                {
                    worldserver1.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
                    worldserver1.setAllowedSpawnTypes(true, true);
                }
                else if (isSinglePlayer())
                {
                    worldserver1.getWorldInfo().setDifficulty(difficulty);
                    worldserver1.setAllowedSpawnTypes(worldserver1.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                }
                else
                {
                    worldserver1.getWorldInfo().setDifficulty(difficulty);
                    worldserver1.setAllowedSpawnTypes(allowSpawnMonsters(), canSpawnAnimals);
                }
            }
        }
    }

    public boolean allowSpawnMonsters()
    {
        return true;
    }

    public void canCreateBonusChest(boolean enable)
    {
        enableBonusChest = enable;
    }

    public ISaveFormat getActiveAnvilConverter()
    {
        return anvilConverterForAnvilFile;
    }

    public String getResourcePackUrl()
    {
        return resourcePackUrl;
    }

    public String getResourcePackHash()
    {
        return resourcePackHash;
    }

    public void setResourcePack(String url, String hash)
    {
        resourcePackUrl = url;
        resourcePackHash = hash;
    }

    public abstract boolean isDedicatedServer();

    public boolean isServerInOnlineMode()
    {
        return onlineMode;
    }

    public void setOnlineMode(boolean online)
    {
        onlineMode = online;
    }

    public boolean func_190518_ac()
    {
        return field_190519_A;
    }

    public boolean getCanSpawnAnimals()
    {
        return canSpawnAnimals;
    }

    public void setCanSpawnAnimals(boolean spawnAnimals)
    {
        canSpawnAnimals = spawnAnimals;
    }

    public boolean getCanSpawnNPCs()
    {
        return canSpawnNPCs;
    }

    /**
     * Get if native transport should be used. Native transport means linux server performance improvements and
     * optimized packet sending/receiving on linux
     */
    public abstract boolean shouldUseNativeTransport();

    public void setCanSpawnNPCs(boolean spawnNpcs)
    {
        canSpawnNPCs = spawnNpcs;
    }

    public boolean isPVPEnabled()
    {
        return pvpEnabled;
    }

    public void setAllowPvp(boolean allowPvp)
    {
        pvpEnabled = allowPvp;
    }

    public boolean isFlightAllowed()
    {
        return allowFlight;
    }

    public void setAllowFlight(boolean allow)
    {
        allowFlight = allow;
    }

    /**
     * Return whether command blocks are enabled.
     */
    public abstract boolean isCommandBlockEnabled();

    public String getMOTD()
    {
        return motd;
    }

    public void setMOTD(String motdIn)
    {
        motd = motdIn;
    }

    public int getBuildLimit()
    {
        return buildLimit;
    }

    public void setBuildLimit(int maxBuildHeight)
    {
        buildLimit = maxBuildHeight;
    }

    public boolean isServerStopped()
    {
        return serverStopped;
    }

    public PlayerList getPlayerList()
    {
        return playerList;
    }

    public void setPlayerList(PlayerList list)
    {
        playerList = list;
    }

    /**
     * Sets the game type for all worlds.
     */
    public void setGameType(GameType gameMode)
    {
        for (WorldServer worldserver1 : worldServers)
        {
            worldserver1.getWorldInfo().setGameType(gameMode);
        }
    }

    public NetworkSystem getNetworkSystem()
    {
        return networkSystem;
    }

    public boolean serverIsInRunLoop()
    {
        return serverIsRunning;
    }

    public boolean getGuiEnabled()
    {
        return false;
    }

    /**
     * On dedicated does nothing. On integrated, sets commandsAllowedForAll, gameType and allows external connections.
     */
    public abstract String shareToLAN(GameType type, boolean allowCheats);

    public int getTickCounter()
    {
        return tickCounter;
    }

    public void enableProfiling()
    {
        startProfiling = true;
    }

    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    public World getEntityWorld()
    {
        return worldServers[0];
    }

    public boolean isBlockProtected(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        return false;
    }

    /**
     * Get the forceGamemode field (whether joining players will be put in their old gamemode or the default one)
     */
    public boolean getForceGamemode()
    {
        return isGamemodeForced;
    }

    public static long getCurrentTimeMillis()
    {
        return System.currentTimeMillis();
    }

    public int getMaxPlayerIdleMinutes()
    {
        return maxPlayerIdleMinutes;
    }

    public void setPlayerIdleTimeout(int idleTimeout)
    {
        maxPlayerIdleMinutes = idleTimeout;
    }

    public MinecraftSessionService getInstanceSessionService()
    {
        return sessionService;
    }

    public GameProfileRepository getGameProfileRepository()
    {
        return profileRepo;
    }

    public PlayerProfileCache getPlayerProfileCache()
    {
        return profileCache;
    }

    public ServerPingData getServerStatusResponse()
    {
        return statusResponse;
    }

    public void refreshStatusNextTick()
    {
        nanoTimeSinceStatusRefresh = 0L;
    }

    @Nullable
    public Entity getEntityFromUuid(UUID uuid)
    {
        for (WorldServer worldserver1 : worldServers)
        {
            if (worldserver1 != null)
            {
                Entity entity = worldserver1.getEntityFromUuid(uuid);

                if (entity != null)
                {
                    return entity;
                }
            }
        }

        return null;
    }

    /**
     * Returns true if the command sender should be sent feedback about executed commands
     */
    public boolean sendCommandFeedback()
    {
        return worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
    }

    /**
     * Get the Minecraft server instance
     */
    public MinecraftServer getServer()
    {
        return this;
    }

    public int getMaxWorldSize()
    {
        return 29999984;
    }

    public <V> ListenableFuture<V> callFromMainThread(Callable<V> callable)
    {
        Preconditions.checkNotNull(callable);

        if (!isThisThread() && !isServerStopped())
        {
            ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.create(callable);

            synchronized (futureTaskQueue)
            {
                futureTaskQueue.add(listenablefuturetask);
                return listenablefuturetask;
            }
        }
        else
        {
            try
            {
                return Futures.immediateFuture(callable.call());
            }
            catch (Exception exception)
            {
                return Futures.immediateFailedFuture(exception);
            }
        }
    }

    public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule)
    {
        Preconditions.checkNotNull(runnableToSchedule);
        return callFromMainThread(Executors.callable(runnableToSchedule));
    }

    public boolean isThisThread()
    {
        return Thread.currentThread() == serverThread;
    }

    /**
     * The compression treshold. If the packet is larger than the specified amount of bytes, it will be compressed
     */
    public int getNetworkCompressionThreshold()
    {
        return 256;
    }

    public int getSpawnRadius(@Nullable WorldServer worldIn)
    {
        return worldIn != null ? worldIn.getGameRules().getInt("spawnRadius") : 10;
    }

    public AdvancementManager func_191949_aK()
    {
        return worldServers[0].func_191952_z();
    }

    public FunctionManager func_193030_aL()
    {
        return worldServers[0].func_193037_A();
    }

    public void func_193031_aM()
    {
        if (isThisThread())
        {
            getPlayerList().saveAllPlayerData();
            worldServers[0].getLootTableManager().reloadLootTables();
            func_191949_aK().func_192779_a();
            func_193030_aL().func_193059_f();
            getPlayerList().func_193244_w();
        }
        else
        {
            addScheduledTask(this::func_193031_aM);
        }
    }
}
