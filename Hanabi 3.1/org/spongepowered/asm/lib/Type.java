package org.spongepowered.asm.lib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Type {
   public static final int VOID = 0;
   public static final int BOOLEAN = 1;
   public static final int CHAR = 2;
   public static final int BYTE = 3;
   public static final int SHORT = 4;
   public static final int INT = 5;
   public static final int FLOAT = 6;
   public static final int LONG = 7;
   public static final int DOUBLE = 8;
   public static final int ARRAY = 9;
   public static final int OBJECT = 10;
   public static final int METHOD = 11;
   public static final Type VOID_TYPE = new Type(0, (char[])null, 1443168256, 1);
   public static final Type BOOLEAN_TYPE = new Type(1, (char[])null, 1509950721, 1);
   public static final Type CHAR_TYPE = new Type(2, (char[])null, 1124075009, 1);
   public static final Type BYTE_TYPE = new Type(3, (char[])null, 1107297537, 1);
   public static final Type SHORT_TYPE = new Type(4, (char[])null, 1392510721, 1);
   public static final Type INT_TYPE = new Type(5, (char[])null, 1224736769, 1);
   public static final Type FLOAT_TYPE = new Type(6, (char[])null, 1174536705, 1);
   public static final Type LONG_TYPE = new Type(7, (char[])null, 1241579778, 1);
   public static final Type DOUBLE_TYPE = new Type(8, (char[])null, 1141048066, 1);
   private final int sort;
   private final char[] buf;
   private final int off;
   private final int len;

   private Type(int sort, char[] buf, int off, int len) {
      this.sort = sort;
      this.buf = buf;
      this.off = off;
      this.len = len;
   }

   public static Type getType(String typeDescriptor) {
      return getType(typeDescriptor.toCharArray(), 0);
   }

   public static Type getObjectType(String internalName) {
      char[] buf = internalName.toCharArray();
      return new Type(buf[0] == '[' ? 9 : 10, buf, 0, buf.length);
   }

   public static Type getMethodType(String methodDescriptor) {
      return getType(methodDescriptor.toCharArray(), 0);
   }

   public static Type getMethodType(Type returnType, Type... argumentTypes) {
      return getType(getMethodDescriptor(returnType, argumentTypes));
   }

   public static Type getType(Class c) {
      if (c.isPrimitive()) {
         if (c == Integer.TYPE) {
            return INT_TYPE;
         } else if (c == Void.TYPE) {
            return VOID_TYPE;
         } else if (c == Boolean.TYPE) {
            return BOOLEAN_TYPE;
         } else if (c == Byte.TYPE) {
            return BYTE_TYPE;
         } else if (c == Character.TYPE) {
            return CHAR_TYPE;
         } else if (c == Short.TYPE) {
            return SHORT_TYPE;
         } else if (c == Double.TYPE) {
            return DOUBLE_TYPE;
         } else {
            return c == Float.TYPE ? FLOAT_TYPE : LONG_TYPE;
         }
      } else {
         return getType(getDescriptor(c));
      }
   }

   public static Type getType(Constructor c) {
      return getType(getConstructorDescriptor(c));
   }

   public static Type getType(Method m) {
      return getType(getMethodDescriptor(m));
   }

   public static Type[] getArgumentTypes(String methodDescriptor) {
      char[] buf = methodDescriptor.toCharArray();
      int off = 1;
      int size = 0;

      while(true) {
         while(true) {
            char car = buf[off++];
            if (car == ')') {
               Type[] args = new Type[size];
               off = 1;

               for(size = 0; buf[off] != ')'; ++size) {
                  args[size] = getType(buf, off);
                  off += args[size].len + (args[size].sort == 10 ? 2 : 0);
               }

               return args;
            }

            if (car == 'L') {
               while(buf[off++] != ';') {
               }

               ++size;
            } else if (car != '[') {
               ++size;
            }
         }
      }
   }

   public static Type[] getArgumentTypes(Method method) {
      Class[] classes = method.getParameterTypes();
      Type[] types = new Type[classes.length];

      for(int i = classes.length - 1; i >= 0; --i) {
         types[i] = getType(classes[i]);
      }

      return types;
   }

   public static Type getReturnType(String methodDescriptor) {
      char[] buf = methodDescriptor.toCharArray();
      int off = 1;

      while(true) {
         char car;
         do {
            car = buf[off++];
            if (car == ')') {
               return getType(buf, off);
            }
         } while(car != 'L');

         while(true) {
            if (buf[off++] != ';') {
               continue;
            }
         }
      }
   }

   public static Type getReturnType(Method method) {
      return getType(method.getReturnType());
   }

   public static int getArgumentsAndReturnSizes(String desc) {
      int n = 1;
      int c = 1;

      while(true) {
         while(true) {
            char car = desc.charAt(c++);
            if (car == ')') {
               car = desc.charAt(c);
               return n << 2 | (car == 'V' ? 0 : (car != 'D' && car != 'J' ? 1 : 2));
            }

            if (car == 'L') {
               while(desc.charAt(c++) != ';') {
               }

               ++n;
            } else if (car != '[') {
               if (car != 'D' && car != 'J') {
                  ++n;
               } else {
                  n += 2;
               }
            } else {
               while((car = desc.charAt(c)) == '[') {
                  ++c;
               }

               if (car == 'D' || car == 'J') {
                  --n;
               }
            }
         }
      }
   }

   private static Type getType(char[] buf, int off) {
      int len;
      switch (buf[off]) {
         case 'B':
            return BYTE_TYPE;
         case 'C':
            return CHAR_TYPE;
         case 'D':
            return DOUBLE_TYPE;
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
            return new Type(11, buf, off, buf.length - off);
         case 'F':
            return FLOAT_TYPE;
         case 'I':
            return INT_TYPE;
         case 'J':
            return LONG_TYPE;
         case 'L':
            for(len = 1; buf[off + len] != ';'; ++len) {
            }

            return new Type(10, buf, off + 1, len - 1);
         case 'S':
            return SHORT_TYPE;
         case 'V':
            return VOID_TYPE;
         case 'Z':
            return BOOLEAN_TYPE;
         case '[':
            for(len = 1; buf[off + len] == '['; ++len) {
            }

            if (buf[off + len] == 'L') {
               ++len;

               while(buf[off + len] != ';') {
                  ++len;
               }
            }

            return new Type(9, buf, off, len + 1);
      }
   }

   public int getSort() {
      return this.sort;
   }

   public int getDimensions() {
      int i;
      for(i = 1; this.buf[this.off + i] == '['; ++i) {
      }

      return i;
   }

   public Type getElementType() {
      return getType(this.buf, this.off + this.getDimensions());
   }

   public String getClassName() {
      switch (this.sort) {
         case 0:
            return "void";
         case 1:
            return "boolean";
         case 2:
            return "char";
         case 3:
            return "byte";
         case 4:
            return "short";
         case 5:
            return "int";
         case 6:
            return "float";
         case 7:
            return "long";
         case 8:
            return "double";
         case 9:
            StringBuilder sb = new StringBuilder(this.getElementType().getClassName());

            for(int i = this.getDimensions(); i > 0; --i) {
               sb.append("[]");
            }

            return sb.toString();
         case 10:
            return (new String(this.buf, this.off, this.len)).replace('/', '.');
         default:
            return null;
      }
   }

   public String getInternalName() {
      return new String(this.buf, this.off, this.len);
   }

   public Type[] getArgumentTypes() {
      return getArgumentTypes(this.getDescriptor());
   }

   public Type getReturnType() {
      return getReturnType(this.getDescriptor());
   }

   public int getArgumentsAndReturnSizes() {
      return getArgumentsAndReturnSizes(this.getDescriptor());
   }

   public String getDescriptor() {
      StringBuilder buf = new StringBuilder();
      this.getDescriptor(buf);
      return buf.toString();
   }

   public static String getMethodDescriptor(Type returnType, Type... argumentTypes) {
      StringBuilder buf = new StringBuilder();
      buf.append('(');

      for(int i = 0; i < argumentTypes.length; ++i) {
         argumentTypes[i].getDescriptor(buf);
      }

      buf.append(')');
      returnType.getDescriptor(buf);
      return buf.toString();
   }

   private void getDescriptor(StringBuilder buf) {
      if (this.buf == null) {
         buf.append((char)((this.off & -16777216) >>> 24));
      } else if (this.sort == 10) {
         buf.append('L');
         buf.append(this.buf, this.off, this.len);
         buf.append(';');
      } else {
         buf.append(this.buf, this.off, this.len);
      }

   }

   public static String getInternalName(Class c) {
      return c.getName().replace('.', '/');
   }

   public static String getDescriptor(Class c) {
      StringBuilder buf = new StringBuilder();
      getDescriptor(buf, c);
      return buf.toString();
   }

   public static String getConstructorDescriptor(Constructor c) {
      Class[] parameters = c.getParameterTypes();
      StringBuilder buf = new StringBuilder();
      buf.append('(');

      for(int i = 0; i < parameters.length; ++i) {
         getDescriptor(buf, parameters[i]);
      }

      return buf.append(")V").toString();
   }

   public static String getMethodDescriptor(Method m) {
      Class[] parameters = m.getParameterTypes();
      StringBuilder buf = new StringBuilder();
      buf.append('(');

      for(int i = 0; i < parameters.length; ++i) {
         getDescriptor(buf, parameters[i]);
      }

      buf.append(')');
      getDescriptor(buf, m.getReturnType());
      return buf.toString();
   }

   private static void getDescriptor(StringBuilder buf, Class c) {
      Class d;
      for(d = c; !d.isPrimitive(); d = d.getComponentType()) {
         if (!d.isArray()) {
            buf.append('L');
            String name = d.getName();
            int len = name.length();

            for(int i = 0; i < len; ++i) {
               char car = name.charAt(i);
               buf.append(car == '.' ? '/' : car);
            }

            buf.append(';');
            return;
         }

         buf.append('[');
      }

      char car;
      if (d == Integer.TYPE) {
         car = 'I';
      } else if (d == Void.TYPE) {
         car = 'V';
      } else if (d == Boolean.TYPE) {
         car = 'Z';
      } else if (d == Byte.TYPE) {
         car = 'B';
      } else if (d == Character.TYPE) {
         car = 'C';
      } else if (d == Short.TYPE) {
         car = 'S';
      } else if (d == Double.TYPE) {
         car = 'D';
      } else if (d == Float.TYPE) {
         car = 'F';
      } else {
         car = 'J';
      }

      buf.append(car);
   }

   public int getSize() {
      return this.buf == null ? this.off & 255 : 1;
   }

   public int getOpcode(int opcode) {
      return opcode != 46 && opcode != 79 ? opcode + (this.buf == null ? (this.off & 16711680) >> 16 : 4) : opcode + (this.buf == null ? (this.off & '\uff00') >> 8 : 4);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof Type)) {
         return false;
      } else {
         Type t = (Type)o;
         if (this.sort != t.sort) {
            return false;
         } else {
            if (this.sort >= 9) {
               if (this.len != t.len) {
                  return false;
               }

               int i = this.off;
               int j = t.off;

               for(int end = i + this.len; i < end; ++j) {
                  if (this.buf[i] != t.buf[j]) {
                     return false;
                  }

                  ++i;
               }
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int hc = 13 * this.sort;
      if (this.sort >= 9) {
         int i = this.off;

         for(int end = i + this.len; i < end; ++i) {
            hc = 17 * (hc + this.buf[i]);
         }
      }

      return hc;
   }

   public String toString() {
      return this.getDescriptor();
   }
}
