package org.spongepowered.asm.mixin.transformer.debug;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RuntimeDecompilerAsync extends RuntimeDecompiler implements Runnable, Thread.UncaughtExceptionHandler {
   private final BlockingQueue queue = new LinkedBlockingQueue();
   private final Thread thread = new Thread(this, "Decompiler thread");
   private boolean run = true;

   public RuntimeDecompilerAsync(File outputPath) {
      super(outputPath);
      this.thread.setDaemon(true);
      this.thread.setPriority(1);
      this.thread.setUncaughtExceptionHandler(this);
      this.thread.start();
   }

   public void decompile(File file) {
      if (this.run) {
         this.queue.offer(file);
      } else {
         super.decompile(file);
      }

   }

   public void run() {
      while(this.run) {
         try {
            File file = (File)this.queue.take();
            super.decompile(file);
         } catch (InterruptedException var2) {
            this.run = false;
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

   }

   public void uncaughtException(Thread thread, Throwable ex) {
      this.logger.error("Async decompiler encountered an error and will terminate. Further decompile requests will be handled synchronously. {} {}", new Object[]{ex.getClass().getName(), ex.getMessage()});
      this.flush();
   }

   private void flush() {
      this.run = false;

      File file;
      while((file = (File)this.queue.poll()) != null) {
         this.decompile(file);
      }

   }
}
