package org.spongepowered.tools.agent;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.transformer.MixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.IHotSwap;
import org.spongepowered.asm.mixin.transformer.throwables.MixinReloadException;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.MixinService;

public class MixinAgent implements IHotSwap {
   public static final byte[] ERROR_BYTECODE = new byte[]{1};
   static final MixinAgentClassLoader classLoader = new MixinAgentClassLoader();
   static final Logger logger = LogManager.getLogger("mixin.agent");
   static Instrumentation instrumentation = null;
   private static List agents = new ArrayList();
   final MixinTransformer classTransformer;

   public MixinAgent(MixinTransformer classTransformer) {
      this.classTransformer = classTransformer;
      agents.add(this);
      if (instrumentation != null) {
         this.initTransformer();
      }

   }

   private void initTransformer() {
      instrumentation.addTransformer(new Transformer(), true);
   }

   public void registerMixinClass(String name) {
      classLoader.addMixinClass(name);
   }

   public void registerTargetClass(String name, byte[] bytecode) {
      classLoader.addTargetClass(name, bytecode);
   }

   public static void init(Instrumentation instrumentation) {
      MixinAgent.instrumentation = instrumentation;
      if (!MixinAgent.instrumentation.isRedefineClassesSupported()) {
         logger.error("The instrumentation doesn't support re-definition of classes");
      }

      Iterator var1 = agents.iterator();

      while(var1.hasNext()) {
         MixinAgent agent = (MixinAgent)var1.next();
         agent.initTransformer();
      }

   }

   public static void premain(String arg, Instrumentation instrumentation) {
      System.setProperty("mixin.hotSwap", "true");
      init(instrumentation);
   }

   public static void agentmain(String arg, Instrumentation instrumentation) {
      init(instrumentation);
   }

   class Transformer implements ClassFileTransformer {
      public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined, ProtectionDomain domain, byte[] classfileBuffer) throws IllegalClassFormatException {
         if (classBeingRedefined == null) {
            return null;
         } else {
            byte[] mixinBytecode = MixinAgent.classLoader.getFakeMixinBytecode(classBeingRedefined);
            if (mixinBytecode != null) {
               List targets = this.reloadMixin(className, classfileBuffer);
               return targets != null && this.reApplyMixins(targets) ? mixinBytecode : MixinAgent.ERROR_BYTECODE;
            } else {
               try {
                  MixinAgent.logger.info("Redefining class " + className);
                  return MixinAgent.this.classTransformer.transformClassBytes((String)null, className, classfileBuffer);
               } catch (Throwable var8) {
                  MixinAgent.logger.error("Error while re-transforming class " + className, var8);
                  return MixinAgent.ERROR_BYTECODE;
               }
            }
         }
      }

      private List reloadMixin(String className, byte[] classfileBuffer) {
         MixinAgent.logger.info("Redefining mixin {}", new Object[]{className});

         try {
            return MixinAgent.this.classTransformer.reload(className.replace('/', '.'), classfileBuffer);
         } catch (MixinReloadException var4) {
            MixinAgent.logger.error("Mixin {} cannot be reloaded, needs a restart to be applied: {} ", new Object[]{var4.getMixinInfo(), var4.getMessage()});
         } catch (Throwable var5) {
            MixinAgent.logger.error("Error while finding targets for mixin " + className, var5);
         }

         return null;
      }

      private boolean reApplyMixins(List targets) {
         IMixinService service = MixinService.getService();
         Iterator var3 = targets.iterator();

         while(var3.hasNext()) {
            String target = (String)var3.next();
            String targetName = target.replace('/', '.');
            MixinAgent.logger.debug("Re-transforming target class {}", new Object[]{target});

            try {
               Class targetClass = service.getClassProvider().findClass(targetName);
               byte[] targetBytecode = MixinAgent.classLoader.getOriginalTargetBytecode(targetName);
               if (targetBytecode == null) {
                  MixinAgent.logger.error("Target class {} bytecode is not registered", new Object[]{targetName});
                  return false;
               }

               targetBytecode = MixinAgent.this.classTransformer.transformClassBytes((String)null, targetName, targetBytecode);
               MixinAgent.instrumentation.redefineClasses(new ClassDefinition[]{new ClassDefinition(targetClass, targetBytecode)});
            } catch (Throwable var8) {
               MixinAgent.logger.error("Error while re-transforming target class " + target, var8);
               return false;
            }
         }

         return true;
      }
   }
}
