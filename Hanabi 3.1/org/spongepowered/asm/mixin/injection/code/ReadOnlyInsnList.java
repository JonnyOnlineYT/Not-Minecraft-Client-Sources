package org.spongepowered.asm.mixin.injection.code;

import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;

class ReadOnlyInsnList extends InsnList {
   private InsnList insnList;

   public ReadOnlyInsnList(InsnList insns) {
      this.insnList = insns;
   }

   void dispose() {
      this.insnList = null;
   }

   public final void set(AbstractInsnNode location, AbstractInsnNode insn) {
      throw new UnsupportedOperationException();
   }

   public final void add(AbstractInsnNode insn) {
      throw new UnsupportedOperationException();
   }

   public final void add(InsnList insns) {
      throw new UnsupportedOperationException();
   }

   public final void insert(AbstractInsnNode insn) {
      throw new UnsupportedOperationException();
   }

   public final void insert(InsnList insns) {
      throw new UnsupportedOperationException();
   }

   public final void insert(AbstractInsnNode location, AbstractInsnNode insn) {
      throw new UnsupportedOperationException();
   }

   public final void insert(AbstractInsnNode location, InsnList insns) {
      throw new UnsupportedOperationException();
   }

   public final void insertBefore(AbstractInsnNode location, AbstractInsnNode insn) {
      throw new UnsupportedOperationException();
   }

   public final void insertBefore(AbstractInsnNode location, InsnList insns) {
      throw new UnsupportedOperationException();
   }

   public final void remove(AbstractInsnNode insn) {
      throw new UnsupportedOperationException();
   }

   public AbstractInsnNode[] toArray() {
      return this.insnList.toArray();
   }

   public int size() {
      return this.insnList.size();
   }

   public AbstractInsnNode getFirst() {
      return this.insnList.getFirst();
   }

   public AbstractInsnNode getLast() {
      return this.insnList.getLast();
   }

   public AbstractInsnNode get(int index) {
      return this.insnList.get(index);
   }

   public boolean contains(AbstractInsnNode insn) {
      return this.insnList.contains(insn);
   }

   public int indexOf(AbstractInsnNode insn) {
      return this.insnList.indexOf(insn);
   }

   public ListIterator iterator() {
      return this.insnList.iterator();
   }

   public ListIterator iterator(int index) {
      return this.insnList.iterator(index);
   }

   public final void resetLabels() {
      this.insnList.resetLabels();
   }
}
