package me.wintware.client.module.world;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class FastPlace extends Module {
   public FastPlace() {
      super("FastPlace", Category.World);
   }

   public void onDisable() {
      super.onDisable();
      mc.rightClickDelayTimer = 6;
   }

   @EventTarget
   public void update(EventUpdate e) {
      mc.rightClickDelayTimer = 0;
   }
}
