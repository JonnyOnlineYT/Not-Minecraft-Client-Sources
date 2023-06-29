package org.spongepowered.asm.lib;

class Frame {
   static final int DIM = -268435456;
   static final int ARRAY_OF = 268435456;
   static final int ELEMENT_OF = -268435456;
   static final int KIND = 251658240;
   static final int TOP_IF_LONG_OR_DOUBLE = 8388608;
   static final int VALUE = 8388607;
   static final int BASE_KIND = 267386880;
   static final int BASE_VALUE = 1048575;
   static final int BASE = 16777216;
   static final int OBJECT = 24117248;
   static final int UNINITIALIZED = 25165824;
   private static final int LOCAL = 33554432;
   private static final int STACK = 50331648;
   static final int TOP = 16777216;
   static final int BOOLEAN = 16777225;
   static final int BYTE = 16777226;
   static final int CHAR = 16777227;
   static final int SHORT = 16777228;
   static final int INTEGER = 16777217;
   static final int FLOAT = 16777218;
   static final int DOUBLE = 16777219;
   static final int LONG = 16777220;
   static final int NULL = 16777221;
   static final int UNINITIALIZED_THIS = 16777222;
   static final int[] SIZE;
   Label owner;
   int[] inputLocals;
   int[] inputStack;
   private int[] outputLocals;
   private int[] outputStack;
   int outputStackTop;
   private int initializationCount;
   private int[] initializations;

   final void set(ClassWriter cw, int nLocal, Object[] local, int nStack, Object[] stack) {
      for(int i = convert(cw, nLocal, local, this.inputLocals); i < local.length; this.inputLocals[i++] = 16777216) {
      }

      int nStackTop = 0;

      for(int j = 0; j < nStack; ++j) {
         if (stack[j] == Opcodes.LONG || stack[j] == Opcodes.DOUBLE) {
            ++nStackTop;
         }
      }

      this.inputStack = new int[nStack + nStackTop];
      convert(cw, nStack, stack, this.inputStack);
      this.outputStackTop = 0;
      this.initializationCount = 0;
   }

   private static int convert(ClassWriter cw, int nInput, Object[] input, int[] output) {
      int i = 0;

      for(int j = 0; j < nInput; ++j) {
         if (input[j] instanceof Integer) {
            output[i++] = 16777216 | (Integer)input[j];
            if (input[j] == Opcodes.LONG || input[j] == Opcodes.DOUBLE) {
               output[i++] = 16777216;
            }
         } else if (input[j] instanceof String) {
            output[i++] = type(cw, Type.getObjectType((String)input[j]).getDescriptor());
         } else {
            output[i++] = 25165824 | cw.addUninitializedType("", ((Label)input[j]).position);
         }
      }

      return i;
   }

   final void set(Frame f) {
      this.inputLocals = f.inputLocals;
      this.inputStack = f.inputStack;
      this.outputLocals = f.outputLocals;
      this.outputStack = f.outputStack;
      this.outputStackTop = f.outputStackTop;
      this.initializationCount = f.initializationCount;
      this.initializations = f.initializations;
   }

   private int get(int local) {
      if (this.outputLocals != null && local < this.outputLocals.length) {
         int type = this.outputLocals[local];
         if (type == 0) {
            type = this.outputLocals[local] = 33554432 | local;
         }

         return type;
      } else {
         return 33554432 | local;
      }
   }

   private void set(int local, int type) {
      if (this.outputLocals == null) {
         this.outputLocals = new int[10];
      }

      int n = this.outputLocals.length;
      if (local >= n) {
         int[] t = new int[Math.max(local + 1, 2 * n)];
         System.arraycopy(this.outputLocals, 0, t, 0, n);
         this.outputLocals = t;
      }

      this.outputLocals[local] = type;
   }

   private void push(int type) {
      if (this.outputStack == null) {
         this.outputStack = new int[10];
      }

      int n = this.outputStack.length;
      if (this.outputStackTop >= n) {
         int[] t = new int[Math.max(this.outputStackTop + 1, 2 * n)];
         System.arraycopy(this.outputStack, 0, t, 0, n);
         this.outputStack = t;
      }

      this.outputStack[this.outputStackTop++] = type;
      int top = this.owner.inputStackTop + this.outputStackTop;
      if (top > this.owner.outputStackMax) {
         this.owner.outputStackMax = top;
      }

   }

