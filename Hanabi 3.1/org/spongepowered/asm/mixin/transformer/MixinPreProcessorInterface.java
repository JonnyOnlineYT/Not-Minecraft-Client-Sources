package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidInterfaceMixinException;
import org.spongepowered.asm.util.Bytecode;

class MixinPreProcessorInterface extends MixinPreProcessorStandard {
   MixinPreProcessorInterface(MixinInfo mixin, MixinInfo.MixinClassNode classNode) {
      super(mixin, classNode);
   }

   protected void prepareMethod(MixinInfo.MixinMethodNode mixinMethod, ClassInfo.Method method) {
      if (!Bytecode.hasFlag((MethodNode)mixinMethod, 1) && !Bytecode.hasFlag((MethodNode)mixinMethod, 4096)) {
         throw new InvalidInterfaceMixinException(this.mixin, "Interface mixin contains a non-public method! Found " + method + " in " + this.mixin);
      } else {
         super.prepareMethod(mixinMethod, method);
      }
   }

   protected boolean validateField(MixinTargetContext context, FieldNode field, AnnotationNode shadow) {
      if (!Bytecode.hasFlag((FieldNode)field, 8)) {
         throw new InvalidInterfaceMixinException(this.mixin, "Interface mixin contains an instance field! Found " + field.name + " in " + this.mixin);
      } else {
         return super.validateField(context, field, shadow);
      }
   }
}
