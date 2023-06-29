package org.spongepowered.asm.lib;

public abstract class ClassVisitor {
   protected final int api;
   protected ClassVisitor cv;

   public ClassVisitor(int api) {
      this(api, (ClassVisitor)null);
   }

   public ClassVisitor(int api, ClassVisitor cv) {
      if (api != 262144 && api != 327680) {
         throw new IllegalArgumentException();
      } else {
         this.api = api;
         this.cv = cv;
      }
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      if (this.cv != null) {
         this.cv.visit(version, access, name, signature, superName, interfaces);
      }

   }

   public void visitSource(String source, String debug) {
      if (this.cv != null) {
         this.cv.visitSource(source, debug);
      }

   }

   public void visitOuterClass(String owner, String name, String desc) {
      if (this.cv != null) {
         this.cv.visitOuterClass(owner, name, desc);
      }

   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      return this.cv != null ? this.cv.visitAnnotation(desc, visible) : null;
   }

   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
      if (this.api < 327680) {
         throw new RuntimeException();
      } else {
         return this.cv != null ? this.cv.visitTypeAnnotation(typeRef, typePath, desc, visible) : null;
      }
   }

   public void visitAttribute(Attribute attr) {
      if (this.cv != null) {
         this.cv.visitAttribute(attr);
      }

   }

   public void visitInnerClass(String name, String outerName, String innerName, int access) {
      if (this.cv != null) {
         this.cv.visitInnerClass(name, outerName, innerName, access);
      }

   }

   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
      return this.cv != null ? this.cv.visitField(access, name, desc, signature, value) : null;
   }

   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      return this.cv != null ? this.cv.visitMethod(access, name, desc, signature, exceptions) : null;
   }

   public void visitEnd() {
      if (this.cv != null) {
         this.cv.visitEnd();
      }

   }
}
