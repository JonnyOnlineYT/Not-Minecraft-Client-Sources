package org.spongepowered.asm.mixin.injection.points;

import java.util.Collection;
import java.util.ListIterator;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;

@InjectionPoint.AtCode("RETURN")
public class BeforeReturn extends InjectionPoint {
   private final int ordinal;

   public BeforeReturn(InjectionPointData data) {
      super(data);
      this.ordinal = data.getOrdinal();
   }

   public boolean checkPriority(int targetPriority, int ownerPriority) {
      return true;
   }

   public boolean find(String desc, InsnList insns, Collection nodes) {
      boolean found = false;
      int returnOpcode = Type.getReturnType(desc).getOpcode(172);
      int ordinal = 0;
      ListIterator iter = insns.iterator();

      while(true) {
         AbstractInsnNode insn;
         do {
            do {
               if (!iter.hasNext()) {
                  return found;
               }

               insn = (AbstractInsnNode)iter.next();
            } while(!(insn instanceof InsnNode));
         } while(insn.getOpcode() != returnOpcode);

         if (this.ordinal == -1 || this.ordinal == ordinal) {
            nodes.add(insn);
            found = true;
         }

         ++ordinal;
      }
   }
}
