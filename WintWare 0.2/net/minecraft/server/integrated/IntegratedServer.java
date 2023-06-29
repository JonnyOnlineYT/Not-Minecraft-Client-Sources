package net.minecraft.server.integrated;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.concurrent.FutureTask;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import optifine.Reflector;
import optifine.WorldServerOF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedServer extends MinecraftServer {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Minecraft mc;
   private final WorldSettings theWorldSettings;
   private boolean isGamePaused;
   private boolean isPublic;
   private ThreadLanServerPing lanServerPing;

   public IntegratedServer(Minecraft clientIn, String folderNameIn, String worldNameIn, WorldSettings worldSettingsIn, YggdrasilAuthenticationService authServiceIn, MinecraftSessionService sessionServiceIn, GameProfileRepository profileRepoIn, PlayerProfileCache profileCacheIn) {
      super(new File(clientIn.mcDataDir, "saves"), clientIn.getProxy(), clientIn.getDataFixer(), authServiceIn, sessionServiceIn, profileRepoIn, profileCacheIn);
      this.setServerOwner(clientIn.getSession().getUsername());
      this.setFolderName(folderNameIn);
      this.setWorldName(worldNameIn);
      this.setDemo(clientIn.isDemo());
      this.canCreateBonusChest(worldSettingsIn.isBonusChestEnabled());
      this.setBuildLimit(256);
      this.setPlayerList(new IntegratedPlayerList(this));
      this.mc = clientIn;
      this.theWorldSettings = this.isDemo() ? WorldServerDemo.DEMO_WORLD_SETTINGS : worldSettingsIn;
   }

   public ServerCommandManager createNewCommandManager() {
      return new IntegratedServerCommandManager(this);
   }

   public void loadAllWorlds(String saveName, String worldNameIn, long seed, WorldType type, String generatorOptions) {
      this.convertMapIfNeeded(saveName);
      ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(saveName, true);
      this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
      WorldInfo worldinfo = isavehandler.loadWorldInfo();
      if (Reflector.DimensionManager.exists()) {
         WorldServer worldserver = this.isDemo() ? (WorldServer)(new WorldServerDemo(this, isavehandler, worldinfo, 0, this.theProfiler)).init() : (WorldServer)(new WorldServerOF(this, isavehandler, worldinfo, 0, this.theProfiler)).init();
         worldserver.initialize(this.theWorldSettings);
         Integer[] ainteger = (Integer[])((Integer[])Reflector.call(Reflector.DimensionManager_getStaticDimensionIDs));
         Integer[] ainteger1 = ainteger;
         int i1 = ainteger.length;

         for(int j1 = 0; j1 < i1; ++j1) {
            int k = ainteger1[j1];
            WorldServer worldserver1 = k == 0 ? worldserver : (WorldServer)(new WorldServerMulti(this, isavehandler, k, worldserver, this.theProfiler)).init();
            worldserver1.addEventListener(new ServerWorldEventHandler(this, worldserver1));
            if (!this.isSinglePlayer()) {
               worldserver1.getWorldInfo().setGameType(this.getGameType());
            }

            if (Reflector.EventBus.exists()) {
               Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, worldserver1);
            }
         }

         this.getPlayerList().setPlayerManager(new WorldServer[]{worldserver});
         if (worldserver.getWorldInfo().getDifficulty() == null) {
            this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
         }
      } else {
         this.worldServers = new WorldServer[3];
         this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
         this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
         if (worldinfo == null) {
            worldinfo = new WorldInfo(this.theWorldSettings, worldNameIn);
         } else {
            worldinfo.setWorldName(worldNameIn);
         }

         for(int l = 0; l < this.worldServers.length; ++l) {
            int i1 = 0;
            if (l == 1) {
               i1 = -1;
            }

            if (l == 2) {
               i1 = 1;
            }

            if (l == 0) {
               if (this.isDemo()) {
                  this.worldServers[l] = (WorldServer)(new WorldServerDemo(this, isavehandler, worldinfo, i1, this.theProfiler)).init();
               } else {
                  this.worldServers[l] = (WorldServer)(new WorldServerOF(this, isavehandler, worldinfo, i1, this.theProfiler)).init();
               }

               this.worldServers[l].initialize(this.theWorldSettings);
            } else {
               this.worldServers[l] = (WorldServer)(new WorldServerMulti(this, isavehandler, i1, this.worldServers[0], this.theProfiler)).init();
            }

            this.worldServers[l].addEventListener(new ServerWorldEventHandler(this, this.worldServers[l]));
         }

         this.getPlayerList().setPlayerManager(this.worldServers);
         if (this.worldServers[0].getWorldInfo().getDifficulty() == null) {
            this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
         }
      }

      this.initialWorldChunkLoad();
   }

   public boolean startServer() throws IOException {
      LOGGER.info("Starting integrated minecraft server version 1.12.2");
      this.setOnlineMode(true);
      this.setCanSpawnAnimals(true);
      this.setCanSpawnNPCs(true);
      this.setAllowPvp(true);
      this.setAllowFlight(true);
      LOGGER.info("Generating keypair");
      this.setKeyPair(CryptManager.generateKeyPair());
      Object object1;
      if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists()) {
         object1 = Reflector.call(Reflector.FMLCommonHandler_instance);
         if (!Reflector.callBoolean(object1, Reflector.FMLCommonHandler_handleServerAboutToStart, this)) {
            return false;
         }
      }

      this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.getGeneratorOptions());
      this.setMOTD(this.getServerOwner() + " - " + this.worldServers[0].getWorldInfo().getWorldName());
      if (Reflector.FMLCommonHandler_handleServerStarting.exists()) {
         object1 = Reflector.call(Reflector.FMLCommonHandler_instance);
         if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == Boolean.TYPE) {
            return Reflector.callBoolean(object1, Reflector.FMLCommonHandler_handleServerStarting, this);
         }

         Reflector.callVoid(object1, Reflector.FMLCommonHandler_handleServerStarting, this);
      }

      return true;
   }

   public void tick() {
      boolean flag = this.isGamePaused;
      this.isGamePaused = Minecraft.getMinecraft().getConnection() != null && Minecraft.getMinecraft().isGamePaused();
      if (!flag && this.isGamePaused) {
         LOGGER.info("Saving and pausing game...");
         this.getPlayerList().saveAllPlayerData();
         this.saveAllWorlds(false);
      }

      if (this.isGamePaused) {
         synchronized(this.futureTaskQueue) {
            while(!this.futureTaskQueue.isEmpty()) {
               Util.runTask((FutureTask)this.futureTaskQueue.poll(), LOGGER);
            }
         }
      } else {
         super.tick();
         if (this.mc.gameSettings.renderDistanceChunks != this.getPlayerList().getViewDistance()) {
            LOGGER.info("Changing view distance to {}, from {}", this.mc.gameSettings.renderDistanceChunks, this.getPlayerList().getViewDistance());
            this.getPlayerList().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
         }

         if (this.mc.world != null) {
            WorldInfo worldinfo1 = this.worldServers[0].getWorldInfo();
            WorldInfo worldinfo = this.mc.world.getWorldInfo();
            if (!worldinfo1.isDifficultyLocked() && worldinfo.getDifficulty() != worldinfo1.getDifficulty()) {
               LOGGER.info("Changing difficulty to {}, from {}", worldinfo.getDifficulty(), worldinfo1.getDifficulty());
               this.setDifficultyForAllWorlds(worldinfo.getDifficulty());
            } else if (worldinfo.isDifficultyLocked() && !worldinfo1.isDifficultyLocked()) {
               LOGGER.info("Locking difficulty to {}", worldinfo.getDifficulty());
               WorldServer[] var4 = this.worldServers;
               int var5 = var4.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  WorldServer worldserver = var4[var6];
                  if (worldserver != null) {
                     worldserver.getWorldInfo().setDifficultyLocked(true);
                  }
               }
            }
         }
      }

   }

   public boolean canStructuresSpawn() {
      return false;
   }

   public GameType getGameType() {
      return this.theWorldSettings.getGameType();
   }

   public EnumDifficulty getDifficulty() {
      return this.mc.world == null ? this.mc.gameSettings.difficulty : this.mc.world.getWorldInfo().getDifficulty();
   }

   public boolean isHardcore() {
      return this.theWorldSettings.getHardcoreEnabled();
   }

   public boolean shouldBroadcastRconToOps() {
      return true;
   }

   public boolean shouldBroadcastConsoleToOps() {
      return true;
   }

   public void saveAllWorlds(boolean isSilent) {
      super.saveAllWorlds(isSilent);
   }

   public File getDataDirectory() {
      return this.mc.mcDataDir;
   }

   public boolean isDedicatedServer() {
      return false;
   }

   public boolean shouldUseNativeTransport() {
      return false;
   }

   public void finalTick(CrashReport report) {
      this.mc.crashed(report);
   }

   public CrashReport addServerInfoToCrashReport(CrashReport report) {
      report = super.addServerInfoToCrashReport(report);
      report.getCategory().setDetail("Type", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return "Integrated Server (map_client.txt)";
         }
      });
      report.getCategory().setDetail("Is Modded", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            String s = ClientBrandRetriever.getClientModName();
            if (!s.equals("vanilla")) {
               return "Definitely; Client brand changed to '" + s + "'";
            } else {
               s = IntegratedServer.this.getServerModName();
               if (!"vanilla".equals(s)) {
                  return "Definitely; Server brand changed to '" + s + "'";
               } else {
                  return Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both client + server brands are untouched.";
               }
            }
         }
      });
      return report;
   }

   public void setDifficultyForAllWorlds(EnumDifficulty difficulty) {
      super.setDifficultyForAllWorlds(difficulty);
      if (this.mc.world != null) {
         this.mc.world.getWorldInfo().setDifficulty(difficulty);
      }

   }

   public void addServerStatsToSnooper(Snooper playerSnooper) {
      super.addServerStatsToSnooper(playerSnooper);
      playerSnooper.addClientStat("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
   }

   public boolean isSnooperEnabled() {
      return Minecraft.getMinecraft().isSnooperEnabled();
   }

   public String shareToLAN(GameType type, boolean allowCheats) {
      try {
         int i = -1;

         try {
            i = HttpUtil.getSuitableLanPort();
         } catch (IOException var5) {
         }

         if (i <= 0) {
            i = 25564;
         }

         this.getNetworkSystem().addLanEndpoint((InetAddress)null, i);
         LOGGER.info("Started on {}", i);
         this.isPublic = true;
         this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), i + "");
         this.lanServerPing.start();
         this.getPlayerList().setGameType(type);
         this.getPlayerList().setCommandsAllowedForAll(allowCheats);
         this.mc.player.setPermissionLevel(allowCheats ? 4 : 0);
         return i + "";
      } catch (IOException var6) {
         return null;
      }
   }

   public void stopServer() {
      super.stopServer();
      if (this.lanServerPing != null) {
         this.lanServerPing.interrupt();
         this.lanServerPing = null;
      }

   }

   public void initiateShutdown() {
      if (!Reflector.MinecraftForge.exists() || this.isServerRunning()) {
         Futures.getUnchecked(this.addScheduledTask(new Runnable() {
            public void run() {
               Iterator var1 = Lists.newArrayList(IntegratedServer.this.getPlayerList().getPlayerList()).iterator();

               while(var1.hasNext()) {
                  EntityPlayerMP entityplayermp = (EntityPlayerMP)var1.next();
                  if (!entityplayermp.getUniqueID().equals(IntegratedServer.this.mc.player.getUniqueID())) {
                     IntegratedServer.this.getPlayerList().playerLoggedOut(entityplayermp);
                  }
               }

            }
         }));
      }

      super.initiateShutdown();
      if (this.lanServerPing != null) {
         this.lanServerPing.interrupt();
         this.lanServerPing = null;
      }

   }

   public boolean getPublic() {
      return this.isPublic;
   }

   public void setGameType(GameType gameMode) {
      super.setGameType(gameMode);
      this.getPlayerList().setGameType(gameMode);
   }

   public boolean isCommandBlockEnabled() {
      return true;
   }

   public int getOpPermissionLevel() {
      return 4;
   }
}
