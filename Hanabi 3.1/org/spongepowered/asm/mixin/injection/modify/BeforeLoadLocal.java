package org.spongepowered.asm.mixin.injection.modify;

import java.util.Collection;
import java.util.ListIterator;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.struct.Target;

@InjectionPoint.AtCode("LOAD")
public class BeforeLoadLocal extends ModifyVariableInjector.ContextualInjectionPoint {
   private final Type returnType;
   private final LocalVariableDiscriminator discriminator;
   private final int opcode;
   private final int ordinal;
   private boolean opcodeAfter;

   protected BeforeLoadLocal(InjectionPointData data) {
      this(data, 21, false);
   }

   protected BeforeLoadLocal(InjectionPointData data, int opcode, boolean opcodeAfter) {
      super(data.getContext());
      this.returnType = data.getMethodReturnType();
      this.discriminator = data.getLocalVariableDiscriminator();
      this.opcode = data.getOpcode(this.returnType.getOpcode(opcode));
      this.ordinal = data.getOrdinal();
      this.opcodeAfter = opcodeAfter;
   }

   boolean find(Target target, Collection nodes) {
      SearchState state = new SearchState(this.ordinal, this.discriminator.printLVT());
      ListIterator iter = target.method.instructions.iterator();

      while(true) {
         while(iter.hasNext()) {
            AbstractInsnNode insn = (AbstractInsnNode)iter.next();
            int local;
            if (state.isPendingCheck()) {
               local = this.discriminator.findLocal(this.returnType, this.discriminator.isArgsOnly(), target, insn);
               state.check(nodes, insn, local);
            } else if (insn instanceof VarInsnNode && insn.getOpcode() == this.opcode && (this.ordinal == -1 || !state.success())) {
               state.register((VarInsnNode)insn);
               if (this.opcodeAfter) {
                  state.setPendingCheck();
               } else {
                  local = this.discriminator.findLocal(this.returnType, this.discriminator.isArgsOnly(), target, insn);
                  state.check(nodes, insn, local);
               }
            }
         }

         return state.success();
      }
   }

   static class SearchState {
      private final boolean print;
      private final int targetOrdinal;
      private int ordinal = 0;
      private boolean pendingCheck = false;
      private boolean found = false;
      private VarInsnNode varNode;

      SearchState(int targetOrdinal, boolean print) {
         this.targetOrdinal = targetOrdinal;
         this.print = print;
      }

      boolean success() {
         return this.found;
      }

      boolean isPendingCheck() {
         return this.pendingCheck;
      }

      void setPendingCheck() {
         this.pendingCheck = true;
      }

      void register(VarInsnNode node) {
         this.varNode = node;
      }

      void check(Collection nodes, AbstractInsnNode insn, int local) {
         this.pendingCheck = false;
         if (local == this.varNode.var || local <= -2 && this.print) {
            if (this.targetOrdinal == -1 || this.targetOrdinal == this.ordinal) {
               nodes.add(insn);
               this.found = true;
            }

            ++this.ordinal;
            this.varNode = null;
         }
      }
   }
}
