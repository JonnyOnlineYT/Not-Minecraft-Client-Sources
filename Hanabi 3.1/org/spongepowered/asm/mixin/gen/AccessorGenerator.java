package org.spongepowered.asm.mixin.gen;

import java.util.ArrayList;
import org.spongepowered.asm.lib.tree.MethodNode;

public abstract class AccessorGenerator {
   protected final AccessorInfo info;

   public AccessorGenerator(AccessorInfo info) {
      this.info = info;
   }

   protected final MethodNode createMethod(int maxLocals, int maxStack) {
      MethodNode method = this.info.getMethod();
      MethodNode accessor = new MethodNode(327680, method.access & -1025 | 4096, method.name, method.desc, (String)null, (String[])null);
      accessor.visibleAnnotations = new ArrayList();
      accessor.visibleAnnotations.add(this.info.getAnnotation());
      accessor.maxLocals = maxLocals;
      accessor.maxStack = maxStack;
      return accessor;
   }

   public abstract MethodNode generate();
}
