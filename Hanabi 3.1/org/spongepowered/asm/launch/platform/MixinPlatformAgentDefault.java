package org.spongepowered.asm.launch.platform;

import java.net.URI;

public class MixinPlatformAgentDefault extends MixinPlatformAgentAbstract {
   public MixinPlatformAgentDefault(MixinPlatformManager manager, URI uri) {
      super(manager, uri);
   }

   public void prepare() {
      String compatibilityLevel = this.attributes.get("MixinCompatibilityLevel");
      if (compatibilityLevel != null) {
         this.manager.setCompatibilityLevel(compatibilityLevel);
      }

      String mixinConfigs = this.attributes.get("MixinConfigs");
      int var5;
      if (mixinConfigs != null) {
         String[] var3 = mixinConfigs.split(",");
         int var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            String config = var3[var5];
            this.manager.addConfig(config.trim());
         }
      }

      String tokenProviders = this.attributes.get("MixinTokenProviders");
      if (tokenProviders != null) {
         String[] var9 = tokenProviders.split(",");
         var5 = var9.length;

         for(int var10 = 0; var10 < var5; ++var10) {
            String provider = var9[var10];
            this.manager.addTokenProvider(provider.trim());
         }
      }

   }

   public void initPrimaryContainer() {
   }

   public void inject() {
   }

   public String getLaunchTarget() {
      return this.attributes.get("Main-Class");
   }
}
