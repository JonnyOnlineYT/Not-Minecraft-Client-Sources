package me.wintware.client.module.visual;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class AntiRain extends Module {
   public AntiRain() {
      super("AntiRain", Category.World);
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      if (mc.world.isRaining()) {
         mc.world.setRainStrength(0.0F);
         mc.world.setThunderStrength(0.0F);
      }

   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }
}
