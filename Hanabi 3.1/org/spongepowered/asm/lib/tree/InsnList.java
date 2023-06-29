package org.spongepowered.asm.lib.tree;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.spongepowered.asm.lib.MethodVisitor;

public class InsnList {
   private int size;
   private AbstractInsnNode first;
   private AbstractInsnNode last;
   AbstractInsnNode[] cache;

   public int size() {
      return this.size;
   }

   public AbstractInsnNode getFirst() {
      return this.first;
   }

   public AbstractInsnNode getLast() {
      return this.last;
   }

   public AbstractInsnNode get(int index) {
      if (index >= 0 && index < this.size) {
         if (this.cache == null) {
            this.cache = this.toArray();
         }

         return this.cache[index];
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public boolean contains(AbstractInsnNode insn) {
      AbstractInsnNode i;
      for(i = this.first; i != null && i != insn; i = i.next) {
      }

      return i != null;
   }

   public int indexOf(AbstractInsnNode insn) {
      if (this.cache == null) {
         this.cache = this.toArray();
      }

      return insn.index;
   }

   public void accept(MethodVisitor mv) {
      for(AbstractInsnNode insn = this.first; insn != null; insn = insn.next) {
         insn.accept(mv);
      }

   }

   public ListIterator iterator() {
      return this.iterator(0);
   }

   public ListIterator iterator(int index) {
      return new InsnListIterator(index);
   }

   public AbstractInsnNode[] toArray() {
      int i = 0;
      AbstractInsnNode elem = this.first;

      AbstractInsnNode[] insns;
      for(insns = new AbstractInsnNode[this.size]; elem != null; elem = elem.next) {
         insns[i] = elem;
         elem.index = i++;
      }

      return insns;
   }

   public void set(AbstractInsnNode location, AbstractInsnNode insn) {
      AbstractInsnNode next = location.next;
      insn.next = next;
      if (next != null) {
         next.prev = insn;
      } else {
         this.last = insn;
      }

      AbstractInsnNode prev = location.prev;
      insn.prev = prev;
      if (prev != null) {
         prev.next = insn;
      } else {
         this.first = insn;
      }

      if (this.cache != null) {
         int index = location.index;
         this.cache[index] = insn;
         insn.index = index;
      } else {
         insn.index = 0;
      }

      location.index = -1;
      location.prev = null;
      location.next = null;
   }

   public void add(AbstractInsnNode insn) {
      ++this.size;
      if (this.last == null) {
         this.first = insn;
         this.last = insn;
      } else {
         this.last.next = insn;
         insn.prev = this.last;
      }

      this.last = insn;
      this.cache = null;
      insn.index = 0;
   }

   public void add(InsnList insns) {
      if (insns.size != 0) {
         this.size += insns.size;
         if (this.last == null) {
            this.first = insns.first;
            this.last = insns.last;
         } else {
            AbstractInsnNode elem = insns.first;
            this.last.next = elem;
            elem.prev = this.last;
            this.last = insns.last;
         }

         this.cache = null;
         insns.removeAll(false);
      }
   }

   public void insert(AbstractInsnNode insn) {
      ++this.size;
      if (this.first == null) {
         this.first = insn;
         this.last = insn;
      } else {
         this.first.prev = insn;
         insn.next = this.first;
      }

      this.first = insn;
      this.cache = null;
      insn.index = 0;
   }

   public void insert(InsnList insns) {
      if (insns.size != 0) {
         this.size += insns.size;
         if (this.first == null) {
            this.first = insns.first;
            this.last = insns.last;
         } else {
            AbstractInsnNode elem = insns.last;
            this.first.prev = elem;
            elem.next = this.first;
            this.first = insns.first;
         }

         this.cache = null;
         insns.removeAll(false);
      }
   }

   public void insert(AbstractInsnNode location, AbstractInsnNode insn) {
      ++this.size;
      AbstractInsnNode next = location.next;
      if (next == null) {
         this.last = insn;
      } else {
         next.prev = insn;
      }

      location.next = insn;
      insn.next = next;
      insn.prev = location;
      this.cache = null;
      insn.index = 0;
   }

   public void insert(AbstractInsnNode location, InsnList insns) {
      if (insns.size != 0) {
         this.size += insns.size;
         AbstractInsnNode ifirst = insns.first;
         AbstractInsnNode ilast = insns.last;
         AbstractInsnNode next = location.next;
         if (next == null) {
            this.last = ilast;
         } else {
            next.prev = ilast;
         }

         location.next = ifirst;
         ilast.next = next;
         ifirst.prev = location;
         this.cache = null;
         insns.removeAll(false);
      }
   }

   public void insertBefore(AbstractInsnNode location, AbstractInsnNode insn) {
      ++this.size;
      AbstractInsnNode prev = location.prev;
      if (prev == null) {
         this.first = insn;
      } else {
         prev.next = insn;
      }

      location.prev = insn;
      insn.next = location;
      insn.prev = prev;
      this.cache = null;
      insn.index = 0;
   }

   public void insertBefore(AbstractInsnNode location, InsnList insns) {
      if (insns.size != 0) {
         this.size += insns.size;
         AbstractInsnNode ifirst = insns.first;
         AbstractInsnNode ilast = insns.last;
         AbstractInsnNode prev = location.prev;
         if (prev == null) {
            this.first = ifirst;
         } else {
            prev.next = ifirst;
         }

         location.prev = ilast;
         ilast.next = location;
         ifirst.prev = prev;
         this.cache = null;
         insns.removeAll(false);
      }
   }

   public void remove(AbstractInsnNode insn) {
      --this.size;
      AbstractInsnNode next = insn.next;
      AbstractInsnNode prev = insn.prev;
      if (next == null) {
         if (prev == null) {
            this.first = null;
            this.last = null;
         } else {
            prev.next = null;
            this.last = prev;
         }
      } else if (prev == null) {
         this.first = next;
         next.prev = null;
      } else {
         prev.next = next;
         next.prev = prev;
      }

      this.cache = null;
      insn.index = -1;
      insn.prev = null;
      insn.next = null;
   }

   void removeAll(boolean mark) {
      AbstractInsnNode next;
      if (mark) {
         for(AbstractInsnNode insn = this.first; insn != null; insn = next) {
            next = insn.next;
            insn.index = -1;
            insn.prev = null;
            insn.next = null;
         }
      }

      this.size = 0;
      this.first = null;
      this.last = null;
      this.cache = null;
   }

   public void clear() {
      this.removeAll(false);
   }

   public void resetLabels() {
      for(AbstractInsnNode insn = this.first; insn != null; insn = insn.next) {
         if (insn instanceof LabelNode) {
            ((LabelNode)insn).resetLabel();
         }
      }

   }

   private final class InsnListIterator implements ListIterator {
      AbstractInsnNode next;
      AbstractInsnNode prev;
      AbstractInsnNode remove;

      InsnListIterator(int index) {
         if (index == InsnList.this.size()) {
            this.next = null;
            this.prev = InsnList.this.getLast();
         } else {
            this.next = InsnList.this.get(index);
            this.prev = this.next.prev;
         }

      }

      public boolean hasNext() {
         return this.next != null;
      }

      public Object next() {
         if (this.next == null) {
            throw new NoSuchElementException();
         } else {
            AbstractInsnNode result = this.next;
            this.prev = result;
            this.next = result.next;
            this.remove = result;
            return result;
         }
      }

      public void remove() {
         if (this.remove != null) {
            if (this.remove == this.next) {
               this.next = this.next.next;
            } else {
               this.prev = this.prev.prev;
            }

            InsnList.this.remove(this.remove);
            this.remove = null;
         } else {
            throw new IllegalStateException();
         }
      }

      public boolean hasPrevious() {
         return this.prev != null;
      }

      public Object previous() {
         AbstractInsnNode result = this.prev;
         this.next = result;
         this.prev = result.prev;
         this.remove = result;
         return result;
      }

      public int nextIndex() {
         if (this.next == null) {
            return InsnList.this.size();
         } else {
            if (InsnList.this.cache == null) {
               InsnList.this.cache = InsnList.this.toArray();
            }

            return this.next.index;
         }
      }

      public int previousIndex() {
         if (this.prev == null) {
            return -1;
         } else {
            if (InsnList.this.cache == null) {
               InsnList.this.cache = InsnList.this.toArray();
            }

            return this.prev.index;
         }
      }

      public void add(Object o) {
         if (this.next != null) {
            InsnList.this.insertBefore(this.next, (AbstractInsnNode)o);
         } else if (this.prev != null) {
            InsnList.this.insert(this.prev, (AbstractInsnNode)o);
         } else {
            InsnList.this.add((AbstractInsnNode)o);
         }

         this.prev = (AbstractInsnNode)o;
         this.remove = null;
      }

      public void set(Object o) {
         if (this.remove != null) {
            InsnList.this.set(this.remove, (AbstractInsnNode)o);
            if (this.remove == this.prev) {
               this.prev = (AbstractInsnNode)o;
            } else {
               this.next = (AbstractInsnNode)o;
            }

         } else {
            throw new IllegalStateException();
         }
      }
   }
}
