package org.spongepowered.asm.lib.tree.analysis;

import java.util.ArrayList;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.IincInsnNode;
import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.MultiANewArrayInsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;

public class Frame {
   private Value returnValue;
   private Value[] values;
   private int locals;
   private int top;

   public Frame(int nLocals, int nStack) {
      this.values = (Value[])(new Value[nLocals + nStack]);
      this.locals = nLocals;
   }

   public Frame(Frame src) {
      this(src.locals, src.values.length - src.locals);
      this.init(src);
   }

   public Frame init(Frame src) {
      this.returnValue = src.returnValue;
      System.arraycopy(src.values, 0, this.values, 0, this.values.length);
      this.top = src.top;
      return this;
   }

   public void setReturn(Value v) {
      this.returnValue = v;
   }

   public int getLocals() {
      return this.locals;
   }

   public int getMaxStackSize() {
      return this.values.length - this.locals;
   }

   public Value getLocal(int i) throws IndexOutOfBoundsException {
      if (i >= this.locals) {
         throw new IndexOutOfBoundsException("Trying to access an inexistant local variable");
      } else {
         return this.values[i];
      }
   }

   public void setLocal(int i, Value value) throws IndexOutOfBoundsException {
      if (i >= this.locals) {
         throw new IndexOutOfBoundsException("Trying to access an inexistant local variable " + i);
      } else {
         this.values[i] = value;
      }
   }

   public int getStackSize() {
      return this.top;
   }

   public Value getStack(int i) throws IndexOutOfBoundsException {
      return this.values[i + this.locals];
   }

   public void clearStack() {
      this.top = 0;
   }

   public Value pop() throws IndexOutOfBoundsException {
      if (this.top == 0) {
         throw new IndexOutOfBoundsException("Cannot pop operand off an empty stack.");
      } else {
         return this.values[--this.top + this.locals];
      }
   }

   public void push(Value value) throws IndexOutOfBoundsException {
      if (this.top + this.locals >= this.values.length) {
         throw new IndexOutOfBoundsException("Insufficient maximum stack size.");
      } else {
         this.values[this.top++ + this.locals] = value;
      }
   }

