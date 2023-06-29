package org.spongepowered.asm.mixin.gen;

import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;

public class AccessorGeneratorFieldSetter extends AccessorGeneratorField {
   public AccessorGeneratorFieldSetter(AccessorInfo info) {
      super(info);
   }

   public MethodNode generate() {
      int stackSpace = this.isInstanceField ? 1 : 0;
      int maxLocals = stackSpace + this.targetType.getSize();
      int maxStack = stackSpace + this.targetType.getSize();
      MethodNode method = this.createMethod(maxLocals, maxStack);
      if (this.isInstanceField) {
         method.instructions.add((AbstractInsnNode)(new VarInsnNode(25, 0)));
      }

      method.instructions.add((AbstractInsnNode)(new VarInsnNode(this.targetType.getOpcode(21), stackSpace)));
      int opcode = this.isInstanceField ? 181 : 179;
      method.instructions.add((AbstractInsnNode)(new FieldInsnNode(opcode, this.info.getClassNode().name, this.targetField.name, this.targetField.desc)));
      method.instructions.add((AbstractInsnNode)(new InsnNode(177)));
      return method;
   }
}
