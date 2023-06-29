package org.spongepowered.tools.obfuscation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.processing.Messager;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;
import org.spongepowered.tools.obfuscation.interfaces.ITypeHandleProvider;
import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import org.spongepowered.tools.obfuscation.struct.InjectorRemap;

class AnnotatedMixin {
   private final AnnotationHandle annotation;
   private final Messager messager;
   private final ITypeHandleProvider typeProvider;
   private final IObfuscationManager obf;
   private final IMappingConsumer mappings;
   private final TypeElement mixin;
   private final List methods;
   private final TypeHandle handle;
   private final List targets = new ArrayList();
   private final TypeHandle primaryTarget;
   private final String classRef;
   private final boolean remap;
   private final boolean virtual;
   private final AnnotatedMixinElementHandlerOverwrite overwrites;
   private final AnnotatedMixinElementHandlerShadow shadows;
   private final AnnotatedMixinElementHandlerInjector injectors;
   private final AnnotatedMixinElementHandlerAccessor accessors;
   private final AnnotatedMixinElementHandlerSoftImplements softImplements;
   private boolean validated = false;

   public AnnotatedMixin(IMixinAnnotationProcessor ap, TypeElement type) {
      this.typeProvider = ap.getTypeProvider();
      this.obf = ap.getObfuscationManager();
      this.mappings = this.obf.createMappingConsumer();
      this.messager = ap;
      this.mixin = type;
      this.handle = new TypeHandle(type);
      this.methods = new ArrayList(this.handle.getEnclosedElements(ElementKind.METHOD));
      this.virtual = this.handle.getAnnotation(Pseudo.class).exists();
      this.annotation = this.handle.getAnnotation(Mixin.class);
      this.classRef = TypeUtils.getInternalName(type);
      this.primaryTarget = this.initTargets();
      this.remap = this.annotation.getBoolean("remap", true) && this.targets.size() > 0;
      this.overwrites = new AnnotatedMixinElementHandlerOverwrite(ap, this);
      this.shadows = new AnnotatedMixinElementHandlerShadow(ap, this);
      this.injectors = new AnnotatedMixinElementHandlerInjector(ap, this);
      this.accessors = new AnnotatedMixinElementHandlerAccessor(ap, this);
      this.softImplements = new AnnotatedMixinElementHandlerSoftImplements(ap, this);
   }

   AnnotatedMixin runValidators(IMixinValidator.ValidationPass pass, Collection validators) {
      Iterator var3 = validators.iterator();

      while(var3.hasNext()) {
         IMixinValidator validator = (IMixinValidator)var3.next();
         if (!validator.validate(pass, this.mixin, this.annotation, this.targets)) {
            break;
         }
      }

      if (pass == IMixinValidator.ValidationPass.FINAL && !this.validated) {
         this.validated = true;
         this.runFinalValidation();
      }

      return this;
   }

   private TypeHandle initTargets() {
      TypeHandle primaryTarget = null;

      Iterator var2;
      TypeHandle type;
      try {
         var2 = this.annotation.getList().iterator();

         while(var2.hasNext()) {
            TypeMirror target = (TypeMirror)var2.next();
            type = new TypeHandle((DeclaredType)target);
            if (!this.targets.contains(type)) {
               this.addTarget(type);
               if (primaryTarget == null) {
                  primaryTarget = type;
               }
            }
         }
      } catch (Exception var6) {
         this.printMessage(Kind.WARNING, "Error processing public targets: " + var6.getClass().getName() + ": " + var6.getMessage(), this);
      }

      try {
         var2 = this.annotation.getList("targets").iterator();

         while(var2.hasNext()) {
            String privateTarget = (String)var2.next();
            type = this.typeProvider.getTypeHandle(privateTarget);
            if (!this.targets.contains(type)) {
               if (this.virtual) {
                  type = this.typeProvider.getSimulatedHandle(privateTarget, this.mixin.asType());
               } else {
                  if (type == null) {
                     this.printMessage(Kind.ERROR, "Mixin target " + privateTarget + " could not be found", this);
                     return null;
                  }

                  if (type.isPublic()) {
                     this.printMessage(Kind.WARNING, "Mixin target " + privateTarget + " is public and must be specified in value", this);
                     return null;
                  }
               }

               this.addSoftTarget(type, privateTarget);
               if (primaryTarget == null) {
                  primaryTarget = type;
               }
            }
         }
      } catch (Exception var5) {
         this.printMessage(Kind.WARNING, "Error processing private targets: " + var5.getClass().getName() + ": " + var5.getMessage(), this);
      }

      if (primaryTarget == null) {
         this.printMessage(Kind.ERROR, "Mixin has no targets", this);
      }

      return primaryTarget;
   }

