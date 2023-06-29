package org.spongepowered.asm.lib.tree.analysis;

import java.util.List;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;

public class BasicVerifier extends BasicInterpreter {
   public BasicVerifier() {
      super(327680);
   }

   protected BasicVerifier(int api) {
      super(api);
   }

   public BasicValue copyOperation(AbstractInsnNode insn, BasicValue value) throws AnalyzerException {
      BasicValue expected;
      switch (insn.getOpcode()) {
         case 21:
         case 54:
            expected = BasicValue.INT_VALUE;
            break;
         case 22:
         case 55:
            expected = BasicValue.LONG_VALUE;
            break;
         case 23:
         case 56:
            expected = BasicValue.FLOAT_VALUE;
            break;
         case 24:
         case 57:
            expected = BasicValue.DOUBLE_VALUE;
            break;
         case 25:
            if (!value.isReference()) {
               throw new AnalyzerException(insn, (String)null, "an object reference", value);
            }

            return value;
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         default:
            return value;
         case 58:
            if (!value.isReference() && !BasicValue.RETURNADDRESS_VALUE.equals(value)) {
               throw new AnalyzerException(insn, (String)null, "an object reference or a return address", value);
            }

            return value;
      }

      if (!expected.equals(value)) {
         throw new AnalyzerException(insn, (String)null, expected, value);
      } else {
         return value;
      }
   }

   public BasicValue unaryOperation(AbstractInsnNode insn, BasicValue value) throws AnalyzerException {
      BasicValue expected;
      switch (insn.getOpcode()) {
         case 116:
         case 132:
         case 133:
         case 134:
         case 135:
         case 145:
         case 146:
         case 147:
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
         case 170:
         case 171:
         case 172:
         case 188:
         case 189:
            expected = BasicValue.INT_VALUE;
            break;
         case 117:
         case 136:
         case 137:
         case 138:
         case 173:
            expected = BasicValue.LONG_VALUE;
            break;
         case 118:
         case 139:
         case 140:
         case 141:
         case 174:
            expected = BasicValue.FLOAT_VALUE;
            break;
         case 119:
         case 142:
         case 143:
         case 144:
         case 175:
            expected = BasicValue.DOUBLE_VALUE;
            break;
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
         case 176:
         case 191:
         case 193:
         case 194:
         case 195:
         case 198:
         case 199:
            if (!value.isReference()) {
               throw new AnalyzerException(insn, (String)null, "an object reference", value);
            }

            return super.unaryOperation(insn, value);
         case 179:
            expected = this.newValue(Type.getType(((FieldInsnNode)insn).desc));
            break;
         case 180:
            expected = this.newValue(Type.getObjectType(((FieldInsnNode)insn).owner));
            break;
         case 190:
            if (!this.isArrayValue(value)) {
               throw new AnalyzerException(insn, (String)null, "an array reference", value);
            }

            return super.unaryOperation(insn, value);
         case 192:
            if (!value.isReference()) {
               throw new AnalyzerException(insn, (String)null, "an object reference", value);
            }

            return super.unaryOperation(insn, value);
      }

      if (!this.isSubTypeOf(value, expected)) {
         throw new AnalyzerException(insn, (String)null, expected, value);
      } else {
         return super.unaryOperation(insn, value);
      }
   }

   public BasicValue binaryOperation(AbstractInsnNode insn, BasicValue value1, BasicValue value2) throws AnalyzerException {
      BasicValue expected1;
      BasicValue expected2;
      switch (insn.getOpcode()) {
         case 46:
            expected1 = this.newValue(Type.getType("[I"));
            expected2 = BasicValue.INT_VALUE;
            break;
         case 47:
            expected1 = this.newValue(Type.getType("[J"));
            expected2 = BasicValue.INT_VALUE;
            break;
         case 48:
            expected1 = this.newValue(Type.getType("[F"));
            expected2 = BasicValue.INT_VALUE;
            break;
         case 49:
            expected1 = this.newValue(Type.getType("[D"));
            expected2 = BasicValue.INT_VALUE;
            break;
         case 50:
            expected1 = this.newValue(Type.getType("[Ljava/lang/Object;"));
            expected2 = BasicValue.INT_VALUE;
            break;
         case 51:
            if (this.isSubTypeOf(value1, this.newValue(Type.getType("[Z")))) {
               expected1 = this.newValue(Type.getType("[Z"));
            } else {
               expected1 = this.newValue(Type.getType("[B"));
            }

            expected2 = BasicValue.INT_VALUE;
            break;
         case 52:
            expected1 = this.newValue(Type.getType("[C"));
            expected2 = BasicValue.INT_VALUE;
            break;
         case 53:
            expected1 = this.newValue(Type.getType("[S"));
            expected2 = BasicValue.INT_VALUE;
            break;
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
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
            expected1 = BasicValue.INT_VALUE;
            expected2 = BasicValue.INT_VALUE;
            break;
         case 97:
         case 101:
         case 105:
         case 109:
         case 113:
         case 127:
         case 129:
         case 131:
         case 148:
            expected1 = BasicValue.LONG_VALUE;
            expected2 = BasicValue.LONG_VALUE;
            break;
         case 98:
         case 102:
         case 106:
         case 110:
         case 114:
         case 149:
         case 150:
            expected1 = BasicValue.FLOAT_VALUE;
            expected2 = BasicValue.FLOAT_VALUE;
            break;
         case 99:
         case 103:
         case 107:
         case 111:
         case 115:
         case 151:
         case 152:
            expected1 = BasicValue.DOUBLE_VALUE;
            expected2 = BasicValue.DOUBLE_VALUE;
            break;
         case 121:
         case 123:
         case 125:
            expected1 = BasicValue.LONG_VALUE;
            expected2 = BasicValue.INT_VALUE;
            break;
         case 165:
         case 166:
            expected1 = BasicValue.REFERENCE_VALUE;
            expected2 = BasicValue.REFERENCE_VALUE;
            break;
         case 181:
            FieldInsnNode fin = (FieldInsnNode)insn;
            expected1 = this.newValue(Type.getObjectType(fin.owner));
            expected2 = this.newValue(Type.getType(fin.desc));
      }

      if (!this.isSubTypeOf(value1, expected1)) {
         throw new AnalyzerException(insn, "First argument", expected1, value1);
      } else if (!this.isSubTypeOf(value2, expected2)) {
         throw new AnalyzerException(insn, "Second argument", expected2, value2);
      } else {
         return insn.getOpcode() == 50 ? this.getElementValue(value1) : super.binaryOperation(insn, value1, value2);
      }
   }

