package org.spongepowered.asm.lib.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.MethodVisitor;

public class LookupSwitchInsnNode extends AbstractInsnNode {
   public LabelNode dflt;
   public List keys;
   public List labels;

   public LookupSwitchInsnNode(LabelNode dflt, int[] keys, LabelNode[] labels) {
      super(171);
      this.dflt = dflt;
      this.keys = new ArrayList(keys == null ? 0 : keys.length);
      this.labels = new ArrayList(labels == null ? 0 : labels.length);
      if (keys != null) {
         for(int i = 0; i < keys.length; ++i) {
            this.keys.add(keys[i]);
         }
      }

      if (labels != null) {
         this.labels.addAll(Arrays.asList(labels));
      }

   }

   public int getType() {
      return 12;
   }

   public void accept(MethodVisitor mv) {
      int[] keys = new int[this.keys.size()];

      for(int i = 0; i < keys.length; ++i) {
         keys[i] = (Integer)this.keys.get(i);
      }

      Label[] labels = new Label[this.labels.size()];

      for(int i = 0; i < labels.length; ++i) {
         labels[i] = ((LabelNode)this.labels.get(i)).getLabel();
      }

      mv.visitLookupSwitchInsn(this.dflt.getLabel(), keys, labels);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map labels) {
      LookupSwitchInsnNode clone = new LookupSwitchInsnNode(clone(this.dflt, labels), (int[])null, clone(this.labels, labels));
      clone.keys.addAll(this.keys);
      return clone.cloneAnnotations(this);
   }
}
