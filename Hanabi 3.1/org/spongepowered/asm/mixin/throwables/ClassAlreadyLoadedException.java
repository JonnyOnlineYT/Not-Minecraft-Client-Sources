package org.spongepowered.asm.mixin.throwables;

public class ClassAlreadyLoadedException extends MixinException {
   private static final long serialVersionUID = 1L;

   public ClassAlreadyLoadedException(String message) {
      super(message);
   }

   public ClassAlreadyLoadedException(Throwable cause) {
      super(cause);
   }

   public ClassAlreadyLoadedException(String message, Throwable cause) {
      super(message, cause);
   }
}