   private void push(ClassWriter cw, String desc) {
      int type = type(cw, desc);
      if (type != 0) {
         this.push(type);
         if (type == 16777220 || type == 16777219) {
            this.push(16777216);
         }
      }

   }

   private static int type(ClassWriter cw, String desc) {
      int index = desc.charAt(0) == '(' ? desc.indexOf(41) + 1 : 0;
      String t;
      switch (desc.charAt(index)) {
         case 'B':
         case 'C':
         case 'I':
         case 'S':
         case 'Z':
            return 16777217;
         case 'D':
            return 16777219;
         case 'E':
         case 'G':
         case 'H':
         case 'K':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'T':
         case 'U':
         case 'W':
         case 'X':
         case 'Y':
         default:
            int dims;
            for(dims = index + 1; desc.charAt(dims) == '['; ++dims) {
            }

            int data;
            switch (desc.charAt(dims)) {
               case 'B':
                  data = 16777226;
                  break;
               case 'C':
                  data = 16777227;
                  break;
               case 'D':
                  data = 16777219;
                  break;
               case 'E':
               case 'G':
               case 'H':
               case 'K':
               case 'L':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'R':
               case 'T':
               case 'U':
               case 'V':
               case 'W':
               case 'X':
               case 'Y':
               default:
                  t = desc.substring(dims + 1, desc.length() - 1);
                  data = 24117248 | cw.addType(t);
                  break;
               case 'F':
                  data = 16777218;
                  break;
               case 'I':
                  data = 16777217;
                  break;
               case 'J':
                  data = 16777220;
                  break;
               case 'S':
                  data = 16777228;
                  break;
               case 'Z':
                  data = 16777225;
            }

            return dims - index << 28 | data;
         case 'F':
            return 16777218;
         case 'J':
            return 16777220;
         case 'L':
            t = desc.substring(index + 1, desc.length() - 1);
            return 24117248 | cw.addType(t);
         case 'V':
            return 0;
      }
   }

   private int pop() {
      return this.outputStackTop > 0 ? this.outputStack[--this.outputStackTop] : 50331648 | -(--this.owner.inputStackTop);
   }

   private void pop(int elements) {
      if (this.outputStackTop >= elements) {
         this.outputStackTop -= elements;
      } else {
         Label var10000 = this.owner;
         var10000.inputStackTop -= elements - this.outputStackTop;
         this.outputStackTop = 0;
      }

   }

   private void pop(String desc) {
      char c = desc.charAt(0);
      if (c == '(') {
         this.pop((Type.getArgumentsAndReturnSizes(desc) >> 2) - 1);
      } else if (c != 'J' && c != 'D') {
         this.pop(1);
      } else {
         this.pop(2);
      }

   }

   private void init(int var) {
      if (this.initializations == null) {
         this.initializations = new int[2];
      }

      int n = this.initializations.length;
      if (this.initializationCount >= n) {
         int[] t = new int[Math.max(this.initializationCount + 1, 2 * n)];
         System.arraycopy(this.initializations, 0, t, 0, n);
         this.initializations = t;
      }

      this.initializations[this.initializationCount++] = var;
   }

   private int init(ClassWriter cw, int t) {
      int s;
      if (t == 16777222) {
         s = 24117248 | cw.addType(cw.thisName);
      } else {
         if ((t & -1048576) != 25165824) {
            return t;
         }

         String type = cw.typeTable[t & 1048575].strVal1;
         s = 24117248 | cw.addType(type);
      }

      for(int j = 0; j < this.initializationCount; ++j) {
         int u = this.initializations[j];
         int dim = u & -268435456;
         int kind = u & 251658240;
         if (kind == 33554432) {
            u = dim + this.inputLocals[u & 8388607];
         } else if (kind == 50331648) {
            u = dim + this.inputStack[this.inputStack.length - (u & 8388607)];
         }

         if (t == u) {
            return s;
         }
      }

      return t;
   }

   final void initInputFrame(ClassWriter cw, int access, Type[] args, int maxLocals) {
      this.inputLocals = new int[maxLocals];
      this.inputStack = new int[0];
      int i = 0;
      if ((access & 8) == 0) {
         if ((access & 524288) == 0) {
            this.inputLocals[i++] = 24117248 | cw.addType(cw.thisName);
         } else {
            this.inputLocals[i++] = 16777222;
         }
      }

      for(int j = 0; j < args.length; ++j) {
         int t = type(cw, args[j].getDescriptor());
         this.inputLocals[i++] = t;
         if (t == 16777220 || t == 16777219) {
            this.inputLocals[i++] = 16777216;
         }
      }

      while(i < maxLocals) {
         this.inputLocals[i++] = 16777216;
      }

   }

