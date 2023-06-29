package org.spongepowered.asm.lib.tree;

import java.util.Iterator;
import java.util.List;
import org.spongepowered.asm.lib.MethodVisitor;

public class TryCatchBlockNode {
   public LabelNode start;
   public LabelNode end;
   public LabelNode handler;
   public String type;
   public List visibleTypeAnnotations;
   public List invisibleTypeAnnotations;

   public TryCatchBlockNode(LabelNode start, LabelNode end, LabelNode handler, String type) {
      this.start = start;
      this.end = end;
      this.handler = handler;
      this.type = type;
   }

   public void updateIndex(int index) {
      int newTypeRef = 1107296256 | index << 8;
      Iterator var3;
      TypeAnnotationNode tan;
      if (this.visibleTypeAnnotations != null) {
         for(var3 = this.visibleTypeAnnotations.iterator(); var3.hasNext(); tan.typeRef = newTypeRef) {
            tan = (TypeAnnotationNode)var3.next();
         }
      }

      if (this.invisibleTypeAnnotations != null) {
         for(var3 = this.invisibleTypeAnnotations.iterator(); var3.hasNext(); tan.typeRef = newTypeRef) {
            tan = (TypeAnnotationNode)var3.next();
         }
      }

   }

   public void accept(MethodVisitor mv) {
      mv.visitTryCatchBlock(this.start.getLabel(), this.end.getLabel(), this.handler == null ? null : this.handler.getLabel(), this.type);
      int n = this.visibleTypeAnnotations == null ? 0 : this.visibleTypeAnnotations.size();

      int i;
      TypeAnnotationNode an;
      for(i = 0; i < n; ++i) {
         an = (TypeAnnotationNode)this.visibleTypeAnnotations.get(i);
         an.accept(mv.visitTryCatchAnnotation(an.typeRef, an.typePath, an.desc, true));
      }

      n = this.invisibleTypeAnnotations == null ? 0 : this.invisibleTypeAnnotations.size();

      for(i = 0; i < n; ++i) {
         an = (TypeAnnotationNode)this.invisibleTypeAnnotations.get(i);
         an.accept(mv.visitTryCatchAnnotation(an.typeRef, an.typePath, an.desc, false));
      }

   }
}
