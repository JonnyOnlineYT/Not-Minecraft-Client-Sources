package org.spongepowered.asm.lib.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class FrameNode extends AbstractInsnNode {
   public int type;
   public List local;
   public List stack;

   private FrameNode() {
      super(-1);
   }

   public FrameNode(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
      super(-1);
      this.type = type;
      switch (type) {
         case -1:
         case 0:
            this.local = asList(nLocal, local);
            this.stack = asList(nStack, stack);
            break;
         case 1:
            this.local = asList(nLocal, local);
            break;
         case 2:
            this.local = Arrays.asList();
         case 3:
         default:
            break;
         case 4:
            this.stack = asList(1, stack);
      }

   }

   public int getType() {
      return 14;
   }

   public void accept(MethodVisitor mv) {
      switch (this.type) {
         case -1:
         case 0:
            mv.visitFrame(this.type, this.local.size(), asArray(this.local), this.stack.size(), asArray(this.stack));
            break;
         case 1:
            mv.visitFrame(this.type, this.local.size(), asArray(this.local), 0, (Object[])null);
            break;
         case 2:
            mv.visitFrame(this.type, this.local.size(), (Object[])null, 0, (Object[])null);
            break;
         case 3:
            mv.visitFrame(this.type, 0, (Object[])null, 0, (Object[])null);
            break;
         case 4:
            mv.visitFrame(this.type, 0, (Object[])null, 1, asArray(this.stack));
      }

   }

   public AbstractInsnNode clone(Map labels) {
      FrameNode clone = new FrameNode();
      clone.type = this.type;
      int i;
      Object s;
      if (this.local != null) {
         clone.local = new ArrayList();

         for(i = 0; i < this.local.size(); ++i) {
            s = this.local.get(i);
            if (s instanceof LabelNode) {
               s = labels.get(s);
            }

            clone.local.add(s);
         }
      }

      if (this.stack != null) {
         clone.stack = new ArrayList();

         for(i = 0; i < this.stack.size(); ++i) {
            s = this.stack.get(i);
            if (s instanceof LabelNode) {
               s = labels.get(s);
            }

            clone.stack.add(s);
         }
      }

      return clone;
   }

   private static List asList(int n, Object[] o) {
      return Arrays.asList(o).subList(0, n);
   }

   private static Object[] asArray(List l) {
      Object[] objs = new Object[l.size()];

      for(int i = 0; i < objs.length; ++i) {
         Object o = l.get(i);
         if (o instanceof LabelNode) {
            o = ((LabelNode)o).getLabel();
         }

         objs[i] = o;
      }

      return objs;
   }
}
