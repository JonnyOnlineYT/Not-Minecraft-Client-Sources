package org.spongepowered.tools.obfuscation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic.Kind;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IReferenceManager;

public class ReferenceManager implements IReferenceManager {
   private final IMixinAnnotationProcessor ap;
   private final String outRefMapFileName;
   private final List environments;
   private final ReferenceMapper refMapper = new ReferenceMapper();
   private boolean allowConflicts;

   public ReferenceManager(IMixinAnnotationProcessor ap, List environments) {
      this.ap = ap;
      this.environments = environments;
      this.outRefMapFileName = this.ap.getOption("outRefMapFile");
   }

   public boolean getAllowConflicts() {
      return this.allowConflicts;
   }

   public void setAllowConflicts(boolean allowConflicts) {
      this.allowConflicts = allowConflicts;
   }

   public void write() {
      if (this.outRefMapFileName != null) {
         PrintWriter writer = null;

         try {
            writer = this.newWriter(this.outRefMapFileName, "refmap");
            this.refMapper.write(writer);
         } catch (IOException var11) {
            var11.printStackTrace();
         } finally {
            if (writer != null) {
               try {
                  writer.close();
               } catch (Exception var10) {
               }
            }

         }

      }
   }

   private PrintWriter newWriter(String fileName, String description) throws IOException {
      if (fileName.matches("^.*[\\\\/:].*$")) {
         File outFile = new File(fileName);
         outFile.getParentFile().mkdirs();
         this.ap.printMessage(Kind.NOTE, "Writing " + description + " to " + outFile.getAbsolutePath());
         return new PrintWriter(outFile);
      } else {
         FileObject outResource = this.ap.getProcessingEnvironment().getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", fileName, new Element[0]);
         this.ap.printMessage(Kind.NOTE, "Writing " + description + " to " + (new File(outResource.toUri())).getAbsolutePath());
         return new PrintWriter(outResource.openWriter());
      }
   }

   public ReferenceMapper getMapper() {
      return this.refMapper;
   }

   public void addMethodMapping(String className, String reference, ObfuscationData obfMethodData) {
      Iterator var4 = this.environments.iterator();

      while(var4.hasNext()) {
         ObfuscationEnvironment env = (ObfuscationEnvironment)var4.next();
         MappingMethod obfMethod = (MappingMethod)obfMethodData.get(env.getType());
         if (obfMethod != null) {
            MemberInfo remappedReference = new MemberInfo(obfMethod);
            this.addMapping(env.getType(), className, reference, remappedReference.toString());
         }
      }

   }

   public void addMethodMapping(String className, String reference, MemberInfo context, ObfuscationData obfMethodData) {
      Iterator var5 = this.environments.iterator();

      while(var5.hasNext()) {
         ObfuscationEnvironment env = (ObfuscationEnvironment)var5.next();
         MappingMethod obfMethod = (MappingMethod)obfMethodData.get(env.getType());
         if (obfMethod != null) {
            MemberInfo remappedReference = context.remapUsing(obfMethod, true);
            this.addMapping(env.getType(), className, reference, remappedReference.toString());
         }
      }

   }

   public void addFieldMapping(String className, String reference, MemberInfo context, ObfuscationData obfFieldData) {
      Iterator var5 = this.environments.iterator();

      while(var5.hasNext()) {
         ObfuscationEnvironment env = (ObfuscationEnvironment)var5.next();
         MappingField obfField = (MappingField)obfFieldData.get(env.getType());
         if (obfField != null) {
            MemberInfo remappedReference = MemberInfo.fromMapping(obfField.transform(env.remapDescriptor(context.desc)));
            this.addMapping(env.getType(), className, reference, remappedReference.toString());
         }
      }

   }

   public void addClassMapping(String className, String reference, ObfuscationData obfClassData) {
      Iterator var4 = this.environments.iterator();

      while(var4.hasNext()) {
         ObfuscationEnvironment env = (ObfuscationEnvironment)var4.next();
         String remapped = (String)obfClassData.get(env.getType());
         if (remapped != null) {
            this.addMapping(env.getType(), className, reference, remapped);
         }
      }

   }

   protected void addMapping(ObfuscationType type, String className, String reference, String newReference) {
      String oldReference = this.refMapper.addMapping(type.getKey(), className, reference, newReference);
      if (type.isDefault()) {
         this.refMapper.addMapping((String)null, className, reference, newReference);
      }

      if (!this.allowConflicts && oldReference != null && !oldReference.equals(newReference)) {
         throw new ReferenceConflictException(oldReference, newReference);
      }
   }

   public static class ReferenceConflictException extends RuntimeException {
      private static final long serialVersionUID = 1L;
      private final String oldReference;
      private final String newReference;

      public ReferenceConflictException(String oldReference, String newReference) {
         this.oldReference = oldReference;
         this.newReference = newReference;
      }

      public String getOld() {
         return this.oldReference;
      }

      public String getNew() {
         return this.newReference;
      }
   }
}
