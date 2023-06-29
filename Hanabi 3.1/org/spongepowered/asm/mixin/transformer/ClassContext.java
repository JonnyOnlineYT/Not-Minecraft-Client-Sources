package org.spongepowered.asm.mixin.transformer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.struct.MemberRef;

abstract class ClassContext {
   private final Set upgradedMethods = new HashSet();

   abstract String getClassRef();

   abstract ClassNode getClassNode();

   abstract ClassInfo getClassInfo();

   void addUpgradedMethod(MethodNode method) {
      ClassInfo.Method md = this.getClassInfo().findMethod(method);
      if (md == null) {
         throw new IllegalStateException("Meta method for " + method.name + " not located in " + this);
      } else {
         this.upgradedMethods.add(md);
      }
   }

   protected void upgradeMethods() {
      Iterator var1 = this.getClassNode().methods.iterator();

      while(var1.hasNext()) {
         MethodNode method = (MethodNode)var1.next();
         this.upgradeMethod(method);
      }

   }

   private void upgradeMethod(MethodNode method) {
      Iterator iter = method.instructions.iterator();

      while(iter.hasNext()) {
         AbstractInsnNode insn = (AbstractInsnNode)iter.next();
         if (insn instanceof MethodInsnNode) {
            MemberRef methodRef = new MemberRef.Method((MethodInsnNode)insn);
            if (methodRef.getOwner().equals(this.getClassRef())) {
               ClassInfo.Method md = this.getClassInfo().findMethod(methodRef.getName(), methodRef.getDesc(), 10);
               this.upgradeMethodRef(method, methodRef, md);
            }
         }
      }

   }

   protected void upgradeMethodRef(MethodNode containingMethod, MemberRef methodRef, ClassInfo.Method method) {
      if (methodRef.getOpcode() == 183) {
         if (this.upgradedMethods.contains(method)) {
            methodRef.setOpcode(182);
         }

      }
   }
}
