package org.spongepowered.asm.mixin.transformer;

import java.util.Iterator;
import java.util.Map;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidInterfaceMixinException;

class MixinApplicatorInterface extends MixinApplicatorStandard {
   MixinApplicatorInterface(TargetClassContext context) {
      super(context);
   }

   protected void applyInterfaces(MixinTargetContext mixin) {
      Iterator var2 = mixin.getInterfaces().iterator();

      while(var2.hasNext()) {
         String interfaceName = (String)var2.next();
         if (!this.targetClass.name.equals(interfaceName) && !this.targetClass.interfaces.contains(interfaceName)) {
            this.targetClass.interfaces.add(interfaceName);
            mixin.getTargetClassInfo().addInterface(interfaceName);
         }
      }

   }

   protected void applyFields(MixinTargetContext mixin) {
      Iterator var2 = mixin.getShadowFields().iterator();

      while(var2.hasNext()) {
         Map.Entry entry = (Map.Entry)var2.next();
         FieldNode shadow = (FieldNode)entry.getKey();
         this.logger.error("Ignoring redundant @Shadow field {}:{} in {}", new Object[]{shadow.name, shadow.desc, mixin});
      }

      this.mergeNewFields(mixin);
   }

   protected void applyInitialisers(MixinTargetContext mixin) {
   }

   protected void prepareInjections(MixinTargetContext mixin) {
      Iterator var2 = this.targetClass.methods.iterator();

      while(var2.hasNext()) {
         MethodNode method = (MethodNode)var2.next();

         try {
            InjectionInfo injectInfo = InjectionInfo.parse(mixin, method);
            if (injectInfo != null) {
               throw new InvalidInterfaceMixinException(mixin, injectInfo + " is not supported on interface mixin method " + method.name);
            }
         } catch (InvalidInjectionException var6) {
            String description = var6.getInjectionInfo() != null ? var6.getInjectionInfo().toString() : "Injection";
            throw new InvalidInterfaceMixinException(mixin, description + " is not supported in interface mixin");
         }
      }

   }

   protected void applyInjections(MixinTargetContext mixin) {
   }
}
