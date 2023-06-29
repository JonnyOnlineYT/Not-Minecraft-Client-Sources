package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.potion.Potion;

public class AntiLevitation extends Module {
   public AntiLevitation() {
      super("AntiLevitation", "", Category.Player);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.player.isPotionActive(Potion.getPotionById(25))) {
         mc.player.removeActivePotionEffect(Potion.getPotionById(25));
      }

   }
}
