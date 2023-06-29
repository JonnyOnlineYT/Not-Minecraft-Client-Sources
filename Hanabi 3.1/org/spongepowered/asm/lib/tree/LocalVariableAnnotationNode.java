package org.spongepowered.asm.lib.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.TypePath;

public class LocalVariableAnnotationNode extends TypeAnnotationNode {
   public List start;
   public List end;
   public List index;

   public LocalVariableAnnotationNode(int typeRef, TypePath typePath, LabelNode[] start, LabelNode[] end, int[] index, String desc) {
      this(327680, typeRef, typePath, start, end, index, desc);
   }

   public LocalVariableAnnotationNode(int api, int typeRef, TypePath typePath, LabelNode[] start, LabelNode[] end, int[] index, String desc) {
      super(api, typeRef, typePath, desc);
      this.start = new ArrayList(start.length);
      this.start.addAll(Arrays.asList(start));
      this.end = new ArrayList(end.length);
      this.end.addAll(Arrays.asList(end));
      this.index = new ArrayList(index.length);
      int[] var8 = index;
      int var9 = index.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         int i = var8[var10];
         this.index.add(i);
      }

   }

   public void accept(MethodVisitor mv, boolean visible) {
      Label[] start = new Label[this.start.size()];
      Label[] end = new Label[this.end.size()];
      int[] index = new int[this.index.size()];

      for(int i = 0; i < start.length; ++i) {
         start[i] = ((LabelNode)this.start.get(i)).getLabel();
         end[i] = ((LabelNode)this.end.get(i)).getLabel();
         index[i] = (Integer)this.index.get(i);
      }

      this.accept(mv.visitLocalVariableAnnotation(this.typeRef, this.typePath, start, end, index, this.desc, true));
   }
}
