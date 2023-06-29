package org.spongepowered.asm.lib.tree.analysis;

import java.util.List;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.IntInsnNode;
import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.MultiANewArrayInsnNode;
import org.spongepowered.asm.lib.tree.TypeInsnNode;

public class BasicInterpreter extends Interpreter implements Opcodes {
   public BasicInterpreter() {
      super(327680);
   }

   protected BasicInterpreter(int api) {
      super(api);
   }

   public BasicValue newValue(Type type) {
      if (type == null) {
         return BasicValue.UNINITIALIZED_VALUE;
      } else {
         switch (type.getSort()) {
            case 0:
               return null;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
               return BasicValue.INT_VALUE;
            case 6:
               return BasicValue.FLOAT_VALUE;
            case 7:
               return BasicValue.LONG_VALUE;
            case 8:
               return BasicValue.DOUBLE_VALUE;
            case 9:
            case 10:
               return BasicValue.REFERENCE_VALUE;
            default:
               throw new Error("Internal error");
         }
      }
   }

   public BasicValue newOperation(AbstractInsnNode insn) throws AnalyzerException {
      switch (insn.getOpcode()) {
         case 1:
            return this.newValue(Type.getObjectType("null"));
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
            return BasicValue.INT_VALUE;
         case 9:
         case 10:
            return BasicValue.LONG_VALUE;
         case 11:
         case 12:
         case 13:
            return BasicValue.FLOAT_VALUE;
         case 14:
         case 15:
            return BasicValue.DOUBLE_VALUE;
         case 16:
         case 17:
            return BasicValue.INT_VALUE;
         case 18:
            Object cst = ((LdcInsnNode)insn).cst;
            if (cst instanceof Integer) {
               return BasicValue.INT_VALUE;
            } else if (cst instanceof Float) {
               return BasicValue.FLOAT_VALUE;
            } else if (cst instanceof Long) {
               return BasicValue.LONG_VALUE;
            } else if (cst instanceof Double) {
               return BasicValue.DOUBLE_VALUE;
            } else if (cst instanceof String) {
               return this.newValue(Type.getObjectType("java/lang/String"));
            } else if (cst instanceof Type) {
               int sort = ((Type)cst).getSort();
               if (sort != 10 && sort != 9) {
                  if (sort == 11) {
                     return this.newValue(Type.getObjectType("java/lang/invoke/MethodType"));
                  }

                  throw new IllegalArgumentException("Illegal LDC constant " + cst);
               }

               return this.newValue(Type.getObjectType("java/lang/Class"));
            } else {
               if (cst instanceof Handle) {
                  return this.newValue(Type.getObjectType("java/lang/invoke/MethodHandle"));
               }

               throw new IllegalArgumentException("Illegal LDC constant " + cst);
            }
         case 168:
            return BasicValue.RETURNADDRESS_VALUE;
         case 178:
            return this.newValue(Type.getType(((FieldInsnNode)insn).desc));
         case 187:
            return this.newValue(Type.getObjectType(((TypeInsnNode)insn).desc));
         default:
            throw new Error("Internal error.");
      }
   }

   public BasicValue copyOperation(AbstractInsnNode insn, BasicValue value) throws AnalyzerException {
      return value;
   }

