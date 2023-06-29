package me.wintware.client.ui.configgui.configs;

import java.util.Iterator;
import me.wintware.client.Main;
import me.wintware.client.module.Module;
import me.wintware.client.module.combat.Criticals;
import me.wintware.client.module.combat.KillAura;
import me.wintware.client.module.combat.TargetStrafe;
import me.wintware.client.module.combat.Velocity;
import me.wintware.client.ui.configgui.Config;

public class WellMore extends Config {
   public WellMore() {
      super("WellMore");
   }

   public void loadConfig() {
      try {
         Iterator var1 = Main.instance.moduleManager.modules.iterator();

         while(var1.hasNext()) {
            Module mod = (Module)var1.next();
            if (mod instanceof KillAura) {
               KillAura.yawSpeed.setValDouble(80.0D);
               KillAura.pitchSpeed.setValDouble(80.0D);
               KillAura.randomize.setValDouble(8.0D);
               KillAura.range.setValDouble(4.0D);
               KillAura.fov.setValDouble(360.0D);
               KillAura.onlyCrit.setValue(true);
            }

            if (mod instanceof Criticals) {
               Main.instance.moduleManager.getModuleByClass(Criticals.class).setToggled(false);
            }

            if (mod instanceof TargetStrafe) {
               TargetStrafe.speed.setValDouble(0.2D);
               TargetStrafe.range.setValDouble(3.2D);
            }

            if (mod instanceof Velocity) {
               Main.instance.moduleManager.getModuleByClass(Velocity.class).setToggled(true);
            }
         }
      } catch (Exception var3) {
      }

   }
}
