package org.spongepowered.tools.obfuscation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.struct.InvalidMemberDescriptorException;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IReferenceManager;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.struct.InjectorRemap;

class AnnotatedMixinElementHandlerInjector extends AnnotatedMixinElementHandler {
   AnnotatedMixinElementHandlerInjector(IMixinAnnotationProcessor ap, AnnotatedMixin mixin) {
      super(ap, mixin);
   }

   public void registerInjector(AnnotatedElementInjector elem) {
      if (this.mixin.isInterface()) {
         this.ap.printMessage(Kind.ERROR, "Injector in interface is unsupported", elem.getElement());
      }

      Iterator var2 = elem.getAnnotation().getList("method").iterator();

      while(true) {
         String reference;
         MemberInfo targetMember;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               reference = (String)var2.next();
               targetMember = MemberInfo.parse(reference);
            } while(targetMember.name == null);

            try {
               targetMember.validate();
            } catch (InvalidMemberDescriptorException var7) {
               elem.printMessage(this.ap, Kind.ERROR, var7.getMessage());
            }

            if (targetMember.desc != null) {
               this.validateReferencedTarget((ExecutableElement)elem.getElement(), elem.getAnnotation(), targetMember, elem.toString());
            }
         } while(!elem.shouldRemap());

         Iterator var5 = this.mixin.getTargets().iterator();

