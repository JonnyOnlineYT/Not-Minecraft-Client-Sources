package org.spongepowered.asm.mixin.injection.invoke.arg;

public abstract class Args {
   protected final Object[] values;

   protected Args(Object[] values) {
      this.values = values;
   }

   public int size() {
      return this.values.length;
   }

   public Object get(int index) {
      return this.values[index];
   }

   public abstract void set(int var1, Object var2);

   public abstract void setAll(Object... var1);
}
