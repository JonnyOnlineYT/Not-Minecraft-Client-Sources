package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class IntInsnNode extends AbstractInsnNode {
   public int operand;

   public IntInsnNode(int opcode, int operand) {
      super(opcode);
      this.operand = operand;
   }

   public void setOpcode(int opcode) {
      this.opcode = opcode;
   }

   public int getType() {
      return 1;
   }

   public void accept(MethodVisitor mv) {
      mv.visitIntInsn(this.opcode, this.operand);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map labels) {
      return (new IntInsnNode(this.opcode, this.operand)).cloneAnnotations(this);
   }
}
