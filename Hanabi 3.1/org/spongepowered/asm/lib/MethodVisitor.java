package org.spongepowered.asm.lib;

public abstract class MethodVisitor {
   protected final int api;
   protected MethodVisitor mv;

   public MethodVisitor(int api) {
      this(api, (MethodVisitor)null);
   }

   public MethodVisitor(int api, MethodVisitor mv) {
      if (api != 262144 && api != 327680) {
         throw new IllegalArgumentException();
      } else {
         this.api = api;
         this.mv = mv;
      }
   }

   public void visitParameter(String name, int access) {
      if (this.api < 327680) {
         throw new RuntimeException();
      } else {
         if (this.mv != null) {
            this.mv.visitParameter(name, access);
         }

      }
   }

   public AnnotationVisitor visitAnnotationDefault() {
      return this.mv != null ? this.mv.visitAnnotationDefault() : null;
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      return this.mv != null ? this.mv.visitAnnotation(desc, visible) : null;
   }

   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
      if (this.api < 327680) {
         throw new RuntimeException();
      } else {
         return this.mv != null ? this.mv.visitTypeAnnotation(typeRef, typePath, desc, visible) : null;
      }
   }

   public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
      return this.mv != null ? this.mv.visitParameterAnnotation(parameter, desc, visible) : null;
   }

   public void visitAttribute(Attribute attr) {
      if (this.mv != null) {
         this.mv.visitAttribute(attr);
      }

   }

   public void visitCode() {
      if (this.mv != null) {
         this.mv.visitCode();
      }

   }

   public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
      if (this.mv != null) {
         this.mv.visitFrame(type, nLocal, local, nStack, stack);
      }

   }

   public void visitInsn(int opcode) {
      if (this.mv != null) {
         this.mv.visitInsn(opcode);
      }

   }

   public void visitIntInsn(int opcode, int operand) {
      if (this.mv != null) {
         this.mv.visitIntInsn(opcode, operand);
      }

   }

   public void visitVarInsn(int opcode, int var) {
      if (this.mv != null) {
         this.mv.visitVarInsn(opcode, var);
      }

   }

   public void visitTypeInsn(int opcode, String type) {
      if (this.mv != null) {
         this.mv.visitTypeInsn(opcode, type);
      }

   }

   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
      if (this.mv != null) {
         this.mv.visitFieldInsn(opcode, owner, name, desc);
      }

   }

   /** @deprecated */
   @Deprecated
   public void visitMethodInsn(int opcode, String owner, String name, String desc) {
      if (this.api >= 327680) {
         boolean itf = opcode == 185;
         this.visitMethodInsn(opcode, owner, name, desc, itf);
      } else {
         if (this.mv != null) {
            this.mv.visitMethodInsn(opcode, owner, name, desc);
         }

      }
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      if (this.api < 327680) {
         if (itf != (opcode == 185)) {
            throw new IllegalArgumentException("INVOKESPECIAL/STATIC on interfaces require ASM 5");
         } else {
            this.visitMethodInsn(opcode, owner, name, desc);
         }
      } else {
         if (this.mv != null) {
            this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
         }

      }
   }

   public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
      if (this.mv != null) {
         this.mv.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
      }

   }

   public void visitJumpInsn(int opcode, Label label) {
      if (this.mv != null) {
         this.mv.visitJumpInsn(opcode, label);
      }

   }

   public void visitLabel(Label label) {
      if (this.mv != null) {
         this.mv.visitLabel(label);
      }

   }

   public void visitLdcInsn(Object cst) {
      if (this.mv != null) {
         this.mv.visitLdcInsn(cst);
      }

   }

   public void visitIincInsn(int var, int increment) {
      if (this.mv != null) {
         this.mv.visitIincInsn(var, increment);
      }

   }

   public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
      if (this.mv != null) {
         this.mv.visitTableSwitchInsn(min, max, dflt, labels);
      }

   }

   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
      if (this.mv != null) {
         this.mv.visitLookupSwitchInsn(dflt, keys, labels);
      }

   }

   public void visitMultiANewArrayInsn(String desc, int dims) {
      if (this.mv != null) {
         this.mv.visitMultiANewArrayInsn(desc, dims);
      }

   }

   public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
      if (this.api < 327680) {
         throw new RuntimeException();
      } else {
         return this.mv != null ? this.mv.visitInsnAnnotation(typeRef, typePath, desc, visible) : null;
      }
   }

   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
      if (this.mv != null) {
         this.mv.visitTryCatchBlock(start, end, handler, type);
      }

   }

   public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
      if (this.api < 327680) {
         throw new RuntimeException();
      } else {
         return this.mv != null ? this.mv.visitTryCatchAnnotation(typeRef, typePath, desc, visible) : null;
      }
   }

   public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
      if (this.mv != null) {
         this.mv.visitLocalVariable(name, desc, signature, start, end, index);
      }

   }

   public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible) {
      if (this.api < 327680) {
         throw new RuntimeException();
      } else {
         return this.mv != null ? this.mv.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible) : null;
      }
   }

   public void visitLineNumber(int line, Label start) {
      if (this.mv != null) {
         this.mv.visitLineNumber(line, start);
      }

   }

   public void visitMaxs(int maxStack, int maxLocals) {
      if (this.mv != null) {
         this.mv.visitMaxs(maxStack, maxLocals);
      }

   }

   public void visitEnd() {
      if (this.mv != null) {
         this.mv.visitEnd();
      }

   }
}
