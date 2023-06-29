package org.spongepowered.asm.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.mixin.extensibility.IEnvironmentTokenProvider;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.mixin.transformer.MixinTransformer;
import org.spongepowered.asm.obfuscation.RemapperChain;
import org.spongepowered.asm.service.ILegacyClassTransformer;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.ITransformer;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.ITokenProvider;
import org.spongepowered.asm.util.JavaVersion;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.asm.util.perf.Profiler;

public final class MixinEnvironment implements ITokenProvider {
   private static final Set excludeTransformers = Sets.newHashSet(new String[]{"net.minecraftforge.fml.common.asm.transformers.EventSubscriptionTransformer", "cpw.mods.fml.common.asm.transformers.EventSubscriptionTransformer", "net.minecraftforge.fml.common.asm.transformers.TerminalTransformer", "cpw.mods.fml.common.asm.transformers.TerminalTransformer"});
   private static MixinEnvironment currentEnvironment;
   private static Phase currentPhase;
   private static CompatibilityLevel compatibility;
   private static boolean showHeader;
   private static final Logger logger;
   private static final Profiler profiler;
   private final IMixinService service = MixinService.getService();
   private final Phase phase;
   private final String configsKey;
   private final boolean[] options;
   private final Set tokenProviderClasses = new HashSet();
   private final List tokenProviders = new ArrayList();
   private final Map internalTokens = new HashMap();
   private final RemapperChain remappers = new RemapperChain();
   private Side side;
   private List transformers;
   private String obfuscationContext = null;