         while(var5.hasNext()) {
            TypeHandle target = (TypeHandle)var5.next();
            if (!this.registerInjector(elem, reference, targetMember, target)) {
               break;
            }
         }
      }
   }

   private boolean registerInjector(AnnotatedElementInjector elem, String reference, MemberInfo targetMember, TypeHandle target) {
      String desc = target.findDescriptor(targetMember);
      if (desc == null) {
         Diagnostic.Kind error = this.mixin.isMultiTarget() ? Kind.ERROR : Kind.WARNING;
         if (target.isSimulated()) {
            elem.printMessage(this.ap, Kind.NOTE, elem + " target '" + reference + "' in @Pseudo mixin will not be obfuscated");
         } else if (target.isImaginary()) {
            elem.printMessage(this.ap, error, elem + " target requires method signature because enclosing type information for " + target + " is unavailable");
         } else if (!targetMember.isInitialiser()) {
            elem.printMessage(this.ap, error, "Unable to determine signature for " + elem + " target method");
         }

         return true;
      } else {
         String targetName = elem + " target " + targetMember.name;
         MappingMethod targetMethod = target.getMappingMethod(targetMember.name, desc);
         ObfuscationData obfData = this.obf.getDataProvider().getObfMethod(targetMethod);
         if (obfData.isEmpty()) {
            if (!target.isSimulated()) {
               if (targetMember.isClassInitialiser()) {
                  return true;
               }

               Diagnostic.Kind error = targetMember.isConstructor() ? Kind.WARNING : Kind.ERROR;
               elem.addMessage(error, "No obfuscation mapping for " + targetName, elem.getElement(), elem.getAnnotation());
               return false;
            }

            obfData = this.obf.getDataProvider().getRemappedMethod(targetMethod);
         }

         IReferenceManager refMap = this.obf.getReferenceManager();

         try {
            if (targetMember.owner == null && this.mixin.isMultiTarget() || target.isSimulated()) {
               obfData = AnnotatedMixinElementHandler.stripOwnerData(obfData);
            }

            refMap.addMethodMapping(this.classRef, reference, obfData);
         } catch (ReferenceManager.ReferenceConflictException var14) {
            String conflictType = this.mixin.isMultiTarget() ? "Multi-target" : "Target";
            if (elem.hasCoerceArgument() && targetMember.owner == null && targetMember.desc == null) {
               MemberInfo oldMember = MemberInfo.parse(var14.getOld());
               MemberInfo newMember = MemberInfo.parse(var14.getNew());
               if (oldMember.name.equals(newMember.name)) {
                  obfData = AnnotatedMixinElementHandler.stripDescriptors(obfData);
                  refMap.setAllowConflicts(true);
                  refMap.addMethodMapping(this.classRef, reference, obfData);
                  refMap.setAllowConflicts(false);
                  elem.printMessage(this.ap, Kind.WARNING, "Coerced " + conflictType + " reference has conflicting descriptors for " + targetName + ": Storing bare references " + obfData.values() + " in refMap");
                  return true;
               }
            }

            elem.printMessage(this.ap, Kind.ERROR, conflictType + " reference conflict for " + targetName + ": " + reference + " -> " + var14.getNew() + " previously defined as " + var14.getOld());
         }

         return true;
      }
   }

   public void registerInjectionPoint(AnnotatedElementInjectionPoint elem, String format) {
      if (this.mixin.isInterface()) {
         this.ap.printMessage(Kind.ERROR, "Injector in interface is unsupported", elem.getElement());
      }

      if (elem.shouldRemap()) {
         String type = InjectionPointData.parseType((String)elem.getAt().getValue("value"));
         String target = (String)elem.getAt().getValue("target");
         if ("NEW".equals(type)) {
            this.remapNewTarget(String.format(format, type + ".<target>"), target, elem);
            this.remapNewTarget(String.format(format, type + ".args[class]"), elem.getAtArg("class"), elem);
         } else {
            this.remapReference(String.format(format, type + ".<target>"), target, elem);
         }

      }
   }

   protected final void remapNewTarget(String subject, String reference, AnnotatedElementInjectionPoint elem) {
      if (reference != null) {
         MemberInfo member = MemberInfo.parse(reference);
         String target = member.toCtorType();
         if (target != null) {
            String desc = member.toCtorDesc();
            MappingMethod m = new MappingMethod(target, ".", desc != null ? desc : "()V");
            ObfuscationData remapped = this.obf.getDataProvider().getRemappedMethod(m);
            if (remapped.isEmpty()) {
               this.ap.printMessage(Kind.WARNING, "Cannot find class mapping for " + subject + " '" + target + "'", elem.getElement(), elem.getAnnotation().asMirror());
               return;
            }

            ObfuscationData mappings = new ObfuscationData();
            Iterator var10 = remapped.iterator();

            while(var10.hasNext()) {
               ObfuscationType type = (ObfuscationType)var10.next();
               MappingMethod mapping = (MappingMethod)remapped.get(type);
               if (desc == null) {
                  mappings.put(type, mapping.getOwner());
               } else {
                  mappings.put(type, mapping.getDesc().replace(")V", ")L" + mapping.getOwner() + ";"));
               }
            }

            this.obf.getReferenceManager().addClassMapping(this.classRef, reference, mappings);
         }

         elem.notifyRemapped();
      }
   }

   protected final void remapReference(String subject, String reference, AnnotatedElementInjectionPoint elem) {
      if (reference != null) {
         AnnotationMirror errorsOn = (this.ap.getCompilerEnvironment() == IMixinAnnotationProcessor.CompilerEnvironment.JDT ? elem.getAt() : elem.getAnnotation()).asMirror();
         MemberInfo targetMember = MemberInfo.parse(reference);
         if (!targetMember.isFullyQualified()) {
            String missing = targetMember.owner == null ? (targetMember.desc == null ? "owner and signature" : "owner") : "signature";
            this.ap.printMessage(Kind.ERROR, subject + " is not fully qualified, missing " + missing, elem.getElement(), errorsOn);
         } else {
            try {
               targetMember.validate();
            } catch (InvalidMemberDescriptorException var7) {
               this.ap.printMessage(Kind.ERROR, var7.getMessage(), elem.getElement(), errorsOn);
            }

            try {
               ObfuscationData obfMethodData;
               if (targetMember.isField()) {
                  obfMethodData = this.obf.getDataProvider().getObfFieldRecursive(targetMember);
                  if (obfMethodData.isEmpty()) {
                     this.ap.printMessage(Kind.WARNING, "Cannot find field mapping for " + subject + " '" + reference + "'", elem.getElement(), errorsOn);
                     return;
                  }

                  this.obf.getReferenceManager().addFieldMapping(this.classRef, reference, targetMember, obfMethodData);
               } else {
                  obfMethodData = this.obf.getDataProvider().getObfMethodRecursive(targetMember);
                  if (obfMethodData.isEmpty() && (targetMember.owner == null || !targetMember.owner.startsWith("java/lang/"))) {
                     this.ap.printMessage(Kind.WARNING, "Cannot find method mapping for " + subject + " '" + reference + "'", elem.getElement(), errorsOn);
                     return;
                  }

                  this.obf.getReferenceManager().addMethodMapping(this.classRef, reference, targetMember, obfMethodData);
               }
            } catch (ReferenceManager.ReferenceConflictException var8) {
               this.ap.printMessage(Kind.ERROR, "Unexpected reference conflict for " + subject + ": " + reference + " -> " + var8.getNew() + " previously defined as " + var8.getOld(), elem.getElement(), errorsOn);
               return;
            }

            elem.notifyRemapped();
         }
      }
   }

   static class AnnotatedElementInjectionPoint extends AnnotatedMixinElementHandler.AnnotatedElement {
      private final AnnotationHandle at;
      private Map args;
      private final InjectorRemap state;

      public AnnotatedElementInjectionPoint(ExecutableElement element, AnnotationHandle inject, AnnotationHandle at, InjectorRemap state) {
         super(element, inject);
         this.at = at;
         this.state = state;
      }

      public boolean shouldRemap() {
         return this.at.getBoolean("remap", this.state.shouldRemap());
      }

      public AnnotationHandle getAt() {
         return this.at;
      }

      public String getAtArg(String key) {
         if (this.args == null) {
            this.args = new HashMap();
            Iterator var2 = this.at.getList("args").iterator();

            while(var2.hasNext()) {
               String arg = (String)var2.next();
               if (arg != null) {
                  int eqPos = arg.indexOf(61);
                  if (eqPos > -1) {
                     this.args.put(arg.substring(0, eqPos), arg.substring(eqPos + 1));
                  } else {
                     this.args.put(arg, "");
                  }
               }
            }
         }

         return (String)this.args.get(key);
      }

      public void notifyRemapped() {
         this.state.notifyRemapped();
      }
   }

   static class AnnotatedElementInjector extends AnnotatedMixinElementHandler.AnnotatedElement {
      private final InjectorRemap state;

      public AnnotatedElementInjector(ExecutableElement element, AnnotationHandle annotation, InjectorRemap shouldRemap) {
         super(element, annotation);
         this.state = shouldRemap;
      }

      public boolean shouldRemap() {
         return this.state.shouldRemap();
      }

      public boolean hasCoerceArgument() {
         if (!this.annotation.toString().equals("@Inject")) {
            return false;
         } else {
            Iterator var1 = ((ExecutableElement)this.element).getParameters().iterator();
            if (var1.hasNext()) {
               VariableElement param = (VariableElement)var1.next();
               return AnnotationHandle.of(param, Coerce.class).exists();
            } else {
               return false;
            }
         }
      }

      public void addMessage(Diagnostic.Kind kind, CharSequence msg, Element element, AnnotationHandle annotation) {
         this.state.addMessage(kind, msg, element, annotation);
      }

      public String toString() {
         return this.getAnnotation().toString();
      }
   }
}
