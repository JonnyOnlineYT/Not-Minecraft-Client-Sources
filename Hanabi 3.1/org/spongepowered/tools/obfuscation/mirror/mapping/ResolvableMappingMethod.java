package org.spongepowered.tools.obfuscation.mirror.mapping;

import java.util.Iterator;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;

public final class ResolvableMappingMethod extends MappingMethod {
   private final TypeHandle ownerHandle;

   public ResolvableMappingMethod(TypeHandle owner, String name, String desc) {
      super(owner.getName(), name, desc);
      this.ownerHandle = owner;
   }

   public MappingMethod getSuper() {
      if (this.ownerHandle == null) {
         return super.getSuper();
      } else {
         String name = this.getSimpleName();
         String desc = this.getDesc();
         String signature = TypeUtils.getJavaSignature(desc);
         TypeHandle superClass = this.ownerHandle.getSuperclass();
         if (superClass != null && superClass.findMethod(name, signature) != null) {
            return superClass.getMappingMethod(name, desc);
         } else {
            Iterator var5 = this.ownerHandle.getInterfaces().iterator();

            TypeHandle iface;
            do {
               if (!var5.hasNext()) {
                  if (superClass != null) {
                     return superClass.getMappingMethod(name, desc).getSuper();
                  }

                  return super.getSuper();
               }

               iface = (TypeHandle)var5.next();
            } while(iface.findMethod(name, signature) == null);

            return iface.getMappingMethod(name, desc);
         }
      }
   }

   public MappingMethod move(TypeHandle newOwner) {
      return new ResolvableMappingMethod(newOwner, this.getSimpleName(), this.getDesc());
   }

   public MappingMethod remap(String newName) {
      return new ResolvableMappingMethod(this.ownerHandle, newName, this.getDesc());
   }

   public MappingMethod transform(String newDesc) {
      return new ResolvableMappingMethod(this.ownerHandle, this.getSimpleName(), newDesc);
   }

   public MappingMethod copy() {
      return new ResolvableMappingMethod(this.ownerHandle, this.getSimpleName(), this.getDesc());
   }
}
