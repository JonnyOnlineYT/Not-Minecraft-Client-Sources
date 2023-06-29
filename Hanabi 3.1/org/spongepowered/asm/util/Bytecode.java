package org.spongepowered.asm.util;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.IntInsnNode;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.lib.tree.LineNumberNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.util.CheckClassAdapter;
import org.spongepowered.asm.lib.util.TraceClassVisitor;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.util.throwables.SyntheticBridgeException;

public final class Bytecode {
   public static final int[] CONSTANTS_INT = new int[]{2, 3, 4, 5, 6, 7, 8};
   public static final int[] CONSTANTS_FLOAT = new int[]{11, 12, 13};
   public static final int[] CONSTANTS_DOUBLE = new int[]{14, 15};
   public static final int[] CONSTANTS_LONG = new int[]{9, 10};
   public static final int[] CONSTANTS_ALL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
   private static final Object[] CONSTANTS_VALUES = new Object[]{null, -1, 0, 1, 2, 3, 4, 5, 0L, 1L, 0.0F, 1.0F, 2.0F, 0.0, 1.0};
   private static final String[] CONSTANTS_TYPES = new String[]{null, "I", "I", "I", "I", "I", "I", "I", "J", "J", "F", "F", "F", "D", "D", "I", "I"};
   private static final String[] BOXING_TYPES = new String[]{null, "java/lang/Boolean", "java/lang/Character", "java/lang/Byte", "java/lang/Short", "java/lang/Integer", "java/lang/Float", "java/lang/Long", "java/lang/Double", null, null, null};
   private static final String[] UNBOXING_METHODS = new String[]{null, "booleanValue", "charValue", "byteValue", "shortValue", "intValue", "floatValue", "longValue", "doubleValue", null, null, null};
   private static final Class[] MERGEABLE_MIXIN_ANNOTATIONS = new Class[]{Overwrite.class, Intrinsic.class, Final.class, Debug.class};
   private static Pattern mergeableAnnotationPattern = getMergeableAnnotationPattern();
   private static final Logger logger = LogManager.getLogger("mixin");

   private Bytecode() {
   }

   public static MethodNode findMethod(ClassNode classNode, String name, String desc) {
      Iterator var3 = classNode.methods.iterator();

      MethodNode method;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         method = (MethodNode)var3.next();
      } while(!method.name.equals(name) || !method.desc.equals(desc));

