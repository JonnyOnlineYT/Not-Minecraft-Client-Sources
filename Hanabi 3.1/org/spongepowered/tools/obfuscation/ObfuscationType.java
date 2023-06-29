package org.spongepowered.tools.obfuscation;

import com.google.common.collect.ImmutableList;
import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IOptionProvider;
import org.spongepowered.tools.obfuscation.service.ObfuscationTypeDescriptor;

public final class ObfuscationType {
   private static final Map types = new LinkedHashMap();
   private final String key;
   private final ObfuscationTypeDescriptor descriptor;
   private final IMixinAnnotationProcessor ap;
   private final IOptionProvider options;

   private ObfuscationType(ObfuscationTypeDescriptor descriptor, IMixinAnnotationProcessor ap) {
      this.key = descriptor.getKey();
      this.descriptor = descriptor;
      this.ap = ap;
      this.options = ap;
   }

   public final ObfuscationEnvironment createEnvironment() {
      try {
         Class cls = this.descriptor.getEnvironmentType();
         Constructor ctor = cls.getDeclaredConstructor(ObfuscationType.class);
         ctor.setAccessible(true);
         return (ObfuscationEnvironment)ctor.newInstance(this);
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }

   public String toString() {
      return this.key;
   }

   public String getKey() {
      return this.key;
   }

   public ObfuscationTypeDescriptor getConfig() {
      return this.descriptor;
   }

   public IMixinAnnotationProcessor getAnnotationProcessor() {
      return this.ap;
   }

   public boolean isDefault() {
      String defaultEnv = this.options.getOption("defaultObfuscationEnv");
      return defaultEnv == null && this.key.equals("name") || defaultEnv != null && this.key.equals(defaultEnv.toLowerCase());
   }

   public boolean isSupported() {
      return this.getInputFileNames().size() > 0;
   }

   public List getInputFileNames() {
      ImmutableList.Builder builder = ImmutableList.builder();
      String inputFile = this.options.getOption(this.descriptor.getInputFileOption());
      if (inputFile != null) {
         builder.add(inputFile);
      }

      String extraInputFiles = this.options.getOption(this.descriptor.getExtraInputFilesOption());
      if (extraInputFiles != null) {
         String[] var4 = extraInputFiles.split(";");
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String extraInputFile = var4[var6];
            builder.add(extraInputFile.trim());
         }
      }

      return builder.build();
   }

   public String getOutputFileName() {
      return this.options.getOption(this.descriptor.getOutputFileOption());
   }

   public static Iterable types() {
      return types.values();
   }

   public static ObfuscationType create(ObfuscationTypeDescriptor descriptor, IMixinAnnotationProcessor ap) {
      String key = descriptor.getKey();
      if (types.containsKey(key)) {
         throw new IllegalArgumentException("Obfuscation type with key " + key + " was already registered");
      } else {
         ObfuscationType type = new ObfuscationType(descriptor, ap);
         types.put(key, type);
         return type;
      }
   }

   public static ObfuscationType get(String key) {
      ObfuscationType type = (ObfuscationType)types.get(key);
      if (type == null) {
         throw new IllegalArgumentException("Obfuscation type with key " + key + " was not registered");
      } else {
         return type;
      }
   }
}
