package org.spongepowered.asm.mixin.injection.points;

import java.util.Collection;
import java.util.ListIterator;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

@InjectionPoint.AtCode("TAIL")
public class BeforeFinalReturn extends InjectionPoint {
   private final IMixinContext context;

   public BeforeFinalReturn(InjectionPointData data) {
      super(data);
      this.context = data.getContext();
   }

   public boolean checkPriority(int targetPriority, int ownerPriority) {
      return true;
   }

   public boolean find(String desc, InsnList insns, Collection nodes) {
      AbstractInsnNode ret = null;
      int returnOpcode = Type.getReturnType(desc).getOpcode(172);
      ListIterator iter = insns.iterator();

      while(iter.hasNext()) {
         AbstractInsnNode insn = (AbstractInsnNode)iter.next();
         if (insn instanceof InsnNode && insn.getOpcode() == returnOpcode) {
            ret = insn;
         }
      }

      if (ret == null) {
         throw new InvalidInjectionException(this.context, "TAIL could not locate a valid RETURN in the target method!");
      } else {
         nodes.add(ret);
         return true;
      }
   }
}
