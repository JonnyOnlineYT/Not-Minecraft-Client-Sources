package org.spongepowered.asm.lib.tree.analysis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;

public class SourceInterpreter extends Interpreter implements Opcodes {
   public SourceInterpreter() {
      super(327680);
   }

   protected SourceInterpreter(int api) {
      super(api);
   }

   public SourceValue newValue(Type type) {
      return type == Type.VOID_TYPE ? null : new SourceValue(type == null ? 1 : type.getSize());
   }

   public SourceValue newOperation(AbstractInsnNode insn) {
      int size;
      switch (insn.getOpcode()) {
         case 9:
         case 10:
         case 14:
         case 15:
            size = 2;
            break;
         case 18:
            Object cst = ((LdcInsnNode)insn).cst;
            size = !(cst instanceof Long) && !(cst instanceof Double) ? 1 : 2;
            break;
         case 178:
            size = Type.getType(((FieldInsnNode)insn).desc).getSize();
            break;
         default:
            size = 1;
      }

      return new SourceValue(size, insn);
   }

   public SourceValue copyOperation(AbstractInsnNode insn, SourceValue value) {
      return new SourceValue(value.getSize(), insn);
   }

   public SourceValue unaryOperation(AbstractInsnNode insn, SourceValue value) {
      int size;
      switch (insn.getOpcode()) {
         case 117:
         case 119:
         case 133:
         case 135:
         case 138:
         case 140:
         case 141:
         case 143:
            size = 2;
            break;
         case 180:
            size = Type.getType(((FieldInsnNode)insn).desc).getSize();
            break;
         default:
            size = 1;
      }

      return new SourceValue(size, insn);
   }

   public SourceValue binaryOperation(AbstractInsnNode insn, SourceValue value1, SourceValue value2) {
      byte size;
      switch (insn.getOpcode()) {
         case 47:
         case 49:
         case 97:
         case 99:
         case 101:
         case 103:
         case 105:
         case 107:
         case 109:
         case 111:
         case 113:
         case 115:
         case 121:
         case 123:
         case 125:
         case 127:
         case 129:
         case 131:
            size = 2;
            break;
         default:
            size = 1;
      }

      return new SourceValue(size, insn);
   }

   public SourceValue ternaryOperation(AbstractInsnNode insn, SourceValue value1, SourceValue value2, SourceValue value3) {
      return new SourceValue(1, insn);
   }

   public SourceValue naryOperation(AbstractInsnNode insn, List values) {
      int opcode = insn.getOpcode();
      int size;
      if (opcode == 197) {
         size = 1;
      } else {
         String desc = opcode == 186 ? ((InvokeDynamicInsnNode)insn).desc : ((MethodInsnNode)insn).desc;
         size = Type.getReturnType(desc).getSize();
      }

      return new SourceValue(size, insn);
   }

   public void returnOperation(AbstractInsnNode insn, SourceValue value, SourceValue expected) {
   }

   public SourceValue merge(SourceValue d, SourceValue w) {
      if (d.insns instanceof SmallSet && w.insns instanceof SmallSet) {
         Set s = ((SmallSet)d.insns).union((SmallSet)w.insns);
         return s == d.insns && d.size == w.size ? d : new SourceValue(Math.min(d.size, w.size), s);
      } else if (d.size == w.size && d.insns.containsAll(w.insns)) {
         return d;
      } else {
         HashSet s = new HashSet();
         s.addAll(d.insns);
         s.addAll(w.insns);
         return new SourceValue(Math.min(d.size, w.size), s);
      }
   }
}
