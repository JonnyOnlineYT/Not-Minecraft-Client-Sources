package org.spongepowered.asm.mixin.injection.points;

import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;

@InjectionPoint.AtCode("HEAD")
public class MethodHead extends InjectionPoint {
   public MethodHead(InjectionPointData data) {
      super(data);
   }

   public boolean checkPriority(int targetPriority, int ownerPriority) {
      return true;
   }

   public boolean find(String desc, InsnList insns, Collection nodes) {
      nodes.add(insns.getFirst());
      return true;
   }
}