      return method;
   }

   public static AbstractInsnNode findInsn(MethodNode method, int opcode) {
      Iterator findReturnIter = method.instructions.iterator();

      AbstractInsnNode insn;
      do {
         if (!findReturnIter.hasNext()) {
            return null;
         }

         insn = (AbstractInsnNode)findReturnIter.next();
      } while(insn.getOpcode() != opcode);

      return insn;
   }

   public static MethodInsnNode findSuperInit(MethodNode method, String superName) {
      if (!"<init>".equals(method.name)) {
         return null;
      } else {
         int news = 0;
         Iterator iter = method.instructions.iterator();

         while(true) {
            while(iter.hasNext()) {
               AbstractInsnNode insn = (AbstractInsnNode)iter.next();
               if (insn instanceof TypeInsnNode && insn.getOpcode() == 187) {
                  ++news;
               } else if (insn instanceof MethodInsnNode && insn.getOpcode() == 183) {
                  MethodInsnNode methodNode = (MethodInsnNode)insn;
                  if ("<init>".equals(methodNode.name)) {
                     if (news > 0) {
                        --news;
                     } else if (methodNode.owner.equals(superName)) {
                        return methodNode;
                     }
                  }
               }
            }

            return null;
         }
      }
   }

   public static void textify(ClassNode classNode, OutputStream out) {
      classNode.accept(new TraceClassVisitor(new PrintWriter(out)));
   }

   public static void textify(MethodNode methodNode, OutputStream out) {
      TraceClassVisitor trace = new TraceClassVisitor(new PrintWriter(out));
      MethodVisitor mv = trace.visitMethod(methodNode.access, methodNode.name, methodNode.desc, methodNode.signature, (String[])methodNode.exceptions.toArray(new String[0]));
      methodNode.accept(mv);
      trace.visitEnd();
   }

   public static void dumpClass(ClassNode classNode) {
      ClassWriter cw = new ClassWriter(3);
      classNode.accept(cw);
      dumpClass(cw.toByteArray());
   }

   public static void dumpClass(byte[] bytes) {
      ClassReader cr = new ClassReader(bytes);
      CheckClassAdapter.verify(cr, true, new PrintWriter(System.out));
   }

   public static void printMethodWithOpcodeIndices(MethodNode method) {
      System.err.printf("%s%s\n", method.name, method.desc);
      int i = 0;
      Iterator iter = method.instructions.iterator();

      while(iter.hasNext()) {
         System.err.printf("[%4d] %s\n", i++, describeNode((AbstractInsnNode)iter.next()));
      }

   }

   public static void printMethod(MethodNode method) {
      System.err.printf("%s%s\n", method.name, method.desc);
      Iterator iter = method.instructions.iterator();

      while(iter.hasNext()) {
         System.err.print("  ");
         printNode((AbstractInsnNode)iter.next());
      }

   }

   public static void printNode(AbstractInsnNode node) {
      System.err.printf("%s\n", describeNode(node));
   }

   public static String describeNode(AbstractInsnNode node) {
      if (node == null) {
         return String.format("   %-14s ", "null");
      } else if (node instanceof LabelNode) {
         return String.format("[%s]", ((LabelNode)node).getLabel());
      } else {
         String out = String.format("   %-14s ", node.getClass().getSimpleName().replace("Node", ""));
         if (node instanceof JumpInsnNode) {
            out = out + String.format("[%s] [%s]", getOpcodeName(node), ((JumpInsnNode)node).label.getLabel());
         } else if (node instanceof VarInsnNode) {
            out = out + String.format("[%s] %d", getOpcodeName(node), ((VarInsnNode)node).var);
         } else if (node instanceof MethodInsnNode) {
            MethodInsnNode mth = (MethodInsnNode)node;
            out = out + String.format("[%s] %s %s %s", getOpcodeName(node), mth.owner, mth.name, mth.desc);
         } else if (node instanceof FieldInsnNode) {
            FieldInsnNode fld = (FieldInsnNode)node;
            out = out + String.format("[%s] %s %s %s", getOpcodeName(node), fld.owner, fld.name, fld.desc);
         } else if (node instanceof LineNumberNode) {
            LineNumberNode ln = (LineNumberNode)node;
            out = out + String.format("LINE=[%d] LABEL=[%s]", ln.line, ln.start.getLabel());
         } else if (node instanceof LdcInsnNode) {
            out = out + ((LdcInsnNode)node).cst;
         } else if (node instanceof IntInsnNode) {
            out = out + ((IntInsnNode)node).operand;
         } else if (node instanceof FrameNode) {
            out = out + String.format("[%s] ", getOpcodeName(((FrameNode)node).type, "H_INVOKEINTERFACE", -1));
         } else {
            out = out + String.format("[%s] ", getOpcodeName(node));
         }

         return out;
      }
   }

   public static String getOpcodeName(AbstractInsnNode node) {
      return node != null ? getOpcodeName(node.getOpcode()) : "";
   }

   public static String getOpcodeName(int opcode) {
      return getOpcodeName(opcode, "UNINITIALIZED_THIS", 1);
   }

   private static String getOpcodeName(int opcode, String start, int min) {
      if (opcode >= min) {
         boolean found = false;

         try {
            Field[] var4 = Opcodes.class.getDeclaredFields();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Field f = var4[var6];
               if (found || f.getName().equals(start)) {
                  found = true;
                  if (f.getType() == Integer.TYPE && f.getInt((Object)null) == opcode) {
                     return f.getName();
                  }
               }
            }
         } catch (Exception var8) {
         }
      }

      return opcode >= 0 ? String.valueOf(opcode) : "UNKNOWN";
   }

   public static boolean methodHasLineNumbers(MethodNode method) {
      Iterator iter = method.instructions.iterator();

      do {
         if (!iter.hasNext()) {
            return false;
         }
      } while(!(iter.next() instanceof LineNumberNode));

      return true;
   }

   public static boolean methodIsStatic(MethodNode method) {
      return (method.access & 8) == 8;
   }

   public static boolean fieldIsStatic(FieldNode field) {
      return (field.access & 8) == 8;
   }

   public static int getFirstNonArgLocalIndex(MethodNode method) {
      return getFirstNonArgLocalIndex(Type.getArgumentTypes(method.desc), (method.access & 8) == 0);
   }

   public static int getFirstNonArgLocalIndex(Type[] args, boolean includeThis) {
      return getArgsSize(args) + (includeThis ? 1 : 0);
   }

   public static int getArgsSize(Type[] args) {
      int size = 0;
      Type[] var2 = args;
      int var3 = args.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Type type = var2[var4];
         size += type.getSize();
      }

      return size;
   }

   public static void loadArgs(Type[] args, InsnList insns, int pos) {
      loadArgs(args, insns, pos, -1);
   }

   public static void loadArgs(Type[] args, InsnList insns, int start, int end) {
      loadArgs(args, insns, start, end, (Type[])null);
   }

   public static void loadArgs(Type[] args, InsnList insns, int start, int end, Type[] casts) {
      int pos = start;
      int index = 0;
      Type[] var7 = args;
      int var8 = args.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Type type = var7[var9];
         insns.add((AbstractInsnNode)(new VarInsnNode(type.getOpcode(21), pos)));
         if (casts != null && index < casts.length && casts[index] != null) {
            insns.add((AbstractInsnNode)(new TypeInsnNode(192, casts[index].getInternalName())));
         }

         pos += type.getSize();
         if (end >= start && pos >= end) {
            return;
         }

         ++index;
      }

   }

   public static Map cloneLabels(InsnList source) {
      Map labels = new HashMap();
      Iterator iter = source.iterator();

      while(iter.hasNext()) {
         AbstractInsnNode insn = (AbstractInsnNode)iter.next();
         if (insn instanceof LabelNode) {
            labels.put((LabelNode)insn, new LabelNode(((LabelNode)insn).getLabel()));
         }
      }

      return labels;
   }

   public static String generateDescriptor(Object returnType, Object... args) {
      StringBuilder sb = (new StringBuilder()).append('(');
      Object[] var3 = args;
      int var4 = args.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object arg = var3[var5];
         sb.append(toDescriptor(arg));
      }

      return sb.append(')').append(returnType != null ? toDescriptor(returnType) : "V").toString();
   }

   private static String toDescriptor(Object arg) {
      if (arg instanceof String) {
         return (String)arg;
      } else if (arg instanceof Type) {
         return arg.toString();
      } else if (arg instanceof Class) {
         return Type.getDescriptor((Class)arg);
      } else {
         return arg == null ? "" : arg.toString();
      }
   }

   public static String getDescriptor(Type[] args) {
      return "(" + Joiner.on("").join(args) + ")";
   }

   public static String getDescriptor(Type[] args, Type returnType) {
      return getDescriptor(args) + returnType.toString();
   }

   public static String changeDescriptorReturnType(String desc, String returnType) {
      if (desc == null) {
         return null;
      } else {
         return returnType == null ? desc : desc.substring(0, desc.lastIndexOf(41) + 1) + returnType;
      }
   }

   public static String getSimpleName(Class annotationType) {
      return annotationType.getSimpleName();
   }

   public static String getSimpleName(AnnotationNode annotation) {
      return getSimpleName(annotation.desc);
   }

   public static String getSimpleName(String desc) {
      int pos = Math.max(desc.lastIndexOf(47), 0);
      return desc.substring(pos + 1).replace(";", "");
   }

   public static boolean isConstant(AbstractInsnNode insn) {
      return insn == null ? false : Ints.contains(CONSTANTS_ALL, insn.getOpcode());
   }

   public static Object getConstant(AbstractInsnNode insn) {
      if (insn == null) {
         return null;
      } else if (insn instanceof LdcInsnNode) {
         return ((LdcInsnNode)insn).cst;
      } else {
         int value;
         if (insn instanceof IntInsnNode) {
            value = ((IntInsnNode)insn).operand;
            if (insn.getOpcode() != 16 && insn.getOpcode() != 17) {
               throw new IllegalArgumentException("IntInsnNode with invalid opcode " + insn.getOpcode() + " in getConstant");
            } else {
               return value;
            }
         } else {
            value = Ints.indexOf(CONSTANTS_ALL, insn.getOpcode());
            return value < 0 ? null : CONSTANTS_VALUES[value];
         }
      }
   }

   public static Type getConstantType(AbstractInsnNode insn) {
      if (insn == null) {
         return null;
      } else if (insn instanceof LdcInsnNode) {
         Object cst = ((LdcInsnNode)insn).cst;
         if (cst instanceof Integer) {
            return Type.getType("I");
         } else if (cst instanceof Float) {
            return Type.getType("F");
         } else if (cst instanceof Long) {
            return Type.getType("J");
         } else if (cst instanceof Double) {
            return Type.getType("D");
         } else if (cst instanceof String) {
            return Type.getType("Ljava/lang/String;");
         } else if (cst instanceof Type) {
            return Type.getType("Ljava/lang/Class;");
         } else {
            throw new IllegalArgumentException("LdcInsnNode with invalid payload type " + cst.getClass() + " in getConstant");
         }
      } else {
         int index = Ints.indexOf(CONSTANTS_ALL, insn.getOpcode());
         return index < 0 ? null : Type.getType(CONSTANTS_TYPES[index]);
      }
   }

   public static boolean hasFlag(ClassNode classNode, int flag) {
      return (classNode.access & flag) == flag;
   }

   public static boolean hasFlag(MethodNode method, int flag) {
      return (method.access & flag) == flag;
   }

   public static boolean hasFlag(FieldNode field, int flag) {
      return (field.access & flag) == flag;
   }

   public static boolean compareFlags(MethodNode m1, MethodNode m2, int flag) {
      return hasFlag(m1, flag) == hasFlag(m2, flag);
   }

   public static boolean compareFlags(FieldNode f1, FieldNode f2, int flag) {
      return hasFlag(f1, flag) == hasFlag(f2, flag);
   }

   public static Visibility getVisibility(MethodNode method) {
      return getVisibility(method.access & 7);
   }

   public static Visibility getVisibility(FieldNode field) {
      return getVisibility(field.access & 7);
   }

   private static Visibility getVisibility(int flags) {
      if ((flags & 4) != 0) {
         return Bytecode.Visibility.PROTECTED;
      } else if ((flags & 2) != 0) {
         return Bytecode.Visibility.PRIVATE;
      } else {
         return (flags & 1) != 0 ? Bytecode.Visibility.PUBLIC : Bytecode.Visibility.PACKAGE;
      }
   }

   public static void setVisibility(MethodNode method, Visibility visibility) {
      method.access = setVisibility(method.access, visibility.access);
   }

   public static void setVisibility(FieldNode field, Visibility visibility) {
      field.access = setVisibility(field.access, visibility.access);
   }

   public static void setVisibility(MethodNode method, int access) {
      method.access = setVisibility(method.access, access);
   }

   public static void setVisibility(FieldNode field, int access) {
      field.access = setVisibility(field.access, access);
   }

   private static int setVisibility(int oldAccess, int newAccess) {
      return oldAccess & -8 | newAccess & 7;
   }

   public static int getMaxLineNumber(ClassNode classNode, int min, int pad) {
      int max = 0;
      Iterator var4 = classNode.methods.iterator();

      while(var4.hasNext()) {
         MethodNode method = (MethodNode)var4.next();
         Iterator iter = method.instructions.iterator();

         while(iter.hasNext()) {
            AbstractInsnNode insn = (AbstractInsnNode)iter.next();
            if (insn instanceof LineNumberNode) {
               max = Math.max(max, ((LineNumberNode)insn).line);
            }
         }
      }

      return Math.max(min, max + pad);
   }

   public static String getBoxingType(Type type) {
      return type == null ? null : BOXING_TYPES[type.getSort()];
   }

   public static String getUnboxingMethod(Type type) {
      return type == null ? null : UNBOXING_METHODS[type.getSort()];
   }

   public static void mergeAnnotations(ClassNode from, ClassNode to) {
      to.visibleAnnotations = mergeAnnotations(from.visibleAnnotations, to.visibleAnnotations, "class", from.name);
      to.invisibleAnnotations = mergeAnnotations(from.invisibleAnnotations, to.invisibleAnnotations, "class", from.name);
   }

   public static void mergeAnnotations(MethodNode from, MethodNode to) {
      to.visibleAnnotations = mergeAnnotations(from.visibleAnnotations, to.visibleAnnotations, "method", from.name);
      to.invisibleAnnotations = mergeAnnotations(from.invisibleAnnotations, to.invisibleAnnotations, "method", from.name);
   }

   public static void mergeAnnotations(FieldNode from, FieldNode to) {
      to.visibleAnnotations = mergeAnnotations(from.visibleAnnotations, to.visibleAnnotations, "field", from.name);
      to.invisibleAnnotations = mergeAnnotations(from.invisibleAnnotations, to.invisibleAnnotations, "field", from.name);
   }

   private static List mergeAnnotations(List from, List to, String type, String name) {
      try {
         if (from == null) {
            return (List)to;
         } else {
            if (to == null) {
               to = new ArrayList();
            }

            Iterator var4 = from.iterator();

            while(true) {
               AnnotationNode annotation;
               do {
                  if (!var4.hasNext()) {
                     return (List)to;
                  }

                  annotation = (AnnotationNode)var4.next();
               } while(!isMergeableAnnotation(annotation));

               Iterator iter = ((List)to).iterator();

               while(iter.hasNext()) {
                  if (((AnnotationNode)iter.next()).desc.equals(annotation.desc)) {
                     iter.remove();
                     break;
                  }
               }

               ((List)to).add(annotation);
            }
         }
      } catch (Exception var7) {
         logger.warn("Exception encountered whilst merging annotations for {} {}", new Object[]{type, name});
         return (List)to;
      }
   }

   private static boolean isMergeableAnnotation(AnnotationNode annotation) {
      return annotation.desc.startsWith("L" + Constants.MIXIN_PACKAGE_REF) ? mergeableAnnotationPattern.matcher(annotation.desc).matches() : true;
   }

   private static Pattern getMergeableAnnotationPattern() {
      StringBuilder sb = new StringBuilder("^L(");

      for(int i = 0; i < MERGEABLE_MIXIN_ANNOTATIONS.length; ++i) {
         if (i > 0) {
            sb.append('|');
         }

         sb.append(MERGEABLE_MIXIN_ANNOTATIONS[i].getName().replace('.', '/'));
      }

      return Pattern.compile(sb.append(");$").toString());
   }

   public static void compareBridgeMethods(MethodNode a, MethodNode b) {
      ListIterator ia = a.instructions.iterator();
      ListIterator ib = b.instructions.iterator();

      int index;
      for(index = 0; ia.hasNext() && ib.hasNext(); ++index) {
         AbstractInsnNode na = (AbstractInsnNode)ia.next();
         AbstractInsnNode nb = (AbstractInsnNode)ib.next();
         if (!(na instanceof LabelNode)) {
            if (na instanceof MethodInsnNode) {
               MethodInsnNode ma = (MethodInsnNode)na;
               MethodInsnNode mb = (MethodInsnNode)nb;
               if (!ma.name.equals(mb.name)) {
                  throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_INVOKE_NAME, a.name, a.desc, index, na, nb);
               }

               if (!ma.desc.equals(mb.desc)) {
                  throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_INVOKE_DESC, a.name, a.desc, index, na, nb);
               }
            } else {
               if (na.getOpcode() != nb.getOpcode()) {
                  throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_INSN, a.name, a.desc, index, na, nb);
               }

               if (na instanceof VarInsnNode) {
                  VarInsnNode va = (VarInsnNode)na;
                  VarInsnNode vb = (VarInsnNode)nb;
                  if (va.var != vb.var) {
                     throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_LOAD, a.name, a.desc, index, na, nb);
                  }
               } else if (na instanceof TypeInsnNode) {
                  TypeInsnNode ta = (TypeInsnNode)na;
                  TypeInsnNode tb = (TypeInsnNode)nb;
                  if (ta.getOpcode() == 192 && !ta.desc.equals(tb.desc)) {
                     throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_CAST, a.name, a.desc, index, na, nb);
                  }
               }
            }
         }
      }

      if (ia.hasNext() || ib.hasNext()) {
         throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_LENGTH, a.name, a.desc, index, (AbstractInsnNode)null, (AbstractInsnNode)null);
      }
   }

   public static enum Visibility {
      PRIVATE(2),
      PROTECTED(4),
      PACKAGE(0),
      PUBLIC(1);

      static final int MASK = 7;
      final int access;

      private Visibility(int access) {
         this.access = access;
      }
   }
}