   void execute(int opcode, int arg, ClassWriter cw, Item item) {
      int t1;
      int t2;
      int t3;
      String s;
      switch (opcode) {
         case 0:
         case 116:
         case 117:
         case 118:
         case 119:
         case 145:
         case 146:
         case 147:
         case 167:
         case 177:
            break;
         case 1:
            this.push(16777221);
            break;
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 16:
         case 17:
         case 21:
            this.push(16777217);
            break;
         case 9:
         case 10:
         case 22:
            this.push(16777220);
            this.push(16777216);
            break;
         case 11:
         case 12:
         case 13:
         case 23:
            this.push(16777218);
            break;
         case 14:
         case 15:
         case 24:
            this.push(16777219);
            this.push(16777216);
            break;
         case 18:
            switch (item.type) {
               case 3:
                  this.push(16777217);
                  return;
               case 4:
                  this.push(16777218);
                  return;
               case 5:
                  this.push(16777220);
                  this.push(16777216);
                  return;
               case 6:
                  this.push(16777219);
                  this.push(16777216);
                  return;
               case 7:
                  this.push(24117248 | cw.addType("java/lang/Class"));
                  return;
               case 8:
                  this.push(24117248 | cw.addType("java/lang/String"));
                  return;
               case 9:
               case 10:
               case 11:
               case 12:
               case 13:
               case 14:
               case 15:
               default:
                  this.push(24117248 | cw.addType("java/lang/invoke/MethodHandle"));
                  return;
               case 16:
                  this.push(24117248 | cw.addType("java/lang/invoke/MethodType"));
                  return;
            }
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
         case 197:
         default:
            this.pop(arg);
            this.push(cw, item.strVal1);
            break;
         case 25:
            this.push(this.get(arg));
            break;
         case 46:
         case 51:
         case 52:
         case 53:
            this.pop(2);
            this.push(16777217);
            break;
         case 47:
         case 143:
            this.pop(2);
            this.push(16777220);
            this.push(16777216);
            break;
         case 48:
            this.pop(2);
            this.push(16777218);
            break;
         case 49:
         case 138:
            this.pop(2);
            this.push(16777219);
            this.push(16777216);
            break;
         case 50:
            this.pop(1);
            t1 = this.pop();
            this.push(-268435456 + t1);
            break;
         case 54:
         case 56:
         case 58:
            t1 = this.pop();
            this.set(arg, t1);
            if (arg > 0) {
               t2 = this.get(arg - 1);
               if (t2 != 16777220 && t2 != 16777219) {
                  if ((t2 & 251658240) != 16777216) {
                     this.set(arg - 1, t2 | 8388608);
                  }
               } else {
                  this.set(arg - 1, 16777216);
               }
            }
            break;
         case 55:
         case 57:
            this.pop(1);
            t1 = this.pop();
            this.set(arg, t1);
            this.set(arg + 1, 16777216);
            if (arg > 0) {
               t2 = this.get(arg - 1);
               if (t2 != 16777220 && t2 != 16777219) {
                  if ((t2 & 251658240) != 16777216) {
                     this.set(arg - 1, t2 | 8388608);
                  }
               } else {
                  this.set(arg - 1, 16777216);
               }
            }
            break;
         case 79:
         case 81:
         case 83:
         case 84:
         case 85:
         case 86:
            this.pop(3);
            break;
         case 80:
         case 82:
            this.pop(4);
            break;
         case 87:
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
         case 170:
         case 171:
         case 172:
         case 174:
         case 176:
         case 191:
         case 194:
         case 195:
         case 198:
         case 199:
            this.pop(1);
            break;
         case 88:
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
         case 173:
         case 175:
            this.pop(2);
            break;
         case 89:
            t1 = this.pop();
            this.push(t1);
            this.push(t1);
            break;
         case 90:
            t1 = this.pop();
            t2 = this.pop();
            this.push(t1);
            this.push(t2);
            this.push(t1);
            break;
         case 91:
            t1 = this.pop();
            t2 = this.pop();
            t3 = this.pop();
            this.push(t1);
            this.push(t3);
            this.push(t2);
            this.push(t1);
            break;
         case 92:
            t1 = this.pop();
            t2 = this.pop();
            this.push(t2);
            this.push(t1);
            this.push(t2);
            this.push(t1);
            break;
         case 93:
            t1 = this.pop();
            t2 = this.pop();
            t3 = this.pop();
            this.push(t2);
            this.push(t1);
            this.push(t3);
            this.push(t2);
            this.push(t1);
            break;
         case 94:
            t1 = this.pop();
            t2 = this.pop();
            t3 = this.pop();
            int t4 = this.pop();
            this.push(t2);
            this.push(t1);
            this.push(t4);
            this.push(t3);
            this.push(t2);
            this.push(t1);
            break;
         case 95:
            t1 = this.pop();
            t2 = this.pop();
            this.push(t1);
            this.push(t2);
            break;
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
         case 136:
         case 142:
         case 149:
         case 150:
            this.pop(2);
            this.push(16777217);
            break;
         case 97:
         case 101:
         case 105:
         case 109:
         case 113:
         case 127:
         case 129:
         case 131:
            this.pop(4);
            this.push(16777220);
            this.push(16777216);
            break;
         case 98:
         case 102:
         case 106:
         case 110:
         case 114:
         case 137:
         case 144:
            this.pop(2);
            this.push(16777218);
            break;
         case 99:
         case 103:
         case 107:
         case 111:
         case 115:
            this.pop(4);
            this.push(16777219);
            this.push(16777216);
            break;
         case 121:
         case 123:
         case 125:
            this.pop(3);
            this.push(16777220);
            this.push(16777216);
            break;
         case 132:
            this.set(arg, 16777217);
            break;
         case 133:
         case 140:
            this.pop(1);
            this.push(16777220);
            this.push(16777216);
            break;
         case 134:
            this.pop(1);
            this.push(16777218);
            break;
         case 135:
         case 141:
            this.pop(1);
            this.push(16777219);
            this.push(16777216);
            break;
         case 139:
         case 190:
         case 193:
            this.pop(1);
            this.push(16777217);
            break;
         case 148:
         case 151:
         case 152:
            this.pop(4);
            this.push(16777217);
            break;
         case 168:
         case 169:
            throw new RuntimeException("JSR/RET are not supported with computeFrames option");
         case 178:
            this.push(cw, item.strVal3);
            break;
         case 179:
            this.pop(item.strVal3);
            break;
         case 180:
            this.pop(1);
            this.push(cw, item.strVal3);
            break;
         case 181:
            this.pop(item.strVal3);
            this.pop();
            break;
         case 182:
         case 183:
         case 184:
         case 185:
            this.pop(item.strVal3);
            if (opcode != 184) {
               t1 = this.pop();
               if (opcode == 183 && item.strVal2.charAt(0) == '<') {
                  this.init(t1);
               }
            }

            this.push(cw, item.strVal3);
            break;
         case 186:
            this.pop(item.strVal2);
            this.push(cw, item.strVal2);
            break;
         case 187:
            this.push(25165824 | cw.addUninitializedType(item.strVal1, arg));
            break;
         case 188:
            this.pop();
            switch (arg) {
               case 4:
                  this.push(285212681);
                  return;
               case 5:
                  this.push(285212683);
                  return;
               case 6:
                  this.push(285212674);
                  return;
               case 7:
                  this.push(285212675);
                  return;
               case 8:
                  this.push(285212682);
                  return;
               case 9:
                  this.push(285212684);
                  return;
               case 10:
                  this.push(285212673);
                  return;
               default:
                  this.push(285212676);
                  return;
            }
         case 189:
            s = item.strVal1;
            this.pop();
            if (s.charAt(0) == '[') {
               this.push(cw, '[' + s);
            } else {
               this.push(292552704 | cw.addType(s));
            }
            break;
         case 192:
            s = item.strVal1;
            this.pop();
            if (s.charAt(0) == '[') {
               this.push(cw, s);
            } else {
               this.push(24117248 | cw.addType(s));
            }
      }

   }

