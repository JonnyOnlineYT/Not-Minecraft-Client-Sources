package org.spongepowered.asm.mixin.injection.code;

import com.google.common.base.Strings;
import java.util.Deque;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import org.spongepowered.asm.mixin.injection.throwables.InvalidSliceException;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;

public final class MethodSlice {
   private static final Logger logger = LogManager.getLogger("mixin");
   private final ISliceContext owner;
   private final String id;
   private final InjectionPoint from;
   private final InjectionPoint to;
   private final String name;

   private MethodSlice(ISliceContext owner, String id, InjectionPoint from, InjectionPoint to) {
      if (from == null && to == null) {
         throw new InvalidSliceException(owner, String.format("%s is redundant. No 'from' or 'to' value specified", this));
      } else {
         this.owner = owner;
         this.id = Strings.nullToEmpty(id);
         this.from = from;
         this.to = to;
         this.name = getSliceName(id);
      }
   }

   public String getId() {
      return this.id;
   }

   public ReadOnlyInsnList getSlice(MethodNode method) {
      int max = method.instructions.size() - 1;
      int start = this.find(method, this.from, 0, 0, this.name + "(from)");
      int end = this.find(method, this.to, max, start, this.name + "(to)");
      if (start > end) {
         throw new InvalidSliceException(this.owner, String.format("%s is negative size. Range(%d -> %d)", this.describe(), start, end));
      } else if (start >= 0 && end >= 0 && start <= max && end <= max) {
         return (ReadOnlyInsnList)(start == 0 && end == max ? new ReadOnlyInsnList(method.instructions) : new InsnListSlice(method.instructions, start, end));
      } else {
         throw new InjectionError("Unexpected critical error in " + this + ": out of bounds start=" + start + " end=" + end + " lim=" + max);
      }
   }

   private int find(MethodNode method, InjectionPoint injectionPoint, int defaultValue, int failValue, String description) {
      if (injectionPoint == null) {
         return defaultValue;
      } else {
         Deque nodes = new LinkedList();
         ReadOnlyInsnList insns = new ReadOnlyInsnList(method.instructions);
         boolean result = injectionPoint.find(method.desc, insns, nodes);
         InjectionPoint.Selector select = injectionPoint.getSelector();
         if (nodes.size() != 1 && select == InjectionPoint.Selector.ONE) {
            throw new InvalidSliceException(this.owner, String.format("%s requires 1 result but found %d", this.describe(description), nodes.size()));
         } else if (!result) {
            if (this.owner.getContext().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
               logger.warn("{} did not match any instructions", new Object[]{this.describe(description)});
            }

            return failValue;
         } else {
            return method.instructions.indexOf(select == InjectionPoint.Selector.FIRST ? (AbstractInsnNode)nodes.getFirst() : (AbstractInsnNode)nodes.getLast());
         }
      }
   }

   public String toString() {
      return this.describe();
   }

   private String describe() {
      return this.describe(this.name);
   }

   private String describe(String description) {
      return describeSlice(description, this.owner);
   }

   private static String describeSlice(String description, ISliceContext owner) {
      String annotation = Bytecode.getSimpleName(owner.getAnnotation());
      MethodNode method = owner.getMethod();
      return String.format("%s->%s(%s)::%s%s", owner.getContext(), annotation, description, method.name, method.desc);
   }

   private static String getSliceName(String id) {
      return String.format("@Slice[%s]", Strings.nullToEmpty(id));
   }

   public static MethodSlice parse(ISliceContext owner, Slice slice) {
      String id = slice.id();
      At from = slice.from();
      At to = slice.to();
      InjectionPoint fromPoint = from != null ? InjectionPoint.parse(owner, (At)from) : null;
      InjectionPoint toPoint = to != null ? InjectionPoint.parse(owner, (At)to) : null;
      return new MethodSlice(owner, id, fromPoint, toPoint);
   }

   public static MethodSlice parse(ISliceContext info, AnnotationNode node) {
      String id = (String)Annotations.getValue(node, "id");
      AnnotationNode from = (AnnotationNode)Annotations.getValue(node, "from");
      AnnotationNode to = (AnnotationNode)Annotations.getValue(node, "to");
      InjectionPoint fromPoint = from != null ? InjectionPoint.parse(info, (AnnotationNode)from) : null;
      InjectionPoint toPoint = to != null ? InjectionPoint.parse(info, (AnnotationNode)to) : null;
      return new MethodSlice(info, id, fromPoint, toPoint);
   }

   static final class InsnListSlice extends ReadOnlyInsnList {
      private final int start;
      private final int end;

      protected InsnListSlice(InsnList inner, int start, int end) {
         super(inner);
         this.start = start;
         this.end = end;
      }

      public ListIterator iterator() {
         return this.iterator(0);
      }

      public ListIterator iterator(int index) {
         return new SliceIterator(super.iterator(this.start + index), this.start, this.end, this.start + index);
      }

      public AbstractInsnNode[] toArray() {
         AbstractInsnNode[] all = super.toArray();
         AbstractInsnNode[] subset = new AbstractInsnNode[this.size()];
         System.arraycopy(all, this.start, subset, 0, subset.length);
         return subset;
      }

      public int size() {
         return this.end - this.start + 1;
      }

      public AbstractInsnNode getFirst() {
         return super.get(this.start);
      }

      public AbstractInsnNode getLast() {
         return super.get(this.end);
      }

      public AbstractInsnNode get(int index) {
         return super.get(this.start + index);
      }

      public boolean contains(AbstractInsnNode insn) {
         AbstractInsnNode[] var2 = this.toArray();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            AbstractInsnNode node = var2[var4];
            if (node == insn) {
               return true;
            }
         }

         return false;
      }

      public int indexOf(AbstractInsnNode insn) {
         int index = super.indexOf(insn);
         return index >= this.start && index <= this.end ? index - this.start : -1;
      }

      public int realIndexOf(AbstractInsnNode insn) {
         return super.indexOf(insn);
      }

      static class SliceIterator implements ListIterator {
         private final ListIterator iter;
         private int start;
         private int end;
         private int index;

         public SliceIterator(ListIterator iter, int start, int end, int index) {
            this.iter = iter;
            this.start = start;
            this.end = end;
            this.index = index;
         }

         public boolean hasNext() {
            return this.index <= this.end && this.iter.hasNext();
         }

         public AbstractInsnNode next() {
            if (this.index > this.end) {
               throw new NoSuchElementException();
            } else {
               ++this.index;
               return (AbstractInsnNode)this.iter.next();
            }
         }

         public boolean hasPrevious() {
            return this.index > this.start;
         }

         public AbstractInsnNode previous() {
            if (this.index <= this.start) {
               throw new NoSuchElementException();
            } else {
               --this.index;
               return (AbstractInsnNode)this.iter.previous();
            }
         }

         public int nextIndex() {
            return this.index - this.start;
         }

         public int previousIndex() {
            return this.index - this.start - 1;
         }

         public void remove() {
            throw new UnsupportedOperationException("Cannot remove insn from slice");
         }

         public void set(AbstractInsnNode e) {
            throw new UnsupportedOperationException("Cannot set insn using slice");
         }

         public void add(AbstractInsnNode e) {
            throw new UnsupportedOperationException("Cannot add insn using slice");
         }
      }
   }
}
