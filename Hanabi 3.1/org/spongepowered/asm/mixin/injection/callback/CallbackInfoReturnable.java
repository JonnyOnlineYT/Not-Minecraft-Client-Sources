package org.spongepowered.asm.mixin.injection.callback;

import org.spongepowered.asm.lib.Type;

public class CallbackInfoReturnable extends CallbackInfo {
   private Object returnValue;

   public CallbackInfoReturnable(String name, boolean cancellable) {
      super(name, cancellable);
      this.returnValue = null;
   }

   public CallbackInfoReturnable(String name, boolean cancellable, Object returnValue) {
      super(name, cancellable);
      this.returnValue = returnValue;
   }

   public CallbackInfoReturnable(String name, boolean cancellable, byte returnValue) {
      super(name, cancellable);
      this.returnValue = returnValue;
   }

   public CallbackInfoReturnable(String name, boolean cancellable, char returnValue) {
      super(name, cancellable);
      this.returnValue = returnValue;
   }

   public CallbackInfoReturnable(String name, boolean cancellable, double returnValue) {
      super(name, cancellable);
      this.returnValue = returnValue;
   }

   public CallbackInfoReturnable(String name, boolean cancellable, float returnValue) {
      super(name, cancellable);
      this.returnValue = returnValue;
   }

   public CallbackInfoReturnable(String name, boolean cancellable, int returnValue) {
      super(name, cancellable);
      this.returnValue = returnValue;
   }

   public CallbackInfoReturnable(String name, boolean cancellable, long returnValue) {
      super(name, cancellable);
      this.returnValue = returnValue;
   }

   public CallbackInfoReturnable(String name, boolean cancellable, short returnValue) {
      super(name, cancellable);
      this.returnValue = returnValue;
   }

   public CallbackInfoReturnable(String name, boolean cancellable, boolean returnValue) {
      super(name, cancellable);
      this.returnValue = returnValue;
   }

   public void setReturnValue(Object returnValue) throws CancellationException {
      super.cancel();
      this.returnValue = returnValue;
   }

   public Object getReturnValue() {
      return this.returnValue;
   }

   public byte getReturnValueB() {
      return this.returnValue == null ? 0 : (Byte)this.returnValue;
   }

   public char getReturnValueC() {
      return this.returnValue == null ? '\u0000' : (Character)this.returnValue;
   }

   public double getReturnValueD() {
      return this.returnValue == null ? 0.0 : (Double)this.returnValue;
   }

   public float getReturnValueF() {
      return this.returnValue == null ? 0.0F : (Float)this.returnValue;
   }

   public int getReturnValueI() {
      return this.returnValue == null ? 0 : (Integer)this.returnValue;
   }

   public long getReturnValueJ() {
      return this.returnValue == null ? 0L : (Long)this.returnValue;
   }

   public short getReturnValueS() {
      return this.returnValue == null ? 0 : (Short)this.returnValue;
   }

   public boolean getReturnValueZ() {
      return this.returnValue == null ? false : (Boolean)this.returnValue;
   }

   static String getReturnAccessor(Type returnType) {
      return returnType.getSort() != 10 && returnType.getSort() != 9 ? String.format("getReturnValue%s", returnType.getDescriptor()) : "getReturnValue";
   }

   static String getReturnDescriptor(Type returnType) {
      return returnType.getSort() != 10 && returnType.getSort() != 9 ? String.format("()%s", returnType.getDescriptor()) : String.format("()%s", "Ljava/lang/Object;");
   }
}
