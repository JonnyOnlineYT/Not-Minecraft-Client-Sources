package org.spongepowered.tools.obfuscation;

import java.util.Iterator;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationDataProvider;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;

class AnnotatedMixinElementHandlerShadow extends AnnotatedMixinElementHandler {
   AnnotatedMixinElementHandlerShadow(IMixinAnnotationProcessor ap, AnnotatedMixin mixin) {
      super(ap, mixin);
   }

   public void registerShadow(AnnotatedElementShadow elem) {
      this.validateTarget(elem.getElement(), elem.getAnnotation(), elem.getName(), "@Shadow");
      if (elem.shouldRemap()) {
         Iterator var2 = this.mixin.getTargets().iterator();

         while(var2.hasNext()) {
            TypeHandle target = (TypeHandle)var2.next();
            this.registerShadowForTarget(elem, target);
         }

      }
   }

   private void registerShadowForTarget(AnnotatedElementShadow elem, TypeHandle target) {
      ObfuscationData obfData = elem.getObfuscationData(this.obf.getDataProvider(), target);
      if (obfData.isEmpty()) {
         String info = this.mixin.isMultiTarget() ? " in target " + target : "";
         if (target.isSimulated()) {
            elem.printMessage(this.ap, Kind.WARNING, "Unable to locate obfuscation mapping" + info + " for @Shadow " + elem);
         } else {
            elem.printMessage(this.ap, Kind.WARNING, "Unable to locate obfuscation mapping" + info + " for @Shadow " + elem);
         }

      } else {
         Iterator var4 = obfData.iterator();

         while(var4.hasNext()) {
            ObfuscationType type = (ObfuscationType)var4.next();

            try {
               elem.addMapping(type, (IMapping)obfData.get(type));
            } catch (Mappings.MappingConflictException var7) {
               elem.printMessage(this.ap, Kind.ERROR, "Mapping conflict for @Shadow " + elem + ": " + var7.getNew().getSimpleName() + " for target " + target + " conflicts with existing mapping " + var7.getOld().getSimpleName());
            }
         }

      }
   }

   class AnnotatedElementShadowMethod extends AnnotatedElementShadow {
      public AnnotatedElementShadowMethod(ExecutableElement element, AnnotationHandle annotation, boolean shouldRemap) {
         super(element, annotation, shouldRemap, IMapping.Type.METHOD);
      }

      public MappingMethod getMapping(TypeHandle owner, String name, String desc) {
         return owner.getMappingMethod(name, desc);
      }

      public void addMapping(ObfuscationType type, IMapping remapped) {
         AnnotatedMixinElementHandlerShadow.this.addMethodMapping(type, this.setObfuscatedName(remapped), this.getDesc(), remapped.getDesc());
      }
   }

   class AnnotatedElementShadowField extends AnnotatedElementShadow {
      public AnnotatedElementShadowField(VariableElement element, AnnotationHandle annotation, boolean shouldRemap) {
         super(element, annotation, shouldRemap, IMapping.Type.FIELD);
      }

      public MappingField getMapping(TypeHandle owner, String name, String desc) {
         return new MappingField(owner.getName(), name, desc);
      }

      public void addMapping(ObfuscationType type, IMapping remapped) {
         AnnotatedMixinElementHandlerShadow.this.addFieldMapping(type, this.setObfuscatedName(remapped), this.getDesc(), remapped.getDesc());
      }
   }

   abstract static class AnnotatedElementShadow extends AnnotatedMixinElementHandler.AnnotatedElement {
      private final boolean shouldRemap;
      private final AnnotatedMixinElementHandler.ShadowElementName name;
      private final IMapping.Type type;

      protected AnnotatedElementShadow(Element element, AnnotationHandle annotation, boolean shouldRemap, IMapping.Type type) {
         super(element, annotation);
         this.shouldRemap = shouldRemap;
         this.name = new AnnotatedMixinElementHandler.ShadowElementName(element, annotation);
         this.type = type;
      }

      public boolean shouldRemap() {
         return this.shouldRemap;
      }

      public AnnotatedMixinElementHandler.ShadowElementName getName() {
         return this.name;
      }

      public IMapping.Type getElementType() {
         return this.type;
      }

      public String toString() {
         return this.getElementType().name().toLowerCase();
      }

      public AnnotatedMixinElementHandler.ShadowElementName setObfuscatedName(IMapping name) {
         return this.setObfuscatedName(name.getSimpleName());
      }

      public AnnotatedMixinElementHandler.ShadowElementName setObfuscatedName(String name) {
         return this.getName().setObfuscatedName(name);
      }

      public ObfuscationData getObfuscationData(IObfuscationDataProvider provider, TypeHandle owner) {
         return provider.getObfEntry(this.getMapping(owner, this.getName().toString(), this.getDesc()));
      }

      public abstract IMapping getMapping(TypeHandle var1, String var2, String var3);

      public abstract void addMapping(ObfuscationType var1, IMapping var2);
   }
}