   MixinEnvironment(Phase phase) {
      this.phase = phase;
      this.configsKey = "mixin.configs." + this.phase.name.toLowerCase();
      Object version = this.getVersion();
      if (version != null && "0.7.11".equals(version)) {
         this.service.checkEnv(this);
         this.options = new boolean[MixinEnvironment.Option.values().length];
         Option[] var3 = MixinEnvironment.Option.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Option option = var3[var5];
            this.options[option.ordinal()] = option.getBooleanValue();
         }

         if (showHeader) {
            showHeader = false;
            this.printHeader(version);
         }

      } else {
         throw new MixinException("Environment conflict, mismatched versions or you didn't call MixinBootstrap.init()");
      }
   }

   private void printHeader(Object version) {
      String codeSource = this.getCodeSource();
      String serviceName = this.service.getName();
      Side side = this.getSide();
      logger.info("SpongePowered MIXIN Subsystem Version={} Source={} Service={} Env={}", new Object[]{version, codeSource, serviceName, side});
      boolean verbose = this.getOption(MixinEnvironment.Option.DEBUG_VERBOSE);
      if (verbose || this.getOption(MixinEnvironment.Option.DEBUG_EXPORT) || this.getOption(MixinEnvironment.Option.DEBUG_PROFILER)) {
         PrettyPrinter printer = new PrettyPrinter(32);
         printer.add("SpongePowered MIXIN%s", verbose ? " (Verbose debugging enabled)" : "").centre().hr();
         printer.kv("Code source", codeSource);
         printer.kv("Internal Version", version);
         printer.kv("Java 8 Supported", MixinEnvironment.CompatibilityLevel.JAVA_8.isSupported()).hr();
         printer.kv("Service Name", serviceName);
         printer.kv("Service Class", this.service.getClass().getName()).hr();
         Option[] var7 = MixinEnvironment.Option.values();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Option option = var7[var9];
            StringBuilder indent = new StringBuilder();

            for(int i = 0; i < option.depth; ++i) {
               indent.append("- ");
            }

            printer.kv(option.property, "%s<%s>", indent, option);
         }

         printer.hr().kv("Detected Side", side);
         printer.print(System.err);
      }

   }

   private String getCodeSource() {
      try {
         return this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
      } catch (Throwable var2) {
         return "Unknown";
      }
   }

   public Phase getPhase() {
      return this.phase;
   }

   /** @deprecated */
   @Deprecated
   public List getMixinConfigs() {
      List mixinConfigs = (List)GlobalProperties.get(this.configsKey);
      if (mixinConfigs == null) {
         mixinConfigs = new ArrayList();
         GlobalProperties.put(this.configsKey, mixinConfigs);
      }

      return (List)mixinConfigs;
   }

   /** @deprecated */
   @Deprecated
   public MixinEnvironment addConfiguration(String config) {
      logger.warn("MixinEnvironment::addConfiguration is deprecated and will be removed. Use Mixins::addConfiguration instead!");
      Mixins.addConfiguration(config, this);
      return this;
   }

   void registerConfig(String config) {
      List configs = this.getMixinConfigs();
      if (!configs.contains(config)) {
         configs.add(config);
      }

   }

   /** @deprecated */
   @Deprecated
   public MixinEnvironment registerErrorHandlerClass(String handlerName) {
      Mixins.registerErrorHandlerClass(handlerName);
      return this;
   }

   public MixinEnvironment registerTokenProviderClass(String providerName) {
      if (!this.tokenProviderClasses.contains(providerName)) {
         try {
            Class providerClass = this.service.getClassProvider().findClass(providerName, true);
            IEnvironmentTokenProvider provider = (IEnvironmentTokenProvider)providerClass.newInstance();
            this.registerTokenProvider(provider);
         } catch (Throwable var4) {
            logger.error("Error instantiating " + providerName, var4);
         }
      }

      return this;
   }

   public MixinEnvironment registerTokenProvider(IEnvironmentTokenProvider provider) {
      if (provider != null && !this.tokenProviderClasses.contains(provider.getClass().getName())) {
         String providerName = provider.getClass().getName();
         TokenProviderWrapper wrapper = new TokenProviderWrapper(provider, this);
         logger.info("Adding new token provider {} to {}", new Object[]{providerName, this});
         this.tokenProviders.add(wrapper);
         this.tokenProviderClasses.add(providerName);
         Collections.sort(this.tokenProviders);
      }

      return this;
   }

   public Integer getToken(String token) {
      token = token.toUpperCase();
      Iterator var2 = this.tokenProviders.iterator();

      Integer value;
      do {
         if (!var2.hasNext()) {
            return (Integer)this.internalTokens.get(token);
         }

         TokenProviderWrapper provider = (TokenProviderWrapper)var2.next();
         value = provider.getToken(token);
      } while(value == null);

      return value;
   }

   /** @deprecated */
   @Deprecated
   public Set getErrorHandlerClasses() {
      return Mixins.getErrorHandlerClasses();
   }

   public Object getActiveTransformer() {
      return GlobalProperties.get("mixin.transformer");
   }

   public void setActiveTransformer(ITransformer transformer) {
      if (transformer != null) {
         GlobalProperties.put("mixin.transformer", transformer);
      }

   }

   public MixinEnvironment setSide(Side side) {
      if (side != null && this.getSide() == MixinEnvironment.Side.UNKNOWN && side != MixinEnvironment.Side.UNKNOWN) {
         this.side = side;
      }

      return this;
   }

   public Side getSide() {
      if (this.side == null) {
         Side[] var1 = MixinEnvironment.Side.values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Side side = var1[var3];
            if (side.detect()) {
               this.side = side;
               break;
            }
         }
      }

      return this.side != null ? this.side : MixinEnvironment.Side.UNKNOWN;
   }

   public String getVersion() {
      return (String)GlobalProperties.get("mixin.initialised");
   }

   public boolean getOption(Option option) {
      return this.options[option.ordinal()];
   }

   public void setOption(Option option, boolean value) {
      this.options[option.ordinal()] = value;
   }

   public String getOptionValue(Option option) {
      return option.getStringValue();
   }

   public Enum getOption(Option option, Enum defaultValue) {
      return option.getEnumValue(defaultValue);
   }

   public void setObfuscationContext(String context) {
      this.obfuscationContext = context;
   }

   public String getObfuscationContext() {
      return this.obfuscationContext;
   }

   public String getRefmapObfuscationContext() {
      String overrideObfuscationType = MixinEnvironment.Option.OBFUSCATION_TYPE.getStringValue();
      return overrideObfuscationType != null ? overrideObfuscationType : this.obfuscationContext;
   }

   public RemapperChain getRemappers() {
      return this.remappers;
   }

   public void audit() {
      Object activeTransformer = this.getActiveTransformer();
      if (activeTransformer instanceof MixinTransformer) {
         MixinTransformer transformer = (MixinTransformer)activeTransformer;
         transformer.audit(this);
      }

   }

   public List getTransformers() {
      if (this.transformers == null) {
         this.buildTransformerDelegationList();
      }

      return Collections.unmodifiableList(this.transformers);
   }

   public void addTransformerExclusion(String name) {
      excludeTransformers.add(name);
      this.transformers = null;
   }

   private void buildTransformerDelegationList() {
      logger.debug("Rebuilding transformer delegation list:");
      this.transformers = new ArrayList();
      Iterator var1 = this.service.getTransformers().iterator();

      while(true) {
         while(true) {
            ITransformer transformer;
            do {
               if (!var1.hasNext()) {
                  logger.debug("Transformer delegation list created with {} entries", new Object[]{this.transformers.size()});
                  return;
               }

               transformer = (ITransformer)var1.next();
            } while(!(transformer instanceof ILegacyClassTransformer));

            ILegacyClassTransformer legacyTransformer = (ILegacyClassTransformer)transformer;
            String transformerName = legacyTransformer.getName();
            boolean include = true;
            Iterator var6 = excludeTransformers.iterator();

            while(var6.hasNext()) {
               String excludeClass = (String)var6.next();
               if (transformerName.contains(excludeClass)) {
                  include = false;
                  break;
               }
            }

            if (include && !legacyTransformer.isDelegationExcluded()) {
               logger.debug("  Adding:    {}", new Object[]{transformerName});
               this.transformers.add(legacyTransformer);
            } else {
               logger.debug("  Excluding: {}", new Object[]{transformerName});
            }
         }
      }
   }

   public String toString() {
      return String.format("%s[%s]", this.getClass().getSimpleName(), this.phase);
   }

   private static Phase getCurrentPhase() {
      if (currentPhase == MixinEnvironment.Phase.NOT_INITIALISED) {
         init(MixinEnvironment.Phase.PREINIT);
      }

      return currentPhase;
   }

   public static void init(Phase phase) {
      if (currentPhase == MixinEnvironment.Phase.NOT_INITIALISED) {
         currentPhase = phase;
         MixinEnvironment env = getEnvironment(phase);
         getProfiler().setActive(env.getOption(MixinEnvironment.Option.DEBUG_PROFILER));
         MixinEnvironment.MixinLogWatcher.begin();
      }

   }

   public static MixinEnvironment getEnvironment(Phase phase) {
      return phase == null ? MixinEnvironment.Phase.DEFAULT.getEnvironment() : phase.getEnvironment();
   }

   public static MixinEnvironment getDefaultEnvironment() {
      return getEnvironment(MixinEnvironment.Phase.DEFAULT);
   }

   public static MixinEnvironment getCurrentEnvironment() {
      if (currentEnvironment == null) {
         currentEnvironment = getEnvironment(getCurrentPhase());
      }

      return currentEnvironment;
   }

   public static CompatibilityLevel getCompatibilityLevel() {
      return compatibility;
   }

   /** @deprecated */
   @Deprecated
   public static void setCompatibilityLevel(CompatibilityLevel level) throws IllegalArgumentException {
      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      if (!"org.spongepowered.asm.mixin.transformer.MixinConfig".equals(stackTrace[2].getClassName())) {
         logger.warn("MixinEnvironment::setCompatibilityLevel is deprecated and will be removed. Set level via config instead!");
      }

      if (level != compatibility && level.isAtLeast(compatibility)) {
         if (!level.isSupported()) {
            throw new IllegalArgumentException("The requested compatibility level " + level + " could not be set. Level is not supported");
         }

         compatibility = level;
         logger.info("Compatibility level set to {}", new Object[]{level});
      }

   }

   public static Profiler getProfiler() {
      return profiler;
   }

   static void gotoPhase(Phase phase) {
      if (phase != null && phase.ordinal >= 0) {
         if (phase.ordinal > getCurrentPhase().ordinal) {
            MixinService.getService().beginPhase();
         }

         if (phase == MixinEnvironment.Phase.DEFAULT) {
            MixinEnvironment.MixinLogWatcher.end();
         }

         currentPhase = phase;
         currentEnvironment = getEnvironment(getCurrentPhase());
      } else {
         throw new IllegalArgumentException("Cannot go to the specified phase, phase is null or invalid");
      }
   }

   static {
      currentPhase = MixinEnvironment.Phase.NOT_INITIALISED;
      compatibility = (CompatibilityLevel)MixinEnvironment.Option.DEFAULT_COMPATIBILITY_LEVEL.getEnumValue(MixinEnvironment.CompatibilityLevel.JAVA_6);
      showHeader = true;
      logger = LogManager.getLogger("mixin");
      profiler = new Profiler();
   }

   static class MixinLogWatcher {
      static MixinAppender appender = new MixinAppender();
      static org.apache.logging.log4j.core.Logger log;
      static Level oldLevel = null;

      static void begin() {
         Logger fmlLog = LogManager.getLogger("FML");
         if (fmlLog instanceof org.apache.logging.log4j.core.Logger) {
            log = (org.apache.logging.log4j.core.Logger)fmlLog;
            oldLevel = log.getLevel();
            appender.start();
            log.addAppender(appender);
            log.setLevel(Level.ALL);
         }
      }

      static void end() {
         if (log != null) {
            log.removeAppender(appender);
         }

      }

      static class MixinAppender extends AbstractAppender {
         MixinAppender() {
            super("MixinLogWatcherAppender", (Filter)null, (Layout)null);
         }

         public void append(LogEvent event) {
            if (event.getLevel() == Level.DEBUG && "Validating minecraft".equals(event.getMessage().getFormattedMessage())) {
               MixinEnvironment.gotoPhase(MixinEnvironment.Phase.INIT);
               if (MixinEnvironment.MixinLogWatcher.log.getLevel() == Level.ALL) {
                  MixinEnvironment.MixinLogWatcher.log.setLevel(MixinEnvironment.MixinLogWatcher.oldLevel);
               }

            }
         }
      }
   }

   static class TokenProviderWrapper implements Comparable {
      private static int nextOrder = 0;
      private final int priority;
      private final int order;
      private final IEnvironmentTokenProvider provider;
      private final MixinEnvironment environment;

      public TokenProviderWrapper(IEnvironmentTokenProvider provider, MixinEnvironment environment) {
         this.provider = provider;
         this.environment = environment;
         this.order = nextOrder++;
         this.priority = provider.getPriority();
      }

      public int compareTo(TokenProviderWrapper other) {
         if (other == null) {
            return 0;
         } else {
            return other.priority == this.priority ? other.order - this.order : other.priority - this.priority;
         }
      }

      public IEnvironmentTokenProvider getProvider() {
         return this.provider;
      }

      Integer getToken(String token) {
         return this.provider.getToken(token, this.environment);
      }
   }

   public static enum CompatibilityLevel {
      JAVA_6(6, 50, false),
      JAVA_7(7, 51, false) {
         boolean isSupported() {
            return JavaVersion.current() >= 1.7;
         }
      },
      JAVA_8(8, 52, true) {
         boolean isSupported() {
            return JavaVersion.current() >= 1.8;
         }
      },
      JAVA_9(9, 53, true) {
         boolean isSupported() {
            return false;
         }
      };

      private static final int CLASS_V1_9 = 53;
      private final int ver;
      private final int classVersion;
      private final boolean supportsMethodsInInterfaces;
      private CompatibilityLevel maxCompatibleLevel;

      private CompatibilityLevel(int ver, int classVersion, boolean resolveMethodsInInterfaces) {
         this.ver = ver;
         this.classVersion = classVersion;
         this.supportsMethodsInInterfaces = resolveMethodsInInterfaces;
      }

      private void setMaxCompatibleLevel(CompatibilityLevel maxCompatibleLevel) {
         this.maxCompatibleLevel = maxCompatibleLevel;
      }

      boolean isSupported() {
         return true;
      }

      public int classVersion() {
         return this.classVersion;
      }

      public boolean supportsMethodsInInterfaces() {
         return this.supportsMethodsInInterfaces;
      }

      public boolean isAtLeast(CompatibilityLevel level) {
         return level == null || this.ver >= level.ver;
      }

      public boolean canElevateTo(CompatibilityLevel level) {
         if (level != null && this.maxCompatibleLevel != null) {
            return level.ver <= this.maxCompatibleLevel.ver;
         } else {
            return true;
         }
      }

      public boolean canSupport(CompatibilityLevel level) {
         return level == null ? true : level.canElevateTo(this);
      }

      // $FF: synthetic method
      CompatibilityLevel(int x2, int x3, boolean x4, Object x5) {
         this(x2, x3, x4);
      }
   }

   public static enum Option {
      DEBUG_ALL("debug"),
      DEBUG_EXPORT(DEBUG_ALL, "export"),
      DEBUG_EXPORT_FILTER(DEBUG_EXPORT, "filter", false),
      DEBUG_EXPORT_DECOMPILE(DEBUG_EXPORT, MixinEnvironment.Option.Inherit.ALLOW_OVERRIDE, "decompile"),
      DEBUG_EXPORT_DECOMPILE_THREADED(DEBUG_EXPORT_DECOMPILE, MixinEnvironment.Option.Inherit.ALLOW_OVERRIDE, "async"),
      DEBUG_EXPORT_DECOMPILE_MERGESIGNATURES(DEBUG_EXPORT_DECOMPILE, MixinEnvironment.Option.Inherit.ALLOW_OVERRIDE, "mergeGenericSignatures"),
      DEBUG_VERIFY(DEBUG_ALL, "verify"),
      DEBUG_VERBOSE(DEBUG_ALL, "verbose"),
      DEBUG_INJECTORS(DEBUG_ALL, "countInjections"),
      DEBUG_STRICT(DEBUG_ALL, MixinEnvironment.Option.Inherit.INDEPENDENT, "strict"),
      DEBUG_UNIQUE(DEBUG_STRICT, "unique"),
      DEBUG_TARGETS(DEBUG_STRICT, "targets"),
      DEBUG_PROFILER(DEBUG_ALL, MixinEnvironment.Option.Inherit.ALLOW_OVERRIDE, "profiler"),
      DUMP_TARGET_ON_FAILURE("dumpTargetOnFailure"),
      CHECK_ALL("checks"),
      CHECK_IMPLEMENTS(CHECK_ALL, "interfaces"),
      CHECK_IMPLEMENTS_STRICT(CHECK_IMPLEMENTS, MixinEnvironment.Option.Inherit.ALLOW_OVERRIDE, "strict"),
      IGNORE_CONSTRAINTS("ignoreConstraints"),
      HOT_SWAP("hotSwap"),
      ENVIRONMENT(MixinEnvironment.Option.Inherit.ALWAYS_FALSE, "env"),
      OBFUSCATION_TYPE(ENVIRONMENT, MixinEnvironment.Option.Inherit.ALWAYS_FALSE, "obf"),
      DISABLE_REFMAP(ENVIRONMENT, MixinEnvironment.Option.Inherit.INDEPENDENT, "disableRefMap"),
      REFMAP_REMAP(ENVIRONMENT, MixinEnvironment.Option.Inherit.INDEPENDENT, "remapRefMap"),
      REFMAP_REMAP_RESOURCE(ENVIRONMENT, MixinEnvironment.Option.Inherit.INDEPENDENT, "refMapRemappingFile", ""),
      REFMAP_REMAP_SOURCE_ENV(ENVIRONMENT, MixinEnvironment.Option.Inherit.INDEPENDENT, "refMapRemappingEnv", "name"),
      REFMAP_REMAP_ALLOW_PERMISSIVE(ENVIRONMENT, MixinEnvironment.Option.Inherit.INDEPENDENT, "allowPermissiveMatch", true, "true"),
      IGNORE_REQUIRED(ENVIRONMENT, MixinEnvironment.Option.Inherit.INDEPENDENT, "ignoreRequired"),
      DEFAULT_COMPATIBILITY_LEVEL(ENVIRONMENT, MixinEnvironment.Option.Inherit.INDEPENDENT, "compatLevel"),
      SHIFT_BY_VIOLATION_BEHAVIOUR(ENVIRONMENT, MixinEnvironment.Option.Inherit.INDEPENDENT, "shiftByViolation", "warn"),
      INITIALISER_INJECTION_MODE("initialiserInjectionMode", "default");

      private static final String PREFIX = "mixin";
      final Option parent;
      final Inherit inheritance;
      final String property;
      final String defaultValue;
      final boolean isFlag;
      final int depth;

      private Option(String property) {
         this((Option)null, property, true);
      }

      private Option(Inherit inheritance, String property) {
         this((Option)null, inheritance, property, true);
      }

      private Option(String property, boolean flag) {
         this((Option)null, property, flag);
      }

      private Option(String property, String defaultStringValue) {
         this((Option)null, MixinEnvironment.Option.Inherit.INDEPENDENT, property, false, defaultStringValue);
      }

      private Option(Option parent, String property) {
         this(parent, MixinEnvironment.Option.Inherit.INHERIT, property, true);
      }

      private Option(Option parent, Inherit inheritance, String property) {
         this(parent, inheritance, property, true);
      }

      private Option(Option parent, String property, boolean isFlag) {
         this(parent, MixinEnvironment.Option.Inherit.INHERIT, property, isFlag, (String)null);
      }

      private Option(Option parent, Inherit inheritance, String property, boolean isFlag) {
         this(parent, inheritance, property, isFlag, (String)null);
      }

      private Option(Option parent, String property, String defaultStringValue) {
         this(parent, MixinEnvironment.Option.Inherit.INHERIT, property, false, defaultStringValue);
      }

      private Option(Option parent, Inherit inheritance, String property, String defaultStringValue) {
         this(parent, inheritance, property, false, defaultStringValue);
      }

      private Option(Option parent, Inherit inheritance, String property, boolean isFlag, String defaultStringValue) {
         this.parent = parent;
         this.inheritance = inheritance;
         this.property = (parent != null ? parent.property : "mixin") + "." + property;
         this.defaultValue = defaultStringValue;
         this.isFlag = isFlag;

         int depth;
         for(depth = 0; parent != null; ++depth) {
            parent = parent.parent;
         }

         this.depth = depth;
      }

      Option getParent() {
         return this.parent;
      }

      String getProperty() {
         return this.property;
      }

      public String toString() {
         return this.isFlag ? String.valueOf(this.getBooleanValue()) : this.getStringValue();
      }

      private boolean getLocalBooleanValue(boolean defaultValue) {
         return Boolean.parseBoolean(System.getProperty(this.property, Boolean.toString(defaultValue)));
      }

      private boolean getInheritedBooleanValue() {
         return this.parent != null && this.parent.getBooleanValue();
      }

      final boolean getBooleanValue() {
         if (this.inheritance == MixinEnvironment.Option.Inherit.ALWAYS_FALSE) {
            return false;
         } else {
            boolean local = this.getLocalBooleanValue(false);
            if (this.inheritance == MixinEnvironment.Option.Inherit.INDEPENDENT) {
               return local;
            } else {
               boolean inherited = local || this.getInheritedBooleanValue();
               return this.inheritance == MixinEnvironment.Option.Inherit.INHERIT ? inherited : this.getLocalBooleanValue(inherited);
            }
         }
      }

      final String getStringValue() {
         return this.inheritance != MixinEnvironment.Option.Inherit.INDEPENDENT && this.parent != null && !this.parent.getBooleanValue() ? this.defaultValue : System.getProperty(this.property, this.defaultValue);
      }

      Enum getEnumValue(Enum defaultValue) {
         String value = System.getProperty(this.property, defaultValue.name());

         try {
            return Enum.valueOf(defaultValue.getClass(), value.toUpperCase());
         } catch (IllegalArgumentException var4) {
            return defaultValue;
         }
      }

      private static enum Inherit {
         INHERIT,
         ALLOW_OVERRIDE,
         INDEPENDENT,
         ALWAYS_FALSE;
      }
   }

   public static enum Side {
      UNKNOWN {
         protected boolean detect() {
            return false;
         }
      },
      CLIENT {
         protected boolean detect() {
            String sideName = MixinService.getService().getSideName();
            return "CLIENT".equals(sideName);
         }
      },
      SERVER {
         protected boolean detect() {
            String sideName = MixinService.getService().getSideName();
            return "SERVER".equals(sideName) || "DEDICATEDSERVER".equals(sideName);
         }
      };

      private Side() {
      }

      protected abstract boolean detect();

      // $FF: synthetic method
      Side(Object x2) {
         this();
      }
   }

   public static final class Phase {
      static final Phase NOT_INITIALISED = new Phase(-1, "NOT_INITIALISED");
      public static final Phase PREINIT = new Phase(0, "PREINIT");
      public static final Phase INIT = new Phase(1, "INIT");
      public static final Phase DEFAULT = new Phase(2, "DEFAULT");
      static final List phases;
      final int ordinal;
      final String name;
      private MixinEnvironment environment;

      private Phase(int ordinal, String name) {
         this.ordinal = ordinal;
         this.name = name;
      }

      public String toString() {
         return this.name;
      }

      public static Phase forName(String name) {
         Iterator var1 = phases.iterator();

         Phase phase;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            phase = (Phase)var1.next();
         } while(!phase.name.equals(name));

         return phase;
      }

      MixinEnvironment getEnvironment() {
         if (this.ordinal < 0) {
            throw new IllegalArgumentException("Cannot access the NOT_INITIALISED environment");
         } else {
            if (this.environment == null) {
               this.environment = new MixinEnvironment(this);
            }

            return this.environment;
         }
      }

      static {
         phases = ImmutableList.of(PREINIT, INIT, DEFAULT);
      }
   }
}
