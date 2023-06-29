package ru.rockstar.api.command.impl;

import java.util.Iterator;

import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;
import ru.rockstar.client.features.Feature;

public class PanicCommand extends CommandAbstract {
   public PanicCommand() {
      super("panic", "Disabled all modules", ".panic", "panic");
   }

   public void execute(String... args) {
      if (args[0].equalsIgnoreCase("panic")) {
         Iterator var2 = Main.instance.featureDirector.getFeatureList().iterator();

         
         
         while(var2.hasNext()) {
            Feature feature = (Feature)var2.next();
            if (feature.isToggled()) {
               feature.toggle();
            }
         }
      }

   }
}
