package org.spongepowered.asm.launch.platform;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.service.MixinService;

public class MixinContainer {
   private static final List agentClasses = new ArrayList();
   private final Logger logger = LogManager.getLogger("mixin");
   private final URI uri;
   private final List agents = new ArrayList();

   public MixinContainer(MixinPlatformManager manager, URI uri) {
      this.uri = uri;
      Iterator var3 = agentClasses.iterator();

      while(var3.hasNext()) {
         String agentClass = (String)var3.next();

         try {
            Class clazz = Class.forName(agentClass);
            Constructor ctor = clazz.getDeclaredConstructor(MixinPlatformManager.class, URI.class);
            this.logger.debug("Instancing new {} for {}", new Object[]{clazz.getSimpleName(), this.uri});
            IMixinPlatformAgent agent = (IMixinPlatformAgent)ctor.newInstance(manager, uri);
            this.agents.add(agent);
         } catch (Exception var8) {
            this.logger.catching(var8);
         }
      }

   }

   public URI getURI() {
      return this.uri;
   }

   public Collection getPhaseProviders() {
      List phaseProviders = new ArrayList();
      Iterator var2 = this.agents.iterator();

      while(var2.hasNext()) {
         IMixinPlatformAgent agent = (IMixinPlatformAgent)var2.next();
         String phaseProvider = agent.getPhaseProvider();
         if (phaseProvider != null) {
            phaseProviders.add(phaseProvider);
         }
      }

      return phaseProviders;
   }

   public void prepare() {
      Iterator var1 = this.agents.iterator();

      while(var1.hasNext()) {
         IMixinPlatformAgent agent = (IMixinPlatformAgent)var1.next();
         this.logger.debug("Processing prepare() for {}", new Object[]{agent});
         agent.prepare();
      }

   }

   public void initPrimaryContainer() {
      Iterator var1 = this.agents.iterator();

      while(var1.hasNext()) {
         IMixinPlatformAgent agent = (IMixinPlatformAgent)var1.next();
         this.logger.debug("Processing launch tasks for {}", new Object[]{agent});
         agent.initPrimaryContainer();
      }

   }

   public void inject() {
      Iterator var1 = this.agents.iterator();

      while(var1.hasNext()) {
         IMixinPlatformAgent agent = (IMixinPlatformAgent)var1.next();
         this.logger.debug("Processing inject() for {}", new Object[]{agent});
         agent.inject();
      }

   }

   public String getLaunchTarget() {
      Iterator var1 = this.agents.iterator();

      String launchTarget;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         IMixinPlatformAgent agent = (IMixinPlatformAgent)var1.next();
         launchTarget = agent.getLaunchTarget();
      } while(launchTarget == null);

      return launchTarget;
   }

   static {
      GlobalProperties.put("mixin.agents", agentClasses);
      Iterator var0 = MixinService.getService().getPlatformAgents().iterator();

      while(var0.hasNext()) {
         String agent = (String)var0.next();
         agentClasses.add(agent);
      }

      agentClasses.add("org.spongepowered.asm.launch.platform.MixinPlatformAgentDefault");
   }
}
