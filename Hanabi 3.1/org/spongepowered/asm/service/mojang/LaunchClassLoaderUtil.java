package org.spongepowered.asm.service.mojang;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.launchwrapper.LaunchClassLoader;

final class LaunchClassLoaderUtil {
   private static final String CACHED_CLASSES_FIELD = "cachedClasses";
   private static final String INVALID_CLASSES_FIELD = "invalidClasses";
   private static final String CLASS_LOADER_EXCEPTIONS_FIELD = "classLoaderExceptions";
   private static final String TRANSFORMER_EXCEPTIONS_FIELD = "transformerExceptions";
   private final LaunchClassLoader classLoader;
   private final Map cachedClasses;
   private final Set invalidClasses;
   private final Set classLoaderExceptions;
   private final Set transformerExceptions;

   LaunchClassLoaderUtil(LaunchClassLoader classLoader) {
      this.classLoader = classLoader;
      this.cachedClasses = (Map)getField(classLoader, "cachedClasses");
      this.invalidClasses = (Set)getField(classLoader, "invalidClasses");
      this.classLoaderExceptions = (Set)getField(classLoader, "classLoaderExceptions");
      this.transformerExceptions = (Set)getField(classLoader, "transformerExceptions");
   }

   LaunchClassLoader getClassLoader() {
      return this.classLoader;
   }

   boolean isClassLoaded(String name) {
      return this.cachedClasses.containsKey(name);
   }

   boolean isClassExcluded(String name, String transformedName) {
      return this.isClassClassLoaderExcluded(name, transformedName) || this.isClassTransformerExcluded(name, transformedName);
   }

   boolean isClassClassLoaderExcluded(String name, String transformedName) {
      Iterator var3 = this.getClassLoaderExceptions().iterator();

      String exception;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         exception = (String)var3.next();
      } while((transformedName == null || !transformedName.startsWith(exception)) && !name.startsWith(exception));

      return true;
   }

   boolean isClassTransformerExcluded(String name, String transformedName) {
      Iterator var3 = this.getTransformerExceptions().iterator();

      String exception;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         exception = (String)var3.next();
      } while((transformedName == null || !transformedName.startsWith(exception)) && !name.startsWith(exception));

      return true;
   }

   void registerInvalidClass(String name) {
      if (this.invalidClasses != null) {
         this.invalidClasses.add(name);
      }

   }

   Set getClassLoaderExceptions() {
      return this.classLoaderExceptions != null ? this.classLoaderExceptions : Collections.emptySet();
   }

   Set getTransformerExceptions() {
      return this.transformerExceptions != null ? this.transformerExceptions : Collections.emptySet();
   }

   private static Object getField(LaunchClassLoader classLoader, String fieldName) {
      try {
         Field field = LaunchClassLoader.class.getDeclaredField(fieldName);
         field.setAccessible(true);
         return field.get(classLoader);
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }
}
