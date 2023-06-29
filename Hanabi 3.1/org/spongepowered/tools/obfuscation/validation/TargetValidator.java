package org.spongepowered.tools.obfuscation.validation;

import java.util.Collection;
import java.util.Iterator;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.tools.obfuscation.MixinValidator;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;

public class TargetValidator extends MixinValidator {
   public TargetValidator(IMixinAnnotationProcessor ap) {
      super(ap, IMixinValidator.ValidationPass.LATE);
   }

   public boolean validate(TypeElement mixin, AnnotationHandle annotation, Collection targets) {
      if ("true".equalsIgnoreCase(this.options.getOption("disableTargetValidator"))) {
         return true;
      } else {
         if (mixin.getKind() == ElementKind.INTERFACE) {
            this.validateInterfaceMixin(mixin, targets);
         } else {
            this.validateClassMixin(mixin, targets);
         }

         return true;
      }
   }

   private void validateInterfaceMixin(TypeElement mixin, Collection targets) {
      boolean containsNonAccessorMethod = false;
      Iterator var4 = mixin.getEnclosedElements().iterator();

      while(true) {
         Element element;
         do {
            if (!var4.hasNext()) {
               if (!containsNonAccessorMethod) {
                  return;
               }

               var4 = targets.iterator();

               while(var4.hasNext()) {
                  TypeHandle target = (TypeHandle)var4.next();
                  TypeElement targetType = target.getElement();
                  if (targetType != null && targetType.getKind() != ElementKind.INTERFACE) {
                     this.error("Targetted type '" + target + " of " + mixin + " is not an interface", mixin);
                  }
               }

               return;
            }

            element = (Element)var4.next();
         } while(element.getKind() != ElementKind.METHOD);

         boolean isAccessor = AnnotationHandle.of(element, Accessor.class).exists();
         boolean isInvoker = AnnotationHandle.of(element, Invoker.class).exists();
         containsNonAccessorMethod |= !isAccessor && !isInvoker;
      }
   }

   private void validateClassMixin(TypeElement mixin, Collection targets) {
      TypeMirror superClass = mixin.getSuperclass();
      Iterator var4 = targets.iterator();

      while(var4.hasNext()) {
         TypeHandle target = (TypeHandle)var4.next();
         TypeMirror targetType = target.getType();
         if (targetType != null && !this.validateSuperClass(targetType, superClass)) {
            this.error("Superclass " + superClass + " of " + mixin + " was not found in the hierarchy of target class " + targetType, mixin);
         }
      }

   }

   private boolean validateSuperClass(TypeMirror targetType, TypeMirror superClass) {
      return TypeUtils.isAssignable(this.processingEnv, targetType, superClass) ? true : this.validateSuperClassRecursive(targetType, superClass);
   }

   private boolean validateSuperClassRecursive(TypeMirror targetType, TypeMirror superClass) {
      if (!(targetType instanceof DeclaredType)) {
         return false;
      } else if (TypeUtils.isAssignable(this.processingEnv, targetType, superClass)) {
         return true;
      } else {
         TypeElement targetElement = (TypeElement)((DeclaredType)targetType).asElement();
         TypeMirror targetSuper = targetElement.getSuperclass();
         if (targetSuper.getKind() == TypeKind.NONE) {
            return false;
         } else {
            return this.checkMixinsFor(targetSuper, superClass) ? true : this.validateSuperClassRecursive(targetSuper, superClass);
         }
      }
   }

   private boolean checkMixinsFor(TypeMirror targetType, TypeMirror superClass) {
      Iterator var3 = this.getMixinsTargeting(targetType).iterator();

      TypeMirror mixinType;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         mixinType = (TypeMirror)var3.next();
      } while(!TypeUtils.isAssignable(this.processingEnv, mixinType, superClass));

      return true;
   }
}
