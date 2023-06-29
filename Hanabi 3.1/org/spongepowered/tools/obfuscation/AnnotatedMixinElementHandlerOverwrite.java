package org.spongepowered.tools.obfuscation;

import java.lang.reflect.Method;
import java.util.Iterator;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;

class AnnotatedMixinElementHandlerOverwrite extends AnnotatedMixinElementHandler {
   AnnotatedMixinElementHandlerOverwrite(IMixinAnnotationProcessor ap, AnnotatedMixin mixin) {
      super(ap, mixin);
   }

   public void registerMerge(ExecutableElement method) {
      this.validateTargetMethod(method, (AnnotationHandle)null, new AnnotatedMixinElementHandler.AliasedElementName(method, AnnotationHandle.MISSING), "overwrite", true, true);
   }

   public void registerOverwrite(AnnotatedElementOverwrite elem) {
      AnnotatedMixinElementHandler.AliasedElementName name = new AnnotatedMixinElementHandler.AliasedElementName(elem.getElement(), elem.getAnnotation());
      this.validateTargetMethod((ExecutableElement)elem.getElement(), elem.getAnnotation(), name, "@Overwrite", true, false);
      this.checkConstraints((ExecutableElement)elem.getElement(), elem.getAnnotation());
      if (elem.shouldRemap()) {
         Iterator var3 = this.mixin.getTargets().iterator();

         while(var3.hasNext()) {
            TypeHandle target = (TypeHandle)var3.next();
            if (!this.registerOverwriteForTarget(elem, target)) {
               return;
            }
         }
      }

      if (!"true".equalsIgnoreCase(this.ap.getOption("disableOverwriteChecker"))) {
         Diagnostic.Kind overwriteErrorKind = "error".equalsIgnoreCase(this.ap.getOption("overwriteErrorLevel")) ? Kind.ERROR : Kind.WARNING;
         String javadoc = this.ap.getJavadocProvider().getJavadoc(elem.getElement());
         if (javadoc == null) {
            this.ap.printMessage(overwriteErrorKind, "@Overwrite is missing javadoc comment", elem.getElement());
            return;
         }

         if (!javadoc.toLowerCase().contains("@author")) {
            this.ap.printMessage(overwriteErrorKind, "@Overwrite is missing an @author tag", elem.getElement());
         }

         if (!javadoc.toLowerCase().contains("@reason")) {
            this.ap.printMessage(overwriteErrorKind, "@Overwrite is missing an @reason tag", elem.getElement());
         }
      }

   }

   private boolean registerOverwriteForTarget(AnnotatedElementOverwrite elem, TypeHandle target) {
      MappingMethod targetMethod = target.getMappingMethod(elem.getSimpleName(), elem.getDesc());
      ObfuscationData obfData = this.obf.getDataProvider().getObfMethod(targetMethod);
      if (obfData.isEmpty()) {
         Diagnostic.Kind error = Kind.ERROR;

         try {
            Method md = ((ExecutableElement)elem.getElement()).getClass().getMethod("isStatic");
            if ((Boolean)md.invoke(elem.getElement())) {
               error = Kind.WARNING;
            }
         } catch (Exception var7) {
         }

         this.ap.printMessage(error, "No obfuscation mapping for @Overwrite method", elem.getElement());
         return false;
      } else {
         try {
            this.addMethodMappings(elem.getSimpleName(), elem.getDesc(), obfData);
            return true;
         } catch (Mappings.MappingConflictException var8) {
            elem.printMessage(this.ap, Kind.ERROR, "Mapping conflict for @Overwrite method: " + var8.getNew().getSimpleName() + " for target " + target + " conflicts with existing mapping " + var8.getOld().getSimpleName());
            return false;
         }
      }
   }

   static class AnnotatedElementOverwrite extends AnnotatedMixinElementHandler.AnnotatedElement {
      private final boolean shouldRemap;

      public AnnotatedElementOverwrite(ExecutableElement element, AnnotationHandle annotation, boolean shouldRemap) {
         super(element, annotation);
         this.shouldRemap = shouldRemap;
      }

      public boolean shouldRemap() {
         return this.shouldRemap;
      }
   }
}