   public BasicValue unaryOperation(AbstractInsnNode insn, BasicValue value) throws AnalyzerException {
      String desc;
      switch (insn.getOpcode()) {
         case 116:
         case 132:
         case 136:
         case 139:
         case 142:
         case 145:
         case 146:
         case 147:
            return BasicValue.INT_VALUE;
         case 117:
         case 133:
         case 140:
         case 143:
            return BasicValue.LONG_VALUE;
         case 118:
         case 134:
         case 137:
         case 144:
            return BasicValue.FLOAT_VALUE;
         case 119:
         case 135:
         case 138:
         case 141:
            return BasicValue.DOUBLE_VALUE;
         case 120:
         case 121:
         case 122:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
         case 128:
         case 129:
         case 130:
         case 131:
         case 148:
         case 149:
         case 150:
         case 151:
         case 152:
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
         case 167:
         case 168:
         case 169:
         case 177:
         case 178:
         case 181:
         case 182:
         case 183:
         case 184:
         case 185:
         case 186:
         case 187:
         case 196:
         case 197:
         default:
            throw new Error("Internal error.");
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
         case 170:
         case 171:
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
         case 179:
            return null;
         case 180:
            return this.newValue(Type.getType(((FieldInsnNode)insn).desc));
         case 188:
            switch (((IntInsnNode)insn).operand) {
               case 4:
                  return this.newValue(Type.getType("[Z"));
               case 5:
                  return this.newValue(Type.getType("[C"));
               case 6:
                  return this.newValue(Type.getType("[F"));
               case 7:
                  return this.newValue(Type.getType("[D"));
               case 8:
                  return this.newValue(Type.getType("[B"));
               case 9:
                  return this.newValue(Type.getType("[S"));
               case 10:
                  return this.newValue(Type.getType("[I"));
               case 11:
                  return this.newValue(Type.getType("[J"));
               default:
                  throw new AnalyzerException(insn, "Invalid array type");
            }
         case 189:
            desc = ((TypeInsnNode)insn).desc;
            return this.newValue(Type.getType("[" + Type.getObjectType(desc)));
         case 190:
            return BasicValue.INT_VALUE;
         case 191:
            return null;
         case 192:
            desc = ((TypeInsnNode)insn).desc;
            return this.newValue(Type.getObjectType(desc));
         case 193:
            return BasicValue.INT_VALUE;
         case 194:
         case 195:
         case 198:
         case 199:
            return null;
      }
   }

   public BasicValue binaryOperation(AbstractInsnNode insn, BasicValue value1, BasicValue value2) throws AnalyzerException {
      switch (insn.getOpcode()) {
         case 46:
         case 51:
         case 52:
         case 53:
         case 96:
         case 100:
         case 104:
         case 108:
         case 112:
         case 120:
         case 122:
         case 124:
         case 126:
         case 128:
         case 130:
            return BasicValue.INT_VALUE;
         case 47:
         case 97:
         case 101:
         case 105:
         case 109:
         case 113:
         case 121:
         case 123:
         case 125:
         case 127:
         case 129:
         case 131:
            return BasicValue.LONG_VALUE;
         case 48:
         case 98:
         case 102:
         case 106:
         case 110:
         case 114:
            return BasicValue.FLOAT_VALUE;
         case 49:
         case 99:
         case 103:
         case 107:
         case 111:
         case 115:
            return BasicValue.DOUBLE_VALUE;
         case 50:
            return BasicValue.REFERENCE_VALUE;
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 116:
         case 117:
         case 118:
         case 119:
         case 132:
         case 133:
         case 134:
         case 135:
         case 136:
         case 137:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 145:
         case 146:
         case 147:
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
         case 167:
         case 168:
         case 169:
         case 170:
         case 171:
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
         case 177:
         case 178:
         case 179:
         case 180:
         default:
            throw new Error("Internal error.");
         case 148:
         case 149:
         case 150:
         case 151:
         case 152:
            return BasicValue.INT_VALUE;
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
         case 181:
            return null;
      }
   }

   public BasicValue ternaryOperation(AbstractInsnNode insn, BasicValue value1, BasicValue value2, BasicValue value3) throws AnalyzerException {
      return null;
   }

   public BasicValue naryOperation(AbstractInsnNode insn, List values) throws AnalyzerException {
      int opcode = insn.getOpcode();
      if (opcode == 197) {
         return this.newValue(Type.getType(((MultiANewArrayInsnNode)insn).desc));
      } else {
         return opcode == 186 ? this.newValue(Type.getReturnType(((InvokeDynamicInsnNode)insn).desc)) : this.newValue(Type.getReturnType(((MethodInsnNode)insn).desc));
      }
   }

   public void returnOperation(AbstractInsnNode insn, BasicValue value, BasicValue expected) throws AnalyzerException {
   }

   public BasicValue merge(BasicValue v, BasicValue w) {
      return !v.equals(w) ? BasicValue.UNINITIALIZED_VALUE : v;
   }
}
