package org.spongepowered.tools.obfuscation;

import java.util.Iterator;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;

@SupportedAnnotationTypes({"org.spongepowered.asm.mixin.Mixin", "org.spongepowered.asm.mixin.Shadow", "org.spongepowered.asm.mixin.Overwrite", "org.spongepowered.asm.mixin.gen.Accessor", "org.spongepowered.asm.mixin.Implements"})
public class MixinObfuscationProcessorTargets extends MixinObfuscationProcessor {
   public boolean process(Set annotations, RoundEnvironment roundEnv) {
      if (roundEnv.processingOver()) {
         this.postProcess(roundEnv);
         return true;
      } else {
         this.processMixins(roundEnv);
         this.processShadows(roundEnv);
         this.processOverwrites(roundEnv);
         this.processAccessors(roundEnv);
         this.processInvokers(roundEnv);
         this.processImplements(roundEnv);
         this.postProcess(roundEnv);
         return true;
      }
   }

   protected void postProcess(RoundEnvironment roundEnv) {
      super.postProcess(roundEnv);

      try {
         this.mixins.writeReferences();
         this.mixins.writeMappings();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private void processShadows(RoundEnvironment roundEnv) {
      Iterator var2 = roundEnv.getElementsAnnotatedWith(Shadow.class).iterator();

      while(var2.hasNext()) {
         Element elem = (Element)var2.next();
         Element parent = elem.getEnclosingElement();
         if (!(parent instanceof TypeElement)) {
            this.mixins.printMessage(Kind.ERROR, "Unexpected parent with type " + TypeUtils.getElementType(parent), elem);
         } else {
            AnnotationHandle shadow = AnnotationHandle.of(elem, Shadow.class);
            if (elem.getKind() == ElementKind.FIELD) {
               this.mixins.registerShadow((TypeElement)parent, (VariableElement)elem, shadow);
            } else if (elem.getKind() == ElementKind.METHOD) {
               this.mixins.registerShadow((TypeElement)parent, (ExecutableElement)elem, shadow);
            } else {
               this.mixins.printMessage(Kind.ERROR, "Element is not a method or field", elem);
            }
         }
      }

   }

   private void processOverwrites(RoundEnvironment roundEnv) {
      Iterator var2 = roundEnv.getElementsAnnotatedWith(Overwrite.class).iterator();

      while(var2.hasNext()) {
         Element elem = (Element)var2.next();
         Element parent = elem.getEnclosingElement();
         if (!(parent instanceof TypeElement)) {
            this.mixins.printMessage(Kind.ERROR, "Unexpected parent with type " + TypeUtils.getElementType(parent), elem);
         } else if (elem.getKind() == ElementKind.METHOD) {
            this.mixins.registerOverwrite((TypeElement)parent, (ExecutableElement)elem);
         } else {
            this.mixins.printMessage(Kind.ERROR, "Element is not a method", elem);
         }
      }

   }

   private void processAccessors(RoundEnvironment roundEnv) {
      Iterator var2 = roundEnv.getElementsAnnotatedWith(Accessor.class).iterator();

      while(var2.hasNext()) {
         Element elem = (Element)var2.next();
         Element parent = elem.getEnclosingElement();
         if (!(parent instanceof TypeElement)) {
            this.mixins.printMessage(Kind.ERROR, "Unexpected parent with type " + TypeUtils.getElementType(parent), elem);
         } else if (elem.getKind() == ElementKind.METHOD) {
            this.mixins.registerAccessor((TypeElement)parent, (ExecutableElement)elem);
         } else {
            this.mixins.printMessage(Kind.ERROR, "Element is not a method", elem);
         }
      }

   }

   private void processInvokers(RoundEnvironment roundEnv) {
      Iterator var2 = roundEnv.getElementsAnnotatedWith(Invoker.class).iterator();

      while(var2.hasNext()) {
         Element elem = (Element)var2.next();
         Element parent = elem.getEnclosingElement();
         if (!(parent instanceof TypeElement)) {
            this.mixins.printMessage(Kind.ERROR, "Unexpected parent with type " + TypeUtils.getElementType(parent), elem);
         } else if (elem.getKind() == ElementKind.METHOD) {
            this.mixins.registerInvoker((TypeElement)parent, (ExecutableElement)elem);
         } else {
            this.mixins.printMessage(Kind.ERROR, "Element is not a method", elem);
         }
      }

   }

   private void processImplements(RoundEnvironment roundEnv) {
      Iterator var2 = roundEnv.getElementsAnnotatedWith(Implements.class).iterator();

      while(true) {
         while(var2.hasNext()) {
            Element elem = (Element)var2.next();
            if (elem.getKind() != ElementKind.CLASS && elem.getKind() != ElementKind.INTERFACE) {
               this.mixins.printMessage(Kind.ERROR, "Found an @Implements annotation on an element which is not a class or interface", elem);
            } else {
               AnnotationHandle implementsAnnotation = AnnotationHandle.of(elem, Implements.class);
               this.mixins.registerSoftImplements((TypeElement)elem, implementsAnnotation);
            }
         }

         return;
      }
   }
}
