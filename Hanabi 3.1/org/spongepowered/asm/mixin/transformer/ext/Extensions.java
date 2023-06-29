package org.spongepowered.asm.mixin.transformer.ext;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.MixinTransformer;

public final class Extensions {
   private final MixinTransformer transformer;
   private final List extensions = new ArrayList();
   private final Map extensionMap = new HashMap();
   private final List generators = new ArrayList();
   private final List generatorsView;
   private final Map generatorMap;
   private List activeExtensions;

   public Extensions(MixinTransformer transformer) {
      this.generatorsView = Collections.unmodifiableList(this.generators);
      this.generatorMap = new HashMap();
      this.activeExtensions = Collections.emptyList();
      this.transformer = transformer;
   }

   public MixinTransformer getTransformer() {
      return this.transformer;
   }

   public void add(IExtension extension) {
      this.extensions.add(extension);
      this.extensionMap.put(extension.getClass(), extension);
   }

   public List getExtensions() {
      return Collections.unmodifiableList(this.extensions);
   }

   public List getActiveExtensions() {
      return this.activeExtensions;
   }

   public IExtension getExtension(Class extensionClass) {
      return (IExtension)lookup(extensionClass, this.extensionMap, this.extensions);
   }

   public void select(MixinEnvironment environment) {
      ImmutableList.Builder activeExtensions = ImmutableList.builder();
      Iterator var3 = this.extensions.iterator();

      while(var3.hasNext()) {
         IExtension extension = (IExtension)var3.next();
         if (extension.checkActive(environment)) {
            activeExtensions.add(extension);
         }
      }

      this.activeExtensions = activeExtensions.build();
   }

   public void preApply(ITargetClassContext context) {
      Iterator var2 = this.activeExtensions.iterator();

      while(var2.hasNext()) {
         IExtension extension = (IExtension)var2.next();
         extension.preApply(context);
      }

   }

   public void postApply(ITargetClassContext context) {
      Iterator var2 = this.activeExtensions.iterator();

      while(var2.hasNext()) {
         IExtension extension = (IExtension)var2.next();
         extension.postApply(context);
      }

   }

   public void export(MixinEnvironment env, String name, boolean force, byte[] bytes) {
      Iterator var5 = this.activeExtensions.iterator();

      while(var5.hasNext()) {
         IExtension extension = (IExtension)var5.next();
         extension.export(env, name, force, bytes);
      }

   }

   public void add(IClassGenerator generator) {
      this.generators.add(generator);
      this.generatorMap.put(generator.getClass(), generator);
   }

   public List getGenerators() {
      return this.generatorsView;
   }

   public IClassGenerator getGenerator(Class generatorClass) {
      return (IClassGenerator)lookup(generatorClass, this.generatorMap, this.generators);
   }

   private static Object lookup(Class extensionClass, Map map, List list) {
      Object extension = map.get(extensionClass);
      if (extension == null) {
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            Object classGenerator = var4.next();
            if (extensionClass.isAssignableFrom(classGenerator.getClass())) {
               extension = classGenerator;
               break;
            }
         }

         if (extension == null) {
            throw new IllegalArgumentException("Extension for <" + extensionClass.getName() + "> could not be found");
         }

         map.put(extensionClass, extension);
      }

      return extension;
   }
}
