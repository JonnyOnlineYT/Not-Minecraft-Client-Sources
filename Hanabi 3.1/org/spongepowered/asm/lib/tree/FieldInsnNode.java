package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class FieldInsnNode extends AbstractInsnNode {
   public String owner;
   public String name;
   public String desc;

   public FieldInsnNode(int opcode, String owner, String name, String desc) {
      super(opcode);
      this.owner = owner;
      this.name = name;
      this.desc = desc;
   }

   public void setOpcode(int opcode) {
      this.opcode = opcode;
   }

   public int getType() {
      return 4;
   }

   public void accept(MethodVisitor mv) {
      mv.visitFieldInsn(this.opcode, this.owner, this.name, this.desc);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map labels) {
      return (new FieldInsnNode(this.opcode, this.owner, this.name, this.desc)).cloneAnnotations(this);
   }
}
