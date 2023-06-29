package org.spongepowered.tools.obfuscation;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.util.ObfuscationUtil;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationEnvironment;
import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import org.spongepowered.tools.obfuscation.mapping.IMappingProvider;
import org.spongepowered.tools.obfuscation.mapping.IMappingWriter;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;

public abstract class ObfuscationEnvironment implements IObfuscationEnvironment {
   protected final ObfuscationType type;
   protected final IMappingProvider mappingProvider;
   protected final IMappingWriter mappingWriter;
   protected final RemapperProxy remapper = new RemapperProxy();
   protected final IMixinAnnotationProcessor ap;
   protected final String outFileName;
   protected final List inFileNames;
   private boolean initDone;

   protected ObfuscationEnvironment(ObfuscationType type) {
      this.type = type;
      this.ap = type.getAnnotationProcessor();
      this.inFileNames = type.getInputFileNames();
      this.outFileName = type.getOutputFileName();
      this.mappingProvider = this.getMappingProvider(this.ap, this.ap.getProcessingEnvironment().getFiler());
      this.mappingWriter = this.getMappingWriter(this.ap, this.ap.getProcessingEnvironment().getFiler());
   }

   public String toString() {
      return this.type.toString();
   }

   protected abstract IMappingProvider getMappingProvider(Messager var1, Filer var2);

   protected abstract IMappingWriter getMappingWriter(Messager var1, Filer var2);

   private boolean initMappings() {
      if (!this.initDone) {
         this.initDone = true;
         if (this.inFileNames == null) {
            this.ap.printMessage(Kind.ERROR, "The " + this.type.getConfig().getInputFileOption() + " argument was not supplied, obfuscation processing will not occur");
            return false;
         }

         int successCount = 0;
         Iterator var2 = this.inFileNames.iterator();

         while(var2.hasNext()) {
            String inputFileName = (String)var2.next();
            File inputFile = new File(inputFileName);

            try {
               if (inputFile.isFile()) {
                  this.ap.printMessage(Kind.NOTE, "Loading " + this.type + " mappings from " + inputFile.getAbsolutePath());
                  this.mappingProvider.read(inputFile);
                  ++successCount;
               }
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }

         if (successCount < 1) {
            this.ap.printMessage(Kind.ERROR, "No valid input files for " + this.type + " could be read, processing may not be sucessful.");
            this.mappingProvider.clear();
         }
      }

      return !this.mappingProvider.isEmpty();
   }

   public ObfuscationType getType() {
      return this.type;
   }

   public MappingMethod getObfMethod(MemberInfo method) {
      MappingMethod obfd = this.getObfMethod(method.asMethodMapping());
      if (obfd == null && method.isFullyQualified()) {
         TypeHandle type = this.ap.getTypeProvider().getTypeHandle(method.owner);
         if (type != null && !type.isImaginary()) {
            TypeMirror superClass = type.getElement().getSuperclass();
            if (superClass.getKind() != TypeKind.DECLARED) {
               return null;
            } else {
               String superClassName = ((TypeElement)((DeclaredType)superClass).asElement()).getQualifiedName().toString();
               return this.getObfMethod(new MemberInfo(method.name, superClassName.replace('.', '/'), method.desc, method.matchAll));
            }
         } else {
            return null;
         }
      } else {
         return obfd;
      }
   }

   public MappingMethod getObfMethod(MappingMethod method) {
      return this.getObfMethod(method, true);
   }

   public MappingMethod getObfMethod(MappingMethod method, boolean lazyRemap) {
      if (!this.initMappings()) {
         return null;
      } else {
         boolean remapped = true;
         MappingMethod mapping = null;

         for(MappingMethod md = method; md != null && mapping == null; md = md.getSuper()) {
            mapping = this.mappingProvider.getMethodMapping(md);
         }

         if (mapping == null) {
            if (lazyRemap) {
               return null;
            }

            mapping = method.copy();
            remapped = false;
         }

         String remappedOwner = this.getObfClass(mapping.getOwner());
         if (remappedOwner != null && !remappedOwner.equals(method.getOwner()) && !remappedOwner.equals(mapping.getOwner())) {
            if (remapped) {
               return mapping.move(remappedOwner);
            } else {
               String desc = ObfuscationUtil.mapDescriptor(mapping.getDesc(), this.remapper);
               return new MappingMethod(remappedOwner, mapping.getSimpleName(), desc);
            }
         } else {
            return remapped ? mapping : null;
         }
      }
   }

   public MemberInfo remapDescriptor(MemberInfo method) {
      boolean transformed = false;
      String owner = method.owner;
      String desc;
      if (owner != null) {
         desc = this.remapper.map(owner);
         if (desc != null) {
            owner = desc;
            transformed = true;
         }
      }

      desc = method.desc;
      if (desc != null) {
         String newDesc = ObfuscationUtil.mapDescriptor(method.desc, this.remapper);
         if (!newDesc.equals(method.desc)) {
            desc = newDesc;
            transformed = true;
         }
      }

      return transformed ? new MemberInfo(method.name, owner, desc, method.matchAll) : null;
   }

   public String remapDescriptor(String desc) {
      return ObfuscationUtil.mapDescriptor(desc, this.remapper);
   }

   public MappingField getObfField(MemberInfo field) {
      return this.getObfField(field.asFieldMapping(), true);
   }

   public MappingField getObfField(MappingField field) {
      return this.getObfField(field, true);
   }

   public MappingField getObfField(MappingField field, boolean lazyRemap) {
      if (!this.initMappings()) {
         return null;
      } else {
         MappingField mapping = this.mappingProvider.getFieldMapping(field);
         if (mapping == null) {
            if (lazyRemap) {
               return null;
            }

            mapping = field;
         }

         String remappedOwner = this.getObfClass(mapping.getOwner());
         if (remappedOwner != null && !remappedOwner.equals(field.getOwner()) && !remappedOwner.equals(mapping.getOwner())) {
            return mapping.move(remappedOwner);
         } else {
            return mapping != field ? mapping : null;
         }
      }
   }

   public String getObfClass(String className) {
      return !this.initMappings() ? null : this.mappingProvider.getClassMapping(className);
   }

   public void writeMappings(Collection consumers) {
      IMappingConsumer.MappingSet fields = new IMappingConsumer.MappingSet();
      IMappingConsumer.MappingSet methods = new IMappingConsumer.MappingSet();
      Iterator var4 = consumers.iterator();

      while(var4.hasNext()) {
         IMappingConsumer mappings = (IMappingConsumer)var4.next();
         fields.addAll(mappings.getFieldMappings(this.type));
         methods.addAll(mappings.getMethodMappings(this.type));
      }

      this.mappingWriter.write(this.outFileName, this.type, fields, methods);
   }

   final class RemapperProxy implements ObfuscationUtil.IClassRemapper {
      public String map(String typeName) {
         return ObfuscationEnvironment.this.mappingProvider == null ? null : ObfuscationEnvironment.this.mappingProvider.getClassMapping(typeName);
      }

      public String unmap(String typeName) {
         return ObfuscationEnvironment.this.mappingProvider == null ? null : ObfuscationEnvironment.this.mappingProvider.getClassMapping(typeName);
      }
   }
}
