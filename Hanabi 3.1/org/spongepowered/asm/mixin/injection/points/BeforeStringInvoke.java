package org.spongepowered.asm.mixin.injection.points;

import java.util.Collection;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;

@InjectionPoint.AtCode("INVOKE_STRING")
public class BeforeStringInvoke extends BeforeInvoke {
   private static final String STRING_VOID_SIG = "(Ljava/lang/String;)V";
   private final String ldcValue;
   private boolean foundLdc;

   public BeforeStringInvoke(InjectionPointData data) {
      super(data);
      this.ldcValue = data.get("ldc", (String)null);
      if (this.ldcValue == null) {
         throw new IllegalArgumentException(this.getClass().getSimpleName() + " requires named argument \"ldc\" to specify the desired target");
      } else if (!"(Ljava/lang/String;)V".equals(this.target.desc)) {
         throw new IllegalArgumentException(this.getClass().getSimpleName() + " requires target method with with signature " + "(Ljava/lang/String;)V");
      }
   }

   public boolean find(String desc, InsnList insns, Collection nodes) {
      this.foundLdc = false;
      return super.find(desc, insns, nodes);
   }

   protected void inspectInsn(String desc, InsnList insns, AbstractInsnNode insn) {
      if (insn instanceof LdcInsnNode) {
         LdcInsnNode node = (LdcInsnNode)insn;
         if (node.cst instanceof String && this.ldcValue.equals(node.cst)) {
            this.log("{} > found a matching LDC with value {}", new Object[]{this.className, node.cst});
            this.foundLdc = true;
            return;
         }
      }

      this.foundLdc = false;
   }

   protected boolean matchesInsn(MemberInfo nodeInfo, int ordinal) {
      this.log("{} > > found LDC \"{}\" = {}", new Object[]{this.className, this.ldcValue, this.foundLdc});
      return this.foundLdc && super.matchesInsn(nodeInfo, ordinal);
   }
}
