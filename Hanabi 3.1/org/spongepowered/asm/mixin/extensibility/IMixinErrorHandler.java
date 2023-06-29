package org.spongepowered.asm.mixin.extensibility;

import org.apache.logging.log4j.Level;

public interface IMixinErrorHandler {
   ErrorAction onPrepareError(IMixinConfig var1, Throwable var2, IMixinInfo var3, ErrorAction var4);

   ErrorAction onApplyError(String var1, Throwable var2, IMixinInfo var3, ErrorAction var4);

   public static enum ErrorAction {
      NONE(Level.INFO),
      WARN(Level.WARN),
      ERROR(Level.FATAL);

      public final Level logLevel;

      private ErrorAction(Level logLevel) {
         this.logLevel = logLevel;
      }
   }
}