   public void execute(AbstractInsnNode insn, Interpreter interpreter) throws AnalyzerException {
      Value value2;
      Value value1;
      int var;
      Value value3;
      ArrayList values;
      int i;
      String desc;
      switch (insn.getOpcode()) {
         case 0:
         case 167:
         case 169:
            break;
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
            this.push(interpreter.newOperation(insn));
            break;
         case 19:
         case 20:
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
         case 196:
         default:
            throw new RuntimeException("Illegal opcode " + insn.getOpcode());
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
            this.push(interpreter.copyOperation(insn, this.getLocal(((VarInsnNode)insn).var)));
            break;
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
            value2 = this.pop();
            value1 = this.pop();
            this.push(interpreter.binaryOperation(insn, value1, value2));
            break;
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
            value1 = interpreter.copyOperation(insn, this.pop());
            var = ((VarInsnNode)insn).var;
            this.setLocal(var, value1);
            if (value1.getSize() == 2) {
               this.setLocal(var + 1, interpreter.newValue((Type)null));
            }

            if (var > 0) {
               Value local = this.getLocal(var - 1);
               if (local != null && local.getSize() == 2) {
                  this.setLocal(var - 1, interpreter.newValue((Type)null));
               }
            }
            break;
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
            value3 = this.pop();
            value2 = this.pop();
            value1 = this.pop();
            interpreter.ternaryOperation(insn, value1, value2, value3);
            break;
         case 87:
            if (this.pop().getSize() == 2) {
               throw new AnalyzerException(insn, "Illegal use of POP");
            }
            break;
         case 88:
            if (this.pop().getSize() == 1 && this.pop().getSize() != 1) {
               throw new AnalyzerException(insn, "Illegal use of POP2");
            }
            break;
         case 89:
            value1 = this.pop();
            if (value1.getSize() != 1) {
               throw new AnalyzerException(insn, "Illegal use of DUP");
            }

            this.push(value1);
            this.push(interpreter.copyOperation(insn, value1));
            break;
         case 90:
            value1 = this.pop();
            value2 = this.pop();
            if (value1.getSize() == 1 && value2.getSize() == 1) {
               this.push(interpreter.copyOperation(insn, value1));
               this.push(value2);
               this.push(value1);
               break;
            }

            throw new AnalyzerException(insn, "Illegal use of DUP_X1");
         case 91:
            value1 = this.pop();
            if (value1.getSize() == 1) {
               value2 = this.pop();
               if (value2.getSize() != 1) {
                  this.push(interpreter.copyOperation(insn, value1));
                  this.push(value2);
                  this.push(value1);
                  break;
               }

               value3 = this.pop();
               if (value3.getSize() == 1) {
                  this.push(interpreter.copyOperation(insn, value1));
                  this.push(value3);
                  this.push(value2);
                  this.push(value1);
                  break;
               }
            }

            throw new AnalyzerException(insn, "Illegal use of DUP_X2");
         case 92:
            value1 = this.pop();
            if (value1.getSize() == 1) {
               value2 = this.pop();
               if (value2.getSize() != 1) {
                  throw new AnalyzerException(insn, "Illegal use of DUP2");
               }

               this.push(value2);
               this.push(value1);
               this.push(interpreter.copyOperation(insn, value2));
               this.push(interpreter.copyOperation(insn, value1));
            } else {
               this.push(value1);
               this.push(interpreter.copyOperation(insn, value1));
            }
            break;
         case 93:
            value1 = this.pop();
            if (value1.getSize() == 1) {
               value2 = this.pop();
               if (value2.getSize() == 1) {
                  value3 = this.pop();
                  if (value3.getSize() == 1) {
                     this.push(interpreter.copyOperation(insn, value2));
                     this.push(interpreter.copyOperation(insn, value1));
                     this.push(value3);
                     this.push(value2);
                     this.push(value1);
                     break;
                  }
               }
            } else {
               value2 = this.pop();
               if (value2.getSize() == 1) {
                  this.push(interpreter.copyOperation(insn, value1));
                  this.push(value2);
                  this.push(value1);
                  break;
               }
            }

            throw new AnalyzerException(insn, "Illegal use of DUP2_X1");
         case 94:
            value1 = this.pop();
            if (value1.getSize() == 1) {
               value2 = this.pop();
               if (value2.getSize() != 1) {
                  throw new AnalyzerException(insn, "Illegal use of DUP2_X2");
               }

               value3 = this.pop();
               if (value3.getSize() == 1) {
                  Value value4 = this.pop();
                  if (value4.getSize() != 1) {
                     throw new AnalyzerException(insn, "Illegal use of DUP2_X2");
                  }

                  this.push(interpreter.copyOperation(insn, value2));
                  this.push(interpreter.copyOperation(insn, value1));
                  this.push(value4);
                  this.push(value3);
                  this.push(value2);
                  this.push(value1);
               } else {
                  this.push(interpreter.copyOperation(insn, value2));
                  this.push(interpreter.copyOperation(insn, value1));
                  this.push(value3);
                  this.push(value2);
                  this.push(value1);
               }
            } else {
               value2 = this.pop();
               if (value2.getSize() == 1) {
                  value3 = this.pop();
                  if (value3.getSize() != 1) {
                     throw new AnalyzerException(insn, "Illegal use of DUP2_X2");
                  }

                  this.push(interpreter.copyOperation(insn, value1));
                  this.push(value3);
                  this.push(value2);
                  this.push(value1);
               } else {
                  this.push(interpreter.copyOperation(insn, value1));
                  this.push(value2);
                  this.push(value1);
               }
            }
            break;
         case 95:
            value2 = this.pop();
            value1 = this.pop();
            if (value1.getSize() == 1 && value2.getSize() == 1) {
               this.push(interpreter.copyOperation(insn, value2));
               this.push(interpreter.copyOperation(insn, value1));
               break;
            }

            throw new AnalyzerException(insn, "Illegal use of SWAP");
         case 96:
         case 97:
         case 98:
         case 99:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 109:
         case 110:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
            value2 = this.pop();
            value1 = this.pop();
            this.push(interpreter.binaryOperation(insn, value1, value2));
            break;
         case 116:
         case 117:
         case 118:
         case 119:
            this.push(interpreter.unaryOperation(insn, this.pop()));
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
            value2 = this.pop();
            value1 = this.pop();
            this.push(interpreter.binaryOperation(insn, value1, value2));
            break;
         case 132:
            var = ((IincInsnNode)insn).var;
            this.setLocal(var, interpreter.unaryOperation(insn, this.getLocal(var)));
            break;
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
            this.push(interpreter.unaryOperation(insn, this.pop()));
            break;
         case 148:
         case 149:
         case 150:
         case 151:
         case 152:
            value2 = this.pop();
            value1 = this.pop();
            this.push(interpreter.binaryOperation(insn, value1, value2));
            break;
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
            interpreter.unaryOperation(insn, this.pop());
            break;
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
            value2 = this.pop();
            value1 = this.pop();
            interpreter.binaryOperation(insn, value1, value2);
            break;
         case 168:
            this.push(interpreter.newOperation(insn));
            break;
         case 170:
         case 171:
            interpreter.unaryOperation(insn, this.pop());
            break;
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
            value1 = this.pop();
            interpreter.unaryOperation(insn, value1);
            interpreter.returnOperation(insn, value1, this.returnValue);
            break;
         case 177:
            if (this.returnValue != null) {
               throw new AnalyzerException(insn, "Incompatible return type");
            }
            break;
         case 178:
            this.push(interpreter.newOperation(insn));
            break;
         case 179:
            interpreter.unaryOperation(insn, this.pop());
            break;
         case 180:
            this.push(interpreter.unaryOperation(insn, this.pop()));
            break;
         case 181:
            value2 = this.pop();
            value1 = this.pop();
            interpreter.binaryOperation(insn, value1, value2);
            break;
         case 182:
         case 183:
         case 184:
         case 185:
            values = new ArrayList();
            desc = ((MethodInsnNode)insn).desc;

            for(i = Type.getArgumentTypes(desc).length; i > 0; --i) {
               values.add(0, this.pop());
            }

            if (insn.getOpcode() != 184) {
               values.add(0, this.pop());
            }

            if (Type.getReturnType(desc) == Type.VOID_TYPE) {
               interpreter.naryOperation(insn, values);
            } else {
               this.push(interpreter.naryOperation(insn, values));
            }
            break;
         case 186:
            values = new ArrayList();
            desc = ((InvokeDynamicInsnNode)insn).desc;

            for(i = Type.getArgumentTypes(desc).length; i > 0; --i) {
               values.add(0, this.pop());
            }

            if (Type.getReturnType(desc) == Type.VOID_TYPE) {
               interpreter.naryOperation(insn, values);
            } else {
               this.push(interpreter.naryOperation(insn, values));
            }
            break;
         case 187:
            this.push(interpreter.newOperation(insn));
            break;
         case 188:
         case 189:
         case 190:
            this.push(interpreter.unaryOperation(insn, this.pop()));
            break;
         case 191:
            interpreter.unaryOperation(insn, this.pop());
            break;
         case 192:
         case 193:
            this.push(interpreter.unaryOperation(insn, this.pop()));
            break;
         case 194:
         case 195:
            interpreter.unaryOperation(insn, this.pop());
            break;
         case 197:
            values = new ArrayList();

            for(int i = ((MultiANewArrayInsnNode)insn).dims; i > 0; --i) {
               values.add(0, this.pop());
            }

            this.push(interpreter.naryOperation(insn, values));
            break;
         case 198:
         case 199:
            interpreter.unaryOperation(insn, this.pop());
      }

   }

   public boolean merge(Frame frame, Interpreter interpreter) throws AnalyzerException {
      if (this.top != frame.top) {
         throw new AnalyzerException((AbstractInsnNode)null, "Incompatible stack heights");
      } else {
         boolean changes = false;

         for(int i = 0; i < this.locals + this.top; ++i) {
            Value v = interpreter.merge(this.values[i], frame.values[i]);
            if (!v.equals(this.values[i])) {
               this.values[i] = v;
               changes = true;
            }
         }

         return changes;
      }
   }

   public boolean merge(Frame frame, boolean[] access) {
      boolean changes = false;

      for(int i = 0; i < this.locals; ++i) {
         if (!access[i] && !this.values[i].equals(frame.values[i])) {
            this.values[i] = frame.values[i];
            changes = true;
         }
      }

      return changes;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();

      int i;
      for(i = 0; i < this.getLocals(); ++i) {
         sb.append(this.getLocal(i));
      }

      sb.append(' ');

      for(i = 0; i < this.getStackSize(); ++i) {
         sb.append(this.getStack(i).toString());
      }

      return sb.toString();
   }
}
