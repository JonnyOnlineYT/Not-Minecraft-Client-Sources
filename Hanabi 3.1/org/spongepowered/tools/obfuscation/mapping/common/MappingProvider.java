package org.spongepowered.tools.obfuscation.mapping.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.mapping.IMappingProvider;

public abstract class MappingProvider implements IMappingProvider {
   protected final Messager messager;
   protected final Filer filer;
   protected final BiMap packageMap = HashBiMap.create();
   protected final BiMap classMap = HashBiMap.create();
   protected final BiMap fieldMap = HashBiMap.create();
   protected final BiMap methodMap = HashBiMap.create();

   public MappingProvider(Messager messager, Filer filer) {
      this.messager = messager;
      this.filer = filer;
   }

   public void clear() {
      this.packageMap.clear();
      this.classMap.clear();
      this.fieldMap.clear();
      this.methodMap.clear();
   }

   public boolean isEmpty() {
      return this.packageMap.isEmpty() && this.classMap.isEmpty() && this.fieldMap.isEmpty() && this.methodMap.isEmpty();
   }

   public MappingMethod getMethodMapping(MappingMethod method) {
      return (MappingMethod)this.methodMap.get(method);
   }

   public MappingField getFieldMapping(MappingField field) {
      return (MappingField)this.fieldMap.get(field);
   }

   public String getClassMapping(String className) {
      return (String)this.classMap.get(className);
   }

   public String getPackageMapping(String packageName) {
      return (String)this.packageMap.get(packageName);
   }
}