   final boolean merge(ClassWriter cw, Frame frame, int edge) {
      boolean changed = false;
      int nLocal = this.inputLocals.length;
      int nStack = this.inputStack.length;
      if (frame.inputLocals == null) {
         frame.inputLocals = new int[nLocal];
         changed = true;
      }

      int i;
      int s;
      int t;
      int dim;
      int kind;
      for(i = 0; i < nLocal; ++i) {
         if (this.outputLocals != null && i < this.outputLocals.length) {
            s = this.outputLocals[i];
            if (s == 0) {
               t = this.inputLocals[i];
            } else {
               dim = s & -268435456;
               kind = s & 251658240;
               if (kind == 16777216) {
                  t = s;
               } else {
                  if (kind == 33554432) {
                     t = dim + this.inputLocals[s & 8388607];
                  } else {
                     t = dim + this.inputStack[nStack - (s & 8388607)];
                  }

                  if ((s & 8388608) != 0 && (t == 16777220 || t == 16777219)) {
                     t = 16777216;
                  }
               }
            }
         } else {
            t = this.inputLocals[i];
         }

         if (this.initializations != null) {
            t = this.init(cw, t);
         }

         changed |= merge(cw, t, frame.inputLocals, i);
      }

      if (edge > 0) {
         for(i = 0; i < nLocal; ++i) {
            t = this.inputLocals[i];
            changed |= merge(cw, t, frame.inputLocals, i);
         }

         if (frame.inputStack == null) {
            frame.inputStack = new int[1];
            changed = true;
         }

         changed |= merge(cw, edge, frame.inputStack, 0);
         return changed;
      } else {
         int nInputStack = this.inputStack.length + this.owner.inputStackTop;
         if (frame.inputStack == null) {
            frame.inputStack = new int[nInputStack + this.outputStackTop];
            changed = true;
         }

         for(i = 0; i < nInputStack; ++i) {
            t = this.inputStack[i];
            if (this.initializations != null) {
               t = this.init(cw, t);
            }

            changed |= merge(cw, t, frame.inputStack, i);
         }

         for(i = 0; i < this.outputStackTop; ++i) {
            s = this.outputStack[i];
            dim = s & -268435456;
            kind = s & 251658240;
            if (kind == 16777216) {
               t = s;
            } else {
               if (kind == 33554432) {
                  t = dim + this.inputLocals[s & 8388607];
               } else {
                  t = dim + this.inputStack[nStack - (s & 8388607)];
               }

               if ((s & 8388608) != 0 && (t == 16777220 || t == 16777219)) {
                  t = 16777216;
               }
            }

            if (this.initializations != null) {
               t = this.init(cw, t);
            }

            changed |= merge(cw, t, frame.inputStack, nInputStack + i);
         }

         return changed;
      }
   }

