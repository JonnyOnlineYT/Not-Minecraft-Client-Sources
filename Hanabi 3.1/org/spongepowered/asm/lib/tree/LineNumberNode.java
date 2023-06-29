package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class LineNumberNode extends AbstractInsnNode {
   public int line;
   public LabelNode start;

   public LineNumberNode(int line, LabelNode start) {
      super(-1);
      this.line = line;
      this.start = start;
   }

   public int getType() {
      return 15;
   }

   public void accept(MethodVisitor mv) {
      mv.visitLineNumber(this.line, this.start.getLabel());
   }

   public AbstractInsnNode clone(Map labels) {
      return new LineNumberNode(this.line, clone(this.start, labels));
   }
}
