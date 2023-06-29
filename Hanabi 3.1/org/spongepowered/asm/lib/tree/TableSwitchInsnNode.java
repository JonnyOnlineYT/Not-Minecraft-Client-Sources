package org.spongepowered.asm.lib.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.MethodVisitor;

public class TableSwitchInsnNode extends AbstractInsnNode {
   public int min;
   public int max;
   public LabelNode dflt;
   public List labels;

   public TableSwitchInsnNode(int min, int max, LabelNode dflt, LabelNode... labels) {
      super(170);
      this.min = min;
      this.max = max;
      this.dflt = dflt;
      this.labels = new ArrayList();
      if (labels != null) {
         this.labels.addAll(Arrays.asList(labels));
      }

   }

   public int getType() {
      return 11;
   }

   public void accept(MethodVisitor mv) {
      Label[] labels = new Label[this.labels.size()];

      for(int i = 0; i < labels.length; ++i) {
         labels[i] = ((LabelNode)this.labels.get(i)).getLabel();
      }

      mv.visitTableSwitchInsn(this.min, this.max, this.dflt.getLabel(), labels);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map labels) {
      return (new TableSwitchInsnNode(this.min, this.max, clone(this.dflt, labels), clone(this.labels, labels))).cloneAnnotations(this);
   }
}
