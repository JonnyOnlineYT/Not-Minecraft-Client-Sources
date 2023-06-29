package org.spongepowered.tools.obfuscation;

import java.util.Iterator;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;

@SupportedAnnotationTypes({"org.spongepowered.asm.mixin.injection.Inject", "org.spongepowered.asm.mixin.injection.ModifyArg", "org.spongepowered.asm.mixin.injection.ModifyArgs", "org.spongepowered.asm.mixin.injection.Redirect", "org.spongepowered.asm.mixin.injection.At"})
public class MixinObfuscationProcessorInjection extends MixinObfuscationProcessor {
   public boolean process(Set annotations, RoundEnvironment roundEnv) {
      if (roundEnv.processingOver()) {
         this.postProcess(roundEnv);
         return true;
      } else {
         this.processMixins(roundEnv);
         this.processInjectors(roundEnv, Inject.class);
         this.processInjectors(roundEnv, ModifyArg.class);
         this.processInjectors(roundEnv, ModifyArgs.class);
         this.processInjectors(roundEnv, Redirect.class);
         this.processInjectors(roundEnv, ModifyVariable.class);
         this.processInjectors(roundEnv, ModifyConstant.class);
         this.postProcess(roundEnv);
         return true;
      }
   }

   protected void postProcess(RoundEnvironment roundEnv) {
      super.postProcess(roundEnv);

      try {
         this.mixins.writeReferences();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private void processInjectors(RoundEnvironment roundEnv, Class injectorClass) {
      Iterator var3 = roundEnv.getElementsAnnotatedWith(injectorClass).iterator();

      while(var3.hasNext()) {
         Element elem = (Element)var3.next();
         Element parent = elem.getEnclosingElement();
         if (!(parent instanceof TypeElement)) {
            throw new IllegalStateException("@" + injectorClass.getSimpleName() + " element has unexpected parent with type " + TypeUtils.getElementType(parent));
         }

         AnnotationHandle inject = AnnotationHandle.of(elem, injectorClass);
         if (elem.getKind() == ElementKind.METHOD) {
            this.mixins.registerInjector((TypeElement)parent, (ExecutableElement)elem, inject);
         } else {
            this.mixins.printMessage(Kind.WARNING, "Found an @" + injectorClass.getSimpleName() + " annotation on an element which is not a method: " + elem.toString());
         }
      }

   }
}
