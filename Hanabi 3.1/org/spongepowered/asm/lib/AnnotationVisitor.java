package org.spongepowered.asm.lib;

public abstract class AnnotationVisitor {
   protected final int api;
   protected AnnotationVisitor av;

   public AnnotationVisitor(int api) {
      this(api, (AnnotationVisitor)null);
   }

   public AnnotationVisitor(int api, AnnotationVisitor av) {
      if (api != 262144 && api != 327680) {
         throw new IllegalArgumentException();
      } else {
         this.api = api;
         this.av = av;
      }
   }

   public void visit(String name, Object value) {
      if (this.av != null) {
         this.av.visit(name, value);
      }

   }

   public void visitEnum(String name, String desc, String value) {
      if (this.av != null) {
         this.av.visitEnum(name, desc, value);
      }

   }

   public AnnotationVisitor visitAnnotation(String name, String desc) {
      return this.av != null ? this.av.visitAnnotation(name, desc) : null;
   }

   public AnnotationVisitor visitArray(String name) {
      return this.av != null ? this.av.visitArray(name) : null;
   }

   public void visitEnd() {
      if (this.av != null) {
         this.av.visitEnd();
      }

   }
}
