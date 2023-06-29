package org.spongepowered.asm.launch.platform;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.service.MixinService;

public class MixinPlatformManager {
   private static final String DEFAULT_MAIN_CLASS = "net.minecraft.client.main.Main";
   private static final String MIXIN_TWEAKER_CLASS = "org.spongepowered.asm.launch.MixinTweaker";
   private static final Logger logger = LogManager.getLogger("mixin");
   private final Map containers = new LinkedHashMap();
   private MixinContainer primaryContainer;
   private boolean prepared = false;
   private boolean injected;

   public void init() {
      logger.debug("Initialising Mixin Platform Manager");
      URI uri = null;

      try {
         uri = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
         if (uri != null) {
            logger.debug("Mixin platform: primary container is {}", new Object[]{uri});
            this.primaryContainer = this.addContainer(uri);
         }
      } catch (URISyntaxException var3) {
         var3.printStackTrace();
      }

      this.scanClasspath();
   }

   public Collection getPhaseProviderClasses() {
      Collection phaseProviders = this.primaryContainer.getPhaseProviders();
      return (Collection)(phaseProviders != null ? Collections.unmodifiableCollection(phaseProviders) : Collections.emptyList());
   }

   public final MixinContainer addContainer(URI uri) {
      MixinContainer existingContainer = (MixinContainer)this.containers.get(uri);
      if (existingContainer != null) {
         return existingContainer;
      } else {
         logger.debug("Adding mixin platform agents for container {}", new Object[]{uri});
         MixinContainer container = new MixinContainer(this, uri);
         this.containers.put(uri, container);
         if (this.prepared) {
            container.prepare();
         }

         return container;
      }
   }

   public final void prepare(List args) {
      this.prepared = true;
      Iterator var2 = this.containers.values().iterator();

      while(var2.hasNext()) {
         MixinContainer container = (MixinContainer)var2.next();
         container.prepare();
      }

      if (args != null) {
         this.parseArgs(args);
      } else {
         String argv = System.getProperty("sun.java.command");
         if (argv != null) {
            this.parseArgs(Arrays.asList(argv.split(" ")));
         }
      }

   }

   private void parseArgs(List args) {
      boolean captureNext = false;

      String arg;
      for(Iterator var3 = args.iterator(); var3.hasNext(); captureNext = "--mixin".equals(arg)) {
         arg = (String)var3.next();
         if (captureNext) {
            this.addConfig(arg);
         }
      }

   }

   public final void inject() {
      if (!this.injected) {
         this.injected = true;
         if (this.primaryContainer != null) {
            this.primaryContainer.initPrimaryContainer();
         }

         this.scanClasspath();
         logger.debug("inject() running with {} agents", new Object[]{this.containers.size()});
         Iterator var1 = this.containers.values().iterator();

         while(var1.hasNext()) {
            MixinContainer container = (MixinContainer)var1.next();

            try {
               container.inject();
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }

      }
   }

   private void scanClasspath() {
      URL[] sources = MixinService.getService().getClassProvider().getClassPath();
      URL[] var2 = sources;
      int var3 = sources.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         URL url = var2[var4];

         try {
            URI uri = url.toURI();
            if (!this.containers.containsKey(uri)) {
               logger.debug("Scanning {} for mixin tweaker", new Object[]{uri});
               if ("file".equals(uri.getScheme()) && (new File(uri)).exists()) {
                  MainAttributes attributes = MainAttributes.of(uri);
                  String tweaker = attributes.get("TweakClass");
                  if ("org.spongepowered.asm.launch.MixinTweaker".equals(tweaker)) {
                     logger.debug("{} contains a mixin tweaker, adding agents", new Object[]{uri});
                     this.addContainer(uri);
                  }
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }

   }

   public String getLaunchTarget() {
      Iterator var1 = this.containers.values().iterator();

      String mainClass;
      do {
         if (!var1.hasNext()) {
            return "net.minecraft.client.main.Main";
         }

         MixinContainer container = (MixinContainer)var1.next();
         mainClass = container.getLaunchTarget();
      } while(mainClass == null);

      return mainClass;
   }

   final void setCompatibilityLevel(String level) {
      try {
         MixinEnvironment.CompatibilityLevel value = MixinEnvironment.CompatibilityLevel.valueOf(level.toUpperCase());
         logger.debug("Setting mixin compatibility level: {}", new Object[]{value});
         MixinEnvironment.setCompatibilityLevel(value);
      } catch (IllegalArgumentException var3) {
         logger.warn("Invalid compatibility level specified: {}", new Object[]{level});
      }

   }

   final void addConfig(String config) {
      if (config.endsWith(".json")) {
         logger.debug("Registering mixin config: {}", new Object[]{config});
         Mixins.addConfiguration(config);
      } else if (config.contains(".json@")) {
         int pos = config.indexOf(".json@");
         String phaseName = config.substring(pos + 6);
         config = config.substring(0, pos + 5);
         MixinEnvironment.Phase phase = MixinEnvironment.Phase.forName(phaseName);
         if (phase != null) {
            logger.warn("Setting config phase via manifest is deprecated: {}. Specify target in config instead", new Object[]{config});
            logger.debug("Registering mixin config: {}", new Object[]{config});
            MixinEnvironment.getEnvironment(phase).addConfiguration(config);
         }
      }

   }

   final void addTokenProvider(String provider) {
      if (provider.contains("@")) {
         String[] parts = provider.split("@", 2);
         MixinEnvironment.Phase phase = MixinEnvironment.Phase.forName(parts[1]);
         if (phase != null) {
            logger.debug("Registering token provider class: {}", new Object[]{parts[0]});
            MixinEnvironment.getEnvironment(phase).registerTokenProviderClass(parts[0]);
         }

      } else {
         MixinEnvironment.getDefaultEnvironment().registerTokenProviderClass(provider);
      }
   }
}
