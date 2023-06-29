package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class TypeInsnNode extends AbstractInsnNode {
   public String desc;

   public TypeInsnNode(int opcode, String desc) {
      super(opcode);
      this.desc = desc;
   }

   public void setOpcode(int opcode) {
      this.opcode = opcode;
   }

   public int getType() {
      return 3;
   }

   public void accept(MethodVisitor mv) {
      mv.visitTypeInsn(this.opcode, this.desc);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map labels) {
      return (new TypeInsnNode(this.opcode, this.desc)).cloneAnnotations(this);
   }
}
