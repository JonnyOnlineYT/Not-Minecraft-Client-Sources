package org.spongepowered.tools.obfuscation.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import javax.tools.Diagnostic.Kind;
import org.spongepowered.tools.obfuscation.ObfuscationType;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

public final class ObfuscationServices {
   private static ObfuscationServices instance;
   private final ServiceLoader serviceLoader = ServiceLoader.load(IObfuscationService.class, this.getClass().getClassLoader());
   private final Set services = new HashSet();

   private ObfuscationServices() {
   }

   public static ObfuscationServices getInstance() {
      if (instance == null) {
         instance = new ObfuscationServices();
      }

      return instance;
   }

   public void initProviders(IMixinAnnotationProcessor ap) {
      try {
         Iterator var2 = this.serviceLoader.iterator();

         while(true) {
            String serviceName;
            Collection obfTypes;
            do {
               IObfuscationService service;
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  service = (IObfuscationService)var2.next();
               } while(this.services.contains(service));

               this.services.add(service);
               serviceName = service.getClass().getSimpleName();
               obfTypes = service.getObfuscationTypes();
            } while(obfTypes == null);

            Iterator var6 = obfTypes.iterator();

            while(var6.hasNext()) {
               ObfuscationTypeDescriptor obfType = (ObfuscationTypeDescriptor)var6.next();

               try {
                  ObfuscationType type = ObfuscationType.create(obfType, ap);
                  ap.printMessage(Kind.NOTE, serviceName + " supports type: \"" + type + "\"");
               } catch (Exception var9) {
                  var9.printStackTrace();
               }
            }
         }
      } catch (ServiceConfigurationError var10) {
         ap.printMessage(Kind.ERROR, var10.getClass().getSimpleName() + ": " + var10.getMessage());
         var10.printStackTrace();
      }
   }

   public Set getSupportedOptions() {
      Set supportedOptions = new HashSet();
      Iterator var2 = this.serviceLoader.iterator();

      while(var2.hasNext()) {
         IObfuscationService provider = (IObfuscationService)var2.next();
         Set options = provider.getSupportedOptions();
         if (options != null) {
            supportedOptions.addAll(options);
         }
      }

      return supportedOptions;
   }

   public IObfuscationService getService(Class serviceClass) {
      Iterator var2 = this.serviceLoader.iterator();

      IObfuscationService service;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         service = (IObfuscationService)var2.next();
      } while(!serviceClass.getName().equals(service.getClass().getName()));

      return service;
   }
}
