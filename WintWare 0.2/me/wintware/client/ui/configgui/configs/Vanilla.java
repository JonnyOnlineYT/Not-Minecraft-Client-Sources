package me.wintware.client.ui.configgui.configs;

import java.util.Iterator;
import me.wintware.client.Main;
import me.wintware.client.module.Module;
import me.wintware.client.module.combat.Criticals;
import me.wintware.client.module.combat.KillAura;
import me.wintware.client.module.combat.Velocity;
import me.wintware.client.ui.configgui.Config;

public class Vanilla extends Config {
   public Vanilla() {
      super("Vanilla");
   }

   public void loadConfig() {
      try {
         Iterator var1 = Main.instance.moduleManager.modules.iterator();

         while(var1.hasNext()) {
            Module mod = (Module)var1.next();
            if (mod instanceof KillAura) {
               KillAura.yawSpeed.setValDouble(80.0D);
               KillAura.randomize.setValDouble(0.1D);
               KillAura.range.setValDouble(6.0D);
               KillAura.fov.setValDouble(360.0D);
               KillAura.onlyCrit.setValue(true);
            }

            if (mod instanceof Criticals) {
               Main.instance.moduleManager.getModuleByClass(Criticals.class).setToggled(true);
            }

            if (mod instanceof Velocity) {
               Main.instance.moduleManager.getModuleByClass(Velocity.class).setToggled(true);
            }
         }
      } catch (Exception var3) {
      }

   }
}
