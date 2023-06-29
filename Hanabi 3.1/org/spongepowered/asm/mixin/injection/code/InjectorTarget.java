package org.spongepowered.asm.mixin.injection.code;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import org.spongepowered.asm.util.Annotations;

public class InjectorTarget {
   private final ISliceContext context;
   private final Map cache = new HashMap();
   private final Target target;
   private final String mergedBy;
   private final int mergedPriority;

   public InjectorTarget(ISliceContext context, Target target) {
      this.context = context;
      this.target = target;
      AnnotationNode merged = Annotations.getVisible(target.method, MixinMerged.class);
      this.mergedBy = (String)Annotations.getValue(merged, "mixin");
      this.mergedPriority = (Integer)Annotations.getValue(merged, "priority", (int)1000);
   }

   public String toString() {
      return this.target.toString();
   }

   public Target getTarget() {
      return this.target;
   }

   public MethodNode getMethod() {
      return this.target.method;
   }

   public boolean isMerged() {
      return this.mergedBy != null;
   }

   public String getMergedBy() {
      return this.mergedBy;
   }

   public int getMergedPriority() {
      return this.mergedPriority;
   }

   public InsnList getSlice(String id) {
      ReadOnlyInsnList slice = (ReadOnlyInsnList)this.cache.get(id);
      if (slice == null) {
         MethodSlice sliceInfo = this.context.getSlice(id);
         if (sliceInfo != null) {
            slice = sliceInfo.getSlice(this.target.method);
         } else {
            slice = new ReadOnlyInsnList(this.target.method.instructions);
         }

         this.cache.put(id, slice);
      }

      return slice;
   }

   public InsnList getSlice(InjectionPoint injectionPoint) {
      return this.getSlice(injectionPoint.getSlice());
   }

   public void dispose() {
      Iterator var1 = this.cache.values().iterator();

      while(var1.hasNext()) {
         ReadOnlyInsnList insns = (ReadOnlyInsnList)var1.next();
         insns.dispose();
      }

      this.cache.clear();
   }
}
