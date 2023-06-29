package org.spongepowered.asm.util.asm;

import java.util.List;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.analysis.SimpleVerifier;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

public class MixinVerifier extends SimpleVerifier {
   private Type currentClass;
   private Type currentSuperClass;
   private List currentClassInterfaces;
   private boolean isInterface;

   public MixinVerifier(Type currentClass, Type currentSuperClass, List currentClassInterfaces, boolean isInterface) {
      super(currentClass, currentSuperClass, currentClassInterfaces, isInterface);
      this.currentClass = currentClass;
      this.currentSuperClass = currentSuperClass;
      this.currentClassInterfaces = currentClassInterfaces;
      this.isInterface = isInterface;
   }

   protected boolean isInterface(Type type) {
      return this.currentClass != null && type.equals(this.currentClass) ? this.isInterface : ClassInfo.forType(type).isInterface();
   }

   protected Type getSuperClass(Type type) {
      if (this.currentClass != null && type.equals(this.currentClass)) {
         return this.currentSuperClass;
      } else {
         ClassInfo c = ClassInfo.forType(type).getSuperClass();
         return c == null ? null : Type.getType("L" + c.getName() + ";");
      }
   }

   protected boolean isAssignableFrom(Type type, Type other) {
      if (type.equals(other)) {
         return true;
      } else if (this.currentClass != null && type.equals(this.currentClass)) {
         if (this.getSuperClass(other) == null) {
            return false;
         } else if (!this.isInterface) {
            return this.isAssignableFrom(type, this.getSuperClass(other));
         } else {
            return other.getSort() == 10 || other.getSort() == 9;
         }
      } else if (this.currentClass != null && other.equals(this.currentClass)) {
         if (this.isAssignableFrom(type, this.currentSuperClass)) {
            return true;
         } else {
            if (this.currentClassInterfaces != null) {
               for(int i = 0; i < this.currentClassInterfaces.size(); ++i) {
                  Type v = (Type)this.currentClassInterfaces.get(i);
                  if (this.isAssignableFrom(type, v)) {
                     return true;
                  }
               }
            }

            return false;
         }
      } else {
         ClassInfo typeInfo = ClassInfo.forType(type);
         if (typeInfo == null) {
            return false;
         } else {
            if (typeInfo.isInterface()) {
               typeInfo = ClassInfo.forName("java/lang/Object");
            }

            return ClassInfo.forType(other).hasSuperClass(typeInfo);
         }
      }
   }
}
