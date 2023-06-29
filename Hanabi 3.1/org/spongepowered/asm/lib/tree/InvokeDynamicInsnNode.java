package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.MethodVisitor;

public class InvokeDynamicInsnNode extends AbstractInsnNode {
   public String name;
   public String desc;
   public Handle bsm;
   public Object[] bsmArgs;

   public InvokeDynamicInsnNode(String name, String desc, Handle bsm, Object... bsmArgs) {
      super(186);
      this.name = name;
      this.desc = desc;
      this.bsm = bsm;
      this.bsmArgs = bsmArgs;
   }

   public int getType() {
      return 6;
   }

   public void accept(MethodVisitor mv) {
      mv.visitInvokeDynamicInsn(this.name, this.desc, this.bsm, this.bsmArgs);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map labels) {
      return (new InvokeDynamicInsnNode(this.name, this.desc, this.bsm, this.bsmArgs)).cloneAnnotations(this);
   }
}
