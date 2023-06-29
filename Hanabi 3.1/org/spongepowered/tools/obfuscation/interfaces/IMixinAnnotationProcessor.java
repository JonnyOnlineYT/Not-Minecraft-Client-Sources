package org.spongepowered.tools.obfuscation.interfaces;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import org.spongepowered.asm.util.ITokenProvider;

public interface IMixinAnnotationProcessor extends Messager, IOptionProvider {
   CompilerEnvironment getCompilerEnvironment();

   ProcessingEnvironment getProcessingEnvironment();

   IObfuscationManager getObfuscationManager();

   ITokenProvider getTokenProvider();

   ITypeHandleProvider getTypeProvider();

   IJavadocProvider getJavadocProvider();

   public static enum CompilerEnvironment {
      JAVAC,
      JDT;
   }
}
