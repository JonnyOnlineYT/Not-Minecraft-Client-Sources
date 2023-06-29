package org.spongepowered.asm.mixin.transformer;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinInitialisationError;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
import org.spongepowered.asm.mixin.refmap.RemappingReferenceMapper;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.VersionNumber;

final class MixinConfig implements Comparable, IMixinConfig {
   private static int configOrder = 0;
   private static final Set globalMixinList = new HashSet();
   private final Logger logger = LogManager.getLogger("mixin");
   private final transient Map mixinMapping = new HashMap();
   private final transient Set unhandledTargets = new HashSet();
   private final transient List mixins = new ArrayList();
   private transient Config handle;
   @SerializedName("target")
   private String selector;
   @SerializedName("minVersion")
   private String version;
   @SerializedName("compatibilityLevel")
   private String compatibility;
   @SerializedName("required")
   private boolean required;
   @SerializedName("priority")
   private int priority = 1000;
   @SerializedName("mixinPriority")
   private int mixinPriority = 1000;
   @SerializedName("package")
   private String mixinPackage;
   @SerializedName("mixins")
   private List mixinClasses;
   @SerializedName("client")
   private List mixinClassesClient;
   @SerializedName("server")
   private List mixinClassesServer;
   @SerializedName("setSourceFile")
   private boolean setSourceFile = false;
   @SerializedName("refmap")
   private String refMapperConfig;
   @SerializedName("verbose")
   private boolean verboseLogging;
   private final transient int order;
   private final transient List listeners;
   private transient IMixinService service;
   private transient MixinEnvironment env;
   private transient String name;
   @SerializedName("plugin")
   private String pluginClassName;
   @SerializedName("injectors")
   private InjectorOptions injectorOptions;
   @SerializedName("overwrites")
   private OverwriteOptions overwriteOptions;
   private transient IMixinConfigPlugin plugin;
   private transient IReferenceMapper refMapper;
   private transient boolean prepared;
   private transient boolean visited;

   private MixinConfig() {
      this.order = configOrder++;
      this.listeners = new ArrayList();
      this.injectorOptions = new InjectorOptions();
      this.overwriteOptions = new OverwriteOptions();
      this.prepared = false;
      this.visited = false;
   }

   private boolean onLoad(IMixinService service, String name, MixinEnvironment fallbackEnvironment) {
      this.service = service;
      this.name = name;
      this.env = this.parseSelector(this.selector, fallbackEnvironment);
      this.required &= !this.env.getOption(MixinEnvironment.Option.IGNORE_REQUIRED);
      this.initCompatibilityLevel();
      this.initInjectionPoints();
      return this.checkVersion();
   }

   private void initCompatibilityLevel() {
      if (this.compatibility != null) {
         MixinEnvironment.CompatibilityLevel level = MixinEnvironment.CompatibilityLevel.valueOf(this.compatibility.trim().toUpperCase());
         MixinEnvironment.CompatibilityLevel current = MixinEnvironment.getCompatibilityLevel();
         if (level != current) {
            if (current.isAtLeast(level) && !current.canSupport(level)) {
               throw new MixinInitialisationError("Mixin config " + this.name + " requires compatibility level " + level + " which is too old");
            } else if (!current.canElevateTo(level)) {
               throw new MixinInitialisationError("Mixin config " + this.name + " requires compatibility level " + level + " which is prohibited by " + current);
            } else {
               MixinEnvironment.setCompatibilityLevel(level);
            }
         }
      }
   }

   private MixinEnvironment parseSelector(String target, MixinEnvironment fallbackEnvironment) {
      if (target != null) {
         String[] selectors = target.split("[&\\| ]");
         String[] var4 = selectors;
         int var5 = selectors.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String sel = var4[var6];
            sel = sel.trim();
            Pattern environmentSelector = Pattern.compile("^@env(?:ironment)?\\(([A-Z]+)\\)$");
            Matcher environmentSelectorMatcher = environmentSelector.matcher(sel);
            if (environmentSelectorMatcher.matches()) {
               return MixinEnvironment.getEnvironment(MixinEnvironment.Phase.forName(environmentSelectorMatcher.group(1)));
            }
         }

         MixinEnvironment.Phase phase = MixinEnvironment.Phase.forName(target);
         if (phase != null) {
            return MixinEnvironment.getEnvironment(phase);
         }
      }