   private void printMessage(Diagnostic.Kind kind, CharSequence msg, AnnotatedMixin mixin) {
      this.messager.printMessage(kind, msg, this.mixin, this.annotation.asMirror());
   }

   private void addSoftTarget(TypeHandle type, String reference) {
      ObfuscationData obfClassData = this.obf.getDataProvider().getObfClass(type);
      if (!obfClassData.isEmpty()) {
         this.obf.getReferenceManager().addClassMapping(this.classRef, reference, obfClassData);
      }

      this.addTarget(type);
   }

   private void addTarget(TypeHandle type) {
      this.targets.add(type);
   }

   public String toString() {
      return this.mixin.getSimpleName().toString();
   }

   public AnnotationHandle getAnnotation() {
      return this.annotation;
   }

   public TypeElement getMixin() {
      return this.mixin;
   }

   public TypeHandle getHandle() {
      return this.handle;
   }

   public String getClassRef() {
      return this.classRef;
   }

   public boolean isInterface() {
      return this.mixin.getKind() == ElementKind.INTERFACE;
   }

   /** @deprecated */
   @Deprecated
   public TypeHandle getPrimaryTarget() {
      return this.primaryTarget;
   }

   public List getTargets() {
      return this.targets;
   }

   public boolean isMultiTarget() {
      return this.targets.size() > 1;
   }

   public boolean remap() {
      return this.remap;
   }

   public IMappingConsumer getMappings() {
      return this.mappings;
   }

   private void runFinalValidation() {
      Iterator var1 = this.methods.iterator();

      while(var1.hasNext()) {
         ExecutableElement method = (ExecutableElement)var1.next();
         this.overwrites.registerMerge(method);
      }

   }

   public void registerOverwrite(ExecutableElement method, AnnotationHandle overwrite, boolean shouldRemap) {
      this.methods.remove(method);
      this.overwrites.registerOverwrite(new AnnotatedMixinElementHandlerOverwrite.AnnotatedElementOverwrite(method, overwrite, shouldRemap));
   }

   public void registerShadow(VariableElement field, AnnotationHandle shadow, boolean shouldRemap) {
      this.shadows.registerShadow(this.shadows.new AnnotatedElementShadowField(field, shadow, shouldRemap));
   }

   public void registerShadow(ExecutableElement method, AnnotationHandle shadow, boolean shouldRemap) {
      this.methods.remove(method);
      this.shadows.registerShadow(this.shadows.new AnnotatedElementShadowMethod(method, shadow, shouldRemap));
   }

   public void registerInjector(ExecutableElement method, AnnotationHandle inject, InjectorRemap remap) {
      this.methods.remove(method);
      this.injectors.registerInjector(new AnnotatedMixinElementHandlerInjector.AnnotatedElementInjector(method, inject, remap));
      List ats = inject.getAnnotationList("at");
      Iterator var5 = ats.iterator();

      while(var5.hasNext()) {
         AnnotationHandle at = (AnnotationHandle)var5.next();
         this.registerInjectionPoint(method, inject, at, remap, "@At(%s)");
      }

      List slices = inject.getAnnotationList("slice");
      Iterator var12 = slices.iterator();

      while(var12.hasNext()) {
         AnnotationHandle slice = (AnnotationHandle)var12.next();
         String id = (String)slice.getValue("id", "");
         AnnotationHandle from = slice.getAnnotation("from");
         if (from != null) {
            this.registerInjectionPoint(method, inject, from, remap, "@Slice[" + id + "](from=@At(%s))");
         }

         AnnotationHandle to = slice.getAnnotation("to");
         if (to != null) {
            this.registerInjectionPoint(method, inject, to, remap, "@Slice[" + id + "](to=@At(%s))");
         }
      }

   }

   public void registerInjectionPoint(ExecutableElement element, AnnotationHandle inject, AnnotationHandle at, InjectorRemap remap, String format) {
      this.injectors.registerInjectionPoint(new AnnotatedMixinElementHandlerInjector.AnnotatedElementInjectionPoint(element, inject, at, remap), format);
   }

   public void registerAccessor(ExecutableElement element, AnnotationHandle accessor, boolean shouldRemap) {
      this.methods.remove(element);
      this.accessors.registerAccessor(new AnnotatedMixinElementHandlerAccessor.AnnotatedElementAccessor(element, accessor, shouldRemap));
   }

   public void registerInvoker(ExecutableElement element, AnnotationHandle invoker, boolean shouldRemap) {
      this.methods.remove(element);
      this.accessors.registerAccessor(new AnnotatedMixinElementHandlerAccessor.AnnotatedElementInvoker(element, invoker, shouldRemap));
   }

   public void registerSoftImplements(AnnotationHandle implementsAnnotation) {
      this.softImplements.process(implementsAnnotation);
   }
}
