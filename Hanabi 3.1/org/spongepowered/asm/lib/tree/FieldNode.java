package org.spongepowered.asm.lib.tree;

import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.Attribute;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.FieldVisitor;
import org.spongepowered.asm.lib.TypePath;

public class FieldNode extends FieldVisitor {
   public int access;
   public String name;
   public String desc;
   public String signature;
   public Object value;
   public List visibleAnnotations;
   public List invisibleAnnotations;
   public List visibleTypeAnnotations;
   public List invisibleTypeAnnotations;
   public List attrs;

   public FieldNode(int access, String name, String desc, String signature, Object value) {
      this(327680, access, name, desc, signature, value);
      if (this.getClass() != FieldNode.class) {
         throw new IllegalStateException();
      }
   }

   public FieldNode(int api, int access, String name, String desc, String signature, Object value) {
      super(api);
      this.access = access;
      this.name = name;
      this.desc = desc;
      this.signature = signature;
      this.value = value;
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      AnnotationNode an = new AnnotationNode(desc);
      if (visible) {
         if (this.visibleAnnotations == null) {
            this.visibleAnnotations = new ArrayList(1);
         }

         this.visibleAnnotations.add(an);
      } else {
         if (this.invisibleAnnotations == null) {
            this.invisibleAnnotations = new ArrayList(1);
         }

         this.invisibleAnnotations.add(an);
      }

      return an;
   }

   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
      TypeAnnotationNode an = new TypeAnnotationNode(typeRef, typePath, desc);
      if (visible) {
         if (this.visibleTypeAnnotations == null) {
            this.visibleTypeAnnotations = new ArrayList(1);
         }

         this.visibleTypeAnnotations.add(an);
      } else {
         if (this.invisibleTypeAnnotations == null) {
            this.invisibleTypeAnnotations = new ArrayList(1);
         }

         this.invisibleTypeAnnotations.add(an);
      }

      return an;
   }

   public void visitAttribute(Attribute attr) {
      if (this.attrs == null) {
         this.attrs = new ArrayList(1);
      }

      this.attrs.add(attr);
   }

   public void visitEnd() {
   }

   public void check(int api) {
      if (api == 262144) {
         if (this.visibleTypeAnnotations != null && this.visibleTypeAnnotations.size() > 0) {
            throw new RuntimeException();
         }

         if (this.invisibleTypeAnnotations != null && this.invisibleTypeAnnotations.size() > 0) {
            throw new RuntimeException();
         }
      }

   }

   public void accept(ClassVisitor cv) {
      FieldVisitor fv = cv.visitField(this.access, this.name, this.desc, this.signature, this.value);
      if (fv != null) {
         int n = this.visibleAnnotations == null ? 0 : this.visibleAnnotations.size();

         int i;
         AnnotationNode an;
         for(i = 0; i < n; ++i) {
            an = (AnnotationNode)this.visibleAnnotations.get(i);
            an.accept(fv.visitAnnotation(an.desc, true));
         }

         n = this.invisibleAnnotations == null ? 0 : this.invisibleAnnotations.size();

         for(i = 0; i < n; ++i) {
            an = (AnnotationNode)this.invisibleAnnotations.get(i);
            an.accept(fv.visitAnnotation(an.desc, false));
         }

         n = this.visibleTypeAnnotations == null ? 0 : this.visibleTypeAnnotations.size();

         TypeAnnotationNode an;
         for(i = 0; i < n; ++i) {
            an = (TypeAnnotationNode)this.visibleTypeAnnotations.get(i);
            an.accept(fv.visitTypeAnnotation(an.typeRef, an.typePath, an.desc, true));
         }

         n = this.invisibleTypeAnnotations == null ? 0 : this.invisibleTypeAnnotations.size();

         for(i = 0; i < n; ++i) {
            an = (TypeAnnotationNode)this.invisibleTypeAnnotations.get(i);
            an.accept(fv.visitTypeAnnotation(an.typeRef, an.typePath, an.desc, false));
         }

         n = this.attrs == null ? 0 : this.attrs.size();

         for(i = 0; i < n; ++i) {
            fv.visitAttribute((Attribute)this.attrs.get(i));
         }

         fv.visitEnd();
      }
   }
}
