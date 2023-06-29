package org.spongepowered.asm.lib.tree.analysis;

import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.lib.tree.LabelNode;

class Subroutine {
   LabelNode start;
   boolean[] access;
   List callers;

   private Subroutine() {
   }

   Subroutine(LabelNode start, int maxLocals, JumpInsnNode caller) {
      this.start = start;
      this.access = new boolean[maxLocals];
      this.callers = new ArrayList();
      this.callers.add(caller);
   }

   public Subroutine copy() {
      Subroutine result = new Subroutine();
      result.start = this.start;
      result.access = new boolean[this.access.length];
      System.arraycopy(this.access, 0, result.access, 0, this.access.length);
      result.callers = new ArrayList(this.callers);
      return result;
   }

   public boolean merge(Subroutine subroutine) throws AnalyzerException {
      boolean changes = false;

      int i;
      for(i = 0; i < this.access.length; ++i) {
         if (subroutine.access[i] && !this.access[i]) {
            this.access[i] = true;
            changes = true;
         }
      }

      if (subroutine.start == this.start) {
         for(i = 0; i < subroutine.callers.size(); ++i) {
            JumpInsnNode caller = (JumpInsnNode)subroutine.callers.get(i);
            if (!this.callers.contains(caller)) {
               this.callers.add(caller);
               changes = true;
            }
         }
      }

      return changes;
   }
}