   private static boolean merge(ClassWriter cw, int t, int[] types, int index) {
      int u = types[index];
      if (u == t) {
         return false;
      } else {
         if ((t & 268435455) == 16777221) {
            if (u == 16777221) {
               return false;
            }

            t = 16777221;
         }

         if (u == 0) {
            types[index] = t;
            return true;
         } else {
            int v;
            if ((u & 267386880) != 24117248 && (u & -268435456) == 0) {
               if (u == 16777221) {
                  v = (t & 267386880) != 24117248 && (t & -268435456) == 0 ? 16777216 : t;
               } else {
                  v = 16777216;
               }
            } else {
               if (t == 16777221) {
                  return false;
               }

               int vdim;
               if ((t & -1048576) == (u & -1048576)) {
                  if ((u & 267386880) == 24117248) {
                     v = t & -268435456 | 24117248 | cw.getMergedType(t & 1048575, u & 1048575);
                  } else {
                     vdim = -268435456 + (u & -268435456);
                     v = vdim | 24117248 | cw.addType("java/lang/Object");
                  }
               } else if ((t & 267386880) != 24117248 && (t & -268435456) == 0) {
                  v = 16777216;
               } else {
                  vdim = ((t & -268435456) != 0 && (t & 267386880) != 24117248 ? -268435456 : 0) + (t & -268435456);
                  int udim = ((u & -268435456) != 0 && (u & 267386880) != 24117248 ? -268435456 : 0) + (u & -268435456);
                  v = Math.min(vdim, udim) | 24117248 | cw.addType("java/lang/Object");
               }
            }

            if (u != v) {
               types[index] = v;
               return true;
            } else {
               return false;
            }
         }
      }
   }

   static {
      int[] b = new int[202];
      String s = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";

      for(int i = 0; i < b.length; ++i) {
         b[i] = s.charAt(i) - 69;
      }

      SIZE = b;
   }
}