      return fallbackEnvironment;
   }

   private void initInjectionPoints() {
      if (this.injectorOptions.injectionPoints != null) {
         Iterator var1 = this.injectorOptions.injectionPoints.iterator();

         while(var1.hasNext()) {
            String injectionPoint = (String)var1.next();

            try {
               Class injectionPointClass = this.service.getClassProvider().findClass(injectionPoint, true);
               if (InjectionPoint.class.isAssignableFrom(injectionPointClass)) {
                  InjectionPoint.register(injectionPointClass);
               } else {
                  this.logger.error("Unable to register injection point {} for {}, class must extend InjectionPoint", new Object[]{injectionPointClass, this});
               }
            } catch (Throwable var4) {
               this.logger.catching(var4);
            }
         }

      }
   }

   private boolean checkVersion() throws MixinInitialisationError {
      if (this.version == null) {
         this.logger.error("Mixin config {} does not specify \"minVersion\" property", new Object[]{this.name});
      }

      VersionNumber minVersion = VersionNumber.parse(this.version);
      VersionNumber curVersion = VersionNumber.parse(this.env.getVersion());
      if (minVersion.compareTo(curVersion) > 0) {
         this.logger.warn("Mixin config {} requires mixin subsystem version {} but {} was found. The mixin config will not be applied.", new Object[]{this.name, minVersion, curVersion});
         if (this.required) {
            throw new MixinInitialisationError("Required mixin config " + this.name + " requires mixin subsystem version " + minVersion);
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   void addListener(IListener listener) {
      this.listeners.add(listener);
   }

   void onSelect() {
      if (this.pluginClassName != null) {
         try {
            Class pluginClass = this.service.getClassProvider().findClass(this.pluginClassName, true);
            this.plugin = (IMixinConfigPlugin)pluginClass.newInstance();
            if (this.plugin != null) {
               this.plugin.onLoad(this.mixinPackage);
            }
         } catch (Throwable var2) {
            var2.printStackTrace();
            this.plugin = null;
         }
      }

      if (!this.mixinPackage.endsWith(".")) {
         this.mixinPackage = this.mixinPackage + ".";
      }

      boolean suppressRefMapWarning = false;
      if (this.refMapperConfig == null) {
         if (this.plugin != null) {
            this.refMapperConfig = this.plugin.getRefMapperConfig();
         }

         if (this.refMapperConfig == null) {
            suppressRefMapWarning = true;
            this.refMapperConfig = "mixin.refmap.json";
         }
      }

      this.refMapper = ReferenceMapper.read(this.refMapperConfig);
      this.verboseLogging |= this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE);
      if (!suppressRefMapWarning && this.refMapper.isDefault() && !this.env.getOption(MixinEnvironment.Option.DISABLE_REFMAP)) {
         this.logger.warn("Reference map '{}' for {} could not be read. If this is a development environment you can ignore this message", new Object[]{this.refMapperConfig, this});
      }

      if (this.env.getOption(MixinEnvironment.Option.REFMAP_REMAP)) {
         this.refMapper = RemappingReferenceMapper.of(this.env, this.refMapper);
      }

   }

   void prepare() {
      if (!this.prepared) {
         this.prepared = true;
         this.prepareMixins(this.mixinClasses, false);
         switch (this.env.getSide()) {
            case CLIENT:
               this.prepareMixins(this.mixinClassesClient, false);
               break;
            case SERVER:
               this.prepareMixins(this.mixinClassesServer, false);
               break;
            case UNKNOWN:
            default:
               this.logger.warn("Mixin environment was unable to detect the current side, sided mixins will not be applied");
         }

      }
   }

   void postInitialise() {
      if (this.plugin != null) {
         List pluginMixins = this.plugin.getMixins();
         this.prepareMixins(pluginMixins, true);
      }

      Iterator iter = this.mixins.iterator();

      while(iter.hasNext()) {
         MixinInfo mixin = (MixinInfo)iter.next();

         try {
            mixin.validate();
            Iterator var3 = this.listeners.iterator();

            while(var3.hasNext()) {
               IListener listener = (IListener)var3.next();
               listener.onInit(mixin);
            }
         } catch (InvalidMixinException var5) {
            this.logger.error(var5.getMixin() + ": " + var5.getMessage(), var5);
            this.removeMixin(mixin);
            iter.remove();
         } catch (Exception var6) {
            this.logger.error(var6.getMessage(), var6);
            this.removeMixin(mixin);
            iter.remove();
         }
      }

   }

   private void removeMixin(MixinInfo remove) {
      Iterator var2 = this.mixinMapping.values().iterator();

      while(var2.hasNext()) {
         List mixinsFor = (List)var2.next();
         Iterator iter = mixinsFor.iterator();

         while(iter.hasNext()) {
            if (remove == iter.next()) {
               iter.remove();
            }
         }
      }

   }

   private void prepareMixins(List mixinClasses, boolean suppressPlugin) {
      if (mixinClasses != null) {
         Iterator var3 = mixinClasses.iterator();

         while(true) {
            String mixinClass;
            String fqMixinClass;
            do {
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  mixinClass = (String)var3.next();
                  fqMixinClass = this.mixinPackage + mixinClass;
               } while(mixinClass == null);
            } while(globalMixinList.contains(fqMixinClass));

            MixinInfo mixin = null;

            try {
               mixin = new MixinInfo(this.service, this, mixinClass, true, this.plugin, suppressPlugin);
               if (mixin.getTargetClasses().size() > 0) {
                  globalMixinList.add(fqMixinClass);
                  Iterator var7 = mixin.getTargetClasses().iterator();

                  while(var7.hasNext()) {
                     String targetClass = (String)var7.next();
                     String targetClassName = targetClass.replace('/', '.');
                     this.mixinsFor(targetClassName).add(mixin);
                     this.unhandledTargets.add(targetClassName);
                  }

                  var7 = this.listeners.iterator();

                  while(var7.hasNext()) {
                     IListener listener = (IListener)var7.next();
                     listener.onPrepare(mixin);
                  }

                  this.mixins.add(mixin);
               }
            } catch (InvalidMixinException var10) {
               if (this.required) {
                  throw var10;
               }

               this.logger.error(var10.getMessage(), var10);
            } catch (Exception var11) {
               if (this.required) {
                  throw new InvalidMixinException(mixin, "Error initialising mixin " + mixin + " - " + var11.getClass() + ": " + var11.getMessage(), var11);
               }

               this.logger.error(var11.getMessage(), var11);
            }
         }
      }
   }

   void postApply(String transformedName, ClassNode targetClass) {
      this.unhandledTargets.remove(transformedName);
   }

   public Config getHandle() {
      if (this.handle == null) {
         this.handle = new Config(this);
      }

      return this.handle;
   }

   public boolean isRequired() {
      return this.required;
   }

   public MixinEnvironment getEnvironment() {
      return this.env;
   }

   public String getName() {
      return this.name;
   }

   public String getMixinPackage() {
      return this.mixinPackage;
   }

   public int getPriority() {
      return this.priority;
   }

   public int getDefaultMixinPriority() {
      return this.mixinPriority;
   }

   public int getDefaultRequiredInjections() {
      return this.injectorOptions.defaultRequireValue;
   }

   public String getDefaultInjectorGroup() {
      String defaultGroup = this.injectorOptions.defaultGroup;
      return defaultGroup != null && !defaultGroup.isEmpty() ? defaultGroup : "default";
   }

   public boolean conformOverwriteVisibility() {
      return this.overwriteOptions.conformAccessModifiers;
   }

   public boolean requireOverwriteAnnotations() {
      return this.overwriteOptions.requireOverwriteAnnotations;
   }

   public int getMaxShiftByValue() {
      return Math.min(Math.max(this.injectorOptions.maxShiftBy, 0), 5);
   }

   public boolean select(MixinEnvironment environment) {
      this.visited = true;
      return this.env == environment;
   }

   boolean isVisited() {
      return this.visited;
   }

   int getDeclaredMixinCount() {
      return getCollectionSize(this.mixinClasses, this.mixinClassesClient, this.mixinClassesServer);
   }

   int getMixinCount() {
      return this.mixins.size();
   }

   public List getClasses() {
      return Collections.unmodifiableList(this.mixinClasses);
   }

   public boolean shouldSetSourceFile() {
      return this.setSourceFile;
   }

   public IReferenceMapper getReferenceMapper() {
      if (this.env.getOption(MixinEnvironment.Option.DISABLE_REFMAP)) {
         return ReferenceMapper.DEFAULT_MAPPER;
      } else {
         this.refMapper.setContext(this.env.getRefmapObfuscationContext());
         return this.refMapper;
      }
   }

   String remapClassName(String className, String reference) {
      return this.getReferenceMapper().remap(className, reference);
   }

   public IMixinConfigPlugin getPlugin() {
      return this.plugin;
   }

   public Set getTargets() {
      return Collections.unmodifiableSet(this.mixinMapping.keySet());
   }

   public Set getUnhandledTargets() {
      return Collections.unmodifiableSet(this.unhandledTargets);
   }

   public Level getLoggingLevel() {
      return this.verboseLogging ? Level.INFO : Level.DEBUG;
   }

   public boolean packageMatch(String className) {
      return className.startsWith(this.mixinPackage);
   }

   public boolean hasMixinsFor(String targetClass) {
      return this.mixinMapping.containsKey(targetClass);
   }

   public List getMixinsFor(String targetClass) {
      return this.mixinsFor(targetClass);
   }

   private List mixinsFor(String targetClass) {
      List mixins = (List)this.mixinMapping.get(targetClass);
      if (mixins == null) {
         mixins = new ArrayList();
         this.mixinMapping.put(targetClass, mixins);
      }

      return (List)mixins;
   }

   public List reloadMixin(String mixinClass, byte[] bytes) {
      Iterator iter = this.mixins.iterator();

      MixinInfo mixin;
      do {
         if (!iter.hasNext()) {
            return Collections.emptyList();
         }

         mixin = (MixinInfo)iter.next();
      } while(!mixin.getClassName().equals(mixinClass));

      mixin.reloadMixin(bytes);
      return mixin.getTargetClasses();
   }

   public String toString() {
      return this.name;
   }

   public int compareTo(MixinConfig other) {
      if (other == null) {
         return 0;
      } else {
         return other.priority == this.priority ? this.order - other.order : this.priority - other.priority;
      }
   }

   static Config create(String configFile, MixinEnvironment outer) {
      try {
         IMixinService service = MixinService.getService();
         MixinConfig config = (MixinConfig)(new Gson()).fromJson(new InputStreamReader(service.getResourceAsStream(configFile)), MixinConfig.class);
         return config.onLoad(service, configFile, outer) ? config.getHandle() : null;
      } catch (Exception var4) {
         var4.printStackTrace();
         throw new IllegalArgumentException(String.format("The specified resource '%s' was invalid or could not be read", configFile), var4);
      }
   }

   private static int getCollectionSize(Collection... collections) {
      int total = 0;
      Collection[] var2 = collections;
      int var3 = collections.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Collection collection = var2[var4];
         if (collection != null) {
            total += collection.size();
         }
      }

      return total;
   }

   interface IListener {
      void onPrepare(MixinInfo var1);

      void onInit(MixinInfo var1);
   }

   static class OverwriteOptions {
      @SerializedName("conformVisibility")
      boolean conformAccessModifiers;
      @SerializedName("requireAnnotations")
      boolean requireOverwriteAnnotations;
   }

   static class InjectorOptions {
      @SerializedName("defaultRequire")
      int defaultRequireValue = 0;
      @SerializedName("defaultGroup")
      String defaultGroup = "default";
      @SerializedName("injectionPoints")
      List injectionPoints;
      @SerializedName("maxShiftBy")
      int maxShiftBy = 0;
   }
}
