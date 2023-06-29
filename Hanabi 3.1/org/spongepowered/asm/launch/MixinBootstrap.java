package org.spongepowered.asm.launch;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.platform.MixinPlatformManager;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.service.MixinService;

public abstract class MixinBootstrap {
   public static final String VERSION = "0.7.11";
   private static final Logger logger = LogManager.getLogger("mixin");
   private static boolean initialised = false;
   private static boolean initState = true;
   private static MixinPlatformManager platform;

   private MixinBootstrap() {
   }

   /** @deprecated */
   @Deprecated
   public static void addProxy() {
      MixinService.getService().beginPhase();
   }

   public static MixinPlatformManager getPlatform() {
      if (platform == null) {
         Object globalPlatformManager = GlobalProperties.get("mixin.platform");
         if (globalPlatformManager instanceof MixinPlatformManager) {
            platform = (MixinPlatformManager)globalPlatformManager;
         } else {
            platform = new MixinPlatformManager();
            GlobalProperties.put("mixin.platform", platform);
            platform.init();
         }
      }

      return platform;
   }

   public static void init() {
      if (start()) {
         doInit((List)null);
      }
   }

   static boolean start() {
      if (isSubsystemRegistered()) {
         if (!checkSubsystemVersion()) {
            throw new MixinInitialisationError("Mixin subsystem version " + getActiveSubsystemVersion() + " was already initialised. Cannot bootstrap version " + "0.7.11");
         } else {
            return false;
         }
      } else {
         registerSubsystem("0.7.11");
         if (!initialised) {
            initialised = true;
            String command = System.getProperty("sun.java.command");
            if (command != null && command.contains("GradleStart")) {
               System.setProperty("mixin.env.remapRefMap", "true");
            }

            MixinEnvironment.Phase initialPhase = MixinService.getService().getInitialPhase();
            if (initialPhase == MixinEnvironment.Phase.DEFAULT) {
               logger.error("Initialising mixin subsystem after game pre-init phase! Some mixins may be skipped.");
               MixinEnvironment.init(initialPhase);
               getPlatform().prepare((List)null);
               initState = false;
            } else {
               MixinEnvironment.init(initialPhase);
            }

            MixinService.getService().beginPhase();
         }

         getPlatform();
         return true;
      }
   }

   static void doInit(List args) {
      if (!initialised) {
         if (isSubsystemRegistered()) {
            logger.warn("Multiple Mixin containers present, init suppressed for 0.7.11");
         } else {
            throw new IllegalStateException("MixinBootstrap.doInit() called before MixinBootstrap.start()");
         }
      } else {
         getPlatform().getPhaseProviderClasses();
         if (initState) {
            getPlatform().prepare(args);
            MixinService.getService().init();
         }

      }
   }

   static void inject() {
      getPlatform().inject();
   }

   private static boolean isSubsystemRegistered() {
      return GlobalProperties.get("mixin.initialised") != null;
   }

   private static boolean checkSubsystemVersion() {
      return "0.7.11".equals(getActiveSubsystemVersion());
   }

   private static Object getActiveSubsystemVersion() {
      Object version = GlobalProperties.get("mixin.initialised");
      return version != null ? version : "";
   }

   private static void registerSubsystem(String version) {
      GlobalProperties.put("mixin.initialised", version);
   }

   static {
      MixinService.boot();
      MixinService.getService().prepare();
   }
}
