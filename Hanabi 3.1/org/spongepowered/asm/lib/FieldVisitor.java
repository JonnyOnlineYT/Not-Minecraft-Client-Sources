package org.spongepowered.asm.lib;

public abstract class FieldVisitor {
   protected final int api;
   protected FieldVisitor fv;

   public FieldVisitor(int api) {
      this(api, (FieldVisitor)null);
   }

   public FieldVisitor(int api, FieldVisitor fv) {
      if (api != 262144 && api != 327680) {
         throw new IllegalArgumentException();
      } else {
         this.api = api;
         this.fv = fv;
      }
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      return this.fv != null ? this.fv.visitAnnotation(desc, visible) : null;
   }

   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
      if (this.api < 327680) {
         throw new RuntimeException();
      } else {
         return this.fv != null ? this.fv.visitTypeAnnotation(typeRef, typePath, desc, visible) : null;
      }
   }

   public void visitAttribute(Attribute attr) {
      if (this.fv != null) {
         this.fv.visitAttribute(attr);
      }

   }

   public void visitEnd() {
      if (this.fv != null) {
         this.fv.visitEnd();
      }

   }
}
