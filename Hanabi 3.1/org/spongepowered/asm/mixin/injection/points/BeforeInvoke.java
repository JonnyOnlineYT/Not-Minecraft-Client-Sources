package org.spongepowered.asm.mixin.injection.points;

import java.util.Collection;
import java.util.ListIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

@InjectionPoint.AtCode("INVOKE")
public class BeforeInvoke extends InjectionPoint {
   protected final MemberInfo target;
   protected final boolean allowPermissive;
   protected final int ordinal;
   protected final String className;
   protected final IMixinContext context;
   protected final Logger logger = LogManager.getLogger("mixin");
   private boolean log = false;

   public BeforeInvoke(InjectionPointData data) {
      super(data);
      this.target = data.getTarget();
      this.ordinal = data.getOrdinal();
      this.log = data.get("log", false);
      this.className = this.getClassName();
      this.context = data.getContext();
      this.allowPermissive = this.context.getOption(MixinEnvironment.Option.REFMAP_REMAP) && this.context.getOption(MixinEnvironment.Option.REFMAP_REMAP_ALLOW_PERMISSIVE) && !this.context.getReferenceMapper().isDefault();
   }

   private String getClassName() {
      InjectionPoint.AtCode atCode = (InjectionPoint.AtCode)this.getClass().getAnnotation(InjectionPoint.AtCode.class);
      return String.format("@At(%s)", atCode != null ? atCode.value() : this.getClass().getSimpleName().toUpperCase());
   }

   public BeforeInvoke setLogging(boolean logging) {
      this.log = logging;
      return this;
   }

   public boolean find(String desc, InsnList insns, Collection nodes) {
      this.log("{} is searching for an injection point in method with descriptor {}", this.className, desc);
      if (!this.find(desc, insns, nodes, this.target, BeforeInvoke.SearchType.STRICT) && this.target.desc != null && this.allowPermissive) {
         this.logger.warn("STRICT match for {} using \"{}\" in {} returned 0 results, attempting permissive search. To inhibit permissive search set mixin.env.allowPermissiveMatch=false", new Object[]{this.className, this.target, this.context});
         return this.find(desc, insns, nodes, this.target, BeforeInvoke.SearchType.PERMISSIVE);
      } else {
         return true;
      }
   }

   protected boolean find(String desc, InsnList insns, Collection nodes, MemberInfo member, SearchType searchType) {
      if (member == null) {
         return false;
      } else {
         MemberInfo target = searchType == BeforeInvoke.SearchType.PERMISSIVE ? member.transform((String)null) : member;
         int ordinal = 0;
         int found = 0;

         AbstractInsnNode insn;
         for(ListIterator iter = insns.iterator(); iter.hasNext(); this.inspectInsn(desc, insns, insn)) {
            insn = (AbstractInsnNode)iter.next();
            if (this.matchesInsn(insn)) {
               MemberInfo nodeInfo = new MemberInfo(insn);
               this.log("{} is considering insn {}", this.className, nodeInfo);
               if (target.matches(nodeInfo.owner, nodeInfo.name, nodeInfo.desc)) {
                  this.log("{} > found a matching insn, checking preconditions...", this.className);
                  if (this.matchesInsn(nodeInfo, ordinal)) {
                     this.log("{} > > > found a matching insn at ordinal {}", this.className, ordinal);
                     if (this.addInsn(insns, nodes, insn)) {
                        ++found;
                     }

                     if (this.ordinal == ordinal) {
                        break;
                     }
                  }

                  ++ordinal;
               }
            }
         }

         if (searchType == BeforeInvoke.SearchType.PERMISSIVE && found > 1) {
            this.logger.warn("A permissive match for {} using \"{}\" in {} matched {} instructions, this may cause unexpected behaviour. To inhibit permissive search set mixin.env.allowPermissiveMatch=false", new Object[]{this.className, member, this.context, found});
         }

         return found > 0;
      }
   }

   protected boolean addInsn(InsnList insns, Collection nodes, AbstractInsnNode insn) {
      nodes.add(insn);
      return true;
   }

   protected boolean matchesInsn(AbstractInsnNode insn) {
      return insn instanceof MethodInsnNode;
   }

   protected void inspectInsn(String desc, InsnList insns, AbstractInsnNode insn) {
   }

   protected boolean matchesInsn(MemberInfo nodeInfo, int ordinal) {
      this.log("{} > > comparing target ordinal {} with current ordinal {}", this.className, this.ordinal, ordinal);
      return this.ordinal == -1 || this.ordinal == ordinal;
   }

   protected void log(String message, Object... params) {
      if (this.log) {
         this.logger.info(message, params);
      }

   }

   public static enum SearchType {
      STRICT,
      PERMISSIVE;
   }
}