   public BasicValue ternaryOperation(AbstractInsnNode insn, BasicValue value1, BasicValue value2, BasicValue value3) throws AnalyzerException {
      BasicValue expected1;
      BasicValue expected3;
      switch (insn.getOpcode()) {
         case 79:
            expected1 = this.newValue(Type.getType("[I"));
            expected3 = BasicValue.INT_VALUE;
            break;
         case 80:
            expected1 = this.newValue(Type.getType("[J"));
            expected3 = BasicValue.LONG_VALUE;
            break;
         case 81:
            expected1 = this.newValue(Type.getType("[F"));
            expected3 = BasicValue.FLOAT_VALUE;
            break;
         case 82:
            expected1 = this.newValue(Type.getType("[D"));
            expected3 = BasicValue.DOUBLE_VALUE;
            break;
         case 83:
            expected1 = value1;
            expected3 = BasicValue.REFERENCE_VALUE;
            break;
         case 84:
            if (this.isSubTypeOf(value1, this.newValue(Type.getType("[Z")))) {
               expected1 = this.newValue(Type.getType("[Z"));
            } else {
               expected1 = this.newValue(Type.getType("[B"));
            }

            expected3 = BasicValue.INT_VALUE;
            break;
         case 85:
            expected1 = this.newValue(Type.getType("[C"));
            expected3 = BasicValue.INT_VALUE;
            break;
         case 86:
            expected1 = this.newValue(Type.getType("[S"));
            expected3 = BasicValue.INT_VALUE;
            break;
         default:
            throw new Error("Internal error.");
      }

      if (!this.isSubTypeOf(value1, expected1)) {
         throw new AnalyzerException(insn, "First argument", "a " + expected1 + " array reference", value1);
      } else if (!BasicValue.INT_VALUE.equals(value2)) {
         throw new AnalyzerException(insn, "Second argument", BasicValue.INT_VALUE, value2);
      } else if (!this.isSubTypeOf(value3, expected3)) {
         throw new AnalyzerException(insn, "Third argument", expected3, value3);
      } else {
         return null;
      }
   }

   public BasicValue naryOperation(AbstractInsnNode insn, List values) throws AnalyzerException {
      int opcode = insn.getOpcode();
      int i;
      if (opcode == 197) {
         for(i = 0; i < values.size(); ++i) {
            if (!BasicValue.INT_VALUE.equals(values.get(i))) {
               throw new AnalyzerException(insn, (String)null, BasicValue.INT_VALUE, (Value)values.get(i));
            }
         }
      } else {
         i = 0;
         int j = 0;
         if (opcode != 184 && opcode != 186) {
            Type owner = Type.getObjectType(((MethodInsnNode)insn).owner);
            if (!this.isSubTypeOf((BasicValue)values.get(i++), this.newValue(owner))) {
               throw new AnalyzerException(insn, "Method owner", this.newValue(owner), (Value)values.get(0));
            }
         }

         String desc = opcode == 186 ? ((InvokeDynamicInsnNode)insn).desc : ((MethodInsnNode)insn).desc;
         Type[] args = Type.getArgumentTypes(desc);

         while(i < values.size()) {
            BasicValue expected = this.newValue(args[j++]);
            BasicValue encountered = (BasicValue)values.get(i++);
            if (!this.isSubTypeOf(encountered, expected)) {
               throw new AnalyzerException(insn, "Argument " + j, expected, encountered);
            }
         }
      }

      return super.naryOperation(insn, values);
   }

   public void returnOperation(AbstractInsnNode insn, BasicValue value, BasicValue expected) throws AnalyzerException {
      if (!this.isSubTypeOf(value, expected)) {
         throw new AnalyzerException(insn, "Incompatible return type", expected, value);
      }
   }

   protected boolean isArrayValue(BasicValue value) {
      return value.isReference();
   }

   protected BasicValue getElementValue(BasicValue objectArrayValue) throws AnalyzerException {
      return BasicValue.REFERENCE_VALUE;
   }

   protected boolean isSubTypeOf(BasicValue value, BasicValue expected) {
      return value.equals(expected);
   }
}
