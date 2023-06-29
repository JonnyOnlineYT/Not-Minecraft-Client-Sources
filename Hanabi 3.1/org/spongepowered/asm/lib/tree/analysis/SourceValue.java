package org.spongepowered.asm.lib.tree.analysis;

import java.util.Set;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;

public class SourceValue implements Value {
   public final int size;
   public final Set insns;

   public SourceValue(int size) {
      this(size, SmallSet.emptySet());
   }

   public SourceValue(int size, AbstractInsnNode insn) {
      this.size = size;
      this.insns = new SmallSet(insn, (Object)null);
   }

   public SourceValue(int size, Set insns) {
      this.size = size;
      this.insns = insns;
   }

   public int getSize() {
      return this.size;
   }

   public boolean equals(Object value) {
      if (!(value instanceof SourceValue)) {
         return false;
      } else {
         SourceValue v = (SourceValue)value;
         return this.size == v.size && this.insns.equals(v.insns);
      }
   }

   public int hashCode() {
      return this.insns.hashCode();
   }
}
