package net.minecraft.server.integrated;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.OS;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import optifine.Reflector;
import optifine.WorldServerOF;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedServer extends MinecraftServer
{
    private static final Logger LOGGER = LogManager.getLogger();

    /** The Minecraft instance. */
    private final Minecraft mc;
    private final WorldSettings theWorldSettings;
    private boolean isGamePaused;
    private boolean isPublic;
    private ThreadLanServerPing lanServerPing;

    public IntegratedServer(Minecraft clientIn, String folderNameIn, String worldNameIn, WorldSettings worldSettingsIn, YggdrasilAuthenticationService authServiceIn, MinecraftSessionService sessionServiceIn, GameProfileRepository profileRepoIn, PlayerProfileCache profileCacheIn)
    {
        super(new File(Minecraft.gameDir, "saves"), Minecraft.getDataFixer(), authServiceIn, sessionServiceIn, profileRepoIn, profileCacheIn);
        setServerOwner(Minecraft.getSession().getUsername());
        setFolderName(folderNameIn);
        setWorldName(worldNameIn);
        canCreateBonusChest(worldSettingsIn.isBonusChestEnabled());
        setBuildLimit(256);
        setPlayerList(new IntegratedPlayerList(this));
        mc = clientIn;
        theWorldSettings = worldSettingsIn;
    }

    public ServerCommandManager createNewCommandManager()
    {
        return new IntegratedServerCommandManager(this);
    }

    public void loadAllWorlds(String saveName, String worldNameIn, long seed, WorldType type, String generatorOptions)
    {
        convertMapIfNeeded(saveName);
        ISaveHandler isavehandler = getActiveAnvilConverter().getSaveLoader(saveName, true);
        setResourcePackFromWorld(getFolderName(), isavehandler);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();

        if (Reflector.DimensionManager.exists())
        {
            WorldServer worldserver = (WorldServer)(new WorldServerOF(this, isavehandler, worldinfo, 0, theProfiler)).init();
            worldserver.initialize(theWorldSettings);
            Integer[] ainteger = (Integer[])Reflector.call(Reflector.DimensionManager_getStaticDimensionIDs);
            Integer[] ainteger1 = ainteger;
            int i1 = ainteger.length;

            for (int j1 = 0; j1 < i1; ++j1)
            {
                int k = ainteger1[j1].intValue();
                WorldServer worldserver1 = k == 0 ? worldserver : (WorldServer) (new WorldServerMulti(this, isavehandler, k, worldserver, theProfiler)).init();
                worldserver1.addEventListener(new ServerWorldEventHandler(this, worldserver1));

                if (!isSinglePlayer())
                {
                    worldserver1.getWorldInfo().setGameType(getGameType());
                }

                if (Reflector.EventBus.exists())
                {
                    Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, worldserver1);
                }
            }

            getPlayerList().setPlayerManager(new WorldServer[] {worldserver});

            if (worldserver.getWorldInfo().getDifficulty() == null)
            {
                setDifficultyForAllWorlds(Minecraft.gameSettings.difficulty);
            }
        }
        else
        {
            worldServers = new WorldServer[3];
            timeOfLastDimensionTick = new long[worldServers.length][100];
            setResourcePackFromWorld(getFolderName(), isavehandler);

            if (worldinfo == null)
            {
                worldinfo = new WorldInfo(theWorldSettings, worldNameIn);
            }
            else
            {
                worldinfo.setWorldName(worldNameIn);
            }

            for (int l = 0; l < worldServers.length; ++l)
            {
                int i1 = 0;

                if (l == 1)
                {
                    i1 = -1;
                }

                if (l == 2)
                {
                    i1 = 1;
                }

                if (l == 0)
                {
                    worldServers[l] = (WorldServer)(new WorldServerOF(this, isavehandler, worldinfo, i1, theProfiler)).init();
                    worldServers[l].initialize(theWorldSettings);
                }
                else
                {
                    worldServers[l] = (WorldServer)(new WorldServerMulti(this, isavehandler, i1, worldServers[0], theProfiler)).init();
                }

                worldServers[l].addEventListener(new ServerWorldEventHandler(this, worldServers[l]));
            }

            getPlayerList().setPlayerManager(worldServers);

            if (worldServers[0].getWorldInfo().getDifficulty() == null)
            {
                setDifficultyForAllWorlds(Minecraft.gameSettings.difficulty);
            }
        }

        initialWorldChunkLoad();
    }

    /**
     * Initialises the server and starts it.
     */
    public boolean startServer() throws IOException
    {
        LOGGER.info("Starting integrated minecraft server version 1.12.2");
        setOnlineMode(true);
        setCanSpawnAnimals(true);
        setCanSpawnNPCs(true);
        setAllowPvp(true);
        setAllowFlight(true);
        LOGGER.info("Generating keypair");
        setKeyPair(CryptManager.generateKeyPair());

        if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists())
        {
            Object object = Reflector.call(Reflector.FMLCommonHandler_instance);

            if (!Reflector.callBoolean(object, Reflector.FMLCommonHandler_handleServerAboutToStart, this))
            {
                return false;
            }
        }

        loadAllWorlds(getFolderName(), getWorldName(), theWorldSettings.getSeed(), theWorldSettings.getTerrainType(), theWorldSettings.getGeneratorOptions());
        setMOTD(getServerOwner() + " - " + worldServers[0].getWorldInfo().getWorldName());

        if (Reflector.FMLCommonHandler_handleServerStarting.exists())
        {
            Object object1 = Reflector.call(Reflector.FMLCommonHandler_instance);

            if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == Boolean.TYPE)
            {
                return Reflector.callBoolean(object1, Reflector.FMLCommonHandler_handleServerStarting, this);
            }

            Reflector.callVoid(object1, Reflector.FMLCommonHandler_handleServerStarting, this);
        }

        return true;
    }

    /**
     * Main function called by run() every loop.
     */
    public void tick()
    {
        boolean flag = isGamePaused;
        isGamePaused = Minecraft.getMinecraft().getConnection() != null && Minecraft.getMinecraft().isGamePaused();

        if (!flag && isGamePaused)
        {
            LOGGER.info("Saving and pausing game...");
            getPlayerList().saveAllPlayerData();
            saveAllWorlds(false);
        }

        if (isGamePaused)
        {
            synchronized (futureTaskQueue)
            {
                while (!futureTaskQueue.isEmpty())
                {
                    OS.runTask(futureTaskQueue.poll(), LOGGER);
                }
            }
        }
        else
        {
            super.tick();

            if (Minecraft.gameSettings.renderDistance != getPlayerList().getViewDistance())
            {
                LOGGER.info("Changing view distance to {}, from {}", Integer.valueOf(Minecraft.gameSettings.renderDistance), Integer.valueOf(getPlayerList().getViewDistance()));
                getPlayerList().setViewDistance(Minecraft.gameSettings.renderDistance);
            }

            if (Minecraft.world != null)
            {
                WorldInfo worldinfo1 = worldServers[0].getWorldInfo();
                WorldInfo worldinfo = Minecraft.world.getWorldInfo();

                if (!worldinfo1.isDifficultyLocked() && worldinfo.getDifficulty() != worldinfo1.getDifficulty())
                {
                    LOGGER.info("Changing difficulty to {}, from {}", worldinfo.getDifficulty(), worldinfo1.getDifficulty());
                    setDifficultyForAllWorlds(worldinfo.getDifficulty());
                }
                else if (worldinfo.isDifficultyLocked() && !worldinfo1.isDifficultyLocked())
                {
                    LOGGER.info("Locking difficulty to {}", worldinfo.getDifficulty());

                    for (WorldServer worldserver : worldServers)
                    {
                        if (worldserver != null)
                        {
                            worldserver.getWorldInfo().setDifficultyLocked(true);
                        }
                    }
                }
            }
        }
    }

    public boolean canStructuresSpawn()
    {
        return false;
    }

    public GameType getGameType()
    {
        return theWorldSettings.getGameType();
    }

    /**
     * Get the server's difficulty
     */
    public EnumDifficulty getDifficulty()
    {
        return Minecraft.world == null ? Minecraft.gameSettings.difficulty : Minecraft.world.getWorldInfo().getDifficulty();
    }

    /**
     * Defaults to false.
     */
    public boolean isHardcore()
    {
        return theWorldSettings.getHardcoreEnabled();
    }

    /**
     * Get if RCON command events should be broadcast to ops
     */
    public boolean shouldBroadcastRconToOps()
    {
        return true;
    }

    /**
     * Get if console command events should be broadcast to ops
     */
    public boolean shouldBroadcastConsoleToOps()
    {
        return true;
    }

    /**
     * par1 indicates if a log message should be output.
     */
    public void saveAllWorlds(boolean isSilent)
    {
        super.saveAllWorlds(isSilent);
    }

    public File getDataDirectory()
    {
        return Minecraft.gameDir;
    }

    public boolean isDedicatedServer()
    {
        return false;
    }

    /**
     * Get if native transport should be used. Native transport means linux server performance improvements and
     * optimized packet sending/receiving on linux
     */
    public boolean shouldUseNativeTransport()
    {
        return false;
    }

    /**
     * Called on exit from the main run() loop.
     */
    public void finalTick(CrashReport report)
    {
        Minecraft.displayCrashReport(report);
    }

    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    public CrashReport addServerInfoToCrashReport(CrashReport report)
    {
        report = super.addServerInfoToCrashReport(report);
        report.getCategory().setDetail("Type", () -> "Integrated Server (map_client.txt)");
        report.getCategory().setDetail("Is Modded", () -> "Definitely; Client brand changed to 'MoonWare'");
        return report;
    }

    public void setDifficultyForAllWorlds(EnumDifficulty difficulty)
    {
        super.setDifficultyForAllWorlds(difficulty);

        if (Minecraft.world != null)
        {
            Minecraft.world.getWorldInfo().setDifficulty(difficulty);
        }
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return false;
    }

    /**
     * On dedicated does nothing. On integrated, sets commandsAllowedForAll, gameType and allows external connections.
     */
    public String shareToLAN(GameType type, boolean allowCheats)
    {
        try
        {
            int i = -1;

            try
            {
                i = HttpUtil.getSuitableLanPort();
            }
            catch (IOException var5)
            {
            }

            if (i <= 0)
            {
                i = 25564;
            }

            getNetworkSystem().addLanEndpoint(null, i);
            LOGGER.info("Started on {}", i);
            isPublic = true;
            lanServerPing = new ThreadLanServerPing(getMOTD(), i + "");
            lanServerPing.start();
            getPlayerList().setGameType(type);
            getPlayerList().setCommandsAllowedForAll(allowCheats);
            Minecraft.player.setPermissionLevel(allowCheats ? 4 : 0);
            return i + "";
        }
        catch (IOException var61)
        {
            return null;
        }
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    public void stopServer()
    {
        super.stopServer();

        if (lanServerPing != null)
        {
            lanServerPing.interrupt();
            lanServerPing = null;
        }
    }

    /**
     * Sets the serverRunning variable to false, in order to get the server to shut down.
     */
    public void initiateShutdown()
    {
        if (!Reflector.MinecraftForge.exists() || isServerRunning())
        {
            Futures.getUnchecked(addScheduledTask(new Runnable()
            {
                public void run()
                {
                    for (EntityPlayerMP entityplayermp : Lists.newArrayList(getPlayerList().getPlayerList()))
                    {
                        if (!entityplayermp.getUniqueID().equals(Minecraft.player.getUniqueID()))
                        {
                            getPlayerList().playerLoggedOut(entityplayermp);
                        }
                    }
                }
            }));
        }

        super.initiateShutdown();

        if (lanServerPing != null)
        {
            lanServerPing.interrupt();
            lanServerPing = null;
        }
    }

    /**
     * Returns true if this integrated server is open to LAN
     */
    public boolean getPublic()
    {
        return isPublic;
    }

    /**
     * Sets the game type for all worlds.
     */
    public void setGameType(GameType gameMode)
    {
        super.setGameType(gameMode);
        getPlayerList().setGameType(gameMode);
    }

    /**
     * Return whether command blocks are enabled.
     */
    public boolean isCommandBlockEnabled()
    {
        return true;
    }

    public int getOpPermissionLevel()
    {
        return 4;
    }
}
